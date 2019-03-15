package com.founder.mobileinternet.cmsinterface.service.cms;

public interface PayService {
	public String createpayrecordpc(String payType, String payID, String payNumber, String payMoney, String payChannel,
			String payTime, String payOrder);
	public String createpayrecordapp(String payType, String payID, String payNumber, String payMoney, String payChannel,
			String payTime, String payOrder);
	public String getpaylog(String orderNum);
}
