package com.founder.mobileinternet.cmsinterface.ui.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.founder.mobileinternet.cmsinterface.pojo.cms.RecommendInfo;
import com.founder.mobileinternet.cmsinterface.service.cms.ExamineService;
import com.founder.mobileinternet.cmsinterface.service.cms.LeaderService;
import com.founder.mobileinternet.cmsinterface.service.cms.RecommendService;
import com.founder.mobileinternet.cmsinterface.util.CommonToolUtil;
import com.founder.mobileinternet.cmsinterface.util.XYComment;

/**
 * 把几个项目中扩展的功能写在这里
 * @author Gong Lijie
 */
@XYComment(name="来自项目扩展的功能")
@Controller
public class OtherExtController {
	@Autowired
	private ExamineService examineService;
	@Autowired
	private RecommendService recommendService; 
	
	@Autowired
	private LeaderService leaderService;
	

	@XYComment(name="人物列表")
	@RequestMapping(value = { "leaderView" })
	@ResponseBody
	public void leaderList(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="siteId", defaultValue="1") int siteID,
			@RequestParam("start") int start,@RequestParam("count") int count){
		String info = this.leaderService.getLeaderList(start, count, siteID);
		CommonToolUtil.outputJson(response, info);
	}
	
	@XYComment(name="地区人物列表")
	@RequestMapping(value = { "regionleaderView" })
	@ResponseBody
	public void regionLeaderList(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="siteId", defaultValue="1") int siteID,
			@RequestParam("regionID") int regionID){
		String info = this.leaderService.getRegionLeaderList(regionID, siteID);
		CommonToolUtil.outputJson(response, info);
	}
	
	@XYComment(name="人物")
	@RequestMapping(value = { "leader" })
	@ResponseBody
	public void leaderDetail(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="siteId", defaultValue="1") int siteID,
			@RequestParam("id") int id){
		String info = this.leaderService.getLeaderDetail(id, siteID);
		CommonToolUtil.outputJson(response, info);
	}

	@XYComment(name="同步原创稿件")
	@RequestMapping(value = { "examine/syn" })
	@ResponseBody
	public void jhdSync(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="siteID", defaultValue="1") int siteID,
			@RequestParam(value="type") String type){
		String info = "";
		if("doc".equals(type)){
			Integer page = CommonToolUtil.getIntValue(request.getParameter("page"), 1);
			Integer size = CommonToolUtil.getIntValue(request.getParameter("size"), 20);
			
			
			String startTime = request.getParameter("startTime");
			String endTime = request.getParameter("endTime");
			
			
			SimpleDateFormat  sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			try {
				if(StringUtils.hasText(startTime)){
					sdf.parse(startTime);
				}
				if(StringUtils.hasText(endTime)){
					sdf.parse(endTime);
				}
			} catch (Exception e) {
				//时间转换错误
				info = "error:time format";
			}
			
			info = examineService.docSync(siteID,page, size, startTime, endTime);
			
		}else if("column".equals(type)){
			info = examineService.columnSync(siteID);
		}
		CommonToolUtil.outputJson(request,response, info);
	}
	
	@RequestMapping(value = "recommendList")
	@ResponseBody
	public void getRecommendList(RecommendInfo recommendInfo,
			@RequestParam(value="start",defaultValue="0")int start,
			@RequestParam(value="count",defaultValue="20")int count,
			@RequestParam(value="siteID",defaultValue="1")int siteID,
			HttpServletRequest request, HttpServletResponse response)
			throws JsonParseException, JsonMappingException, IOException {
		String info = recommendService.getRecommendList(recommendInfo,start,count,siteID);
		String columnId=request.getParameter("columnId");
		if(null!=columnId&&!"".equals(columnId)){
			info=recommendService.revise(info,columnId,start);
		}
		CommonToolUtil.outputJson( request,response, info);
	}
	@RequestMapping(value = "historyRecList")
	@ResponseBody
	public void getHistoryRecList(RecommendInfo recommendInfo,
								 @RequestParam(value="start",defaultValue="0")int start,
								 @RequestParam(value="count",defaultValue="20")int count,
								 @RequestParam(value="siteID",defaultValue="1")int siteID,
								 HttpServletRequest request, HttpServletResponse response) {
		String info = recommendService.getHistoryRecList(recommendInfo,start,count,siteID);
		CommonToolUtil.outputJson( request,response, info);
	}
	@RequestMapping(value="recKeywords")
	@ResponseBody
	public void getRecKeywords(RecommendInfo recommendInfo,
							   HttpServletRequest request,HttpServletResponse response) {
		String info=recommendService.getRecKeywords(recommendInfo);
		CommonToolUtil.outputJson(request,response,info);
	}

    /**
     * 保存邮件订阅信息
     */
    @RequestMapping(value = "emailSubscribe", method = RequestMethod.POST)
    public void emailSubscribe(@RequestParam(value = "siteID", defaultValue = "1") int siteID, String name, String email,
                               HttpServletRequest request, HttpServletResponse response) throws Exception {
        String info = recommendService.saveEmailSubscribe(siteID, name, email);
        CommonToolUtil.outputJson(response, info);
    }
}
