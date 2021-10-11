package com.ims.buss.system;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.ims.buss.model.MenuCatalog;
import com.ims.buss.model.MenuLarge;
import com.ims.buss.service.MenuCatalogService;
import com.ims.buss.service.MenuLargeService;
import com.ims.core.matatype.Dto;
import com.ims.core.matatype.Dtos;
import com.ims.core.util.WebplusFormater;
import com.ims.core.util.WebplusUtil;
import com.ims.core.vo.R;
import com.ims.core.web.BaseController;

/**
 * <p>
 * 菜品大类 前端控制器
 * </p>
 *
 * @author 陈骑元
 * @since 2020-07-26
 */
@Controller
@RequestMapping("/buss/menuLarge")
public class MenuLargeController extends BaseController {

    @Autowired
    private MenuLargeService menuLargeService;
    @Autowired
    private MenuCatalogService menuCatalogService;
    


	/**
	 * 
	 * 简要说明：分页查询 
	 * 编写者：陈骑元
	 * 创建时间：2020-07-26
	 * @param 说明
	 * @return 说明
	 */
	@RequestMapping("list")
	@ResponseBody
	public R list() {
		String shopId=this.getUserId();
		Dto pDto = Dtos.newDto(request);
		pDto.put("shopId", shopId);
		pDto.setOrder(" sort_no ASC ");
		Page<MenuLarge> page =menuLargeService.likePage(pDto);
		return R.toPage(page);
	}



	/**
	 * 
	 * 简要说明： 新增信息保存 
	 * 编写者：陈骑元
	 * 创建时间：2020-07-26
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("save")
	@ResponseBody
	public R save(MenuLarge menuLarge) {
		String shopId=this.getUserId();
		Date now=WebplusUtil.getDateTime();
		EntityWrapper<MenuLarge> wrapper=new EntityWrapper<MenuLarge>();
		wrapper.eq("shop_id",shopId);
		wrapper.eq("large_name", menuLarge.getLargeName());
		int count=menuLargeService.selectCount(wrapper);
		if(count>0) {
			
			return R.warn("大类名称已经存在");
		}
		menuLarge.setShopId(shopId);
		menuLarge.setCreateBy(shopId);
		menuLarge.setCreateTime(now);
		menuLarge.setUpdateBy(shopId);
		menuLarge.setUpdateTime(now);
		Dto calcDto = Dtos.newCalcDto("IFNULL(MAX(sort_no),0)+1");
		calcDto.put("shopId", shopId);
		String maxSortNo =menuLargeService.calc(calcDto);
		if(WebplusUtil.isEmpty(maxSortNo)){
			maxSortNo="1";
		}
		int sortNo=Integer.parseInt(maxSortNo);
		menuLarge.setSortNo(sortNo);
		boolean result = menuLargeService.insert(menuLarge);
		if (result) {
			return R.ok();
		} else {
			return R.error();
		}

	}
	/**
	 * 
	 * 简要说明： 跳转到编辑页面 
	 * 编写者：陈骑元
	 * 创建时间：2020-07-26
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("edit")
	@ResponseBody
	public R edit(String id) {
		MenuLarge menuLarge=menuLargeService.selectById(id);
		return R.toData(menuLarge);
	}
	
	/**
	 * 
	 * 简要说明：修改信息
	 * 编写者：陈骑元
	 * 创建时间：2020-07-26
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("update")
	@ResponseBody
	public R update(MenuLarge menuLarge) {
		String shopId=this.getUserId();
		String largeId=menuLarge.getLargeId();
		MenuLarge entity=menuLargeService.selectById(largeId);
		String largeName=menuLarge.getLargeName();
		if(!entity.getLargeName().equals(largeName)) {
			EntityWrapper<MenuLarge> wrapper=new EntityWrapper<MenuLarge>();
			wrapper.eq("shop_id",shopId);
			wrapper.eq("large_name", menuLarge.getLargeName());
			int count=menuLargeService.selectCount(wrapper);
			if(count>0) {
				
				return R.warn("大类名称已经存在");
			}
		}
		menuLarge.setUpdateBy(shopId);
		menuLarge.setUpdateTime(WebplusUtil.getDateTime());
		boolean result = menuLargeService.updateById(menuLarge);
		if (result) {
			return R.ok();
		} else {
			return R.error();
		}
		
	}
	
	
	/**
	 * 
	 * 简要说明：删除信息
	 * 编写者：陈骑元
	 * 创建时间：2020-07-26
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("remove")
	@ResponseBody
	public R remove(String id) {
		String shopId=this.getUserId();
		boolean result = menuLargeService.deleteById(id);
		if (result) {
			MenuCatalog entity=new MenuCatalog();
			entity.setLargeId("");
			EntityWrapper<MenuCatalog> wrapper=new EntityWrapper<MenuCatalog>();
			wrapper.eq("shop_id", shopId);
			wrapper.eq("large_id", id);
			menuCatalogService.update(entity, wrapper);
			return R.ok();
		} else {
			return R.error("删除失败");
		}
		
	}
	
	/**
	 * 
	 * 简要说明：批量删除信息
	 * 编写者：陈骑元
	 * 创建时间：2020-07-26
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("batchRemove")
	@ResponseBody
	public R batchRemove(String ids) {
		List<String> idList=WebplusFormater.separatStringToList(ids);
		boolean result = menuLargeService.deleteBatchIds(idList);
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
	public R updateSort(String largeId,String sortWay,String whetherBuffet){
		MenuLarge menuLarge=menuLargeService.selectById(largeId);
		String shopId=menuLarge.getShopId();
		int sortNo=menuLarge.getSortNo();
		if("1".equals(sortWay)){  //更新到顶部
			Dto calcDto = Dtos.newCalcDto("IFNULL(MIN(sort_no),0)");
			calcDto.put("shopId", shopId);
			String minSortNoStr=menuLargeService.calc(calcDto);
			int minSortNo=Integer.parseInt(minSortNoStr);
			if(sortNo>minSortNo){
				Dto updateDto=Dtos.newDto();
				updateDto.put("shopId", shopId);
				menuLargeService.updateSortDown(updateDto);
				MenuLarge updateEntity=new MenuLarge();
				updateEntity.setSortNo(minSortNo);
			    updateEntity.setLargeId(largeId);
			    menuLargeService.updateById(updateEntity);
			}
		}else if("2".equals(sortWay)){  //上升1位
			Dto calcDto = Dtos.newCalcDto("IFNULL(MAX(sort_no),0)");
			calcDto.put("shopId", shopId);
			calcDto.put("sortNoLt", sortNo);
			String maxSortNoStr=menuLargeService.calc(calcDto);
			int maxSortNo=Integer.parseInt(maxSortNoStr);
			if(maxSortNo>0){
				MenuLarge entity=new MenuLarge();
				entity.setSortNo(sortNo);
				EntityWrapper<MenuLarge> entityWrapper = new EntityWrapper<MenuLarge>();
				entityWrapper.eq("sort_no", maxSortNo);
				entityWrapper.eq("shop_id",shopId);
				menuLargeService.update(entity, entityWrapper);
				MenuLarge updateEntity=new MenuLarge();
				updateEntity.setSortNo(maxSortNo);
			    updateEntity.setLargeId(largeId);
			    menuLargeService.updateById(updateEntity);
			}
				
		}else if("3".equals(sortWay)){  //下降1位
			Dto calcDto = Dtos.newCalcDto("IFNULL(MIN(sort_no),0)");
			calcDto.put("shopId", shopId);
			calcDto.put("sortNoGt", sortNo);
			String minSortNoStr=menuLargeService.calc(calcDto);
			int minSortNo=Integer.parseInt(minSortNoStr);
			if(minSortNo>0){
				MenuLarge entity=new MenuLarge();
				entity.setSortNo(sortNo);
				EntityWrapper<MenuLarge> entityWrapper = new EntityWrapper<MenuLarge>();
				entityWrapper.eq("sort_no", minSortNo);
				entityWrapper.eq("shop_id",shopId);
				menuLargeService.update(entity, entityWrapper);
				MenuLarge updateEntity=new MenuLarge();
				updateEntity.setSortNo(minSortNo);
				updateEntity.setLargeId(largeId);
			    menuLargeService.updateById(updateEntity);
			}
				
		}else{  //下降到最后1位
			Dto calcDto = Dtos.newCalcDto("IFNULL(MAX(sort_no),0)");
			calcDto.put("shopId", shopId);
			String maxSortNoStr=menuLargeService.calc(calcDto);
			int maxSortNo=Integer.parseInt(maxSortNoStr);
			if(sortNo<maxSortNo){
				Dto updateDto=Dtos.newDto();
				updateDto.put("shopId", shopId);
				updateDto.put("sortNoGt", sortNo);
				menuLargeService.updateSortUp(updateDto);
				MenuLarge updateEntity=new MenuLarge();
				updateEntity.setSortNo(maxSortNo);
			    updateEntity.setLargeId(largeId);
			    menuLargeService.updateById(updateEntity);
			}
		}
		 
		return R.ok();
			
	}
	/**
	 * 
	 * 简要说明：
	 * 编写者：陈骑元（chenqiyuan@toonan.com）
	 * 创建时间： 2020年7月27日 上午12:24:50 
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("listMenuLarge")
	@ResponseBody
	public R listMenuLarge() {
		String shopId=this.getUserId();
		Dto pDto = Dtos.newDto(request);
		pDto.put("shopId", shopId);
		pDto.setOrder(" sort_no ASC ");
		List<MenuLarge> menuLargeList=menuLargeService.list(pDto);
		
		return R.toList(menuLargeList);
	}
}

