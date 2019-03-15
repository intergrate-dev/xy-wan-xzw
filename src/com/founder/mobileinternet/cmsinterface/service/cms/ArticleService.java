package com.founder.mobileinternet.cmsinterface.service.cms;

import java.io.InputStream;

public interface ArticleService {
	public String getColumnArticles(int columnId, int start, int count, int lastFileId, int siteID,int typeScreen);
	
	public String articleHot(int siteID, int lastFileId, int start, int type);
	public String getArticleContent(int paramInt, int colID, int siteID);
	
	public String search(int paramInt1, String paramString, int paramInt2, int paramInt3, int paramInt4);
	public String searchAll(int paramInt1, String paramString, int paramInt2, int paramInt3, int siteID);
	public String getAuthorArticles(int id, int start, int count, int lastFileId, int siteID);
	
	public String articleListRefresh(int coID, int colLibID, long colID, int page, int siteID);
	public String tagArticles(String tag, int page, int siteID);
	public String authorArticles(String author, int page, int siteID);
	public String tradeArticles(String tradeIDs, int page, int siteID);

	String getModuleView(String id, int userID);
	
	public String myCollectionList(String articleIDs,int ch);

    public String getPubArticles(int userID,String data);

    public String addArticle(int userID, String data);

	public String getArticleDetail(String data, int userID);

	public InputStream getImage(String path);

	public String getTabs(int userID,String data);

	public String pubSearch(String data);

	public String revokeSearch(String data);

	public String flowRecordList(String data);

	public String pushApp(String data, int userID);

	String delete(int userID, String data);

	public String getAuditArticles(String data, int userID);

    String publishArticle(int userID, String data);

	public String transfer(int userID, String data);

	public String reject(int userID, String data);

    String revokeArticle(int userID, String data);

	String revokeDelete(int userID, String data);

	String getNewArticleHotList(int siteID, int articleType, int timeType, int orderType, int channel);

	String getSubColArticles(int columnID, int siteID);
	
	String getTopicArticles(int topicId, int start, int count, int lastFileId, int siteID,int type,int channel);

	public String getArticleCountInfo(long aid, long cid);

	String addArticleForBig(String data);

	String searchWebArticles(int columnId, String key, int start, int count, int siteID);
	
	String deleteArticleForBig(String data);
}