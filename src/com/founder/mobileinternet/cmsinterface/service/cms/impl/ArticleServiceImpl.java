package com.founder.mobileinternet.cmsinterface.service.cms.impl;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.JarURLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import net.sf.json.JsonConfig;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.founder.mobileinternet.cmsinterface.pojo.Configure;
import com.founder.mobileinternet.cmsinterface.service.RedisKey;
import com.founder.mobileinternet.cmsinterface.service.RedisManager;
import com.founder.mobileinternet.cmsinterface.service.cms.ArticleService;
import com.founder.mobileinternet.cmsinterface.util.CommonToolUtil;

@Service
public class ArticleServiceImpl implements ArticleService {

    private static final int CACHE_LENGTH = 200;
    private static final int COUNT = 20;

    @Autowired
    private RedisManager redisManager;
    @Autowired
    private Configure configure;

    public String getColumnArticles(int columnID, int start, int count,
            int lastFileId, int siteID,int typeScreen) {

        int S = (start / CACHE_LENGTH) * CACHE_LENGTH;
        if (0 == start) lastFileId = 0;

        JSONArray jsonArr = getArticles(columnID, start, lastFileId, S, false, siteID,typeScreen);

        int jsonLength = jsonArr.size();
        if(jsonLength<10){
            JSONArray articleArray = new JSONArray();
            for (int i=0; i<jsonLength; i++) {
                JSONObject jsonObj = (JSONObject) jsonArr.get(i);
                if (null != jsonObj && !jsonObj.isEmpty() && !"null".equals(jsonObj.toString())) {
                    try {
                        jsonObj.getString("fileId");
                        articleArray.add(jsonObj);
                    }catch (Exception e){
                        jsonArr.clear();
                        jsonArr.addAll(articleArray);
                        break;
                    }
                }
            }
        }

	    //改写点击数等
	    rewriteCount(jsonArr);

        //加推荐模块
        String modules = getColumnModules(columnID, siteID);

        JSONObject jsonObj = new JSONObject();
	    jsonObj.put("list", jsonArr);
        jsonObj.put("modules", modules);

        //增加广告
        setAdv(jsonObj,columnID);

        return jsonObj.toString();
    }

    private void setAdv(JSONObject jsonObj,int columnID){
        String key = RedisKey.ADV_COLUMN_LIST_KEY + columnID;
        String value = getDataResult(key, "/getAdvs.do?columnID="+columnID, 0);
        if(null==value){
            jsonObj.put("adv","[]");
        }else{
            String newAdvs=screenAdvs(value);
            jsonObj.put("adv",newAdvs);
        }
    }

    private String screenAdvs(String value){
        JSONArray jsonArr=JSONArray.fromObject(value);
        JSONArray newJson=new JSONArray();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for(int i=0;i<jsonArr.size();i++){
            JSONObject jsonObject=(JSONObject) jsonArr.get(i);
            if(jsonObject.containsKey("endTime")){
            try{
                //筛选过期广告
                if(df.parse(df.format(new Date())).getTime()<
                        df.parse(jsonObject.getString("endTime")).getTime()){
                    newJson.add(jsonObject);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            }
        }
        return newJson.toString();
    }


    public String articleHot(int siteID, int lastFileId, int start, int type) {

        String key = redisManager.getKeyBySite(RedisKey.APP_ARTICLELIST_HOT_KEY, siteID) + type;
        String value = redisManager.get(key);

        JSONObject result = rewrite(value);
        return result.toString();
    }

    public String getArticleContent(int articleId, int colID, int siteID) {
        String key = RedisKey.APP_ARTICLE_KEY + articleId;
        String value = getDataResult(key, "/getArticle.do?docID=" + articleId
                + "&colID=" + colID + "&siteID=" + siteID, colID);


        String colKey = RedisKey.APP_COL_KEY + colID;
        String colValue = getColumnResult(colKey, "/getColumn.do?siteID=" + siteID + "&colID=" + colID);

        //可能相关稿件也需要替换url
        if (value != null) {
	        JSONArray datas = new JSONArray();
	        datas.add(JSONObject.fromObject(value));
	        value = ArticleCountHelper.setArticlesCount(redisManager, datas).get(0).toString();
	        JSONArray related = JSONObject.fromObject(value).getJSONArray("related") ;
	        related = ArticleCountHelper.setArticlesCount(redisManager, related);
	        JSONObject obj = JSONObject.fromObject(value) ;
	        obj.put("related", related);
            if(null!=colValue){
                JSONObject colJson = JSONObject.fromObject(colValue);
                String presentColumnName = String.valueOf(colJson.get("columnName"));
                obj.put("presentColumnName", presentColumnName);
                String presentColumnStype = String.valueOf(colJson.get("columnStyle"));
                obj.put("presentColumnStype", presentColumnStype);
            }

	        value = obj.toString() ;
        }
        return value;
    }

    private String getColumnResult(String key, String urlParam) {
        String value = redisManager.get(key);
        if (value == null) {
            String url = configure.getAppServerUrl() + urlParam;
            if (CommonToolUtil.canGetData(url)) {
                value = redisManager.get(key);
            }
        }

        return value;
    }

    public String search(
            int columnID, String key, int start, int count,
            int siteID) {
        String url = configure.getAppServerUrl() + "/search.do?colID="
                + columnID + "&key=" + key + "&start=" + start + "&count="
                + count + "&siteID=" + siteID;
        return CommonToolUtil.getData(url);
    }

    public String searchAll(
            int columnID, String key, int start, int count,
            int siteID) {
        String url = null;
        try {
            url = configure.getAppServerUrl() + "/searchAll.do?colID="
                    + columnID + "&key=" + URLEncoder.encode(key, "UTF-8") + "&start=" + start + "&count="
                    + count + "&siteID=" + siteID;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return CommonToolUtil.getData(url);
    }

    @Override
    public String searchWebArticles(
            int columnID, String key, int start, int count,
            int siteID) {
        String url = null;
        try {
            url = configure.getAppServerUrl() + "/searchWebArticles.do?colID="
                    + columnID + "&key=" + URLEncoder.encode(key, "UTF-8") + "&start=" + start + "&count="
                    + count + "&siteID=" + siteID;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return CommonToolUtil.getData(url);
    }

    @Override
    public String getAuthorArticles(int id, int start, int count, int lastFileId, int siteID) {
        String key = RedisKey.APP_AUTHOR_ARTICLES_KEY + id + "." + start;

        String value = redisManager.get(key);
        if (value == null) {
            String url = configure.getAppServerUrl() + "/authorArticles.do?id="
                    + id + "&start=" + start + "&count=" + count + "&siteID=" + siteID;
            //System.out.println("没有缓存数据，重新获取   请求地址：" + url);
            if (CommonToolUtil.canGetData(url))
                value = redisManager.get(key);
        }
        if (value != null) {
            if (start > 0 && lastFileId > 0)
                value = CommonToolUtil.listFilter(value, lastFileId, null, "fileId");
            value = ArticleCountHelper.setArticlesCount(redisManager, JSONArray.fromObject(value)).toString();
        }
        return value;
    }

    //栏目页翻页
    public String articleListRefresh(int coID, int colLibID, long colID, int page, int siteID) {
        //由于栏目页要按照指定的栏目ID做redis的key，比较复杂，所以外网api不从redis读，都访问内网api

        String url = configure.getAppServerUrl();
        url = url.substring(0, url.length() - 4);
        url += "/articleListRefresh.do?coID=" + coID + "&colLibID="
                + colLibID + "&colID=" + colID + "&page=" + page + "&siteID=" + siteID;

        return CommonToolUtil.getData(url);
    }

    //标签检索
    public String tagArticles(String tag, int page, int siteID) {
        String url = configure.getAppServerUrl();
        //url = url.substring(0, url.length() - 4);
        url += "/tagArticles.do?tag=" + tag + "&page=" + page + "&siteID=" + siteID;

        return CommonToolUtil.getData(url);
    }

    public String authorArticles(String author, int page, int siteID) {
        String url = configure.getAppServerUrl();
        url = url.substring(0, url.length() - 4);
        url += "/authorArticles.do?author=" + author + "&page=" + page + "&siteID=" + siteID;

        return CommonToolUtil.getData(url);
    }

    public String tradeArticles(String tradeIDs, int page, int siteID){
        String url = configure.getAppServerUrl();
        url = url.substring(0, url.length() - 4);
        url += "/tradeArticles.do?tradeIDs=" + tradeIDs + "&page=" + page + "&siteID=" + siteID;

        return CommonToolUtil.getData(url);
    }

    public String getModuleView(String id, int userID){
		String key = RedisKey.APP_MODULEITEM_LIST + id;
		String value = redisManager.get(key);

		if (null == value) {
			String url = configure.getAppServerUrl() + "/moduleView.do?id=" + id;
			if (CommonToolUtil.canGetData(url)) {
				value = redisManager.get(key);
			}
		}
		if (value != null) {
			value = moduleRevise(value, userID) ;
		}
		return value;
	}

    private String getDataResult(String key, String urlParam, int colID) {
        String value = redisManager.get(key);
        if (value == null) {
            value = getValByCmsApi(key, urlParam);
        } else {
            if (colID > 0) // 稿件内容详情
                value = getArticleValue(key, urlParam, colID, value);
        }
        return value;
    }

    private String getArticleValue(String key, String urlParam, int colID, String value) {
        JSONObject articleJson = JSONObject.fromObject(value);
        int type = articleJson.getInt("articleType");
        String adv = null;
        if (type == 1) {
            adv = getRedisValue(RedisKey.ADV_PAGE_ALBUM_KEY + colID);
        } else
            adv = getRedisValue(RedisKey.ADV_PAGE_KEY + colID);
        if (null == adv) {
            value = getValByCmsApi(key, urlParam);
        } else {
            articleJson.put("adv", adv);
            value = articleJson.toString();
        }
        return value;
    }

    private String getValByCmsApi(String key, String urlParam) {
        String url = configure.getAppServerUrl() + urlParam;
        //System.out.println("没有缓存数据，重新获取。  请求地址：" + url);
        String value = null;
        if (CommonToolUtil.canGetData(url)) {
            value = redisManager.get(key);
        }
        return value;
    }

    /**
     * 从Redis中取出一个值
     */
    private String getRedisValue(String key) {
    	return redisManager.get(key);
    }

    /**
     * 修改稿件的点击数等计数
     * setCount 的逻辑：
     * 1. countClick ： click  share +countClickInitial > countclick
     * 2. countPraise
     * 3. countShare
     * 4. countDiscuss
     */
    private JSONArray rewriteCount(JSONArray jsonArr) {
        for (int i = 0, length = jsonArr.size(); i < length; i++) {
            JSONObject jsonObj = (JSONObject) jsonArr.get(i);
            if (null != jsonObj && !jsonObj.isEmpty() && !"null".equals(jsonObj.toString())) {

                String docId = "";
                try {
                    docId = jsonObj.getString("fileId");
                }catch (Exception e){
                    System.out.println("ArticleServiceImpl:319行，未获取到fileId");
                    System.out.println(jsonArr);
                    continue;
                }

                System.out.println(i + " docId:" +docId);
                //1. countClick
                int countClick = ArticleCountHelper.calArticleCountClick(redisManager, jsonObj,
                		Long.parseLong(docId));
                jsonObj.put("countClick", countClick);

                //2. countPraise
                int countPraise = getCounts(jsonObj, docId, "countPraise", "p");
                jsonObj.put("countPraise", countPraise);

                //3. countShare
                int countShare = getCounts(jsonObj, docId, "countShare", "s");
                jsonObj.put("countShare", countShare);

                //4. countDiscuss
                int countDiscuss = getCounts(jsonObj, docId, "countDiscuss", "d");
                jsonObj.put("countDiscuss", countDiscuss);
            }
        }
        return jsonArr;
    }

    /**
     * 分别从 Redis 中与 JSON中 获取count值，取大的
     *
     * @param jsonObj
     * @param docId
     * @param paramName
     * @param field
     * @return
     */
    private int getCounts(JSONObject jsonObj, String docId, String paramName, String field) {
		// 从Redis中获取 count
		String key = RedisKey.NIS_EVENT_ARTICLE + docId;
		String countRS = redisManager.hget(key, field);
		int count = CommonToolUtil.isBlank(countRS) ? 0 : Integer.valueOf(countRS);

		// 从Json中获取 count
		if (jsonObj.has(paramName)) {
			String countJS = jsonObj.getString(paramName);
			int countJ = CommonToolUtil.isBlank(countJS) ? 0 : Integer
					.valueOf(countJS);
			// 取大的
			count = count > countJ ? count : countJ;
		}
		return count;
    }

    private JSONObject rewrite(String value) {
        JSONObject jsonObj = JSONObject.fromObject(value);
        JSONArray jsonArr = rewriteCount(jsonObj.getJSONArray("list"));
        jsonObj.put("list", jsonArr);

        return jsonObj;
    }

    private JSONArray getArticles(int columnID, int start,
            int lastFileId, int S, Boolean secondFind, int siteID,int typeScreen) {
        String key = RedisKey.APP_ARTICLELIST_AD_KEY + columnID + "." + S;
        if(typeScreen==1)
            key = RedisKey.APP_ARTICLELIST_AD_WX_KEY + columnID + "." + S;
        if (!redisManager.exists(key)) {
    		//若Redis里不存在，则调用内网api重置。这里加同步锁，以免并发调用。
        	synchronized(this) {
        		//多一次判断exist，后面线程可以不再多调用内网api
        		if (!redisManager.exists(key)) {
		            try {
						String url = configure.getAppServerUrl() + "/getArticles.do?colID=" + columnID
						        + "&start=" + S + "&siteID=" + siteID+"&typeScreen="+typeScreen;
						CommonToolUtil.canGetData(url);
					} catch (Exception e) {
						e.printStackTrace();
					}
        		}
        	}
            if (redisManager.exists(key)) {
                return getValue(columnID, start, lastFileId, S, secondFind, siteID,typeScreen);
            } else if (secondFind)
                return getSecondFindValue(columnID, start);
        	return new JSONArray();
        } else {
            return getValue(columnID, start, lastFileId, S, secondFind, siteID,typeScreen);
        }
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

    private JSONArray getSecondFindValue(int columnID, int start) {
        int S = (start / CACHE_LENGTH) * CACHE_LENGTH;
        String key = RedisKey.APP_ARTICLELIST_AD_KEY + columnID + "." + S;

        int index = start % CACHE_LENGTH;

    	return getSomeFromRedisList(key, index);
    }

    private JSONArray getValue(int columnID, int start,
            int lastFileId, int S, Boolean secondFind, int siteID,int typeScreen) {
        String key = RedisKey.APP_ARTICLELIST_AD_KEY + columnID + "." + S;
        if(typeScreen==1)
            key = RedisKey.APP_ARTICLELIST_AD_WX_KEY + columnID + "." + S;

        if (lastFileId > 0) {
            int index = getIndex(key, lastFileId);
            if (index < 0) { // 是当前list最后一个
                S += CACHE_LENGTH;
                //20160713 getArticles的参数lastFileId由0改成lastFileId
                return getArticles(columnID, S, lastFileId, S, true, siteID,typeScreen);
            } else if (index > 0) { //找到且不是最后一个
            	return getSomeFromRedisList(key, index);
            } else { //在list中没找到lastID
                if (!secondFind) {
                	//在list中没找到，则到下一页去查
                    S += CACHE_LENGTH;
                    return getArticles(columnID, start, lastFileId, S, true, siteID,typeScreen);
                } else {
                	//已经查过下一页了还没找到，可能是撤稿了，为避免返回空，就按start读一页返回，很可能有重稿
                    return getSecondFindValue(columnID, start);
                }
            }
        } else {
        	return getSomeFromRedisList(key, 0);
        }
    }

    //从Redis的list里读一段
    private JSONArray getSomeFromRedisList(String key, int index) {
    	//list的最后一个元素是记录位置索引的，所以要加判断
    	JSONArray result = new JSONArray();
    	int end = index + COUNT - 1;
    	long len = redisManager.llen(key);
    	if(len==1){
    		return result;
    	}
    	if (end >= len - 1) end = (int)len - 2;

    	List<String> list = redisManager.lrange(key, index, end);

    	result.addAll(list);

    	return result;
    }

    /**
     * 稿件位置索引放在列表的最后，取出来转成Json对象，按lastFileId读出位置
     */
    private int getIndex(String key, int lastFileId) {
    	//从队尾读出位置索引
    	List<String> indexRange = redisManager.lrange(key, -1, -1);
    	if (indexRange == null || indexRange.size() == 0) return 0;

    	//转换成json
    	JSONObject indexJson = JSONObject.fromObject(indexRange.get(0));
    	String idKey = String.valueOf(lastFileId);
    	if (indexJson.containsKey(idKey)) {
    		int index = indexJson.getInt(idKey) + 1;
    		//若是队尾，则应读下一个列表了
    		if (index + 1 == redisManager.llen(key))
    			return -1;
    		else
    			return index;
    	}
    	return 0;
    }

    private String moduleRevise(String value, long userID){
    	if (userID <= 0 || value == null) return value;

		JSONObject jsonObj = JSONObject.fromObject(value);
		JSONArray jsonArray = jsonObj.getJSONArray("list");

		String type = jsonObj.getString("type");
		if ("0".equals(type)) {
			//修改稿件的计数
			jsonObj.put("list", rewriteCount(jsonArray));
		} else if ("1".equals(type)) {
			//修改栏目的订阅数
			jsonObj.put("list", rewriteColumnCount(jsonArray));

            //增加：订阅的栏目ID
			String colIDs = redisManager.hget(RedisKey.MY_COLUMN_KEY, String.valueOf(userID));
			if (null == colIDs) {
				String url = configure.getAppServerUrl() + "myColumnIDs.do?userID=" + userID;
				if (CommonToolUtil.canGetData(url))
					colIDs = redisManager.hget(RedisKey.MY_COLUMN_KEY, String.valueOf(userID));
			}
			jsonObj.put("subIDs",colIDs);
		} else if ("2".equals(type)) { //问吧模块，要取出用户关注的问吧ID
			//增加：关注的互动话题ID
			String key = RedisKey.MY_SUBJECT_SUBIDS_KEY + userID;
			String url = configure.getAppServerUrl() + "/mySubjectIDsSubscribe.do?userid=" + userID;
			String ids = CommonToolUtil.getDataResult(key, url, redisManager, configure);

			//内网api的数据格式是{list:[]}，添加subIDs即可
			jsonObj.put("subIDs",ids);
		}
		value = jsonObj.toString();
        return value;
    }

    //添加栏目订阅数
    private JSONArray rewriteColumnCount(JSONArray jsonArr) {
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < jsonArr.size(); i++) {
            JSONObject jsonObject = (JSONObject) jsonArr.get(i);
            String columnNum = redisManager.hget(RedisKey.NIS_EVENT_SUBSCRIBE_COLUMN, jsonObject.getString("columnId"));
            if (null == columnNum) {
                String countUrl = configure.getAppServerUrl() + "/getColSubscribeCount.do?id=" + jsonObject.getString("columnId");
                columnNum = CommonToolUtil.getData(countUrl);
            }
            if (!jsonObject.getString("rssCount").equals(columnNum) && null != columnNum) {
            	int count = Integer.parseInt(columnNum);
                if (count < 0) count = 0;
                jsonObject.put("rssCount", count);
            }
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

	public String myCollectionList(String articleIDs, int ch) {
        String url = configure.getAppServerUrl();
        url = url.substring(0, url.length() - 4);
        url += "/collectArticles.do?articleIDs=" + articleIDs + "&ch=" + ch;
        return CommonToolUtil.getData(url);
	}

    public String getPubArticles(int userID,String data) {

        String url = configure.getNewAppServerUrl() + "/getPubArticles.do?userID="+userID;
        String results = null;
        try {
            results = CommonToolUtil.getData(url,data);
        } catch (Exception e) {
            e.printStackTrace();
            results = CommonToolUtil.buildFailureArrayResult("请求内网接口失败");
        }

        return results;
    }

    public String addArticle(int userID, String data) {

        String url = configure.getNewAppServerUrl() + "/addArticle.do?userID="+userID;
        String results = null;
        try {
            results = CommonToolUtil.getData(url,data);
        } catch (Exception e) {
            e.printStackTrace();
            results = CommonToolUtil.buildFailureObjectResult("请求内网接口失败");
        }

        return results;
    }

    @Override
    public String getArticleDetail(String data, int userID) {
        String url = configure.getNewAppServerUrl() + "/getArticleDetail.do?userID=" + userID;
        String results = null;
        try {
            results = CommonToolUtil.getData(url,data);
        } catch (Exception e) {
            e.printStackTrace();
        	results = CommonToolUtil.buildFailureObjectResult("请求内网接口失败");
        }

        return results;
    }

    @Override
    public InputStream getImage(String path) {
        String url = configure.getNewAppServerUrl() + "/getImage.do?" + path;
        return CommonToolUtil.getInputStream(url);
    }

    @Override
    public String getTabs(int userID,String data) {

        String url = configure.getNewAppServerUrl() + "/getTabs.do?userID="+userID;
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
	public String pubSearch(String data) {
		String url = configure.getNewAppServerUrl() + "/pubSearch.do";
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
	public String revokeSearch(String data) {
		String url = configure.getNewAppServerUrl() + "/revokeSearch.do";
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
	public String flowRecordList(String data) {
		String url = configure.getNewAppServerUrl() + "/flowRecordList.do";
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
	public String pushApp(String data, int userID) {
		String url = configure.getNewAppServerUrl() + "/pushApp.do?userID=" + userID;
        String results = null;
        try {
            results = CommonToolUtil.getData(url,data);
        } catch (Exception e) {
            e.printStackTrace();
        	results = CommonToolUtil.buildFailureObjectResult("请求内网接口失败");
        }

        return results;
	}

//	private String buildFailureResult(String errorInfo) {
//		JSONObject result = new JSONObject();
//		result.put("success", false);
//		result.put("errorInfo", errorInfo);
//		result.put("results", new JSONArray());
//		return result.toString();
//	}

	@Override
    public String delete(int userID, String data) {

        String url = configure.getNewAppServerUrl() + "/deleteArticle.do?userID="+userID;
        String results = null;
        try {
            results = CommonToolUtil.getData(url,data);
        } catch (Exception e) {
            e.printStackTrace();
            results = CommonToolUtil.buildFailureObjectResult("请求内网接口失败");
        }

        return results;
    }

    @Override
    public String publishArticle(int userID, String data) {
        String url = configure.getNewAppServerUrl() + "/pubArticle.do?userID=" + userID;
        String results = null;
        try {
            results = CommonToolUtil.getData(url,data);
        } catch (Exception e) {
            e.printStackTrace();
            results = CommonToolUtil.buildFailureObjectResult("请求内网接口失败");
        }

        return results;
    }

	@Override
	public String getAuditArticles(String data, int userID) {
		String url = configure.getNewAppServerUrl() + "/getAuditArticles.do?userID=" + userID;
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
	public String transfer(int userID, String data) {
		String url = configure.getNewAppServerUrl() + "/transferArticle.do?userID=" + userID;
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
	public String reject(int userID, String data) {
		String url = configure.getNewAppServerUrl() + "/rejectArticle.do?userID=" + userID;
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
    public String revokeArticle(int userID, String data) {
        String url = configure.getNewAppServerUrl() + "/revokeArticle.do?userID=" + userID;
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
    public String revokeDelete(int userID, String data) {
        String url = configure.getNewAppServerUrl() + "/revokeDelete.do?userID=" + userID;
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
    public String getNewArticleHotList(int siteID, int articleType, int timeType, int orderType, int channel) {
        String key;
        if(channel == 1){//查看app稿件
            key = redisManager.getKeyBySite(RedisKey.APP_NEW_HOT_ARTICLELIST_KEY, siteID) + articleType +"."+ timeType +"."+ orderType;
        }else{
            key = redisManager.getKeyBySite(RedisKey.WEB_NEW_HOT_ARTICLELIST_KEY, siteID) + articleType +"."+ timeType +"."+ orderType;
        }
        String urlParam = "/newArticleHotList.do?siteID="+siteID+"&articleType="+articleType+"&timeType="+timeType+"&orderType="+orderType+"&channel="+channel;
        if (!redisManager.exists(key)) {
            //若Redis里不存在，则调用内网api重置。这里加同步锁，以免并发调用。
            synchronized (this) {
                //多一次判断exist，后面线程可以不再多调用内网api
                if (!redisManager.exists(key)) {
                    try {
                        String url = configure.getAppServerUrl() + urlParam;
                        CommonToolUtil.canGetData(url);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        String value = getRedisValue(key);

        if(StringUtils.isBlank(value)){
            value = "{\"list\": []}";
        }else{
            JSONObject jsonObject = JSONObject.fromObject(value);
            JSONArray jsonArray = JSONArray.fromObject(jsonObject.getJSONArray("list"));
            //改写点击数等
            rewriteCount(jsonArray);

            JSONObject result = new JSONObject();
            result.put("list",jsonArray);

            value = String.valueOf(result);

        }

        return value;
    }


    @SuppressWarnings("unchecked")
	@Override
    public String getSubColArticles(int columnID, int siteID){
    	String columnKey = redisManager.getKeyBySite(RedisKey.APP_COLLIST_KEY, siteID) + columnID;

		String columnValue = redisManager.get(columnKey);
		if (columnValue == null) {
			System.out.println(columnValue);
			synchronized(this){
				columnValue = redisManager.get(columnKey);
				System.out.println(columnValue);
				if(columnValue == null){
					String columnUrl = configure.getAppServerUrl() + "/getColumns.do?siteID=" + siteID + "&colID=" + columnID;
//					System.out.println("没有缓存数据，重新获取。  请求地址：" + columnUrl);
					if (CommonToolUtil.canGetData(columnUrl))
						columnValue = redisManager.get(columnKey);
				}
			}
		}

		if(columnValue == null){
			return "[{}]";
		}

		JSONObject jsonObj=JSONObject.fromObject(columnValue);
		String str=jsonObj.getString("columns");
		if(null!=str&&!"".equals(str)) {
			JSONArray jsonArr = JSONArray.fromObject(str);
			final JSONArray jsonArray=new JSONArray();
			for (int i = 0; i < jsonArr.size(); i++) {
				JSONObject jsonObject = (JSONObject) jsonArr.get(i);
				JSONObject one = new JSONObject();
                one.put("column", jsonObject);

                JSONArray articles = new JSONArray();
                String key = RedisKey.APP_ARTICLELIST_AD_KEY + jsonObject.getString("columnId") + "." + 0;
                if (!redisManager.exists(key)) {
            		//若Redis里不存在，则调用内网api重置。这里加同步锁，以免并发调用。
                	synchronized(this) {
                		//多一次判断exist，后面线程可以不再多调用内网api
                		if (!redisManager.exists(key)) {
        		            try {
        						String url = configure.getAppServerUrl() + "/getArticles.do?colID=" + jsonObject.getString("columnId")
        						        + "&start=" + 0 + "&siteID=" + siteID+"&typeScreen="+0;
        						CommonToolUtil.canGetData(url);
        					} catch (Exception e) {
        						e.printStackTrace();
        					}
                		}
                	}
                    if (redisManager.exists(key)) {
                    	articles = getSomeFromRedisListNew(key, 1);
                    }
                } else {
                	articles = getSomeFromRedisListNew(key, 1);
                }

                //改写点击数等
                if(articles!=null){
                    rewriteCount(articles);
                }

                one.put("list", articles);
				jsonArray.add(one);
			}

            Collections.sort(jsonArray, new Comparator<JSONObject>() {
                @Override
                public int compare(JSONObject o1, JSONObject o2) {
                    if(o1.getJSONArray("list").size()>0&&o2.getJSONArray("list").size()>0){
                        try {
                            return new String(o2.getJSONArray("list").getJSONObject(0).getString("publishtime")).compareTo
                                    (new String(o1.getJSONArray("list").getJSONObject(0).getString("publishtime")));
                        }catch (Exception e){
                            System.out.println("o1:"+o1.toString());
                            System.out.println("o2:"+o2.toString());
                            System.out.println("jsonArray:"+jsonArray.toString());
                            System.out.println("ArticleServiceImpl:896行");
                            return 0;
                        }
                    }else{
                        return 0;
                    }
                }
            });


            JSONObject result = new JSONObject();
			result.put("list", jsonArray);
			return result.toString();
		} else {
			return "[{}]";
		}
    }
    
    private JSONArray getSomeFromRedisListNew(String key, int count) {
    	//list的最后一个元素是记录位置索引的，所以要加判断
    	long len = redisManager.llen(key);
    	if (count >= len) count = (int)len - 1;
		
		JSONArray result = new JSONArray();
		if(count > 0){
			List<String> list = redisManager.lrange(key, 0, count - 1);
			result.addAll(list);
		}
		return result;
	}
    
    public String getTopicArticles(int topicID, int start, int count, 
            int lastFileId, int siteID,int type,int channel) {

        int S = (start / CACHE_LENGTH) * CACHE_LENGTH;
        if (0 == start) lastFileId = 0;
        
        JSONArray jsonArr = getTrueTopicArticles(topicID, start, lastFileId, S, false, siteID,type,channel);
        
	    //改写点击数等
	    rewriteCount(jsonArr);
       
        //加推荐模块
//        String modules = getColumnModules(columnID, siteID);
        
        JSONObject jsonObj = new JSONObject();
	    jsonObj.put("list", jsonArr);
//        jsonObj.put("modules", modules);

        //增加广告
//        setAdv(jsonObj,columnID);
        
        return jsonObj.toString();
    }
    
    private JSONArray getTrueTopicArticles(int topicID, int start, 
            int lastFileId, int S, Boolean secondFind, int siteID,int type,int channel) {
        String key = redisManager.getKeyBySite(RedisKey.APP_ARTICLELIST_TOPIC_KEY, siteID) + channel + "." + topicID + "." + type + "." + S;
        if (!redisManager.exists(key)) {
    		//若Redis里不存在，则调用内网api重置。这里加同步锁，以免并发调用。
        	synchronized(this) {
        		//多一次判断exist，后面线程可以不再多调用内网api
        		if (!redisManager.exists(key)) {
		            try {
						String url = configure.getAppServerUrl() + "/getTopicArticles.do?topicID=" + topicID
						        + "&start=" + S + "&siteID=" + siteID+"&type="+type+"&channel="+channel;
						CommonToolUtil.canGetData(url);
					} catch (Exception e) {
						e.printStackTrace();
					}
        		}
        	}
            if (redisManager.exists(key)) {
                return getTopicValue(topicID, start, lastFileId, S, secondFind, siteID,type,channel);
            } else if (secondFind)
                return getTopicSecondFindValue(topicID, start, siteID, type,channel);
        	return new JSONArray();
        } else {
            return getTopicValue(topicID, start, lastFileId, S, secondFind, siteID, type,channel);
        }
    }
    
    private JSONArray getTopicValue(int topicID, int start,
            int lastFileId, int S, Boolean secondFind, int siteID,int type,int channel) {
    	String key = redisManager.getKeyBySite(RedisKey.APP_ARTICLELIST_TOPIC_KEY, siteID) + channel + "." + topicID + "." + type + "." + S;
        
        if (lastFileId > 0) {
            int index = getIndex(key, lastFileId);
            if (index < 0) { // 是当前list最后一个
                S += CACHE_LENGTH;
                //20160713 getArticles的参数lastFileId由0改成lastFileId
                return getTrueTopicArticles(topicID, S, lastFileId, S, true, siteID,type,channel);
            } else if (index > 0) { //找到且不是最后一个
            	return getSomeFromRedisList(key, index);
            } else { //在list中没找到lastID
                if (!secondFind) {
                	//在list中没找到，则到下一页去查
                    S += CACHE_LENGTH;
                    return getTrueTopicArticles(topicID, start, lastFileId, S, true, siteID,type,channel);
                } else {
                	//已经查过下一页了还没找到，可能是撤稿了，为避免返回空，就按start读一页返回，很可能有重稿
                    return getTopicSecondFindValue(topicID, start, siteID, type,channel);
                }
            }
        } else {
        	return getSomeFromRedisList(key, 0);
        }
    }
    
    private JSONArray getTopicSecondFindValue(int topicID, int start, int siteID,int type,int channel) {
        int S = (start / CACHE_LENGTH) * CACHE_LENGTH;
        String key = redisManager.getKeyBySite(RedisKey.APP_ARTICLELIST_TOPIC_KEY, siteID) + channel + "." + topicID + "." + type + "." + S;
        
        int index = start % CACHE_LENGTH;
        
    	return getSomeFromRedisList(key, index);
    }
    
    @Override
	public String getArticleCountInfo(long docID, long colID) {
		String url = configure.getAppServerUrl()
        	+ "getArticleCountInfo.do?colID=" + colID + "&docID=" + docID ;
        return CommonToolUtil.getData(url);
	}

    @Override
    public String addArticleForBig(String data) {
        String url = configure.getAppServerUrl()+"addArticleForBig.do";
        String results = "";
        try {
            results = CommonToolUtil.getData(url,data);
        } catch (Exception e) {
            e.printStackTrace();
            results = CommonToolUtil.buildFailureObjectResult("请求内网接口失败");
        }
        return results;
    }
    
    @Override
    public String deleteArticleForBig(String data) {
        String url = configure.getAppServerUrl()+"deleteArticleForBig.do";
        String results = "";
        try {
            results = CommonToolUtil.getData(url,data);
        } catch (Exception e) {
            e.printStackTrace();
            results = CommonToolUtil.buildFailureObjectResult("请求内网接口失败");
        }
        return results;
    }
}