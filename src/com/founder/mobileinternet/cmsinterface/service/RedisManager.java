package com.founder.mobileinternet.cmsinterface.service;

import java.util.List;
import java.util.Set;

import com.founder.mobileinternet.cmsinterface.pojo.Configure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.founder.mobileinternet.cmsinterface.util.CommonToolUtil;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

@Service
public class RedisManager {
	@Autowired
	private  RedisService redisService;
    @Autowired
    private Configure configure;
    public static final int minute1 = 60; //设置Key的过期时间，1分钟
    public static final int hour1 = minute1 * 60 ; //设置Key的过期时间，1小时
    public static final int day1 = hour1 * 24; //设置Key的过期时间，1天
    public static final int week1 = day1 * 7; //1周




	
	/**
	 * 多站点时的Key命名。
	 * 在siteID>1时，Redis中的key需要带站点ID。
	 * 注意，不会因多站点而混淆的Key不需要加站点ID
	 */
	public String getKeyBySite(String key, int siteID) {
		if (siteID <= 1) return key;
		
		boolean endsWithDot = key.endsWith(".");
		
		if (!endsWithDot) key += ".";
		key += "site" + siteID;
		
		if (endsWithDot) key += ".";
		
		return key;
	}
	/**
	 * 多站点时的Key命名。
	 * 在siteID>1时，Redis中的key需要带站点ID。
	 * 注意，不会因多站点而混淆的Key不需要加站点ID
	 */
	public String getKeyBySite(String key, String siteId) {
		int siteID = CommonToolUtil.getIntValue(siteId, 1);
		
		return getKeyBySite(key, siteID);
	}
	
	/**
	 * 多站点时取分类的key,部分分类按站点区分,部分分类不按站点区分
	 * @param siteID 站点ID
	 * @param code 分类码
	 * @return 分类的key
	 */
	public String getCatKeyBySite(int siteID,String code) {
		if(code.equals("PHOTO") || code.equals("VIDEO") || code.equals("SPECIAL")
				|| code.equals("SOURCE") || code.equals("TEMPLATE") || code.equals("BLOCK")
				|| code.equals("RESOURCE") || code.equals("EXTFIELD") || code.equals("VOTE")
				|| code.equals("DISCUSSTYPE") || code.equals("QA") || code.equals("ARTICLETRADE")){
			return getKeyBySite(RedisKey.APP_CATS_KEY,siteID) + code;
		}
		return RedisKey.APP_CATS_KEY + code ;
	}
	
	public String getCatKeyBySite(String siteId,String code) {
		int siteID = CommonToolUtil.getIntValue(siteId, 1);
		
		return getCatKeyBySite(siteID,code);

	}
	
	/**
	 * 向Hash插入field。只用于评论延迟提交
	 */
	public void hset(String key, String field, String value) {
        redisService.hset(key,field,value);
	}

	public void hset(String key, String field, String value, int expireTime) {
		redisService.hset(key, field, value,expireTime);
	}

	/**
	 * 从Redis中取出一个Hash的域值
	 */
	public String hget(String key, String field) {
		return redisService.hget(key,field);
	}
	
	public List<String> hmget(String key, String field) {
		return redisService.hmget(key,field);
	}

	/**
	 * 从Redis中取出一个值
	 */
	public String get(String key) {
		return redisService.get(key);
	}

	/**
	 * 清空一个值
	 */
	public void clear(String key) {
		redisService.clear(key);
	}

	/**
	 * 判断一个key是否存在
	 */
	public boolean exists(String key) {
		return redisService.exists(key);
	}

    /**
     * 值+1
     */
    public void incr(String key) {
		redisService.incr(key);
    }

    /**
     * 设置过期时间+1
     */
    public void expire(String key,int time) {
		redisService.expire(key,time);
    }

	/**
	 * 拦截同一ip短时间内多次访问
	 */
	public boolean setLcIfNotExist(String ip) {
		return setIfNotExist("app.timer.lc." + ip, configure.getTimerLc());
	}
	public boolean setSubIfNotExist(String ip) {
		return setIfNotExist("app.timer.sub." + ip, configure.getTimerSub());
	}
	public boolean setUpIfNotExist(String ip) {
		return setIfNotExist("app.timer.up." + ip, configure.getTimerUp());
	}
	public boolean setEventIfNotExist(String ip) {
		return setIfNotExist("app.timer.event." + ip, configure.getTimerEvent());
	}

	private static final int COUNT_DELAY_DISCUSS = 2000;
	private static final int COUNT_DELAY_SUBJECTQA = 200;
	private static final int COUNT_DELAY_QA = 200;
	private static final int COUNT_DELAY_FAVORITE = 1000;
	private static final int COUNT_DELAY_EXPOSE = 100;
	private static final int COUNT_DELAY_ENTRY = 100;
	private static final int COUNT_DELAY_FEEDBACK = 100;
	private static final int COUNT_DELAY_TIPOFF = 100;

	public void set(String key, String value, int expireTime) {
 		redisService.set(key,value,expireTime);
	}

	/** 延迟入库的评论：增加一个新评论 */
	public  void addDelayDiscuss(String value) {
		addDelay(RedisKey.APP_DELAY_DISCUSS_KEY, value, COUNT_DELAY_DISCUSS);
	}
	/** 延迟入库的话题（问吧）：增加一个新提问 */
	public  void addDelaySubjectQA(String value) {
		addDelay(RedisKey.APP_DELAY_SUBJECTQA_KEY, value, COUNT_DELAY_SUBJECTQA);
	}
	/** 延迟入库的问答（问政）：增加一个新提问 */
	public  void addDelayQA(String value) {
		addDelay(RedisKey.APP_DELAY_QA_KEY, value, COUNT_DELAY_QA);
	}

	/** 延迟入库的收藏 */
	public void addDelayFavorite(String value) {
		addDelay(RedisKey.APP_DELAY_FAVORITE_KEY, value, COUNT_DELAY_FAVORITE);
	}
	/** 延迟入库的举报 */
	public void addDelayExpose(String value) {
		addDelay(RedisKey.APP_DELAY_EXPOSE_KEY, value, COUNT_DELAY_EXPOSE);
	}
	/** 延迟入库的活动报名 */
	public void addDelayEntry(String value) {
		addDelay(RedisKey.APP_DELAY_ENTRY_KEY, value, COUNT_DELAY_ENTRY);
	}
	/** 延迟入库的意见反馈 */
	public void addDelayFeedback(String value) {
		addDelay(RedisKey.APP_DELAY_FEEDBACK_KEY, value, COUNT_DELAY_FEEDBACK);
	}
	/** 延迟入库的报料 */
	public void addDelayTipoff(String value) {
		addDelay(RedisKey.APP_DELAY_TIPOFF_KEY, value, COUNT_DELAY_TIPOFF);
	}

	private boolean setIfNotExist(String key, int expireTime) {
        boolean result = true;
        if (redisService.exists(key)) {
            result =  false;
		} else {
			redisService.incr(key); // +1
			redisService.expire(key, expireTime); // expireTime秒过期
		}
		return result;
	}

	private void addDelay(String key, String value, int count) {
        redisService.addDelay(key,value,count);
	}

    public  boolean hexists(String key, String field) {
        return redisService.hexists(key,field);
    }

	/**
	 * 从Redis中取出一个集合的所有成员
	 */
	public Set<String> smembers(String key) {
        return redisService.smembers(key);
	}
	/**
	 * 向Redis集合插入成员，整数
	 */
	public void sadd(String key, long id) {
        redisService.sadd(key,id);
	}
	/**
	 * 去掉Redis集合的成员
	 */
	public void srem(String key, String field) {
        redisService.srem(key,field);
	}
	/**
	 * 向Redis集合插入成员
	 */
	public void sadd(String key, String value) {
		redisService.sadd(key,value);
	}
	/**
	 * 查询Redis集合里是否存在某值
	 */
	public boolean sismember(String key, String value) {
		return redisService.sismember(key,value);
	}
	/**
	 * 向list插入数据，用于我的评论
	 */
	public void lpush(String key, String value) {
        redisService.lpush(key,value);
	}
	/**
	 * 向list插入数据，用于我的评论
	 */
	public long llen(String key) {
		return redisService.llen(key);
	}
	/**
	 * 获取list数据，用于我的评论
	 * @param start 
	 * @param end 
	 */
	public List<String> lrange(String key, long start, long end) {
 		return redisService.lrange(key,start,end);
	}
}
