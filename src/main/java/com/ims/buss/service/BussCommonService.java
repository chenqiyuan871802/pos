package com.ims.buss.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.plugins.Page;
import com.ims.buss.mapper.BussCommonMapper;
import com.ims.buss.model.Desk;
import com.ims.buss.model.MenuDict;
import com.ims.buss.model.OrderLine;
import com.ims.buss.model.OrderLineSum;
import com.ims.buss.model.OrderSum;
import com.ims.buss.model.Printer;
import com.ims.buss.model.StepSpec;
import com.ims.core.matatype.Dto;
import com.ims.core.vo.Query;

/**
 * 
 * 类名:com.ims.buss.service.BussCommonService
 * 描述:业务通用逻辑处理
 * 编写者:陈骑元
 * 创建时间:2019年7月27日 下午11:16:13
 * 修改说明:
 */
@Service
public class BussCommonService {
	
	@Autowired
    private BussCommonMapper bussCommonMapper;
	
	/**
	 * 
	 * 简要说明：分页查询菜品信息
	 * 编写者：陈骑元
	 * 创建时间：2019年8月30日 下午2:28:51
	 * @param 说明
	 * @return 说明
	 */
	public Page<MenuDict> listMenuDictPage(Dto pDto ){
		 Query<MenuDict> query=new Query<MenuDict>(pDto);
		 query.setRecords(bussCommonMapper.listMenuDictPage(query, pDto));
		 return query;
	};
	
	/**
	 * 
	 * 简要说明：加载菜品
	 * 编写者：陈骑元（chenqiyuan@toonan.com）
	 * 创建时间： 2020年3月18日 下午8:48:33 
	 * @param 说明
	 * @return 说明
	 */
	public List<MenuDict> listMenuDict(Dto pDto ){
		
		return bussCommonMapper.listMenuDict(pDto);
	};
	/**
	 * 
	 * 简要说明：查询步骤规格
	 * 编写者：陈骑元（chenqiyuan@toonan.com）
	 * 创建时间： 2020年3月18日 下午8:48:33 
	 * @param 说明
	 * @return 说明
	 */
	public List<StepSpec> listStepSpec(Dto pDto ){
		
		return bussCommonMapper.listStepSpec(pDto);
	};
	/**
	 * 
	 * 简要说明：查询自助餐菜品
	 * 编写者：陈骑元
	 * 创建时间：2019年8月30日 下午2:28:51
	 * @param 说明
	 * @return 说明
	 */
	public List<MenuDict> listBuffetMenuDict(Dto pDto ){
		
		return bussCommonMapper.listBuffetMenuDict(pDto);
				
	};
	
	/**
	 * 
	 * 简要说明：查询打印机管理
	 * 编写者：陈骑元
	 * 创建时间：2019年8月30日 下午2:28:51
	 * @param 说明
	 * @return 说明
	 */
	public Page<Printer> listPrinterPage(Dto pDto ){
		 Query<Printer> query=new Query<Printer>(pDto);
		 query.setRecords(bussCommonMapper.listPrinterPage(query, pDto));
		 return query;
	}
	/**
	 * 
	 * 简要说明：查询桌号管理
	 * 编写者：陈骑元
	 * 创建时间：2019年8月30日 下午2:28:51
	 * @param 说明
	 * @return 说明
	 */
	public Page<Desk> listDeskPage(Dto pDto ){
		Query<Desk> query=new Query<Desk>(pDto);
		query.setRecords(bussCommonMapper.listDeskPage(query, pDto));
		return query;
	}
	/**
	 * 
	 * 简要说明：查询菜品统计信息
	 * 编写者：陈骑元
	 * 创建时间：2019年11月10日 下午1:45:42
	 * @param 说明
	 * @return 说明
	 */
	public OrderSum queryOrderSum(Dto pDto){
		
		return bussCommonMapper.queryOrderSum(pDto);
	}
    /**
     * 
     * 简要说明：
     * 编写者：陈骑元
     * 创建时间：2019年11月10日 下午2:54:28
     * @param 说明
     * @return 说明
     */
	public List<OrderLine> listSumOrderLine(Dto pDto){
		
		return bussCommonMapper.listSumOrderLine(pDto);
	};
	
	
	/**
	 * 
	 * 简要说明：订单日报表统计
	 * 编写者：陈骑元
	 * 创建时间：2019年11月10日 下午1:45:42
	 * @param 说明
	 * @return 说明
	 */
	public Page<OrderSum> listSumDateOrderPage(Dto pDto){
		
		Query<OrderSum> query=new Query<OrderSum>(pDto);
		query.setRecords(bussCommonMapper.listSumDateOrderPage(query, pDto));
		return query;
	}
	/**
	 * 
	 * 简要说明：订单月报表统计
	 * 编写者：陈骑元
	 * 创建时间：2019年11月10日 下午1:45:42
	 * @param 说明
	 * @return 说明
	 */
	public Page<OrderSum> listSumMonthOrderPage(Dto pDto){
		
		Query<OrderSum> query=new Query<OrderSum>(pDto);
		query.setRecords(bussCommonMapper.listSumMonthOrderPage(query, pDto));
		return query;
	}
	
	/**
	 * 
	 * 简要说明：订单日报表统计
	 * 编写者：陈骑元
	 * 创建时间：2019年11月10日 下午1:45:42
	 * @param 说明
	 * @return 说明
	 */
	public List<OrderSum> listSumDateOrder(Dto pDto){
		
	
		return bussCommonMapper.listSumDateOrder(pDto);
	}
	/**
	 * 
	 * 简要说明：订单月报表统计
	 * 编写者：陈骑元
	 * 创建时间：2019年11月10日 下午1:45:42
	 * @param 说明
	 * @return 说明
	 */
	public List<OrderSum> listSumMonthOrder(Dto pDto){
		
		
		return bussCommonMapper.listSumMonthOrder(pDto);
	}
	
	 /**
     * 
     * 简要说明：初金，入金初金汇总
     * 编写者：陈骑元
     * 创建时间：2019年11月10日 下午2:54:28
     * @param 说明
     * @return 说明
     */
	public OrderSum querySumCashbox(Dto pDto){
		
		return bussCommonMapper.querySumCashbox(pDto);
	};
	/**
	 * 
	 * 简要说明：查询订单汇总
	 * 编写者：陈骑元（chenqiyuan@toonan.com）
	 * 创建时间： 2020年7月18日 下午10:41:10 
	 * @param 说明
	 * @return 说明
	 */
	public OrderSum  queryOrderSummary(Dto pDto) {
		
		return bussCommonMapper.queryOrderSummary(pDto);
	}
	
	/**
	 * 
	 * 简要说明：分页查询菜品统计
	 * 编写者：陈骑元
	 * 创建时间：2019年8月30日 下午2:28:51
	 * @param 说明
	 * @return 说明
	 */
	public Page<OrderLineSum> listOrderLineSumPage(Dto pDto ){
		 Query<OrderLineSum> query=new Query<OrderLineSum>(pDto);
		 query.setRecords(bussCommonMapper.listOrderLineSumPage(query, pDto));
		 return query;
	};
	/**
	 * 
	 * 简要说明：分页查询菜品统计
	 * 编写者：陈骑元
	 * 创建时间：2019年8月30日 下午2:28:51
	 * @param 说明
	 * @return 说明
	 */
	public List<OrderLineSum> listOrderLineSum(Dto pDto ){
	
		return bussCommonMapper.listOrderLineSum(pDto);
	};
	
	 /**
     * 
     * 简要说明：
     * 编写者：陈骑元
     * 创建时间：2019年11月10日 下午2:54:28
     * @param 说明
     * @return 说明
     */
	public List<OrderLine> listSumTakeOutOrderLine(Dto pDto){
		
		return bussCommonMapper.listSumTakeOutOrderLine(pDto);
	};
	
	
	/**
	 * 
	 * 简要说明：查询菜品统计信息
	 * 编写者：陈骑元
	 * 创建时间：2019年11月10日 下午1:45:42
	 * @param 说明
	 * @return 说明
	 */
	public OrderSum queryOrderSumNew(Dto pDto){
		
		return bussCommonMapper.queryOrderSumNew(pDto);
	}
}
