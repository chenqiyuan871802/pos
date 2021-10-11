package com.ims.system.controller;

import org.springframework.web.bind.annotation.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.ims.core.cache.WebplusCache;
import com.ims.core.constant.WebplusCons;
import com.ims.core.matatype.Dto;
import com.ims.core.matatype.Dtos;
import com.ims.core.util.WebplusSqlHelp;
import com.ims.core.util.WebplusUtil;
import com.ims.core.vo.R;
import com.ims.core.vo.TreeModel;
import com.ims.core.web.BaseController;
import com.ims.system.constant.SystemCons;
import com.ims.system.model.Menu;
import com.ims.system.service.MenuService;
import com.ims.system.util.SystemCache;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;

/**
 * <p>
 * 菜单配置 前端控制器
 * </p>
 *
 * @author 陈骑元
 * @since 2018-09-28
 */
@Controller
@RequestMapping("/system/menu")
public class MenuController extends BaseController {

    @Autowired
    private MenuService menuService;

	/**
	 * 
	 * 加载菜单树
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "loadMenuTree")
	@ResponseBody
	public List<TreeModel> loadMenuTree(HttpServletRequest request, HttpServletResponse response) {
		Dto pDto=Dtos.newDto(request);
		List<TreeModel> treeModelList=menuService.loadMenuTree(pDto);
		return treeModelList;
	}
	/**
	 * 
	 * 简要说明：分页查询 
	 * 编写者：陈骑元
	 * 创建时间：2018-09-28
	 * @param 说明
	 * @return 说明
	 */
	@RequiresPermissions("system:menu:menu")
	@RequestMapping("list")
	@ResponseBody
	public R list() {
		Dto pDto = Dtos.newDto(request);
		pDto.setOrder("cascade_id  ASC,sort_no ASC ");
		Page<Menu> page =menuService.likePage(pDto);
		
		WebplusCache.convertItem(page);
		return R.toPage(page);
	}

	
	/**
	 * 
	 * 简要说明： 新增信息保存 
	 * 编写者：陈骑元
	 * 创建时间：2018-09-28
	 * @param 说明
	 * @return 说明
	 */
	@RequiresPermissions("system:menu:add")
	@PostMapping("save")
	@ResponseBody
	public R save(Menu menu) {
		String userId=this.getUserId();
		
		Dto pDto=Dtos.newDto(request);
		if(!SystemCons.MODULE_TYPE_PARENT.equals(menu.getModuleType())){
			EntityWrapper<Menu> countWrapper = new EntityWrapper<Menu>();
			WebplusSqlHelp.eq(countWrapper, "parent_id", menu.getParentId());
			WebplusSqlHelp.eq(countWrapper, "menu_code", menu.getMenuCode());
			int count=menuService.selectCount(countWrapper);
			if(count>0){
				return R.warn("菜单编码已被占用，请使用其它编码。");
			}
		}
		List<Menu> menuList=new ArrayList<Menu>();
		Dto calcDto = Dtos.newCalcDto("MAX(cascade_id)");
		calcDto.put("parentId", menu.getParentId());
		String maxCascadeId =menuService.calc(calcDto);
		if(WebplusUtil.isEmpty(maxCascadeId)){
			Menu parentMenu=menuService.selectById(menu.getParentId());
			if(WebplusUtil.isEmpty(parentMenu)){
				maxCascadeId="0.0000";
			}else{
				maxCascadeId=parentMenu.getCascadeId()+".0000";
			}
			
		}
		
		Date now=WebplusUtil.getDateTime();
		String curCascadeId=WebplusUtil.createCascadeId(maxCascadeId, 9999);
		menu.setCascadeId(curCascadeId);
		menu.setMenuId(WebplusUtil.uuid());
		menu.setCreateBy(userId);
		menu.setUpdateBy(userId);
		menu.setCreateTime(now);
		menu.setUpdateTime(now);
		menuList.add(menu);
		/*
		 * 判断是否自动生成新增、修改、删除菜单按钮
		 */
		if(WebplusCons.WHETHER_YES.equals(pDto.getString("whether_add"))){
			
			/*
			 * 添加新增按钮
			 */
			Menu menuAdd=new Menu();
			menuAdd.setMenuName("新增");//菜单名称
			menuAdd.setMenuCode("add");//菜单编码
			menuAdd.setParentId(menu.getMenuId());//菜单父级编号
			menuAdd.setMenuType(menu.getMenuType());//菜单类型
			menuAdd.setCascadeId(curCascadeId+".0001");//分类科目语义ID
			menuAdd.setIsAutoExpand(WebplusCons.WHETHER_NO);//是否自动展开(0否、1是)
			menuAdd.setStatus(WebplusCons.ENABLED_YES);//当前状态(0:停用;1:启用)
			menuAdd.setEditMode(WebplusCons.EDITMODE_EDIT);//编辑模式(0:只读;1:可编辑)
			menuAdd.setSortNo(1);//排序号
			menuAdd.setModuleType(SystemCons.MODULE_TYPE_BUTTON);//模块类型 1父级菜单2子菜单3按钮
			menuAdd.setCreateBy(userId);
			menuAdd.setUpdateBy(userId);
			menuAdd.setCreateTime(now);
			menuAdd.setUpdateTime(now);
			menuList.add(menuAdd);
			/*
			 * 添加编辑按钮
			 */
			Menu menuEdit=new Menu();
			menuEdit.setMenuName("编辑");//菜单名称
			menuEdit.setMenuCode("edit");//菜单编码
			menuEdit.setParentId(menu.getMenuId());//菜单父级编号
			menuEdit.setMenuType(menu.getMenuType());//菜单类型
			menuEdit.setCascadeId(curCascadeId+".0002");//分类科目语义ID
			menuEdit.setIsAutoExpand(WebplusCons.WHETHER_NO);//是否自动展开(0否、1是)
			menuEdit.setStatus(WebplusCons.ENABLED_YES);//当前状态(0:停用;1:启用)
			menuEdit.setEditMode(WebplusCons.EDITMODE_EDIT);//编辑模式(0:只读;1:可编辑)
			menuEdit.setSortNo(2);//排序号
			menuEdit.setModuleType(SystemCons.MODULE_TYPE_BUTTON);//模块类型 1父级菜单2子菜单3按钮
			menuEdit.setCreateBy(userId);
			menuEdit.setUpdateBy(userId);
			menuEdit.setCreateTime(now);
			menuEdit.setUpdateTime(now);
			menuList.add(menuEdit);
			/*
			 * 添加删除按钮
			 */
			Menu menuDelete=new Menu();
			menuDelete.setMenuName("删除");//菜单名称
			menuDelete.setMenuCode("remove");//菜单编码
			menuDelete.setParentId(menu.getMenuId());//菜单父级编号
			menuDelete.setMenuType(menu.getMenuType());//菜单类型
			menuDelete.setCascadeId(curCascadeId+".0003");//分类科目语义ID
			menuDelete.setIsAutoExpand(WebplusCons.WHETHER_NO);//是否自动展开(0否、1是)
			menuDelete.setStatus(WebplusCons.ENABLED_YES);//当前状态(0:停用;1:启用)
			menuDelete.setEditMode(WebplusCons.EDITMODE_EDIT);//编辑模式(0:只读;1:可编辑)
			menuDelete.setSortNo(3);//排序号
			menuDelete.setModuleType(SystemCons.MODULE_TYPE_BUTTON);//模块类型 1父级菜单2子菜单3按钮
			menuDelete.setCreateBy(userId);
			menuDelete.setUpdateBy(userId);
			menuDelete.setCreateTime(now);
			menuDelete.setUpdateTime(now);
			menuList.add(menuDelete);
		}
		boolean result =menuService.insertBatch(menuList);
		if (result) {
			SystemCache.romveCacheMenu();
			return R.ok();
		} else {
			return R.error("保存失败");
		}
	

	}
	/**
	 * 
	 * 简要说明： 跳转到编辑页面 
	 * 编写者：陈骑元
	 * 创建时间：2018-09-28
	 * @param 说明
	 * @return 说明
	 */
	@RequiresPermissions("system:menu:edit")
	@PostMapping("edit")
	@ResponseBody
	public R edit(String id) {
		Menu menu=menuService.selectById(id);
        return R.toData(menu);		
	}
	
	/**
	 * 
	 * 简要说明：修改信息
	 * 编写者：陈骑元
	 * 创建时间：2018-09-28
	 * @param 说明
	 * @return 说明
	 */
    @RequiresPermissions("system:menu:edit")
	@PostMapping("update")
	@ResponseBody
	public R update(Menu menu) {
    	String userId=this.getUserId();
    	Date updateTime=WebplusUtil.getDateTime();
		Menu menuOld=menuService.selectById(menu.getMenuId());
		String menuCode=menu.getMenuCode();
		if(WebplusUtil.isNotEmpty(menuCode)){
			if(!SystemCons.MODULE_TYPE_PARENT.equals(menu.getModuleType())){
				if(!menuOld.getMenuCode().equals(menuCode)){  //菜单编码有改变
					EntityWrapper<Menu> countWrapper = new EntityWrapper<Menu>();
					WebplusSqlHelp.eq(countWrapper, "parent_id", menuOld.getParentId());
					WebplusSqlHelp.eq(countWrapper, "menu_code", menu.getMenuCode());
					int count=menuService.selectCount(countWrapper);
					if(count>0){
						return R.warn("菜单编码已被占用，请使用其它编码。");
					}
				}
			}
			
		}
		if(SystemCons.MODULE_TYPE_PARENT.equals(menu.getModuleType())){
			if(!menuOld.getMenuType().equals(menu.getMenuType())){  //菜单类型变化了，级联更新下级菜单
				EntityWrapper<Menu> menuWrapper = new EntityWrapper<Menu>();
				WebplusSqlHelp.rlike(menuWrapper, "cascade_id", menuOld.getCascadeId());
				Menu menuEntity=new Menu();
				menuEntity.setMenuType(menu.getMenuType());
				menuEntity.setUpdateBy(userId);
				menuEntity.setUpdateTime(updateTime);
				menuService.update(menuEntity, menuWrapper);
			}
		}
		if(!SystemCons.MODULE_TYPE_SUB.equals(menu.getModuleType())){
			String status=menu.getStatus();
			if(!menuOld.getStatus().equals(status)){  //启用是否发生变化，如果发生变化级联锁定下级
				if(WebplusCons.ENABLED_NO.equals(status)){  //只有禁用的时候在级联更新
					EntityWrapper<Menu> menuWrapper = new EntityWrapper<Menu>();
					WebplusSqlHelp.rlike(menuWrapper, "cascade_id", menuOld.getCascadeId());
					Menu menuEntity=new Menu();
					menuEntity.setStatus(status);
					menuEntity.setUpdateBy(userId);
					menuEntity.setUpdateTime(updateTime);
					menuService.update(menuEntity, menuWrapper);
				}
				
			}
		}
		menu.setUpdateBy(userId);
		menu.setUpdateTime(updateTime);
		boolean result = menuService.updateById(menu);
		if (result) {
			SystemCache.romveCacheMenu();
			return R.ok();
		} else {
			return R.error("更新失败");
		}
		
	}
	
	/**
	 * 
	 * 简要说明：删除信息
	 * 编写者：陈骑元
	 * 创建时间：2018-09-28
	 * @param 说明
	 * @return 说明
	 */
	@RequiresPermissions("system:menu:remove")
	@PostMapping("remove")
	@ResponseBody
	public R remove(String id) {
		Menu menu=menuService.selectById(id);
		String moduleType=menu.getModuleType();
		 //如果是父菜单则进行下面校验校验是否存在子菜单，存在子菜单不允许删除
		if(SystemCons.MODULE_TYPE_PARENT.equals(moduleType)){ 
			EntityWrapper<Menu> countWrapper = new EntityWrapper<Menu>();
			WebplusSqlHelp.eq(countWrapper, "parent_id", id);
			int row=menuService.selectCount(countWrapper);
			if(row>0){
				
				return R.warn("操作失败，当前菜单下存在子菜单，不允许删除，请先删除子菜单然后再删除。");
			}
		}
		if(SystemCons.MODULE_TYPE_SUB.equals(moduleType)){ //如果是子菜单则删除按钮菜单
			EntityWrapper<Menu> removeWrapper = new EntityWrapper<Menu>();
			WebplusSqlHelp.eq(removeWrapper, "parent_id", id);
			menuService.delete(removeWrapper);
		}
		boolean result = menuService.deleteById(id);
		if (result) {
			SystemCache.romveCacheMenu();
			return R.ok("删除成功");
		} else {
			return R.error("删除失败");
		}
		
	}
	

	/**
	 * 
	 * 简要说明：刷新菜单缓存
	 * 编写者：陈骑元
	 * 创建时间：2018年5月13日 下午11:09:04
	 * @param 说明
	 * @return 说明
	 */
	@RequiresPermissions("system:menu:refreshCache")
	@PostMapping("refreshCache")
	@ResponseBody
	public R refreshCache() {
		SystemCache.romveCacheMenu();
	    
		return R.ok("刷新菜单缓存操作成功");
	}
}

