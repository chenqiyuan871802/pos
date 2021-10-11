package com.ims.buss.mapper;

import com.ims.buss.model.MenuCsv;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import java.util.List;
import com.ims.core.matatype.Dto;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

/**
 * <p>
 * 菜单的csv文件 Mapper 接口
 * </p>
 *
 * @author 陈骑元
 * @since 2019-08-06
 */
public interface MenuCsvMapper extends BaseMapper<MenuCsv> {

    /**
	 * 根据Dto查询并返回数据持久化对象集合
	 * 
	 * @return List<MenuCsv>
	 */
	List<MenuCsv> list(Dto pDto);
    /**
	 * 根据Dto查询并返回分页数据持久化对象集合
	 * 
	 * @return List<MenuCsv>
	 */
	List<MenuCsv> listPage(Pagination page,Dto pDto);
		
	/**
	 * 根据Dto模糊查询并返回数据持久化对象集合(字符型字段模糊匹配，其余字段精确匹配)
	 * 
	 * @return List<MenuCsv>
	 */
	List<MenuCsv> like(Dto pDto);

	/**
	 * 根据Dto模糊查询并返回分页数据持久化对象集合(字符型字段模糊匹配，其余字段精确匹配)
	 * 
	 * @return List<MenuCsv>
	 */
	List<MenuCsv> likePage(Pagination page,Dto pDto);
	
	/**
	 * 根据数学表达式进行数学运算
	 * 
	 * @param pDto
	 * @return String
	 */
	 String calc(Dto pDto);


}
