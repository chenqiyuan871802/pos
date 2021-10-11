package com.ims.system.controller;

import org.springframework.web.bind.annotation.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.google.common.collect.Lists;
import com.ims.core.cache.WebplusCache;
import com.ims.core.constant.WebplusCons;
import com.ims.core.matatype.Dto;
import com.ims.core.matatype.Dtos;
import com.ims.core.util.WebplusFormater;
import com.ims.core.util.WebplusHashCodec;
import com.ims.core.util.WebplusSqlHelp;
import com.ims.core.util.WebplusUtil;
import com.ims.core.vo.R;
import com.ims.core.web.BaseController;
import com.ims.system.constant.SystemCons;
import com.ims.system.model.User;
import com.ims.system.service.UserService;

import java.util.List;



import org.springframework.stereotype.Controller;

/**
 * <p>
 * 用户基本信息表 前端控制器
 * </p>
 *
 * @author 陈骑元
 * @since 2018-09-28
 */
@Controller
@RequestMapping("/system/user")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

	

	/**
	 * 
	 * 简要说明：分页查询 
	 * 编写者：陈骑元
	 * 创建时间：2018-09-28
	 * @param 说明
	 * @return 说明
	 */
	@RequestMapping("list")
	@ResponseBody
	public R list() {
		Dto pDto = Dtos.newDto(request);
		pDto.put("isDel", WebplusCons.WHETHER_NO);
		pDto.setOrder(" a.create_time DESC ");
		Page<User> page =userService.listUserPage(pDto);
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
    @RequiresPermissions("system:user:add")
	@PostMapping("save")
	@ResponseBody
	public R save(User user) {
    	String userId=this.getUserId();
		EntityWrapper<User> countWrapper = new EntityWrapper<User>();
		WebplusSqlHelp.eq(countWrapper, "account", user.getAccount());
		WebplusSqlHelp.eq(countWrapper, "is_del",WebplusCons.WHETHER_NO);
		int count=userService.selectCount(countWrapper);
		if(count>0){
			return R.warn("该账号已被注册，请注册其它账号。");
		}
		user.setCreateBy(userId);
		user.setUpdateBy(userId);
		user.setCreateTime(WebplusUtil.getDateTime());
		user.setUpdateTime(WebplusUtil.getDateTime());
		user.setUserType(SystemCons.USER_TYPE_COMMON);
		String password=user.getPassword();
		String encryptPassword=WebplusHashCodec.md5(password);
		user.setPassword(encryptPassword);
		boolean result = userService.insert(user);
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
	 * 创建时间：2018-09-28
	 * @param 说明
	 * @return 说明
	 */
    @RequiresPermissions("system:user:edit")
	@PostMapping("edit")
	@ResponseBody
	public R edit(String id) {
		User user=userService.selectById(id);
		/*Dept dept=deptService.selectById(user.getDeptId());
		user.setDeptName(dept.getDeptName());*/
		return R.toData(user);
	}
	
	/**
	 * 
	 * 简要说明：修改信息
	 * 编写者：陈骑元
	 * 创建时间：2018-09-28
	 * @param 说明
	 * @return 说明
	 */
    @RequiresPermissions("system:user:edit")
	@PostMapping("update")
	@ResponseBody
	public R update(User user) {
    	String userId=this.getUserId();
		user.setUpdateTime(WebplusUtil.getDateTime());
		if(WebplusCons.ENABLED_YES.equals(user.getStatus())){
			user.setErrorNum(0);
		}
		user.setUpdateBy(userId);
		user.setUpdateTime(WebplusUtil.getDateTime());
		boolean result = userService.updateById(user);
		if (result) {
			return R.ok();
		} else {
			return R.error("更新失败");
		}
		
	}

	/**
	 * 
	 * 简要说明：修改信息
	 * 编写者：陈骑元
	 * 创建时间：2018-09-28
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("updatePassword")
	@ResponseBody
	public R updatePassword(String userId,String password) {
		
		String updateBy=this.getUserId();
		if(WebplusUtil.isEmpty(userId)){  //如果用户编号为空，则是修改自己的用户密码
			userId=updateBy;
		}
		String encryptPassword=WebplusHashCodec.md5(password);
		User user=new User();
		user.setUserId(userId);
		user.setPassword(encryptPassword);
		user.setUpdateBy(updateBy);
		user.setUpdateTime(WebplusUtil.getDateTime());
		boolean result = userService.updateById(user);
		if (result) {
			return R.ok("密码重置成功");
		} else {
			return R.error("密码重置失败");
		}
		
	}
	/**
	 * 
	 * 简要说明：移动用户
	 * 编写者：陈骑元
	 * 创建时间：2018-09-28
	 * @param 说明
	 * @return 说明
	 */
	@RequiresPermissions("system:user:move")
	@PostMapping("saveMoveUser")
	@ResponseBody
	public R saveMoveUser(String ids,String deptId) {
		String updateBy=this.getUserId();
		List<String> idList=WebplusFormater.separatStringToList(ids);
		List<User> userList=Lists.newArrayList();
		for(String userId:idList){
			User user=new User();
			user.setUserId(userId);
			user.setDeptId(deptId);
			user.setUpdateBy(updateBy);
			user.setUpdateTime(WebplusUtil.getDateTime());
			userList.add(user);
		}
		boolean result=userService.updateBatchById(userList);
		if (result) {
			return R.ok("移动用户成功");
		} else {
			return R.error("移动用户失败");
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
	@RequiresPermissions("system:user:remove")
	@PostMapping("remove")
	@ResponseBody
	public R remove(String id) {
		String updateBy=this.getUserId();
		User user=new User();
		user.setUserId(id);
		user.setIsDel(WebplusCons.WHETHER_YES);
		user.setUpdateBy(updateBy);
		user.setUpdateTime(WebplusUtil.getDateTime());
		boolean result=userService.updateById(user);
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
	 * 创建时间：2018-09-28
	 * @param 说明
	 * @return 说明
	 */
	@RequiresPermissions("system:user:remove")
	@PostMapping("batchRemove")
	@ResponseBody
	public R batchRemove(String ids) {
		String updateBy=this.getUserId();
		List<String> idList=WebplusFormater.separatStringToList(ids);
		List<User> userList=Lists.newArrayList();
		for(String userId:idList){
			User user=new User();
			user.setUserId(userId);
			user.setIsDel(WebplusCons.WHETHER_YES);
			user.setUpdateBy(updateBy);
			user.setUpdateTime(WebplusUtil.getDateTime());
			userList.add(user);
		}
		boolean result=userService.updateBatchById(userList);
		if (result) {
			return R.ok();
		} else {
			return R.error("删除失败");
		}
		
	}
	
}

