package com.founder.mobileinternet.cmsinterface.service.cms;

public interface OrdersService {
	public String create(String meal, String users, String pay, String operator, String total, String orderSource,
			String siteID, String taxpayer, String unitNum, String unitAddr);

	public String checkuser(String mobile, String siteID);

	public String getMsg(String orderNum, String docid);

	public String cancel(String orderNum);

	public String del(String orderNum, String docid);

	public String getMessages(String orderNum, String docid, String siteID);

	public String getMessagesPackage(String orderNum, String docid, String siteID);
	
	public String getPcNoMsg(String uid, String siteID);

	public String regain(String orderNum);

	public String pay(String orderNum, String docid, String mealName, String mealMoney);

	public String renew(String setmealid, String ssoid, String siteID);

	public String alipayapi(String WIDout_trade_no, String WIDsubject, String WIDtotal_fee, String WIDbody, String extra_common_param);

	public String alipayAppPay(String orderNum, String mealName, String mealMoney, String uid);

	public String alipayPc(String body, String buyer_email, String buyer_id, String exterface, String is_success,
			String notify_id, String out_trade_no, String payment_type, String trade_no, String trade_status,
			String extra_common_param);

	public String alipayApp( String out_trade_no);

}
