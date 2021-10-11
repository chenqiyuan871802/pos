package com.ims.buss.service;

import com.ims.buss.model.Desk;
import com.ims.buss.model.Order;
import com.ims.buss.model.OrderLine;
import com.ims.buss.model.OrderLink;
import com.ims.buss.constant.BussCons;
import com.ims.buss.mapper.DeskMapper;
import com.ims.buss.mapper.OrderLineMapper;
import com.ims.buss.mapper.OrderLinkMapper;
import com.ims.buss.mapper.OrderMapper;
import com.ims.buss.service.OrderService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import com.ims.core.constant.WebplusCons;
import com.ims.core.matatype.Dto;
import com.ims.core.util.WebplusUtil;
import com.baomidou.mybatisplus.plugins.Page;
import com.ims.core.vo.Query;
import com.ims.core.vo.R;

/**
 * <p>
 * 订单信息 服务实现类
 * </p>
 *
 * @author 陈骑元
 * @since 2019-07-20
 */
@Service
public class OrderService extends ServiceImpl<OrderMapper, Order>  {
    
	@Autowired
	private OrderLinkMapper orderLinkMapper;
	
	@Autowired
	private OrderLineMapper orderLineMapper;
	@Autowired
	private DeskMapper deskMapper;
   
     /**
	 * 根据Dto查询并返回数据持久化对象集合
	 * 
	 * @return List<Order>
	 */
	public List<Order> list(Dto pDto){
	    
	    return baseMapper.list(pDto);
	};
    /**
	 * 根据Dto查询并返回分页数据持久化对象集合
	 * 
	 * @return Page<Order>
	 */
	public Page<Order> listPage(Dto pDto){
	    Query<Order> query=new Query<Order>(pDto);
	    query.setRecords(baseMapper.listPage(query,pDto));
	    return query;
	};
		
	/**
	 * 根据Dto模糊查询并返回数据持久化对象集合(字符型字段模糊匹配，其余字段精确匹配)
	 * 
	 * @return List<Order>
	 */
	public List<Order> like(Dto pDto){
	
	    return baseMapper.like(pDto);
	
	};

	/**
	 * 根据Dto模糊查询并返回分页数据持久化对象集合(字符型字段模糊匹配，其余字段精确匹配)
	 * 
	 * @return Page<Order>
	 */
	public Page<Order> likePage(Dto pDto){
	    Query<Order> query=new Query<Order>(pDto);
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
	 * 简要说明：保存合并订单信息
	 * 编写者：陈骑元
	 * 创建时间：2019年10月19日 下午6:30:16
	 * @param 说明
	 * @return 说明
	 */
	@Transactional
	public boolean saveMergeOrder(Order order,List<Order> childOrderList,List<OrderLink> orderLinkList){
		if(WebplusUtil.isNotEmpty(childOrderList)){
			for(Order childOrder:childOrderList){
				baseMapper.updateById(childOrder);
			}
		}
		if(WebplusUtil.isNotEmpty(orderLinkList)){
			for(OrderLink orderLink:orderLinkList){
				
				orderLinkMapper.insert(orderLink);
			}
		}
		
		return baseMapper.insert(order)>0;
	}
	/**
	 * 
	 * 简要说明：保存合并订单信息
	 * 编写者：陈骑元
	 * 创建时间：2019年10月19日 下午6:30:16
	 * @param 说明
	 * @return 说明
	 */
	@Transactional
	public boolean saveMergeOrder(Order order,List<OrderLink> orderLinkList){
		/*
		 * if(WebplusUtil.isNotEmpty(childOrderList)){ for(Order
		 * childOrder:childOrderList){ baseMapper.updateById(childOrder); } }
		 */
		if(WebplusUtil.isNotEmpty(orderLinkList)){
			for(OrderLink orderLink:orderLinkList){
				
				orderLinkMapper.insert(orderLink);
			}
		}
		
		return baseMapper.insert(order)>0;
	}
	 /**
	  * 
	  * 简要说明：保存订单信息
	  * 编写者：陈骑元
	  * 创建时间：2019年12月11日 上午2:56:04
	  * @param 说明
	  * @return 说明
	  */
	@Transactional
	public boolean saveOrder(Order order,List<OrderLine> orderLineList,Desk desk){
		if(WebplusUtil.isNotEmpty(orderLineList)){
			for(OrderLine orderLine:orderLineList){
				
				orderLineMapper.insert(orderLine);
			}
		}
		
		deskMapper.updateById(desk);
		return baseMapper.insert(order)>0;
	}
	/**
	 * 
	 * 简要说明：更新订单状态
	 * 编写者：陈骑元（chenqiyuan@toonan.com）
	 * 创建时间： 2020年4月25日 下午1:33:52 
	 * @param 说明
	 * @return 说明
	 */
	public R updateOrderStatus(String orderId,String orderStatus,String staffId) {
		if(WebplusUtil.isEmpty(orderId)) {
			
			 return R.error("订单编号不能为空");
		}
		Order order=new Order();
		order.setOrderId(orderId);
		order.setOrderStatus(orderStatus);
		order.setUpdateTime(WebplusUtil.getDateTime());
		order.setUpdateStaff(staffId);
		if(BussCons.ORDER_STATUS_FINISH.equals(orderStatus)) {
			order.setWhetherPay(WebplusCons.WHETHER_YES);
			order.setPayTime(WebplusUtil.getDateTime());
			order.setPayWay(BussCons.PAY_WAY_OFFLINE);
		    
		}
		int i=baseMapper.updateById(order);
		if(i>0) {
			
			return R.ok();
		}
		return R.error();
	}
	
	
	  /**
	  * 
	  * 简要说明：分页查询会员订单
	  * 编写者：陈骑元（chenqiyuan@toonan.com）
	  * 创建时间： 2020年5月5日 上午7:13:58 
	  * @param 说明
	  * @return 说明
	  */
   public Page<Order> listMemberOrderPage(Dto pDto){
	   Query<Order> query=new Query<Order>(pDto);
	  query.setRecords(baseMapper.listMemberOrderPage(query, pDto));
	  return query;
    }
}
