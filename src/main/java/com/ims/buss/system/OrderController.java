package com.ims.buss.system;

import org.springframework.web.bind.annotation.*;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.ims.core.cache.WebplusCache;
import com.ims.core.constant.WebplusCons;
import com.ims.core.matatype.Dto;
import com.ims.core.matatype.Dtos;
import com.ims.core.util.WebplusFormater;
import com.ims.core.util.WebplusUtil;
import com.ims.core.vo.Item;
import com.ims.core.vo.R;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.ims.buss.constant.BussCons;
import com.ims.buss.model.Desk;
import com.ims.buss.model.MenuCatalog;
import com.ims.buss.model.MenuDict;
import com.ims.buss.model.MenuLarge;
import com.ims.buss.model.Order;
import com.ims.buss.model.OrderLine;
import com.ims.buss.model.OrderLineSum;
import com.ims.buss.model.OrderSum;
import com.ims.buss.model.Shop;
import com.ims.buss.service.BussCommonService;
import com.ims.buss.service.DeskService;
import com.ims.buss.service.MenuCatalogService;
import com.ims.buss.service.MenuDictService;
import com.ims.buss.service.MenuLargeService;
import com.ims.buss.service.OrderCommonService;
import com.ims.buss.service.OrderLineService;
import com.ims.buss.service.OrderService;
import com.ims.buss.service.ShopService;
import com.ims.buss.util.BussUtil;

import org.springframework.stereotype.Controller;
import com.ims.core.web.BaseController;

/**
 * <p>
 * 订单信息 前端控制器
 * </p>
 *
 * @author 陈骑元
 * @since 2019-07-20
 */
@Controller
@RequestMapping("/buss/order")
public class OrderController extends BaseController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderLineService orderLineService;
    @Autowired
    private ShopService shopService;
	@Autowired
	private DeskService deskService;
	@Autowired
	private BussCommonService bussCommonService;
	@Autowired
	private OrderCommonService orderCommonService;
	@Autowired
	private MenuCatalogService menuCatalogService;
	@Autowired
	private MenuLargeService  menuLargeService ;
	@Autowired
	private MenuDictService menuDictService;


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
		String userId=this.getUserId();
		Dto pDto = Dtos.newDto(request);
		pDto.put("shopId", userId);
		pDto.put("whetherRemove", WebplusCons.WHETHER_NO);
		List<String> orderTypeList=Lists.newArrayList();
		orderTypeList.add(BussCons.ORDER_TYPE_CHILD);
		orderTypeList.add(BussCons.ORDER_TYPE_PARENT);
		orderTypeList.add(BussCons.ORDER_TYPE_COMMON);
		orderTypeList.add(BussCons.ORDER_TYPE_SIGNLE);
		pDto.setOrder("create_time DESC");
		Page<Order> page =orderService.likePage(pDto);
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
	public R save(Order order) {
		boolean result = orderService.insert(order);
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
	 * 创建时间：2019-07-20
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("edit")
	@ResponseBody
	public R edit(String id) {
		Order order=orderService.selectById(id);
		return R.toData(order);
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
	public R update(Order order) {
		boolean result = orderService.updateById(order);
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
		
		boolean result=orderService.deleteById(id);
		if (result) {

			Desk desk = new Desk();
			desk.setDeskStatus(BussCons.DESK_STATUS_EMPTY); // 解除占座
			desk.setEatNum(0);
			desk.setOrderNo("");
			desk.setOrderTime(null);
			desk.setUpdateTime(WebplusUtil.getDateTime());
			EntityWrapper<Desk> wrapper = new EntityWrapper<Desk>();
			wrapper.eq("order_no", id);
			deskService.update(desk, wrapper);
			return R.ok();
			
			
		} else {
			return R.error("");
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
		boolean result = orderService.deleteBatchIds(idList);
		if (result) {
			return R.ok();
		} else {
			return R.error("删除失败");
		}
		
	}
	/**
	 * 
	 * 简要说明：调到详情页面
	 * 编写者：陈骑元
	 * 创建时间：2019-07-20
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("show")
	@ResponseBody
	public R show(String id) {
		Order order=orderService.selectById(id);
		String whetherParentOrder=WebplusCons.WHETHER_NO;
	    if(BussCons.ORDER_TYPE_PARENT.equals(order.getOrderType())){
	       	whetherParentOrder=WebplusCons.WHETHER_YES;
	    }
		Dto pDto=Dtos.newDto();
		if(WebplusCons.WHETHER_YES.equals(whetherParentOrder)){
			List<String> orderIdList=orderCommonService.getParentOrderId(id);
			pDto.put("orderIdList",orderIdList);
			//pDto.put("parentOrderNo", id);
		}else{
			pDto.put("orderId", id);
		}
		pDto.setOrder(" create_time ASC ");
		List<OrderLine> orderLineList=orderLineService.list(pDto);
		orderLineList=this.removeWasteData(orderLineList);
		order=this.getOrder(order, orderLineList, WebplusCons.WHETHER_NO);
		return R.toData(order);
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
	 * 简要说明：获取订单
	 * 编写者：陈骑元
	 * 创建时间：2019年10月19日 下午4:30:14
	 * @param 说明
	 * @return 说明
	 */
	private Order getOrder(Order order,List<OrderLine> orderLineList,String whetherMerge){
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
						eatAmountAfter+= BussUtil.countSubRate(menuSumPrice, subAmount, subRate);
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
			int outTaxAmount= outTaxAmountBefore+ outTaxAmountAfter;
			int taxAmount= taxAmountBefore+taxAmountAfter;
			int subRate=order.getSubRate();
			int subAmount=order.getSubAmount();
			totalAmount=BussUtil.countSubRate(totalAmount, subAmount, subRate);
			order.setTaxAfterAmount(eatAmountAfter+outAmountAfter);
			order.setTaxBeforeAmount(eatAmountBefore+outAmountBefore);
			order.setDeskAmount(deskAmount);
			order.setMenuAmount(menuAmount);
			order.setOutTaxAmount(outTaxAmount);
			order.setSmallTotalAmount(menuAmount+deskAmount);
			order.setTaxAmount(taxAmount);
			order.setConsumeTax(taxAmountBefore);
			if(order.getTotalAmount().intValue()!=totalAmount){
				order.setTotalAmount(totalAmount);
				orderService.updateById(order);
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
	 * 
	 * 简要说明：分页查询 
	 * 编写者：陈骑元
	 * 创建时间：2019-07-20
	 * @param 说明
	 * @return 说明
	 */
	@RequestMapping("listSumDateOrder")
	@ResponseBody
	public R listSumDateOrder() {
		String userId=this.getUserId();
		int subMinute=0;
		Shop shop=shopService.selectById(userId);
		if(WebplusUtil.isNotEmpty(shop)){
			String timeReport=shop.getTimeReport();
			subMinute=BussUtil.countHourTime(timeReport);
		}
		
		Dto pDto = Dtos.newDto(request);
		pDto.put("shopId", userId);
		pDto.put("whetherPay", WebplusCons.WHETHER_YES);
		pDto.put("subMinute", subMinute);
	    List<String> orderTypeList=Lists.newArrayList();
		orderTypeList.add(BussCons.ORDER_TYPE_COMMON);
	    orderTypeList.add(BussCons.ORDER_TYPE_PARENT);
	    orderTypeList.add(BussCons.ORDER_TYPE_SIGNLE);
	    pDto.put("orderTypeList", orderTypeList);
		Page<OrderSum> page =bussCommonService.listSumDateOrderPage(pDto);
		return R.toPage(page);
	}
	
	/**
	 * 
	 * @param 下载CSV模板
	 * @param 
	 * @return
	 */
	@PostMapping("exportSumDateOrder")
	@ResponseBody
    public R exportSumDateOrder(){
		String userId=this.getUserId();
		int subMinute=0;
		Shop shop=shopService.selectById(userId);
		if(WebplusUtil.isNotEmpty(shop)){
			String timeReport=shop.getTimeReport();
			subMinute=BussUtil.countHourTime(timeReport);
		}
		
		Dto pDto = Dtos.newDto(request);
		pDto.put("shopId", userId);
		pDto.put("whetherPay", WebplusCons.WHETHER_YES);
		pDto.put("subMinute", subMinute);
	    List<String> orderTypeList=Lists.newArrayList();
		orderTypeList.add(BussCons.ORDER_TYPE_COMMON);
	    orderTypeList.add(BussCons.ORDER_TYPE_PARENT);
	    orderTypeList.add(BussCons.ORDER_TYPE_SIGNLE);
	    pDto.put("orderTypeList", orderTypeList);
	    List<OrderSum> orderSumList=bussCommonService.listSumDateOrder(pDto);
		List<Dto> dataList=WebplusUtil.copyPropertiesList( orderSumList);
		Dto dataDto=Dtos.newDto();
		dataDto.put("dataList", dataList);
		String fid=BussUtil.createTemplateExcel("excel/export_date_order_template.xlsx", dataDto);
		if(WebplusUtil.isNotEmpty(fid)){
			
		   
		   return R.ok().put("fid", fid);
		}
		return R.error();
	}
	/**
	 * 
	 * 简要说明：分页查询 
	 * 编写者：陈骑元
	 * 创建时间：2019-07-20
	 * @param 说明
	 * @return 说明
	 */
	@RequestMapping("listSumDateTakeOutOrder")
	@ResponseBody
	public R listSumDateTakeOutOrder() {
		String userId=this.getUserId();
		int subMinute=0;
		Shop shop=shopService.selectById(userId);
		if(WebplusUtil.isNotEmpty(shop)){
			String timeReport=shop.getTimeReport();
			subMinute=BussUtil.countHourTime(timeReport);
		}
		
		Dto pDto = Dtos.newDto(request);
		pDto.put("shopId", userId);
		pDto.put("whetherPay", WebplusCons.WHETHER_YES);
		pDto.put("subMinute", subMinute);
	    List<String> orderTypeList=Lists.newArrayList();
		orderTypeList.add(BussCons.ORDER_TYPE_MOBILE);
	    orderTypeList.add(BussCons.ORDER_TYPE_NETWORK);
	    pDto.put("orderTypeList", orderTypeList);
		Page<OrderSum> page =bussCommonService.listSumDateOrderPage(pDto);
		return R.toPage(page);
	}
	
	
	
	/**
	 * 
	 * 简要说明：分页查询 
	 * 编写者：陈骑元
	 * 创建时间：2019-07-20
	 * @param 说明
	 * @return 说明
	 */
	@RequestMapping("listSumMonthOrder")
	@ResponseBody
	public R listSumMonthOrder() {
		String userId=this.getUserId();
		Dto pDto = Dtos.newDto(request);
		int subMinute=0;
		Shop shop=shopService.selectById(userId);
		if(WebplusUtil.isNotEmpty(shop)){
			String timeReport=shop.getTimeReport();
			subMinute=BussUtil.countHourTime(timeReport);
		}
		pDto.put("shopId", userId);
		pDto.put("whetherPay", WebplusCons.WHETHER_YES);
		pDto.put("subMinute", subMinute);
		List<String> orderTypeList=Lists.newArrayList();
		orderTypeList.add(BussCons.ORDER_TYPE_COMMON);
		orderTypeList.add(BussCons.ORDER_TYPE_PARENT);
		orderTypeList.add(BussCons.ORDER_TYPE_SIGNLE);
		pDto.put("orderTypeList", orderTypeList);
		Page<OrderSum> page =bussCommonService.listSumMonthOrderPage(pDto);
		return R.toPage(page);
	}
	
	/**
	 * 
	 * @param 下载CSV模板
	 * @param 
	 * @return
	 */
	@PostMapping("exportSumMonthOrder")
	@ResponseBody
    public R exportSumMonthOrder(){
		String userId=this.getUserId();
		Dto pDto = Dtos.newDto(request);
		int subMinute=0;
		Shop shop=shopService.selectById(userId);
		if(WebplusUtil.isNotEmpty(shop)){
			String timeReport=shop.getTimeReport();
			subMinute=BussUtil.countHourTime(timeReport);
		}
		pDto.put("shopId", userId);
		pDto.put("whetherPay", WebplusCons.WHETHER_YES);
		pDto.put("subMinute", subMinute);
		List<String> orderTypeList=Lists.newArrayList();
		orderTypeList.add(BussCons.ORDER_TYPE_COMMON);
		orderTypeList.add(BussCons.ORDER_TYPE_PARENT);
		orderTypeList.add(BussCons.ORDER_TYPE_SIGNLE);
		pDto.put("orderTypeList", orderTypeList);
	    List<OrderSum> orderSumList=bussCommonService.listSumMonthOrder(pDto);
		List<Dto> dataList=WebplusUtil.copyPropertiesList( orderSumList);
		Dto dataDto=Dtos.newDto();
		dataDto.put("dataList", dataList);
		String fid=BussUtil.createTemplateExcel("excel/export_month_order_template.xlsx", dataDto);
		if(WebplusUtil.isNotEmpty(fid)){
			
		   
		   return R.ok().put("fid", fid);
		}
		return R.error();
	}
	/**
	 * 
	 * 简要说明：分页查询 
	 * 编写者：陈骑元
	 * 创建时间：2019-07-20
	 * @param 说明
	 * @return 说明
	 */
	@RequestMapping("listSumMonthTakeOutOrder")
	@ResponseBody
	public R listSumMonthTakeOutOrder() {
		String userId=this.getUserId();
		Dto pDto = Dtos.newDto(request);
		int subMinute=0;
		Shop shop=shopService.selectById(userId);
		if(WebplusUtil.isNotEmpty(shop)){
			String timeReport=shop.getTimeReport();
			subMinute=BussUtil.countHourTime(timeReport);
		}
		pDto.put("shopId", userId);
		pDto.put("whetherPay", WebplusCons.WHETHER_YES);
		pDto.put("subMinute", subMinute);
		List<String> orderTypeList=Lists.newArrayList();
		orderTypeList.add(BussCons.ORDER_TYPE_MOBILE);
	    orderTypeList.add(BussCons.ORDER_TYPE_NETWORK);
		pDto.put("orderTypeList", orderTypeList);
		Page<OrderSum> page =bussCommonService.listSumMonthOrderPage(pDto);
		return R.toPage(page);
	}
	
	/**
	 * 
	 * 简要说明：查询汇总订单
	 * 编写者：陈骑元
	 * 创建时间：2019-07-20
	 * @param 说明
	 * @return 说明
	 */
	@RequestMapping("showSumOrder")
	@ResponseBody
	public R showSumOrder(String queryDate,String queryMonth) {
		String userId=this.getUserId();
		int subMinute=0;
		Shop shop=shopService.selectById(userId);
		if(WebplusUtil.isNotEmpty(shop)){
			String timeReport=shop.getTimeReport();
			subMinute=BussUtil.countHourTime(timeReport);
		}
		
		Dto pDto = Dtos.newDto(request);
		pDto.put("shopId", userId);
		pDto.put("whetherPay", WebplusCons.WHETHER_YES);
		pDto.put("queryDate", queryDate);
		pDto.put("queryMonth", queryMonth);
		pDto.put("subMinute",subMinute);
		List<OrderLine > orderLineList=bussCommonService.listSumOrderLine(pDto);
		List<OrderLine> orderLineListNew = Lists.newArrayList();
		for (OrderLine orderLine : orderLineList) {
			String menuIndexId = orderLine.getMenuIndexId();
			String whetherBuffet = orderLine.getWhetherBuffet();
			if (WebplusCons.WHETHER_NO.equals(whetherBuffet) || WebplusUtil.isEmpty(menuIndexId)) {
				Integer buyNum = BussUtil.dealEmptyAmount(orderLine.getBuyNum());
				Integer menuPrice = BussUtil.dealEmptyAmount(orderLine.getMenuPrice());
				Integer menuSumPrice = menuPrice * buyNum;
				orderLine.setMenuSumPrice(menuSumPrice);
				orderLineListNew.add(orderLine);
			}

		}
		 List<Dto> dataList=this.getSumOrderLine(orderLineListNew);
         return R.toList(dataList);
	}
	
	/**
	 * 
	 * 简要说明：查询汇总订单
	 * 编写者：陈骑元
	 * 创建时间：2019-07-20
	 * @param 说明
	 * @return 说明
	 */
	@RequestMapping("showSumTakeOutOrder")
	@ResponseBody
	public R showSumTakeOutOrder(String queryDate,String queryMonth) {
		String userId=this.getUserId();
		Dto pDto = Dtos.newDto(request);
		pDto.put("shopId", userId);
		pDto.put("whetherPay", WebplusCons.WHETHER_YES);
		pDto.put("queryDate", queryDate);
		pDto.put("queryMonth", queryMonth);
		List<OrderLine > orderLineList=bussCommonService.listSumTakeOutOrderLine(pDto);
		List<OrderLine> orderLineListNew = Lists.newArrayList();
		for (OrderLine orderLine : orderLineList) {
			String menuIndexId = orderLine.getMenuIndexId();
			String whetherBuffet = orderLine.getWhetherBuffet();
			if (WebplusCons.WHETHER_NO.equals(whetherBuffet) || WebplusUtil.isEmpty(menuIndexId)) {
				Integer buyNum = BussUtil.dealEmptyAmount(orderLine.getBuyNum());
				Integer menuPrice = BussUtil.dealEmptyAmount(orderLine.getMenuPrice());
				Integer menuSumPrice = menuPrice * buyNum;
				orderLine.setMenuSumPrice(menuSumPrice);
				orderLineListNew.add(orderLine);
			}

		}
		 List<Dto> dataList=this.getSumOrderLine(orderLineListNew);
         return R.toList(dataList);
	}
	
	/**
	 * 
	 * 简要说明：
	 * 编写者：陈骑元
	 * 创建时间：2019年11月10日 下午3:34:48
	 * @param 说明
	 * @return 说明
	 */
	private List<Dto> getSumOrderLine(List<OrderLine> orderLineList){
		List<OrderLine>   orderLineListNew = this.getOrderLineList( orderLineList, WebplusCons.WHETHER_YES);
        orderLineListNew=this.getMergeOrderLine( orderLineListNew);   //合并订单
        Collections.sort(orderLineListNew, new Comparator< OrderLine>() {

			@Override
			public int compare(OrderLine o1, OrderLine o2) {
				if(o2.getMenuSumPrice().intValue()>o1.getMenuSumPrice().intValue()){
					
					return 1;
				}else{
					return -1;
				}
				
			}
        	
        });
	    List<Dto> dataList=Lists.newArrayList();
	    for(OrderLine orderLine:orderLineListNew){
	    	Dto dataDto=Dtos.newDto();
	    	
	    	dataDto.put("menuName", orderLine.getMenuName());
	    	dataDto.put("buyNum", orderLine.getBuyNum());
	    	dataDto.put("menuSumPrice", orderLine.getMenuSumPrice());
	    	dataDto.put("menuPrice", orderLine.getMenuPrice());
	    	dataList.add(dataDto);
	    }
	    
	    return dataList;
	    
	}
	
	/**
	 * 
	 * 简要说明：分页查询 
	 * 编写者：陈骑元
	 * 创建时间：2019-07-20
	 * @param 说明
	 * @return 说明
	 */
	@RequestMapping("listTakeOutOrder")
	@ResponseBody
	public R listTakeOutOrder() {
		String userId=this.getUserId();
		Dto pDto = Dtos.newDto(request);
		pDto.put("shopId", userId);
		pDto.put("whetherRemove", WebplusCons.WHETHER_NO);
		List<String> orderTypeList=Lists.newArrayList();
		orderTypeList.add(BussCons.ORDER_TYPE_NETWORK);
		orderTypeList.add(BussCons.ORDER_TYPE_MOBILE);
		pDto.setOrder("create_time DESC");
		Page<Order> page =orderService.likePage(pDto);
		WebplusCache.convertItem(page);
		return R.toPage(page);
	}
	
	/**
	 * 
	 * 简要说明：分类
	 * 编写者：陈骑元（chenqiyuan@toonan.com）
	 * 创建时间： 2020年7月29日 上午8:19:42 
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("listCatalog")
	@ResponseBody
	public R listCatalog(String catalogWay) {
		String shopId=this.getUserId();
		List<Item> itemList=Lists.newArrayList();
		Dto pDto=Dtos.newDto();
		pDto.put("shopId", shopId);
		pDto.setOrder("sort_no ASC");
	    pDto.put("languageType",BussCons.LANGUAGE_TYPE_JP);
		if(BussCons.CATALOG_WAY_LARGE.equals(catalogWay)) {
			List<MenuLarge> menuLargeList=menuLargeService.list(pDto);
			for(MenuLarge menuLarge:menuLargeList) {
				Item item=new Item();
				item.setItemCode(menuLarge.getLargeId()	);
				item.setItemName(menuLarge.getLargeName());
				itemList.add(item);
			}
		}else if(BussCons.CATALOG_WAY_CATALOG.equals(catalogWay)) {
			pDto.put("whetherBuffet", WebplusCons.WHETHER_NO);
			pDto.setOrder("large_id ASC,sort_no ASC");
			List<MenuCatalog> menuCatalogList=menuCatalogService.list(pDto);
			for(MenuCatalog menuCatalog:menuCatalogList) {
				Item item=new Item();
				item.setItemCode(menuCatalog.getCatalogIndexId());
				item.setItemName(menuCatalog.getCatalogName());
				itemList.add(item);
			}
			
		}else if(BussCons.CATALOG_WAY_DICT.equals(catalogWay)) {
			pDto.setOrder("catalog_index_id ASC,sort_no ASC");
			List<MenuDict> menuDictList=menuDictService.list(pDto);
			for(MenuDict menuDict:menuDictList) {
				Item item=new Item();
				item.setItemCode(menuDict.getMenuIndexId());
				item.setItemName(menuDict.getMenuName());
				itemList.add(item);
			}
		}else if(BussCons.CATALOG_WAY_BUFFET.equals(catalogWay)) {
			pDto.setOrder("catalog_index_id ASC");
			pDto.put("whetherBuffet", WebplusCons.WHETHER_YES);
			List<MenuCatalog> menuCatalogList=menuCatalogService.list(pDto);
			for(MenuCatalog menuCatalog:menuCatalogList) {
				Item item=new Item();
				item.setItemCode(menuCatalog.getCatalogIndexId());
				item.setItemName(menuCatalog.getCatalogName());
				itemList.add(item);
			}
		}
		
		
		return R.toList(itemList);
		
	}
	
	/**
	 * 
	 * 简要说明：菜品分类统计分页
	 * 编写者：陈骑元（chenqiyuan@toonan.com）
	 * 创建时间： 2020年7月29日 上午8:16:32 
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("listOrderLineSumPage")
	@ResponseBody
	public R  listOrderLineSumPage(String catalogWay,String catalogId,
			String timeWay,String queryTime,String beginMinute,String endMinute,
			String queryMonth,String queryWeek,String page,String limit) {
		String userId=this.getUserId();
		int subMinute=0;
		Shop shop=shopService.selectById(userId);
		if(WebplusUtil.isNotEmpty(shop)){
			String timeReport=shop.getTimeReport();
			subMinute=BussUtil.countHourTime(timeReport);
		}
		Dto pDto=Dtos.newDto();
		pDto.put("page", page);
		pDto.put("limit",limit);
		String shopId=this.getUserId();
		pDto.put("shopId", shopId);
		pDto.put("subMinute", subMinute);
		if(BussCons.CATALOG_WAY_LARGE.equals(catalogWay)) {
			 pDto.put("largeId", catalogId);
		}else if(BussCons.CATALOG_WAY_CATALOG.equals(catalogWay)) {
			 pDto.put("catalogIndexId", catalogId);
			 pDto.put("whetherBuffet", WebplusCons.WHETHER_NO);
		}else if(BussCons.CATALOG_WAY_DICT.equals(catalogWay)) {
		   pDto.put("menuIndexId", catalogId);
		   pDto.put("whetherBuffet", WebplusCons.WHETHER_NO);
		}else if(BussCons.CATALOG_WAY_BUFFET.equals(catalogWay)) {
			 pDto.put("catalogIndexId", catalogId);
			 pDto.put("whetherBuffet", WebplusCons.WHETHER_YES);
		}
		if(BussCons.TIME_WAY_MONTH.equals(timeWay)) {
			pDto.put("queryMonth", queryTime);
		}else if(BussCons.TIME_WAY_DATE.equals(timeWay)) {
			pDto.put("queryDate", queryTime);
		}else if(BussCons.TIME_WAY_WEEK.equals(timeWay)) {
			pDto.put("queryMonth", queryMonth);
			pDto.put("queryWeek", queryTime);
		}else {
			pDto.put("queryMonth", queryMonth);
			pDto.put("queryWeek", queryWeek);
		}
		pDto.put("beginMinute", beginMinute);
		pDto.put("endMinute", endMinute);
		pDto.put("whetherPay", WebplusCons.WHETHER_YES);
		Page<OrderLineSum> pageResult=bussCommonService.listOrderLineSumPage(pDto);
		return R.toPage(pageResult);
		
	}
	
	/**
	 * 
	 * 简要说明：菜品分类统计分页
	 * 编写者：陈骑元（chenqiyuan@toonan.com）
	 * 创建时间： 2020年7月29日 上午8:16:32 
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("exportOrderLineSum")
	@ResponseBody
	public R  exportOrderLineSum(String exportWay,String catalogWay,String catalogId,
			String timeWay,String queryTime,String beginMinute,String endMinute,String queryMonth,String queryWeek) {
		Dto pDto=Dtos.newDto();
		String shopId=this.getUserId();
		int subMinute=0;
		Shop shop=shopService.selectById(shopId);
		if(WebplusUtil.isNotEmpty(shop)){
			String timeReport=shop.getTimeReport();
			subMinute=BussUtil.countHourTime(timeReport);
		}
		pDto.put("shopId", shopId);
		if(BussCons.CATALOG_WAY_LARGE.equals(catalogWay)) {
			 pDto.put("largeId", catalogId);
		}else if(BussCons.CATALOG_WAY_CATALOG.equals(catalogWay)) {
			 pDto.put("catalogIndexId", catalogId);
			 pDto.put("whetherBuffet", WebplusCons.WHETHER_NO);
		}else if(BussCons.CATALOG_WAY_DICT.equals(catalogWay)) {
		   pDto.put("menuIndexId", catalogId);
		   pDto.put("whetherBuffet", WebplusCons.WHETHER_NO);
		}else if(BussCons.CATALOG_WAY_BUFFET.equals(catalogWay)) {
			 pDto.put("catalogIndexId", catalogId);
			 pDto.put("whetherBuffet", WebplusCons.WHETHER_YES);
		}
		if(BussCons.TIME_WAY_MONTH.equals(timeWay)) {
			pDto.put("queryMonth", queryTime);
		}else if(BussCons.TIME_WAY_DATE.equals(timeWay)) {
			
			pDto.put("queryDate", queryTime);
		}else if(BussCons.TIME_WAY_WEEK.equals(timeWay)) {
			pDto.put("queryMonth", queryMonth);
			pDto.put("queryWeek", queryTime);
		}else {
			pDto.put("queryMonth", queryMonth);
			pDto.put("queryWeek", queryWeek);
		}
		String countName="";
		if("1".equals(exportWay)) {
			countName="商品統計（"+queryTime+"）";
		}else if("2".equals(exportWay)) {
			countName="時間帯売上（"+queryTime+"）";
		}else {
			countName="曜日売上（"+queryTime+"）";
		}
		pDto.put("subMinute", subMinute);
		pDto.put("beginMinute", beginMinute);
		pDto.put("endMinute", endMinute);
		pDto.put("whetherPay", WebplusCons.WHETHER_YES);
	    List<OrderLineSum> orderLineSumList=bussCommonService.listOrderLineSum(pDto);
	    List<Dto> dataList=WebplusUtil.copyPropertiesList(orderLineSumList);
		Dto dataDto=Dtos.newDto();
		dataDto.put("dataList", dataList);
		dataDto.put("countName", countName);
		String fid=BussUtil.createTemplateExcel("excel/export_orderLine_template.xlsx", dataDto);
		if(WebplusUtil.isNotEmpty(fid)){
			
		   
		   return R.ok().put("fid", fid);
		}
		return R.error();
	}
	
	/**
	 * 
	 * 简要说明：支付订单结算
	 * 编写者：陈骑元
	 * 创建时间：2019-07-20
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("payOrder")
	@ResponseBody
	public R payOrder(String id) {
		Order entityDb=orderService.selectById(id);
		String userId=this.getUserId();
		Order order=new Order();
		order.setOrderId(id);
		order.setWhetherPay(WebplusCons.WHETHER_YES);
		order.setPayTime(WebplusUtil.getDateTime());
		order.setPayWay(BussCons.PAY_WAY_CASH);
		order.setUpdateBy(userId);
		order.setUpdateTime(WebplusUtil.getDateTime());
		order.setCashAmount(entityDb.getTotalAmount());
		boolean result=orderService.updateById(order);
		if (result) {

			Desk desk = new Desk();
			desk.setDeskStatus(BussCons.DESK_STATUS_EMPTY); // 解除占座
			desk.setEatNum(0);
			desk.setOrderNo("");
			desk.setOrderTime(null);
			desk.setUpdateTime(WebplusUtil.getDateTime());
			EntityWrapper<Desk> wrapper = new EntityWrapper<Desk>();
			wrapper.eq("order_no", id);
			deskService.update(desk, wrapper);
			return R.ok();
			
			
		} else {
			return R.error("");
		}
		
	}
	/**
	 * 
	 * 简要说明：修改订单支付方式
	 * 编写者：陈骑元（chenqiyuan@toonan.com）
	 * 创建时间： 2020年11月16日 下午11:00:52 
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("updatePayWay")
	@ResponseBody
	public R updatePayWay(Order order) {
		if(WebplusUtil.isEmpty(order.getCashAmount())) {
			order.setCashAmount(0);
		}
		if(WebplusUtil.isEmpty(order.getCreditAmount())) {
			order.setCreditAmount(0);
		}
		if(WebplusUtil.isEmpty(order.getOtherAmount())) {
			order.setOtherAmount(0);
		}
		String userId=this.getUserId();
		order.setUpdateBy(userId);
		order.setUpdateTime(WebplusUtil.getDateTime());
		boolean result=orderService.updateById(order);
		if (result) {
			
			return R.ok();
		}
		return R.error();
		
	}
}

