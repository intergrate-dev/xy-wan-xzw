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

/**
 * 报料类
 */
public class TipoffInfo {
	private int siteID = 1;
	private String topic;
	private String content;
	private String tag = "";
	
	// （图片url，多个，已保存在外网，可访问的地址）:[url1,url2,url3,….]
	private List<String> imgUrl = new ArrayList<String>();
	// （视频url，多个，已保存在外网，可访问的地址）:[url1,url2,url3,….]
	private List<String> videoUrl = new ArrayList<String>();
	
	// （经度）
	private double longitude;
	// （纬度）
	private double latitude;
	// （地理位置文字描述）
	private String location = "";
	
	// 联系电话
	private String contactNo = "";
	
	private int userID;
	private String userName;
	//来源
	private int source;

	private String userOtherID="";

	public String getUserOtherID() {
		return userOtherID;
	}

	public void setUserOtherID(String userOtherID) {
		this.userOtherID = userOtherID;
	}

	public int getSource() {
		return source;
	}

	public void setSource(int source) {
		this.source = source;
	}

	public int getSiteID() {
		return siteID;
	}

	public void setSiteID(int siteID) {
		this.siteID = siteID;
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

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getContactNo() {
		return contactNo;
	}

	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
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
}
