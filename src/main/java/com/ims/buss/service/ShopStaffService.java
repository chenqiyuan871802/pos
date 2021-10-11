package com.ims.buss.service;

import com.ims.buss.model.ShopStaff;
import com.ims.buss.mapper.ShopStaffMapper;
import com.ims.buss.service.ShopStaffService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import java.util.List;
import com.ims.core.matatype.Dto;
import com.baomidou.mybatisplus.plugins.Page;
import com.ims.core.vo.Query;

/**
 * <p>
 * 店铺员工 服务实现类
 * </p>
 *
 * @author 陈骑元
 * @since 2020-04-23
 */
@Service
public class ShopStaffService extends ServiceImpl<ShopStaffMapper, ShopStaff>  {
     
   
     /**
	 * 根据Dto查询并返回数据持久化对象集合
	 * 
	 * @return List<ShopStaff>
	 */
	public List<ShopStaff> list(Dto pDto){
	    
	    return baseMapper.list(pDto);
	};
    /**
	 * 根据Dto查询并返回分页数据持久化对象集合
	 * 
	 * @return Page<ShopStaff>
	 */
	public Page<ShopStaff> listPage(Dto pDto){
	    Query<ShopStaff> query=new Query<ShopStaff>(pDto);
	    query.setRecords(baseMapper.listPage(query,pDto));
	    return query;
	};
		
	/**
	 * 根据Dto模糊查询并返回数据持久化对象集合(字符型字段模糊匹配，其余字段精确匹配)
	 * 
	 * @return List<ShopStaff>
	 */
	public List<ShopStaff> like(Dto pDto){
	
	    return baseMapper.like(pDto);
	
	};

	/**
	 * 根据Dto模糊查询并返回分页数据持久化对象集合(字符型字段模糊匹配，其余字段精确匹配)
	 * 
	 * @return Page<ShopStaff>
	 */
	public Page<ShopStaff> likePage(Dto pDto){
	    Query<ShopStaff> query=new Query<ShopStaff>(pDto);
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
