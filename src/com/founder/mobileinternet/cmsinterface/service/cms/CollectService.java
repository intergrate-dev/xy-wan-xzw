package com.founder.mobileinternet.cmsinterface.service.cms;

public interface CollectService {
	public String getCollectId(String userId);

	public String getCollectIds(String userId);

	public String getCollect(String userId, String articleId, String articleName);

	public String delCollect(String userId, String articleId);

	public String checkCollection(String userId, String articleId);
}
