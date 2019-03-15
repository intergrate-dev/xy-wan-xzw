/*
 *
 * $Revision: 1 $
 * $History: EventInfo.java $
 * 
 * Copyright (c) 2006,北大方正电子有限公司数字媒体开发部
 * All rights reserved.
 */
package com.founder.mobileinternet.cmsinterface.pojo.cms;

import java.util.ArrayList;
import java.util.List;

public class LiveInfo {
    // （站点ID）
    private int siteID = 1;
    // （主贴ID，做跟帖时有此参数，发新帖时=0）”:
    private long rootID;
    // （父贴ID，若是对跟帖进行回复，则有这个参数）“:
    private long parentID;
    private int parentUserID;
    private String topic;
    private String content;
    // （图片url，多个，已保存在外网，可访问的地址）:[url1,url2,url3,….]
    private List<String> imgUrl = new ArrayList<String>();
    // （视频url，多个，已保存在外网，可访问的地址）:[url1,url2,url3,….]
    private List<String> videoUrl = new ArrayList<String>();
    // （经度）
    private double longitude;
    // （纬度）
    private double latitude;
    // （地理位置文字描述）
    private String location;
    // （联系电话，只在报料里用到）
    private int userID;

    private int loginID;
    // （注册用户名）”:
    private String userName;
    // (用户其它标识（非注册用户时的标识，如移动设备号）): 123
    private String userOtherID;

    private String time;

    private String sign;

    private String url;

    private int sourceType;

    public int getSiteID() {
        return siteID;
    }

    public void setSiteID(int siteID) {
        this.siteID = siteID;
    }

    public long getRootID() {
        return rootID;
    }

    public void setRootID(long rootID) {
        this.rootID = rootID;
    }

    public long getParentID() {
        return parentID;
    }

    public void setParentID(long parentID) {
        this.parentID = parentID;
    }

    public int getParentUserID() {
        return parentUserID;
    }

    public void setParentUserID(int parentUserID) {
        this.parentUserID = parentUserID;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(List<String> imgUrl) {
        this.imgUrl = imgUrl;
    }

    public List<String> getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(List<String> videoUrl) {
        this.videoUrl = videoUrl;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserOtherID() {
        return userOtherID;
    }

    public void setUserOtherID(String userOtherID) {
        this.userOtherID = userOtherID;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getSourceType() {
        return sourceType;
    }

    public void setSourceType(int sourceType) {
        this.sourceType = sourceType;
    }

    public int getLoginID() {
        return loginID;
    }

    public void setLoginID(int loginID) {
        this.loginID = loginID;
    }
}
