package com.founder.mobileinternet.cmsinterface.service.cms.impl;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.founder.mobileinternet.cmsinterface.pojo.Configure;
import com.founder.mobileinternet.cmsinterface.pojo.cms.SubjectQAInfo;
import com.founder.mobileinternet.cmsinterface.service.RedisKey;
import com.founder.mobileinternet.cmsinterface.service.RedisManager;
import com.founder.mobileinternet.cmsinterface.service.cms.SubjectService;
import com.founder.mobileinternet.cmsinterface.util.CommonToolUtil;
@Service
public class SubjectServiceImpl implements SubjectService {
	@Autowired
	private RedisManager redisManager;
	@Autowired
	private Configure configure;
	
	@Override
	public String getSubjectList(int siteID,int page,Long userid) {

		String key = redisManager.getKeyBySite(RedisKey.APP_SUBJECT_LIST_KEY, siteID) + page;
		String url = this.configure.getAppServerUrl() + "/subjectList.do?siteID="
				+siteID+"&page="+page+"&userid="+userid;
		String sublist = CommonToolUtil.getDataResult(key, url,redisManager,configure);
		key = RedisKey.MY_SUBJECT_SUBIDS_KEY + userid;
		url = this.configure.getAppServerUrl() + "/mySubjectIDsSubscribe.do?userid=" + userid;
		String ids = CommonToolUtil.getDataResult(key, url, redisManager, configure);

		//内网api的数据格式是{list:[]}，添加subIDs即可
		JSONObject jsonObj = JSONObject.fromObject(sublist);
		jsonObj.put("subIDs",ids);
		JSONArray jsonArr = jsonObj.getJSONArray("list") ;
		jsonObj.put("list", getSubCount(jsonArr)) ;

		return jsonObj.toString();
	}

	@Override
	public String getSubjectListWithCat(int siteID, int catID,int page,Long userid) {

		String key = RedisKey.APP_SUBJECT_CAT_KEY+catID+"."+page;
		String url = this.configure.getAppServerUrl() + "/subjectListWithCat.do?siteID="
				+siteID+"&catID="+catID+"&page="+page+"&userid="+userid;
		String sublist=CommonToolUtil.getDataResult(key, url,redisManager,configure);

		key=RedisKey.MY_SUBJECT_SUBIDS_KEY+userid;
		url=this.configure.getAppServerUrl() + "/mySubjectIDsSubscribe.do?userid="+userid;
		String ids=CommonToolUtil.getDataResult(key, url,redisManager,configure);

		//内网api的数据格式是{list:[]}，添加subIDs即可
		JSONObject jsonObj = JSONObject.fromObject(sublist);
		jsonObj.put("subIDs",ids);
        JSONArray jsonArr = jsonObj.getJSONArray("list") ;
        jsonObj.put("list", getSubCount(jsonArr)) ;
		
		return jsonObj.toString();
	}

	@Override
	public String getSubject(int siteID, int id) {

		String key = RedisKey.APP_SUBJECT_KEY+ id;
		String url = this.configure.getAppServerUrl() + "/getSubject.do?siteID="
				+siteID+"&id="+id;
		String value = CommonToolUtil.getDataResult(key, url,redisManager,configure);
		return value;
	}
	
	//我发起的话题列表
	@Override
	public String getmySubject(int siteID, Long userid, int page) {

		String key = RedisKey.MY_SUBJECT_KEY+userid+"."+page;
		String url = this.configure.getAppServerUrl() + "/mySubject.do?siteID="
				+siteID+"&userid="+userid+"&page="+page;
		String value=CommonToolUtil.getDataResult(key, url,redisManager,configure);
		return value;
	}

	@Override
	public String getmySubjectSubscribe(int siteID, Long userid, int page) {

		String key = RedisKey.MY_SUBJECT_SUBS_KEY+ userid+"."+page;
		String url = this.configure.getAppServerUrl() + "/mySubjectSubscribe.do?siteID="
				+siteID+"&userid="+userid+"&page="+page;
		String value = CommonToolUtil.getDataResult(key, url,redisManager,configure);
		return value;
	}

	@Override
	public boolean comitsQA(SubjectQAInfo sQAInfo) {
		ObjectMapper objectMapper = new ObjectMapper();
		try{
			String value=objectMapper.writeValueAsString(sQAInfo);
			redisManager.addDelaySubjectQA(value);
		}catch (Exception e){
			e.printStackTrace();
		}

		return true;
	}

	@Override
	public String getQuestionList(int siteID, int subjectID, int page) {
		String key = RedisKey.APP_SUBJECT_QALIST_KEY+subjectID+"."+ page;
		String url = this.configure.getAppServerUrl() + "/questionList.do?siteID="
				+siteID+"&subjectID="+subjectID+"&page="+page;
		String value = CommonToolUtil.getDataResult(key, url,redisManager,configure);
		
		return CommonToolUtil.refreshCountsInList(RedisKey.NIS_EVENT_SUBJECTQA, value,redisManager);
	}

	@Override
	public String getQuestionListHot(int siteID, int subjectID, int page) {
		String key = RedisKey.APP_SUBJECT_HOT_KEY+ subjectID +"." +page;
		String url = this.configure.getAppServerUrl() + "/questionListHot.do?siteID="
				+siteID+"&subjectID="+subjectID+"&page="+page;
		String value =CommonToolUtil.getDataResult(key, url,redisManager,configure);
		
		return CommonToolUtil.refreshCountsInList(RedisKey.NIS_EVENT_SUBJECTQA, value,redisManager);
	}

	@Override
	public String getQuestionDetail(int siteID, int fileID) {
		String key= RedisKey.APP_SUBJECT_QA_KEY+fileID;
		String url = configure.getAppServerUrl() + "/questionDetail.do?subjectQAID="+fileID+"&siteID="+siteID;
		String value=CommonToolUtil.getDataResult(key, url,redisManager,configure);
		if(null!=value)
			value=CommonToolUtil.refreshCount(RedisKey.NIS_EVENT_SUBJECTQA, JSONObject.fromObject(value),redisManager);
		return value;
	}

	@Override
	public String MyQuestion(int siteID, Long userID,int page) {
		String key=RedisKey.MY_SUBJICTQA_KEY + userID + "." + page;
		String url = this.configure.getAppServerUrl() + "/myQuestion.do?siteID="
				+siteID+"&userID="+userID+"&page="+page;
		String value=CommonToolUtil.getDataResult(key, url,redisManager,configure);
		return CommonToolUtil.refreshCountsInList(RedisKey.NIS_EVENT_SUBJECTQA, value,redisManager);
	}
	
	// 获取话题关注按数
	private JSONArray getSubCount(JSONArray jsonArr) {
		int size = jsonArr.size();
		for (int i = 0; i < size; i++) {
			JSONObject jsonObj = (JSONObject)jsonArr.get(i);
			String id = jsonObj.getString("fileId");
			if (redisManager.hget(RedisKey.NIS_EVENT_SUBCRIBE_SUBJECT, id) != null) {
				long count = Long.parseLong(redisManager.hget(RedisKey.NIS_EVENT_SUBCRIBE_SUBJECT, id));
				jsonObj.put("countFan", count < 0 ? 0 : count) ;  //有没有可能出现<0的情况？？
			} 
		}
		return jsonArr ;
	}
}
