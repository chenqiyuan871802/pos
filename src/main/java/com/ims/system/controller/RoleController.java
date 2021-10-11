package com.ims.system.controller;

import org.springframework.web.bind.annotation.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import com.baomidou.mybatisplus.plugins.Page;
import com.ims.core.cache.WebplusCache;
import com.ims.core.matatype.Dto;
import com.ims.core.matatype.Dtos;
import com.ims.core.util.WebplusFormater;
import com.ims.core.util.WebplusUtil;
import com.ims.core.vo.R;
import com.ims.core.vo.TreeModel;
import com.ims.core.vo.UserToken;
import com.ims.core.web.BaseController;
import com.ims.system.constant.SystemCons;
import com.ims.system.model.Role;
import com.ims.system.service.RoleService;
import com.ims.system.util.SystemCache;

import java.util.List;
import org.springframework.stereotype.Controller;

/**
 * <p>
 * 角色表 前端控制器
 * </p>
 *
 * @author 陈骑元
 * @since 2018-10-02
 */
@Controller
@RequestMapping("/system/role")
public class RoleController extends BaseController {

    @Autowired
    private RoleService roleService;

	/**
	 * 
	 * 简要说明：分页查询 
	 * 编写者：陈骑元
	 * 创建时间：2018-10-02
	 * @param 说明
	 * @return 说明
	 */
    @RequiresPermissions("system:role:role")
	@RequestMapping("list")
	@ResponseBody
	public R list() {
		Dto pDto = Dtos.newDto(request);
		pDto.setOrder(" create_time DESC ");
		UserToken userToken=this.getUserToken();
		if(!SystemCons.SUPER_ADMIN.equals(userToken.getAccount())){  //如果不是超级管理员，只能查看自己创建角色
			pDto.put("createBy", userToken.getUserId());
		}
		Page<Role> page =roleService.likePage(pDto);
		WebplusCache.convertItem(page);
		return R.toPage(page);
	}

	
	/**
	 * 
	 * 简要说明： 新增信息保存 
	 * 编写者：陈骑元
	 * 创建时间：2018-10-02
	 * @param 说明
	 * @return 说明
	 */
    @RequiresPermissions("system:role:add")
	@PostMapping("save")
	@ResponseBody
	public R save(Role role) {
    	String userId=this.getUserId();
    	role.setCreateBy(userId);
    	role.setUpdateBy(userId);
		role.setCreateTime(WebplusUtil.getDateTime());
		role.setUpdateTime(WebplusUtil.getDateTime());
		boolean result = roleService.insert(role);
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
	 * 创建时间：2018-10-02
	 * @param 说明
	 * @return 说明
	 */
    @RequiresPermissions("system:role:edit")
	@PostMapping("edit")
	@ResponseBody
	public R edit(String id) {
		Role role=roleService.selectById(id);
	    return R.toData(role);
	}
	
	/**
	 * 
	 * 简要说明：修改信息
	 * 编写者：陈骑元
	 * 创建时间：2018-10-02
	 * @param 说明
	 * @return 说明
	 */
    @RequiresPermissions("system:role:edit")
	@PostMapping("update")
	@ResponseBody
	public R update(Role role) {
    	String userId=this.getUserId();
    	role.setUpdateBy(userId);
		role.setUpdateTime(WebplusUtil.getDateTime());
		boolean result = roleService.updateById(role);
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
	 * 创建时间：2018-10-02
	 * @param 说明
	 * @return 说明
	 */
	@RequiresPermissions("system:role:remove")
	@PostMapping("remove")
	@ResponseBody
	public R remove(String id) {
		boolean result = roleService.removeRole(id);
		if (result) {
			return R.ok();
		} else {
			return R.error("删除失败");
		}
		
	}
	
	
	
	/**
	 * 
	 * 简要说明：展示权限菜单树
	 * 编写者：陈骑元
	 * 创建时间：2018-12-18
	 * @param 说明
	 * @return 说明
	 */
	@RequiresPermissions("system:role:roleMenu")
	@PostMapping("listRoleMenu")
	@ResponseBody
	public List<TreeModel> listRoleMenu(String roleId) {
		UserToken userToken=this.getUserToken();
		List<TreeModel> roleMenuList=  roleService.listRoleMenu(roleId,userToken);
		return roleMenuList;
		
	}
	/**
	 * 
	 * 简要说明： 保存授权菜单
	 * 编写者：陈骑元
	 * 创建时间：2018-10-02
	 * @param 说明
	 * @return 说明
	 */
	@RequiresPermissions("system:role:roleMenu")
	@PostMapping("saveRoleMenu")
	@ResponseBody
	public R saveRoleMenu(String roleId,String menuIds) {
		String userId=this.getUserId();
		List<String> menuIdList=WebplusFormater.separatStringToList(menuIds);
		boolean result=roleService.batchSaveRoleMenu(roleId, menuIdList,userId);
		if (result) {
			return R.ok("授权菜单成功");
		} else {
			return R.error("授权菜单失败");
		}
	}
	
	/**
	 * 
	 * 简要说明： 保存授权用户
	 * 编写者：陈骑元
	 * 创建时间：2018-10-02
	 * @param 说明
	 * @return 说明
	 */
	@RequiresPermissions("system:role:roleUser")
	@PostMapping("saveRoleUser")
	@ResponseBody
	public R saveRoleUser(String roleId,String userIds) {
		String createBy=this.getUserId();
		List<String> userIdList=WebplusFormater.separatStringToList(userIds);
		boolean result=roleService.batchSaveRoleUser(roleId, userIdList,createBy);
		if (result) {
			return R.ok("用户授权成功");
		} else {
			return R.error("用户授权失败");
		}
	}
	/**
	 * 
	 * 简要说明： 撤销授权用户
	 * 编写者：陈骑元
	 * 创建时间：2018-10-02
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("removeRoleUser")
	@ResponseBody
	public R removeRoleUser(String roleId,String userIds) {
		List<String> userIdList=WebplusFormater.separatStringToList(userIds);
		boolean result=roleService.batchRemoveRoleUser(roleId, userIdList);
		if (result) {
			return R.ok("撤销用户授权成功");
		} else {
			return R.error("撤销用户授权失败");
		}
	}
	/**
	 * 
	 * 简要说明：清空字典缓存
	 * 编写者：陈骑元
	 * 创建时间：2018年5月13日 下午11:09:04
	 * @param 说明
	 * @return 说明
	 */
	@RequiresPermissions("system:role:refreshCache")
	@PostMapping("refreshCache")
	@ResponseBody
	public R refreshCache() {
		
		SystemCache.flushRoleMenu();
		
		return R.ok("清空缓存操作成功");
	}
}

