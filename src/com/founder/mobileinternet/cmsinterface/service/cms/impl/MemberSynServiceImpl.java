package com.founder.mobileinternet.cmsinterface.service.cms.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.founder.mobileinternet.cmsinterface.pojo.Configure;
import com.founder.mobileinternet.cmsinterface.service.cms.MemberSynService;
import com.founder.mobileinternet.cmsinterface.util.CommonToolUtil;

@Service
public class MemberSynServiceImpl implements MemberSynService {

	@Autowired
	private Configure configure;

	public String registerEx(String ssoid, String username, String nickname, String mobile, String email,
			String password) {
		String url = configure.getInnerApiUrl() + "/api/member/syn/registerEx.do?ssoid=" + ssoid + "&username="
				+ username + "&nickname=" + nickname + "&mobile=" + mobile + "&email=" + email;
		return CommonToolUtil.getData(url);
	};

	public String modify(String nickname, String mobile) {
		String url = configure.getInnerApiUrl() + "/api/member/syn/modify.do?nickname=" + nickname + "&mobile="
				+ mobile;
		return CommonToolUtil.getData(url);
	};

	public String updatePassword(String password, String mobile) {
		String url = configure.getInnerApiUrl() + "/api/member/syn/updatePassword.do?password=" + password + "&mobile="
				+ mobile;
		return CommonToolUtil.getData(url);
	};

	public String loginByOther(int type, String oid, String name) {
		String url = configure.getInnerApiUrl() + "/api/member/syn/loginByOther.do?type=" + type + "&oid=" + oid
				+ "&name=" + name;
		return CommonToolUtil.getData(url);
	};

}
