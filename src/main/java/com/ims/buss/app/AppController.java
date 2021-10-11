package com.ims.buss.app;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ClassUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.google.common.collect.Lists;
import com.ims.buss.asyncTask.PrintAsyncTask;
import com.ims.buss.constant.BussCons;
import com.ims.buss.model.ApkVersion;
import com.ims.buss.model.Area;
import com.ims.buss.model.BuffetSpec;
import com.ims.buss.model.CashboxRecord;
import com.ims.buss.model.Desk;
import com.ims.buss.model.Member;
import com.ims.buss.model.MenuCatalog;
import com.ims.buss.model.MenuDict;
import com.ims.buss.model.MenuLarge;
import com.ims.buss.model.Order;
import com.ims.buss.model.OrderLine;
import com.ims.buss.model.OrderLineSum;
import com.ims.buss.model.OrderLink;
import com.ims.buss.model.OrderSum;
import com.ims.buss.model.Printer;
import com.ims.buss.model.Shop;
import com.ims.buss.print.FzCloudUtil;
import com.ims.buss.service.ApkVersionService;
import com.ims.buss.service.AreaService;
import com.ims.buss.service.BuffetSpecService;
import com.ims.buss.service.BussCommonService;
import com.ims.buss.service.CashboxRecordService;
import com.ims.buss.service.DeskService;
import com.ims.buss.service.MemberService;
import com.ims.buss.service.MenuCatalogService;
import com.ims.buss.service.MenuDictService;
import com.ims.buss.service.MenuLargeService;
import com.ims.buss.service.OrderCommonService;
import com.ims.buss.service.OrderLineService;
import com.ims.buss.service.OrderLinkService;
import com.ims.buss.service.OrderService;
import com.ims.buss.service.PrinterService;
import com.ims.buss.service.ShopService;
import com.ims.buss.util.BussCache;
import com.ims.buss.util.BussUtil;
import com.ims.buss.util.PointApiUtil;
import com.ims.core.cache.WebplusCache;
import com.ims.core.constant.WebplusCons;
import com.ims.core.matatype.Dto;
import com.ims.core.matatype.Dtos;
import com.ims.core.matatype.impl.HashDto;
import com.ims.core.util.WebplusFile;
import com.ims.core.util.WebplusFormater;
import com.ims.core.util.WebplusHashCodec;
import com.ims.core.util.WebplusJson;
import com.ims.core.util.WebplusQrcode;
import com.ims.core.util.WebplusSqlHelp;
import com.ims.core.util.WebplusUtil;
import com.ims.core.vo.Item;
import com.ims.core.vo.R;
import com.ims.core.vo.UserToken;
import com.ims.core.web.BaseController;
import com.ims.system.constant.SystemCons;
import com.ims.system.model.User;
import com.ims.system.service.UserService;

/**
 * 
 * 类名:com.ims.buss.app.AppController 描述:app接口 编写者:陈骑元 创建时间:2019年7月21日 下午10:59:30
 * 修改说明:
 */
@Controller
@RequestMapping("/app")
public class AppController extends BaseController {

	@Autowired
	private UserService userService;
	@Autowired
	private DeskService deskService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private OrderLineService orderLineService;
	@Autowired
	private ShopService shopService;
	@Autowired
	private AreaService areaService;
	@Autowired
	private MenuDictService menuDictService;
	@Autowired
	private MenuCatalogService menuCatalogService;
	@Autowired
	private BussCommonService bussCommonService;
	@Autowired
	private ApkVersionService apkVersionService;
	@Autowired
	private MemberService memberService;
	@Autowired
	private BuffetSpecService buffetSpecService;
	@Autowired
	private PrintAsyncTask printAsyncTask;
	@Autowired
	private OrderCommonService orderCommonService;
	@Autowired
	private CashboxRecordService cashboxRecordService;
	@Autowired
	private OrderLinkService  orderLinkService ;
	@Autowired
	private MenuLargeService  menuLargeService ;
    @Autowired
    private PrinterService printerService;
	/**
	 * 
	 * 简要说明：app用户登陆 编写者：陈骑元 创建时间：2019年7月23日 下午10:40:41
	 * 
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("login")
	@ResponseBody
	public R login(String account, String password) {
		if (WebplusUtil.isEmpty(account)) {

			return R.error("アカウントを入力してください");
		}
		if (WebplusUtil.isEmpty(password)) {

			return R.error("パスワードを入力してください");
		}
		EntityWrapper<User> userWrapper = new EntityWrapper<User>();
		WebplusSqlHelp.eq(userWrapper, "binary account", account);
		WebplusSqlHelp.eq(userWrapper, "is_del", WebplusCons.WHETHER_NO);
		User user = userService.selectOne(userWrapper);
		if (WebplusUtil.isEmpty(user)) {

			return R.error("このアカウントは存在しません");
		}
		if (SystemCons.USER_STATUS_LOCK.equals(user.getStatus())) {

			return R.error("アカウントがロックされます");
		}
		if (!password.equals(user.getPassword())) {

			return R.error("パスワードが間違っています");
		}
		UserToken userToken = new UserToken();
		userToken.setAccount(user.getAccount());
		userToken.setUserId(user.getUserId());
		userToken.setUsername(user.getUsername());
		String token = WebplusCache.createToken(userToken);
		userToken.setToken(token);

		Dto dataDto = Dtos.newDto();
		WebplusUtil.copyProperties(userToken, dataDto);
		Shop shop = shopService.selectById(user.getUserId());
		dataDto.put("shopName", shop.getShopName());
		dataDto.put("shopAddress", shop.getShopAddress());
		dataDto.put("shopTelephone", shop.getShopTelephone());
		dataDto.put("shopFirstImage", shop.getShopFirstImage());
		dataDto.put("shopQrcode", shop.getShopQrcode());
		dataDto.put("taxes", shop.getTaxes());
		dataDto.put("tableAmount", shop.getTableAmount());
		dataDto.put("shopStatus", shop.getShopStatus());
		dataDto.put("deliverAmount", shop.getDeliverAmount());
		return R.toData(dataDto);

	}

	/**
	 * 
	 * 简要说明：修改吃饭人数 编写者：陈骑元 创建时间：2019年7月27日 上午12:04:28
	 * 
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("updateEatNum")
	@ResponseBody
	public R updateEatNum(String deskId, int eatNum) {
		Desk entity = deskService.selectById(deskId);
		if (WebplusUtil.isEmpty(entity)) {

			return R.error("卓番が間違ってます");
		}
		Desk desk = new Desk();
		desk.setDeskId(deskId);
		desk.setEatNum(eatNum);
		if (WebplusUtil.isEmpty(entity.getOrderNo())) {
			String orderNo = WebplusCache.createOrderNum();
			desk.setOrderNo(orderNo);
		} else {
			Order order = new Order();
			order.setOrderId(entity.getOrderNo());
			order.setDeskNum(desk.getEatNum());
			order.setEatNum(desk.getEatNum());
			order.setUpdateTime(WebplusUtil.getDateTime());
			orderService.updateById(order);
		}
		desk.setUpdateTime(WebplusUtil.getDateTime());
		boolean result = deskService.updateById(desk);
		if (result) {

			return R.ok();
		}
		return R.error();
	}

	/**
	 * 
	 * 简要说明：获取二维码url连接 编写者：陈骑元 创建时间：2019年7月27日 上午12:04:28
	 * 
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("getQrcodeUrl")
	@ResponseBody
	public R getQrcodeUrl(String deskId) {
		if (WebplusUtil.isEmpty(deskId)) {

			return R.error("卓番が間違ってます");
		}
		Desk desk = deskService.selectById(deskId);
		if (WebplusUtil.isEmpty(desk)) {

			return R.error("卓番が間違ってます");
		}
		String orderNo = desk.getOrderNo();
		if (WebplusUtil.isEmpty(orderNo)) {
			orderNo = WebplusCache.createOrderNum();
			desk.setOrderNo(orderNo);
			desk.setUpdateTime(WebplusUtil.getDateTime());
			deskService.updateById(desk);
		}
		String shopId = desk.getShopId();
		String accessToken = WebplusHashCodec.encryptBase64(orderNo);
		String qrcodeUrl = WebplusCache.getParamValue(WebplusCons.REQUEST_URL_KEY) + "/h5/index.html?shopId=" + shopId
				+ "&accessToken=" + accessToken;
		Dto dataDto = Dtos.newDto();
		dataDto.put("qrcodeUrl", qrcodeUrl);
		return R.toData(dataDto);
	}
	/**
	 * 
	 * 简要说明：获取二维码url连接 编写者：陈骑元 创建时间：2019年7月27日 上午12:04:28
	 * 
	 * @param 说明
	 * @return 说明
	 * @throws FileNotFoundException 
	 */
	@PostMapping("sendDeskQrcodePrint")
	@ResponseBody
	public R sendDeskQrcodePrint(String deskId)  {
		if (WebplusUtil.isEmpty(deskId)) {
			
			return R.error("卓番が間違ってます");
		}
		Desk desk = deskService.selectById(deskId);
		if (WebplusUtil.isEmpty(desk)) {
			
			return R.error("卓番が間違ってます");
		}
		String orderNo = desk.getOrderNo();
		if (WebplusUtil.isEmpty(orderNo)) {
			orderNo = WebplusCache.createOrderNum();
			desk.setOrderNo(orderNo);
			desk.setUpdateTime(WebplusUtil.getDateTime());
			deskService.updateById(desk);
		}
		String shopId = desk.getShopId();
		String accessToken = WebplusHashCodec.encryptBase64(orderNo);
		String qrcodeUrl = WebplusCache.getParamValue(WebplusCons.REQUEST_URL_KEY) + "/h5/index.html?shopId=" + shopId
				+ "&accessToken=" + accessToken;
		String folderPath= ClassUtils.getDefaultClassLoader().getResource("static/qrcode").getPath();
		WebplusFile.createFolder(folderPath);
		String fileName=orderNo+".png";
		String filePath=folderPath+File.separator+fileName;
		File file=new File(filePath);
		if(!file.exists()){
			WebplusQrcode.createQrcodeImage(qrcodeUrl, folderPath, fileName); //创建二维码
		}
		String qrcodeImgUrl=WebplusCache.getParamValue(WebplusCons.REQUEST_URL_KEY)+"/qrcode/"+fileName;
		String areaId=desk.getAreaId();
		String deskName=desk.getDeskName();
		if(WebplusUtil.isNotEmpty(areaId)) {
			Area area=areaService.selectById(areaId);
			deskName=area.getAreaName()+deskName;
		}
		printAsyncTask.sendDeskQrcodePrint(shopId,qrcodeImgUrl, deskName);
		return R.ok();
	}

	/**
	 * 简要说明：查询桌子 编写者：陈骑元 创建时间：2019年7月23日 下午10:40:41
	 * 
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("queryDeskList")
	@ResponseBody
	public R queryDeskList(String areaId) {
		String shopId = this.getUserId();
		if (WebplusUtil.isNotEmpty(areaId)) {

			Dto pDto = Dtos.newDto();
			pDto.put("shopId", shopId);
			pDto.put("areaId", areaId);
			pDto.setOrder(" sort_no ASC ");
			List<Desk> deskList = deskService.list(pDto);
			return R.toList(deskList).put("whetherDesk", WebplusCons.WHETHER_YES);
		} else {
			List<Area> areaList = areaService.queryAreaList(shopId);
			if (WebplusUtil.isNotEmpty(areaList)) {

				return R.toList(areaList).put("whetherDesk", WebplusCons.WHETHER_NO);
			}

			Dto pDto = Dtos.newDto();
			pDto.put("shopId", shopId);
			pDto.setOrder(" sort_no ASC ");
			List<Desk> deskList = deskService.list(pDto);
			return R.toList(deskList).put("whetherDesk", WebplusCons.WHETHER_YES);

		}

	}

	/**
	 * 简要说明：查询桌子 编写者：陈骑元 创建时间：2019年7月23日 下午10:40:41
	 * 
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("listAreaAndDesk")
	@ResponseBody
	public R listAreaAndDesk() {
		String shopId = this.getUserId();
		List<Area> areaList = areaService.queryAreaList(shopId);
		if (WebplusUtil.isNotEmpty(areaList)) {
			List<Dto> dataList = Lists.newArrayList();
			for (Area area : areaList) {
				Dto dataDto = Dtos.newDto();
				String areaId = area.getAreaId();
				Dto pDto = Dtos.newDto();
				pDto.put("shopId", shopId);
				pDto.put("areaId", areaId);
				pDto.setOrder(" sort_no ASC ");
				List<Desk> deskList = deskService.list(pDto);
				List<Desk> deskListNew=this.getBuffetDeskList(deskList);
				dataDto.put("areaId", areaId);
				dataDto.put("areaName", area.getAreaName());
				dataDto.put("deskList", deskListNew);
				dataList.add(dataDto);
			}

			return R.ok().put("areaList", areaList).put("deskList", dataList).put("whetherArea",
					WebplusCons.WHETHER_YES);
		} else {

			Dto pDto = Dtos.newDto();
			pDto.put("shopId", shopId);
			pDto.setOrder(" sort_no ASC ");
			
			List<Desk> deskList = deskService.list(pDto);
			List<Desk> deskListNew=this.getBuffetDeskList(deskList);
			return R.toList(deskListNew).put("whetherArea", WebplusCons.WHETHER_NO);

		}

	}

	/**
	 * 简要说明：查询结算订单 编写者：陈骑元 创建时间：2019年7月23日 下午10:40:41
	 * 
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("queryOrderList")
	@ResponseBody
	public R queryOrderList() {
		String shopId = this.getUserId();
		Dto pDto = Dtos.newDto();
		Shop shop = shopService.selectById(shopId);
		String queryDate=WebplusUtil.getCurrentDate();
		if (WebplusUtil.isNotEmpty(shop)) {
			String timeReport = shop.getTimeReport();
			if (BussCons.TIME_REPORT_DEFAULT.equals(timeReport)) {
				pDto.put("queryDate", queryDate);
			} else {
			    queryDate = BussUtil.getReportDate(timeReport);
				String tomorrowDate = WebplusUtil.plusDay(1, queryDate);
				String beginTime = queryDate + " " + timeReport;
				String endTime = tomorrowDate + " " + timeReport;
				pDto.put("beginTime", beginTime);
				pDto.put("endTime", endTime);
			}
		} else {
			pDto.put("queryDate", queryDate);
		}
		
		pDto.put("shopId", shopId);
		pDto.put("whetherRemove", WebplusCons.WHETHER_NO);
		pDto.put("whetherPay", WebplusCons.WHETHER_YES);
		List<String> orderTypeList = Lists.newArrayList();
		orderTypeList.add(BussCons.ORDER_TYPE_COMMON);
		orderTypeList.add(BussCons.ORDER_TYPE_PARENT);
		orderTypeList.add(BussCons.ORDER_TYPE_SIGNLE);
		pDto.put("orderTypeList", orderTypeList);
		pDto.setOrder(" create_time DESC ");
		List<Order> orderList = orderService.like(pDto);
		if (WebplusUtil.isNotEmpty(orderList)) {
			for (Order order : orderList) {
				Integer deskAmount = BussUtil.dealEmptyAmount(order.getDeskAmount());
				Integer menuAmount = BussUtil.dealEmptyAmount(order.getMenuAmount());
				Integer smallTotalAmount = deskAmount + menuAmount;
				order.setSmallTotalAmount(smallTotalAmount);
			}
		}
		return R.toList(orderList);

	}
    
	
	/**
	 * 简要说明：查询结算订单 编写者：陈骑元 创建时间：2019年7月23日 下午10:40:41
	 * 
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("listOrderPage")
	@ResponseBody
	public R listOrderPage(String beginDate,String endDate,String whetherPay,String currentPage,String pageSize) {
		String userId = this.getUserId();
		Dto pDto = Dtos.newPage(currentPage, pageSize);
		if(WebplusUtil.isEmpty(beginDate)) {
			beginDate=WebplusUtil.getCurrentDate();
		}
		
		if(WebplusUtil.isEmpty(endDate)) {
			endDate=WebplusUtil.getCurrentDate();
		}
		System.out.println(endDate);
		
		pDto.put("beginDate", beginDate);
		pDto.put("endDate", endDate);
		pDto.put("shopId", userId);
		pDto.put("whetherRemove", WebplusCons.WHETHER_NO);
		pDto.put("whetherPay",whetherPay);
		pDto.println();
		List<String> orderTypeList = Lists.newArrayList();
		orderTypeList.add(BussCons.ORDER_TYPE_COMMON);
		orderTypeList.add(BussCons.ORDER_TYPE_PARENT);
		orderTypeList.add(BussCons.ORDER_TYPE_SIGNLE);
		pDto.put("orderTypeList", orderTypeList);
		pDto.setOrder(" create_time DESC ");
		Page<Order> page=orderService.listPage(pDto);
		List<Order> orderList=page.getRecords();
		if (WebplusUtil.isNotEmpty(orderList)) {
			for (Order order : orderList) {
				Integer deskAmount = BussUtil.dealEmptyAmount(order.getDeskAmount());
				Integer menuAmount = BussUtil.dealEmptyAmount(order.getMenuAmount());
				Integer smallTotalAmount = deskAmount + menuAmount;
				order.setSmallTotalAmount(smallTotalAmount);
			}
		}
		page.setRecords(orderList); 
		return R.toApiPage(page);

	}

	/**
	 * 
	 * 简要说明：查询桌订单 编写者：陈骑元 创建时间：2019年7月28日 下午8:05:38
	 * 
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("queryDeskOrder")
	@ResponseBody
	public R queryDeskOrder(String deskId, String orderId,String whetherMerge,String whetherPos) {
		if(WebplusUtil.isNotEmpty(orderId)) {
			Order order = orderService.selectById(orderId);
			if(WebplusCons.WHETHER_YES.equals(whetherPos)) {
				
				return this.getBuffetOrder(order);
			}else {
				String whehterParentOrder = WebplusCons.WHETHER_NO;
				if (BussCons.ORDER_TYPE_PARENT.equals(order.getOrderType())) {
					whehterParentOrder = WebplusCons.WHETHER_YES;
				}
				List<OrderLine> orderLineList = this.getOrderLineList(orderId, whehterParentOrder);
				order = this.getOrder(order, orderLineList, WebplusCons.WHETHER_YES);
				return R.toData(order);

			}
			
		}else {
			if (WebplusUtil.isEmpty(deskId)) {

				return R.error("卓番が間違ってます");
			}
			Desk desk = deskService.selectById(deskId);
			if (WebplusUtil.isEmpty(desk)) {

				return R.error("卓番が間違ってます");
			}
			 orderId = desk.getOrderNo();
			if (WebplusUtil.isEmpty(orderId)) {

				return R.error("まだ注文されてません");
			}
			Order order = orderService.selectById(orderId);
			if (WebplusUtil.isEmpty(order)) {

				return R.error("まだ注文されてません");
			}
			if (WebplusCons.WHETHER_YES.equals(order.getWhetherPay())) {

				return R.error("お会計は完了してます、ホームボタンよりお戻りください");
			}
			if(WebplusCons.WHETHER_YES.equals(whetherPos)) {
				
				return this.getBuffetOrder(order);
			}else {
				Dto pDto = Dtos.newDto();
				pDto.put("orderId", orderId);
				pDto.setOrder(" create_time ASC ");
				List<OrderLine> orderLineList = orderLineService.list(pDto);
				order = this.getOrder(order, orderLineList, whetherMerge);
				return R.toData(order);
			}
			
		}
	
	}

	/**
	 * 简要说明：查询点的菜品 编写者：陈骑元 创建时间：2019年7月23日 下午10:40:41
	 * 
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("queryOrderLineList")
	@ResponseBody
	public R queryOrderLineList(String orderId) {
		if (WebplusUtil.isEmpty(orderId)) {

			return R.error("注文番号が無意味です");
		}
		Order order = orderService.selectById(orderId);
		if (WebplusUtil.isEmpty(order)) {

			return R.toList(Lists.newArrayList());

		}
		if (WebplusCons.WHETHER_YES.equals(order.getWhetherPay())) {
			return R.toList(Lists.newArrayList());
		}
		String whetherParentOrder = WebplusCons.WHETHER_NO;
		if (BussCons.ORDER_TYPE_PARENT.equals(order.getOrderType())) {
			whetherParentOrder = WebplusCons.WHETHER_YES;
		}
		List<OrderLine> dataList = this.getOrderLineList(orderId, whetherParentOrder);
		dataList = this.mergeMenuSpec(dataList, WebplusCons.WHETHER_YES); // 合并规格的名称
		return R.toList(dataList);

	}

	/**
	 * 简要说明：查询订单详情 编写者：陈骑元 创建时间：2019年7月23日 下午10:40:41
	 * 
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("queryOrderDetail")
	@ResponseBody
	public R queryOrderDetail(String orderId,String whetherPos) {
		if (WebplusUtil.isEmpty(orderId)) {

			return R.error("注文番号が無意味です");
		}
		
		Order order = orderService.selectById(orderId);
		if(WebplusCons.WHETHER_YES.equals(whetherPos)) {
			
			return this.getBuffetOrder(order);
		}else {
			String whehterParentOrder = WebplusCons.WHETHER_NO;
			if (BussCons.ORDER_TYPE_PARENT.equals(order.getOrderType())) {
				whehterParentOrder = WebplusCons.WHETHER_YES;
			}
			List<OrderLine> orderLineList = this.getOrderLineList(orderId, whehterParentOrder);
			order = this.getOrder(order, orderLineList, WebplusCons.WHETHER_YES);
			return R.toData(order);

		}
		
	}

	/**
	 * 
	 * 简要说明：获取订单 编写者：陈骑元 创建时间：2019年10月19日 下午4:30:14
	 * 
	 * @param 说明
	 * @return 说明
	 */
	private Order getOrder(Order order, List<OrderLine> orderLineList, String whetherMerge) {
		orderLineList = this.removeWasteData(orderLineList);
		List<OrderLine> orderLineListNew = Lists.newArrayList();
		int totalAmount = 0;
		int menuAmount = 0;
		int outAmountBefore = 0; // 外卖金额税前
		int outAmountAfter = 0; // 外卖金额税后
		int eatAmountBefore = 0; // 堂食金额税前
		int eatAmountAfter = 0; // 堂食金额税后
		int historyOutAmountBefore = 0; // 外卖金额税前
		int historyOutAmountAfter = 0; // 外卖金额税后
		int historyEatAmountBefore = 0; // 堂食金额税前
		int historyEatAmountAfter = 0; // 堂食金额税后
		for (OrderLine orderLine : orderLineList) {
			String menuIndexId = orderLine.getMenuIndexId();
			String whetherBuffet = orderLine.getWhetherBuffet();
			if (WebplusCons.WHETHER_NO.equals(whetherBuffet) || WebplusUtil.isEmpty(menuIndexId)) {
				Integer buyNum = BussUtil.dealEmptyAmount(orderLine.getBuyNum());
				Integer menuPrice = BussUtil.dealEmptyAmount(orderLine.getMenuPrice());
				Integer menuSumPrice = menuPrice * buyNum;
				Integer subRate = BussUtil.dealEmptyAmount(orderLine.getSubRate());
				Integer subAmount = BussUtil.dealEmptyAmount(orderLine.getSubAmount());
				String taxType = orderLine.getTaxType();
				if (WebplusCons.WHETHER_YES.equals(orderLine.getWhetherTakeOut())) { // 外卖金额
					if (BussCons.TAX_TYPE_BEFORE.equals(taxType)) {
						outAmountBefore += BussUtil.countSubRate(menuSumPrice, subAmount, subRate);
						historyOutAmountBefore +=menuSumPrice;

					} else {
						historyOutAmountAfter+=menuSumPrice;
						outAmountAfter += BussUtil.countSubRate(menuSumPrice, subAmount, subRate);
					}

				} else {
					if (BussCons.TAX_TYPE_BEFORE.equals(taxType)) {
						historyEatAmountBefore+=menuSumPrice;
						eatAmountBefore += BussUtil.countSubRate(menuSumPrice, subAmount, subRate);
					} else {
						historyEatAmountAfter+=menuSumPrice;
						eatAmountAfter += BussUtil.countSubRate(menuSumPrice, subAmount, subRate);
					}

				}
				orderLine.setMenuSumPrice(menuSumPrice);
				orderLineListNew.add(orderLine);
			}

		}
		Shop shop = shopService.selectById(order.getShopId());
		Integer tableAmount = BussUtil.dealEmptyAmount(shop.getTableAmount());
		// Integer taxes=BussUtil.dealEmptyAmount(shop.getTaxes());
		Integer eatNum = BussUtil.dealEmptyAmount(order.getEatNum());
		menuAmount = eatAmountBefore + eatAmountAfter + outAmountBefore + outAmountAfter; // 菜单总金额
		int deskAmount = eatNum * tableAmount;
		int taxAmountBefore = BussUtil.countTaxes(0, eatAmountBefore, BussCons.EAT_TAXES);
		int outTaxAmountBefore = BussUtil.countTaxes(0, outAmountBefore, BussCons.OUT_TAXES);
		int historyTaxAmountBefore = BussUtil.countTaxes(0, historyEatAmountBefore, BussCons.EAT_TAXES);
		int historyOutTaxAmountBefore = BussUtil.countTaxes(0, historyOutAmountBefore, BussCons.OUT_TAXES);
		totalAmount = deskAmount + menuAmount + taxAmountBefore + outTaxAmountBefore;
		int historyTotalAmount=historyTaxAmountBefore+historyOutTaxAmountBefore+deskAmount+historyEatAmountBefore +historyEatAmountAfter + historyOutAmountBefore + historyOutAmountAfter;
		int taxAmountAfter = BussUtil.countAfterTaxes(deskAmount,eatAmountAfter, BussCons.EAT_TAXES);
		int outTaxAmountAfter = BussUtil.countAfterTaxes(outAmountAfter, BussCons.OUT_TAXES);
		int outTaxAmount = outTaxAmountBefore + outTaxAmountAfter;
		int taxAmount = taxAmountBefore + taxAmountAfter;
		int subRate = BussUtil.dealEmptyAmount(order.getSubRate());
		int subAmount = BussUtil.dealEmptyAmount(order.getSubAmount());
		order.setHistoryTotalAmount(historyTotalAmount);
		totalAmount = BussUtil.countSubRate(totalAmount, subAmount, subRate);
		int subTax=0;
		if(subRate>0||subAmount>0) {
			int disAmount=historyTotalAmount-totalAmount;
			if(disAmount>0)	{
				subTax=BussUtil.countAfterTaxes(disAmount, BussCons.EAT_TAXES);
			}
		}
		int consumeTax=outTaxAmount +taxAmount-subTax;
		order.setTaxAfterAmount(eatAmountAfter + outAmountAfter);
		order.setTaxBeforeAmount(eatAmountBefore + outAmountBefore);
		order.setDeskAmount(deskAmount);
		order.setMenuAmount(menuAmount);
		order.setOutTaxAmount(outTaxAmount);
		order.setSmallTotalAmount(menuAmount + deskAmount);
		order.setTaxAmount(taxAmount);
		order.setConsumeTax(consumeTax);
		if (WebplusUtil.isNotEmpty(order.getTotalAmount())&&WebplusUtil.isNotEmpty(order.getOrderId())) {
			if (order.getTotalAmount().intValue() != totalAmount) {
				order.setTotalAmount(totalAmount);
				orderService.updateById(order);
			}
		} else {
			order.setTotalAmount(totalAmount);
		}

		if (WebplusUtil.isEmpty(order.getPayTime())) {
			order.setPayTime(WebplusUtil.getDateTime());
		}
		List<OrderLine> dataList = this.getOrderLineList(orderLineListNew, WebplusCons.WHETHER_YES);
		if (WebplusCons.WHETHER_YES.equals(whetherMerge)) {
			dataList = this.getMergeOrderLine(dataList);
		}
		order.setDataList(dataList);
		return order;
	}

	/***
	 * 
	 * 简要说明：合并相同菜单行 编写者：陈骑元 创建时间：2019年10月20日 下午10:34:37
	 * 
	 * @param 说明
	 * @return 说明
	 */
	private List<OrderLine> getMergeOrderLine(List<OrderLine> orderLineList) {
		List<OrderLine> orderLineListNew = Lists.newArrayList();
		Map<String, OrderLine> lineMap = new LinkedHashMap<String, OrderLine>();
		if (WebplusUtil.isNotEmpty(orderLineList)) {
			for (int i = 0; i < orderLineList.size(); i++) {
				OrderLine orderLine = orderLineList.get(i);
				String key = "";
				if (WebplusUtil.isEmpty(orderLine.getMenuIndexId())) {
					String specIndexId=orderLine.getSpecIndexId();
					if(WebplusUtil.isNotEmpty(specIndexId)){
						key = orderLine.getCatalogIndexId()+specIndexId;
					}else {
						key = orderLine.getCatalogIndexId();
					}
					
				} else {
					List<OrderLine> specList=orderLine.getSpecList();
					if(WebplusUtil.isNotEmpty(specList)) {
						key = orderLine.getMenuIndexId()+this.getSpecIndexId(specList);
					}else {
						key = orderLine.getMenuIndexId();
					}
					
				}
				if (lineMap.containsKey(key)) {
					OrderLine orderLineEntity = lineMap.get(key);
					int buyNum = orderLine.getBuyNum() + orderLineEntity.getBuyNum();
					int menuSumPrice = orderLine.getMenuSumPrice() + orderLineEntity.getMenuSumPrice();
					orderLineEntity.setBuyNum(buyNum);
					orderLineEntity.setMenuSumPrice(menuSumPrice);
					lineMap.put(key, orderLineEntity);
				} else {
					lineMap.put(key, orderLine);
				}
			}
		}
		for (String key : lineMap.keySet()) {
			orderLineListNew.add(lineMap.get(key));
		}

		return orderLineListNew;
	}
	/**
	 * 
	 * 简要说明：获取所有specIdenxIdz在一起
	 * 编写者：陈骑元（chenqiyuan@toonan.com）
	 * 创建时间： 2020年9月24日 下午10:15:20 
	 * @param 说明
	 * @return 说明
	 */
	private String getSpecIndexId(List<OrderLine> specList) {
		String specIndexId="";
		for(OrderLine orderLine:specList) {
			specIndexId+=orderLine.getSpecIndexId();
		}
		return specIndexId;
	}
	/**
	 * 
	 * 简要说明：从数据库获取下单的信息
	 * 编写者：陈骑元（chenqiyuan@toonan.com）
	 * 创建时间： 2020年7月19日 上午11:24:42 
	 * @param 说明
	 * @return 说明
	 */
    private List<OrderLine> getOrderLineListByDB(String orderId, String whetherParentOrder){
    	Dto pDto = Dtos.newDto();
		if (WebplusCons.WHETHER_YES.equals(whetherParentOrder)) {
			List<String> orderIdList = orderCommonService.getParentOrderId(orderId);
			pDto.put("orderIdList", orderIdList);
			// pDto.put("parentOrderNo", orderId);
		} else {
			pDto.put("orderId", orderId);
		}

		pDto.setOrder(" create_time ASC ");
		List<OrderLine> orderLineList = orderLineService.list(pDto);
		return  orderLineList ;
    }
	/**
	 * 
	 * 简要说明：获取订单项目 编写者：陈骑元 创建时间：2019年10月19日 下午4:18:45
	 * 
	 * @param 说明
	 * @return 说明
	 */
	private List<OrderLine> getOrderLineList(String orderId, String whetherParentOrder) {
		List<OrderLine> orderLineList=this.getOrderLineListByDB(orderId, whetherParentOrder);
	

		return this.getOrderLineList(orderLineList);

	}
    /**
     * 
     * 简要说明：获取非自助餐的菜品
     * 编写者：陈骑元（chenqiyuan@toonan.com）
     * 创建时间： 2020年7月19日 上午11:30:39 
     * @param 说明
     * @return 说明
     */
	private List<OrderLine> getOrderLineList(List<OrderLine> orderLineList){
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
		return  orderLineListNew;
	}
	/**
	 * 
	 * 简要说明：获取组合订单组合 编写者：陈骑元 创建时间：2019年9月1日 下午9:13:11
	 * 
	 * @param 说明
	 * @return 说明
	 */
	private List<OrderLine> getOrderLineList(List<OrderLine> orderLineList, String whetherCountSpec) {
		List<OrderLine> orderLineListNew = Lists.newArrayList();
		for (OrderLine orderLine : orderLineList) {
			String whetherSpec = orderLine.getWhetherSpec();
			String whetherBuffet = orderLine.getWhetherBuffet();
			String menuIndexId = orderLine.getMenuIndexId();
			if (WebplusCons.WHETHER_NO.equals(whetherSpec)
					|| (WebplusCons.WHETHER_YES.equals(whetherBuffet) && WebplusUtil.isEmpty(menuIndexId))) {
				if (WebplusUtil.isNotEmpty(menuIndexId)) {
					String lineId = orderLine.getLineId();
					if (WebplusUtil.isNotEmpty(lineId)) {
						List<OrderLine> specList = this.getStepSpec(lineId, orderLineList);
						if (WebplusUtil.isNotEmpty(specList)) {
							if (WebplusCons.WHETHER_YES.equals(whetherCountSpec)) {
								int singleMenuPriceTotal = this.getSpecMenuPrice(orderLine.getMenuSumPrice(), specList);
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
	 * 简要说明：获取菜品规格的 编写者：陈骑元 创建时间：2019年9月1日 下午9:13:11
	 * 
	 * @param 说明
	 * @return 说明
	 */
	private List<OrderLine> mergeMenuSpec(List<OrderLine> orderLineList, String whetherCountSpec) {
		List<OrderLine> orderLineListNew = Lists.newArrayList();
		for (OrderLine orderLine : orderLineList) {
			String whetherSpec = orderLine.getWhetherSpec();
			String whetherBuffet = orderLine.getWhetherBuffet();
			String menuIndexId = orderLine.getMenuIndexId();
			if (WebplusCons.WHETHER_NO.equals(whetherSpec)
					|| (WebplusCons.WHETHER_YES.equals(whetherBuffet) && WebplusUtil.isEmpty(menuIndexId))) {

				if (WebplusUtil.isNotEmpty(menuIndexId)) {
					String lineId = orderLine.getLineId();
					if (WebplusUtil.isNotEmpty(lineId)) {
						List<OrderLine> specList = this.getStepSpec(lineId, orderLineList);
						if (WebplusUtil.isNotEmpty(specList)) {
							if (WebplusCons.WHETHER_YES.equals(whetherCountSpec)) {
								int singleMenuPriceTotal = this.getSpecMenuPrice(orderLine.getMenuSumPrice(), specList);
								orderLine.setMenuSumPrice(singleMenuPriceTotal);
							}
							String specName = this.getMenuSpecName(specList);
							if (WebplusUtil.isNotEmpty(specName)) {
								String menuName = orderLine.getMenuName() + "(" + specName + ")";
								orderLine.setMenuName(menuName);
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
	 * 简要说明：合并规格的名字 编写者：陈骑元 创建时间：2019年12月14日 下午9:48:34
	 * 
	 * @param 说明
	 * @return 说明
	 */
	private String getMenuSpecName(List<OrderLine> specList) {
		String specName = "";
		if (WebplusUtil.isNotEmpty(specList)) {
			for (int i = 0; i < specList.size(); i++) {
				OrderLine specLine = specList.get(i);
				if (i == 0) {
					specName = specLine.getMenuName();
				} else {
					specName += "+" + specLine.getMenuName();
				}
			}
		}
		return specName;
	}

	/**
	 * 
	 * 简要说明：获取菜品规格 编写者：陈骑元 创建时间：2019年9月1日 下午9:21:17
	 * 
	 * @param 说明
	 * @return 说明
	 */
	private List<OrderLine> getStepSpec(String lineId, List<OrderLine> orderLineList) {
		List<OrderLine> specList = Lists.newArrayList();
		for (OrderLine orderLine : orderLineList) {
			String whetherSpec = orderLine.getWhetherSpec();
			if (WebplusCons.WHETHER_YES.equals(whetherSpec)) {
				String parentId = orderLine.getParentId();
				if (lineId.equals(parentId)) {
					specList.add(orderLine);
				}
			}
		}
		return specList;
	}

	/**
	 * 
	 * 简要说明：获取菜品规格 编写者：陈骑元 创建时间：2019年9月1日 下午9:21:17
	 * 
	 * @param 说明
	 * @return 说明
	 */
	private Integer getSpecMenuPrice(int menuPrice, List<OrderLine> specList) {
		int singleMenuPrice = menuPrice;
		for (OrderLine specLine : specList) {
			Integer singleSumPrice = specLine.getMenuSumPrice();
			if (WebplusUtil.isNotEmpty(singleSumPrice)) {
				singleMenuPrice += singleSumPrice;
			}

		}
		return singleMenuPrice;
	}

	/**
	 * 简要说明：修改订单中菜单 编写者：陈骑元 创建时间：2019年7月23日 下午10:40:41
	 * 
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("updateOrder")
	@ResponseBody
	public R updateOrder(String orderLines, String removeLineIds, String orderId, Integer eatNum) {
		if (WebplusUtil.isNotAnyEmpty(orderId, eatNum)) {
			Order order = new Order();
			order.setEatNum(eatNum);
			order.setOrderId(orderId);
			order.setUpdateTime(WebplusUtil.getDateTime());
			orderService.updateById(order);
		}
		if (WebplusUtil.isNotEmpty(orderLines)) {
			List<Dto> dataList = WebplusJson.fromJson(orderLines);
			List<OrderLine> orderLineList = Lists.newArrayList();
			List<String> lineIdList = getOrderLineId(dataList);
			if (WebplusUtil.isNotAnyEmpty(lineIdList)) {
				List<OrderLine> lineList = orderLineService.selectBatchIds(lineIdList);
				for (Dto dataDto : dataList) {
					String lineId = dataDto.getString("lineId");
					Integer buyNum = dataDto.getInteger("buyNum");
					if (WebplusUtil.isNotEmpty(lineId)) {
						OrderLine orderLineDb = this.checkBuffetLine(lineId, lineList);
						if (WebplusUtil.isNotEmpty(orderLineDb)) { // 是自助餐
							int buyNumDb = orderLineDb.getBuyNum();
							int chooseNumDb = orderLineDb.getChooseNum();
							int maxNum = chooseNumDb / buyNumDb;
							int chooseNum = buyNum * maxNum;
							OrderLine orderLine = new OrderLine();
							orderLine.setLineId(lineId);
							orderLine.setBuyNum(buyNum);
							orderLine.setChooseNum(chooseNum);
							orderLine.setUpdateTime(WebplusUtil.getDateTime());
							orderLineList.add(orderLine);
						} else {
							OrderLine orderLine = new OrderLine();
							orderLine.setLineId(lineId);
							orderLine.setBuyNum(buyNum);
							orderLine.setUpdateTime(WebplusUtil.getDateTime());
							orderLineList.add(orderLine);
						}

					}

				}

				if (WebplusUtil.isNotEmpty(orderLineList)) {
					orderLineService.updateBatchById(orderLineList);
				}
				if (WebplusUtil.isNotEmpty(orderLineList)) { // 同时更新规格的数量
					for (OrderLine orderLine : orderLineList) {
						if (WebplusUtil.isNotEmpty(orderLine.getLineId())) {
							OrderLine entity = new OrderLine();
							entity.setBuyNum(orderLine.getBuyNum());
							entity.setUpdateTime(WebplusUtil.getDateTime());
							EntityWrapper<OrderLine> wrapper = new EntityWrapper<OrderLine>();
							wrapper.eq("parent_id", orderLine.getLineId());
							orderLineService.update(entity, wrapper);
						}

					}
				}
			}

			if (WebplusUtil.isNotEmpty(removeLineIds)) {
				List<String> lineIdListRemove = WebplusFormater.separatStringToList(removeLineIds);
				orderLineService.deleteBatchIds(lineIdListRemove);
				for (String lineId : lineIdListRemove) {
					Dto pDto = Dtos.newDto();
					pDto.put("parent_id", lineId);
					orderLineService.deleteByMap(pDto); // 同时删除规格
				}
			}

		}

		return R.ok();

	}

	/**
	 * 
	 * 简要说明：检查是否自助餐，并返回自助餐列表 编写者：陈骑元 创建时间：2019年12月30日 下午10:18:39
	 * 
	 * @param 说明
	 * @return 说明
	 */
	private OrderLine checkBuffetLine(String lineId, List<OrderLine> orderLineList) {
		OrderLine orderLine = this.getOrderLine(lineId, orderLineList);
		if (WebplusUtil.isNotEmpty(orderLine)) {
			if (WebplusUtil.isNotEmpty(orderLine.getCatalogIndexId()) && WebplusUtil.isEmpty(orderLine.getMenuIndexId())
					&& WebplusCons.WHETHER_YES.equals(orderLine.getWhetherBuffet())) {

				return orderLine;
			}
		}
		return null;
	}

	/**
	 * 
	 * 简要说明：获取对应的点餐 编写者：陈骑元 创建时间：2019年12月30日 下午10:19:41
	 * 
	 * @param 说明
	 * @return 说明
	 */
	private OrderLine getOrderLine(String lineId, List<OrderLine> orderLineList) {
		if (WebplusUtil.isNotEmpty(orderLineList)) {
			for (OrderLine orderLine : orderLineList) {
				if (orderLine.getLineId().equals(lineId)) {

					return orderLine;
				}
			}
		}
		return null;
	}

	/**
	 * 
	 * 简要说明：获取订单的类表编号 编写者：陈骑元 创建时间：2019年12月30日 下午10:12:10
	 * 
	 * @param 说明
	 * @return 说明
	 */
	private List<String> getOrderLineId(List<Dto> dataList) {
		List<String> lineIdList = Lists.newArrayList();
		if (WebplusUtil.isNotEmpty(dataList)) {
			for (Dto dataDto : dataList) {
				String lineId = dataDto.getString("lineId");
				lineIdList.add(lineId);
			}
		}
		return lineIdList;
	}

	/**
	 * 
	 * 简要说明：结算订单 
	 * 编写者：陈骑元
	 * 创建时间：2019年7月24日 上午8:40:43
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("tallyOrder")
	@ResponseBody
	public R tallyOrder(String orderId, Integer cashAmount, Integer creditAmount, 
			Integer couponsAmount,Integer otherAmount,String payWay) {
		if (WebplusUtil.isEmpty(orderId)) {

			return R.error("注文番号が無意味です");
		}
		if (WebplusUtil.isEmpty(creditAmount)) {
			creditAmount = 0;
		}
		if (WebplusUtil.isEmpty(couponsAmount)) {
			couponsAmount = 0;
		}
		if (WebplusUtil.isEmpty(otherAmount)) {
			otherAmount = 0;
		}
		if(BussCons.PAY_WAY_CARD.equals(payWay)&&creditAmount==0) {
			creditAmount=cashAmount;
			cashAmount=0;
		}else if(BussCons.PAY_WAY_QT.equals(payWay)&&otherAmount ==0) {
			otherAmount=cashAmount;
			cashAmount=0;
		}
		String key = BussCons.PARENT_ORDER_KEY + orderId;
		if (WebplusCache.exists(key)) {
			String parentOrderId = WebplusCache.getString(key);
			String singleOrderJson = WebplusCache.getString(BussCons.ORDER_INFO_KEY + orderId);
			String orderLines = WebplusCache.getString(BussCons.ORDER_LINE_KEY + orderId);
			Order singleOrder = WebplusJson.fromJson(singleOrderJson, Order.class);

			Order order = orderService.selectById(parentOrderId);
			Integer eatNumRemain = order.getEatNum() - singleOrder.getEatNum();
			order.setEatNum(eatNumRemain);
			if (eatNumRemain < 0) {
				eatNumRemain = 0;
			}
			order.setEatNum(eatNumRemain);
			List<Dto> lineDtoList = WebplusJson.fromJson(orderLines);
			Dto pDto = Dtos.newDto("orderId", parentOrderId);
			List<OrderLine> orderLineList = orderLineService.list(pDto);
			List<OrderLine> singleLineList = Lists.newArrayList();
			List<OrderLine> updateLineList = Lists.newArrayList();
			List<String> deleteLineIdList = Lists.newArrayList();
			for (Dto lineDto : lineDtoList) {
				String lineId = lineDto.getString("lineId");
				Integer buyNum = lineDto.getInteger("buyNum");
				OrderLine orderLine = this.getOrderLine(orderLineList, lineId);
				if (WebplusUtil.isNotEmpty(orderLine) && buyNum > 0) {
					OrderLine singleLine = new OrderLine();
					WebplusUtil.copyProperties(orderLine, singleLine);
					Integer buyNumSource = orderLine.getBuyNum();
					List<OrderLine> specLineList = this.getSpecLine(orderLineList, lineId);
					String whetherUpdate = WebplusCons.WHETHER_NO;
					String lineIdNew = WebplusUtil.uuid();
					singleLine.setLineId(lineIdNew);
					singleLine.setOrderId(orderId);
					if (buyNum >= buyNumSource) {
						buyNum = buyNumSource;
						deleteLineIdList.add(lineId);
					} else {
						int remainBuyNum = buyNumSource - buyNum;
						orderLine.setBuyNum(remainBuyNum);
						updateLineList.add(orderLine);
						whetherUpdate = WebplusCons.WHETHER_YES;
					}
					singleLine.setBuyNum(buyNum);
					singleLineList.add(singleLine);
					for (OrderLine specLine : specLineList) {
						OrderLine singleSpecLine = new OrderLine();
						WebplusUtil.copyProperties(specLine, singleSpecLine);
						singleSpecLine.setOrderId(orderId);
						singleSpecLine.setParentId(lineIdNew);
						singleSpecLine.setBuyNum(buyNum);
						singleSpecLine.setLineId(WebplusUtil.uuid());
						singleLineList.add(singleSpecLine);
						if (WebplusCons.WHETHER_YES.equals(whetherUpdate)) { // 更新
							int remainBuyNum = specLine.getBuyNum() - buyNum;
							specLine.setBuyNum(remainBuyNum);
							updateLineList.add(specLine);
						} else {
							deleteLineIdList.add(specLine.getLineId());
						}
					}
				}

			}
			if (WebplusUtil.isNotEmpty(singleLineList)) {
				orderLineService.insertBatch(singleLineList);

				if (WebplusUtil.isNotEmpty(deleteLineIdList)) {
					orderLineService.deleteBatchIds(deleteLineIdList);
				}
				List<OrderLine> orderLineListNew = this.getRemainOrderLine(orderLineList, updateLineList,
						deleteLineIdList);

				if (WebplusUtil.isNotEmpty(updateLineList)) {
					orderLineService.updateBatchById(updateLineList);

				}
				if (WebplusUtil.isNotEmpty(orderLineListNew)) {
					order.setEatNum(eatNumRemain);
					order.setDeskNum(eatNumRemain);
					order = this.getOrder(order, orderLineListNew, WebplusCons.WHETHER_NO);
				} else {
					orderService.deleteById(orderId);
					this.updateDesk(order);
				}

			}
			int zeroAmount = cashAmount + creditAmount + couponsAmount + otherAmount - singleOrder.getTotalAmount();
			singleOrder.setPayWay(payWay);
			singleOrder.setCashAmount(cashAmount);
			singleOrder.setCreditAmount(creditAmount);
			singleOrder.setCouponsAmount(couponsAmount);
			singleOrder.setZeroAmount(zeroAmount);
			singleOrder.setOtherAmount(otherAmount);
			singleOrder.setUpdateTime(WebplusUtil.getDateTime());
			singleOrder.setWhetherPay(WebplusCons.WHETHER_YES);
			singleOrder.setPayTime(WebplusUtil.getDateTime());
			orderService.insert(singleOrder);
			WebplusCache.delString(key);
			WebplusCache.delString(BussCons.ORDER_LINE_KEY + orderId);
			WebplusCache.delString(BussCons.ORDER_INFO_KEY + orderId);
			return R.toData(singleOrder);
		} else {
			Order order = orderService.selectById(orderId);
			if (WebplusUtil.isEmpty(order)) {
				return R.error("注文番号が間違ってます");
			}

			if (WebplusCons.WHETHER_YES.equals(order.getWhetherPay())) {
				return R.error("お会計は完了してます、ホームボタンよりお戻りください");
			}

			String whehterParentOrder = WebplusCons.WHETHER_NO;
			if (BussCons.ORDER_TYPE_PARENT.equals(order.getOrderType())) {
				whehterParentOrder = WebplusCons.WHETHER_YES;
			}
			List<OrderLine> orderLineList = this.getOrderLineList(orderId, whehterParentOrder);
			order = this.getOrder(order, orderLineList, WebplusCons.WHETHER_YES);
		
			int zeroAmount = cashAmount + creditAmount + couponsAmount + otherAmount - order.getTotalAmount();
			order.setPayWay(payWay);
			order.setCashAmount(cashAmount);
			order.setCreditAmount(creditAmount);
			order.setCouponsAmount(couponsAmount);
			order.setZeroAmount(zeroAmount);
			order.setOtherAmount(otherAmount);
			order.setUpdateTime(WebplusUtil.getDateTime());
			order.setWhetherPay(WebplusCons.WHETHER_YES);
			order.setPayTime(WebplusUtil.getDateTime());
			boolean result = orderService.updateById(order);
			if (result) {
				this.updateDesk(order);
				return R.toData(order);
			} else {

				return R.error();
			}

		}
		
	}

	/**
	 * 
	 * 简要说明：更新桌位信息 编写者：陈骑元 创建时间：2019年10月19日 下午7:09:24
	 * 
	 * @param 说明
	 * @return 说明
	 */
	private void updateDesk(Order order) {
		if (BussCons.ORDER_TYPE_PARENT.equals(order.getOrderType())) {
			Dto pDto = Dtos.newDto("parentOrderNo", order.getOrderId());
			List<Order> orderList = orderService.list(pDto);
			for (Order childOrder : orderList) {
				String deskId = childOrder.getDeskId();
				if (WebplusUtil.isNotEmpty(deskId)) {
					Desk desk = new Desk();
					desk.setDeskId(deskId);
					desk.setDeskStatus(BussCons.DESK_STATUS_EMPTY); // 解除占座
					desk.setEatNum(0);
					desk.setOrderNo("");
					desk.setOrderTime(null);
					desk.setUpdateTime(WebplusUtil.getDateTime());
					deskService.updateById(desk);
				}
				childOrder.setUpdateTime(WebplusUtil.getDateTime());
				childOrder.setUpdateBy(order.getShopId());
				childOrder.setOrderType(BussCons.ORDER_TYPE_CHILD);
				childOrder.setWhetherPay(WebplusCons.WHETHER_YES);
				childOrder.setPayTime(WebplusUtil.getDateTime());
			    orderService.updateById(childOrder);
			}
		} else {
			if(!BussCons.ORDER_TYPE_SIGNLE.equals(order.getOrderType())) {
				Desk desk = new Desk();
				desk.setDeskId(order.getDeskId());
				desk.setDeskStatus(BussCons.DESK_STATUS_EMPTY); // 解除占座
				desk.setEatNum(0);
				desk.setOrderNo("");
				desk.setOrderTime(null);
				desk.setUpdateTime(WebplusUtil.getDateTime());
				deskService.updateById(desk);
			}
			
		}

	}

	/**
	 * 简要说明：买单 编写者：陈骑元 创建时间：2019年7月23日 下午10:40:41
	 * 
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("saleOrder")
	@ResponseBody
	public R saleOrder(String orderId, Integer cashAmount) {
		if (WebplusUtil.isEmpty(orderId)) {

			return R.error("注文番号が無意味です");
		}
		Order order = orderService.selectById(orderId);
		if (WebplusUtil.isEmpty(order)) {
			return R.error("注文番号が間違ってます");
		}

		order.setCashAmount(cashAmount);
		order.setUpdateTime(WebplusUtil.getDateTime());
		order.setWhetherPay(WebplusCons.WHETHER_YES);
		order.setPayTime(WebplusUtil.getDateTime());
		boolean result = orderService.updateById(order);
		if (result) {
			this.updateDesk(order);
			return R.toData(order);
		} else {

			return R.error();
		}
	}

	/**
	 * 简要说明：删除订单中菜单 编写者：陈骑元 创建时间：2019年7月23日 下午10:40:41
	 * 
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("removeOrderLine")
	@ResponseBody
	public R removeOrderLine(String lineId) {
		if (WebplusUtil.isEmpty(lineId)) {

			return R.error("主键编号不能为空");
		}
		boolean result = orderLineService.deleteById(lineId);
		if (result) {
			Dto pDto = Dtos.newDto();
			pDto.put("parent_id", lineId);
			orderLineService.deleteByMap(pDto); // 同时删除规格
			return R.ok();
		} else {

			return R.error();
		}

	}

	/**
	 * 简要说明：删除订单 编写者：陈骑元 创建时间：2019年7月23日 下午10:40:41
	 * 
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("removeOrder")
	@ResponseBody
	public R remove(String orderId) {
		if (WebplusUtil.isEmpty(orderId)) {

			return R.error("注文番号が無意味です");
		}
		Order order = orderService.selectById(orderId);
		order.setOrderId(orderId);
		order.setWhetherRemove(WebplusCons.WHETHER_YES);
		order.setRemoveTime(WebplusUtil.getDateTime());
		boolean result = orderService.updateById(order);
		if (result) {

			// 没结算的要清除占座
			if (WebplusCons.WHETHER_NO.equals(order.getWhetherPay())) {//
				Desk desk = new Desk();
				desk.setDeskId(order.getDeskId());
				desk.setDeskStatus(BussCons.DESK_STATUS_EMPTY); // 解除占座
				desk.setEatNum(0);
				desk.setOrderNo("");
				desk.setUpdateTime(WebplusUtil.getDateTime());
				deskService.updateById(desk);
			}

			return R.ok();
		} else {

			return R.error();
		}

	}

	/**
	 * 
	 * 简要说明：修改信息 编写者：陈骑元 创建时间：2019-07-20
	 * 
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("updateMenuStatus")
	@ResponseBody
	public R updateMenuStatus(String menuId, String menuStatus) {
		MenuDict entity = new MenuDict();
		entity.setMenuStatus(menuStatus);
		EntityWrapper<MenuDict> wrapper = new EntityWrapper<MenuDict>();
		wrapper.eq("menu_index_id", menuId);
		boolean result = menuDictService.update(entity, wrapper);
		if (result) {
			return R.ok();
		} else {
			return R.error();
		}

	}

	/**
	 * 
	 * 简要说明：保存订单 编写者：陈骑元（chenqiyuan@toonan.com） 创建时间： 2020年6月30日 上午8:03:56
	 * 
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("saveOrder")
	@ResponseBody
	public R saveOrder(String deskId, String orderLines) {

		return orderCommonService.saveOrder(deskId, orderLines);
	}

	/**
	 * 简要说明：合并订单 编写者：陈骑元 创建时间：2019年10月19日 下午4:26:29
	 * 
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("saveMergeOrder")
	@ResponseBody
	public R saveMergeOrder(String deskIds) {
		if (WebplusUtil.isEmpty(deskIds)) {

			return R.error("卓番が間違ってます");
		}
		String shopId = this.getUserId();
		List<String> deskIdList = WebplusFormater.separatStringToList(deskIds);
		List<Order> childOrderList = Lists.newArrayList();
		List<String> subOrderIdList=Lists.newArrayList();
		for (String deskId : deskIdList) {
			Desk desk = deskService.selectById(deskId);
			if (WebplusUtil.isNotEmpty(desk)) {
				String orderNo = desk.getOrderNo();
				if (WebplusUtil.isNotEmpty(orderNo)) {
					subOrderIdList.add(orderNo);
					Order order = orderService.selectById(orderNo);
					if (WebplusUtil.isNotEmpty(order)) {
						if (WebplusCons.WHETHER_NO.equals(order.getWhetherPay())) { // 未支付
							childOrderList.add(order);
						}
					}
				}
			}
		}
		//如果已经合并把原来的订单查询出来
        if(WebplusUtil.isNotEmpty(subOrderIdList)) {  //获取一个桌位订单查看是否有合并的母单
        	
        	EntityWrapper<OrderLink> wrapper=new EntityWrapper<OrderLink>();
        	wrapper.in("child_order_no", subOrderIdList);
        	OrderLink orderLink=orderLinkService.selectOne(wrapper);
        	if(WebplusUtil.isNotEmpty(orderLink)) {
        		String parentOrderNo=orderLink.getParentOrderNo();
        		if(WebplusUtil.isNotEmpty(parentOrderNo)) {
        			orderService.deleteById(parentOrderNo);  //删除订单
        			EntityWrapper<OrderLink> deleteWrapper=new EntityWrapper<OrderLink>();
        			deleteWrapper.eq("parent_order_no", parentOrderNo);
        			orderLinkService.delete(deleteWrapper); // 删除原来的信息
        		}
        	}
        }
		if (WebplusUtil.isNotEmpty(childOrderList)) {
			Date currentTime = WebplusUtil.getDateTime();
			List<String> orderIdList = Lists.newArrayList();
			List<OrderLink> orderLinkList = Lists.newArrayList();
			int eatNum = 0;
			String orderNo = WebplusCache.createOrderNum();
			String deskName = "";
			for (Order order : childOrderList) {
				String childOrderNo = order.getOrderId();
				deskName = deskName + order.getDeskName() + ",";
				orderIdList.add(childOrderNo);
				eatNum += order.getEatNum();
				OrderLink orderLink = new OrderLink();
				orderLink.setParentOrderNo(orderNo);
				orderLink.setChildOrderNo(childOrderNo);
				orderLink.setCreateBy(shopId);
				orderLink.setCraeteTime(currentTime);
				orderLinkList.add(orderLink);
				order.setUpdateTime(currentTime);
				order.setUpdateBy(shopId);
				order.setOrderType(BussCons.ORDER_TYPE_CHILD);
				order.setWhetherPay(WebplusCons.WHETHER_YES);
				order.setPayTime(WebplusUtil.getDateTime());
			}
			if (WebplusUtil.isNotEmpty(deskName)) {
				deskName = deskName.substring(0, deskName.length() - 1);
			}
			Order order = new Order();
			order.setOrderId(orderNo);
			order.setEatNum(eatNum);
			order.setDeskNum(eatNum);
			order.setShopId(shopId);
			order.setDeskName(deskName);
			order.setOrderTime(currentTime);
			order.setCreateTime(currentTime);
			order.setCreateBy(shopId);
			order.setOrderType(BussCons.ORDER_TYPE_PARENT);
			Dto pDto = Dtos.newDto();
			pDto.put("orderIdList", orderIdList);
			pDto.setOrder(" create_time ASC ");
			List<OrderLine> orderLineList = orderLineService.list(pDto);
			order = this.getOrder(order, orderLineList, WebplusCons.WHETHER_YES);
			boolean result = orderService.saveMergeOrder(order,childOrderList, orderLinkList);
			if (result) {

				return R.toData(order);
			}
		}

		return R.error();
	}

	/**
	 * 
	 * 简要说明：单个买单
	 *  编写者：陈骑元（chenqiyuan@toonan.com）
	 *  创建时间： 2020年7月5日 上午3:08:00
	 * 
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("saveSingleOrder")
	@ResponseBody
	public R saveSingleOrder(String orderId, Integer eatNum, String orderLines) {
		if (WebplusUtil.isEmpty(orderId)) {

			return R.error("注文番号が無意味です");
		}

		if (WebplusUtil.isEmpty(orderLines)) {

			return R.error("まだ注文されてません,orderLines is empty");
		}
		if (WebplusUtil.isEmpty(eatNum)) {
			eatNum = 0;
		}
		Order order = orderService.selectById(orderId);
		if (WebplusUtil.isEmpty(order)) {
			return R.error("まだ注文されてません");
		}
		Dto pDto = Dtos.newDto("orderId", orderId);
		List<OrderLine> orderLineList = orderLineService.list(pDto);
		if (WebplusUtil.isEmpty(orderLineList)) {
			return R.error("まだ注文されてません");
		}
		List<Dto> lineDtoList=WebplusJson.fromJson(orderLines);
		if(WebplusUtil.isEmpty(lineDtoList)) {
			
			return R.error("It is illegal to buy dishes alone");
		}
		Integer eatNumRemain=order.getEatNum()-eatNum;
		if(eatNumRemain<0) {
			eatNumRemain=0;
		}
		String singleOrderId=WebplusCache.createOrderNum();
		//order.setEatNum(eatNumRemain);	
		List<OrderLine> singleLineList=Lists.newArrayList();

		for(Dto lineDto:lineDtoList) {
			String lineId=lineDto.getString("lineId");
			Integer buyNum=lineDto.getInteger("buyNum");
			OrderLine orderLine=this.getOrderLine(orderLineList, lineId);
			if(WebplusUtil.isNotEmpty(orderLine)&&buyNum>0) {
				OrderLine singleLine=new OrderLine();
				WebplusUtil.copyProperties(orderLine, singleLine);
				Integer buyNumSource=orderLine.getBuyNum();
				List<OrderLine> specLineList=this.getSpecLine(orderLineList, lineId);
				String whetherUpdate=WebplusCons.WHETHER_NO;
				String lineIdNew=WebplusUtil.uuid();
				singleLine.setLineId(lineIdNew);
				singleLine.setOrderId(singleOrderId);
				if(buyNum>=buyNumSource) {
					buyNum=buyNumSource;
				  
				}else {
					int remainBuyNum=buyNumSource-buyNum;
					orderLine.setBuyNum(remainBuyNum);
					whetherUpdate=WebplusCons.WHETHER_YES;
				}
				singleLine.setBuyNum(buyNum);
				singleLineList.add(singleLine);
				for(OrderLine specLine:specLineList) {
					OrderLine singleSpecLine=new OrderLine();
					WebplusUtil.copyProperties(specLine, singleSpecLine);
					singleSpecLine.setOrderId(singleOrderId);
					singleSpecLine.setParentId(lineIdNew);
					singleSpecLine.setBuyNum(buyNum);
					singleSpecLine.setLineId(WebplusUtil.uuid());
					singleLineList.add(singleSpecLine);
					if(WebplusCons.WHETHER_YES.equals(whetherUpdate)) {  //更新
						int remainBuyNum=specLine.getBuyNum()-buyNum;
						specLine.setBuyNum(remainBuyNum);
					}
				}
			}
			
			
				
			}
		if(WebplusUtil.isNotEmpty(singleLineList)) {
 			Order singleOrder=new Order();
			WebplusUtil.copyProperties(order, singleOrder);
			singleOrder.setEatNum(eatNum);
			singleOrder.setDeskNum(eatNum);
			singleOrder.setOrderType(BussCons.ORDER_TYPE_SIGNLE);
			singleOrder.setOrderId("");
			singleOrder=this.getOrder(singleOrder, singleLineList,WebplusCons.WHETHER_NO);
			singleOrder.setOrderId(singleOrderId);
			WebplusCache.setString(BussCons.PARENT_ORDER_KEY+singleOrderId, orderId,BussCons.ORDER_LIMIT_TIME);
			WebplusCache.setString(BussCons.ORDER_INFO_KEY+singleOrderId, WebplusJson.toJson(singleOrder),BussCons.ORDER_LIMIT_TIME);
			WebplusCache.setString(BussCons.ORDER_LINE_KEY+singleOrderId, orderLines,BussCons.ORDER_LIMIT_TIME);
			return R.toData(singleOrder);
		}
		return R.ok();
	}
	/**
	 * 
	 * 简要说明：获取剩余菜品
	 * 编写者：陈骑元（chenqiyuan@toonan.com）
	 * 创建时间： 2020年7月20日 下午10:57:58 
	 * @param 说明
	 * @return 说明
	 */
	private List<OrderLine> getRemainOrderLine(List<OrderLine> orderLineList,
			List<OrderLine> updateLineList,List<String> deleteList){
		List<OrderLine> orderLineListNew=Lists.newArrayList();
		for(OrderLine orderLine:orderLineList) {
			String lineId=orderLine.getLineId();
			if(!WebplusUtil.checkExistByList(lineId, deleteList)) {
				if(!checkOrderLine(lineId,updateLineList)) {
					orderLineListNew.add(orderLine);
				}
			}
		}
		if(WebplusUtil.isNotEmpty(updateLineList)) {
			orderLineListNew.addAll(updateLineList);
		}
		return orderLineListNew;
	}
	
	/**
	 * 
	 * 简要说明：检查是否存在
	 * 编写者：陈骑元（chenqiyuan@toonan.com）
	 * 创建时间： 2020年7月20日 下午11:04:06 
	 * @param 说明
	 * @return 说明
	 */
	private  boolean checkOrderLine(String lineId,List<OrderLine> orderLineList) {
		for(OrderLine orderLine:orderLineList) {
			if(orderLine.getLineId().equals(lineId))	{
				return true;
			}
		}
		return false;
	}
	
    /***
     * 
     * 简要说明：获取规格菜品
     * 编写者：陈骑元（chenqiyuan@toonan.com）
     * 创建时间： 2020年7月14日 下午8:34:35 
     * @param 说明
     * @return 说明
     */
	private List<OrderLine> getSpecLine(List<OrderLine> orderLineList ,String lineId){
	      List<OrderLine> specLineList=Lists.newArrayList();
		  for(OrderLine orderLine:orderLineList) {
			  if(lineId.equals(orderLine.getParentId())) {
				  specLineList.add(orderLine);
			  }
		  }
		  return specLineList;
	}
	/**
	 * 获取单点的菜品
	 */
	private OrderLine getOrderLine(List<OrderLine> orderLineList,String lineId) {
		for(OrderLine orderLine:orderLineList) {
			if(lineId.equals(orderLine.getLineId())) {
				
				return orderLine;
			}
		}
		return null;
	}
	
	/**
	 * 
	 * 简要说明：加载单品菜单 编写者：陈骑元 创建时间：2019年11月10日 上午8:17:19
	 * 
	 * @param 说
	 * @return 说明
	 */
	@PostMapping("listMenuDict")
	@ResponseBody
	public R listMenuDict(String whetherPos) {
		String shopId = this.getUserId();
		List<Dto> dataList = Lists.newArrayList();
		if(WebplusCons.WHETHER_YES.equals(whetherPos)) {
			List<Dto> menuList = Lists.newArrayList();
			Dto pDto = Dtos.newDto();
			pDto.put("shopId", shopId);
			pDto.put("whetherBuffet", BussCons.CUSTOM_MENU_TYPE);
			pDto.put("languageType", BussCons.LANGUAGE_TYPE_JP);
			pDto.setOrder(" sort_no ASC ");
			List<MenuCatalog> customMenuList = menuCatalogService.list(pDto);
			if(WebplusUtil.isNotEmpty(customMenuList)) {
				String catalogName=BussCache.getLanguageValue(BussCons.LANGUAGE_TYPE_JP, "custom_menu");
				Dto dataDto=Dtos.newDto();
				dataDto.put("catalogIndexId", "0");
				dataDto.put("menuName", catalogName);
				dataDto.put("menuPrice", 0);
				dataDto.put("whetherBuffet", BussCons.CUSTOM_MENU_TYPE);
				dataDto.put("customMenuList", customMenuList);
				menuList.add(dataDto);
			}
			pDto.put("whetherFirst", WebplusCons.WHETHER_NO);
			pDto.put("whetherBuffet", WebplusCons.WHETHER_YES);
			List<MenuCatalog> buffetMenuList = menuCatalogService.list(pDto);
			for (MenuCatalog menuCatalog : buffetMenuList) {
				String catalogIndexId = menuCatalog.getCatalogId();
				String catalogName = menuCatalog.getCatalogName();

				EntityWrapper<BuffetSpec> wrapper = new EntityWrapper<BuffetSpec>();
				wrapper.eq("catalog_index_id", catalogIndexId);
				wrapper.eq("language_type", BussCons.LANGUAGE_TYPE_JP);
				wrapper.orderBy("sort_no");
				List<BuffetSpec> buffetSpecList = buffetSpecService.selectList(wrapper);
				Dto dataDto = Dtos.newDto();
				dataDto.put("whetherBuffet", WebplusCons.WHETHER_YES);
				dataDto.put("catalogIndexId", catalogIndexId);
				dataDto.put("menuName", catalogName);
				dataDto.put("menuPrice", menuCatalog.getBuffetPrice());
				dataDto.put("whetherSpec", WebplusCons.WHETHER_NO);
				if(WebplusUtil.isNotEmpty(buffetSpecList)) {
					dataDto.put("whetherSpec", WebplusCons.WHETHER_YES);
					dataDto.put("specList", buffetSpecList);
				}
				menuList.add(dataDto);
				/*
				 * if (WebplusUtil.isNotEmpty(buffetSpecList)) { for (BuffetSpec buffetSpec :
				 * buffetSpecList) { Dto dataDto = Dtos.newDto(); dataDto.put("whetherBuffet",
				 * WebplusCons.WHETHER_YES); dataDto.put("catalogIndexId", catalogIndexId);
				 * dataDto.put("specIndexId", buffetSpec.getSpecIndexId());
				 * dataDto.put("menuPrice", buffetSpec.getSpecPrice()); dataDto.put("menuName",
				 * catalogName + "（" + buffetSpec.getSpecName() + "）"); menuList.add(dataDto); }
				 * } else { Dto dataDto = Dtos.newDto(); dataDto.put("whetherBuffet",
				 * WebplusCons.WHETHER_YES); dataDto.put("catalogIndexId", catalogIndexId);
				 * dataDto.put("menuName", catalogName); dataDto.put("menuPrice",
				 * menuCatalog.getBuffetPrice()); menuList.add(dataDto); }
				 */

			}
			pDto.remove("whetherFirst");
			pDto.put("whetherBuffet", BussCons.SERVER_MENU_TYPE);
			List<MenuCatalog> serverMenuList = menuCatalogService.list(pDto);
			for (MenuCatalog serverMenu : serverMenuList) {
				String catalogIndexId = serverMenu.getCatalogId();
				String catalogName = serverMenu.getCatalogName();
				Dto dataDto = Dtos.newDto();
				dataDto.put("whetherBuffet", BussCons.SERVER_MENU_TYPE);
				dataDto.put("catalogIndexId", catalogIndexId);
				dataDto.put("menuName", catalogName);
				dataDto.put("menuPrice", serverMenu.getBuffetPrice());
				menuList.add(dataDto);
			}
			String catalogName=BussCache.getLanguageValue(BussCons.LANGUAGE_TYPE_JP, "order_menu");
			Dto buffetDto = Dtos.newDto();
			buffetDto .put("catalogIndexId", "");
			buffetDto .put("catalogName", catalogName);
			buffetDto .put("catalogType", "0");
			buffetDto.put("menuDictList", menuList);
			dataList.add(buffetDto);
		}
	
		
		Dto pDto = Dtos.newDto();
		pDto.put("shopId", shopId);
		pDto.put("whetherBuffet", WebplusCons.WHETHER_NO);
		pDto.put("languageType", BussCons.LANGUAGE_TYPE_JP);
		pDto.setOrder(" sort_no ASC ");
		List<MenuCatalog> menuCatalogList = menuCatalogService.list(pDto);
		List<MenuDict> menuDictList = menuDictService.list(pDto);
	    
		for (MenuCatalog menuCatalog : menuCatalogList) {
			Dto dataDto = Dtos.newDto();
			String catalogIndexId = menuCatalog.getCatalogIndexId();
			dataDto.put("catalogIndexId", catalogIndexId);
			dataDto.put("catalogName", menuCatalog.getCatalogName());
			dataDto.put("catalogType", menuCatalog.getCatalogType());
			List<MenuDict> menuDictListNew = this.getCatalogMenuDictList(catalogIndexId, menuDictList);
			dataDto.put("menuDictList", menuDictListNew);
			dataList.add(dataDto);
		}
		return R.toList(dataList);
	}

	/**
	 * 
	 * 简要说明：查询产品规格 编写者：陈骑元 创建时间：2019年8月2日 下午10:54:12
	 * 
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("listMenuStepSpec")
	@ResponseBody
	public R listMenuStepSpec(String menuIndexId) {

		return orderCommonService.listMenuStepSpec(menuIndexId, BussCons.LANGUAGE_TYPE_JP);
	}

	/**
	 * 
	 * 简要说明：点餐服务菜单 编写者：陈骑元 创建时间：2020年1月13日 下午8:49:43
	 * 
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("listOrderServerMenu")
	@ResponseBody
	public R listOrderServerMenu() {
		String shopId = this.getUserId();
		List<Dto> dataList = Lists.newArrayList();
		Dto pDto = Dtos.newDto();
		pDto.put("shopId", shopId);
		pDto.put("whetherBuffet", WebplusCons.WHETHER_YES);
		pDto.put("languageType", BussCons.LANGUAGE_TYPE_JP);
		pDto.put("whetherFirst", WebplusCons.WHETHER_NO);
		pDto.setOrder(" sort_no ASC ");
		List<MenuCatalog> buffetMenuList = menuCatalogService.list(pDto);
		for (MenuCatalog menuCatalog : buffetMenuList) {
			String catalogIndexId = menuCatalog.getCatalogId();
			String catalogName = menuCatalog.getCatalogName();

			EntityWrapper<BuffetSpec> wrapper = new EntityWrapper<BuffetSpec>();
			wrapper.eq("catalog_index_id", catalogIndexId);
			wrapper.eq("language_type", BussCons.LANGUAGE_TYPE_JP);
			wrapper.orderBy("sort_no");
			List<BuffetSpec> buffetSpecList = buffetSpecService.selectList(wrapper);
			if (WebplusUtil.isNotEmpty(buffetSpecList)) {
				for (BuffetSpec buffetSpec : buffetSpecList) {
					Dto dataDto = Dtos.newDto();
					dataDto.put("catalogIndexId", catalogIndexId);
					dataDto.put("specIndexId", buffetSpec.getSpecIndexId());
					dataDto.put("menuPrice", buffetSpec.getSpecPrice());
					dataDto.put("catalogName", catalogName + "（" + buffetSpec.getSpecName() + "）");
					dataList.add(dataDto);
				}
			} else {
				Dto dataDto = Dtos.newDto();
				dataDto.put("catalogIndexId", catalogIndexId);
				dataDto.put("catalogName", catalogName);
				dataDto.put("menuPrice", menuCatalog.getBuffetPrice());
				dataList.add(dataDto);
			}

		}
		pDto.put("whetherBuffet", BussCons.SERVER_MENU_TYPE);
		List<MenuCatalog> serverMenuList = menuCatalogService.list(pDto);
		for (MenuCatalog serverMenu : serverMenuList) {
			String catalogIndexId = serverMenu.getCatalogId();
			String catalogName = serverMenu.getCatalogName();
			Dto dataDto = Dtos.newDto();
			dataDto.put("catalogIndexId", catalogIndexId);
			dataDto.put("catalogName", catalogName);
			dataDto.put("menuPrice", serverMenu.getBuffetPrice());
			dataList.add(dataDto);
		}
		return R.toList(dataList);

	}
	/**
	 * 
	 * 简要说明：点餐服务菜单 编写者：陈骑元 创建时间：2020年1月13日 下午8:49:43
	 * 
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("listOtherMenu")
	@ResponseBody
	public R listOtherMenu() {
		String shopId = this.getUserId();
		List<Dto> dataList = Lists.newArrayList();
		Dto pDto = Dtos.newDto();
		pDto.put("shopId", shopId);
		pDto.put("whetherBuffet", BussCons.CUSTOM_MENU_TYPE);
		pDto.put("languageType", BussCons.LANGUAGE_TYPE_JP);
		pDto.setOrder(" sort_no ASC ");
		List<MenuCatalog> customMenuList = menuCatalogService.list(pDto);
		if(WebplusUtil.isNotEmpty(customMenuList)) {
			String catalogName=BussCache.getLanguageValue(BussCons.LANGUAGE_TYPE_JP, "custom_menu");
			Dto dataDto=Dtos.newDto();
			dataDto.put("catalogIndexId", "0");
			dataDto.put("catalogName", catalogName);
			dataDto.put("menuPrice", 0);
			dataDto.put("whetherBuffet", BussCons.CUSTOM_MENU_TYPE);
			dataDto.put("customMenuList", customMenuList);
		}
		pDto.put("whetherFirst", WebplusCons.WHETHER_NO);
		
		List<MenuCatalog> buffetMenuList = menuCatalogService.list(pDto);
		for (MenuCatalog menuCatalog : buffetMenuList) {
			String catalogIndexId = menuCatalog.getCatalogId();
			String catalogName = menuCatalog.getCatalogName();

			EntityWrapper<BuffetSpec> wrapper = new EntityWrapper<BuffetSpec>();
			wrapper.eq("catalog_index_id", catalogIndexId);
			wrapper.eq("language_type", BussCons.LANGUAGE_TYPE_JP);
			wrapper.orderBy("sort_no");
			List<BuffetSpec> buffetSpecList = buffetSpecService.selectList(wrapper);
			if (WebplusUtil.isNotEmpty(buffetSpecList)) {
				for (BuffetSpec buffetSpec : buffetSpecList) {
					Dto dataDto = Dtos.newDto();
					dataDto.put("whetherBuffet", WebplusCons.WHETHER_YES);
					dataDto.put("catalogIndexId", catalogIndexId);
					dataDto.put("specIndexId", buffetSpec.getSpecIndexId());
					dataDto.put("menuPrice", buffetSpec.getSpecPrice());
					dataDto.put("catalogName", catalogName + "（" + buffetSpec.getSpecName() + "）");
					dataList.add(dataDto);
				}
			} else {
				Dto dataDto = Dtos.newDto();
				dataDto.put("whetherBuffet", WebplusCons.WHETHER_YES);
				dataDto.put("catalogIndexId", catalogIndexId);
				dataDto.put("catalogName", catalogName);
				dataDto.put("menuPrice", menuCatalog.getBuffetPrice());
				dataList.add(dataDto);
			}

		}
		pDto.remove("whetherFirst");
		pDto.put("whetherBuffet", BussCons.SERVER_MENU_TYPE);
		List<MenuCatalog> serverMenuList = menuCatalogService.list(pDto);
		for (MenuCatalog serverMenu : serverMenuList) {
			String catalogIndexId = serverMenu.getCatalogId();
			String catalogName = serverMenu.getCatalogName();
			Dto dataDto = Dtos.newDto();
			dataDto.put("whetherBuffet", BussCons.SERVER_MENU_TYPE);
			dataDto.put("catalogIndexId", catalogIndexId);
			dataDto.put("catalogName", catalogName);
			dataDto.put("menuPrice", serverMenu.getBuffetPrice());
			dataList.add(dataDto);
		}
		return R.toList(dataList);

	}
	/**
	 * 
	 * 简要说明：点餐服务 编写者：陈骑元 创建时间：2020年1月13日 下午9:08:40
	 * 
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("saveOrderServer")
	@ResponseBody
	public R saveOrderServer(String deskId, String orderLines) {
		if (WebplusUtil.isNotAnyEmpty(deskId, orderLines)) {
			Desk desk = deskService.selectById(deskId);
			if (WebplusUtil.isEmpty(desk)) {

				return R.error("卓番が間違ってます");
			}
			String shopId = desk.getShopId();
			String orderId = desk.getOrderNo();
			String deskName = desk.getDeskName();
			Date currentTime = WebplusUtil.getDateTime();
			if (WebplusUtil.isNotEmpty(orderId)) {
				List<Dto> orderLineList = WebplusJson.fromJson(orderLines);
				if (WebplusUtil.isNotEmpty(orderLineList)) {
					List<MenuCatalog> menuCatalogList = this.getMenuCatalogList(orderLineList);
					List<OrderLine> orderLineListNew = Lists.newArrayList();
					List<Dto> printList = Lists.newArrayList();
					String buffetCatalogIndexId = "";
					int chooseNum = 0;
					for (Dto orderLineDto : orderLineList) {
						String catalogIndexId = orderLineDto.getString("catalogIndexId");
						String specIndexId = orderLineDto.getString("specIndexId");
						int buyNum = orderLineDto.getInteger("buyNum");
						MenuCatalog menuCatalog = this.getMenuCatalog(catalogIndexId, menuCatalogList);
						if (WebplusUtil.isNotEmpty(menuCatalog)) {
							String whetherBuffet = menuCatalog.getWhetherBuffet();
							String menuName = menuCatalog.getCatalogName();
							Integer buffetPrice = menuCatalog.getBuffetPrice();
							if (WebplusCons.WHETHER_YES.equals(whetherBuffet) || buffetPrice > 0) {
								OrderLine orderLine = new OrderLine();
								orderLine.setOrderId(orderId);
								orderLine.setCatalogIndexId(catalogIndexId);
								orderLine.setShopId(shopId);
								orderLine.setBuyNum(buyNum);
								orderLine.setCreateTime(currentTime);
								orderLine.setUpdateTime(currentTime);
								orderLine.setPrintCatalogIndexId(catalogIndexId);
								orderLine.setMenuName(menuCatalog.getCatalogName());
								orderLine.setMenuPrice(menuCatalog.getBuffetPrice());

								orderLine.setPrintMenuName(menuCatalog.getCatalogName());
								orderLine.setWhetherAdd(WebplusCons.WHETHER_NO);
								orderLine.setWhetherSpec(WebplusCons.WHETHER_NO);
								if (WebplusCons.WHETHER_YES.equals(whetherBuffet)) {
									orderLine.setWhetherBuffet(WebplusCons.WHETHER_YES);
									chooseNum += buyNum * menuCatalog.getLimitNum();
									buffetCatalogIndexId = catalogIndexId;
									orderLine.setChooseNum(chooseNum);
								} else {
									orderLine.setWhetherBuffet(BussCons.SERVER_MENU_TYPE);
								}

								orderLine.setTaxType(menuCatalog.getTaxType());
								if (WebplusUtil.isNotEmpty(specIndexId)) {
									BuffetSpec buffetSpec = buffetSpecService.selectById(specIndexId);
									if (WebplusUtil.isNotEmpty(buffetSpec)) {
										orderLine.setWhetherSpec(WebplusCons.WHETHER_YES);
										menuName = menuCatalog.getCatalogName() + "（" + buffetSpec.getSpecName() + "）";
										orderLine.setMenuName(menuName);
										orderLine.setPrintMenuName(menuName);
										orderLine.setMenuPrice(buffetSpec.getSpecPrice());
									}
								}
								orderLine.setSpecIndexId(specIndexId);
								orderLineListNew.add(orderLine);
							}
							Dto printDto = Dtos.newDto();
							printDto.put("shopId", shopId);
							printDto.put("orderId", orderId);
							printDto.put("printNum", menuCatalog.getPrintNum());
							printDto.put("printMenuName", menuName);
							printDto.put("buyNum", buyNum);
							printDto.put("whetherBuffet", whetherBuffet);
							printList.add(printDto);
						}

					}
					Order order = orderService.selectById(orderId);
					if (WebplusUtil.isEmpty(order)) {
						Integer eatNum = desk.getEatNum();
						order = new Order();
						order.setOrderId(orderId);
						order.setEatNum(eatNum);
						order.setDeskNum(eatNum);
						order.setShopId(shopId);
						order.setDeskId(desk.getDeskId());
						order.setDeskName(desk.getDeskName());
						order.setOrderTime(currentTime);
						order.setCreateTime(currentTime);
						desk.setDeskStatus(BussCons.DESK_STATUS_FULL);
						desk.setOrderTime(currentTime);
						desk.setUpdateTime(WebplusUtil.getDateTime());
						deskService.updateById(desk);
						orderService.insert(order);

					}
					if (WebplusUtil.isNotEmpty(orderLineListNew)) {
						orderLineService.insertBatch(orderLineListNew);
					}
					if (WebplusUtil.isNotEmpty(buffetCatalogIndexId)) {
						orderCommonService.setBuffetOderCache(orderId, buffetCatalogIndexId, chooseNum);
					}
					printAsyncTask.sendPrintOrderServer(deskName, printList);

					return R.ok();
				}
			}
		}
		return R.error();
	}

	/**
	 * 
	 * 简要说明：获取菜品类型 编写者：陈骑元 创建时间：2020年1月13日 下午9:56:46
	 * 
	 * @param 说明
	 * @return 说明
	 */
	private MenuCatalog getMenuCatalog(String catalogIndexId, List<MenuCatalog> menuCatalogList) {
		if (WebplusUtil.isNotAnyEmpty(menuCatalogList, catalogIndexId)) {
			for (MenuCatalog menuCatalog : menuCatalogList) {
				if (catalogIndexId.equals(menuCatalog.getCatalogId())) {
					return menuCatalog;
				}
			}
		}
		return null;
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
	 * 
	 * 简要说明：查询今天销售总额 编写者：陈骑元 创建时间：2019年11月10日 下午3:43:45
	 * 
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("queryTodayAmount")
	@ResponseBody
	public R queryTodayAmount() {
		String queryDate = WebplusUtil.getDateStr();
		Dto calcDto = Dtos.newCalcDto("IFNULL(SUM(total_amount),0)");

		String shopId = this.getUserId();
		calcDto.put("shopId", shopId);

		calcDto.put("whetherPay", WebplusCons.WHETHER_YES);
		Shop shop = shopService.selectById(shopId);
		if (WebplusUtil.isNotEmpty(shop)) {
			String timeReport = shop.getTimeReport();
			if (BussCons.TIME_REPORT_DEFAULT.equals(timeReport)) {
				calcDto.put("queryDate", queryDate);
			} else {
				queryDate = BussUtil.getReportDate(timeReport);
				String tomorrowDate = WebplusUtil.getCurrentDate(WebplusCons.DATE, 1);
				String beginTime = queryDate + " " + timeReport;
				String endTime = tomorrowDate + " " + timeReport;
				calcDto.put("beginTime", beginTime);
				calcDto.put("endTime", endTime);
			}
		} else {
			calcDto.put("queryDate", queryDate);
		}

		List<String> orderTypeList = Lists.newArrayList();
		orderTypeList.add(BussCons.ORDER_TYPE_COMMON);
		orderTypeList.add(BussCons.ORDER_TYPE_PARENT);
		orderTypeList.add(BussCons.ORDER_TYPE_SIGNLE);
		calcDto.put("orderTypeList", orderTypeList);

		String totalAmountStr = orderService.calc(calcDto);

		return R.toData(totalAmountStr);
	}

	/**
	 * 
	 * 简要说明：查询订单汇总信息 编写者：陈骑元 创建时间：2019年11月10日 下午2:58:42
	 * 
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("queryOrderSum")
	@ResponseBody
	public R queryOrderSum(String queryDate) {
		if (WebplusUtil.isEmpty(queryDate)) {
			queryDate = WebplusUtil.getDateStr();
		}
		String shopId = this.getUserId();
		Dto pDto = Dtos.newDto();
		pDto.put("shopId", shopId);
		Shop shop = shopService.selectById(shopId);
		if (WebplusUtil.isNotEmpty(shop)) {
			String timeReportStr = shop.getTimeReport();
			if (BussCons.TIME_REPORT_DEFAULT.equals(timeReportStr)) {
				pDto.put("queryDate", queryDate);
			} else {
				String currentDate = WebplusUtil.getDateStr();
				if (queryDate.equals(currentDate)) {
					queryDate = BussUtil.getReportDate(timeReportStr);
				}
				String tomorrowDate = WebplusUtil.plusDay(1, queryDate);
				String beginTime = queryDate + " " + timeReportStr;
				String endTime = tomorrowDate + " " + timeReportStr;
				pDto.put("beginTime", beginTime);
				pDto.put("endTime", endTime);
			}
		} else {
			pDto.put("queryDate", queryDate);
		}
		pDto.put("whetherPay", WebplusCons.WHETHER_YES);
		List<String> orderTypeList = Lists.newArrayList();
		orderTypeList.add(BussCons.ORDER_TYPE_COMMON);
		orderTypeList.add(BussCons.ORDER_TYPE_PARENT);
		orderTypeList.add(BussCons.ORDER_TYPE_SIGNLE);
		pDto.put("orderTypeList", orderTypeList);
		int subMinute=0;
		if(WebplusUtil.isNotEmpty(shop)){
			String timeReport=shop.getTimeReport();
			subMinute=BussUtil.countHourTime(timeReport);
		}
		pDto.put("subMinute",subMinute);
		OrderSum orderSum = bussCommonService.queryOrderSum(pDto);
		if (WebplusUtil.isEmpty(orderSum)) {
			orderSum = new OrderSum();
		}
		
		List<OrderLine> orderLineList = bussCommonService.listSumOrderLine(pDto);
		List<OrderLine> orderLineListNew = Lists.newArrayList();
		int tenMenuAmount = 0;
		int eightMenuAmount = 0;
		for (OrderLine orderLine : orderLineList) {
			String menuIndexId = orderLine.getMenuIndexId();
			String whetherBuffet = orderLine.getWhetherBuffet();
			if (WebplusCons.WHETHER_NO.equals(whetherBuffet) || WebplusUtil.isEmpty(menuIndexId)) {
				Integer buyNum = BussUtil.dealEmptyAmount(orderLine.getBuyNum());
				Integer menuPrice = BussUtil.dealEmptyAmount(orderLine.getMenuPrice());
				Integer menuSumPrice = menuPrice * buyNum;
				if (WebplusCons.WHETHER_YES.equals(orderLine.getWhetherTakeOut())) {
					eightMenuAmount += menuSumPrice;
				} else {
					tenMenuAmount += menuSumPrice;
				}
				orderLine.setMenuSumPrice(menuSumPrice);
				orderLineListNew.add(orderLine);
			}

		}
		 tenMenuAmount+=orderSum.getDeskAmount();
		int tenTaxAmount = BussUtil.countTaxes(0, tenMenuAmount, BussCons.EAT_TAXES);
		orderSum.setOrderDate(queryDate);
		orderSum.setTenMenuAmount(tenMenuAmount);
		orderSum.setEightMenuAmount(eightMenuAmount);
		orderSum.setTenTaxAmount(tenTaxAmount);
		List<Dto> dataList = this.getSumOrderLine(orderLineListNew);

		return R.toDataAndList(orderSum, dataList);

	}

	/**
	 * 
	 * 简要说明：查询最新apk 编写者：陈骑元 创建时间：2019年11月12日 下午10:07:06
	 * 
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("queryNewApk")
	@ResponseBody
	public R queryNewApk(String apkType) {
		if(WebplusUtil.isEmpty(apkType)) {
			apkType=BussCons.APK_TYPE_ANDROID;
		}
		EntityWrapper<ApkVersion> wrapper = new EntityWrapper<ApkVersion>();
		wrapper.eq("apk_type", apkType);
		wrapper.orderBy(" version_num DESC");
		ApkVersion apkVersion = apkVersionService.selectOne(wrapper);

		return R.toData(apkVersion);

	}

	/**
	 * 
	 * 简要说明：下载APK方法 编写者：陈骑元 创建时间：2019年10月25日 下午11:03:57
	 * 
	 * @param 说明
	 * @return 说明
	 */
	@RequestMapping(value = "downloadApk")
	public void downloadApk(String fid, HttpServletRequest request, HttpServletResponse response) {

		if (WebplusUtil.isNotEmpty(fid)) {
			String folderPath = WebplusCache.getParamValue(WebplusCons.SAVE_ROOT_PATH_KEY);
			String filePath = folderPath + File.separator + BussCons.APK_FILE + File.separator + fid;
			WebplusFile.downloadFile(request, response, filePath, fid);
		}

	}

	/**
	 * 
	 * 简要说明：获取总分 编写者：陈骑元 创建时间：2019年12月16日 下午11:38:11
	 * 
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("getSummary")
	@ResponseBody
	public R getSummary(String orderId, String memberId) {
		if (WebplusUtil.isEmpty(memberId)) {

			return R.error("会员编号不能为空");
		}
		EntityWrapper<Member> wrapper = new EntityWrapper<Member>();
		wrapper.eq("member_num", memberId);
		Member member = memberService.selectOne(wrapper);
		if (WebplusUtil.isEmpty(member)) {
			return R.error("会员编号不合法");
		}
		int point = PointApiUtil.getSummary(memberId);
		Dto dataDto = Dtos.newDto();
		dataDto.put("point", point);
		dataDto.put("memberId", memberId);
		dataDto.put("username", member.getUsername());
		dataDto.put("mobile", member.getMobile());
		dataDto.put("pointPer", "1");
		return R.toData(dataDto);
	}

	/**
	 * 
	 * 简要说明：订单积分 编写者：陈骑元 创建时间：2019年12月16日 下午11:51:28
	 * 
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("plusPoint")
	@ResponseBody
	public R plusPoint(String orderId, String memberId, int totalAmount) {
		if (WebplusUtil.isEmpty(orderId)) {
			return R.error("注文番号が無意味です");
		}
		if (WebplusUtil.isEmpty(memberId)) {
			return R.error("会员编号不能为空");
		}
		Order order = orderService.selectById(orderId);
		if (WebplusUtil.isEmpty(order)) {

			return R.error("订单号不存在");
		}
		if (WebplusUtil.isNotEmpty(order.getMemberId())) {

			return R.error("该订单已经进行积分");
		}
		String shopId = order.getShopId();
		Shop shop = shopService.selectById(shopId);
		String mertCode = shop.getMertCode();
		if (WebplusUtil.isEmpty(mertCode)) {

			return R.error("mertCode还没配置");
		}
		boolean result = PointApiUtil.adjust(memberId, mertCode, shop.getOutletCode(), totalAmount);
		if (result) {
			order.setPlusPoint(totalAmount);
			order.setMemberId(memberId);
			orderService.updateById(order);

			return R.ok();
		}
		return R.error();
	}

	/**
	 * 
	 * 简要说明：积分抵扣 编写者：陈骑元 创建时间：2019年12月16日 下午11:51:28
	 * 
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("subPoint")
	@ResponseBody
	public R subPoint(String orderId, String memberId, int point) {
		if (WebplusUtil.isEmpty(orderId)) {
			return R.error("注文番号が無意味です");
		}
		if (WebplusUtil.isEmpty(memberId)) {
			return R.error("会员编号不能为空");
		}
		Order order = orderService.selectById(orderId);
		if (WebplusUtil.isEmpty(order)) {

			return R.error("订单号不存在");
		}
		String shopId = order.getShopId();
		Shop shop = shopService.selectById(shopId);
		String mertCode = shop.getMertCode();
		if (WebplusUtil.isEmpty(mertCode)) {

			return R.error("mertCode还没配置");
		}
		boolean result = PointApiUtil.adjust(memberId, mertCode, shop.getOutletCode(), 0 - point);
		if (result) {
			int pointAmount = BussUtil.convertPoint(BussCons.POINT_PER_ONE, point);
			int totalPointAmount = BussUtil.dealEmptyAmount(order.getPointAmount()) + pointAmount;
			order.setPointAmount(totalPointAmount);
			order.setSubPoint(point);
			order.setMemberId(memberId);
			orderService.updateById(order);
			return R.toData(pointAmount);
		}
		return R.error();
	}

	/**
	 * 
	 * 简要说明：积分调整 编写者：陈骑元 创建时间：2019年12月16日 下午11:51:28
	 * 
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("adjustPoint")
	@ResponseBody
	public R adjustPoint(String orderId, String memberId, int totalAmount, int point) {
		if (WebplusUtil.isEmpty(orderId)) {
			return R.error("注文番号が無意味です");
		}
		if (WebplusUtil.isEmpty(memberId)) {
			return R.error("会员编号不能为空");
		}
		Order order = orderService.selectById(orderId);
		if (WebplusUtil.isEmpty(order)) {

			return R.error("订单号不存在");
		}
		if (totalAmount < 1) {
			return R.error("订单总金额为负数");
		}
		String shopId = order.getShopId();
		Shop shop = shopService.selectById(shopId);
		String mertCode = shop.getMertCode();
		if (WebplusUtil.isEmpty(mertCode)) {

			return R.error("mertCode还没配置");
		}
		if (point > 0) {
			boolean result = PointApiUtil.adjust(memberId, mertCode, shop.getOutletCode(), 0 - point);
			if (!result) {
				return R.error("积分抵扣失败");
			}
			order.setSubPoint(point);
			order.setPointAmount(point);
			totalAmount = totalAmount - point;
		}
		int tax = BussUtil.countAfterTaxes(totalAmount, 10);
		int baseTotal = totalAmount - tax;
		boolean result = PointApiUtil.earn(memberId, mertCode, shop.getOutletCode(), baseTotal, tax, totalAmount);
		if (result) {
			// order.setPlusPoint(plusPoint);
			order.setMemberId(memberId);
			orderService.updateById(order);
			return R.ok();
		}
		return R.error();
	}

	/**
	 * 
	 * 简要说明：获取汇总菜单 编写者：陈骑元 创建时间：2019年11月10日 下午3:34:48
	 * 
	 * @param 说明
	 * @return 说明
	 */
	private List<Dto> getSumOrderLine(List<OrderLine> orderLineList) {
		List<OrderLine> orderLineListNew = this.getOrderLineList(orderLineList, WebplusCons.WHETHER_YES);
		orderLineListNew = this.getMergeOrderLine(orderLineListNew); // 合并订单
		Collections.sort(orderLineListNew, new Comparator<OrderLine>() {

			@Override
			public int compare(OrderLine o1, OrderLine o2) {
				if (o2.getMenuSumPrice().intValue() > o1.getMenuSumPrice().intValue()) {

					return 1;
				} else {
					return -1;
				}

			}

		});
		List<Dto> dataList = Lists.newArrayList();
		for (OrderLine orderLine : orderLineListNew) {
			Dto dataDto = Dtos.newDto();

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
	 * 简要说明：获取种类菜品 编写者：陈骑元 创建时间：2019年8月2日 下午11:12:41
	 * 
	 * @param 说明
	 * @return 说明
	 */
	private List<MenuDict> getCatalogMenuDictList(String catalogIndexId, List<MenuDict> menuDictList) {
		List<MenuDict> menuDictListNew = Lists.newArrayList();
		for (MenuDict menuDict : menuDictList) {
			if (catalogIndexId.equals(menuDict.getCatalogIndexId())) {

				menuDictListNew.add(menuDict);
			}
		}
		return menuDictListNew;
	}

	/**
	 * 
	 * 简要说明：去除垃圾数据 编写者：陈骑元 创建时间：2019年12月10日 上午1:27:08
	 * 
	 * @param 说明
	 * @return 说明
	 */
	private List<OrderLine> removeWasteData(List<OrderLine> orderLineList) {
		List<OrderLine> orderLineListNew = Lists.newArrayList();
		if (WebplusUtil.isNotEmpty(orderLineList)) {
			for (OrderLine orderLine : orderLineList) {
				String parentId = orderLine.getParentId();
				if (WebplusUtil.isNotEmpty(parentId)) {
					if (checkParentId(parentId, orderLineList)) {
						orderLineListNew.add(orderLine);
					}
				} else {
					orderLineListNew.add(orderLine);
				}
			}
		}

		return orderLineListNew;
	}

	/**
	 * 
	 * 简要说明：检查parentId是否有效 编写者：陈骑元 创建时间：2019年12月10日 上午1:29:39
	 * 
	 * @param 说明
	 * @return 说明
	 */
	private boolean checkParentId(String parentId, List<OrderLine> orderLineList) {
		for (OrderLine orderLine : orderLineList) {
			if (parentId.equals(orderLine.getLineId())) {

				return true;
			}
		}

		return false;
	}

	/**
	 * 
	 * 简要说明：设置订单限制 编写者：陈骑元 创建时间：2019年12月16日 下午11:38:11
	 * 
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("setOrderLimit")
	@ResponseBody
	public R setOrderLimit(Integer limit) {
		String shopId = this.getUserId();
		if (WebplusUtil.isNotEmpty(limit) && limit > 0) {

			String key = BussCons.ORDER_LIMIT_KEY + shopId;
			WebplusCache.setString(key, shopId, limit * 60);
		}

		return R.ok();
	}

	

	/**
	 * 
	 * 简要说明：折扣递减 编写者：陈骑元 创建时间：2019年12月16日 下午11:51:28
	 * 
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("subOrderAmount")
	@ResponseBody
	public R subOrderAmount(String orderId, Integer subAmount, Integer subRate) {
		String userId = this.getUserId();
		if (WebplusUtil.isEmpty(orderId)) {

			return R.error("注文番号が無意味です");
		}
		if (WebplusUtil.isEmpty(subAmount)) {
			subAmount = 0;
		}
		if (WebplusUtil.isEmpty(subRate)) {
			subRate = 100;
		}
		if (subRate < 0) {
			subRate = 0;
		}
		if (subRate > 100) {
			subRate = 100;
		}
		if (subAmount < 0) {
			subAmount = 0;
		}
		Order order = orderService.selectById(orderId);
		if (WebplusUtil.isEmpty(order)) {

			return R.error("注文書番号は存在しません");
		}
		Integer totalAmount = order.getTotalAmount();

		totalAmount = BussUtil.countSubRate(totalAmount, subAmount, subRate);
		order.setTotalAmount(totalAmount);
		order.setSubAmount(subAmount);
		order.setSubRate(subRate);
		order.setUpdateTime(WebplusUtil.getDateTime());
		order.setUpdateBy(userId);
		boolean result = orderService.updateById(order);
		if (result) {
			return R.ok();
		}
		return R.error();
	}

	/**
	 * 
	 * 简要说明：菜品递减 编写者：陈骑元 创建时间：2019年12月16日 下午11:51:28
	 * 
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("subOrderLineAmount")
	@ResponseBody
	public R subOrderLineAmount(String lineIds, Integer subAmount, Integer subRate) {
		String userId = this.getUserId();
		if (WebplusUtil.isEmpty(lineIds)) {

			return R.error("lineIdsは空ではない");
		}
		if (WebplusUtil.isEmpty(subAmount)) {
			subAmount = 0;
		}
		if (WebplusUtil.isEmpty(subRate)) {
			subRate = 100;
		}
		if (subRate < 0) {
			subRate = 0;
		}
		if (subRate > 100) {
			subRate = 100;
		}
		if (subAmount < 0) {
			subAmount = 0;
		}
		List<String> lineIdList = WebplusFormater.separatStringToList(lineIds);
		if (WebplusUtil.isNotEmpty(lineIdList)) {
			List<OrderLine> orderLineList = Lists.newArrayList();
			for (String lineId : lineIdList) {
				OrderLine orderLine = new OrderLine();
				orderLine.setLineId(lineId);
				orderLine.setSubAmount(subAmount);
				orderLine.setSubRate(subRate);
				orderLine.setUpdateTime(WebplusUtil.getDateTime());
				orderLine.setUpdateBy(userId);
				orderLineList.add(orderLine);
			}
			if (WebplusUtil.isNotEmpty(orderLineList)) {
				orderLineService.updateBatchById(orderLineList);
				if (WebplusUtil.isNotEmpty(orderLineList)) { // 同时更新规格的数量
					for (OrderLine orderLine : orderLineList) {
						if (WebplusUtil.isNotEmpty(orderLine.getLineId())) {
							OrderLine entity = new OrderLine();
							entity.setSubAmount(orderLine.getSubAmount());
							entity.setSubRate(orderLine.getSubRate());
							entity.setUpdateTime(WebplusUtil.getDateTime());
							EntityWrapper<OrderLine> wrapper = new EntityWrapper<OrderLine>();
							wrapper.eq("parent_id", orderLine.getLineId());
							orderLineService.update(entity, wrapper);
						}

					}
				}
			}
		}

		return R.ok();
	}

	/**
	 * 
	 * 简要说明： 编写者：陈骑元（chenqiyuan@toonan.com） 创建时间： 2020年7月5日 上午2:37:28
	 * 
	 * @param 说明 取消折扣
	 * @return 说明
	 */
	@PostMapping("cancelDiscount")
	@ResponseBody
	public R cancelDiscount(String orderId, String cancelWay) {
		if (WebplusUtil.isEmpty(orderId)) {

			return R.error("注文番号が無意味です");
		}
		if (BussCons.CANCEL_WAY_FULL.equals(cancelWay)) {
			Order order = new Order();
			order.setSubAmount(0);
			order.setSubRate(0);
			order.setOrderId(orderId);
			orderService.updateById(order);
			OrderLine orderLine = new OrderLine();
			orderLine.setSubAmount(0);
			orderLine.setSubRate(0);
			EntityWrapper<OrderLine> wrapper = new EntityWrapper<OrderLine>();
			wrapper.eq("order_id", orderId);
			orderLineService.update(orderLine, wrapper);
		} else if (BussCons.CANCEL_WAY_ORDER.equals(cancelWay)) {
			Order order = new Order();
			order.setSubAmount(0);
			order.setOrderId(orderId);
			orderService.updateById(order);
			OrderLine orderLine = new OrderLine();
			orderLine.setSubAmount(0);
			EntityWrapper<OrderLine> wrapper = new EntityWrapper<OrderLine>();
			wrapper.eq("order_id", orderId);
			orderLineService.update(orderLine, wrapper);
		} else if (BussCons.CANCEL_WAY_ORDERLINE.equals(cancelWay)) {
			Order order = new Order();
			order.setSubRate(0);
			order.setOrderId(orderId);
			orderService.updateById(order);
			OrderLine orderLine = new OrderLine();
			orderLine.setSubRate(0);
			EntityWrapper<OrderLine> wrapper = new EntityWrapper<OrderLine>();
			wrapper.eq("order_id", orderId);
			orderLineService.update(orderLine, wrapper);
		}
		return R.ok();
	}

	/**
	 * 
	 * 简要说明：转移桌号 编写者：陈骑元（chenqiyuan@toonan.com） 创建时间： 2020年6月24日 下午10:25:34
	 * 
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("transferDesk")
	@ResponseBody
	public R transferDesk(String fromDeskId, String toDeskId) {
		if (WebplusUtil.isEmpty(fromDeskId)) {

			return R.error("fromDeskId卓番が間違ってます");
		}
		if (WebplusUtil.isEmpty(toDeskId)) {

			return R.error("toDeskId卓番が間違ってます");
		}
		Desk fromDesk = deskService.selectById(fromDeskId);
		if (WebplusUtil.isEmpty(fromDesk)) {
			return R.error("fromDeskId卓番が間違ってます");
		}
		Desk toDesk = deskService.selectById(toDeskId);
		if (WebplusUtil.isEmpty(toDesk)) {
			return R.error("toDeskId卓番が間違ってます");
		}
		if (BussCons.DESK_STATUS_EMPTY.equals(fromDesk.getDeskStatus())) {

			return R.error("fromDeskId卓番が間違ってます");
		}
		if (BussCons.DESK_STATUS_FULL.equals(toDesk.getDeskStatus())) {

			return R.error("toDeskId卓番が間違ってます");
		}
		// 设置新的桌位
		String orderId = fromDesk.getOrderNo();
		toDesk.setOrderNo(orderId);
		toDesk.setDeskStatus(BussCons.DESK_STATUS_FULL);
		toDesk.setEatNum(fromDesk.getEatNum());
		toDesk.setUpdateTime(WebplusUtil.getDateTime());
		deskService.updateById(toDesk);
		// 解除占座
		fromDesk.setDeskStatus(BussCons.DESK_STATUS_EMPTY); // 解除占座
		fromDesk.setEatNum(0);
		fromDesk.setOrderNo("");
		fromDesk.setOrderTime(null);
		fromDesk.setUpdateTime(WebplusUtil.getDateTime());
		deskService.updateById(fromDesk);
		if (WebplusUtil.isNotEmpty(orderId)) {
			Order order = new Order();
			order.setOrderId(orderId);
			order.setDeskId(toDeskId);
			order.setDeskName(toDesk.getDeskName());
			order.setUpdateTime(WebplusUtil.getDateTime());
			orderService.updateById(order);
		}

		return R.ok();
	}
    
	
	/**
	 * 
	 * 简要说明：查询自助餐规格
	 * 编写者：陈骑元
	 * 创建时间：2020年1月10日 下午10:38:24
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("listBuffetSpec")
	@ResponseBody
	public R listBuffetSpec(String catalogIndexId){
		
		EntityWrapper<BuffetSpec> wrapper=new EntityWrapper<BuffetSpec>();
		wrapper.eq("catalog_index_id", catalogIndexId);
		wrapper.eq("language_type", BussCons.LANGUAGE_TYPE_JP);
		wrapper.orderBy("sort_no");
		List<BuffetSpec>  buffetSpecList=buffetSpecService.selectList(wrapper);
		
		return R.toList(buffetSpecList);
	}
	/**
	 * 
	 * 简要说明：提出登录 编写者：陈骑元（chenqiyuan@toonan.com） 创建时间： 2020年6月24日 下午10:19:43
	 * 
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("logout")
	@ResponseBody
	public R logout(String token) {

		WebplusCache.removeToken(token);

		return R.ok();
	}
	
	/**
	 * 
	 * 简要说明：推送打印小票
	 * 编写者：陈骑元（chenqiyuan@toonan.com）
	 * 创建时间： 2020年7月15日 下午10:37:01 
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("pushPrintReceipt")
	@ResponseBody
	public R pushPrintReceipt(String orderId,String receiptType) {
		if (WebplusUtil.isEmpty(orderId)) {

			return R.error("注文番号が無意味です");
		}

		Order order = orderService.selectById(orderId);
		if (WebplusUtil.isEmpty(order)) {
			return R.error("まだ注文されてません");
		}
		String whehterParentOrder = WebplusCons.WHETHER_NO;
		if (BussCons.ORDER_TYPE_PARENT.equals(order.getOrderType())) {
			whehterParentOrder = WebplusCons.WHETHER_YES;
		}
		List<OrderLine> orderLineList = this.getOrderLineList(orderId, whehterParentOrder);
		order = this.getOrder(order, orderLineList, WebplusCons.WHETHER_YES);
		if(BussCons.PRINT_BILL.equals(receiptType)) {
			printAsyncTask.sendPosOrderPrint(order, order.getDataList());
		}else {
			Shop shop=shopService.selectById(order.getShopId());
			printAsyncTask.sendReceiptPrint(order.getTotalAmount(),order.getConsumeTax(),shop);
		}
		return R.ok();
	}
	
	/**
	 * 
	 * 简要说明：保存钱箱记录
	 * 编写者：陈骑元（chenqiyuan@toonan.com）
	 * 创建时间： 2020年7月15日 下午10:37:01 
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("saveCashboxRecord")
	@ResponseBody
	public R saveCashboxRecord(Integer amount,String recordType) {
		String shopId=this.getUserId();
		CashboxRecord cashboxRecord=new CashboxRecord();
		cashboxRecord.setAmount(amount);
		cashboxRecord.setRecordType(recordType);
		cashboxRecord.setShopId(shopId);
		cashboxRecord.setCreateTime(WebplusUtil.getDateTime());
		boolean result=cashboxRecordService.insert(cashboxRecord);
		if(result) {
			
			return R.ok();
		}
		return R.error();
	}
	/**
	 * 
	 * 简要说明：初金、入金、出金
	 * 编写者：陈骑元（chenqiyuan@toonan.com）
	 * 创建时间： 2020年7月18日 下午4:50:41 
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("querySumCashbox")
	@ResponseBody
	public R querySumCashbox(String beginDate,String endDate) {
		String queryDate=WebplusUtil.getDateStr();
		String shopId = this.getUserId();
		Dto pDto = Dtos.newDto();
		pDto.put("shopId", shopId);
		Shop shop = shopService.selectById(shopId);
		if (WebplusUtil.isNotEmpty(shop)) {
			String timeReportStr = shop.getTimeReport();
			if (BussCons.TIME_REPORT_DEFAULT.equals(timeReportStr)) {
				pDto.put("queryDate", queryDate);
			} else {
				String currentDate = WebplusUtil.getDateStr();
				if (queryDate.equals(currentDate)) {
					queryDate = BussUtil.getReportDate(timeReportStr);
				}
				String tomorrowDate = WebplusUtil.plusDay(1, queryDate);
				String beginTime = queryDate + " " + timeReportStr;
				String endTime = tomorrowDate + " " + timeReportStr;
				pDto.put("beginTime", beginTime);
				pDto.put("endTime", endTime);
			}
		} else {
			pDto.put("queryDate", queryDate);
		}
		pDto.put("queryDate", queryDate); 
		pDto.put("shopId", shopId);
		List<String> orderTypeList = Lists.newArrayList();
		orderTypeList.add(BussCons.ORDER_TYPE_COMMON);
		orderTypeList.add(BussCons.ORDER_TYPE_PARENT);
		orderTypeList.add(BussCons.ORDER_TYPE_SIGNLE);
		pDto.put("orderTypeList", orderTypeList);
		int subMinute=0;
		if(WebplusUtil.isNotEmpty(shop)){
			String timeReport=shop.getTimeReport();
			subMinute=BussUtil.countHourTime(timeReport);
		}
		pDto.put("subMinute",subMinute);
		OrderSum cashBoxSum=bussCommonService.querySumCashbox(pDto);
		OrderSum orderSum=bussCommonService.queryOrderSummary(pDto);
		Dto dataDto=Dtos.newDto();
		if(WebplusUtil.isNotEmpty(cashBoxSum)) {
			dataDto.put("inAmount", cashBoxSum.getInAmount());
			dataDto.put("outAmount",cashBoxSum.getOutAmount());
			dataDto.put("beginAmount", cashBoxSum.getBeginAmount());
		}else {
			dataDto.put("inAmount", 0);
			dataDto.put("outAmount", 0);
			dataDto.put("beginAmount", 0);
		}
		if(WebplusUtil.isNotEmpty(orderSum)) {
			dataDto.put("totalAmount", orderSum.getTotalAmount());
			dataDto.put("payOrderCount", orderSum.getPayOrderCount());
			dataDto.put("cashTotalAmount", orderSum.getCashTotalAmount());
			dataDto.put("cardTotalAmount", orderSum.getCardTotalAmount());
			dataDto.put("otherTotalAmount", orderSum.getOtherTotalAmount());
		}else {
			dataDto.put("totalAmount", 0);
			dataDto.put("payOrderCount", 0);
			dataDto.put("cashTotalAmount",0);
			dataDto.put("cardTotalAmount", 0);
			dataDto.put("otherTotalAmount", 0);
		}	
		return R.toData(dataDto);
	}
	/**
	 * 
	 * 简要说明：自定义打印
	 * 编写者：陈骑元（chenqiyuan@toonan.com）
	 * 创建时间： 2020年7月16日 下午11:05:03 
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("pushCustomPrint")
	@ResponseBody
	public R pushCustomPrint(Integer amount,Integer num) {
		String shopId=this.getUserId();
		Shop shop=shopService.selectById(shopId);
		Integer consumeTax=BussUtil.countTaxes(0, amount, BussCons.EAT_TAXES);
		for(int i=0;i<num;i++) {
			printAsyncTask.sendReceiptPrint(amount, consumeTax, shop);
		}
		
		return R.ok();
	}
	/**
	 * 
	 * 简要说明：查询今天汇总
	 * 编写者：陈骑元（chenqiyuan@toonan.com）
	 * 创建时间： 2020年7月18日 下午10:42:34 
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("queryTotaySum")
	@ResponseBody
	public R queryTotaySum() {
		String shopId=this.getUserId();
		String queryDate=WebplusUtil.getCurrentDate();
		Dto pDto=Dtos.newDto();
		List<String> orderTypeList = Lists.newArrayList();
		orderTypeList.add(BussCons.ORDER_TYPE_COMMON);
		orderTypeList.add(BussCons.ORDER_TYPE_PARENT);
		orderTypeList.add(BussCons.ORDER_TYPE_SIGNLE);
		pDto.put("orderTypeList", orderTypeList);
		pDto.put("queryDate", queryDate);
		pDto.put("shopId", shopId);
		int subMinute=0;
		Shop shop=shopService.selectById(shopId);
		if(WebplusUtil.isNotEmpty(shop)){
			String timeReport=shop.getTimeReport();
			subMinute=BussUtil.countHourTime(timeReport);
		}
		pDto.put("subMinute",subMinute);
		OrderSum orderSum=bussCommonService.queryOrderSummary(pDto);
		Dto dataDto=Dtos.newDto();
		if(WebplusUtil.isNotEmpty(orderSum)) {
			dataDto.put("orderCount", orderSum.getOrderCount());
			dataDto.put("payOrderCount", orderSum.getPayOrderCount());
			dataDto.put("totalAmount", orderSum.getTotalAmount());
			dataDto.put("predictAmount", orderSum.getPredictAmount());
			dataDto.put("eatNum", orderSum.getEatNum());
		}else {
			dataDto.put("orderCount", 0);
			dataDto.put("payOrderCount",0);
			dataDto.put("totalAmount", 0);
			dataDto.put("predictAmount", 0);
			dataDto.put("eatNum", 0);
		}
		
		return R.toData(dataDto);
		
	}
	/**
	 * 
	 * 简要说明：打开钱箱
	 * 编写者：陈骑元（chenqiyuan@toonan.com）
	 * 创建时间： 2020年7月26日 下午1:47:40 
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("openCashbox")
	@ResponseBody
	public R openCashbox() {
		String shopId=this.getUserId();
		EntityWrapper<Printer> wrapper=new EntityWrapper<Printer>();
		wrapper.eq("shop_id", shopId);
		wrapper.eq("whether_pos", WebplusCons.WHETHER_YES);
		Printer printer=printerService.selectOne(wrapper);
		if(WebplusUtil.isNotEmpty(printer)) {
			String printNum=printer.getPrintNum();
			if(WebplusUtil.isNotEmpty(printNum)){
				
				return FzCloudUtil.sendOpenCashbox(printNum, printer.getGatewayType());
			}
		}
		return R.error();
				
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
			String timeWay,String queryTime,String beginMinute,String endMinute,String queryMonth,String queryWeek,
			String currentPage,String pageSize) {
		Dto pDto=Dtos.newPage(currentPage, pageSize);
		String shopId=this.getUserId();
		pDto.put("shopId", shopId);
		if(BussCons.CATALOG_WAY_LARGE.equals(catalogWay)) {
			 pDto.put("largeId", catalogId);
		}else if(BussCons.CATALOG_WAY_CATALOG.equals(catalogWay)) {
			 pDto.put("catalogIndexId", catalogId);
		}else if(BussCons.CATALOG_WAY_DICT.equals(catalogWay)) {
		   pDto.put("menuIndexId", catalogId);
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
		Page<OrderLineSum> page=bussCommonService.listOrderLineSumPage(pDto);
		return R.toApiPage(page);
	}
	/**
	 * 
	 * 简要说明：获取自助餐判断
	 * 编写者：陈骑元（chenqiyuan@toonan.com）
	 * 创建时间： 2020年7月13日 下午9:57:11 
	 * @param 说明
	 * @return 说明
	 */
	private List<Desk> getBuffetDeskList(List<Desk> deskList){
		List<Desk> deskListNew=Lists.newArrayList();
		for(Desk desk:deskList) {
			String orderId=desk.getOrderNo();
			if(WebplusUtil.isNotEmpty(orderId)) {
				Dto buffetDto=this.getBuffetTimeLimit(orderId);
				if(WebplusUtil.isNotEmpty(buffetDto)) {
					desk.setWhetherBuffet(WebplusCons.WHETHER_YES);
					desk.setTimeLimit(buffetDto.getInteger("timeLimit"));
					desk.setBuffetTime(buffetDto.getString("orderTime"));
				}else {
					desk.setWhetherBuffet(WebplusCons.WHETHER_NO);
					desk.setTimeLimit(0);
					desk.setBuffetTime("");
				}
			}else {
				desk.setWhetherBuffet(WebplusCons.WHETHER_NO);
				desk.setTimeLimit(0);
				desk.setBuffetTime("");
			}
			deskListNew.add(desk);
		}
		
		return deskListNew;
	}
	/**
	 * 
	 * 简要说明：获取自助餐的菜品列表
	 * 编写者：陈骑元（chenqiyuan@toonan.com）
	 * 创建时间： 2020年7月19日 上午11:38:49 
	 * @param 说明
	 * @return 说明
	 */
    public R getBuffetOrder(Order order) {
    	String whetherParentOrder = WebplusCons.WHETHER_NO;
		if (BussCons.ORDER_TYPE_PARENT.equals(order.getOrderType())) {
			whetherParentOrder = WebplusCons.WHETHER_YES;
		}
		String orderId=order.getOrderId();
		List<OrderLine> orderLineList=this.getOrderLineListByDB(orderId, whetherParentOrder);
		List<OrderLine> buffetList=this.getBuffetList(orderLineList);
		List<OrderLine> orderLineListNew= this.getOrderLineList(orderLineList);
		order=this.getOrder(order, orderLineListNew, WebplusCons.WHETHER_YES);
		String whetherBuffet=WebplusCons.WHETHER_NO;
		
		if(WebplusUtil.isNotEmpty(buffetList)) {
			whetherBuffet=WebplusCons.WHETHER_YES;
			List<OrderLine> buffetOrderLineList=this.getBufferOrderLine(orderLineList);
		    order.getDataList().addAll(buffetOrderLineList);
			//order.setDataList(orderLineListNew);
			Dto catalogDto=Dtos.newDto();
			String key=BussCons.BUFFET_ORDER_KEY+orderId;
			String value=WebplusCache.getString(key);
			if(WebplusUtil.isNotEmpty(value)) {
				Dto limitDto=WebplusJson.fromJson(value, HashDto.class);
				catalogDto.putAll(limitDto);
			
	        }
		    	
			return R.toData(order).put("whetherBuffet", whetherBuffet).put("buffetOrderLineList", buffetOrderLineList).put("buffet", catalogDto);
		}else {
			
			return R.toData(order).put("whetherBuffet", whetherBuffet);
		}
		
		
    }
    /**
     * 
     * 简要说明：去掉自助餐的菜品
     * 编写者：陈骑元（chenqiyuan@toonan.com）
     * 创建时间： 2020年7月19日 下午4:32:31 
     * @param 说明
     * @return 说明
     */
    public List<OrderLine> removeBuffetOrderLine(List<OrderLine> orderLineList){
    	List<OrderLine> orderLineListNew=Lists.newArrayList();
    	 for(OrderLine orderLine:orderLineList) {
    		 String menuIndexId=orderLine.getMenuIndexId();
    		 String whetherBuffet=orderLine.getWhetherBuffet();
    		 if(WebplusUtil.isNotEmpty(menuIndexId)&&WebplusCons.WHETHER_NO.equals(whetherBuffet)){
    			 orderLineListNew.add(orderLine);
					
			 }
    	 }
    	 
    	 return orderLineListNew;
    }
    /**
     * 
     * 简要说明：自助餐
     * 编写者：陈骑元（chenqiyuan@toonan.com）
     * 创建时间： 2020年7月19日 下午4:15:21 
     * @param 说明
     * @return 说明
     */
    public List<OrderLine> getBuffetList(List<OrderLine> orderLineList){
    	List<OrderLine> buffetList=Lists.newArrayList();
    	 for(OrderLine orderLine:orderLineList) {
    		 String menuIndexId=orderLine.getMenuIndexId();
    		 String whetherBuffet=orderLine.getWhetherBuffet();
    		 if(WebplusUtil.isEmpty(menuIndexId)&&WebplusCons.WHETHER_YES.equals(whetherBuffet)){
					buffetList.add(orderLine);
					
			 }
    	 }
    	 return buffetList;
    }
    /**
     * 
     * 简要说明：获取自助餐的菜品
     * 编写者：陈骑元（chenqiyuan@toonan.com）
     * 创建时间： 2020年7月19日 下午3:52:20 
     * @param 说明
     * @return 说明
     */
    public List<OrderLine> getBufferOrderLine(List<OrderLine> orderLineList) {
         List<OrderLine> buffetOrderLineList=Lists.newArrayList();
    	 for(OrderLine orderLine:orderLineList) {
    		 String menuIndexId=orderLine.getMenuIndexId();
    		 String whetherBuffet=orderLine.getWhetherBuffet();
    		 if(WebplusUtil.isNotEmpty(menuIndexId)&&WebplusCons.WHETHER_YES.equals(whetherBuffet)){
					buffetOrderLineList.add(orderLine);
					
			 }
    	 }
    	 
    	 return buffetOrderLineList;
    }
	/**
	 * 
	 * 简要说明：获取自助餐用餐时长
	 * 编写者：陈骑元（chenqiyuan@toonan.com）
	 * 创建时间： 2020年7月13日 下午9:53:33 
	 * @param 说明
	 * @return 说明
	 */
	public Dto getBuffetTimeLimit(String orderId) {
		if(WebplusUtil.isNotEmpty(orderId)) {
			String key=BussCons.BUFFET_ORDER_KEY+orderId;
			String value=WebplusCache.getString(key);
			if(WebplusUtil.isNotEmpty(value)) {
				Dto limitDto=WebplusJson.fromJson(value, HashDto.class);
				
		        return limitDto;
			}
		}
		
		return  Dtos.newDto();
	}
	/**
	 * 
	 * 简要说明：发送打印日报
	 * 编写者：陈骑元（chenqiyuan@toonan.com）
	 * 创建时间： 2020年10月1日 下午6:49:01 
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("sendPrintDateReport")
	@ResponseBody
	public R sendPrintDateReport() {
		
		String  queryDate = WebplusUtil.getDateStr();
		
		String shopId = this.getUserId();
		Dto pDto = Dtos.newDto();
		pDto.put("shopId", shopId);
		Shop shop = shopService.selectById(shopId);
		if (WebplusUtil.isNotEmpty(shop)) {
			String timeReportStr = shop.getTimeReport();
			if (BussCons.TIME_REPORT_DEFAULT.equals(timeReportStr)) {
				pDto.put("queryDate", queryDate);
			} else {
				String currentDate = WebplusUtil.getDateStr();
				if (queryDate.equals(currentDate)) {
					queryDate = BussUtil.getReportDate(timeReportStr);
				}
				String tomorrowDate = WebplusUtil.plusDay(1, queryDate);
				String beginTime = queryDate + " " + timeReportStr;
				String endTime = tomorrowDate + " " + timeReportStr;
				pDto.put("beginTime", beginTime);
				pDto.put("endTime", endTime);
			}
		} else {
			pDto.put("queryDate", queryDate);
		}
		 
		pDto.put("whetherPay", WebplusCons.WHETHER_YES);
		List<String> orderTypeList = Lists.newArrayList();
		orderTypeList.add(BussCons.ORDER_TYPE_COMMON);
		orderTypeList.add(BussCons.ORDER_TYPE_PARENT);
		orderTypeList.add(BussCons.ORDER_TYPE_SIGNLE);
		int subMinute=0;
		if(WebplusUtil.isNotEmpty(shop)){
			String timeReport=shop.getTimeReport();
			subMinute=BussUtil.countHourTime(timeReport);
		}
		pDto.put("subMinute",subMinute);
		pDto.put("orderTypeList", orderTypeList);
		OrderSum orderSum = bussCommonService.queryOrderSumNew(pDto);
		if (WebplusUtil.isEmpty(orderSum)) {
			orderSum = new OrderSum();
		}
		List<OrderLine> orderLineList = bussCommonService.listSumOrderLine(pDto);
		List<OrderLine> orderLineListNew = Lists.newArrayList();
		int tenMenuAmount = 0;
		int eightMenuAmount = 0;
		for (OrderLine orderLine : orderLineList) {
			String menuIndexId = orderLine.getMenuIndexId();
			String whetherBuffet = orderLine.getWhetherBuffet();
			if (WebplusCons.WHETHER_NO.equals(whetherBuffet) || WebplusUtil.isEmpty(menuIndexId)) {
				Integer buyNum = BussUtil.dealEmptyAmount(orderLine.getBuyNum());
				Integer menuPrice = BussUtil.dealEmptyAmount(orderLine.getMenuPrice());
				Integer menuSumPrice = menuPrice * buyNum;
				if (WebplusCons.WHETHER_YES.equals(orderLine.getWhetherTakeOut())) {
					eightMenuAmount += menuSumPrice;
				} else {
					tenMenuAmount += menuSumPrice;
				}
				orderLine.setMenuSumPrice(menuSumPrice);
				orderLineListNew.add(orderLine);
			}

		}
		 tenMenuAmount+=orderSum.getDeskAmount();
		int tenTaxAmount = BussUtil.countTaxes(0, tenMenuAmount, BussCons.EAT_TAXES);
		orderSum.setOrderDate(queryDate);
		orderSum.setTenMenuAmount(tenMenuAmount);
		orderSum.setEightMenuAmount(eightMenuAmount);
		orderSum.setTenTaxAmount(tenTaxAmount);
		List<Dto> dataList = this.getSumOrderLine(orderLineListNew);
		printAsyncTask.sendDateReportPrint(shopId, orderSum, dataList);
		return R.ok();
	}
	

}
