package com.ims.core.redis;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.ims.core.constant.WebplusCons;
import com.ims.core.util.WebplusJson;
import com.ims.core.util.WebplusUtil;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

/**
 * 
 * 类描述： <b>Redis客户端</b>
 * 创建人：陈骑元
 * 创建时间：2016-6-2 上午12:51:44
 * 修改人：蓝枫 
 * 修改时间：2016-6-2 上午12:51:44
 * 修改备注： 
 * @version
 */
@Component("webplusJedis")
public class WebplusJedis {
	private  Logger logger = LoggerFactory.getLogger(WebplusJedis.class);
	
	 @Autowired(required=false)
	 private JedisPool jedisPool;
	 
	 @Autowired(required=false)
	 private JedisCluster jedisCluster;
	 



	/**
	 * 获取Jedis连接客户端
	 * 
	 * @return
	 */
	public Jedis getJedisClient() {
	
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
		} catch (Exception e) {
			logger.error("获取Redis客户端连接失败。"+e);
		}
		if (jedis == null) {
			logger.warn("没有获取到Redis客户端连接。");
		}
		return jedis;
	}

	/**
	 * 安全回收资源
	 * 
	 * @param jedis
	 */
	public  void close(Jedis jedis) {
		try {
			if(jedis!=null){
				jedis.close();
			}
		} catch (Exception e) {
			if (jedis.isConnected()) {
				jedis.quit();
				jedis.disconnect();
			}
		}
	}

	/**
	 * 设置字符串型数据
	 * 
	 * @param key
	 *            存储键
	 * @param value
	 *            存储值
	 * @param timeout
	 *            超时时间(单位：秒） 设置为0，则无时效性。
	 * @return
	 */
	public void setString(String key, String value, int timeout) {
		if (WebplusUtil.isEmpty(key)) {
			throw new NullPointerException("Key不能为空!");
		}
		if(checkRedisMode(WebplusCons.REDIS_MODE_CLUSTER)){
			jedisCluster.set(key, value);
			if (timeout > 0) {
				jedisCluster.expire(key, timeout);
			}
		}else{
			Jedis jedis = null;
			try {
				jedis = getJedisClient();
				jedis.set(key, value);
				if (timeout > 0) {
					jedis.expire(key, timeout);
				}
			} catch (Exception e) {
				close(jedis);
				logger.error("操作Redis失败", e);
			} finally {
				close(jedis);
			}
		}
		
	}

	/**
	 * 设置字符串型数据过期时间
	 * 
	 * @param key
	 *            存储键
	 * @param timeout
	 *            超时时间(单位：秒）
	 * @param key
	 */
	public void exprString(String key, int timeout) {
		if (WebplusUtil.isEmpty(key)) {
			return;
		}
		if(checkRedisMode(WebplusCons.REDIS_MODE_CLUSTER)){
			jedisCluster.expire(key, timeout);
			
		}else{
			Jedis jedis = null;
			try {
				jedis = getJedisClient();
				jedis.expire(key, timeout);
			} catch (Exception e) {
				close(jedis);
				logger.error("操作Redis失败", e);
			} finally {
				close(jedis);
			}
		}
		
		
	}

	/**
	 * 设置序列化对象数据
	 * 
	 * @param key
	 *            存储键
	 * @param value
	 *            存储值
	 * @param timeout
	 *            超时时间(单位：秒） 设置为0，则无时效性。
	 * @return
	 */
	public void setObj(String key, byte[] value, int timeout) {
		if (WebplusUtil.isEmpty(key)) {
			throw new NullPointerException("Key不能为空!");
		}
		if(checkRedisMode(WebplusCons.REDIS_MODE_CLUSTER)){
			jedisCluster.set(key.getBytes(), value);
			if (timeout > 0) {
				jedisCluster.expire(key, timeout);
			}
		}else{
			Jedis jedis = null;
			try {
				jedis = getJedisClient();
				jedis.set(key.getBytes(), value);
				if (timeout > 0) {
					jedis.expire(key, timeout);
				}
			} catch (Exception e) {
				close(jedis);
				logger.error("操作Redis失败", e);
			} finally {
				close(jedis);
			}
		}
		
	}

	/**
	 * 获取字符串型数据
	 * 
	 * @param key
	 * @return
	 */
	public  String getString(String key) {
		if (WebplusUtil.isEmpty(key)) {
			throw new NullPointerException("Key不能为空!");
		}
		String value = null;
		if(checkRedisMode(WebplusCons.REDIS_MODE_CLUSTER)){
			value =jedisCluster.get(key);
		}else{
			Jedis jedis = null;
			try {
				jedis = getJedisClient();
				value = jedis.get(key);
			} catch (Exception e) {
				close(jedis);
				logger.error("操作Redis失败", e);
			} finally {
				close(jedis);
			}
		}
		
		return value;
	}

	/**
	 * 获取序列化对象数据
	 * 
	 * @param key
	 * @return
	 */
	public  byte[] getObj(String key) {
		if (WebplusUtil.isEmpty(key)) {
			throw new NullPointerException("Key不能为空!");
		}
		byte[] value = null;
		if(checkRedisMode(WebplusCons.REDIS_MODE_CLUSTER)){
			value= jedisCluster.get(key.getBytes());
		}else{
			Jedis jedis = null;
			try {
				jedis = getJedisClient();
				value = jedis.get(key.getBytes());
			} catch (Exception e) {
				close(jedis);
				logger.error("操作Redis失败", e);
			} finally {
				close(jedis);
			}
		}
		
		return value;
	}

	/**
	 * 删除对象数据
	 * 
	 * @param key
	 */
	public  void delObj(String key) {
		if (WebplusUtil.isEmpty(key)) {
			return;
		}
		if(checkRedisMode(WebplusCons.REDIS_MODE_CLUSTER)){
			jedisCluster.del(key.getBytes());
		}else{
			Jedis jedis = null;
			try {
				jedis = getJedisClient();
				jedis.del(key.getBytes());
			} catch (Exception e) {
				close(jedis);
				logger.error("操作Redis失败", e);
			} finally {
				close(jedis);
			}
		}
		
	}

	/**
	 * 删除字符串数据
	 * 
	 * @param key
	 */
	public  void delString(String key) {
		if (WebplusUtil.isEmpty(key)) {
			return;
		}
		if(checkRedisMode(WebplusCons.REDIS_MODE_CLUSTER)){
			jedisCluster.del(key);
		}else{
			Jedis jedis = null;
			try {
				jedis = getJedisClient();
				jedis.del(key);
			} catch (Exception e) {
				close(jedis);
				logger.error("操作Redis失败", e);
			} finally {
				close(jedis);
			}
		}
		
	}
	/**
	 * 哈希获取
	 * 
	 * @param key
	 */
	public  String  hget(String key,String field) {
		if (WebplusUtil.isEmpty(key)||WebplusUtil.isEmpty(field)) {
			return "";
		}
		String value="";
		if(checkRedisMode(WebplusCons.REDIS_MODE_CLUSTER)){
			value=jedisCluster.hget(key, field);
		}else{
			Jedis jedis = null;
			try {
				jedis = getJedisClient();
				value=jedis.hget(key, field);
			} catch (Exception e) {
				close(jedis);
				logger.error("操作Redis失败", e);
			} finally {
				close(jedis);
			}
		}
		return value;
		
	}
	
	/**
	 * 哈希获取所有
	 * 
	 * @param key
	 */
	public  Map<String,String>  hgetAll(String key) {
		if (WebplusUtil.isEmpty(key)) {
			return new HashMap<String,String>();
		}
		Map<String,String> valueMap=new HashMap<String,String>();
		if(checkRedisMode(WebplusCons.REDIS_MODE_CLUSTER)){
			valueMap=jedisCluster.hgetAll(key);
		}else{
			Jedis jedis = null;
			try {
				jedis = getJedisClient();
				valueMap=jedis.hgetAll(key);
			} catch (Exception e) {
				close(jedis);
				logger.error("操作Redis失败", e);
			} finally {
				close(jedis);
			}
		}
		return valueMap;
		
	}
	
	/**
	 * 哈希设置
	 * 
	 * @param key
	 */
	public void  hset(String key,String field,String value) {
		if (WebplusUtil.isEmpty(key)) {
			throw new NullPointerException("Key不能为空!");
		}
		if(checkRedisMode(WebplusCons.REDIS_MODE_CLUSTER)){
			jedisCluster.hset(key, field, value);
		}else{
			Jedis jedis = null;
			try {
				jedis = getJedisClient();
				jedis.hset(key, field, value);
			} catch (Exception e) {
				close(jedis);
				logger.error("操作Redis失败", e);
			} finally {
				close(jedis);
			}
		}
		
	}
	
	/**
	 * 哈希设置
	 * 
	 * @param key
	 */
	public void  hdel(String key,String... fields) {
		if (WebplusUtil.isEmpty(key)) {
			throw new NullPointerException("Key不能为空!");
		}
		if(checkRedisMode(WebplusCons.REDIS_MODE_CLUSTER)){
			jedisCluster.hdel(key, fields);
		}else{
			Jedis jedis = null;
			try {
				jedis = getJedisClient();
				jedis.hdel(key, fields);
			} catch (Exception e) {
				close(jedis);
				logger.error("操作Redis失败", e);
			} finally {
				close(jedis);
			}
		}
		
	}
	
	/**
	 * 哈希设置
	 * 
	 * @param key
	 */
	public void hmset(String key,Map<String, String> hash) {
		if (WebplusUtil.isEmpty(key)) {
			throw new NullPointerException("Key不能为空!");
		}
		if(checkRedisMode(WebplusCons.REDIS_MODE_CLUSTER)){
			jedisCluster.hmset(key, hash);
		}else{
			Jedis jedis = null;
			try {
				jedis = getJedisClient();
				jedis.hmset(key, hash);
			} catch (Exception e) {
				close(jedis);
				logger.error("操作Redis失败", e);
			} finally {
				close(jedis);
			}
		}
		
	}
	/**
	 * 随机设置lrange
	 * 
	 * @param key
	 */
	public List<String> lrange(String key,long start,  long end) {
		if (WebplusUtil.isEmpty(key)) {
			throw new NullPointerException("Key不能为空!");
		}
		List<String> strList=Lists.newArrayList();
		if(checkRedisMode(WebplusCons.REDIS_MODE_CLUSTER)){
			strList=jedisCluster.lrange(key, start, end);
		}else{
			Jedis jedis = null;
			try {
				jedis = getJedisClient();
				strList=jedis.lrange(key, start, end);
			} catch (Exception e) {
				close(jedis);
				logger.error("操作Redis失败", e);
			} finally {
				close(jedis);
			}
		}
		return strList;
	}
	
	/**
	 * 随机设置lrange
	 * 
	 * @param key
	 */
	public void rpush(String key,List<?> dataList) {
		if (WebplusUtil.isEmpty(key)) {
			throw new NullPointerException("Key不能为空!");
		}
		if(checkRedisMode(WebplusCons.REDIS_MODE_CLUSTER)){
			for(Object data:dataList){
				jedisCluster.rpush(key, WebplusJson.toJson(data));
			}
		}else{
			Jedis jedis = null;
			try {
				jedis = getJedisClient();
				for(Object data:dataList){
					jedis.rpush(key, WebplusJson.toJson(data));
				}
				
			} catch (Exception e) {
				close(jedis);
				logger.error("操作Redis失败", e);
			} finally {
				close(jedis);
			}
		}
		
	}
	
	/**
	 * 随机设置lrange
	 * 
	 * @param key
	 */
	public void rpush(String key,String value) {
		if (WebplusUtil.isEmpty(key)) {
			throw new NullPointerException("Key不能为空!");
		}
		if(checkRedisMode(WebplusCons.REDIS_MODE_CLUSTER)){
			
		   jedisCluster.rpush(key, value);
			
		}else{
			Jedis jedis = null;
			try {
				jedis = getJedisClient();
				
					jedis.rpush(key, value);
				
				
			} catch (Exception e) {
				close(jedis);
				logger.error("操作Redis失败", e);
			} finally {
				close(jedis);
			}
		}
		
	}
	
	/**
	 * 判断键是否存在
	 * 
	 * @param key
	 */
	public boolean exists(String key) {
		if (WebplusUtil.isEmpty(key)) {
			throw new NullPointerException("Key不能为空!");
		}
		boolean result=false;
		if(checkRedisMode(WebplusCons.REDIS_MODE_CLUSTER)){
			
			result=jedisCluster.exists(key);
			
		}else{
			Jedis jedis = null;
			try {
				jedis = getJedisClient();
				result=jedis.exists(key);
				
			} catch (Exception e) {
				close(jedis);
				logger.error("操作Redis失败", e);
			} finally {
				close(jedis);
			}
		}
		return result;
	}
	
	/**
	 * 判断键是否存在
	 * 
	 * @param key
	 */
	public void expire(String key,int seconds ) {
		if (WebplusUtil.isEmpty(key)) {
			throw new NullPointerException("Key不能为空!");
		}
		if(checkRedisMode(WebplusCons.REDIS_MODE_CLUSTER)){
			
		jedisCluster.expire(key, seconds);
			
		}else{
			Jedis jedis = null;
			try {
				jedis = getJedisClient();
				jedis.expire(key, seconds);
				
			} catch (Exception e) {
				close(jedis);
				logger.error("操作Redis失败", e);
			} finally {
				close(jedis);
			}
		}
		
	}
	
	/**
	 * 判断键是否存在
	 * 
	 * @param key
	 */
	public Long incr(String key ) {
		
		if (WebplusUtil.isEmpty(key)) {
			throw new NullPointerException("Key不能为空!");
		}
		Long value	=0l;
		if(checkRedisMode(WebplusCons.REDIS_MODE_CLUSTER)){
		
			value=jedisCluster.incr(key);
		}else{
			Jedis jedis = null;
			try {
				jedis = getJedisClient();
				value=jedis.incr(key);
				
			} catch (Exception e) {
				close(jedis);
				logger.error("操作Redis失败", e);
			} finally {
				close(jedis);
			}
		}
		return value;
		
	}
	
	/**
	 * 判断键是否存在
	 * 
	 * @param key
	 */
	public String  getSet(String key,String value ) {
		
		if (WebplusUtil.isEmpty(key)) {
			throw new NullPointerException("Key不能为空!");
		}
		String rtnValue="";
		if(checkRedisMode(WebplusCons.REDIS_MODE_CLUSTER)){
		
			rtnValue=jedisCluster.getSet(key, rtnValue);
		}else{
			Jedis jedis = null;
			try {
				jedis = getJedisClient();
				rtnValue=jedis.getSet(key, value);
				
			} catch (Exception e) {
				close(jedis);
				logger.error("操作Redis失败", e);
			} finally {
				close(jedis);
			}
		}
		return rtnValue;
		
	}
	/**
	 * 哈希设置
	 * 
	 * @param key
	 */
	public Set<String> keys(String pattern) {
		if (WebplusUtil.isEmpty(pattern)) {
			throw new NullPointerException("pattern不能为空!");
		}
		Set<String> keys = new TreeSet<>();

		if(checkRedisMode(WebplusCons.REDIS_MODE_CLUSTER)){
			Map<String, JedisPool> clusterNodes = jedisCluster.getClusterNodes();
			for(String k : clusterNodes.keySet()){
				logger.debug("Getting keys from: {}", k);
				JedisPool jp = clusterNodes.get(k);
				Jedis connection = jp.getResource();
				try {
					keys.addAll(connection.keys(pattern));
				} catch(Exception e){
					logger.error("Getting keys error: {}", e);
				} finally{
					logger.debug("Connection closed.");
					connection.close();//用完一定要close这个链接！！！
				}
			}

		}else{
			Jedis jedis = null;
			try {
				jedis = getJedisClient();
				keys=jedis.keys(pattern);
			} catch (Exception e) {
				close(jedis);
				logger.error("操作Redis失败", e);
			} finally {
				close(jedis);
			}
		}
		return keys;
	}


	/**
	 * 清除DB
	 * 
	 * @param key
	 */
	public  void flushDB() {
		Jedis jedis = null;
		try {
			jedis = getJedisClient();
			jedis.flushDB();
			logger.info("Redsi缓存DB重置成功。");
		} catch (Exception e) {
			close(jedis);
			logger.error("操作Redis失败", e);
		} finally {
			close(jedis);
		}
	}
	/**
	 * 
	 * 简要说明：检查redis模式
	 * 编写者：陈骑元
	 * 创建时间：2018年12月25日 下午8:25:54
	 * @param 说明
	 * @return 说明
	 */
	private boolean checkRedisMode(String mode){
		String redisMode=System.getProperty("redisMode");
		if(redisMode.equals(mode)){
			return true;
		}
		return false;
	}

   /**
    * 
    * 简要说明：判断redis 是否存活 false 不在线
    * 编写者：陈骑元
    * 创建时间：2016年12月12日 上午11:47:39
    * @param 说明
    * @return 说明
    */
	public   boolean  isLive1() {
		Jedis jedis = null;
		try {
			jedis = getJedisClient();
			if(jedis!=null){
				String ping=jedis.ping();
				if("PONG".equals(ping)){
					return true;
				}
			}
		
		} catch (Exception e) {
			close(jedis);
			logger.error("操作Redis失败", e);
		} finally {
			close(jedis);
		}
		return false;
	}

	
}
