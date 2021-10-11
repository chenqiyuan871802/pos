package com.ims.buss.system;

import org.springframework.web.bind.annotation.*;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.ims.core.cache.WebplusCache;
import com.ims.core.constant.WebplusCons;
import com.ims.core.matatype.Dto;
import com.ims.core.matatype.Dtos;
import com.ims.core.util.WebplusFormater;
import com.ims.core.util.WebplusJson;
import com.ims.core.util.WebplusUtil;
import com.ims.core.vo.Item;
import com.ims.core.vo.R;

import java.util.Date;
import java.util.List;

import com.ims.buss.constant.BussCons;
import com.ims.buss.model.BuffetSpec;
import com.ims.buss.model.MenuCatalog;
import com.ims.buss.model.MenuDict;
import com.ims.buss.model.Printer;
import com.ims.buss.model.Shop;
import com.ims.buss.service.BuffetMenuService;
import com.ims.buss.service.BuffetSpecService;
import com.ims.buss.service.MenuCatalogService;
import com.ims.buss.service.MenuDictService;
import com.ims.buss.service.MenuSpecService;
import com.ims.buss.service.PrinterService;
import com.ims.buss.service.ShopService;

import org.springframework.stereotype.Controller;
import com.ims.core.web.BaseController;

/**
 * <p>
 * 菜单类目 前端控制器
 * </p>
 *
 * @author 陈骑元
 * @since 2019-07-20
 */
@Controller
@RequestMapping("/buss/menuCatalog")
public class MenuCatalogController extends BaseController {

    @Autowired
    private MenuCatalogService menuCatalogService;

    @Autowired
    private ShopService shopService;
    /**
     *菜品业务逻辑
     */
    @Autowired
    private MenuDictService menuDictService;
    /**
     * 打印机管理
     */
    @Autowired
    private PrinterService printerService;
    /**
     *菜品规格业务逻辑
     */
    @Autowired
    private MenuSpecService menuSpecService;
    
    /**
     *菜品规格业务逻辑
     */
    @Autowired
    private BuffetMenuService buffetMenuService;
    
    /**
     *菜品规格业务逻辑
     */
    @Autowired
    private BuffetSpecService buffetSpecService;
	/**
	 * 
	 * 简要说明：分页查询 
	 * 编写者：陈骑元
	 * 创建时间：2019-07-20
	 * @param 说明
	 * @return 说明
	 */
	@RequestMapping("list")
	@ResponseBody
	public R list() {
		Dto pDto = Dtos.newDto(request);
		String catalogTypes=pDto.getString("catalogTypes");
		List<String> catalogTypeList=WebplusFormater.separatStringToList(catalogTypes);
		
		String userId=this.getUserId();
		pDto.put("shopId", userId);
		pDto.put("whetherBuffet", WebplusCons.WHETHER_NO);
		pDto.put("catalogTypeList", catalogTypeList);
		pDto.put("languageType", BussCons.LANGUAGE_TYPE_JP);
		pDto.setOrder(" sort_no ASC");
		Page<MenuCatalog> page =menuCatalogService.likePage(pDto);
		WebplusCache.convertItem(page);
		return R.toPage(page);
	}



	/**
	 * 
	 * 简要说明： 新增信息保存 
	 * 编写者：陈骑元
	 * 创建时间：2019-07-20
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("save")
	@ResponseBody
	public R save(MenuCatalog menuCatalog,String specJson) {
		String userId=this.getUserId();
		boolean result =false;
		Date currentTime=WebplusUtil.getDateTime();
		String catalogType=menuCatalog.getCatalogType();
		if(WebplusUtil.isEmpty(menuCatalog.getCatalogId())){
			String catalogId=WebplusUtil.uuid();
			menuCatalog.setCatalogId(catalogId);
			if(BussCons.LANGUAGE_TYPE_JP.equals(menuCatalog.getLanguageType())){
				menuCatalog.setCatalogIndexId(catalogId);
				Dto calcDto = Dtos.newCalcDto("IFNULL(MAX(sort_no),0)+1");
				calcDto.put("shopId", userId);
				calcDto.put("languageType", BussCons.LANGUAGE_TYPE_JP);
				calcDto.put("whetherBuffet", menuCatalog.getWhetherBuffet());
				if(WebplusUtil.isNotEmpty(catalogType)) {
					List<String> catalogTypeList=Lists.newArrayList();
					if(BussCons.CATALOG_TYPE_TAKEOUT.equals(catalogType)) {
						catalogTypeList.add(BussCons.CATALOG_TYPE_TAKEOUT);
				    }else {
				    	catalogTypeList.add(BussCons.CATALOG_TYPE_EATIN);
				    	catalogTypeList.add(BussCons.CATALOG_TYPE_BESIDES);
				    }
					calcDto.put("catalogTypeList", catalogTypeList);
				}
				
				String maxSortNo =menuCatalogService.calc(calcDto);
				if(WebplusUtil.isEmpty(maxSortNo)){
					maxSortNo="1";
				}
				menuCatalog.setWhetherFirst(WebplusCons.WHETHER_NO);
				int sortNo=Integer.parseInt(maxSortNo);
				menuCatalog.setSortNo(sortNo);
			}
			menuCatalog.setShopId(userId);
			menuCatalog.setCreateBy(userId);
			menuCatalog.setCreateTime(currentTime);
			menuCatalog.setUpdateBy(userId);
			menuCatalog.setUpdateTime(currentTime);
			result = menuCatalogService.insert(menuCatalog);
			
		}else{
			menuCatalog.setUpdateBy(userId);
			menuCatalog.setUpdateTime(currentTime);
			result = menuCatalogService.updateById(menuCatalog);
			if(result){
				if(BussCons.LANGUAGE_TYPE_JP.equals(menuCatalog.getLanguageType())&&WebplusCons.WHETHER_YES.equals(menuCatalog.getWhetherBuffet())){
					MenuCatalog entity=new MenuCatalog();
					entity.setBuffetImage(menuCatalog.getBuffetImage());
					entity.setBuffetPrice(menuCatalog.getBuffetPrice());
					entity.setLimitNum(menuCatalog.getLimitNum());
					entity.setMealType(menuCatalog.getMealType());
					entity.setTimeLimit(menuCatalog.getTimeLimit());
					entity.setLargeId(menuCatalog.getLargeId());
					EntityWrapper<MenuCatalog> wrapper=new EntityWrapper<MenuCatalog>();
					wrapper.eq("catalog_index_id", menuCatalog.getCatalogIndexId());
					menuCatalogService.update(entity, wrapper);
				}
				if(BussCons.LANGUAGE_TYPE_JP.equals(menuCatalog.getLanguageType())
						&&BussCons.SERVER_MENU_TYPE.equals(menuCatalog.getWhetherBuffet())){
					MenuCatalog entity=new MenuCatalog();
					entity.setBuffetPrice(menuCatalog.getBuffetPrice());
					entity.setLargeId(menuCatalog.getLargeId());
					EntityWrapper<MenuCatalog> wrapper=new EntityWrapper<MenuCatalog>();
					wrapper.eq("catalog_index_id", menuCatalog.getCatalogIndexId());
					menuCatalogService.update(entity, wrapper);
				}
				if(BussCons.LANGUAGE_TYPE_JP.equals(menuCatalog.getLanguageType())&&!WebplusCons.WHETHER_YES.equals(menuCatalog.getWhetherBuffet())){
					MenuCatalog entity=new MenuCatalog();
					entity.setCatalogType(menuCatalog.getCatalogType());
					entity.setLargeId(menuCatalog.getLargeId());
					EntityWrapper<MenuCatalog> wrapper=new EntityWrapper<MenuCatalog>();
					wrapper.eq("catalog_index_id", menuCatalog.getCatalogIndexId());
					menuCatalogService.update(entity, wrapper);
				}
				
				
				
			}
		}
		
		if (result) {
			List<BuffetSpec> buffetSpecList=this.saveBuffetSpec(specJson, menuCatalog);
			return R.ok().put("data", menuCatalog).put("dataList", buffetSpecList);
		} else {
			return R.error("保存失败");
		}

	}
	
	/**
	 * 
	 * 简要说明：更新保存自助餐
	 * 编写者：陈骑元
	 * 创建时间：2020年1月11日 下午5:09:42
	 * @param 说明
	 * @return 说明
	 */
	private List<BuffetSpec> saveBuffetSpec(String specJson,MenuCatalog menuCatalog){
		String userId=menuCatalog.getUpdateBy();
	    String languageType=menuCatalog.getLanguageType();
		List<BuffetSpec> buffetSpecList=Lists.newArrayList();
		String catalogIndexId=menuCatalog.getCatalogIndexId();
		Date currentTime=WebplusUtil.getDateTime();
		if(WebplusUtil.isNotEmpty(specJson)){
			
			List<Dto> specDtoList=WebplusJson.fromJson(specJson);
			if(WebplusUtil.isNotEmpty(specDtoList)){
				List<BuffetSpec> updateBuffetSpecList=Lists.newArrayList();
				int i=0;
				for(Dto specDto:specDtoList){
					i=i+1;
					String specId=specDto.getString("specId");
					String specName=specDto.getString("specName");
					Integer specPrice=specDto.getInteger("specPrice");
					String specIndexId=specDto.getString("specIndexId");
					if(WebplusUtil.isNotEmpty(specId)){
						BuffetSpec buffetSpec=new BuffetSpec();
						buffetSpec.setSpecId(specId);
						buffetSpec.setSpecName(specName);
						buffetSpec.setSpecPrice(specPrice);
						buffetSpec.setShopId(userId);
						buffetSpec.setSpecIndexId(specIndexId);
						buffetSpec.setCatalogIndexId(catalogIndexId);
						buffetSpec.setUpdateBy(userId);
						buffetSpec.setUpdateTime(currentTime);
						buffetSpecService.updateById(buffetSpec);
						buffetSpecList.add(buffetSpec);
						updateBuffetSpecList.add(buffetSpec);
					}else{
						BuffetSpec buffetSpec=new BuffetSpec();
						buffetSpec.setSpecName(specName);
						buffetSpec.setSpecPrice(specPrice);
						buffetSpec.setCatalogIndexId(menuCatalog.getCatalogIndexId());
						String specIdSub=WebplusUtil.uuid();
						buffetSpec.setSpecId(specIdSub);
						if(BussCons.LANGUAGE_TYPE_JP.equals(languageType)){
							buffetSpec.setSpecIndexId(specIdSub);
						}else{
						    buffetSpec.setSpecIndexId(specIndexId);
						}
						buffetSpec.setSortNo(i);
						buffetSpec.setLanguageType(languageType);
						buffetSpec.setShopId(userId);
						buffetSpec.setCreateBy(userId);
						buffetSpec.setCreateTime(currentTime);
						buffetSpec.setUpdateBy(userId);
						buffetSpec.setUpdateTime(currentTime);
						
						buffetSpecService.insert(buffetSpec);
						buffetSpecList.add(buffetSpec);
					}
					
					
				}
			  if(WebplusUtil.isNotEmpty(updateBuffetSpecList)){
				  for(BuffetSpec buffetSpec:updateBuffetSpecList){
					     BuffetSpec entity=new BuffetSpec();
						entity.setSpecPrice(buffetSpec.getSpecPrice());
						EntityWrapper<BuffetSpec> updateWrapper = new EntityWrapper<BuffetSpec>();
						updateWrapper.eq("spec_index_id",buffetSpec.getSpecIndexId());
						buffetSpecService.update(entity, updateWrapper);
				  }
			  }
			}
			
		}
		return buffetSpecList;
	}
	/**
	 * 
	 * 简要说明： 跳转到编辑页面 
	 * 编写者：陈骑元
	 * 创建时间：2019-07-20
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("edit")
	@ResponseBody
	public R edit(String id) {
		MenuCatalog menuCatalog=menuCatalogService.selectById(id);
		return R.toData(menuCatalog);
	}
	
	/**
	 * 
	 * 简要说明：修改信息
	 * 编写者：陈骑元
	 * 创建时间：2019-07-20
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("update")
	@ResponseBody
	public R update(MenuCatalog menuCatalog) {
		boolean result = menuCatalogService.updateById(menuCatalog);
		if (result) {
			return R.ok();
		} else {
			return R.error("更新失败");
		}
		
	}
	
	/**
	 * 
	 * 简要说明： 跳转到编辑页面 
	 * 编写者：陈骑元
	 * 创建时间：2019-07-20
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("show")
	@ResponseBody
	public R show(String catalogIndexId,String languageType,String queryWay) {
		EntityWrapper<MenuCatalog> wrapper = new EntityWrapper<MenuCatalog>();
		wrapper.eq("catalog_index_id",catalogIndexId);
		wrapper.eq("language_type", languageType);
		MenuCatalog menuCatalog=menuCatalogService.selectOne(wrapper);
		if(WebplusUtil.isEmpty(queryWay)){
			
			return R.toData(menuCatalog);
		}
       if(BussCons.LANGUAGE_TYPE_JP.equals(languageType)){ //如果日语
			
			
			EntityWrapper<BuffetSpec> specWrapper = new EntityWrapper<BuffetSpec>();
			specWrapper.eq("catalog_index_id",catalogIndexId);
			specWrapper.eq("language_type", languageType);
			specWrapper.orderBy("sort_no");
			List<BuffetSpec> menuSpecList=buffetSpecService.selectList(specWrapper);
			return R.toDataAndList(menuCatalog, menuSpecList);
		}else{
			if(WebplusUtil.isEmpty(menuCatalog)){
				EntityWrapper<MenuCatalog> jpWrapper = new EntityWrapper<MenuCatalog>();
				jpWrapper.eq("catalog_index_id",catalogIndexId);
				jpWrapper.eq("language_type", BussCons.LANGUAGE_TYPE_JP);
				jpWrapper.orderBy("sort_no");
				menuCatalog=menuCatalogService.selectOne(jpWrapper);
			    menuCatalog.setCatalogId("");
				EntityWrapper<BuffetSpec> specWrapper = new EntityWrapper<BuffetSpec>();
				specWrapper.eq("catalog_index_id",catalogIndexId);
				specWrapper.eq("language_type",BussCons.LANGUAGE_TYPE_JP );
				specWrapper.orderBy("sort_no");
				List<BuffetSpec> buffetSpecList=buffetSpecService.selectList(specWrapper);
				List<BuffetSpec> menuSpecListNew=Lists.newArrayList();
				for(BuffetSpec buffetSpec:buffetSpecList){
					buffetSpec.setSpecId("");
					menuSpecListNew.add(buffetSpec);
				}
				return R.toDataAndList(menuCatalog, menuSpecListNew);
			}else{
				EntityWrapper<BuffetSpec> specWrapper = new EntityWrapper<BuffetSpec>();
				specWrapper.eq("catalog_index_id",catalogIndexId);
				specWrapper.eq("language_type",languageType );
				specWrapper.orderBy("sort_no");
				List<BuffetSpec> buffetSpecList=buffetSpecService.selectList(specWrapper);
				EntityWrapper<BuffetSpec> specJpWrapper = new EntityWrapper<BuffetSpec>();
				specJpWrapper.eq("catalog_index_id",catalogIndexId);
				specJpWrapper.eq("language_type",BussCons.LANGUAGE_TYPE_JP );
				specJpWrapper.orderBy("sort_no");
				List<BuffetSpec> jpBuffetSpecList=buffetSpecService.selectList(specJpWrapper);
				if(WebplusUtil.isEmpty(buffetSpecList)){
					List<BuffetSpec> buffetSpecListNew=Lists.newArrayList();
					for(BuffetSpec buffetSpec:jpBuffetSpecList){
						buffetSpec.setSpecId("");
						buffetSpecListNew.add(buffetSpec);
					}
					return R.toDataAndList(menuCatalog, buffetSpecListNew);
				}else{
					if(jpBuffetSpecList.size()>buffetSpecList.size()){
						for(BuffetSpec buffetSpec:jpBuffetSpecList){
							if(!checkExistSpec(buffetSpec.getSpecIndexId(), buffetSpecList)){
								buffetSpec.setSpecId("");
								buffetSpecList.add(buffetSpec);
								
							}
						}
					}
					return R.toDataAndList(menuCatalog, buffetSpecList);
				}
			}
		}
		
	}
       
       
       /**
   	 * 
   	 * 简要说明：检查规格
   	 * 编写者：陈骑元
   	 * 创建时间：2019年7月29日 下午11:39:34
   	 * @param 说明
   	 * @return 说明
   	 */
   	private boolean checkExistSpec(String specIndexId,List<BuffetSpec> buffetSpecList){
   	    for(BuffetSpec buffetSpec:buffetSpecList){
   	    	if(specIndexId.equals(buffetSpec.getSpecIndexId())){
   	    		
   	    		return true;
   	    	}
   	    }
   		
   		return false;
   	}
	/**
	 * 
	 * 简要说明：删除信息
	 * 编写者：陈骑元
	 * 创建时间：2019-07-20
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("remove")
	@ResponseBody
	public R remove(String id) {
		MenuCatalog menuCatalog=menuCatalogService.selectById(id);
		String whetherBuffet=menuCatalog.getWhetherBuffet();
		if(BussCons.SERVER_MENU_TYPE.equals(whetherBuffet)){
			boolean result=menuCatalogService.deleteById(id);
			if(result){
				return R.ok();
			}
			return R.error();
		}else{
			String shopId=this.getUserId();
			Dto pDto=Dtos.newDto();
			pDto.put("catalog_index_id", id);
			pDto.put("shop_id", shopId);
			boolean result = menuCatalogService.deleteByMap(pDto);
			if (result) {
				if(WebplusCons.WHETHER_YES.equals(whetherBuffet)){  //清除绑定
					Dto removeDto=Dtos.newDto();
					removeDto.put("catalog_index_id", id);
					buffetMenuService.deleteByMap(removeDto);
				}else{
					Dto menuDto=Dtos.newDto();
					menuDto.put("catalog_index_id", id);
					menuDto.put("shop_id", shopId);
					menuDto.put("language_type", BussCons.LANGUAGE_TYPE_JP);
					List<MenuDict> menuDictList=menuDictService.selectByMap(menuDto);
					for(MenuDict menuDict:menuDictList){  //循环删除产品
						String menuIndexId=menuDict.getMenuIndexId();
						Dto deleteMap=Dtos.newDto();
						deleteMap.put("menu_index_id", menuIndexId);
						deleteMap.put("shop_id", shopId);
						menuSpecService.deleteByMap(deleteMap);  
					}
					//删除菜品
					Dto columnDto=Dtos.newDto();  
					columnDto.put("catalog_index_id",id);
					columnDto.put("shop_id", shopId);
					menuDictService.deleteByMap(columnDto);  
				}
				
				return R.ok();
			} else {
				return R.error("");
			}
		}
	
		
	}
	
	/**
	 * 
	 * 简要说明：批量删除信息
	 * 编写者：陈骑元
	 * 创建时间：2019-07-20
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("batchRemove")
	@ResponseBody
	public R batchRemove(String ids) {
		List<String> idList=WebplusFormater.separatStringToList(ids);
		boolean result = menuCatalogService.deleteBatchIds(idList);
		if (result) {
			return R.ok();
		} else {
			return R.error("删除失败");
		}
		
	}
	
	/**
	 * 
	 * 简要说明：更新排序方式
	 * 编写者：陈骑元
	 * 创建时间：2019年7月24日 下午10:22:46
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("updateSort")
	@ResponseBody
	public R updateSort(String id,String sortWay,String whetherBuffet){
		MenuCatalog menuCatalog=menuCatalogService.selectById(id);
		String shopId=menuCatalog.getShopId();
		String catalogIndexId=menuCatalog.getCatalogIndexId();
		List<String> catalogTypeList=Lists.newArrayList();
		String catalogType=menuCatalog.getCatalogType();
		if(WebplusUtil.isNotEmpty(catalogType)) {
			
			if(BussCons.CATALOG_TYPE_TAKEOUT.equals(catalogType)) {
				catalogTypeList.add(BussCons.CATALOG_TYPE_TAKEOUT);
		    }else {
		    	catalogTypeList.add(BussCons.CATALOG_TYPE_EATIN);
		    	catalogTypeList.add(BussCons.CATALOG_TYPE_BESIDES);
		    }
		}
		int sortNo=menuCatalog.getSortNo();
		if("1".equals(sortWay)){  //更新到顶部
			Dto calcDto = Dtos.newCalcDto("IFNULL(MIN(sort_no),0)");
			calcDto.put("shopId", shopId);
			calcDto.put("languageType",BussCons.LANGUAGE_TYPE_JP);
			calcDto.put("whetherBuffet", whetherBuffet);
			calcDto.put("catalogTypeList",catalogTypeList);
			String minSortNoStr=menuCatalogService.calc(calcDto);
			int minSortNo=Integer.parseInt(minSortNoStr);
			if(sortNo>minSortNo){
				Dto updateDto=Dtos.newDto();
				updateDto.put("shopId", shopId);
				updateDto.put("sortNoLt", sortNo);
				updateDto.put("whetherBuffet", whetherBuffet);
				updateDto.put("catalogTypeList",catalogTypeList);
				menuCatalogService.updateSortDown(updateDto);
				MenuCatalog updateEntity=new MenuCatalog();
				updateEntity.setSortNo(minSortNo);
				EntityWrapper<MenuCatalog> updateWrapper = new EntityWrapper<MenuCatalog>();
				updateWrapper.eq("shop_id", menuCatalog.getShopId());
				updateWrapper.eq("catalog_index_id", menuCatalog.getCatalogIndexId());
				updateWrapper.eq("whether_buffet", whetherBuffet);
				menuCatalogService.update(updateEntity, updateWrapper);
			}
		}else if("2".equals(sortWay)){  //上升1位
			Dto calcDto = Dtos.newCalcDto("IFNULL(MAX(sort_no),0)");
			calcDto.put("shopId", shopId);
			calcDto.put("sortNoLt", sortNo);
			calcDto.put("languageType",BussCons.LANGUAGE_TYPE_JP);
			calcDto.put("whetherBuffet", whetherBuffet);
			String maxSortNoStr=menuCatalogService.calc(calcDto);
			int maxSortNo=Integer.parseInt(maxSortNoStr);
			if(maxSortNo>0){
				MenuCatalog entity=new MenuCatalog();
				entity.setSortNo(sortNo);
				EntityWrapper<MenuCatalog> entityWrapper = new EntityWrapper<MenuCatalog>();
				entityWrapper.eq("sort_no", maxSortNo);
				entityWrapper.eq("shop_id",shopId);
				entityWrapper.eq("whether_buffet", whetherBuffet);
				if(WebplusUtil.isNotEmpty(catalogTypeList)) {
					entityWrapper.in("catalog_type", catalogTypeList);
				}
				
				menuCatalogService.update(entity, entityWrapper);
				MenuCatalog updateEntity=new MenuCatalog();
				updateEntity.setSortNo(maxSortNo);
				EntityWrapper<MenuCatalog> updateWrapper = new EntityWrapper<MenuCatalog>();
				updateWrapper.eq("shop_id", menuCatalog.getShopId());
				updateWrapper.eq("catalog_index_id",catalogIndexId);
				updateWrapper.eq("whether_buffet", whetherBuffet);
				menuCatalogService.update(updateEntity, updateWrapper);
			}
				
		}else if("3".equals(sortWay)){  //下降1位
			Dto calcDto = Dtos.newCalcDto("IFNULL(MIN(sort_no),0)");
			calcDto.put("shopId", shopId);
			calcDto.put("languageType",BussCons.LANGUAGE_TYPE_JP);
			calcDto.put("sortNoGt", sortNo);
			calcDto.put("catalogTypeList",catalogTypeList);
			String minSortNoStr=menuCatalogService.calc(calcDto);
			int minSortNo=Integer.parseInt(minSortNoStr);
			if(minSortNo>0){
				MenuCatalog entity=new MenuCatalog();
				entity.setSortNo(sortNo);
				EntityWrapper<MenuCatalog> entityWrapper = new EntityWrapper<MenuCatalog>();
				entityWrapper.eq("sort_no", minSortNo);
				entityWrapper.eq("shop_id",shopId);
				entityWrapper.eq("whether_buffet", whetherBuffet);
				if(WebplusUtil.isNotEmpty(catalogTypeList)) {
					entityWrapper.in("catalog_type", catalogTypeList);
				}
				menuCatalogService.update(entity, entityWrapper);
				MenuCatalog updateEntity=new MenuCatalog();
				updateEntity.setSortNo(minSortNo);
				EntityWrapper<MenuCatalog> updateWrapper = new EntityWrapper<MenuCatalog>();
				updateWrapper.eq("shop_id", menuCatalog.getShopId());
				updateWrapper.eq("catalog_index_id",catalogIndexId);
				updateWrapper.eq("whether_buffet", whetherBuffet);
				menuCatalogService.update(updateEntity, updateWrapper);
			}
				
		}else{  //下降到最后1位
			Dto calcDto = Dtos.newCalcDto("IFNULL(MAX(sort_no),0)");
			calcDto.put("shopId", shopId);
			calcDto.put("languageType",BussCons.LANGUAGE_TYPE_JP);
			calcDto.put("whetherBuffet", whetherBuffet);
			calcDto.put("catalogTypeList",catalogTypeList);
			String maxSortNoStr=menuCatalogService.calc(calcDto);
			int maxSortNo=Integer.parseInt(maxSortNoStr);
			if(sortNo<maxSortNo){
				Dto updateDto=Dtos.newDto();
				updateDto.put("shopId", shopId);
				updateDto.put("sortNoGt", sortNo);
				updateDto.put("whetherBuffet", whetherBuffet);
				updateDto.put("catalogTypeList",catalogTypeList);
				menuCatalogService.updateSortUp(updateDto);
				MenuCatalog updateEntity=new MenuCatalog();
				updateEntity.setSortNo(maxSortNo);
				EntityWrapper<MenuCatalog> updateWrapper = new EntityWrapper<MenuCatalog>();
				updateWrapper.eq("shop_id", menuCatalog.getShopId());
				updateWrapper.eq("catalog_index_id",catalogIndexId);
				updateWrapper.eq("whether_buffet", whetherBuffet);
				menuCatalogService.update(updateEntity, updateWrapper);
			}
		}
		 
		return R.ok();
			
	}
	
	/**
	 * 
	 * 简要说明：获取打印列表
	 * 编写者：陈骑元
	 * 创建时间：2019年7月26日 上午12:17:08
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("listPrinter")
	@ResponseBody
	public R listPrinter(){
		String userId=this.getUserId();
		Dto pDto = Dtos.newDto(request);
		pDto.put("shopId", userId);
		pDto.setOrder(" create_time DESC ");
		List<Printer> printerList=printerService.list(pDto);
		return R.toList(printerList);
	}
	
	/**
	 * 
	 * 简要说明：获取打印列表
	 * 编写者：陈骑元
	 * 创建时间：2019年7月26日 上午12:17:08
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("listPrinterNew")
	@ResponseBody
	public List<Printer> listPrinteNew(String printNums){
		String userId=this.getUserId();
		Dto pDto = Dtos.newDto(request);
		pDto.put("shopId", userId);
		pDto.setOrder(" create_time DESC ");
		List<Printer> printerList=printerService.list(pDto);
		List<String> printNumList=WebplusFormater.separatStringToList(printNums);
		for(Printer printer:printerList	) {
			if(WebplusUtil.checkStrExistInList(printer.getPrintNum(), printNumList)) {
				printer.setWhetherSelect(WebplusCons.WHETHER_YES);
			}
		}
		return printerList; 
	}
	/**
	 * 
	 * 简要说明：查询语言类型
	 * 编写者：陈骑元
	 * 创建时间：2019年7月26日 上午12:17:08
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("listLanguage")
	@ResponseBody
	public R listLanguage(){
		String userId=this.getUserId();
		Shop shop=shopService.selectById(userId);
		String language=shop.getLanguage();
		List<Item> itemList=WebplusCache.getContainsItem("language_type", language);
		return R.toList(itemList);
	}
	
	
	/**
	 * 
	 * 简要说明：更新税金类型
	 * 编写者：陈骑元
	 * 创建时间：2019-07-20
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("updateTaxType")
	@ResponseBody
	public R updateTaxType(String catalogId,String taxType) {
		MenuCatalog entity=new MenuCatalog();
		entity.setTaxType(taxType);
		EntityWrapper<MenuCatalog> wrapper=new EntityWrapper<MenuCatalog>();
		wrapper.eq("catalog_index_id", catalogId);
		boolean result=menuCatalogService.update(entity, wrapper);
		if (result) {
			return R.ok();
		} else {
			return R.error();
		}
		
	}
	
	/**
	 * 
	 * 简要说明：更新是否置顶
	 * 编写者：陈骑元
	 * 创建时间：2019-07-20
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("updateTop")
	@ResponseBody
	public R updateTop(String catalogId,String whetherTop) {
		MenuCatalog entity=new MenuCatalog();
		entity.setWhetherTop(whetherTop);
		EntityWrapper<MenuCatalog> wrapper=new EntityWrapper<MenuCatalog>();
		wrapper.eq("catalog_index_id", catalogId);
		boolean result=menuCatalogService.update(entity, wrapper);
		if (result) {
			return R.ok();
		} else {
			return R.error();
		}
		
	}
	

	/**
	 * 
	 * 简要说明：更新首页
	 * 编写者：陈骑元
	 * 创建时间：2019-07-20
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("updateWhetherFirst")
	@ResponseBody
	public R updateWhetherFirst(String catalogId,String whetherFirst) {
		MenuCatalog entity=new MenuCatalog();
		entity.setWhetherFirst(whetherFirst);
		EntityWrapper<MenuCatalog> wrapper=new EntityWrapper<MenuCatalog>();
		wrapper.eq("catalog_index_id", catalogId);
		boolean result=menuCatalogService.update(entity, wrapper);
		if (result) {
			return R.ok();
		} else {
			return R.error();
		}
		
	}
	
}

