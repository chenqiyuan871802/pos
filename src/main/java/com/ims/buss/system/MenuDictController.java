package com.ims.buss.system;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.google.common.collect.Lists;
import com.ims.core.cache.WebplusCache;
import com.ims.core.constant.WebplusCons;
import com.ims.core.matatype.Dto;
import com.ims.core.matatype.Dtos;
import com.ims.core.util.WebplusFormater;
import com.ims.core.util.WebplusJson;
import com.ims.core.util.WebplusUtil;
import com.ims.core.vo.R;

import java.util.Date;
import java.util.List;

import com.ims.buss.constant.BussCons;
import com.ims.buss.model.MenuDict;
import com.ims.buss.model.MenuSpec;
import com.ims.buss.model.MenuStep;
import com.ims.buss.model.StepSpec;
import com.ims.buss.service.MenuDictService;
import com.ims.buss.service.MenuSpecService;
import com.ims.buss.service.MenuStepService;
import com.ims.buss.service.StepSpecService;

import org.springframework.stereotype.Controller;
import com.ims.core.web.BaseController;

/**
 * <p>
 * 菜单目录 前端控制器
 * </p>
 *
 * @author 陈骑元
 * @since 2019-07-20
 */
@Controller
@RequestMapping("/buss/menuDict")
public class MenuDictController extends BaseController {

    @Autowired
    private MenuDictService menuDictService;
    @Autowired
    private MenuSpecService menuSpecService;
    /**
     * 菜单规格步骤
     */
    @Autowired
    private MenuStepService menuStepService;
    /**
     * 菜单规格步骤
     */
    @Autowired
    private StepSpecService stepSpecService;


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
		String userId=this.getUserId();
		pDto.put("shopId", userId);
		pDto.put("languageType", BussCons.LANGUAGE_TYPE_JP);
		pDto.setOrder(" sort_no ASC");
		Page<MenuDict> page =menuDictService.likePage(pDto);
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
	public R save(MenuDict menuDict) {
		String userId=this.getUserId();
		boolean result =false;
		Date currentTime=WebplusUtil.getDateTime();
		String languageType=menuDict.getLanguageType();
		if(WebplusUtil.isEmpty(menuDict.getMenuId())){
			String menuId=WebplusUtil.uuid();
			menuDict.setMenuId(menuId);
			if(BussCons.LANGUAGE_TYPE_JP.equals(languageType)){
				
			    Dto calcDto = Dtos.newCalcDto("IFNULL(MAX(sort_no),0)+1");
				calcDto.put("shopId", userId);
				calcDto.put("catalogIndexId", menuDict.getCatalogIndexId());
				calcDto.put("languageType", BussCons.LANGUAGE_TYPE_JP);
				String maxSortNo =menuDictService.calc(calcDto);
				if(WebplusUtil.isEmpty(maxSortNo)){
					maxSortNo="1";
				}
				int sortNo=Integer.parseInt(maxSortNo);
				menuDict.setMenuIndexId(menuId);
				menuDict.setSortNo(sortNo);
				
			}
	
			menuDict.setShopId(userId);
			menuDict.setCreateBy(userId);
			menuDict.setCreateTime(currentTime);
			menuDict.setUpdateBy(userId);
			menuDict.setUpdateTime(currentTime);
			result = menuDictService.insert(menuDict);
		}else{
			
			menuDict.setUpdateBy(userId);
			menuDict.setUpdateTime(currentTime);
			result = menuDictService.updateById(menuDict);
			if(result){
				if(BussCons.LANGUAGE_TYPE_JP.equals(languageType)){
					MenuDict entity=new MenuDict();
					entity.setMenuImage(menuDict.getMenuImage());
					entity.setMenuPrice(menuDict.getMenuPrice());
					entity.setMenuNum(menuDict.getMenuNum());
					entity.setMenuType(menuDict.getMenuType());
					entity.setMealType(menuDict.getMealType());
					entity.setCostPrice(menuDict.getCostPrice());
					EntityWrapper<MenuDict> wrapper=new EntityWrapper<MenuDict>();
					wrapper.eq("menu_index_id", menuDict.getMenuIndexId());
					menuDictService.update(entity, wrapper);
					
				}
			}
		}
		
		if (result) {
			return R.ok().put("data", menuDict);
		} else {
			return R.error("");
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
	@PostMapping("edit")
	@ResponseBody
	public R edit(String id) {
		MenuDict menuDict=menuDictService.selectById(id);
		return R.toData(menuDict);
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
	public R update(MenuDict menuDict) {
		boolean result = menuDictService.updateById(menuDict);
		if (result) {
			return R.ok();
		} else {
			return R.error("更新失败");
		}
		
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
		MenuDict menuDict=menuDictService.selectById(id);
	    Dto pDto=Dtos.newDto();
	    pDto.put("menu_index_id", menuDict.getMenuIndexId());
		boolean result = menuDictService.deleteByMap(pDto);
		if (result) {
			menuSpecService.deleteByMap(pDto);
			return R.ok();
		} else {
			return R.error();
		}
		
	}
	/**
	 * 
	 * 简要说明：删除菜单规格
	 * 编写者：陈骑元
	 * 创建时间：2019-07-20
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("removeMenuSpec")
	@ResponseBody
	public R removeMenuSpec(String id) {
		MenuSpec menuSpec=menuSpecService.selectById(id);
		String menuIndexId=menuSpec.getMenuIndexId();
	    Dto pDto=Dtos.newDto();
	    pDto.put("spec_index_id", menuSpec.getSpecIndexId());
	    pDto.put("menu_index_id", menuIndexId);
		boolean result = menuSpecService.deleteByMap(pDto);
		if (result) {
			EntityWrapper<MenuSpec> wrapper = new EntityWrapper<MenuSpec>();
			wrapper .eq("menu_index_id",menuSpec.getMenuIndexId());
			int count=menuSpecService.selectCount(wrapper);
			if(count==0){ //没有规格
				MenuDict entity=new MenuDict();
				entity.setWhetherSpec(WebplusCons.WHETHER_NO);
				EntityWrapper<MenuDict> updateWrapper = new EntityWrapper<MenuDict>();
				updateWrapper.eq("menu_index_id",menuIndexId);
				menuDictService.update(entity, updateWrapper);
			}
			return R.ok();
		} else {
			return R.error();
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
		boolean result = menuDictService.deleteBatchIds(idList);
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
	public R updateSort(String id,String sortWay){
		String userId=this.getUserId();
		String key=BussCons.SORT_UPDATE_KEY+userId;
		if(WebplusCache.exists(key)) {
			
			return R.error("wait for 2sec");
		}else {
			WebplusCache.setString(key, key, 2);
		}
		MenuDict menuDict=menuDictService.selectById(id);
		String shopId=menuDict.getShopId();
		
		String catalogIndexId=menuDict.getCatalogIndexId();
		String menuIndexId=menuDict.getMenuIndexId();
		int sortNo=menuDict.getSortNo();
		if("1".equals(sortWay)){  //更新到顶部
			Dto calcDto = Dtos.newCalcDto("IFNULL(MIN(sort_no),0)");
			calcDto.put("shopId", shopId);
			calcDto.put("catalogIndexId", catalogIndexId);
			calcDto.put("languageType",BussCons.LANGUAGE_TYPE_JP);
			String minSortNoStr=menuDictService.calc(calcDto);
			int minSortNo=Integer.parseInt(minSortNoStr);
			if(sortNo>minSortNo){
				Dto updateDto=Dtos.newDto();
				updateDto.put("shopId", shopId);
				updateDto.put("catalogIndexId", catalogIndexId);
				updateDto.put("sortNoLt", sortNo);
				updateDto.put("menuIndexId", menuIndexId);
				menuDictService.updateSortDown(updateDto);
				MenuDict updateEntity=new MenuDict();
				updateEntity.setSortNo(minSortNo);
				updateEntity.setUpdateTime(WebplusUtil.getDateTime());
				EntityWrapper<MenuDict> updateWrapper = new EntityWrapper<MenuDict>();
				updateWrapper.eq("shop_id", menuDict.getShopId());
				updateWrapper.eq("catalog_index_id", catalogIndexId);
				updateWrapper.eq("menu_index_id", menuIndexId);
				menuDictService.update(updateEntity, updateWrapper);
			}
		}else if("2".equals(sortWay)){  //上升1位
			Dto calcDto = Dtos.newCalcDto("IFNULL(MAX(sort_no),0)");
			calcDto.put("shopId", shopId);
			calcDto.put("sortNoLt", sortNo);
			calcDto.put("catalogIndexId", catalogIndexId);
			calcDto.put("languageType",BussCons.LANGUAGE_TYPE_JP);
			String maxSortNoStr=menuDictService.calc(calcDto);
			int maxSortNo=Integer.parseInt(maxSortNoStr);
			if(maxSortNo>0){
				MenuDict entity=new MenuDict();
				entity.setSortNo(sortNo);
				entity.setUpdateTime(WebplusUtil.getDateTime());
				EntityWrapper<MenuDict> entityWrapper = new EntityWrapper<MenuDict>();
				entityWrapper.eq("sort_no", maxSortNo);
				entityWrapper.eq("shop_id",shopId);
				entityWrapper.ne("menu_index_id", menuIndexId);
				entityWrapper.eq("catalog_index_id",catalogIndexId);
				menuDictService.update(entity, entityWrapper);
				MenuDict updateEntity=new MenuDict();
				updateEntity.setSortNo(maxSortNo);
				updateEntity.setUpdateTime(WebplusUtil.getDateTime());
				EntityWrapper<MenuDict> updateWrapper = new EntityWrapper<MenuDict>();
				updateWrapper.eq("shop_id", menuDict.getShopId());
				updateWrapper.eq("catalog_index_id",catalogIndexId);
				updateWrapper.eq("menu_index_id", menuIndexId);
				menuDictService.update(updateEntity, updateWrapper);
			}
				
		}else if("3".equals(sortWay)){  //下降1位
			Dto calcDto = Dtos.newCalcDto("IFNULL(MIN(sort_no),0)");
			calcDto.put("shopId", shopId);
			calcDto.put("catalogIndexId", catalogIndexId);
			calcDto.put("languageType",BussCons.LANGUAGE_TYPE_JP);
			calcDto.put("sortNoGt", sortNo);
			String minSortNoStr=menuDictService.calc(calcDto);
			int minSortNo=Integer.parseInt(minSortNoStr);
			if(minSortNo>0){
				MenuDict entity=new MenuDict();
				entity.setSortNo(sortNo);
				entity.setUpdateTime(WebplusUtil.getDateTime());
				EntityWrapper<MenuDict> entityWrapper = new EntityWrapper<MenuDict>();
				entityWrapper.eq("sort_no", minSortNo);
				entityWrapper.eq("shop_id",shopId);
				entityWrapper.eq("catalog_index_id",catalogIndexId);
				entityWrapper.ne("menu_index_id", menuIndexId);
				menuDictService.update(entity, entityWrapper);
				MenuDict updateEntity=new MenuDict();
				updateEntity.setSortNo(minSortNo);
				updateEntity.setUpdateTime(WebplusUtil.getDateTime());
				EntityWrapper<MenuDict> updateWrapper = new EntityWrapper<MenuDict>();
				updateWrapper.eq("shop_id", menuDict.getShopId());
				updateWrapper.eq("catalog_index_id",catalogIndexId);
				updateWrapper.eq("menu_index_id", menuIndexId);
				menuDictService.update(updateEntity, updateWrapper);
			}
				
		}else{  //下降到最后1位
			Dto calcDto = Dtos.newCalcDto("IFNULL(MAX(sort_no),0)");
			calcDto.put("shopId", shopId);
			calcDto.put("catalogIndexId", catalogIndexId);
			calcDto.put("languageType",BussCons.LANGUAGE_TYPE_JP);
			String maxSortNoStr=menuDictService.calc(calcDto);
			int maxSortNo=Integer.parseInt(maxSortNoStr);
			if(sortNo<maxSortNo){
				Dto updateDto=Dtos.newDto();
				updateDto.put("shopId", shopId);
				updateDto.put("sortNoGt", sortNo);
				updateDto.put("catalogIndexId", catalogIndexId);
				updateDto.put("menuIndexId", menuIndexId);
				menuDictService.updateSortUp(updateDto);
				MenuDict updateEntity=new MenuDict();
				updateEntity.setSortNo(maxSortNo);
				updateEntity.setUpdateTime(WebplusUtil.getDateTime());
				EntityWrapper<MenuDict> updateWrapper = new EntityWrapper<MenuDict>();
				updateWrapper.eq("shop_id", menuDict.getShopId());
				updateWrapper.eq("catalog_index_id",catalogIndexId);
				updateWrapper.eq("menu_index_id", menuIndexId);
				menuDictService.update(updateEntity, updateWrapper);
			}
		}
		 
		return R.ok();
			
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
	public R show(String menuIndexId,String languageType) {
		EntityWrapper<MenuDict> wrapper = new EntityWrapper<MenuDict>();
		wrapper.eq("menu_index_id",menuIndexId);
		wrapper.eq("language_type", languageType);
		MenuDict menuDict=menuDictService.selectOne(wrapper);
		if(BussCons.LANGUAGE_TYPE_JP.equals(languageType)){ //如果日语
			
			return R.toData(menuDict);
		}else{
			if(WebplusUtil.isEmpty(menuDict)){
				EntityWrapper<MenuDict> jpWrapper = new EntityWrapper<MenuDict>();
				jpWrapper.eq("menu_index_id",menuIndexId);
				jpWrapper.eq("language_type", BussCons.LANGUAGE_TYPE_JP);
			    menuDict=menuDictService.selectOne(jpWrapper);
                menuDict.setMenuId("");
			}
				
			if(WebplusUtil.isNotEmpty(menuDict.getMenuImage())){
				EntityWrapper<MenuDict> jpWrapper = new EntityWrapper<MenuDict>();
				jpWrapper.eq("menu_index_id",menuIndexId);
				jpWrapper.eq("language_type", BussCons.LANGUAGE_TYPE_JP);
			    MenuDict menuDictJp=menuDictService.selectOne(jpWrapper);
			    menuDict.setMenuImage(menuDictJp.getMenuImage());
			}
			return R.toData(menuDict);
		}
		
	
	}
	
	/**
	 * 
	 * 简要说明：加载菜单步骤
	 * 编写者：陈骑元
	 * 创建时间：2019-07-20
	 * @param 说明
	 * @return 说明
	 */
	@RequestMapping("listMenuStep")
	@ResponseBody
	public R listMenuStep() {
		Dto pDto = Dtos.newDto(request);
		String userId=this.getUserId();
		pDto.put("shopId", userId);
		pDto.put("languageType", BussCons.LANGUAGE_TYPE_JP);
		pDto.setOrder(" step_num ASC");
		Page<MenuStep> page=menuStepService.listPage(pDto);
		return R.toPage(page);
	}
	/**
	 * 
	 * 简要说明：保存规格步骤
	 * 编写者：陈骑元
	 * 创建时间：2019年8月29日 下午2:46:18
	 * @param 说明
	 * @return 说明
	 */
	@RequestMapping("saveMenuStep")
	@ResponseBody
	public R saveMenuStep(MenuStep menuStep,String stepSpecs){
		String userId=this.getUserId();
		boolean result=false;
		Date currentTime=WebplusUtil.getDateTime();
		String languageType=menuStep.getLanguageType();
		List<StepSpec> returnStepSpecList=Lists.newArrayList();
		if(WebplusUtil.isEmpty(menuStep.getStepId())){  //如果为空说明是新增
			String stepId=WebplusUtil.uuid();
			menuStep.setStepId(stepId);
			menuStep.setShopId(userId);
			menuStep.setCreateTime(currentTime);
			menuStep.setCreateBy(userId);
			menuStep.setUpdateBy(userId);
			menuStep.setUpdateTime(currentTime);
			if(BussCons.LANGUAGE_TYPE_JP.equals(languageType)){
				menuStep.setStepIndexId(stepId);
			}
			List<StepSpec> stepSpecList=Lists.newArrayList();
			if(WebplusUtil.isNotEmpty(stepSpecs)){
				List<Dto> dataList=WebplusJson.fromJson(stepSpecs);
				int i=0;
				for(Dto dataDto:dataList){
					i=i+1;
					StepSpec stepSpec=new StepSpec();
					WebplusUtil.copyProperties(dataDto, stepSpec);
					String specId=WebplusUtil.uuid();
					stepSpec.setSpecId(specId);
					if(BussCons.LANGUAGE_TYPE_JP.equals(languageType)){
						stepSpec.setSpecIndexId(specId);
					}
					stepSpec.setLanguageType(languageType);
					stepSpec.setStepIndexId(menuStep.getStepIndexId());
					stepSpec.setShopId(userId);
					stepSpec.setCreateTime(currentTime);
					stepSpec.setCreateBy(userId);
					stepSpec.setUpdateBy(userId);
					stepSpec.setUpdateTime(currentTime);
					stepSpec.setSortNo(i);
					stepSpecList.add(stepSpec);
				}
			}
			
		   result=menuStepService.insert(menuStep);
		   if(result){
			   if(WebplusUtil.isNotEmpty(stepSpecList)){
					stepSpecService.insertBatch(stepSpecList);
					returnStepSpecList.addAll(stepSpecList);
				}
		   }
		   if(result&&BussCons.LANGUAGE_TYPE_JP.equals(languageType)){  //更新是否规格步骤
			   MenuDict menuDict=new MenuDict();
			   menuDict.setWhetherSpec(WebplusCons.WHETHER_YES);
			   EntityWrapper<MenuDict> wrapper = new EntityWrapper<MenuDict>();
			   wrapper.eq("menu_index_id", menuStep.getMenuIndexId());
			   menuDictService.update(menuDict, wrapper);
		   }
		}else{
			menuStep.setUpdateBy(userId);
			menuStep.setUpdateTime(currentTime);
			List<StepSpec> addStepSpecList=Lists.newArrayList();
			List<StepSpec> updateStepSpecList=Lists.newArrayList();
			if(WebplusUtil.isNotEmpty(stepSpecs)){
				List<Dto> dataList=WebplusJson.fromJson(stepSpecs);
				
				int i=0;
				for(Dto dataDto:dataList){
					i=i+1;
					StepSpec stepSpec=new StepSpec();
					WebplusUtil.copyProperties(dataDto, stepSpec);
					stepSpec.setUpdateBy(userId);
					stepSpec.setUpdateTime(currentTime);
					stepSpec.setSortNo(i);
					if(WebplusUtil.isEmpty(stepSpec.getSpecId())){
						String specId=WebplusUtil.uuid();
						stepSpec.setSpecId(specId);
						if(BussCons.LANGUAGE_TYPE_JP.equals(languageType)){
							stepSpec.setSpecIndexId(specId);
						}
						stepSpec.setLanguageType(languageType);
						stepSpec.setStepIndexId(menuStep.getStepIndexId());
						stepSpec.setShopId(userId);
						stepSpec.setCreateTime(currentTime);
						stepSpec.setCreateBy(userId);
						addStepSpecList.add(stepSpec);
					}else{
						updateStepSpecList.add(stepSpec);
					}
					
					returnStepSpecList.add(stepSpec);
				}
			}
			result=menuStepService.updateById(menuStep);
			if(result){
				if(BussCons.LANGUAGE_TYPE_JP.equals(languageType)){  //批量处理各种语言变化
					MenuStep entity=new MenuStep();
					entity.setStepNum(menuStep.getStepNum());
					entity.setChooseType(menuStep.getChooseType());
					entity.setMaxChoose(menuStep.getMaxChoose());
					EntityWrapper<MenuStep> updateWrapper = new EntityWrapper<MenuStep>();
					updateWrapper.eq("step_index_id", menuStep.getStepIndexId());
					menuStepService.update(entity, updateWrapper);
				}
				if(WebplusUtil.isNotEmpty(addStepSpecList)){
					stepSpecService.insertBatch(addStepSpecList);
				}
				if(WebplusUtil.isNotEmpty(updateStepSpecList)){
					stepSpecService.updateBatchById(updateStepSpecList);
					if(BussCons.LANGUAGE_TYPE_JP.equals(languageType)){
						for(StepSpec stepSpec:updateStepSpecList){
							StepSpec entity=new StepSpec();
							entity.setSpecPrice(stepSpec.getSpecPrice());
							EntityWrapper<StepSpec> updateWrapper = new EntityWrapper<StepSpec>();
							updateWrapper.eq("spec_index_id",stepSpec.getSpecIndexId());
							stepSpecService.update(entity, updateWrapper);
						}
					}
				}
			}
		}
		if(result){
			
			return R.toDataAndList(menuStep, returnStepSpecList);
		}
		return R.error();
	}
	/**
	 * 
	 * 简要说明：删除菜单步骤
	 * 编写者：陈骑元
	 * 创建时间：2019年8月29日 下午2:41:28
	 * @param 说明
	 * @return 说明
	 */
	@RequestMapping("removeMenuStep")
	@ResponseBody
	public R removeMenuStep(String id){
		MenuStep menuStep=menuStepService.selectById(id);
		String menuIndexId=menuStep.getMenuIndexId();
		Dto removeDto=Dtos.newDto();
		removeDto.put("step_index_id", id);
		boolean result=menuStepService.deleteByMap(removeDto);
		if(result){
			stepSpecService.deleteByMap(removeDto);
			EntityWrapper<MenuStep> wrapper=new EntityWrapper<MenuStep>();
			wrapper.eq("menu_index_id", menuIndexId);
			wrapper.eq("language_type", BussCons.LANGUAGE_TYPE_JP);
			int count=menuStepService.selectCount(wrapper);
			if(count==0){ //如果等于0去除规格
				MenuDict menuDict=new MenuDict();
				menuDict.setWhetherSpec(WebplusCons.WHETHER_NO);
				EntityWrapper<MenuDict> dictWrapper=new EntityWrapper<MenuDict>();
				dictWrapper.eq("menu_index_id", menuIndexId);
				menuDictService.update(menuDict, dictWrapper);
			}
			return R.ok();
		}
		return R.error();
	}
	/**
	 * 
	 * 简要说明：删除步骤规格
	 * 编写者：陈骑元
	 * 创建时间：2019年8月29日 下午2:41:28
	 * @param 说明
	 * @return 说明
	 */
	@RequestMapping("removeStepSpec")
	@ResponseBody
	public R removeStepSpec(String specIndexId){
		Dto removeDto=Dtos.newDto();
		removeDto.put("spec_index_id",specIndexId);
		boolean result=stepSpecService.deleteByMap(removeDto);
		if(result){
		
			return R.ok();
		}
		return R.error();
	}
	/**
	 * 
	 * 简要说明：展示步骤规范
	 * 编写者：陈骑元
	 * 创建时间：2019年8月29日 下午4:26:38
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("showStepSpec")
	@ResponseBody
	public R showStepSpec(String stepIndexId,String languageType){
		EntityWrapper<MenuStep> menuStepWrapper=new EntityWrapper<MenuStep>();
		menuStepWrapper.eq("step_index_id", stepIndexId);
		menuStepWrapper.eq("language_type", languageType);
		MenuStep menuStep=menuStepService.selectOne(menuStepWrapper);
		if(BussCons.LANGUAGE_TYPE_JP.equals(languageType)){
			EntityWrapper<StepSpec> specWrapper = new EntityWrapper<StepSpec>();
			specWrapper.eq("step_index_id",stepIndexId);
			specWrapper.eq("language_type", languageType);
			specWrapper.orderBy("sort_no", true);
			List<StepSpec> stepSpecList=stepSpecService.selectList(specWrapper);
			return R.toDataAndList(menuStep,  stepSpecList);
			
		}else{
			if(WebplusUtil.isEmpty(menuStep)){
				EntityWrapper<MenuStep> menuStepWrapperJp=new EntityWrapper<MenuStep>();
				menuStepWrapperJp.eq("step_index_id", stepIndexId);
				menuStepWrapperJp.eq("language_type", BussCons.LANGUAGE_TYPE_JP);
				menuStep=menuStepService.selectOne(menuStepWrapperJp);
				menuStep.setStepId("");
				EntityWrapper<StepSpec> specWrapper = new EntityWrapper<StepSpec>();
				specWrapper.eq("step_index_id",stepIndexId);
				specWrapper.eq("language_type",BussCons.LANGUAGE_TYPE_JP);
				specWrapper.orderBy("sort_no", true);
				List<StepSpec> stepSpecList=stepSpecService.selectList(specWrapper);
				return R.toDataAndList(menuStep,  stepSpecList);
			}else{
				EntityWrapper<StepSpec> specWrapper = new EntityWrapper<StepSpec>();
				specWrapper.eq("step_index_id",stepIndexId);
				specWrapper.eq("language_type", languageType);
				specWrapper.orderBy("sort_no", true);
				List<StepSpec> stepSpecList=stepSpecService.selectList(specWrapper);
				EntityWrapper<StepSpec> specJpWrapper = new EntityWrapper<StepSpec>();
				specJpWrapper.eq("step_index_id",stepIndexId);
				specJpWrapper.eq("language_type",BussCons.LANGUAGE_TYPE_JP);
				specJpWrapper.orderBy("sort_no", true);
				List<StepSpec> stepSpecJpList=stepSpecService.selectList(specJpWrapper);
				if(WebplusUtil.isEmpty(stepSpecList)){
					List<StepSpec> stepSpecListNew=Lists.newArrayList();
					for(StepSpec stepSpec:stepSpecJpList){
						stepSpec.setSpecId("");
						stepSpecListNew.add(stepSpec);
					}
					return R.toDataAndList(menuStep, stepSpecListNew);
				}else{
					if(stepSpecJpList.size()>stepSpecList.size()){
						for(StepSpec stepSpec:stepSpecJpList){
							if(!checkExistSpec(stepSpec.getSpecIndexId(), stepSpecList)){
								stepSpec.setSpecId("");
								stepSpecList.add(stepSpec);
								
							}
						}
						return R.toDataAndList(menuStep, stepSpecList);
					}
					
					return R.toDataAndList(menuStep, stepSpecList);
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
	private boolean checkExistSpec(String specIndexId,List<StepSpec> stepSpecList){
	    for(StepSpec stepSpec:stepSpecList){
	    	if(specIndexId.equals(stepSpec.getSpecIndexId())){
	    		
	    		return true;
	    	}
	    }
		
		return false;
	}
	/**
	 * 
	 * 简要说明：修改信息
	 * 编写者：陈骑元
	 * 创建时间：2019-07-20
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("updateMenuStatus")
	@ResponseBody
	public R updateMenuStatus(String menuId,String menuStatus) {
		MenuDict entity=new MenuDict();
		entity.setMenuStatus(menuStatus);
		EntityWrapper<MenuDict> wrapper=new EntityWrapper<MenuDict>();
		wrapper.eq("menu_index_id", menuId);
		boolean result=menuDictService.update(entity, wrapper);
		if (result) {
			return R.ok();
		} else {
			return R.error();
		}
		
	}
	/**
	 * 
	 * 简要说明：删除照片
	 * 编写者：陈骑元
	 * 创建时间：2019年11月11日 下午11:51:46
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("removeMenuImage")
	@ResponseBody
	public R removeMenuImage(String menuIndexId){
		MenuDict entity=new MenuDict();
		entity.setMenuImage("");
		
		EntityWrapper<MenuDict> wrapper=new EntityWrapper<MenuDict>();
		wrapper.eq("menu_index_id", menuIndexId);
		menuDictService.update(entity, wrapper);
		
		return R.ok();
	}
	
	/**
	 * 
	 * 简要说明：删除照片
	 * 编写者：陈骑元
	 * 创建时间：2019年11月11日 下午11:51:46
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("resetSort")
	@ResponseBody
	public R resetSort(String catalogIndexId){
		EntityWrapper<MenuDict> wrapper=new EntityWrapper<MenuDict>();
		wrapper.eq("catalog_index_id", catalogIndexId);
		wrapper.orderBy("sort_no ASC, update_time DESC");
		wrapper.eq("language_type", BussCons.LANGUAGE_TYPE_JP);
		List<MenuDict> menuDictList=menuDictService.selectList(wrapper);
		if(WebplusUtil.isNotEmpty(menuDictList)) {
			for(int i=0;i<menuDictList.size();i++) {
				MenuDict menuDictEntity=menuDictList.get(i);
				MenuDict entity=new MenuDict();
				entity.setSortNo(i+1);
				EntityWrapper<MenuDict> updateWrapper = new EntityWrapper<MenuDict>();
				updateWrapper.eq("catalog_index_id", catalogIndexId);
				updateWrapper.eq("menu_index_id", menuDictEntity.getMenuIndexId());
				menuDictService.update(entity, updateWrapper);
			}
		}
		return R.ok();
	}
	
}

