package com.founder.mobileinternet.cmsinterface.util;

import com.founder.mobileinternet.cmsinterface.pojo.Configure;
import net.sf.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import redis.clients.jedis.HostAndPort;

import java.net.URI;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

@SuppressWarnings("rawtypes")
public class ParamConfigInjectSupport implements ApplicationListener<ContextRefreshedEvent>{
	private final Logger log = LoggerFactory.getLogger(ParamConfigInjectSupport.class);

	private String innerApiUrl;
	
	public void init(){
		String innerUrl = System.getenv("INNER_API_URL");
		if(innerUrl != null && !"".equals(innerUrl)) innerApiUrl = innerUrl;
		String strEntity = "";
		HttpParams param = new BasicHttpParams();
		ThreadSafeClientConnManager cm = new ThreadSafeClientConnManager();
		cm.setDefaultMaxPerRoute(500);
		cm.setMaxTotal(500);
		HttpClient httpClient = new DefaultHttpClient(cm, param);
		httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);
		httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 10000);
		HttpPost httpPost = new HttpPost();
		int rtCode = 0;
		try {
			httpPost.setURI(new URI(innerApiUrl + "/api/param/paramConfig.do"));
			HttpResponse response = httpClient.execute(httpPost);
			rtCode = response.getStatusLine().getStatusCode();
			if (200 == rtCode) {
				HttpEntity responseEntity = response.getEntity();
				if (responseEntity != null) {
					strEntity = EntityUtils.toString(responseEntity, "UTF-8");
					if(strEntity != null && !"".equals(strEntity)){
						JSONObject obj = JSONObject.fromObject(strEntity);
						Iterator iterator = obj.keys();
						while(iterator.hasNext()){
							String key = (String) iterator.next();
							String value = obj.getString(key);
							System.setProperty(key, value);
						}
						print(obj);
					}
				}else{
					log.info("");
				}
			}
		} catch (Exception e1) {
			log.error("\r\n\r\n请检查翔宇主程序或者内网API参数：");
			log.error("innerApiUrl = " + innerApiUrl);
			log.error("result = " + strEntity);
			log.error(rtCode+"\r\n\r\n");
		} finally {
			httpPost.abort();
			httpClient.getConnectionManager().shutdown();
			HttpClientUtils.closeQuietly(httpClient);
		}
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		ApplicationContext context = event.getApplicationContext();
		if(context.getParent() == null){
			context.getBean(Configure.class).setInnerApiUrl(innerApiUrl);
			printEnv();
			printUrl(context.getBeansOfType(HostAndPort.class));
		}
	}
	
	private void print(JSONObject obj){
		log.info("------------------数据库参数变量信息----------------------");
		Iterator iterator = obj.keys();
		while(iterator.hasNext()){
			String key = (String) iterator.next();
			String value = obj.getString(key);
			log.info("key = " + key + ", value = " + value);
		}
		log.info("------------------------------------------------------");
	}
	
	private void printEnv(){
		log.info("---------------------系统变量信息----------------------");
		Properties properties = System.getProperties();
		for(Map.Entry<Object, Object> entry:properties.entrySet()){
			if(entry.getKey().toString().contains("_ADDR")
					|| entry.getKey().toString().equals("WEBSITE_URL")){
				log.info("key = "+entry.getKey()+", value = "+entry.getValue());
			}
		}
		Map<String, String> map = System.getenv();
		log.info("---------------------环境变量信息-----------------------");
		for(Entry<String, String> entry:map.entrySet()){
			if(entry.getKey().toString().contains("_ADDR")
					|| entry.getKey().toString().equals("WEBSITE_URL")){
				log.info("key = "+entry.getKey()+", value = "+entry.getValue());
			}
		}
		log.info("-----------------------------------------------------");
	}
	
	private void printUrl(Map<?,?> map){
		log.info("------------------Redis连接信息--------------");
		log.info("Redis节点信息 : ");
		for (Map.Entry<?, ?> entry : map.entrySet()) {
			log.info("key = "+entry.getKey()+", value = "+entry.getValue());
		}
		log.info("-----------------------------------------------------\r\n\r\n");
	}
	
	public void setInnerApiUrl(String innerApiUrl) {
		this.innerApiUrl = innerApiUrl;
	}
}
