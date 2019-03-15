package com.founder.mobileinternet.cmsinterface.service.cms.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.founder.mobileinternet.cmsinterface.pojo.Configure;
import com.founder.mobileinternet.cmsinterface.service.cms.ColumnsService;
import com.founder.mobileinternet.cmsinterface.util.CommonToolUtil;

@Service
public class ColumnsServiceImpl implements ColumnsService {

	@Autowired
	private Configure configure;

	public String getColsId(String userId, String siteID){
		String url = configure.getInnerApiUrl() + "/api/column/getColsId.do?userId=" + userId + "&siteID=" + siteID;
		return CommonToolUtil.getData(url);
	};

	public String getColumnId(String userId, String pId, String siteID){
		String url = configure.getInnerApiUrl() + "/api/column/getColumnId.do?userId=" + userId + "&pId=" + pId + "&siteID=" + siteID;;
		return CommonToolUtil.getData(url);
	};
}
