package com.founder.mobileinternet.cmsinterface.service.cms;



public interface DuibaService {
    public String autologin(String uid, String redirect);
    public String creditConsume(String appKey, String timestamp, String uid, String credits,
			String description, String orderNum, String type, String facePrice, String actualPrice,
			String waitAudit,String ip,String params,String sign) throws Exception;
    public String creditNotify( String appKey, String timestamp, String success, String errorMessage,
			String bizId, String orderNum,String sign);
}
