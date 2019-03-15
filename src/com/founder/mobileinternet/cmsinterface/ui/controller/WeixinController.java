package com.founder.mobileinternet.cmsinterface.ui.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.founder.mobileinternet.cmsinterface.service.RedisManager;
import com.founder.mobileinternet.cmsinterface.service.cms.WeixinService;
import com.founder.mobileinternet.cmsinterface.util.CommonToolUtil;
import com.founder.mobileinternet.cmsinterface.util.XYComment;

@XYComment(name = "微信")
@Controller
public class WeixinController {

	@Autowired
	private WeixinService weixinService;
	
	@Autowired
	private RedisManager redisManager;
	
	@XYComment(name = "微信账号（公众号）列表", comment = "获取微信账号（公众号）列表")
	@RequestMapping(value = "getWxAccounts")
	@ResponseBody
	public void getWxAccounts(@RequestParam int loginID,
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
			String info = weixinService.getWxAccounts(data, userID);
			CommonToolUtil.outputJson(request, response, info);
		} else {
			CommonToolUtil.outputJson(request, response, "");
		}
	}

	@XYComment(name="微信图文组 列表接口")
    @RequestMapping(value = "getWxGroupArticles")
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
    	
        boolean validResult = CommonToolUtil.checkValid(request.getServletPath(),loginID,time,sign,redisManager);
        if(validResult){
    	    String info = weixinService.getWxGroupArticles(data, userID);
    	    CommonToolUtil.outputJson(request,response,info);
        }else {
            CommonToolUtil.outputJson(request,response,"验证失败");
        }
    }
	
	@XYComment(name="微信图文详情接口")
    @RequestMapping(value = "getWxGroupArticleDetail")
    @ResponseBody
    public void getWxGroupArticleDetail(
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
            String info = weixinService.getWxGroupArticleDetail(data, userID);
            CommonToolUtil.outputJson(request,response,info);
        }else {
            CommonToolUtil.outputJson(request,response,"验证失败");
        }

    }
	@XYComment(name="微信图文详情接口")
    @RequestMapping(value = "getWxGroupArticleDetailUPOrDown")
    @ResponseBody
    public void getWxGroupArticleDetailUPOrDown(
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
            String info = weixinService.getWxGroupArticleDetailUPOrDown(data, userID);
            CommonToolUtil.outputJson(request,response,info);
        }else {
            CommonToolUtil.outputJson(request,response,"验证失败");
        }

    }


    @XYComment(name="微信图文组 审核全部通过")
    @RequestMapping(value = "transferGroup")
    @ResponseBody
    public void transferGroup(
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
            String info = weixinService.transferGroup(userID, data);
            CommonToolUtil.outputJson(request,response,info);
        }else {
            CommonToolUtil.outputJson(request,response,"验证失败");
        }

    }
    
    @XYComment(name="微信图文组 全部驳回")
    @RequestMapping(value = "rejectGroup")
    @ResponseBody
    public void rejectGroup(
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
            String info = weixinService.rejectGroup(userID, data);
            CommonToolUtil.outputJson(request,response,info);
        }else {
            CommonToolUtil.outputJson(request,response,"验证失败");
        }

    }
	
    @XYComment(name="微信图文组中 单篇稿件 审核通过")
    @RequestMapping(value = "transferGroupOne")
    @ResponseBody
    public void transferGroupOne(
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
            String info = weixinService.transferGroupOne(userID, data);
            CommonToolUtil.outputJson(request,response,info);
        }else {
            CommonToolUtil.outputJson(request,response,"验证失败");
        }

    }
    
    @XYComment(name="微信图文组中 单篇稿件 驳回")
    @RequestMapping(value = "rejectGroupOne")
    @ResponseBody
    public void rejectGroupOne(
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
            String info = weixinService.rejectGroupOne(userID, data);
            CommonToolUtil.outputJson(request,response,info);
        }else {
            CommonToolUtil.outputJson(request,response,"验证失败");
        }

    }
	
}
