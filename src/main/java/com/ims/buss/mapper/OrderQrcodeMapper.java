package com.ims.buss.mapper;

import com.ims.buss.model.OrderQrcode;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import java.util.List;
import com.ims.core.matatype.Dto;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

/**
 * <p>
 * 点餐生成二维码 Mapper 接口
 * </p>
 *
 * @author 陈骑元
 * @since 2019-07-26
 */
public interface OrderQrcodeMapper extends BaseMapper<OrderQrcode> {

    /**
	 * 根据Dto查询并返回数据持久化对象集合
	 * 
	 * @return List<OrderQrcode>
	 */
	List<OrderQrcode> list(Dto pDto);
    /**
	 * 根据Dto查询并返回分页数据持久化对象集合
	 * 
	 * @return List<OrderQrcode>
	 */
	List<OrderQrcode> listPage(Pagination page,Dto pDto);
		
	/**
	 * 根据Dto模糊查询并返回数据持久化对象集合(字符型字段模糊匹配，其余字段精确匹配)
	 * 
	 * @return List<OrderQrcode>
	 */
	List<OrderQrcode> like(Dto pDto);

	/**
	 * 根据Dto模糊查询并返回分页数据持久化对象集合(字符型字段模糊匹配，其余字段精确匹配)
	 * 
	 * @return List<OrderQrcode>
	 */
	List<OrderQrcode> likePage(Pagination page,Dto pDto);
	
	/**
	 * 根据数学表达式进行数学运算
	 * 
	 * @param pDto
	 * @return String
	 */
	 String calc(Dto pDto);


}
