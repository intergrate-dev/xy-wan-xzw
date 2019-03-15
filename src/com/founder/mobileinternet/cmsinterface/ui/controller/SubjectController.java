package com.founder.mobileinternet.cmsinterface.ui.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.founder.mobileinternet.cmsinterface.pojo.cms.SubjectQAInfo;
import com.founder.mobileinternet.cmsinterface.service.RedisManager;
import com.founder.mobileinternet.cmsinterface.service.cms.SubjectService;
import com.founder.mobileinternet.cmsinterface.util.CommonToolUtil;
import com.founder.mobileinternet.cmsinterface.util.XYComment;
@XYComment(name="互动话题")
@Controller
public class SubjectController {
	@Autowired
	private SubjectService subjectService;
	@Autowired
	private RedisManager redisManager;

	@XYComment(name="互动话题列表" ,comment="读互动话题的列表")
	@RequestMapping(value = { "subjectList" })
	@ResponseBody
	public void getSubjectList(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "siteID", defaultValue = "1") int siteID,
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "userID") Long userID) {

		String info = subjectService.getSubjectList(siteID, page,
				userID);
		CommonToolUtil.outputJson(request, response, info);
	}

	@XYComment(name="分类的互动话题列表" ,comment="按分类读互动话题的列表")
	@RequestMapping(value = { "subjectListWithCat" })
	@ResponseBody
	public void getSubjectListWithCat(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "siteID", defaultValue = "1") int siteID,
			@RequestParam(value = "catID") int catID,
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "userID") Long userID) {

		String info = subjectService.getSubjectListWithCat(siteID, catID,
				page,userID);
		CommonToolUtil.outputJson(response, info);
	}
	@XYComment(name="我发起的互动话题" ,comment="读我发起的互动话题的列表")
	@RequestMapping(value = { "mySubject" })
	@ResponseBody
	public void getmySubject( HttpServletRequest request,
							  HttpServletResponse response,
							  @RequestParam(value = "siteID", defaultValue = "1") int siteID,
							  @RequestParam(value = "userID") Long userID,
							  @RequestParam(value = "page", defaultValue = "0") int page) {
		String info = subjectService.getmySubject(siteID, userID, page);
		CommonToolUtil.outputJson(response, info);
	}

	@XYComment(name="我关注的互动话题" ,comment="读我关注的互动话题的列表")
	@RequestMapping(value = { "mySubjectSubscribe" })
	@ResponseBody
	public void getmySubjectSubscribe(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "siteID", defaultValue = "1") int siteID,
			@RequestParam(value = "userID") Long userID,
			@RequestParam(value = "page", defaultValue = "0") int page) {

		String info = subjectService.getmySubjectSubscribe(siteID, userID, page);
		CommonToolUtil.outputJson(response, info);
	}

	@XYComment(name="互动话题详情" ,comment="读单个互动话题")
	@RequestMapping(value = { "getSubject" })
	@ResponseBody
	public void getSubject(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "siteID", defaultValue = "1") int siteID,
			@RequestParam(value = "id") int id) {

		String info = subjectService.getSubject(siteID, id);
		CommonToolUtil.outputJson(request,response, info);
	}

	@XYComment(name="互动话题提交" ,comment="将提交的互动话题延迟入库后保存")
	@RequestMapping(value = "ask", method = RequestMethod.POST)
	@ResponseBody
	public void ask(SubjectQAInfo sQAInfo, HttpServletRequest request,
			HttpServletResponse response) {
		boolean info = redisManager.setUpIfNotExist(request.getRemoteAddr());
		if (info) {
			//加IP
			sQAInfo.setIpaddress(request.getRemoteAddr());
			info=subjectService.comitsQA(sQAInfo);
		}
		CommonToolUtil.outputJson(response, info);
	}

	@XYComment(name="话题的最热问题列表" ,comment="按评论数倒叙读话题的最热问题列表")
	@RequestMapping(value = { "questionListHot" })
	@ResponseBody
	public void getQuestionListHot(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "siteID", defaultValue = "1") int siteID,
			@RequestParam(value = "subjectID") int subjectID,
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "count", defaultValue = "20") int count) {
		String info = subjectService.getQuestionListHot(siteID, subjectID,page);
		CommonToolUtil.outputJson(request, response, info);
	}

	@XYComment(name="话题的问题列表" ,comment="读话题的问题列表")
	@RequestMapping(value = { "questionList" })
	@ResponseBody
	public void getQuestionList(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "siteID", defaultValue = "1") int siteID,
			@RequestParam(value = "subjectID") int subjectID,
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "count", defaultValue = "20") int count) {
		String info = subjectService.getQuestionList(siteID, subjectID, page);
		CommonToolUtil.outputJson(request, response, info);
	}
	@XYComment(name="话题的问题详情" ,comment="读话题的单个问题详情")
	@RequestMapping(value = { "questionDetail" })
	@ResponseBody
	public void getQuestionDetail(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "siteID",defaultValue = "1") int siteID,
			int fileId){
		String info =subjectService.getQuestionDetail(siteID, fileId);
		CommonToolUtil.outputJson(request, response, info);
	}

	@XYComment(name="我的问题列表" ,comment="读我的问题列表")
	@RequestMapping(value = { "myQuestion" })
	@ResponseBody
	public void getMyQuestion(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "siteID", defaultValue = "1") int siteID,
			@RequestParam(value = "userID") Long userID,
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "count", defaultValue = "20") int count) {
		String info = subjectService.MyQuestion(siteID, userID, page);
		CommonToolUtil.outputJson(response, info);
	}
}
