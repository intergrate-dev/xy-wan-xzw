package com.founder.mobileinternet.cmsinterface.service.cms;


public interface EventService {
    public String eventTypes();
    public String scoreRuleList(String source,String tc);
    public String convert(String source,String tc,String memberID,String info);
    public String event1(String eType ,String tc,String member,String time,String sign,String siteID);
    public String event(String info ,String tc,String member,String time,String sign,String siteID);
}
