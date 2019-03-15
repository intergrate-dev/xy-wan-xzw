package com.founder.mobileinternet.cmsinterface.service.cms.impl;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.founder.mobileinternet.cmsinterface.pojo.Configure;
import com.founder.mobileinternet.cmsinterface.pojo.cms.CommentInfo;
import com.founder.mobileinternet.cmsinterface.service.RedisKey;
import com.founder.mobileinternet.cmsinterface.service.RedisManager;
import com.founder.mobileinternet.cmsinterface.service.cms.CommentService;
import com.founder.mobileinternet.cmsinterface.util.CommonToolUtil;

@Service
public class CommentServiceImpl implements CommentService {
	@Autowired
	private RedisManager redisManager;
	@Autowired
	private Configure configure;
	private static final int COUNT = 20;

	public boolean commitComment(CommentInfo commentInfo) {
		if (redisManager.sismember(RedisKey.APP_NIS_SHUTUP_USER_KEY, String.valueOf(commentInfo.getUserID()))) {
			return false;
		}

		if (redisManager.sismember(RedisKey.APP_NIS_SHUTUP_IP_KEY, String.valueOf(commentInfo.getIpaddress()))) {
			return false;
		}
		JSONObject discussJson = JSONObject.fromObject(commentInfo);
		redisManager.addDelayDiscuss(discussJson.toString());
		return true;
	}

	public String discussHot(int articleId, int count, int source, int lastFileId, int siteID) {
		String key = RedisKey.APP_DISCUSS_HOT_KEY + source + "." + articleId;
		String urlParam = "/discussHot.do?id=" + articleId + "&count=" + count + "&source=" + source + "&siteID="
				+ siteID;
		String value = getDataResult(key, urlParam);
		return value;
	}

	public String discussView(int articleId, int source, int lastFileId, int siteID, int page, int flat) {
		String key = RedisKey.APP_DISCUSS_VIEW_KEY + source + "." + articleId + "." + page;
		String urlParam = "/discussView.do?id=" + articleId
		// + "&type=" + type+ "&start=" + start + "&count=" + count
				+ "&source=" + source + "&siteID=" + siteID + "&page=" + page + "&flat=" + flat;
		String value = getDataResult(key, urlParam);
		if (page > 0 && lastFileId > 0 && null != value) {
			value = CommonToolUtil.listFilter(value, lastFileId, "list", "id");
		}
		return value;
	}

    public String discussViewOrderByPraise(int articleId, int source, int lastFileId, int siteID, int page, int isOrderByPraise) {
        String key = RedisKey.APP_DISCUSS_VIEW_KEY + source + "." + articleId + "." + page + "." + isOrderByPraise;
        String urlParam = "/discussViewOrderByPraise.do?id=" + articleId
                // + "&type=" + type+ "&start=" + start + "&count=" + count
                + "&source=" + source + "&siteID=" + siteID + "&page=" + page + "&isOrderByPraise=" + isOrderByPraise;
        String value = getDataResult(key, urlParam);
        if (page > 0 && lastFileId > 0 && null != value) {
            value = CommonToolUtil.listFilter(value, lastFileId, "list", "id");
        }
        return value;
    }

	public String discussReply(int id, int lastFileId, int siteID, int page) {
		String key = RedisKey.APP_DISCUSS_REPLY_KEY + id + "." + page;
		String urlParam = "/discussReply.do?id=" + id
		// + "&start=" + start + "&count=" + count
				+ "&siteID=" + siteID + "&page=" + page;
		String value = getDataResult(key, urlParam);
		if (page > 0 && lastFileId > 0 && null != value) {
			value = CommonToolUtil.listFilter(value, lastFileId, "list", "id");
		}
		return value;
	}

	public String discussCount(int articleId, int type, int source, int siteID) {
		String urlParam = "/getDiscussCount.do?id=" + articleId 
				+ "&type=" + type + "&source=" + source + "&siteID=" + siteID;
		String url = configure.getAppServerUrl() + urlParam;

		return CommonToolUtil.getData(url);
	}

	public String getMyDiscuss(int userID, int page, int siteID) {
		
		String key = redisManager.getKeyBySite(RedisKey.MY_DISCUSS_KEY,siteID) + userID;
		String value=getListDataResult(key, "/myDiscuss.do?userID=" + userID + "&siteID=" + siteID,page,siteID);
		return value;
	}
	public String getMyDiscussReply(int userID, int page, int siteID) {
		
		String key = RedisKey.MY_REPLY_KEY+ userID;
		String value=getListDataResult(key, "/myDiscussReply.do?userID=" + userID + "&siteID=" + siteID ,page,siteID);
		return value;
	}
	@Override
	public boolean expose(int siteID, int id, int userID, String userName, String reason,int sourceType,int type) {
		JSONObject discussJson = new JSONObject();
		discussJson.put("siteID", siteID);
		discussJson.put("rootID", id);
		discussJson.put("userID", userID);
		discussJson.put("userName", userName);
		discussJson.put("reason", reason);
		discussJson.put("sourceType", sourceType);
		discussJson.put("type", type);
		redisManager.addDelayExpose(discussJson.toString());
	
		return true;
	}

	private String getDataResult(String key, String urlParam) {
		String value = redisManager.get(key);
		if (value == null) {
			String url = configure.getAppServerUrl() + urlParam;
			// System.out.println("没有缓存数据，重新获取。 请求地址：" + url);
			if (CommonToolUtil.canGetData(url))
				value = redisManager.get(key);
		}
		if (null != value)
			value = getRedisCountJsonStr(value);
		return value;
	}

	private String getRedisCountJsonStr(String value) {
		JSONObject jsonObj = JSONObject.fromObject(value);
		jsonObj.put("list", setCount(jsonObj.getJSONArray("list")));
		return jsonObj.toString();
	}

	private JSONArray setCount(JSONArray jsonArr) {
		int size = jsonArr.size();
		for (int i = 0; i < size; i++) {
			JSONObject jsonObj = (JSONObject) jsonArr.get(i);
			
			String countKey = RedisKey.NIS_EVENT_DISCUSS + jsonObj.getString("id");
			
			//取Redis中的点赞数
			int redisCount = CommonToolUtil.getIntValue(redisManager.hget(countKey, "p"), 0);
			int jsonCount = CommonToolUtil.getIntValue(jsonObj.getString("countPraise"), 0);
			if (redisCount > jsonCount) {
				jsonObj.put("countPraise", Long.valueOf(redisCount));
			}
			//取Redis中的评论数
			redisCount = CommonToolUtil.getIntValue(redisManager.hget(countKey, "d"), 0);
			jsonCount = CommonToolUtil.getIntValue(jsonObj.getString("countDiscuss"), 0);
			if (redisCount > jsonCount) {
				jsonObj.put("countDiscuss", Long.valueOf(redisCount));
			}
			
			if (jsonObj.containsKey("topDiscuss")) // 评论的评论也要加count
				setCount(jsonObj.getJSONObject("topDiscuss").getJSONArray("list"));
		}
		return jsonArr;
	}

	private String getListDataResult(String key, String urlParam, int page,int siteID){
		long start = page * COUNT;
		long end = (page + 1) * COUNT - 1;
		
		List<String> value = null;
		
		//先从redis的list里读，无则读内网
		long size = redisManager.llen(key);
		if (size == 0) {
			String url = configure.getAppServerUrl() + urlParam;
			if (CommonToolUtil.canGetData(url))
				value = redisManager.lrange(key, start, end);
		} else {
			value = redisManager.lrange(key, start, end);
		}
		
		JSONArray jsonArr = new JSONArray();
		if (value != null && value.size() > 0) {
			JSONObject jsonOne = JSONObject.fromObject(value.get(0));
			if (page == 0 && (jsonOne.isEmpty() || jsonOne.isNullObject())) {
				value.remove(0);
			}

			if(value.size()>0){
				JSONObject jsonLastOne = JSONObject.fromObject(value.get(value.size()-1));
				if(page==0&&(jsonLastOne.isEmpty()||jsonLastOne.isNullObject())){
					value.remove(value.get(value.size()-1));
				}
			}
			for (String discuss : value) {
				JSONObject jsonObj = JSONObject.fromObject(discuss);
				jsonArr.add(jsonObj);
			}
        }
		
		JSONObject disList= new JSONObject();
		disList.put("list", jsonArr.toString());
		return disList.toString();
	}
	
	@Override
	public boolean discussDelete(int userID, int discussID, int siteID) {
		JSONObject discussJson = new JSONObject();
		discussJson.put("userID", userID);
		discussJson.put("siteID", siteID);
		discussJson.put("discussID", discussID);
		
		String url = configure.getAppServerUrl() + "/discussDelete.do";
		try {
			return CommonToolUtil.postData(url, discussJson.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
}