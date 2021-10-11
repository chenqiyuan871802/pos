package com.ims.system.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ims.core.matatype.Dto;
import com.ims.core.vo.Query;
import com.ims.system.mapper.ParamMapper;
import com.ims.system.model.Param;

import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.plugins.Page;

/**
 * <p>
 * 键值参数 服务实现类
 * </p>
 *
 * @author 陈骑元
 * @since 2018-04-09
 */
@Service
public class ParamService extends ServiceImpl<ParamMapper, Param>  {
     
   
     /**
	 * 根据Dto查询并返回数据持久化对象集合
	 * 
	 * @return List<Param>
	 */
	public List<Param> list(Dto pDto){
	    
	    return baseMapper.list(pDto);
	};
    /**
	 * 根据Dto查询并返回分页数据持久化对象集合
	 * 
	 * @return Page<Param>
	 */
	public Page<Param> listPage(Dto pDto){
	    Query<Param> query=new Query<Param>(pDto);
	    query.setRecords(baseMapper.listPage(query,pDto));
	    return query;
	};
		
	/**
	 * 根据Dto模糊查询并返回数据持久化对象集合(字符型字段模糊匹配，其余字段精确匹配)
	 * 
	 * @return List<Param>
	 */
	public List<Param> like(Dto pDto){
	
	    return baseMapper.like(pDto);
	
	};

	/**
	 * 根据Dto模糊查询并返回分页数据持久化对象集合(字符型字段模糊匹配，其余字段精确匹配)
	 * 
	 * @return Page<Param>
	 */
	public Page<Param> likePage(Dto pDto){
	    Query<Param> query=new Query<Param>(pDto);
	    query.setRecords(baseMapper.likePage(query,pDto));
	    return query;
	};
}
