package com.ims.buss.system;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.ims.core.cache.WebplusCache;
import com.ims.core.constant.WebplusCons;
import com.ims.core.matatype.Dto;
import com.ims.core.matatype.Dtos;
import com.ims.core.util.WebplusFormater;
import com.ims.core.util.WebplusSqlHelp;
import com.ims.core.util.WebplusUtil;
import com.ims.core.vo.R;
import java.util.List;

import com.ims.buss.constant.BussCons;
import com.ims.buss.model.MenuCatalog;
import com.ims.buss.model.Printer;
import com.ims.buss.print.FzCloudUtil;
import com.ims.buss.print.LavUtil;
import com.ims.buss.service.BussCommonService;
import com.ims.buss.service.MenuCatalogService;
import com.ims.buss.service.PrinterService;
import com.ims.buss.util.BussUtil;

import org.springframework.stereotype.Controller;
import com.ims.core.web.BaseController;

/**
 * <p>
 * 打印机表 前端控制器
 * </p>
 *
 * @author 陈骑元
 * @since 2019-07-20
 */
@Controller
@RequestMapping("/buss/printer")
public class PrinterController extends BaseController {

    @Autowired
    private PrinterService printerService;
    /**
     * 清除菜品打印机
     */
    @Autowired
    private MenuCatalogService menuCatalogService;
    /**
     * 通用业务逻辑处理
     */
    @Autowired
    private BussCommonService bussCommonService;

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
		pDto.setOrder(" create_time DESC ");
		Page<Printer> page =printerService.likePage(pDto);
		return R.toPage(page);
	}

	/**
	 * 
	 * 简要说明：查询打印机处理
	 * 编写者：陈骑元
	 * 创建时间：2019-07-20
	 * @param 说明
	 * @return 说明
	 */
	@RequestMapping("listPrinter")
	@ResponseBody
	public R listPrinter() {
		Dto pDto=Dtos.newDto(request);
		pDto.setOrder(" a.shop_id ASC ");
		
		Page<Printer> page =bussCommonService.listPrinterPage(pDto);
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
	public R save(Printer printer) {
		String userId=this.getUserId();
		EntityWrapper<Printer> countWrapper = new EntityWrapper<Printer>();
		WebplusSqlHelp.eq(countWrapper, "print_num", printer.getPrintNum());
		int count=printerService.selectCount(countWrapper);
		if(count>0){
			
			return R.warn("当前打印机已经存在。");
		}
		
		printer.setCreateBy(userId);
		printer.setUpdateBy(userId);
		printer.setCreateTime(WebplusUtil.getDateTime());
		printer.setUpdateTime(WebplusUtil.getDateTime());
		String printType=printer.getPrintType();
		R r=null;
		if(BussCons.PRINT_TYPE_FZY.equals(printType)){
			r=FzCloudUtil.addDevice(printer.getPrintNum(), printer.getPrintSecretKey(),printer.getPrintNum(),printer.getGatewayType());
			
		}else{
			r=LavUtil.addPrinter(printer.getPrintNum(),printer.getPrintSecretKey());
		}
		if(WebplusCons.SUCCESS==r.getAppCode()){
			printer.setWhetherEnroll(WebplusCons.WHETHER_YES);
			printer.setPrintStatus(BussCons.PRINT_STATUS_ONLINE);
		}else{
			printer.setWhetherEnroll(WebplusCons.WHETHER_NO);
			printer.setPrintStatus(BussCons.PRINT_STATUS_OFFLINE);
		}
		boolean result = printerService.insert(printer);
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
	 * 创建时间：2019-07-20
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("edit")
	@ResponseBody
	public R edit(String id) {
		Printer printer=printerService.selectById(id);
		return R.toData(printer);
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
	public R update(Printer printer) {
		String printId=printer.getPrintId();
		String userId=this.getUserId();
		Printer entity=printerService.selectById(printId);
		if(!entity.getPrintNum().equals(printer.getPrintNum())){
			EntityWrapper<Printer> countWrapper = new EntityWrapper<Printer>();
			WebplusSqlHelp.eq(countWrapper, "print_num", printer.getPrintNum());
			int count=printerService.selectCount(countWrapper);
			if(count>0){
				
				return R.warn("当前打印机已经存在。");
			}
			//关联新的打印机
			if(WebplusCons.WHETHER_YES.equals(entity.getWhetherGrant())){
				MenuCatalog menuCatalog=new MenuCatalog();
				menuCatalog.setPrintNum(printer.getPrintNum());
				EntityWrapper<MenuCatalog> updateWrapper = new EntityWrapper<MenuCatalog>();
				updateWrapper.eq("shop_id",  entity.getShopId());
				updateWrapper.eq("print_num",entity.getPrintNum());
				menuCatalogService.update(menuCatalog, updateWrapper);
			}
			
		}
		if(WebplusCons.WHETHER_NO.equals(entity.getWhetherEnroll())){
			String printType=printer.getPrintType();
			R r=null;
			if(BussCons.PRINT_TYPE_FZY.equals(printType)){
				r=FzCloudUtil.addDevice(printer.getPrintNum(), printer.getPrintSecretKey(),printer.getPrintNum(),printer.getGatewayType());
				
			}else{
				r=LavUtil.addPrinter(printer.getPrintNum(),printer.getPrintSecretKey());
			}
			if(WebplusCons.SUCCESS==r.getAppCode()){
				printer.setWhetherEnroll(WebplusCons.WHETHER_YES);
				printer.setPrintStatus(BussCons.PRINT_STATUS_ONLINE);
			}else{
				printer.setWhetherEnroll(WebplusCons.WHETHER_NO);
				printer.setPrintStatus(BussCons.PRINT_STATUS_OFFLINE);
			}
		}
		printer.setUpdateBy(userId);
		printer.setUpdateTime(WebplusUtil.getDateTime());
		boolean result = printerService.updateById(printer);
		if (result) {
			return R.ok();
		} else {
			return R.error();
		}
		
	}
	
	@PostMapping("updateStatus")
	@ResponseBody
	public R updateStatus(Printer printer) {
		String userId=this.getUserId();
		printer.setUpdateBy(userId);
		printer.setUpdateTime(WebplusUtil.getDateTime());
		boolean result = printerService.updateById(printer);
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
	 * 创建时间：2019-07-20
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("remove")
	@ResponseBody
	public R remove(String id) {
		Printer printer=printerService.selectById(id);
		boolean result = printerService.deleteById(id);
		if (result) {
			if(WebplusCons.WHETHER_YES.equals(printer.getWhetherGrant())){  //如果已经授权
				MenuCatalog menuCatalog=new MenuCatalog();
				menuCatalog.setPrintNum("");
				EntityWrapper<MenuCatalog> updateWrapper = new EntityWrapper<MenuCatalog>();
				updateWrapper.eq("shop_id",  printer.getShopId());
				updateWrapper.eq("print_num",printer.getPrintNum());
				menuCatalogService.update(menuCatalog, updateWrapper);
			}
			if(BussCons.PRINT_TYPE_FZY.equals(printer.getPrintType())){  //如果是飞猪云
				FzCloudUtil.delDevice(printer.getPrintNum(),printer.getGatewayType());
				
			}else{
				LavUtil.deletePrinter(printer.getPrintNum());
			}
			return R.ok();
		} else {
			return R.error();
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
		boolean result = printerService.deleteBatchIds(idList);
		if (result) {
			return R.ok();
		} else {
			return R.error();
		}
		
	}
	/**
	 * 
	 * 简要说明：设备注册
	 * 编写者：陈骑元
	 * 创建时间：2019年9月8日 下午4:17:46
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("enroll")
	@ResponseBody
	public R enroll(String printId ){
		Printer printer=printerService.selectById(printId);
		R r=null;
		if(BussCons.PRINT_TYPE_FZY.equals(printer.getPrintType())){
			
			r=FzCloudUtil.addDevice(printer.getPrintNum(), printer.getPrintSecretKey(), printer.getPrintNum(),printer.getGatewayType());
		}else{
			r=LavUtil.addPrinter(printer.getPrintNum(),printer.getPrintSecretKey());
		}
		if(WebplusCons.SUCCESS==r.getAppCode()){
			printer.setWhetherEnroll(WebplusCons.WHETHER_YES);
			printer.setPrintStatus(BussCons.PRINT_STATUS_ONLINE);
			printerService.updateById(printer);
		}
		
		return r;
	}
	/**
	 * 
	 * 简要说明：设备注册
	 * 编写者：陈骑元
	 * 创建时间：2019年9月8日 下午4:17:46
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("removeEnroll")
	@ResponseBody
	public R removeEnroll(String printId ){
		Printer printer=printerService.selectById(printId);
		R r=null;
		if(BussCons.PRINT_TYPE_FZY.equals(printer.getPrintType())){
			
			r=FzCloudUtil.delDevice(printer.getPrintNum(),printer.getGatewayType());
		}else{
			r=LavUtil.deletePrinter(printer.getPrintNum());
		}
		if(WebplusCons.SUCCESS==r.getAppCode()){
			printer.setWhetherEnroll(WebplusCons.WHETHER_NO);
			printer.setPrintStatus(BussCons.PRINT_STATUS_OFFLINE);
			printerService.updateById(printer);
		}
		
		return r;
	}
	
	/**
	 * 
	 * 简要说明：批量删除信息
	 * 编写者：陈骑元
	 * 创建时间：2019-07-20
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("grantShop")
	@ResponseBody
	public R grantShop(String printId,String shopId) {
		String userId=this.getUserId();
		Printer printer=new Printer();
		printer.setPrintId(printId);
		printer.setShopId(shopId);
		printer.setUpdateBy(userId);
		printer.setWhetherGrant(WebplusCons.WHETHER_YES);
		printer.setUpdateTime(WebplusUtil.getDateTime());
		boolean result = printerService.updateById(printer);
		if (result) {
			return R.ok();
		} else {
			return R.error();
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
	@PostMapping("removeGrant")
	@ResponseBody
	public R removeGrant(String printId) {
		Printer printer=printerService.selectById(printId);
		printer.setShopId("");
		printer.setWhetherGrant(WebplusCons.WHETHER_NO);
		boolean result=printerService.updateById(printer);
		if (result) {
			MenuCatalog menuCatalog=new MenuCatalog();
			menuCatalog.setPrintNum("");
			EntityWrapper<MenuCatalog> updateWrapper = new EntityWrapper<MenuCatalog>();
			updateWrapper.eq("shop_id",  printer.getShopId());
			updateWrapper.eq("print_num",printer.getPrintNum());
			menuCatalogService.update(menuCatalog, updateWrapper);
			return R.ok();
		} else {
			return R.error();
		}
		
	}
	/**
	 * 
	 * 简要说明：发送指令
	 * 编写者：陈骑元
	 * 创建时间：2019-07-20
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("send")
	@ResponseBody
	public R send(String printId,String content) {
		
		Printer printer=printerService.selectById(printId);
		String printNum=printer.getPrintNum();
		boolean result=false;
		if(printNum.indexOf("4004")>-1){
		 result=LavUtil.sendPrint(printNum, content, WebplusUtil.uuid());
		}else{
		     //content=BussUtil.createFzPrintMenuContent("T1",2, content);
			 result=FzCloudUtil.sendPrint(printNum, content,printer.getGatewayType());
		}
		
		if(result){
			return R.ok();
		}
		return R.error();
	}
}

