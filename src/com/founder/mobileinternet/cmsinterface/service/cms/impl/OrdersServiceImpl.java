package com.founder.mobileinternet.cmsinterface.service.cms.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.founder.mobileinternet.cmsinterface.pojo.Configure;
import com.founder.mobileinternet.cmsinterface.service.cms.OrdersService;
import com.founder.mobileinternet.cmsinterface.util.CommonToolUtil;

@Service
public class OrdersServiceImpl implements OrdersService {
	@Autowired
	private Configure configure;

	public String create(String meal, String users, String pay, String operator, String total, String orderSource,
			String siteID, String taxpayer, String unitNum, String unitAddr) {
		String meal1 = meal.replaceAll("\\{", "%7B").replaceAll("\\}", "%7D").replaceAll("\"", "%22")
				.replaceAll("\\:", "%3A").replaceAll("\\,", "%2C");
		String users1 = users.replaceAll("\\{", "%7B").replaceAll("\\}", "%7D").replaceAll("\"", "%22")
				.replaceAll("\\:", "%3A").replaceAll("\\,", "%2C").replaceAll("\\[", "%5B").replaceAll("\\]", "%5D");
		String url = configure.getInnerApiUrl() + "/api/order/create.do?meal=" + meal1 + "&users=" + users1 + "&pay="
				+ pay + "&operator=" + operator + "&total=" + total + "&orderSource=" + orderSource + "&siteID="
				+ siteID + "&taxpayer=" + taxpayer + "&unitNum=" + unitNum + "&unitAddr=" + unitAddr;
		url.replaceAll(" ", "%20");
		return CommonToolUtil.getData(url);
	};

	public String checkuser(String mobile, String siteID) {
		String url = configure.getInnerApiUrl() + "/api/order/checkuser.do?mobile=" + mobile + "&siteID=" + siteID;
		return CommonToolUtil.getData(url);
	};

	public String getMsg(String orderNum, String docid) {
		String url = configure.getInnerApiUrl() + "/api/order/getMsg.do?orderNum=" + orderNum + "&docid=" + docid;
		return CommonToolUtil.getData(url);
	};

	public String cancel(String orderNum) {
		String url = configure.getInnerApiUrl() + "/api/order/cancel.do?orderNum=" + orderNum;
		return CommonToolUtil.getData(url);
	};

	public String del(String orderNum, String docid) {
		String url = configure.getInnerApiUrl() + "/api/order/del.do?orderNum=" + orderNum + "&docid=" + docid;
		return CommonToolUtil.getData(url);
	};

	public String getMessages(String orderNum, String uid, String siteID) {
		String url = configure.getInnerApiUrl() + "/api/order/getMessages.do?orderNum=" + orderNum + "&uid=" + uid
				+ "&siteID=" + siteID;
		return CommonToolUtil.getData(url);
	};

	public String getPcNoMsg(String uid, String siteID) {
		String url = configure.getInnerApiUrl() + "/api/order/getPcNoMsg.do?uid="
				+ uid + "&siteID=" + siteID;
		return CommonToolUtil.getData(url);
	};
	
	public String getMessagesPackage(String orderNum, String uid, String siteID) {
		String url = configure.getInnerApiUrl() + "/api/order/getMessagesPackage.do?orderNum=" + orderNum + "&uid="
				+ uid + "&siteID=" + siteID;
		return CommonToolUtil.getData(url);
	};

	public String regain(String orderNum) {
		String url = configure.getInnerApiUrl() + "/api/order/regain.do?orderNum=" + orderNum ;
		return CommonToolUtil.getData(url);
	};

	public String pay(String orderNum, String docid, String mealName, String mealMoney) {
		String url = configure.getInnerApiUrl() + "/api/order/pay.do?orderNum=" + orderNum + "&docid=" + docid
				+ "&mealName=" + mealName + "&mealMoney=" + mealMoney;
		return CommonToolUtil.getData(url);
	}

	public String renew(String setmealid, String ssoid, String siteID) {
		String url = configure.getInnerApiUrl() + "/api/order/renew.do?setmealid=" + setmealid + "&ssoid=" + ssoid + "&siteID=" + siteID;
		return CommonToolUtil.getData(url);
	}

	@Override
	public String alipayapi(String WIDout_trade_no, String WIDsubject, String WIDtotal_fee, String WIDbody,
			String extra_common_param) {
		String WIDout_trade_no1 = WIDout_trade_no.replaceAll("\\{", "%7B").replaceAll("\\}", "%7D")
				.replaceAll("\"", "%22").replaceAll("\\:", "%3A").replaceAll("\\,", "%2C").replaceAll("\\[", "%5B")
				.replaceAll("\\]", "%5D");
		String WIDsubject1 = WIDsubject.replaceAll("\\{", "%7B").replaceAll("\\}", "%7D").replaceAll("\"", "%22")
				.replaceAll("\\:", "%3A").replaceAll("\\,", "%2C").replaceAll("\\[", "%5B").replaceAll("\\]", "%5D");
		String WIDtotal_fee1 = WIDtotal_fee.replaceAll("\\{", "%7B").replaceAll("\\}", "%7D").replaceAll("\"", "%22")
				.replaceAll("\\:", "%3A").replaceAll("\\,", "%2C").replaceAll("\\[", "%5B").replaceAll("\\]", "%5D");
		String WIDbody1 = WIDbody.replaceAll("\\{", "%7B").replaceAll("\\}", "%7D").replaceAll("\"", "%22")
				.replaceAll("\\:", "%3A").replaceAll("\\,", "%2C").replaceAll("\\[", "%5B").replaceAll("\\]", "%5D");
		String url = configure.getInnerApiUrl() + "/api/order/alipayapi.do?WIDout_trade_no=" + WIDout_trade_no1
				+ "&WIDsubject=" + WIDsubject1 + "&WIDtotal_fee=" + WIDtotal_fee1 + "&WIDbody=" + WIDbody1 + "&extra_common_param="
				+ extra_common_param;
		return CommonToolUtil.getData(url);
	}

	@Override
	public String alipayAppPay(String orderNum, String mealName, String mealMoney, String uid) {
		String mealName1 = mealName.replaceAll("\\{", "%7B").replaceAll("\\}", "%7D").replaceAll("\"", "%22")
				.replaceAll("\\:", "%3A").replaceAll("\\,", "%2C").replaceAll("\\[", "%5B").replaceAll("\\]", "%5D");
		String url = configure.getInnerApiUrl() + "/api/order/alipayAppPay.do?orderNum=" + orderNum + "&mealName="
				+ mealName1 + "&mealMoney=" + mealMoney + "&uid=" + uid;
		return CommonToolUtil.getData(url);
	}

	@Override
	public String alipayPc(String body, String buyer_email, String buyer_id, String exterface, String is_success,
			String notify_id, String out_trade_no, String payment_type, String trade_no, String trade_status,
			String extra_common_param) {
		String body1 = body.replaceAll("\\{", "%7B").replaceAll("\\}", "%7D").replaceAll("\"", "%22")
				.replaceAll("\\:", "%3A").replaceAll("\\,", "%2C").replaceAll("\\[", "%5B").replaceAll("\\]", "%5D");
		String buyer_email1 = buyer_email.replaceAll("\\{", "%7B").replaceAll("\\}", "%7D").replaceAll("\"", "%22")
				.replaceAll("\\:", "%3A").replaceAll("\\,", "%2C").replaceAll("\\[", "%5B").replaceAll("\\]", "%5D");
		String exterface1 = exterface.replaceAll("\\{", "%7B").replaceAll("\\}", "%7D").replaceAll("\"", "%22")
				.replaceAll("\\:", "%3A").replaceAll("\\,", "%2C").replaceAll("\\[", "%5B").replaceAll("\\]", "%5D");
		String out_trade_no1 = out_trade_no.replaceAll("\\{", "%7B").replaceAll("\\}", "%7D").replaceAll("\"", "%22")
				.replaceAll("\\:", "%3A").replaceAll("\\,", "%2C").replaceAll("\\[", "%5B").replaceAll("\\]", "%5D");
		String payment_type1 = payment_type.replaceAll("\\{", "%7B").replaceAll("\\}", "%7D").replaceAll("\"", "%22")
				.replaceAll("\\:", "%3A").replaceAll("\\,", "%2C").replaceAll("\\[", "%5B").replaceAll("\\]", "%5D");
		String trade_no1 = trade_no.replaceAll("\\{", "%7B").replaceAll("\\}", "%7D").replaceAll("\"", "%22")
				.replaceAll("\\:", "%3A").replaceAll("\\,", "%2C").replaceAll("\\[", "%5B").replaceAll("\\]", "%5D");
		String trade_status1 = trade_status.replaceAll("\\{", "%7B").replaceAll("\\}", "%7D").replaceAll("\"", "%22")
				.replaceAll("\\:", "%3A").replaceAll("\\,", "%2C").replaceAll("\\[", "%5B").replaceAll("\\]", "%5D");
		String extra_common_param1 = extra_common_param.replaceAll("\\{", "%7B").replaceAll("\\}", "%7D")
				.replaceAll("\"", "%22").replaceAll("\\:", "%3A").replaceAll("\\,", "%2C").replaceAll("\\[", "%5B")
				.replaceAll("\\]", "%5D");
		String url = configure.getInnerApiUrl() + "/api/order/alipayPc.do?body=" + body1 + "&buyer_email="
				+ buyer_email1 + "&buyer_id=" + buyer_id + "&is_success=" + is_success + "&notify_id=" + notify_id
				+ "&out_trade_no=" + out_trade_no1 + "&trade_status=" + trade_status1 + "&extra_common_param="
				+ extra_common_param1 + "&exterface=" + exterface1 + "&payment_type=" + payment_type1 + "&trade_no="
				+ trade_no1;
		return CommonToolUtil.getData(url);
	}

	@Override
	public String alipayApp(String out_trade_no) {
		String url = configure.getInnerApiUrl() + "/api/order/alipayApp.do?out_trade_no=" + out_trade_no;
		return CommonToolUtil.getData(url);
	}
}
