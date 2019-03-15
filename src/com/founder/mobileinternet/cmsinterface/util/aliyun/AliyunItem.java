package com.founder.mobileinternet.cmsinterface.util.aliyun;
public class AliyunItem {  
	//定义该文档的操作行为，可以为“add”、“update”、“delete”
    private String cmd;
  
    // 摘要数据存储数组  
    private Object fields;

	public String getCmd() {
		return cmd;
	}

	public void setCmd(String cmd) {
		this.cmd = cmd;
	}

	public Object getFields() {
		return fields;
	}

	public void setFields(Object fields) {
		this.fields = fields;
	}  
 
}  