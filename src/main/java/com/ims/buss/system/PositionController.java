package com.ims.buss.system;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.baomidou.mybatisplus.plugins.Page;
import com.ims.core.cache.WebplusCache;
import com.ims.core.matatype.Dto;
import com.ims.core.matatype.Dtos;
import com.ims.core.util.WebplusFormater;
import com.ims.core.vo.R;
import java.util.List;
import com.ims.buss.model.Position;
import com.ims.buss.service.PositionService;
import com.ims.buss.util.BussCache;

import org.springframework.stereotype.Controller;
import com.ims.core.web.BaseController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 陈骑元
 * @since 2020-04-23
 */
@Controller
@RequestMapping("/buss/position")
public class PositionController extends BaseController {

    @Autowired
    private PositionService positionService;


	/**
	 * 
	 * 简要说明：分页查询 
	 * 编写者：陈骑元
	 * 创建时间：2020-04-23
	 * @param 说明
	 * @return 说明
	 */
	@RequestMapping("list")
	@ResponseBody
	public R list() {
		Dto pDto = Dtos.newDto(request);
		pDto.setOrder("position_type ASC,position_code ASC");
		Page<Position> page =positionService.likePage(pDto);
		WebplusCache.convertItem(page);
		return R.toPage(page);
	}



	/**
	 * 
	 * 简要说明： 新增信息保存 
	 * 编写者：陈骑元
	 * 创建时间：2020-04-23
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("save")
	@ResponseBody
	public R save(Position position) {
		boolean result = positionService.insert(position);
		if (result) {
			return R.ok();
		} else {
			return R.error("保存失败");
		}

	}
	/**
	 * 
	 * 简要说明： 跳转到编辑页面 
	 * 编写者：陈骑元
	 * 创建时间：2020-04-23
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("edit")
	@ResponseBody
	public R edit(String id) {
		Position position=positionService.selectById(id);
		return R.toData(position);
	}
	
	/**
	 * 
	 * 简要说明：修改信息
	 * 编写者：陈骑元
	 * 创建时间：2020-04-23
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("update")
	@ResponseBody
	public R update(Position position) {
		boolean result = positionService.updateById(position);
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
	 * 创建时间：2020-04-23
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("remove")
	@ResponseBody
	public R remove(String id) {
		boolean result = positionService.deleteById(id);
		if (result) {
			return R.ok();
		} else {
			return R.error("删除失败");
		}
		
	}
	
	/**
	 * 
	 * 简要说明：批量删除信息
	 * 编写者：陈骑元
	 * 创建时间：2020-04-23
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("batchRemove")
	@ResponseBody
	public R batchRemove(String ids) {
		List<String> idList=WebplusFormater.separatStringToList(ids);
		boolean result = positionService.deleteBatchIds(idList);
		if (result) {
			return R.ok();
		} else {
			return R.error("删除失败");
		}
		
	}
	
	/**
	 * 
	 * 简要说明：刷新区域缓存
	 * 编写者：陈骑元
	 * 创建时间：2018年5月13日 下午11:09:04
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("refreshCache")
	@ResponseBody
	public R refreshCache() {
	     BussCache.refreshPosition();
	    
		return R.ok("刷新区域缓存操作成功");
	}
}

