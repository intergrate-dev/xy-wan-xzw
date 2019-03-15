package com.founder.mobileinternet.cmsinterface.service.cms;

import com.founder.mobileinternet.cmsinterface.pojo.cms.CommentInfo;

public interface CommentService {
	public boolean commitComment(CommentInfo commentInfo);
	public String discussHot(int articleId, int count, int source, int lastFileId, int siteID);
	public String discussView(int articleId, int source, int lastFileId, int siteID, int page, int flat);
	public String discussViewOrderByPraise(int articleId, int source, int lastFileId, int siteID, int page, int isOrderByPraise);
	public String discussReply(int id,  int lastFileId, int siteID,int page);
	public String discussCount(int articleId, int type, int source, int siteID);

	public String getMyDiscuss(int userID, int page, int siteID);
	public String getMyDiscussReply(int userID, int page, int siteID);
	public boolean expose(int siteID, int id, int userID, String userName, String reason,int sourceType,int type);
	public boolean discussDelete(int userID, int discussID, int siteID);
	}
