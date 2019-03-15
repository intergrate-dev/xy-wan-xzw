package com.founder.mobileinternet.cmsinterface.ui.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.founder.mobileinternet.cmsinterface.pojo.Configure;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.founder.mobileinternet.cmsinterface.pojo.cms.ActivityInfo;
import com.founder.mobileinternet.cmsinterface.service.RedisManager;
import com.founder.mobileinternet.cmsinterface.service.cms.ActiveService;
import com.founder.mobileinternet.cmsinterface.util.CommonToolUtil;
import com.founder.mobileinternet.cmsinterface.util.XYComment;

@XYComment(name = "活动")
@Controller
public class ActiveController {
	@Autowired
	ActiveService acService;
	@Autowired
	private RedisManager redisManager;
    @Autowired
    private Configure configure;

	// 活动列表
	@XYComment(name = "活动列表", comment = "读栏目的活动列表")
	@RequestMapping(value = "activityList")
	public void activityList(
			@RequestParam(value = "siteID", defaultValue = "1") int siteID,
			@RequestParam(value = "lastFileId", defaultValue = "0") int lastFileId,
			@RequestParam(value = "page") Integer page, 
			@RequestParam(required = false) Integer start,
			// @RequestParam(defaultValue = "-1") String userId,
			HttpServletRequest request, HttpServletResponse response) {
		page = CommonToolUtil.getPage(start, page);

		String info = acService.activityList(siteID, page, lastFileId);
		// if (userId != null && !"".equals(userId)) {//获取活动点赞数功能，不需要了
		// info = acService.Revise(info, userId);
		// }
		CommonToolUtil.outputJson(request, response, info);
	}

	@XYComment(name = "活动详情", comment = "读取活动详情")
	@RequestMapping(value = "activityDetail")
	public void activityDetail(
			@RequestParam(value = "siteId", defaultValue = "1") int siteID, 
			@RequestParam(value = "fileId")int fileId,
			@RequestParam(defaultValue = "0") int isDebug,
			HttpServletRequest request, HttpServletResponse response) {
		String info = acService.activityDetail(siteID, fileId, isDebug);

		String origin = request.getHeader("origin");
		if(origin!=null&&configure.getWebsiteUrl().contains(origin)){
			response.addHeader("Access-Control-Allow-Origin",origin);
		}
		CommonToolUtil.outputJson(request, response, info);
	}

	// 保存活动报名
	@XYComment(name = "提交报名", comment = "保存活动报名")
	@RequestMapping(value = "saveActivity")
	public void saveActivity(ActivityInfo activityInfo, HttpServletRequest request, HttpServletResponse response) {
		// 放入Redis
		try {
			String value = new ObjectMapper().writeValueAsString(activityInfo);
			redisManager.addDelayEntry(value);
			CommonToolUtil.outputJson(request, response, "true");
		} catch (Exception e) {
			e.printStackTrace();
			CommonToolUtil.outputJson(request, response, "false");
		}
	}

	@XYComment(name = "活动报名名单", comment = "获取活动报名名单")
	@RequestMapping(value = "entryList")
	public void entryList(
			@RequestParam(value = "siteId", defaultValue = "1") int siteID,
			@RequestParam("fileId") int fileId, 
			HttpServletRequest request, HttpServletResponse response) {
		String info = acService.entryList(siteID, fileId);

        String origin = request.getHeader("origin");
        if(origin!=null&&configure.getWebsiteUrl().contains(origin)){
            response.addHeader("Access-Control-Allow-Origin",origin);
        }
		CommonToolUtil.outputJson(request, response, info);
	}

	@XYComment(name = "我的活动", comment = "获取我报名过的活动列表")
	@RequestMapping(value = "myActivityList")
	@ResponseBody
	public void myActivityList(
			@RequestParam("userID") int userID, 
			@RequestParam("page") int page,
			@RequestParam(value = "siteId", defaultValue = "1") int siteID,
			HttpServletRequest request, HttpServletResponse response) {
		String info = acService.getMyActivityList(userID, page, siteID);
		CommonToolUtil.outputJson(request, response, info);
	}
}
