package com.founder.mobileinternet.cmsinterface.service.cms;

import com.founder.mobileinternet.cmsinterface.pojo.cms.EventInfo;
import com.founder.mobileinternet.cmsinterface.pojo.cms.FeedInfo;
import com.founder.mobileinternet.cmsinterface.pojo.cms.TipoffInfo;

/**
 * 其它互动功能的接口
 */
public interface NisService {

	public boolean commitFeed(FeedInfo feedInfo);
	
	public boolean commitTipoff(TipoffInfo forumInfo);

	public String myTipoff(int userID, int page, int siteID);

	public String tipoffContent(int docID, int siteID);

	public boolean event(EventInfo paramEventInfo);
	String redDot(Integer userId);
	boolean read(int userId,int type);

	public String getMyFav(int userID, int page, int siteID);

	public boolean hasFav(int userID, long articleID, int type, int siteID);
	
	/**
	 * 读互动计数
	 * @param id 对象ID
	 * @param source 对象类型 0稿件，1直播，3数字报稿件，4问吧问答，5问答，6活动
	 */
	String getCounts(long id, int source, int siteID);


}
