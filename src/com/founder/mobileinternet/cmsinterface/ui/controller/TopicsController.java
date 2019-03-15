package com.founder.mobileinternet.cmsinterface.ui.controller;

import com.founder.mobileinternet.cmsinterface.pojo.cms.TopicInfo;
import com.founder.mobileinternet.cmsinterface.service.cms.TopicsService;
import com.founder.mobileinternet.cmsinterface.util.CommonToolUtil;
import com.founder.mobileinternet.cmsinterface.util.XYComment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@XYComment(name="话题")
@Controller
public class TopicsController {

    @Autowired
    private TopicsService topicsService;

    @XYComment(name="热点话题列表")
    @RequestMapping(value = "hotTopics")
    @ResponseBody
    public void hotTopics(int siteID, HttpServletRequest request,
                         HttpServletResponse response) {
        String info= topicsService.hotTopics(siteID);
        CommonToolUtil.outputJson(request,response, info);
    }

    @XYComment(name="话题列表")
    @RequestMapping(value = "topicsByGroup")
    @ResponseBody
    public void topicsByGroup(int siteID, HttpServletRequest request,
                          HttpServletResponse response) {
        String info= topicsService.topicsByGroup(siteID);
        CommonToolUtil.outputJson(request,response, info);
    }
    
    @XYComment(name="网站话题列表")
    @RequestMapping(value = "topics")
    @ResponseBody
    public void topics(int siteID, int page, HttpServletRequest request,
                         HttpServletResponse response) {
        String info= topicsService.topics(siteID,page);
        CommonToolUtil.outputJson(request,response, info);
    }
    
    @XYComment(name="稿件话题列表")
    @RequestMapping(value = "articleTopics")
    @ResponseBody
    public void articleTopics(int articleID, int channel, HttpServletRequest request,
                              HttpServletResponse response) {
        String info= topicsService.articleTopics(articleID,channel);
        CommonToolUtil.outputJson(request,response, info);
    }

    @XYComment(name="稿件话题组话题列表")
    @RequestMapping(value = "articleTopicsByGroup")
    @ResponseBody
    public void articleTopicsByGroup(int articleID, int channel, int groupID, HttpServletRequest request,
                              HttpServletResponse response) {
        String info= topicsService.articleTopicsByGroup(articleID,channel,groupID);
        CommonToolUtil.outputJson(request,response, info);
    }

    @XYComment(name="网站话题组话题列表")
    @RequestMapping(value = "webTopicsByGroup")
    @ResponseBody
    public void topicsByGroup(int siteID, int page,int groupID, HttpServletRequest request,
                       HttpServletResponse response) {
        String info= topicsService.webTopicsByGroup(siteID,page,groupID);
        CommonToolUtil.outputJson(request,response, info);
    }

}
