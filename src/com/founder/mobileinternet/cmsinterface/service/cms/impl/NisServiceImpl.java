package com.founder.mobileinternet.cmsinterface.service.cms.impl;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.founder.mobileinternet.cmsinterface.pojo.Configure;
import com.founder.mobileinternet.cmsinterface.pojo.cms.EventInfo;
import com.founder.mobileinternet.cmsinterface.pojo.cms.FeedInfo;
import com.founder.mobileinternet.cmsinterface.pojo.cms.TipoffInfo;
import com.founder.mobileinternet.cmsinterface.service.RedisKey;
import com.founder.mobileinternet.cmsinterface.service.RedisManager;
import com.founder.mobileinternet.cmsinterface.service.cms.NisService;
import com.founder.mobileinternet.cmsinterface.util.CommonToolUtil;

@Service
public class NisServiceImpl implements NisService {
	@Autowired
	private Configure configure;
    @Autowired
    private RedisManager redisManager;
	private static final int COUNT = 20;

	public boolean commitTipoff(TipoffInfo forumInfo) {
		String url = configure.getAppServerUrl() + "/tipoff.do";
		return CommonToolUtil.postBeanData(url, forumInfo);
	}

	public String myTipoff(int userID, int page, int siteID) {
		String key = redisManager.getKeyBySite(RedisKey.MY_TIPOFF_KEY,siteID)+ userID;
		JSONArray jsonArr = getListDataResult(key,
				"/myTipoff.do?userID="
						+ userID + "&siteID=" + siteID, page);
		return jsonArr.toString();
	}

	@Override
	public String tipoffContent(int docID, int siteID) {
		String key =RedisKey.APP_TIPOFF_KEY+ docID;

		String url =configure.getAppServerUrl()+"tipoffContent.do?docID="+ docID + "&siteID=" + siteID;
		String value = redisManager.get(key);
		if (value == null) {
			if (CommonToolUtil.canGetData(url)) {
				value = redisManager.get(key);
			}
		}else{
			return value;
		}


		return value;
	}

	public boolean commitFeed(FeedInfo feedInfo) {
		String url = configure.getAppServerUrl() + "/feed.do";
		return CommonToolUtil.postBeanData(url, feedInfo);
	}
    public boolean event(EventInfo eventInfo) {
	    String url = configure.getAppServerUrl() + "/event.do";
	    return CommonToolUtil.postBeanData(url, eventInfo);
	}

	/**
     * 小红点
     *
     */
    @Override
    public String redDot(Integer userId) {
        JSONObject result = new JSONObject();
        // 获得消息树最新的系统时间
        String InfoPublishTime = redisManager.get(RedisKey.RED_DOT_MESSAGE);
        result.put("messageTime", InfoPublishTime == null ? "0" : InfoPublishTime);
        if (userId != null && userId > 0) {
            //得到订阅的栏目ID
            String records=redisManager.hget(RedisKey.MY_COLUMN_KEY,String.valueOf(userId));
			if (null == records) {
                String url=configure.getAppServerUrl()+"myColumnIDs.do?userID="+userId;
                if(CommonToolUtil.canGetData(url))
                    records=redisManager.hget(RedisKey.MY_COLUMN_KEY,String.valueOf(userId));
            }
            //得到订阅栏目的最新发布时间
            JSONArray jsonArr=new JSONArray();
            if(null!=records&&!"".equals(records)){
                String[] ids = records.split(",");
                for(int i=0;i<ids.length;i++){
                    JSONObject jsonObj=new JSONObject();
                    jsonObj.put("colID",ids[i]);
                    jsonObj.put("time",redisManager.hget(RedisKey.RED_DOT_ARTICLE,ids[i]));
                    jsonArr.add(jsonObj);
                }
            }
            result.put("columns",jsonArr);
            //我的评论是否有新回复
            setState(result,RedisKey.RED_DOT_DISCUSS,userId,"discussReplay");
            //我的话题提问是否有新回复
            setState(result,RedisKey.RED_DOT_SUBJECTQA,userId,"subjectQA");
            //我的问答提问是否有新回复
            setState(result,RedisKey.RED_DOT_QA,userId,"qa");
            //我报名的活动是否有新状态
            setState(result,RedisKey.RED_DOT_ACTIVITY,userId,"entry");

        }

        return result.toString();
    }
    // type（0读评论，1读话题提问，2读提问，3读活动）
    public boolean read(int userId,int type){
        switch (type){
            case 0:
                redisManager.clear(RedisKey.RED_DOT_DISCUSS);
                break;
            case 1:
                redisManager.clear(RedisKey.RED_DOT_SUBJECTQA);
                break;
            case 2:
                redisManager.clear(RedisKey.RED_DOT_QA);
                break;
            case 3:
                redisManager.clear(RedisKey.RED_DOT_ACTIVITY);
                break;
        }
        return true;
    }
    public String getMyFav(int userID, int page, int siteID) {
		String key = redisManager.getKeyBySite(RedisKey.MY_FAVORITE_KEY,siteID)+ userID;
		System.out.println(key);
		JSONArray jsonArr = getListDataResult(key, 
				"/myFav.do?userID=" + userID + "&siteID=" + siteID, page);
		
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("list", jsonArr);
		return jsonObj.toString();
	}

	public boolean hasFav(int userID, long articleID, int type, int siteID){
		String key = redisManager.getKeyBySite(RedisKey.MY_FAVORITE_KEY,siteID)+ userID;
		long redisCount = redisManager.llen(key);
		if(redisCount==0){

			String url = configure.getAppServerUrl() +  "/myFav.do?userID=" + userID + "&siteID=" + siteID;
//			System.out.println("没有缓存数据，重新获取。  请求地址：" + url);
			if (CommonToolUtil.canGetData(url));
		}
		List<String> value = redisManager.lrange(key, 0, 100);
		
		for (String object : value) {
			if(object==null) continue;
			JSONObject obj = JSONObject.fromObject(object);
			if(!obj.containsKey("articleID"))continue;
			long redisArticleID=obj.getLong("articleID");
			int redisType=obj.getInt("type");
				if(redisArticleID==articleID&&redisType==type){
					return true;
				}
		}
		return false;
	}

	public String getCounts(long id, int source, int siteID) {
		//按source区分应读的互动计数Key，若Redis中无则调用内网API读入
		String key = getCountKey(id, source);
		if (!redisManager.exists(key)) {
			String urlParam = "/getCounts.do?id=" + id + "&source=" + source;
			String url = configure.getAppServerUrl() + urlParam;
			CommonToolUtil.getData(url);
		}
		
		//从Redis的hash中读出需要的计数
		JSONObject json = new JSONObject();
		json.put("countClick", getCount(key, "c"));
		json.put("countPraise", getCount(key, "p"));
		json.put("countDiscuss", getCount(key, "d"));
		
		//若是稿件，点击数要加初始阅读数、分享点击数
		if (source == 0) {
			//读Redis中的稿件详情json
			String akey = RedisKey.APP_ARTICLE_KEY + id;
			if (!redisManager.exists(akey)) {
				String urlParam = "/getArticle.do?docID=" + id + "&siteID=" + siteID;
				String url = configure.getAppServerUrl() + urlParam;
				CommonToolUtil.getData(url);
			}
			String article = redisManager.get(akey);
			if (article != null) {
				JSONObject jsonArticle = JSONObject.fromObject(article);
				int countClick = ArticleCountHelper.calArticleCountClick(redisManager, jsonArticle, id);
				json.put("countClick", countClick);
			}
		}
		return json.toString();
	}

	private String getCountKey(long id, int source) {
		String key = null;
		
		switch (source) {
		case 0:
			key = RedisKey.NIS_EVENT_ARTICLE + id;
			break;
		case 1:
			key = RedisKey.NIS_EVENT_LIVE + id;
			break;
		case 3:
			key = RedisKey.NIS_EVENT_PAPERARTICLE + id;
			break;
		case 4:
			key = RedisKey.NIS_EVENT_SUBJECTQA + id;
			break;
		case 5:
			key = RedisKey.NIS_EVENT_QA + id;
			break;
		case 6:
			key = RedisKey.NIS_EVENT_ACTIVITY + id;
			break;
		default:
			key = null;
		}
		return key;
	}
	
	private int getCount(String key, String field) {
		try {
			String count = redisManager.hget(key, field);
			if (count == null) return 0;
			
			return Integer.parseInt(count);
		} catch (NumberFormatException e) {
			return 0;
		}
	}
	
	private void setState(JSONObject result,String key,Integer userId,String name) {
        boolean flag =redisManager.sismember(key,userId.toString());
        result.put(name,flag);
    }

	private JSONArray getListDataResult(String key, String urlParam, int page){
		long start=page*COUNT;
		long end=(page+1)*COUNT-1;
		
		JSONArray jsonArr = new JSONArray();
		long size = redisManager.llen(key);
		List<String> value = null;
		if (size == 0) {
			String url = configure.getAppServerUrl() + urlParam;
			if (CommonToolUtil.canGetData(url))
				value = redisManager.lrange(key, start, end);
		} else {
			value = redisManager.lrange(key, start, end);
		}
		
		if (null == value) {
			return jsonArr;
		}else{
			if(value.size()>0){
				JSONObject jsonOne = JSONObject.fromObject(value.get(0));
				if(page==0&&(jsonOne.isEmpty()||jsonOne.isNullObject())){
					value.remove(0);
				}
				if(value.size()>0) {
					JSONObject jsonLastOne = JSONObject.fromObject(value.get(value.size() - 1));
					if (page == 0 && (jsonLastOne.isEmpty() || jsonLastOne.isNullObject())) {
						value.remove(value.get(value.size() - 1));
					}
				}
			}
			for (String discuss : value) {
				JSONObject jsonObj = JSONObject.fromObject(discuss);
				jsonArr.add(jsonObj);
			}
        }
		return jsonArr;
	}
}
