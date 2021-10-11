package com.ims.buss.mapper;

import com.ims.buss.model.Position;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import java.util.List;
import com.ims.core.matatype.Dto;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 陈骑元
 * @since 2020-04-23
 */
public interface PositionMapper extends BaseMapper<Position> {

    /**
	 * 根据Dto查询并返回数据持久化对象集合
	 * 
	 * @return List<Position>
	 */
	List<Position> list(Dto pDto);
    /**
	 * 根据Dto查询并返回分页数据持久化对象集合
	 * 
	 * @return List<Position>
	 */
	List<Position> listPage(Pagination page,Dto pDto);
		
	/**
	 * 根据Dto模糊查询并返回数据持久化对象集合(字符型字段模糊匹配，其余字段精确匹配)
	 * 
	 * @return List<Position>
	 */
	List<Position> like(Dto pDto);

	/**
	 * 根据Dto模糊查询并返回分页数据持久化对象集合(字符型字段模糊匹配，其余字段精确匹配)
	 * 
	 * @return List<Position>
	 */
	List<Position> likePage(Pagination page,Dto pDto);
	
	/**
	 * 根据数学表达式进行数学运算
	 * 
	 * @param pDto
	 * @return String
	 */
	 String calc(Dto pDto);


}
