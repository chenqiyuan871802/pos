package com.ims.buss.mapper;

import com.ims.buss.model.MenuLarge;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import java.util.List;
import com.ims.core.matatype.Dto;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

/**
 * <p>
 * 菜品大类 Mapper 接口
 * </p>
 *
 * @author 陈骑元
 * @since 2020-07-26
 */
public interface MenuLargeMapper extends BaseMapper<MenuLarge> {

    /**
	 * 根据Dto查询并返回数据持久化对象集合
	 * 
	 * @return List<MenuLarge>
	 */
	List<MenuLarge> list(Dto pDto);
    /**
	 * 根据Dto查询并返回分页数据持久化对象集合
	 * 
	 * @return List<MenuLarge>
	 */
	List<MenuLarge> listPage(Pagination page,Dto pDto);
		
	/**
	 * 根据Dto模糊查询并返回数据持久化对象集合(字符型字段模糊匹配，其余字段精确匹配)
	 * 
	 * @return List<MenuLarge>
	 */
	List<MenuLarge> like(Dto pDto);

	/**
	 * 根据Dto模糊查询并返回分页数据持久化对象集合(字符型字段模糊匹配，其余字段精确匹配)
	 * 
	 * @return List<MenuLarge>
	 */
	List<MenuLarge> likePage(Pagination page,Dto pDto);
	
	/**
	 * 根据数学表达式进行数学运算
	 * 
	 * @param pDto
	 * @return String
	 */
	 String calc(Dto pDto);
	 /**
	  * 
	  * 简要说明：向上更新
	  * 编写者：陈骑元
	  * 创建时间：2019年7月24日 下午11:02:00
	  * @param 说明
	  * @return 说明
	  */
	 int updateSortUp(Dto pDto);
	 /**
	  * 
	  * 简要说明：向下更新
	  * 编写者：陈骑元
	  * 创建时间：2019年7月24日 下午11:02:00
	  * @param 说明
	  * @return 说明
	  */
	 int updateSortDown(Dto pDto);


}
