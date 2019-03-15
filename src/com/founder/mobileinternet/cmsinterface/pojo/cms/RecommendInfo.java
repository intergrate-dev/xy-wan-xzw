package com.founder.mobileinternet.cmsinterface.pojo.cms;


public class RecommendInfo {
	private String appid;//应用ID
	private String dev;//移动设备ID
	private String t;//事件发生时间
	private String uid;//用户id
	private String aid;//文章id
	private String bid;//推荐栏id
	private String cname;//栏目名称（层级表示，如财经/房产）
	private String separator;//栏目名称层级表示分隔符（如：/)
	private String rule;//推荐规则
	private String rule_view = "false";//是否返回推荐规则
	private String param_view = "false";//是否返回处理参数
	private String row;//需要返回的文章数量
	private String attrs;//需要返回的文章属性
	private String debug = "false";//是否为debug
	public String getAppid() {
		return appid;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}
	public String getDev() {
		return dev;
	}
	public void setDev(String dev) {
		this.dev = dev;
	}
	public String getT() {
		return t;
	}
	public void setT(String t) {
		this.t = t;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getAid() {
		return aid;
	}
	public void setAid(String aid) {
		this.aid = aid;
	}
	public String getBid() {
		return bid;
	}
	public void setBid(String bid) {
		this.bid = bid;
	}
	public String getCname() {
		return cname;
	}
	public void setCname(String cname) {
		this.cname = cname;
	}
	public String getSeparator() {
		return separator;
	}
	public void setSeparator(String separator) {
		this.separator = separator;
	}
	public String getRule() {
		return rule;
	}
	public void setRule(String rule) {
		this.rule = rule;
	}
	public String getRule_view() {
		return rule_view;
	}
	public void setRule_view(String rule_view) {
		this.rule_view = rule_view;
	}
	public String getParam_view() {
		return param_view;
	}
	public void setParam_view(String param_view) {
		this.param_view = param_view;
	}
	public String getRow() {
		return row;
	}
	public void setRow(String row) {
		this.row = row;
	}
	public String getAttrs() {
		return attrs;
	}
	public void setAttrs(String attrs) {
		this.attrs = attrs;
	}
	public String getDebug() {
		return debug;
	}
	public void setDebug(String debug) {
		this.debug = debug;
	}
	
	@Override
	public String toString() {
		StringBuffer buff = new StringBuffer();
		buff.append("appid=").append(appid);
		buff.append("&dev=").append(dev);
		buff.append("&t=").append(t);
		buff.append("&uid=").append(uid);
		buff.append("&aid=").append(aid);
		buff.append("&bid=").append(bid);
		buff.append("&cname=").append(cname);
		buff.append("&separator=").append(separator);
		buff.append("&rule=").append(rule);
		buff.append("&rule_view=").append(rule_view);
		buff.append("&param_view=").append(param_view);
		buff.append("&row=").append(row);
		buff.append("&attrs=").append(attrs);
		buff.append("&debug=").append(debug);
		return buff.toString();
	}
}
