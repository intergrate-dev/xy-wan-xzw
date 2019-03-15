package com.founder.mobileinternet.cmsinterface.upload;

import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;

public class CleanTmpGUID extends Thread
{
	private ConcurrentHashMap<String, ProgressInfo> map;
	private final int cleanTimeInterval = 5*60*1000;
	private final int sleepTimeInterval = 5*60*1000;

	@Override
	public void run()
	{
		while (true)
		{
			System.out.println(System.currentTimeMillis() + "->clean....");
			Enumeration<String> keys = map.keys();
			while(keys.hasMoreElements())
			{
				String key = keys.nextElement();
				ProgressInfo progressInfo = map.get(key);
				if(progressInfo.getFinishTime() != 0 && System.currentTimeMillis()-progressInfo.getFinishTime() > cleanTimeInterval)
				{
					map.remove(key);
					System.out.println("remove key=" + key);
				}
			}
			
			try
			{
				sleep(sleepTimeInterval);
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}

	public CleanTmpGUID(ConcurrentHashMap<String, ProgressInfo> map)
	{
		this.map = map;
	}
}
