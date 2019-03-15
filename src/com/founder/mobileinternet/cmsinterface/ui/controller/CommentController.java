package com.founder.mobileinternet.cmsinterface.ui.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.founder.mobileinternet.cmsinterface.pojo.Configure;
import com.founder.mobileinternet.cmsinterface.pojo.cms.CommentInfo;
import com.founder.mobileinternet.cmsinterface.service.RedisManager;
import com.founder.mobileinternet.cmsinterface.service.cms.CommentService;
import com.founder.mobileinternet.cmsinterface.util.CommonToolUtil;
import com.founder.mobileinternet.cmsinterface.util.XYComment;

@XYComment(name="评论")
@Controller
public class CommentController {
	@Autowired
	private CommentService commentService;
	@Autowired
	private RedisManager redisManager;
	@Autowired
    private Configure configure;
	
    @XYComment(name="评论提交", comment="提交评论及回复")
	@RequestMapping(value = {"discuss", "discussDelay"}, method = RequestMethod.POST)
	@ResponseBody
	public void commitComment(CommentInfo commentInfo,
			HttpServletRequest request, HttpServletResponse response) {

    	String origin = request.getHeader("origin");
	    if(origin!=null&&configure.getWebsiteUrl().contains(origin)){
	    	response.addHeader("Access-Control-Allow-Origin",origin);
	    }
    	 response.setHeader("Access-Control-Allow-Method", "POST");  
		/*String timer = configure.getTimer_up();
		if(null != timer && !"0".equals(timer)) {
			info = redisManager.isNotIntercept("app.timer.up." + request.getRemoteAddr(),
					timer);
		}*/
		//加IP
		commentInfo.setIPAddress(request.getRemoteAddr());
	
		
		//放入Redis
		try {
			String value = new ObjectMapper().writeValueAsString(commentInfo);
			redisManager.addDelayDiscuss(value);
			CommonToolUtil.outputJson(response, true);
		} catch (Exception e) {
			e.printStackTrace();
			CommonToolUtil.outputJson(response, false);
		}
	}

    @XYComment(name="热门评论", comment="获取热门评论")
	@RequestMapping(value = "discussHot")
	@ResponseBody
	public void discussHot(
			@RequestParam("id") int articleID,
			@RequestParam(value="count", defaultValue="5") int count,
			@RequestParam(value="siteId", defaultValue="1") int siteID,
			HttpServletRequest request, HttpServletResponse response) {

		int lastFileId = CommonToolUtil.getIntValue(request.getParameter("lastFileId"), 0);
		String info = commentService.discussHot(articleID, count,
				CommonToolUtil.getIntValue(request.getParameter("source"), 0),
				lastFileId, siteID);
		CommonToolUtil.outputJson(request, response, info);
	}

    @XYComment(name="评论列表", comment="获取最新评论列表")
	@RequestMapping(value = "discussView")
	@ResponseBody
	public void discussView(
			@RequestParam("id") int articleId,
			@RequestParam(defaultValue = "0") int source,
			@RequestParam(value = "page") Integer page, 
			@RequestParam(defaultValue = "0") int lastFileId,
			@RequestParam(value="siteId", defaultValue="1") int siteID,
			@RequestParam(required = false) Integer start,
			HttpServletRequest request, HttpServletResponse response) {
		page = CommonToolUtil.getPage(start, page);
		String info = commentService.discussView(articleId, source, lastFileId, siteID, page, 0);
		CommonToolUtil.outputJson(request, response, info);
	}

    /**
     * @param isOrderByPraise 是否按照点赞数排序，直播评论的需求，默认按照点赞数排序(从大到小)
     */
    @XYComment(name="评论列表", comment="获取最新评论列表按照点赞数从多到少的顺序排序")
    @RequestMapping(value = "discussViewOrderByPraise")
    @ResponseBody
    public void discussViewOrderByPraise(
            @RequestParam("id") int articleId,
            @RequestParam(defaultValue = "0") int source,
            @RequestParam(value = "page") Integer page,
            @RequestParam(defaultValue = "0") int lastFileId,
            @RequestParam(value="siteId", defaultValue="1") int siteID,
            @RequestParam(required = false) Integer start,
            @RequestParam(defaultValue = "1") int isOrderByPraise,
            HttpServletRequest request, HttpServletResponse response) {
        page = CommonToolUtil.getPage(start, page);
        String info = commentService.discussViewOrderByPraise(articleId, source, lastFileId, siteID, page, isOrderByPraise);
        CommonToolUtil.outputJson(request, response, info);
    }

    @XYComment(name="评论列表（扁平列表）", comment="获取最新评论列表，不区分是不是评论的回复")
	@RequestMapping(value = "discussViewFlat")
	@ResponseBody
	public void discussViewFlat(
			@RequestParam("id") int articleId,
			@RequestParam(defaultValue = "0") int source,
			@RequestParam(value = "page") Integer page, 
			@RequestParam(defaultValue = "0") int lastFileId,
			@RequestParam(value="siteId", defaultValue="1") int siteID,
			HttpServletRequest request, HttpServletResponse response) {
		String info = commentService.discussView(articleId, source, lastFileId, siteID, page, 1);

        String origin = request.getHeader("origin");
        if(origin!=null&&configure.getWebsiteUrl().contains(origin)){
            response.addHeader("Access-Control-Allow-Origin",origin);
        }
		CommonToolUtil.outputJson(request, response, info);
	}

   @XYComment(name="评论回复列表", comment="获取评论的回复列表")
	@RequestMapping(value = "discussReply")
	@ResponseBody
	public void discussReply(
			@RequestParam("id") int id,
			@RequestParam(value="siteId", defaultValue="1") int siteID,
			@RequestParam(value = "page") Integer page, 
			@RequestParam(required = false) Integer start,
			HttpServletRequest request, HttpServletResponse response) {

		//Integer page =request.getParameter("page")==null?null:Integer.valueOf(request.getParameter("page"));
		//Integer start = request.getParameter("start")==null?null:Integer.valueOf(request.getParameter("start"));
		
		int lastFileId = CommonToolUtil.getIntValue(request.getParameter("lastFileId"), 0);
		page=CommonToolUtil.getPage(start, page);
		String info = commentService.discussReply(id, lastFileId, siteID,page);

		CommonToolUtil.outputJson(request, response, info);
	}

    @XYComment(name="获取评论数")
	@RequestMapping(value = "discussCount")
	@ResponseBody
	public void discussCount(
			@RequestParam("id") int articleId,
			@RequestParam("type") int type, 
			@RequestParam(value="siteId", defaultValue="1") int siteID,
			@RequestParam(value="source", defaultValue="0") int source,
			HttpServletRequest request,
			HttpServletResponse response) {
		String info = commentService.discussCount(articleId, type,
				source, siteID);
		CommonToolUtil.outputJson(request, response, info);
	}

	@XYComment(name="我的评论", comment="获取我的评论")
	@RequestMapping(value = "myDiscuss")
	@ResponseBody
	public void getMyDiscuss(@RequestParam("userID") int userID,
			@RequestParam("page") int page,
			@RequestParam(value="siteID", defaultValue="1") int siteID,
			HttpServletRequest request,HttpServletResponse response)
			throws Exception {
		String info = commentService.getMyDiscuss( userID,page, siteID);

		CommonToolUtil.outputJson(request, response, info);
	}

	@XYComment(name="评论我的", comment="获取对我的评论的回复列表")
	@RequestMapping(value = "myDiscussReply")
	@ResponseBody
	public void getMyReply(@RequestParam("userID") int userID,
			@RequestParam("page") int page,
			@RequestParam(value="siteId", defaultValue="1") int siteID,
			HttpServletRequest request,HttpServletResponse response)
					throws Exception {

		String info = commentService.getMyDiscussReply( userID,page, siteID);
		CommonToolUtil.outputJson(request, response, info);
	}

    @XYComment(name="举报")
	@RequestMapping(value = "expose")
	@ResponseBody
	public void expose( HttpServletRequest request,
			@RequestParam("reason") String reason,
			@RequestParam("rootID") int rootID,
			@RequestParam("userID") int userID,
			@RequestParam("userName") String userName,
			@RequestParam(value="siteId", defaultValue="1") int siteID,
			@RequestParam(value="sourceType", defaultValue="0") int sourceType,
			@RequestParam(value="type", defaultValue="0") int type,
			HttpServletResponse response) {
		
		boolean info = commentService.expose(siteID,rootID,userID,userName,reason,sourceType,type);
		CommonToolUtil.outputJson(request, response, String.valueOf(info));
	}
    
    @XYComment(name="评论删除", comment="删除我的评论")
	@RequestMapping(value = "discussDelete")
	@ResponseBody
	public void discussDelete(@RequestParam("userID") int userID,
			@RequestParam("discussID") int discussID,
			@RequestParam(value="siteID", defaultValue="1") int siteID,
			HttpServletRequest request,HttpServletResponse response)
			throws Exception {
		boolean info = commentService.discussDelete(userID, discussID, siteID);

		CommonToolUtil.outputJson(request, response, String.valueOf(info));
	}
    
}