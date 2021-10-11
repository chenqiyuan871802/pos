package com.ims.buss.system;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.baomidou.mybatisplus.plugins.Page;
import com.google.common.collect.Lists;
import com.ims.core.cache.WebplusCache;
import com.ims.core.constant.WebplusCons;
import com.ims.core.matatype.Dto;
import com.ims.core.matatype.Dtos;
import com.ims.core.util.WebplusFormater;
import com.ims.core.util.WebplusUtil;
import com.ims.core.vo.R;
import java.util.List;

import com.ims.buss.constant.BussCons;
import com.ims.buss.model.BuffetMenu;
import com.ims.buss.model.MenuCatalog;
import com.ims.buss.model.MenuDict;
import com.ims.buss.service.BuffetMenuService;
import com.ims.buss.service.BuffetSpecService;
import com.ims.buss.service.BussCommonService;
import com.ims.buss.service.MenuCatalogService;

import org.springframework.stereotype.Controller;
import com.ims.core.web.BaseController;

/**
 * <p>
 * 自助餐菜单关联表 前端控制器
 * </p>
 *
 * @author 陈骑元
 * @since 2019-08-28
 */
@Controller
@RequestMapping("/buss/buffetCatalog")
public class BuffetCatalogController extends BaseController {

    @Autowired
    private BuffetMenuService buffetMenuService;
    
    @Autowired
    private MenuCatalogService menuCatalogService;
    @Autowired
    private BussCommonService bussCommonService;
    @Autowired
    private BuffetSpecService buffetSpecService;

	/**
	 * 
	 * 简要说明：分页查询 
	 * 编写者：陈骑元
	 * 创建时间：2019-08-28
	 * @param 说明
	 * @return 说明
	 */
	@RequestMapping("list")
	@ResponseBody
	public R list() {
		Dto pDto = Dtos.newDto(request);
		String userId=this.getUserId();
		pDto.put("shopId", userId);
		pDto.put("whetherBuffet", WebplusCons.WHETHER_YES);
		pDto.put("languageType", BussCons.LANGUAGE_TYPE_JP);
		pDto.setOrder(" sort_no ASC");
		Page<MenuCatalog> page =menuCatalogService.likePage(pDto);
		WebplusCache.convertItem(page);
		return R.toPage(page);
	}
	/**
	 * 
	 * 简要说明：加载菜品
	 * 编写者：陈骑元
	 * 创建时间：2019-08-28
	 * @param 说明
	 * @return 说明
	 */
	@RequestMapping("listMenuDict")
	@ResponseBody
	public R listMenuDict() {
		Dto pDto = Dtos.newDto(request);
		String userId=this.getUserId();
		List<String> menuTypeList=Lists.newArrayList();
		menuTypeList.add(BussCons.MENU_TYPE_COMMON);
		menuTypeList.add(BussCons.MENU_TYPE_BUFFET);
		List<String> catalogTypeList=Lists.newArrayList();
		catalogTypeList.add(BussCons.CATALOG_TYPE_BESIDES);
		catalogTypeList.add(BussCons.CATALOG_TYPE_EATIN);
		pDto.put("menuTypeList", menuTypeList);
		pDto.put("catalogTypeList", catalogTypeList);
		pDto.put("shopId", userId);
		pDto.put("whetherBuffet", WebplusCons.WHETHER_NO);
		pDto.put("languageType", BussCons.LANGUAGE_TYPE_JP);
		pDto.setOrder(" b.sort_no ASC,a.sort_no ASC");
		Page<MenuDict> page =bussCommonService.listMenuDictPage(pDto);
		return R.toPage(page);
	}


	/**
	 * 
	 * 简要说明： 保存自助餐菜单信息
	 * 编写者：陈骑元
	 * 创建时间：2019-08-28
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("saveBuffetMenu")
	@ResponseBody
	public R saveBuffetMenu(String catalogIndexId,String menuIndexIds) {
		String userId=this.getUserId();
	    List<String> menuIndexIdList=WebplusFormater.separatStringToList(menuIndexIds);
	    List<BuffetMenu> buffetMenuList=Lists.newArrayList();
	    for(String menuIndexId:menuIndexIdList){
	    	BuffetMenu buffetMenu=new BuffetMenu();
	    	buffetMenu.setCatalogIndexId(catalogIndexId);
	    	buffetMenu.setMenuIndexId(menuIndexId);
	    	buffetMenu.setCreateBy(userId);
	    	buffetMenu.setCreateTime(WebplusUtil.getDateTime());
	    	buffetMenuList.add(buffetMenu);
	    }
	    if(WebplusUtil.isNotEmpty(buffetMenuList)){
	    	boolean result=buffetMenuService.insertBatch(buffetMenuList);
	    	if (result) {
				return R.ok();
			} 
	    }
		return R.error();
	}

	/**
	 * 
	 * 简要说明：删除自助餐信息
	 * 编写者：陈骑元
	 * 创建时间：2019-08-28
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("removeBuffetMenu")
	@ResponseBody
	public R removeBuffetMenu(String catalogIndexId,String menuIndexIds) {
	    List<String> menuIndexIdList=WebplusFormater.separatStringToList(menuIndexIds);
	    for(String menuIndexId:menuIndexIdList){
	    	Dto removeDto=Dtos.newDto();
	    	removeDto.put("catalog_index_id", catalogIndexId);
	    	removeDto.put("menu_index_id", menuIndexId);
	    	buffetMenuService.deleteByMap(removeDto);
	    }
		
	    return R.ok();
	}
	/**
	 * 
	 * 简要说明：
	 * 编写者：陈骑元
	 * 创建时间：2019年8月30日 下午10:35:11
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("listMenuCatalog")
	@ResponseBody
	public R listMenuCatalog(){
		Dto pDto = Dtos.newDto(request);
		String userId=this.getUserId();
		pDto.put("shopId", userId);
		pDto.put("whetherBuffet", WebplusCons.WHETHER_NO);
		pDto.put("languageType", BussCons.LANGUAGE_TYPE_JP);
		pDto.setOrder(" sort_no ASC");
		List<MenuCatalog> menuCatalogList=menuCatalogService.list(pDto);
		return R.toList(menuCatalogList);
	}

	/**
	 * 
	 * 简要说明：删除信息
	 * 编写者：陈骑元
	 * 创建时间：2019-07-20
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("removeBuffetSpec")
	@ResponseBody
	public R removeBuffetSpec(String id) {
		if (WebplusUtil.isNotEmpty(id)) {
			Dto pDto = Dtos.newDto();
			pDto.put("spec_index_id", id);
			boolean result = buffetSpecService.deleteByMap(pDto);
			if (result) {
				return R.ok();
			}
		}
		return R.error();
	}
	
	/**
	 * 
	 * 简要说明：加载自助餐服务
	 * 编写者：陈骑元
	 * 创建时间：2019-08-28
	 * @param 说明
	 * @return 说明
	 */
	@RequestMapping("listOrderServer")
	@ResponseBody
	public R listOrderServer() {
		Dto pDto = Dtos.newDto(request);
		String userId=this.getUserId();
		pDto.put("shopId", userId);
		pDto.put("whetherBuffet", BussCons.SERVER_MENU_TYPE);
		pDto.put("languageType", BussCons.LANGUAGE_TYPE_JP);
		pDto.setOrder(" sort_no ASC");
		Page<MenuCatalog> page =menuCatalogService.likePage(pDto);
		WebplusCache.convertItem(page);
		return R.toPage(page);
	}
	
	/**
	 * 
	 * 简要说明：加载自助餐服务
	 * 编写者：陈骑元
	 * 创建时间：2019-08-28
	 * @param 说明
	 * @return 说明
	 */
	@RequestMapping("listCustomMenu")
	@ResponseBody
	public R listCustomMenu() {
		Dto pDto = Dtos.newDto(request);
		String userId=this.getUserId();
		pDto.put("shopId", userId);
		pDto.put("whetherBuffet", BussCons.CUSTOM_MENU_TYPE);
		pDto.put("languageType", BussCons.LANGUAGE_TYPE_JP);
		pDto.setOrder(" sort_no ASC");
		Page<MenuCatalog> page =menuCatalogService.likePage(pDto);
		WebplusCache.convertItem(page);
		return R.toPage(page);
	}
}

