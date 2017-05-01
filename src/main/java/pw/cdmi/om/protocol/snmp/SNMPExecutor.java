package pw.cdmi.om.protocol.snmp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import pw.cdmi.om.protocol.snmp.mib.MibSystem;

/************************************************************
 * TODO(对类的简要描述说明 – 必须).
 * TODO(对类的作用含义说明 – 可选).
 * TODO(对类的使用方法说明 – 可选).
 * 
 * @author WUWEI
 * @version iSoc Service Platform, 2015年3月13日
 ************************************************************/
public class SNMPExecutor {

	public void excute() throws IOException{
		SNMPTarget target = new SNMPTarget(); // SNMP参数信息
		target.setTargetIp("127.0.0.1"); // 设备IP
		target.setReadCommunity("public"); // 读密码
		target.setWriteCommunity("public"); // 写密码
		target.setPort(161); // 端口号
		target.setSnmpVersion(SnmpConstants.version1); // SNMP版本 1:V2C 0:V1

		SNMPClient client = new SNMPClient(target);
		// MibSystem newMibObj = client.getMibField(target,mib, 1);

	}

	public static void main(String[] args) throws IOException {
		// SNMPTarget target = new SNMPTarget(); // SNMP参数信息
		// target.setTargetIp("127.0.0.1"); // 设备IP
		// target.setReadCommunity("public"); // 读密码
		// target.setWriteCommunity("public"); // 写密码
		// target.setPort(161); // 端口号
		// target.setSnmpVersion(SnmpConstants.version2c); // SNMP版本 1:V2C 0:V1
		//
		// SNMPClient<MibSystem> client = new SNMPClient<MibSystem>();
		// MibSystem mib = new MibSystem();
		// Map<String, String> result = client.snmpGet(target, mib);
		// Iterator<String> iterator = result.keySet().iterator();
		// while (iterator.hasNext()) {
		// String key = iterator.next();
		// System.out.print(key);
		// System.out.print(" : ");
		// System.out.println(result.get(key));
		// }
		// System.out.println("OK");
//		testSnmpField();
		testSnmpObject();
		// syncGetBulk();
	}

	public static void testSnmpField() throws IOException {
		SNMPTarget target = new SNMPTarget(); // SNMP参数信息
		target.setTargetIp("127.0.0.1"); // 设备IP
		target.setReadCommunity("public"); // 读密码
		target.setWriteCommunity("public"); // 写密码
		target.setPort(161); // 端口号
		target.setSnmpVersion(SnmpConstants.version2c); // SNMP版本 1:V2C 0:V1

		SNMPClient client = new SNMPClient(target);
		String result = client.getMibField(MibSystem.sysName);
		System.out.println(result);
	}

	public static void testSnmpObject() throws IOException {
		SNMPTarget target = new SNMPTarget(); // SNMP参数信息
		target.setTargetIp("192.168.1.30"); // 设备IP
		target.setReadCommunity("public"); // 读密码
		target.setWriteCommunity("public"); // 写密码
		target.setPort(161); // 端口号
		target.setSnmpVersion(SnmpConstants.version2c); // SNMP版本 1:V2C 0:V1

		SNMPClient client = new SNMPClient(target);
//		Map<String, String> result = client.snmpWalkGroup(target, MibSystem.sysName.getParent());

	}

	public static void syncGetBulk() {
		try {
			Snmp snmp = new Snmp(new DefaultUdpTransportMapping()); // 构造一个UDP
			snmp.listen(); // 开始监听snmp消息

			CommunityTarget target = new CommunityTarget();
			target.setCommunity(new OctetString("public"));// snmpv2的团体名
			target.setVersion(SnmpConstants.version2c);  // snmp版本
			target.setAddress(new UdpAddress("127.0.0.1/161"));
			target.setTimeout(60000); // 时延
			target.setRetries(1); // 重传

			PDU pdu = new PDU();  // a SNMP protocol data unit
			// 使用GETBULK方式获取数据,无论什么方式取oid,都是取叶子节点，叶子节点的父节点都不会取到
			pdu.setType(PDU.GETBULK);

			// snmp getBulk独有
			pdu.setMaxRepetitions(9); // 每个OID通过GETBULK方式获取多少个数据
			/*偏移量，假设有两个oid,0代表两个oid都取3000个叶子oid,1代表第一个取它最近的第一个oid,第二个取3000个oid,
			 * 大于1的数代表两个oid都是取他们最近的第一个oid
			 */
			pdu.setNonRepeaters(1);

			// 添加oid,可以多个
			pdu.add(new VariableBinding(new OID("1.3.6.1.2.1.1")));
			ResponseEvent responseEvent = snmp.send(pdu, target);
			PDU response = responseEvent.getResponse();

			if (response == null) {
				System.out.println("TimeOut...");
			} else {
				if (response.getErrorStatus() == PDU.noError) {
					// 读取数据
					Vector<? extends VariableBinding> vbs = response.getVariableBindings();
					List<SnmpResult> result = new ArrayList<SnmpResult>(vbs.size());
					for (VariableBinding vb : vbs) {
						result.add(new SnmpResult(vb.getOid().toString(), vb.getVariable().toString()));
						System.out.println(vb.getOid().toString() + ":" + vb.getVariable().toString());
					}
					// System.out.println(JSONArray.fromObject(result).toString());
					System.out.println(vbs.size());
				} else {
					System.out.println("Error:" + response.getErrorStatusText());
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
