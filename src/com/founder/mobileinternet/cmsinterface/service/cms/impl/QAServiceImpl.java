package com.founder.mobileinternet.cmsinterface.service.cms.impl;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.founder.mobileinternet.cmsinterface.pojo.Configure;
import com.founder.mobileinternet.cmsinterface.pojo.cms.NisQaInfo;
import com.founder.mobileinternet.cmsinterface.service.RedisKey;
import com.founder.mobileinternet.cmsinterface.service.RedisManager;
import com.founder.mobileinternet.cmsinterface.service.cms.QAService;
import com.founder.mobileinternet.cmsinterface.util.CommonToolUtil;

/**
 * Created by isaac_gu on 2016/10/24.
 */
@Service
public class QAServiceImpl implements QAService {

    @Autowired
	private RedisManager redisManager;
    @Autowired
    private Configure configure;

    @Override
    public String qaList(int siteID, int groupId, int lastFileId,int page) {
      //拿到key
        String key = redisManager.getKeyBySite(RedisKey.APP_QALIST_KEY, siteID) + groupId + "." + page;
		String url = configure.getAppServerUrl() + "/qaList.do?groupId=" + groupId
				+ "&page=" + page + "&siteID=" + siteID;
        if(0 == page) lastFileId = 0;
		String value =CommonToolUtil.getDataResult(key, url,redisManager,configure);
		
		if (page > 0 && lastFileId > 0 && null != value) {
			value = CommonToolUtil.listFilter(value, lastFileId, null, "fileId");
		}
		return CommonToolUtil.refreshCounts(RedisKey.NIS_EVENT_QA, value,redisManager);
    }

    //问政提交
	@Override
	public boolean commitPolitics(NisQaInfo nisQaInfo) {
		ObjectMapper objectMapper = new ObjectMapper();
		try{
			String value=objectMapper.writeValueAsString(nisQaInfo);
			redisManager.addDelayQA(value);
		}catch (Exception e){
			e.printStackTrace();
		}

		return true;
	}
    //问政详情
	@Override
	public String qaDetail(int siteID, int fileId) {
		String key= RedisKey.APP_QA_KEY + fileId;
		String url = configure.getAppServerUrl() + "/qaDetail.do?fileId="+fileId+"&siteID="+siteID;
		String value=CommonToolUtil.getDataResult(key,url,redisManager,configure);
		if(null!=value) {
			value=CommonToolUtil.refreshCount(RedisKey.NIS_EVENT_QA, JSONObject.fromObject(value),redisManager);
		}
		return value;
	}
	//我的问政
	@Override
	public String myQA(int userID, int page, int siteID) {
		String key= RedisKey.MY_QA_KEY+userID+"."+page;
		String url = configure.getAppServerUrl() + "/myQA.do?userID="+userID+"&page="+page+"&siteId"+siteID;
		String value=CommonToolUtil.getDataResult(key,url,redisManager,configure);
		
		value = CommonToolUtil.refreshCounts(RedisKey.NIS_EVENT_QA, value,redisManager) ;
		JSONObject redisJson = new JSONObject() ;
		redisJson.put("list", JSONArray.fromObject(value)) ;
		return redisJson.toString();
	}
}
