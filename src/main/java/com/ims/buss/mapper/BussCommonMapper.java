package com.ims.buss.mapper;

import java.util.List;

import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.ims.buss.model.Desk;
import com.ims.buss.model.MenuDict;
import com.ims.buss.model.OrderLine;
import com.ims.buss.model.OrderLineSum;
import com.ims.buss.model.OrderSum;
import com.ims.buss.model.Printer;
import com.ims.buss.model.StepSpec;
import com.ims.core.matatype.Dto;

/**
 * 
 * 类名:com.ims.buss.mapper.BussCommonMapper
 * 描述:通用业务处理接口
 * 编写者:陈骑元
 * 创建时间:2019年7月27日 下午11:14:39
 * 修改说明:
 */
public interface BussCommonMapper {
	
	/**
	 * 
	 * 简要说明：分页查询产品信息
	 * 编写者：陈骑元
	 * 创建时间：2019年8月30日 下午2:28:51
	 * @param 说明
	 * @return 说明
	 */
	List<MenuDict> listMenuDictPage(Pagination page,Dto pDto );
	
	/**
	 * 
	 * 简要说明：下载菜品
	 * 编写者：陈骑元（chenqiyuan@toonan.com）
	 * 创建时间： 2020年3月18日 下午8:48:33 
	 * @param 说明
	 * @return 说明
	 */
	List<MenuDict> listMenuDict(Dto pDto );
	/**
	 * 
	 * 简要说明：查询步骤规格
	 * 编写者：陈骑元（chenqiyuan@toonan.com）
	 * 创建时间： 2020年3月18日 下午8:48:33 
	 * @param 说明
	 * @return 说明
	 */
	List<StepSpec> listStepSpec(Dto pDto );
	
	/**
	 * 
	 * 简要说明：查询自助餐菜品
	 * 编写者：陈骑元
	 * 创建时间：2019年8月30日 下午2:28:51
	 * @param 说明
	 * @return 说明
	 */
	List<MenuDict> listBuffetMenuDict(Dto pDto );
	
	/**
	 * 
	 * 简要说明：查询打印机管理
	 * 编写者：陈骑元
	 * 创建时间：2019年8月30日 下午2:28:51
	 * @param 说明
	 * @return 说明
	 */
	List<Printer> listPrinterPage(Pagination page,Dto pDto );
	/**
	 * 
	 * 简要说明：查询桌号管理
	 * 编写者：陈骑元
	 * 创建时间：2019年8月30日 下午2:28:51
	 * @param 说明
	 * @return 说明
	 */
	List<Desk> listDeskPage(Pagination page,Dto pDto );
	
	/**
	 * 
	 * 简要说明：查询菜品统计信息
	 * 编写者：陈骑元
	 * 创建时间：2019年11月10日 下午1:45:42
	 * @param 说明
	 * @return 说明
	 */
	OrderSum queryOrderSum(Dto pDto);
	/**
	 * 
	 * 简要说明：查询汇总下单列表
	 * 编写者：陈骑元
	 * 创建时间：2019年11月10日 下午1:45:42
	 * @param 说明
	 * @return 说明
	 */
	List<OrderLine> listSumOrderLine(Dto pDto);
	/**
	 * 
	 * 简要说明：统计日期订单汇总
	 * 编写者：陈骑元
	 * 创建时间：2019年11月24日 下午10:38:29
	 * @param 说明
	 * @return 说明
	 */
	List<OrderSum> listSumDateOrderPage(Pagination page,Dto pDto );
	/**
	 * 
	 * 简要说明：统计月订单汇总
	 * 编写者：陈骑元
	 * 创建时间：2019年11月24日 下午10:38:29
	 * @param 说明
	 * @return 说明
	 */
	List<OrderSum> listSumMonthOrderPage(Pagination page,Dto pDto );
	
	/**
	 * 
	 * 简要说明：统计日期订单汇总
	 * 编写者：陈骑元
	 * 创建时间：2019年11月24日 下午10:38:29
	 * @param 说明
	 * @return 说明
	 */
	List<OrderSum> listSumDateOrder(Dto pDto );
	/**
	 * 
	 * 简要说明：统计月订单汇总
	 * 编写者：陈骑元
	 * 创建时间：2019年11月24日 下午10:38:29
	 * @param 说明
	 * @return 说明
	 */
	List<OrderSum> listSumMonthOrder(Dto pDto );
	/**
	 * 
	 * 简要说明：查询订单汇总
	 * 编写者：陈骑元（chenqiyuan@toonan.com）
	 * 创建时间： 2020年7月18日 下午12:07:39 
	 * @param 说明
	 * @return 说明
	 */
	OrderSum  querySumCashbox(Dto pDto);
	/**
	 * 
	 * 简要说明：查询订单汇总
	 * 编写者：陈骑元（chenqiyuan@toonan.com）
	 * 创建时间： 2020年7月18日 下午10:40:38 
	 * @param 说明
	 * @return 说明
	 */
	OrderSum  queryOrderSummary(Dto pDto);
	
	/**
	 * 
	 * 简要说明：菜品统计
	 * 编写者：陈骑元（chenqiyuan@toonan.com）
	 * 创建时间： 2020年7月31日 下午9:24:46 
	 * @param 说明
	 * @return 说明
	 */
	List<OrderLineSum> listOrderLineSumPage(Pagination page,Dto pDto);
	/**
	 * 
	 * 简要说明：菜品统计
	 * 编写者：陈骑元（chenqiyuan@toonan.com）
	 * 创建时间： 2020年7月31日 下午9:24:46 
	 * @param 说明
	 * @return 说明
	 */
	List<OrderLineSum> listOrderLineSum(Dto pDto);

	/**
	 * 
	 * 简要说明：查询汇总外卖下单列表
	 * 编写者：陈骑元
	 * 创建时间：2019年11月10日 下午1:45:42
	 * @param 说明
	 * @return 说明
	 */
	List<OrderLine> listSumTakeOutOrderLine(Dto pDto);
	
	/**
	 * 
	 * 简要说明：查询菜品统计信息
	 * 编写者：陈骑元
	 * 创建时间：2019年11月10日 下午1:45:42
	 * @param 说明
	 * @return 说明
	 */
	OrderSum queryOrderSumNew(Dto pDto);
}
