package com.ims.buss.app;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.google.common.collect.Lists;
import com.ims.buss.constant.BussCons;
import com.ims.buss.model.Member;
import com.ims.buss.model.MemberAddress;
import com.ims.buss.model.Order;
import com.ims.buss.model.OrderLine;
import com.ims.buss.model.Shop;
import com.ims.buss.model.ShopStaff;
import com.ims.buss.service.MemberAddressService;
import com.ims.buss.service.MemberService;
import com.ims.buss.service.OrderCommonService;
import com.ims.buss.service.OrderLineService;
import com.ims.buss.service.OrderService;
import com.ims.buss.service.ShopService;
import com.ims.buss.service.ShopStaffService;
import com.ims.buss.util.BussCache;
import com.ims.buss.util.PointApiUtil;
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
 * @ClassName:  TakeOutController   
 * @Description:外卖实体控制类
 * @author: 陈骑元（chenqiyuan@toonan.com)
 * @date:   2020年4月25日 上午9:28:57     
 * @Copyright: 2020 www.toonan.com Inc. All rights reserved. 
 * 注意：本内容仅限于广州市图南软件有限公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Controller
@RequestMapping("/app/takeOut/")
public class TakeOutController  extends BaseController{
	
	@Autowired
	private ShopStaffService shopStaffService;
	@Autowired
	private MemberAddressService memberAddressService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private OrderLineService orderLineService;
	@Autowired
	private OrderCommonService orderCommonService;

	@Autowired
	private MemberService memberService;
	
	@Autowired
	private ShopService shopService;
    /**
     * 
     * 简要说明：更新店铺营运状态
     * 编写者：陈骑元（chenqiyuan@toonan.com）
     * 创建时间： 2020年5月16日 下午9:27:32 
     * @param 说明
     * @return 说明
     */
	@PostMapping("updateShopStatus")
	@ResponseBody
	public R updateShopStatus(String shopStatus) {
		String shopId=this.getUserId();
		Shop shop=new Shop();
		shop.setShopId(shopId);
		shop.setShopStatus(shopStatus);
		shop.setUpdateBy(shopId);
		shop.setUpdateTime(WebplusUtil.getDateTime());
		boolean result=shopService.updateById(shop);
		if(result) {
			return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 
	 * 简要说明：获取省市区信息
	 * 编写者：陈骑元（chenqiyuan@toonan.com）
	 * 创建时间： 2020年4月25日 上午10:00:25 
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("getPositionList")
	@ResponseBody
	public R  getPositionList(String typeCode) {
		List<Item> itemList=BussCache.getPositionList(typeCode);
		return R.toList(itemList);
	}
	/**
	 * 
	 * 简要说明：通过店员编号获取店员信息
	 * 编写者：陈骑元（chenqiyuan@toonan.com）
	 * 创建时间： 2020年4月25日 上午10:03:02 
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("getShopStaff")
	@ResponseBody
	public R getShopStaff(String staffNum) {
		
		if(WebplusUtil.isEmpty(staffNum)) {
			
			return R.error("店員番号を入力してください");
		}
		String shopId=this.getUserId();
		EntityWrapper<ShopStaff> wrapper=new EntityWrapper<ShopStaff>();
		wrapper.eq("staff_num", staffNum);
		wrapper.eq("shop_id", shopId);
		ShopStaff shopStaff=shopStaffService.selectOne(wrapper);
		if(WebplusUtil.isNotEmpty(shopStaff)) {
			
			return R.toData(shopStaff);
		}
		return R.error("店員番号が間違いました");
		
	}
	/**
	 * 
	 * 简要说明：获取会员地址信息
	 * 编写者：陈骑元（chenqiyuan@toonan.com）
	 * 创建时间： 2020年4月25日 上午10:09:26 
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("getMemberAddressList")
	@ResponseBody
	public R getMemberAddressList(String mobile) {
		if(WebplusUtil.isEmpty(mobile)) {
			
			return R.error("携帯番号を入力してください");
		}
		Dto pDto=Dtos.newDto();
		pDto.put("mobile", mobile);
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
	 * 简要说明：获取网络订单数量
	 * 编写者：陈骑元（chenqiyuan@toonan.com）
	 * 创建时间： 2020年4月26日 下午11:14:34 
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("getNetworkOrderNum")
	@ResponseBody
	public R getNetworkOrderNum() {
		String shopId=this.getUserId();
		String key=BussCons.NETWORK_NUM_KEY+shopId;
		String value=WebplusCache.getString(key);
		if(WebplusUtil.isNotEmpty(value)) {
			
			return R.toData(value);
		}
		return R.toData(0);
	}
	/**
	 * 
	 * 简要说明：获取订单详情
	 * 编写者：陈骑元（chenqiyuan@toonan.com）
	 * 创建时间： 2020年4月25日 下午4:37:45 
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("getOrderDetail")
	@ResponseBody
	public R getOrderDetail(String orderId) {
		 
		return orderCommonService.getOrderDetail(orderId, WebplusCons.WHETHER_NO);
	}
	/**
	 * 
	 * 简要说明：新增用户信息
	 * 编写者：陈骑元（chenqiyuan@toonan.com）
	 * 创建时间： 2020年4月25日 上午10:43:23 
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("saveMemberAddress")
	@ResponseBody
	public R saveMemberAddress(MemberAddress memberAddress) {
		
		return memberAddressService.saveMemberAddress(memberAddress);
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
		
			 return R.error("addressIdが必須です");
		}
		MemberAddress memberAddress=new MemberAddress();
		memberAddress.setAddressId(addressId);
		memberAddress.setWhetherRemove(WebplusCons.WHETHER_YES);
		memberAddressService.updateById(memberAddress);
		return R.ok();
	}
	/**
	 * 
	 * 简要说明：获取外卖订单
	 * 编写者：陈骑元（chenqiyuan@toonan.com）
	 * 创建时间： 2020年4月25日 下午4:13:56 
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("listMemberOrderPage")
	@ResponseBody
	public R  listMemberOrderPage() {
		String shopId=this.getUserId();
		List<String> orderTypeList=Lists.newArrayList();
		orderTypeList.add(BussCons.ORDER_TYPE_NETWORK);
		orderTypeList.add(BussCons.ORDER_TYPE_MOBILE);
		Dto pDto=Dtos.newDto(request);
		pDto.put("orderTypeList", orderTypeList);
		pDto.put("shopId", shopId);
		pDto.setOrder(" order_time DESC ");
		Page<Order> page=orderService.likePage(pDto);
		WebplusCache.convertItem(page);
		return  R.toApiPage(page);
	}
	/**
	 * 
	 * 简要说明：获取外卖订单
	 * 编写者：陈骑元（chenqiyuan@toonan.com）
	 * 创建时间： 2020年4月25日 下午9:10:12 
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("listMenuDict")
	@ResponseBody
	public R listMenuDict() {
		String shopId=this.getUserId();
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
	@PostMapping("listMenuStepSpec")
	@ResponseBody
	public R listMenuStepSpec(String menuIndexId){
		
		return orderCommonService.listMenuStepSpec(menuIndexId, BussCons.LANGUAGE_TYPE_JP);
	}
	/**
	 * 
	 * 简要说明：更新订单的准备状态
	 * 编写者：陈骑元（chenqiyuan@toonan.com）
	 * 创建时间： 2020年4月25日 下午1:31:39 
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("updateReadyStatus")
	@ResponseBody
	public R updateReadyStatus(String orderId,String appendAddress,String staffId) {
		if(WebplusUtil.isEmpty(orderId)) {
			
			 return R.error("注文番号が無意味です");
		}
		Order order=new Order();
		order.setOrderId(orderId);
		order.setAppendAddress(appendAddress);
		order.setOrderStatus(BussCons.ORDER_STATUS_READY);
		order.setUpdateTime(WebplusUtil.getDateTime());
		order.setUpdateStaff(staffId);
		boolean result=orderService.updateById(order);
		if(result) {
			return R.ok();
		}
		return R.error();
	}
	/**
	 * 
	 * 简要说明：更新订单的配送状态
	 * 编写者：陈骑元（chenqiyuan@toonan.com）
	 * 创建时间： 2020年4月25日 下午1:31:39 
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("updateSendStatus")
	@ResponseBody
	public R updateSendStatus(String orderId,String staffId) {
		
		return orderService.updateOrderStatus(orderId, BussCons.ORDER_STATUS_SEND, staffId);
	}
	/**
	 * 
	 * 简要说明：更新订单的提货状态
	 * 编写者：陈骑元（chenqiyuan@toonan.com）
	 * 创建时间： 2020年4月25日 下午1:31:39 
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("updatePickupStatus")
	@ResponseBody
	public R updatePickupStatus(String orderId,String staffId) {
		
		return orderService.updateOrderStatus(orderId, BussCons.ORDER_STATUS_PICKUP, staffId);
	}
	/**
	 * 
	 * 简要说明：更新订单的完成状态
	 * 编写者：陈骑元（chenqiyuan@toonan.com）
	 * 创建时间： 2020年4月25日 下午1:31:39 
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("updateFinishStatus")
	@ResponseBody
	public R updateFinishStatus(String orderId,String staffId) {
		R r=orderService.updateOrderStatus(orderId, BussCons.ORDER_STATUS_FINISH, staffId);
		if(WebplusCons.SUCCESS==r.getAppCode()) {
			orderCommonService.adjustAndEarnPoint(orderId);
		}
		return r;
	}
	
	/**
	 * 
	 * 简要说明：删除订单
	 * 编写者：陈骑元（chenqiyuan@toonan.com）
	 * 创建时间： 2020年4月25日 下午12:22:04 
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("removeOrder")
	@ResponseBody
	public R removeOrder(String orderId,String staffId) {
		
	   return orderService.updateOrderStatus(orderId, BussCons.ORDER_STATUS_CANCEL, staffId);
	}
	
	/** 
	 * 简要说明：删除订单中菜单
	 * 编写者：陈骑元
	 * 创建时间：2019年7月23日 下午10:40:41
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("removeOrderLine")
	@ResponseBody
	public R removeOrderLine(String lineId) {
	    if(WebplusUtil.isEmpty(lineId)){
	    	
	    	return R.error("KEYが必須です");
	    }
	    OrderLine orderLine=orderLineService.selectById(lineId);
	    boolean result=orderLineService.deleteById(lineId);
		if(result){
			Dto pDto=Dtos.newDto();
			pDto.put("parent_id", lineId);
			orderLineService.deleteByMap( pDto);	//同时删除规格	
			if(WebplusUtil.isNotEmpty(orderLine)) {
			    orderCommonService.updateCountOrder(orderLine.getOrderId());
			}
			return R.ok();
		}else{
			
			return R.error();
		}
			
	}
	/**
	 * 
	 * 简要说明：保存电话订单
	 * 编写者：陈骑元（chenqiyuan@toonan.com）
	 * 创建时间： 2020年4月25日 下午9:18:12 
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("saveMobileOrder")
	@ResponseBody
	public R saveMobileOrder() {
		String shopId=this.getUserId();
		Dto pDto=Dtos.newDto(request);
		pDto.put("shopId", shopId);
		pDto.put("orderType", BussCons.ORDER_TYPE_MOBILE);
		R r=orderCommonService.saveOrder(pDto);
		return  r;
		
	}
	/**
	 * 
	 * 简要说明：
	 * 编写者：陈骑元（chenqiyuan@toonan.com）
	 * 创建时间： 2020年5月3日 下午5:27:53 
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("updateOrder")
	@ResponseBody
	public R updateOrder(String orderId,String orderRemark,String appendAddress,
			String orderLines,String removeLineIds) {
		
		
		return orderCommonService.updateOrder(orderId, orderRemark, appendAddress, orderLines, removeLineIds);
		
	}
	/**
	 * 
	 * 简要说明：清空网络订单数量
	 * 编写者：陈骑元（chenqiyuan@toonan.com）
	 * 创建时间： 2020年4月26日 下午11:14:34 
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("clearNetworkOrderNum")
	@ResponseBody
	public R clearNetworkOrderNum() {
		String shopId=this.getUserId();
		String key=BussCons.NETWORK_NUM_KEY+shopId;
		WebplusCache.delString(key);
		return R.toData(0);
	}
	
	/**
	 * 
	 * 简要说明：获取总分
	 * 编写者：陈骑元
	 * 创建时间：2019年12月16日 下午11:38:11
	 * @param 说明
	 * @return 说明
	 */
	@GetMapping("getSummary")
	@ResponseBody
	public R getSummary(String memberNo){
		if(WebplusUtil.isEmpty(memberNo)){
			
			return R.error("The membership number cannot be empty");
		}
		EntityWrapper<Member> wrapper=new EntityWrapper<Member>();
		wrapper.eq("member_num", memberNo);
		Member member=memberService.selectOne(wrapper);
		if(WebplusUtil.isEmpty(member)){
			return R.error("会員番号が間違いました");
		}
		int point=PointApiUtil.getSummary(memberNo);
		Dto dataDto=Dtos.newDto();
		dataDto.put("point", point);
		dataDto.put("memberId", memberNo);
		dataDto.put("username", member.getUsername());
		dataDto.put("mobile", member.getMobile());
		dataDto.put("pointPer", "1");
		return R.toData(dataDto);
	}
	
	/**
	 * 
	 * 简要说明：积分抵扣
	 * 编写者：陈骑元
	 * 创建时间：2019年12月16日 下午11:51:28
	 * @param 说明
	 * @return 说明
	 */
	@GetMapping("subPoint")
	@ResponseBody
	public R subPoint(String memberNo,int point){
	   
		 if(WebplusUtil.isEmpty(memberNo)){
			 return R.error("会員番号が間違いました");
		 }
		 String shopId=this.getUserId();
		 Shop shop=shopService.selectById(shopId);
		 String mertCode=shop.getMertCode();
		 if(WebplusUtil.isEmpty(mertCode)){
			 
			 return R.error("mertCodeが必須です");
		 }
		 boolean result=PointApiUtil.adjust(memberNo,mertCode,shop.getOutletCode(), 0-point);
		 if(result) {
			 
			 return R.ok();
		 }
		 return R.error();
	}
	/**
	 * 
	 * 简要说明：积分生成
	 * 编写者：陈骑元（chenqiyuan@toonan.com）
	 * 创建时间： 2020年5月4日 上午9:58:45 
	 * @param 说明
	 * @return 说明
	 */
	@GetMapping("earn")
	@ResponseBody
	public R earn(String memberNo,Integer baseTotal,Integer tax) {
		 if(WebplusUtil.isEmpty(memberNo)){
			 return R.error("会員番号が間違いました");
		 }
		 if(WebplusUtil.isEmpty(baseTotal)) {
			 baseTotal=0;
		 }
		
		 if(WebplusUtil.isEmpty(tax)) {
			 tax=0;
		 }
		 if(baseTotal<1){
			 return R.error("最低金額が１からです");
		 }
		 String shopId=this.getUserId();
		 Shop shop=shopService.selectById(shopId);
		 String mertCode=shop.getMertCode();
		 if(WebplusUtil.isEmpty(mertCode)){
			 
			 return R.error("mertCodeが必須です");
		 }
		 Integer totalAmount=baseTotal+tax;
		 
		 boolean result=PointApiUtil.earn(memberNo, mertCode,shop.getOutletCode(), baseTotal, tax,totalAmount);
		 if(result){
			
			 return R.ok();
		 }
		 return R.error();
	}
	/**
	 * 
	 * 简要说明：积分调整
	 * 编写者：陈骑元
	 * 创建时间：2019年12月16日 下午11:51:28
	 * @param 说明
	 * @return 说明
	 */
	@GetMapping("adjustPoint")
	@ResponseBody
	public R adjustPoint(String memberNo,Integer baseTotal,Integer tax,Integer point){
		 if(WebplusUtil.isEmpty(memberNo)){
			 return R.error("会員番号が間違いました");
		 }
		 if(WebplusUtil.isEmpty(baseTotal)) {
			 baseTotal=0;
		 }
		
		 if(WebplusUtil.isEmpty(tax)) {
			 tax=0;
		 }
		 if(WebplusUtil.isEmpty(point)) {
			 point=0;
		 }
		 if(baseTotal<1){
			 return R.error("最低金額が１からです");
		 }
		 String shopId=this.getUserId();
		 Shop shop=shopService.selectById(shopId);
		 String mertCode=shop.getMertCode();
		 if(WebplusUtil.isEmpty(mertCode)){
			 
			 return R.error("mertCodeが必須です");
		 }
		 if(point>0){
			 boolean result=PointApiUtil.adjust(memberNo,mertCode,shop.getOutletCode(), 0-point);
			 if(!result){
				 return R.error("ポイント利用が失敗しました");
			 }
			
		 }
		 Integer totalAmount=baseTotal+tax;
		 boolean result=PointApiUtil.earn(memberNo, mertCode,shop.getOutletCode(), baseTotal, tax,totalAmount);
		 if(result){
			
			 return R.ok();
		 }
		 return R.error();
	}
}
