package com.ims.config;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.ims.core.constant.WebplusCons;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * 
 * 类名:com.ims.config.RedisConfig
 * 描述:redis缓存配置类
 * 编写者:陈骑元
 * 创建时间:2018年4月5日 上午9:25:04
 * 修改说明:
 */
@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {
	
	@Value("${spring.redis.mode}")
	private String  mode;
	
	@Value("${spring.redis.clusterNodes}")
	private String  clusterNodes;
	
	@Value("${spring.redis.host}")
	private String host;

	@Value("${spring.redis.port}")
	private int port;

	@Value("${spring.redis.password}")
	private String password;
	
	@Value("${spring.redis.timeout}")
	private int timeout;

	@Value("${spring.redis.pool.max-active}")
	private int maxActive;
	
	@Value("${spring.redis.pool.max-wait}")
	private long maxWaitMillis;
	
	@Value("${spring.redis.pool.max-idle}")
	private int maxIdle;

	@Value("${spring.redis.pool.min-idle}")
	private int minIdle;

	

	@Value("${redis.cache.expiration:3600}")
	private Long expiration;
	
	@Value("${redis.cache.appId:websys}")
	private String appId;
	
    /**
     * 
     * 简要说明：集群连接方式
     * 编写者：陈骑元
     * 创建时间：2018年12月25日 下午1:53:16
     * @param 说明
     * @return 说明
     */
    @Bean
    @ConditionalOnProperty(name = {"spring.redis.mode"}, havingValue = WebplusCons.REDIS_MODE_CLUSTER)
    public JedisCluster getJedisCluster() {
    		
    	JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
	    jedisPoolConfig.setMaxIdle(maxIdle);
	    jedisPoolConfig.setMinIdle(minIdle);
	    jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
	    jedisPoolConfig.setMaxTotal(maxActive);
	    System.setProperty("redisMode",mode);
	    System.setProperty("appId",appId);
        String[] serverArray = clusterNodes.split(",");//获取服务器数组(这里要相信自己的输入，所以没有考虑空指针问题)
        Set<HostAndPort> nodes = new HashSet<>();
        for (String ipPort : serverArray) {
            String[] ipPortPair = ipPort.split(":");
            nodes.add(new HostAndPort(ipPortPair[0].trim(), Integer.valueOf(ipPortPair[1].trim())));
        }
        if(StringUtils.isNotBlank(password)){
        	 return  new JedisCluster(nodes,timeout,1000,1,password ,jedisPoolConfig);//需要密码连接的创建对象方
		}else{
			 return  new JedisCluster(nodes,timeout,jedisPoolConfig);
		}
        
    
       
    	   
       
    }
    
    @Bean
    @ConditionalOnProperty(name = {"spring.redis.mode"}, havingValue = WebplusCons.REDIS_MODE_SINGLE)
	public JedisPool redisPoolFactory() {
		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
	    jedisPoolConfig.setMaxIdle(maxIdle);
	    jedisPoolConfig.setMinIdle(minIdle);
	    jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
	    jedisPoolConfig.setMaxTotal(maxActive);
	    System.setProperty("redisMode",mode);
	    System.setProperty("appId",appId);
		JedisPool jedisPool =null;
		if(StringUtils.isNotBlank(password)){
			jedisPool=new JedisPool(jedisPoolConfig, host, port, timeout, password);
		}else{
			jedisPool = new JedisPool(jedisPoolConfig, host, port);
		}

		return jedisPool;
	}

	@Bean
	public CacheManager cacheManager(@SuppressWarnings("rawtypes") RedisTemplate redisTemplate) {
		RedisCacheManager rcm = new RedisCacheManager(redisTemplate);
		rcm.setDefaultExpiration(expiration);
		return rcm;
	}

	@Bean
	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
		RedisTemplate<String, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(factory);
		template.setKeySerializer(new StringRedisSerializer());
		template.setValueSerializer(new JdkSerializationRedisSerializer());
		return template;
	}

}
