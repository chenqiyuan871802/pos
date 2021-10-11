package com.ims.system.mapper;

import java.util.List;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.ims.core.matatype.Dto;
import com.ims.system.model.Param;

/**
 * <p>
 * 键值参数 Mapper 接口
 * </p>
 *
 * @author 陈骑元
 * @since 2018-04-09
 */
public interface ParamMapper extends BaseMapper<Param> {

    /**
	 * 根据Dto查询并返回数据持久化对象集合
	 * 
	 * @return List<Param>
	 */
	List<Param> list(Dto pDto);
    /**
	 * 根据Dto查询并返回分页数据持久化对象集合
	 * 
	 * @return List<Param>
	 */
	List<Param> listPage(Pagination page,Dto pDto);
		
	/**
	 * 根据Dto模糊查询并返回数据持久化对象集合(字符型字段模糊匹配，其余字段精确匹配)
	 * 
	 * @return List<Param>
	 */
	List<Param> like(Dto pDto);

	/**
	 * 根据Dto模糊查询并返回分页数据持久化对象集合(字符型字段模糊匹配，其余字段精确匹配)
	 * 
	 * @return List<Param>
	 */
	List<Param> likePage(Pagination page,Dto pDto);


}
