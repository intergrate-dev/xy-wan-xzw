package com.founder.mobileinternet.cmsinterface.service.cms;

import com.founder.mobileinternet.cmsinterface.pojo.cms.LogInfo;

public interface AppService {
	public String getConfig(int paramInt, int siteID);
	public String getCats(String code, String siteId, String keywords);
	public String getVerifiCode(String phoneNo);
//	public String getMyLive(int id, int start, int count, int siteID);
	/*public String getMyColumn(int id, int siteID);*/
	public String siteConf(int siteID);
	public boolean logInfo(LogInfo logInfo);
	String login(String user, String pwd);
	/**
	 * 获取app更新
	 */
	public String getAppInfo(String appKey,String versionCode,String channel);

	String logout(int loginID, long time, String sign, String url );

	String message(int page, int siteID);

	String userIsExist(String user);

	String getUrl();
	
	String myInfo(int userID);

    String qrCodeLogin(String user, String token);

	String getJsApiTicket();
}
