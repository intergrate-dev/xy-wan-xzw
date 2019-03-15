package com.founder.mobileinternet.cmsinterface.ui.controller;

import static com.founder.mobileinternet.cmsinterface.util.CommonToolUtil.getPage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.founder.mobileinternet.cmsinterface.pojo.cms.NisQaInfo;
import com.founder.mobileinternet.cmsinterface.service.RedisManager;
import com.founder.mobileinternet.cmsinterface.service.cms.QAService;
import com.founder.mobileinternet.cmsinterface.util.CommonToolUtil;
import com.founder.mobileinternet.cmsinterface.util.XYComment;

/**
 * Created by isaac_gu on 2016/10/24.
 */
@XYComment(name="互动问答")
@Controller
public class QAController {

    @Autowired
    QAService qaService;
    @Autowired
	private RedisManager redisManager;
    //问政列表
    @XYComment(name="互动问答列表" ,comment="读互动问答的稿件列表")
    @RequestMapping(value = "qaList")
    public void qaList(
            @XYComment(name="分组ID" ,comment="用于区分互动问答的二级栏目")
    		@RequestParam(defaultValue = "-1")int groupId,
            @RequestParam(value = "siteID", defaultValue = "1") int siteID,
            @XYComment(name="页数" ,comment="用于分页，默认是0")
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false) Integer start,
            @XYComment(name="取上一次列表的最后一篇稿件ID" ,comment="用于翻页时查重")
            @RequestParam(value = "lastFileId", defaultValue = "0") int lastFileId,
            HttpServletRequest request, HttpServletResponse response) {
        page=getPage(start,page);
        String info = qaService.qaList(siteID, groupId, lastFileId,page);
        CommonToolUtil.outputJson(request, response, info);
    }
    //问政提交
    @XYComment(name="互动问答提交")
    @RequestMapping(value="saveQa")
    public void saveQa(
            @XYComment(name="互动问答提交对象" ,comment="用于存储互动问答提交时的一系列参数")
            NisQaInfo nisQaInfo,
            HttpServletRequest request,HttpServletResponse response){
    	boolean info = redisManager.setUpIfNotExist(request.getRemoteAddr());
		if(info) info = qaService.commitPolitics(nisQaInfo);
		CommonToolUtil.outputJson( request,response, info+"");
    }
    //问政详情
    @XYComment(name="互动问答详情")
    @RequestMapping(value="qaDetail")
    public void qaDetail(
            HttpServletRequest request,HttpServletResponse response,
            @RequestParam(value = "siteId",defaultValue = "1") int siteID,int fileId){
    	String info =qaService.qaDetail(siteID, fileId);
		CommonToolUtil.outputJson(request, response, info);
    }
    @XYComment(name="我的互动问答")
    @RequestMapping(value = "myQA")
    public void myQA(
            HttpServletRequest request,HttpServletResponse response,int userID,
            @XYComment(name="页数" ,comment="用于分页，默认是0")
            @RequestParam(defaultValue = "0")int page,
            @RequestParam(value = "siteID",defaultValue = "1") int siteID){
        String info=qaService.myQA(userID,page,siteID);
        CommonToolUtil.outputJson(request,response,info);
    }

}
