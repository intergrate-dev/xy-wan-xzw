package com.founder.mobileinternet.cmsinterface.service.cms.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.founder.mobileinternet.cmsinterface.pojo.Configure;
import com.founder.mobileinternet.cmsinterface.service.cms.ExamineService;
import com.founder.mobileinternet.cmsinterface.util.CommonToolUtil;

@Service
public class ExamineServiceImpl implements ExamineService {

	@Autowired
	private Configure configure;

	@Override
	public String docSync(int siteID, int page, int size, String startTime,
			String endTime) {
		String url = (this.configure.getAppServerUrl()
				+ "/originalArticle.do?siteID=" + siteID + "&page=" + page
				+ "&size=" + size + "&startTime=" + startTime + "&endTime=" + endTime)
				.replaceAll(" ", "%20");
		String data = CommonToolUtil.getData(url);
		return data;
	}

	@Override
	public String columnSync(int siteID) {
		String url = this.configure.getAppServerUrl()
				+ "/examineColumns.do?siteID=" + siteID;
		String data = CommonToolUtil.getData(url);
		return data;

	}

}
