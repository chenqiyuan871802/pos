package com.ims.buss.mapper;

import com.ims.buss.model.CashboxRecord;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import java.util.List;
import com.ims.core.matatype.Dto;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

/**
 * <p>
 * 钱箱记录表 Mapper 接口
 * </p>
 *
 * @author 陈骑元
 * @since 2020-07-15
 */
public interface CashboxRecordMapper extends BaseMapper<CashboxRecord> {

    /**
	 * 根据Dto查询并返回数据持久化对象集合
	 * 
	 * @return List<CashboxRecord>
	 */
	List<CashboxRecord> list(Dto pDto);
    /**
	 * 根据Dto查询并返回分页数据持久化对象集合
	 * 
	 * @return List<CashboxRecord>
	 */
	List<CashboxRecord> listPage(Pagination page,Dto pDto);
		
	/**
	 * 根据Dto模糊查询并返回数据持久化对象集合(字符型字段模糊匹配，其余字段精确匹配)
	 * 
	 * @return List<CashboxRecord>
	 */
	List<CashboxRecord> like(Dto pDto);

	/**
	 * 根据Dto模糊查询并返回分页数据持久化对象集合(字符型字段模糊匹配，其余字段精确匹配)
	 * 
	 * @return List<CashboxRecord>
	 */
	List<CashboxRecord> likePage(Pagination page,Dto pDto);
	
	/**
	 * 根据数学表达式进行数学运算
	 * 
	 * @param pDto
	 * @return String
	 */
	 String calc(Dto pDto);


}
