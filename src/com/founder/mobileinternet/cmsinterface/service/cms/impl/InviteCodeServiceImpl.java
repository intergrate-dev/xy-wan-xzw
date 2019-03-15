package com.founder.mobileinternet.cmsinterface.service.cms.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.founder.mobileinternet.cmsinterface.pojo.Configure;
import com.founder.mobileinternet.cmsinterface.service.cms.InviteCodeService;
import com.founder.mobileinternet.cmsinterface.util.CommonToolUtil;

@Service
public class InviteCodeServiceImpl implements InviteCodeService {

	@Autowired
	private Configure configure;

	public String codeImeiRecord(String code, String imei, String uid, String name, String siteID) {
		String url = configure.getInnerApiUrl() + "/api/invitecode/codeImeiRecord.do?code=" + code + "&imei=" + imei + "&uid="
				+ uid + "&name=" + name + "&siteID=" + siteID;
		return CommonToolUtil.getData(url);
	};

	public String getInviteCode(String uid, String siteID){
		String url = configure.getInnerApiUrl() + "/api/invitecode/getInviteCode.do?uid=" + uid + "&siteID=" + siteID;
		return CommonToolUtil.getData(url);
	};

	public String getInviteRecord(String uid, String siteID){
		String url = configure.getInnerApiUrl() + "/api/invitecode/getInviteRecord.do?uid=" + uid + "&siteID=" + siteID;
		return CommonToolUtil.getData(url);
	};
	public String siteConf(String siteID){
		String url = configure.getInnerApiUrl() + "/api/invitecode/siteConf.do?siteID=" + siteID;
		return CommonToolUtil.getData(url);
	};
	public String logoConf(String siteID){
		String url = configure.getInnerApiUrl() + "/api/invitecode/logoConf.do?siteID=" + siteID;
		return CommonToolUtil.getData(url);
	};
}
