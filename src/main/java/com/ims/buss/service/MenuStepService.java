package com.ims.buss.service;

import com.ims.buss.model.MenuStep;
import com.ims.buss.mapper.MenuStepMapper;
import com.ims.buss.service.MenuStepService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import java.util.List;
import com.ims.core.matatype.Dto;
import com.baomidou.mybatisplus.plugins.Page;
import com.ims.core.vo.Query;

/**
 * <p>
 * 菜品规格步骤 服务实现类
 * </p>
 *
 * @author 陈骑元
 * @since 2019-08-28
 */
@Service
public class MenuStepService extends ServiceImpl<MenuStepMapper, MenuStep>  {
     
   
     /**
	 * 根据Dto查询并返回数据持久化对象集合
	 * 
	 * @return List<MenuStep>
	 */
	public List<MenuStep> list(Dto pDto){
	    
	    return baseMapper.list(pDto);
	};
    /**
	 * 根据Dto查询并返回分页数据持久化对象集合
	 * 
	 * @return Page<MenuStep>
	 */
	public Page<MenuStep> listPage(Dto pDto){
	    Query<MenuStep> query=new Query<MenuStep>(pDto);
	    query.setRecords(baseMapper.listPage(query,pDto));
	    return query;
	};
		
	/**
	 * 根据Dto模糊查询并返回数据持久化对象集合(字符型字段模糊匹配，其余字段精确匹配)
	 * 
	 * @return List<MenuStep>
	 */
	public List<MenuStep> like(Dto pDto){
	
	    return baseMapper.like(pDto);
	
	};

	/**
	 * 根据Dto模糊查询并返回分页数据持久化对象集合(字符型字段模糊匹配，其余字段精确匹配)
	 * 
	 * @return Page<MenuStep>
	 */
	public Page<MenuStep> likePage(Dto pDto){
	    Query<MenuStep> query=new Query<MenuStep>(pDto);
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
