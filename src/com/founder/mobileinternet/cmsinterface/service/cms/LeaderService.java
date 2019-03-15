package com.founder.mobileinternet.cmsinterface.service.cms;

public interface LeaderService {
	/**
	 * 获取领导人列表
	 * @param start
	 * @param count
	 * @param siteID 
	 * @return
	 */
	public String getLeaderList(int start,int count, int siteID);
	/**
	 * 地区领导人列表
	 * @param regionID
	 * @param siteID 
	 * @return
	 */
	public String getRegionLeaderList(int regionID, int siteID);
	/**
	 * 获取领导人详细信息
	 * @param id
	 * @param siteID 
	 * @return
	 */
	public String getLeaderDetail(int id, int siteID);
}
