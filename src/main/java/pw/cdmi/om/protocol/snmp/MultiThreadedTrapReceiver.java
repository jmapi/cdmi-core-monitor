package pw.cdmi.om.protocol.snmp;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.CommandResponder;
import org.snmp4j.CommandResponderEvent;
import org.snmp4j.MessageDispatcherImpl;
import org.snmp4j.MessageException;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.mp.MPv1;
import org.snmp4j.mp.MPv2c;
import org.snmp4j.mp.MPv3;
import org.snmp4j.mp.StateReference;
import org.snmp4j.mp.StatusInformation;
import org.snmp4j.security.SecurityModels;
import org.snmp4j.security.SecurityProtocols;
import org.snmp4j.security.USM;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.TcpAddress;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultTcpTransportMapping;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.util.MultiThreadedMessageDispatcher;
import org.snmp4j.util.ThreadPool;
import org.springframework.stereotype.Component;

/************************************************************
 * TODO(对类的简要描述说明 – 必须).
 * TODO(对类的作用含义说明 – 可选).
 * TODO(对类的使用方法说明 – 可选).
 * 
 * @author WUWEI
 * @version iSoc Service Platform, 2015年3月12日
 ************************************************************/
@Component
public class MultiThreadedTrapReceiver implements CommandResponder {

	private static final Logger logger = LoggerFactory.getLogger(MultiThreadedTrapReceiver.class);

	private static final int udp_port = 162;

	private static final int tcp_port = 161;

	private static final String host = "127.0.0.1";
	
	/** 
	 * 实现CommandResponder的processPdu方法, 用于处理传入的请求、PDU等信息 
	 * 当接收到<span class="hilite3">trap</span> 
	 时，会自动进入这个方法 
	*/
	@Override
	public void processPdu(CommandResponderEvent event) {

		// 解析Response
		if (event == null) {
			return;
		}
		
		PDU result = event.getPDU();
		if (result != null) {
			Address source = event.getPeerAddress();
			String addr = source.toString();
			String sourceIp = addr.substring(0, addr.indexOf("/"));

			if ((result.getType() != PDU.TRAP) && (result.getType() != PDU.V1TRAP) && (result.getType() != PDU.REPORT)
					&& (result.getType() != PDU.RESPONSE)) {
				result.setErrorIndex(0);
				result.setErrorStatus(0);
				result.setType(PDU.RESPONSE);
				StatusInformation statusInformation = new StatusInformation();
				StateReference ref = event.getStateReference();
				try {
					event.getMessageDispatcher().returnResponsePdu(event.getMessageProcessingModel(),
						event.getSecurityModel(), event.getSecurityName(), event.getSecurityLevel(), result,
						event.getMaxSizeResponsePDU(), ref, statusInformation);
				} catch (MessageException ex) {
					logger.debug("Error while sending response: " + ex.getMessage());
				}
			}
			Vector<VariableBinding> recVBs = result.getVariableBindings();
			Map<String,String> trapInfo = new LinkedHashMap<String, String>();
			for (int i = 0; i < recVBs.size(); i++) {
				VariableBinding recVB = recVBs.elementAt(i);
				trapInfo.put(recVB.getOid().toString(), recVB.getVariable().toString());
//				trapHelper.saveTrapData(sourceIp, trapInfo);
				logger.debug(recVB.getOid() + " : " + recVB.getVariable());
			}
		}

	}

	private MultiThreadedMessageDispatcher dispatcher;

	private Snmp snmp = null;

	private Address listenAddress;

	private ThreadPool threadPool;

	public MultiThreadedTrapReceiver() {
		// BasicConfigurator.configure();
		threadPool = ThreadPool.create("Trap", 2);
		dispatcher = new MultiThreadedMessageDispatcher(threadPool, new MessageDispatcherImpl());
	}

	private void init(String address) throws UnknownHostException, IOException {
		listenAddress = GenericAddress.parse(System.getProperty("snmp4j.listenAddress", address)); // 本地IP与监听端口
		TransportMapping transport;
		// 对TCP与UDP协议进行处理
		if (listenAddress instanceof UdpAddress) {
			transport = new DefaultUdpTransportMapping((UdpAddress) listenAddress);
		} else {
			transport = new DefaultTcpTransportMapping((TcpAddress) listenAddress);
		}
		snmp = new Snmp(dispatcher, transport);
		snmp.getMessageDispatcher().addMessageProcessingModel(new MPv1());
		snmp.getMessageDispatcher().addMessageProcessingModel(new MPv2c());
		snmp.getMessageDispatcher().addMessageProcessingModel(new MPv3());
		USM usm = new USM(SecurityProtocols.getInstance(), new OctetString(MPv3.createLocalEngineID()), 0);
		SecurityModels.getInstance().addSecurityModel(usm);
		snmp.listen();
	}

	// @PostConstruct
	public void run_udp() {
		try {
			init("udp:" + host + "/" + udp_port);
			snmp.addCommandResponder(this);
			logger.info("开始监听SNMP Trap信息... Trap端口：udp(" + udp_port + ")");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	// @PostConstruct
	public void run_tcp() {
		try {
			init("tcp:" + host + "/" + tcp_port);
			snmp.addCommandResponder(this);
			logger.info("开始监听SNMP Trap信息... Trap端口：tcp(" + tcp_port + ")");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void main(String[] gx) {
		System.out.println("进入MultiThreadedTrapRe....");
		MultiThreadedTrapReceiver multithreadedtrapreceiver = new MultiThreadedTrapReceiver();
		multithreadedtrapreceiver.run_udp();
		multithreadedtrapreceiver.run_tcp();
	}

}
