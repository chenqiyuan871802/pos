package com.ims.buss.service;

import com.ims.buss.model.CashboxRecord;
import com.ims.buss.mapper.CashboxRecordMapper;
import com.ims.buss.service.CashboxRecordService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import java.util.List;
import com.ims.core.matatype.Dto;
import com.baomidou.mybatisplus.plugins.Page;
import com.ims.core.vo.Query;

/**
 * <p>
 * 钱箱记录表 服务实现类
 * </p>
 *
 * @author 陈骑元
 * @since 2020-07-15
 */
@Service
public class CashboxRecordService extends ServiceImpl<CashboxRecordMapper, CashboxRecord>  {
     
   
     /**
	 * 根据Dto查询并返回数据持久化对象集合
	 * 
	 * @return List<CashboxRecord>
	 */
	public List<CashboxRecord> list(Dto pDto){
	    
	    return baseMapper.list(pDto);
	};
    /**
	 * 根据Dto查询并返回分页数据持久化对象集合
	 * 
	 * @return Page<CashboxRecord>
	 */
	public Page<CashboxRecord> listPage(Dto pDto){
	    Query<CashboxRecord> query=new Query<CashboxRecord>(pDto);
	    query.setRecords(baseMapper.listPage(query,pDto));
	    return query;
	};
		
	/**
	 * 根据Dto模糊查询并返回数据持久化对象集合(字符型字段模糊匹配，其余字段精确匹配)
	 * 
	 * @return List<CashboxRecord>
	 */
	public List<CashboxRecord> like(Dto pDto){
	
	    return baseMapper.like(pDto);
	
	};

	/**
	 * 根据Dto模糊查询并返回分页数据持久化对象集合(字符型字段模糊匹配，其余字段精确匹配)
	 * 
	 * @return Page<CashboxRecord>
	 */
	public Page<CashboxRecord> likePage(Dto pDto){
	    Query<CashboxRecord> query=new Query<CashboxRecord>(pDto);
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
	 
}
