package com.founder.mobileinternet.cmsinterface.ui.controller;

import com.founder.mobileinternet.cmsinterface.service.RedisManager;
import com.founder.mobileinternet.cmsinterface.service.cms.VideoService;
import com.founder.mobileinternet.cmsinterface.util.CommonToolUtil;
import com.founder.mobileinternet.cmsinterface.util.XYComment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by yu.feng on 2017/11/13.
 */
@XYComment(name="视频")
@Controller
public class VideoController {

    @Autowired
    private VideoService videoService;
    @Autowired
    private RedisManager redisManager;

    @XYComment(name="移动采编视频分组")
    @RequestMapping(value = "getVideoGroup")
    @ResponseBody
    public void getVideoGroup(
            @RequestParam int loginID,
            @RequestParam int userID,
            @RequestParam long time,
            @RequestParam String sign,
            @RequestParam String data,

            HttpServletRequest request, HttpServletResponse response){
        System.out.println("url------>"+request.getServletPath());
        System.out.println("loginID------>"+loginID);
        System.out.println("userID------>"+userID);
        System.out.println("time------>"+time);
        System.out.println("sign------>"+sign);
        System.out.println("data------>"+data);

        boolean validResult = CommonToolUtil.checkValid(request.getServletPath(),loginID,time,sign,redisManager);
        if(validResult){
            String info = videoService.getVideoGroup(userID, data);
            CommonToolUtil.outputJson(request,response,info);
        }else {
            CommonToolUtil.outputJson(request,response,"");
        }

    }

    @XYComment(name="移动采编视频列表")
    @RequestMapping(value = "getVideos")
    @ResponseBody
    public void getVideos(
            @RequestParam int loginID,
            @RequestParam int userID,
            @RequestParam long time,
            @RequestParam String sign,
            @RequestParam String data,

            HttpServletRequest request, HttpServletResponse response){
        System.out.println("url------>"+request.getServletPath());
        System.out.println("loginID------>"+loginID);
        System.out.println("userID------>"+userID);
        System.out.println("time------>"+time);
        System.out.println("sign------>"+sign);
        System.out.println("data------>"+data);

        boolean validResult = CommonToolUtil.checkValid(request.getServletPath(),loginID,time,sign,redisManager);
        if(validResult){
            String info = videoService.getVideos(userID, data);
            CommonToolUtil.outputJson(request,response,info);
        }else {
            CommonToolUtil.outputJson(request,response,"");
        }

    }

    @XYComment(name="移动采编视频发布")
    @RequestMapping(value = "pubVideo")
    @ResponseBody
    public void pubVideo(
            @RequestParam int loginID,
            @RequestParam int userID,
            @RequestParam long time,
            @RequestParam String sign,
            @RequestParam String data,

            HttpServletRequest request, HttpServletResponse response){
        System.out.println("url------>"+request.getServletPath());
        System.out.println("loginID------>"+loginID);
        System.out.println("userID------>"+userID);
        System.out.println("time------>"+time);
        System.out.println("sign------>"+sign);
        System.out.println("data------>"+data);

        boolean validResult = CommonToolUtil.checkValid(request.getServletPath(),loginID,time,sign,redisManager);
        if(validResult){
            String info = videoService.pubVideo(userID, data);
            CommonToolUtil.outputJson(request,response,info);
        }else {
            CommonToolUtil.outputJson(request,response,"");
        }

    }

    @XYComment(name="移动采编视频取消发布")
    @RequestMapping(value = "revokeVideo")
    @ResponseBody
    public void revokeVideo(
            @RequestParam int loginID,
            @RequestParam int userID,
            @RequestParam long time,
            @RequestParam String sign,
            @RequestParam String data,

            HttpServletRequest request, HttpServletResponse response){
        System.out.println("url------>"+request.getServletPath());
        System.out.println("loginID------>"+loginID);
        System.out.println("userID------>"+userID);
        System.out.println("time------>"+time);
        System.out.println("sign------>"+sign);
        System.out.println("data------>"+data);

        boolean validResult = CommonToolUtil.checkValid(request.getServletPath(),loginID,time,sign,redisManager);
        if(validResult){
            String info = videoService.revokeVideo(userID, data);
            CommonToolUtil.outputJson(request,response,info);
        }else {
            CommonToolUtil.outputJson(request,response,"");
        }

    }

}
