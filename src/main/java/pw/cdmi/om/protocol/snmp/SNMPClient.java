package pw.cdmi.om.protocol.snmp;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.ScopedPDU;
import org.snmp4j.Snmp;
import org.snmp4j.Target;
import org.snmp4j.TransportMapping;
import org.snmp4j.UserTarget;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.event.ResponseListener;
import org.snmp4j.mp.MPv3;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.security.AuthMD5;
import org.snmp4j.security.PrivDES;
import org.snmp4j.security.SecurityLevel;
import org.snmp4j.security.SecurityModels;
import org.snmp4j.security.SecurityProtocols;
import org.snmp4j.security.USM;
import org.snmp4j.security.UsmUser;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.Variable;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.util.DefaultPDUFactory;
import org.snmp4j.util.TableEvent;
import org.snmp4j.util.TableUtils;

import pw.cdmi.om.protocol.snmp.mib.MibOID;

/************************************************************
 * TODO(对类的简要描述说明 – 必须).
 * TODO(对类的作用含义说明 – 可选).
 * TODO(对类的使用方法说明 – 可选).
 * 
 * @author WUWEI
 * @version iSoc Service Platform, 2015年3月12日
 ************************************************************/
public class SNMPClient {

	private final static Logger logger = LoggerFactory.getLogger(SNMPClient.class);

	private final int RETRIES = 3;	// 重试次数

	private final int TIMEOUT = 1000;	// 超时时间

	private Snmp snmp = null;

	private PDU pdu = null;

	private Target target = null;

	private int version = SnmpConstants.version2c;
	public SNMPClient(SNMPTarget target) throws IOException{
		init(target, target.getReadCommunity());
	}
	private void init(SNMPTarget target, String community) throws IOException {
		this.version = target.getSnmpVersion();
		TransportMapping transport = new DefaultUdpTransportMapping();
		transport = new DefaultUdpTransportMapping();
		snmp = new Snmp(transport);
		if (SnmpConstants.version3 == this.version) {
			// 设置安全模式
			USM usm = new USM(SecurityProtocols.getInstance(), new OctetString(MPv3.createLocalEngineID()), 0);
			SecurityModels.getInstance().addSecurityModel(usm);
			// 构造报文
			pdu = new ScopedPDU();
			// FIXME 测试时请尝试去除下面的语句。
			((ScopedPDU) pdu).setContextEngineID(new OctetString(target.getContextEngineId()));
		} else {
			// 构造报文
			pdu = new PDU();
		} 

		// 生成目标地址对象,示例 udp:127.0.0.1/161
		// Address targetAddress = GenericAddress.parse("udp:" + target.getTargetIp() + "/" + target.getPort());
		Address targetAddress = new UdpAddress(target.getTargetIp() + "/" + target.getPort());
		if (version == SnmpConstants.version3) {
			UsmUser user = new UsmUser(new OctetString(target.getUser()), AuthMD5.ID, new OctetString(
				target.getPassword()), PrivDES.ID, new OctetString(target.getPassword()));
			// 添加用户
			snmp.getUSM().addUser(user.getSecurityName(),// FIXME 检查另一种写法user.getSecurityName()
				user);
			this.target = new UserTarget();
			int securityLevel=0;
			if(community.equals("authPriv")){
				securityLevel=SecurityLevel.AUTH_PRIV;
			}else if(community.equals("authNoPriv")){
				securityLevel=SecurityLevel.AUTH_NOPRIV;
			}else if(community.equals("noAuthNoPriv")){
				securityLevel=SecurityLevel.NOAUTH_NOPRIV;
			}
			// 设置安全级别
			((UserTarget) this.target).setSecurityLevel(securityLevel);
			((UserTarget) this.target).setSecurityName(user.getSecurityName());
			this.target.setVersion(SnmpConstants.version3);
		} else {
			this.target = new CommunityTarget();

			((CommunityTarget) this.target).setCommunity(new OctetString(community));
			if (version == SnmpConstants.version1) {
				this.target.setVersion(SnmpConstants.version1);
			} else {
				this.target.setVersion(SnmpConstants.version2c);
			}
		}
		// 目标对象相关设置
		this.target.setAddress(targetAddress);
		this.target.setRetries(RETRIES);
		this.target.setTimeout(TIMEOUT);
		// 开始监听消息
		transport.listen();
	}

	public void close() {
		
		this.target = null;
		this.pdu = null;
		try {
			this.snmp.close();
			this.snmp = null;
		} catch (IOException e) {
			logger.error("snmp连接关闭失败："+e.getMessage());
		}
	}
	
	public boolean testConnected() {
		try {
			this.pdu.setType(PDU.GET);
			this.pdu.add(new VariableBinding(new OID("1.3.6.1.2.1.1.5.0")));
			ResponseEvent responseEvent=this.snmp.send(this.pdu, this.target);
			PDU response = responseEvent.getResponse();
			
			if(response!=null){
				VariableBinding vb = (VariableBinding) response.getVariableBindings().firstElement();
				if(vb.getVariable().getClass().getName().contains("Counter32")){
					
					return false;
				}
				return true;
			}else{
				return false;
			}
		} catch (IOException ex) {
			return false;
		}
	}

	public String snmpGet(MibOID mo) throws IOException {
		this.pdu.clear();
		this.pdu.setType(PDU.GET);

		// 设置要获取的对象ID，这个OID代表远程计算机的名称
//		if ((mo.getOID()).endsWith(".0")) {
			this.pdu.add(new VariableBinding(new OID(mo.getOID())));
//		} else {
//			throw new RuntimeException(mo.getOID() + " 为非基本节点");
//		}
		ResponseEvent respEvent = snmp.send(this.pdu, this.target);

		PDU response = respEvent.getResponse();

		if (response !=null && response.getErrorStatus() == PDU.noError) {
			VariableBinding vb = (VariableBinding) response.getVariableBindings().firstElement();
			if("Null".equals(vb.getVariable().toString())){
				logger.warn("通过SNMP读取" + this.target.getAddress().toString() + "上的OID[" + mo.getOID() + "]为空或失败");
				throw new IOException("获取的数据是空值 ,可能是监控对象类型不符导致");
			}
			return vb.getVariable().toString();
		} else {
			throw new IOException("获取数据时失败,可能是由于无访问权限" + ((response !=null) ? ",错误信息:" + response.getErrorStatusText() : ""));
		}
	}

	public String snmpGetNext(MibOID mo) throws IOException {
		this.pdu.clear();
		this.pdu.setType(PDU.GETNEXT);

		this.pdu.add(new VariableBinding(new OID(mo.getOID())));

		ResponseEvent respEvent = snmp.send(this.pdu, this.target);

		PDU response = respEvent.getResponse();

		if (response.getErrorStatus() == PDU.noError) {
			Map<String, String> result = new HashMap<String, String>();
			Vector<? extends VariableBinding> vbs = response.getVariableBindings();
			for (VariableBinding vb : vbs) {
				result.put(vb.getOid().toString(), vb.getVariable().toString());
				System.out.println(vb.getOid().toString() + ":" + vb.getVariable().toString());
			}
			return result.toString();
		} else {
			throw new RuntimeException("错误信息:" + response.getErrorStatusText());
		}
	}

	public Map<String, String> snmpGetField(String oid) throws IOException {
		this.pdu.setType(PDU.GET);

		this.pdu.add(new VariableBinding(new OID(oid)));

		ResponseEvent respEvent = snmp.send(this.pdu, this.target);

		PDU response = respEvent.getResponse();

		if (response == null) {
			System.out.println("TimeOut...");
			throw new RuntimeException("错误信息: TimeOut...");
		} else {
			if (response.getErrorStatus() == PDU.noError) {
				// 读取数据
				Vector<? extends VariableBinding> vbs = response.getVariableBindings();
				Map<String, String> result = new HashMap<String, String>();

				for (VariableBinding vb : vbs) {
					result.put(vb.getOid().toString(), vb.getVariable().toString());
					System.out.println(vb.getOid().toString() + ":" + vb.getVariable().toString());
				}
				return result;
			} else {
				throw new RuntimeException("错误信息:" + response.getErrorStatusText());
			}
		}
	}

	public Map<String, String> snmpGetMultiField(SNMPTarget target, HashSet<String> oids) throws IOException {
		// 初始化SNMP连接信息
		init(target, target.getReadCommunity());
		this.pdu.setType(PDU.GETNEXT);

		Iterator<String> iterator = oids.iterator();
		while (iterator.hasNext()) {
			this.pdu.add(new VariableBinding(new OID(iterator.next())));
		}

		ResponseEvent respEvent = snmp.send(this.pdu, this.target);

		PDU response = respEvent.getResponse();

		if (response == null) {
			throw new RuntimeException("错误信息: TimeOut...");
		} else {
			if (response.getErrorStatus() == PDU.noError) {
				// 读取数据
				Vector<? extends VariableBinding> vbs = response.getVariableBindings();
				Map<String, String> result = new HashMap<String, String>();

				for (VariableBinding vb : vbs) {
					result.put(vb.getOid().toString(), vb.getVariable().toString());
					System.out.println(vb.getOid().toString() + ":" + vb.getVariable().toString());
				}
				return result;
			} else {
				throw new RuntimeException("错误信息:" + response.getErrorStatusText());
			}
		}
	}

	public Map<String, String> snmpGetGroup(SNMPTarget target, MibOID mib) throws IOException {
		// 初始化SNMP连接信息
		init(target, target.getReadCommunity());
		// 使用GETBULK方式获取数据,无论什么方式取oid,都是取叶子节点，叶子节点的父节点都不会取到
		this.pdu.setType(PDU.GETBULK);
		this.pdu.setMaxRepetitions(mib.size()); // 每个OID通过GETBULK方式获取多少个数据
		/*偏移量，假设有两个oid,0代表两个oid都取3000个叶子oid,1代表第一个取它最近的第一个oid,第二个取3000个oid,
		 * 大于1的数代表两个oid都是取他们最近的第一个oid
		 */
		this.pdu.setNonRepeaters(0);
		// 设置要获取的对象ID，这个OID代表远程计算机的名称
		this.pdu.add(new VariableBinding(new OID(mib.getOID())));

		ResponseEvent respEvent = snmp.send(this.pdu, this.target);
		PDU response = respEvent.getResponse();

		if (response == null) {
			System.out.println("TimeOut...");
			throw new RuntimeException("错误信息: TimeOut...");
		} else {
			if (response.getErrorStatus() == PDU.noError) {
				// 读取数据
				Vector<? extends VariableBinding> vbs = response.getVariableBindings();
				Map<String, String> result = new HashMap<String, String>();

				for (VariableBinding vb : vbs) {
					result.put(vb.getOid().toString(), vb.getVariable().toString());
					System.out.println(vb.getOid().toString() + ":" + vb.getVariable().toString());
				}
				return result;
			} else {
				throw new RuntimeException("错误信息:" + response.getErrorStatusText());
			}
		}

	}

	public Map<String, String> snmpWalkGroup( MibOID mib) throws IOException {
		return snmpWalkGroup(mib, null, null);
	}

	public Map<String, String> snmpWalkGroup(MibOID mib, int lowerIndex) throws IOException {
		lowerIndex = (lowerIndex < 0) ? 0 : lowerIndex;
		OID lower = new OID(String.valueOf(lowerIndex));
		return snmpWalkGroup( new OID[] { new OID(mib.getOID()) }, lower, null);
	}

	public Map<String, String> snmpWalkGroup(MibOID mib, int lowerIndex, int upperIndex)
		throws IOException {
		lowerIndex = (lowerIndex < 0) ? 0 : lowerIndex;
		upperIndex = (upperIndex < 0) ? 0 : upperIndex;
		if (upperIndex < lowerIndex) {
			throw new RuntimeException("参数upperIndex 应该大于lowerIndex");
		}
		OID lower = new OID(String.valueOf(lowerIndex));
		OID upper = new OID(String.valueOf(upperIndex));
		return snmpWalkGroup( new OID[] { new OID(mib.getOID()) }, lower, upper);
	}

	public Map<String, String> snmpWalkGroup( MibOID mib, String lowerOID) throws IOException {
		OID lower = (lowerOID == null) ? null : new OID(String.valueOf(lowerOID));
		return snmpWalkGroup(new OID[] { new OID(mib.getOID()) }, lower, null);
	}

	public Map<String, String> snmpWalkGroup(MibOID mib, String lowerOID, String upperOID)
		throws IOException {
		OID lower = (lowerOID == null) ? null : new OID(String.valueOf(lowerOID));
		OID upper = (upperOID == null) ? null : new OID(String.valueOf(upperOID));
		return snmpWalkGroup(new OID[] { new OID(mib.getOID()) }, lower, upper);
	}

	public Map<String, String> snmpWalkGroup(OID[] parentOid, OID lowerOid, OID upperOid)
		throws IOException {
		this.pdu.clear();
		// 使用GETBULK方式获取数据,无论什么方式取oid,都是取叶子节点，叶子节点的父节点都不会取到
		this.pdu.setType(PDU.GETBULK);

		TableUtils utils = new TableUtils(snmp, new DefaultPDUFactory(PDU.GETBULK));// GETNEXT or GETBULK
		// FIXME 不清楚setMaxNumRowsPerPDU的作用？
		utils.setMaxNumRowsPerPDU(1); // only for GETBULK, set max-repetitions, default is 10,必须大约零

		// If not null, all returned rows have an index in a range (lowerBoundIndex, upperBoundIndex]
		// lowerBoundIndex,upperBoundIndex都为null时返回所有的叶子节点。 必须具体到某个OID,,否则返回的结果不会在(lowerBoundIndex, upperBoundIndex)之间

		List<TableEvent> list = utils.getTable(this.target, parentOid, lowerOid, upperOid); //
		if(list == null || list.size() < 1){
			throw new IOException("获取数据时失败,可能是由于无访问权限");
		}
		Map<String, String> result = new LinkedHashMap<String, String>();
		for (TableEvent e : list) {
			VariableBinding[] vbs = e.getColumns();
			for (VariableBinding vb : vbs) {
				if("Null".equals(vb.getVariable().toString())){
					logger.warn("通过SNMP读取" + this.target.getAddress().toString() + "上的OID[" + vb.getOid().toString() + "]为空或失败");
					throw new IOException("获取的数据是空值 ,可能是监控对象类型不符导致");
				}
				result.put(vb.getOid().toString(), vb.getVariable().toString());
			}
		}
		return result;
	}

	public void GetBulk(SNMPTarget target, Map<String, Map<String, String>> result, Collection<String> oids,
		String[] rootOIDs) throws IOException {
		// 初始化SNMP连接信息
		init(target, target.getReadCommunity());
		this.pdu.setType(PDU.GETBULK);

		this.pdu.setMaxRepetitions(10);// 每次返回條數
		this.pdu.setNonRepeaters(0);// 標量個數
		for (String oid : oids) {// 裝配oid請求對象
			this.pdu.add(new VariableBinding(new OID(oid)));// 添加oid綁定
		}

		Map<String, String> curr_oids = new HashMap<String, String>();// the next oid
		for (String rootOID : rootOIDs) {// 添加數據收集器
			if (result.keySet().size() != rootOIDs.length) {
				result.put(rootOID, new HashMap<String, String>());
			}
			curr_oids.put(rootOID, rootOID);
		}

		ResponseEvent rspEvt = snmp.send(this.pdu, this.target);
		PDU response = rspEvt.getResponse();
		if (null != response && response.getErrorIndex() == PDU.noError && response.getErrorStatus() == PDU.noError) {

			// 循環數據桶
			Vector<VariableBinding> vector = response.getVariableBindings();
			for (VariableBinding variable : vector) {
				String key = variable.getOid().toString();
				boolean isEnd = true;// 結束狀態 標誌
				for (String rootOID : rootOIDs) {
					if (key.contains(rootOID)) {// 判断获得的值是否是指定根节点下面
						String value = variable.getVariable().toString();
						result.get(rootOID).put(key.replace(rootOID, ""), value);
						// System.out.println(key+"="+value);
						curr_oids.put(rootOID, key);
						isEnd = false;
					}
				}
				if (isEnd) {
					return;
				}
			}
			if (curr_oids.isEmpty()) {
				return;
			}
			GetBulk(target, result, curr_oids.values(), rootOIDs);
		}
	}

	public void snmpGetBulk(SNMPTarget target, Map<String, String> result, OID oid, String rootOID) throws IOException {
		// 初始化SNMP连接信息
//		init(target, target.getReadCommunity());
		this.pdu.clear();
		this.pdu.setType(PDU.GETBULK);

		this.pdu.setMaxRepetitions(10);// 每次返回條數
		this.pdu.setNonRepeaters(0);// 標量個數
		this.pdu.add(new VariableBinding(new OID(oid)));// 添加oid綁定

		ResponseEvent rspEvt = snmp.send(this.pdu, this.target);
		PDU response = rspEvt.getResponse();
		if (null != response && response.getErrorIndex() == PDU.noError && response.getErrorStatus() == PDU.noError) {
			OID curr_oid = null;
			// 循環數據桶
			Vector<VariableBinding> vector = response.getVariableBindings();
			for (VariableBinding variable : vector) {
				String syn = variable.getVariable().getSyntaxString();// 節點數據類型

				String key = variable.getOid().toString();
				if (key.contains(rootOID)) {// 判断获得的值是否是指定根节点下面
					String value = variable.getVariable().toString();
					result.put(key.replace(rootOID, ""), value);
					// System.out.println(key+"="+value);
					curr_oid = variable.getOid();
				}
			}
			if (curr_oid == null) {
				return;
			}
			snmpGetBulk(target, result, curr_oid, rootOID);
		}
	}

	public void snmpWalk(SNMPTarget target, List<String> indexs, OID oid, String rootOID) throws IOException {
		// 初始化SNMP连接信息
		init(target, target.getReadCommunity());
		this.pdu.setType(PDU.GETBULK);

		this.pdu.setMaxRepetitions(10);// 每次返回條數

		this.pdu.add(new VariableBinding(new OID(oid)));// 添加oid綁定

		ResponseEvent rspEvt = snmp.send(this.pdu, this.target);
		PDU response = rspEvt.getResponse();
		if (null != response && response.getErrorIndex() == PDU.noError && response.getErrorStatus() == PDU.noError) {
			OID curr_oid = null;
			// 循环读取数据桶
			Vector<VariableBinding> vector = response.getVariableBindings();
			for (VariableBinding variable : vector) {
				String curr_str = variable.getOid().toString();
				if (curr_str.contains(rootOID)) {// 判断获得的值是否是指定根节点下面
					String index = SNMPClient.ascllToMac(curr_str.replace(rootOID, ""));
					indexs.add(index);
					curr_oid = variable.getOid();
				} else {
					return;
				}
			}
			if (curr_oid == null) {
				return;
			}
			snmpWalk(target, indexs, curr_oid, rootOID);
		}
	}

	public void Walk(SNMPTarget target, Map<String, String> result, OID oid) throws IOException {
		// 初始化SNMP连接信息
		init(target, target.getReadCommunity());
		this.pdu.setType(PDU.GETBULK);

		this.pdu.add(new VariableBinding(oid));
		ResponseEvent rspEvt = snmp.send(this.pdu, this.target);
		PDU response = rspEvt.getResponse();
		if (response.getErrorIndex() == PDU.noError && response.getErrorStatus() == PDU.noError) {
			VariableBinding vb = (VariableBinding) response.getVariableBindings().firstElement();
			OID curr_oid = vb.getOid();
			String curr_str = curr_oid.toString();
			if (curr_str.contains("WALK")) {// 判断获得的值是否是指定根节点下面
				String key = vb.getOid().toString();
				result.put(key.replace("WALK", ""), vb.getVariable().toString());
				Walk(target, result, curr_oid);
			}
		} else {
			throw new RuntimeException("错误信息:" + response.getErrorStatusText());
		}
	}

	public void Set(SNMPTarget target, OID oid, Variable newVar) throws IOException {
		// 初始化SNMP连接信息
		init(target, target.getWriteCommunity());
		this.pdu.setType(PDU.SET);

		this.pdu.add(new VariableBinding(oid, newVar));
		ResponseEvent rspEvt = snmp.send(this.pdu, this.target);
		PDU response = rspEvt.getResponse();
		if (response.getErrorIndex() == PDU.noError && response.getErrorStatus() == PDU.noError) {
			VariableBinding vb = (VariableBinding) response.getVariableBindings().firstElement();
			Variable var = vb.getVariable();
			if (var.equals(newVar)) {// 比较返回值和设置值
				System.out.println("SET操作成功 ！");
			} else {
				System.out.println("SET操作失败 ！");
			}
		} else {
			throw new RuntimeException("错误信息:" + response.getErrorStatusText());
		}
	}

	public static String ascllToMac(String oid) {
		System.out.println("oid:" + oid);
		return oid;
	}

	public void sendMessage(boolean synchronous, final Boolean bro, PDU pdu, String ipAddress) throws IOException {

		if (synchronous) {
			// 发送报文 并且接受响应
			ResponseEvent event = snmp.send(pdu, target);

			// 处理响应
			PDU response = event.getResponse();
			if (response.getErrorIndex() == PDU.noError && response.getErrorStatus() == PDU.noError) {
				VariableBinding variable = (VariableBinding) response.getVariableBindings().firstElement();
			} else {
				throw new RuntimeException("错误信息:" + response.getErrorStatusText());
			}
			int size = response.size();
			System.out.println(size);
			for (int n = 0; n < size; n++) {
				System.out.println("Synchronize(同步) message(消息) from(来自) " + event.getPeerAddress() + "\r\n"
						+ "request(发送的请求):" + event.getRequest() + "\r\n" + "response(返回的响应):" + response.get(n));
			}
			/**
			 * 输出结果：
			 * Synchronize(同步) message(消息) from(来自) 192.168.1.233/161
			 * request(发送的请求):GET[requestID=632977521, errorStatus=Success(0), errorIndex=0, VBS[1.3.6.1.2.1.1.5.0 = Null]]
			 * response(返回的响应):RESPONSE[requestID=632977521, errorStatus=Success(0), errorIndex=0, VBS[1.3.6.1.2.1.1.5.0 = WIN-667H6TS3U37]]
			 * 
			 * */
		} else {
			// 设置监听对象
			ResponseListener listener = new ResponseListener() {
				@Override
				public void onResponse(ResponseEvent event) {
					if (bro.equals(false)) {
						((Snmp) event.getSource()).cancel(event.getRequest(), this);
					}
					// 处理响应
					PDU request = event.getRequest();
					PDU response = event.getResponse();
					int size = response.size();
					System.out.println(size);
					for (int n = 0; n < size; n++) {
						System.out.println("Asynchronise(异步) message(消息) from(来自) " + event.getPeerAddress() + "\r\n"
								+ "request(发送的请求):" + request.get(n) + "\r\n" + "response(返回的响应):" + response.get(n));
					}
				}
			};
			// 发送报文
			snmp.send(pdu, target, null, listener);
		}
	}

	public MibOID getMibObject(SNMPTarget target, MibOID mib) throws IOException {
		// // 构造报文
		// PDU pdu = new PDU();
		// // PDU pdu = new ScopedPDU();
		// // 设置要获取的对象ID，这个OID代表远程计算机的名称
		// OID oids = new OID(mib.getMappingOID());
		// pdu.add(new VariableBinding(oids));
		// // 设置报文类型
		// pdu.setType(PDU.GETBULK);
		// // ((ScopedPDU) pdu).setContextName(new OctetString("priv"));
		// try {
		// // 发送消息 其中最后一个是想要发送的目标地址
		// // manager.sendMessage(false, true, pdu, "udp:192.168.1.229/161");//192.168.1.229 Linux服务器
		// this.sendMessage(true, true, pdu, "udp:127.0.0.1/161");// 192.168.1.233 WinServer2008服务器
		// System.out.println("excute snmp");
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		// return null;
		Map<String, String> result = new HashMap<String, String>();
		result = snmpGetGroup(target, mib);
		return null;
	}

	// public String getMibField(SNMPTarget target, OMMappingInfo mib, int index) throws IOException {
	// OMMappingInfo om = mib.getIndex(index);
	// switch(om.getMode()){
	// case PDU.GET:
	// return snmpGet(target, om);
	// case PDU.GETNEXT:
	// return snmpGetNext(target, om);
	// default:
	// return null;
	// }
	// }

	public String getMibField(MibOID mib) throws IOException {
		switch (mib.getMode()) {
		case PDU.GET:
			return snmpGet(mib);
		case PDU.GETNEXT:
			return snmpGetNext(mib);
		case PDU.GETBULK:
			return snmpWalkGroup(mib).toString();
		default:
			return null;
		}
	}

	public Map<String, Object> getMibField(MibOID[] columns) throws IOException {
		Map<String, Object> result = new HashMap<String, Object>();
		for (MibOID column : columns) {
			switch (column.getMode()) {
			case PDU.GET:
				result.put(column.getOID(), snmpGet(column));
				break;
			case PDU.GETNEXT:
				result.put(column.getOID(), snmpGetNext(column));
				break;
			case PDU.GETBULK:
				result.put(column.getOID(), snmpWalkGroup(column));
				break;
			default:
				break;
			}
		}
		return result;
	}
}
