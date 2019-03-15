package com.founder.mobileinternet.cmsinterface.service.cms;

import com.founder.mobileinternet.cmsinterface.pojo.cms.ActivityInfo;

public interface ActiveService {
    String activityList(int siteID,  int page, int lastFileId);
    public String commitActivity(ActivityInfo activityInfo);
    public String activityDetail(int siteID,int fileId, int isDebug);
//    public String Revise(String info ,String userId);
    public String entryList(int siteID,int fileId);
    public String getMyActivityList(int userID, int page, int siteID);

}
