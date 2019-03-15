package com.founder.mobileinternet.cmsinterface.service.cms.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.founder.mobileinternet.cmsinterface.pojo.Configure;
import com.founder.mobileinternet.cmsinterface.service.cms.SetMealService;
import com.founder.mobileinternet.cmsinterface.util.CommonToolUtil;

@Service
public class SetMealServiceImpl implements SetMealService {

	@Autowired
	private Configure configure;

	public String FindSetMeal(String siteID) {
		String url = configure.getInnerApiUrl() + "/api/setmeal/FindSetMeal.do?siteID=" + siteID;
		return CommonToolUtil.getData(url);
	};

	public String FindSetMealByIds(String ids) {
		;
		String url = configure.getInnerApiUrl() + "/api/setmeal/FindSetMealByIds.do?ids=" + ids;
		return CommonToolUtil.getData(url);
	}

}
