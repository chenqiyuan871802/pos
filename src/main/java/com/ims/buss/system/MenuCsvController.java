package com.ims.buss.system;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.ims.buss.constant.BussCons;
import com.ims.buss.model.MenuCatalog;
import com.ims.buss.model.MenuCsv;
import com.ims.buss.model.MenuDict;
import com.ims.buss.model.MenuModel;
import com.ims.buss.model.MenuStep;
import com.ims.buss.model.Shop;
import com.ims.buss.model.SpecModel;
import com.ims.buss.model.StepSpec;
import com.ims.buss.service.BussCommonService;
import com.ims.buss.service.MenuCatalogService;
import com.ims.buss.service.MenuCsvService;
import com.ims.buss.service.MenuDictService;
import com.ims.buss.service.MenuStepService;
import com.ims.buss.service.ShopService;
import com.ims.buss.service.StepSpecService;
import com.ims.buss.util.BussUtil;
import com.ims.core.cache.WebplusCache;
import com.ims.core.constant.WebplusCons;
import com.ims.core.matatype.Dto;
import com.ims.core.matatype.Dtos;
import com.ims.core.util.WebplusFile;
import com.ims.core.util.WebplusFormater;
import com.ims.core.util.WebplusUtil;
import com.ims.core.vo.Item;
import com.ims.core.vo.R;
import com.ims.core.web.BaseController;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;



/**
 * <p>
 * 菜单的csv文件 前端控制器
 * </p>
 *
 * @author 陈骑元
 * @since 2019-08-06
 */
@Controller
@RequestMapping("/buss/menuCsv")
public class MenuCsvController extends BaseController {
	private static Log log = LogFactory.getLog(MenuCsvController.class);
    @Autowired
    private MenuCsvService menuCsvService;
    @Autowired
    private MenuDictService menuDictService;
    @Autowired
    private MenuCatalogService menuCatalogService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private BussCommonService bussCommonService;
    @Autowired
    private MenuStepService menuStepService;
    @Autowired
    private StepSpecService stepSpecService;
    
    
    

	/**
	 * 
	 * 简要说明：分页查询 
	 * 编写者：陈骑元
	 * 创建时间：2019-08-06
	 * @param 说明
	 * @return 说明
	 */
	@RequestMapping("list")
	@ResponseBody
	public R list() {
		String shopId=this.getUserId();
		Dto pDto = Dtos.newDto(request);
		pDto.put("shopId", shopId);
		pDto.setOrder(" create_time DESC ");
		Page<MenuCsv> page =menuCsvService.likePage(pDto);
		WebplusCache.convertItem(page);
		return R.toPage(page);
	}



	/**
	 * 
	 * 简要说明： 新增信息保存 
	 * 编写者：陈骑元
	 * 创建时间：2019-08-06
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("save")
	@ResponseBody
	public R save(MenuCsv menuCsv) {
		boolean result = menuCsvService.insert(menuCsv);
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
	 * 创建时间：2019-08-06
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("edit")
	@ResponseBody
	public R edit(String id) {
		MenuCsv menuCsv=menuCsvService.selectById(id);
		return R.toData(menuCsv);
	}
	
	/**
	 * 
	 * 简要说明：修改信息
	 * 编写者：陈骑元
	 * 创建时间：2019-08-06
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("update")
	@ResponseBody
	public R update(MenuCsv menuCsv) {
		boolean result = menuCsvService.updateById(menuCsv);
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
	 * 创建时间：2019-08-06
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("remove")
	@ResponseBody
	public R remove(String id) {
		MenuCsv menuCsv=menuCsvService.selectById(id);
		String rootPath=WebplusCache.getParamValue(WebplusCons.SAVE_ROOT_PATH_KEY);
		String filePath=rootPath+File.separator+BussCons.CSV_FILE+File.separator+menuCsv.getFid();
		WebplusFile.deleteFile(filePath);
		boolean result = menuCsvService.deleteById(id);
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
	 * 创建时间：2019-08-06
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("batchRemove")
	@ResponseBody
	public R batchRemove(String ids) {
		List<String> idList=WebplusFormater.separatStringToList(ids);
		boolean result = menuCsvService.deleteBatchIds(idList);
		if (result) {
			return R.ok();
		} else {
			return R.error("删除失败");
		}
		
	}
	/**
	 * 
	 * @param 下载CSV模板
	 * @param 
	 * @return
	 */
	@RequestMapping(value = "downloadCsv")
	public void downloadCsv(String id,HttpServletRequest request,HttpServletResponse response) {
		MenuCsv menuCsv=menuCsvService.selectById(id);
		if(WebplusUtil.isNotEmpty(menuCsv)){
			String rootPath=WebplusCache.getParamValue(WebplusCons.SAVE_ROOT_PATH_KEY);
			String filePath=rootPath+File.separator+BussCons.CSV_FILE+File.separator+menuCsv.getFid();
			WebplusFile.downloadFile(request, response,  filePath,menuCsv.getFileName());
		}
		
	}
	/**
	 * 
	 * @param 下载CSV模板
	 * @param 
	 * @return
	 */
	@RequestMapping(value = "downloadCsvTemplate")
	public void downloadCsvTemplate(HttpServletRequest request,HttpServletResponse response) {
		String folderPath=WebplusCache.getParamValue(WebplusCons.SAVE_ROOT_PATH_KEY	);
		String filePath=folderPath+File.separator+BussCons.CSV_TEMPLATE;
		WebplusFile.downloadFile(request, response,  filePath,BussCons.CSV_TEMPLATE);
	}
	/**
	 * 
	 * @param 下载CSV模板
	 * @param 
	 * @return
	 */
	@RequestMapping(value = "downloadSpecCsvTemplate")
	public void downloaSpecdCsvTemplate(HttpServletRequest request,HttpServletResponse response) {
		String folderPath=WebplusCache.getParamValue(WebplusCons.SAVE_ROOT_PATH_KEY	);
		String filePath=folderPath+File.separator+BussCons.SPEC_CSV_TEMPLATE;
		WebplusFile.downloadFile(request, response,  filePath,BussCons.SPEC_CSV_TEMPLATE);
	}
	
	/**
	 * 
	 * @param 下载CSV模板
	 * @param 
	 * @return
	 */
	@PostMapping("exportMenuDict")
	@ResponseBody
    public R exportMenuDict(){
		String shopId=this.getUserId();
		Dto pDto = Dtos.newDto(request);
		pDto.put("shopId", shopId);
		pDto.put("languageType", BussCons.LANGUAGE_TYPE_JP);
		pDto.setOrder("b.sort_no ASC,a.sort_no ASC");
		List<MenuDict> menuDictList=bussCommonService.listMenuDict(pDto);
		List<Dto> dataList=WebplusUtil.copyPropertiesList(menuDictList);
		Dto dataDto=Dtos.newDto();
		dataDto.put("dataList", dataList);
		String fid=BussUtil.createTemplateExcel("excel/export_csv_template.xlsx", dataDto);
		if(WebplusUtil.isNotEmpty(fid)){
			
		   
		   return R.ok().put("fid", fid);
		}
		return R.error();
	}
	
	
	/**
	 * 
	 * @param 下载CSV模板
	 * @param 
	 * @return
	 */
	@PostMapping("exportStepSpec")
	@ResponseBody
    public R exportStepSpec(){
		String shopId=this.getUserId();
		Dto pDto = Dtos.newDto(request);
		pDto.put("shopId", shopId);
		pDto.put("languageType", BussCons.LANGUAGE_TYPE_JP);
		pDto.setOrder("a.sort_no ASC,b.sort_no ASC,c.step_num ASC ,d.sort_no ASC");
		List<StepSpec> stepSpecList=bussCommonService.listStepSpec(pDto);
		List<Dto> dataList=WebplusUtil.copyPropertiesList(stepSpecList);
		Dto dataDto=Dtos.newDto();
		dataDto.put("dataList", dataList);
		String fid=BussUtil.createTemplateExcel("excel/export_spec_csv_template.xlsx", dataDto);
		if(WebplusUtil.isNotEmpty(fid)){
			
		   
		   return R.ok().put("fid", fid);
		}
		return R.error();
	}
	/**
	 * 
	 * 简要说明：上传菜单csv 文件
	 * 编写者：陈骑元
	 * 创建时间：2019-02-23
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("uploadCsv")
	@ResponseBody
	public R uploadCsv(@RequestParam(value = "file", required = false) MultipartFile file,
			HttpServletRequest request,HttpServletResponse response) {
		if(WebplusUtil.isNotEmpty(file)&&file.getSize()>0){
			
			try {
				String rootPath=WebplusCache.getParamValue(WebplusCons.SAVE_ROOT_PATH_KEY);
				String folderPath=rootPath+File.separator+BussCons.CSV_FILE;
				String fileName=file.getOriginalFilename();
				String fid=WebplusUtil.uuid()+"."+WebplusFile.getFileType(fileName);
				WebplusFile.createFolder(folderPath); 
				File targetFile = new File(folderPath,fid); 
				file.transferTo(targetFile);
				String filePath=folderPath+File.separator+fid;
				String userId=this.getUserId();
				this.saveExcelMenu(filePath, userId);
				MenuCsv menuCsv=new MenuCsv();
				menuCsv.setShopId(userId);
				menuCsv.setCreateTime(WebplusUtil.getDateTime());
				menuCsv.setFid(fid);
				menuCsv.setFileName(fileName);
				menuCsv.setStatus(WebplusCons.WHETHER_YES);
		        menuCsvService.insert(menuCsv);
				return R.ok();
			} catch (Exception e) {
				
				e.printStackTrace();
			} 
			return R.error();
			 
		}
       
		return R.error();
	}
	
	/**
	 * 
	 * 简要说明：上传规格CSV文件
	 * 编写者：陈骑元
	 * 创建时间：2019-02-23
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("uploadSpecCsv")
	@ResponseBody
	public R uploadSpecCsv(@RequestParam(value = "file", required = false) MultipartFile file,
			HttpServletRequest request,HttpServletResponse response) {
		if(WebplusUtil.isNotEmpty(file)&&file.getSize()>0){
			
			try {
				String rootPath=WebplusCache.getParamValue(WebplusCons.SAVE_ROOT_PATH_KEY);
				String folderPath=rootPath+File.separator+BussCons.CSV_FILE;
				String fileName=file.getOriginalFilename();
				String fid=WebplusUtil.uuid()+"."+WebplusFile.getFileType(fileName);
				WebplusFile.createFolder(folderPath); 
				File targetFile = new File(folderPath,fid); 
				file.transferTo(targetFile);
				String filePath=folderPath+File.separator+fid;
				String userId=this.getUserId();
				this.saveExcelSpec(filePath, userId);
				MenuCsv menuCsv=new MenuCsv();
				menuCsv.setShopId(userId);
				menuCsv.setCreateTime(WebplusUtil.getDateTime());
				menuCsv.setFid(fid);
				menuCsv.setFileName(fileName);
				menuCsv.setStatus(WebplusCons.WHETHER_YES);
		        menuCsvService.insert(menuCsv);
				return R.ok();
			} catch (Exception e) {
				
				e.printStackTrace();
			} 
			return R.error();
			 
		}
       
		return R.error();
	}
	/**
	 * 
	 * 简要说明：保存菜单数据存入数据库
	 * 编写者：陈骑元
	 * 创建时间：2019年8月10日 下午11:57:43
	 * @param 说明
	 * @return 说明
	 */
	private void saveExcelMenu(String filePath,String shopId){
		Shop shop=shopService.selectById(shopId);
		if(WebplusUtil.isNotEmpty(shop)){
			String language=shop.getLanguage();
			List<String> languageTypeList=WebplusFormater.separatStringToList(language);
			Map<String,List<MenuDict>>  menuDictMap=readExcel( filePath, languageTypeList,shopId);
			List<MenuDict> menuDictJpList=menuDictMap.get(BussCons.LANGUAGE_TYPE_JP);  //优选获取日语的
			if(WebplusUtil.isNotEmpty(menuDictJpList)){
				Dto pDto=Dtos.newDto();
				pDto.put("shopId", shopId);
				Dto calcDto = Dtos.newCalcDto("IFNULL(MAX(sort_no),0)+1");
				calcDto.put("shopId", shopId);
				calcDto.put("languageType", BussCons.LANGUAGE_TYPE_JP);
				String maxSortNo =menuCatalogService.calc(calcDto);
				if(WebplusUtil.isEmpty(maxSortNo)){
					maxSortNo="1";
				}
				int sortNo=Integer.parseInt(maxSortNo);
				List<MenuCatalog> menuCatalogDBList=menuCatalogService.list(pDto);
				List<MenuDict> menuDictDBList=menuDictService.list(pDto);
				List<MenuCatalog> menuCatalogJpList=this.getMenuCatalog(menuDictJpList, BussCons.LANGUAGE_TYPE_JP, shopId);
				this.saveJpMenuCatalog(menuCatalogDBList, menuCatalogJpList,sortNo); //处理日本的
				this.saveJpMenuDict(menuDictDBList, menuCatalogJpList, menuDictJpList);
				for(String key:menuDictMap.keySet()){
					if(!BussCons.LANGUAGE_TYPE_JP.equals(key)){
						List<MenuDict> menuDictList=menuDictMap.get(key);
						if(WebplusUtil.isNotEmpty(menuDictList)){
							List<MenuCatalog> menuCatalogList=this.getMenuCatalog(menuDictList,key, shopId);
							this.saveMenuCatalog( menuCatalogDBList, menuCatalogJpList,menuCatalogList);
							this.saveMenuDict(menuDictDBList, menuDictJpList, menuDictList);
						}
					}
					
				}
			}
		}
		
	}
	/**
	 * 
	 * 简要说明：保存其他菜品种类
	 * 编写者：陈骑元
	 * 创建时间：2019年8月11日 上午12:56:13
	 * @param 说明
	 * @return 说明
	 */
	public void saveMenuCatalog(List<MenuCatalog> menuCatalogDBList,List<MenuCatalog> menuCatalogJpList,List<MenuCatalog> menuCatalogList){
		int jpSize=menuCatalogJpList.size();
		int size=menuCatalogList.size();
		for(int i=0;i<size;i++){
			if(i<jpSize){
				MenuCatalog menuCatalogJp=menuCatalogJpList.get(i);
				MenuCatalog menuCatalog=menuCatalogList.get(i);
				String catalogName=menuCatalog.getCatalogName();
				String languageType=menuCatalog.getLanguageType();
				String catalogIndexId=menuCatalogJp.getCatalogIndexId();
				int sortNo= menuCatalogJp.getSortNo();
				MenuCatalog entity=checkMenuCatalog(catalogName,languageType,catalogIndexId,menuCatalogDBList);
				if(WebplusUtil.isEmpty(entity)){  //不存在就更新
				
					String catalogId=WebplusUtil.uuid();
					menuCatalog.setCatalogId(catalogId);
					menuCatalog.setCatalogIndexId(catalogIndexId);
					menuCatalog.setSortNo(sortNo);
					menuCatalogService.insert(menuCatalog);
				}
			}
		
		}
		
	}
	/**
	 * 
	 * 简要说明：保存日语的菜品种类
	 * 编写者：陈骑元
	 * 创建时间：2019年8月11日 上午12:38:33
	 * @param 说明
	 * @return 说明
	 */
	public void saveJpMenuCatalog(List<MenuCatalog> menuCatalogDBList,List<MenuCatalog> menuCatalogJpList,int maxSortNo){
		 int sortNo=maxSortNo-1;
		for(int i=0;i<menuCatalogJpList.size();i++){
			
			MenuCatalog menuCatalog=menuCatalogJpList.get(i);
			String catalogName=menuCatalog.getCatalogName();
			String languageType=menuCatalog.getLanguageType();
			MenuCatalog entity=checkMenuCatalog(catalogName,languageType,menuCatalogDBList);
			if(WebplusUtil.isNotEmpty(entity)){  //已经存在就不更新了
				menuCatalog.setCatalogId(entity.getCatalogId());
				menuCatalog.setCatalogIndexId(entity.getCatalogId());
				menuCatalog.setSortNo(entity.getSortNo());
			}else{  //不存在则进行更新
				sortNo=sortNo+1;
				String catalogId=WebplusUtil.uuid();
				menuCatalog.setCatalogId(catalogId);
				menuCatalog.setCatalogIndexId(catalogId);
				menuCatalog.setSortNo(sortNo);
				
				menuCatalogService.insert(menuCatalog);
				
			}
		}
		
	}
	
	/**
	 * 
	 * 简要说明：优先保存其他菜品
	 * 编写者：陈骑元
	 * 创建时间：2019年8月11日 上午12:38:33
	 * @param 说明
	 * @return 说明
	 */
	public void saveMenuDict(List<MenuDict> menuDictDBList,List<MenuDict> menuDictJpList,List<MenuDict> menuDictList){
		 int jpSize=menuDictJpList.size();
		 
		for(int i=0;i<menuDictList.size();i++	){
			if(i<jpSize){
				MenuDict jpMenuDict=menuDictJpList.get(i);
				MenuDict menuDict=menuDictList.get(i);
				String catalogIndexId=jpMenuDict.getCatalogIndexId();
				String menuIndexId=jpMenuDict.getMenuIndexId();
				String menuName=menuDict.getMenuName();
				String languageType=menuDict.getLanguageType();
				menuDict.setCatalogIndexId(catalogIndexId);
				menuDict.setMenuIndexId(menuIndexId);
				menuDict.setSortNo(jpMenuDict.getSortNo());
				MenuDict entity=checkMenuDict(menuName,languageType,catalogIndexId,menuIndexId,menuDictDBList);
				if(WebplusUtil.isNotEmpty(entity)){  //已经存在就不更新了
					menuDict.setMenuId(entity.getMenuId());
					menuDict.setUpdateTime(WebplusUtil.getDateTime());
					menuDictService.updateById(menuDict);
				}else{  //不存在则进行更新
					String menuId=WebplusUtil.uuid();
					menuDict.setMenuId(menuId);
					menuDictService.insert(menuDict);
				}
			}
		
			
		}
		
	}
	/**
	 * 
	 * 简要说明：获取菜单类下面最大的菜品索引
	 * 编写者：陈骑元
	 * 创建时间：2019年8月12日 上午2:06:55
	 * @param 说明
	 * @return 说明
	 */
	public int getMenuDictMaxSortNo(String shopId,String catalogIndexId){

	    Dto calcDto = Dtos.newCalcDto("IFNULL(MAX(sort_no),0)+1");
		calcDto.put("shopId", shopId);
		calcDto.put("catalogIndexId", catalogIndexId);
		calcDto.put("languageType", BussCons.LANGUAGE_TYPE_JP);
		String maxSortNo =menuDictService.calc(calcDto);
		if(WebplusUtil.isEmpty(maxSortNo)){
			maxSortNo="1";
		}
		int sortNo=Integer.parseInt(maxSortNo);
		return sortNo;
	}
	/**
	 * 
	 * 简要说明：优先保存日本菜品
	 * 编写者：陈骑元
	 * 创建时间：2019年8月11日 上午12:38:33
	 * @param 说明
	 * @return 说明
	 */
	public void saveJpMenuDict(List<MenuDict> menuDictDBList,List<MenuCatalog> menuCatalogJpList,List<MenuDict> menuDictJpList){
		  Dto sortDto=Dtos.newDto();
		for(MenuDict menuDict:menuDictJpList){
			String catalogName=menuDict.getCatalogName();
			String catalogIndexId=this.getCatalogIndexId(catalogName, menuCatalogJpList);
			String shopId=menuDict.getShopId();
			if(!sortDto.containsKey(catalogIndexId)){
				int sortNo=this.getMenuDictMaxSortNo(shopId, catalogIndexId);
				sortDto.put(catalogIndexId, sortNo);
			}
			if(WebplusUtil.isNotEmpty(catalogIndexId)){
				String menuName=menuDict.getMenuName();
				String languageType=menuDict.getLanguageType();
				menuDict.setCatalogIndexId(catalogIndexId);
				if(WebplusUtil.isEmpty(menuDict.getMealType())){
					menuDict.setMealType(BussCons.MEAL_TYPE_COMMON);
				}
				String mealType=menuDict.getMealType();
				MenuDict entity=checkMenuDict(menuName,languageType,catalogIndexId,menuDictDBList,mealType);
				if(WebplusUtil.isNotEmpty(entity)){  //已经存在就不更新了
					menuDict.setMenuId(entity.getMenuId());
					menuDict.setUpdateTime(WebplusUtil.getDateTime());
					menuDict.setMenuIndexId(entity.getMenuIndexId());
					menuDictService.updateById(menuDict);
				}else{  //不存在则进行更新
					String menuId=WebplusUtil.uuid();
					int sortNo=sortDto.getInteger(catalogIndexId);
					menuDict.setMenuId(menuId);
					menuDict.setMenuIndexId(menuId);
					menuDict.setSortNo(sortNo);
					
					menuDictService.insert(menuDict);
					sortDto.put(catalogIndexId, sortNo+1);
				}
			}
			
		}
		
	}
	/**
	 * 
	 * 简要说明：获取菜品种类索引
	 * 编写者：陈骑元
	 * 创建时间：2019年8月11日 上午1:29:44
	 * @param 说明
	 * @return 说明
	 */
	public String getCatalogIndexId(String catalogName,List<MenuCatalog> menuCatalogJpList){
		for(MenuCatalog menuCatalog:menuCatalogJpList){
			if(catalogName.equals(menuCatalog.getCatalogName().trim())){
				
				return menuCatalog.getCatalogIndexId();
			}
		}
		return "";
	}
	
	/**
	 * 
	 * 简要说明：检查菜单名称
	 * 编写者：陈骑元
	 * 创建时间：2019年8月11日 上午1:23:37
	 * @param 说明
	 * @return 说明
	 */
	public MenuDict checkMenuDict(String menuName,String languageType,String catalogIndexId,String menuIndexId,List<MenuDict> menuDictDBList){
		System.out.println("menuIndexId:"+menuIndexId+",catalogIndexId"+catalogIndexId);
		for(MenuDict menuDict:menuDictDBList){
	        
			if(menuName.equals(menuDict.getMenuName().trim())&&languageType.equals(menuDict.getLanguageType())
					&&catalogIndexId.equals(menuDict.getCatalogIndexId())&& menuIndexId.equals(menuDict.getMenuIndexId())){
				return menuDict;
			}
		}
		return null;
	}
	/**
	 * 
	 * 简要说明：检查菜单名称
	 * 编写者：陈骑元
	 * 创建时间：2019年8月11日 上午1:23:37
	 * @param 说明
	 * @return 说明
	 */
	public MenuDict checkMenuDict(String menuName,String languageType,String catalogIndexId,List<MenuDict> menuDictDBList,String mealType){
		for(MenuDict menuDict:menuDictDBList){
			if(menuName.equals(menuDict.getMenuName().trim())&&languageType.equals(menuDict.getLanguageType())
					&&catalogIndexId.equals(menuDict.getCatalogIndexId())&&mealType.equals(menuDict.getMealType())){
				return menuDict;
			}
		}
		return null;
	}
	/**
	 * 
	 * 简要说明：检查当前菜品是否存在，存在就返回当前菜品
	 * 编写者：陈骑元
	 * 创建时间：2019年8月11日 上午12:41:10
	 * @param 说明
	 * @return 说明
	 */
	public MenuCatalog  checkMenuCatalog(String catalogName,String languageType,String catalogIndexId,List<MenuCatalog> menuCatalogDBList){
		
		  for(MenuCatalog menuCatalog:menuCatalogDBList){
			  if(catalogName.equals(menuCatalog.getCatalogName().trim())&&languageType.equals(menuCatalog.getLanguageType())
					  &&catalogIndexId.equals(menuCatalog.getCatalogIndexId())){
				  
				  return menuCatalog;
			  }
		  }
		  return null;
	}
	/**
	/**
	 * 
	 * 简要说明：检查当前菜品是否存在，存在就返回当前菜品
	 * 编写者：陈骑元
	 * 创建时间：2019年8月11日 上午12:41:10
	 * @param 说明
	 * @return 说明
	 */
	public MenuCatalog  checkMenuCatalog(String catalogName,String languageType,List<MenuCatalog> menuCatalogDBList){
		
		  for(MenuCatalog menuCatalog:menuCatalogDBList){
			  if(catalogName.equals(menuCatalog.getCatalogName().trim())&&languageType.equals(menuCatalog.getLanguageType())){
				  
				  return menuCatalog;
			  }
		  }
		  return null;
	}
	/**
	 * 
	 * 简要说明：获取菜品种类
	 * 编写者：陈骑元
	 * 创建时间：2019年8月11日 上午12:21:30
	 * @param 说明
	 * @return 说明
	 */
	public List<MenuCatalog> getMenuCatalog(List<MenuDict> menuDictList,String languageType,String shopId){
		Dto tmpDto=Dtos.newDto();
		List<MenuCatalog> menuCatalogList=Lists.newArrayList();
		for(MenuDict menuDict:menuDictList){
			String catalogName=menuDict.getCatalogName();
			if(WebplusUtil.isNotEmpty(catalogName)){
				catalogName=catalogName.trim();
				if(!tmpDto.containsKey(catalogName)){
				
					MenuCatalog menuCatalog=new MenuCatalog();
					menuCatalog.setCatalogName(catalogName);
					menuCatalog.setLanguageType(languageType);
					menuCatalog.setShopId(shopId);
					menuCatalog.setCreateBy(shopId);
					menuCatalog.setCreateTime(WebplusUtil.getDateTime());
					menuCatalog.setUpdateBy(shopId);
					menuCatalog.setUpdateTime(WebplusUtil.getDateTime());
					menuCatalogList.add(menuCatalog);
					tmpDto.put(catalogName, catalogName);
				}
			}
		}
		tmpDto.clear();
		return menuCatalogList;
	}
	/**
	 * 
	 * 简要说明：读写Excel
	 * 编写者：陈骑元
	 * 创建时间：2019年8月7日 上午8:30:16
	 * @param 说明
	 * @return 说明
	 */
	private Map<String,List<MenuDict>> readExcel(String filePath,List<String> languageTypeList,String shopId){
		Map<String,List<MenuDict>> menuDictMap=new HashMap<String,List<MenuDict>>();
		 for(String languageType:languageTypeList){
			 int sheet=Integer.parseInt(languageType);
			 List<MenuDict> menuDictList=this.readExcelToMenuDict(filePath, sheet,shopId,languageType);
			 menuDictMap.put(languageType, menuDictList);
		 }
		return menuDictMap;
		
	}
	/**
	 * 
	 * 简要说明：读写Excel到菜品中
	 * 编写者：陈骑元
	 * 创建时间：2019年8月7日 上午8:45:55
	 * @param 说明
	 * @return 说明
	 */
	private List<MenuDict> readExcelToMenuDict(String filePath,int sheet,String shopId,String languageType){
		List<MenuDict> menuDictList=Lists.newArrayList();
		
		try {
			
			ImportParams params = new ImportParams();
	        params.setTitleRows(0);
	        params.setHeadRows(1);
			params.setStartSheetIndex(sheet-1);
			params.setSheetNum(1);
			List< MenuModel> dataList= ExcelImportUtil.importExcel(new File(filePath),
					 MenuModel.class, params);
			if(WebplusUtil.isNotEmpty(dataList)){
				 for(MenuModel menuModel : dataList) {
			          if(WebplusUtil.isNotEmpty(menuModel)){
			        	 if(WebplusUtil.isNotEmpty(menuModel.getCatalogName())){
			        		 MenuDict menuDict=new MenuDict();
			        		 menuModel.setCatalogName(menuModel.getCatalogName().trim());
			        		 menuModel.setMenuName(menuModel.getMenuName().trim());
			        		 WebplusUtil.copyProperties(menuModel, menuDict);
			        		 menuDict.setLanguageType(languageType);
			        		 
			        		 menuDict.setShopId(shopId);
			        		 menuDict.setCreateBy(shopId);
			        		 menuDict.setCreateTime(WebplusUtil.getDateTime());
			        		 menuDict.setUpdateBy(shopId);
			        		 menuDict.setUpdateTime(WebplusUtil.getDateTime());
			        		 menuDictList.add(menuDict);
			        	 }
			          }
			      }
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return menuDictList;
		
	}
	/**
	 * 
	 * 简要说明：保存规格数据存入数据库
	 * 编写者：陈骑元
	 * 创建时间：2019年8月10日 下午11:57:43
	 * @param 说明
	 * @return 说明
	 */
	private void saveExcelSpec(String filePath,String shopId){
		Shop shop=shopService.selectById(shopId);
		if(WebplusUtil.isNotEmpty(shop)){
			
			String language=shop.getLanguage();
			List<String> languageTypeList=WebplusFormater.separatStringToList(language);
			Map<String,List<StepSpec>>  specMap=readStepSpecExcel( filePath, languageTypeList,shopId);
			List<StepSpec> stepSpecJpList=specMap.get(BussCons.LANGUAGE_TYPE_JP);  //优选获取日语的
			if(WebplusUtil.isNotEmpty(stepSpecJpList)){
				Dto pDto=Dtos.newDto();
				pDto.put("shopId", shopId);
				List<MenuStep> menuStepDBList=menuStepService.list(pDto);
				List<StepSpec> stepSpecDBList=stepSpecService.list(pDto);
				pDto.put("languageType", BussCons.LANGUAGE_TYPE_JP);
				List<MenuDict> menuDictDBList=bussCommonService.listBuffetMenuDict(pDto);  //查询数据库当前用户所有菜品
				List<MenuStep> menuStepJpList=this.getMenuStep(stepSpecJpList, menuDictDBList, BussCons.LANGUAGE_TYPE_JP, shopId);
				
				this.saveJpMenuStep(menuStepJpList, menuStepDBList);
				this.saveJpStepSpec(stepSpecJpList, menuStepJpList, stepSpecDBList);
				for(String key:specMap.keySet()){
					if(!BussCons.LANGUAGE_TYPE_JP.equals(key)){
						List<StepSpec> stepSpecList=specMap.get(key);
						if(WebplusUtil.isNotEmpty(stepSpecList)){
							List<MenuStep>  menuStepList=this.getMenuStep(stepSpecList, menuDictDBList, key, shopId);
							this.saveMenuStep(menuStepList, menuStepJpList, menuStepDBList);
							this.saveStepSpec(stepSpecList, stepSpecJpList, stepSpecDBList);
						}
					}
					
				}
			}
		}
		
	}
	/**
	 * 
	 * 简要说明：保存日本菜品步骤
	 * 编写者：陈骑元
	 * 创建时间：2019年10月30日 下午11:34:03
	 * @param 说明
	 * @return 说明
	 */
	private void saveJpMenuStep(List<MenuStep> menuStepJpList,List<MenuStep> menuStepDBList){
		
		for(MenuStep menuStep:menuStepJpList){
			String menuIndexId=menuStep.getMenuIndexId();
			String stepName=menuStep.getStepName();
			
			MenuStep entity=this.checkMenuStep(menuIndexId, stepName, BussCons.LANGUAGE_TYPE_JP, menuStepDBList);
		    if(WebplusUtil.isNotEmpty(entity)){
		    	entity.setStepNum(menuStep.getStepNum());
		    	menuStepService.updateById(entity);
		    	menuStep.setStepId(entity.getStepId());
		    	menuStep.setStepIndexId(entity.getStepIndexId());
		    	
		    }else{
		    	String stepId=WebplusUtil.uuid();
		    	menuStep.setStepId(stepId);
		    	menuStep.setStepIndexId(stepId);
		    	menuStepService.insert(menuStep);
		    	MenuDict menuDict=new MenuDict();
		    	menuDict.setWhetherSpec(WebplusCons.WHETHER_YES);
		    	EntityWrapper<MenuDict> entityMapper=new EntityWrapper<MenuDict>();
		    	entityMapper.eq("shop_id", menuStep.getShopId());
		    	entityMapper.eq("menu_index_id",menuStep.getMenuIndexId());
		    	entityMapper.eq("language_type",menuStep.getLanguageType());
		    	menuDictService.update(menuDict, entityMapper);
		    }
		}
	}
	/**
	 * 
	 * 简要说明：保存其他菜单步骤
	 * 编写者：陈骑元
	 * 创建时间：2019年10月30日 下午11:34:03
	 * @param 说明
	 * @return 说明
	 */
	private void saveMenuStep(List<MenuStep> menuStepList,List<MenuStep> menuStepJpList,List<MenuStep> menuStepDBList){
		int jpSize=menuStepJpList.size();
		int size=menuStepList.size();
		for(int i=0;i<size;i++){
			if(i<jpSize){
				MenuStep jpMenuStep=menuStepJpList.get(i);
				MenuStep menuStep=menuStepList.get(i);
				String stepIndexId=jpMenuStep.getStepIndexId();
				String stepName=menuStep.getStepName();
				String menuIndexId=jpMenuStep.getMenuIndexId();
				String languageType=menuStep.getLanguageType();
			    MenuStep entity=this.checkMenuStep(menuIndexId, stepName, languageType, menuStepDBList);
			    if(WebplusUtil.isNotEmpty(entity)){
			    	entity.setStepNum(menuStep.getStepNum());
					menuStepService.updateById(entity);
					menuStep.setStepIndexId(stepIndexId);
					menuStep.setStepId(entity.getStepId());
					menuStep.setMenuIndexId(menuIndexId);
			    }else{
			    	String stepId=WebplusUtil.uuid();
					menuStep.setStepId(stepId);
					menuStep.setStepIndexId(stepIndexId);
					menuStep.setMenuIndexId(menuIndexId);
					menuStepService.insert(menuStep);
					MenuDict menuDict=new MenuDict();
					menuDict.setWhetherSpec(WebplusCons.WHETHER_YES);
					EntityWrapper<MenuDict> entityMapper=new EntityWrapper<MenuDict>();
					entityMapper.eq("shop_id", menuStep.getShopId());
					entityMapper.eq("menu_index_id",menuStep.getMenuIndexId());
					entityMapper.eq("language_type",menuStep.getLanguageType());
					menuDictService.update(menuDict, entityMapper);
			    }
			}
		}
		
	}
	/**
	 * 
	 * 简要说明：保存步骤规格
	 * 编写者：陈骑元
	 * 创建时间：2019年10月30日 下午11:34:03
	 * @param 说明
	 * @return 说明
	 */
	private void saveJpStepSpec(List<StepSpec> stepSpecJpList,List<MenuStep> menuStepJpList,List<StepSpec> stepSpecDBList){
		  Dto sortDto=Dtos.newDto();
			for(StepSpec stepSpec:stepSpecJpList){
				String stepName=stepSpec.getStepName();
				String menuIndexId=stepSpec.getMenuIndexId();
				if(WebplusUtil.isNotEmpty(menuIndexId)){
					String stepIndexId=this.getStepIndexId(menuIndexId, stepName, menuStepJpList);
					
					if(WebplusUtil.isNotEmpty(stepIndexId)){
						String shopId=stepSpec.getShopId();
						if(!sortDto.containsKey(stepIndexId)){
							int sortNo=this.getStepSpecMaxSortNo(shopId, stepIndexId);
							sortDto.put(stepIndexId, sortNo);
						}
						String specName=stepSpec.getSpecName();
						String languageType=stepSpec.getLanguageType();
					
						StepSpec entity=this.checkStepSpec(stepIndexId, specName, languageType, stepSpecDBList);
						if(WebplusUtil.isNotEmpty(entity)){  //已经存在就不更新了
							entity.setSpecPrice(stepSpec.getSpecPrice());
							stepSpec.setSortNo(entity.getSortNo());
							stepSpec.setSpecIndexId(entity.getSpecIndexId());
							stepSpec.setStepIndexId(stepIndexId);
							stepSpec.setSpecId(entity.getSpecId());
							stepSpecService.updateById(entity);
						}else{  //不存在则进行更新
							String specId=WebplusUtil.uuid();
							int sortNo=sortDto.getInteger(stepIndexId);
							stepSpec.setSpecId(specId);
							stepSpec.setSpecIndexId(specId);
							stepSpec.setSortNo(sortNo);
							stepSpec.setStepIndexId(stepIndexId);
							stepSpecService.insert(stepSpec);
							sortDto.put(stepIndexId, sortNo+1);
						}
				}
				
				}else {
					log.error("导入中文规格失败："+stepSpec.getCatalogName()+">>>"+stepSpec.getMenuName()+">>>"
				    +stepSpec.getStepName()+">>"+stepSpec.getSpecName());
				}
				
			}
		
	}
	/**
	 * 
	 * 简要说明：保存其他语言步骤规格
	 * 编写者：陈骑元
	 * 创建时间：2019年10月30日 下午11:34:03
	 * @param 说明
	 * @return 说明
	 */
	private void saveStepSpec(List<StepSpec> stepSpecList,List<StepSpec> stepSpecJpList,List<StepSpec> stepSpecDBList){
	    int jpSize=stepSpecJpList.size();
	    
	    List<Item> languageTypeList=WebplusCache.getItemList("language_type");
	    for(int i=0;i<stepSpecList.size();i++){
	    	if(i<jpSize){
	    		StepSpec jpStepSpec=stepSpecJpList.get(i);
	    		StepSpec stepSpec=stepSpecList.get(i);
	    		String stepIndexId=jpStepSpec.getStepIndexId();
	    		if(WebplusUtil.isNotEmpty(stepIndexId)){
	    			int sortNo=jpStepSpec.getSortNo();
		    		
		    		String specIndexId=jpStepSpec.getSpecIndexId();
		    		String specName=stepSpec.getSpecName();
		    		String languageType=stepSpec.getLanguageType();
		    		StepSpec entity=this.checkStepSpec(stepIndexId, specName, languageType, stepSpecDBList);
		    		if(WebplusUtil.isNotEmpty(entity)){  //已经存在就不更新了
						entity.setSpecPrice(stepSpec.getSpecPrice());
						stepSpecService.updateById(entity);
					}else{  //不存在则进行更新
						String specId=WebplusUtil.uuid();
						stepSpec.setSpecId(specId);
						stepSpec.setSpecIndexId(specIndexId);
						stepSpec.setSortNo(sortNo);
						stepSpec.setStepIndexId(stepIndexId);
						stepSpecService.insert(stepSpec);
					}
	    		}else{
	    			String languageName=WebplusCache.getItemName(languageTypeList, stepSpec.getLanguageType());
	    			log.error("导入"+languageName+"规格失败："+stepSpec.getCatalogName()+">>>"+stepSpec.getMenuName()+">>>"
	    				    +stepSpec.getStepName()+">>"+stepSpec.getSpecName());
	    		}
	    		
	    	}
	    }
	
		
	}
	/**
	 * 
	 * 简要说明：获取步骤索引编号
	 * 编写者：陈骑元
	 * 创建时间：2019年10月31日 上午12:16:29
	 * @param 说明
	 * @return 说明
	 */
	private String getStepIndexId(String menuIndexId,String stepName,List<MenuStep> menuStepJpList){
		for(MenuStep menuStep:menuStepJpList){
			if(menuIndexId.equals(menuStep.getMenuIndexId())&&stepName.equals(menuStep.getStepName())){
				
				return menuStep.getStepIndexId();
			}
		}
		return "";
	}
	/**
	 * 
	 * 简要说明：获取步骤规格下面最大的索引
	 * 编写者：陈骑元
	 * 创建时间：2019年8月12日 上午2:06:55
	 * @param 说明
	 * @return 说明
	 */
	public int getStepSpecMaxSortNo(String shopId,String stepIndexId){

	    Dto calcDto = Dtos.newCalcDto("IFNULL(MAX(sort_no),0)+1");
		calcDto.put("shopId", shopId);
		calcDto.put("stepIndexId", stepIndexId);
		calcDto.put("languageType", BussCons.LANGUAGE_TYPE_JP);
		String maxSortNo =stepSpecService.calc(calcDto);
		if(WebplusUtil.isEmpty(maxSortNo)){
			maxSortNo="1";
		}
		int sortNo=Integer.parseInt(maxSortNo);
		return sortNo;
	}
	/**
	 * 
	 * 简要说明：获取菜品步骤
	 * 编写者：陈骑元
	 * 创建时间：2019年8月11日 上午12:21:30
	 * @param 说明
	 * @return 说明
	 */
	public List<MenuStep> getMenuStep(List<StepSpec> stepSpecList,List<MenuDict> menuDictDBList,String languageType,String shopId){
		Dto tmpDto = Dtos.newDto();
		List<MenuStep> menuStepList = Lists.newArrayList();
		for (StepSpec stepSpec : stepSpecList) {
			String catalogName = stepSpec.getCatalogName();
			String menuName = stepSpec.getMenuName();
			String stepName = stepSpec.getStepName();
			String mealType=stepSpec.getMealType();
			if(WebplusUtil.isEmpty(mealType)){
				mealType=BussCons.MEAL_TYPE_COMMON;
			}
			if (BussCons.LANGUAGE_TYPE_JP.equals(languageType)) {
				
				MenuDict menuDict = this.getMenuDict(catalogName, menuName, mealType,menuDictDBList);
				if (WebplusUtil.isNotEmpty(menuDict)) {
					String key = catalogName + menuName + stepName+mealType;
					if (!tmpDto.containsKey(key)) {
						String menuIndexId=menuDict.getMenuIndexId();
						tmpDto.put(key, menuIndexId);
						MenuStep menuStep = new MenuStep();
						menuStep.setStepNum(stepSpec.getStepNum());
						menuStep.setMenuIndexId(menuIndexId);
						menuStep.setStepName(stepName);
						menuStep.setShopId(shopId);
						menuStep.setLanguageType(languageType);
						menuStep.setChooseType(BussCons.CHOOSE_TYPE_RADIO);
						menuStep.setMaxChoose(0);
						menuStep.setCreateBy(shopId);
						menuStep.setCreateTime(WebplusUtil.getDateTime());
						menuStep.setUpdateBy(shopId);
						menuStep.setUpdateTime(WebplusUtil.getDateTime());
						menuStepList.add(menuStep);
					}
					String menuIndexId=tmpDto.getString(key);
					stepSpec.setMenuIndexId(menuIndexId);

				}

			} else {
				String key = catalogName + menuName + stepName+mealType;
				if (!tmpDto.containsKey(key)) {
					tmpDto.put(key, key);
					MenuStep menuStep = new MenuStep();
					menuStep.setStepName(stepName);
					menuStep.setStepNum(stepSpec.getStepNum());
					menuStep.setShopId(shopId);
					menuStep.setLanguageType(languageType);
					menuStep.setChooseType(BussCons.CHOOSE_TYPE_RADIO);
					menuStep.setMaxChoose(0);
					menuStep.setCreateBy(shopId);
					menuStep.setCreateTime(WebplusUtil.getDateTime());
					menuStep.setUpdateBy(shopId);
					menuStep.setUpdateTime(WebplusUtil.getDateTime());
					menuStepList.add(menuStep);
				}

			}

		}
		tmpDto.clear();
		return menuStepList;
	}
	/**
	 * 
	 * 简要说明：通过类目和菜单名称获取菜单实体类
	 * 编写者：陈骑元
	 * 创建时间：2019年10月27日 上午10:23:25
	 * @param 说明
	 * @return 说明
	 */
    public MenuDict getMenuDict(String catalogName,String menuName,String mealType,List<MenuDict> menuDictDBList){
		for(MenuDict menuDict:menuDictDBList){
			if(catalogName.equals(menuDict.getCatalogName().trim())&&menuName.equals(menuDict.getMenuName().trim())&&
					mealType.equals(menuDict.getMealType())){
				
				return menuDict;
			}
		}
		
		return null;
	}
	
	/**
	 * 
	 * 简要说明：读写规格Excel
	 * 编写者：陈骑元
	 * 创建时间：2019年8月7日 上午8:30:16
	 * @param 说明
	 * @return 说明
	 */
	private Map<String,List<StepSpec>> readStepSpecExcel(String filePath,List<String> languageTypeList,String shopId){
		Map<String,List<StepSpec>> specMap=new LinkedHashMap<String,List<StepSpec>>();
		 for(String languageType:languageTypeList){
			 int sheet=Integer.parseInt(languageType);
			 List<StepSpec> specModelList=this.readExcelToStepSpec(filePath, sheet, languageType,shopId);
			 specMap.put(languageType, specModelList);
		 }
		return specMap;
	}
	
	/**
	 * 
	 * 简要说明：检查规格
	 * 编写者：陈骑元
	 * 创建时间：2019年10月30日 下午11:18:47
	 * @param 说明
	 * @return 说明
	 */
	private StepSpec checkStepSpec(String stepIndexId,String specName,String languageType,List<StepSpec> stepSpecDBList){
		for(StepSpec stepSpec:stepSpecDBList){
			if(stepIndexId.equals(stepSpec.getStepIndexId())
					&&specName.equals(stepSpec.getSpecName())&&languageType.equals(stepSpec.getLanguageType())){
				
				return stepSpec;
			}
		}
		return null;
	}
	/**
	 * 
	 * 简要说明：检查步骤规格
	 * 编写者：陈骑元
	 * 创建时间：2019年10月30日 下午11:18:47
	 * @param 说明
	 * @return 说明
	 */
	private MenuStep checkMenuStep(String menuIndexId,String stepName,String languageType,List<MenuStep> menuStepDBList){
		for(MenuStep menuStep:menuStepDBList){
			if(menuIndexId.equals(menuStep.getMenuIndexId())
					&&stepName.equals(menuStep.getStepName())&&languageType.equals(menuStep.getLanguageType())){
				
				return menuStep;
			}
		}
		return null;
	}
	/**
	 * 
	 * 简要说明：读写Excel到规格的模型中
	 * 编写者：陈骑元
	 * 创建时间：2019年8月7日 上午8:45:55
	 * @param 说明
	 * @return 说明
	 */
	private List<StepSpec> readExcelToStepSpec(String filePath,int sheet,String languageType,String shopId){
		List<StepSpec> stepSpecList=Lists.newArrayList();
		
		try {
			ImportParams params = new ImportParams();
	        params.setTitleRows(0);
	        params.setHeadRows(1);
			params.setStartSheetIndex(sheet-1);
			params.setSheetNum(1);
			List< SpecModel> dataList= ExcelImportUtil.importExcel(new File(filePath),
					 SpecModel.class, params);
			if(WebplusUtil.isNotEmpty(dataList)){
				 for(SpecModel specModel: dataList) {
			          if(WebplusUtil.isNotEmpty(specModel)){
			        	  String catalogName=specModel.getCatalogName();
			        	  String menuName=specModel.getMenuName();
			        	  String stepName=specModel.getStepName();
			        	  String specName=specModel.getSpecName();
			        	 if(WebplusUtil.isNotAnyEmpty(catalogName,menuName,stepName,specName)){
			        		 catalogName=catalogName.trim();
			        		 menuName=menuName.trim();
			        		 stepName=stepName.trim();
			        		 specName=specName.trim();
			        		 StepSpec stepSpec=new StepSpec();
			        		 WebplusUtil.copyProperties(specModel, stepSpec);
			        		 stepSpec.setCatalogName(catalogName);
			        		 stepSpec.setMenuName(menuName);
			        		 stepSpec.setStepName(stepName);
			        		 stepSpec.setSpecName(specName);
			        		 stepSpec.setLanguageType(languageType);
			        		 stepSpec.setShopId(shopId);
			        		 stepSpec.setCreateBy(shopId);
			        		 stepSpec.setCreateTime(WebplusUtil.getDateTime());
			        		 stepSpec.setUpdateBy(shopId);
			        		 stepSpec.setUpdateTime(WebplusUtil.getDateTime());
			        		 stepSpecList.add(stepSpec);
			        	 }
			          }
			      }
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return stepSpecList;
		
	}
}

