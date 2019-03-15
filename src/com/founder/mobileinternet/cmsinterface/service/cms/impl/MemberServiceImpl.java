package com.founder.mobileinternet.cmsinterface.service.cms.impl;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;

import net.sf.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.founder.mobileinternet.cmsinterface.pojo.Configure;
import com.founder.mobileinternet.cmsinterface.service.cms.MemberService;
import com.founder.mobileinternet.cmsinterface.upload.UploadFactories;
import com.founder.mobileinternet.cmsinterface.util.CommonToolUtil;

@Service
public class MemberServiceImpl implements MemberService {
	private final static String ILLEGAL_WORD = "5";//非法词类型
	private final static String CALL_FAIL_CODE = "0";//非法词类型
	private final static String CALL_FAIL_MSG = "请求失败！";//非法词类型

	@Autowired
	private Configure configure;

	public String register(String nickname, String mobile, String email, String password, String code,
			String deviceid) {
		String url = configure.getInnerApiUrl() + "/api/member/register.do?nickname=" + nickname + "&mobile=" + mobile
				+ "&email=" + email + "&password=" + password + "&code=" + code + "&deviceid=" + deviceid;
		return CommonToolUtil.getData(url);
	};

	public String registerEx(String nickname, String mobile, String email, String password) {
		String url = configure.getInnerApiUrl() + "/api/member/registerEx.do?nickname=" + nickname + "&mobile="
				+ mobile + "&email=" + email + "&password=" + password;
		return CommonToolUtil.getData(url);
	};

	public String Login(String mobile, String email, String password) {
		String url = configure.getInnerApiUrl() + "/api/member/Login.do?mobile=" + mobile + "&email=" + email
				+ "&password=" + password;
		return CommonToolUtil.getData(url);
	};

	public String loginByOther(int type, String oid, String name) {
		String url = configure.getInnerApiUrl() + "/api/member/loginByOther.do?type=" + type + "&oid=" + oid
				+ "&name=" + name;
		return CommonToolUtil.getData(url);
	};

	public String uploadPortrait(MultipartEntity entity, String userId)
			throws URISyntaxException, IOException {
		String url = configure.getInnerApiUrl() + "/api/member/uploadPortrait.do?uid=" + userId;
		return sendFiles(entity, url);
		
	};

	private String sendFiles(MultipartEntity entity, String url) throws URISyntaxException, IOException {
        String result = null;

        ThreadSafeClientConnManager manager = UploadFactories.getConnManager();
        HttpClient httpClient = new DefaultHttpClient(manager);

        HttpPost post = new HttpPost();
        post.setEntity(entity);
        post.setURI(new URI(url));

        HttpResponse response = httpClient.execute(post);
		System.out.println("statusCode: " + response.getStatusLine().getStatusCode());
		if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
            HttpEntity responseEntity = response.getEntity();
            if (responseEntity != null) {
                result = EntityUtils.toString(responseEntity, HTTP.UTF_8);
            }
        }
		try {
			Thread.sleep(3000L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return result;
    }

	public String modify(String uid, String nickname, String mobile, String email, String password, String birthday,
			String region, String sex, String address, String code, String deviceid) {
		String url = configure.getInnerApiUrl() + "/api/member/modify.do?nickname=" + nickname + "&mobile=" + mobile
				+ "&email=" + email + "&password=" + password + "&code=" + code + "&deviceid=" + deviceid + "&birthday="
				+ birthday + "&region=" + region + "&sex=" + sex + "&address=" + address + "&uid=" + uid;
		return CommonToolUtil.getData(url);
	};

	public String ForgetPassword(String password, String mobile) {
		String url = configure.getInnerApiUrl() + "/api/member/ForgetPassword.do?password=" + password + "&mobile="
				+ mobile;
		return CommonToolUtil.getData(url);
	};

	public String getPortrait(String uid) {
		String url = configure.getInnerApiUrl() + "/api/member/getPortrait.do?uid=" + uid;
		return CommonToolUtil.getData(url);
	};

	public String getUserMessage(String ssoid) {
		String url = configure.getInnerApiUrl() + "/api/member/getUserMessage.do?ssoid=" + ssoid;
		return CommonToolUtil.getData(url);
	};

	public String score(String source, String oriID, String tc, String table) {
		String url = configure.getInnerApiUrl() + "/api/member/score.do?source=" + source + "&id=" + oriID + "&tc="
				+ tc + "&table=" + table;
		return CommonToolUtil.getData(url);
	};

	public String scoreList(String source, String oriID, String tc, String table, String getAll, String curPage,
			String pageSize) {
		String url = configure.getInnerApiUrl() + "/api/member/scoreList.do?source=" + source + "&id=" + oriID
				+ "&tc=" + tc + "&table=" + table + "&getAll=" + getAll + "&curPage=" + curPage + "&pageSize="
				+ pageSize;
		return CommonToolUtil.getData(url);
	};

	@Override
	public String checkSensiWord(String nickName) throws Exception {
		String url = configure.getInnerApiUrl() + "/xy/wordList/checkSensitiveWord.do?content=" +
				URLEncoder.encode(nickName, "UTF-8") + "&type=" + ILLEGAL_WORD + "&method=checkSensitive";
		JSONObject json = null;
		String data = CommonToolUtil.getData(url);
		if (data.equals("0")) {
			json = new JSONObject();
			json.put("code", CALL_FAIL_CODE);
			json.put("inform", CALL_FAIL_MSG);
		}
		return json == null ? data : json.toString();
	}

}
