package com.founder.mobileinternet.cmsinterface.service.cms;

public interface ExamineService {
	/**
	 * 同步交汇点栏目或原创稿件
	 * @param page
	 * @param size
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public String docSync(int siteID,int page,int size,String startTime,String endTime);
	/**
	 * 获取栏目
	 * @return
	 */
	public String columnSync(int siteID);
}
