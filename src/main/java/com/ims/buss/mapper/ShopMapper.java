package com.ims.buss.mapper;

import com.ims.buss.model.Shop;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import java.util.List;
import com.ims.core.matatype.Dto;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

/**
 * <p>
 * 店铺表 Mapper 接口
 * </p>
 *
 * @author 陈骑元
 * @since 2019-07-20
 */
public interface ShopMapper extends BaseMapper<Shop> {

    /**
	 * 根据Dto查询并返回数据持久化对象集合
	 * 
	 * @return List<Shop>
	 */
	List<Shop> list(Dto pDto);
    /**
	 * 根据Dto查询并返回分页数据持久化对象集合
	 * 
	 * @return List<Shop>
	 */
	List<Shop> listPage(Pagination page,Dto pDto);
		
	/**
	 * 根据Dto模糊查询并返回数据持久化对象集合(字符型字段模糊匹配，其余字段精确匹配)
	 * 
	 * @return List<Shop>
	 */
	List<Shop> like(Dto pDto);

	/**
	 * 根据Dto模糊查询并返回分页数据持久化对象集合(字符型字段模糊匹配，其余字段精确匹配)
	 * 
	 * @return List<Shop>
	 */
	List<Shop> likePage(Pagination page,Dto pDto);
	
	/**
	 * 根据数学表达式进行数学运算
	 * 
	 * @param pDto
	 * @return String
	 */
	 String calc(Dto pDto);
	 /**
	  * 
	  */
	 List<Shop> listShopPage(Pagination page,Dto pDto);


}
