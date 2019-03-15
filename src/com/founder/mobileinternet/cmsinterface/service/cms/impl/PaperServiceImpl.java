package com.founder.mobileinternet.cmsinterface.service.cms.impl;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.founder.mobileinternet.cmsinterface.pojo.Configure;
import com.founder.mobileinternet.cmsinterface.service.RedisKey;
import com.founder.mobileinternet.cmsinterface.service.RedisManager;
import com.founder.mobileinternet.cmsinterface.service.cms.PaperService;
import com.founder.mobileinternet.cmsinterface.util.CommonToolUtil;
@Service
public class PaperServiceImpl implements PaperService{
	private static final Logger log = LoggerFactory.getLogger(LeaderServiceImpl.class);

	@Autowired
	private RedisManager redisManager;
	@Autowired
	private Configure configure;

	/**
	 * 站点报纸列表
	 */
	@Override
	public String getPapers(int siteID) {
		String key = RedisKey.APP_PAPER_KEY + siteID;
		String value = redisManager.get(key);
		if(value == null){
			log.info("没有缓存数据，重新获取");
			String url = this.configure.getAppServerUrl() + "/getPapers.do?siteID="+siteID;
			log.debug("请求地址：" + url);
			if(CommonToolUtil.canGetData(url))
				value = redisManager.get(key);
		}
		return value;

	}

	@Override
	public String getPaperDates(int siteID, long id,int start,int count) {
		String key = RedisKey.APP_PAPER_DATE_KEY + id;
		String value = redisManager.get(key);
		if(value == null){
			log.info("没有缓存数据，重新获取");
			String url = this.configure.getAppServerUrl() + "/getPaperDates.do?siteID="
					+siteID+"&paperID="+id+"&start="+start+"&count="+count;
			log.debug("请求地址：" + url);
			if(CommonToolUtil.canGetData(url))
				value = redisManager.get(key);
		}
		return value;
	}

	@Override
	public String getPaperLayouts(int siteID, int id, String date) {
		String url = this.configure.getAppServerUrl() + "/getPaperLayouts.do?siteID="
				+siteID+"&paperID="+id+"&date="+date;
		date = CommonToolUtil.getData(url);
		String key = RedisKey.APP_PAPER_LAYOUT_KEY + id + "." + date;
		String value =  redisManager.get(key);
		if (value != null){
			value = setArticleList(siteID,value);
		}
		return value;
	}

	@Override
	public String getPaperArticles(int siteID, int id) {
		String key = RedisKey.APP_PAPER_ARTICLELIST_KEY + id;
		String value = redisManager.get(key);
		if(value == null){
			log.info("没有缓存数据，重新获取");
			String url = this.configure.getAppServerUrl() + "/getPaperArticles.do?siteID="
					+siteID+"&id="+id;
			log.debug("请求地址：" + url);
			if(CommonToolUtil.canGetData(url))
				value = redisManager.get(key);
		}
		JSONArray artArry = JSONArray.fromObject(value);
		//过滤空标题稿件
//		artArry = filterArtArry(artArry);
		//设置点击量、阅读数
		value = ArticleCountHelper.setArticlesCount(redisManager, artArry).toString();
		if (null != value && !"".equals(value)) {
			JSONArray jsonArr=JSONArray.fromObject(value);
			for(int i=0;i<jsonArr.size();i++){
				JSONObject jsonObj= (JSONObject) jsonArr.get(i);
				// 评论数
				String countKey = RedisKey.NIS_EVENT_PAPERARTICLE + jsonObj.getString("fileId");
				String count = redisManager.hget(countKey, "d");
				String countDiscuss = jsonObj.getString("countDiscuss");
				
				if(!CommonToolUtil.isBlank(count) && !CommonToolUtil.isBlank(countDiscuss)
						&& Integer.valueOf(count) > Integer.valueOf(countDiscuss)){
					jsonObj.put("countDiscuss", Integer.valueOf(count));
				}
			}
			return jsonArr.toString();
		}
		return value;
	}

	@Override
	public String getPaperArticle(int siteID, long id) {
		String key = RedisKey.APP_PAPER_ARTICLE_KEY + id;
		String value = redisManager.get(key);
		if(value == null){
			log.info("没有缓存数据，重新获取");
			String url = this.configure.getAppServerUrl() + "/getPaperArticle.do?siteID="+siteID
					+"&id="+id;
			log.debug("请求地址：" + url);
			if(CommonToolUtil.canGetData(url))
				value = redisManager.get(key);
		}
		value = value.replaceAll("&nbsp;", "");
		return setCount(value);
	}
	private String setCount(String value){
		if(null!=value&&!"".equals(value)){
			JSONObject jsonObj=JSONObject.fromObject(value);
			String key = RedisKey.NIS_EVENT_PAPERARTICLE + jsonObj.getInt("fileId");
			String count = redisManager.hget(key, "p");
			int countPraise = getCount(jsonObj, "countPraise");
			if (!CommonToolUtil.isBlank(count) && Integer.parseInt(count) > countPraise){
				jsonObj.put("countPraise", Integer.valueOf(count));
			}
		}
		return value;
	}
	private static int getCount(JSONObject jsonObj, String key) {
		if (!jsonObj.containsKey(key)) return 0;

		Object count = jsonObj.get(key);
		if (count == null) return 0;

		if (count instanceof String) {
			if (count.equals("")) return 0;
			return Integer.parseInt((String)count);
		} else if (count instanceof Integer) {
			return (Integer)count;
		} else {
			return 0;
		}
	}
	private String setArticleList(int siteID,String  value){
		JSONObject fromObject = JSONObject.fromObject(value);
		Object layouts = fromObject.get("layouts");
		if(layouts==null) return value;
		JSONArray jsonArray = JSONArray.fromObject(layouts);
		JSONArray newArr = new JSONArray();
		for(int i=0 ;i<jsonArray.size();i++){
			JSONObject json = JSONObject.fromObject(jsonArray.get(i));
			int id = Integer.parseInt(json.get("id").toString());
			String alist = getPaperArticles(siteID, id);
			json.put("list", alist);
			newArr.add(json);
		}
		fromObject.remove("layouts");
		fromObject.put("layouts", newArr);
		return fromObject.toString();
	}
}
