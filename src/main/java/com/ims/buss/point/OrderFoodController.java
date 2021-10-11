package com.ims.buss.point;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.plugins.Page;
import com.ims.buss.constant.BussCons;
import com.ims.buss.model.MemberAddress;
import com.ims.buss.model.Order;
import com.ims.buss.model.Shop;
import com.ims.buss.service.MemberAddressService;
import com.ims.buss.service.OrderCommonService;
import com.ims.buss.service.OrderService;
import com.ims.buss.service.ShopService;
import com.ims.buss.util.BussCache;
import com.ims.core.cache.WebplusCache;
import com.ims.core.constant.WebplusCons;
import com.ims.core.matatype.Dto;
import com.ims.core.matatype.Dtos;
import com.ims.core.util.WebplusUtil;
import com.ims.core.vo.Item;
import com.ims.core.vo.R;
import com.ims.core.web.BaseController;

/**
 * 
 * @ClassName:  OrderFoodController   
 * @Description:订餐外卖
 * @author: 陈骑元（chenqiyuan@toonan.com)
 * @date:   2020年5月4日 上午7:06:44     
 * @Copyright: 2020 www.toonan.com Inc. All rights reserved. 
 * 注意：本内容仅限于广州市图南软件有限公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Controller
@RequestMapping("/point/orderFood")
public class OrderFoodController  extends BaseController {
	
	@Autowired
	private  ShopService shopService;
	
	@Autowired
	private OrderCommonService orderCommonService;
	@Autowired
	private MemberAddressService memberAddressService;
	@Autowired
	private OrderService orderService;
	/**
	 * 
	 * 简要说明：获取店铺详情
	 * 编写者：陈骑元（chenqiyuan@toonan.com）
	 * 创建时间： 2020年5月4日 上午8:16:26 
	 * @param 说明
	 * @return 说明
	 */
	@GetMapping("getShopDetail")
	@ResponseBody
    public R getShopDetail(String shopId) {
		
		Shop shop=shopService.selectById(shopId);
		
		
		return R.toData(shop);
    	
    }
	
	/**
	 * 
	 * 简要说明：获取外卖订单
	 * 编写者：陈骑元（chenqiyuan@toonan.com）
	 * 创建时间： 2020年4月25日 下午9:10:12 
	 * @param 说明
	 * @return 说明
	 */
	@GetMapping("listMenuDict")
	@ResponseBody
	public R listMenuDict(String shopId) {
		if(WebplusUtil.isEmpty(shopId)) {
			
			return R.error("The store number cannot be empty");
		}
		List<Dto> dataList=orderCommonService.listMenuDict(shopId);
		
		return R.toList(dataList);
	} 
	/**
	 * 
	 * 简要说明：查询产品规格
	 * 编写者：陈骑元
	 * 创建时间：2019年8月2日 下午10:54:12
	 * @param 说明
	 * @return 说明
	 */
	@GetMapping("listMenuStepSpec")
	@ResponseBody
	public R listMenuStepSpec(String menuIndexId){
		
		return orderCommonService.listMenuStepSpec(menuIndexId, BussCons.LANGUAGE_TYPE_JP);
	}
	/**
	 * 
	 * 简要说明：保存外卖订单
	 * 编写者：陈骑元（chenqiyuan@toonan.com）
	 * 创建时间： 2020年5月4日 上午7:18:07 
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("saveOrderFood")
	@ResponseBody
	public R saveOrderFood() {
		String memberId=this.getUserId();
		Dto pDto=Dtos.newDto(request);
		pDto.put("memberId", memberId);
		pDto.put("orderType", BussCons.ORDER_TYPE_NETWORK);
		R r=orderCommonService.saveOrder(pDto);
		return r;
	}
	
	/**
	 * 
	 * 简要说明：获取会员地址信息
	 * 编写者：陈骑元（chenqiyuan@toonan.com）
	 * 创建时间： 2020年4月25日 上午10:09:26 
	 * @param 说明
	 * @return 说明
	 */
	@GetMapping("listMemberAddress")
	@ResponseBody
	public R listMemberAddress() {
		String memberId=this.getUserId();
		Dto pDto=Dtos.newDto();
		pDto.put("memberId", memberId);
		pDto.put("whetherRemove", WebplusCons.WHETHER_NO);
		pDto.setOrder("create_time DESC ");
		List<MemberAddress> addressList=memberAddressService.listMemberAddress(pDto);
		WebplusCache.convertItem(addressList);
		for(MemberAddress memberAddress:addressList ) {
			String fullAddress=memberAddress.getExtMap().get("province_dict")+""
		  +memberAddress.getExtMap().get("city_dict")+memberAddress.getExtMap().get("town_dict")+memberAddress.getDetailAddress();
			memberAddress.setFullAddress(fullAddress);
		}
		
		return R.toList(addressList);
	}

	/**
	 * 
	 * 简要说明：获取省市区信息
	 * 编写者：陈骑元（chenqiyuan@toonan.com）
	 * 创建时间： 2020年4月25日 上午10:00:25 
	 * @param 说明
	 * @return 说明
	 */
	@GetMapping("listPosition")
	@ResponseBody
	public R  listPosition(String typeCode) {
		List<Item> itemList=BussCache.getPositionList(typeCode);
		return R.toList(itemList);
	}
	/**
	 * 
	 * 简要说明：保存会员地址
	 * 编写者：陈骑元（chenqiyuan@toonan.com）
	 * 创建时间： 2020年5月4日 上午8:20:34 
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("saveMemberAddress")
	@ResponseBody
	public R saveMemberAddress(MemberAddress memberAddress) {
		String  memberId=this.getUserId();
		memberAddress.setMemberId(memberId);
		R verifyR=memberAddressService.verifyMemberAddress(memberAddress);
		if(WebplusCons.ERROR==verifyR.getAppCode()) {
			 return verifyR;
		}
		memberAddress.setWhetherRemove(WebplusCons.WHETHER_NO);
		memberAddress.setCreateTime(WebplusUtil.getDateTime());
	    boolean result=memberAddressService.insert(memberAddress);
	    if(result) {
	    	
	    	return R.ok();
	    }
		return R.error();
	}
	

	/**
	 * 
	 * 简要说明：编辑会员地址
	 * 编写者：陈骑元（chenqiyuan@toonan.com）
	 * 创建时间： 2020年4月25日 下午12:06:54 
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("updateMemberAddress")
	@ResponseBody
	public R updateMemberAddress(MemberAddress memberAddress) {
		
		return memberAddressService.updateMemberAddress(memberAddress);
	}
	
	/**
	 * 
	 * 简要说明：删除会员地址
	 * 编写者：陈骑元（chenqiyuan@toonan.com）
	 * 创建时间： 2020年4月25日 下午12:06:54 
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("removeMemberAddress")
	@ResponseBody
	public R removeMemberAddress(String addressId) {
		if(WebplusUtil.isEmpty(addressId)) {
		
			 return R.error("地址编号addressId不能为空");
		}
		MemberAddress memberAddress=new MemberAddress();
		memberAddress.setAddressId(addressId);
		memberAddress.setWhetherRemove(WebplusCons.WHETHER_YES);
		memberAddressService.updateById(memberAddress);
		return R.ok();
	}
	/**
	 * 
	 * 简要说明：
	 * 编写者：陈骑元（chenqiyuan@toonan.com）
	 * 创建时间： 2020年5月5日 上午7:17:39 
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("listMemberOrderPage")
	@ResponseBody
	public R listMemberOrderPage(String currentPage,String pageSize) {
		String userId=this.getUserId();
		Dto pDto=Dtos.newPage(currentPage,pageSize);
		pDto.put("memberId", userId);
		pDto.setOrder(" a.order_time DESC ");
		Page<Order> page=orderService.listMemberOrderPage( pDto);
		WebplusCache.convertItem(page);
		return R.toApiPage(page);
		
	}
		
	/**
	 * 
	 * 简要说明：获取订单详情
	 * 编写者：陈骑元（chenqiyuan@toonan.com）
	 * 创建时间： 2020年4月25日 下午4:37:45 
	 * @param 说明
	 * @return 说明
	 */
	@GetMapping("getOrderDetail")
	@ResponseBody
	public R getOrderDetail(String orderId) {
		 
		return orderCommonService.getOrderDetail(orderId, WebplusCons.WHETHER_NO);
	}
}
