package com.founder.mobileinternet.cmsinterface.service.cms.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.founder.mobileinternet.cmsinterface.pojo.Configure;
import com.founder.mobileinternet.cmsinterface.service.RedisKey;
import com.founder.mobileinternet.cmsinterface.service.RedisManager;
import com.founder.mobileinternet.cmsinterface.service.cms.LeaderService;
import com.founder.mobileinternet.cmsinterface.util.CommonToolUtil;

@Service
public class LeaderServiceImpl implements LeaderService {
	private static final Logger log = LoggerFactory.getLogger(LeaderServiceImpl.class);
	
	@Autowired
	private RedisManager redisManager;
	@Autowired
	private Configure configure;

	@Override
	public String getLeaderList(int start, int count, int siteID) {
		String key = redisManager.getKeyBySite(RedisKey.APP_LEADERLIST_KEY, siteID) + start;
		return getDataResult(key, "/leaderView.do?start=" + start +"&count=" +count
				+ "&siteID=" + siteID);
	}

	@Override
	public String getLeaderDetail(int id, int siteID) {
		String key = RedisKey.APP_LEADER_KEY + id;
		return getDataResult(key, "/leader.do?id="+id+ "&siteID=" + siteID);
	}

	@Override
	public String getRegionLeaderList(int regionID, int siteID) {
		String key = redisManager.getKeyBySite(RedisKey.APP_LEADERLIST_REGION_KEY, siteID) + regionID;
		return getDataResult(key, "/regionLeaderView.do?regionID="+regionID+ "&siteID=" + siteID);
	}
	
	private String getDataResult(String key, String urlParam){
		String value = redisManager.get(key);
		if(value == null){
			log.info("没有缓存数据，重新获取");
			String url = this.configure.getAppServerUrl() + urlParam;
			log.debug("请求地址：" + url);
			if(CommonToolUtil.canGetData(url))
				value = redisManager.get(key);
		}
		return value;
	}
}
