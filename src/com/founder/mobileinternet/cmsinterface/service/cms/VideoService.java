package com.founder.mobileinternet.cmsinterface.service.cms;

/**
 * Created by yu.feng on 2017/11/13.
 */
public interface VideoService {

    String getVideoGroup(int userID, String data);

    String getVideos(int userID, String data);

    String pubVideo(int userID, String data);

    String revokeVideo(int userID, String data);
}
