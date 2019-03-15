package com.founder.mobileinternet.cmsinterface.util;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;

import net.sf.json.JSONObject;

import com.founder.mobileinternet.cmsinterface.service.RedisManager;

public class WXUtil {


	public static String sha1(String s) {
		try {
			// Create SHA1 Hash
			MessageDigest digest = java.security.MessageDigest
					.getInstance("SHA-1");
			digest.update(s.getBytes(Charset.forName("utf-8")));
			byte messageDigest[] = digest.digest();

			return toHexString(messageDigest);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return "";
	}

	// String to byte
	private static String toHexString(byte[] b) { 
		StringBuilder sb = new StringBuilder(b.length * 2);
		for (int i = 0; i < b.length; i++) {
			sb.append(Character.forDigit((b[i] & 240) >> 4, 16));
			sb.append(Character.forDigit(b[i] & 15, 16));
		}
		return sb.toString();
	}
	
	/**
	 * 实现微信jsapi_ticket签名
	 * @param jsapi_ticket
	 * @param noncestr
	 * @param timestamp
	 * @param url
	 * @return
	 */
	public static JSONObject getSignature(String appid, String jsapi_ticket, String noncestr, String timestamp, String url){
		String signature = sha1("jsapi_ticket="+jsapi_ticket+"&noncestr="+noncestr+"&timestamp="+timestamp+"&url="+url);
		JSONObject result = new JSONObject();
		result.put("appid", appid);
		result.put("noncestr", noncestr);
		result.put("timestamp", timestamp);
		result.put("signature", signature);
		return result;
	}
	
	/**
	 * 生成时间戳
	 * @return
	 */
	public static String createTimpstamp(){
		return Long.toString(System.currentTimeMillis() / 1000);
	}
	
	/**
	 * 生成随机字符串
	 * @return
	 */
	public static String createNoncestr(){
		return UUID.randomUUID().toString();
	}
	
}
