package com.founder.mobileinternet.cmsinterface.ui.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.founder.mobileinternet.cmsinterface.service.cms.PaperService;
import com.founder.mobileinternet.cmsinterface.util.CommonToolUtil;
import com.founder.mobileinternet.cmsinterface.util.XYComment;

@XYComment(name="数字报")
@Controller
public class PaperController {
	@Autowired
	private PaperService paperService;
	


	@XYComment(name="读所有报刊列表", comment="获取站点下所有报刊列表")
	@RequestMapping(value = { "getPapers" })
	@ResponseBody
	public void getPapers(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="siteID", defaultValue="1") int siteID){
		String info = paperService.getPapers(siteID);
		CommonToolUtil.outputJson( request,response, info);
	}

	@XYComment(name="读刊期列表", comment="获取某个报刊下最近N期刊期")
	@RequestMapping(value = { "getPaperDates" })
	@ResponseBody
	public void getPaperDates(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="siteID", defaultValue="1") int siteID,
			@XYComment(name="报纸ID", comment="报纸ID")
			@RequestParam("id")long id,@RequestParam(value="start", defaultValue="0")int start,
			@RequestParam(value="count", defaultValue="60")int count){
		String info = paperService.getPaperDates(siteID, id,start,count);
		CommonToolUtil.outputJson( request,response, info);
	}

	@XYComment(name="读版面列表", comment="获取某个刊期下所有版面信息")
	@RequestMapping(value = {"getPaperLayouts"})
	@ResponseBody
	public void getPaperLayouts(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="siteID", defaultValue="1") int siteID,
			@XYComment(name="刊期", comment="刊期，如：20160101")
			@RequestParam("date") String date,
			@XYComment(name="报纸ID", comment="报纸ID")
			@RequestParam("id")int id){
		//String date = request.getParameter("date");
		String info = paperService.getPaperLayouts(siteID, id, date);
		CommonToolUtil.outputJson( request,response, info);
	}
	@XYComment(name="读数字报稿件列表", comment="获取某个版面下的所有稿件列表")
	@RequestMapping(value ={"getPaperArticles"})
	@ResponseBody
	public void getPaperArticles(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="siteID", defaultValue="1") int siteID,
			@XYComment(name="版面ID", comment="版面ID")
			@RequestParam("id")int id){
		String info = paperService.getPaperArticles(siteID, id);
		CommonToolUtil.outputJson( request,response, info);
	}
	@XYComment(name="读数字报稿件详细", comment="获取数字报稿件详细")
	@RequestMapping(value ={"getPaperArticle"})
	@ResponseBody
	public void getPaperArticle(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="siteID", defaultValue="1") int siteID,
			@XYComment(name="数字报稿件id", comment="数字报稿件id")
			@RequestParam("id")long id){
		
		String info = paperService.getPaperArticle(siteID, id);
		CommonToolUtil.outputJson( request,response, info);
	}
	
}
