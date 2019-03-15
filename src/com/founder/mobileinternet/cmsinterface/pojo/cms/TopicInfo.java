/*
 *
 * $Revision: 1 $
 * $History: EventInfo.java $
 * 
 * Copyright (c) 2006,北大方正电子有限公司数字媒体开发部
 * All rights reserved.
 */
package com.founder.mobileinternet.cmsinterface.pojo.cms;

import com.founder.mobileinternet.cmsinterface.util.XYComment;

public class TopicInfo {
	private int siteID = 1;
	
	@XYComment(name="订阅对象")
	private long id;
	
	@XYComment(name="用户ID")
	private int userID;
	@XYComment(name="用户名")
	private String userName;
	
	@XYComment(name="订阅类型", comment="1是栏目订阅，2是记者关注，3是互动话题关注")
	private int type;
	@XYComment(name="设备类型", comment="暂时先加上")
	private String device ;

	// （经度）
	private double longitude;
	// （纬度）
	private double latitude;
	// （地理位置文字描述）
	private String location;
	
	public int getSiteID() {
		return siteID;
	}

	public void setSiteID(int siteID) {
		this.siteID = siteID;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	public String getDevice() {
		return device;
	}

	public void setDevice(String device) {
		this.device = device;
	}
}
