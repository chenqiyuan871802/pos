package com.ims.system.mapper;

import java.util.List;

import com.ims.core.matatype.Dto;
import com.ims.system.model.Menu;


/**
 * 
 * 类名:com.toonan.system.mapper.SystemCommonMapper
 * 描述:系统通用查询接口
 * 编写者:陈骑元
 * 创建时间:2018年12月17日 下午2:34:46
 * 修改说明:
 */
public interface SystemCommonMapper{
   
	
	/**
	 * 
	 * 简要说明：查询授权菜单
	 * 编写者：陈骑元
	 * 创建时间：2019年1月7日 下午8:46:47
	 * @param 说明
	 * @return 说明
	 */
	 List<Menu> listRoleMenu(Dto pDto);
	 
	 /**
	  * 
	  * 简要说明：查询角色授权菜单编号
	  * 编写者：陈骑元
	  * 创建时间：2019年1月8日 上午10:43:49
	  * @param 说明
	  * @return 说明
	  */
     List<String> listRoleMenuId(String userId);
}
