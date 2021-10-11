package com.ims.buss.mapper;

import com.ims.buss.model.ApkVersion;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import java.util.List;
import com.ims.core.matatype.Dto;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

/**
 * <p>
 * APK版本管理 Mapper 接口
 * </p>
 *
 * @author 陈骑元
 * @since 2019-10-14
 */
public interface ApkVersionMapper extends BaseMapper<ApkVersion> {

    /**
	 * 根据Dto查询并返回数据持久化对象集合
	 * 
	 * @return List<ApkVersion>
	 */
	List<ApkVersion> list(Dto pDto);
    /**
	 * 根据Dto查询并返回分页数据持久化对象集合
	 * 
	 * @return List<ApkVersion>
	 */
	List<ApkVersion> listPage(Pagination page,Dto pDto);
		
	/**
	 * 根据Dto模糊查询并返回数据持久化对象集合(字符型字段模糊匹配，其余字段精确匹配)
	 * 
	 * @return List<ApkVersion>
	 */
	List<ApkVersion> like(Dto pDto);

	/**
	 * 根据Dto模糊查询并返回分页数据持久化对象集合(字符型字段模糊匹配，其余字段精确匹配)
	 * 
	 * @return List<ApkVersion>
	 */
	List<ApkVersion> likePage(Pagination page,Dto pDto);
	
	/**
	 * 根据数学表达式进行数学运算
	 * 
	 * @param pDto
	 * @return String
	 */
	 String calc(Dto pDto);


}
