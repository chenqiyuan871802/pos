package com.ims.buss.service;

import com.ims.buss.model.ShopCoupon;
import com.ims.buss.mapper.ShopCouponMapper;
import com.ims.buss.service.ShopCouponService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import java.util.List;
import com.ims.core.matatype.Dto;
import com.baomidou.mybatisplus.plugins.Page;
import com.ims.core.vo.Query;

/**
 * <p>
 * 优惠券 服务实现类
 * </p>
 *
 * @author 陈骑元
 * @since 2020-05-01
 */
@Service
public class ShopCouponService extends ServiceImpl<ShopCouponMapper, ShopCoupon>  {
     
   
     /**
	 * 根据Dto查询并返回数据持久化对象集合
	 * 
	 * @return List<ShopCoupon>
	 */
	public List<ShopCoupon> list(Dto pDto){
	    
	    return baseMapper.list(pDto);
	};
    /**
	 * 根据Dto查询并返回分页数据持久化对象集合
	 * 
	 * @return Page<ShopCoupon>
	 */
	public Page<ShopCoupon> listPage(Dto pDto){
	    Query<ShopCoupon> query=new Query<ShopCoupon>(pDto);
	    query.setRecords(baseMapper.listPage(query,pDto));
	    return query;
	};
		
	/**
	 * 根据Dto模糊查询并返回数据持久化对象集合(字符型字段模糊匹配，其余字段精确匹配)
	 * 
	 * @return List<ShopCoupon>
	 */
	public List<ShopCoupon> like(Dto pDto){
	
	    return baseMapper.like(pDto);
	
	};

	/**
	 * 根据Dto模糊查询并返回分页数据持久化对象集合(字符型字段模糊匹配，其余字段精确匹配)
	 * 
	 * @return Page<ShopCoupon>
	 */
	public Page<ShopCoupon> likePage(Dto pDto){
	    Query<ShopCoupon> query=new Query<ShopCoupon>(pDto);
	    query.setRecords(baseMapper.likePage(query,pDto));
	    return query;
	};
	/**
	 * 根据数学表达式进行数学运算
	 * 
	 * @param pDto
	 * @return String
	 */
	 public String calc(Dto pDto){
	 
	     return baseMapper.calc(pDto);
	 }
	 
	 /**
	  * 
	  * 简要说明：查询店铺优惠券
	  * 编写者：陈骑元（chenqiyuan@toonan.com）
	  * 创建时间： 2020年5月2日 上午7:07:15 
	  * @param 说明
	  * @return 说明
	  */
	 public Page<ShopCoupon> listShopCouponPage(Dto pDto){
		 
		 Query<ShopCoupon> query=new Query<ShopCoupon>(pDto);
		 query.setRecords(baseMapper.listShopCouponPage(query,pDto));
		 return query;
	 }
	 
	 /**
	  * 
	  * 简要说明：查询会员优惠券信息
	  * 编写者：陈骑元（chenqiyuan@toonan.com）
	  * 创建时间： 2020年5月2日 上午7:40:57 
	  * @param 说明
	  * @return 说明
	  */
	 public Page<ShopCoupon> listMemberCouponPage(Dto pDto){
		 Query<ShopCoupon> query=new Query<ShopCoupon>(pDto);
		 query.setRecords(baseMapper.listMemberCouponPage(query,pDto));
		 return query;
	 }
     

	

	 
}
