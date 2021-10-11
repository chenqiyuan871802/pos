package com.ims.buss.asyncTask;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.ims.buss.constant.BussCons;
import com.ims.buss.model.MenuCatalog;
import com.ims.buss.model.Order;
import com.ims.buss.model.OrderLine;
import com.ims.buss.model.OrderSum;
import com.ims.buss.model.PrintLog;
import com.ims.buss.model.Printer;
import com.ims.buss.model.Shop;
import com.ims.buss.print.FzCloudUtil;
import com.ims.buss.service.MenuCatalogService;
import com.ims.buss.service.OrderLineService;
import com.ims.buss.service.PrintLogService;
import com.ims.buss.service.PrinterService;
import com.ims.buss.service.ShopService;
import com.ims.buss.util.BussUtil;
import com.ims.core.cache.WebplusCache;
import com.ims.core.constant.WebplusCons;
import com.ims.core.matatype.Dto;
import com.ims.core.util.WebplusFormater;
import com.ims.core.util.WebplusUtil;
import com.ims.core.vo.Item;
import com.ims.core.vo.R;

/**
 * 
 * 类名:com.ims.buss.asyncTask.PrintAsyncTask 描述:打印异步任务处理 编写者:陈骑元 创建时间:2019年8月4日
 * 下午3:56:39 修改说明:
 */
@Component
public class PrintAsyncTask {

	private static Log log = LogFactory.getLog(PrintAsyncTask.class);
	/**
	 * 打印异步任务处理
	 */
	@Autowired
	private OrderLineService orderLineService;

	@Autowired
	private MenuCatalogService menuCatalogService;
    
	@Autowired
	private PrinterService printerService;
	@Autowired
	private PrintLogService printLogService;
	
	@Autowired
	private ShopService shopService;
	
	
	@Async
	public void sendTakeOutOrderPrint(Order order,List<OrderLine> lineList) {
		String shopId=order.getShopId();
		EntityWrapper<Printer> wrapper=new EntityWrapper<Printer>();
		wrapper.eq("shop_id", shopId);
		wrapper.eq("whether_server", WebplusCons.WHETHER_YES);
		Printer printer=printerService.selectOne(wrapper);
		if(WebplusUtil.isNotEmpty(printer)){
			String printNum=printer.getPrintNum();
			if(WebplusUtil.isNotEmpty(printNum)){
				String message=this.getTakeOutPrintMessage(order, lineList);
				boolean result=FzCloudUtil.sendPrint(printNum, message,printer.getGatewayType());
				if (result) {
					
					log.info("发送飞猪云打印机终端号（" + printNum + ")打印（订单号=" + order.getOrderId() + ",外卖订单信息="
							+ message + ")服务打印成功");
				}else{
					log.error("发送飞猪云打印机终端号（" + printNum + ")打印（订单号=" + order.getOrderId() + ",外卖订单信息="
							+ message + ")服务打印失败");
					
				}
				return ;
			}
		}
		log.error("发送飞猪云打印机失败，无法找到相关打印机信息打印(订单号=" + order.getOrderId()+")外卖订单信息打印失败");
	}
	/**
	 * 
	 * 简要说明：POS打印机
	 * 编写者：陈骑元（chenqiyuan@toonan.com）
	 * 创建时间： 2020年7月29日 下午11:04:24 
	 * @param 说明
	 * @return 说明
	 */
	@Async
	public void sendPosOrderPrint(Order order,List<OrderLine> lineList) {
		String shopId=order.getShopId();
		EntityWrapper<Printer> wrapper=new EntityWrapper<Printer>();
		wrapper.eq("shop_id", shopId);
		wrapper.eq("whether_pos", WebplusCons.WHETHER_YES);
		Printer printer=printerService.selectOne(wrapper);
		if(WebplusUtil.isNotEmpty(printer)){
			String printNum=printer.getPrintNum();
			if(WebplusUtil.isNotEmpty(printNum)){
				String message=this.getPosOrderPrintMessage(order, lineList);
				boolean result=FzCloudUtil.sendPrint(printNum, message,printer.getGatewayType());
				if (result) {
					
					log.info("发送飞猪云打印机终端号（" + printNum + ")打印（订单号=" + order.getOrderId() + ",POS订单信息="
							+ message + ")服务打印成功");
				}else{
					log.error("发送飞猪云打印机终端号（" + printNum + ")打印（订单号=" + order.getOrderId() + ",POS订单信息="
							+ message + ")服务打印失败");
					
				}
				return ;
			}
		}
		log.error("发送飞猪云打印机失败，无法找到相关打印机信息打印(订单号=" + order.getOrderId()+")POS订单信息打印失败");
	}
	
	/**
	 * 
	 * 简要说明：POS打印机
	 * 编写者：陈骑元（chenqiyuan@toonan.com）
	 * 创建时间： 2020年7月29日 下午11:04:24 
	 * @param 说明
	 * @return 说明
	 */
	@Async
	public void sendReceiptPrint(Integer totalAmount,Integer consumeTax,Shop shop) {
		String shopId=shop.getShopId();
		EntityWrapper<Printer> wrapper=new EntityWrapper<Printer>();
		wrapper.eq("shop_id", shopId);
		wrapper.eq("whether_pos", WebplusCons.WHETHER_YES);
		Printer printer=printerService.selectOne(wrapper);
		if(WebplusUtil.isNotEmpty(printer)){
			String printNum=printer.getPrintNum();
			if(WebplusUtil.isNotEmpty(printNum)){
				String message=this.getReceiptMessage(totalAmount, consumeTax, shop);
				boolean result=FzCloudUtil.sendPrint(printNum, message,printer.getGatewayType());
				if (result) {
					
					log.info("发送飞猪云打印机终端号（" + printNum + ")打印小票,小票信息="
							+ message + ")服务打印成功");
				}else{
					log.error("发送飞猪云打印机终端号（" + printNum + ")打印小票,小票信息="
							+ message + ")服务打印失败");
					
				}
				return ;
			}
		}
		log.error("发送飞猪云打印机失败，无法找到相关打印机信息打印小票失败");
	}
	/**
	 * 
	 * 简要说明：获取小票信息
	 * 编写者：陈骑元（chenqiyuan@toonan.com）
	 * 创建时间： 2020年8月2日 下午8:58:26 
	 * @param 说明
	 * @return 说明
	 */
	public String getReceiptMessage(Integer totalAmount,Integer consumeTax,Shop shop) {
		String cureateDate=WebplusUtil.getDateStr("yyyy年MM月dd日");
		String totalAmountStr=BussUtil.formatAmount(totalAmount);
		String consumeTaxStr=BussUtil.formatAmount(consumeTax);
		StringBuffer sb=new StringBuffer();
		sb.append("<center><FS><FB>領収書</FB></FS>\n");
		sb.append("\n");
		sb.append("_________________________様\n");
		sb.append("\n");
		sb.append(cureateDate+"\n");
		sb.append("\n");
		sb.append("<FS><FB>").append(totalAmountStr).append("円</FB></FS>\n");
		sb.append("但________________________\n");
		sb.append("上記正に領収いたしました\n");
		sb.append("\n");
		sb.append("<FS><FB>合計：").append(totalAmountStr).append("円</FB></FS>\n");
		sb.append("<FS><FB>内消費税：").append(consumeTaxStr).append("円</FB></FS>\n");
		sb.append("\n");
		sb.append(shop.getShopName()).append("\n");
		sb.append(shop.getShopTelephone()).append("\n");
		sb.append(shop.getShopAddress()).append("\n");
		
		return sb.toString();
	}
	/**
	 * 
	 * 简要说明：获取打印信息
	 * 编写者：陈骑元（chenqiyuan@toonan.com）
	 * 创建时间： 2020年5月9日 下午10:02:08 
	 * @param 说明
	 * @return 说明
	 */
	public String getTakeOutPrintMessage(Order order,List<OrderLine> lineList) {
		StringBuffer sb=new StringBuffer();
		String  shopId=order.getShopId();
		Shop shop=shopService.selectById(shopId);
		String orderType="テイクアウト";
		if(BussCons.ORDER_CATEGORY_DELIVERY.equals(order.getOrderCategory())) {
			orderType="デリバリー";
		}
		sb.append("<center> <FS2>").append(orderType).append("</FS2>").append("\n");
		sb.append("<center>--").append(shop.getShopName()).append("\n");
		sb.append("[注文時間] ").append(WebplusUtil.date2String(order.getOrderTime(), WebplusCons.DATETIMEMIN)).append("\n");
		sb.append("[注文番号] ").append(order.getOrderId()).append("\n");
		sb.append("注文内容").append("\n");
		sb.append("<table:18,24>").append("\n");
	    if(WebplusUtil.isNotEmpty(lineList)) {
	    	for(OrderLine orderLine:lineList) {
	    		
	    		sb.append("<tr>");
    			sb.append("<td>").append(orderLine.getPrintMenuName()).append("</td>");
    			sb.append("<td>").append("x").append(orderLine.getBuyNum()).append("</td>");
    			sb.append("<td>").append(orderLine.getMenuSumPrice()).append("</td>");
    			sb.append("</tr>");
	    	}
	    }
		sb.append("</table>").append("\n");
		sb.append("<FS>[合計]￥").append(order.getTotalAmount()).append("\n");
		sb.append("<FS>[消費税]￥").append(order.getConsumeTax()).append("\n");
		sb.append("<FS>[ポイント]￥").append(BussUtil.dealEmptyAmount(order.getSubPoint())).append("\n");
		sb.append("<FS> [名前]").append(order.getUsername()).append("\n");
		String appendAddress=order.getAppendAddress();
		if(WebplusUtil.isNotEmpty(appendAddress)) {
			sb.append("<FS>[住所]").append(appendAddress).append("\n");
		}else {
			sb.append("<FS>[住所]").append(order.getOrderAddress()).append("\n");
		}
		
		sb.append("<FS>[電話]").append(order.getMobile()).append("\n");
	    Date pickUpTime=order.getPickUpTime();
	    if(WebplusUtil.isNotEmpty(pickUpTime)) {
	    	sb.append("<FS>[指定時間]").append(WebplusUtil.date2String(pickUpTime, WebplusCons.DATETIMEMIN)).append("\n");
	    }else {
	    	sb.append("<FS>[指定時間]").append("").append("\n");
	    }
		String orderRemark=order.getOrderRemark();
		if(WebplusUtil.isEmpty(orderRemark)) {
			orderRemark="";
		}
		sb.append("<FS>[備考]").append(orderRemark).append("\n");
		return sb.toString();
	}
	
	 /** 
	 * 简要说明：获取POS打印信息
	 * 编写者：陈骑元（chenqiyuan@toonan.com）
	 * 创建时间： 2020年5月9日 下午10:02:08 
	 * @param 说明
	 * @return 说明
	 */
	public String getPosOrderPrintMessage(Order order,List<OrderLine> lineList) {
		StringBuffer sb=new StringBuffer();
		String  shopId=order.getShopId();
		Shop shop=shopService.selectById(shopId);
		sb.append("<FB>店舗名： ").append(shop.getShopName()).append("\n");
		sb.append("住所： ").append(shop.getShopAddress()).append("\n");
		sb.append("卓番：  ").append(order.getDeskName()).append("\n");
		sb.append("時間：").append(WebplusUtil.date2String(order.getOrderTime(), WebplusCons.DATETIMEMIN)).append("\n");
		sb.append("注文番号： ").append(order.getOrderId()).append("\n");
		sb.append("<zsplit:58></zsplit:58>\n");
		sb.append("<table:18,24>").append("\n");
	    if(WebplusUtil.isNotEmpty(lineList)) {
	    	if(order.getDeskAmount()>0) {
	    		sb.append("<tr>");
	   			sb.append("<td>").append("お通し").append("</td>");
	   			sb.append("<td>").append("x").append(order.getEatNum()).append("</td>");
	   			sb.append("<td>").append(BussUtil.formatAmount(order.getDeskAmount())).append("円</td>");
	   			sb.append("</tr>");
	    	}
	    	for(OrderLine orderLine:lineList) {
	    	String menuName=orderLine.getMenuName();
	    	sb.append("<tr>");
   			sb.append("<td>").append(menuName).append("</td>");
   			sb.append("<td>").append("x").append(orderLine.getBuyNum()).append("</td>");
   			sb.append("<td>").append(BussUtil.formatAmount(orderLine.getMenuSumPrice())).append("円</td>");
   			sb.append("</tr>");
	    	List<OrderLine> specList=orderLine.getSpecList();
	    	if(WebplusUtil.isNotEmpty(specList)) {
	    		for(OrderLine spec:specList) {
	    			sb.append("<tr>");
	       			sb.append("<td>  ").append(spec.getMenuName()).append("</td>");
	       			sb.append("<td>").append("").append("</td>");
	       			sb.append("<td>").append(BussUtil.formatAmount(spec.getMenuSumPrice())).append("円</td>");
	       			sb.append("</tr>");
	    		}
	    	}
	    		
	    	}
	    }
		sb.append("</table>").append("\n");
		sb.append("<zsplit:58></zsplit:58>\n");
		sb.append("<FB>小計：").append(BussUtil.formatAmount(order.getSmallTotalAmount())).append("円\n");
		int subAmount=order.getHistoryTotalAmount()-order.getTotalAmount();
		if(subAmount>0) {
			sb.append("値引：").append(BussUtil.formatAmount(subAmount)).append("円\n");
		}
		List<Item> itemList=WebplusCache.getItemList("pay_way");
		int cashAmount=0;
		String payWay=order.getPayWay();
		String payWayStr=WebplusCache.getItemName(itemList, payWay);
		if(BussCons.PAY_WAY_CASH.equals(payWay)) {
			cashAmount=BussUtil.dealEmptyAmount(order.getCashAmount());
		}else if(BussCons.PAY_WAY_CARD.equals(payWay)) {
			cashAmount=BussUtil.dealEmptyAmount(order.getCreditAmount());
		}else {
			cashAmount=BussUtil.dealEmptyAmount(order.getOtherAmount());
		}
		sb.append("消費税：").append(BussUtil.formatAmount(order.getConsumeTax())).append("円</FB>\n");
		sb.append("<zsplit:58></zsplit:58>\n");
		sb.append("<FB>合計：").append(BussUtil.formatAmount(order.getTotalAmount())).append("円\n");
		sb.append("おつり：").append(BussUtil.formatAmount(order.getZeroAmount())).append("円\n");
		sb.append("お預かり：").append(BussUtil.formatAmount(cashAmount)).append("円\n");
		sb.append("支払方法：").append(payWayStr).append("\n");
		sb.append("ポイント利用：").append(BussUtil.dealEmptyAmount(order.getPlusPoint())).append("</FB>");
		return sb.toString();
	}
	
	/**
	 * 
	 * 简要说明：
	 * 编写者：陈骑元（chenqiyuan@toonan.com）
	 * 创建时间： 2020年9月30日 下午10:11:23 
	 * @param 说明
	 * @return 说明
	 */
	private String getSpecMenuName(List<OrderLine> specList){
		
		 String menuName="";
		 for(OrderLine spec:specList) {
			 menuName+="■"+spec.getMenuName();
		 }
		 return menuName;
	}
	/**
	 * 
	 * 简要说明：打印推送服务
	 * 编写者：陈骑元
	 * 创建时间：2020年1月15日 下午11:03:59
	 * @param 说明
	 * @return 说明
	 */
	@Async
	public void sendMessagePrint(String shopId,String deskName,String message){
		EntityWrapper<Printer> wrapper=new EntityWrapper<Printer>();
		wrapper.eq("shop_id", shopId);
		wrapper.eq("whether_server", WebplusCons.WHETHER_YES);
		Printer printer=printerService.selectOne(wrapper);
		if(WebplusUtil.isNotEmpty(printer)){
			String printNum=printer.getPrintNum();
			if(WebplusUtil.isNotEmpty(printNum)){
				boolean result=FzCloudUtil.sendPrint(printNum, message,printer.getGatewayType());
				if (result) {
					
					log.info("发送飞猪云打印机终端号（" + printNum + ")打印（" + deskName + ")呼叫（"
							+ message + ")服务成功");
				}else{
					log.error("发送飞猪云打印机终端号（" + printNum + ")打印（" + deskName + ")呼叫（"
							+ message + ")服务失败");
					return ;
				}
			}
			
		}
		log.error("发送飞猪云打印机失败，无法找到相关打印机信息打印(" + deskName + ")呼叫（"
				+ message + ")服务失败");
	}
	/**
	 * 
	 * 简要说明：批量异步发送打印 编写者：陈骑元 创建时间：2019年8月4日 下午4:01:29
	 * 
	 * @param 说明
	 * @return 说明
	 */
	@Async
	public void batchSendPrintMenu(String deskName, List<OrderLine> orderLineList) {
		if (WebplusUtil.isNotEmpty(orderLineList)) {
			List<String> catalogIdList = this.getMenuCatalogId(orderLineList);

			if (WebplusUtil.isNotEmpty(catalogIdList)) {
				List<MenuCatalog> menuCatalogList = menuCatalogService.selectBatchIds(catalogIdList);
				if (WebplusUtil.isNotEmpty(menuCatalogList)) {
					List<OrderLine> orderLineListNew = this.getPrintOrderLineList(orderLineList);
					for (OrderLine orderLine : orderLineListNew) {
						MenuCatalog menuCatalog = this.getMenuCatalog(orderLine.getPrintCatalogIndexId(),
								menuCatalogList);
						if (WebplusUtil.isNotEmpty(menuCatalog)) {
							String printNum = menuCatalog.getPrintNum();
							if (WebplusUtil.isNotEmpty(printNum)) { // 进行推送
								List<String> printNumList = WebplusFormater.separatStringToList(printNum);
								for (String printNumSend : printNumList) {
									String printMenuName = orderLine.getPrintMenuName();
									if (WebplusCons.WHETHER_YES.equals(orderLine.getWhetherBuffet())) {
										printMenuName = "（放）" + printMenuName;
									}
							     this.sendPrintBase(printNumSend, orderLine.getShopId(),
											orderLine.getOrderId(), deskName, printMenuName, orderLine.getBuyNum());
								}

								String lineId = orderLine.getLineId();
								if (WebplusUtil.isNotEmpty(lineId)) {
									OrderLine entity = new OrderLine();
									entity.setLineId(lineId);
									entity.setWhetherPrint(WebplusCons.WHETHER_YES);
									orderLineService.updateById(entity);
								}

							}

						}
					}

				}
			}
		}
	}
	/**
	 * 
	 * 简要说明：发送打印机基本入口
	 * 编写者：陈骑元
	 * 创建时间：2020年1月17日 下午11:52:47
	 * @param 说明
	 * @return 说明
	 */
	public boolean sendPrintBase(String printNum,String shopId,String orderId,String deskName,String printMenuName,Integer buyNum){
		String content = BussUtil.createFzPrintMenuContent(deskName, buyNum, printMenuName);
		String gatewayType=this.getGatewayType(printNum);
		R r = FzCloudUtil.sendPrintMessage(printNum, content,gatewayType);
		if(WebplusCons.SUCCESS==r.getAppCode()){
			log.info("发送打印机终端号（" + printNum + ")打印（订单号="
					+ orderId + ",桌号=" + deskName + ")菜单（"
					+ printMenuName + ")份数(" +buyNum+ "份)成功");
			return true;
		}else{
			String errorMessage=r.getString("errorMessage");
			log.error("发送 打印机终端号（" + printNum + ")打印（订单号="+ orderId + ",桌号=" + deskName + ")菜单（"
					+ printMenuName + ")份数(" +buyNum+ "份)失败,错误异常="+errorMessage);
			PrintLog printLog=new PrintLog();
			String logId=WebplusUtil.uuid();
			printLog.setLogId(logId);
			printLog.setShopId(shopId);
			printLog.setPrintNum(printNum);
			printLog.setOrderId(orderId);
			printLog.setDeskName(deskName);
			printLog.setPrintMenuName(printMenuName);
			printLog.setBuyNum(buyNum);
			printLog.setLogStatus(WebplusCons.ERROR+"");
			printLog.setErrorMessage(errorMessage);
			printLog.setErrorCount(1);
			printLog.setLogTime(WebplusUtil.getDateTime());
			printLogService.insert(printLog);  //插入日志
			//进行补发
			r = FzCloudUtil.sendPrintMessage(printNum, content,gatewayType);
			if(WebplusCons.SUCCESS==r.getAppCode()){
				log.info("补发打印机终端号（" + printNum + ")打印（订单号="+ orderId + ",桌号=" + deskName + ")菜单（"
						+ printMenuName + ")份数(" +buyNum+ "份)成功");
				//第二次补发成功
				PrintLog updatePrintLog=new PrintLog();
				updatePrintLog.setLogId(logId);
				updatePrintLog.setLogStatus(WebplusCons.SUCCESS+"");
				updatePrintLog.setPrintId(r.getString("printId"));
				updatePrintLog.setUpdateTime(WebplusUtil.getDateTime());
				printLogService.updateById(updatePrintLog);
				return true;
			}else{
				errorMessage=r.getString("errorMessage");
				log.error("重新补发发送 打印机终端号（" + printNum + ")打印（订单号="+ orderId + ",桌号=" + deskName + ")菜单（"
						+ printMenuName + ")份数(" +buyNum+ "份)失败,错误异常="+errorMessage);
				PrintLog updatePrintLog=new PrintLog();
				updatePrintLog.setLogId(logId);
				updatePrintLog.setPrintId(r.getString("printId"));
				updatePrintLog.setUpdateTime(WebplusUtil.getDateTime());
				updatePrintLog.setErrorCount(2);
				updatePrintLog.setErrorMessage("重新补发异常:"+errorMessage);
				printLogService.updateById(updatePrintLog);
				return false;
			}
			
		}
		
	}
	/**
	 * 
	 * 简要说明：重发失败打印
	 * 编写者：陈骑元
	 * 创建时间：2020年1月17日 下午11:52:47
	 * @param 说明
	 * @return 说明
	 */
	@Async
	public void  resendPrint(PrintLog printLog){
	    String deskName=printLog.getDeskName();
	    Integer buyNum=printLog.getBuyNum();
	    String printMenuName=printLog.getPrintMenuName();
	    String printNum=printLog.getPrintNum();
	    String orderId=printLog.getOrderId();
	    String logId=printLog.getLogId();
		String content = BussUtil.createFzPrintMenuContent(deskName, buyNum, printMenuName);
		String gatewayType=this.getGatewayType(printNum);
	
		R r = FzCloudUtil.sendPrintMessage(printNum, content,gatewayType);
		if(WebplusCons.SUCCESS==r.getAppCode()){
			log.info("重新发送打印机终端号（" + printNum + ")打印（订单号="
					+ orderId + ",桌号=" + deskName + ")菜单（"
					+ printMenuName + ")份数(" +buyNum+ "份)成功，第三次");
			PrintLog updatePrintLog=new PrintLog();
			updatePrintLog.setLogId(logId);
			updatePrintLog.setPrintId(r.getString("printId"));
			updatePrintLog.setUpdateTime(WebplusUtil.getDateTime());
			updatePrintLog.setLogStatus(WebplusCons.SUCCESS+"");
			printLogService.updateById(updatePrintLog);
		}else{
			String errorMessage=r.getString("errorMessage");
			log.error("重新补发发送第三次 打印机终端号（" + printNum + ")打印（订单号="+ orderId + ",桌号=" + deskName + ")菜单（"
					+ printMenuName + ")份数(" +buyNum+ "份)失败,错误异常="+errorMessage);
			PrintLog updatePrintLog=new PrintLog();
			updatePrintLog.setLogId(logId);
			updatePrintLog.setPrintId(r.getString("printId"));
			updatePrintLog.setUpdateTime(WebplusUtil.getDateTime());
			updatePrintLog.setErrorCount(3);
			updatePrintLog.setErrorMessage("第三次重新补发异常:"+errorMessage);
			printLogService.updateById(updatePrintLog);
			
			//进行补发
			r = FzCloudUtil.sendPrintMessage(printNum, content,gatewayType);
			if(WebplusCons.SUCCESS==r.getAppCode()){
				log.info("重新补发打印机终端号（" + printNum + ")打印（订单号="+ orderId + ",桌号=" + deskName + ")菜单（"
						+ printMenuName + ")份数(" +buyNum+ "份)成功，第四次终于成功");
				//第三次补发成功
				PrintLog updateEntity=new PrintLog();
				updateEntity.setLogId(logId);
				updateEntity.setLogStatus(WebplusCons.SUCCESS+"");
				updateEntity.setPrintId(r.getString("printId"));
				updateEntity.setUpdateTime(WebplusUtil.getDateTime());
				printLogService.updateById(updateEntity);
			}else{
				errorMessage=r.getString("errorMessage");
				log.error("重新补发发送 打印机终端号（" + printNum + ")打印（订单号="+ orderId + ",桌号=" + deskName + ")菜单（"
						+ printMenuName + ")份数(" +buyNum+ "份)失败,错误异常="+errorMessage+"第四次失败");
				PrintLog fourLog=new PrintLog();
				fourLog.setLogId(logId);
				fourLog.setPrintId(r.getString("printId"));
				fourLog.setUpdateTime(WebplusUtil.getDateTime());
				fourLog.setErrorCount(4);
				fourLog.setErrorMessage("第四次重新补发异常:"+errorMessage);
				printLogService.updateById(fourLog);
			}
			
		}
		
	}
	
	/**
	 * 
	 * 简要说明：发送点餐
	 * 编写者：陈骑元
	 * 创建时间：2020年1月13日 下午9:10:56
	 * @param 说明
	 * @return 说明
	 */
	@Async
	public void sendPrintOrderServer(String deskName,List<Dto> orderServerList){
		if(WebplusUtil.isNotEmpty(orderServerList)){
			for(Dto dataDto:orderServerList){
				this.sendSingleOrderServer(deskName, dataDto);
			}
		}
	}
	
	
    public void sendSingleOrderServer(String deskName,Dto dataDto){
    	String printNum = dataDto.getString("printNum");
    	String orderId=dataDto.getString("orderId");
    	String shopId=dataDto.getString("shopId");
		if(WebplusUtil.isNotEmpty(printNum)){
			String printMenuName=dataDto.getString("printMenuName");
			Integer buyNum=dataDto.getInteger("buyNum");
			String whetherBuffet=dataDto.getString("whetherBuffet");
			if(WebplusCons.WHETHER_YES.equals(whetherBuffet)){
				printMenuName="（放）"+printMenuName;
			}
			
		   this.sendPrintBase(printNum, shopId,
					orderId, deskName, printMenuName,buyNum);
			
		}
		
    }
	/**
	 * 
	 * 简要说明：返回对应的菜单类目 编写者：陈骑元 创建时间：2019年8月4日 下午4:13:39
	 * 
	 * @param 说明
	 * @return 说明
	 */
	private List<String> getMenuCatalogId(List<OrderLine> orderLineList) {
		List<String> menuCatalogIdList = Lists.newArrayList();
		for (OrderLine orderLine : orderLineList) {

			String catalogId = orderLine.getPrintCatalogIndexId();
			if (WebplusUtil.isNotEmpty(catalogId)) {
				menuCatalogIdList.add(catalogId);
			}
		}

		return menuCatalogIdList;

	}

	/**
	 * 
	 * 简要说明：返回对应的菜单类目 编写者：陈骑元 创建时间：2019年8月4日 下午4:13:39
	 * 
	 * @param 说明
	 * @return 说明
	 */
	private MenuCatalog getMenuCatalog(String catalogId, List<MenuCatalog> menuCatalogList) {
		for (MenuCatalog menuCatalog : menuCatalogList) {

			if (catalogId.equals(menuCatalog.getCatalogId())) {
				return menuCatalog;
			}
		}

		return null;

	}
	/**
	 * 
	 * 简要说明：获取打印订单列表
	 * 编写者：陈骑元
	 * 创建时间：2019年9月1日 下午6:59:46
	 * @param 说明
	 * @return 说明
	 */
	private List<OrderLine> getPrintOrderLineList(List<OrderLine> orderLineList){
	    List<OrderLine> orderLineListNew=Lists.newArrayList();
	    for(OrderLine orderLine:orderLineList){
	    	String whetherSpec=orderLine.getWhetherSpec();
	    	if(WebplusCons.WHETHER_NO.equals(whetherSpec)||
	    			WebplusUtil.isEmpty(orderLine.getMenuIndexId())){
	    	    String lineId=orderLine.getLineId();
	    	    if(WebplusUtil.isNotEmpty(lineId)){
	    	    	String printSpecName=this.getPrintSpecName(lineId, orderLineList);
	    	    	if(WebplusUtil.isNotEmpty(printSpecName)){
	    	    		String printMenuName=orderLine.getPrintMenuName()+printSpecName;
	    	    		orderLine.setPrintMenuName(printMenuName);
	    	    	}
	    	    	
	    	    }
	    		orderLineListNew.add(orderLine);
	    	}
	    }
	    return orderLineListNew;
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
	 * 简要说明：发送二维码打印
	 * 编写者：陈骑元（chenqiyuan@toonan.com）
	 * 创建时间： 2020年8月2日 下午8:05:46 
	 * @param 说明
	 * @return 说明
	 */
	public void sendDeskQrcodePrint(String shopId,String url,String deskName) {
		EntityWrapper<Printer> wrapper=new EntityWrapper<Printer>();
		wrapper.eq("shop_id", shopId);
		wrapper.eq("whether_pos", WebplusCons.WHETHER_YES);
		Printer printer=printerService.selectOne(wrapper);
		if(WebplusUtil.isNotEmpty(printer)){
			String printNum=printer.getPrintNum();
			if(WebplusUtil.isNotEmpty(printNum)){
				StringBuffer sb=new StringBuffer();
				sb.append("<IMG>").append(url).append("</IMG>\n");
				sb.append("<center><FB>").append(deskName).append("</FB>");
				String message=sb.toString();
				boolean result=FzCloudUtil.sendPrint(printNum, message,printer.getGatewayType());
				if (result) {
					
					log.info("发送飞猪云打印机终端号（" + printNum + ")打印（桌号=" +deskName+ ",二维码信息="
							+ message + ")服务打印成功");
				}else{
					log.error("发送飞猪云打印机终端号（" + printNum + ")打印（桌号=" +deskName+ ",二维码信息="
							+ message + ")服务打印失败");
					
				}
				return ;
			}
		}
		log.error("发送飞猪云打印机失败，无法找到相关打印机信息打印桌号二维码：桌号=" +deskName+ ",二维码信息="+ url );
		
		
	}
	/**
	 * 
	 * 简要说明：获取打印机
	 * 编写者：陈骑元
	 * 创建时间：2020年2月4日 下午12:42:41
	 * @param 说明
	 * @return 说明
	 */
	private Printer getPrinter(String printNum){
		EntityWrapper<Printer> wrapper=new EntityWrapper<Printer>();
		wrapper.eq("print_num", printNum);
		return printerService.selectOne(wrapper);
	}
	/**
	 * 
	 * 简要说明：获取网关类型
	 * 编写者：陈骑元
	 * 创建时间：2020年2月4日 下午12:45:49
	 * @param 说明
	 * @return 说明
	 */
	private String getGatewayType(String printNum){
		Printer printer=this.getPrinter(printNum);
		if(WebplusUtil.isNotEmpty(printer)){
			
			return printer.getGatewayType();
		}
		return "";
	}
	
	/**
	 * 
	 * 简要说明：发送二维码打印
	 * 编写者：陈骑元（chenqiyuan@toonan.com）
	 * 创建时间： 2020年8月2日 下午8:05:46 
	 * @param 说明
	 * @return 说明
	 */
	public void sendDateReportPrint(String shopId,OrderSum orderSum,List<Dto> dataList) {
		EntityWrapper<Printer> wrapper=new EntityWrapper<Printer>();
		wrapper.eq("shop_id", shopId);
		wrapper.eq("whether_pos", WebplusCons.WHETHER_YES);
		Printer printer=printerService.selectOne(wrapper);
		if(WebplusUtil.isNotEmpty(printer)){
			String printNum=printer.getPrintNum();
			if(WebplusUtil.isNotEmpty(printNum)){
				String message=this.getDateReportPrintMessage(orderSum, dataList);
				boolean result=FzCloudUtil.sendPrint(printNum, message,printer.getGatewayType());
				if (result) {
					
					log.info("发送飞猪云打印机终端号（" + printNum + ")打印日报（日报内容="+ message + ")服务打印成功");
				}else{
					log.error("发送飞猪云打印机终端号（" + printNum + ")打印日报（日报内容="+ message + ")服务打印失败");
					
				}
				return ;
			}
		}
		log.error("发送飞猪云打印机失败，无法找到相关打印机信息打印日报");
		
		
	}
	 /** 
		 * 简要说明：获取POS打印信息
		 * 编写者：陈骑元（chenqiyuan@toonan.com）
		 * 创建时间： 2020年5月9日 下午10:02:08 
		 * @param 说明
		 * @return 说明
		 */
		public String getDateReportPrintMessage(OrderSum orderSum,List<Dto> dataList) {
			StringBuffer sb=new StringBuffer();
			sb.append("<FB>日にち：").append(orderSum.getOrderDate()).append("\n");
			sb.append("来客： ").append(orderSum.getDeskNum()).append("人\n");
			sb.append("<zsplit:58></zsplit:58>\n");
			sb.append("売上合計（税込）：").append(BussUtil.formatAmount(orderSum.getTotalAmount())).append("円\n");
			sb.append("消費税：").append(BussUtil.formatAmount(orderSum.getConsumeTax())).append("円\n");
			sb.append("現金： ").append(BussUtil.formatAmount(orderSum.getCashTotalAmount())).append("円\n");
			sb.append("クレジット： ").append(BussUtil.formatAmount(orderSum.getCardTotalAmount())).append("円\n");
			sb.append("その他： ").append(BussUtil.formatAmount(orderSum.getOtherTotalAmount())).append("円\n");
			sb.append("<zsplit:58></zsplit:58>\n");
			sb.append("<table:18,24>").append("\n");
		    if(WebplusUtil.isNotEmpty(dataList)) {
		    	sb.append("<tr>");
	   			sb.append("<td>").append("お通し").append("</td>");
	   			sb.append("<td>").append("x").append(orderSum.getEatNum()).append("</td>");
	   			sb.append("<td>").append(BussUtil.formatAmount(orderSum.getDeskAmount())).append("</td>");
	   			sb.append("</tr>");
		    	for(Dto dataDto:dataList) {
		    	
		    		sb.append("<tr>");
	   			sb.append("<td>").append(dataDto.getString("menuName")).append("</td>");
	   			sb.append("<td>").append("x").append(dataDto.getInteger("buyNum")).append("</td>");
	   			sb.append("<td>").append(BussUtil.formatAmount(dataDto.getInteger("menuSumPrice"))).append("</td>");
	   			sb.append("</tr>");
		    	}
		    }
			sb.append("</table>").append("\n");
			
			return sb.toString();
		}
	
	
	
}
