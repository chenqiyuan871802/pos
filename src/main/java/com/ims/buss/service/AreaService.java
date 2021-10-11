package com.ims.buss.service;

import com.ims.buss.model.Area;
import com.ims.buss.mapper.AreaMapper;
import com.ims.buss.service.AreaService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import java.util.List;
import com.ims.core.matatype.Dto;
import com.ims.core.matatype.Dtos;
import com.baomidou.mybatisplus.plugins.Page;
import com.ims.core.vo.Query;
import com.ims.core.vo.R;

/**
 * <p>
 * 区域表 服务实现类
 * </p>
 *
 * @author 陈骑元
 * @since 2019-10-14
 */
@Service
public class AreaService extends ServiceImpl<AreaMapper, Area>  {
     
   
     /**
	 * 根据Dto查询并返回数据持久化对象集合
	 * 
	 * @return List<Area>
	 */
	public List<Area> list(Dto pDto){
	    
	    return baseMapper.list(pDto);
	};
    /**
	 * 根据Dto查询并返回分页数据持久化对象集合
	 * 
	 * @return Page<Area>
	 */
	public Page<Area> listPage(Dto pDto){
	    Query<Area> query=new Query<Area>(pDto);
	    query.setRecords(baseMapper.listPage(query,pDto));
	    return query;
	};
		
	/**
	 * 根据Dto模糊查询并返回数据持久化对象集合(字符型字段模糊匹配，其余字段精确匹配)
	 * 
	 * @return List<Area>
	 */
	public List<Area> like(Dto pDto){
	
	    return baseMapper.like(pDto);
	
	};

	/**
	 * 根据Dto模糊查询并返回分页数据持久化对象集合(字符型字段模糊匹配，其余字段精确匹配)
	 * 
	 * @return Page<Area>
	 */
	public Page<Area> likePage(Dto pDto){
	    Query<Area> query=new Query<Area>(pDto);
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
	 /**
	  * 
	  * 简要说明：向上更新
	  * 编写者：陈骑元
	  * 创建时间：2019年7月24日 下午11:02:00
	  * @param 说明
	  * @return 说明
	  */
	 public int updateSortUp(Dto pDto){
		 
		 return baseMapper.updateSortUp(pDto);
	 };
	 /**
	  * 
	  * 简要说明：向下更新
	  * 编写者：陈骑元
	  * 创建时间：2019年7月24日 下午11:02:00
	  * @param 说明
	  * @return 说明
	  */
	 public int updateSortDown(Dto pDto){
		 
		 return baseMapper.updateSortDown(pDto);
	 };
	 /**
	  * 
	  * 简要说明：查询区域类表
	  * 编写者：陈骑元
	  * 创建时间：2019年10月19日 下午1:57:39
	  * @param 说明
	  * @return 说明
	  */
	 public List<Area> queryAreaList(String shopId){
			Dto pDto=Dtos.newDto();
			pDto.put("shopId", shopId);
			pDto.setOrder("sort_no ASC ");
			List<Area> areaList=baseMapper.list(pDto);
			return areaList;
		}
}
