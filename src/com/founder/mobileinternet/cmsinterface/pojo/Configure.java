package com.founder.mobileinternet.cmsinterface.pojo;

public class Configure {
	private String innerApiUrl; //内网api
	private String ssoUrl;
	private String websiteUrl; //网站Url，做跨域允许
	
	private String sms_url;
	private String sms_userName;
	private String sms_password;

	private String timer_lc;
	private String timer_up;
	private String timer_sub;
	private String timer_event;

	private int timerLc = -1;
	private int timerUp = -1;
	private int timerSub = -1;
	private int timerEvent = -1;
	
	public String getWebsiteUrl() {
		return websiteUrl;
	}

	public void setWebsiteUrl(String websiteUrl) {
		this.websiteUrl = websiteUrl;
	}

	/** 内网api根路径 */
	public String getInnerApiUrl() {
		return innerApiUrl;
	}

	public void setInnerApiUrl(String amucServerUrl) {
		this.innerApiUrl = amucServerUrl;
	}

	/** 内网api的CMS功能根路径，以/api/app为前缀 */
	public String getAppServerUrl() {
		return getInnerApiUrl() + "/api/app/";
	}

	/** 移动采编内网api的CMS功能根路径，以/api/newMobile为前缀 */
	public String getNewAppServerUrl() {
		return getInnerApiUrl() + "/api/newMobile/";
	}

	public String getSsoUrl() {
		return ssoUrl;
	}

	public void setSsoUrl(String ssoSaasUrl) {
		this.ssoUrl = ssoSaasUrl;
	}

	public String getSms_url() {
		return sms_url;
	}

	public void setSms_url(String sms_url) {
		this.sms_url = sms_url;
	}

	public String getSms_userName() {
		return sms_userName;
	}

	public void setSms_userName(String sms_userName) {
		this.sms_userName = sms_userName;
	}

	public String getSms_password() {
		return sms_password;
	}

	public void setSms_password(String sms_password) {
		this.sms_password = sms_password;
	}

	public String getTimer_lc() {
		return timer_lc;
	}

	public void setTimer_lc(String timer_lc) {
		this.timer_lc = timer_lc;
	}

	public String getTimer_up() {
		return timer_up;
	}

	public void setTimer_up(String timer_up) {
		this.timer_up = timer_up;
	}

	public String getTimer_sub() {
		return timer_sub;
	}

	public void setTimer_sub(String timer_sub) {
		this.timer_sub = timer_sub;
	}

	public String getTimer_event() {
		return timer_event;
	}

	public void setTimer_event(String timer_event) {
		this.timer_event = timer_event;
	}

	public int getTimerLc() {
		if (timerLc < 0)
			timerLc = Integer.parseInt(timer_lc);
		return timerLc;
	}

	public int getTimerUp() {
		if (timerUp < 0)
			timerUp = Integer.parseInt(timer_up);
		return timerUp;
	}

	public int getTimerSub() {
		if (timerSub < 0)
			timerSub = Integer.parseInt(timer_sub);
		return timerSub;
	}

	public int getTimerEvent() {
		if (timerEvent < 0)
			timerEvent = Integer.parseInt(timer_event);
		return timerEvent;
	}
}