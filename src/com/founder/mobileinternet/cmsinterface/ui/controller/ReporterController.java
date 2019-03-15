package com.founder.mobileinternet.cmsinterface.ui.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.founder.mobileinternet.cmsinterface.service.RedisManager;
import com.founder.mobileinternet.cmsinterface.service.cms.ReporterService;
import com.founder.mobileinternet.cmsinterface.util.CommonToolUtil;
import com.founder.mobileinternet.cmsinterface.util.XYComment;


@XYComment(name="记者")
@Controller
public class ReporterController {
	@Autowired
	private ReporterService reporterService;
	@Autowired
	private RedisManager redisManager;

    @XYComment(name="读记者文章数", comment="获取记者文章数")
	@RequestMapping(value = { "authorCount" })
	@ResponseBody
	public void authorCount(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value="siteId", defaultValue="1") int siteID,
            @XYComment(name="记者id")
			@RequestParam("id") int id){
		String info = this.reporterService.getAuthorCount(id, siteID);
		CommonToolUtil.outputJson(request,response, info);
	}

    @XYComment(name="关注记者", comment="提交用户关注的记者信息")
	@RequestMapping(value = { "myAuthor" },method = { RequestMethod.POST })
	@ResponseBody
	public void myAuthor(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="siteId", defaultValue="1") int siteID,
			@RequestParam("userID") int userID, @RequestParam("userName") String userName,
			@RequestParam("authorID") int authorID, @RequestParam("authorName") String authorName){
		boolean info = redisManager.setSubIfNotExist(request.getRemoteAddr());
		if(info) info = reporterService.myAuthor(siteID, userID, userName, authorID, authorName);
		CommonToolUtil.outputJson(request,response, info+"");
	}


    @XYComment(name="我的记者", comment="获取用户关注的记者列表")
	@RequestMapping(value = { "myAuthorView" })
	@ResponseBody
	public void myAuthorView(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="id") int id,
			@RequestParam(value="page",required = false) int page,
			@RequestParam(value="lastFileId", defaultValue="0") int lastFileId,
			@RequestParam(value="siteId", defaultValue="1") int siteID){
    	
		String info = this.reporterService.myAuthorView(siteID, id, page, lastFileId);
		CommonToolUtil.outputJson(request,response, info);
	}

    @XYComment(name="取消关注记者", comment="用户取消关注记者接口")
	@RequestMapping(value = { "myAuthorCancel" },method = { RequestMethod.POST })
	public void myAuthorCancel(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="siteId", defaultValue="1") int siteID,
			@RequestParam("userID")int userID,
			@RequestParam("authorID")int authorID){
		boolean info = redisManager.setSubIfNotExist(request.getRemoteAddr());
		if(info) info = this.reporterService.myAuthorCancel(siteID, userID, authorID);
		CommonToolUtil.outputJson(request,response, info+"");
	}

    @XYComment(name="是否关注", comment="判断用户是否关注该记者")
	@RequestMapping(value = { "isAttention" },method = { RequestMethod.POST })
	public void isAttention(@RequestParam(value="siteId", defaultValue="1") int siteID,
			@RequestParam(value="userID") Integer userID,
			@RequestParam(value="authorID") Integer authorID,
			HttpServletRequest request,HttpServletResponse response){
    	boolean info = this.reporterService.isAttention(userID, authorID, siteID);
    	CommonToolUtil.outputJson(request,response, info+"");
	}
}
