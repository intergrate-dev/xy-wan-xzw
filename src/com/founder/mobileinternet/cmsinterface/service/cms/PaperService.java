package com.founder.mobileinternet.cmsinterface.service.cms;

public interface PaperService {
	/**
	 * 站点报纸列表
	 */
	public String getPapers(int siteID);
	/**
	 * 报纸期刊列表
	 */
	public String getPaperDates(int siteID,long id,int start,int count);
	/**
	 * 期刊版面列表
	 */
	public String getPaperLayouts(int siteID,int id,String date);
	/**
	 * 版面稿件列表
	 */
	public String getPaperArticles(int siteID,int id);
	/**
	 * 稿件详细内容
	 */
	public String getPaperArticle(int siteID,long id);
}
