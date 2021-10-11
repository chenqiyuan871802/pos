package com.ims.core.matatype;

import javax.servlet.http.HttpServletRequest;

import com.ims.core.constant.WebplusCons;
import com.ims.core.matatype.impl.HashDto;
import com.ims.core.util.WebplusUtil;







/**
 * 
 * 类描述： <b>数据传输对象实例化辅助类</b>
 * 创建人：陈骑元
 * 创建时间：2016-6-1 上午01:38:21
 * 修改人：蓝枫 
 * 修改时间：2016-6-1 上午01:38:21
 * 修改备注： 
 * @version
 */
public class Dtos {

	/**
	 * 创建一个常规的Dto对象
	 * 
	 */
	public static Dto newDto() {
		return new HashDto();
	}
	

	/**
	 * 创建一个常规的Dto对象，并初始化一个键值对。
	 * 
	 * @param keyString
	 * @param valueObject
	 * @return
	 */
	public static Dto newDto(String keyString, Object valueObject) {
		Dto dto = new HashDto();
		dto.put(keyString, valueObject);
		return dto;
	}

	/**
	 * <b>创建一个带有初始化参数的Dto对象</b>
	 * <p>
	 * 参数包含：1、界面提交的请求参数；2、当前用户对象(UserInfoVO)，通过getUserInfo()获得。
	 * 
	 * @param request
	 * @return
	 */
	public static Dto newDto(HttpServletRequest request) {
		return new HashDto(request);
	}

	/**
	 * 创建一个数学运算SQL的参数DTO
	 * <p>
	 * 如：Dto dto = Dtos.newCalcDto("MIN(cascade_id_)");
	 * 
	 * @param expr
	 *            构造参数为运算表达式
	 */
	public static Dto newCalcDto(String expr) {
		Dto dto = newDto();
		dto.put(WebplusCons.CALCEXPR, expr);
		return dto;
	}
	/**
	 * 
	 * 简要说明：构造一个分页参数Dto
	 * 编写者：陈骑元
	 * 创建时间：2019年3月2日 上午8:39:29
	 * @param 说明
	 * @return 说明
	 */
	public static Dto newPage(String currentPage,String pageSize) {
		Dto dto = newDto();
		if(WebplusUtil.isEmpty(currentPage)){
			currentPage=WebplusCons.DEFAULT_CURRENTPAGE;
		}
		if(WebplusUtil.isEmpty(pageSize)){
			pageSize=WebplusCons.DEFAULT_PAGESIZE;
		}
		dto.put(WebplusCons.PAGE, currentPage);
		dto.put(WebplusCons.LIMIT, pageSize);
		return dto;
	}

}
