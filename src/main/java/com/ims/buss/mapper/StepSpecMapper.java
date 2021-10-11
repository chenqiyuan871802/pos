package com.ims.buss.mapper;

import com.ims.buss.model.StepSpec;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import java.util.List;
import com.ims.core.matatype.Dto;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

/**
 * <p>
 * 步骤规格 Mapper 接口
 * </p>
 *
 * @author 陈骑元
 * @since 2019-08-28
 */
public interface StepSpecMapper extends BaseMapper<StepSpec> {

    /**
	 * 根据Dto查询并返回数据持久化对象集合
	 * 
	 * @return List<StepSpec>
	 */
	List<StepSpec> list(Dto pDto);
    /**
	 * 根据Dto查询并返回分页数据持久化对象集合
	 * 
	 * @return List<StepSpec>
	 */
	List<StepSpec> listPage(Pagination page,Dto pDto);
		
	/**
	 * 根据Dto模糊查询并返回数据持久化对象集合(字符型字段模糊匹配，其余字段精确匹配)
	 * 
	 * @return List<StepSpec>
	 */
	List<StepSpec> like(Dto pDto);

	/**
	 * 根据Dto模糊查询并返回分页数据持久化对象集合(字符型字段模糊匹配，其余字段精确匹配)
	 * 
	 * @return List<StepSpec>
	 */
	List<StepSpec> likePage(Pagination page,Dto pDto);
	
	/**
	 * 根据数学表达式进行数学运算
	 * 
	 * @param pDto
	 * @return String
	 */
	 String calc(Dto pDto);


}
