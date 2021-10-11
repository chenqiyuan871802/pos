package com.ims.buss.service;

import com.ims.buss.model.StepSpec;
import com.ims.buss.mapper.StepSpecMapper;
import com.ims.buss.service.StepSpecService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import java.util.List;
import com.ims.core.matatype.Dto;
import com.baomidou.mybatisplus.plugins.Page;
import com.ims.core.vo.Query;

/**
 * <p>
 * 步骤规格 服务实现类
 * </p>
 *
 * @author 陈骑元
 * @since 2019-08-28
 */
@Service
public class StepSpecService extends ServiceImpl<StepSpecMapper, StepSpec>  {
     
   
     /**
	 * 根据Dto查询并返回数据持久化对象集合
	 * 
	 * @return List<StepSpec>
	 */
	public List<StepSpec> list(Dto pDto){
	    
	    return baseMapper.list(pDto);
	};
    /**
	 * 根据Dto查询并返回分页数据持久化对象集合
	 * 
	 * @return Page<StepSpec>
	 */
	public Page<StepSpec> listPage(Dto pDto){
	    Query<StepSpec> query=new Query<StepSpec>(pDto);
	    query.setRecords(baseMapper.listPage(query,pDto));
	    return query;
	};
		
	/**
	 * 根据Dto模糊查询并返回数据持久化对象集合(字符型字段模糊匹配，其余字段精确匹配)
	 * 
	 * @return List<StepSpec>
	 */
	public List<StepSpec> like(Dto pDto){
	
	    return baseMapper.like(pDto);
	
	};

	/**
	 * 根据Dto模糊查询并返回分页数据持久化对象集合(字符型字段模糊匹配，其余字段精确匹配)
	 * 
	 * @return Page<StepSpec>
	 */
	public Page<StepSpec> likePage(Dto pDto){
	    Query<StepSpec> query=new Query<StepSpec>(pDto);
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
