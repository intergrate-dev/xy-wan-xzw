package com.founder.mobileinternet.cmsinterface.service.cms;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Cookie;

public interface VoteWriteService {
	public String recordAccessCount(int voteid,String time,String sign);

	public String buttonRecordVoteCount(int voteid, String vote_optionid, int userid, int vote_type, int vote_mode,
			int vt_most_choose_num,String uuidCodeStr,String time,String sign,HttpServletRequest request, HttpServletResponse response);

	public String recordVoteCountAndLog(int voteid, String vote_optionid, int userid, int vote_type, int vote_mode,
			String info,String uuidCodeStr,String time,String sign,HttpServletRequest request, HttpServletResponse response);

	public String getUser(int userid);
	
	public String getUserUUID() throws Exception;
	public Cookie setVoteCookie(HttpServletResponse response, HttpServletRequest request,int voteId) throws Exception;
	 public Cookie getVoteCookie(HttpServletRequest request) throws Exception;

}
