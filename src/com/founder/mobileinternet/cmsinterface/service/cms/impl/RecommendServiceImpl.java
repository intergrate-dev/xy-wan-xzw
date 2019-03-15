package com.founder.mobileinternet.cmsinterface.service.cms.impl;

import com.founder.mobileinternet.cmsinterface.service.RedisKey;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.founder.mobileinternet.cmsinterface.pojo.Configure;
import com.founder.mobileinternet.cmsinterface.pojo.cms.RecommendInfo;
import com.founder.mobileinternet.cmsinterface.service.RedisManager;
import com.founder.mobileinternet.cmsinterface.service.cms.RecommendService;
import com.founder.mobileinternet.cmsinterface.util.CommonToolUtil;

@Service
public class RecommendServiceImpl implements RecommendService {
	@Autowired
	private RedisManager redisManager;
	@Autowired
	private Configure configure;
	@Override
	public String getHistoryRecList(RecommendInfo recommendInfo,int start,int count,int siteID) {

		String url = configure.getAppServerUrl()+"/historyRecList.do?start="+start+"&count="+count+"&siteID="+siteID;
		return getData(url,recommendInfo);
	}
	public String getRecommendList(RecommendInfo recommendInfo,int start,int count,int siteID) {

		String url = configure.getAppServerUrl()+"/recommendList.do?start="+start+"&count="+count+"&siteID="+siteID;
		return getData(url,recommendInfo);
	}
	public String getRecKeywords(RecommendInfo recommendInfo) {
		String url = configure.getAppServerUrl()+"/recKeywords.do";
		String key= RedisKey.APP_ARTICLE_WORDS_KEY+recommendInfo.getAid();

		String value=redisManager.get(key);
		if(null==value){
			CommonToolUtil.getBeanData(url,recommendInfo);
			value=redisManager.get(key);
		}

		return value;
	}

    public String revise(String info ,String columnId,int start){
		return info;
	}

	private String getData(String url,RecommendInfo recommendInfo){
		try {
			String info = CommonToolUtil.getBeanData(url, recommendInfo);
			if (info != null && !"".equals(info)) {
				//从redis中读取阅读数，重置阅读数
				JSONObject obj = JSONObject.fromObject(info);
				JSONArray jsonArr = ArticleCountHelper.setArticlesCount(redisManager, obj.getJSONArray("list"));

				obj.put("list", jsonArr);
				return obj.toString();
			} else {
				return "{list:[]}";
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return "{list:[]}";
		}
	}

	@Override
	public String saveEmailSubscribe(int siteID, String name, String email) {
        String urlParam = "/emailSubscribe.do?siteID=" + siteID + "&name=" + name + "&email=" + email;
        String url = configure.getAppServerUrl() + urlParam;
        String info = CommonToolUtil.getData(url);
		return info;
	}
}
