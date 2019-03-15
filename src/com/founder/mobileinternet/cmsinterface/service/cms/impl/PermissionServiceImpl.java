package com.founder.mobileinternet.cmsinterface.service.cms.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.founder.mobileinternet.cmsinterface.pojo.Configure;
import com.founder.mobileinternet.cmsinterface.service.cms.PermissionService;
import com.founder.mobileinternet.cmsinterface.util.CommonToolUtil;

@Service
public class PermissionServiceImpl implements PermissionService {
	@Autowired
	private Configure configure;

	
	
	public String getUserPermission(String uid, String siteID) {
		String url = configure.getInnerApiUrl() + "/api/read/getUserPermission.do?uid=" + uid + "&siteID=" + siteID;
		return CommonToolUtil.getData(url);
	}

	public String getAllPermission(String siteID){
		String url = configure.getInnerApiUrl() + "/api/read/getAllPermission.do?siteID=" + siteID;
		return CommonToolUtil.getData(url);
	}

}
		

