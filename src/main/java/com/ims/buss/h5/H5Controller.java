package com.ims.buss.h5;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.ims.buss.asyncTask.PrintAsyncTask;
import com.ims.buss.constant.BussCons;
import com.ims.buss.model.BuffetMenu;
import com.ims.buss.model.BuffetSpec;
import com.ims.buss.model.Desk;
import com.ims.buss.model.Member;
import com.ims.buss.model.MenuCatalog;
import com.ims.buss.model.MenuDict;
import com.ims.buss.model.MenuSpec;
import com.ims.buss.model.MenuStep;
import com.ims.buss.model.Order;
import com.ims.buss.model.OrderLine;
import com.ims.buss.model.Shop;
import com.ims.buss.model.StepSpec;
import com.ims.buss.service.BuffetSpecService;
import com.ims.buss.service.BussCommonService;
import com.ims.buss.service.DeskService;
import com.ims.buss.service.MemberService;
import com.ims.buss.service.MenuCatalogService;
import com.ims.buss.service.MenuDictService;
import com.ims.buss.service.MenuSpecService;
import com.ims.buss.service.MenuStepService;
import com.ims.buss.service.OrderCommonService;
import com.ims.buss.service.OrderLineService;
import com.ims.buss.service.OrderService;
import com.ims.buss.service.ShopService;
import com.ims.buss.service.StepSpecService;
import com.ims.buss.util.BussCache;
import com.ims.buss.util.BussUtil;
import com.ims.core.cache.WebplusCache;
import com.ims.core.constant.WebplusCons;
import com.ims.core.matatype.Dto;
import com.ims.core.matatype.Dtos;
import com.ims.core.matatype.impl.HashDto;
import com.ims.core.util.WebplusFormater;
import com.ims.core.util.WebplusHashCodec;
import com.ims.core.util.WebplusJson;
import com.ims.core.util.WebplusUtil;
import com.ims.core.vo.Item;
import com.ims.core.vo.R;
import com.ims.websocket.AppPushServer;

import net.sf.json.JSONObject;

/**
 * 
 * ??????:com.ims.buss.h5.H5Controller
 * ??????:h5????????????
 * ?????????:?????????
 * ????????????:2019???7???21??? ??????11:03:42
 * ????????????:
 */
@Controller
@RequestMapping("/h5")
public class H5Controller {
	private static Log log = LogFactory.getLog(H5Controller.class);
	@Autowired
	private DeskService deskService;
	@Autowired
	private ShopService shopService;
	@Autowired
	private OrderLineService orderLineService;
	@Autowired
	private MenuCatalogService menuCatalogService;
	@Autowired
	private MenuSpecService menuSpecService;
	@Autowired
	private MenuDictService menuDictService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private PrintAsyncTask printAsyncTask;
	@Autowired
	private MenuStepService menuStepService;
	@Autowired
	private StepSpecService stepSpecService;
	@Autowired
	private BussCommonService bussCommonService;
	@Autowired
	private BuffetSpecService buffetSpecService;
	@Autowired
	private OrderCommonService orderCommonService;
	@Autowired
	private MemberService mbmerService;
	
	@RequestMapping("payReturn")
	public void payReturn(String shopId,String token,String orderFoodType,
			String result,HttpServletResponse response) {
		if(WebplusUtil.isNotEmpty(result)) {
			String resultJson=WebplusHashCodec.decBase64(result);
			log.info("??????????????????????????????"+resultJson);
			if(WebplusUtil.isNotEmpty(resultJson)) {
				JSONObject json = JSONObject.fromObject(resultJson);
				JSONObject resultObj=json.getJSONObject("transactionresult");
				String orderId=resultObj.getString("OrderID");
				String payResult=resultObj.getString("Result");
				if("PAYSUCCESS".equals(payResult)) {
					if(WebplusUtil.isNotEmpty(orderId)) {
						Order order=new Order();
						order.setOrderId(orderId);
						order.setPayTime(WebplusUtil.getDateTime());
						order.setWhetherPay(WebplusCons.WHETHER_YES);
						order.setPayWay(BussCons.PAY_WAY_CARD);
						orderService.updateById(order);
						log.info("??????["+orderId+"]??????????????????????????????????????????"+resultJson);
						
					}
					
				}
				
			}
			
		
		}
		if(WebplusUtil.isNotEmpty(shopId)) {
			String url=WebplusCache.getParamValue(WebplusCons.REQUEST_URL_KEY)+"/h6/index.html?shopId="+shopId+"&orderFoodType="+orderFoodType;
		    if(WebplusUtil.isNotEmpty(token)) {
		    	url+="&token="+token;
		    }
			String outString="<script>window.location.href='"+url+"'</script>";
		    WebplusUtil.writeRaw(response, outString);
		}
	}
	/**
	 * 
	 * ???????????????????????????????????????
	 * ????????????????????????chenqiyuan@toonan.com???
	 * ??????????????? 2020???9???10??? ??????9:28:09 
	 * @param ??????
	 * @return ??????
	 */
	@RequestMapping("memberReturn")
	public void memberReturn(String memberId ,String shopId,String token,String orderFoodType,
			String result,HttpServletResponse response) {
		if(WebplusUtil.isNotEmpty(result)) {
			String resultJson=WebplusHashCodec.decBase64(result);
			log.info("??????????????????????????????"+resultJson);
			if(WebplusUtil.isNotEmpty(resultJson)) {
				
				if(resultJson.indexOf("SUCCESS")>-1) {
					if(WebplusUtil.isNotEmpty(memberId)) {
						Member member=new Member();
						member.setMemberId(memberId);
						member.setWhetherCard(WebplusCons.WHETHER_YES);
						member.setUpdateTime(WebplusUtil.getDateTime());
						mbmerService.updateById(member);
						log.info("??????["+memberId+"]??????????????????????????????????????????"+resultJson);
						
					}
					
				}
				
			}
			
		
		}
		if(WebplusUtil.isNotEmpty(shopId)) {
			String url=WebplusCache.getParamValue(WebplusCons.REQUEST_URL_KEY)+"/h6/index.html?shopId="+shopId+"&orderFoodType="+orderFoodType;
		    if(WebplusUtil.isNotEmpty(token)) {
		    	url+="&token="+token;
		    }
			String outString="<script>window.location.href='"+url+"'</script>";
		    WebplusUtil.writeRaw(response, outString);
		}
	}
	/**
	 * 
	 * ?????????????????????????????????
	 * ?????????????????????
	 * ???????????????2019???8???2??? ??????11:25:37
	 * @param ??????
	 * @return ??????
	 */
	@PostMapping("queryShopInfo")
	@ResponseBody
	public R queryShopInfo(String shopId,String languageType,String accessToken){
		Shop shop=shopService.selectById(shopId);
		EntityWrapper<MenuCatalog> wrapper=new EntityWrapper<MenuCatalog>();
	
		String mealType=BussUtil.getMenuMealType(shop.getTimeLimitStart(),shop.getTimeLimit());  //??????????????????
		List<String> mealTypeList=Lists.newArrayList();
		mealTypeList.add(BussCons.MEAL_TYPE_COMMON);
		mealTypeList.add(mealType);
		wrapper.in("meal_type", mealTypeList);
		wrapper.eq("shop_id", shopId);
		wrapper.eq("whether_buffet", WebplusCons.WHETHER_YES);
		wrapper.eq("language_type", languageType);
	    int count=menuCatalogService.selectCount(wrapper);
	    String whetherShowBuffet=WebplusCons.WHETHER_NO;
	    if(count>0){
	    	whetherShowBuffet=WebplusCons.WHETHER_YES;
	    }
	    String whetherShowPrice=WebplusCons.WHETHER_YES;
	    String displayType="";
	    String deskName="";
	    String whetherOrderBuffet=WebplusCons.WHETHER_NO;
	  
	    if(WebplusUtil.isNotEmpty(accessToken)){  
	    	String orderId=WebplusHashCodec.decryptBase64(accessToken);
			if(WebplusUtil.isNotEmpty(orderId)){
				EntityWrapper<Desk> deskWrapper = new EntityWrapper<Desk>();
	    		deskWrapper.eq("order_no", orderId);
	    		Desk desk=deskService.selectOne(deskWrapper);
				if(WebplusUtil.isNotEmpty(desk)){
					deskName=desk.getDeskName();
					if(WebplusCons.WHETHER_NO.equals(desk.getWhetherShowPrice())){
						whetherShowPrice=WebplusCons.WHETHER_NO;
						
					}
					displayType=desk.getDisplayType();
				}
			   if(this.checkOrderBuffet(orderId)) {
				   whetherOrderBuffet=WebplusCons.WHETHER_YES;
			   }
			}
	    }
	    if(WebplusUtil.isEmpty(displayType)){
                  displayType=BussCons.DISPLAY_TYPE_FULL;
	    }
	    if(BussCons.LANGUAGE_TYPE_CH.equals(languageType)){
	    	shop.setShopIntroduce(shop.getShopIntroduceCh());
	    }else if(BussCons.LANGUAGE_TYPE_TC.equals(languageType)){
	    	shop.setShopIntroduce(shop.getShopIntroduceTc());
	    }else if(BussCons.LANGUAGE_TYPE_KRO.equals(languageType)){
	    	shop.setShopIntroduce(shop.getShopIntroduceKro());
	    }else if(BussCons.LANGUAGE_TYPE_ENG.equals(languageType)){
	    	shop.setShopIntroduce(shop.getShopIntroduceEng());
	    }
	  
		return R.toData(shop).put("whetherShowBuffet", whetherShowBuffet).put("whetherOrderBuffet", whetherOrderBuffet).
				put("whetherShowPrice", whetherShowPrice).put("displayType", displayType).put("deskName", deskName);
	}
	/**
	 * 
	 * ??????????????????????????????
	 * ?????????????????????
	 * ???????????????2019???8???6??? ??????9:04:43
	 * @param ??????
	 * @return ??????
	 */
	@PostMapping("queryLanguagePack")
	@ResponseBody
	public R queryLanguagePack(String shopId,String languageType){
		if(WebplusUtil.isEmpty(languageType)){
			languageType=BussCons.LANGUAGE_TYPE_JP;
		}else{
			Shop shop=shopService.selectById(shopId);
			if(WebplusUtil.isNotEmpty(shop)){
				String language=shop.getLanguage();
				if(language.indexOf(languageType)<0){
					languageType=BussCons.LANGUAGE_TYPE_JP;
				}
			}
			
		}
		List<Dto> dataList=BussCache.getLanguagePack(languageType);
		if(WebplusUtil.isEmpty(dataList)){
			dataList=BussCache.getLanguagePack(BussCons.LANGUAGE_TYPE_JP);
		}
		return R.toList(dataList);
	}
	
	
	
		/**
	 * 
	 * ??????????????????????????????????????????
	 * ?????????????????????
	 * ???????????????2019???7???27??? ??????11:25:56
	 * @param ??????
	 * @return ??????
	 */
	@PostMapping("loadShopMenu")
	@ResponseBody
	public R loadShopMenu(String accessToken,String shopId,String languageType){
		String orderId=WebplusHashCodec.decryptBase64(accessToken);
		if(WebplusUtil.isNotEmpty(orderId)){
			EntityWrapper<Desk> wrapper = new EntityWrapper<Desk>();
    		wrapper.eq("order_no", orderId);
    		Desk desk=deskService.selectOne(wrapper);
			
			if(WebplusUtil.isNotEmpty(desk)){
				
				shopId=desk.getShopId();
			}
		}
		if(WebplusUtil.isNotEmpty(shopId)){
			if(WebplusUtil.isEmpty(languageType)){
				languageType=BussCons.LANGUAGE_TYPE_JP;
			}
			Shop shop=shopService.selectById(shopId);
			String language=shop.getLanguage();
			if(language.indexOf(languageType)<0){
				languageType=BussCons.LANGUAGE_TYPE_JP;
			}
			List<Dto> dataList=Lists.newArrayList();
			Dto pDto=Dtos.newDto();
			pDto.put("shopId", shopId);
			pDto.put("whetherBuffet", WebplusCons.WHETHER_NO);
			pDto.put("languageType", languageType);
			pDto.setOrder(" sort_no ASC ");
			List<MenuCatalog> menuCatalogList=menuCatalogService.list(pDto);
			List<String> mealTypeList=Lists.newArrayList();
			String mealType=BussUtil.getMenuMealType(shop.getTimeLimitStart(),shop.getTimeLimit());  //??????????????????
			mealTypeList.add(BussCons.MEAL_TYPE_COMMON);
			mealTypeList.add(mealType);
			List<String> menuTypeList=Lists.newArrayList();
			menuTypeList.add(BussCons.MENU_TYPE_COMMON);
			menuTypeList.add(BussCons.MENU_TYPE_RULE);
			pDto.put("mealTypeList", mealTypeList);
			pDto.put("menuTypeList", menuTypeList);
			List<MenuDict> menuDictList=menuDictService.list(pDto);
			List<MenuCatalog> catalogList=Lists.newArrayList();  //??????????????????????????????????????????
			for(MenuCatalog menuCatalog:menuCatalogList){
			
				String catalogIndexId=menuCatalog.getCatalogIndexId();
				List<MenuDict> menuDictListNew=this.getCatalogMenuDictList(catalogIndexId, menuDictList,WebplusCons.WHETHER_NO);
				if(WebplusUtil.isNotEmpty(menuDictListNew)){  //??????????????????????????????????????????
					Dto dataDto=Dtos.newDto();
					dataDto.put("catalogIndexId", catalogIndexId);
					dataDto.put("catalogName", menuCatalog.getCatalogName());
					dataDto.put("catalogType", menuCatalog.getCatalogType());
					dataDto.put("taxType", menuCatalog.getTaxType());
					dataDto.put("menuDictList", menuDictListNew);
					dataList.add(dataDto);
					catalogList.add(menuCatalog);
				}
			
			}
			return R.toDataAndList(shop, dataList).put("menuCatalogList", catalogList);
		}
		return R.error();
		
	}
	
	
	/**
	 * 
	 * ??????????????????????????????????????????
	 * ?????????????????????
	 * ???????????????2019???7???27??? ??????11:25:56
	 * @param ??????
	 * @return ??????
	 */
	@PostMapping("loadShopMenuNew")
	@ResponseBody
	public R loadShopMenuNew(String accessToken,String shopId,String languageType){
		  String displayType= BussCons.DISPLAY_TYPE_FULL;
		  String orderId=""	;
		if (WebplusUtil.isNotEmpty(accessToken) && !"undefined".equals(accessToken)) {
			orderId = WebplusHashCodec.decryptBase64(accessToken);
			if (WebplusUtil.isNotEmpty(orderId)) {
				EntityWrapper<Desk> wrapper = new EntityWrapper<Desk>();
				wrapper.eq("order_no", orderId);
				Desk desk = deskService.selectOne(wrapper);
				if (WebplusUtil.isNotEmpty(desk)) {

					shopId = desk.getShopId();
					displayType = desk.getDisplayType();
				}

			}
		}
		if (WebplusUtil.isNotEmpty(shopId)) {
			if (WebplusUtil.isEmpty(languageType)) {
				languageType = BussCons.LANGUAGE_TYPE_JP;
			}
			Shop shop = shopService.selectById(shopId);
			String language = shop.getLanguage();
			if (language.indexOf(languageType) < 0) {
				languageType = BussCons.LANGUAGE_TYPE_JP;
			}
			List<Dto> dataList = Lists.newArrayList();
     
			String mealType = BussUtil.getMenuMealType(shop.getTimeLimitStart(), shop.getTimeLimit()); // ??????????????????
			Dto pDto = Dtos.newDto();
			List<String> catalogTypeList=Lists.newArrayList();
			catalogTypeList.add(BussCons.CATALOG_TYPE_BESIDES);
			catalogTypeList.add(BussCons.CATALOG_TYPE_EATIN);
			pDto.put("catalogTypeList", catalogTypeList);
			pDto.put("shopId", shopId);
			pDto.put("languageType", languageType);
			pDto.put("whetherBuffet", WebplusCons.WHETHER_NO);
			Dto catalogDto = Dtos.newDto();
			String whetherBuffet = WebplusCons.WHETHER_NO; // ?????????????????????
			if (WebplusUtil.isNotEmpty(orderId)) {
					
					String key=BussCons.BUFFET_ORDER_KEY+orderId;
					String value=WebplusCache.getString(key);
					if(WebplusUtil.isNotEmpty(value)) {
						Dto limitDto=WebplusJson.fromJson(value, HashDto.class);
						catalogDto.putAll(limitDto);
					}
					if(WebplusUtil.isNotEmpty(catalogDto)) {
						whetherBuffet = WebplusCons.WHETHER_YES;
					}

			
			}
			
			pDto.setOrder(" sort_no ASC ");
			List<MenuCatalog> 	menuCatalogList = menuCatalogService.list(pDto);
			List<MenuCatalog> catalogList = Lists.newArrayList(); // ??????????????????????????????????????????
			
			// ?????????????????????
			if (BussCons.DISPLAY_TYPE_BUFFET.equals(displayType) || BussCons.DISPLAY_TYPE_FULL.equals(displayType)) {
				if(WebplusCons.WHETHER_NO.equals(whetherBuffet)) {
					List<MenuCatalog> buffetCatalogList = Lists.newArrayList();
					EntityWrapper<MenuCatalog> wrapper=new EntityWrapper<MenuCatalog>();
					wrapper.eq("language_type", languageType);
					wrapper.eq("shop_id",shopId);
					wrapper.eq("whether_first",WebplusCons.WHETHER_YES);
					MenuCatalog firstMenuCatalog=menuCatalogService.selectOne(wrapper);
					if (WebplusUtil.isNotEmpty(firstMenuCatalog)) { // ??????????????????
						buffetCatalogList.add(firstMenuCatalog);
						Dto dataDto = Dtos.newDto();
						String catalogName = BussCache.getLanguageValue(languageType, "buffet");
						dataDto.put("catalogIndexId", "0");
						dataDto.put("catalogName", catalogName);
						dataDto.put("menuDictList", buffetCatalogList);
						dataList.add(dataDto);
						MenuCatalog buffetCatalog = new MenuCatalog();
						buffetCatalog.setCatalogName(catalogName);
						buffetCatalog.setCatalogIndexId("0");
						catalogList.add(buffetCatalog); // ?????????????????????
						
					}
				}
				
				/*
				 * if (BussCons.DISPLAY_TYPE_BUFFET.equals(displayType) &&
				 * WebplusCons.WHETHER_YES.equals(whetherBuffet)) { String catalogIndexId =
				 * catalogDto.getString("catalogIndexId"); List<String> mealTypeList =
				 * Lists.newArrayList(); mealTypeList.add(BussCons.MEAL_TYPE_COMMON);
				 * mealTypeList.add(mealType); Dto buffetDto = Dtos.newDto();
				 * buffetDto.put("mealTypeList", mealTypeList); buffetDto.put("shopId",
				 * shop.getShopId()); buffetDto.put("languageType", languageType);
				 * buffetDto.put("inCatalogIndexId", catalogIndexId);
				 * buffetDto.setOrder(" b.sort_no ASC,a.sort_no ASC"); List<MenuDict>
				 * menuDictList = bussCommonService.listBuffetMenuDict(buffetDto);
				 * List<MenuCatalog> buffetMenuCatalogList = getBuffetMenuCatalog(menuDictList,
				 * languageType,whetherBuffet); this.addCatalogMenu(buffetMenuCatalogList,
				 * menuDictList, WebplusCons.WHETHER_YES, dataList, catalogList); }
				 */
			}
			// ????????????????????????
			if (BussCons.DISPLAY_TYPE_MENU.equals(displayType) || BussCons.DISPLAY_TYPE_FULL.equals(displayType)) {
				List<String> mealTypeList = Lists.newArrayList();
				mealTypeList.add(BussCons.MEAL_TYPE_COMMON);
				mealTypeList.add(mealType);
				List<String> menuTypeList = Lists.newArrayList();
				menuTypeList.add(BussCons.MENU_TYPE_COMMON);
				menuTypeList.add(BussCons.MENU_TYPE_RULE);
				/*
				 * if(BussCons.DISPLAY_TYPE_FULL.equals(displayType)&&WebplusCons.WHETHER_YES.
				 * equals(whetherBuffet)) { menuTypeList.add(BussCons.MENU_TYPE_BUFFET); }
				 */
				pDto.put("mealTypeList", mealTypeList);
				pDto.put("menuTypeList", menuTypeList);
				pDto.put("whetherCustom", WebplusCons.WHETHER_NO);
				List<MenuDict> menuDictList = menuDictService.list(pDto);
				List<MenuCatalog> commonMenuCatalogList = getCommonMenuCatalogList(menuCatalogList);
				this.addCatalogMenu(commonMenuCatalogList, menuDictList,WebplusCons.WHETHER_NO, dataList,
						catalogList);

			}

			return R.toDataAndList(shop, dataList).put("menuCatalogList", catalogList)
					.put("whetherBuffet", whetherBuffet).put("buffetCatalog", catalogDto);
		}
		return R.error();
		
	}
	
	private void addCatalogMenu(List<MenuCatalog> menuCatalogList,List<MenuDict> menuDictList,
			String whetherBuffet,List<Dto> dataList,List<MenuCatalog> catalogList){
		
		
		for (MenuCatalog menuCatalog : menuCatalogList) {

			String catalogIndexId = menuCatalog.getCatalogIndexId();
			List<MenuDict> menuDictListNew  = this.getCatalogMenuDictList(catalogIndexId, menuDictList,whetherBuffet);
			

			if (WebplusUtil.isNotEmpty(menuDictListNew)) { // ??????????????????????????????????????????
				Dto dataDto = Dtos.newDto();
				dataDto.put("catalogIndexId", catalogIndexId);
				dataDto.put("catalogName", menuCatalog.getCatalogName());
				dataDto.put("catalogType", menuCatalog.getCatalogType());
				dataDto.put("taxType", menuCatalog.getTaxType());
				dataDto.put("menuDictList", menuDictListNew);
				dataList.add(dataDto);
				catalogList.add(menuCatalog);
			}

		}
	}
	/**
	 * 
	 * ????????????????????????????????????
	 * ?????????????????????
	 * ???????????????2020???1???10??? ??????10:38:24
	 * @param ??????
	 * @return ??????
	 */
	@PostMapping("listBuffetSpec")
	@ResponseBody
	public R listBuffetSpec(String catalogIndexId,String languageType){
		if(WebplusUtil.isEmpty(languageType)){
			languageType=BussCons.LANGUAGE_TYPE_JP;
		}
		EntityWrapper<BuffetSpec> wrapper=new EntityWrapper<BuffetSpec>();
		wrapper.eq("catalog_index_id", catalogIndexId);
		wrapper.eq("language_type", languageType);
		wrapper.orderBy("sort_no");
		List<BuffetSpec>  buffetSpecList=buffetSpecService.selectList(wrapper);
		
		return R.toList(buffetSpecList);
	}
	
	/**
	 * 
	 * ??????????????????????????????????????????
	 * ????????????????????????chenqiyuan@toonan.com???
	 * ??????????????? 2020???4???9??? ??????9:59:14 
	 * @param ??????
	 * @return ??????
	 */
	@PostMapping("checkBuffetOrder")
	@ResponseBody
	public R checkBuffetOrder(String accessToken){
		if(WebplusUtil.isNotEmpty(accessToken)) {
			String orderId = WebplusHashCodec.decryptBase64(accessToken);
			if(WebplusUtil.isNotEmpty(orderId)) {
				String key=BussCons.BUFFET_ORDER_KEY+orderId;
				if(WebplusCache.exists(key)) {
					
					return R.ok();
				}
			}

			
		}
		return R.error();
		
	}
	/**
	 * 
	 * ????????????????????????????????????
	 * ?????????????????????
	 * ???????????????2020???1???13??? ??????12:37:54
	 * @param ??????
	 * @return ??????
	 */
	public List<MenuCatalog> getBuffetMenuCatalog(List<MenuDict> menuDictList,String languageType,String whetherBuffet){
		List<MenuCatalog> menuCatalogList=Lists.newArrayList();
		List<String> catalogIdList=this.getCatalogIdList(menuDictList);
		if(WebplusUtil.isNotEmpty(catalogIdList)){
			EntityWrapper<MenuCatalog> wrapper=new EntityWrapper<MenuCatalog>();
			wrapper.in("catalog_index_id", catalogIdList);
			wrapper.in("language_type", languageType);
			if(WebplusCons.WHETHER_YES.equals(whetherBuffet)) {
				wrapper.orderBy("whether_top DESC, sort_no ASC");
			}else {
				wrapper.orderBy(" sort_no ASC ");
			}
			
			menuCatalogList=menuCatalogService.selectList(wrapper);
		}
		
       return menuCatalogList;
	}
	
	/**
	 * 
	 * ?????????????????????????????????
	 * ?????????????????????
	 * ???????????????2020???1???13??? ??????12:40:04
	 * @param ??????
	 * @return ??????
	 */
	private List<String> getCatalogIdList(List<MenuDict> menuDictList){
		List<String> catalogIdList=Lists.newArrayList();
		Dto idDto=Dtos.newDto();
		if(WebplusUtil.isNotEmpty(menuDictList)){
			for(MenuDict menuDict:menuDictList){
				String catalogIndexId=menuDict.getCatalogIndexId();
				if(!idDto.containsKey(catalogIndexId)){
					idDto.put(catalogIndexId, catalogIndexId);
				}
			}
		}
		
		for(String key:idDto.keySet()){
			catalogIdList.add(key);
		}
		return catalogIdList;
	}
	/**
	 * 
	 * ???????????????????????????????????????
	 * ?????????????????????
	 * ???????????????2020???1???4??? ??????11:00:24
	 * @param ??????
	 * @return ??????
	 */
	public List<MenuCatalog> getChooseBuffetCatalog(List<MenuCatalog> menuCatalogList,String catalogIndexId){
		 List<MenuCatalog> buffetCatalogList=Lists.newArrayList();
		if(WebplusUtil.isNotEmpty(menuCatalogList)&&WebplusUtil.isNotEmpty(catalogIndexId)){
			for(MenuCatalog menuCatalog:menuCatalogList){
				if(catalogIndexId.equals(menuCatalog.getCatalogIndexId())){
					buffetCatalogList.add(menuCatalog);
					
					return buffetCatalogList;
					
				}
			}
		}
		return buffetCatalogList;
	}
	/**
	 * 
	 * ??????????????????????????????
	 * ?????????????????????
	 * ???????????????2020???1???4??? ??????9:32:41
	 * @param ??????
	 * @return ??????
	 */
	public List<MenuCatalog> getBuffetMenuList(List<MenuCatalog> menuCatalogList,String mealType){
		List<MenuCatalog> buffetMenuList=Lists.newArrayList();
		if(WebplusUtil.isNotEmpty(menuCatalogList)){
			for(MenuCatalog menuCatalog:menuCatalogList){
				if(WebplusCons.WHETHER_YES.equals(menuCatalog.getWhetherBuffet())){
					String mealTypeTmp=menuCatalog.getMealType();
					if(BussCons.MEAL_TYPE_COMMON.equals(mealTypeTmp)||mealType.equals(mealTypeTmp)){
						EntityWrapper<BuffetSpec> countWrapper=new EntityWrapper<BuffetSpec>();
						countWrapper.eq("catalog_index_id", menuCatalog.getCatalogIndexId()	);
						countWrapper.eq("shop_id", menuCatalog.getShopId());
						countWrapper.eq("language_type", menuCatalog.getLanguageType());
						int count=buffetSpecService.selectCount(countWrapper);
						if(count>0){  //????????????
							menuCatalog.setWhetherSpec(WebplusCons.WHETHER_YES);
						}else{  //???????????????
							menuCatalog.setWhetherSpec(WebplusCons.WHETHER_NO);
						}
						buffetMenuList.add(menuCatalog);
					}
				}
			}
		}
		
		return buffetMenuList;
	}
	/**
	 * 
	 * ???????????????????????????????????????
	 * ?????????????????????
	 * ???????????????2020???1???4??? ??????9:25:09
	 * @param ??????
	 * @return ??????
	 */
	private List<MenuCatalog> getCommonMenuCatalogList(List<MenuCatalog> menuCatalogList){
		List<MenuCatalog> menuCatalogListNew=Lists.newArrayList();
		if(WebplusUtil.isNotEmpty(menuCatalogList)){
			for(MenuCatalog menuCatalog:menuCatalogList){
				if(WebplusCons.WHETHER_NO.equals(menuCatalog.getWhetherBuffet())){
					 menuCatalogListNew.add(menuCatalog);
				}
			}
		}
		
		return menuCatalogListNew;
		
	}
	/**
	 * 
	 * ?????????????????????????????????,???????????????????????????
	 * ?????????????????????
	 * ???????????????2019???8???2??? ??????11:12:41
	 * @param ??????
	 * @return ??????
	 */
	public List<MenuDict> getCatalogMenuDictList(String catalogIndexId,List<MenuDict> menuDictList,List<BuffetMenu> buffetMenuList){
		  List<MenuDict> menuDictListNew=Lists.newArrayList();
		  for(MenuDict menuDict:menuDictList){
			  if(catalogIndexId.equals(menuDict.getCatalogIndexId())){
				  if(checkBuffet(menuDict.getMenuIndexId(),buffetMenuList)){  //???????????????????????????
					  menuDict.setWhetherBuffet(WebplusCons.WHETHER_YES);
				  }else{
					  menuDict.setWhetherBuffet(WebplusCons.WHETHER_NO);
				  }
				  menuDictListNew.add(menuDict);
			  }
		  }
		  return menuDictListNew;
	}
	/**
	 * 
	 * ???????????????????????????????????????????????????
	 * ?????????????????????
	 * ???????????????2020???1???4??? ??????10:43:30
	 * @param ??????
	 * @return ??????
	 */
	private boolean checkBuffet(String menuIndexId,List<BuffetMenu> buffetMenuList){
		if(WebplusUtil.isNotEmpty(buffetMenuList)&&WebplusUtil.isNotEmpty(menuIndexId)){
			for(BuffetMenu buffetMenu:buffetMenuList){
				if(menuIndexId.equals(buffetMenu.getMenuIndexId())){
					
					return true;
				}
			}
		}
		return false;
		
	}
	/**
	 * 
	 * ?????????????????????????????????
	 * ?????????????????????
	 * ???????????????2019???8???2??? ??????11:12:41
	 * @param ??????
	 * @return ??????
	 */
	private List<MenuDict> getCatalogMenuDictList(String catalogIndexId,List<MenuDict> menuDictList,String whetherBuffet){
		  List<MenuDict> menuDictListNew=Lists.newArrayList();
		  for(MenuDict menuDict:menuDictList){
			  if(catalogIndexId.equals(menuDict.getCatalogIndexId())){
				  menuDict.setWhetherBuffet(whetherBuffet);
				  menuDictListNew.add(menuDict);
			  }
		  }
		  return menuDictListNew;
	}
	/**
	 * 
	 * ????????????????????????????????????
	 * ?????????????????????
	 * ???????????????2019???8???30??? ??????9:54:48
	 * @param ??????
	 * @return ??????
	 */
	@PostMapping("loadShopBuffetMenu")
	@ResponseBody
	public R loadShopBuffetMenu(String shopId,String accessToken,String languageType){
		if(WebplusUtil.isNotEmpty(shopId)){
			if(WebplusUtil.isEmpty(languageType)){
				languageType=BussCons.LANGUAGE_TYPE_JP;
			}
			Shop shop=shopService.selectById(shopId);
			String language=shop.getLanguage();
			if(language.indexOf(languageType)<0){
				languageType=BussCons.LANGUAGE_TYPE_JP;
			}
			Dto pDto=Dtos.newDto();
			String orderId=WebplusHashCodec.decryptBase64(accessToken);
			if(WebplusUtil.isNotEmpty(orderId)){
				EntityWrapper<OrderLine> lineWrapper = new EntityWrapper<OrderLine>();
				lineWrapper.eq("order_id", orderId);
				lineWrapper.eq("whether_buffet", WebplusCons.WHETHER_YES);
				List<OrderLine> orderLineList=orderLineService.selectList(lineWrapper);
				Dto catalogDto=this.getBuffetCatalog(orderLineList);
				String catalogIndexId=catalogDto.getString("catalogIndexId");
				pDto.put("catalogIndexId", catalogIndexId);
				if(WebplusUtil.isNotEmpty(catalogIndexId)){ //?????????????????????
					List<Dto> dataList=this.getBuffetMenuDict(shop, catalogIndexId, languageType,WebplusCons.WHETHER_YES);
				    return R.toDataAndList(catalogDto, dataList).put("whetherBuffet", WebplusCons.WHETHER_YES);
				}
				
			}
			String mealType=BussUtil.getMenuMealType(shop.getTimeLimitStart(),shop.getTimeLimit());  //??????????????????
			List<String> mealTypeList=Lists.newArrayList();
			mealTypeList.add(BussCons.MEAL_TYPE_COMMON);
			mealTypeList.add(mealType);
			pDto.put("mealTypeList", mealTypeList);
			pDto.put("shopId", shopId);
			pDto.put("whetherBuffet", WebplusCons.WHETHER_YES);
			pDto.put("languageType", languageType);
			pDto.setOrder(" sort_no ASC ");
			List<MenuCatalog> menuCatalogList=menuCatalogService.list(pDto);
			
			return R.toList(menuCatalogList).put("whetherBuffet",  WebplusCons.WHETHER_NO);
		}
		
		return R.toList(Lists.newArrayList());
	}
	/**
	 * 
	 * ?????????????????????????????????
	 * ?????????????????????
	 * ???????????????2019???8???2??? ??????10:54:12
	 * @param ??????
	 * @return ??????
	 */
	@PostMapping("queryMenuStepSpecList")
	@ResponseBody
	public R queryMenuStepSpecList(String menuIndexId,String languageType){
		
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
	 * ??????????????????????????????????????????
	 * ?????????????????????
	 * ???????????????2019???9???1??? ??????12:46:55
	 * @param ??????
	 * @return ??????
	 */
	@PostMapping("queryBuffetMenuList")
	@ResponseBody
	public R queryBuffetMenuList(String accessToken,String languageType){
		String orderId=WebplusHashCodec.decryptBase64(accessToken);
		if(WebplusUtil.isNotEmpty(orderId)){
			
			EntityWrapper<Desk> wrapper = new EntityWrapper<Desk>();
    		wrapper.eq("order_no", orderId);
    		Desk desk=deskService.selectOne(wrapper);
			
			if(WebplusUtil.isNotEmpty(desk)){
				if(WebplusUtil.isEmpty(languageType)){
					languageType=BussCons.LANGUAGE_TYPE_JP;
				}
				String shopId=desk.getShopId();
				Shop shop=shopService.selectById(shopId);
				String language=shop.getLanguage();
				if(language.indexOf(languageType)<0){
					languageType=BussCons.LANGUAGE_TYPE_JP;
				}
				
				Dto catalogDto=this.getBuffetDto(orderId);
				//???????????????????????????
				String catalogIndexId=catalogDto.getString("catalogIndexId");
				if(WebplusUtil.isNotEmpty(catalogIndexId)){
					List<Dto> dataList=this.getBuffetMenuDict(shop, catalogIndexId, languageType,WebplusCons.WHETHER_NO);
				    return R.toDataAndList(catalogDto, dataList);
				}
			}
			
		}
		return R.toDataAndList(null, Lists.newArrayList());
	}
	
	/**
	 * 
	 * ????????????????????????????????????
	 * ????????????????????????chenqiyuan@toonan.com???
	 * ??????????????? 2020???4???4??? ??????10:42:32 
	 * @param ??????
	 * @return ??????
	 */
	public Dto getBuffetDto(String orderId) {
		Dto limitDto=Dtos.newDto();
		String key=BussCons.BUFFET_ORDER_KEY+orderId;
		String value=WebplusCache.getString(key);
		
		if(WebplusUtil.isNotEmpty(value)) {
		    limitDto=WebplusJson.fromJson(value, HashDto.class);
			
		}
		
		return limitDto;
	}
	/**
	 * 
	 * ??????????????????????????????????????????
	 * ????????????????????????chenqiyuan@toonan.com???
	 * ??????????????? 2020???4???4??? ??????10:42:32 
	 * @param ??????
	 * @return ??????
	 */
	public boolean checkOrderBuffet(String orderId) {
		String key=BussCons.BUFFET_ORDER_KEY+orderId;
		String value=WebplusCache.getString(key);
		
		if(WebplusUtil.isNotEmpty(value)) {
		    
			return true;
			
		}
		
		return false;
	}
	/**
	 * 
	 * ????????????????????????????????????
	 * ?????????????????????
	 * ???????????????2019???10???19??? ??????12:06:19
	 * @param ??????
	 * @return ??????
	 */
	private List<Dto> getBuffetMenuDict(Shop shop,String catalogIndexId,String languageType,String whetherShowBuffet){
		List<Dto> dataList=Lists.newArrayList();
		if(WebplusCons.WHETHER_YES.equals(whetherShowBuffet)) {
			//?????????????????????
			Dto buffetDto=Dtos.newDto();
			String  buffetName=BussCache.getLanguageValue(languageType, BussCons.BUFFET_NAME_KEY);
			buffetDto.put("catalogIndexId", catalogIndexId);
			buffetDto.put("catalogName", buffetName);
			List<MenuDict> buffetMenuList=Lists.newArrayList();
			//?????????????????????
			EntityWrapper<MenuCatalog> wrapper = new EntityWrapper<MenuCatalog>();
			wrapper.eq("catalog_index_id",catalogIndexId);
			wrapper.eq("language_type", languageType);
			MenuCatalog menuCatalog=menuCatalogService.selectOne(wrapper);
			MenuDict catalogMenu=new MenuDict();
			catalogMenu.setCatalogIndexId(catalogIndexId);
			catalogMenu.setMenuId(menuCatalog.getCatalogId());
			catalogMenu.setMenuName(menuCatalog.getCatalogName());
			catalogMenu.setMenuIndexId(catalogIndexId);
			catalogMenu.setMenuPrice(menuCatalog.getBuffetPrice());
			catalogMenu.setMenuImage(menuCatalog.getBuffetImage());
			catalogMenu.setMenuIntroduce(menuCatalog.getBuffetIntroduce());
			buffetMenuList.add(catalogMenu);
			buffetDto.put("menuDictList", buffetMenuList);
			dataList.add(buffetDto);
		}
		
		Dto pDto = Dtos.newDto();
		String mealType=BussUtil.getMenuMealType(shop.getTimeLimitStart(),shop.getTimeLimit());  //?????????????????? 
		List<String> mealTypeList=Lists.newArrayList();
		mealTypeList.add(BussCons.MEAL_TYPE_COMMON);
		mealTypeList.add(mealType);
		pDto.put("mealTypeList", mealTypeList);
		pDto.put("shopId",shop.getShopId());
		pDto.put("languageType",languageType);
		pDto.put("inCatalogIndexId", catalogIndexId);
		pDto.setOrder(" b.whether_top DESC, b.sort_no ASC,a.sort_no ASC");
	    List<MenuDict> menuDictList=bussCommonService.listBuffetMenuDict(pDto);
	    List<Dto> catalogList=getCatalogList(menuDictList);
	    for(Dto dataDto:catalogList){
		    String catalogIndexIdTmp=dataDto.getString("catalogIndexId");
			List<MenuDict> menuDictListNew=this.getCatalogMenuDictList(catalogIndexIdTmp, menuDictList,WebplusCons.WHETHER_YES);
			dataDto.put("menuDictList", menuDictListNew);
			 dataList.add(dataDto);
		}
	   
	    return  dataList;
	}
	/**
	 * 
	 * ????????????????????????????????????????????????
	 * ?????????????????????
	 * ???????????????2019???9???1??? ??????1:42:21
	 * @param ??????
	 * @return ??????
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
				
				dataList.add(dataDto);
			}
				
		}
		catalogDto.clear();
		return dataList;
	}
	/**
	 * 
	 * ????????????????????????????????????
	 * ?????????????????????
	 * ???????????????2019???9???1??? ??????12:59:46
	 * @param ??????
	 * @return ??????
	 */
	private Dto getBuffetCatalog(List<OrderLine> orderLineList){
		Dto catalogDto=Dtos.newDto();
		if(WebplusUtil.isNotEmpty(orderLineList)){
			String buffetCatalogIndexId="";
			int totalChooseNum=0;
			int chooseNum=0;
			for(OrderLine orderLine:orderLineList){
			   String whetherSpec=orderLine.getWhetherSpec();
			   String menuIndexId=orderLine.getMenuIndexId();
			   String catalogIndexId=orderLine.getCatalogIndexId();
			   if(WebplusUtil.isNotEmpty(catalogIndexId)&&WebplusUtil.isEmpty(menuIndexId)){
				   buffetCatalogIndexId=catalogIndexId;
				   totalChooseNum+=orderLine.getChooseNum();
			   }
			   if(WebplusUtil.isNotEmpty(menuIndexId)&&WebplusCons.WHETHER_NO.equals(whetherSpec)){ //????????????
				   chooseNum+=orderLine.getBuyNum();
				   
			   }
			}
			int remainChooseNum=totalChooseNum-chooseNum;
			catalogDto.put("catalogIndexId", buffetCatalogIndexId);
			catalogDto.put("totalChooseNum",totalChooseNum);
			catalogDto.put("chooseNum", chooseNum);
			catalogDto.put("remainChooseNum", remainChooseNum);
		}
		
		return catalogDto;
	}
	/**
	 * 
	 * ?????????????????????????????????
	 * ?????????????????????
	 * ???????????????2019???8???2??? ??????10:54:12
	 * @param ??????
	 * @return ??????
	 */
	@PostMapping("queryMenuSpecList")
	@ResponseBody
	public R queryMenuSpecList(String menuIndexId,String languageType){
		
		if(WebplusUtil.isNotEmpty(menuIndexId)){
			if(WebplusUtil.isEmpty(languageType)){
				languageType=BussCons.LANGUAGE_TYPE_JP;
			}
			EntityWrapper<MenuSpec> wrapper = new EntityWrapper<MenuSpec>();
    		wrapper.eq("menu_index_id", menuIndexId);
    		wrapper.eq("language_type", languageType);
    		List<MenuSpec> menuSpecList=menuSpecService.selectList(wrapper);
    		return R.toList(menuSpecList);
		}
		return R.toList(Lists.newArrayList());
	}
	
	/**
	 * 
	 * ????????????????????????????????????
	 * ?????????????????????
	 * ???????????????2019???9???1??? ??????5:05:51
	 * @param ??????
	 * @return ??????
	 */
	@PostMapping("saveBuffetOrderNew")
	@ResponseBody
	public R saveBuffetOrderNew(String accessToken,String buffetLines){
		String orderId=WebplusHashCodec.decryptBase64(accessToken);
		Date currentTime=WebplusUtil.getDateTime();
		if(WebplusUtil.isNotEmpty(orderId)){
			
			Order order=orderService.selectById(orderId);
			if(WebplusUtil.isNotEmpty(order)){ //????????????????????????
				String shopId=order.getShopId();
				String key=BussCons.ORDER_LIMIT_KEY+shopId;
				if(WebplusCache.exists(key)) {
					return R.warn();
				}
				if(WebplusCons.WHETHER_NO.equals(order.getWhetherPay())){  //???????????????????????????
					
					List<OrderLine> buffetLineList=this.getBuffetLine(buffetLines, orderId, shopId);
					if(WebplusUtil.isNotEmpty(buffetLineList)){
						OrderLine entity=new OrderLine();
	    				entity.setWhetherAdd(WebplusCons.WHETHER_NO); //???????????????
	    				EntityWrapper<OrderLine> wrapper = new EntityWrapper<OrderLine>();
	    				wrapper.eq("whether_add", WebplusCons.WHETHER_YES);
	    				wrapper.eq("order_id", orderId);
	    				orderLineService.update(entity, wrapper);
	    				boolean result=orderLineService.insertBatch(buffetLineList);
	    				if(result){
	    					//????????????
                            this.setBuffetOderCache(orderId, buffetLineList);
	    					EntityWrapper<Desk> deskWrapper = new EntityWrapper<Desk>();
		   				    deskWrapper.eq("order_no", orderId);
		   		    		Desk desk=deskService.selectOne(deskWrapper);
		   		    		if(WebplusUtil.isNotEmpty(desk)){
		   	 				   printAsyncTask.batchSendPrintMenu(desk.getDeskName(), buffetLineList);
		   		    		}
	    				}
	   				    
					}
 				 
				}
			
	
			}else{
				EntityWrapper<Desk> wrapper = new EntityWrapper<Desk>();
	    		wrapper.eq("order_no", orderId);
	    		Desk desk=deskService.selectOne(wrapper);
	    		if(WebplusUtil.isNotEmpty(desk)){
	    			String shopId=desk.getShopId();
	    			List<OrderLine> buffetLineList=this.getBuffetLine(buffetLines, orderId, shopId);
	    			if(WebplusUtil.isNotEmpty(buffetLineList)){
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
	    				desk.setDeskStatus(BussCons.DESK_STATUS_FULL);
	    				desk.setOrderTime(currentTime);
	    				desk.setUpdateTime(WebplusUtil.getDateTime());
	    				deskService.updateById(desk);
	    				boolean result=orderService.saveOrder(order, buffetLineList, desk);
	    				if(result){
	    					//????????????
                            this.setBuffetOderCache(orderId, buffetLineList);
                            //??????????????????
			    		    printAsyncTask.batchSendPrintMenu(desk.getDeskName(), buffetLineList);
	    				}
	    			    
	    			}	 
	    			
	    		}
		}
		}
	   return R.ok();
	}
	/**
	 * ?????????????????????
	 * @param buffetLineList
	 */
	private void setBuffetOderCache(String orderId,List<OrderLine> buffetLineList) {
		if(WebplusUtil.isNotEmpty(buffetLineList)) {
			String key=BussCons.BUFFET_ORDER_KEY+orderId;
			int chooseNum=this.getChooseNum(buffetLineList);
			OrderLine orderLine=buffetLineList.get(0);
			String catalogIndexId=orderLine.getCatalogIndexId();
			if(WebplusCache.exists(key)) {
				String value=WebplusCache.getString(key);
				if(WebplusUtil.isNotEmpty(value)) {
					Dto limitDto=WebplusJson.fromJson(value, HashDto.class);
					String orderTime=limitDto.getString("orderTime");
					long timeLimit=limitDto.getLong("timeLimit");
					int timeLimitInt=limitDto.getInteger("timeLimit");
					
					if(BussUtil.checkBuffetTimeLimit(orderTime, timeLimit)) {
						this.setBuffetCache(key, catalogIndexId,chooseNum);
					}else {
						Integer chooseNumCache=limitDto.getInteger("totalChooseNum");
						int totalNum=BussUtil.dealEmptyAmount(chooseNumCache)+chooseNum;
						limitDto.put("totalChooseNum",totalNum);
						String valueNew=WebplusJson.toJson(limitDto);
						WebplusCache.setString(key, valueNew,(timeLimitInt+60)*60);
					}
				}
				
			}else {
				this.setBuffetCache(key, catalogIndexId,chooseNum);
			}
			
		}
		
	}
	/***
	 * 
	 * ???????????????
	 * ????????????????????????chenqiyuan@toonan.com???
	 * ??????????????? 2020???4???3??? ??????10:24:46 
	 * @param ??????
	 * @return ??????
	 */
	private int getChooseNum(List<OrderLine> buffetLineList) {
		int chooseNum=0;
		if(WebplusUtil.isNotEmpty(buffetLineList)) {
			for(OrderLine orderLine:buffetLineList) {
				chooseNum+=BussUtil.dealEmptyAmount(orderLine.getChooseNum());
			}
		}
		return chooseNum;
	}
	/**
	 * ????????????
	 * @param key
	 * @param buffetLineList
	 */
	private void setBuffetCache(String key,String catalogIndexId,int chooseNum) {
		
		MenuCatalog menuCatalog=menuCatalogService.selectById(catalogIndexId);
		if(WebplusUtil.isNotEmpty(menuCatalog)) {
			Integer timeLimit=menuCatalog.getTimeLimit();
			if(WebplusUtil.isEmpty(timeLimit)) {
				timeLimit=0;
			}
			
			Dto dataDto=Dtos.newDto();
			dataDto.put("orderTime", WebplusUtil.getDateStr(WebplusCons.DATETIME));
			dataDto.put("timeLimit", timeLimit);
			dataDto.put("catalogIndexId", catalogIndexId);
			dataDto.put("totalChooseNum", chooseNum);
			String value=WebplusJson.toJson(dataDto);
			if(timeLimit==0) {
				WebplusCache.setString(key, value,60*60*5);
			}else {
				WebplusCache.setString(key, value,(timeLimit+60)*60);
				
			}
			
			
			
		}
	}
	/**
	 * 
	 * ???????????????
	 * ?????????????????????
	 * ???????????????2020???1???10??? ??????10:53:16
	 * @param ??????
	 * @return ??????
	 */
	private List<OrderLine> getBuffetLine(String buffetLines,String orderId,String shopId){
		List<OrderLine> orderLineList=Lists.newArrayList();
		Date currentTime=WebplusUtil.getDateTime();
		if(WebplusUtil.isNotEmpty(buffetLines)){  //??????????????????
			 List<Dto> lineDtoList=WebplusJson.fromJson(buffetLines);
			 if(WebplusUtil.isNotEmpty(lineDtoList)){
				 for(Dto lineDto:lineDtoList){
					 String catalogIndexId=lineDto.getString("catalogIndexId");
					 Integer buyNum=lineDto.getInteger("buyNum");
					 if(WebplusUtil.isNotAnyEmpty(catalogIndexId,buyNum)){
						  
						   MenuCatalog menuCatalog=menuCatalogService.selectById(catalogIndexId);
						   if(WebplusUtil.isNotEmpty(menuCatalog)){
							   OrderLine orderLine=new OrderLine();
		 					   orderLine.setOrderId(orderId);
		 					   orderLine.setCatalogIndexId(catalogIndexId);
		   				       orderLine.setShopId(shopId);
		   				       orderLine.setBuyNum(buyNum);
		   				       orderLine.setCreateTime(currentTime);
		   				       orderLine.setUpdateTime(currentTime);
		   				       orderLine.setPrintCatalogIndexId(catalogIndexId);
		   				       orderLine.setMenuName(menuCatalog.getCatalogName());
		   				       orderLine.setMenuPrice(menuCatalog.getBuffetPrice());
		   				       int chooseNum=buyNum*menuCatalog.getLimitNum();
		   				       orderLine.setChooseNum(chooseNum);
		   				       orderLine.setPrintMenuName(menuCatalog.getCatalogName());
		   				       orderLine.setWhetherAdd(WebplusCons.WHETHER_NO);
		   				       orderLine.setWhetherSpec(WebplusCons.WHETHER_NO);
		   				       orderLine.setWhetherBuffet(WebplusCons.WHETHER_YES);
		   				       orderLine.setTaxType(menuCatalog.getTaxType());
							   String specIndexId=lineDto.getString("specIndexId");
							   if(WebplusUtil.isNotEmpty(specIndexId)){
								   BuffetSpec buffetSpec=buffetSpecService.selectById(specIndexId);
								   if(WebplusUtil.isNotEmpty(buffetSpec)){
									   orderLine.setWhetherSpec(WebplusCons.WHETHER_YES);
									   String menuName=menuCatalog.getCatalogName()+"???"+buffetSpec.getSpecName()+"???";
									   orderLine.setMenuName(menuName);
									   orderLine.setPrintMenuName(menuName);
									   orderLine.setMenuPrice(buffetSpec.getSpecPrice());
								   }
							   }
							   orderLine.setSpecIndexId(specIndexId);
							   orderLineList.add(orderLine);
						   }
						
					 }
				 }
				
			 }
		}
		
		return orderLineList;
	}
	/**
	 * 
	 * ????????????????????????????????????
	 * ?????????????????????
	 * ???????????????2019???9???1??? ??????5:05:51
	 * @param ??????
	 * @return ??????
	 */
	@PostMapping("saveBuffetOrder")
	@ResponseBody
	public R saveBuffetOrder(String accessToken,String catalogIndexId,int buyNum){
		String orderId=WebplusHashCodec.decryptBase64(accessToken);
		Date currentTime=WebplusUtil.getDateTime();
		if(WebplusUtil.isNotEmpty(orderId)){
			Order order=orderService.selectById(orderId);
			if(WebplusUtil.isNotEmpty(order)){ //????????????????????????
				if(WebplusCons.WHETHER_NO.equals(order.getWhetherPay())){  //???????????????????????????
					String shopId=order.getShopId();
					OrderLine entity=new OrderLine();
    				entity.setWhetherAdd(WebplusCons.WHETHER_NO); //???????????????
    				EntityWrapper<OrderLine> wrapper = new EntityWrapper<OrderLine>();
    				wrapper.eq("whether_add", WebplusCons.WHETHER_YES);
    				wrapper.eq("order_id", orderId);
    				orderLineService.update(entity, wrapper);
    				MenuCatalog menuCatalog=menuCatalogService.selectById(catalogIndexId);
 				   if(WebplusUtil.isNotEmpty(menuCatalog)){ //?????????????????????
 					   OrderLine orderLine=new OrderLine();
 					   orderLine.setOrderId(orderId);
 					   orderLine.setCatalogIndexId(catalogIndexId);
   				       orderLine.setShopId(shopId);
   				       orderLine.setBuyNum(buyNum);
   				       orderLine.setCreateTime(currentTime);
   				       orderLine.setUpdateTime(currentTime);
   				       orderLine.setPrintCatalogIndexId(catalogIndexId);
   				       orderLine.setMenuName(menuCatalog.getCatalogName());
   				       orderLine.setMenuPrice(menuCatalog.getBuffetPrice());
   				       int chooseNum=buyNum*menuCatalog.getLimitNum();
   				       orderLine.setChooseNum(chooseNum);
   				       orderLine.setPrintMenuName(menuCatalog.getCatalogName());
   				       orderLine.setWhetherAdd(WebplusCons.WHETHER_NO);
   				       orderLine.setWhetherSpec(WebplusCons.WHETHER_NO);
   				       orderLine.setWhetherBuffet(WebplusCons.WHETHER_YES);
   				       orderLine.setTaxType(menuCatalog.getTaxType());
   				       orderLineService.insert(orderLine); 
   				    EntityWrapper<Desk> deskWrapper = new EntityWrapper<Desk>();
   				    deskWrapper.eq("order_no", orderId);
   		    		Desk desk=deskService.selectOne(deskWrapper);
   		    		if(WebplusUtil.isNotEmpty(desk)){
   		    		   List<OrderLine> orderLineList=Lists.newArrayList();
   	 				   orderLineList.add(orderLine);
   	 				   //??????????????????
   	 				   printAsyncTask.batchSendPrintMenu(desk.getDeskName(), orderLineList);
   		    		}
   				 
 				   }
				}
			}else{
				EntityWrapper<Desk> wrapper = new EntityWrapper<Desk>();
	    		wrapper.eq("order_no", orderId);
	    		Desk desk=deskService.selectOne(wrapper);
	    		if(WebplusUtil.isNotEmpty(desk)){
	    			String shopId=desk.getShopId();
	    			int eatNum=desk.getEatNum();
	    			
	    			order=new Order();
    				order.setOrderId(orderId);
    				order.setEatNum(eatNum);
    				order.setShopId(shopId);
    				order.setDeskId(desk.getDeskId());
    				order.setDeskName(desk.getDeskName());
    				order.setOrderTime(currentTime);
    				order.setCreateTime(currentTime);
    				boolean result=orderService.insert(order);	
	    			if(result){
	    				   MenuCatalog menuCatalog=menuCatalogService.selectById(catalogIndexId);
	    				   if(WebplusUtil.isNotEmpty(menuCatalog)){ //?????????????????????
	    					   OrderLine orderLine=new OrderLine();
	    					   orderLine.setOrderId(orderId);
		    				   orderLine.setCatalogIndexId(catalogIndexId);
		    				   orderLine.setPrintCatalogIndexId(catalogIndexId);
		    				   orderLine.setShopId(shopId);
		    				   orderLine.setBuyNum(buyNum);
		    				   orderLine.setCreateTime(currentTime);
		    				   orderLine.setUpdateTime(currentTime);
		    				   orderLine.setMenuName(menuCatalog.getCatalogName());
		    				   orderLine.setMenuPrice(menuCatalog.getBuffetPrice());
		    				   int chooseNum=buyNum*menuCatalog.getLimitNum();
		    				   orderLine.setChooseNum(chooseNum);
		    				   orderLine.setPrintMenuName(menuCatalog.getCatalogName());
		    				   orderLine.setWhetherAdd(WebplusCons.WHETHER_NO);
		    				   orderLine.setWhetherSpec(WebplusCons.WHETHER_NO);
		    				   orderLine.setWhetherBuffet(WebplusCons.WHETHER_YES);
		    				   orderLine.setTaxType(menuCatalog.getTaxType());
		    				   orderLineService.insert(orderLine);
		    				   List<OrderLine> orderLineList=Lists.newArrayList();
		    				   orderLineList.add(orderLine);
		    				   //??????????????????
		    				   printAsyncTask.batchSendPrintMenu(desk.getDeskName(), orderLineList);
	    				   }
	    				  
	    				   desk.setDeskStatus(BussCons.DESK_STATUS_FULL);
	    				   desk.setOrderTime(currentTime);
	    				   desk.setUpdateTime(WebplusUtil.getDateTime());
	    				   deskService.updateById(desk);
	    					return R.ok();
	    			  }
	    		  }
	    		}
			
		}
		
	   return R.ok();
	}
	/**
	 * 
	 * ???????????????????????????
	 * ?????????????????????
	 * ???????????????2019???8???2??? ??????11:51:09
	 * @param ??????
	 * @return ??????
	 */
	@PostMapping("saveOrder")
	@ResponseBody
	public R saveOrder(String accessToken,String orderLines){
		String orderId=WebplusHashCodec.decryptBase64(accessToken);
		
		if(WebplusUtil.isNotAnyEmpty(orderId,orderLines)){
			
			Order order=orderService.selectById(orderId);
	
			if(WebplusUtil.isNotEmpty(order)){  //????????????????????????
				String shopId=order.getShopId();
				String key=BussCons.ORDER_LIMIT_KEY+shopId;
				if(WebplusCache.exists(key)) {
					return R.warn();
				}
				if(WebplusCons.WHETHER_NO.equals(order.getWhetherPay())){  //???????????????????????????
					
	    			List<OrderLine> orderLineList=this.getAddOrderLine(shopId,orderId, orderLines, WebplusCons.WHETHER_YES);
	    			if(WebplusUtil.isNotEmpty(orderLineList)){
	    				
	    				OrderLine orderLine=new OrderLine();
	    				orderLine.setWhetherAdd(WebplusCons.WHETHER_NO); //???????????????
	    				EntityWrapper<OrderLine> wrapper = new EntityWrapper<OrderLine>();
	    				wrapper.eq("whether_add", WebplusCons.WHETHER_YES);
	    				wrapper.eq("order_id", orderId);
	    				orderLineService.update(orderLine, wrapper);
	    				boolean result=orderLineService.insertBatch(orderLineList);
	    			   if( result){
	    				  
	    				   printAsyncTask.batchSendPrintMenu(order.getDeskName(), orderLineList); //??????????????????
	    				   return R.ok();
	    			   }
	    			}
				}
				
			}else{
				Date currentTime=WebplusUtil.getDateTime();
				EntityWrapper<Desk> wrapper = new EntityWrapper<Desk>();
	    		wrapper.eq("order_no", orderId);
	    		Desk desk=deskService.selectOne(wrapper);
	    		if(WebplusUtil.isNotEmpty(desk)){
	    			String shopId=desk.getShopId();
	    			int eatNum=desk.getEatNum();
	    			List<OrderLine> orderLineList=this.getAddOrderLine(shopId, orderId,orderLines, WebplusCons.WHETHER_NO);
	    			if(WebplusUtil.isNotEmpty(orderLineList)){
	    				order=new Order();
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
    					boolean result=orderService.saveOrder(order, orderLineList, desk);
	    				if(result){
	    				   
	    				   printAsyncTask.batchSendPrintMenu(desk.getDeskName(), orderLineList); //??????????????????
	    					return R.ok();
	    				}
	    			}
	    		}
			}
			
		}
		return R.ok();
	}
	
	/**
	 * 
	 * ???????????????????????????????????????????????????
	 * ?????????????????????
	 * ???????????????2020???1???5??? ??????9:31:52
	 * @param ??????
	 * @return ??????
	 *//*
	private void saveOrderLimit(String shopId,String orderId){
		Shop shop=shopService.selectById(shopId);
		if(WebplusUtil.isNotEmpty(shop)){
			Integer orderLimit=shop.getOrderLimit();
			if(WebplusUtil.isNotEmpty(orderLimit)&&orderLimit.intValue()>0){ //??????????????????????????????
				String key=BussCons.ORDER_LIMIT+orderId;
				WebplusCache.setString(key,orderId ,orderLimit*60);
			}
		}
	}*/
	/**
	 * 
	 * ?????????????????????????????????
	 * ?????????????????????
	 * ???????????????2019???8???2??? ??????11:51:09
	 * @param ??????
	 * @return ??????
	 */
	@PostMapping("saveAddToOrder")
	@ResponseBody
	public R saveAddToOrder(String accessToken,String orderLines){
		String orderId=WebplusHashCodec.decryptBase64(accessToken);
		if(WebplusUtil.isNotAnyEmpty(orderId,orderLines)){
		   Order order=orderService.selectById(orderId);
    		if(WebplusUtil.isNotEmpty(order)){
    			if(WebplusCons.WHETHER_NO.equals(order.getWhetherPay())){  //???????????????????????????
    				String shopId=order.getShopId();
        			List<OrderLine> orderLineList=this.getAddOrderLine(shopId,orderId, orderLines, WebplusCons.WHETHER_YES);
        			if(WebplusUtil.isNotEmpty(orderLineList)){
        				OrderLine orderLine=new OrderLine();
        				orderLine.setWhetherAdd(WebplusCons.WHETHER_NO); //???????????????
        				EntityWrapper<OrderLine> wrapper = new EntityWrapper<OrderLine>();
        				wrapper.eq("whether_add", WebplusCons.WHETHER_YES);
        				wrapper.eq("order_id", orderId);
        				orderLineService.update(orderLine, wrapper);
        				boolean result=orderLineService.insertBatch(orderLineList);
        			   if( result){
        				   printAsyncTask.batchSendPrintMenu(order.getDeskName(), orderLineList); //??????????????????
        				   return R.ok();
        			   }
        			}
    			}
    		
    		}
		}
		return R.ok();
	}
	
	  /**
     * 
     * ??????????????????????????????
     * ?????????????????????
     * ???????????????2019???7???31??? ??????9:27:49
     * @param ??????
     * @return ??????
     */
	@PostMapping("callBuffetOrder")
	@ResponseBody
	public R  callBuffetOrder(String accessToken){
		String orderId=WebplusHashCodec.decryptBase64(accessToken);
        if(WebplusUtil.isNotEmpty(orderId)){
        	EntityWrapper<Desk> wrapper = new EntityWrapper<Desk>();
    		wrapper.eq("order_no", orderId);
    		Desk desk=deskService.selectOne(wrapper);
    		if(WebplusUtil.isNotEmpty(desk)){
    			String shopId=desk.getShopId();
    			Shop shop=shopService.selectById(shopId);
    			if(WebplusUtil.isNotEmpty(shop)){
    				String deskName=desk.getDeskName();
    				
    				String pushType=shop.getPushType();
    				if(BussCons.PUSH_TYPE_ALL.equals(pushType)||
    						BussCons.PUSH_TYPE_APP.equals(pushType)){
    					String message=deskName+"     ???????????????????????????";
    					System.out.println(message);
    					 AppPushServer.sendMessage(message,shopId);
    				}
        	       
    				if(BussCons.PUSH_TYPE_ALL.equals(pushType)||
    						BussCons.PUSH_TYPE_PRINT.equals(pushType)){
    					String pushMessage=BussUtil.createFzPrintMenuContent(deskName,"???????????????????????????");
    					printAsyncTask.sendMessagePrint(shopId, desk.getDeskName(), pushMessage);
   				    }
        			return R.ok();
    			}
    			
    		}
        }
	    return R.error();
	}
	
	  /**
     * 
     * ??????????????????????????????
     * ?????????????????????
     * ???????????????2019???7???31??? ??????9:27:49
     * @param ??????
     * @return ??????
     */
	@PostMapping("callWaiter")
	@ResponseBody
	public R  callWaiter(String accessToken){
		return this.sendPushMessage(accessToken, " ?????????");
	}
	/**
     * 
     * ???????????????????????????
     * ?????????????????????
     * ???????????????2019???7???31??? ??????9:27:49
     * @param ??????
     * @return ??????
     */
	@PostMapping("callTally")
	@ResponseBody
	public R  callTally(String accessToken){
		
		return this.sendPushMessage(accessToken, " ??????");
	}
	/**
	 * 
	 * ?????????????????????????????????
	 * ?????????????????????
	 * ???????????????2020???1???15??? ??????10:10:25
	 * @param ??????
	 * @return ??????
	 */
	private R sendPushMessage(String accessToken,String message){
		String orderId=WebplusHashCodec.decryptBase64(accessToken);
        if(WebplusUtil.isNotEmpty(orderId)){
        	EntityWrapper<Desk> wrapper = new EntityWrapper<Desk>();
    		wrapper.eq("order_no", orderId);
    		Desk desk=deskService.selectOne(wrapper);
    		if(WebplusUtil.isNotEmpty(desk)){
    			String shopId=desk.getShopId();
    			Shop shop=shopService.selectById(shopId);
    			if(WebplusUtil.isNotEmpty(shop)){
    				
    				String pushType=shop.getPushType();
    				if(BussCons.PUSH_TYPE_ALL.equals(pushType)||
    						BussCons.PUSH_TYPE_APP.equals(pushType)){
    					String pushMessage=desk.getDeskName()+" "+message;
    					 AppPushServer.sendMessage(pushMessage,shopId);
    				}
        	       
    				if(BussCons.PUSH_TYPE_ALL.equals(pushType)||
    						BussCons.PUSH_TYPE_PRINT.equals(pushType)){
    					String pushMessage="<center><FS2><FH2><FB>"+desk.getDeskName()+message+"</FB></FH2></FS2>\n\n\n";
    					printAsyncTask.sendMessagePrint(shopId, desk.getDeskName(), pushMessage);
   				    }
        			return R.ok();
    			}
    			
    		}
        }
	    return R.error();
	}
	/**
	 * 
	 * ?????????????????????????????????
	 * ?????????????????????
	 * ???????????????2019???8???3??? ??????3:38:43
	 * @param ??????
	 * @return ??????
	 */
	@PostMapping("queryOrderLineList")
	@ResponseBody
	public R queryOrderLineList(String accessToken,String languageType){
		String orderId=WebplusHashCodec.decryptBase64(accessToken);
		if(WebplusUtil.isNotEmpty(orderId)){
			Order order=orderService.selectById(orderId);
			if(WebplusUtil.isNotEmpty(order)){
				if(WebplusUtil.isEmpty(languageType)){
					languageType=BussCons.LANGUAGE_TYPE_JP;
				}
				Dto pDto=Dtos.newDto();
				pDto.put("orderId", orderId);
				List<OrderLine> orderLineList=orderLineService.list(pDto);
				orderLineList=this.removeWasteData(orderLineList);
				int menuAmount=0;
				List<Dto> dataList=Lists.newArrayList();
				List<String> menuIndexIdList=Lists.newArrayList();
				List<String> specIndexIdList=Lists.newArrayList();
				List<Dto> buffetOrderList=Lists.newArrayList();// ???????????????
				List<String> catalogIndexIdList=Lists.newArrayList();
				List<String> buffetSpecIndexIdList=Lists.newArrayList();
				String bufferCatalogName="";
				int outAmountBefore=0;  //??????????????????
				int outAmountAfter=0;  //??????????????????
				int eatAmountBefore=0;  //??????????????????
				int eatAmountAfter=0;  //??????????????????
				for(OrderLine orderLine:orderLineList){
					
					String whetherBuffet=orderLine.getWhetherBuffet();
					String specIndexId=orderLine.getSpecIndexId();
					String menuIndexId=orderLine.getMenuIndexId();
					String catalogIndexId=orderLine.getCatalogIndexId();
					Integer subAmount=BussUtil.dealEmptyAmount(orderLine.getSubAmount());
					Integer subRate=BussUtil.dealEmptyAmount(orderLine.getSubRate());
					Dto dataDto=Dtos.newDto();
					WebplusUtil.copyProperties(orderLine, dataDto);
					if(WebplusUtil.isNotEmpty(menuIndexId)&&WebplusCons.WHETHER_YES.equals(whetherBuffet)){
						buffetOrderList.add(dataDto);
						
					}else{
						Integer buyNum=BussUtil.dealEmptyAmount(orderLine.getBuyNum());
						Integer menuPrice=BussUtil.dealEmptyAmount(orderLine.getMenuPrice());
						int singleSumPrice=menuPrice*buyNum;
						
						String taxType=orderLine.getTaxType();
						if(WebplusCons.WHETHER_YES.equals(orderLine.getWhetherTakeOut())){  //????????????
							if(BussCons.TAX_TYPE_BEFORE.equals(taxType	)){
								outAmountBefore+=BussUtil.countSubRate(singleSumPrice, subAmount, subRate);
							}else{
								outAmountAfter+=BussUtil.countSubRate(singleSumPrice, subAmount, subRate);;
							}
							
						}else{
							if(BussCons.TAX_TYPE_BEFORE.equals(taxType	)){
								eatAmountBefore+=BussUtil.countSubRate(singleSumPrice, subAmount, subRate);;
							}else{
								eatAmountAfter+=BussUtil.countSubRate(singleSumPrice, subAmount, subRate);;
							}
							
						}
						
						dataDto.put("singleSumPrice", singleSumPrice);
						dataList.add(dataDto);
					}
					if((WebplusUtil.isEmpty(menuIndexId)&&WebplusCons.WHETHER_YES.equals(whetherBuffet))
							||BussCons.SERVER_MENU_TYPE.equals(whetherBuffet)){
						if(WebplusUtil.isNotEmpty(catalogIndexId)){
							catalogIndexIdList.add(catalogIndexId);
						    if(WebplusCons.WHETHER_YES.equals(whetherBuffet)){
						    	  bufferCatalogName=orderLine.getMenuName();
						    }
						 
						}
						if(WebplusUtil.isNotEmpty(specIndexId)){
							buffetSpecIndexIdList.add(specIndexId);
						}
						
					}
					if(WebplusUtil.isNotEmpty(menuIndexId)){
						menuIndexIdList.add(menuIndexId);
					}
					
					if(WebplusUtil.isNotEmpty(specIndexId)){
						specIndexIdList.add(specIndexId);
					}
				}
				//????????????
				Shop shop=shopService.selectById(order.getShopId());
				Integer tableAmount=BussUtil.dealEmptyAmount(shop.getTableAmount());
			   // Integer taxes=BussUtil.dealEmptyAmount(shop.getTaxes());
				Integer eatNum=BussUtil.dealEmptyAmount(order.getEatNum());
				menuAmount=eatAmountBefore+eatAmountAfter+outAmountBefore+outAmountAfter;  //???????????????
				int deskAmount=eatNum*tableAmount;
				int taxAmountBefore=BussUtil.countTaxes(0, eatAmountBefore, BussCons.EAT_TAXES);
				int outTaxAmountBefore=BussUtil.countTaxes(0, outAmountBefore, BussCons.OUT_TAXES);
				int totalAmount=deskAmount+menuAmount+taxAmountBefore+outTaxAmountBefore;
				int taxAmountAfter=BussUtil.countAfterTaxes(deskAmount,eatAmountAfter,BussCons.EAT_TAXES);
				int outTaxAmountAfter=BussUtil.countAfterTaxes(outAmountAfter,BussCons.OUT_TAXES);
				int outTaxAmount= outTaxAmountBefore+ outTaxAmountAfter;
				int taxAmount= taxAmountBefore+taxAmountAfter;
				int subRate=BussUtil.dealEmptyAmount(order.getSubRate());
				int subAmount=BussUtil.dealEmptyAmount(order.getSubAmount());
				totalAmount=BussUtil.countSubRate(totalAmount, subAmount, subRate);
				order.setTaxAfterAmount(eatAmountAfter+outAmountAfter);
				order.setTaxBeforeAmount(eatAmountBefore+outAmountBefore);
				order.setDeskAmount(deskAmount);
				order.setMenuAmount(menuAmount);
				order.setOutTaxAmount(outTaxAmount);
				order.setSmallTotalAmount(menuAmount+deskAmount);
				order.setTotalAmount(totalAmount);
				order.setTaxAmount(taxAmount);
				order.setConsumeTax(taxAmountBefore);
				Dto orderDto=Dtos.newDto();
				WebplusUtil.copyProperties(order, orderDto);
				
				if(!BussCons.LANGUAGE_TYPE_JP.equals(languageType)){ //?????????????????????????????????????????????
					List<MenuCatalog> menuCatalogList=this.listBuffetCatalog(catalogIndexIdList, languageType);
					List<MenuDict> menuDictList=this.listMenuDict(menuIndexIdList, languageType);
					List<StepSpec> stepSpecList=this.listStepSpec(specIndexIdList, languageType);
					List<BuffetSpec> buffetSpecList=this.listBuffetSpec(buffetSpecIndexIdList, languageType);
					for(Dto dataDto:dataList){
						String catalogIndexId=dataDto.getString("catalogIndexId");
						String menuIndexId=dataDto.getString("menuIndexId");
						String specIndexId=dataDto.getString("specIndexId");
						String whetherBuffet=dataDto.getString("whetherBuffet");
						if((WebplusUtil.isNotEmpty(catalogIndexId)&&WebplusCons.WHETHER_YES.equals(whetherBuffet))
								||BussCons.SERVER_MENU_TYPE.equals(whetherBuffet)){
							MenuCatalog menuCatalog=this.getMenuCatalog(catalogIndexId, menuCatalogList, languageType);
							if(WebplusUtil.isNotEmpty(menuCatalog)){
								dataDto.put("menuName", menuCatalog.getCatalogName());
								if(WebplusUtil.isNotEmpty(specIndexId)){
									BuffetSpec buffetSpec=this.getBuffetSpec(specIndexId, buffetSpecList, languageType);
									if(WebplusUtil.isNotEmpty(buffetSpec)){
										dataDto.put("menuName", menuCatalog.getCatalogName()+"???"+buffetSpec.getSpecName()+"???");
									}
									
								}
								 if(WebplusCons.WHETHER_YES.equals(whetherBuffet)){
									 bufferCatalogName=menuCatalog.getCatalogName();
								 }
								
							}
							
						}else{
							
							if(WebplusUtil.isEmpty(specIndexId)){
								MenuDict menuDict=this.getMenuDict(menuIndexId, menuDictList, languageType);
								if(WebplusUtil.isNotEmpty(menuDict)){
									dataDto.put("menuName", menuDict.getMenuName());
								}
							
								
							}else{
								StepSpec stepSpec=this.getStepSpec(specIndexId, stepSpecList, languageType);
								if(WebplusUtil.isNotEmpty(stepSpec)){
									dataDto.put("menuName", stepSpec.getSpecName());
								}
								
							}
								
						}
						
						
					}
					for(Dto buffetDto:buffetOrderList){
						String menuIndexId=buffetDto.getString("menuIndexId");
						String specIndexId=buffetDto.getString("specIndexId");
						if(WebplusUtil.isEmpty(specIndexId)){
							MenuDict menuDict=this.getMenuDict(menuIndexId, menuDictList, languageType);
						    if(WebplusUtil.isNotEmpty(menuDict)){
						    	buffetDto.put("menuName", menuDict.getMenuName());
						    }
							
						}else{
							StepSpec stepSpec=this.getStepSpec(specIndexId, stepSpecList, languageType);
							if(WebplusUtil.isNotEmpty(stepSpec)){
								buffetDto.put("menuName", stepSpec.getSpecName());
							}
							
						}
					}
				}
				List<Dto> orderDtoList=this.getOrderLineList(dataList,WebplusCons.WHETHER_YES);
				List<Dto> buffetDtoList=this.getOrderLineList(buffetOrderList,WebplusCons.WHETHER_NO);
				Dto catalogDto=Dtos.newDto();
			    if(WebplusUtil.isNotEmpty(bufferCatalogName)) {
			    	String key=BussCons.BUFFET_ORDER_KEY+orderId;
					String value=WebplusCache.getString(key);
					if(WebplusUtil.isNotEmpty(value)) {
						Dto limitDto=WebplusJson.fromJson(value, HashDto.class);
						catalogDto.putAll(limitDto);
					}
			    }
				return R.toDataAndList(orderDto, orderDtoList).put("buffetOrderList", buffetDtoList).put("buffetCatalogName", bufferCatalogName).put("buffetData", catalogDto);
			}
			
			
		}
		return R.error();
	}
	
																																																																						
	
	/**
	 * 
	 * ?????????????????????????????????????????????
	 * ?????????????????????
	 * ???????????????2019???8???3??? ??????11:42:24
	 * @param ??????
	 * @return ??????
	 */
	@PostMapping("showOrderLine")
	@ResponseBody
	public R showOrderLine(String shopId,String orderLines){
		if(WebplusUtil.isNotAnyEmpty(shopId,orderLines)){
			List<Dto> lineDtoList=WebplusJson.fromJson(orderLines);
			if(WebplusUtil.isNotEmpty(lineDtoList)){
				Shop shop=shopService.selectById(shopId);
				if(WebplusUtil.isNotEmpty(shop)){
					String languageType=BussCons.LANGUAGE_TYPE_JP;
					List<String> menuIndexIdList=this.getMenuId(lineDtoList);
					List<String> specIndexIdList=this.getSpecId(lineDtoList);
					List<MenuDict> menuDictList=this.listMenuDict(menuIndexIdList, languageType);
					List<StepSpec> stepSpecList=this.listStepSpec(specIndexIdList, languageType);
					List<Dto> dataList=this.getMoreLanguageOrderLine(lineDtoList, menuDictList, stepSpecList, languageType);
					return R.toList(dataList);
				}
			}
			
		}
		return R.error();
	}
	
	
	
	/**
	 * 
	 * ???????????????????????????????????????
	 * ?????????????????????
	 * ???????????????2019???9???1??? ??????9:13:11
	 * @param ??????
	 * @return ??????
	 */
	private List<Dto> getOrderLineList(List<Dto> orderLineList,String whetherCountSpec){
		List<Dto> dataList=Lists.newArrayList();
		for(Dto dataDto:orderLineList){
			String whetherSpec=dataDto.getString("whetherSpec");
			String whetherBuffet=dataDto.getString("whetherBuffet");
			String menuIndexId=dataDto.getString("menuIndexId");
			if(WebplusCons.WHETHER_NO.equals(whetherSpec)||
					(WebplusCons.WHETHER_YES.equals(whetherBuffet)&&WebplusUtil.isEmpty( menuIndexId))){
				
				if(WebplusUtil.isNotEmpty(menuIndexId)){
					String lineId=dataDto.getString("lineId");
					if(WebplusUtil.isNotEmpty(lineId)){
						List<Dto> specList=this.getStepSpec(lineId, orderLineList);
						if(WebplusUtil.isNotEmpty(specList)){
							if(WebplusCons.WHETHER_YES.equals(whetherCountSpec)){
								int singleMenuPriceTotal=this.getSpecMenuPrice(dataDto.getInteger("singleSumPrice"), specList);
								dataDto.put("singleSumPrice", singleMenuPriceTotal);
							}
							dataDto.put("whetherSpec", WebplusCons.WHETHER_YES);
							dataDto.put("specList",specList);
						}
					}
					
				}
				dataList.add(dataDto);
			}
		}
		return dataList;
	}
	/**
	 * 
	 * ?????????????????????????????????
	 * ?????????????????????
	 * ???????????????2019???9???1??? ??????9:21:17
	 * @param ??????
	 * @return ??????
	 */
	private List<Dto> getStepSpec(String lineId,List<Dto> orderLineList){
		List<Dto> specList=Lists.newArrayList();
		for(Dto dataDto:orderLineList){
			String whetherSpec=dataDto.getString("whetherSpec");
			if(WebplusCons.WHETHER_YES.equals(whetherSpec)){
				String parentId=dataDto.getString("parentId");
				if(lineId.equals(parentId)){
					specList.add(dataDto);
				}
			}
		}
		return specList;
	}
	
	/**
	 * 
	 * ?????????????????????????????????
	 * ?????????????????????
	 * ???????????????2019???9???1??? ??????9:21:17
	 * @param ??????
	 * @return ??????
	 */
	private Integer getSpecMenuPrice(int menuPrice, List<Dto> specList){
	    int singleMenuPrice=menuPrice;
		for(Dto specDto:specList){
			Integer singleSumPrice=specDto.getInteger("singleSumPrice");
			if(WebplusUtil.isNotEmpty(singleSumPrice)){
				singleMenuPrice+=singleSumPrice;
			}
			
		}
		return singleMenuPrice;
	}
	/**
	 * 
	 * ??????????????????????????????????????????
	 * ?????????????????????
	 * ???????????????2019???8???3??? ??????2:41:17
	 * @param ??????
	 * @return ??????
	 */
	public List<Dto> getMoreLanguageOrderLine(List<Dto> lineDtoList,List<MenuDict> menuDictList,List<StepSpec> stepSpecList,String languageTypes){
		List<String> languageTypeList=WebplusFormater.separatStringToList(languageTypes);
		List<Dto> dataList=Lists.newArrayList();
		List<Item> itemList=WebplusCache.getContainsItem(BussCons.CACHE_LANGUAGE_TYPE_KEY, languageTypeList);
		for(Item item:itemList){
			Dto dataDto=Dtos.newDto();
			String languageType=item.getItemCode();
			dataDto.put("languageType", languageType);
			dataDto.put("languageTypeDict", item.getItemName());
			List<Dto> orderLineList=this.getLanguageOrderLine(lineDtoList, menuDictList, stepSpecList, languageType);
			dataDto.put("orderLineList", orderLineList);
			dataList.add(dataDto);
		}
	    
		return dataList;
	}
	
	/**
	 * 
	 * ?????????????????????????????????????????????
	 * ?????????????????????
	 * ???????????????2019???8???3??? ??????2:41:17
	 * @param ??????
	 * @return ??????
	 */
	public List<Dto> getLanguageOrderLine(List<Dto> lineDtoList,List<MenuDict> menuDictList,List<StepSpec> stepSpecList,String languageType){
		List<Dto> orderLineList=Lists.newArrayList();
		for(Dto lineDto:lineDtoList){
			Dto dataDto=Dtos.newDto();
			dataDto.putAll(lineDto);
			String menuIndexId=dataDto.getString("menuIndexId");
			String specIndexId=dataDto.getString("specIndexId");
			
			if(WebplusUtil.isNotEmpty(menuIndexId)){
				MenuDict menuDict=this.getMenuDict(menuIndexId, menuDictList, languageType);
				if(WebplusUtil.isNotEmpty(menuDict)){
					if(WebplusUtil.isEmpty(specIndexId)){
						dataDto.put("menuName", menuDict.getMenuName());
						dataDto.put("menuPrice", menuDict.getMenuPrice());
					    dataDto.put("whetherSpec", WebplusCons.WHETHER_NO);
					    dataDto.put("menuNum", menuDict.getMenuNum());
						orderLineList.add(dataDto);
					}else{
					    StepSpec stepSpec=this.getStepSpec(specIndexId, stepSpecList, languageType);
						if(WebplusUtil.isNotEmpty(stepSpec)){
							dataDto.put("menuName", stepSpec.getSpecName());
							dataDto.put("menuPrice", stepSpec.getSpecPrice());
						    dataDto.put("whetherSpec", WebplusCons.WHETHER_YES);
							orderLineList.add(dataDto);
						}
					}
				}
			}
			
		}
		List<Dto> dataList=this.getOrderLineList(orderLineList,WebplusCons.WHETHER_NO);
		return dataList;
	}
	
	
	
	/**
	 * 
	 * ??????????????????????????????
	 * ?????????????????????
	 * ???????????????2019???8???3??? ??????11:52:55
	 * @param ?????? langugaeType ??????????????????
	 * @return ??????
	 */
	public List<MenuCatalog> listBuffetCatalog(List<String> catalogIndexIdList,String languageType){
		List<String> languageTypeList=WebplusFormater.separatStringToList(languageType);
		if(WebplusUtil.isNotEmpty(catalogIndexIdList)){
			EntityWrapper<MenuCatalog> wrapper = new EntityWrapper<MenuCatalog>();
			wrapper.in("catalog_index_id", catalogIndexIdList);
			//wrapper.eq("whether_buffet", WebplusCons.WHETHER_YES);
			if(WebplusUtil.isNotEmpty(languageTypeList)){
				wrapper.in("language_type", languageTypeList);
				
			}
			List<MenuCatalog> menuCatalogList=menuCatalogService.selectList(wrapper);
			return menuCatalogList;
		}
		
		return Lists.newArrayList();
	}
	
	/**
	 * 
	 * ???????????????????????????
	 * ?????????????????????
	 * ???????????????2019???8???3??? ??????11:52:55
	 * @param ?????? langugaeType ??????????????????
	 * @return ??????
	 */
	public List<MenuDict> listMenuDict(List<String> menuIndexIdList,String languageType){
		List<String> languageTypeList=WebplusFormater.separatStringToList(languageType);
		if(WebplusUtil.isNotEmpty(menuIndexIdList)){
			EntityWrapper<MenuDict> wrapper = new EntityWrapper<MenuDict>();
			wrapper.in("menu_index_id", menuIndexIdList);
			if(WebplusUtil.isNotEmpty(languageTypeList)){
				wrapper.in("language_type", languageTypeList);
				
			}
			List<MenuDict> menuDictList=menuDictService.selectList(wrapper);
			return menuDictList;
		}
		
		return Lists.newArrayList();
	}
	
	/**
	 * 
	 * ?????????????????????????????????
	 * ?????????????????????
	 * ???????????????2019???8???3??? ??????11:52:55
	 * @param ?????? langugaeType ??????????????????
	 * @return ??????
	 */
	public List<StepSpec> listStepSpec(List<String> specIndexIdList,String languageType){
		List<String> languageTypeList=WebplusFormater.separatStringToList(languageType);
		if(WebplusUtil.isNotEmpty(specIndexIdList)){
			EntityWrapper<StepSpec> wrapper = new EntityWrapper<StepSpec>();
			wrapper.in("spec_index_id", specIndexIdList);
			if(WebplusUtil.isNotEmpty(languageTypeList)){
				wrapper.in("language_type", languageTypeList);
				
			}
			List<StepSpec> stepSpecList=stepSpecService.selectList(wrapper);
			return stepSpecList;
		}
		
		return Lists.newArrayList();
	}
	
	/**
	 * 
	 * ????????????????????????????????????
	 * ?????????????????????
	 * ???????????????2019???8???3??? ??????11:52:55
	 * @param ?????? langugaeType ??????????????????
	 * @return ??????
	 */
	public List<BuffetSpec> listBuffetSpec(List<String> specIndexIdList,String languageType){
		List<String> languageTypeList=WebplusFormater.separatStringToList(languageType);
		if(WebplusUtil.isNotEmpty(specIndexIdList)){
			EntityWrapper<BuffetSpec> wrapper = new EntityWrapper<BuffetSpec>();
			wrapper.in("spec_index_id", specIndexIdList);
			if(WebplusUtil.isNotEmpty(languageTypeList)){
				wrapper.in("language_type", languageTypeList);
				
			}
			List<BuffetSpec> buffetSpecList=buffetSpecService.selectList(wrapper);
			return buffetSpecList;
		}
		
		return Lists.newArrayList();
	}
	
	/**
	 * 
	 * ???????????????????????????
	 * ?????????????????????
	 * ???????????????2019???8???3??? ??????11:52:55
	 * @param ?????? langugaeType ??????????????????
	 * @return ??????
	 */
	public List<MenuSpec> listMenuSpec(List<String> specIndexIdList,String languageType){
		List<String> languageTypeList=WebplusFormater.separatStringToList(languageType);
		if(WebplusUtil.isNotEmpty(specIndexIdList)){
			EntityWrapper<MenuSpec> wrapper = new EntityWrapper<MenuSpec>();
			wrapper.in("spec_index_id", specIndexIdList);
			if(WebplusUtil.isNotEmpty(languageTypeList)){
				wrapper.in("language_type", languageTypeList);
				
			}
			List<MenuSpec> menuSpecList=menuSpecService.selectList(wrapper);
			return menuSpecList;
		}
		
		return Lists.newArrayList();
	}
	
	/**
	 * 
	 * ?????????????????????????????????
	 * ?????????????????????
	 * ???????????????2019???8???3??? ??????12:25:33
	 * @param ??????
	 * @return ??????
	 */
	public List<OrderLine> getAddOrderLine(String shopId,String orderId,String orderLines,String whetherAdd){
	
		List<OrderLine> orderLineList=Lists.newArrayList();
		List<Dto> dataList=WebplusJson.fromJson(orderLines);
		if(WebplusUtil.isNotEmpty(dataList)){
			dataList=this.removeWasteMenuSpec(dataList);  //????????????parentId??????
			List<String> menuIdList=this.getMenuId(dataList);
			List<String> specIdList=this.getSpecId(dataList);
			List<MenuDict> menuDictList=Lists.newArrayList();
			if(WebplusUtil.isNotEmpty(menuIdList)){
				menuDictList=menuDictService.selectBatchIds(menuIdList);
			}
			List<MenuCatalog> menuCatalogList=this.getMenuCatalog(menuDictList);
			
			List<StepSpec> stepSpecList=Lists.newArrayList();
			if(WebplusUtil.isNotEmpty(specIdList)){
				stepSpecList=stepSpecService.selectBatchIds(specIdList);
			}
			
			
			for(Dto dataDto:dataList){
				OrderLine orderLine=new OrderLine();
				
				WebplusUtil.copyProperties(dataDto, orderLine);
				String catalogIndexId=orderLine.getCatalogIndexId();
				if(WebplusUtil.isNotEmpty(catalogIndexId)){
					orderLine.setWhetherBuffet(WebplusCons.WHETHER_YES);
					
				}else{
					orderLine.setWhetherBuffet(WebplusCons.WHETHER_NO);
				}
				orderLine.setOrderId(orderId);
				orderLine.setShopId(shopId);
				orderLine.setWhetherAdd(whetherAdd);
				orderLine.setWhetherPrint(WebplusCons.WHETHER_NO);
				orderLine.setCreateTime(WebplusUtil.getDateTime());
				orderLine.setWhetherTakeOut(WebplusCons.WHETHER_NO); //??????
				String menuIndexId=orderLine.getMenuIndexId();
				if(WebplusUtil.isNotEmpty(menuIndexId)){
					MenuDict menuDict=this.getMenuDict(menuIndexId, menuDictList,"");
					if(WebplusUtil.isNotEmpty(menuDict)){
						String specIndexId=orderLine.getSpecIndexId();
						String menuName=menuDict.getMenuName();
						String shortName=menuDict.getShortName();
						orderLine.setPrintCatalogIndexId(menuDict.getCatalogIndexId());
						if(WebplusUtil.isEmpty(catalogIndexId)){
							orderLine.setCatalogIndexId(menuDict.getCatalogIndexId());
							MenuCatalog menuCatalog=this.getMenuCatalog(menuDict.getCatalogIndexId(),menuCatalogList,"");
							if(WebplusUtil.isNotEmpty(menuCatalog)){
								if(BussCons.CATALOG_TYPE_TAKEOUT.equals(menuCatalog.getCatalogType())
										||BussCons.CATALOG_TYPE_BESIDES.equals(menuCatalog.getCatalogType())){  //????????????
									orderLine.setWhetherTakeOut(WebplusCons.WHETHER_YES);
								}
							   orderLine.setTaxType(menuCatalog.getTaxType());
							}
						}
						if(WebplusUtil.isNotEmpty(specIndexId)){
							
							StepSpec stepSpec=this.getStepSpec(specIndexId, stepSpecList,"");
							if(WebplusUtil.isNotEmpty(stepSpec)){
								String specName=stepSpec.getSpecName();
								orderLine.setPrintMenuName(specName);
								orderLine.setMenuName(specName);
								orderLine.setMenuPrice(stepSpec.getSpecPrice());
								orderLine.setWhetherSpec(WebplusCons.WHETHER_YES);
								orderLineList.add(orderLine);
							}
						}else{
						
							orderLine.setMenuName(menuName);
							orderLine.setMenuPrice(menuDict.getMenuPrice());
							if(WebplusUtil.isNotEmpty(shortName)){
							 orderLine.setPrintMenuName(shortName);
							}else{
								orderLine.setPrintMenuName(menuName);
							}
							
							orderLine.setWhetherSpec(WebplusCons.WHETHER_NO);
							orderLineList.add(orderLine);
						}
					}
				}
			}
		}
		return orderLineList;
		
	}
	/**
	 * 
	 * ???????????????????????????parentiId????????????
	 * ?????????????????????
	 * ???????????????2019???12???10??? ??????8:42:56
	 * @param ??????
	 * @return ??????
	 */
	private List<Dto> removeWasteMenuSpec(List<Dto> dataList){
		List<Dto> dataListNew=Lists.newArrayList();
		for(Dto dataDto:dataList){
			String parentId=dataDto.getString("parentId");
			if(WebplusUtil.isNotEmpty(parentId)){
				Dto parentDto=getParent(parentId,dataList);
				 if(WebplusUtil.isNotEmpty(parentDto)){
					 String catalogIndexId=parentDto.getString("catalogIndexId");
					 if(WebplusUtil.isNotEmpty(catalogIndexId)){
						 dataDto.put("catalogIndexId", catalogIndexId);
					 }else{
						 dataDto.put("catalogIndexId", "");
					 }
					 dataListNew.add(dataDto);
				 }
			}else{
				
				dataListNew.add(dataDto);
			}
		}
		
		return dataListNew;
	}
	 /**
	  * ??????????????????parentId
	  */
	private Dto getParent(String parentId,List<Dto> dataList){
		for(Dto dataDto:dataList){
			String lineId=dataDto.getString("lineId");
			if(parentId.equals(lineId)){
				
				return dataDto;
			}
		}
		return null;
	}
	/**
	 * 
	 * ??????????????????????????????????????????
	 * ?????????????????????
	 * ???????????????2019???9???15??? ??????10:07:57
	 * @param ??????
	 * @return ??????
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
     * ?????????????????????????????????????????????
     * ?????????????????????
     * ???????????????2019???8???3??? ??????12:16:10
     * @param ??????
     * @return ??????
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
     * ?????????????????????????????????????????????
     * ?????????????????????
     * ???????????????2019???8???3??? ??????12:16:10
     * @param ??????
     * @return ??????
     */
	private BuffetSpec getBuffetSpec(String specIndexId,List<BuffetSpec> buffetSpecList,String languageType){
		for(BuffetSpec buffetSpec:buffetSpecList){
			if(WebplusUtil.isEmpty(languageType)){
				if(specIndexId.equals(buffetSpec.getSpecIndexId())){
					
					return  buffetSpec;
				}
			}else{
               if(specIndexId.equals(buffetSpec.getSpecIndexId())
            		   &&languageType.equals(buffetSpec.getLanguageType())){
					
            	   return buffetSpec;
				}
			}
			
		}
		
		return null;
	}
    /**
     * 
     * ????????????????????????????????????
     * ?????????????????????
     * ???????????????2019???8???3??? ??????12:16:10
     * @param ??????
     * @return ??????
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
	 * ???????????????????????????????????????
	 * ?????????????????????
	 * ???????????????2019???8???3??? ??????12:16:10
	 * @param ??????
	 * @return ??????
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
	 * ????????????????????????????????????
	 * ?????????????????????
	 * ???????????????2019???8???3??? ??????12:16:10
	 * @param ??????
	 * @return ??????
	 */
	public MenuSpec getMenuSpec(String specIndexId,List<MenuSpec> menuSpecList,String languageType){
		for(MenuSpec menuSpec:menuSpecList){
			if(WebplusUtil.isEmpty(languageType)){
				if(specIndexId.equals(menuSpec.getSpecIndexId())){
					
					return menuSpec;
				}	
			}else{
                if(specIndexId.equals(menuSpec.getSpecIndexId())
                		&&languageType.equals(menuSpec.getLanguageType())){
					
					return menuSpec;
				}	
			}
			
		}
		
		return null;
	}
	

	/**
	 * 
	 * ???????????????????????????????????????
	 * ?????????????????????
	 * ???????????????2019???8???2??? ??????11:58:42
	 * @param ??????
	 * @return ??????
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
	 * ????????????????????????????????????????????????????????????
	 * ?????????????????????
	 * ???????????????2019???8???2??? ??????11:58:42
	 * @param ??????
	 * @return ??????
	 */
	private List<String> getSpecId(List<Dto> dataList){
		List<String> specIdList=Lists.newArrayList();
		for(Dto dataDto:dataList){
			String specIndexId=dataDto.getString("specIndexId");
			if(WebplusUtil.isNotEmpty(specIndexId)){
				specIdList.add(specIndexId);
			}
			
		}
		
		return specIdList;
	}
	
	/**
	 * 
	 * ?????????????????????????????????
	 * ?????????????????????
	 * ???????????????2019???12???10??? ??????1:27:08
	 * @param ??????
	 * @return ??????
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
	 * ?????????????????????parentId????????????
	 * ?????????????????????
	 * ???????????????2019???12???10??? ??????1:29:39
	 * @param ??????
	 * @return ??????
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
	 * ?????????????????????????????????
	 * ????????????????????????chenqiyuan@toonan.com???
	 * ??????????????? 2020???6???27??? ??????8:52:12 
	 * @param ??????
	 * @return ??????
	 */
	@PostMapping("sendPay")
	@ResponseBody
	public R sendPay(String orderId,String token) {
		
		
		return orderCommonService.sendPay(orderId,token);
	}
  
}
