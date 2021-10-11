package com.ims.system.service;


import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ims.core.matatype.Dto;
import com.ims.core.vo.Query;
import com.ims.system.mapper.DictIndexMapper;
import com.ims.system.model.DictIndex;

/**
 * <p>
 * 数据字典索引表 服务实现类
 * </p>
 *
 * @author 陈骑元
 * @since 2018-05-01
 */
@Service
public class DictIndexService extends ServiceImpl<DictIndexMapper, DictIndex> {
     
   
     /**
	 * 根据Dto查询并返回数据持久化对象集合
	 * 
	 * @return List<DictIndex>
	 */
	public List<DictIndex> list(Dto pDto){
	    
	    return baseMapper.list(pDto);
	};
    /**
	 * 根据Dto查询并返回分页数据持久化对象集合
	 * 
	 * @return Page<DictIndex>
	 */
	public Page<DictIndex> listPage(Dto pDto){
	    Query<DictIndex> query=new Query<DictIndex>(pDto);
	    query.setRecords(baseMapper.listPage(query,pDto));
	    return query;
	};
		
	/**
	 * 根据Dto模糊查询并返回数据持久化对象集合(字符型字段模糊匹配，其余字段精确匹配)
	 * 
	 * @return List<DictIndex>
	 */
	public List<DictIndex> like(Dto pDto){
	
	    return baseMapper.like(pDto);
	
	};

	/**
	 * 根据Dto模糊查询并返回分页数据持久化对象集合(字符型字段模糊匹配，其余字段精确匹配)
	 * 
	 * @return Page<DictIndex>
	 */
	public Page<DictIndex> likePage(Dto pDto){
	    Query<DictIndex> query=new Query<DictIndex>(pDto);
	    query.setRecords(baseMapper.likePage(query,pDto));
	    return query;
	};
}
