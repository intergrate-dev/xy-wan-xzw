package com.founder.mobileinternet.cmsinterface.service.cms;


public interface InviteCodeService {
    public String codeImeiRecord(String code,String imei,String uid,String name,String siteID);
    public String getInviteCode(String uid,String siteID);
    public String getInviteRecord(String uid,String siteID);
    public String siteConf(String siteID);
    public String logoConf(String siteID);
}
