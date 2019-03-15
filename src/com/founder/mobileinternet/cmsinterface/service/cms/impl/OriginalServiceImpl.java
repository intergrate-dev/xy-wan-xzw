package com.founder.mobileinternet.cmsinterface.service.cms.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.founder.mobileinternet.cmsinterface.pojo.Configure;
import com.founder.mobileinternet.cmsinterface.service.RedisManager;
import com.founder.mobileinternet.cmsinterface.service.cms.OriginalService;
import com.founder.mobileinternet.cmsinterface.util.CommonToolUtil;

@Service
public class OriginalServiceImpl implements OriginalService {

	private static final int CACHE_LENGTH = 200;
    private static final int COUNT = 20;
	
    @Autowired
    private RedisManager redisManager;
    @Autowired
    private Configure configure;
    
	@Override
	public String getOriAuditCats(String data, int userID) {
		String url = configure.getNewAppServerUrl() + "/getOriAuditCats.do?userID="+userID;
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
	public String getOriginalArticles(String data, int userID) {
		String url = configure.getNewAppServerUrl() + "getOriginalArticles.do?userID=" + userID;
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
	public String getOriginalDetail(String data, int userID) {
		String url = configure.getNewAppServerUrl() + "/getOriginalDetail.do?userID=" + userID;
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
    public String getDocId(String data, int userID) {
        String url = configure.getNewAppServerUrl() + "/getDocId.do?userID=" + userID;
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
	public String transferOriginal(int userID, String data) {
		String url = configure.getNewAppServerUrl() + "/transferOriginal.do?userID=" + userID;
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
	public String rejectOriginal(int userID, String data) {
		String url = configure.getNewAppServerUrl() + "rejectOriginal.do?userID=" + userID;
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
	public String rejectOriginalFir(int userID, String data) {
		String url = configure.getNewAppServerUrl() + "rejectOriginalFir.do?userID=" + userID;
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
