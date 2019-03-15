package com.founder.mobileinternet.cmsinterface.service.cms;

public interface MemberSynService {
	public String registerEx(String ssoid, String username, String nickname, String mobile, String email,
			String password);

	public String modify(String nickname, String mobile);

	public String updatePassword(String password, String mobile);

	public String loginByOther(int type, String oid, String name);

}
