package com.founder.mobileinternet.cmsinterface.service.cms.impl;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.founder.mobileinternet.cmsinterface.service.RedisKey;
import com.founder.mobileinternet.cmsinterface.service.RedisManager;
import com.founder.mobileinternet.cmsinterface.util.CommonToolUtil;

public class ArticleCountHelper {
	
	/**
	 * 对json里的每个稿件，改为Redis里的实时事件数
	 */
	public static JSONArray setArticlesCount(RedisManager redisManager, JSONArray jsonArr){
		int size = jsonArr.size();
		for (int i = 0; i < size; i++) {
			JSONObject jsonObj = (JSONObject)jsonArr.get(i);
			if(null!=jsonObj&&jsonObj.size()>0){
				String id = jsonObj.getString("fileId");
				// 稿件点击数
				String key = RedisKey.NIS_EVENT_ARTICLE + id;
				String count1 = redisManager.hget(key, "c");
				String count2 = redisManager.hget(key, "sc");
				int countClickInitial = getCount(jsonObj, "countClickInitial");

				int count3 = 0;
				if(!CommonToolUtil.isBlank(count1)) count3 = Integer.valueOf(count1);
				if(!CommonToolUtil.isBlank(count2)) count3 += Integer.valueOf(count2);
				count3 += Integer.valueOf(countClickInitial);

				int jsoncountClick = getCount(jsonObj, "countClick");
				if (count3 > jsoncountClick){
					jsonObj.put("countClick", count3);
				}

				// 稿件点赞数
				count1 = redisManager.hget(key, "p");
				int countPraise = getCount(jsonObj, "countPraise");
				if (!CommonToolUtil.isBlank(count1) && Integer.parseInt(count1) > countPraise){
					jsonObj.put("countPraise", Integer.valueOf(count1));
				}
				// 分享数
				count1 = redisManager.hget(key, "s");
				int countShare = getCount(jsonObj, "countShare");
				if(!CommonToolUtil.isBlank(count1) && Integer.valueOf(count1) > countShare){
					jsonObj.put("countShare", Integer.valueOf(count1));
				}
				// 评论数
				count1 = redisManager.hget(key, "d");
				int countDiscuss = getCount(jsonObj, "countDiscuss");
				if(!CommonToolUtil.isBlank(count1) && Integer.valueOf(count1) > countDiscuss){
					jsonObj.put("countDiscuss", Integer.valueOf(count1));
				}
			}

		}
		return jsonArr;
	}
	
	/**
	 * 读出Redis中的稿件事件数
	 */
	public static String getArticleCount(RedisManager redisManager, long id, String field) {
		String key = RedisKey.NIS_EVENT_ARTICLE + id;
		
		return redisManager.hget(key, field);
	}
	/**
	 * 读出Redis中的直播事件数
	 */
	public static String getLiveCount(RedisManager redisManager, long id, String field) {
		String key = RedisKey.NIS_EVENT_LIVE + id;
		
		return redisManager.hget(key, field);
	}
	
	/**
	 * 读稿件的点击数，包括初始点击数+实际点击数+分享点击数
	 */
	public static int calArticleCountClick(RedisManager redisManager,
			JSONObject jsonArticle, long docID) {
		int countClick = 0;

		// 从Redis里读点击数=实际点击数+分享点击数
		String key = RedisKey.NIS_EVENT_ARTICLE + docID;
		// 点击数
		String click_RS = redisManager.hget(key, "c");
		if (!CommonToolUtil.isBlank(click_RS)) {
			countClick += Integer.valueOf(click_RS);
		}
		// 分享点击数
		String share_RS = redisManager.hget(key, "sc");
		if (!CommonToolUtil.isBlank(share_RS)) {
			countClick += Integer.valueOf(share_RS);
		}

		// +初始点击数
		int countInitial = getCount(jsonArticle, "countClickInitial");
		countClick += countInitial;

		// 从json中把 countClick取出来， 如果countClick 大于 相加的值，使用json中的
		int countClickJson = getCount(jsonArticle, "countClick");
		if (countClickJson > countClick)
			countClick = countClickJson;

		return countClick;
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
}
