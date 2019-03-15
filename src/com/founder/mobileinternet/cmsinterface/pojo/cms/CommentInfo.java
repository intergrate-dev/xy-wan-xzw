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

public class CommentInfo {
	private int siteID = 1;// 站点ID
	
	@XYComment(name="评论对象（稿件/直播/活动等）的ID")
	private long rootID;
	
	private String content;
	
	@XYComment(name="来源类型", comment="0稿件，1直播，3数字报稿件，4互动话题问答，5互动问答，6活动")
	private int sourceType = 0;
	
	@XYComment(name="直播员类型", comment="直播员提交评论，作为直播员类型")
	private Integer type = 0;
	
	@XYComment(name="评论渠道", comment="默认是2移动App，0是网站，1是触屏")
	private int channel =2;
	
	@XYComment(name="上传的图片", comment="图片url，可多个")
	private List<String> imgUrl = new ArrayList<String>();
	
	@XYComment(name="上传的视频", comment="与图片一样，目前没使用")
	private List<String> videoUrl = new ArrayList<String>();
	
	private int userID = -1;// 用户ID
	private String userName;// 用户名

	public String getUserOtherID() {
		return userOtherID;
	}

	public void setUserOtherID(String userOtherID) {
		this.userOtherID = userOtherID;
	}

	private String userOtherID;

	@XYComment(name="父评论ID")
	private long parentID = -1;
	
	private double longitude;// （经度）
	private double latitude;// （纬度）
	private String location = "";// （地理位置文字描述）
	
	@XYComment(name="IP，自动获取")
	private String ipaddress;//IP地址
	
	public int getSourceType() {
		return this.sourceType;
	}

	public void setSourceType(int sourceType) {
		this.sourceType = sourceType;
	}

	public int getChannel() {
		return channel;
	}

	public void setChannel(int channel) {
		this.channel = channel;
	}

	public int getSiteID() {
		return siteID;
	}

	public void setSiteID(int siteID) {
		this.siteID = siteID;
	}

	public void setRootID(int rootID) {
		this.rootID = rootID;
	}

	public void setParentID(int parentID) {
		this.parentID = parentID;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
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

	public String getIpaddress() {
		return ipaddress;
	}

	public void setIPAddress(String IPAddress) {
		this.ipaddress = IPAddress;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		if(type==null){
			type=0;
		}
		this.type = type;
	}
}
