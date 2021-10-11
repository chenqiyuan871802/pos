package com.ims.buss.mapper;

import com.ims.buss.model.ShopCoupon;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import java.util.List;
import com.ims.core.matatype.Dto;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

/**
 * <p>
 * 优惠券 Mapper 接口
 * </p>
 *
 * @author 陈骑元
 * @since 2020-05-01
 */
public interface ShopCouponMapper extends BaseMapper<ShopCoupon> {

    /**
	 * 根据Dto查询并返回数据持久化对象集合
	 * 
	 * @return List<ShopCoupon>
	 */
	List<ShopCoupon> list(Dto pDto);
    /**
	 * 根据Dto查询并返回分页数据持久化对象集合
	 * 
	 * @return List<ShopCoupon>
	 */
	List<ShopCoupon> listPage(Pagination page,Dto pDto);
		
	/**
	 * 根据Dto模糊查询并返回数据持久化对象集合(字符型字段模糊匹配，其余字段精确匹配)
	 * 
	 * @return List<ShopCoupon>
	 */
	List<ShopCoupon> like(Dto pDto);

	/**
	 * 根据Dto模糊查询并返回分页数据持久化对象集合(字符型字段模糊匹配，其余字段精确匹配)
	 * 
	 * @return List<ShopCoupon>
	 */
	List<ShopCoupon> likePage(Pagination page,Dto pDto);
	
	/**
	 * 根据数学表达式进行数学运算
	 * 
	 * @param pDto
	 * @return String
	 */
	 String calc(Dto pDto);
	 /**
	  * 
	  * 简要说明：查询店铺优惠券
	  * 编写者：陈骑元（chenqiyuan@toonan.com）
	  * 创建时间： 2020年5月2日 上午7:07:15 
	  * @param 说明
	  * @return 说明
	  */
	 List<ShopCoupon> listShopCouponPage(Pagination page,Dto pDto);
	 /**
	  * 
	  * 简要说明：查询会员优惠券信息
	  * 编写者：陈骑元（chenqiyuan@toonan.com）
	  * 创建时间： 2020年5月2日 上午7:40:57 
	  * @param 说明
	  * @return 说明
	  */
	 List<ShopCoupon> listMemberCouponPage(Pagination page,Dto pDto);

}
