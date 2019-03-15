package com.founder.mobileinternet.cmsinterface.util;

import java.util.Map;

public class BrowserAdapter
{
	private Map<String, String> deviceMappingViewpage;

	public BrowserAdapter(Map<String, String> deviceMappingViewpage)
	{
		this.deviceMappingViewpage = deviceMappingViewpage;
	}
	
	public String getTemplateWidth(String userAgent)
	{
		if(userAgent != null && !userAgent.isEmpty())
		{
			String templateWidth = "";
			for (String key : deviceMappingViewpage.keySet())
			{
				if (userAgent.toLowerCase().contains(key.toLowerCase()))
				{
					templateWidth = deviceMappingViewpage.get(key);
				}
			}
			
			return (templateWidth.isEmpty() ? deviceMappingViewpage.get("default") : templateWidth);
		}
		return deviceMappingViewpage.get("default");
	}

	public String getRealViewPath(String userAgent, String viewName)
	{
		return getTemplateWidth(userAgent) + "/" + viewName;
	}
}
