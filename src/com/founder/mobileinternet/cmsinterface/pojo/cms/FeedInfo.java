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

import com.founder.mobileinternet.cmsinterface.util.XYComment;

public class FeedInfo {

	// （站点ID）
	private int siteID = 1;
	// （内容）
	private String content;
	
	@XYComment(name="上传的图片", comment="图片url，可多个")
	private List<String> imgUrl = new ArrayList<String>();
	
	@XYComment(name="上传的视频", comment="与图片一样，目前没使用")
	private List<String> videoUrl = new ArrayList<String>();
	
	// （经度）
	private double longitude;
	// （纬度）
	private double latitude;
	// （地理位置文字描述）
	private String location = "";
	
	private int userID;
	private String userName;
	
	private String phone = "";
	private String email = "";

	public int getSiteID() {
		return siteID;
	}

	public void setSiteID(int siteID) {
		this.siteID = siteID;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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
