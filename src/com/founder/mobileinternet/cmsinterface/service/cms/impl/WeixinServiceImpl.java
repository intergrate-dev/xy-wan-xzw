package com.founder.mobileinternet.cmsinterface.service.cms.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.founder.mobileinternet.cmsinterface.pojo.Configure;
import com.founder.mobileinternet.cmsinterface.service.RedisManager;
import com.founder.mobileinternet.cmsinterface.service.cms.WeixinService;
import com.founder.mobileinternet.cmsinterface.util.CommonToolUtil;

@Service
public class WeixinServiceImpl implements WeixinService {

	private static final int CACHE_LENGTH = 200;
    private static final int COUNT = 20;
	
    @Autowired
    private RedisManager redisManager;
    @Autowired
    private Configure configure;
	
	@Override
	public String getWxAccounts(String data, int userID) {
		String url = configure.getNewAppServerUrl() + "/getWxAccounts.do?userID="+userID;
		String results = null;
		try {
			results = CommonToolUtil.getData(url,data);
		} catch (Exception e) {
			e.printStackTrace();
			results = CommonToolUtil.buildFailureArrayResult("请求内网接口失败");
		}

		return results;
	}

	@Override
	public String getWxGroupArticles(String data, int userID) {
		String url = configure.getNewAppServerUrl() + "/getWxGroupArticles.do?userID=" + userID;
        String results = null;
        try {
            results = CommonToolUtil.getData(url,data);
        } catch (Exception e) {
            e.printStackTrace();
        	results = CommonToolUtil.buildFailureArrayResult("请求内网接口失败");
        }

        return results;
	}

	@Override
	public String getWxGroupArticleDetail(String data, int userID) {
		String url = configure.getNewAppServerUrl() 
				+ "/getWxGroupArticleDetail.do?userID=" + userID;
        String results = null;
        try {
            results = CommonToolUtil.getData(url,data);
        } catch (Exception e) {
            e.printStackTrace();
        	results = CommonToolUtil.buildFailureObjectResult("请求内网接口失败");
        }

        return results;
	}
	@Override
	public String getWxGroupArticleDetailUPOrDown(String data, int userID) {
		String url = configure.getNewAppServerUrl()
				+ "/getWxGroupArticleDetailUPOrDown.do?userID=" + userID;
        String results = null;
        try {
            results = CommonToolUtil.getData(url,data);
        } catch (Exception e) {
            e.printStackTrace();
        	results = CommonToolUtil.buildFailureObjectResult("请求内网接口失败");
        }

        return results;
	}

	@Override
	public String transferGroup(int userID, String data) {
		String url = configure.getNewAppServerUrl() + "/transferGroup.do?userID=" + userID;
        String results = null;
        try {
            results = CommonToolUtil.getData(url,data);
        } catch (Exception e) {
            e.printStackTrace();
        	results = CommonToolUtil.buildFailureArrayResult("请求内网接口失败");
        }

        return results;
	}

	@Override
	public String rejectGroup(int userID, String data) {
		String url = configure.getNewAppServerUrl() + "/rejectGroup.do?userID=" + userID;
        String results = null;
        try {
            results = CommonToolUtil.getData(url,data);
        } catch (Exception e) {
            e.printStackTrace();
        	results = CommonToolUtil.buildFailureArrayResult("请求内网接口失败");
        }

        return results;
	}

	@Override
	public String transferGroupOne(int userID, String data) {
		String url = configure.getNewAppServerUrl() + "/transferGroupOne.do?userID=" + userID;
        String results = null;
        try {
            results = CommonToolUtil.getData(url,data);
        } catch (Exception e) {
            e.printStackTrace();
        	results = CommonToolUtil.buildFailureArrayResult("请求内网接口失败");
        }

        return results;
	}

	@Override
	public String rejectGroupOne(int userID, String data) {
		String url = configure.getNewAppServerUrl() + "/rejectGroupOne.do?userID=" + userID;
        String results = null;
        try {
            results = CommonToolUtil.getData(url,data);
        } catch (Exception e) {
            e.printStackTrace();
        	results = CommonToolUtil.buildFailureArrayResult("请求内网接口失败");
        }

        return results;
	}

}
