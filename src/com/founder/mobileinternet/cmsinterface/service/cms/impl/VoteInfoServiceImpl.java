package com.founder.mobileinternet.cmsinterface.service.cms.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;
import net.sf.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.founder.mobileinternet.cmsinterface.pojo.Configure;
import com.founder.mobileinternet.cmsinterface.service.cms.VoteInfoService;
import com.founder.mobileinternet.cmsinterface.util.CommonToolUtil;
import com.founder.mobileinternet.cmsinterface.service.RedisManager;

@Service
public class VoteInfoServiceImpl implements VoteInfoService {

	@Autowired
	private Configure configure;
	@Autowired
	private RedisManager redisManager;
	private static final String dataName = "amuc.vote.voteInfo";  //缓存进redis数据库中的表名

	public String getVoteInfo(String voteid) {
		/*String url = configure.getInnerApiUrl() + "/api/vote/info.do?voteid=" + voteid;
		return CommonToolUtil.getData(url);*/
		Integer vid = null;
		try{
			vid = Integer.parseInt(voteid);
		}catch(Exception e){
			e.printStackTrace();
			return JSONObject.fromObject("{'info':'参数类型错误','state':'error'}").toString();
		}
		String voteinfo = redisManager.hget(dataName, dataName+vid);
		if(voteinfo!=null){
			String start = redisManager.hget(dataName+".start", dataName+vid);
			String end = redisManager.hget(dataName+".end", dataName+vid);
			String jsonStr = redisManager.hget(dataName, dataName+vid);
			JSONObject info = JSONObject.fromObject(jsonStr);
			JSONObject infoinfo1 = JSONObject.fromObject(info.getString("info"));
			
			//选项id:1,2,3
			String vs_votetheme_list1 = infoinfo1.getString("vs_votetheme_list");
			JSONArray vs_votetheme_list = JSONArray.fromObject(vs_votetheme_list1);
			JSONArray vt_voteoption_list = JSONArray.fromObject(vs_votetheme_list.getJSONObject(0).getString("vt_voteoption_list"));
			JSONArray vt_voteoption_list1 = new JSONArray();
			if(vt_voteoption_list.size()>0){  
				for(int i=0;i<vt_voteoption_list.size();i++){  
					JSONObject vt_voteoption = vt_voteoption_list.getJSONObject(i);
					JSONObject voteoption = this.getVoteCountsByRedis(voteid,vt_voteoption);
					vt_voteoption_list1.add(i,voteoption);
				}  
				vs_votetheme_list.getJSONObject(0).put("vt_voteoption_list",vt_voteoption_list1);
				infoinfo1.put("vs_votetheme_list",vs_votetheme_list);
				info.put("info",infoinfo1);
				jsonStr = info.toString();
			}
			
			
			if(start==null&&end==null){//判断是否有时间缓存
				try{
					String startime = infoinfo1.getString("vs_startime");
					String endtime = infoinfo1.getString("vs_endtime");
					Integer isBegin = this.TimeState(startime, endtime);
					redisManager.hset(dataName+".start", dataName+vid, startime);
					redisManager.hset(dataName+".end", dataName+vid, endtime);
					return this.getFinalJsonStr(jsonStr, isBegin, jsonStr.indexOf("{")+1);
				}catch(Exception e){
					e.printStackTrace();
					return JSONObject.fromObject("{'info':'缓存日期转换错误','state':'error'}").toString();
				}
			}else{//有时间缓存时
				if(start==null||end==null){
					return JSONObject.fromObject("{'info':'开始时间或结束时间为空','state':'error'}").toString();
				}else{
					try{
						Integer isBegin = this.TimeState(start, end);
						return this.getFinalJsonStr(jsonStr, isBegin, jsonStr.indexOf("{")+1);
					}catch(Exception e){
						e.printStackTrace();
						return JSONObject.fromObject("{'info':'缓存日期转换错误','state':'error'}").toString();
					}
				}
			}
		}
		return "{'info':'缓存不存在该投票','state':'error'}";
	};

	public String getVoteCounts(String voteid, String vote_optionid) {
		/*String url = configure.getInnerApiUrl() + "/api/vote/votecounts.do?voteid=" + voteid
				+ "&vote_optionid=" + vote_optionid;
		return CommonToolUtil.getData(url);*/
		if(voteid == ""||vote_optionid == ""){
			return JSONObject.fromObject("{'info':'请检查参数是否为空','state':'error'}").toString();
		}else{
			String[] vote_optionids = vote_optionid.split(",");
			//投票总数
			String voteAccessCount = redisManager.get("vote.accesscount."+voteid);
			String voteCount = redisManager.get("vote.voteCount."+voteid);
			if(voteAccessCount==null){
				voteAccessCount = "0";
			}
			if(voteCount==null){
				voteCount = "0";
			}
			
			JSONArray option_list = new JSONArray();
			JSONObject info = new JSONObject();
			JSONObject jsonStr = new JSONObject();
			
			for(int i = 0; i<vote_optionids.length;i++){
				JSONObject option = new JSONObject();
				String opCount = redisManager.hmget("vote.option.personinfo." + voteid +"."+ vote_optionids[i], "vote.optionCount."+voteid+"."+vote_optionids[i]).get(0);
				if(opCount==null){
					opCount = "0";
				}
				option.put("option_count",opCount);
				option.put("optionid",vote_optionids[i]);
				option_list.add(option);
			}
			info.put("option_list",option_list);
			info.put("vote_access_count",voteAccessCount);
			info.put("vote_count",voteCount);
			
			jsonStr.put("info",info);
			jsonStr.put("state","success");
			
			return jsonStr.toString();
		}
	};

	public String clearVoteInfo() {
		String url = configure.getInnerApiUrl() + "/api/vote/clear.do";
		return CommonToolUtil.getData(url);
	};

	public String getVoteInfoTime(String voteid) {
		String url = configure.getInnerApiUrl() + "/api/vote/time.do?voteid=" + voteid;
		return CommonToolUtil.getData(url);
	};

	public String getOptionInfo(String voteid, String vote_optionid) {
		String url = configure.getInnerApiUrl() + "/api/vote/optionInfo.do?voteid=" + voteid + "&vote_optionid="
				+ vote_optionid;
		return CommonToolUtil.getData(url);
	};
	
	/**
	 * @author
	 * @param begin 活动开始时间
	 * @param end 活动结束时间
	 * @return 0:活动进行中,1:活动未开始,2:活动以结束
	 * @throws Exception
	 */
	public Integer TimeState(String begin,String end)throws Exception{
		Date currentTime = new Date();
		Date beginTime = null;
		Date endTime = null;
			
		beginTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(begin);
		endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(end);
		
		if(beginTime.getTime()<currentTime.getTime()&&endTime.getTime()>currentTime.getTime()){
			return 0;//活动进行中
		}else if(beginTime.getTime()>=currentTime.getTime()){
			return 1;//活动未开始
		}else if(endTime.getTime()<=currentTime.getTime()){
			return 2;//活动已结束
		}
		return null;
		
	}
	
	/**
	 * @author
	 * @param voteinfo 缓存json字符串 
	 * @param isBegin 投票状态码
	 * @param index 插入点
	 * @return 接口最终json包
	 */
	public String getFinalJsonStr(String voteinfo,Integer isBegin,Integer index){
		StringBuilder sb = new StringBuilder();
		String isbegin = "\"isbegin\":"+isBegin+",";
		sb.append(voteinfo).insert(index, isbegin); 
		return sb.toString();
	}
	
	/**
	 * 
	 * @param voteid 投票设置ID
	 * @param vt_voteoption 投票选项ID
	 * @return 
	 */
	public JSONObject getVoteCountsByRedis(String voteid,JSONObject vt_voteoption){
		
		String voteoptionid = vt_voteoption.getString("vo_id");
		String opCount = redisManager.hmget("vote.option.personinfo." + voteid +"."+ voteoptionid, "vote.optionCount."+voteid+"."+voteoptionid).get(0);
		if(opCount==null){
			opCount = "0";
		}
		vt_voteoption.put("vo_votes",opCount);
		return vt_voteoption;
	}
}
