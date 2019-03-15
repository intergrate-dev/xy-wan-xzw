package com.founder.mobileinternet.cmsinterface.service.cms;

public interface OriginalService {

	public String getOriAuditCats(String data, int userID);

	public String getOriginalArticles(String data, int userID);

	public String getOriginalDetail(String data, int userID);

	public String transferOriginal(int userID, String data);

	public String rejectOriginal(int userID, String data);
	public String rejectOriginalFir(int userID, String data);
	public String getDocId(String data, int userID);

}
