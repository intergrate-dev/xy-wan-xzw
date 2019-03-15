package com.founder.mobileinternet.cmsinterface.service.cms.impl;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.founder.mobileinternet.cmsinterface.pojo.Configure;
import com.founder.mobileinternet.cmsinterface.service.RedisKey;
import com.founder.mobileinternet.cmsinterface.service.RedisManager;
import com.founder.mobileinternet.cmsinterface.service.cms.ColumnService;
import com.founder.mobileinternet.cmsinterface.util.CommonToolUtil;

@Service
public class ColumnServiceImpl implements ColumnService {
	@Autowired
	private RedisManager redisManager;
	@Autowired
	private Configure configure;

    private static final Logger log = LoggerFactory
            .getLogger(LeaderServiceImpl.class);
	
	public String getColumnAll(int siteID, int columnID) {
		String key = redisManager.getKeyBySite(RedisKey.APP_COLLISTALL_KEY, siteID) + columnID;
		return getDataResult(key, "/getColumnsAll.do?siteID=" + siteID + "&colID=" + columnID);
	}
	
	public String getColumns(int siteID, int columnID) {
		String key = redisManager.getKeyBySite(RedisKey.APP_COLLIST_KEY, siteID) + columnID;
		return getDataResult(key, "/getColumns.do?siteID=" + siteID + "&colID=" + columnID);
	}
	
	public String getColumnIds(int siteID, String columnID) {
		String url = configure.getAppServerUrl() +"/getColumnIds.do?siteID=" + siteID + "&colID=" + columnID;
		return CommonToolUtil.getData(url);
	}

	public String getColumn(int siteID, int columnID) {
		String key = RedisKey.APP_COL_KEY + columnID;
		String value = redisManager.get(key);
		if (value == null) {
			String url = configure.getAppServerUrl() + "/getColumn.do?siteID=" + siteID + "&colID=" + columnID;
//			System.out.println("没有缓存数据，重新获取。  请求地址：" + url);
			if (CommonToolUtil.canGetData(url))
				value = redisManager.get(key);
		}
		if(null!=value){
			JSONObject jsonObject=JSONObject.fromObject(value);
			resetRssCount(jsonObject);
			value=jsonObject.toString();
		}
		return value;
	}
	
	private String getDataResult(String key, String urlParam){
		String value = redisManager.get(key);
		if (value == null) {
			String url = configure.getAppServerUrl() + urlParam;
//			System.out.println("没有缓存数据，重新获取。  请求地址：" + url);
            log.info("没有缓存数据，重新获取。  请求地址：" + url);
			if (CommonToolUtil.canGetData(url))
				value = redisManager.get(key);
            log.info("栏目获取请求后所得"+value);
		}
        if(null!=value){
            log.info("重置栏目订阅数");
			JSONObject jsonObj=JSONObject.fromObject(value);
			String str=jsonObj.getString("columns");
			if(null!=str&&!"".equals(str)) {
				JSONArray jsonArr = JSONArray.fromObject(str);
				JSONArray jsonArray=new JSONArray();
				for (int i = 0; i < jsonArr.size(); i++) {
					JSONObject jsonObject = (JSONObject) jsonArr.get(i);
					resetRssCount(jsonObject);
					jsonArray.add(jsonObject);
				}
				jsonObj.put("columns",jsonArray);
				return jsonObj.toString();
			}
            log.info("重置栏目订阅数完成");
		}

		return value;
	}
	private void resetRssCount(JSONObject jsonObject){
		String columnNum = redisManager.hget(RedisKey.NIS_EVENT_SUBSCRIBE_COLUMN, jsonObject.getString("columnId"));
		if(null==columnNum){
			String countUrl=configure.getAppServerUrl()+"/getColSubscribeCount.do?id="+jsonObject.getString("columnId");
			columnNum=CommonToolUtil.getData(countUrl);
		}
		if (!jsonObject.getString("rssCount").equals(columnNum)&&null!=columnNum ){
			if(Integer.parseInt(columnNum)<0){
				jsonObject.put("rssCount","0");
			}else{
				jsonObject.put("rssCount",columnNum);
			}
		}
	}

	@Override
	//为了获取最新数据，不查询缓存，直接调用后台服务
	public String getNodeTree() {
		String url = configure.getAppServerUrl() +"/getNodeTree.do?" ;
		String value = CommonToolUtil.getData(url);
		return value;
	}

	@Override
	public String getPubCols(int userID, String data){

		String url = configure.getNewAppServerUrl() + "/getPubCols.do?userID="+userID;
		String results = null;
		try {
			results = CommonToolUtil.getData(url,data);
		} catch (Exception e) {
			e.printStackTrace();
			results = CommonToolUtil.buildFailureArrayResult("请求内网接口失败");
		}

		return results;
	}

	@Override
	public String colIsOp(int userID, String data){

		String url = configure.getNewAppServerUrl() + "/colIsOp.do?userID="+userID;
		String results = null;
		try {
			results = CommonToolUtil.getData(url,data);
		} catch (Exception e) {
			e.printStackTrace();
			JSONObject result = new JSONObject();
			result.put("success", false);
			result.put("errorInfo", "请求内网接口失败");
			return result.toString();
		}

		return results;
	}
	
	@Override
	public String colSearch(int userID, String data) {
		String url = configure.getNewAppServerUrl() + "/colSearch.do?userID="+userID;
		String results = null;
		try {
			results = CommonToolUtil.getData(url,data);
		} catch (Exception e) {
			e.printStackTrace();
			results = CommonToolUtil.buildFailureArrayResult("请求内网接口失败");
		}
		return results;
	}

	@Override
	public String getAuditCols(int userID, String data){

		String url = configure.getNewAppServerUrl() + "/getAuditCols.do?userID="+userID;
		String results = null;
		try {
			results = CommonToolUtil.getData(url,data);
		} catch (Exception e) {
			e.printStackTrace();
			results = CommonToolUtil.buildFailureArrayResult("请求内网接口失败");
		}

		return results;
	}

	@Override
	public String getColumnsAllEasy(String data) {
        String result = "";
	    String siteID = "1";
	    String parentID = "0";
	    try {
            JSONObject dataJson = JSONObject.fromObject(data);
            siteID = dataJson.getString("siteID");
            parentID = dataJson.getString("parentID");
        }catch (Exception e){
	        e.printStackTrace();
            result = "1";
	        return result;
        }

		String key = RedisKey.WEB_COLLISTALL_KEY + siteID;
        result = redisManager.hget(key,parentID);
		if(StringUtils.isBlank(result)){
			String url = configure.getAppServerUrl()+"/getColumnsAllEasy.do?siteID="+siteID+"&parentID="+parentID;
			if (CommonToolUtil.canGetData(url)) {
                result = redisManager.hget(key,parentID);
			}else{
                result = "2";
			}
		}

		return result;
	}

}