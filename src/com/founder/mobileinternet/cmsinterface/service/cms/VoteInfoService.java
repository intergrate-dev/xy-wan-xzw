package com.founder.mobileinternet.cmsinterface.service.cms;

public interface VoteInfoService {
	public String getVoteInfo(String voteid);

	public String getVoteCounts(String voteid, String vote_optionid);

	public String clearVoteInfo();

	public String getVoteInfoTime(String voteid);

	public String getOptionInfo(String voteid, String vote_optionid);
}
