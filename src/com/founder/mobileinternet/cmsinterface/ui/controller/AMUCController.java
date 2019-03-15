package com.founder.mobileinternet.cmsinterface.ui.controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Cookie;

import net.sf.json.JSONObject;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.http.NameValuePair;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.founder.mobileinternet.cmsinterface.pojo.Configure;
import com.founder.mobileinternet.cmsinterface.service.RedisManager;
import com.founder.mobileinternet.cmsinterface.service.cms.CollectService;
import com.founder.mobileinternet.cmsinterface.service.cms.DuibaService;
import com.founder.mobileinternet.cmsinterface.service.cms.EventService;
import com.founder.mobileinternet.cmsinterface.service.cms.InviteCodeService;
import com.founder.mobileinternet.cmsinterface.service.cms.MemberService;
import com.founder.mobileinternet.cmsinterface.service.cms.MemberSynService;
import com.founder.mobileinternet.cmsinterface.service.cms.OrdersService;
import com.founder.mobileinternet.cmsinterface.service.cms.PaperCardService;
import com.founder.mobileinternet.cmsinterface.service.cms.PermissionService;
import com.founder.mobileinternet.cmsinterface.service.cms.SetMealService;
import com.founder.mobileinternet.cmsinterface.service.cms.VoteInfoService;
import com.founder.mobileinternet.cmsinterface.service.cms.VoteWriteService;
import com.founder.mobileinternet.cmsinterface.service.cms.PayService;
import com.founder.mobileinternet.cmsinterface.service.cms.ColumnsService;
import com.founder.mobileinternet.cmsinterface.util.CommonToolUtil;

@Controller
@RequestMapping("amuc/api")
public class AMUCController {
	@Autowired
	DuibaService duibaService;

	@Autowired
	EventService eventService;

	@Autowired
	InviteCodeService inviteCodeService;

	@Autowired
	MemberService memberService;

	@Autowired
	MemberSynService memberSynService;

	@Autowired
	OrdersService ordersService;

	@Autowired
	CollectService collectService;

	@Autowired
	PaperCardService paperCardService;

	@Autowired
	SetMealService setMealService;

	@Autowired
	VoteInfoService voteInfoService;

	@Autowired
	VoteWriteService voteWriteService;

	@Autowired
	PermissionService permissionService;

	@Autowired
	PayService payService;
	
	@Autowired
	ColumnsService columnsService;

	@Autowired
	Configure configure;
	
	@Autowired
	private RedisManager redisManager;

	@RequestMapping(value = "duiba/autologin")
	public void autologin(@RequestParam(value = "uid") String uid, @RequestParam(value = "redirect") String redirect,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		String info = duibaService.autologin(uid, redirect);
		JSONObject json = JSONObject.fromObject(info);
		String url = json.getString("url");
		response.sendRedirect(url);
		CommonToolUtil.outputJson(response, info);
	}

	@RequestMapping(value = "duiba/creditConsume")
	public void creditConsume(@RequestParam(value = "appKey") String appKey,
			@RequestParam(value = "timestamp") String timestamp, @RequestParam(value = "uid") String uid,
			@RequestParam(value = "credits") String credits, @RequestParam(value = "description") String description,
			@RequestParam(value = "orderNum") String orderNum,@RequestParam(value = "type") String type, 
			@RequestParam(value = "facePrice") String facePrice,@RequestParam(value = "actualPrice") String actualPrice,
			@RequestParam(value = "waitAudit") String waitAudit,@RequestParam(value = "ip") String ip, 
			@RequestParam(value = "params") String params,@RequestParam(value = "sign") String sign, 
			HttpServletRequest request, HttpServletResponse response) throws Exception{
		String info = duibaService.creditConsume(appKey, timestamp, uid, credits, description, orderNum, type,
				facePrice, actualPrice, waitAudit, ip, params, sign);
		CommonToolUtil.outputJson(response, info);
	}

	@RequestMapping(value = "duiba/creditNotify")
	public void creditNotify(@RequestParam(value = "sign") String sign, @RequestParam(value = "appKey") String appKey,
			@RequestParam(value = "timestamp") String timestamp, @RequestParam(value = "success") String success,
			@RequestParam(value = "errorMessage") String errorMessage, @RequestParam(value = "bizId") String bizId,
			@RequestParam(value = "orderNum") String orderNum,
			HttpServletRequest request, HttpServletResponse response) {
		String info = duibaService.creditNotify(appKey, timestamp, success, errorMessage, bizId, orderNum, sign);
		CommonToolUtil.outputJson(response, info);
	}

	@RequestMapping(value = "event/eventTypes")
	public void eventTypes(HttpServletRequest request, HttpServletResponse response) {
		String info = eventService.eventTypes();
		CommonToolUtil.outputJson(response, info);
	}

	@RequestMapping(value = "event/scoreRuleList")
	public void scoreRuleList(@RequestParam(value = "source") String source, @RequestParam(value = "tc") String tc,
			HttpServletRequest request, HttpServletResponse response) {
		String info = eventService.scoreRuleList(source, tc);
		CommonToolUtil.outputJson(response, info);
	}

	@RequestMapping(value = "event/convert")
	public void convert(@RequestParam(value = "source", defaultValue = "") String source,
			@RequestParam(value = "tc", defaultValue = "") String tc,
			@RequestParam(value = "member", defaultValue = "") String memberID,
			@RequestParam(value = "info", defaultValue = "") String info, HttpServletRequest request,
			HttpServletResponse response) {
		String info1 = eventService.convert(source, tc, memberID, info);
		CommonToolUtil.outputJson(response, info1);
	}
	//支持多站点
	@RequestMapping(value = "event/event1")
	public void event1(@RequestParam(value = "eType", defaultValue = "") String eType, 
			@RequestParam(value = "tc", defaultValue = "") String tc,
			@RequestParam(value = "member", defaultValue = "") String member,
			@RequestParam(value = "time", defaultValue = "") String time,
			@RequestParam(value = "sign", defaultValue = "") String sign, @RequestParam(value = "siteID", defaultValue = "1") String siteID, 
			HttpServletRequest request,HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("utf-8");
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setContentType("text/html;charset=UTF-8");
		System.out.println("eType:"+eType+",tc:"+tc+ ",member:"+member+",time:"+time+",sign:"+sign+",siteID:"+siteID);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");//设置日期格式
	    Date dateBegin=new Date();
	    long startTime=System.currentTimeMillis();
	    System.out.println("外网进入时间： 标准形式-->"+df.format(dateBegin)+"     long值形式-->"+startTime);
		//检查当前访问的key是否存在redis中，存在阻止本次请求。
		String key="amuc.event." + member + "."  + eType + "." + request.getRemoteAddr();
		if (redisManager.exists(key)) {
			JSONObject obj = new JSONObject();
			obj.put("code", "1009");
			obj.put("msg", "行为过于频繁");
			CommonToolUtil.outputJson(response,String.valueOf(obj));
			System.out.println("外网积分接口返回：-->"+String.valueOf(obj));
			return;
		} else {
			redisManager.incr(key);// +1
			redisManager.expire(key, 2);
		}
		
		
		String info = eventService.event1(eType, tc, member,time,sign,siteID);
		
		System.out.println("外网积分接口返回info---------->:"+info);
		System.out.println();
		CommonToolUtil.outputJson(response, info);
	}

	@RequestMapping(value = "event/event")
	public void event(@RequestParam(value = "info", defaultValue = "") String info,
			@RequestParam(value = "tc", defaultValue = "") String tc,
			@RequestParam(value = "time", defaultValue = "") String time,
			@RequestParam(value = "sign", defaultValue = "") String sign,
			@RequestParam(value = "member", defaultValue = "") String member, @RequestParam(value = "siteID", defaultValue = "1") String siteID, 
			HttpServletRequest request,HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("utf-8");
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setContentType("text/html;charset=UTF-8");
		//Map<String, String> paramMap = new HashMap();
		//paramMap.put("tc", tc);
		//paramMap.put("info", info);
		//paramMap.put("member", member);
		//CommonToolUtil.checkValid(request, paramMap, configure);
		String info1 = eventService.event(info, tc, member,time,sign,siteID);
		CommonToolUtil.outputJson(response, info1);
	}

	@RequestMapping(value = "invitecode/codeImeiRecord")
	public void codeImeiRecord(@RequestParam(value = "code") String code, @RequestParam(value = "imei") String imei,
			@RequestParam(value = "uid") String uid, @RequestParam(value = "name") String name, @RequestParam(value = "siteID", defaultValue = "1") String siteID,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setContentType("text/html;charset=UTF-8");
		if (!CommonToolUtil.isNumeric(uid)) {
			uid = CommonToolUtil.AESDncode(uid);
		}
		// HTTPHelper.checkValid(request);
		String info = inviteCodeService.codeImeiRecord(code, imei, uid, name, siteID);
		CommonToolUtil.outputJson(response, info);
	}

	@RequestMapping(value = "invitecode/getInviteCode")
	public void getInviteCode(@RequestParam(value = "uid") String uid, @RequestParam(value = "siteID", defaultValue = "1") String siteID, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setContentType("text/html;charset=UTF-8");
		if (!CommonToolUtil.isNumeric(uid)) {
			uid = CommonToolUtil.AESDncode(uid);
		}
		// HTTPHelper.checkValid(request);
		String info = inviteCodeService.getInviteCode(uid,siteID);
		CommonToolUtil.outputJson(response, info);
	}

	@RequestMapping(value = "invitecode/getInviteRecord")
	public void getInviteRecord(@RequestParam(value = "uid") String uid, @RequestParam(value = "siteID", defaultValue = "1") String siteID, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setContentType("text/html;charset=UTF-8");
		if (!CommonToolUtil.isNumeric(uid)) {
			uid = CommonToolUtil.AESDncode(uid);
		}
		// HTTPHelper.checkValid(request);
		String info = inviteCodeService.getInviteRecord(uid,siteID);
		CommonToolUtil.outputJson(response, info);
	}
	
	@RequestMapping(value = "invitecode/siteConf")
	public void siteConf(@RequestParam(value = "siteID", defaultValue = "1") String siteID, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setContentType("text/html;charset=UTF-8");
		
		String info = inviteCodeService.siteConf(siteID);
		CommonToolUtil.outputJson(response, info);
	}
	
	@RequestMapping(value = "invitecode/logoConf")
	public void logoConf(@RequestParam(value = "siteID", defaultValue = "1") String siteID, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setContentType("text/html;charset=UTF-8");
		
		String info = inviteCodeService.logoConf(siteID);
		CommonToolUtil.outputJson(response, info);
	}

	@RequestMapping(value = "member/register")
	public void register(@RequestParam(value = "nickname") String nickname,
			@RequestParam(value = "mobile") String mobile, @RequestParam(value = "email") String email,
			@RequestParam(value = "password") String password, @RequestParam(value = "code") String code,
			@RequestParam(value = "deviceid") String deviceid, HttpServletRequest request,
			HttpServletResponse response) {
		String info = memberService.register(nickname, mobile, email, password, code, deviceid);
		CommonToolUtil.outputJson(response, info);
	}

	@RequestMapping(value = "member/registerEx")
	public void registerEx(@RequestParam(value = "nickname") String nickname,
			@RequestParam(value = "mobile") String mobile, @RequestParam(value = "email") String email,
			@RequestParam(value = "password") String password, HttpServletRequest request,
			HttpServletResponse response) {
		String info = memberService.registerEx(nickname, mobile, email, password);
		CommonToolUtil.outputJson(response, info);
	}

	@RequestMapping(value = "member/Login")
	public void Login(@RequestParam(value = "mobile") String mobile, @RequestParam(value = "email") String email,
			@RequestParam(value = "password") String password, HttpServletRequest request,
			HttpServletResponse response) {
		String info = memberService.Login(mobile, email, password);
		CommonToolUtil.outputJson(response, info);
	}

	@RequestMapping(value = "member/loginByOther")
	public void loginByOther(@RequestParam(value = "type") int type, @RequestParam(value = "oid") String oid,
			@RequestParam(value = "name") String name, HttpServletRequest request, HttpServletResponse response) {
		String info = memberService.loginByOther(type, oid, name);
		CommonToolUtil.outputJson(response, info);
	}

	private MultipartEntity getMultipartEntity(List<FileItem> items) throws IOException {
		Iterator<FileItem> iter = items.iterator();
		MultipartEntity entity = new MultipartEntity();
		int i = 1;
		while (iter.hasNext()) {
			FileItem item = iter.next();
			if (!item.isFormField() && item.getSize() > 0) {
				entity.addPart("file" + i++, new InputStreamBody(item.getInputStream(), item.getName()));
			}
		}
		return entity;
	}

	@RequestMapping(value = "member/uploadPortrait")
	@ResponseBody
	public void uploadPortrait(@RequestParam(value = "uid") String uid, HttpServletRequest request,
			HttpServletResponse response) throws FileUploadException, IOException, URISyntaxException {
		ServletFileUpload upload = new ServletFileUpload(new DiskFileItemFactory());
		@SuppressWarnings("unchecked")
		List<FileItem> items = upload.parseRequest(request);
		MultipartEntity entity = getMultipartEntity(items);
		String info = memberService.uploadPortrait(entity, uid);
		CommonToolUtil.outputJson(response, info);
	}

	@RequestMapping(value = "member/modify")
	public void modify(@RequestParam(value = "uid") String uid, @RequestParam(value = "nickname") String nickname,
			@RequestParam(value = "mobile") String mobile, @RequestParam(value = "email") String email,
			@RequestParam(value = "password") String password, @RequestParam(value = "sex") String sex,
			@RequestParam(value = "address") String address, @RequestParam(value = "region") String region,
			@RequestParam(value = "code") String code, @RequestParam(value = "deviceid") String deviceid,
			@RequestParam(value = "birthday") String birthday, HttpServletRequest request,
			HttpServletResponse response) {
		System.out.println("amuc/api/member/uploadImage start ......");
		response.setContentType("text/html;charset=UTF-8");
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Cache-Control","no-cache");
		String info = memberService.modify(uid, nickname, mobile, email, password, birthday, region, sex, address, code,
				deviceid);
		CommonToolUtil.outputJson(response, info);
	}

	@RequestMapping(value = "member/ForgetPassword")
	public void ForgetPassword(@RequestParam(value = "password") String password,
			@RequestParam(value = "mobile") String mobile, HttpServletRequest request, HttpServletResponse response) {
		String info = memberService.ForgetPassword(password, mobile);
		CommonToolUtil.outputJson(response, info);
	}

	@RequestMapping(value = "member/getPortrait")
	public void getPortrait(@RequestParam(value = "uid", defaultValue = "") String uid, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setContentType("text/html;charset=UTF-8");
		String info = memberService.getPortrait(uid);
		JSONObject json = JSONObject.fromObject(info);
		String url = json.getString("url");
		if (!url.equals("notexist") && !url.equals("default.bmp")) {
			response.sendRedirect(url);
		}else if (url.equals("default.bmp")) {
			String path = request.getContextPath();  
			String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/resources/image/";
			System.out.println("-----------------------------------basePath---"+basePath);
			response.sendRedirect(basePath+url);
		}
		System.out.println("----------------------------------- member/getPortrait, headImg url: --- " + url);
		CommonToolUtil.outputJson(response, info);
	}

	@RequestMapping(value = "member/getUserMessage")
	public void getUserMessage(@RequestParam(value = "ssoid") String ssoid, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setContentType("text/html;charset=UTF-8");
		String info = memberService.getUserMessage(ssoid);
		CommonToolUtil.outputJson(response, info);
	}

	@RequestMapping(value = "member/score")
	public void score(@RequestParam(value = "source", defaultValue = "") String source, @RequestParam(value = "id", defaultValue = "") String oriID,
			@RequestParam(value = "tc", defaultValue = "") String tc, @RequestParam(value = "table", defaultValue = "") String table,
			HttpServletRequest request, HttpServletResponse response) {
		String info = memberService.score(source, oriID, tc, table);
		CommonToolUtil.outputJson(response, info);
	}

	@RequestMapping(value = "member/scoreList")
	public void scoreList(@RequestParam(value = "source") String source, @RequestParam(value = "id") String oriID,
			@RequestParam(value = "tc") String tc, @RequestParam(value = "table") String table,
			@RequestParam(value = "getAll") String getAll, @RequestParam(value = "curPage") String curPage,
			@RequestParam(value = "pageSize") String pageSize, HttpServletRequest request,
			HttpServletResponse response) {
		String info = memberService.scoreList(source, oriID, tc, table, getAll, curPage, pageSize);
		CommonToolUtil.outputJson(response, info);
	}

	@RequestMapping(value = "member/syn/registerEx")
	public void registerEx(@RequestParam(value = "ssoid") String ssoid,
			@RequestParam(value = "username") String username, @RequestParam(value = "nickname") String nickname,
			@RequestParam(value = "mobile") String mobile, @RequestParam(value = "email") String email,
			@RequestParam(value = "password") String password, HttpServletRequest request,
			HttpServletResponse response) {
		String info = memberSynService.registerEx(ssoid, username, nickname, mobile, email, password);
		CommonToolUtil.outputJson(response, info);
	}

	@RequestMapping(value = "member/syn/modify")
	public void modify(@RequestParam(value = "nickname") String nickname, @RequestParam(value = "mobile") String mobile,
			HttpServletRequest request, HttpServletResponse response) {
		System.out.println("amuc/api/member/uploadImage start ......");
		response.setContentType("text/html;charset=UTF-8");
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Cache-Control","no-cache");
		String info = memberSynService.modify(nickname, mobile);
		CommonToolUtil.outputJson(response, info);
	}

	@RequestMapping(value = "member/syn/updatePassword")
	public void updatePassword(@RequestParam(value = "password") String password,
			@RequestParam(value = "mobile") String mobile, HttpServletRequest request, HttpServletResponse response) {
		String info = memberSynService.updatePassword(password, mobile);
		CommonToolUtil.outputJson(response, info);
	}

	@RequestMapping(value = "member/syn/loginByOther")
	public void synloginByOther(@RequestParam(value = "type") int type, @RequestParam(value = "oid") String oid,
			@RequestParam(value = "name") String name, HttpServletRequest request, HttpServletResponse response) {
		String info = memberSynService.loginByOther(type, oid, name);
		CommonToolUtil.outputJson(response, info);
	}

	@RequestMapping(value = "order/create")
	public void create(@RequestParam(value = "meal") String meal, @RequestParam(value = "users") String users,
			@RequestParam(value = "pay") String pay, @RequestParam(value = "operator") String operator,
			@RequestParam(value = "total") String total, @RequestParam(value = "orderSource") String orderSource, @RequestParam(value = "siteID", defaultValue = "1") String siteID,
			@RequestParam(value = "taxpayer") String taxpayer, @RequestParam(value = "unitNum") String unitNum, @RequestParam(value = "unitAddr") String unitAddr,HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setContentType("text/html;charset=UTF-8");
		// HTTPHelper.checkValid(request);
		String info = ordersService.create(meal, users, pay, operator, total, orderSource, siteID, taxpayer, unitNum, unitAddr);
		CommonToolUtil.outputJson(response, info);
	}

	@RequestMapping(value = "order/checkuser")
	public void checkuser(@RequestParam(value = "mobile") String mobile,@RequestParam(value = "siteID", defaultValue = "1") String siteID, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setContentType("text/html;charset=UTF-8");
		String info = ordersService.checkuser(mobile,siteID);
		CommonToolUtil.outputJson(response, info);
	}

	@RequestMapping(value = "order/getMsg")
	public void getMsg(@RequestParam(value = "orderNum") String orderNum, @RequestParam(value = "docid") String docid,
			HttpServletRequest request, HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setContentType("text/html;charset=UTF-8");
		String info = ordersService.getMsg(orderNum, docid);
		CommonToolUtil.outputJson(response, info);
	}

	@RequestMapping(value = "order/cancel")
	public void cancel(@RequestParam(value = "orderNum") String orderNum,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setContentType("text/html;charset=UTF-8");
		// HTTPHelper.checkValid(request);
		String info = ordersService.cancel(orderNum);
		CommonToolUtil.outputJson(response, info);
	}

	@RequestMapping(value = "order/del")
	public void del(@RequestParam(value = "orderNum") String orderNum, @RequestParam(value = "docid") String docid,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setContentType("text/html;charset=UTF-8");
		// HTTPHelper.checkValid(request);
		String info = ordersService.del(orderNum, docid);
		CommonToolUtil.outputJson(response, info);
	}

	@RequestMapping(value = "order/getMessages")
	public void getMessages(@RequestParam(value = "orderNum") String orderNum, @RequestParam(value = "uid") String uid,@RequestParam(value = "siteID", defaultValue = "1") String siteID,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setContentType("text/html;charset=UTF-8");
		if (!CommonToolUtil.isNumeric(uid)) {
			uid = CommonToolUtil.AESDncode(uid);
		}
		// HTTPHelper.checkValid(request);
		String info = ordersService.getMessages(orderNum, uid, siteID);
		CommonToolUtil.outputJson(response, info);
	}
	
	@RequestMapping(value = "order/getMessagesPackage")
	public void getMessagesPackage(@RequestParam(value = "orderNum") String orderNum, @RequestParam(value = "uid") String uid,@RequestParam(value = "siteID", defaultValue = "1") String siteID,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setContentType("text/html;charset=UTF-8");
		if (!CommonToolUtil.isNumeric(uid)) {
			uid = CommonToolUtil.AESDncode(uid);
		}
		// HTTPHelper.checkValid(request);
		String info = ordersService.getMessagesPackage(orderNum, uid, siteID);
		CommonToolUtil.outputJson(response, info);
	}
	
	@RequestMapping(value = "order/getPcNoMsg")
	public void getMessagesPackage(@RequestParam(value = "uid") String uid,@RequestParam(value = "siteID", defaultValue = "1") String siteID,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setContentType("text/html;charset=UTF-8");
		if (!CommonToolUtil.isNumeric(uid)) {
			uid = CommonToolUtil.AESDncode(uid);
		}
		// HTTPHelper.checkValid(request);
		String info = ordersService.getPcNoMsg(uid, siteID);
		CommonToolUtil.outputJson(response, info);
	}

	@RequestMapping(value = "order/regain")
	public void regain(@RequestParam(value = "orderNum") String orderNum, 
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setContentType("text/html;charset=UTF-8");
		// HTTPHelper.checkValid(request);
		String info = ordersService.regain(orderNum);
		CommonToolUtil.outputJson(response, info);
	}

	@RequestMapping(value = "order/pay")
	public void pay(@RequestParam(value = "orderNum") String orderNum, @RequestParam(value = "docid") String docid,
			@RequestParam(value = "mealName") String mealName, @RequestParam(value = "mealMoney") String mealMoney,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setContentType("text/html;charset=UTF-8");
		// HTTPHelper.checkValid(request);
		String info = ordersService.pay( orderNum,  docid,  mealName, mealMoney);
		CommonToolUtil.outputJson(response, info);
	}

	@RequestMapping(value = "read/getUserPermission")
	public void getUserPermission(@RequestParam(value = "uid") String uid, @RequestParam(value = "siteID", defaultValue = "1") String siteID,HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setContentType("text/html;charset=UTF-8");
		if (!CommonToolUtil.isNumeric(uid)) {
			uid = CommonToolUtil.AESDncode(uid);
		}
		String info = permissionService.getUserPermission(uid,siteID);
		CommonToolUtil.outputJson(response, info);
	}

	@RequestMapping(value = "read/getAllPermission")
	public void getAllPermission(@RequestParam(value = "siteID", defaultValue = "1") String siteID, HttpServletRequest request, HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setContentType("text/html;charset=UTF-8");
		String info = permissionService.getAllPermission(siteID);
		CommonToolUtil.outputJson(response, info);
	}

	@RequestMapping(value = "order/renew")
	public void renew(@RequestParam(value = "setmealid") String setmealid, @RequestParam(value = "ssoid") String ssoid,@RequestParam(value = "siteID", defaultValue = "1") String siteID,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setContentType("text/html;charset=UTF-8");
		if (!CommonToolUtil.isNumeric(ssoid)) {
			ssoid = CommonToolUtil.AESDncode(ssoid);
		}
		// HTTPHelper.checkValid(request);
		String info = ordersService.renew(setmealid, ssoid, siteID);
		CommonToolUtil.outputJson(response, info);
	}

	@RequestMapping(value = "collect/getCollectId")
	public void getCollectId(@RequestParam(value = "userId") String userId, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setContentType("text/html;charset=UTF-8");
		if (!CommonToolUtil.isNumeric(userId)) {
			userId = CommonToolUtil.AESDncode(userId);
		}
		String info = collectService.getCollectId(userId);
		CommonToolUtil.outputJson(response, info);
	}

	@RequestMapping(value = "collect/getCollectIds")
	public void getCollectIds(@RequestParam(value = "userId") String userId, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setContentType("text/html;charset=UTF-8");
		if (!CommonToolUtil.isNumeric(userId)) {
			userId = CommonToolUtil.AESDncode(userId);
		}
		String info = collectService.getCollectIds(userId);
		CommonToolUtil.outputJson(response, info);
	}

	@RequestMapping(value = "collect/getCollect")
	public void getCollect(@RequestParam(value = "userId") String userId,
			@RequestParam(value = "articleId") String articleId,
			@RequestParam(value = "articleName") String articleName, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setContentType("text/html;charset=UTF-8");
		if (!CommonToolUtil.isNumeric(userId)) {
			userId = CommonToolUtil.AESDncode(userId);
		}
		String info = collectService.getCollect(userId, articleId, articleName);
		CommonToolUtil.outputJson(response, info);
	}

	@RequestMapping(value = "collect/delCollect")
	public void delCollect(@RequestParam(value = "userId") String userId,
			@RequestParam(value = "articleId") String articleId, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setContentType("text/html;charset=UTF-8");
		if (!CommonToolUtil.isNumeric(userId)) {
			userId = CommonToolUtil.AESDncode(userId);
		}
		String info = collectService.delCollect(userId, articleId);
		CommonToolUtil.outputJson(response, info);
	}

	@RequestMapping(value = "collect/checkCollection")
	public void checkCollection(@RequestParam(value = "userId") String userId,
			@RequestParam(value = "articleId") String articleId, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setContentType("text/html;charset=UTF-8");
		if (!CommonToolUtil.isNumeric(userId)) {
			userId = CommonToolUtil.AESDncode(userId);
		}
		String info = collectService.checkCollection(userId, articleId);
		CommonToolUtil.outputJson(response, info);
	}

	@RequestMapping(value = "pcard/activatePaperCard")
	public void activatePaperCard(@RequestParam(value = "uid") String uid, @RequestParam(value = "ssoid") String ssoid,
			@RequestParam(value = "pcno") String pcno, @RequestParam(value = "password") String password,@RequestParam(value = "siteID") String siteID,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setHeader("Access-Control-Allow-Origin", "*");
		/*response.setHeader("Access-Control-Allow-Methods", "GET, HEAD, POST, PUT, DELETE, TRACE, OPTIONS, PATCH");
		response.addHeader("Access-Control-Allow-Headers","ticket,program-sign,timestamp,random,program-params,Content-type,content-type");
		response.setHeader("Access-Control-Allow-Headers", "program-sign");
		response.setHeader("Access-Control-Allow-Headers", "timestamp");
		response.setHeader("Access-Control-Allow-Headers", "random");
		response.setHeader("Access-Control-Allow-Headers", "program-params");
		response.setHeader("Access-Control-Allow-Headers", "Content-type");*/
		response.setContentType("text/html;charset=UTF-8");
		Map<String, String> paramMap = new HashMap();
		if (!CommonToolUtil.isNumeric(ssoid)) {
			ssoid = CommonToolUtil.AESDncode(ssoid);
		}
		paramMap.put("uid", uid);
		paramMap.put("ssoid", ssoid);
		paramMap.put("pcno", pcno);
		paramMap.put("password", password);
		//CommonToolUtil.checkValid(request, paramMap, configure);
		String info = paperCardService.activatePaperCard(uid, ssoid, pcno, password, siteID);
		CommonToolUtil.outputJson(response, info);
	}

	@RequestMapping(value = "setmeal/FindSetMeal")
	public void FindSetMeal(@RequestParam(value = "siteID", defaultValue = "1") String siteID, HttpServletRequest request, HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setContentType("text/html;charset=UTF-8");
		String info = setMealService.FindSetMeal(siteID);
		CommonToolUtil.outputJson(response, info);
	}

	@RequestMapping(value = "setmeal/FindSetMealByIds")
	public void FindSetMealByIds(@RequestParam(value = "ids") String ids, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setContentType("text/html;charset=UTF-8");
		String info = setMealService.FindSetMealByIds(ids);
		CommonToolUtil.outputJson(response, info);
	}

	@RequestMapping(value = "vote/info")
	public void getVoteInfo(@RequestParam(value = "voteid") String voteid, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		String info = voteInfoService.getVoteInfo(voteid);
		CommonToolUtil.outputJson(response, info);
	}

	@RequestMapping(value = "vote/votecounts")
	public void getVoteCounts(@RequestParam(value = "voteid") String voteid,
			@RequestParam(value = "vote_optionid") String vote_optionid, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		String info = voteInfoService.getVoteCounts(voteid, vote_optionid);
		CommonToolUtil.outputJson(response, info);
	}

	@RequestMapping(value = "vote/clear")
	public void clearVoteInfo(HttpServletRequest request, HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		String info = voteInfoService.clearVoteInfo();
		CommonToolUtil.outputJson(response, info);
	}

	@RequestMapping(value = "vote/time")
	public void getVoteInfoTime(@RequestParam(value = "voteid") String voteid, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		String info = voteInfoService.getVoteInfoTime(voteid);
		CommonToolUtil.outputJson(response, info);
	}

	@RequestMapping(value = "vote/optionInfo")
	public void getOptionInfo(@RequestParam(value = "voteid") String voteid,
			@RequestParam(value = "vote_optionid") String vote_optionid, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		String info = voteInfoService.getOptionInfo(voteid, vote_optionid);
		CommonToolUtil.outputJson(response, info);
	}

	@RequestMapping(value = "vote/recordVote/recordAccessCount")
	public void recordAccessCount(@RequestParam(value = "voteid") int voteid,@RequestParam(value = "time",defaultValue="") String time,@RequestParam(value = "sign",defaultValue="") String sign, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		String info = voteWriteService.recordAccessCount(voteid,time,sign);
		CommonToolUtil.outputJson(response, info);
	}

	@RequestMapping(value = "vote/recordVote/buttonRecordVoteCount")
	public void buttonRecordVoteCount(@RequestParam(value = "voteid") int voteid,
			@RequestParam(value = "vote_optionid") String vote_optionid, @RequestParam(value = "userid") int userid,
			@RequestParam(value = "vote_type") int vote_type, @RequestParam(value = "vote_mode") int vote_mode,
			@RequestParam(value = "vt_most_choose_num") int vt_most_choose_num,@RequestParam(value = "time",defaultValue="") String time,@RequestParam(value = "sign",defaultValue="") String sign,
			HttpServletRequest request,	HttpServletResponse response) {
		String serverName=request.getServerName();
		String originHeader = request.getHeader("Origin");
		System.out.println("button投票 --- serverName："+serverName+"  originHeader:"+originHeader);
		if(compHostOrigin(serverName,originHeader)){
			response.setHeader("Access-Control-Allow-Origin", originHeader);
			response.setHeader("Access-Control-Allow-Credentials", "true");
		}else{
			response.setHeader("Access-Control-Allow-Origin", "*");
		}
		try{
			Cookie voteCookie=  voteWriteService.setVoteCookie(response,request,voteid);
			String info = voteWriteService.buttonRecordVoteCount(voteid, vote_optionid, userid, vote_type, vote_mode,vt_most_choose_num,voteCookie.getValue(),time,sign,request,response);
			CommonToolUtil.outputJson(response, info,voteCookie);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "vote/recordVote/recordVoteCountAndLog")
	public void recordVoteCountAndLog(@RequestParam(value = "voteid") int voteid,
			@RequestParam(value = "vote_optionid") String vote_optionid, @RequestParam(value = "userid") int userid,
			@RequestParam(value = "vote_type") int vote_type, @RequestParam(value = "vote_mode") int vote_mode,
			@RequestParam(value = "info") String info,@RequestParam(value = "time",defaultValue="") String time,@RequestParam(value = "sign",defaultValue="") String sign, HttpServletRequest request, HttpServletResponse response) {
		String serverName=request.getServerName();
		String originHeader = request.getHeader("Origin");
		System.out.println("投票 --- serverName："+serverName+"  originHeader:"+originHeader);
		if(compHostOrigin(serverName,originHeader)){
			response.setHeader("Access-Control-Allow-Origin", originHeader);
			response.setHeader("Access-Control-Allow-Credentials", "true");
		}else{
			response.setHeader("Access-Control-Allow-Origin", "*");
		}
		try{
			Cookie voteCookie=  voteWriteService.setVoteCookie(response,request,voteid);
			String result = voteWriteService.recordVoteCountAndLog(voteid, vote_optionid, userid, vote_type, vote_mode,info,voteCookie.getValue(),time,sign,request,response);
			CommonToolUtil.outputJson(response,result,voteCookie);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private static String cutString(String str){
		int start=str.indexOf(".")+1;
		int end=str.lastIndexOf(":");
		if(end<=0||end<start){
			end=str.length();
		}
		String result=str.substring(start,end);
		return result;
	}
	
	private static boolean compHostOrigin(String host,String origin){
		String cutHost=cutString(host);
		if(origin==null)
			return false;
		String cutOrigin=cutString(origin);
		if(cutHost.equals(cutOrigin))
			return true;
		else
			return false;
	}

	@RequestMapping(value = "vote/recordVote/getUser")
	public void getUser(@RequestParam(value = "userid") int userid, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		String info = voteWriteService.getUser(userid);
		CommonToolUtil.outputJson(response, info);
	}

	/*
	 * 支付宝电脑网站异步通知接口
	 */
	@RequestMapping(value = "pay/createpayrecordpc")
	public void createpayrecord(@RequestParam(value = "payType", defaultValue = "支付宝") String payType,
			@RequestParam(value = "buyer_email", defaultValue = "") String payID,//支付用户号
			@RequestParam(value = "trade_no", defaultValue = "") String payNumber,//订单交易号
			@RequestParam(value = "total_fee", defaultValue = "") String payMoney,//订单金额
			@RequestParam(value = "notify_time", defaultValue = "") String payTime,//支付时间
			@RequestParam(value = "out_trade_no", defaultValue = "") String payOrder,//订单号
			@RequestParam(value = "payChannel", defaultValue = "PC") String payChannel, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setContentType("text/html;charset=UTF-8");
		String info = payService.createpayrecordpc(payType, payID, payNumber, payMoney, payChannel,payTime,payOrder);
		CommonToolUtil.outputJson(response, info);
	}
	/*
	 * 支付宝手机异步通知接口
	 */
	@RequestMapping(value = "pay/createpayrecordapp")
	public void createpayrecordapp(@RequestParam(value = "payType", defaultValue = "支付宝") String payType,
			@RequestParam(value = "buyer_logon_id", defaultValue = "") String payID,//支付用户号
			@RequestParam(value = "trade_no", defaultValue = "") String payNumber,//订单交易号
			@RequestParam(value = "buyer_pay_amount", defaultValue = "") String payMoney,//订单金额
			@RequestParam(value = "notify_time", defaultValue = "") String payTime,//支付时间
			@RequestParam(value = "out_trade_no", defaultValue = "") String payOrder,//订单号
			@RequestParam(value = "payChannel", defaultValue = "移动端") String payChannel, HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setContentType("text/html;charset=UTF-8");
		String info = payService.createpayrecordapp(payType, payID, payNumber, payMoney, payChannel,payTime,payOrder);
		CommonToolUtil.outputJson(response, info);
	}
	
	/**
	 * 支付信息获取接口
	 */
	@RequestMapping(value = "pay/getpaylog")
	public void getpaylog(@RequestParam(value = "payNum") String payNum,
			 HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setContentType("text/html;charset=UTF-8");
		String info = payService.getpaylog(payNum);
		CommonToolUtil.outputJson(response, info);
	}

	/**
	 * 支付电脑网站支付接口
	 * @param WIDout_trade_no
	 * @param WIDsubject
	 * @param WIDtotal_fee
	 * @param WIDbody
	 * @param extra_common_param
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "order/alipayapi")
	public void alipayapi(@RequestParam(value = "WIDout_trade_no") String WIDout_trade_no,
			@RequestParam(value = "WIDsubject") String WIDsubject,
			@RequestParam(value = "WIDtotal_fee") String WIDtotal_fee, @RequestParam(value = "WIDbody") String WIDbody,
			@RequestParam(value = "extra_common_param") String extra_common_param, HttpServletRequest request, HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setContentType("text/html;charset=UTF-8");
		String info = ordersService.alipayapi(WIDout_trade_no, WIDsubject, WIDtotal_fee, WIDbody, extra_common_param);
		CommonToolUtil.outputJson(response, info);
	}

	/*
	 * 支付宝电脑网站同步通知接口
	 */
	@RequestMapping(value = "order/alipayPc")
	public void alipayPc(@RequestParam(value = "body", defaultValue = "") String body,
			@RequestParam(value = "buyer_email", defaultValue = "") String buyer_email, @RequestParam(value = "buyer_id", defaultValue = "") String buyer_id,
			@RequestParam(value = "exterface", defaultValue = "") String exterface, @RequestParam(value = "is_success", defaultValue = "") String is_success,
			@RequestParam(value = "notify_id", defaultValue = "") String notify_id,
			@RequestParam(value = "out_trade_no", defaultValue = "") String out_trade_no,
			@RequestParam(value = "payment_type", defaultValue = "") String payment_type,
			@RequestParam(value = "trade_status", defaultValue = "") String trade_status,
			@RequestParam(value = "extra_common_param", defaultValue = "") String extra_common_param,
			@RequestParam(value = "trade_no", defaultValue = "") String trade_no, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setContentType("text/html;charset=UTF-8");
		String info = ordersService.alipayPc(body, buyer_email, buyer_id, exterface, is_success, notify_id,
				out_trade_no, payment_type, trade_no, trade_status, extra_common_param);
		JSONObject json = JSONObject.fromObject(info);
		String url = json.getString("url");
		response.sendRedirect(url);
	
	}
	
	/**
	 * 支付宝手机网站支付接口
	 */
	@RequestMapping(value = "order/alipayAppPay")
	public void alipayAppPay(@RequestParam(value = "orderNum") String orderNum,
			@RequestParam(value = "mealName") String mealName, @RequestParam(value = "mealMoney") String mealMoney,
			@RequestParam(value = "uid") String uid, HttpServletRequest request, HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setContentType("text/html;charset=UTF-8");
		String info = ordersService.alipayAppPay(orderNum, mealName, mealMoney, uid);
		CommonToolUtil.outputJson(response, info);
	}

	/*
	 * 支付宝手机同步通知接口
	 */
	@RequestMapping(value = "order/alipayApp")
	public void alipayApp(
			@RequestParam(value = "out_trade_no") String out_trade_no,

			HttpServletResponse response) throws IOException {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setContentType("text/html;charset=UTF-8");
		String info = ordersService.alipayApp(out_trade_no);
		JSONObject json = JSONObject.fromObject(info);
		String url = json.getString("url");
		response.sendRedirect(url);
	}
	
	@RequestMapping(value = "column/getColsId")
	public void getColsId(@RequestParam(value = "userId") String userId, @RequestParam(value = "siteID", defaultValue = "1") String siteID, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setContentType("text/html;charset=UTF-8");
		if (!CommonToolUtil.isNumeric(userId) && !"-1".equals(userId)) {
			userId = CommonToolUtil.AESDncode(userId);
		}
		String info = columnsService.getColsId(userId,siteID);
		CommonToolUtil.outputJson(response, info);
	}
	
	@RequestMapping(value = "column/getColumnId")
	public void getColumnId(@RequestParam(value = "userId") String userId, @RequestParam(value = "pId") String pId,@RequestParam(value = "siteID", defaultValue = "1") String siteID, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setContentType("text/html;charset=UTF-8");
		if (!CommonToolUtil.isNumeric(userId) && !"-1".equals(userId)) {
			userId = CommonToolUtil.AESDncode(userId);
		}
		String info = columnsService.getColumnId(userId,pId,siteID);
		CommonToolUtil.outputJson(response, info);
	}

	/**
	 * 对昵称作敏感词检查
	 * @param nickName
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "member/nickName/checkSensiWord")
	public void checkSensiWord(@RequestParam(value = "nickName") String nickName, HttpServletRequest request,
							   HttpServletResponse response) throws Exception {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setContentType("text/html;charset=UTF-8");
		String info = memberService.checkSensiWord(nickName);
		CommonToolUtil.outputJson(response, info);
	}

	@RequestMapping(value = "member/uploadImage", method = {RequestMethod.POST})
	@ResponseBody
	public void uploadImage(HttpServletRequest request, HttpServletResponse response) throws FileUploadException, IOException, URISyntaxException {
		System.out.println("amuc/api/member/uploadImage start ......");
		response.setContentType("text/html;charset=UTF-8");
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Cache-Control","no-cache");
		ServletFileUpload upload = new ServletFileUpload(new DiskFileItemFactory());
		List<FileItem> items = upload.parseRequest(request);
		String userId = null;
		for (FileItem item : items) {
			if (item.getFieldName().equals("userId")) {
				userId = item.getString();
			}
		}
		System.out.println("amuc/api/member/uploadImage, uid: " + userId);
		MultipartEntity entity = getMultipartEntity(items);
		String info = memberService.uploadPortrait(entity, userId);
		System.out.println("amuc/api/member/uploadImage info: " + info);
		CommonToolUtil.outputJson(response, info);
	}
}