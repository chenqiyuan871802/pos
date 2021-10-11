package com.ims.buss.mapper;

import com.ims.buss.model.LanguagePack;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import java.util.List;
import com.ims.core.matatype.Dto;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

/**
 * <p>
 * 语言包 Mapper 接口
 * </p>
 *
 * @author 陈骑元
 * @since 2019-08-03
 */
public interface LanguagePackMapper extends BaseMapper<LanguagePack> {

    /**
	 * 根据Dto查询并返回数据持久化对象集合
	 * 
	 * @return List<LanguagePack>
	 */
	List<LanguagePack> list(Dto pDto);
    /**
	 * 根据Dto查询并返回分页数据持久化对象集合
	 * 
	 * @return List<LanguagePack>
	 */
	List<LanguagePack> listPage(Pagination page,Dto pDto);
		
	/**
	 * 根据Dto模糊查询并返回数据持久化对象集合(字符型字段模糊匹配，其余字段精确匹配)
	 * 
	 * @return List<LanguagePack>
	 */
	List<LanguagePack> like(Dto pDto);

	/**
	 * 根据Dto模糊查询并返回分页数据持久化对象集合(字符型字段模糊匹配，其余字段精确匹配)
	 * 
	 * @return List<LanguagePack>
	 */
	List<LanguagePack> likePage(Pagination page,Dto pDto);
	
	/**
	 * 根据数学表达式进行数学运算
	 * 
	 * @param pDto
	 * @return String
	 */
	 String calc(Dto pDto);


}
