package com.ims.buss.system;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.ims.core.matatype.Dto;
import com.ims.core.matatype.Dtos;
import com.ims.core.util.WebplusFormater;
import com.ims.core.util.WebplusSqlHelp;
import com.ims.core.util.WebplusUtil;
import com.ims.core.vo.R;
import java.util.List;
import com.ims.buss.model.Area;
import com.ims.buss.model.Desk;
import com.ims.buss.service.AreaService;
import com.ims.buss.service.DeskService;

import org.springframework.stereotype.Controller;
import com.ims.core.web.BaseController;

/**
 * <p>
 * 区域表 前端控制器
 * </p>
 *
 * @author 陈骑元
 * @since 2019-10-14
 */
@Controller
@RequestMapping("/buss/area")
public class AreaController extends BaseController {

    @Autowired
    private AreaService areaService;
    @Autowired
    private DeskService deskService;


	/**
	 * 
	 * 简要说明：分页查询 
	 * 编写者：陈骑元
	 * 创建时间：2019-10-14
	 * @param 说明
	 * @return 说明
	 */
	@RequestMapping("list")
	@ResponseBody
	public R list() {
		String userId=this.getUserId();
		Dto pDto = Dtos.newDto(request);
		pDto.put("shopId", userId);
		pDto.setOrder( " sort_no ASC ");
		Page<Area> page =areaService.likePage(pDto);
		return R.toPage(page);
	}



	/**
	 * 
	 * 简要说明： 新增信息保存 
	 * 编写者：陈骑元
	 * 创建时间：2019-10-14
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("save")
	@ResponseBody
	public R save(Area area) {
		String userId=this.getUserId();
		EntityWrapper<Area> countWrapper = new EntityWrapper<Area>();
		WebplusSqlHelp.eq(countWrapper, "area_name",area.getAreaName());
		WebplusSqlHelp.eq(countWrapper, "shop_id",userId);
		int count=areaService.selectCount(countWrapper);
		if(count>0){
			
			return R.warn("区域已经存在。");
		}
		Dto calcDto = Dtos.newCalcDto("IFNULL(MAX(sort_no),0)+1");
		calcDto.put("shopId", userId);
		String maxSortNo =areaService.calc(calcDto);
		if(WebplusUtil.isEmpty(maxSortNo)){
			maxSortNo="1";
		}
		area.setSortNo(Integer.parseInt(maxSortNo));
		area.setShopId(userId);
		area.setCreateBy(userId);
		area.setUpdateBy(userId);
		area.setCreateTime(WebplusUtil.getDateTime());
		area.setUpdateTime(WebplusUtil.getDateTime());
		boolean result = areaService.insert(area);
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
	 * 创建时间：2019-10-14
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("edit")
	@ResponseBody
	public R edit(String id) {
		Area area=areaService.selectById(id);
		return R.toData(area);
	}
	
	/**
	 * 
	 * 简要说明：修改信息
	 * 编写者：陈骑元
	 * 创建时间：2019-10-14
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("update")
	@ResponseBody
	public R update(Area area) {
		String userId = this.getUserId();
		Area entity = areaService.selectById(area.getAreaId());
		if (!entity.getAreaName().equals(area.getAreaName())) {
			EntityWrapper<Area> countWrapper = new EntityWrapper<Area>();
			WebplusSqlHelp.eq(countWrapper, "area_name", area.getAreaName());
			WebplusSqlHelp.eq(countWrapper, "shop_id", userId);
			int count = areaService.selectCount(countWrapper);
			if (count > 0) {

				return R.warn("区域已存在。");
			}
		}
		area.setUpdateBy(userId);
		area.setUpdateTime(WebplusUtil.getDateTime());
		boolean result = areaService.updateById(area);
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
	 * 创建时间：2019-10-14
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("remove")
	@ResponseBody
	public R remove(String id) {
		boolean result = areaService.deleteById(id);
		if (result) {
			Desk desk=new Desk();
			desk.setAreaId("");
			EntityWrapper<Desk> wrapper=new EntityWrapper<Desk>();
			wrapper.eq("area_id",id);
			deskService.update(desk, wrapper);
			return R.ok();
		} else {
			return R.error();
		}
		
	}
	
	/**
	 * 
	 * 简要说明：批量删除信息
	 * 编写者：陈骑元
	 * 创建时间：2019-10-14
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("batchRemove")
	@ResponseBody
	public R batchRemove(String ids) {
		List<String> idList=WebplusFormater.separatStringToList(ids);
		boolean result = areaService.deleteBatchIds(idList);
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
		Area area=areaService.selectById(id);
		int sortNo=area.getSortNo();
		String shopId=area.getShopId();
		if("1".equals(sortWay)){  //更新到顶部
			Dto calcDto = Dtos.newCalcDto("IFNULL(MIN(sort_no),0)");
			calcDto.put("shopId", shopId);
			String minSortNoStr=areaService.calc(calcDto);
			int minSortNo=Integer.parseInt(minSortNoStr);
			if(sortNo>minSortNo){
				Dto updateDto=Dtos.newDto();
				updateDto.put("shopId", shopId);
				updateDto.put("sortNoLt", sortNo);
				areaService.updateSortDown(updateDto);
				area.setSortNo(minSortNo);
				areaService.updateById(area);
			}
		}else if("2".equals(sortWay)){  //上升1位
			Dto calcDto = Dtos.newCalcDto("IFNULL(MAX(sort_no),0)");
			calcDto.put("shopId", shopId);
			calcDto.put("sortNoLt", sortNo);
			String maxSortNoStr=areaService.calc(calcDto);
			int maxSortNo=Integer.parseInt(maxSortNoStr);
			if(maxSortNo>0){
				Area entity=new Area();
				entity.setSortNo(sortNo);
				EntityWrapper<Area> updateWrapper = new EntityWrapper<Area>();
				updateWrapper.eq("sort_no", maxSortNo);
				updateWrapper.eq("shop_id", shopId);
				areaService.update(entity, updateWrapper);
				area.setSortNo(maxSortNo);
				areaService.updateById(area);
			}
				
		}else if("3".equals(sortWay)){  //下降1位
			Dto calcDto = Dtos.newCalcDto("IFNULL(MIN(sort_no),0)");
			calcDto.put("shopId", shopId);
			calcDto.put("sortNoGt", sortNo);
			String minSortNoStr=areaService.calc(calcDto);
			int minSortNo=Integer.parseInt(minSortNoStr);
			if(minSortNo>0){
				Area entity=new Area();
				entity.setSortNo(sortNo);
				EntityWrapper<Area> updateWrapper = new EntityWrapper<Area>();
				updateWrapper.eq("sort_no", minSortNo);
				updateWrapper.eq("shop_id", shopId);
				areaService.update(entity, updateWrapper);
				area.setSortNo(minSortNo);
				areaService.updateById(area);
			}
				
		}else{  //下降到最后1位
			Dto calcDto = Dtos.newCalcDto("IFNULL(MAX(sort_no),0)");
			calcDto.put("shopId", shopId);
			String maxSortNoStr=areaService.calc(calcDto);
			int maxSortNo=Integer.parseInt(maxSortNoStr);
			if(sortNo<maxSortNo){
				Dto updateDto=Dtos.newDto();
				updateDto.put("shopId", shopId);
				updateDto.put("sortNoGt", sortNo);
				areaService.updateSortUp(updateDto);
				area.setSortNo(maxSortNo);
				areaService.updateById(area);
			}
		}
		 
		return R.ok();
			
	}
	
	/**
	 * 
	 * 简要说明：更新排序方式
	 * 编写者：陈骑元
	 * 创建时间：2019年7月24日 下午10:22:46
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("queryAreaList")
	@ResponseBody
	public R queryAreaList(){
		String shopId=this.getUserId();
		List<Area> areaList= areaService.queryAreaList(shopId);
		return R.toList(areaList);
	}
}

