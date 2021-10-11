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
import com.ims.core.util.WebplusHashCodec;
import com.ims.core.util.WebplusQrcode;
import com.ims.core.util.WebplusSqlHelp;
import com.ims.core.util.WebplusUtil;
import com.ims.core.vo.R;

import java.io.File;
import java.util.List;

import com.ims.buss.constant.BussCons;
import com.ims.buss.model.Desk;
import com.ims.buss.service.BussCommonService;
import com.ims.buss.service.DeskService;
import org.springframework.stereotype.Controller;
import com.ims.core.web.BaseController;

/**
 * <p>
 * 桌位 前端控制器
 * </p>
 *
 * @author 陈骑元
 * @since 2019-07-20
 */
@Controller
@RequestMapping("/buss/desk")
public class DeskController extends BaseController {

    @Autowired
    private DeskService deskService;
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
		pDto.setOrder("b.sort_no ASC ,a.sort_no ASC ");
		Page<Desk> page =bussCommonService.listDeskPage(pDto);
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
	public R save(Desk desk) {
		String userId=this.getUserId();
		String areaId=desk.getAreaId();
		
		EntityWrapper<Desk> countWrapper = new EntityWrapper<Desk>();
		WebplusSqlHelp.eq(countWrapper, "desk_name",desk.getDeskName());
		WebplusSqlHelp.eq(countWrapper, "shop_id",userId);
		if(WebplusUtil.isEmpty(areaId)){
			areaId="0";
			countWrapper.and(" IFNULL(area_id,'')=''");
		}else{
			WebplusSqlHelp.eq(countWrapper, "area_id",areaId);
		}
		
		int count=deskService.selectCount(countWrapper);
		if(count>0){
			
			return R.warn("桌号已存在。");
		}
		Dto calcDto = Dtos.newCalcDto("IFNULL(MAX(sort_no),0)+1");
		calcDto.put("shopId", userId);
		calcDto.put("areaId", areaId);
		String maxSortNo =deskService.calc(calcDto);
		if(WebplusUtil.isEmpty(maxSortNo)){
			maxSortNo="1";
		}
		desk.setSortNo(Integer.parseInt(maxSortNo));
		desk.setShopId(userId);
		desk.setCreateBy(userId);
		desk.setUpdateBy(userId);
		desk.setCreateTime(WebplusUtil.getDateTime());
		desk.setUpdateTime(WebplusUtil.getDateTime());
		boolean result = deskService.insert(desk);
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
		Desk desk=deskService.selectById(id);
		return R.toData(desk);
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
	public R update(Desk desk) {
		String userId=this.getUserId();
		String areaId=desk.getAreaId();
		
		Desk entity=deskService.selectById(desk.getDeskId());
		if(!entity.getDeskName().equals(desk.getDeskName())){
			EntityWrapper<Desk> countWrapper = new EntityWrapper<Desk>();
			WebplusSqlHelp.eq(countWrapper, "desk_name",desk.getDeskName());
			WebplusSqlHelp.eq(countWrapper, "shop_id",userId);
			if(WebplusUtil.isEmpty(areaId)){
				countWrapper.and(" IFNULL(area_id,'')=''");
			}else{
				WebplusSqlHelp.eq(countWrapper, "area_id",areaId);
			}
			int count=deskService.selectCount(countWrapper);
			if(count>0){
				
				return R.warn("桌号已存在。");
			}
		}
		String oldAreaId=entity.getAreaId(); //旧的区域编号
		if(WebplusUtil.isEmpty(areaId)){
			areaId="0";
		}
		if(WebplusUtil.isEmpty(oldAreaId)){
			oldAreaId="0";
		}
		if(!areaId.equals(oldAreaId)){  //区域编号变化了
			Dto calcDto = Dtos.newCalcDto("IFNULL(MAX(sort_no),0)+1");
			calcDto.put("shopId", userId);
			calcDto.put("areaId", areaId);
			String maxSortNo =deskService.calc(calcDto);
			if(WebplusUtil.isEmpty(maxSortNo)){
				maxSortNo="1";
			}
			desk.setSortNo(Integer.parseInt(maxSortNo));
		}
		if(BussCons.DESK_STATUS_EMPTY.equals(desk.getDeskStatus())) {
			desk.setEatNum(0);
			desk.setOrderNo("");
			desk.setOrderTime(null);
		}
		desk.setUpdateBy(userId);
		desk.setUpdateTime(WebplusUtil.getDateTime());
		boolean result = deskService.updateById(desk);
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
		boolean result = deskService.deleteById(id);
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
	@PostMapping("batchRemove")
	@ResponseBody
	public R batchRemove(String ids) {
		List<String> idList=WebplusFormater.separatStringToList(ids);
		boolean result = deskService.deleteBatchIds(idList);
		if (result) {
			return R.ok();
		} else {
			return R.error();
		}
		
	}
	/**
	 * 
	 * 简要说明：更新排序方式
	 * 编写者：陈骑元
	 * 创建时间：2019年7月24日 下午10:22:46
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("updateSort")
	@ResponseBody
	public R updateSort(String id,String sortWay){
		Desk desk=deskService.selectById(id);
		String areaId=desk.getAreaId();
		if(WebplusUtil.isEmpty(areaId)){
			areaId="0";
		}
		int sortNo=desk.getSortNo();
		String shopId=desk.getShopId();
		if("1".equals(sortWay)){  //更新到顶部
			Dto calcDto = Dtos.newCalcDto("IFNULL(MIN(sort_no),0)");
			calcDto.put("shopId", shopId);
			calcDto.put("areaId", areaId);
			String minSortNoStr=deskService.calc(calcDto);
			int minSortNo=Integer.parseInt(minSortNoStr);
			if(sortNo>minSortNo){
				Dto updateDto=Dtos.newDto();
				updateDto.put("shopId", shopId);
				updateDto.put("sortNoLt", sortNo);
				updateDto.put("areaId", areaId);
				deskService.updateSortDown(updateDto);
				desk.setSortNo(minSortNo);
				deskService.updateById(desk);
			}
		}else if("2".equals(sortWay)){  //上升1位
			Dto calcDto = Dtos.newCalcDto("IFNULL(MAX(sort_no),0)");
			calcDto.put("shopId", shopId);
			calcDto.put("sortNoLt", sortNo);
			calcDto.put("areaId", areaId);
			String maxSortNoStr=deskService.calc(calcDto);
			int maxSortNo=Integer.parseInt(maxSortNoStr);
			if(maxSortNo>0){
				Desk entity=new Desk();
				entity.setSortNo(sortNo);
				EntityWrapper<Desk> updateWrapper = new EntityWrapper<Desk>();
				updateWrapper.eq("sort_no", maxSortNo);
				updateWrapper.eq("shop_id", shopId);
				if("0".equals(areaId)){
					updateWrapper.and(" IFNULL(area_id,'')=''");
							
				}else{
					updateWrapper.eq("area_id", areaId);
				}
				deskService.update(entity, updateWrapper);
				desk.setSortNo(maxSortNo);
				deskService.updateById(desk);
			}
				
		}else if("3".equals(sortWay)){  //下降1位
			Dto calcDto = Dtos.newCalcDto("IFNULL(MIN(sort_no),0)");
			calcDto.put("shopId", shopId);
			calcDto.put("sortNoGt", sortNo);
			calcDto.put("areaId", areaId);
			String minSortNoStr=deskService.calc(calcDto);
			int minSortNo=Integer.parseInt(minSortNoStr);
			if(minSortNo>0){
				Desk entity=new Desk();
				entity.setSortNo(sortNo);
				EntityWrapper<Desk> updateWrapper = new EntityWrapper<Desk>();
				updateWrapper.eq("sort_no", minSortNo);
				updateWrapper.eq("shop_id", shopId);
				if(WebplusUtil.isNotEmpty(areaId)){
					if("0".equals(areaId)){
						updateWrapper.and(" IFNULL(area_id,'')=''");
								
					}else{
						updateWrapper.eq("area_id", areaId);
					}
					
				}
				deskService.update(entity, updateWrapper);
				desk.setSortNo(minSortNo);
				deskService.updateById(desk);
			}
				
		}else{  //下降到最后1位
			Dto calcDto = Dtos.newCalcDto("IFNULL(MAX(sort_no),0)");
			calcDto.put("shopId", shopId);
			calcDto.put("areaId", areaId);
			String maxSortNoStr=deskService.calc(calcDto);
			int maxSortNo=Integer.parseInt(maxSortNoStr);
			if(sortNo<maxSortNo){
				Dto updateDto=Dtos.newDto();
				updateDto.put("shopId", shopId);
				updateDto.put("sortNoGt", sortNo);
				updateDto.put("areaId", areaId);
				deskService.updateSortUp(updateDto);
				desk.setSortNo(maxSortNo);
				deskService.updateById(desk);
			}
		}
		 
		return R.ok();
			
	}
	/***
	 * 
	 * 简要说明：展示订单二维码
	 * 编写者：陈骑元
	 * 创建时间：2019年10月19日 下午7:30:48
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("showOrderQrcode")
	@ResponseBody
	public R showOrderQrcode(String id){
		Desk desk = deskService.selectById(id);
		String orderNo = desk.getOrderNo();
		if (WebplusUtil.isNotEmpty(orderNo)) {
			String rootPath=WebplusCache.getParamValue(WebplusCons.SAVE_ROOT_PATH_KEY);
			String shopId = desk.getShopId();
			String accessToken = WebplusHashCodec.encryptBase64(orderNo);
			String qrcodeUrl = WebplusCache.getParamValue(WebplusCons.REQUEST_URL_KEY) + "/h5/index.html?shopId="
					+ shopId + "&accessToken=" + accessToken;
			String destPath=rootPath+File.separator+BussCons.QRCODE_IMAGE;
			String fileName="Q"+orderNo+".png";
            WebplusQrcode.createQrcodeImage(qrcodeUrl, destPath, fileName); //创建二维码
            Dto dataDto=Dtos.newDto("fileName",fileName);
            return R.toData(dataDto);
		}
		return R.error();
	}
	/**
	 * 
	 * 简要说明：点餐成功
	 * 编写者：陈骑元
	 * 创建时间：2019年10月19日 下午8:15:07
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("addOrderNum")
	@ResponseBody
	public R addOrderNum(Desk desk){
		String userId=this.getUserId();
		String orderNo=WebplusCache.createOrderNum();
    	desk.setOrderNo(orderNo);
    	desk.setUpdateTime(WebplusUtil.getDateTime());
    	desk.setUpdateBy(userId);
     	boolean result=deskService.updateById(desk);
     	if(result){
     		
     		return R.ok();
     	}
     	return R.error();
	}
	/**
	 * 
	 * 简要说明：更新价格显示状态
	 * 编写者：陈骑元
	 * 创建时间：2019年10月20日 下午11:54:18
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("updateDeskPriceStatus")
	@ResponseBody
	public R updateDeskPriceStatus(String deskId,String whetherShowPrice){
		Desk desk=new Desk();
		desk.setDeskId(deskId);
		desk.setWhetherShowPrice(whetherShowPrice);
		boolean result=deskService.updateById(desk);
		if(result){
			
			return R.ok();
		}
       return R.error();
	}
}

