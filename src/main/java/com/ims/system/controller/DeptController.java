package com.ims.system.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
import com.ims.system.model.Dept;
import com.ims.system.service.DeptService;

/**
 * <p>
 * 组织机构 前端控制器
 * </p>
 *
 * @author 陈骑元
 * @since 2018-05-14
 */
@Controller
@RequestMapping("/system/dept")
public class DeptController extends BaseController {

    @Autowired
    private DeptService deptService;
	
	/**
	 * 
	 * 加载组织机构树
	 * @param request
	 * @param response
	 */
    
	@RequestMapping(value = "loadDeptTree")
	@ResponseBody
	public List<TreeModel> loadDeptTree(HttpServletRequest request, HttpServletResponse response) {
		Dto pDto=Dtos.newDto(request);
		List<TreeModel> treeModelList=deptService.loadDeptTree(pDto);
		
		return treeModelList;
	}
	
	/**
	 * 
	 * 简要说明：分页查询 
	 * 编写者：陈骑元
	 * 创建时间：2018-05-14
	 * @param 说明
	 * @return 说明
	 */
	@RequiresPermissions("system:dept:dept")
	@RequestMapping("list")
	@ResponseBody
	public R list() {
		Dto pDto = Dtos.newDto(request);
		pDto.put("isDel", WebplusCons.WHETHER_NO);
		pDto.setOrder(" LENGTH(cascade_id) ASC,sort_no ASC ");
		Page<Dept> page =deptService.likePage(pDto);
		WebplusCache.convertItem(page);
		return R.toPage(page);
	}
 

	/**
	 * 
	 * 简要说明： 新增信息保存 
	 * 编写者：陈骑元
	 * 创建时间：2018-05-14
	 * @param 说明
	 * @return 说明
	 */
	@RequiresPermissions("system:dept:add")
	@PostMapping("save")
	@ResponseBody
	public R save(Dept dept) {
		String userId=this.getUserId();
		Dto calcDto = Dtos.newCalcDto("MAX(cascade_id)");
		calcDto.put("parentId", dept.getParentId());
		String maxCascadeId =deptService.calc(calcDto);
		if(WebplusUtil.isEmpty(maxCascadeId)){
			Dept parentDept=deptService.selectById(dept.getParentId());
			if(WebplusUtil.isEmpty(parentDept)){
					maxCascadeId="0.0000";
			}else{
				maxCascadeId=parentDept.getCascadeId()+".0000";
			}
				
		}
		String curCascadeId=WebplusUtil.createCascadeId(maxCascadeId, 9999);
		dept.setCascadeId(curCascadeId);
		dept.setCreateBy(userId);
		dept.setUpdateBy(userId);
		dept.setCreateTime(WebplusUtil.getDateTime());
		dept.setUpdateTime(WebplusUtil.getDateTime());
		boolean result = deptService.insert(dept);
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
	 * 创建时间：2018-05-14
	 * @param 说明
	 * @return 说明
	 */
	@RequiresPermissions("system:dept:edit")
	@PostMapping("edit")
	@ResponseBody
	public R edit(String id) {
		Dept dept=deptService.selectById(id);
		String parentId=dept.getParentId();
		if(SystemCons.TREE_ROOT_ID.equals(id)){
			dept.setParentName("顶级结构");
		}else{
			Dept parentDept=deptService.selectById(parentId);
			dept.setParentName(parentDept.getDeptName());
			
		}
		
		return R.toData(dept);
	}
	/**
	 * 
	 * 简要说明：修改信息
	 * 编写者：陈骑元
	 * 创建时间：2018-05-14
	 * @param 说明
	 * @return 说明
	 */
	@RequiresPermissions("system:dept:edit")
	@PostMapping("update")
	@ResponseBody
	public R update(Dept dept) {
		String userId=this.getUserId();
		dept.setUpdateTime(WebplusUtil.getDateTime());
		dept.setUpdateBy(userId);
		boolean result = deptService.updateById(dept);
		if (result) {
			return R.ok();
		} else {
			return R.error("更新失败");
		}
		
	}

	/**
	 * 
	 * 简要说明：保存移动机构信息
	 * 编写者：陈骑元
	 * 创建时间：2018-05-14
	 * @param 说明
	 * @return 说明
	 */
	@RequiresPermissions("system:dept:move")
	@PostMapping("saveMoveDept")
	@ResponseBody
	public R saveMoveDept(Dept dept) {
		boolean result = deptService.updateDept(dept);
		if (result) {
			return R.ok("移动机构成功");
		} else {
			return R.error("移动机构失败");
		}
		
	}
	
	
	/**
	 * 
	 * 简要说明：删除信息
	 * 编写者：陈骑元
	 * 创建时间：2018-05-14
	 * @param 说明
	 * @return 说明
	 */
	@RequiresPermissions("system:dept:remove")
	@PostMapping("remove")
	@ResponseBody
	public R remove(String id) {
		String userId=this.getUserId();
		EntityWrapper<Dept> wrapper = new EntityWrapper<Dept>();
		WebplusSqlHelp.eq(wrapper, "parent_id", id);
		WebplusSqlHelp.eq(wrapper, "is_del", WebplusCons.WHETHER_NO);
		int row=deptService.selectCount(wrapper);
		if(row>0){
			return R.warn("操作失败，当前组织机构下存在子机构，不允许删除，请先删除子机构然后再删除。");
		}
		Dept dept=new Dept();
		dept.setDeptId(id);
		dept.setUpdateBy(userId);
		dept.setIsDel(WebplusCons.WHETHER_YES);
		dept.setUpdateTime(WebplusUtil.getDateTime());
		boolean result = deptService.updateById(dept);
		if (result) {
			return R.ok();
		} else {
			return R.error("删除失败");
		}
		
	}
	
}

