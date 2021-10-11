package com.ims.system.util;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.google.common.collect.Lists;
import com.ims.core.cache.WebplusCache;
import com.ims.core.constant.WebplusCons;
import com.ims.core.redis.WebplusJedis;
import com.ims.core.util.WebplusSpringContext;
import com.ims.core.util.WebplusUtil;
import com.ims.core.vo.TreeModel;
import com.ims.system.constant.SystemCons;
import com.ims.system.model.Dict;
import com.ims.system.model.Menu;
import com.ims.system.model.Param;
import com.ims.system.service.SystemCacheService;

/**
 * 
 * 类名:com.toonan.system.util.SystemCache
 * 描述:系统缓存提供
 * 编写者:陈骑元
 * 创建时间:2019年4月28日 上午10:00:44
 * 修改说明:
 */
public class SystemCache {
	

	private static SystemCacheService systemCacheService = WebplusSpringContext.getBean("systemCacheService");
	/**
	 * jedis帮助
	 */
	private static WebplusJedis webplusJedis = WebplusSpringContext.getBean("webplusJedis");
	

	/**
	 * 
	 * 简要说明：根据参数进行缓存并返回
	 * 编写者：陈骑元
	 * 创建时间：2019年4月28日 下午10:10:51
	 * @param 说明
	 * @return 说明
	 */
	public static Param cacheParam(String paramKey) {
            
       return systemCacheService.cacheParam(paramKey);
		
	}
	/**
	 * 
	 * 简要说明：根据参数键移除参数缓存
	 * 编写者：陈骑元
	 * 创建时间：2017年1月24日 上午10:22:18
	 * 
	 * @param 说明
	 * @return 说明
	 */
	public static void removeCacheParam(String paramKey) {

       systemCacheService.removeCacheParam(paramKey);
         
		
	}
	
	/**
	 * 
	 * 简要说明：刷新所有缓存
	 * 编写者：陈骑元
	 * 创建时间：2017年1月24日 上午10:22:18
	 * 
	 * @param 说明
	 * @return 说明
	 */
	public static void refreshParam() {
		
		systemCacheService.cacheAllParam();
		
	}
	/**
	 * 
	 * 简要说明：根据参数键移除参数缓存
	 * 编写者：陈骑元
	 * 创建时间：2017年1月24日 上午10:22:18
	 * 
	 * @param 说明
	 * @return 说明
	 */
	public static void flushParam() {
		
		systemCacheService.flushParam();
		
	}
	
	/**
	 * 
	 * 简要说明：获取缓存的字典
	 * 编写者：陈骑元
	 * 创建时间：2018年5月2日 下午10:57:15
	 * @param 说明
	 * @return 说明
	 */
	public static List<Dict> getCacheDict(String dictKey) {
		
		List<Dict> dictList=systemCacheService.getCacheDict(dictKey);
		return dictList;
		
	}
	/**
	 * 
	 * 简要说明：缓存所有字典
	 * 编写者：陈骑元
	 * 创建时间：2018年5月2日 下午10:57:15
	 * @param 说明
	 * @return 说明
	 */
	public static List<Dict> cacheDict(String dictKey) {
		
		return systemCacheService.cacheDict(dictKey);
		
	}
	
	
	/**
	 * 
	 * 简要说明：缓存所有字典
	 * 编写者：陈骑元
	 * 创建时间：2018年5月2日 下午10:57:15
	 * @param 说明
	 * @return 说明
	 */
	public static void cacheAllDict() {
		
		systemCacheService.cacheAllDict();
		
	}
	/**
	 * 
	 * 简要说明：删除缓存字典
	 * 编写者：陈骑元
	 * 创建时间：2018年5月2日 下午10:57:15
	 * @param 说明
	 * @return 说明
	 */
	public static void  removeCacheDict(String dictKey) {
		
		systemCacheService.removeCacheDict(dictKey);
	}
	/**
	 * 
	 * 简要说明：清空缓存字典
	 * 编写者：陈骑元
	 * 创建时间：2018年5月2日 下午10:57:15
	 * @param 说明
	 * @return 说明
	 */
	public static void  flushDict() {
		
		systemCacheService.flushDict();
	}

	/**
	 * 
	 * 简要说明：获取缓存菜单 编写者：陈骑元 创建时间：2018年12月29日 下午11:43:32
	 * 
	 * @param 说明
	 * @return 说明
	 */
	public static List<Menu> getCacheRoleMenu(String userId) {
		String roleMenuSwitch = WebplusCache.getParamValue(SystemCons.ROLE_MENU_SWITCH_KEY);
		if (WebplusCons.SWITCH_ON.equals(roleMenuSwitch)) {
			return systemCacheService.getCacheRoleMenu(userId);
		} else {
			return getRoleMenuByAllMenu(userId);
		}

	}

	/**
	 * 
	 * 简要说明：获取看片菜单 
	 * 编写者：陈骑元
	 *  创建时间：2019年1月7日 下午8:51:59
	 * 
	 * @param 说明
	 * @return 说明
	 */
	public static List<TreeModel> getCardMenu(String userId, String menuType,String wheherSuper) {
		TreeModel rootMode = new TreeModel();
		rootMode.setId(SystemCons.TREE_ROOT_ID);
		List<Menu> menuList =Lists.newArrayList();
		if(WebplusCons.WHETHER_YES.equals(wheherSuper)){  //如果是超级管理员具有所有的菜单权限
			
			menuList = getCacheMenu(); 
		}else{
		 menuList = getCacheRoleMenu(userId);
		}
		
		for (Menu menu : menuList) {
			String menuTypeNew = menu.getMenuType();
			if (WebplusUtil.isEmpty(menuType)||SystemCons.MENU_TYPE_SYSTEM.equals(menuTypeNew) || menuTypeNew.equals(menuType)) {

				String modelType = menu.getModuleType();
				if (!SystemCons.MODULE_TYPE_BUTTON.equals(modelType)) {
					TreeModel menuMode = new TreeModel();
					menuMode.setId(menu.getMenuId());
					menuMode.setName(menu.getMenuName());
					if (SystemCons.MODULE_TYPE_PARENT.equals(modelType)) {
						menuMode.setParent(true);
					} else {
						menuMode.setParent(false);
					}
					menuMode.setpId(menu.getParentId());
					if(WebplusUtil.isNotEmpty(menu.getUrl())&&!menu.getUrl().startsWith("/")&&!menu.getUrl().startsWith("http:")&&!menu.getUrl().startsWith("https:")){
						menu.setUrl(menu.getUrl());//url如果不是以'/'开头的，则加上'/'
					}
					menuMode.setUrl(menu.getUrl());
					menuMode.setIcon(menu.getIconName());
					if (WebplusCons.WHETHER_YES.equals(menu.getIsAutoExpand())) {
						menuMode.setOpen(true);// 展开节点
					} else {
						menuMode.setOpen(false);// 展开节点
					}
					rootMode.add(menuMode);
				}
			}
		}

		return rootMode.getChildren();

	}

	/**
	 * 
	 * 简要说明：更具用户编号获取授权菜单 编写者：陈骑元 创建时间：2019年1月8日 上午10:53:34
	 * 
	 * @param 说明
	 * @return 说明
	 */
	public static List<String> listRoleMenuId(String userId) {

		return systemCacheService.listRoleMenuId(userId);
	}

	/**
	 * 
	 * 简要说明：获取所有菜单 编写者：陈骑元 创建时间：2019年1月8日 上午10:53:34
	 * 
	 * @param 说明
	 * @return 说明
	 */
	public static List<Menu> getCacheMenu() {

		return systemCacheService.getCacheMenu();
	}

	/**
	 * 
	 * 简要说明：从所有菜单中过滤 编写者：陈骑元 创建时间：2019年1月8日 上午10:55:49
	 * 
	 * @param 说明
	 * @return 说明
	 */
	@SuppressWarnings("unchecked")
	public static List<Menu> getRoleMenuByAllMenu(String userId) {
		List<Menu> menuList = getCacheMenu();
		List<String> menuIdList = listRoleMenuId(userId);
		List<Menu> roleMenuList = Lists.newArrayList();
		for (Menu menu : menuList) {
			String menuId = menu.getMenuId();
			if (WebplusUtil.contains(menuId, menuIdList)) {
				roleMenuList.add(menu);
			}
		}
		roleMenuList = WebplusUtil.removeRepeat(roleMenuList);
		return roleMenuList;
	}

	/**
	 * 
	 * 简要说明：刷新角色菜单 编写者：陈骑元 创建时间：2018年5月1日 下午11:09:19
	 * 
	 * @param 说明
	 * @return 说明
	 */
	public static void flushRoleMenu() {

		Set<String> keySet = webplusJedis.keys(WebplusCons.CACHE_PREFIX.ROLEMENU + "*");
		for (String key : keySet) {
			webplusJedis.delString(key);
		}

	}
	/**
	 * 
	 * 简要说明：获取认证的权限 
	 * 编写者：陈骑元
	 * 创建时间：2019年1月8日 下午1:41:07
	 * @param 说明
	 * @return 说明
	 */
	public static Set<String> getAuthPermissions(String userId,String whetherSuper) {
		
		Set<String> permissionsSet = new HashSet<String>();
		List<Menu> menuList=Lists.newArrayList();
		if(WebplusCons.WHETHER_YES.equals(whetherSuper)){  //超级管理员具有所有权限
			menuList =  getCacheMenu();
		}else{
			menuList = getCacheRoleMenu(userId);
		}
		for (Menu menu : menuList) {
			String menuCode = menu.getMenuCode();
			if (WebplusUtil.isNotEmpty(menuCode)) {
				if(menuCode.indexOf(":")>-1){  //存在冒号的不需要处理
					permissionsSet.add(menuCode);
				}
				
			}
		}
		return permissionsSet;
	}
	
	/**
	 * 
	 * 简要说明：移除菜单缓存 
	 * 编写者：陈骑元 
	 * 创建时间：2018年5月1日 下午11:09:19
	 * @param 说明
	 * @return 说明
	 */
	public static void romveCacheMenu() {
		webplusJedis.delString(WebplusCons.CACHE_PREFIX.MENU);

	}
	
	
	
	
	

}
