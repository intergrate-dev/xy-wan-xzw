package com.founder.mobileinternet.cmsinterface.service.cms;

import com.founder.mobileinternet.cmsinterface.pojo.cms.LiveInfo;
import com.founder.mobileinternet.cmsinterface.pojo.cms.LiveParam;

public interface LiveService {
	public String commitLive(LiveInfo forumInfo);
	public String liveView(int id, int index, int page, int lastFileId, int siteID);
	String liveStatus(LiveParam param);
	String liveComing(int siteId);

    String lives(long loginID, String time, String sign, String url,int siteID);

    String allLives(long loginID, String time, String sign, String url,int siteID,String status,int lastID);

	String queryLiveStatus(String requestUrl, int loginID, long time, String sign, String[] streamID);
}
