package com.founder.mobileinternet.cmsinterface.util.aliyun;

import java.math.BigDecimal;

public class AliyunArticle {  
	
    private String cmd;
    
    private Integer articleId;
    
    private String title;
    
    private Integer siteId;
    
    private Integer nodeId;
    
    private Long expirationTime;
    
    private BigDecimal  displayorder;

    private Integer publishState;
    
    private String keyword;

	public String getCmd() {
		return cmd;
	}

	public void setCmd(String cmd) {
		this.cmd = cmd;
	}

	public Integer getArticleId() {
		return articleId;
	}

	public void setArticleId(Integer articleId) {
		this.articleId = articleId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getSiteId() {
		return siteId;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

	public Integer getNodeId() {
		return nodeId;
	}

	public void setNodeId(Integer nodeId) {
		this.nodeId = nodeId;
	}

	public Long getExpirationTime() {
		return expirationTime;
	}

	public void setExpirationTime(Long expirationTime) {
		this.expirationTime = expirationTime;
	}

	public BigDecimal  getDisplayorder() {
		return displayorder;
	}

	public void setDisplayorder(BigDecimal  displayorder) {
		this.displayorder = displayorder;
	}

	public Integer getPublishState() {
		return publishState;
	}

	public void setPublishState(Integer publishState) {
		this.publishState = publishState;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
    
//    private String content;

 
}  