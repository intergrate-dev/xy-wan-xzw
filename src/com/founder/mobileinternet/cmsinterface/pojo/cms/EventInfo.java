/*
 *
 * $Revision: 1 $
 * $History: EventInfo.java $
 * 
 * Copyright (c) 2006,北大方正电子有限公司数字媒体开发部
 * All rights reserved.
 */
package com.founder.mobileinternet.cmsinterface.pojo.cms;

public class EventInfo {

	private int siteID = 1;
	// (稿件ID/评论ID/论坛帖子ID)
	private int id;
	// (点赞类型：0—稿件点赞；1—评论点赞；2—论坛点赞；6-稿件行业分类点击)
	private int type;
	// (事件类型：0—稿件点击；1—点赞)
	private int eventType;
	// (用户ID（会员ID）)
	private int userID;
	// (用户其它标识（非注册用户时的标识，如移动设备号）)
	private String userOtherID;
	private int channel =2;

	public int getSiteID() {
		return siteID;
	}

	public void setSiteID(int siteID) {
		this.siteID = siteID;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getEventType() {
		return eventType;
	}

	public void setEventType(int eventType) {
		this.eventType = eventType;
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public String getUserOtherID() {
		return userOtherID;
	}

	public void setUserOtherID(String userOtherID) {
		this.userOtherID = userOtherID;
	}

	public int getChannel() {
		return channel;
	}

	public void setChannel(int channel) {
		this.channel = channel;
	}

}
