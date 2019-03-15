package com.founder.mobileinternet.cmsinterface.service.cms.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.founder.mobileinternet.cmsinterface.pojo.Configure;
import com.founder.mobileinternet.cmsinterface.pojo.cms.VoteInfo;
import com.founder.mobileinternet.cmsinterface.service.RedisKey;
import com.founder.mobileinternet.cmsinterface.service.RedisManager;
import com.founder.mobileinternet.cmsinterface.service.cms.VoteService;
import com.founder.mobileinternet.cmsinterface.util.CommonToolUtil;

@Service
public class VoteServiceImpl implements VoteService {
	@Autowired
	private RedisManager redisManager;
	@Autowired
	private Configure configure;

	public String commitVote(VoteInfo voteInfo) {
		String url = configure.getAppServerUrl() + "/vote.do";
		return CommonToolUtil.postBeanDataWithResult(url, voteInfo);
	}

	public String voteCount(int id, int siteID) {
		String key = RedisKey.APP_VOTE_COUNT_KEY + id;
		return getDataResult(key, "/voteCount.do?id=" + id + "&siteID=" + siteID);
	}

	public String voteResult(int id, int siteID) {
		String key = RedisKey.APP_VOTE_RESULT_KEY + id;
		return getDataResult(key, "/voteResult.do?id=" + id + "&siteID=" + siteID);
	}
	
	private String getDataResult(String key, String urlParam){
		String value = redisManager.get(key);
		if (value == null) {
			String url = configure.getAppServerUrl() + urlParam;
//			System.out.println("没有缓存数据，重新获取。  请求地址：" + url);
			if (CommonToolUtil.canGetData(url)) 
				value = redisManager.get(key);
		}
		return value;
	}
}