package net._139130.www;

import java.net.URL;
import java.util.UUID;

import org.apache.axis.MessageContext;
import org.apache.axis.transport.http.HTTPConstants;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		WebServiceLocator locator = new WebServiceLocator();
		WebServiceSoapStub stub = (WebServiceSoapStub)locator.getWebServiceSoap(new URL("http://10.0.10.89:8888/Service/WebService.asmx?wsdl"));
		stub._setProperty(MessageContext.HTTP_TRANSPORT_VERSION, HTTPConstants.HEADER_PROTOCOL_V11);
		MTPacks pack = new MTPacks();
		pack.setBatchID(UUID.randomUUID().toString());
		pack.setUuid(pack.getBatchID());
		pack.setSendType(0);
		pack.setMsgType(1);
		pack.setBatchName("测试批次");
		MessageData[] msgs = new MessageData[1];
		msgs[0] = new MessageData();
		msgs[0].setPhone("15101115183");
		msgs[0].setContent("验证码0870");
		
		pack.setMsgs(msgs);
		GsmsResponse resp = stub.post("nfapp", "123456", pack);
		System.out.println(resp.getResult() + "--" + resp.getMessage());
		
//		AccountInfo acInfo = stub.getAccountInfo("admin", "123456");
//		System.out.println(acInfo);
//		System.out.println(stub.postSingle("admin", "123456", "13430258111", "test msg", null));
	}

}
