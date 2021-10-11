package com.ims.buss.service;

import com.ims.buss.model.OrderLine;
import com.ims.buss.mapper.OrderLineMapper;
import com.ims.buss.service.OrderLineService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import java.util.List;
import com.ims.core.matatype.Dto;
import com.baomidou.mybatisplus.plugins.Page;
import com.ims.core.vo.Query;

/**
 * <p>
 * 订单明细 服务实现类
 * </p>
 *
 * @author 陈骑元
 * @since 2019-07-20
 */
@Service
public class OrderLineService extends ServiceImpl<OrderLineMapper, OrderLine>  {
     
   
     /**
	 * 根据Dto查询并返回数据持久化对象集合
	 * 
	 * @return List<OrderLine>
	 */
	public List<OrderLine> list(Dto pDto){
	    
	    return baseMapper.list(pDto);
	};
    /**
	 * 根据Dto查询并返回分页数据持久化对象集合
	 * 
	 * @return Page<OrderLine>
	 */
	public Page<OrderLine> listPage(Dto pDto){
	    Query<OrderLine> query=new Query<OrderLine>(pDto);
	    query.setRecords(baseMapper.listPage(query,pDto));
	    return query;
	};
		
	/**
	 * 根据Dto模糊查询并返回数据持久化对象集合(字符型字段模糊匹配，其余字段精确匹配)
	 * 
	 * @return List<OrderLine>
	 */
	public List<OrderLine> like(Dto pDto){
	
	    return baseMapper.like(pDto);
	
	};

	/**
	 * 根据Dto模糊查询并返回分页数据持久化对象集合(字符型字段模糊匹配，其余字段精确匹配)
	 * 
	 * @return Page<OrderLine>
	 */
	public Page<OrderLine> likePage(Dto pDto){
	    Query<OrderLine> query=new Query<OrderLine>(pDto);
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
