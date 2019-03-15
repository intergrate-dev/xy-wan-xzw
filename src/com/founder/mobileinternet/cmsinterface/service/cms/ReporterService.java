package com.founder.mobileinternet.cmsinterface.service.cms;

public interface ReporterService {
	/**
	 * 获取记者文章数和粉丝数
	 * @param id 记者ID
	 * @param siteID 
	 * @return
	 */
	public String getAuthorCount(int id, int siteID);

	/**
	 * 关注
	 * @param siteID
	 * @param userID  用户ID
	 * @param userName 用户名（关注者）
	 * @param authorID 记者ID
	 * @param authorName 记着名（被关注者）
	 * @return
	 */
	public boolean myAuthor(int siteID,int userID,
			String userName,int authorID,String authorName);

	/**
	 * 关注的记者列表
	 * @param id 用户ID
	 * @param start
	 * @param count
	 * @param siteID 
	 * @return
	 */
	public String myAuthorView(int siteID,int id,int page,int lastFileId);

	/**
	 * 取消关注
	 * @param siteID
	 * @param userID
	 * @param authorID
	 * @return
	 */
	public boolean myAuthorCancel(int siteID,int userID,int authorID);

	/**
	 * 判断是否关注
	 * @param userID
	 * @param authorID
	 * @param siteID 
	 * @return
	 */
	public boolean isAttention(int userID,int authorID, int siteID);
}
