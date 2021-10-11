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
import com.ims.core.vo.UserToken;
import com.ims.core.web.BaseController;
import com.ims.system.constant.SystemCons;
import com.ims.system.model.Param;
import com.ims.system.service.ParamService;
import com.ims.system.util.SystemCache;

import org.springframework.stereotype.Controller;

/**
 * <p>
 * 键值参数 前端控制器
 * </p>
 *
 * @author 陈骑元
 * @since 2018-04-12
 */
@Controller
@RequestMapping("/system/param")
public class ParamController extends BaseController {
	
    @Autowired
    private ParamService paramService;
	

	/**
	 * 
	 * 简要说明：分页查询 
	 * 编写者：陈骑元
	 * 创建时间：2018-04-12
	 * @param 说明
	 * @return 说明
	 */
    @RequiresPermissions("system:param:param")
	@RequestMapping("list")
	@ResponseBody
	public R list() {
		Dto pDto = Dtos.newDto(request);
		pDto.setOrder("create_time DESC");
		Page<Param> page =paramService.likePage(pDto);
		WebplusCache.convertItem(page);
		return R.toPage(page);
	}



	/**
	 * 
	 * 简要说明： 新增信息保存 
	 * 编写者：陈骑元
	 * 创建时间：2018-04-12
	 * @param 说明
	 * @return 说明
	 */
    @RequiresPermissions("system:param:add")
	@PostMapping("save")
	@ResponseBody
	public R save(Param param) {
    	String userId=this.getUserId();
		EntityWrapper<Param> countWrapper = new EntityWrapper<Param>();
		WebplusSqlHelp.eq(countWrapper, "param_key", param.getParamKey());
		int count=paramService.selectCount(countWrapper);
		if(count>0){
			return R.warn("参数键已被占用，请修改其它参数键再保存。");
		}
		param.setCreateBy(userId);
		param.setUpdateBy(userId);
		param.setCreateTime(WebplusUtil.getDateTime());
		param.setUpdateTime(WebplusUtil.getDateTime());
		boolean result = paramService.insert(param);
		if (result) {
			SystemCache.cacheParam(param.getParamKey());
			return R.ok();
		} else {
			return R.error("保存失败");
		}

	}
	/**
	 * 
	 * 简要说明： 跳转到编辑页面 
	 * 编写者：陈骑元
	 * 创建时间：2018-04-12
	 * @param 说明
	 * @return 说明
	 */
    @RequiresPermissions("system:param:edit")
	@PostMapping("edit")
	@ResponseBody
	public R edit(String id) {
		Param param=paramService.selectById(id);
		return R.toData(param);
	}
	
	/**
	 * 
	 * 简要说明：修改信息
	 * 编写者：陈骑元
	 * 创建时间：2018-04-12
	 * @param 说明
	 * @return 说明
	 */
    @RequiresPermissions("system:param:edit")
	@PostMapping("update")
	@ResponseBody
	public R update(Param param,String oldParamKey) {
    	String userId=this.getUserId();
		if(WebplusUtil.isNotEmpty(oldParamKey)){
			if(!oldParamKey.equals(param.getParamKey())){
				EntityWrapper<Param> countWrapper = new EntityWrapper<Param>();
				WebplusSqlHelp.eq(countWrapper, "param_key", param.getParamKey());
				int count=paramService.selectCount(countWrapper);
				if(count>0){
					return R.warn("参数键已被占用，请修改其它参数键再保存。");
				}
				SystemCache.removeCacheParam(oldParamKey);
			}
			
		}
		param.setUpdateBy(userId);
		param.setUpdateTime(WebplusUtil.getDateTime());
		boolean result = paramService.updateById(param);
		if (result) {
			String paramKey=param.getParamKey();
			if(WebplusUtil.isEmpty(paramKey)){
				Param paramEntity=paramService.selectById(param.getParamId());
				paramKey=paramEntity.getParamKey();
			}
			SystemCache.cacheParam(paramKey);
			return R.ok();
		} else {
			return R.error("更新失败");
		}
		
	}
	

	/**
	 * 
	 * 简要说明：删除信息
	 * 编写者：陈骑元
	 * 创建时间：2018-04-12
	 * @param 说明
	 * @return 说明
	 */
	@RequiresPermissions("system:param:remove")
	@PostMapping("remove")
	@ResponseBody
	public R remove(String id) {
		Param param=paramService.selectById(id);
		UserToken userToken=this.getUserToken();
		if(WebplusCons.EDITMODE_READ.equals(param.getEditMode())
				&&!SystemCons.SUPER_ADMIN.equals(userToken.getAccount())){
			
			return R.warn("当前删除的键值参数数据为只读，只读的数据不能修改和删除");
		}
		boolean result = paramService.deleteById(id);
		if (result) {
			SystemCache.removeCacheParam(param.getParamKey());
			return R.ok();
		} else {
			return R.error("删除失败");
		}
		
	}
	/**
	 * 
	 * 简要说明：刷新键值参数缓存
	 * 编写者：陈骑元
	 * 创建时间：2018年5月13日 下午11:09:04
	 * @param 说明
	 * @return 说明
	 */
	@RequiresPermissions("system:param:refreshParam")
	@PostMapping("refreshParam")
	@ResponseBody
	public R refreshParam() {
		SystemCache.refreshParam();
	    
		return R.ok("刷新键值参数缓存操作成功");
	}
	
	
}

