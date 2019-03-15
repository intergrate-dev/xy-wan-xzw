package com.founder.mobileinternet.cmsinterface.service.cms.impl;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Random;
import java.util.UUID;

import javax.xml.rpc.ServiceException;

import net._139130.www.MTPacks;
import net._139130.www.MessageData;
import net._139130.www.WebServiceLocator;
import net._139130.www.WebServiceSoapStub;
import net.sf.json.JSONObject;

import org.apache.axis.MessageContext;
import org.apache.axis.transport.http.HTTPConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.founder.mobileinternet.cmsinterface.pojo.Configure;
import com.founder.mobileinternet.cmsinterface.pojo.cms.LogInfo;
import com.founder.mobileinternet.cmsinterface.service.RedisKey;
import com.founder.mobileinternet.cmsinterface.service.RedisManager;
import com.founder.mobileinternet.cmsinterface.service.cms.AppService;
import com.founder.mobileinternet.cmsinterface.util.CommonToolUtil;

@Service
public class AppServiceImpl implements AppService {
	@Autowired
    private RedisManager redisManager;
	@Autowired
	private Configure configure;

	public String getConfig(int appID, int siteID) {
		String key = RedisKey.APP_START_KEY + appID;
		String value=getDataResult(key, "/startup.do?appID=" + appID + "&siteID=" + siteID);
		/*
		if(null!=value){
			String appInfo=CommonToolUtil.getData(configure.getAppServerUrl()+"/getAppInfo.do");
			JSONObject jsonObj=JSONObject.fromObject(value);
			if(jsonObj.has("version") && !jsonObj.getString("version").equals(appInfo))
				jsonObj.put("version",appInfo);
			value=jsonObj.toString();
		}
		*/
		return value;
	}

	public String getCats(String code, String siteId, String keywords) {
		String key = redisManager.getCatKeyBySite(siteId, code);
		if(keywords==null || "".equals(keywords.trim())){
			return getDataResult(key, "/getCats.do?code=" + code + "&siteID=" + siteId);
		}
		return CommonToolUtil.getData(configure.getAppServerUrl() + "/myCats.do?code=" + code + "&siteID=" + siteId + "&keywords=" + keywords);
	}

	public boolean logInfo(LogInfo logInfo) {
		String url = configure.getAppServerUrl() + "/loginfo.do";
		return CommonToolUtil.postBeanData(url, logInfo);
	}

	@Override
	public String login(String user, String pwd) {
		String url = configure.getAppServerUrl() + "/login.do";
		JSONObject data = new JSONObject();
		data.put("user", user);
		data.put("pwd", pwd);
		try {
			return CommonToolUtil.getData(url, data.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "0";
	}

	@Override
	public String logout(int loginID, long time, String sign, String _url) {
		String url = configure.getAppServerUrl() + "/logout.do";
		JSONObject data = new JSONObject();
		data.put("loginID", loginID);
		data.put("time", time);
		data.put("sign", sign);
		data.put("url", _url);
		try {
			return CommonToolUtil.getData(url, data.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "0";
	}

	public String getVerifiCode(String phoneNo) {
		Random r = new Random();
		String verifiCode = "";
		for (int i = 0; i < 4; i++) {
			verifiCode += r.nextInt(10);
		}
		doSendSms(phoneNo, "验证码" + verifiCode);
		return verifiCode;
	}

	@Override
    public String siteConf(int siteID) {
        String key = RedisKey.SITE_CONF_KEY + siteID;
        String urlParam = "/siteConf.do?siteID="+siteID;
		return getData(key, urlParam);
    }

	@Override
	public String getAppInfo(String appKey, String versionCode,String channel) {

		StringBuffer key = new StringBuffer(RedisKey.APP_MOBILEAPP + appKey);
		if(null != channel && !"null".equals(channel)){
			key.append("."+channel);	
		}
		
		String value = redisManager.get(key.toString());
		if (value == null) {
			String url = this.configure.getAppServerUrl()
					+ "/mobileApp.do?appKey=" + appKey+"&channel="+channel;
			if (CommonToolUtil.canGetData(url))
				value = redisManager.get(key.toString());
		}
		if (StringUtils.hasText(value)) {
			JSONObject newInfo = JSONObject.fromObject(value);
			if (newInfo.isEmpty() || handleVersion(versionCode) >= handleVersion((null == newInfo.getString("new_version"))?"0": newInfo.getString("new_version"))) {
				JSONObject date = new JSONObject();
				date.put("update", "No");
				value = date.toString();
			}
		}

		return value;
	}
	@Override
	public String message(int page, int siteID) {
		String key = redisManager.getKeyBySite(RedisKey.APP_MESSAGE_LIST_KEY, siteID) + page;
		return getDataResult(key, "/message.do?page=" + page + "&siteID=" + siteID);
	}

	private String getDataResult(String key, String urlParam){
			//String value = redisManager.get(key);
			String value = null;
			if (null == value) {
				String url = configure.getAppServerUrl() + urlParam;
	//			System.out.println("没有缓存数据，重新获取。  请求地址：" + url);
				if (CommonToolUtil.canGetData(url))
					value = redisManager.get(key);
			}
			return value;
		}

	private void doSendSms(String phoneNo, String nayiyou) {
		WebServiceLocator locator = new WebServiceLocator();
		WebServiceSoapStub stub = null;
		try {
			stub = (WebServiceSoapStub) locator.getWebServiceSoap(
					new URL(configure.getSms_url()));
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		} catch (ServiceException e1) {
			e1.printStackTrace();
		}
		stub._setProperty(MessageContext.HTTP_TRANSPORT_VERSION,
				HTTPConstants.HEADER_PROTOCOL_V11);
		MTPacks pack = new MTPacks();
		pack.setBatchID(UUID.randomUUID().toString());
		pack.setUuid(pack.getBatchID());
		pack.setSendType(0);
		pack.setMsgType(1);
		pack.setBatchName("测试批次");
		MessageData[] msgs = new MessageData[1];
		msgs[0] = new MessageData();
		msgs[0].setPhone(phoneNo);
		msgs[0].setContent(nayiyou);
	
		pack.setMsgs(msgs);
		try {
			/*resp = */stub.post(configure.getSms_userName(),
					configure.getSms_password(), pack);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	private String getData(String key, String urlParam) {
		String value = redisManager.get(key);
		if (value == null) {
			String url = configure.getAppServerUrl() + urlParam;
			//System.out.println("没有缓存数据，重新获取。  请求地址：" + url);
			if (CommonToolUtil.canGetData(url))
				 value = redisManager.get(key);
		}
		return value;
	}

	private int handleVersion(String version){
		return Integer.parseInt(version.replaceAll("\\.", ""));
	}

	@Override
	public String userIsExist(String user) {
		String url = configure.getAppServerUrl() + "/userIsExist.do";
		JSONObject data = new JSONObject();
		data.put("user", user);
		try {
			return CommonToolUtil.getData(url, data.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "0";
	}

	@Override
	public String getUrl() {
		String url = configure.getNewAppServerUrl() + "/getUrl.do";
		try {
			return CommonToolUtil.getData(url);
		} catch (Exception e) {
			return CommonToolUtil.buildFailureObjectResult("请求内网接口失败");
		}
	}

	@Override
	public String myInfo(int userID) {
		String url = configure.getNewAppServerUrl() + "/myInfo.do?userID="+userID;
		try {
			return CommonToolUtil.getData(url);
		} catch (Exception e) {
			return CommonToolUtil.buildFailureObjectResult("请求内网接口失败");
		}
	}

    @Override
    public String qrCodeLogin(String user, String token) {
        String url = configure.getAppServerUrl() + "/qrCodeLogin.do";
        JSONObject data = new JSONObject();
        data.put("user", user);
        data.put("token", token);
        try {
            return CommonToolUtil.getData(url, data.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "0";
    }
    
    @Override
	public String getJsApiTicket() {
		String url = configure.getAppServerUrl() + "/getJsApiTicket.do";
		return CommonToolUtil.getData(url);
	}
}
