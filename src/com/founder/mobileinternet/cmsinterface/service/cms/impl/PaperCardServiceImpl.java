package com.founder.mobileinternet.cmsinterface.service.cms.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.founder.mobileinternet.cmsinterface.pojo.Configure;
import com.founder.mobileinternet.cmsinterface.service.cms.PaperCardService;
import com.founder.mobileinternet.cmsinterface.util.CommonToolUtil;

@Service
public class PaperCardServiceImpl implements PaperCardService {
	
	@Autowired
	private Configure configure;
	
	public String activatePaperCard(String uid, String ssoid, String pcno, String password, String siteID) {
		String url = configure.getInnerApiUrl() + "/api/pcard/activatePaperCard.do?uid=" + uid + "&ssoid=" + ssoid
				+ "&pcno=" + pcno + "&password=" + password + "&siteID=" + siteID;
		return CommonToolUtil.getData(url);
	};
}
