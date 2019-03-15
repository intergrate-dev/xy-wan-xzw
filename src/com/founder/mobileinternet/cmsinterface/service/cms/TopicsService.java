package com.founder.mobileinternet.cmsinterface.service.cms;

public interface TopicsService {
    public String hotTopics(int siteID);
    public String topicsByGroup(int siteID);
    String topics(int siteID,int page);
    String articleTopics(int articleID,int channel);
    String articleTopicsByGroup(int articleID, int channel, int groupID);

    String webTopicsByGroup(int siteID, int page, int groupID);
}
