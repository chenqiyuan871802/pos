package com.ims.buss.util;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ims.buss.constant.BussCons;
import com.ims.buss.model.LanguagePack;
import com.ims.buss.model.Position;
import com.ims.buss.service.LanguagePackService;
import com.ims.buss.service.PositionService;
import com.ims.core.cache.WebplusCache;
import com.ims.core.constant.WebplusCons;
import com.ims.core.matatype.Dto;
import com.ims.core.matatype.Dtos;
import com.ims.core.redis.WebplusJedis;
import com.ims.core.util.WebplusJson;
import com.ims.core.util.WebplusSpringContext;
import com.ims.core.util.WebplusUtil;
import com.ims.core.vo.Item;


/**
 * 
 * 类名:com.ims.buss.util.BussCache
 * 描述:业务缓存处理
 * 编写者:陈骑元
 * 创建时间:2019年8月5日 下午12:15:24
 * 修改说明:
 */
public class BussCache {
	
	private static Log log = LogFactory.getLog(BussCache.class);
	
	/**
	 * jedis帮助
	 */
	private static LanguagePackService languagePackService = WebplusSpringContext.getBean("languagePackService");
	/**
	 * 地址实体类
	 */
	private static PositionService positionService = WebplusSpringContext.getBean("positionService");
	
	/**
	 * jedis帮助
	 */
	private static WebplusJedis webplusJedis = WebplusSpringContext.getBean("webplusJedis");
	
	
	
	/**
	 * 
	 * 简要说明：缓存所有语言
	 * 编写者：陈骑元
	 * 创建时间：2019年8月5日 下午12:28:46
	 * @param 说明
	 * @return 说明
	 */
	public static void cacheAllLanguage(){
		List<Item> itemList=WebplusCache.getItemList(BussCons.CACHE_LANGUAGE_TYPE_KEY);
		for(Item item:itemList){
			String languageType=item.getItemCode();
		    flushLanguage(languageType); //先进行清空
		    List<LanguagePack> languagePackList=getLanguagePackByDB(languageType);
		    Map<String, String> cacheMap = Maps.newHashMap();
			for (LanguagePack languagePack : languagePackList) {
				cacheMap.put(languagePack.getLanguageCode(),WebplusJson.toJson(languagePack));
			}
			if(WebplusUtil.isNotEmpty(cacheMap)){
				webplusJedis.hmset(BussCons.CACHE_LANGUAGE_KEY+languageType, cacheMap);
			}
		}
	}
	/**
	 * 
	 * 简要说明：根据语言代码获取语言值
	 * 编写者：陈骑元
	 * 创建时间：2019年8月6日 下午8:38:59
	 * @param 说明
	 * @return 说明
	 */
	public static String getLanguageValue(String languageType,String languageCode){
		String languageJson = webplusJedis.hget(BussCons.CACHE_LANGUAGE_KEY + languageType, languageCode);

		if (WebplusUtil.isNotEmpty(languageJson)) {
			LanguagePack languagePack = WebplusJson.fromJson(languageJson, LanguagePack.class);
			if (WebplusUtil.isNotEmpty(languagePack)) {

				return languagePack.getLanguageValue();
			}
		}

		EntityWrapper<LanguagePack> wrapper = new EntityWrapper<LanguagePack>();
		wrapper.eq("language_code", languageCode);
		wrapper.eq("language_type", languageType);
		wrapper.eq("status", WebplusCons.ENABLED_YES);
		LanguagePack languagePack = languagePackService.selectOne(wrapper);
		if (WebplusUtil.isNotEmpty(languagePack)) {

			return languagePack.getLanguageValue();
		}
		log.error("系统没有找到该语言类型languageType=" + languageType + "，语言代码languageCode=" + languageCode + "的对照值");
		return "";
	}
	/**
	 * 
	 * 简要说明：返回
	 * 编写者：陈骑元
	 * 创建时间：2019年8月6日 下午8:55:52
	 * @param 说明
	 * @return 说明
	 */
	public static List<Dto> getLanguagePack(String languageType){
		List<Dto> dataList=Lists.newArrayList();
		List<LanguagePack> languagePackList=getCacheLanguagePack(languageType);
		for(LanguagePack languagePack:languagePackList){
			Dto dataDto=Dtos.newDto();
			dataDto.put("languageCode", languagePack.getLanguageCode());
			dataDto.put("languageValue", languagePack.getLanguageValue());
			dataList.add(dataDto);
		}
			
		return dataList;
	}
	/**
	 * 
	 * 简要说明：获取对应语言包
	 * 编写者：陈骑元
	 * 创建时间：2019年8月6日 上午8:59:01
	 * @param 说明
	 * @return 说明
	 */
	public static List<LanguagePack> getCacheLanguagePack(String languageType){
		
		Map<String,String> cacheMap=webplusJedis.hgetAll(BussCons.CACHE_LANGUAGE_KEY+languageType);
		List<LanguagePack> languagePackList=Lists.newArrayList();
		if(WebplusUtil.isNotEmpty(cacheMap)){
		     for(String key:cacheMap.keySet()){
		    	 String value=cacheMap.get(key);
		    	 if(WebplusUtil.isNotEmpty(value)){
		    		 LanguagePack languagePack=WebplusJson.fromJson(value, LanguagePack.class);
		    		 languagePackList.add(languagePack);
		    	 }
		     }
		}
		if(WebplusUtil.isEmpty(languagePackList)){  //如果等于空查询数据库,并缓存到redis数据库
			languagePackList=getLanguagePackByDB(languageType);
			Map<String, String> cacheMapNew = Maps.newHashMap();
			for (LanguagePack languagePack : languagePackList) {  
				cacheMapNew.put(languagePack.getLanguageCode(),WebplusJson.toJson(languagePack));
			}
			if(WebplusUtil.isNotEmpty(cacheMapNew)){
				webplusJedis.hmset(BussCons.CACHE_LANGUAGE_KEY+languageType, cacheMapNew);
			}
		}
		return languagePackList;
	}
	
	
	
	/**
	 * 
	 * 简要说明：从数据获取对应语言包
	 * 编写者：陈骑元
	 * 创建时间：2019年8月5日 下午12:31:18
	 * @param 说明
	 * @return 说明
	 */
	public static List<LanguagePack> getLanguagePackByDB(String languageType){
		Dto pDto =Dtos.newDto();
		pDto.put("status", WebplusCons.ENABLED_YES);
		pDto.put("languageType", languageType);
		List<LanguagePack> languagePackList=languagePackService.list(pDto);
		return languagePackList;
	}
	/**
	 * 
	 * 简要说明：清除语言缓存
	 * 编写者：陈骑元
	 * 创建时间：2019年8月5日 下午12:21:30
	 * @param 说明
	 * @return 说明
	 */
	public static void flushLanguage(String languageType){
		Set<String> keySet=webplusJedis.keys(BussCons.CACHE_LANGUAGE_KEY+languageType+"*");
		for(String key:keySet){
			webplusJedis.delString(key);
		}
	}
	
	/**
	 * 
	 * 简要说明：获取位置
	 * 编写者：陈骑元（chenqiyuan@toonan.com）
	 * 创建时间： 2020年4月25日 上午9:50:21 
	 * @param 说明
	 * @return 说明
	 */
	public static List<Item> getPositionList(String typeCode){
		List<Item> itemList=Lists.newArrayList();
		if(WebplusUtil.isEmpty(typeCode)) {
			typeCode=BussCons.POSITION_TOP_CODE;
		}
		String key=WebplusCons.POSITION_KEY+typeCode;
		List<String> dictRedisList = webplusJedis.lrange(key, 0, -1);
		if (WebplusUtil.isNotEmpty(dictRedisList)) {
			for (String dictString : dictRedisList) {
				if(WebplusUtil.isNotEmpty(dictString)) {
					itemList.add(WebplusJson.fromJson(dictString, Item.class));
				}
			}
		}
		if(WebplusUtil.isEmpty(itemList)) {
			itemList=getPositionByDB(typeCode);
			if(WebplusUtil.isNotEmpty(itemList)) {
				webplusJedis.rpush(key, itemList);
			}else {
				log.error("系统没有找到typeCode="+typeCode+"的的位置信息");
			}
		}
		return itemList;
	}
	/**
	 * 
	 * 简要说明：通过上下级获取省市区地址
	 * 编写者：陈骑元（chenqiyuan@toonan.com）
	 * 创建时间： 2020年4月25日 上午9:35:08 
	 * @param 说明
	 * @return 说明
	 */
	public static List<Item> getPositionByDB(String parentCode) {
		List<Item> itemList=Lists.newArrayList();
		Dto pDto=Dtos.newDto();
		if(WebplusUtil.isEmpty(parentCode)) {
			parentCode=BussCons.POSITION_TOP_CODE;
		}
		pDto.put("parentCode", parentCode);
		pDto.setOrder("position_id ASC ");
		List<Position> positionList=positionService.list(pDto);
		for(Position position:positionList) {
			Item item=new Item();
			item.setItemCode(position.getPositionCode());
			item.setItemName(position.getPositionName());
			item.setTypeCode(position.getParentCode());
			itemList.add(item);
		}
		return itemList;
	}
	/**
	 * 
	 * 简要说明：刷新区域缓存
	 * 编写者：陈骑元（chenqiyuan@toonan.com）
	 * 创建时间： 2020年4月27日 上午12:27:11 
	 * @param 说明
	 * @return 说明
	 */
	public static void refreshPosition() {
		String key=WebplusCons.CACHE_PREFIX.DICT+WebplusCons.POSITION_DICT;
		webplusJedis.delString(key);
		List<Position> positionList=positionService.list(Dtos.newDto());
		for(Position position:positionList) {
			Item item=new Item();
			item.setItemCode(position.getPositionCode());
			item.setItemName(position.getPositionName());
			item.setTypeCode(WebplusCons.POSITION_DICT);
			webplusJedis.rpush(key, WebplusJson.toJson(item));
		}
	}

}
