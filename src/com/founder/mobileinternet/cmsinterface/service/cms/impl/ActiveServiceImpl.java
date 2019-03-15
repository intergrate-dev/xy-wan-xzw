package com.founder.mobileinternet.cmsinterface.service.cms.impl;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.founder.mobileinternet.cmsinterface.pojo.Configure;
import com.founder.mobileinternet.cmsinterface.pojo.cms.ActivityInfo;
import com.founder.mobileinternet.cmsinterface.service.RedisKey;
import com.founder.mobileinternet.cmsinterface.service.RedisManager;
import com.founder.mobileinternet.cmsinterface.service.cms.ActiveService;
import com.founder.mobileinternet.cmsinterface.util.CommonToolUtil;

@Service
public class ActiveServiceImpl implements ActiveService {
	private static final int CACHE_LENGTH = 200;
	private static final int COUNT = 20;

	@Autowired
	private RedisManager redisManager;
	@Autowired
	private Configure configure;

	// 活动列表
	@Override
	public String activityList(int siteID, int page, int lastFileId) {
		int start = page * COUNT;
		// 获得第几页
		int pageNo = (start / CACHE_LENGTH) * CACHE_LENGTH;
		// 拿到key
		String key = redisManager.getKeyBySite(RedisKey.APP_ACTIVITY_KEY, siteID) + pageNo;
		// 从redis中，获取值
		String value = redisManager.get(key);
		if (0 == start)
			lastFileId = 0;
		value = getArticles(siteID, start, pageNo, lastFileId, value, key, false);
		return value;
	}

	@Override
	public String activityDetail(int siteID, int fileId, int isDebug) {
		String key = RedisKey.APP_ACTIVITY_DETAIL_KEY + fileId;
		String value = redisManager.get(key);
		if (null == value) {
			String url = configure.getAppServerUrl() + "/activityDetail.do?fileId=" + fileId + "&siteID=" + siteID;
			CommonToolUtil.getData(url);
			value = redisManager.get(key);
		}

		if (null != value && !"".equals(value)) {
			JSONObject jsonObj = JSONObject.fromObject(value);
			if(!jsonObj.containsKey("fileId")){
				return value;
			}
			String str = redisManager.get(RedisKey.APP_ACTIVITY_ENTRYNUM+jsonObj.getString("fileId"));
			if (null != str && !"".equals(str)) {
				jsonObj.put("participatorNum", Integer.parseInt(str));
			}
			String countKey = RedisKey.NIS_EVENT_ACTIVITY +  jsonObj.getString("fileId");
			String countStr = redisManager.hget(countKey, "p");
			String countStrClick = redisManager.hget(countKey, "c");
            String strDiscuss = redisManager.hget(countKey, "d");//评论数
            if (null != countStr && !"".equals(countStr)) {
                jsonObj.put("countPraise", Integer.parseInt(countStr));
            }
            if (null != countStrClick && !"".equals(countStrClick)) {
                jsonObj.put("countClick", Integer.parseInt(countStrClick));
            }else{
                jsonObj.put("countClick", 0);
            }
            if (StringUtils.isNotBlank(strDiscuss)) {
                jsonObj.put("countDiscuss", Integer.parseInt(strDiscuss));
            }else{
                jsonObj.put("countDiscuss", 0);
            }
            value = jsonObj.toString();
		}
		return value;
	}


	@Override
	public String commitActivity(ActivityInfo activityInfo) {
		String url = configure.getAppServerUrl() + "/saveActivity.do";
		return CommonToolUtil.getBeanData(url, activityInfo);
	}

	String getNameListExist(int fileId) {
		String entrykey = RedisKey.APP_ACTIVITY_ENTRY + fileId;
		String value = redisManager.get(entrykey);

		if (null == value) {
			String entrylisturl = configure.getAppServerUrl() + "/activityEntryList.do?fileId=" + fileId;
			if (CommonToolUtil.canGetData(entrylisturl)) {
				value = redisManager.get(entrykey);
			}
		}

		return value;
	}

	@Override
	public String entryList(int siteID, int fileId) {

		String key = RedisKey.APP_ACTIVITY_ENTRY + fileId;
		String value = redisManager.get(key);
		String url = "/entryList.do?fileId=" + fileId + "&siteID=" + siteID;
		value = getDataResult(key, url);
		
		return value;

	}

	@Override
	public String getMyActivityList(int userID, int page, int siteID) {

		String key = RedisKey.MY_ENTRY_KEY + userID + "." + page;
		String value = getDataResult(key, "/myActivityList.do?userID=" + userID + "&siteID=" + siteID + "&page=" + page);

		JSONObject json = JSONObject.fromObject(value);
		JSONArray jsonArr = new JSONArray();
		JSONObject jsonObj = new JSONObject();
		if (value == null) {
			jsonObj.put("list", jsonArr.toString());
			return jsonObj.toString();
		}
		JSONArray jsonValueArr = json.getJSONArray("list");
		
	
		for (int i = 0; i < jsonValueArr.size(); i++) {
			JSONObject jsonObject = jsonValueArr.getJSONObject(i);
			if(jsonObject.isNullObject()){
				continue;
			}
			String countKey = RedisKey.NIS_EVENT_ACTIVITY + jsonObject.getString("fileId");
			String str = redisManager.hget(countKey, "p");
			if (null != str && !"".equals(str)) {
				jsonObject.put("countPraise", Integer.parseInt(str));
			}
			jsonArr.add(jsonObject);
		}
		jsonObj.put("list", jsonArr);
		value = jsonObj.toString();
		
		return value;
	}

	private String getArticles(int siteID, int start, int pageNo, int lastFileId, String value, String key,
			Boolean secondFind) {
		if (value == null) {
			String url = configure.getAppServerUrl() + "/activityList.do?start=" + pageNo + "&count=" + CACHE_LENGTH
					+ "&siteID=" + siteID;
			if (CommonToolUtil.canGetData(url)) {
				value = redisManager.get(key);
				
			}
		}
		if (value != null) {
			value = getValue(value, start, lastFileId, pageNo, siteID, false);
		} else if (secondFind)
			value = getSecondFindValue(siteID, start);
		JSONArray jsonArr = new JSONArray();
		if(value!=null){
			jsonArr = JSONArray.fromObject(value);
		}
		JSONArray json = new JSONArray();
		JSONObject jsonObj = new JSONObject();
	
		for (int i = 0; i < jsonArr.size(); i++) {
			JSONObject jsonObject = (JSONObject) jsonArr.get(i);
			String countKey = RedisKey.NIS_EVENT_ACTIVITY + jsonObject.getString("fileId");
            System.out.println(countKey);
			String str = redisManager.hget(countKey, "p");//点赞数
            String strClick = redisManager.hget(countKey, "c");//点击数
            String strDiscuss = redisManager.hget(countKey, "d");//评论数
			if (null != str && !"".equals(str)) {
				jsonObject.put("countPraise", Integer.parseInt(str));
			}
			if (StringUtils.isNotBlank(strClick)) {
                jsonObject.put("countClick", Integer.parseInt(strClick));
            }
            if (StringUtils.isNotBlank(strDiscuss)) {
                jsonObject.put("countDiscuss", Integer.parseInt(strDiscuss));
            }

	        String key1=RedisKey.APP_ACTIVITY_ENTRYNUM+jsonObject.getString("fileId");
			String str1 = redisManager.get(key1);
			if (null != str1 && !"".equals(str1)) {
				jsonObject.put("participatorNum", Integer.parseInt(str1));
			}
			json.add(jsonObject);
		}
		
		jsonObj.put("list", json);
		value = jsonObj.toString();
		return value;
	}

	private String getValue(String value, int start, int lastFileId, int pageNo, int siteID, Boolean secondFind) {
	
		JSONArray jsonArr = JSONArray.fromObject(value);
	
		int index = 0;
		if (lastFileId > 0) {
			index = getIndex(lastFileId, jsonArr);
			if (index > 0) { // 找到且不是最后一个
				value = getValByIndex(jsonArr, index);
			} else if (-1 == index) { // 找到且是当前list最后一个
				pageNo += CACHE_LENGTH;

				String key = redisManager.getKeyBySite(RedisKey.APP_ACTIVITY_KEY, siteID) + pageNo;
				value = redisManager.get(key);
				// 20160713 getArticles的参数lastFileId由0改成lastFileId
				value = getArticles(siteID, pageNo, pageNo, lastFileId, value, key, true);
			} else { // 在list中没找到lastID
				if (secondFind) {
					value = getSecondFindValue(siteID, start);
				} else {
					pageNo += CACHE_LENGTH;
					
					String key = redisManager.getKeyBySite(RedisKey.APP_ACTIVITY_KEY, siteID) + pageNo;
					value = redisManager.get(key);
					value = getArticles(siteID, start, pageNo, lastFileId, value, key, true);
				}
			}
		} else {
			index = start % CACHE_LENGTH;
			value = getValByIndex(jsonArr, index);
		}
	
		return value;
	}

	private int getIndex(int lastFileId, JSONArray jsonArr) {
		int size = jsonArr.size();
		for (int i = 0; i < size; i++) {
			JSONObject _jsonObj = (JSONObject) jsonArr.get(i);
			if (lastFileId == _jsonObj.getInt("fileId")) {
				if (i == (size - 1))
					return -1;
				return i + 1;
			}
		}
		return 0;
	}

	private String getValByIndex(JSONArray jsonArr, int index) {
		int size = jsonArr.size();
		JSONArray _jsonArr = new JSONArray();
		for (int i = index; i < size; i++) {
			_jsonArr.add(jsonArr.get(i));
			if (i + 1 - index == COUNT)
				break;
		}
		return _jsonArr.toString();
	}

	private String getSecondFindValue(int siteID, int start) {
		int pageNo = (start / CACHE_LENGTH) * CACHE_LENGTH;
		
		String key = redisManager.getKeyBySite(RedisKey.APP_ACTIVITY_KEY, siteID) + pageNo;
		String value = redisManager.get(key);
		JSONArray jsonArr = JSONArray.fromObject(value);
		int index = start % CACHE_LENGTH;
		return getValByIndex(jsonArr, index);
	}

	private String getDataResult(String key, String urlParam) {
		String value = redisManager.get(key);
		if (value == null) {
			String url = configure.getAppServerUrl() + urlParam;
			// System.out.println("没有缓存数据，重新获取。 请求地址：" + url);
			if (CommonToolUtil.canGetData(url))
				value = redisManager.get(key);
		}
		return value;
	}
}
