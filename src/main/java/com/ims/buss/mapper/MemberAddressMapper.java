package com.ims.buss.mapper;

import com.ims.buss.model.MemberAddress;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import java.util.List;
import com.ims.core.matatype.Dto;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

/**
 * <p>
 * 会员地址 Mapper 接口
 * </p>
 *
 * @author 陈骑元
 * @since 2020-04-23
 */
public interface MemberAddressMapper extends BaseMapper<MemberAddress> {

    /**
	 * 根据Dto查询并返回数据持久化对象集合
	 * 
	 * @return List<MemberAddress>
	 */
	List<MemberAddress> list(Dto pDto);
    /**
	 * 根据Dto查询并返回分页数据持久化对象集合
	 * 
	 * @return List<MemberAddress>
	 */
	List<MemberAddress> listPage(Pagination page,Dto pDto);
		
	/**
	 * 根据Dto模糊查询并返回数据持久化对象集合(字符型字段模糊匹配，其余字段精确匹配)
	 * 
	 * @return List<MemberAddress>
	 */
	List<MemberAddress> like(Dto pDto);

	/**
	 * 根据Dto模糊查询并返回分页数据持久化对象集合(字符型字段模糊匹配，其余字段精确匹配)
	 * 
	 * @return List<MemberAddress>
	 */
	List<MemberAddress> likePage(Pagination page,Dto pDto);
	
	/**
	 * 根据数学表达式进行数学运算
	 * 
	 * @param pDto
	 * @return String
	 */
	 String calc(Dto pDto);
	 /**
	  * 
	  * 简要说明：获取会员地址
	  * 编写者：陈骑元（chenqiyuan@toonan.com）
	  * 创建时间： 2020年4月25日 上午10:26:34 
	  * @param 说明
	  * @return 说明
	  */
	 List<MemberAddress> listMemberAddress(Dto pDto);
	 
	 /**
	  * 
	  * 简要说明：查询单个会员编号
	  * 编写者：陈骑元（chenqiyuan@toonan.com）
	  * 创建时间： 2020年5月4日 上午7:54:41 
	  * @param 说明
	  * @return 说明
	  */
	 MemberAddress selectMemberAddress(String addressId);


}
