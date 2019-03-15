package com.founder.mobileinternet.cmsinterface.upload;

public class ProgressInfo
{
	private long bytesRead;
	private long contentLength;
	private int items;
	private String result;
	private Long finishTime = 0L;

	public long getBytesRead()
	{
		return bytesRead;
	}

	public void setBytesRead(long bytesRead)
	{
		this.bytesRead = bytesRead;
	}

	public long getContentLength()
	{
		return contentLength;
	}

	public void setContentLength(long contentLength)
	{
		this.contentLength = contentLength;
	}

	public int getItems()
	{
		return items;
	}

	public void setItems(int items)
	{
		this.items = items;
	}

	public String getResult()
	{
		return result;
	}

	public void setResult(String result)
	{
		this.result = result;
	}

	public Long getFinishTime()
	{
		return finishTime;
	}

	public void setFinishTime(Long finishTime)
	{
		this.finishTime = finishTime;
	}
}
