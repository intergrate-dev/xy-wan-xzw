package com.founder.mobileinternet.cmsinterface.ui.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.founder.mobileinternet.cmsinterface.pojo.Configure;
import com.founder.mobileinternet.cmsinterface.service.RedisManager;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.founder.mobileinternet.cmsinterface.service.cms.ColumnService;
import com.founder.mobileinternet.cmsinterface.util.CommonToolUtil;
import com.founder.mobileinternet.cmsinterface.util.XYComment;

@XYComment(name = "栏目")
@Controller
public class ColumnController {
	@Autowired
	private ColumnService columnService;
	@Autowired
	private RedisManager redisManager;
	@Autowired
    private Configure configure;

	@XYComment(name = "读子孙栏目", comment = "获取该栏目的子孙栏目列表")
	@RequestMapping(value = "getColumnsAll")
	@ResponseBody
	public void getColumnsAll(@RequestParam(value="siteId", defaultValue="1") int siteId,
			@RequestParam(value="parentColumnId", defaultValue="0") int colID,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		String info = columnService.getColumnAll(siteId, colID);
		CommonToolUtil.outputJson(request,response, info);
	}

	@XYComment(name = "读子栏目", comment = "获取该栏目的子栏目列表")
	@RequestMapping(value = "getColumns")
	@ResponseBody
	public void getColumns(@RequestParam(value="siteId", defaultValue="1") int siteId,
			@RequestParam(value="parentColumnId", defaultValue="0") int colID,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		String info = columnService.getColumns(siteId, colID);

        String origin = request.getHeader("origin");
        if(origin!=null&&configure.getWebsiteUrl().contains(origin)){
            response.addHeader("Access-Control-Allow-Origin",origin);
        }
		CommonToolUtil.outputJson(request, response, info);
	}


	@XYComment(name = "读栏目信息", comment = "获取该栏目的栏目信息")
	@RequestMapping(value = "getColumn")
	@ResponseBody
	public void getColumn(@RequestParam("columnId") int columnId,
			@RequestParam(value="siteId", defaultValue="1") int siteId,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		String info = columnService.getColumn(siteId, columnId);

		String origin = request.getHeader("origin");
		if(origin!=null&&configure.getWebsiteUrl().contains(origin)){
			response.addHeader("Access-Control-Allow-Origin",origin);
		}
		CommonToolUtil.outputJson(request, response, info);
	}
	
	/**
	 * 读所有站点的所有栏目信息，用于与第三方同步的场合。
     * 无缓存。返回XML。
	 */

	@XYComment(name = "读栏目树", comment = "读所有站点的所有栏目信息，用于与第三方同步的场合")
	@RequestMapping(value = "getNodeTree")
	@ResponseBody
	public void getAllSiteColumn(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String info = columnService.getNodeTree();
		CommonToolUtil.outputJson(request,response, info);
	}

	@XYComment(name = "移动采编发布库栏目列表", comment = "获取发布库栏目列表")
	@RequestMapping(value = "getPubCols")
	@ResponseBody
	public void getPubCols(
			@RequestParam int loginID,
			@RequestParam int userID,
		    @RequestParam long time,
		    @RequestParam String sign,
		    @RequestParam String data,
	   		HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("url------>"+request.getServletPath());
		System.out.println("loginID------>"+loginID);
		System.out.println("userID------>"+userID);
		System.out.println("time------>"+time);
		System.out.println("sign------>"+sign);
		System.out.println("data------>"+data);

        boolean validResult = CommonToolUtil.checkValid(request.getServletPath(),loginID,time,sign,redisManager);
        if(validResult){
			String info = columnService.getPubCols(userID,data);
			CommonToolUtil.outputJson(request,response,info);
        }else {
            CommonToolUtil.outputJson(request,response,"");
        }
	}

	@XYComment(name = "移动采编发布库栏目权限判断", comment = "是否有发布库栏目权限")
	@RequestMapping(value = "colIsOp")
	@ResponseBody
	public void colIsOp(
			@RequestParam int loginID,
			@RequestParam int userID,
			@RequestParam long time,
			@RequestParam String sign,
			@RequestParam String data,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("url------>"+request.getServletPath());
		System.out.println("loginID------>"+loginID);
		System.out.println("userID------>"+userID);
		System.out.println("time------>"+time);
		System.out.println("sign------>"+sign);
		System.out.println("data------>"+data);

        boolean validResult = CommonToolUtil.checkValid(request.getServletPath(),loginID,time,sign,redisManager);
        if(validResult){
			String info = columnService.colIsOp(userID,data);
			CommonToolUtil.outputJson(request,response,info);
        }else {
            CommonToolUtil.outputJson(request,response,"");
        }
	}
	
	@XYComment(name = "移动采编栏目检索接口", comment = "栏目检索接口")
	@RequestMapping(value = "colSearch")
	@ResponseBody
	public void colSearch(
			@RequestParam int loginID,
			@RequestParam int userID,
			@RequestParam long time,
			@RequestParam String sign,
			@RequestParam String data,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		System.out.println("url------>"+request.getServletPath());
		System.out.println("loginID------>"+loginID);
		System.out.println("userID------>"+userID);
		System.out.println("time------>"+time);
		System.out.println("sign------>"+sign);
		System.out.println("data------>"+data);
		
        boolean validResult = CommonToolUtil.checkValid(request.getServletPath(),loginID,time,sign,redisManager);
        if(validResult){
			String info = columnService.colSearch(userID,data);
			CommonToolUtil.outputJson(request,response,info);
        }else {
            CommonToolUtil.outputJson(request,response,"");
        }
	}

	@XYComment(name = "移动采编稿件审核栏目", comment = "获取稿件审核栏目")
	@RequestMapping(value = "getAuditCols")
	@ResponseBody
	public void getAuditCols(
			@RequestParam int loginID,
			@RequestParam int userID,
			@RequestParam long time,
			@RequestParam String sign,
			@RequestParam String data,

			HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("url------>"+request.getServletPath());
		System.out.println("loginID------>"+loginID);
		System.out.println("userID------>"+userID);
		System.out.println("time------>"+time);
		System.out.println("sign------>"+sign);
		System.out.println("data------>"+data);

        boolean validResult = CommonToolUtil.checkValid(request.getServletPath(),loginID,time,sign,redisManager);
        if(validResult){
			String info = columnService.getAuditCols(userID,data);
			CommonToolUtil.outputJson(request,response,info);
        }else {
            CommonToolUtil.outputJson(request,response,"");
        }
	}

	@XYComment(name = "读栏目树", comment = "查看栏目树，用于百格视频使用")
	@RequestMapping(value = "getColumnsAllEasy",method = RequestMethod.POST)
	@ResponseBody
	public void getColumnsAllEasy(HttpServletRequest request,HttpServletResponse response,int eid, long time,
								  String data,String sign) throws Exception {
        System.out.println("url------>"+request.getServletPath());
        System.out.println("eid------>"+eid);
        System.out.println("time------>"+time);
        System.out.println("sign------>"+sign);
        System.out.println("data------>"+data);

        String authResult = CommonToolUtil.checkAuth(eid,time,data,sign,redisManager,configure);
        if(authResult!=null){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("success",false);
            jsonObject.put("message",authResult);
            CommonToolUtil.outputJson(request,response, jsonObject.toString());
        }else{
            String info = columnService.getColumnsAllEasy(data);
            if("1".equals(info)){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("success",false);
                jsonObject.put("message","入参data格式错误");
                info = jsonObject.toString();
            }else if("2".equals(info)){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("success",true);
                jsonObject.put("columns","[]");
                info = jsonObject.toString();
            }

            CommonToolUtil.outputJson(request,response, info);
        }
    }

}