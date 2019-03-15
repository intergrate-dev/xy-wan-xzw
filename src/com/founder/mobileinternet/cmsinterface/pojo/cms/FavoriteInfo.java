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

public class FavoriteInfo {
	private int userID = 0;
	private int siteID = 1;
	private long articleID;

	
	@XYComment(name="收藏对象类型", comment="0=稿件，1=直播，3=数字报，4=话题，5=问答，6=活动")
	private int type;
	
	@XYComment(name="渠道", comment="默认是2移动App，0是网站，1是触屏")
	private int channel = 2;
	
	private int cancel = 0;//为1，则做删除

	private String imgUrl="";

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public int getUserID() {
		return userID;
	}
	public void setUserID(int userID) {
		this.userID = userID;
	}
	public int getSiteID() {
		return siteID;
	}
	public void setSiteID(int siteID) {
		this.siteID = siteID;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public long getArticleID() {
		return articleID;
	}
	public void setArticleID(long articleID) {
		this.articleID = articleID;
	}
	public int getCancel() {
		return cancel;
	}
	public void setCancel(int cancel) {
		this.cancel = cancel;
	}
	public int getChannel() {
		return channel;
	}
	public void setChannel(int channel) {
		this.channel = channel;
	}
}
