package com.ims.system.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ims.core.constant.WebplusCons;
import com.ims.core.matatype.Dto;
import com.ims.core.vo.Query;
import com.ims.core.vo.TreeModel;
import com.ims.system.constant.SystemCons;
import com.ims.system.mapper.MenuMapper;
import com.ims.system.model.Menu;

import org.assertj.core.util.Lists;
import org.springframework.stereotype.Service;
import java.util.List;

import com.baomidou.mybatisplus.plugins.Page;

/**
 * <p>
 * 菜单配置 服务实现类
 * </p>
 *
 * @author 陈骑元
 * @since 2018-09-28
 */
@Service
public class MenuService extends ServiceImpl<MenuMapper, Menu>  {
     
   
     /**
	 * 根据Dto查询并返回数据持久化对象集合
	 * 
	 * @return List<Menu>
	 */
	public List<Menu> list(Dto pDto){
	    
	    return baseMapper.list(pDto);
	};
    /**
	 * 根据Dto查询并返回分页数据持久化对象集合
	 * 
	 * @return Page<Menu>
	 */
	public Page<Menu> listPage(Dto pDto){
	    Query<Menu> query=new Query<Menu>(pDto);
	    query.setRecords(baseMapper.listPage(query,pDto));
	    
	    return query;
	};
		
	/**
	 * 根据Dto模糊查询并返回数据持久化对象集合(字符型字段模糊匹配，其余字段精确匹配)
	 * 
	 * @return List<Menu>
	 */
	public List<Menu> like(Dto pDto){
	
	    return baseMapper.like(pDto);
	
	};

	/**
	 * 根据Dto模糊查询并返回分页数据持久化对象集合(字符型字段模糊匹配，其余字段精确匹配)
	 * 
	 * @return Page<Menu>
	 */
	public Page<Menu> likePage(Dto pDto){
	    Query<Menu> query=new Query<Menu>(pDto);
	    query.setRecords(baseMapper.likePage(query,pDto));
	    return query;
	};
	/**
	 * 根据数学表达式进行数学运算
	 * 
	 * @param pDto
	 * @return String
	 */
	 public String calc(Dto pDto){
	 
	     return baseMapper.calc(pDto);
	 }
	 /**
	  * 加载菜单树
	  */
	public List<TreeModel> loadMenuTree(Dto pDto) {
		pDto.setOrder("LENGTH(cascade_id) ASC,sort_no ASC ");
		List<Menu> menuList=this.list(pDto);
		List<TreeModel> modelList=Lists.newArrayList();
		TreeModel rootModel=new TreeModel();
		rootModel.setName(SystemCons.MENU_ROOT_NAME);
		rootModel.setId(SystemCons.TREE_ROOT_ID);
		rootModel.setCascadeId(SystemCons.TREE_ROOT_CASCADE_ID);
		rootModel.setOpen(true);
		modelList.add(rootModel);
		for(int i=0;i<menuList.size();i++){
			Menu menu=menuList.get(i);
			String parentId=menu.getParentId();
			TreeModel treeModel=new TreeModel();
			treeModel.setId(menu.getMenuId());
			treeModel.setpId(parentId);
			treeModel.setCascadeId(menu.getCascadeId());
			treeModel.setName(menu.getMenuName());
			String is_auto_expand = menu.getIsAutoExpand();
			if(WebplusCons.WHETHER_YES.equals(is_auto_expand)){
				treeModel.setOpen(true);
			}
			treeModel.setChildren(null);
			treeModel.setChecked(false);
		    modelList.add(treeModel);
			
		}
		return modelList;
	}
	
	 
}
