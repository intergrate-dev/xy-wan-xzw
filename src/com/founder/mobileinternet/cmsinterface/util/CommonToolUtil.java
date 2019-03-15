package com.founder.mobileinternet.cmsinterface.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URI;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Cookie;

import com.founder.mobileinternet.cmsinterface.service.RedisKey;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import com.founder.mobileinternet.cmsinterface.pojo.Configure;
import com.founder.mobileinternet.cmsinterface.service.RedisManager;
import com.mchange.v1.io.InputStreamUtils;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.io.UnsupportedEncodingException;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class CommonToolUtil {

	private static ThreadSafeClientConnManager cm = null;
	public static final int ALIST_COUNT = 20; // 稿件列表一次获取的数量

	public static boolean postData(String url, String data) throws Exception {
		return Boolean.parseBoolean(postDataWithResult(url, data));
	}

	public static String postDataWithResult(String url, String data) throws Exception {
		String strEntity = null;
		HttpParams param = new BasicHttpParams();
		initPool();
		HttpClient httpClient = new DefaultHttpClient(cm, param);

		httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);
		httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 10000);
		List<NameValuePair> params = new ArrayList<>();
		params.add(new BasicNameValuePair("data", data));

		HttpPost httpPost = new HttpPost();
		httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
		try {
			httpPost.setURI(new URI(url));
			HttpResponse response = httpClient.execute(httpPost);
			int rtCode = response.getStatusLine().getStatusCode();
			//System.out.println("http postData返回值="+rtCode);
			if (200 == rtCode) {
				HttpEntity responseEntity = response.getEntity();
				if (responseEntity != null)
					strEntity = EntityUtils.toString(responseEntity, "UTF-8");
			} else {
				closeRequest(httpClient, httpPost);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			closeRequest(httpClient, httpPost);
		} finally {
		}

		return strEntity;
		//System.out.println(strEntity);
		/*boolean canGetData = false;
		try {
			canGetData = Boolean.parseBoolean(strEntity);
		} catch (Exception e) {
			canGetData = false;
			closeRequest(httpClient, httpPost);
		}
		return canGetData;*/
	}

	public static boolean canGetData(String url) {
		String strEntity = getData(url);
		boolean canGetData = false;
		try {
			canGetData = Boolean.parseBoolean(strEntity);
		} catch (Exception e) {
			canGetData = false;
		}
		return canGetData;
	}

	public static String getData(String url) {
		String strEntity = null;
		HttpParams param = new BasicHttpParams();
		initPool();

		HttpClient httpClient = new DefaultHttpClient(cm, param);
		httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);
		httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 20000);
		HttpPost httpPost = new HttpPost();
		int rtCode = 0;
		try {
			httpPost.setURI(new URI(url));
			HttpResponse response = httpClient.execute(httpPost);
			rtCode = response.getStatusLine().getStatusCode();
			//System.out.println("http getData返回值="+rtCode);
			if (200 == rtCode) {
				HttpEntity responseEntity = response.getEntity();
				if (responseEntity != null)
					strEntity = EntityUtils.toString(responseEntity, "UTF-8");
			} else {
				closeRequest(httpClient, httpPost);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			closeRequest(httpClient, httpPost);
		} finally {
		}
		if (null == strEntity) {
			closeRequest(httpClient, httpPost);
			strEntity = "" + rtCode;
		}
		return strEntity;
	}

	public static String getData(String url, String data) throws Exception {
		String strEntity = null;
		HttpParams param = new BasicHttpParams();
		initPool();
		HttpClient httpClient = new DefaultHttpClient(cm, param);

		httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);
		httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 10000);
		List<NameValuePair> params = new ArrayList<>();
		params.add(new BasicNameValuePair("data", data));

		HttpPost httpPost = new HttpPost();
		httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
		try {
			httpPost.setURI(new URI(url));
			HttpResponse response = httpClient.execute(httpPost);
			int rtCode = response.getStatusLine().getStatusCode();
			//System.out.println("http postData返回值="+rtCode);
			if (200 == rtCode) {
				HttpEntity responseEntity = response.getEntity();
				if (responseEntity != null)
					strEntity = EntityUtils.toString(responseEntity, "UTF-8");
			} else {
				closeRequest(httpClient, httpPost);
			}
		} catch (Exception e) {
			e.printStackTrace();
			closeRequest(httpClient, httpPost);
			throw new RuntimeException("请求异常:error_3");
		} finally {
		}
		if (null == strEntity) {
			closeRequest(httpClient, httpPost);
			strEntity = "";
		}
		return strEntity;
	}

	public static String getBeanData(String url, Object bean) {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return getData(url, objectMapper.writeValueAsString(bean));
		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw new RuntimeException("请求异常:error_4");
		}
	}

	private static void closeRequest(HttpClient httpClient, HttpPost httpPost) {
		httpPost.abort();
		httpClient.getConnectionManager().shutdown();
		HttpClientUtils.closeQuietly(httpClient);
		cm = null;
	}

	private static void closeRequest(HttpClient httpClient, HttpGet httpGet) {
		httpGet.abort();
		httpClient.getConnectionManager().shutdown();
		HttpClientUtils.closeQuietly(httpClient);
		cm = null;
	}

	public static boolean isBlank(String str) {
		return null == str || "".equals(str);
	}

	public static int getIntValue(String str, int init) {
		if (isBlank(str)) return init;
		return Integer.parseInt(str);
	}

	public static long getLongValue(String str) {
		if (isBlank(str)) return 0;
		return Long.parseLong(str);
	}

	// 去除翻页时可能存在的重复数据
	public static String listFilter(
			String value, int lastFileId,
			String name, String id) {

		JSONArray _jsonArr = new JSONArray();
		if (null != name) {
			JSONObject jsonObj = JSONObject.fromObject(value);
			JSONArray jsonArr = jsonObj.getJSONArray(name);
			int size = jsonArr.size();
			for (int i = 0; i < size; i++) {
				JSONObject _jsonObj = (JSONObject) jsonArr.get(i);
				_jsonArr.add(_jsonObj);
				if (lastFileId == _jsonObj.getInt(id)) _jsonArr.clear();
			}
			jsonObj.put(name, _jsonArr);
			return jsonObj.toString();
		} else {
			JSONArray jsonArr = JSONArray.fromObject(value);
			int size = jsonArr.size();
			for (int i = 0; i < size; i++) {
				JSONObject _jsonObj = (JSONObject) jsonArr.get(i);
				_jsonArr.add(_jsonObj);
				if (lastFileId == _jsonObj.getInt(id)) _jsonArr.clear();
			}
			return _jsonArr.toString();
		}
	}
	
	/**
	 * json输出
	 */
	public static void outputJson(HttpServletResponse response, Object info,Cookie cookie) {
		response.addCookie(cookie);
		try {
			PrintWriter out = response.getWriter();
			out.print(info);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * json输出
	 */
	public static void outputJson(HttpServletResponse response, Object info) {
		try {
			PrintWriter out = response.getWriter();
			out.print(info);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 跨域json输出
	 */
	public static void outputJson(
			HttpServletRequest request,
			HttpServletResponse response, String info) {
		String jsoncallback = request.getParameter("jsoncallback");
		if (!CommonToolUtil.isBlank(jsoncallback))
			info = jsoncallback + "(" + info + ")";
		outputJson(response, info);
	}

	public static boolean postBeanData(String url, Object bean) {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return postData(url, objectMapper.writeValueAsString(bean));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static String postBeanDataWithResult(String url, Object bean) {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return postDataWithResult(url, objectMapper.writeValueAsString(bean));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


	private static synchronized void initPool() {
		if (cm == null) {
			cm = new ThreadSafeClientConnManager();
			cm.setDefaultMaxPerRoute(500);
			cm.setMaxTotal(500);
		}
	}

	/**
	 * 获得分页的page - 为兼容老的分页
	 * @param start
	 * @param page
	 * @return
	 */
	public static int getPage(Integer start, Integer page) {
		if (start != null) {
			return start / ALIST_COUNT;
		}
		return page == null ? 0 : page;
	}
	public static String getDataResult(String key, String urlParam, RedisManager redisManager, Configure configure) {
		String value = redisManager.get(key);
		if (value == null) {
			String url = urlParam;
			if (CommonToolUtil.canGetData(url)) {
				value = redisManager.get(key);
			}
		}
		return value;
	}
	//为列表赋redis中最新的互动计数，目前只有评论和点赞数，待扩展
	public static String refreshCounts(String eventKey, String value,RedisManager redisManager) {
		if (null != value) {
			JSONArray jsonArr = JSONArray.fromObject(value);
			int size = jsonArr.size();
			for (int i = 0; i < size; i++) {
				JSONObject jsonObj = (JSONObject) jsonArr.get(i);
				refreshCount(eventKey, jsonObj,redisManager);
			}
			return jsonArr.toString();
		}
		return value;
	}
	
	/**
	 * 为列表赋redis中最新的互动计数，value格式是{list:[], XXXX}
	 * @return
	 */
	public static String refreshCountsInList(String eventKey, String value,RedisManager redisManager) {
		try {
			if (null != value) {
				JSONObject json = JSONObject.fromObject(value);
				JSONArray jsonArr = json.getJSONArray("list");
				int size = jsonArr.size();
				for (int i = 0; i < size; i++) {
					JSONObject jsonObj = (JSONObject) jsonArr.get(i);
					refreshCount(eventKey, jsonObj,redisManager);
				}
				return json.toString();
			}
		} catch (Exception e) {
		}
		return value;
	}
	//为对象赋redis中最新的互动计数，目前只有评论和点赞数，待扩展
	public  static String refreshCount(String eventKey, JSONObject jsonObj,RedisManager redisManager){
		if (null != jsonObj && jsonObj.has("fileId")) {
			String docId = jsonObj.getString("fileId");
			if(jsonObj.has("countPraise")){
				int countPraise = getCounts(eventKey, jsonObj, docId, "countPraise", "p",redisManager);
				jsonObj.put("countPraise", countPraise);
			}
			if(jsonObj.has("countDiscuss")) {
				int countDiscuss = getCounts(eventKey, jsonObj, docId, "countDiscuss", "d",redisManager);
				jsonObj.put("countDiscuss", countDiscuss);
			}
		}
		return jsonObj.toString();
	}
	/**
	 * 分别从 Redis 中与 JSON中 获取count值，取大的
	 *
	 * @param jsonObj
	 * @param docId
	 * @param paramName
	 * @param field
	 * @return
	 */
	private  static int getCounts(String eventKey, JSONObject jsonObj, String docId, String paramName, String field, RedisManager redisManager) {
		// 从Redis中获取 count
		String key = eventKey + docId;
		String countRS = redisManager.hget(key, field);
		int count = CommonToolUtil.isBlank(countRS) ? 0 : Integer.valueOf(countRS);

		// 从Json中获取 count
		String countJS = jsonObj.getString(paramName);
		int countJ = CommonToolUtil.isBlank(countJS) ? 0 : Integer.valueOf(countJS);
		
		// 取大的
		count = count > countJ ? count : countJ;
		return count;
	}

//	private static String filterBadChar(String str) {
//		return str.replace("%", "%25").replace("<", "%3C").replace(">", "%3E")
//				.replace("\"", "%22").replace("{", "%7b").replace("}", "%7d")
//				.replace("\\", "%5C").replace(" ", "%20").replace("&", "%26")
//				.replace("^", "%5E");
//	}

	public static void checkValid(ServletRequest req,Map<String, String> paramMap, Configure configure) throws Exception {
    HttpServletRequest request = (HttpServletRequest) req;
		request.setCharacterEncoding("utf-8");
		
		String sign = request.getHeader("program-sign");
		String time = request.getHeader("timestamp");
		long t = time == null ? 0 : Long.valueOf(time);
		String random = request.getHeader("random");
		String params = request.getHeader("program-params");
		
		boolean valid = params == null ? false : params.matches("^[a-zA-Z_]+(,[a-zA-Z_]+)*$");
		if (random == null || sign == null || !valid || t < 1451577600000l) {
			throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "arguments are not enough");
		}
		StringBuffer buffer = new StringBuffer("random=").append(random).append("&timestamp=").append(time);
		StringBuffer sBuffer = new StringBuffer();
		boolean paramsValid = true;
		for (String p : params.split(",")) {
			String v = paramMap.get(p);
			if (v == null) {
				paramsValid = false;
				break;
			}
			sBuffer.append(p).append("=").append(v).append("&");
		}
		if (paramsValid) {
			sBuffer.append("secret=").append(MD5Util.md5(buffer.toString()));
			String md5 = MD5Util.md5(sBuffer.toString());
			if (!sign.equals(md5))
				paramsValid = false;
		}
		if (!paramsValid) {
			throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "sign is not invalid.");
		}
	}

	/*
	* AES加密
	* 1.构造密钥生成器
	* 2.根据ecnodeRules规则初始化密钥生成器
	* 3.产生密钥
	* 4.创建和初始化密码器
	* 5.内容加密
	* 6.返回字符串
	*/
    public static String AESEncode(String encodeRules,String content){
        try {
            //1.构造密钥生成器，指定为AES算法,不区分大小写
            KeyGenerator keygen=KeyGenerator.getInstance("AES");
            //2.根据ecnodeRules规则初始化密钥生成器
            //生成一个128位的随机源,根据传入的字节数组
            keygen.init(128, new SecureRandom(encodeRules.getBytes()));
              //3.产生原始对称密钥
            SecretKey original_key=keygen.generateKey();
              //4.获得原始对称密钥的字节数组
            byte [] raw=original_key.getEncoded();
            //5.根据字节数组生成AES密钥
            SecretKey key=new SecretKeySpec(raw, "AES");
              //6.根据指定算法AES自成密码器
            Cipher cipher=Cipher.getInstance("AES");
              //7.初始化密码器，第一个参数为加密(Encrypt_mode)或者解密解密(Decrypt_mode)操作，第二个参数为使用的KEY
            cipher.init(Cipher.ENCRYPT_MODE, key);
            //8.获取加密内容的字节数组(这里要设置为utf-8)不然内容中如果有中文和英文混合中文就会解密为乱码
            byte [] byte_encode=content.getBytes("utf-8");
            //9.根据密码器的初始化方式--加密：将数据加密
            byte [] byte_AES=cipher.doFinal(byte_encode);
          //10.将加密后的数据转换为字符串
            //这里用Base64Encoder中会找不到包
            //解决办法：
            //在项目的Build path中先移除JRE System Library，再添加库JRE System Library，重新编译后就一切正常了。
            String AES_encode=new String(new BASE64Encoder().encode(byte_AES));
          //11.将字符串返回
            return AES_encode;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        
        //如果有错就返加nulll
        return null;         
    }
    /*
     * AES解密
     * 解密过程：
     * 1.同加密1-4步
     * 2.将加密后的字符串反纺成byte[]数组
     * 3.将加密内容解密
     */
    public static String AESDncode(String content){
    	return AESDncode("ABCDDCBA", content);
    }
    public static String AESDncode(String encodeRules,String content){
        try {
            //1.构造密钥生成器，指定为AES算法,不区分大小写
            KeyGenerator keygen=KeyGenerator.getInstance("AES");
            //2.根据ecnodeRules规则初始化密钥生成器
            //生成一个128位的随机源,根据传入的字节数组
            keygen.init(128, new SecureRandom(encodeRules.getBytes()));
              //3.产生原始对称密钥
            SecretKey original_key=keygen.generateKey();
              //4.获得原始对称密钥的字节数组
            byte [] raw=original_key.getEncoded();
            //5.根据字节数组生成AES密钥
            SecretKey key=new SecretKeySpec(raw, "AES");
              //6.根据指定算法AES自成密码器
            Cipher cipher=Cipher.getInstance("AES");
              //7.初始化密码器，第一个参数为加密(Encrypt_mode)或者解密(Decrypt_mode)操作，第二个参数为使用的KEY
            cipher.init(Cipher.DECRYPT_MODE, key);
            //8.将加密并编码后的内容解码成字节数组
            byte [] byte_content= new BASE64Decoder().decodeBuffer(content);
            /*
             * 解密
             */
            byte [] byte_decode=cipher.doFinal(byte_content);
            String AES_decode=new String(byte_decode,"utf-8");
            return AES_decode;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        
        //如果有错就返加nulll
        return null;         
    }
    //判断字符串是否为数字
    public static boolean isNumeric(String str){ 
	   Pattern pattern = Pattern.compile("[0-9]*"); 
	   Matcher isNum = pattern.matcher(str);
	   if( !isNum.matches() ){
	       return false; 
	   } 
	   return true; 
	}

	public static boolean checkValid(String url,int loginID,long time,String sign,RedisManager redisManager ){
        //调试用
		if("debug1q2w3e".equals(sign)){
			return true;
		}

    	long currentTime = System.currentTimeMillis();
        if((currentTime-time)>1000*60){
        	System.out.println("请求超时");
            return false;
        }

        String token = redisManager.hget(RedisKey.APP_USER_TOKEN, String.valueOf(loginID));

        StringBuffer urlToSign = new StringBuffer();
        urlToSign.append(url).append("?loginID=").append(loginID)
                .append("&time=").append(time).append("&token=").append(token);

        System.out.println("urlToSign------>"+urlToSign);

        String _sign = MD5Util.md5(urlToSign.toString());

        System.out.println("_sign------>"+_sign);

        if(!_sign.equals(sign)){
			System.out.println("sign验证失败");
            return false;
        }

        return true;
    }

	public static InputStream getInputStream(String url){
		HttpParams param = new BasicHttpParams();
		initPool();

		HttpClient httpClient = new DefaultHttpClient(cm, param);
		httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);
		httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 10000);
		HttpGet httpGet = new HttpGet(url);
		try {
			int rtCode = 0;
			HttpResponse response = httpClient.execute(httpGet);
			rtCode = response.getStatusLine().getStatusCode();

			if (200 == rtCode) {
				InputStream in = response.getEntity().getContent();
				return in;
			} else {
				closeRequest(httpClient, httpGet);
			}
		} catch (Exception e) {
			closeRequest(httpClient, httpGet);
		}
		return null;
	}
	
	 /*
     * 加密用的Key 可以用26个字母和数字组成 此处使用AES-128-CBC加密模式，key需要为16位。
     * 可与安卓、ios互通，福州日报调试可用
     */
    private static String sKey = "smkldospdosldaaa";
    private static String ivParameter = "0392039203920300";
    
	public static String Encrypt(String encData ,String secretKey,String vector) throws Exception {
	        
        if(secretKey == null) {
            return null;
        }
        if(secretKey.length() != 16) {
            return null;
        }
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] raw = secretKey.getBytes();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        IvParameterSpec iv = new IvParameterSpec(vector.getBytes());// 使用CBC模式，需要一个向量iv，可增加加密算法的强度
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        byte[] encrypted = cipher.doFinal(encData.getBytes("utf-8"));
        return new BASE64Encoder().encode(encrypted);// 此处使用BASE64做转码。
    }


    // 加密
    public static String encrypt(String sSrc) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] raw = sKey.getBytes();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());// 使用CBC模式，需要一个向量iv，可增加加密算法的强度
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        byte[] encrypted = cipher.doFinal(sSrc.getBytes("utf-8"));
        return new BASE64Encoder().encode(encrypted);// 此处使用BASE64做转码。
    }

    // 解密
    public static String decrypt(String sSrc) throws Exception {
        try {
            byte[] raw = sKey.getBytes("ASCII");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] encrypted1 = new BASE64Decoder().decodeBuffer(sSrc);// 先用base64解密
            byte[] original = cipher.doFinal(encrypted1);
            String originalString = new String(original, "utf-8");
            return originalString;
        } catch (Exception ex) {
            return null;
        }
    }

	public String decrypt(String sSrc,String key,String ivs) throws Exception {
        try {
            byte[] raw = key.getBytes("ASCII");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec(ivs.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] encrypted1 = new BASE64Decoder().decodeBuffer(sSrc);// 先用base64解密
            byte[] original = cipher.doFinal(encrypted1);
            String originalString = new String(original, "utf-8");
            return originalString;
        } catch (Exception ex) {
            return null;
        }
    }
    
    public static String encodeBytes(byte[] bytes) {
        StringBuffer strBuf = new StringBuffer();

        for (int i = 0; i < bytes.length; i++) {
            strBuf.append((char) (((bytes[i] >> 4) & 0xF) + ((int) 'a')));
            strBuf.append((char) (((bytes[i]) & 0xF) + ((int) 'a')));
        }

        return strBuf.toString();
    }

	public static String buildFailureArrayResult(String errorInfo) {
		JSONObject result = new JSONObject();
		result.put("success", false);
		result.put("errorInfo", errorInfo);
		result.put("results", new JSONArray());
		return result.toString();
	}

	public static String buildFailureObjectResult(String errorInfo) {
		JSONObject result = new JSONObject();
		result.put("success", false);
		result.put("errorInfo", errorInfo);
		result.put("results", new JSONObject());
		return result.toString();
	}

	public static String checkAuth(int eid, long time, String data, String sign,RedisManager redisManager,Configure configure) {
        //调试用
        if("debug1q2w3e".equals(sign)){

            return null;
        }
	    String authKey = RedisKey.EXTERNAL_KEY;
        String authKeyValue = redisManager.hget(authKey, String.valueOf(eid));
        if(StringUtils.isBlank(authKeyValue)){
            String url = configure.getAppServerUrl()+"/getExternalSystemAuth.do?eid="+eid;
            if (CommonToolUtil.canGetData(url)){
                authKeyValue = redisManager.hget(authKey,String.valueOf(eid));
                if(StringUtils.isBlank(authKeyValue)){
                    authKeyValue = "身份验证失败；系统中未查询到eid为"+eid+"的授权用户！";
                    return authKeyValue;
                }
            }else{
                authKeyValue = "身份验证失败；内部错误，请联系开发人员！";
                return authKeyValue;
            }
        }

        long timeInterval = 1 * 1000 * 30;
        long currentTime = System.currentTimeMillis();
        if((currentTime-time-timeInterval) > 0){
            authKeyValue = "请求超时,请刷新time值。";
            return authKeyValue;
        }

        StringBuffer tmpSign = new StringBuffer();
        if(data == null){
            data = "";
        }
        tmpSign.append("eid=").append(eid).append("&time=").append(time).append("&data=").append(data).append("&esecret=").append(authKeyValue);

        System.out.println("Sign---->"+tmpSign.toString());

        String currentSign = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(tmpSign.toString().getBytes("UTF-8"));
            currentSign = toHex(md.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            authKeyValue = "身份验证失败；内部错误，请联系开发人员！";
            return authKeyValue;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            authKeyValue = "身份验证失败；内部错误，请联系开发人员！";
            return authKeyValue;
        }

        System.out.println("currentSign---->"+currentSign);
        if(currentSign == null || !currentSign.equals(sign)){
            authKeyValue = "sign验证失败！";
            return authKeyValue;
        }

		return null;
	}

    private static String toHex(byte buffer[]) {
        StringBuffer sb = new StringBuffer(buffer.length * 2);
        for (int i = 0; i < buffer.length; i++) {
            sb.append(Character.forDigit((buffer[i] & 240) >> 4, 16));
            sb.append(Character.forDigit(buffer[i] & 15, 16));
        }
        return sb.toString();
    }
}
