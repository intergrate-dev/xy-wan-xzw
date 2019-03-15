package com.founder.mobileinternet.cmsinterface.service.cms;

import com.founder.mobileinternet.cmsinterface.pojo.cms.NisQaInfo;

/**
 * Created by isaac_gu on 2016/10/24.
 */
public interface QAService {
    String qaList(int siteID,int groupId, int lastFileId,int page);
    public boolean commitPolitics(NisQaInfo nisQaInfo);
    public String qaDetail(int siteID,int fileId);
    public String myQA(int userID,int page,int siteID);
}
