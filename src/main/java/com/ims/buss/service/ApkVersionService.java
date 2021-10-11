package com.ims.buss.service;

import com.ims.buss.model.ApkVersion;
import com.ims.buss.mapper.ApkVersionMapper;
import com.ims.buss.service.ApkVersionService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import java.util.List;
import com.ims.core.matatype.Dto;
import com.baomidou.mybatisplus.plugins.Page;
import com.ims.core.vo.Query;

/**
 * <p>
 * APK版本管理 服务实现类
 * </p>
 *
 * @author 陈骑元
 * @since 2019-10-14
 */
@Service
public class ApkVersionService extends ServiceImpl<ApkVersionMapper, ApkVersion>  {
     
   
     /**
	 * 根据Dto查询并返回数据持久化对象集合
	 * 
	 * @return List<ApkVersion>
	 */
	public List<ApkVersion> list(Dto pDto){
	    
	    return baseMapper.list(pDto);
	};
    /**
	 * 根据Dto查询并返回分页数据持久化对象集合
	 * 
	 * @return Page<ApkVersion>
	 */
	public Page<ApkVersion> listPage(Dto pDto){
	    Query<ApkVersion> query=new Query<ApkVersion>(pDto);
	    query.setRecords(baseMapper.listPage(query,pDto));
	    return query;
	};
		
	/**
	 * 根据Dto模糊查询并返回数据持久化对象集合(字符型字段模糊匹配，其余字段精确匹配)
	 * 
	 * @return List<ApkVersion>
	 */
	public List<ApkVersion> like(Dto pDto){
	
	    return baseMapper.like(pDto);
	
	};

	/**
	 * 根据Dto模糊查询并返回分页数据持久化对象集合(字符型字段模糊匹配，其余字段精确匹配)
	 * 
	 * @return Page<ApkVersion>
	 */
	public Page<ApkVersion> likePage(Dto pDto){
	    Query<ApkVersion> query=new Query<ApkVersion>(pDto);
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
