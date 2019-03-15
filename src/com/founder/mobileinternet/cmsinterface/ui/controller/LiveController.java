package com.founder.mobileinternet.cmsinterface.ui.controller;

import com.founder.mobileinternet.cmsinterface.pojo.Configure;
import com.founder.mobileinternet.cmsinterface.pojo.cms.LiveInfo;
import com.founder.mobileinternet.cmsinterface.pojo.cms.LiveParam;
import com.founder.mobileinternet.cmsinterface.service.RedisManager;
import com.founder.mobileinternet.cmsinterface.service.cms.LiveService;
import com.founder.mobileinternet.cmsinterface.util.CommonToolUtil;
import com.founder.mobileinternet.cmsinterface.util.XYComment;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@XYComment(name = "直播")
@Controller
public class LiveController {

    @Autowired
    private LiveService liveService;
    @Autowired
    private RedisManager redisManager;
    @Autowired
    private Configure configure;

    @XYComment(name = "提交直播的报道")
    @RequestMapping(value = "live")
    @ResponseBody
    public void commitLive(
            LiveInfo forumInfo, HttpServletRequest request,
            HttpServletResponse response) {
        boolean info = redisManager.setUpIfNotExist(request.getRemoteAddr());
        forumInfo.setUrl(request.getServletPath());
        JSONObject json = new JSONObject();
        json.put("code", 11);
        if (info) {
            String result = liveService.commitLive(forumInfo);
            json.putAll(JSONObject.fromObject(result));
        } else {
            json.put("code", 10);
            json.put("error", "创建remoteAdd失败");
        }
        CommonToolUtil.outputJson(request, response, json.toString());
    }

    @XYComment(name = "直播列表", comment = "读直播话题和话题的报道列表")
    @RequestMapping(value = "liveView")
    @ResponseBody
    public void liveView(
            int id,
            @RequestParam(required = false) Integer start,
            @RequestParam(required = false) Integer page,
            @RequestParam(defaultValue = "1") int siteId,

            @XYComment(name = "索引", comment = "当为1时为直播话题报道列表按距离排序（但目前后台api无此排序参数）")
            @RequestParam(defaultValue = "0") int index,

            @XYComment(name = "上一次列表的最后一篇稿件ID", comment = "用于翻页时查重")
            @RequestParam(defaultValue = "0") int lastFileId,
            HttpServletRequest request, HttpServletResponse response) {
        page = CommonToolUtil.getPage(start, page);
        String info = liveService.liveView(id, index, page, lastFileId, siteId);

        String origin = request.getHeader("origin");
        if(origin!=null&&configure.getWebsiteUrl().contains(origin)){
            response.addHeader("Access-Control-Allow-Origin",origin);
        }
        CommonToolUtil.outputJson(request, response, info);
    }

    /**
     * 事件消息通知 - 直播期间 腾讯会调用这个接口 通知状态
     */
    @RequestMapping(value = "liveStatus")
    @ResponseBody
    public void liveStatus(
            HttpServletRequest request,
            HttpServletResponse response, @RequestBody JSONObject json
    ) {
        LiveParam param = (LiveParam) JSONObject.toBean(json, LiveParam.class);
        param.setJson(json.toString());
        System.out.println(
                "/***********************live:" + param.getStream_id() + " status:" + param.getEvent_type() + "****************************/");
        liveService.liveStatus(param);
        CommonToolUtil.outputJson(request, response, "{ \"code\":0 }");
    }

    /**
     * 直播预告
     */
    @XYComment(name = "直播预告列表", comment = "读预告直播话题和话题的报道列表")
    @RequestMapping(value = "liveComing")
    @ResponseBody
    public void liveComing(
            HttpServletRequest request,
            @RequestParam(defaultValue = "1") int siteID,
            HttpServletResponse response) {
        String info = liveService.liveComing(siteID);
        CommonToolUtil.outputJson(request, response, info);
    }

    @XYComment(name = "直播列表", comment = "只用于现场直播员，不做缓存")
    @RequestMapping(value = "lives")
    @ResponseBody
    public void lives(
            HttpServletRequest request, HttpServletResponse response, int loginID,
            @XYComment(name = "发送请求的时间", comment = "用于和后台执行请求时的当前时间进行比较，判断请求是否超时")
                    String time,
            @XYComment(name = "请求路径的MD5值", comment = "用于判断签名是否一致")
                    String sign,int siteID) {
        String info = liveService.lives(loginID, time, sign, request.getServletPath(),siteID);
        CommonToolUtil.outputJson(request, response, info);
    }

    @RequestMapping("queryLiveStatus")
    public void queryLiveStatus(
            HttpServletRequest request, HttpServletResponse response, int loginID, long time, String sign,
            String[] streamID) {
        String info = liveService.queryLiveStatus(request.getServletPath(),  loginID, time, sign, streamID);
        CommonToolUtil.outputJson(request, response, info);
    }

    @XYComment(name = "全部直播列表", comment = "用于移动采编选直播话题")
    @RequestMapping(value = "allLives")
    @ResponseBody
    public void allLives(
            HttpServletRequest request, HttpServletResponse response, int loginID,
            @XYComment(name = "发送请求的时间", comment = "用于和后台执行请求时的当前时间进行比较，判断请求是否超时")
                    String time,
            @XYComment(name = "请求路径的MD5值", comment = "用于判断签名是否一致")
                    String sign,int siteID,String status,int lastID) {
        String info = liveService.allLives(loginID, time, sign, request.getServletPath(),siteID,status,lastID);
        CommonToolUtil.outputJson(request, response, info);
    }

    @RequestMapping(value = "test")
    public String test() {
        return "test";
    }
}