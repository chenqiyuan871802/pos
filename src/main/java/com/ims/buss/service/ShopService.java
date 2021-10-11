package com.ims.buss.service;

import com.ims.buss.model.Shop;
import com.ims.buss.mapper.ShopMapper;
import com.ims.buss.service.ShopService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import java.util.List;
import com.ims.core.matatype.Dto;
import com.baomidou.mybatisplus.plugins.Page;
import com.ims.core.vo.Query;

/**
 * <p>
 * 店铺表 服务实现类
 * </p>
 *
 * @author 陈骑元
 * @since 2019-07-20
 */
@Service
public class ShopService extends ServiceImpl<ShopMapper, Shop>  {
     
   
     /**
	 * 根据Dto查询并返回数据持久化对象集合
	 * 
	 * @return List<Shop>
	 */
	public List<Shop> list(Dto pDto){
	    
	    return baseMapper.list(pDto);
	};
    /**
	 * 根据Dto查询并返回分页数据持久化对象集合
	 * 
	 * @return Page<Shop>
	 */
	public Page<Shop> listPage(Dto pDto){
	    Query<Shop> query=new Query<Shop>(pDto);
	    query.setRecords(baseMapper.listPage(query,pDto));
	    return query;
	};
		
	/**
	 * 根据Dto模糊查询并返回数据持久化对象集合(字符型字段模糊匹配，其余字段精确匹配)
	 * 
	 * @return List<Shop>
	 */
	public List<Shop> like(Dto pDto){
	
	    return baseMapper.like(pDto);
	
	};

	/**
	 * 根据Dto模糊查询并返回分页数据持久化对象集合(字符型字段模糊匹配，其余字段精确匹配)
	 * 
	 * @return Page<Shop>
	 */
	public Page<Shop> likePage(Dto pDto){
	    Query<Shop> query=new Query<Shop>(pDto);
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
	   * 简要说明：加载店铺信息
	   * 编写者：陈骑元（chenqiyuan@toonan.com）
	   * 创建时间： 2020年5月2日 下午1:45:28 
	   * @param 说明
	   * @return 说明
	   */
	  public Page<Shop> listShopPage(Dto pDto){
			Query<Shop> query=new Query<Shop>(pDto);
			query.setRecords(baseMapper.listShopPage(query, pDto));
			return query;
	   };	

	 
}
