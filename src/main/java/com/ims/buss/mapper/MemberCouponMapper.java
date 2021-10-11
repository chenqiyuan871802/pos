package com.ims.buss.mapper;

import com.ims.buss.model.MemberCoupon;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import java.util.List;
import com.ims.core.matatype.Dto;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

/**
 * <p>
 * 会员优惠券 Mapper 接口
 * </p>
 *
 * @author 陈骑元
 * @since 2020-05-01
 */
public interface MemberCouponMapper extends BaseMapper<MemberCoupon> {

    /**
	 * 根据Dto查询并返回数据持久化对象集合
	 * 
	 * @return List<MemberCoupon>
	 */
	List<MemberCoupon> list(Dto pDto);
    /**
	 * 根据Dto查询并返回分页数据持久化对象集合
	 * 
	 * @return List<MemberCoupon>
	 */
	List<MemberCoupon> listPage(Pagination page,Dto pDto);
		
	/**
	 * 根据Dto模糊查询并返回数据持久化对象集合(字符型字段模糊匹配，其余字段精确匹配)
	 * 
	 * @return List<MemberCoupon>
	 */
	List<MemberCoupon> like(Dto pDto);

	/**
	 * 根据Dto模糊查询并返回分页数据持久化对象集合(字符型字段模糊匹配，其余字段精确匹配)
	 * 
	 * @return List<MemberCoupon>
	 */
	List<MemberCoupon> likePage(Pagination page,Dto pDto);
	
	/**
	 * 根据数学表达式进行数学运算
	 * 
	 * @param pDto
	 * @return String
	 */
	 String calc(Dto pDto);


}
