package com.founder.mobileinternet.cmsinterface.service.cms;

import com.founder.mobileinternet.cmsinterface.pojo.cms.SubjectQAInfo;

public interface SubjectService {

	/**
	 * 话题列表
	 */
	public String getSubjectList(int siteID,int page,Long userid);
	/**
	 * 根据分类ID获得话题列表
	 */
	public String getSubjectListWithCat(int siteID,int catID,int page,Long userid);

	/**
	 * 获得单个话题
	 */
	public String getSubject(int siteID, int id);
	
	/**
	 * 对话题进行提问
	 */
	public boolean comitsQA(SubjectQAInfo sQAInfo);
	/**
	 * 最新问题列表
	 */
	public String getQuestionList(int siteID, int subjectID,int page);
	/**
	 * 问答详情
	 */
	public String getQuestionDetail(int siteID, int fileID);
	/**
	 * 热门问题列表
	 */
	public String getQuestionListHot(int siteID, int subjectID,int page);
	/**
	 * 我的提问列表
	 * @param page
	 */
	public String MyQuestion(int siteID, Long userID, int page);
	/**
	 * 我关注的话题
	 * @param siteID
	 * @param userid
	 * @return
	 */
	public String getmySubjectSubscribe(int siteID, Long userid, int page);
	public String getmySubject(int siteID, Long userid, int page);
	
}
