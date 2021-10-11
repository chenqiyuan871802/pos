package com.ims.buss.mapper;

import com.ims.buss.model.Order;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import java.util.List;
import com.ims.core.matatype.Dto;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

/**
 * <p>
 * 订单信息 Mapper 接口
 * </p>
 *
 * @author 陈骑元
 * @since 2019-07-20
 */
public interface OrderMapper extends BaseMapper<Order> {

    /**
	 * 根据Dto查询并返回数据持久化对象集合
	 * 
	 * @return List<Order>
	 */
	List<Order> list(Dto pDto);
    /**
	 * 根据Dto查询并返回分页数据持久化对象集合
	 * 
	 * @return List<Order>
	 */
	List<Order> listPage(Pagination page,Dto pDto);
		
	/**
	 * 根据Dto模糊查询并返回数据持久化对象集合(字符型字段模糊匹配，其余字段精确匹配)
	 * 
	 * @return List<Order>
	 */
	List<Order> like(Dto pDto);

	/**
	 * 根据Dto模糊查询并返回分页数据持久化对象集合(字符型字段模糊匹配，其余字段精确匹配)
	 * 
	 * @return List<Order>
	 */
	List<Order> likePage(Pagination page,Dto pDto);
	
	/**
	 * 根据数学表达式进行数学运算
	 * 
	 * @param pDto
	 * @return String
	 */
	 String calc(Dto pDto);
	 /**
	  * 
	  * 简要说明：分页查询会员订单
	  * 编写者：陈骑元（chenqiyuan@toonan.com）
	  * 创建时间： 2020年5月5日 上午7:13:58 
	  * @param 说明
	  * @return 说明
	  */
	 List<Order> listMemberOrderPage(Pagination page,Dto pDto);


}
