package com.founder.mobileinternet.cmsinterface.listener;

import java.io.File;
import java.net.URI;

import net.sf.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import com.founder.mobileinternet.cmsinterface.pojo.Configure;
import com.founder.mobileinternet.cmsinterface.util.CommonToolUtil;

public class HttpPostTool {

	private static int SocketPort = -1;
	private static boolean isSocketStart = false;
	private static ThreadSafeClientConnManager cm = null;
	public static String postStream(String url, File file) throws Exception {
	String strEntity = null;
	HttpParams param = new BasicHttpParams();
	initPool();
	HttpClient httpClient = new DefaultHttpClient(cm, param);

	httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 30000);
	httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 30000);
	HttpPost httpPost = new HttpPost();
	MultipartEntity reqEntity = new MultipartEntity();  
	FileBody bin = new FileBody(file);  
	reqEntity.addPart("file", bin);//file为请求后台的File upload;属性      
	httpPost.setEntity(reqEntity);
    
	try {
		httpPost.setURI(new URI(url));
		HttpResponse response = httpClient.execute(httpPost);
		int rtCode = response.getStatusLine().getStatusCode();
		if (200 == rtCode) {
			HttpEntity responseEntity = response.getEntity();
			if (responseEntity != null)
				strEntity = EntityUtils.toString(responseEntity, "UTF-8");
		} else {
			closeRequest(httpClient, httpPost);
		}
	} catch (Exception e1) {
		//e1.printStackTrace();
		closeRequest(httpClient, httpPost);
	} finally {
	}

	return strEntity;
	}
	
	private static synchronized void initPool() {
		if (cm == null) {
			cm = new ThreadSafeClientConnManager();
			cm.setDefaultMaxPerRoute(500);
			cm.setMaxTotal(500);
		}
	}
	
	private static void closeRequest(HttpClient httpClient, HttpPost httpPost) {
		httpPost.abort();
		httpClient.getConnectionManager().shutdown();
		HttpClientUtils.closeQuietly(httpClient);
		cm = null;
	}
	
	public static int getSocketPort(){
		return SocketPort;
	}
	
	public static boolean isSocketStart() {
		return isSocketStart;
	}
	
	public static void initVideoSocketParams(Configure configure) {
		try {
			String url = configure.getNewAppServerUrl();
			if (!url.endsWith("/")) url +="/";
			url += "getVideoSocketParams.do";
			String data = CommonToolUtil.getData(url);
			JSONObject jsonObject = JSONObject.fromObject(data);
			SocketPort = jsonObject.getInt("socketPort");
			isSocketStart = jsonObject.getBoolean("isSocketStart");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
