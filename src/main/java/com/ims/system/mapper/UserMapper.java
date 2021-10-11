package com.ims.system.mapper;

import java.util.List;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.ims.core.matatype.Dto;
import com.ims.system.model.User;

/**
 * <p>
 * 用户基本信息表 Mapper 接口
 * </p>
 *
 * @author 陈骑元
 * @since 2018-09-28
 */
public interface UserMapper extends BaseMapper<User> {

    /**
	 * 根据Dto查询并返回数据持久化对象集合
	 * 
	 * @return List<User>
	 */
	List<User> list(Dto pDto);
    /**
	 * 根据Dto查询并返回分页数据持久化对象集合
	 * 
	 * @return List<User>
	 */
	List<User> listPage(Pagination page,Dto pDto);
		
	/**
	 * 根据Dto模糊查询并返回数据持久化对象集合(字符型字段模糊匹配，其余字段精确匹配)
	 * 
	 * @return List<User>
	 */
	List<User> like(Dto pDto);

	/**
	 * 根据Dto模糊查询并返回分页数据持久化对象集合(字符型字段模糊匹配，其余字段精确匹配)
	 * 
	 * @return List<User>
	 */
	List<User> likePage(Pagination page,Dto pDto);
	
	/**
	 * 根据数学表达式进行数学运算
	 * 
	 * @param pDto
	 * @return String
	 */
	 String calc(Dto pDto);
	 
	 /**
	  * 
	  * 简要说明：分页查询用户信息
	  * 编写者：陈骑元
	  * 创建时间：2018年9月28日 上午11:46:04
	  * @param 说明
	  * @return 说明
	  */
	 List<User> listUserPage(Pagination page,Dto pDto);


}
