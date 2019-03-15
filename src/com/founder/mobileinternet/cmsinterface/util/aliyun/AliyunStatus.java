package com.founder.mobileinternet.cmsinterface.util.aliyun;

/**
 * 稿件状态枚举
 * @author chenHa
 */
public enum AliyunStatus
{
	add("新建","add"),update("修改","update"),delete("删除","delete");
	
	private String name;
	
	private String value;

	private AliyunStatus(String name, String value)
	{
		this.name = name;
		this.value = value;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getValue()
	{
		return value;
	}

	public void setValue(String value)
	{
		this.value = value;
	}
	
}
