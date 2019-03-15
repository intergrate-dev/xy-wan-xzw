package com.founder.mobileinternet.cmsinterface.service.cms;

import com.founder.mobileinternet.cmsinterface.pojo.cms.TopicInfo;

public interface TopicService {
	public boolean topicSub(TopicInfo topicInfo);
	public boolean topicSubCancel(TopicInfo topicInfo);
	public String subcribeView(int siteID,long userID,int columnId,int count,String device);
	public String subcribeViewV51(int siteID,long userID,int columnId,int count,String device);
	public String getSubcribeCols(int siteID,String key,long colID);

	String subcribeXY(int siteID, int columnId, long userID, String device);
}
