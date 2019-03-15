package com.founder.mobileinternet.cmsinterface.upload;

import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.fileupload.ProgressListener;

public class UploadProgressListener implements ProgressListener
{
	private ConcurrentHashMap<String, ProgressInfo> map;
	private String uniqid;

	public UploadProgressListener(ConcurrentHashMap<String, ProgressInfo> map, String uniqid)
	{
		this.uniqid = uniqid;
		this.map = map;
		ProgressInfo progressInfo = new ProgressInfo();
		map.put(uniqid, progressInfo);
	}

	@Override
	public void update(long pBytesRead, long pContentLength, int pItems)
	{
		ProgressInfo progressInfo = map.get(uniqid);
		if (progressInfo != null)
		{
			//System.out.println("update = " + pBytesRead);
			progressInfo.setBytesRead(pBytesRead);
			progressInfo.setContentLength(pContentLength);
			progressInfo.setItems(pItems);
			if(pBytesRead == pContentLength)
			{
				progressInfo.setFinishTime(System.currentTimeMillis());
			}
		}
	}

}
