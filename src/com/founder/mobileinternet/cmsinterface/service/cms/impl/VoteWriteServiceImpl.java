package com.founder.mobileinternet.cmsinterface.service.cms.impl;

import com.founder.mobileinternet.cmsinterface.service.RedisManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



import com.founder.mobileinternet.cmsinterface.pojo.Configure;
import com.founder.mobileinternet.cmsinterface.service.cms.VoteWriteService;
import com.founder.mobileinternet.cmsinterface.util.CommonToolUtil;


import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Cookie;
import java.net.URLEncoder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class VoteWriteServiceImpl implements VoteWriteService {

	@Autowired
	private Configure configure;

    @Autowired
    private RedisManager redisManager;

	public String recordAccessCount(int voteid,String time,String sign) {
		String url = configure.getInnerApiUrl() + "/api/vote/recordVote/recordAccessCount.do?voteid=" + voteid+"&time="+time+"&sign="+sign;
		return CommonToolUtil.getData(url);
	};

	public String buttonRecordVoteCount(int voteid, String vote_optionid, int userid, int vote_type, int vote_mode,
			int vt_most_choose_num,String uuidCodeStr,String time,String sign,HttpServletRequest request, HttpServletResponse response) {
		String url ="";
		try{
		url = configure.getInnerApiUrl() + "/api/vote/recordVote/buttonRecordVoteCount.do?voteid=" + voteid
				+ "&vote_optionid=" + vote_optionid + "&userid=" + userid + "&vote_type=" + vote_type + "&vote_mode="
				+ vote_mode + "&vt_most_choose_num=" + vt_most_choose_num+"&ip="+this.getIpAddr(request)+"&cookie="+uuidCodeStr+"&time="+time+"&sign="+sign;
		}catch(Exception e){
			e.printStackTrace();
		}
		return CommonToolUtil.getData(url);
	};

	public String recordVoteCountAndLog(int voteid, String vote_optionid, int userid, int vote_type, int vote_mode,
			String info,String uuidCodeStr,String time,String sign,HttpServletRequest request, HttpServletResponse response) {
		String url ="";
		try{
			url = configure.getInnerApiUrl() + "/api/vote/recordVote/recordVoteCountAndLog.do?voteid=" + voteid
					+ "&vote_optionid=" + vote_optionid + "&userid=" + userid + "&vote_type=" + vote_type + "&vote_mode="
					+ vote_mode + "&info=" + URLEncoder.encode(info,"utf-8") +"&ip="+this.getIpAddr(request)+"&cookie="+uuidCodeStr+"&time="+time+"&sign="+sign;
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println("--- 调内网url:"+url);
		return CommonToolUtil.getData(url);
	};

	public String getUser(int userid){
		String url = configure.getInnerApiUrl() + "/api/vote/recordVote/getUser.do?userid=" + userid;
		return CommonToolUtil.getData(url);
	};
	
	/**
     * 获取投票者IP
     * 
     * 在一般情况下使用Request.getRemoteAddr()即可，但是经过nginx等反向代理软件后，这个方法会失效。
     * 
     * 本方法先从Header中获取X-Real-IP，如果不存在再从X-Forwarded-For获得第一个IP(用,分割)
	 * 经过多次代理可能有多个ip串，取第一个即可
     * 如果还不存在则调用Request .getRemoteAddr()。
     * 
     * @param request
     * @return
     */
    private String getIpAddr(HttpServletRequest request) throws Exception{
        String ip = request.getHeader("X-Real-IP");
        if (!StringUtils.isBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }
        ip = request.getHeader("X-Forwarded-For");
        if (!StringUtils.isBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个IP值，第一个为真实IP。
            int index = ip.indexOf(',');
            if (index != -1) {
                return ip.substring(0, index);
            } else {
                return ip;
            }
        } else{
		    ip = request.getHeader("Proxy-Client-IP");
		}
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("HTTP_CLIENT_IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");  
        }
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
           ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
           ip = request.getRemoteAddr();
        }
	    return ip;
    }
    
    /**
     * 
     * 创建一个用户客户端的cookie，与ip共同判断用户数据，如果cookie没存过，就创建一个cookie
     * @param request
     * @throws Exception
     */
    public Cookie setVoteCookie(HttpServletResponse response, HttpServletRequest request,int voteId) throws Exception{
    	
    	Cookie existVoteCookie=this.getVoteCookie(request);
    	//System.out.println("existValue"+existVoteCookie.getValue());
    	if(null == existVoteCookie){
    		String uuidCode=this.getUserUUID();
    		existVoteCookie = new Cookie("existvotecookie",uuidCode);
    	}
    	// 获取截止时间与当前时间之间的秒数
    	SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	String endTime = new String();
    	if (redisManager.hexists("amuc.vote.voteInfo.end", "amuc.vote.voteInfo"+voteId)){
    		endTime = redisManager.hget("amuc.vote.voteInfo.end", "amuc.vote.voteInfo"+voteId);
    	}
    	Date endDate = format.parse(endTime);
    	long endDateSecond = endDate.getTime()/1000;
    	long currentDate=new Date().getTime()/1000; 
    	//获取cookie的存活时间，投票活动的截止时间与当前时间的相差秒数
    	existVoteCookie.setMaxAge((int) (endDateSecond-currentDate));
    	existVoteCookie.setPath("/");
    	return existVoteCookie;
    }
    
    /**
     * 获取cookie，返回一个Boolean值，存在则返回true
     * @param request
     * @return
     * @throws Exception
     */
    public Cookie getVoteCookie(HttpServletRequest request) throws Exception{
    	String existValue = "";
    	// 从request中获取cookie
    	Cookie[] cookies = request.getCookies();
    	if(cookies!=null && cookies.length>0){
    		// 遍历cookies
    		for(int i=0;i<cookies.length;i++){
    			Cookie curCookie = cookies[i];
    			if(curCookie.getName().equalsIgnoreCase("existvotecookie")){
    				return curCookie;
    			}	
    		}
    	}
    	return null;
    }
    
    /**
     * 生成一个UUID,作为cookie的值
     * @return
     * @throws Exception
     */
    public String getUserUUID() throws Exception{
    	
    	UUID uuid = UUID.randomUUID();   
        String uuidStr = uuid.toString();
        uuidStr = uuidStr.replaceAll("-", "");
        return uuidStr;
    }

}
