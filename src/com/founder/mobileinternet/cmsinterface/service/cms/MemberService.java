package com.founder.mobileinternet.cmsinterface.service.cms;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.http.entity.mime.MultipartEntity;

public interface MemberService {
	public String register(String nickname, String mobile, String email, String password, String code, String deviceid);

	public String registerEx(String nickname, String mobile, String email, String password);

	public String Login(String mobile, String email, String password);

	public String loginByOther(int type, String oid, String name);

	public String uploadPortrait(MultipartEntity entity,String uid) throws URISyntaxException, IOException;

	public String modify(String uid, String nickname, String mobile, String email, String password, String birthday,
			String region, String sex, String address, String code, String deviceid);

	public String ForgetPassword(String password, String mobile);

	public String getPortrait(String uid);

	public String getUserMessage(String ssoid);

	public String score(String source, String oriID, String tc, String table);

	public String scoreList(String source, String oriID, String tc, String table, String getAll, String curPage,
			String pageSize);

	String checkSensiWord(String nickName) throws Exception;
}
