package com.ims.system.mapper;

import java.util.List;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.ims.core.matatype.Dto;
import com.ims.system.model.Dict;

/**
 * <p>
 * 数据字典 Mapper 接口
 * </p>
 *
 * @author 陈骑元
 * @since 2018-05-01
 */
public interface DictMapper extends BaseMapper<Dict> {

    /**
	 * 根据Dto查询并返回数据持久化对象集合
	 * 
	 * @return List<Dict>
	 */
	List<Dict> list(Dto pDto);
    /**
	 * 根据Dto查询并返回分页数据持久化对象集合
	 * 
	 * @return List<Dict>
	 */
	List<Dict> listPage(Pagination page,Dto pDto);
		
	/**
	 * 根据Dto模糊查询并返回数据持久化对象集合(字符型字段模糊匹配，其余字段精确匹配)
	 * 
	 * @return List<Dict>
	 */
	List<Dict> like(Dto pDto);

	/**
	 * 根据Dto模糊查询并返回分页数据持久化对象集合(字符型字段模糊匹配，其余字段精确匹配)
	 * 
	 * @return List<Dict>
	 */
	List<Dict> likePage(Pagination page,Dto pDto);


}
