package com.ims.buss.system;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.baomidou.mybatisplus.plugins.Page;
import com.ims.core.matatype.Dto;
import com.ims.core.matatype.Dtos;
import com.ims.core.util.WebplusFormater;
import com.ims.core.vo.R;
import java.util.List;
import com.ims.buss.model.MenuStep;
import com.ims.buss.service.MenuStepService;
import org.springframework.stereotype.Controller;
import com.ims.core.web.BaseController;

/**
 * <p>
 * 菜品规格步骤 前端控制器
 * </p>
 *
 * @author 陈骑元
 * @since 2019-08-28
 */
@Controller
@RequestMapping("/buss/menuStep")
public class MenuStepController extends BaseController {

    @Autowired
    private MenuStepService menuStepService;


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
		Page<MenuStep> page =menuStepService.likePage(pDto);
		return R.toPage(page);
	}



	/**
	 * 
	 * 简要说明： 新增信息保存 
	 * 编写者：陈骑元
	 * 创建时间：2019-08-28
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("save")
	@ResponseBody
	public R save(MenuStep menuStep) {
		boolean result = menuStepService.insert(menuStep);
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
	 * 创建时间：2019-08-28
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("edit")
	@ResponseBody
	public R edit(String id) {
		MenuStep menuStep=menuStepService.selectById(id);
		return R.toData(menuStep);
	}
	
	/**
	 * 
	 * 简要说明：修改信息
	 * 编写者：陈骑元
	 * 创建时间：2019-08-28
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("update")
	@ResponseBody
	public R update(MenuStep menuStep) {
		boolean result = menuStepService.updateById(menuStep);
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
	 * 创建时间：2019-08-28
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("remove")
	@ResponseBody
	public R remove(String id) {
		boolean result = menuStepService.deleteById(id);
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
	 * 创建时间：2019-08-28
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("batchRemove")
	@ResponseBody
	public R batchRemove(String ids) {
		List<String> idList=WebplusFormater.separatStringToList(ids);
		boolean result = menuStepService.deleteBatchIds(idList);
		if (result) {
			return R.ok();
		} else {
			return R.error("删除失败");
		}
		
	}
	
}

