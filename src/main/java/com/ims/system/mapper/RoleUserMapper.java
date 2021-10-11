package com.ims.system.mapper;

import java.util.List;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.ims.core.matatype.Dto;
import com.ims.system.model.RoleUser;

/**
 * <p>
 * 角色与用户关联 Mapper 接口
 * </p>
 *
 * @author 陈骑元
 * @since 2018-10-02
 */
public interface RoleUserMapper extends BaseMapper<RoleUser> {

    /**
	 * 根据Dto查询并返回数据持久化对象集合
	 * 
	 * @return List<RoleUser>
	 */
	List<RoleUser> list(Dto pDto);
    /**
	 * 根据Dto查询并返回分页数据持久化对象集合
	 * 
	 * @return List<RoleUser>
	 */
	List<RoleUser> listPage(Pagination page,Dto pDto);
		
	/**
	 * 根据Dto模糊查询并返回数据持久化对象集合(字符型字段模糊匹配，其余字段精确匹配)
	 * 
	 * @return List<RoleUser>
	 */
	List<RoleUser> like(Dto pDto);

	/**
	 * 根据Dto模糊查询并返回分页数据持久化对象集合(字符型字段模糊匹配，其余字段精确匹配)
	 * 
	 * @return List<RoleUser>
	 */
	List<RoleUser> likePage(Pagination page,Dto pDto);
	
	/**
	 * 根据数学表达式进行数学运算
	 * 
	 * @param pDto
	 * @return String
	 */
	 String calc(Dto pDto);


}
