package com.founder.mobileinternet.cmsinterface.pojo.cms;

import com.founder.mobileinternet.cmsinterface.util.XYComment;

public class SubjectQAInfo {

	// （站点ID）
	private int siteID = 1;
	// (用户ID),
	private int userID;
	// （注册用户名）”:
	private String userName;
	// 用户图像地址
	private String pic;
	// -问题内容--
	private String question;
	// （选题ID）”:
	private long subjectID;
	// 话题标题
	private String topic;
	// （经度）
	private double longitude;
	// （纬度）
	private double latitude;
	// （地理位置文字描述）
	private String location;
	// 设备号
	private String device;

	@XYComment(name = "IP，自动获取")
	private String ipaddress;

	public int getSiteID() {
		return siteID;
	}

	public void setSiteID(int siteID) {
		this.siteID = siteID;
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

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public long getSubjectID() {
		return subjectID;
	}

	public void setSubjectID(long subjectID) {
		this.subjectID = subjectID;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
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

	public String getDevice() {
		return device;
	}

	public void setDevice(String device) {
		this.device = device;
	}

	public String getIpaddress() {
		return ipaddress;
	}

	public void setIpaddress(String ipaddress) {
		this.ipaddress = ipaddress;
	}
}
