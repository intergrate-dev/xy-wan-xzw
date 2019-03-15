package com.founder.mobileinternet.cmsinterface.upload;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.ProgressListener;

public class UploadProgressWithSessionListener implements ProgressListener
{
	public static final String PROGRESS_INFO_SESSION_NAME = "progressInfo";
	private HttpSession session;

	public UploadProgressWithSessionListener(HttpServletRequest request)
	{
		session = request.getSession();
		ProgressInfo progressInfo = new ProgressInfo();
		session.setAttribute(PROGRESS_INFO_SESSION_NAME, progressInfo);
	}

	@Override
	public void update(long pBytesRead, long pContentLength, int pItems)
	{
		ProgressInfo progressInfo = (ProgressInfo) session.getAttribute(PROGRESS_INFO_SESSION_NAME);
		if (progressInfo != null)
		{
			progressInfo.setBytesRead(pBytesRead);
			progressInfo.setContentLength(pContentLength);
			progressInfo.setItems(pItems);
		}
	}

}
