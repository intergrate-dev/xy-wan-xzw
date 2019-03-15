package com.founder.mobileinternet.cmsinterface.upload;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ProgressInfoServlet extends HttpServlet
{
	private static final long serialVersionUID = -1133857405377686271L;

	/**
	 * The doGet method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		System.out.println("111111111111111111111111111111");
		this.doPost(request, response);
	}

	/**
	 * The doPost method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to
	 * post.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		response.reset();
		response.setHeader("Cache-Control", "no-cache"); // HTTP 1.1
		response.setHeader("Pragma", "no-cache"); // HTTP 1.0
		response.setDateHeader("Expires", 0); // prevents caching at the proxy
												// server
		response.setContentType("plain/html");
		PrintWriter out = response.getWriter();

		if (request.getParameter("uniqid") != null)
		{
			if (request.getParameter("js") != null)
			{
				// js模式
				ConcurrentHashMap<String, ProgressInfo> map = UploadFileUniqidCache.getMap(getServletContext());
				ProgressInfo progressInfo = map.get(request.getParameter("uniqid"));

				if (progressInfo != null)
				{
					StringBuilder sb = new StringBuilder();
					sb.append("var info=");
					sb.append("{");
					sb.append("\"bytesRead\":");
					sb.append(progressInfo.getBytesRead());
					sb.append(",\"contentLength\":");
					sb.append(progressInfo.getContentLength());
					sb.append(",\"items\":");
					sb.append(progressInfo.getItems());
					sb.append(",\"result\":");

					String result = "null";
					if (progressInfo.getContentLength() > 0 && progressInfo.getBytesRead() == progressInfo.getContentLength())
					{
						if (progressInfo.getResult() != null)
						{
							result = progressInfo.getResult();
						}
					}
					sb.append(result);
					sb.append("}");

					out.print(sb.toString());

					System.out.println(progressInfo.getBytesRead());
					System.out.println(progressInfo.getContentLength());
					System.out.println(result);
					if (progressInfo.getBytesRead() == progressInfo.getContentLength() && !result.equalsIgnoreCase("null"))
					{
						map.remove(request.getParameter("uniqid"));
					}
				} else
				{
					out.print("{\"contentLength\":-1}");
				}
			} else
			{
				// 非js模式
				ConcurrentHashMap<String, ProgressInfo> map = UploadFileUniqidCache.getMap(getServletContext());
				ProgressInfo progressInfo = map.get(request.getParameter("uniqid"));

				if (progressInfo != null)
				{
					StringBuilder sb = new StringBuilder();
					sb.append("{");
					sb.append("\"bytesRead\":");
					sb.append(progressInfo.getBytesRead());
					sb.append(",\"contentLength\":");
					sb.append(progressInfo.getContentLength());
					sb.append(",\"items\":");
					sb.append(progressInfo.getItems());
					sb.append("}");

					out.print(sb.toString());

					if (progressInfo.getBytesRead() == progressInfo.getContentLength())
					{
						map.remove(request.getParameter("uniqid"));
					}
				} else
				{
					out.print("{\"contentLength\":-1}");
				}
			}
		} else
		{
			out.print("invalid request");
		}

		out.flush();
		out.close();
	}
}
