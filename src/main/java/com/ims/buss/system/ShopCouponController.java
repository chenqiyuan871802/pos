package com.ims.buss.system;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.baomidou.mybatisplus.plugins.Page;
import com.ims.core.cache.WebplusCache;
import com.ims.core.constant.WebplusCons;
import com.ims.core.matatype.Dto;
import com.ims.core.matatype.Dtos;
import com.ims.core.util.WebplusFormater;
import com.ims.core.util.WebplusUtil;
import com.ims.core.vo.R;
import java.util.List;

import com.ims.buss.constant.BussCons;
import com.ims.buss.model.ShopCoupon;
import com.ims.buss.service.ShopCouponService;
import org.springframework.stereotype.Controller;
import com.ims.core.web.BaseController;

/**
 * <p>
 * 优惠券 前端控制器
 * </p>
 *
 * @author 陈骑元
 * @since 2020-05-01
 */
@Controller
@RequestMapping("/buss/shopCoupon")
public class ShopCouponController extends BaseController {

    @Autowired
    private ShopCouponService shopCouponService;


	/**
	 * 
	 * 简要说明：分页查询 
	 * 编写者：陈骑元
	 * 创建时间：2020-05-01
	 * @param 说明
	 * @return 说明
	 */
	@RequestMapping("list")
	@ResponseBody
	public R list() {
		String shopId=this.getUserId();
		Dto pDto = Dtos.newDto(request);
		pDto.put("shopId", shopId);
		pDto.put("whetherRemove", WebplusCons.WHETHER_NO);
		pDto.setOrder("create_time DESC ");
		Page<ShopCoupon> page =shopCouponService.likePage(pDto);
		WebplusCache.convertItem(page);
		return R.toPage(page);
	}



	/**
	 * 
	 * 简要说明： 新增信息保存 
	 * 编写者：陈骑元
	 * 创建时间：2020-05-01
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("save")
	@ResponseBody
	public R save(ShopCoupon shopCoupon) {
		String shopId=this.getUserId();
		shopCoupon.setShopId(shopId);
		shopCoupon.setCreateBy(shopId);
		shopCoupon.setCreateTime(WebplusUtil.getDateTime());
		shopCoupon.setUpdateBy(shopId);
		shopCoupon.setUpdateTime(WebplusUtil.getDateTime());
		shopCoupon.setCouponStatus(BussCons.COUPON_STATUS_WAIT);
		boolean result = shopCouponService.insert(shopCoupon);
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
	 * 创建时间：2020-05-01
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("edit")
	@ResponseBody
	public R edit(String id) {
		ShopCoupon shopCoupon=shopCouponService.selectById(id);
		return R.toData(shopCoupon);
	}
	
	/**
	 * 
	 * 简要说明：修改信息
	 * 编写者：陈骑元
	 * 创建时间：2020-05-01
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("update")
	@ResponseBody
	public R update(ShopCoupon shopCoupon) {
		String shopId=this.getUserId();
		shopCoupon.setUpdateBy(shopId);
		shopCoupon.setUpdateTime(WebplusUtil.getDateTime());
		boolean result = shopCouponService.updateById(shopCoupon);
		if (result) {
			return R.ok();
		} else {
			return R.error("更新失败");
		}
		
	}
	/**
	 * 
	 * 简要说明：
	 * 编写者：陈骑元（chenqiyuan@toonan.com）
	 * 创建时间： 2020年5月4日 下午10:07:11 
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("updateStatus")
	@ResponseBody
	public R updateStatus(String couponId,String couponStatus) {
		String shopId=this.getUserId();
		ShopCoupon shopCoupon=new ShopCoupon();
		shopCoupon.setCouponId(couponId);
		shopCoupon.setCouponStatus(couponStatus);
		shopCoupon.setUpdateBy(shopId);
		shopCoupon.setUpdateTime(WebplusUtil.getDateTime());
		boolean result=shopCouponService.updateById(shopCoupon);
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
	 * 创建时间：2020-05-01
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("remove")
	@ResponseBody
	public R remove(String id) {
		String shopId=this.getUserId();
		ShopCoupon shopCoupon=new ShopCoupon();
		shopCoupon.setCouponId(id);
		shopCoupon.setWhetherRemove(WebplusCons.WHETHER_YES	);
		shopCoupon.setUpdateBy(shopId);
		shopCoupon.setUpdateTime(WebplusUtil.getDateTime());
		boolean result=shopCouponService.updateById(shopCoupon);
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
	 * 创建时间：2020-05-01
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("batchRemove")
	@ResponseBody
	public R batchRemove(String ids) {
		List<String> idList=WebplusFormater.separatStringToList(ids);
		boolean result = shopCouponService.deleteBatchIds(idList);
		if (result) {
			return R.ok();
		} else {
			return R.error("删除失败");
		}
		
	}
	
}

