package com.founder.mobileinternet.cmsinterface.service.cms.impl;

import com.founder.mobileinternet.cmsinterface.pojo.Configure;
import com.founder.mobileinternet.cmsinterface.service.RedisKey;
import com.founder.mobileinternet.cmsinterface.service.RedisManager;
import com.founder.mobileinternet.cmsinterface.service.cms.TopicsService;
import com.founder.mobileinternet.cmsinterface.util.CommonToolUtil;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TopicsServiceImpl implements TopicsService {

    @Autowired
    private RedisManager redisManager;
    @Autowired
    private Configure configure;
    private static final int COUNT = 20;

    @Override
    public String hotTopics(int siteID) {
        String key = RedisKey.APP_HOT_TOPICS_KEY + siteID;
        String value = redisManager.get(key);
        if (null == value) {
            String url = configure.getAppServerUrl() + "/hotTopics.do?siteID=" + siteID;
            CommonToolUtil.getData(url);
            value = redisManager.get(key);
        }

        if (null != value && !"".equals(value)) {
            return value;
        } else {
            return "{list:[]}";

        }
//        return value;
    }

    @Override
    public String topicsByGroup(int siteID) {
        String key = RedisKey.APP_TOPICSBYGROUP_KEY + siteID;
        String value = redisManager.get(key);
        if (null == value) {
            String url = configure.getAppServerUrl() + "/topicsByGroup.do?siteID=" + siteID;
            CommonToolUtil.getData(url);
            value = redisManager.get(key);
        }

        if (null != value && !"".equals(value)) {
            return value;
        } else {
            return "{list:[]}";

        }
//        return value;
    }
    
    @Override
    public String topics(int siteID,int page) {
    	long start = page * COUNT;
		long end = (page + 1) * COUNT - 1;
		
        String key = RedisKey.WEB_TOPICS_KEY + siteID;
        List<String> value = null;
//        String value = redisManager.get(key);
        long len = redisManager.llen(key);
        if (len == 0) {
            String url = configure.getAppServerUrl() + "/topics.do?siteID=" + siteID;
            CommonToolUtil.getData(url);
            value = redisManager.lrange(key, start, end);
        } else {
        	value = redisManager.lrange(key, start, end);
        }

        if (null != value && !"".equals(value)) {
            return "{\"list\":" + value.toString() + "}";
        } else {
            return "{\"list\":[]}";

        }
//        return value;
    }
    
    @Override
    public String articleTopics(int articleID,int channel) {
        String key = RedisKey.ARTICLE_TOPICS_KEY + channel + "." + articleID;
        String value = redisManager.get(key);
        if (null == value) {
            String url = configure.getAppServerUrl() + "/articleTopics.do?articleID=" + articleID + "&channel=" + channel;
            CommonToolUtil.getData(url);
            value = redisManager.get(key);
        }

        if (null != value && !"".equals(value)) {
            return value;
        } else {
            return "{list:[]}";

        }
    }

    @Override
    public String articleTopicsByGroup(int articleID, int channel, int groupID) {
        String key = RedisKey.ARTICLE_TOPICSGROUP_KEY + channel + "." + articleID+"."+groupID;
        String value = redisManager.get(key);
        if (null == value) {
            String url = configure.getAppServerUrl() + "/articleTopicsByGroup.do?articleID=" + articleID + "&channel=" + channel+"&groupID="+groupID;
            CommonToolUtil.getData(url);
            value = redisManager.get(key);
        }
        if (null != value && !"".equals(value)) {
            return value;
        } else {
            return "{list:[]}";

        }
    }

    @Override
    public String webTopicsByGroup(int siteID, int page, int groupID) {
        long start = page * COUNT;
        long end = (page + 1) * COUNT - 1;

        String key = RedisKey.WEB_TOPICSBYGROUP_KEY + siteID +"."+ groupID;
        List<String> value = null;
        long len = redisManager.llen(key);
        if (len == 0) {
            String url = configure.getAppServerUrl() + "/webTopicsByGroup.do?siteID=" + siteID +"&groupID="+ groupID;
            CommonToolUtil.getData(url);
            value = redisManager.lrange(key, start, end);
        } else {
            value = redisManager.lrange(key, start, end);
        }

        if (null != value && !"".equals(value)) {
            return "{\"list\":" + value.toString() + "}";
        } else {
            return "{\"list\":[]}";

        }
    }
}
