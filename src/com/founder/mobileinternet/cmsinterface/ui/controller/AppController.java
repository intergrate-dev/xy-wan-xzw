package com.founder.mobileinternet.cmsinterface.ui.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.founder.mobileinternet.cmsinterface.pojo.cms.LogInfo;
import com.founder.mobileinternet.cmsinterface.service.RedisManager;
import com.founder.mobileinternet.cmsinterface.service.cms.AppService;
import com.founder.mobileinternet.cmsinterface.util.CommonToolUtil;
import com.founder.mobileinternet.cmsinterface.util.WXUtil;
import com.founder.mobileinternet.cmsinterface.util.XYComment;

@XYComment(name="App配置")
@Controller
public class AppController {
	@Autowired
	private AppService appService;
	@Autowired
	private RedisManager redisManager;


	@XYComment(name="app配置", comment="读app启动配置信息")
	@RequestMapping(value = "getConfig")
	@ResponseBody
	public void getConfig(
			@RequestParam("appID") int appID,
			@RequestParam(value="siteId", defaultValue="1") int siteID,
			HttpServletRequest request, HttpServletResponse response)
			throws JsonParseException, JsonMappingException, IOException {
		
		String info = appService.getConfig(appID, siteID);
		CommonToolUtil.outputJson(request, response, info);
	}

	@XYComment(name="分类", comment="读分类")
	@RequestMapping(value = "getCats")
	@ResponseBody
	public void getCats(@RequestParam(value="siteId", defaultValue="1") String siteId,
						@RequestParam(value="code", defaultValue="DISCUSSTYPE") String code,
						@RequestParam(value="keywords", defaultValue="") String keywords,
						@RequestParam(value="channel", defaultValue="1") int channel,
						HttpServletRequest request,HttpServletResponse response)
			throws IOException {
		String info = appService.getCats(code, siteId, keywords);
		
		CommonToolUtil.outputJson(request, response, info);

	}

	@XYComment(name="发验证码", comment="给手机号发送验证码")
	@RequestMapping(value = "getVerifiCode")
	@ResponseBody
	public void getVerifiCode(@RequestParam("phoneNo") String phoneNo,
			HttpServletRequest request, HttpServletResponse response) throws JsonParseException,
			JsonMappingException, IOException {
		String info = appService.getVerifiCode(phoneNo);

		CommonToolUtil.outputJson(request, response, info);
	}

	@XYComment(name="接收登录信息（不再使用）",comment="提交打开app的设备信息")
	@RequestMapping(value = "loginfo", method = RequestMethod.POST)
	@ResponseBody
	public void loginfo(LogInfo logInfo, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		/*
		boolean info = redisManager.setLcIfNotExist(request.getRemoteAddr());
		if(info) info = appService.logInfo(logInfo);

		CommonToolUtil.outputJson(request, response, info+"");
		*/
	}

	@RequestMapping(value = "login")
	@ResponseBody
	public void login(HttpServletRequest request,HttpServletResponse response, String user, String pwd)
			throws Exception {
		String info = appService.login(user, pwd);
		
		if (!"0".equals(info)) {
			JSONObject json = JSONObject.fromObject(info);

			response.setHeader("Access-Control-Allow-Origin", "*");
			response.setHeader("Access-Control-Expose-Headers", "api-version,api-data,code,error");

			response.setHeader("api-version", "5.1");
			response.setHeader("api-data", json.getString("api-data"));
			response.setHeader("code", json.getString("code"));
			if (json.has("error")) {
				response.setHeader("error", json.getString("error"));
			}
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("secretReturnData",json.getString("secretReturnData"));
			if(json.has("sites")){
				jsonObject.put("sites",json.getJSONArray("sites"));
			}
			info = jsonObject.toString();
//			info = json.getString("secretReturnData");
		}
		CommonToolUtil.outputJson(request, response, info);
	}

	@XYComment(name="启动api",comment="")
	@RequestMapping(value = "siteConf")
	@ResponseBody
	public void siteConf( HttpServletRequest request,
							 @RequestParam(value="siteId", defaultValue="1") int siteID,
							 HttpServletResponse response) {

		String info = appService.siteConf(siteID);
		CommonToolUtil.outputJson(request, response, info);
	}
	
	@XYComment(name="读App更新包")
	@RequestMapping(value = { "MobileApp" })
	public void getMobileApp(HttpServletRequest request,
			HttpServletResponse response,
			String appKey,
			String versionCode,
			String channel) {
		
		String info = appService.getAppInfo(appKey, versionCode,channel);
		CommonToolUtil.outputJson(response, info);
	}

	@RequestMapping(value = "logout")
	@ResponseBody
	public void logout(HttpServletRequest request, HttpServletResponse response, int loginID, long time, String sign)
			throws Exception {
		String info = appService.logout(loginID, time, sign,request.getServletPath());
		CommonToolUtil.outputJson(request,response, info);
	}

	@XYComment(name="系统消息列表")
   @RequestMapping(value = "message")
    @ResponseBody
    public void message(HttpServletRequest request, HttpServletResponse response, @RequestParam(defaultValue = "0") int page, int siteID)
            throws Exception {
        String info = appService.message(page, siteID);
        CommonToolUtil.outputJson(request,response, info);
    }

	@RequestMapping(value = "userIsExist")
	@ResponseBody
	public void userIsExist(HttpServletRequest request,HttpServletResponse response, String user)
			throws Exception {
		String info = appService.userIsExist(user);
		if (!"0".equals(info)) {
			JSONObject json = JSONObject.fromObject(info);
			response.setHeader("api-version", "5.1");
			response.setHeader("api-data", json.getString("api-data"));
			response.setHeader("code", json.getString("code"));
			if (json.has("error")) {
				response.setHeader("error", json.getString("error"));
			}
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("secretReturnData",json.getString("secretReturnData"));
			if(json.has("sites")){
				jsonObject.put("sites",json.getJSONArray("sites"));
			}
			info = jsonObject.toString();
//			info = json.getString("secretReturnData");
		}
		CommonToolUtil.outputJson(request, response, info);
	}

	@RequestMapping(value = "getUrl")
	@ResponseBody
	public void getUrl(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String info = appService.getUrl();
		CommonToolUtil.outputJson(request,response, info);
	}
  
	@XYComment(name="移动采编我的个人信息接口")
    @RequestMapping(value = "myInfo")
    @ResponseBody
    public void myInfo(
    		@RequestParam int loginID,
            @RequestParam int userID,
            @RequestParam long time,
            @RequestParam String sign,
            HttpServletRequest request, HttpServletResponse response){

        System.out.println("url------->"+request.getServletPath());
        System.out.println("loginID---->"+loginID);
        System.out.println("userID---->"+userID);
        System.out.println("time------>"+time);
        System.out.println("sign------>"+sign);


//        boolean validResult = CommonToolUtil.checkValid(request.getServletPath(),loginID,time,sign);
//        if(validResult){
        String info = appService.myInfo(userID);
        CommonToolUtil.outputJson(request,response,info);
//        }else {
//            CommonToolUtil.outputJson(request,response,"验证失败");
//        }

    }

    @RequestMapping(value = "qrCodeLogin")
    @ResponseBody
    public void qrCodeLogin(HttpServletRequest request,HttpServletResponse response, String user, String token)
            throws Exception {
        String info = appService.qrCodeLogin(user, token);
        if (!"0".equals(info)) {
            JSONObject json = JSONObject.fromObject(info);
            response.setHeader("api-version", "5.1");
            response.setHeader("api-data", json.getString("api-data"));
            response.setHeader("code", json.getString("code"));
            if (json.has("error")) {
                response.setHeader("error", json.getString("error"));
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("secretReturnData",json.getString("secretReturnData"));
            if(json.has("sites")){
                jsonObject.put("sites",json.getJSONArray("sites"));
            }
            info = jsonObject.toString();
//			info = json.getString("secretReturnData");
        }
        CommonToolUtil.outputJson(request, response, info);
    }
    
    @XYComment(name="微信分享获取签名接口")
	@RequestMapping(value = "wx/signature")
	@ResponseBody
	public void getWXTicket(
			@RequestParam(value = "url") String url,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
    	//如果传参数m=refresh,强制刷新jsapi_ticket
    	String m = request.getParameter("m");
    	if(!StringUtils.isBlank(m) && m.equals("refresh")) appService.getJsApiTicket();
    	
		String noncestr = WXUtil.createNoncestr();
		String timestamp = WXUtil.createTimpstamp();
		String appid = redisManager.get("wx_jsapi_appid");
		if(StringUtils.isBlank(appid)) appid = appService.getJsApiTicket();
		String jsapi_ticket = redisManager.get("wx_jsapi_ticket");
		String result = WXUtil.getSignature(appid, jsapi_ticket, noncestr, timestamp, url).toString();
		//如果传参数t=debug,返回的json带上jsapi_ticket
		String t = request.getParameter("t");
		if(!StringUtils.isBlank(t) && t.equals("debug")) {
			JSONObject ret = WXUtil.getSignature(appid, jsapi_ticket, noncestr, timestamp, url);
			ret.put("jsapi_ticket", jsapi_ticket);
			result = ret.toString();
		}
		//若果有callback参数，则返回json包上函数名
		String callback = request.getParameter("callback");
		if(!StringUtils.isBlank(callback)){
			result = callback + "("+result+")";
		}
		CommonToolUtil.outputJson(request,response, result);
	}
}
