package com.ims.buss.system;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.ims.core.constant.WebplusCons;
import com.ims.core.matatype.Dto;
import com.ims.core.matatype.Dtos;
import com.ims.core.util.WebplusFormater;
import com.ims.core.util.WebplusUtil;
import com.ims.core.vo.R;
import java.util.List;
import com.ims.buss.model.ShopStaff;
import com.ims.buss.service.ShopStaffService;
import org.springframework.stereotype.Controller;
import com.ims.core.web.BaseController;

/**
 * <p>
 * 店铺员工 前端控制器
 * </p>
 *
 * @author 陈骑元
 * @since 2020-04-27
 */
@Controller
@RequestMapping("/buss/shopStaff")
public class ShopStaffController extends BaseController {

    @Autowired
    private ShopStaffService shopStaffService;


	/**
	 * 
	 * 简要说明：分页查询 
	 * 编写者：陈骑元
	 * 创建时间：2020-04-27
	 * @param 说明
	 * @return 说明
	 */
	@RequestMapping("list")
	@ResponseBody
	public R list() {
		String shopId=this.getUserId();
		Dto pDto = Dtos.newDto(request);
		pDto.put("shopId", shopId);
		pDto.setOrder("create_time DESC ");
		Page<ShopStaff> page =shopStaffService.likePage(pDto);
		return R.toPage(page);
	}



	/**
	 * 
	 * 简要说明： 新增信息保存 
	 * 编写者：陈骑元
	 * 创建时间：2020-04-27
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("save")
	@ResponseBody
	public R save(ShopStaff shopStaff) {
		String shopId=this.getUserId();
		EntityWrapper<ShopStaff> wrapper=new EntityWrapper<ShopStaff>();
		wrapper.eq("whether_remove", WebplusCons.WHETHER_NO	);
		wrapper.eq("staff_num", shopStaff.getStaffNum());
		wrapper.eq("shop_id", shopId);
		int count=shopStaffService.selectCount(wrapper);
		if(count>0) {	
			
			return R.warn("该员工号已经存在");
		}
		shopStaff.setShopId(shopId);
		shopStaff.setCreateBy(shopId);
		shopStaff.setUpdateBy(shopId);
		shopStaff.setCreateTime(WebplusUtil.getDateTime());
		shopStaff.setUpdateTime(WebplusUtil.getDateTime());
		boolean result = shopStaffService.insert(shopStaff);
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
	 * 创建时间：2020-04-27
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("edit")
	@ResponseBody
	public R edit(String id) {
		ShopStaff shopStaff=shopStaffService.selectById(id);
		return R.toData(shopStaff);
	}
	
	/**
	 * 
	 * 简要说明：修改信息
	 * 编写者：陈骑元
	 * 创建时间：2020-04-27
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("update")
	@ResponseBody
	public R update(ShopStaff shopStaff) {
		String shopId=this.getUserId();
		ShopStaff entity=shopStaffService.selectById(shopStaff.getStaffId());
		if(!entity.getStaffNum().equals(shopStaff.getStaffNum())) {
			EntityWrapper<ShopStaff> wrapper=new EntityWrapper<ShopStaff>();
			wrapper.eq("whether_remove", WebplusCons.WHETHER_NO	);
			wrapper.eq("staff_num", shopStaff.getStaffNum());
			wrapper.eq("shop_id", shopId);
			int count=shopStaffService.selectCount(wrapper);
			if(count>0) {	
				
				return R.warn("该员工号已经存在");
			}
		}
		shopStaff.setUpdateBy(shopId);
		shopStaff.setUpdateTime(WebplusUtil.getDateTime());
		boolean result = shopStaffService.updateById(shopStaff);
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
	 * 创建时间：2020-04-27
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("remove")
	@ResponseBody
	public R remove(String id) {
		String shopId=this.getUserId();
		ShopStaff shopStaff=new ShopStaff();
		shopStaff.setStaffId(id);
		shopStaff.setWhetherRemove(WebplusCons.WHETHER_YES);
		shopStaff.setUpdateBy(shopId);
		shopStaff.setUpdateTime(WebplusUtil.getDateTime());
		boolean result = shopStaffService.deleteById(id);
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
	 * 创建时间：2020-04-27
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("batchRemove")
	@ResponseBody
	public R batchRemove(String ids) {
		List<String> idList=WebplusFormater.separatStringToList(ids);
		boolean result = shopStaffService.deleteBatchIds(idList);
		if (result) {
			return R.ok();
		} else {
			return R.error("删除失败");
		}
		
	}
	
}

