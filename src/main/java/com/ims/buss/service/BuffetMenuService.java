package com.ims.buss.service;

import com.ims.buss.model.BuffetMenu;
import com.ims.buss.mapper.BuffetMenuMapper;
import com.ims.buss.service.BuffetMenuService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import java.util.List;
import com.ims.core.matatype.Dto;
import com.baomidou.mybatisplus.plugins.Page;
import com.ims.core.vo.Query;

/**
 * <p>
 * 自助餐菜单关联表 服务实现类
 * </p>
 *
 * @author 陈骑元
 * @since 2019-08-28
 */
@Service
public class BuffetMenuService extends ServiceImpl<BuffetMenuMapper, BuffetMenu>  {
     
   
     /**
	 * 根据Dto查询并返回数据持久化对象集合
	 * 
	 * @return List<BuffetMenu>
	 */
	public List<BuffetMenu> list(Dto pDto){
	    
	    return baseMapper.list(pDto);
	};
    /**
	 * 根据Dto查询并返回分页数据持久化对象集合
	 * 
	 * @return Page<BuffetMenu>
	 */
	public Page<BuffetMenu> listPage(Dto pDto){
	    Query<BuffetMenu> query=new Query<BuffetMenu>(pDto);
	    query.setRecords(baseMapper.listPage(query,pDto));
	    return query;
	};
		
	/**
	 * 根据Dto模糊查询并返回数据持久化对象集合(字符型字段模糊匹配，其余字段精确匹配)
	 * 
	 * @return List<BuffetMenu>
	 */
	public List<BuffetMenu> like(Dto pDto){
	
	    return baseMapper.like(pDto);
	
	};

	/**
	 * 根据Dto模糊查询并返回分页数据持久化对象集合(字符型字段模糊匹配，其余字段精确匹配)
	 * 
	 * @return Page<BuffetMenu>
	 */
	public Page<BuffetMenu> likePage(Dto pDto){
	    Query<BuffetMenu> query=new Query<BuffetMenu>(pDto);
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
