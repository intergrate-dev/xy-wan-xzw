package com.founder.mobileinternet.cmsinterface.service.cms.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.founder.mobileinternet.cmsinterface.pojo.cms.LiveParam;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.founder.mobileinternet.cmsinterface.pojo.Configure;
import com.founder.mobileinternet.cmsinterface.pojo.cms.LiveInfo;
import com.founder.mobileinternet.cmsinterface.service.RedisKey;
import com.founder.mobileinternet.cmsinterface.service.RedisManager;
import com.founder.mobileinternet.cmsinterface.service.cms.LiveService;
import com.founder.mobileinternet.cmsinterface.util.CommonToolUtil;

import static com.founder.mobileinternet.cmsinterface.service.RedisKey.APP_LIVE_STATUS_KEY;

@Service
public class LiveServiceImpl implements LiveService {
    @Autowired
    private RedisManager redisManager;
    @Autowired
    private Configure configure;

    public String commitLive(LiveInfo forumInfo) {
        String url = configure.getAppServerUrl() + "/live.do";
        return CommonToolUtil.postBeanDataWithResult(url, forumInfo);
    }

    public String liveView(
            int id, int index, int page, int lastFileId, int siteID) {
        String key = RedisKey.APP_LIVELIST_KEY + id + "." + page;
        String urlParam = "/liveView.do?id=" + id + "&page=" + page + "&siteID=" + siteID;
        String value = getDataResult(key, urlParam, index);
        if (page > 0 && lastFileId > 0 && null != value) {
            value = CommonToolUtil.listFilter(value, lastFileId, "list", "fileId");
        }

        if (page == 0 && value != null) {
            JSONObject json = JSONObject.fromObject(value);
            JSONObject main, currentStatus;
            String[] s;
            if (json.containsKey("main")) {
                main = json.getJSONObject("main");
                if (main.containsKey("streamIds") && (main.getInt("status") == 1 || main.getInt("status") == 0)) {
                    s = main.getString("streamIds").split(",");
                    currentStatus = new JSONObject();
                    for (String _s : s) {
                        if(redisManager.exists(APP_LIVE_STATUS_KEY + "current." + _s)){
                            currentStatus.put( _s, redisManager.get(APP_LIVE_STATUS_KEY + "current." + _s));
                        }else{
                            currentStatus.put( _s, "0");
                        }
                    }
                    json.put("currentStatus", currentStatus.toString());
                    value = json.toString();
                }
            }
        }
        return value;
    }

    @Override
    public String liveStatus(LiveParam param) {
        if (param.getEvent_type() == 100) {  //生成新的录播文件
           /* redisManager.hset(APP_LIVE_STATUS_KEY + param.getStream_id(), "status.100.video_id", param.getVideo_id(),
                              RedisManager.week1);
            redisManager.hset(APP_LIVE_STATUS_KEY + param.getStream_id(), "status.100.video_url", param.getVideo_url(),
                              RedisManager.week1);
            redisManager.hset(APP_LIVE_STATUS_KEY + param.getStream_id(), "status.100.start_time",
                              param.getStart_time() + "", RedisManager.week1);
            redisManager.hset(APP_LIVE_STATUS_KEY + param.getStream_id(), "status.100.end_time",
                              param.getEnd_time() + "", RedisManager.week1);
            redisManager.hset(APP_LIVE_STATUS_KEY + param.getStream_id(), "status.100.json", param.getJson(),
                              RedisManager.week1);
            redisManager.hset(APP_LIVE_STATUS_KEY + param.getStream_id(), "status.100.file_format",
                              param.getFile_format(), RedisManager.week1);
            redisManager.hset(APP_LIVE_STATUS_KEY + param.getStream_id(), "status.100.file_id", param.getFile_id(),
                              RedisManager.week1);*/
            String url = configure.getAppServerUrl() + "/saveLivePlaybackUrl.do";
            CommonToolUtil.postBeanDataWithResult(url, param);
        }
        //保存所有的
        redisManager.set(APP_LIVE_STATUS_KEY + "all." + param.getEvent_type() + "." + param.getStream_id(),
                         param.getJson() + "", RedisManager.day1);
        redisManager.set(APP_LIVE_STATUS_KEY + "current." + param.getStream_id(), param.getEvent_type() + "",
                         RedisManager.week1);
        return null;
    }

    @Override
    public String liveComing(int siteID) {
        String urlParam = "/liveComing.do?siteID=" + siteID;
        String key = redisManager.getKeyBySite(RedisKey.APP_LIVE_COMING_KEY, siteID);
        return getDataResult(key, urlParam, 2);
    }

    @Override
    public String lives(long loginID, String time, String sign, String requestUrl,int siteID) {
        JSONObject json = new JSONObject();
        json.put("loginID", loginID);
        json.put("time", time);
        json.put("sign", sign);
        json.put("url", requestUrl);
        json.put("siteID",siteID);
        String url = configure.getAppServerUrl() + "/lives.do";
        try {
            return CommonToolUtil.getData(url, json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "{}";
    }

    @Override
    public String allLives(long loginID, String time, String sign, String requestUrl,int siteID,String status,int lastID) {
        JSONObject json = new JSONObject();
        json.put("loginID", loginID);
        json.put("time", time);
        json.put("sign", sign);
        json.put("url", requestUrl);
        json.put("siteID",siteID);
        json.put("status",status);
        json.put("lastID",lastID);
        String url = configure.getAppServerUrl() + "/allLives.do";
        try {
            return CommonToolUtil.getData(url, json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "{}";
    }

    @Override
    public String queryLiveStatus(String requestUrl, int loginID, long time, String sign, String[] streamIDs) {
        JSONObject json = new JSONObject();
        json.put("loginID", loginID);
        json.put("time", time);
        json.put("sign", sign);
        json.put("streamIDs", streamIDs);
        json.put("url", requestUrl);
        String url = configure.getAppServerUrl() + "/queryLiveStatus.do";
        try {
            return CommonToolUtil.getData(url, json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "{\"code\":1}";
    }

    private String getDataResult(String key, String urlParam, int index) {
        String value = redisManager.get(key);
        if (value == null) {
            String url = configure.getAppServerUrl() + urlParam;
//			System.out.println("没有缓存数据，重新获取   请求地址：" + url);
            if (CommonToolUtil.canGetData(url)) {
                value = redisManager.get(key);
                if (1 == index && null != value)
                    value = getSortedValue(value);
            }
        }
        if (null != value) {
            JSONObject jsonObj = JSONObject.fromObject(value);
            JSONArray jsonArr = setCount(jsonObj.getJSONArray("list"));
            jsonObj.put("list", jsonArr);
            value = jsonObj.toString();
        }
        return value;
    }

    private String getSortedValue(String value) {
        @SuppressWarnings("unchecked")
        List<JSONObject> list = (List<JSONObject>) JSONArray
                .toCollection(JSONObject.fromObject(value)
                                      .getJSONArray("list"), JSONObject.class);

        Collections.sort(list, new Comparator<JSONObject>() {
            public int compare(JSONObject json1, JSONObject json2) {
                double result = json1.getDouble("distance") - json2.getDouble("distance");
                if (result < 0) return -1;
                else if (result > 0) return 1;
                return 0;
            }
        });
        JSONObject json = new JSONObject();
        json.put("list", list);
        return json.toString();
    }

    private JSONArray setCount(JSONArray jsonArr) {
        int size = jsonArr.size();
        for (int i = 0; i < size; i++) {
            JSONObject jsonObj = (JSONObject) jsonArr.get(i);
            String id = jsonObj.getString("fileId");

            String key = RedisKey.NIS_EVENT_LIVE + id;

            String count1 = redisManager.hget(key, "p");
            if (!CommonToolUtil.isBlank(count1)) {
                jsonObj.put("countPraise", Integer.valueOf(count1));
            }
            count1 = redisManager.hget(key, "d");
            if (!CommonToolUtil.isBlank(count1)) {
                jsonObj.put("countDiscuss", Integer.valueOf(count1));
            }
        }
        return jsonArr;
    }
}