package com.ims.buss.service;

import com.ims.buss.model.SmsRecord;
import com.ims.buss.mapper.SmsRecordMapper;
import com.ims.buss.service.SmsRecordService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import java.util.List;
import com.ims.core.matatype.Dto;
import com.baomidou.mybatisplus.plugins.Page;
import com.ims.core.vo.Query;

/**
 * <p>
 * 短信记录表 服务实现类
 * </p>
 *
 * @author 陈骑元
 * @since 2019-12-14
 */
@Service
public class SmsRecordService extends ServiceImpl<SmsRecordMapper, SmsRecord>  {
     
   
     /**
	 * 根据Dto查询并返回数据持久化对象集合
	 * 
	 * @return List<SmsRecord>
	 */
	public List<SmsRecord> list(Dto pDto){
	    
	    return baseMapper.list(pDto);
	};
    /**
	 * 根据Dto查询并返回分页数据持久化对象集合
	 * 
	 * @return Page<SmsRecord>
	 */
	public Page<SmsRecord> listPage(Dto pDto){
	    Query<SmsRecord> query=new Query<SmsRecord>(pDto);
	    query.setRecords(baseMapper.listPage(query,pDto));
	    return query;
	};
		
	/**
	 * 根据Dto模糊查询并返回数据持久化对象集合(字符型字段模糊匹配，其余字段精确匹配)
	 * 
	 * @return List<SmsRecord>
	 */
	public List<SmsRecord> like(Dto pDto){
	
	    return baseMapper.like(pDto);
	
	};

	/**
	 * 根据Dto模糊查询并返回分页数据持久化对象集合(字符型字段模糊匹配，其余字段精确匹配)
	 * 
	 * @return Page<SmsRecord>
	 */
	public Page<SmsRecord> likePage(Dto pDto){
	    Query<SmsRecord> query=new Query<SmsRecord>(pDto);
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
