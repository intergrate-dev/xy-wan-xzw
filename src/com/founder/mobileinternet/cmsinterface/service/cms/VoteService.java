package com.founder.mobileinternet.cmsinterface.service.cms;

import com.founder.mobileinternet.cmsinterface.pojo.cms.VoteInfo;

public interface VoteService {

	public String commitVote(VoteInfo voteInfo);

	public String voteCount(int id, int siteID);

	public String voteResult(int id, int siteID);

}
