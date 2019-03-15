package com.founder.mobileinternet.cmsinterface.service.cms.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.founder.mobileinternet.cmsinterface.pojo.Configure;
import com.founder.mobileinternet.cmsinterface.service.cms.DuibaService;
import com.founder.mobileinternet.cmsinterface.util.CommonToolUtil;
import java.net.URLEncoder;

@Service
public class DuibaServiceImpl implements DuibaService {

	@Autowired
	private Configure configure;

	public String autologin(String uid, String redirect) {
		String url = configure.getInnerApiUrl() + "/api/duiba/autologin.do?uid=" + uid + "&redirect=" + redirect;
		return CommonToolUtil.getData(url);
	};

	public String creditConsume(String appKey, String timestamp, String uid, String credits,
			String description, String orderNum, String type, String facePrice, String actualPrice,
			String waitAudit, String ip, String params,String sign) throws Exception{
		String url = configure.getInnerApiUrl() + "/api/duiba/creditConsume.do?sign=" + sign + "&appKey=" + appKey
				+ "&timestamp=" + timestamp + "&uid=" + uid + "&credits=" + credits + "&description=" + URLEncoder.encode(description, "UTF-8")
				+ "&orderNum=" + orderNum + "&type=" + type + "&facePrice=" + facePrice + "&actualPrice=" + actualPrice
				+ "&waitAudit=" + waitAudit + "&ip=" + ip + "&params=" + URLEncoder.encode(params, "UTF-8");
		System.out.println("---------积分消费接口:"+url);
		return CommonToolUtil.getData(url);
	};

	public String creditNotify( String appKey, String timestamp, String success, String errorMessage,
			String bizId, String orderNum,String sign) {
		String url = configure.getInnerApiUrl() + "/api/duiba/creditNotify.do?sign=" + sign + "&appKey=" + appKey
				+ "&timestamp=" + timestamp + "&success=" + success + "&errorMessage=" + errorMessage + "&bizId="
				+ bizId + "&orderNum=" + orderNum;
		System.out.println("---------兑换结果通知接口:"+url);
		return CommonToolUtil.getData(url);
	};
}
