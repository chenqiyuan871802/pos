package com.ims.core.cache;


import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baomidou.mybatisplus.plugins.Page;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.Lists;
import com.ims.core.annotation.ItemTag;
import com.ims.core.constant.WebplusCons;
import com.ims.core.matatype.Dto;
import com.ims.core.matatype.Dtos;
import com.ims.core.matatype.impl.BaseModel;
import com.ims.core.matatype.impl.HashDto;
import com.ims.core.redis.WebplusJedis;
import com.ims.core.util.WebplusJson;
import com.ims.core.util.WebplusSpringContext;
import com.ims.core.util.WebplusUtil;
import com.ims.core.vo.Item;
import com.ims.core.vo.UserToken;

/**
 * 
 * 类名:com.toonan.core.cache.WebplusCache
 * 描述:缓存支持
 * 编写者:陈骑元
 * 创建时间:2019年4月27日 上午12:40:32
 * 修改说明:
 */
public class WebplusCache {
	private static Logger logger = LoggerFactory.getLogger(WebplusCache.class);
	/**
	 * jedis帮助
	 */
	private static WebplusJedis webplusJedis = WebplusSpringContext.getBean("webplusJedis");
	
	
	/**
	 * 
	 * 简要说明：获取键值参数整数，返回默认值
	 * 编写者：陈骑元
	 * 创建时间：2019年2月24日 上午1:44:55
	 * @param 说明
	 * @return 说明
	 */
	public static int getParamIntValue(String paramKey,int defaultValue) {
		int paramValue=getParamIntValue(paramKey);
		if(paramValue==-99){
			return defaultValue;
		}
		return paramValue;
	}
	/**
	 * 根据参数键获取参数值
	 * 
	 * @param paramPO参数键
	 * 
	 */
	public static int getParamIntValue(String paramKey) {
	    String paramValue=getParamValue(paramKey);
	    try {
			return Integer.parseInt(paramValue);
		} catch (Exception e) {
			
		}
		return -99;
	}

	/**
	 * 根据参数键获取参数值
	 * 
	 * @param paramPO参数键
	 * 
	 */
	public static String getParamValue(String paramKey) {
		String paramValue = "";
		Dto paramDto = getCacheParam(paramKey);
		if (WebplusUtil.isNotEmpty(paramDto)) {
			paramValue =paramDto.getString("paramValue");
		}
		return paramValue;
	}

	/**
	 * 从数据库参数表中根据参数键获取参数值
	 * 
	 * @param paramKey
	 *            参数键
	 * @param defaultValue
	 *            缺省值
	 * @return
	 */
	public static String getParamValue(String paramKey, String defaultValue) {
		String valueString = getParamValue(paramKey);
		if (WebplusUtil.isEmpty(valueString)) {
			valueString = defaultValue;
		}
		return valueString;
	}

	/**
	 * 
	 * 简要说明：根据参数键获取键值参数Dto
	 * 编写者：陈骑元
	 * 创建时间：2017年1月24日 上午10:22:18
	 * 
	 * @param 说明
	 * @return 说明
	 */
	public static Dto getCacheParam(String paramKey) {
		if(WebplusUtil.isEmpty(paramKey)){
			logger.error("获取缓存中键值参数失败：参数键为空");
			return null;
		}
		String paramJson = webplusJedis.hget(WebplusCons.CACHE_PREFIX.PARAM, paramKey);
		if(WebplusUtil.isEmpty(paramJson)){
			logger.error("缓存中没有获取到参数键【"+paramKey+"】,请确认参数键是否正确，或者键值参数表是否配置或启用，或是否刷新键值参数缓存");
			return null;
		}
		Dto paramDto=WebplusJson.fromJson(paramJson, HashDto.class);
		
		return paramDto;
	}
	
	/**
	 * 
	 * 简要说明：获取过滤的字典集合
	 * 编写者：陈骑元
	 * 创建时间：2018年12月15日 下午5:33:41
	 * @param 说明
	 * @return 说明
	 */
	public static List<Item> getItemList(String typeCode,String filterCodestr) {
		List<Item> itemList=Lists.newArrayList();
		List<Dto> dictList=getCacheDict(typeCode);
		 for(Dto dictDto:dictList){
			 if(dictDto.containsKey("itemCode")) {
				 Item item=new Item();
				 WebplusUtil.copyProperties(dictDto, item);
				 itemList.add(item);
			 }else {
				 String itemCode=dictDto.getString("dictCode");
				 if(!WebplusUtil.contains(itemCode, filterCodestr)){
					 Item item=new Item();
					 item.setTypeCode(typeCode);
					 item.setItemCode(itemCode);
					 item.setItemName(dictDto.getString("dictValue"));
					 itemList.add(item);
					 
				 }
			 }
			
		
	   }
		
		return itemList;
	}
	/**
	 * 
	 * 简要说明：获取包含字典
	 * 编写者：陈骑元
	 * 创建时间：2019年7月26日 上午12:27:00
	 * @param 说明
	 * @return 说明
	 */
	public static List<Item> getContainsItem(String typeCode,String containsCode){
		List<Item> itemListNew=Lists.newArrayList();
		List<Item> itemList=getItemList(typeCode);
		for(Item item:itemList)	{
			 if(WebplusUtil.contains(item.getItemCode(), containsCode)){
				 
				 itemListNew.add(item);
				 
			 }
		}
		return itemListNew;
	}
	/**
	 * 
	 * 简要说明：获取包含字典
	 * 编写者：陈骑元
	 * 创建时间：2019年7月26日 上午12:27:00
	 * @param 说明
	 * @return 说明
	 */
	public static List<Item> getContainsItem(String typeCode,List<String> containsCodeList){
		List<Item> itemListNew=Lists.newArrayList();
		List<Item> itemList=getItemList(typeCode);
		for(Item item:itemList)	{
			if(WebplusUtil.contains(item.getItemCode(), containsCodeList)){
				
				itemListNew.add(item);
				
			}
		}
		return itemListNew;
	}
	/**
	 * 
	 * 简要说明：获取字典值
	 * 编写者：陈骑元
	 * 创建时间：2018年12月15日 下午5:33:41
	 * @param 说明
	 * @return 说明
	 */
	public static List<Item> getItemList(String typeCode) {
		
		
		return getItemList(typeCode,"");
		
	}
	/**
	 * 
	 * 简要说明：根据字典类型和字典对照码获取字典对照值
	 * 编写者：陈骑元
	 * 创建时间：2019年4月28日 下午9:20:59
	 * @param 说明
	 * @return 说明
	 */
	public static String getItemName(String typeCode,String itemCode) {
		List<Item> itemList=getItemList(typeCode);
		
		return getItemName(itemList,itemCode);
	}
	
	
	/**
	 * 
	 * 简要说明：根据字典列表和字典对照码获取字典对照值
	 * 编写者：陈骑元
	 * 创建时间：2019年4月28日 下午9:20:05
	 * @param 说明
	 * @return 说明
	 */
	public static String getItemName(List<Item> itemList,String itemCode) {
		if(WebplusUtil.isNotEmpty(itemList)){
			for (Item item:itemList) {
				if (item.getItemCode().equals(itemCode)) {
					
					return  item.getItemName();
				}
			}
		}
		
		return "";
	}
	/**
	 * 
	 * 简要说明：获取缓存的字典
	 * 编写者：陈骑元
	 * 创建时间：2018年5月2日 下午10:57:15
	 * @param 说明
	 * @return 说明
	 */
	private static List<Dto> getCacheDict(String dictKey) {
		if(WebplusUtil.isEmpty(dictKey)){
			logger.error("获取缓存中字典失败：字典标识键为空");
			return Lists.newArrayList();
		}
		List<String> dictRedisList = webplusJedis.lrange(WebplusCons.CACHE_PREFIX.DICT + dictKey, 0, -1);
		if (WebplusUtil.isEmpty(dictRedisList)) {
			logger.error("缓存中没有获取到字典标识键【"+dictKey+"】字典,请确认字典标识键是否正确，或者通用字典是否配置或启用改字典标识键字典，或是否刷新通用字典缓存");
			return Lists.newArrayList();
		} 
		List<Dto> dictList=Lists.newArrayList();
		for (String dicString : dictRedisList) {
			dictList.add(WebplusJson.fromJson(dicString, HashDto.class));
		}
		return dictList;
	}
	
	/**
	 * 
	 * 简要说明：转换分页
	 * 编写者：陈骑元
	 * 创建时间：2018年12月17日 下午8:15:29
	 * @param 说明
	 * @return 说明
	 */
	public static void convertItem(Page<?> page){
		if(WebplusUtil.isNotEmpty(page)){
			convertItem(page.getRecords());
		}
		
	}
	/**
	 * 
	 * 简要说明：转换字典
	 * 编写者：陈骑元
	 * 创建时间：2018年12月17日 下午7:02:43
	 * @param 说明
	 * @return 说明
	 */
	public static void convertItem(List<?> list){
		
		if(WebplusUtil.isNotEmpty(list)){
			Object dictBean=list.get(0);
			Dto itemDto=getConvertItemData(dictBean);
			for (Object bean : list) {
				convertItem(bean,itemDto);
	        }
		}
	}
	/**
	 * 
	 * 简要说明：     
	 * 编写者：陈骑元
	 * 创建时间：2018年12月18日 下午9:48:00
	 * @param 说明
	 * @return 说明
	 */
	public static void convertItem(Object  bean){
		
		Dto itemDto=getConvertItemData(bean);
		convertItem(bean, itemDto);
	}
	
	/**
	 * 
	 * 简要说明：转换map字典
	 * 编写者：陈骑元
	 * 创建时间：2018年12月18日 下午9:47:55
	 * @param 说明
	 * @return 说明
	 */
    @SuppressWarnings("rawtypes")
	public static void convertMapDict(List<? extends Map> listMap,Dto keyDto){
		Dto itemDto=getItemDto(keyDto);
		for(Map<?,?> dataMap:listMap){
			convertItem(dataMap,keyDto,itemDto);
		}
		
	}
	/**
	 * 
	 * 简要说明：转换map字典
	 * 编写者：陈骑元
	 * 创建时间：2018年12月18日 下午9:47:55
	 * @param 说明
	 * @return 说明
	 */
    public static void convertMapItem(Map<?, ?> dataMap,Dto keyDto){
		Dto itemDto=getItemDto(keyDto);
		convertItem(dataMap,keyDto,itemDto);
	}
    /**
     * 
     * 简要说明：转换字典
     * 编写者：陈骑元
     * 创建时间：2018年12月18日 下午10:04:19
     * @param 说明
     * @return 说明
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	private  static void convertItem(Map dataMap,Dto keyDto,Dto itemDto){
    	Set<String> keySet=keyDto.keySet();
		for(String key:keySet){
			if(WebplusUtil.isNotEmpty(key)){
				String dictValue="";
				Object value=dataMap.get(key);
				if(WebplusUtil.isNotEmpty(value)){
					 String typeCode=keyDto.getString(key);
					 dictValue=getItemName(typeCode,value.toString(),itemDto);
				 }
				 dataMap.put(key+"_dict", dictValue);
			}
				
		}
    	
    	
    }
    /**
     * 
     * 简要说明：返回字典
     * 编写者：陈骑元
     * 创建时间：2018年12月18日 下午9:52:50
     * @param 说明
     * @return 说明
     */
    private static Dto getItemDto(Dto keyDto){
    	Dto dictDto=Dtos.newDto();
    	if(WebplusUtil.isNotEmpty(keyDto)){
    		Set<String> keySet=keyDto.keySet();
    		for(String key:keySet){
    			String typeCode=keyDto.getString(key);
    			
    			if(WebplusUtil.isNotEmpty(typeCode)){
    			 dictDto.putAll(getItemDto(typeCode));
    			  
    			}
    				
    		}
    	}
    	return dictDto;
    	
    }
	
	/**
	 * 
	 * 简要说明：转换字典
	 * 编写者：陈骑元
	 * 创建时间：2018年12月17日 下午7:02:43
	 * @param 说明
	 * @return 说明
	 */
	@SuppressWarnings("rawtypes")
	public static void convertItem(Object  bean,Dto itemDto){
		  if (WebplusUtil.isEmpty(bean)) {
	            return;
	        }
	        
	        if(!(bean instanceof BaseModel)){
	        	throw new RuntimeException("指定的pojo"+bean.getClass()+" 不能获取数据字典，需要继承BaseModel");
	        }
	        try {
	        BaseModel ext  = (BaseModel)bean;
	        Class c = ext.getClass();
	        do {
	            Field[] fields = c.getDeclaredFields();
	            for (Field field : fields) {
	                if (field.isAnnotationPresent(ItemTag.class)) {
	                    field.setAccessible(true);
	                    ItemTag dictTag = field.getAnnotation(ItemTag.class);
	                    String typeCode=dictTag.type();
	                    if(WebplusUtil.isNotEmpty(dictTag)){
	                    	   try {
	   	                        String display = "";
	   	                        Object fieldValue = field.get(ext);
	   	                        if (WebplusUtil.isNotEmpty(fieldValue)) {
	   	                           display=getItemName(typeCode,fieldValue.toString(),itemDto);
	   	                        }
	   	                      
	   	                        ext.set(field.getName()+"_" + dictTag.suffix(), display);
	   	                    } catch (Exception e) {
	   	                        e.printStackTrace();
	   	                    }
	   	    
	                    }
	                 
	                }
	                if (field.isAnnotationPresent(JsonFormat.class)) {
						field.setAccessible(true);
						JsonFormat jsonFormat = field.getAnnotation(JsonFormat.class);
						String pattern = jsonFormat.pattern();
						if (WebplusUtil.isEmpty(pattern)) {
							pattern = WebplusCons.DATETIME;
						}
						Object fieldValue = field.get(ext);
						String dateValue="";
						if(WebplusUtil.isNotEmpty(fieldValue)) {
							if(fieldValue  instanceof Date) {
								dateValue=WebplusUtil.date2String((Date)fieldValue, pattern);
							}
							
						}
						ext.set(field.getName()+"_format", dateValue);
					}
	            }
	         c = c.getSuperclass();
	        }while(c!=BaseModel.class);
	        } catch (Exception e) {
				// TODO: handle exception
			}
	}
	
	/**
	 * 
	 * 简要说明：获取字典DICT集合Dto 使用键值typeCode_itemCode 组成
	 * 编写者：陈骑元
	 * 创建时间：2018年12月17日 下午7:43:06
	 * @param 说明
	 * @return 说明
	 */
	public static Dto getItemDto(String typeCode){
		Dto itemDto=Dtos.newDto();
		List<Item> itemList=getItemList(typeCode);
		for(Item item:itemList){
			String key=item.getTypeCode()+"_"+item.getItemCode();
			itemDto.put(key, item.getItemName());
		}
			
		
		return itemDto;
	}
	/**
	 * 
	 * 简要说明：解析字典
	 * 编写者：陈骑元
	 * 创建时间：2018年12月17日 下午7:53:36
	 * @param 说明
	 * @return 说明
	 */
   private static String getItemName(String typeCode,String itemCode,Dto itemDto){
	   String itemName="";
	   if(WebplusUtil.isNotEmpty(itemCode)){
		   String[] codeArray=itemCode.split(",");
		   for(String code:codeArray){
			   String key= typeCode+"_"+code;
			 
			   if(itemDto.containsKey(key)){
				   itemName+=itemDto.getString(key)+",";
			   }else{
				   itemName+=itemCode+",";
			   }
		   }
	   }
	   if(WebplusUtil.isNotEmpty(itemName)){
		   itemName=itemName.substring(0, itemName.length()-1);
	   }
	   return itemName;
   }
	/**
	 * 
	 * 简要说明：获取转换好的字典数据
	 * 编写者：陈骑元
	 * 创建时间：2018年12月17日 下午7:32:59
	 * @param 说明
	 * @return 说明
	 */
	@SuppressWarnings("rawtypes")
	private static Dto getConvertItemData(Object  bean){
		Dto dictDto=Dtos.newDto();
		if (WebplusUtil.isEmpty(bean)) {
            return dictDto;
        }
        
        if(!(bean instanceof BaseModel)){
        	return dictDto;
        }
        
        BaseModel ext  = (BaseModel)bean;
        Class c = ext.getClass();
        do {
            Field[] fields = c.getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(ItemTag.class)) {
                    field.setAccessible(true);
                    ItemTag dictTag = field.getAnnotation(ItemTag.class);
                    String type=dictTag.type();
                    if(WebplusUtil.isNotEmpty(type)){
                    	dictDto.putAll(getItemDto(type));
                    }
                }
            }
         c = c.getSuperclass();
        }while(c!=BaseModel.class);
        
        return dictDto;
	}
	
	/**
	 * 
	 * 简要说明：创建token，并返回token字符串 
	 * 编写者：陈骑元 
	 * 创建时间：2018年12月22日 下午1:58:54
	 * @param 说明
	 *     tokenJson存储token数据的json数据串
	 * @return 说明
	 */
	public static String createToken(String token,String tokenJson) {
		
		String tokenKey = WebplusCons.CACHE_PREFIX.TOKEN + token;
		webplusJedis.setString(tokenKey, tokenJson, WebplusCons.DEFAULT_TIMECOUT);
		return token;
	}

	/**
	 * 
	 * 简要说明：创建token，并返回token字符串 编写者：陈骑元 创建时间：2018年12月22日 下午1:58:54
	 * 
	 * @param 说明
	 *            tokenJson存储token数据的json数据串
	 * @return 说明
	 */
	public static String createToken(UserToken userToken) {
		String token =WebplusUtil.uuid();
		userToken.setToken(token);
		String tokenJson = WebplusJson.toJson(userToken);
		return createToken( token,tokenJson);
	}

	/**
	 * 
	 * 简要说明：根据token 获取token 存储值 编写者：陈骑元 创建时间：2018年12月22日 下午1:58:54
	 * 
	 * @param
	 * @return 说明
	 */
	public static String getToken(String token) {
		String tokenKey = WebplusCons.CACHE_PREFIX.TOKEN + token;
		String jsonStr = webplusJedis.getString(tokenKey);
		logger.info("当前token：" + token + ",返回值：" + jsonStr);
		return jsonStr;
	}

	/**
	 * 
	 * 简要说明：创建token，并返回token字符串 编写者：陈骑元 创建时间：2018年12月22日 下午1:58:54
	 * 
	 * @param 说明
	 *            tokenJson存储token数据的json数据串
	 * @return 说明
	 */
	public static UserToken getUserToken(String token) {
		UserToken userToken = null;
		String tokenJson = getToken(token);

		if (WebplusUtil.isNotEmpty(tokenJson)) {
			userToken = WebplusJson.fromJson(tokenJson, UserToken.class);

		} else {
			logger.error("当前token：" + token + ",无法获取用户信息");
		}
		return userToken;
	}

	/**
	 * 
	 * 简要说明：刷新token的值 编写者：陈骑元 创建时间：2018年12月22日 下午2:32:48
	 * 
	 * @param 说明
	 * @return 说明
	 */
	public static boolean checkAndRefreshToken(String token) {
		boolean result = false;

		String tokenKey = WebplusCons.CACHE_PREFIX.TOKEN + token;
		if (webplusJedis.exists(tokenKey)) {

			webplusJedis.expire(tokenKey, WebplusCons.DEFAULT_TIMECOUT);
			result = true;
		}

		return result;
	}

	/**
	 * 
	 * 简要说明：清空token 编写者：陈骑元 创建时间：2018年12月24日 下午9:22:15
	 * 
	 * @param 说明
	 * @return 说明
	 */
	public static void removeToken(String token) {
		String tokenKey = WebplusCons.CACHE_PREFIX.TOKEN + token;
		webplusJedis.delString(tokenKey);
	}
	
	/**
	 * 
	 * 简要说明：获取redis自增序列 编写者：陈骑元 创建时间：2018年12月24日 下午3:13:50
	 * 
	 * @param 说明
	 * @return 说明
	 */
	public static String createOrderNum() {
		
		String middle = WebplusUtil.getDateStr("yyMMdd");
		try {

			String redisKey = WebplusCons.CACHE_PREFIX.SEQ ;
			Long incr = webplusJedis.incr(redisKey);
			if (incr >= 999999) {
				webplusJedis.getSet(redisKey, "0");
			}
			String end = StringUtils.leftPad(incr + "", 6, "0");
			String seqNumStr = middle + end;
			return seqNumStr;
		} catch (Exception e) {
			
			return WebplusUtil.getDateStr("yyMMddHHmmss");

		}

	}
	

	/**
	 * 
	 * 简要说明：获取
	 * 编写者：陈骑元（chenqiyuan@toonan.com）
	 * 创建时间： 2020年5月10日 上午11:57:57 
	 * @param 说明
	 * @return 说明
	 */
	public static String createFoodNum(String shopId) {
		
		String middle = WebplusUtil.getDateStr("yyMMdd");
		try {

			String redisKey = WebplusCons.CACHE_PREFIX.SEQ+shopId+":"+middle+"" ;
			Long incr = webplusJedis.incr(redisKey);
			if (incr==1) {
				webplusJedis.expire(redisKey,24*3600);
			}
			String seq= StringUtils.leftPad(incr + "", 4, "0");
			
			return seq;
		} catch (Exception e) {
			
			return WebplusUtil.randomBetween(1000,2000)+"";

		}

	}
    /**
     * 
     * 简要说明：获取字符串
     * 编写者：陈骑元
     * 创建时间：2019年8月4日 下午2:13:48
     * @param 说明
     * @return 说明
     */
	public static String getString(String key){
		
		return webplusJedis.getString(key);
	}
	/**
	 * 
	 * 简要说明：
	 * 编写者：陈骑元
	 * 创建时间：2019年8月4日 下午2:14:23
	 * @param 说明
	 * @return 说明
	 */
    public static void setString(String key, String value, int timeout){
		
		 webplusJedis.setString(key, value, timeout);
	}
    
    /**
	 * 
	 * 简要说明：删除key
	 * 编写者：陈骑元
	 * 创建时间：2019年8月4日 下午2:14:23
	 * @param 说明
	 * @return 说明
	 */
    public static void delString(String key){
		
		 webplusJedis.delString(key);
	}
    /**
     * 检查键是否存在
     * @param key
     * @return
     */
    public static boolean exists(String key) {
    	
    	return webplusJedis.exists(key);
    }
}
