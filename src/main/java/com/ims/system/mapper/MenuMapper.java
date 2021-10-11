package com.ims.system.mapper;

import java.util.List;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.ims.core.matatype.Dto;
import com.ims.system.model.Menu;

/**
 * <p>
 * 菜单配置 Mapper 接口
 * </p>
 *
 * @author 陈骑元
 * @since 2018-09-28
 */
public interface MenuMapper extends BaseMapper<Menu> {

    /**
	 * 根据Dto查询并返回数据持久化对象集合
	 * 
	 * @return List<Menu>
	 */
	List<Menu> list(Dto pDto);
    /**
	 * 根据Dto查询并返回分页数据持久化对象集合
	 * 
	 * @return List<Menu>
	 */
	List<Menu> listPage(Pagination page,Dto pDto);
		
	/**
	 * 根据Dto模糊查询并返回数据持久化对象集合(字符型字段模糊匹配，其余字段精确匹配)
	 * 
	 * @return List<Menu>
	 */
	List<Menu> like(Dto pDto);

	/**
	 * 根据Dto模糊查询并返回分页数据持久化对象集合(字符型字段模糊匹配，其余字段精确匹配)
	 * 
	 * @return List<Menu>
	 */
	List<Menu> likePage(Pagination page,Dto pDto);
	
	/**
	 * 根据数学表达式进行数学运算
	 * 
	 * @param pDto
	 * @return String
	 */
	 String calc(Dto pDto);


}
