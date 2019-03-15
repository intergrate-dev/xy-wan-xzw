package com.founder.mobileinternet.cmsinterface.service.cms.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.founder.mobileinternet.cmsinterface.pojo.Configure;
import com.founder.mobileinternet.cmsinterface.service.RedisKey;
import com.founder.mobileinternet.cmsinterface.service.RedisManager;
import com.founder.mobileinternet.cmsinterface.service.cms.ReporterService;
import com.founder.mobileinternet.cmsinterface.util.CommonToolUtil;

@Service
public class ReporterServiceImpl implements ReporterService {
	private static final Logger log = LoggerFactory.getLogger(LeaderServiceImpl.class);

	@Autowired
	private RedisManager redisManager;
	@Autowired
	private Configure configure;

	@Override
	public String getAuthorCount(int id, int siteID) {

		String key = RedisKey.APP_AUTHOR_COUNT_KEY + id;
		String value = this.redisManager.get(key);
		if(value == null){
			log.info("没有缓存数据，重新获取");
			String url = this.configure.getAppServerUrl() + "/authorCount.do?id="+id
					 + "&siteID=" + siteID;
			log.debug("请求地址：" + url);
			if(CommonToolUtil.canGetData(url))
				value = redisManager.get(key);
		}
		return value;
	}

	@Override
	public boolean myAuthor(int siteID, int userID, String userName,
			int authorID, String authorName) {

			String url = this.configure.getAppServerUrl() + "/myAuthor.do?siteID="+siteID
					+ "&userID=" + userID + "&userName=" + userName + "&authorID=" + authorID
					+ "&authorName=" + authorName;
			log.debug("请求地址：" + url);
		return Boolean.parseBoolean(CommonToolUtil.getData(url));
	}

	@Override
	public String myAuthorView(int siteID,int id, int page, int lastFileId) {
		String key = RedisKey.MY_AUTHOR_KEY  + id + "." + page ;
		String value = redisManager.get(key);
		if (value == null) {
			String url = this.configure.getAppServerUrl() + "/myAuthorView.do?siteID=" + siteID + "&id=" + id + "&page=" + page ;
			if(CommonToolUtil.canGetData(url)){
				value = redisManager.get(key);
			}
		}
		if(page > 0 && lastFileId > 0 && null != value){
			value = CommonToolUtil.listFilter(value, lastFileId, null, "id");
		}
		if (value == null)
        	value = "[]";
		return value;
	}

	@Override
	public boolean myAuthorCancel(int siteID, int userID, int authorID) {
		String url = this.configure.getAppServerUrl() + "/myAuthorCancel.do?siteID=" + siteID
				+ "&userID=" + userID + "&authorID=" + authorID;
		return Boolean.parseBoolean(CommonToolUtil.getData(url));
	}

	@Override
	public boolean isAttention(int userID, int authorID, int siteID) {
		String key = RedisKey.APP_AUTHOR_FANS_KEY+authorID ;
		if(!redisManager.exists(key)){
			String url = this.configure.getAppServerUrl() + "/isAttention.do?userID=" + userID
					+ "&authorID=" + authorID + "&siteID=" + siteID;
			CommonToolUtil.canGetData(url);
		}
		return redisManager.sismember(key, String.valueOf(userID));
	}

}
