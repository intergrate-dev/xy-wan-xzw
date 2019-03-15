package com.founder.mobileinternet.cmsinterface.ui.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.founder.mobileinternet.cmsinterface.service.RedisManager;
import com.founder.mobileinternet.cmsinterface.service.cms.ArticleService;
import com.founder.mobileinternet.cmsinterface.service.cms.OriginalService;
import com.founder.mobileinternet.cmsinterface.util.CommonToolUtil;
import com.founder.mobileinternet.cmsinterface.util.XYComment;

@XYComment(name = "源稿")
@Controller
public class OriginalController {
	@Autowired
	private ArticleService articleService;
	@Autowired
	private OriginalService originalService;
	@Autowired
	private RedisManager redisManager;

	@XYComment(name = "源稿审核栏目列表", comment = "获取源稿审核栏目")
	@RequestMapping(value = "getOriAuditCats")
	@ResponseBody
	public void getOriAuditCats(@RequestParam int loginID,
			@RequestParam int userID, @RequestParam long time,
			@RequestParam String sign, @RequestParam String data,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		System.out.println("url------>" + request.getServletPath());
		System.out.println("loginID------>" + loginID);
		System.out.println("userID------>" + userID);
		System.out.println("time------>" + time);
		System.out.println("sign------>" + sign);
		System.out.println("data------>" + data);

		boolean validResult = CommonToolUtil.checkValid(
				request.getServletPath(), loginID, time, sign, redisManager);
		if (validResult) {
			String info = originalService.getOriAuditCats(data, userID);
			CommonToolUtil.outputJson(request, response, info);
		} else {
			CommonToolUtil.outputJson(request, response, "");
		}
	}

	@XYComment(name="源稿列表接口")
    @RequestMapping(value = "getOriginalArticles")
    @ResponseBody
    public void getOriginalArticles(
    		@RequestParam int loginID,
    		@RequestParam int userID,
    		@RequestParam long time,
    		@RequestParam String sign,
    		@RequestParam String data,
    		HttpServletRequest request, HttpServletResponse response){
    	
    	System.out.println("url------->"+request.getServletPath());
        System.out.println("loginID---->"+loginID);
    	System.out.println("userID---->"+userID);
    	System.out.println("time------>"+time);
    	System.out.println("sign------>"+sign);
    	System.out.println("data------>"+data);
    	//用户是否有审核权限
    	
        boolean validResult = CommonToolUtil.checkValid(request.getServletPath(),loginID,time,sign,redisManager);
        if(validResult){
    	    String info = originalService.getOriginalArticles(data, userID);
    	    CommonToolUtil.outputJson(request,response,info);
        }else {
            CommonToolUtil.outputJson(request,response,"验证失败");
        }
    }
	
	@XYComment(name="源稿详情页接口")
    @RequestMapping(value = "getOriginalDetail")
    @ResponseBody
    public void getOriginalDetail(
    		@RequestParam int loginID,
            @RequestParam int userID,
            @RequestParam long time,
            @RequestParam String sign,
            @RequestParam String data,
            HttpServletRequest request, HttpServletResponse response){

        System.out.println("url------>"+request.getServletPath());
        System.out.println("loginID------>"+loginID);
        System.out.println("userID------>"+userID);
        System.out.println("time------>"+time);
        System.out.println("sign------>"+sign);
        System.out.println("data------>"+data);

        boolean validResult = CommonToolUtil.checkValid(request.getServletPath(),loginID,time,sign,redisManager);
        if(validResult){
            String info = originalService.getOriginalDetail(data, userID);
            CommonToolUtil.outputJson(request,response,info);
        }else {
            CommonToolUtil.outputJson(request,response,"验证失败");
        }

    }


    @XYComment(name="源稿详情页接口--上一篇,下一篇")
    @RequestMapping(value = "getOriginalDetailUpOrDown")
    @ResponseBody
    public void getOriginalDetailUpOrDown(
            @RequestParam int loginID,
            @RequestParam int userID,
            @RequestParam long time,
            @RequestParam String sign,
            @RequestParam String data,
            HttpServletRequest request, HttpServletResponse response){
        JSONObject jsonObject = JSONObject.fromObject(data);
//        int docID = jsonObject.getInt("fileId");

        System.out.println("url------>"+request.getServletPath());
        System.out.println("loginID------>"+loginID);
        System.out.println("userID------>"+userID);
        System.out.println("time------>"+time);
        System.out.println("sign------>"+sign);
        System.out.println("data------>"+data);

        boolean validResult = CommonToolUtil.checkValid(request.getServletPath(),loginID,time,sign,redisManager);
        if(validResult){
            String info = originalService.getDocId(data, userID);
            CommonToolUtil.outputJson(request,response,info);
        }else {
            CommonToolUtil.outputJson(request,response,"验证失败");
        }

    }



    @XYComment(name="源稿审核通过接口")
    @RequestMapping(value = "transferOriginal")
    @ResponseBody
    public void transferOriginal(
    		@RequestParam int loginID,
            @RequestParam int userID,
            @RequestParam long time,
            @RequestParam String sign,
            @RequestParam String data,
            HttpServletRequest request, HttpServletResponse response){

        System.out.println("url------->"+request.getServletPath());
        System.out.println("loginID---->"+loginID);
        System.out.println("userID---->"+userID);
        System.out.println("time------>"+time);
        System.out.println("sign------>"+sign);
        System.out.println("data------>"+data);

        boolean validResult = CommonToolUtil.checkValid(request.getServletPath(),loginID,time,sign,redisManager);
        if(validResult){
            String info = originalService.transferOriginal(userID, data);
            CommonToolUtil.outputJson(request,response,info);
        }else {
            CommonToolUtil.outputJson(request,response,"验证失败");
        }

    }
    
    @XYComment(name="源稿审核驳回接口")
    @RequestMapping(value = "rejectOriginal")
    @ResponseBody
    public void rejectOriginal(
    		@RequestParam int loginID,
            @RequestParam int userID,
            @RequestParam long time,
            @RequestParam String sign,
            @RequestParam String data,
            HttpServletRequest request, HttpServletResponse response){

        System.out.println("url------->"+request.getServletPath());
        System.out.println("loginID---->"+loginID);
        System.out.println("userID---->"+userID);
        System.out.println("time------>"+time);
        System.out.println("sign------>"+sign);
        System.out.println("data------>"+data);

        boolean validResult = CommonToolUtil.checkValid(request.getServletPath(),loginID,time,sign,redisManager);
        if(validResult){
            String info = originalService.rejectOriginal(userID, data);
            CommonToolUtil.outputJson(request,response,info);
        }else {
            CommonToolUtil.outputJson(request,response,"验证失败");
        }

    }
    @XYComment(name="源稿审核驳回到一审接口")
    @RequestMapping(value = "rejectOriginalFir")
    @ResponseBody
    public void rejectOriginalFir(
    		@RequestParam int loginID,
            @RequestParam int userID,
            @RequestParam long time,
            @RequestParam String sign,
            @RequestParam String data,
            HttpServletRequest request, HttpServletResponse response){

        System.out.println("url------->"+request.getServletPath());
        System.out.println("loginID---->"+loginID);
        System.out.println("userID---->"+userID);
        System.out.println("time------>"+time);
        System.out.println("sign------>"+sign);
        System.out.println("data------>"+data);

        boolean validResult = CommonToolUtil.checkValid(request.getServletPath(),loginID,time,sign,redisManager);
        if(validResult){
            String info = originalService.rejectOriginalFir(userID, data);
            CommonToolUtil.outputJson(request,response,info);
        }else {
            CommonToolUtil.outputJson(request,response,"验证失败");
        }

    }

	
}
