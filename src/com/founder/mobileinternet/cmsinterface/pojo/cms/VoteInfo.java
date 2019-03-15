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

public class VoteInfo {

	// （站点ID）
	private int siteID = 1;
	// （投票ID）
	private long voteID;
	// 问题ID:
	private long questionID;
	// 选项ID:
	private long optionID;
	// 投票结果["问题ID,选项ID","问题ID,选项ID","问题ID,选项ID"]
	private List<String> voteResult = new ArrayList<String>();
	// （经度）
	private double longitude;
	// （纬度）
	private double latitude;
	// （地理位置文字描述）
	private String location;
	// (注册用户ID): 6,
	private int userID;
	// （注册用户名）”:
	private String userName;
	// (用户其它标识（非注册用户时的标识，如移动设备号）): 123
	private String userOtherID;

	public int getSiteID() {
		return siteID;
	}

	public void setSiteID(int siteID) {
		this.siteID = siteID;
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

	public long getVoteID() {
		return voteID;
	}

	public void setVoteID(long voteID) {
		this.voteID = voteID;
	}

	public long getQuestionID() {
		return questionID;
	}

	public void setQuestionID(long questionID) {
		this.questionID = questionID;
	}

	public long getOptionID() {
		return optionID;
	}

	public void setOptionID(long optionID) {
		this.optionID = optionID;
	}

	public List<String> getVoteResult() {
		return voteResult;
	}

	public void setVoteResult(List<String> voteResult) {
		this.voteResult = voteResult;
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
}
