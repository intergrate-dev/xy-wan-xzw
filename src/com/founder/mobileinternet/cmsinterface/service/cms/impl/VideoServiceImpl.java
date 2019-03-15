package com.founder.mobileinternet.cmsinterface.service.cms.impl;

import com.founder.mobileinternet.cmsinterface.pojo.Configure;
import com.founder.mobileinternet.cmsinterface.service.cms.VideoService;
import com.founder.mobileinternet.cmsinterface.util.CommonToolUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by yu.feng on 2017/11/13.
 */
@Service
public class VideoServiceImpl implements VideoService {

    @Autowired
    private Configure configure;

    @Override
    public String getVideoGroup(int userID, String data) {

        String url = configure.getNewAppServerUrl() + "/getVideoGroup.do?userID=" + userID;
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
    public String getVideos(int userID, String data) {

        String url = configure.getNewAppServerUrl() + "/getVideos.do?userID=" + userID;
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
    public String pubVideo(int userID, String data) {

        String url = configure.getNewAppServerUrl() + "/pubVideo.do?userID=" + userID;
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
    public String revokeVideo(int userID, String data) {

        String url = configure.getNewAppServerUrl() + "/revokeVideo.do?userID=" + userID;
        String results = null;
        try {
            results = CommonToolUtil.getData(url,data);
        } catch (Exception e) {
            e.printStackTrace();
            results = CommonToolUtil.buildFailureObjectResult("请求内网接口失败");
        }

        return results;
    }

}
