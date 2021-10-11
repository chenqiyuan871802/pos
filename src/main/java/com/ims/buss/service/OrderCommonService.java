package com.ims.buss.service;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.google.common.collect.Lists;
import com.ims.buss.asyncTask.PrintAsyncTask;
import com.ims.buss.constant.BussCons;
import com.ims.buss.model.BuffetSpec;
import com.ims.buss.model.Desk;
import com.ims.buss.model.Member;
import com.ims.buss.model.MemberAddress;
import com.ims.buss.model.MenuCatalog;
import com.ims.buss.model.MenuDict;
import com.ims.buss.model.MenuStep;
import com.ims.buss.model.Order;
import com.ims.buss.model.OrderLine;
import com.ims.buss.model.OrderLink;
import com.ims.buss.model.Shop;
import com.ims.buss.model.StepSpec;
import com.ims.buss.util.BussUtil;
import com.ims.buss.util.PointApiUtil;
import com.ims.core.cache.WebplusCache;
import com.ims.core.constant.WebplusCons;
import com.ims.core.matatype.Dto;
import com.ims.core.matatype.Dtos;
import com.ims.core.matatype.impl.HashDto;
import com.ims.core.util.WebplusFormater;
import com.ims.core.util.WebplusHashCodec;
import com.ims.core.util.WebplusJson;
import com.ims.core.util.WebplusUtil;
import com.ims.core.vo.R;
import com.ims.core.vo.UserToken;
import com.ims.websocket.AppPushServer;


/**
 * 
 * @ClassName:  OrderCommonService   
 * @Description:订单通用业务逻辑
 * @author: 陈骑元（chenqiyuan@toonan.com)
 * @date:   2020年4月25日 下午4:35:02     
 * @Copyright: 2020 www.toonan.com Inc. All rights reserved. 
 * 注意：本内容仅限于广州市图南软件有限公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Service
public class OrderCommonService {
	
	private static Log log = LogFactory.getLog(OrderCommonService.class);
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private OrderLineService orderLineService;
	
	@Autowired
	private ShopService shopService;
	
	@Autowired
	private MemberService memberService;
	@Autowired
	private MemberAddressService memberAddressService;
	
	@Autowired
	private BussCommonService bussCommonService;
	
	@Autowired
	private MenuStepService menuStepService;
	@Autowired
	private StepSpecService stepSpecService;
	@Autowired
	private MenuDictService menuDictService;
	@Autowired
	private MenuCatalogService menuCatalogService;
	@Autowired
	private PrintAsyncTask printAsyncTask;
	@Autowired		
    private OrderLinkService orderLinkService;
	@Autowired		
	private DeskService deskService;
	@Autowired
	private BuffetSpecService buffetSpecService;
	 // API通信基础地址
    public static String PAY_URL ;
    
    @Value("${pay.url}")
  	public  void sePayUrl(String payUrl) {
  		
    	PAY_URL=payUrl;
  	}

	
	/**
	 * 
	 * 简要说明：进行订单积分抵扣和生成
	 * 编写者：陈骑元（chenqiyuan@toonan.com）
	 * 创建时间： 2020年5月10日 上午10:12:47 
	 * @param 说明
	 * @return 说明
	 */
	public void adjustAndEarnPoint(String orderId) {
		Order order=orderService.selectById(orderId);
		Integer subPoint=BussUtil.dealEmptyAmount(order.getSubPoint());
		String memberId=order.getMemberId();
		Member member=memberService.selectById(memberId);
		if(WebplusUtil.isNotEmpty(memberId)) {
			if(WebplusUtil.isNotEmpty(member)) {
				String memberNo=member.getMemberNum();
				Shop shop=shopService.selectById(order.getShopId());
				String mertCode=shop.getMertCode();
				 if(WebplusUtil.isNotEmpty(mertCode)){
						if(subPoint>0	) {  //进行积分抵扣
							 PointApiUtil.adjust(memberNo,mertCode,shop.getOutletCode(), 0-subPoint);
						}
				 }
				 Integer totalAmount=BussUtil.dealEmptyAmount(order.getTotalAmount());
				 if(totalAmount>0) {
					 Integer consumeTax=BussUtil.dealEmptyAmount(order.getConsumeTax());
					 Integer baseTotal=totalAmount-consumeTax;
					 PointApiUtil.earn(memberNo, mertCode,shop.getOutletCode(), baseTotal,consumeTax,totalAmount);
				 }
				
				 
				
			}
			
				 
		}
	
	}
	
	public R listMenuStepSpec(String menuIndexId,String languageType){
		if(WebplusUtil.isNotEmpty(menuIndexId)){
			if(WebplusUtil.isEmpty(languageType)){
				languageType=BussCons.LANGUAGE_TYPE_JP;
			}
			Dto pDto=Dtos.newDto();
			pDto.put("menuIndexId", menuIndexId);
			pDto.put("languageType", languageType);
			pDto.setOrder(" step_num ASC ");
			List<MenuStep> menuStepList=menuStepService.list(pDto);
			for(MenuStep menuStep:menuStepList){
				Dto specDto=Dtos.newDto();
				specDto.put("stepIndexId", menuStep.getStepIndexId());
				specDto.put("languageType", languageType);
				specDto.setOrder(" sort_no ASC ");
				List<StepSpec> stepSpecList=stepSpecService.list(specDto);
				menuStep.setStepSpecList(stepSpecList);
			}
    		return R.toList(menuStepList);
		}
		return R.toList(Lists.newArrayList());
	}
	/**
	 * 
	 * 简要说明：查询店铺的外卖菜单
	 * 编写者：陈骑元（chenqiyuan@toonan.com）
	 * 创建时间： 2020年4月25日 下午6:53:33 
	 * @param 说明
	 * @return 说明
	 */
	public List<Dto> listMenuDict(String shopId){
		Shop shop=shopService.selectById(shopId);
		String mealType = BussUtil.getMenuMealType(shop.getTimeLimitStart(), shop.getTimeLimit()); // 获取用餐时段
		Dto pDto=Dtos.newDto();
		pDto.put("shopId", shopId);
		pDto.put("catalogType", BussCons.CATALOG_TYPE_TAKEOUT);
		pDto.put("languageType", BussCons.LANGUAGE_TYPE_JP);
		pDto.put("whetherBuffet", WebplusCons.WHETHER_NO);
		List<String> mealTypeList = Lists.newArrayList();
		mealTypeList.add(BussCons.MEAL_TYPE_COMMON);
		mealTypeList.add(mealType);
		List<String> menuTypeList = Lists.newArrayList();
		menuTypeList.add(BussCons.MENU_TYPE_COMMON);
		menuTypeList.add(BussCons.MENU_TYPE_RULE);
		pDto.setOrder(" b.sort_no ASC,a.sort_no ASC ");
		List<MenuDict> menuDictList=bussCommonService.listMenuDict(pDto);
		List<Dto>  catalogList=this.getCatalogList(menuDictList);
		List<Dto> dataList=Lists.newArrayList();
		for(Dto dataDto:catalogList){
		    String catalogIndexIdTmp=dataDto.getString("catalogIndexId");
			List<MenuDict> menuDictListNew=this.getCatalogMenuDictList(catalogIndexIdTmp, menuDictList);
			if(WebplusUtil.isNotEmpty(menuDictListNew)) {
				dataDto.put("menuDictList", menuDictListNew);
				dataList.add(dataDto);
			}
			
		}
		return dataList;
	}
	/**
	 * 
	 * 简要说明：获取订单详情
	 * 编写者：陈骑元（chenqiyuan@toonan.com）
	 * 创建时间： 2020年4月25日 下午4:46:21 
	 * @param 说明  是否合并相同的行
	 * @return 说明
	 */
	public R getOrderDetail(String orderId,String whetherMerge ) {
		   if(WebplusUtil.isEmpty(orderId)){
		    	
		    	return R.error("注文番号が無意味です");
		    }
	        Order order=orderService.selectById(orderId);
	        if(WebplusUtil.isEmpty(order)) {
	        	
	        	return R.error("订单编号不合法");
	        }
	        String orderType=order.getOrderType();
	        if(BussCons.ORDER_TYPE_MOBILE.equals(orderType)
	        		||BussCons.ORDER_TYPE_NETWORK.equals(orderType)) {
	        	String memberId=order.getMemberId();
	        	Member member=memberService.selectById(memberId);
	        	if(WebplusUtil.isNotEmpty(member)) {
	        		order.setUsername(member.getUsername());
	        		order.setMobile(member.getMobile());
	        	}
	        }
	        String whehterParentOrder=WebplusCons.WHETHER_NO;
	        if(BussCons.ORDER_TYPE_PARENT.equals(order.getOrderType())){
	        	whehterParentOrder=WebplusCons.WHETHER_YES;
	        }
			List<OrderLine> orderLineList=this.getOrderLineList(orderId,whehterParentOrder);
			order=this.getOrder(order, orderLineList, whetherMerge);
			return R.toData(order);
	}
	/**
	 * 
	 * 简要说明：
	 * 编写者：陈骑元（chenqiyuan@toonan.com）
	 * 创建时间： 2020年6月30日 上午8:06:59 
	 * @param 说明
	 * @return 说明
	 */
	public R saveOrder(String deskId,String orderLines) {
		if(WebplusUtil.isAnyEmpty(deskId,orderLines)) {
			
			return R.error("Neither the table number nor the order list can be empty");
		}
		Desk desk=deskService.selectById(deskId);
		if (WebplusUtil.isEmpty(desk)) {

			return R.error("卓番が間違ってます");
		}
		String orderId = desk.getOrderNo();
		if (WebplusUtil.isEmpty(orderId)) {

			return R.error("まだ注文されてません");
		}
		
		String shopId=desk.getShopId();
		List<OrderLine> orderLineList=this.getAddOrderLine(shopId, orderId, orderLines, WebplusCons.WHETHER_NO);
		if(WebplusUtil.isEmpty(orderLineList)) {
			
			return R.error("The order list is not legal");
			
		}
		Shop shop=shopService.selectById(shopId);
		if(BussCons.SHOP_STATUS_STOP.equals(shop.getShopStatus())) {
			
			return R.error("営業を停止する");
		}
		Order order=orderService.selectById(orderId);
		if(WebplusUtil.isNotEmpty(order)){  //订单不为空、更新\
			String key=BussCons.ORDER_LIMIT_KEY+shopId;
			if(WebplusCache.exists(key)) {
				return R.warn();
			}
			if(WebplusCons.WHETHER_NO.equals(order.getWhetherPay())){  //未结算进行订单追加
				
					OrderLine orderLine=new OrderLine();
					orderLine.setWhetherAdd(WebplusCons.WHETHER_NO); //变成已下单
					EntityWrapper<OrderLine> wrapper = new EntityWrapper<OrderLine>();
					wrapper.eq("whether_add", WebplusCons.WHETHER_YES);
					wrapper.eq("order_id", orderId);
					orderLineService.update(orderLine, wrapper);
					boolean result=orderLineService.insertBatch(orderLineList);
				   if( result){
					  
					   printAsyncTask.batchSendPrintMenu(order.getDeskName(), orderLineList); //推送打印服务
					   return R.ok();
				   }
			}
		}else {
			Date currentTime=WebplusUtil.getDateTime();
			int eatNum=desk.getEatNum();
			order=new Order();
			order.setOrderId(orderId);
			order.setEatNum(eatNum);
			order.setDeskNum(eatNum);
			order.setShopId(shopId);
			order.setDeskId(desk.getDeskId());
			order.setDeskName(desk.getDeskName());
			order.setOrderTime(currentTime);
			order.setCreateTime(currentTime);
			desk.setEatNum(eatNum);
			desk.setOrderNo(orderId);
			desk.setDeskStatus(BussCons.DESK_STATUS_FULL);
			desk.setOrderTime(currentTime);
			desk.setUpdateTime(WebplusUtil.getDateTime());
			boolean result=orderService.saveOrder(order, orderLineList, desk);
			if(result){
			   printAsyncTask.batchSendPrintMenu(desk.getDeskName(), orderLineList); //推送打印服务
				return R.ok();
			}
		}
		return R.ok();
	}
	/**
	 * 
	 * 简要说明：保存网络下单
	 * 编写者：陈骑元（chenqiyuan@toonan.com）
	 * 创建时间： 2020年4月26日 下午8:31:17 
	 * @param 说明
	 * @return 说明
	 */
	@Transactional
	public R saveOrder(Dto pDto) {
		R verifyR=this.verifyOrder(pDto);
		if(WebplusCons.ERROR==verifyR.getAppCode()) {
			
			return verifyR;
		}
		String orderId=WebplusCache.createOrderNum();
		String shopId=pDto.getString("shopId");
		String orderLines=pDto.getString("orderLines");
		List<OrderLine> orderLineList=this.getAddOrderLine(shopId, orderId, orderLines, WebplusCons.WHETHER_NO);
		Order order=this.createOrder(orderId, pDto, orderLineList);
		if(WebplusUtil.isNotEmpty(orderLineList)) {
			orderLineService.insertBatch(orderLineList);
		}
		String orderType=pDto.getString("orderType"	);
		boolean result=orderService.insert(order);
		if(result&&BussCons.ORDER_TYPE_NETWORK.equals(orderType)) {
			
			this.setNetworkOrderCount(shopId);
			
		}
		if(result) {
			List<OrderLine> orderLineListNew=this.getOrderLineList(orderLineList, WebplusCons.WHETHER_YES);
			printAsyncTask.sendTakeOutOrderPrint(order,orderLineListNew);
		}
		return R.ok().put("orderId", orderId);
		
	}
	
	/**
	 * 
	 * 简要说明：创建订单
	 * 编写者：陈骑元（chenqiyuan@toonan.com）
	 * 创建时间： 2020年4月26日 下午10:03:56 
	 * @param 说明
	 * @return 说明
	 */
	public Order createOrder(String orderId,Dto pDto,List<OrderLine> orderLineList) {
		Date nowTime=WebplusUtil.getDateTime();
		Order order=new Order();
		WebplusUtil.copyProperties(pDto, order);
		String shopId=order.getShopId();
		String orderCatagory=pDto.getString("orderCategory");
		if(BussCons.ORDER_CATEGORY_DELIVERY.equals(orderCatagory)) {
			String addressId=order.getAddressId();
			MemberAddress memberAddress=memberAddressService.selectMemberAddress(addressId);
			if(WebplusUtil.isNotEmpty(memberAddress)) {
				String orderAddress=this.getFullAddrss(addressId);
				order.setOrderAddress(orderAddress);
				if(WebplusUtil.isEmpty(order.getMobile())) {
					order.setMobile(memberAddress.getMobile());
				}
				order.setUsername(memberAddress.getUsername());
			}
			 
			 Shop shop=shopService.selectById(shopId);
			 Integer deliverAmount=BussUtil.dealEmptyAmount(shop.getDeliverAmount());
			 order.setDeliverAmount(deliverAmount);
		}else {
			String memberId=order.getMemberId();
			Member member=memberService.selectById(memberId);
			if(WebplusUtil.isNotEmpty(member)) {
				order.setMobile(member.getMobile());
				order.setUsername(member.getUsername());
			}
		}
		
	    Integer subPoint=BussUtil.dealEmptyAmount(order.getSubPoint());
	    if(subPoint>0) {  //1分抵扣一日元
	    	order.setPointAmount(subPoint);
	    }
		String foodNum=WebplusCache.createFoodNum(shopId);
		order.setFoodNum(foodNum);
		String orderType=order.getOrderType();
		order.setOrderId(orderId);
		order.setOrderTime(nowTime);
		order.setCreateTime(nowTime);
		if(BussCons.ORDER_TYPE_MOBILE.equals(orderType)) {
			order.setOrderStatus(BussCons.ORDER_STATUS_READY);
		}else {
			order.setOrderStatus(BussCons.ORDER_STATUS_WAIT);
		}
		
		this.countOrder(order, orderLineList);
		return order;
	}
	
	/**
	 * 
	 * 简要说明：更新
	 * 编写者：陈骑元（chenqiyuan@toonan.com）
	 * 创建时间： 2020年4月26日 下午11:11:32 
	 * @param 说明
	 * @return 说明
	 */
	public void updateCountOrder(String orderId) {
		Order order=orderService.selectById(orderId);
		EntityWrapper<OrderLine> wrapper=new EntityWrapper<OrderLine>();
		wrapper.eq("order_id", orderId);
		List<OrderLine> orderLineList=orderLineService.selectList(wrapper);
		this.countOrder(order, orderLineList);
		order.setUpdateTime(WebplusUtil.getDateTime());
		orderService.updateById(order);
	}
	/**
	 * 
	 * 简要说明：设置网络订单缓存
	 * 编写者：陈骑元（chenqiyuan@toonan.com）
	 * 创建时间： 2020年5月4日 上午8:06:12 
	 * @param 说明
	 * @return 说明
	 */
	public void setNetworkOrderCount(String shopId) {
		String key=BussCons.NETWORK_NUM_KEY+shopId;
		String value=WebplusCache.getString(key);
		int valueInt=0;
		if(WebplusUtil.isEmpty(value)) {
			valueInt=1;
		}else {
			valueInt=Integer.parseInt(value)+1;
		}
		WebplusCache.setString(key, valueInt+"", 0);
	    String message=WebplusJson.toJson(R.toData(valueInt));
	    AppPushServer.sendMessage(message, shopId);
	}
	/**
	 * 
	 * 简要说明：计算订单
	 * 编写者：陈骑元（chenqiyuan@toonan.com）
	 * 创建时间： 2020年4月26日 下午10:40:56 
	 * @param 说明
	 * @return 说明
	 */
	public void countOrder(Order order,List<OrderLine> orderLineList) {
		int outAmountBefore=0;  //外卖金额税前
		int outAmountAfter=0;  //外卖金额税后
		
		for(OrderLine orderLine:orderLineList){
		
			Integer subAmount=orderLine.getSubAmount();
			Integer subRate=orderLine.getSubRate();
			Integer buyNum=BussUtil.dealEmptyAmount(orderLine.getBuyNum());
			Integer menuPrice=BussUtil.dealEmptyAmount(orderLine.getMenuPrice());
			int singleSumPrice=menuPrice*buyNum;
			String taxType=orderLine.getTaxType();
			if(BussCons.TAX_TYPE_BEFORE.equals(taxType	)){
				outAmountBefore+=BussUtil.countSubRate(singleSumPrice, subAmount, subRate);
			}else{
				outAmountAfter+=BussUtil.countSubRate(singleSumPrice, subAmount, subRate);;
			}
				
		}
		Integer menuAmount=outAmountBefore+outAmountAfter;  //菜单总金额
		int outTaxAmountBefore=BussUtil.countTaxes(0, outAmountBefore, BussCons.OUT_TAXES);
		int totalAmount=menuAmount+outTaxAmountBefore;
		int outTaxAmountAfter=BussUtil.countAfterTaxes(outAmountAfter,BussCons.OUT_TAXES);
	
		int subRate=BussUtil.dealEmptyAmount(order.getSubRate());
		int subAmount=BussUtil.dealEmptyAmount(order.getSubAmount());
		Integer deliverAmount=BussUtil.dealEmptyAmount(order.getDeliverAmount()	);;
		int deliverTax=BussUtil.countTaxes(deliverAmount, 0, BussCons.OUT_TAXES);
		int outTaxAmount= outTaxAmountBefore+ outTaxAmountAfter;
		int consumeTax=outTaxAmount+deliverTax;
		totalAmount=BussUtil.countSubRate(totalAmount, subAmount, subRate)+deliverAmount-BussUtil.dealEmptyAmount(order.getPointAmount());
		order.setTaxAfterAmount(outAmountAfter);
		order.setTaxBeforeAmount(outAmountBefore);
		order.setDeskAmount(0);
		order.setMenuAmount(menuAmount);
		order.setOutTaxAmount(outTaxAmount);
		order.setSmallTotalAmount(menuAmount);
		order.setTotalAmount(totalAmount);
		order.setTaxAmount(outTaxAmount);
		order.setConsumeTax(consumeTax);
	}
	/**
	 * 
	 * 简要说明：
	 * 编写者：陈骑元（chenqiyuan@toonan.com）
	 * 创建时间： 2020年4月26日 下午10:29:49 
	 * @param 说明
	 * @return 说明
	 */
	private String getFullAddrss(String addressId) {
	   MemberAddress memberAddress=memberAddressService.selectById(addressId);
	 
	 
	   return this.getFullAddrss(memberAddress);
	}
	/**
	 * 
	 * 简要说明：
	 * 编写者：陈骑元（chenqiyuan@toonan.com）
	 * 创建时间： 2020年4月26日 下午10:29:49 
	 * @param 说明
	 * @return 说明
	 */
	private String getFullAddrss(MemberAddress memberAddress) {
		if(WebplusUtil.isNotEmpty(memberAddress)) {
			WebplusCache.convertItem(memberAddress);
			
			return  memberAddress.getExtMap().get("province_dict")+""+memberAddress.getExtMap().get("city_dict")
					+memberAddress.getExtMap().get("town_dict")+memberAddress.getDetailAddress();
		}
		
		return "";
	}
	/**
	 * 
	 * 简要说明：校验电话下单内容
	 * 编写者：陈骑元（chenqiyuan@toonan.com）
	 * 创建时间： 2020年4月26日 下午8:35:27 
	 * @param 说明
	 * @return 说明
	 */
	public R verifyOrder(Dto pDto) {
		String orderType=pDto.getString("orderType");
		if(BussCons.ORDER_TYPE_MOBILE.equals(orderType)) {
			if(WebplusUtil.isEmpty(pDto.getString("staffId"))) {
				
				return R.error("员工编号不能为空");
			}
			
		}
        if(WebplusUtil.isEmpty(pDto.getString("shopId"))) {
			
			return R.error("店铺编号不能为空");
		}
		if(WebplusUtil.isEmpty(pDto.getString("memberId"))) {
			
			return R.error("会员编号不能为空");
		}
		String orderCatagory=pDto.getString("orderCategory");
		if(BussCons.ORDER_CATEGORY_DELIVERY.equals(orderCatagory)) {
			   if(WebplusUtil.isEmpty(pDto.getString("addressId"))) {
					
					return R.error("地址编号不能为空");
				}
		}
     
		if(WebplusUtil.isEmpty(orderCatagory)) {
			
			return R.error("类别不能为空");
		}
		if(WebplusUtil.isEmpty(pDto.getString("orderLines"))) {
			
			return R.error("下单列表不能为空");
		}
		return  R.ok();
	}
	
	
	/**
	 * 
	 * 简要说明：获取新增菜品
	 * 编写者：陈骑元
	 * 创建时间：2019年8月3日 上午12:25:33
	 * @param 说明
	 * @return 说明
	 */
	public List<OrderLine> getAddOrderLine(String shopId,String orderId,String orderLines,String whetherAdd){
	
		List<OrderLine> orderLineList=Lists.newArrayList();
		List<Dto> dataList=WebplusJson.fromJson(orderLines);
		if(WebplusUtil.isNotEmpty(dataList)){
			
			List<String> menuIdList=this.getMenuId(dataList);
			List<String> specIdList=this.getSpecId(dataList);
			List<MenuDict> menuDictList=Lists.newArrayList();
			if(WebplusUtil.isNotEmpty(menuIdList)){
				menuDictList=menuDictService.selectBatchIds(menuIdList);
			}
			
			List<StepSpec> stepSpecList=Lists.newArrayList();
			if(WebplusUtil.isNotEmpty(specIdList)){
				stepSpecList=stepSpecService.selectBatchIds(specIdList);
			}
			List<MenuCatalog> buffetCatalogList = this.getMenuCatalogList(dataList);
			String buffetCatalogIndexId = "";
			int chooseNum = 0;
			List<MenuCatalog> menuCatalogList=this.getMenuCatalog(menuDictList);
			for(Dto dataDto:dataList){
				Integer buyNum=dataDto.getInteger("buyNum");
				String buffetIndexId=dataDto.getString("catalogIndexId");
				String menuIndexId=dataDto.getString("menuIndexId");
				String specIndexId=dataDto.getString("specIndexId");
				OrderLine orderLine=new OrderLine();
				orderLine.setOrderId(orderId);
				orderLine.setBuyNum(buyNum);
				
				orderLine.setShopId(shopId);
				orderLine.setWhetherAdd(whetherAdd);
				orderLine.setWhetherPrint(WebplusCons.WHETHER_NO);
				orderLine.setCreateTime(WebplusUtil.getDateTime());
				orderLine.setWhetherTakeOut(WebplusCons.WHETHER_NO);
				
				if(WebplusUtil.isNotEmpty(buffetIndexId)) {
					MenuCatalog buffetCatalog=this.getMenuCatalog(buffetIndexId, buffetCatalogList, "");
					if(WebplusUtil.isNotEmpty(buffetCatalog)) { //自助餐菜品
						String whetherBuffet=buffetCatalog.getWhetherBuffet();
						if(WebplusCons.WHETHER_YES.equals(whetherBuffet)){
							orderLine.setCatalogIndexId(buffetIndexId);
							orderLine.setPrintCatalogIndexId(buffetIndexId);
							orderLine.setMenuName(buffetCatalog.getCatalogName());
							orderLine.setMenuPrice(buffetCatalog.getBuffetPrice());
							orderLine.setPrintMenuName(buffetCatalog.getCatalogName());
							orderLine.setWhetherBuffet(WebplusCons.WHETHER_YES);
							orderLine.setWhetherSpec(WebplusCons.WHETHER_NO);
							int num=buyNum*buffetCatalog.getLimitNum();
							chooseNum += num;
							orderLine.setChooseNum(num);
							buffetCatalogIndexId =buffetIndexId;
							
							orderLine.setTaxType(buffetCatalog.getTaxType());
							if (WebplusUtil.isNotEmpty(specIndexId)) {
								BuffetSpec buffetSpec = buffetSpecService.selectById(specIndexId);
								if (WebplusUtil.isNotEmpty(buffetSpec)) {
									orderLine.setWhetherSpec(WebplusCons.WHETHER_YES);
									String menuName = buffetCatalog.getCatalogName() + "（" + buffetSpec.getSpecName() + "）";
									orderLine.setMenuName(menuName);
									orderLine.setPrintMenuName(menuName);
									orderLine.setMenuPrice(buffetSpec.getSpecPrice());
									orderLine.setSpecIndexId(specIndexId);
								}
							}
							orderLineList.add(orderLine);	
						}else {
							
							orderLine.setCatalogIndexId(buffetIndexId);
							orderLine.setPrintCatalogIndexId(buffetIndexId);
							orderLine.setMenuName(buffetCatalog.getCatalogName());
							if(BussCons.CUSTOM_MENU_TYPE.equals(whetherBuffet)) {
								Integer menuPrice=BussUtil.dealEmptyAmount(dataDto.getInteger("menuPrice"));
								orderLine.setMenuPrice( menuPrice);
							}else {
								orderLine.setMenuPrice(buffetCatalog.getBuffetPrice());
							}
						
							orderLine.setPrintMenuName(buffetCatalog.getCatalogName());
							orderLine.setWhetherBuffet(buffetCatalog.getWhetherBuffet());
							orderLine.setWhetherSpec(WebplusCons.WHETHER_NO);
							orderLine.setTaxType(buffetCatalog.getTaxType());
							orderLineList.add(orderLine);	
						}
						
					}
				}else {
					orderLine.setMenuIndexId(menuIndexId);
					orderLine.setWhetherBuffet(WebplusCons.WHETHER_NO);
					if(WebplusUtil.isNotEmpty(menuIndexId)){
						MenuDict menuDict=this.getMenuDict(menuIndexId, menuDictList,"");
						if(WebplusUtil.isNotEmpty(menuDict)){
							String menuName=menuDict.getMenuName();
							String shortName=menuDict.getShortName();
							String catalogIndexId=menuDict.getCatalogIndexId();
							orderLine.setPrintCatalogIndexId(catalogIndexId);
							
							orderLine.setCatalogIndexId(catalogIndexId);
							MenuCatalog menuCatalog=this.getMenuCatalog(catalogIndexId,menuCatalogList,"");
							if(WebplusUtil.isNotEmpty(menuCatalog)) {
								orderLine.setTaxType(menuCatalog.getTaxType());
								if(BussCons.CATALOG_TYPE_TAKEOUT.equals(menuCatalog.getCatalogType())
										||BussCons.CATALOG_TYPE_BESIDES.equals(menuCatalog.getCatalogType())){  //外卖种类
									orderLine.setWhetherTakeOut(WebplusCons.WHETHER_YES);
								}
							}
							Integer menuPrice=0;
							
							    menuPrice=BussUtil.dealEmptyAmount(menuDict.getMenuPrice()) ;
								orderLine.setMenuPrice(menuPrice);
							
							
							orderLine.setMenuName(menuName);
							
							if(WebplusUtil.isNotEmpty(shortName)){
							 orderLine.setPrintMenuName(shortName);
							}else{
								orderLine.setPrintMenuName(menuName);
							}
							Integer menuSumPrice = menuPrice * buyNum;
							orderLine.setMenuSumPrice(menuSumPrice);
							orderLine.setWhetherSpec(WebplusCons.WHETHER_NO);
							String lineId=WebplusUtil.uuid();
							orderLine.setLineId(lineId);
							orderLineList.add(orderLine);
							if(WebplusUtil.isNotEmpty(specIndexId)){
							    List<OrderLine> specLineList=this.getSpecOrderLine(specIndexId, orderLine, stepSpecList);
							    if(WebplusUtil.isNotEmpty(specLineList)) {
							    	orderLineList.addAll(specLineList);
							    }
							}
							
								
							
						}
					}
				}
				
			}
			if (WebplusUtil.isNotEmpty(buffetCatalogIndexId)) {
				this.setBuffetOderCache(orderId, buffetCatalogIndexId, chooseNum);
			}
		}
		return orderLineList;
		
	}
	/**
	 * 
	 * 简要说明： 编写者：陈骑元 创建时间：2020年1月13日 下午9:40:55
	 * 
	 * @param 说明
	 * @return 说明
	 */
	private List<MenuCatalog> getMenuCatalogList(List<Dto> orderLineList) {
		List<String> catalogIndexIdList = WebplusUtil.getListDtoByKey(orderLineList, "catalogIndexId");
		if (WebplusUtil.isNotEmpty(catalogIndexIdList)) {

			return menuCatalogService.selectBatchIds(catalogIndexIdList);
		}
		return Lists.newArrayList();
	}
	/**
	 * 设置自助餐缓存
	 * 
	 * @param buffetLineList
	 */
	public void setBuffetOderCache(String orderId, String catalogIndexId, int chooseNum) {
		if (WebplusUtil.isNotEmpty(catalogIndexId)) {
			String key = BussCons.BUFFET_ORDER_KEY + orderId;
			if (WebplusCache.exists(key)) {
				String value = WebplusCache.getString(key);
				if (WebplusUtil.isNotEmpty(value)) {
					Dto limitDto = WebplusJson.fromJson(value, HashDto.class);
					String orderTime = limitDto.getString("orderTime");
					long timeLimit = limitDto.getLong("timeLimit");
					int timeLimitInt = limitDto.getInteger("timeLimit");

					if (BussUtil.checkBuffetTimeLimit(orderTime, timeLimit)) {
						this.setBuffetCache(key, catalogIndexId, chooseNum);
					} else {
					    String catalogIndexIdCache=limitDto.getString("catalogIndexId");
					    if(catalogIndexIdCache.equals(catalogIndexId)) {
					    	Integer chooseNumCache = limitDto.getInteger("totalChooseNum");
							int totalNum = BussUtil.dealEmptyAmount(chooseNumCache) + chooseNum;
							limitDto.put("totalChooseNum", totalNum);
							String valueNew = WebplusJson.toJson(limitDto);
							WebplusCache.setString(key, valueNew, (timeLimitInt + 60) * 60);
					    }else {
					    	this.setBuffetCache(key, catalogIndexId, chooseNum);
					    }
						
					}
				}

			} else {
				this.setBuffetCache(key, catalogIndexId, chooseNum);
			}

		}

	}

	/**
	 * 设置缓存
	 * 
	 * @param key
	 * @param buffetLineList
	 */
	private void setBuffetCache(String key, String catalogIndexId, int chooseNum) {

		MenuCatalog menuCatalog = menuCatalogService.selectById(catalogIndexId);
		if (WebplusUtil.isNotEmpty(menuCatalog)) {
			Integer timeLimit = menuCatalog.getTimeLimit();
			if (WebplusUtil.isEmpty(timeLimit)) {
				timeLimit = 0;
			}

			Dto dataDto = Dtos.newDto();
			dataDto.put("orderTime", WebplusUtil.getDateStr(WebplusCons.DATETIME));
			dataDto.put("timeLimit", timeLimit);
			dataDto.put("catalogIndexId", catalogIndexId);
			dataDto.put("totalChooseNum", chooseNum);
			String value = WebplusJson.toJson(dataDto);
			if (timeLimit == 0) {
				WebplusCache.setString(key, value, 60 * 60 * 5);

			} else {
				WebplusCache.setString(key, value, (timeLimit + 60) * 60);
			}

		}
	}
	/**
	 * 
	 * 简要说明：
	 * 编写者：陈骑元（chenqiyuan@toonan.com）
	 * 创建时间： 2020年4月26日 下午9:35:01 
	 * @param 说明
	 * @return 说明
	 */
	public List<OrderLine>  getSpecOrderLine(String specIndexIds,OrderLine menuOrderLine,List<StepSpec> stepSpecList){
		 List<String> specIndexIdList=WebplusFormater.separatStringToList(specIndexIds);
		 List<OrderLine> orderLineList=Lists.newArrayList();
		 for(String specIndexId:specIndexIdList) {
			 if(WebplusUtil.isNotEmpty(specIndexId)) {
				 StepSpec stepSpec=this.getStepSpec(specIndexId, stepSpecList,"");
				 if(WebplusUtil.isNotEmpty(stepSpec)) {
					 String parentId=menuOrderLine.getLineId();
					 OrderLine orderLine=new OrderLine();
					 WebplusUtil.copyProperties(menuOrderLine, orderLine);
					 orderLine.setLineId(WebplusUtil.uuid());
					 orderLine.setParentId(parentId);
					 orderLine.setSpecIndexId(specIndexId);
					 String specName=stepSpec.getSpecName();
					 Integer specPrice=BussUtil.dealEmptyAmount(stepSpec.getSpecPrice());
				     Integer menuSumPrice = specPrice * orderLine.getBuyNum();
				     orderLine.setMenuSumPrice(menuSumPrice);
					 orderLine.setPrintMenuName(specName);
					 orderLine.setMenuName(specName);
					 orderLine.setMenuPrice(specPrice);
					 orderLine.setWhetherSpec(WebplusCons.WHETHER_YES);
					 orderLineList.add(orderLine);
				 }
			 }
		 }
		 return orderLineList;
	}
	  /**
     * 
     * 简要说明：返回对应的自助餐菜单
     * 编写者：陈骑元
     * 创建时间：2019年8月3日 上午12:16:10
     * @param 说明
     * @return 说明
     */
	private MenuCatalog getMenuCatalog(String catalogIndexId,List<MenuCatalog> menuCatalogList,String languageType){
		for(MenuCatalog menuCatalog:menuCatalogList){
			if(WebplusUtil.isEmpty(languageType)){
				if(catalogIndexId.equals(menuCatalog.getCatalogIndexId())){
					
					return menuCatalog;
				}
			}else{
               if(catalogIndexId.equals(menuCatalog.getCatalogIndexId())
            		   &&languageType.equals(menuCatalog.getLanguageType())){
					
            	   return menuCatalog;
				}
			}
			
		}
		
		return null;
	}
	/**
	 * 
	 * 简要说明：返回对应步骤规格
	 * 编写者：陈骑元
	 * 创建时间：2019年8月3日 上午12:16:10
	 * @param 说明
	 * @return 说明
	 */
	private StepSpec getStepSpec(String specIndexId,List<StepSpec> stepSpecList,String languageType){
		for(StepSpec stepSpec:stepSpecList){
			if(WebplusUtil.isEmpty(languageType)){
				if(specIndexId.equals(stepSpec.getSpecIndexId())){
					
					return stepSpec;
				}	
			}else{
                if(specIndexId.equals(stepSpec.getSpecIndexId())
                		&&languageType.equals(stepSpec.getLanguageType())){
					
					return stepSpec;
				}	
			}
			
		}
		
		return null;
	}
	/**
	 * 
	 * 简要说明：获取菜品里面的类目
	 * 编写者：陈骑元
	 * 创建时间：2019年9月15日 下午10:07:57
	 * @param 说明
	 * @return 说明
	 */
	private List<MenuCatalog>  getMenuCatalog(List<MenuDict> menuDictList){
		List<String> catalogIdList=Lists.newArrayList();
		if(WebplusUtil.isNotEmpty(menuDictList)){
			for(MenuDict menuDict:menuDictList){
				catalogIdList.add(menuDict.getCatalogIndexId());
			}
			if(WebplusUtil.isNotEmpty(catalogIdList)){
				List<MenuCatalog> menuCatalogList=menuCatalogService.selectBatchIds(catalogIdList);
				return menuCatalogList;
			}
			
		}
		return Lists.newArrayList();
		
	}
	/**
	 * 
	 * 简要说明：通过规格索引获取日语的主键编号
	 * 编写者：陈骑元
	 * 创建时间：2019年8月2日 下午11:58:42
	 * @param 说明
	 * @return 说明
	 */
	private List<String> getSpecId(List<Dto> dataList){
		List<String> specIdList=Lists.newArrayList();
		for(Dto dataDto:dataList){
			String specIndexId=dataDto.getString("specIndexId");
			if(WebplusUtil.isNotEmpty(specIndexId)){
				List<String> indexList=WebplusFormater.separatStringToList(specIndexId);
				specIdList.addAll(indexList);
			}
			
		}
		
		return specIdList;
	}
	
	
	
	/**
	 * 
	 * 简要说明：获取菜单索引编号
	 * 编写者：陈骑元
	 * 创建时间：2019年8月2日 下午11:58:42
	 * @param 说明
	 * @return 说明
	 */
	private List<String> getMenuId(List<Dto> dataList){
		List<String> menuIdList=Lists.newArrayList();
		for(Dto dataDto:dataList){
			String menuIndexId=dataDto.getString("menuIndexId");
			if(WebplusUtil.isNotEmpty(menuIndexId)){
				menuIdList.add(menuIndexId);
			}
			
		}
		
		return menuIdList;
	}
	  /**
     * 
     * 简要说明：返回对应的菜品
     * 编写者：陈骑元
     * 创建时间：2019年8月3日 上午12:16:10
     * @param 说明
     * @return 说明
     */
	private MenuDict getMenuDict(String menuIndexId,List<MenuDict> menuDictList,String languageType){
		for(MenuDict menuDict:menuDictList){
			if(WebplusUtil.isEmpty(languageType)){
				if(menuIndexId.equals(menuDict.getMenuIndexId())){
					
					return menuDict;
				}
			}else{
               if(menuIndexId.equals(menuDict.getMenuIndexId())
            		   &&languageType.equals(menuDict.getLanguageType())){
					
					return menuDict;
				}
			}
			
		}
		
		return null;
	}
	/**
	 * 
	 * 简要说明：获取菜单里面的菜品种类
	 * 编写者：陈骑元
	 * 创建时间：2019年9月1日 下午1:42:21
	 * @param 说明
	 * @return 说明
	 */
	private List<Dto> getCatalogList(List<MenuDict> menuDictList){
		Dto catalogDto=Dtos.newDto();
		List<Dto> dataList=Lists.newArrayList();
		for(MenuDict menuDict:menuDictList){
			String catalogIndexId=menuDict.getCatalogIndexId();
			String catalogName=menuDict.getCatalogName();
			if(!catalogDto.containsKey(catalogIndexId)){
				catalogDto.put(catalogIndexId, catalogName);
				Dto dataDto=Dtos.newDto();
				dataDto.put("catalogIndexId", catalogIndexId);
				dataDto.put("catalogName", catalogName);
				dataDto.put("taxType", menuDict.getTaxType());
				dataList.add(dataDto);
			}
				
		}
		catalogDto.clear();
		return dataList;
	}
	
	/**
	 * 
	 * 简要说明：获取种类菜品
	 * 编写者：陈骑元
	 * 创建时间：2019年8月2日 下午11:12:41
	 * @param 说明
	 * @return 说明
	 */
	private List<MenuDict> getCatalogMenuDictList(String catalogIndexId,List<MenuDict> menuDictList){
		  List<MenuDict> menuDictListNew=Lists.newArrayList();
		  for(MenuDict menuDict:menuDictList){
			  if(catalogIndexId.equals(menuDict.getCatalogIndexId())){
				  menuDictListNew.add(menuDict);
			  }
		  }
		  return menuDictListNew;
	}
	/**
	 * 
	 * 简要说明：获取订单
	 * 编写者：陈骑元
	 * 创建时间：2019年10月19日 下午4:30:14
	 * @param 说明
	 * @return 说明
	 */
	private Order getOrder(Order order,List<OrderLine> orderLineList,String whetherMerge){
		orderLineList=this.removeWasteData(orderLineList);
		List<OrderLine> orderLineListNew = Lists.newArrayList();
		int totalAmount = 0;
		int menuAmount = 0;
		int outAmountBefore=0;  //外卖金额税前
		int outAmountAfter=0;  //外卖金额税后
		int eatAmountBefore=0;  //堂食金额税前
		int eatAmountAfter=0;  //堂食金额税后
		for (OrderLine orderLine : orderLineList) {
			String menuIndexId = orderLine.getMenuIndexId();
			String whetherBuffet = orderLine.getWhetherBuffet();
			if (WebplusCons.WHETHER_NO.equals(whetherBuffet) || WebplusUtil.isEmpty(menuIndexId)) {
				Integer buyNum = BussUtil.dealEmptyAmount(orderLine.getBuyNum());
				Integer menuPrice = BussUtil.dealEmptyAmount(orderLine.getMenuPrice());
				Integer menuSumPrice = menuPrice * buyNum;
				Integer subRate=orderLine.getSubRate();
				Integer subAmount=orderLine.getSubAmount();
				String taxType=orderLine.getTaxType();
				if(WebplusCons.WHETHER_YES.equals(orderLine.getWhetherTakeOut())){  //外卖金额
					if(BussCons.TAX_TYPE_BEFORE.equals(taxType	)){
						outAmountBefore+= BussUtil.countSubRate(menuSumPrice, subAmount, subRate);
						
					}else{
						outAmountAfter+= BussUtil.countSubRate(menuSumPrice, subAmount, subRate);
					}
					
				}else{
					if(BussCons.TAX_TYPE_BEFORE.equals(taxType	)){
						eatAmountBefore+= BussUtil.countSubRate(menuSumPrice, subAmount, subRate);
					}else{
						eatAmountAfter+=BussUtil.countSubRate(menuSumPrice, subAmount, subRate);
					}
					
				}
				orderLine.setMenuSumPrice(menuSumPrice);
				orderLineListNew.add(orderLine);
			}

		}
		Shop shop = shopService.selectById(order.getShopId());
		Integer tableAmount=BussUtil.dealEmptyAmount(shop.getTableAmount());
		   // Integer taxes=BussUtil.dealEmptyAmount(shop.getTaxes());
			Integer eatNum=BussUtil.dealEmptyAmount(order.getEatNum());
			menuAmount=eatAmountBefore+eatAmountAfter+outAmountBefore+outAmountAfter;  //菜单总金额
			int deskAmount=eatNum*tableAmount;
			int taxAmountBefore=BussUtil.countTaxes(0, eatAmountBefore, BussCons.EAT_TAXES);
			int outTaxAmountBefore=BussUtil.countTaxes(0, outAmountBefore, BussCons.OUT_TAXES);
			totalAmount=deskAmount+menuAmount+taxAmountBefore+outTaxAmountBefore;
			int taxAmountAfter=BussUtil.countAfterTaxes(deskAmount,eatAmountAfter,BussCons.EAT_TAXES);
			int outTaxAmountAfter=BussUtil.countAfterTaxes(outAmountAfter,BussCons.OUT_TAXES);
			Integer deliverAmount=BussUtil.dealEmptyAmount(order.getDeliverAmount()	);;
			int deliverTax=BussUtil.countAfterTaxes(deliverAmount,BussCons.OUT_TAXES);
			int outTaxAmount= outTaxAmountBefore+ outTaxAmountAfter+deliverTax;
			int taxAmount= taxAmountBefore+taxAmountAfter;
			int subRate=order.getSubRate();
			int subAmount=order.getSubAmount();
			int consumeTax=outTaxAmount+taxAmount;
			totalAmount=BussUtil.countSubRate(totalAmount, subAmount, subRate)+deliverAmount-BussUtil.dealEmptyAmount(order.getPointAmount());
			order.setTaxAfterAmount(eatAmountAfter+outAmountAfter);
			order.setTaxBeforeAmount(eatAmountBefore+outAmountBefore);
			order.setDeskAmount(deskAmount);
			order.setMenuAmount(menuAmount);
			order.setOutTaxAmount(outTaxAmount);
			order.setSmallTotalAmount(menuAmount+deskAmount);
			order.setTaxAmount(taxAmount);
			order.setConsumeTax(consumeTax);
			if(WebplusUtil.isNotEmpty(order.getTotalAmount())){
				if(order.getTotalAmount().intValue()!=totalAmount){
					order.setTotalAmount(totalAmount);
					orderService.updateById(order);
				}
			}else{
				order.setTotalAmount(totalAmount);
			}
		
			
			
		if(WebplusUtil.isEmpty(order.getPayTime())){
			order.setPayTime(WebplusUtil.getDateTime());
		}
		List<OrderLine> dataList = this.getOrderLineList(orderLineListNew, WebplusCons.WHETHER_YES);
		if(WebplusCons.WHETHER_YES.equals(whetherMerge)){
			dataList=this.getMergeOrderLine(dataList);
		}
		order.setDataList(dataList);
		return order;
	}
	
	/**
	 * 
	 * 简要说明：获取组合订单组合
	 * 编写者：陈骑元
	 * 创建时间：2019年9月1日 下午9:13:11
	 * @param 说明
	 * @return 说明
	 */
	private List<OrderLine> getOrderLineList(List<OrderLine> orderLineList,String whetherCountSpec){
		List<OrderLine> orderLineListNew=Lists.newArrayList();
		for(OrderLine orderLine:orderLineList){
			String whetherSpec=orderLine.getWhetherSpec();
			String whetherBuffet=orderLine.getWhetherBuffet();
			String menuIndexId=orderLine.getMenuIndexId();
			if(WebplusCons.WHETHER_NO.equals(whetherSpec)||
					(WebplusCons.WHETHER_YES.equals(whetherBuffet)&&WebplusUtil.isEmpty( menuIndexId))){
				if(WebplusUtil.isNotEmpty(menuIndexId)){
					String lineId=orderLine.getLineId();
					if(WebplusUtil.isNotEmpty(lineId)){
						List<OrderLine> specList=this.getStepSpec(lineId, orderLineList);
						if(WebplusUtil.isNotEmpty(specList)){
							if(WebplusCons.WHETHER_YES.equals(whetherCountSpec)){
								int singleMenuPriceTotal=this.getSpecMenuPrice(orderLine.getMenuSumPrice(), specList);
								orderLine.setMenuSumPrice(singleMenuPriceTotal);
							}
							String specMenuName=this.getPrintSpecName(lineId, orderLineList);
							String printMenuName=orderLine.getPrintMenuName()+specMenuName;
							orderLine.setPrintMenuName(printMenuName);
							orderLine.setWhetherSpec(WebplusCons.WHETHER_YES);
							orderLine.setSpecList(specList);
						}
					}
					
				}
				orderLineListNew.add(orderLine);
			}
		}
		return orderLineListNew;
	}
	
	
	/**
	 * 
	 * 简要说明：获取订单项目
	 * 编写者：陈骑元
	 * 创建时间：2019年10月19日 下午4:18:45
	 * @param 说明
	 * @return 说明
	 */
	private List<OrderLine> getOrderLineList(String orderId,String whetherParentOrder){
			Dto pDto=Dtos.newDto();
			if(WebplusCons.WHETHER_YES.equals(whetherParentOrder)){
				List<String> orderIdList=this.getParentOrderId(orderId);
				pDto.put("orderIdList",orderIdList);
			}else{
				pDto.put("orderId", orderId);
			}
			
			pDto.setOrder(" create_time ASC ");
			List<OrderLine> orderLineList=orderLineService.list(pDto);
			List<OrderLine> orderLineListNew=Lists.newArrayList();
			for(OrderLine orderLine:orderLineList){
				String menuIndexId=orderLine.getMenuIndexId();
				String whetherBuffet=orderLine.getWhetherBuffet();
				if(WebplusCons.WHETHER_NO.equals(whetherBuffet)||WebplusUtil.isEmpty(menuIndexId)){
					Integer buyNum=BussUtil.dealEmptyAmount(orderLine.getBuyNum());
					Integer menuPrice=BussUtil.dealEmptyAmount(orderLine.getMenuPrice());
					Integer menuSumPrice=menuPrice*buyNum;
					orderLine.setMenuSumPrice(menuSumPrice);
					orderLineListNew.add(orderLine);
				}
			}
		 
			
			return orderLineListNew;
		 
	}
	/***
	 * 
	 * 简要说明：合并相同菜单行
	 * 编写者：陈骑元
	 * 创建时间：2019年10月20日 下午10:34:37
	 * @param 说明
	 * @return 说明
	 */
	private List<OrderLine> getMergeOrderLine(List<OrderLine> orderLineList){
		 List<OrderLine> orderLineListNew=Lists.newArrayList();
		 Map<String,OrderLine> lineMap=new LinkedHashMap<String,OrderLine>();
		 if(WebplusUtil.isNotEmpty(orderLineList)){
			 for(int i=0;i<orderLineList.size();i++){
				 OrderLine orderLine=orderLineList.get(i);
				 String key=""	;
				 if(WebplusUtil.isEmpty(orderLine.getMenuIndexId())){
					 key=orderLine.getCatalogIndexId();
				 }else{
					 key=orderLine.getMenuIndexId();
				 }
				 if(lineMap.containsKey(key)){
					 OrderLine orderLineEntity=lineMap.get(key);
					 int buyNum=orderLine.getBuyNum()+orderLineEntity.getBuyNum();
					 int menuSumPrice=orderLine.getMenuSumPrice()+orderLineEntity.getMenuSumPrice();
					 orderLineEntity.setBuyNum(buyNum);
					 orderLineEntity.setMenuSumPrice(menuSumPrice);
					 lineMap.put(key, orderLineEntity);
				 }else{
					 lineMap.put(key, orderLine);
				 }
			 }
		 }
		 for(String key:lineMap.keySet()){
			 orderLineListNew.add(lineMap.get(key));
		 }

         return orderLineListNew;
	}
	 /** 
	 * 简要说明：获取菜品规格
	 * 编写者：陈骑元
	 * 创建时间：2019年9月1日 下午9:21:17
	 * @param 说明
	 * @return 说明
	 */
	private List<OrderLine> getStepSpec(String lineId,List<OrderLine> orderLineList){
		List<OrderLine> specList=Lists.newArrayList();
		for(OrderLine orderLine:orderLineList){
			String whetherSpec=orderLine.getWhetherSpec();
			if(WebplusCons.WHETHER_YES.equals(whetherSpec)){
				String parentId=orderLine.getParentId();
				if(lineId.equals(parentId)){
					specList.add(orderLine);
				}
			}
		}
		return specList;
	}
	/**
	 * 
	 * 简要说明：获取菜品规格
	 * 编写者：陈骑元
	 * 创建时间：2019年9月1日 下午9:21:17
	 * @param 说明
	 * @return 说明
	 */
	private Integer getSpecMenuPrice(int menuPrice, List<OrderLine> specList){
	    int singleMenuPrice=menuPrice;
		for(OrderLine specLine:specList){
			Integer singleSumPrice=specLine.getMenuSumPrice();
			if(WebplusUtil.isNotEmpty(singleSumPrice)){
				singleMenuPrice+=singleSumPrice;
			}
			
		}
		return singleMenuPrice;
	}
	
	/**
	 * 
	 * 简要说明：去除垃圾数据
	 * 编写者：陈骑元
	 * 创建时间：2019年12月10日 上午1:27:08
	 * @param 说明
	 * @return 说明
	 */
	private List<OrderLine> removeWasteData(List<OrderLine> orderLineList){
		List<OrderLine>  orderLineListNew=Lists.newArrayList();
		if(WebplusUtil.isNotEmpty(orderLineList)){
			for(OrderLine orderLine:orderLineList){
				String parentId=orderLine.getParentId();
				if(WebplusUtil.isNotEmpty(parentId)){
					if(checkParentId(parentId,orderLineList)){
						orderLineListNew.add(orderLine);
					}
				}else{
					orderLineListNew.add(orderLine);
				}
			}
		}
		
		return orderLineListNew;
	}
	
	
	
	
	/**
	 * 
	 * 简要说明：检查parentId是否有效
	 * 编写者：陈骑元
	 * 创建时间：2019年12月10日 上午1:29:39
	 * @param 说明
	 * @return 说明
	 */
	private boolean checkParentId(String parentId,List<OrderLine> orderLineList){
		for(OrderLine orderLine:orderLineList){
			if(parentId.equals(orderLine.getLineId())){
				
				return true;
			}
		}
		
		return false;
	}
	/**
	 * 
	 * 简要说明：更新订单
	 * 编写者：陈骑元（chenqiyuan@toonan.com）
	 * 创建时间： 2020年5月3日 下午5:39:10 
	 * @param 说明
	 * @return 说明
	 */
	@Transactional
	public R updateOrder(String orderId,String orderRemark,String appendAddress,
			String orderLines,String removeLineIds) {
		if (WebplusUtil.isEmpty(orderId)) {

			return R.error();
		}
		Order order = new Order();
		order.setOrderId(orderId);
		order.setAppendAddress(appendAddress);
		order.setOrderRemark(orderRemark);
		order.setUpdateTime(WebplusUtil.getDateTime());
		orderService.updateById(order);

		if (WebplusUtil.isNotEmpty(orderLines)) {
			List<Dto> dataList = WebplusJson.fromJson(orderLines);
			List<OrderLine> orderLineList = Lists.newArrayList();
			for (Dto dataDto : dataList) {
				String lineId = dataDto.getString("lineId");
				Integer buyNum = dataDto.getInteger("buyNum");
				if (WebplusUtil.isNotEmpty(lineId)) {
					OrderLine orderLine=new OrderLine();
  	 		    	orderLine.setLineId(lineId);
  	 		    	orderLine.setBuyNum(buyNum);
  	 		    	orderLine.setUpdateTime(WebplusUtil.getDateTime());
  	 		    	orderLineList.add(orderLine);
				}
			}
			if(WebplusUtil.isNotEmpty(orderLineList)){
  	 	    	orderLineService.updateBatchById(orderLineList);
  	 	    }
  	 	    if(WebplusUtil.isNotEmpty(orderLineList)){  //同时更新规格的数量
  	 	    	for(OrderLine orderLine: orderLineList){
  	 	    		if(WebplusUtil.isNotEmpty(orderLine.getLineId())){
  	 	    			OrderLine entity=new OrderLine();
  	 		    		entity.setBuyNum(orderLine.getBuyNum());
  	 		    		entity.setUpdateTime(WebplusUtil.getDateTime());
  	 		    		EntityWrapper<OrderLine> wrapper=new EntityWrapper<OrderLine>();
  	 		    		wrapper.eq("parent_id", orderLine.getLineId());
  	 		    		orderLineService.update(entity, wrapper);
  	 	    		}
  	 	    		
  	 	    	}
  	 	    }
		}
		 if(WebplusUtil.isNotEmpty(removeLineIds)){
	  	    	List<String> lineIdListRemove=WebplusFormater.separatStringToList(removeLineIds);
	  	    	orderLineService.deleteBatchIds(lineIdListRemove);
	  	    	for(String lineId:lineIdListRemove){
	  	    		Dto pDto=Dtos.newDto();
	  				pDto.put("parent_id", lineId);
	  				orderLineService.deleteByMap( pDto);	//同时删除规格			
	  	    	}
	  	    }
		  if(WebplusUtil.isNotAnyEmpty(orderLines,removeLineIds)) {  //重新更新订单
			  
			  this.updateCountOrder(orderId);
		  }
		 return R.ok();
	}
	/**
	 * 
	 * 简要说明：获取打印规格名字
	 * 编写者：陈骑元
	 * 创建时间：2019年9月1日 下午8:14:52
	 * @param 说明
	 * @return 说明
	 */
	private String getPrintSpecName(String lineId,List<OrderLine> orderLineList){
		String specName="";
		for(OrderLine orderLine:orderLineList){
			String whetherSpec=orderLine.getWhetherSpec();
	    	if(WebplusCons.WHETHER_YES.equals(whetherSpec)){
	    		if(lineId.equals(orderLine.getParentId())){
	    			specName+="■"+orderLine.getPrintMenuName();
	    		}
	    	}
		}
		return specName;
	}
	/**
	 * 
	 * 简要说明：获取合并订单的orderId
	 * 编写者：陈骑元（chenqiyuan@toonan.com）
	 * 创建时间： 2020年6月24日 上午7:53:39 
	 * @param 说明
	 * @return 说明
	 */
	public List<String> getParentOrderId(String orderId){
		List<String> idList=Lists.newArrayList();
		EntityWrapper<OrderLink> wrapper=new EntityWrapper<OrderLink>();
		wrapper.eq("parent_order_no", orderId);
		List<OrderLink> orderLinkList=orderLinkService.selectList(wrapper);
		for(OrderLink orderLink:orderLinkList) {
			idList.add(orderLink.getChildOrderNo());
		}
		return idList;
	}
	/**
	 * 
	 * 简要说明：发起支付
	 * 编写者：陈骑元（chenqiyuan@toonan.com）
	 * 创建时间： 2020年6月27日 下午8:09:38 
	 * @param 说明
	 * @return 说明
	 */
	public R sendPay(String orderId,String token) {
		if(WebplusUtil.isEmpty(orderId)) {
			
			return R.error("注文番号が無意味です");
		}
	   Order order=orderService.selectById(orderId);
	   if(WebplusUtil.isEmpty(order)) {
		   
		   return R.error("まだ注文されてません");
	   }
	   if(WebplusCons.WHETHER_YES.equals(order.getWhetherPay())) {
		   
		   return R.error("注文はもう支払いました");
	   }
	   String shopId=order.getShopId();
	   Shop shop=shopService.selectById(shopId);
	   String payConfigId=shop.getPayConfigId();
	   String payAccount=shop.getPayAccount();
	   String payPassword=shop.getPayPassword();
	   if(WebplusUtil.isAnyEmpty(payConfigId,payAccount,payPassword)) {
		   
		   return R.error("A payment failure: parameters (no configuration configid, account, password)");
	   }
	    UserToken userToken=WebplusCache.getUserToken(token);
	    if(WebplusUtil.isNotEmpty(userToken)) {
	       Member member=memberService.selectById(userToken.getUserId());
	       if(WebplusUtil.isNotEmpty(member)&&WebplusCons.WHETHER_NO.equals(member.getWhetherCard())) {
	    	   String memberNum=member.getMemberNum();
	    	   if(WebplusUtil.isNotEmpty(memberNum)) {
	    		   String retUrl= WebplusCache.getParamValue(WebplusCons.REQUEST_URL_KEY)+"/h5/memberReturn?memberId="+member.getMemberId()+"&shopId="+shopId+"&orderFoodType="+shop.getOrderFoodType();
	    		   if(WebplusUtil.isNotEmpty(token)) {
	    				retUrl+="&token="+token;
	    			}
	    		   Dto configDto=Dtos.newDto();
			  	   configDto.put("configid",  payConfigId);
			  	   Dto memberDto=Dtos.newDto();
			  	   memberDto.put("MemberID", memberNum);
			  	   memberDto.put("MemberName", member.getUsername());
			       memberDto.put("Cardeditno",System.currentTimeMillis()+"");
			  	   memberDto.put("RetUrl", retUrl);
			  	   configDto.put("member", memberDto);
			  	   String memberson=WebplusJson.toJson(configDto);
				   String baseJson=WebplusHashCodec.base64(memberson);
				   String shaStr=WebplusHashCodec.sha256(baseJson+payPassword);
				   log.info("发起会员注册["+memberNum+"]:其中生成内容JSON="+memberson+" 加密后Base64="+baseJson+",sha="+shaStr);
				   String payUrl=PAY_URL+"/"+payAccount+"/member/"+baseJson+"."+shaStr;
				   log.info("发起会员注册["+memberNum+"]生成会员链接：payUrl="+payUrl);
				   return R.ok().put("payUrl", payUrl);
	    	   }
	    		   
	    	  
	       }
	       
	    }
	    Dto payDto = Dtos.newDto();
		payDto.put("configid", payConfigId);
		Dto transactionDto = Dtos.newDto();
		transactionDto.put("OrderID",order.getOrderId());
		transactionDto.put("Amount", order.getTotalAmount());
		//transactionDto.put("Tax", order.getTaxAmount());
		String[] payMethods = new String[] {"credit"};
		transactionDto.put("PayMethods", payMethods);
		String retUrl= WebplusCache.getParamValue(WebplusCons.REQUEST_URL_KEY)+"/h5/payReturn?shopId="+shopId+"&orderFoodType="+shop.getOrderFoodType();
		if(WebplusUtil.isNotEmpty(token)) {
			retUrl+="&token="+token;
		}
		transactionDto.put("RetUrl", retUrl);
		transactionDto.put("RetryMax", 99);
		transactionDto.put("ExpireDays", 1);
		payDto.put("transaction", transactionDto);
		String memberId=order.getMemberId();
		if(WebplusUtil.isNotEmpty(memberId)) {
			Member member=memberService.selectById(memberId);
			if(WebplusUtil.isNotEmpty(member)) {
				Dto cardDto = Dtos.newDto();
				cardDto.put("JobCd", "AUTH");
				cardDto.put("Method", "1");
				cardDto.put("PayTime", null);
				cardDto.put("ItemCode", "0000990");
				cardDto.put("TdFlag", "0");
				cardDto.put("MemberID", member.getMemberNum());
				payDto.put("credit", cardDto);
			}
		}
		
		Dto desDto=Dtos.newDto();
		desDto.put("TemplateID", "designA");
		desDto.put("ShopName:", shop.getShopName());
		desDto.put("Lang", "ja");
		payDto.put("displaysetting", desDto);
		String payJson=WebplusJson.toJson(payDto);
		
	    String baseJson=WebplusHashCodec.base64(payJson);
	    String shaStr=WebplusHashCodec.sha256(baseJson+payPassword);
	    log.info("发起订单["+orderId+"]支付:其中生成内容JSON="+payJson+" 加密后Base64="+baseJson+",sha="+shaStr);
	    String payUrl=PAY_URL+"/"+payAccount+"/checkout/"+baseJson+"."+shaStr;
	    log.info("发起订单["+orderId+"]支付生成支付链接：payUrl="+payUrl);
	    return R.ok().put("payUrl", payUrl);
	}
}
