package com.founder.mobileinternet.cmsinterface.upload;

import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class UploadFileUniqidCache implements ServletContextListener
{
	public static final String UNIQID_MAP = "uniqid_map";
	private ConcurrentHashMap<String, ProgressInfo> map = new ConcurrentHashMap<String, ProgressInfo>();
	
	@Override
	public void contextDestroyed(ServletContextEvent event)
	{
		map.clear();
	}

	@Override
	public void contextInitialized(ServletContextEvent event)
	{
		event.getServletContext().setAttribute(UNIQID_MAP, map);
	}

	@SuppressWarnings("unchecked")
	public static ConcurrentHashMap<String, ProgressInfo> getMap(ServletContext context)
	{
		return (ConcurrentHashMap<String, ProgressInfo>)context.getAttribute(UNIQID_MAP);
	}
}
