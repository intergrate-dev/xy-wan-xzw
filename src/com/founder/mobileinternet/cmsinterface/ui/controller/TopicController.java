package com.founder.mobileinternet.cmsinterface.ui.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.founder.mobileinternet.cmsinterface.pojo.cms.TopicInfo;
import com.founder.mobileinternet.cmsinterface.service.RedisManager;
import com.founder.mobileinternet.cmsinterface.service.cms.TopicService;
import com.founder.mobileinternet.cmsinterface.util.CommonToolUtil;
import com.founder.mobileinternet.cmsinterface.util.XYComment;

@XYComment(name="订阅")
@Controller
public class TopicController {
	@Autowired
	private TopicService topicService;
	@Autowired
	private RedisManager redisManager;

	@XYComment(name="订阅")
	@RequestMapping(value = "topicSub")
	@ResponseBody
	public void topicSub(TopicInfo topicInfo, HttpServletRequest request,
			HttpServletResponse response) {
		boolean info= /*= redisManager.setSubIfNotExist(request.getRemoteAddr());
		if(info) info =*/ topicService.topicSub(topicInfo);
		CommonToolUtil.outputJson(request,response, info+"");
	}
	
	@XYComment(name="取消订阅")
	@RequestMapping(value = "topicSubCancel")
	@ResponseBody
	public void topicSubCancel(TopicInfo topicInfo, HttpServletRequest request,
			HttpServletResponse response) {
		boolean info = redisManager.setSubIfNotExist(request.getRemoteAddr());
		if(info) info = topicService.topicSubCancel(topicInfo);
		CommonToolUtil.outputJson(request,response, info+"");
	}
	
	@XYComment(name="订阅列表", comment="读用户的订阅栏目，并且读出每个栏目的头几条稿件")
	@RequestMapping(value = "subcribeView")
	@ResponseBody
	public void subcribeView(
			@RequestParam(value = "userID", defaultValue = "-1") long userID,
			@RequestParam(value = "columnId", defaultValue = "0") int columnId,
			@RequestParam(value = "count", defaultValue = "3") int count,
			@RequestParam(value="siteID",defaultValue="1")int siteID,
			@RequestParam(value="device",required=true)String device,
			HttpServletRequest request, HttpServletResponse response){
		String info = topicService.subcribeView(siteID, userID, columnId, count, device);
		CommonToolUtil.outputJson(request,response, info);
	}
	
	@XYComment(name="订阅列表（新版）", comment="读用户的订阅栏目，并且读出每个栏目的头几条稿件。带推荐模块")
	@RequestMapping(value = "subcribeViewV51")
	@ResponseBody
	public void subcribeViewV51(
			@RequestParam(value = "userID", defaultValue = "-1") long userID,
			@RequestParam(value = "columnId", defaultValue = "0") int columnId,
			@RequestParam(value = "count", defaultValue = "3") int count,
			@RequestParam(value="siteID",defaultValue="1")int siteID,
			@RequestParam(value="device",required=true)String device,
			HttpServletRequest request, HttpServletResponse response) {
		String info = topicService.subcribeViewV51(siteID, userID, columnId, count, device);
		CommonToolUtil.outputJson(request,response, info);
	}
	
	@XYComment(name="订阅栏目搜索", comment="在订阅根栏目下按名称搜索三级栏目")
	@RequestMapping(value = "getSubcribeCols")
	@ResponseBody
	public void getSubcribeCols(
			@RequestParam("key") String key, 
			@RequestParam("colID") long colID,
			@RequestParam(value = "siteID", defaultValue = "1") int siteID,
			HttpServletRequest request, HttpServletResponse response) {
		String info = topicService.getSubcribeCols(siteID, key, colID);
		CommonToolUtil.outputJson(request,response, info);
	}

	@XYComment(name="订阅列表（栏目列表遍历使用）", comment="只返回栏目ID")
	@RequestMapping(value = "subcribeXY")
	@ResponseBody
	public void subcribeXY(
			@RequestParam(value = "userID", defaultValue = "-1") long userID,
            @RequestParam(value = "columnId", defaultValue = "0") int columnId,
			@RequestParam(value="siteID",defaultValue="1")int siteID,
			@RequestParam(value="device",required=true)String device,
			HttpServletRequest request, HttpServletResponse response) {
		String info = topicService.subcribeXY(siteID, columnId, userID, device);
		CommonToolUtil.outputJson(request,response, info);
	}
}