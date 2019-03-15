package com.founder.mobileinternet.cmsinterface.service.cms.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.founder.mobileinternet.cmsinterface.pojo.Configure;
import com.founder.mobileinternet.cmsinterface.service.cms.EventService;
import com.founder.mobileinternet.cmsinterface.util.CommonToolUtil;

@Service
public class EventServiceImpl implements EventService {

	@Autowired
	private Configure configure;

	public String eventTypes() {
		String url = configure.getInnerApiUrl() + "/api/event/eventTypes.do";
		return CommonToolUtil.getData(url);
	};

	public String scoreRuleList(String source, String tc) {
		String url = configure.getInnerApiUrl() + "/api/event/scoreRuleList.do?source=" + source + "&tc=" + tc;
		return CommonToolUtil.getData(url);
	};

	public String convert(String source, String tc, String memberID, String info) {
		String url = configure.getInnerApiUrl() + "/api/event/convert.do?source=" + source + "&tc=" + tc + "&member="
				+ memberID + "&info=" + info.replaceAll("\\{", "%7B").replaceAll("\\}", "%7D").replaceAll("\"", "%22")
						.replaceAll("\\:", "%3A").replaceAll("\\,", "%2C");
		return CommonToolUtil.getData(url);
	};

	public String event1(String eType, String tc, String member,String time,String sign,String siteID) {
		String url = configure.getInnerApiUrl() + "/api/event/event1.do?eType=" + eType + "&tc=" + tc + "&member="
				+ member+"&time="+time+"&sign="+sign+"&siteID="+siteID;
		return CommonToolUtil.getData(url);
	};

	public String event(String info, String tc, String member,String time,String sign,String siteID) {
		String info1 = info.replaceAll("\\{", "%7B").replaceAll("\\}", "%7D").replaceAll("\"", "%22")
				.replaceAll("\\:", "%3A").replaceAll("\\,", "%2C").replaceAll("\\/", "%2F").replaceAll("\\ ", "%20");
		String url = configure.getInnerApiUrl() + "/api/event/event.do?info=" + info1 + "&tc=" + tc + "&member="
				+ member+"&time="+time+"&sign="+sign+"&siteID="+siteID;
		return CommonToolUtil.getData(url);
	};
}
