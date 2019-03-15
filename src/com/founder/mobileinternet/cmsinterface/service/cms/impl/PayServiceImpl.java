package com.founder.mobileinternet.cmsinterface.service.cms.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.founder.mobileinternet.cmsinterface.pojo.Configure;
import com.founder.mobileinternet.cmsinterface.service.cms.PayService;
import com.founder.mobileinternet.cmsinterface.util.CommonToolUtil;

@Service
public class PayServiceImpl implements PayService {

	@Autowired
	private Configure configure;

	public String createpayrecordpc(String payType, String payID, String payNumber, String payMoney, String payChannel,
			String payTime, String payOrder) {
		String url = configure.getInnerApiUrl() + "/api/pay/createpayrecordpc.do?payType=" + payType + "&payID="
				+ payID.replaceAll("\\{", "%7B").replaceAll("\\}", "%7D").replaceAll("\"", "%22")
						.replaceAll("\\:", "%3A").replaceAll("\\,", "%2C").replaceAll("\\/", "%2F")
						.replaceAll("\\ ", "%20")
				+ "&payTime="
				+ payTime.replaceAll("\\{", "%7B").replaceAll("\\}", "%7D").replaceAll("\"", "%22")
						.replaceAll("\\:", "%3A").replaceAll("\\,", "%2C").replaceAll("\\/", "%2F")
						.replaceAll("\\ ", "%20")
				+ "&payNumber=" + payNumber + "&payMoney=" + payMoney + "&payChannel=" + payChannel + "&payOrder="
				+ payOrder;
		return CommonToolUtil.getData(url);
	}

	@Override
	public String createpayrecordapp(String payType, String payID, String payNumber, String payMoney, String payChannel,
			String payTime, String payOrder) {
		String url = configure.getInnerApiUrl() + "/api/pay/createpayrecordapp.do?payType=" + payType + "&payID="
				+ payID.replaceAll("\\{", "%7B").replaceAll("\\}", "%7D").replaceAll("\"", "%22")
						.replaceAll("\\:", "%3A").replaceAll("\\,", "%2C").replaceAll("\\/", "%2F")
						.replaceAll("\\ ", "%20")
				+ "&payTime="
				+ payTime.replaceAll("\\{", "%7B").replaceAll("\\}", "%7D").replaceAll("\"", "%22")
						.replaceAll("\\:", "%3A").replaceAll("\\,", "%2C").replaceAll("\\/", "%2F")
						.replaceAll("\\ ", "%20")
				+ "&payNumber=" + payNumber + "&payMoney=" + payMoney + "&payChannel=" + payChannel + "&payOrder="
				+ payOrder;
		return CommonToolUtil.getData(url);
	}

	@Override
	public String getpaylog(String orderNum) {
		String url = configure.getInnerApiUrl() + "/api/pay/getpaylog.do?orderNum=" + orderNum;
		return CommonToolUtil.getData(url);
	}

}
