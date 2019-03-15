package com.founder.mobileinternet.cmsinterface.service.cms;

public interface ColumnService {
	public String getColumnAll(int siteID, int columnID);
	public String getColumns(int siteID, int columnID);
	public String getColumnIds(int siteID, String columnID);
	public String getColumn(int siteID, int columnID);
	
	/**
	 * 读所有站点的所有栏目信息，用于与第三方同步的场合。
     * 无缓存。返回XML。
	 */
	public String getNodeTree();

	public String getPubCols(int userID, String data);

	public String colIsOp(int userID, String data);
	
	public String colSearch(int userID, String data);

	String getAuditCols(int userID, String data);

    String getColumnsAllEasy(String data);
}
