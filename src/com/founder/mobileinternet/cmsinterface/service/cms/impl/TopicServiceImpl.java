package com.founder.mobileinternet.cmsinterface.service.cms.impl;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.founder.mobileinternet.cmsinterface.pojo.Configure;
import com.founder.mobileinternet.cmsinterface.pojo.cms.TopicInfo;
import com.founder.mobileinternet.cmsinterface.service.RedisKey;
import com.founder.mobileinternet.cmsinterface.service.RedisManager;
import com.founder.mobileinternet.cmsinterface.service.cms.TopicService;
import com.founder.mobileinternet.cmsinterface.util.CommonToolUtil;

@Service
public class TopicServiceImpl implements TopicService {
	@Autowired
	private RedisManager redisManager;
	@Autowired
	private Configure configure;

	public boolean topicSub(TopicInfo topicInfo) {
		String url = configure.getAppServerUrl() + "/topicSub.do";
		return CommonToolUtil.postBeanData(url, topicInfo);
	}
	public boolean topicSubCancel(TopicInfo topicInfo) {
		String url = configure.getAppServerUrl() + "/topicSubCancel.do";
		return CommonToolUtil.postBeanData(url, topicInfo);
	}
	
	@Override
	public String subcribeView(int siteID, long userID, int columnId, int count, String device) {
		JSONArray jsonArray = getSubsList(columnId, siteID, userID, count,  device);
		return jsonArray.toString();
	}
	
	public String subcribeViewV51(int siteID, long userID, int columnId, int count, String device) {
		JSONArray jsonArray = getSubsList(columnId, siteID, userID, count, device);

		//加推荐模块
        String modules = "[]";
        if(columnId!=0){
            modules = getColumnModules(columnId, siteID);
        }

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("list",jsonArray);
        jsonObject.put("modules", modules);
        
		return jsonObject.toString();
	}
	@Override
	public String getSubcribeCols(int siteID, String key, long colID) {
		String url = configure.getAppServerUrl()+"/getSubcribeCols.do?siteID="+siteID
				+"&key="+key+"&colID="+colID;
		return CommonToolUtil.getData(url);
	}

	@Override
	public String subcribeXY(int siteID, int columnId, long userID, String device) {
		JSONArray jsonArray = getSubsIDList(columnId, siteID, userID, device);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("list",jsonArray);
		return jsonObject.toString();
	}

    private JSONArray getSubsIDList(int columnId, int siteID, long userID, String device) {
        String url = configure.getAppServerUrl()+"/subcribeXY.do?siteID="+siteID
                +"&userID="+userID+"&columnId="+columnId+"&device="+device;
        String info = CommonToolUtil.getData(url);

        JSONArray jsonArray = JSONArray.fromObject(info);

        return jsonArray;

    }

    private JSONArray getSubsList(int columnId, int siteID, long userID, int count, String device) {
		String url = configure.getAppServerUrl()+"/subcribeView.do?siteID="+siteID
				+"&userID="+userID+"&columnId="+columnId+"&count="+count+"&device="+device;
		String info = CommonToolUtil.getData(url);

		JSONArray data = JSONArray.fromObject(info);
		return data;
		
//		JSONArray jsonArray = new JSONArray();
//		try {
//			JSONArray data = JSONArray.fromObject(info);
//			for(int i=0;i<data.size();i++){
//				JSONObject jsonObj = (JSONObject) data.get(i);
//				JSONArray list = (JSONArray)jsonObj.get("list");
//				jsonObj.put("list", ArticleCountHelper.setArticlesCount(redisManager, list));
//				jsonArray.add(jsonObj);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return jsonArray;
	}
	private String getColumnModules(int columnID, int siteID) {
    	String key = RedisKey.APP_MODULE_LIST + columnID;
    	String value = redisManager.get(key);
    	
        if (value == null) {
            String url = configure.getAppServerUrl() + "/getColumnModules.do?colID=" + columnID
            		+ "&siteID=" + siteID;
            if (CommonToolUtil.canGetData(url)) {
                value = redisManager.get(key);
            }
        }
        if (value == null)
        	value = "[]";
        return value;
    }
}
