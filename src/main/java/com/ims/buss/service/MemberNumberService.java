package com.ims.buss.service;

import com.ims.buss.model.MemberNumber;
import com.ims.buss.mapper.MemberNumberMapper;
import com.ims.buss.service.MemberNumberService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import java.util.List;
import com.ims.core.matatype.Dto;
import com.baomidou.mybatisplus.plugins.Page;
import com.ims.core.vo.Query;

/**
 * <p>
 * 会员号码表 服务实现类
 * </p>
 *
 * @author 陈骑元
 * @since 2019-12-15
 */
@Service
public class MemberNumberService extends ServiceImpl<MemberNumberMapper, MemberNumber>  {
     
   
     /**
	 * 根据Dto查询并返回数据持久化对象集合
	 * 
	 * @return List<MemberNumber>
	 */
	public List<MemberNumber> list(Dto pDto){
	    
	    return baseMapper.list(pDto);
	};
    /**
	 * 根据Dto查询并返回分页数据持久化对象集合
	 * 
	 * @return Page<MemberNumber>
	 */
	public Page<MemberNumber> listPage(Dto pDto){
	    Query<MemberNumber> query=new Query<MemberNumber>(pDto);
	    query.setRecords(baseMapper.listPage(query,pDto));
	    return query;
	};
		
	/**
	 * 根据Dto模糊查询并返回数据持久化对象集合(字符型字段模糊匹配，其余字段精确匹配)
	 * 
	 * @return List<MemberNumber>
	 */
	public List<MemberNumber> like(Dto pDto){
	
	    return baseMapper.like(pDto);
	
	};

	/**
	 * 根据Dto模糊查询并返回分页数据持久化对象集合(字符型字段模糊匹配，其余字段精确匹配)
	 * 
	 * @return Page<MemberNumber>
	 */
	public Page<MemberNumber> likePage(Dto pDto){
	    Query<MemberNumber> query=new Query<MemberNumber>(pDto);
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
