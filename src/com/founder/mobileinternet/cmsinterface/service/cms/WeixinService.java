package com.founder.mobileinternet.cmsinterface.service.cms;

public interface WeixinService {

	String getWxAccounts(String data, int userID);

	String getWxGroupArticles(String data, int userID);

	String getWxGroupArticleDetail(String data, int userID);
	String getWxGroupArticleDetailUPOrDown(String data, int userID);

	String transferGroup(int userID, String data);

	String rejectGroup(int userID, String data);

	String transferGroupOne(int userID, String data);

	String rejectGroupOne(int userID, String data);

}
