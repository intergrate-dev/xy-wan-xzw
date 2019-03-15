package com.founder.mobileinternet.cmsinterface.service.cms.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.founder.mobileinternet.cmsinterface.pojo.Configure;
import com.founder.mobileinternet.cmsinterface.service.cms.CollectService;
import com.founder.mobileinternet.cmsinterface.util.CommonToolUtil;

@Service
public class CollectServiceImpl implements CollectService {

	@Autowired
	private Configure configure;

	public String getCollectId(String userId) {
		String url = configure.getInnerApiUrl() + "/api/collect/getCollectId.do?userId=" + userId;
		return CommonToolUtil.getData(url);
	};

	public String getCollectIds(String userId) {
		String url = configure.getInnerApiUrl() + "/api/collect/getCollectIds.do?userId=" + userId;
		return CommonToolUtil.getData(url);
	};

	public String getCollect(String userId, String articleId, String articleName) {
		String url = configure.getInnerApiUrl() + "/api/collect/getCollect.do?userId=" + userId + "&articleId="
				+ articleId + "&articleName=" + articleName;
		return CommonToolUtil.getData(url);
	};

	public String delCollect(String userId, String articleId){
		String url = configure.getInnerApiUrl() + "/api/collect/delCollect.do?userId=" + userId + "&articleId="
				+ articleId;
		return CommonToolUtil.getData(url);
	};

	public String checkCollection(String userId, String articleId){
		String url = configure.getInnerApiUrl() + "/api/collect/checkCollection.do?userId=" + userId + "&articleId="
				+ articleId;
		return CommonToolUtil.getData(url);
	};
}
