package com.founder.mobileinternet.cmsinterface.service.cms;

import org.springframework.stereotype.Service;

import com.founder.mobileinternet.cmsinterface.pojo.cms.RecommendInfo;

@Service
public interface RecommendService {
	public String getRecommendList(RecommendInfo recommendInfo,int start,int count,int siteID);
	public String getHistoryRecList(RecommendInfo recommendInfo,int start,int count,int siteID);
	public String getRecKeywords(RecommendInfo recommendInfo);
	public String revise(String info,String columnId,int start);

	public String saveEmailSubscribe(int siteID, String name, String email);
}
