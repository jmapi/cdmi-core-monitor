package pw.cdmi.om.protocol.snmp;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

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
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.Null;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

public class SNMPUtil {
	private final String SNMP_V3 = "3";

	private final String SNMP_V2C = "2";

	private final String SNMP_V1 = "1";



	private final int RETRIES = 2;

	private final int TIMEOUT = 1000;

	private Snmp snmp = null;

	TransportMapping transport = null;

	private String version = null;

	private StringBuffer result = new StringBuffer();

	private Map<String, String> oidMap = new HashMap<String, String>();

	public SNMPUtil(String version) {
		this.version = version;
		try {
			transport = new DefaultUdpTransportMapping();
			snmp = new Snmp(transport);
			if (version.equals(SNMP_V3)) {
				USM usm = new USM(SecurityProtocols.getInstance(), new OctetString(MPv3.createLocalEngineID()), 0);
				SecurityModels.getInstance().addSecurityModel(usm);
			}
			transport.listen();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void colse() {
		if (snmp != null) {
			try {
				snmp.close();
			} catch (Exception e) {
				snmp = null;
			}
		}
		if (transport != null) {
			try {
				transport.close();
			} catch (Exception e) {
				transport = null;
			}
		}
	}


	public String snmpWalkByV1orV2c(String address, String oid,String community) {
		CommunityTarget target = createCommunityTarget(GenericAddress.parse(address),community);
		try {
			PDU pdu = new PDU();
			OID targetOID = new OID(oid);
			pdu.add(new VariableBinding(targetOID));
			boolean finished = false;
			boolean havaResult = false;
			result.append("{\"WorkstationStatus\":[");

			while (!finished) {
				VariableBinding vb = null;
				ResponseEvent respEvent = snmp.getNext(pdu, target);
				PDU response = respEvent.getResponse();
				if (null == response) {
					finished = true;
					break;
				} else {
					vb = response.get(0);
				}
				// check finish
				finished = checkWalkFinished(targetOID, pdu, vb);
				if (!finished) {
					String diskName=((vb.toValueString().split(":").length>2)?getChinese(vb.toValueString()):vb.toValueString());
					result.append("{\"" + vb.getOid().toString() + "\":\"" + (diskName==null?vb.toValueString():diskName) + "\"},");
					havaResult = true;

					// Set up the variable binding for the next entry.
					pdu.setRequestID(new Integer32(0));
					pdu.set(0, vb);
				} else {
					snmp.close();
				}
			}

			if (havaResult) {
				result.deleteCharAt(result.length() - 1);
				
			}
			result.append("]}");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result.toString();
	}

	public String snmpWalkByVersion3(String address, String oid, String username, String password,
		String contextEngineId) {
		UsmUser user = createUsmUser(username, password);
		UserTarget target = createUserTarget(GenericAddress.parse(address), user.getSecurityName());
		snmp.getUSM().addUser(user.getSecurityName(), user);

		boolean havaResult = false;
		result.append("{\"WorkstationStatus\":[");

		try {
			ScopedPDU pdu = new ScopedPDU();
			OID targetOID = new OID(oid);
			pdu.add(new VariableBinding(targetOID));
			boolean finished = false;
			while (!finished) {
				VariableBinding vb = null;
				ResponseEvent respEvent = snmp.getNext(pdu, target);
				PDU response = respEvent.getResponse();
				if (null == response) {
					finished = true;
					break;
				} else {
					vb = response.get(0);
				}
				// check finish
				finished = checkWalkFinished(targetOID, pdu, vb);
				if (!finished) {
					String diskName=((vb.toValueString().split(":").length>2)?getChinese(vb.toValueString()):vb.toValueString());
					result.append("{\"" + vb.getOid().toString() + "\":\"" + (diskName==null?vb.toValueString():diskName) + "\"},");
					havaResult = true;
					// Set up the variable binding for the next entry.
					pdu.setRequestID(new Integer32(0));
					pdu.set(0, vb);
				} else {
					snmp.close();
				}
			}
			if (havaResult) {
				result.deleteCharAt(result.length() - 1);
			}
			result.append("]}");


		} catch (Exception e) {
			e.printStackTrace();
		}
		return result.toString();
	}
	public static String getChinese(String octetString){
        try{
            String[] temps = octetString.split(":");
            byte[] bs = new byte[temps.length];
            for(int i=0;i<temps.length;i++)
                bs[i] = (byte)Integer.parseInt(temps[i],16);
        
            return new String(bs,"GB2312");
        }catch(Exception e){
            return null;
        }
    }
	private boolean checkWalkFinished(OID targetOID, PDU pdu, VariableBinding vb) {
		boolean finished = false;
		if (pdu.getErrorStatus() != 0) {
			finished = true;
		} else if (vb.getOid() == null) {
			finished = true;
		} else if (vb.getOid().size() < targetOID.size()) {
			finished = true;
		} else if (targetOID.leftMostCompare(targetOID.size(), vb.getOid()) != 0) {
			finished = true;
		} else if (Null.isExceptionSyntax(vb.getVariable().getSyntax())) {
			finished = true;
		} else if (vb.getOid().compareTo(targetOID) <= 0) {
			finished = true;
		}
		return finished;
	}

	private String send(Boolean synchronous, final Boolean bro, Snmp snmp, PDU pdu, Target target) throws IOException {
		if (synchronous.equals(true)) {
			ResponseEvent responseEvent = snmp.send(pdu, target);
			return getJsonFromResponse(responseEvent);
		} else {
			// 设置监听对象
			ResponseListener listener = new ResponseListener() {
				@Override
				public void onResponse(ResponseEvent responseEvent) {
					if (bro.equals(false)) {
						((Snmp) responseEvent.getSource()).cancel(responseEvent.getRequest(), this);
					}
					// 处理响应
					PDU request = responseEvent.getRequest();
					PDU response = responseEvent.getResponse();
					System.out.println("Asynchronise message from " + responseEvent.getPeerAddress() + "/nrequest:"
							+ request + "/nresponse:" + response);
					try {
						System.out.println(getJsonFromResponse(responseEvent).toString());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			};
			snmp.send(pdu, target, null, listener);
		}
		return null;
	}

	private UsmUser createUsmUser(String username, String password) {
		UsmUser user = new UsmUser(new OctetString(username), AuthMD5.ID, new OctetString(password), PrivDES.ID,
			new OctetString(password));
		return user;
	}

	private UserTarget createUserTarget(Address targetAddress, OctetString securityName) {
		UserTarget target = new UserTarget();
		target.setSecurityLevel(SecurityLevel.AUTH_PRIV);
		target.setSecurityName(securityName);
		target.setVersion(SnmpConstants.version3);
		target.setAddress(targetAddress);
		target.setRetries(RETRIES);
		target.setTimeout(TIMEOUT);
		return target;
	}

	private CommunityTarget createCommunityTarget(Address targetAddress,String community) {
		CommunityTarget target = new CommunityTarget();
		target.setAddress(targetAddress);
		target.setRetries(RETRIES);
		target.setTimeout(TIMEOUT);
		if (version.equals(SNMP_V1)) {
			target.setVersion(SnmpConstants.version1);
		} else if (version.equals(SNMP_V2C)) {
			target.setVersion(SnmpConstants.version2c);
		}
		OctetString octetString=new OctetString("public");
		if(community!=null){
			octetString=new OctetString(community);
		}
		target.setCommunity(octetString);
		return target;
	}

	private String getJsonFromResponse(ResponseEvent responseEvent) throws IOException {
		PDU response = responseEvent.getResponse();
		
		if (response!=null&&!"0".equals(response.getRequestID().toString())) {
			result.append("{\"WorkstationStatus\":[");
			if (response != null) {
				if (response.getErrorStatus() == PDU.noError) {

					Vector<? extends VariableBinding> vbs = response.getVariableBindings();
					for (VariableBinding vb : vbs) {

						result.append("{\"" + oidMap.get(vb.getOid().toString()) + "\":\"" + vb.toValueString()
								+ "\"},");

					}

					if (vbs.size() > 0) {
						result.deleteCharAt(result.length() - 1);
					}

				} else {
					System.out.println("Error:" + response.getErrorStatusText());
				}
			}
			result.append("]}");
		}else{
			result.append("");
		}
		return result.toString();
	}

}
