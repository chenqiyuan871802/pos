package com.ims.system.service;


import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ims.core.matatype.Dto;
import com.ims.core.vo.Query;
import com.ims.system.mapper.DictMapper;
import com.ims.system.model.Dict;

import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.plugins.Page;

/**
 * <p>
 * 数据字典 服务实现类
 * </p>
 *
 * @author 陈骑元
 * @since 2018-05-01
 */
@Service
public class DictService extends ServiceImpl<DictMapper, Dict> {
     
   
     /**
	 * 根据Dto查询并返回数据持久化对象集合
	 * 
	 * @return List<Dict>
	 */
	public List<Dict> list(Dto pDto){
	    
	    return baseMapper.list(pDto);
	};
    /**
	 * 根据Dto查询并返回分页数据持久化对象集合
	 * 
	 * @return Page<Dict>
	 */
	public Page<Dict> listPage(Dto pDto){
	    Query<Dict> query=new Query<Dict>(pDto);
	    query.setRecords(baseMapper.listPage(query,pDto));
	    return query;
	};
		
	/**
	 * 根据Dto模糊查询并返回数据持久化对象集合(字符型字段模糊匹配，其余字段精确匹配)
	 * 
	 * @return List<Dict>
	 */
	public List<Dict> like(Dto pDto){
	
	    return baseMapper.like(pDto);
	
	};

	/**
	 * 根据Dto模糊查询并返回分页数据持久化对象集合(字符型字段模糊匹配，其余字段精确匹配)
	 * 
	 * @return Page<Dict>
	 */
	public Page<Dict> likePage(Dto pDto){
	    Query<Dict> query=new Query<Dict>(pDto);
	    query.setRecords(baseMapper.likePage(query,pDto));
	    return query;
	};
}
