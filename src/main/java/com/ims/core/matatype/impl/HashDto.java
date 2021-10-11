package com.ims.core.matatype.impl;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.ims.core.constant.WebplusCons;
import com.ims.core.matatype.Dto;
import com.ims.core.util.WebplusTypeConvert;
import com.ims.core.util.WebplusUtil;




/**
 * 
 * 类描述： * <b>数据传输对象实现</b>
 * <p>
 * 对原生Java Map类型的二次包装，提供<b><i>更加方便的存取API、更强的容错和类型转换机制。</i></b>
 * 在平台二次开发过程中具有很强的实用价值。 开发人员需熟练掌握其提供的相关API。
 * </p>
 * 创建人：陈骑元
 * 创建时间：2016-6-1 上午01:16:44
 * 修改人：蓝枫 
 * 修改时间：2016-6-1 上午01:16:44
 * 修改备注： 
 * @version
 */
public class HashDto extends HashMap<String, Object> implements Dto {


	private static final long serialVersionUID = 1L;

	/**
	 * 缺省构造函数
	 */
	public HashDto() {

	}

	/**
	 * <b>创建一个带有初始化参数的Dto对象</b>
	 * <p>
	 * 参数包含：1、界面提交的请求参数；2、当前用户对象(UserInfoVO)，通过getUserInfo()获得。
	 * 
	 * @param request
	 */
	public HashDto(HttpServletRequest request) {
		putAll(WebplusUtil.getParamAsDto(request));
	    
	}


	/**
	 * 以Integer类型返回属性
	 * 
	 * @param pKey
	 * @return Integer 键值
	 * @throws Exception
	 */
	@Override
	public Integer getInteger(String pKey) {
		Object obj = WebplusTypeConvert.convert(get(pKey), "Integer", null);
		if (obj != null)
			return (Integer) obj;
		else
			return null;
	}

	/**
	 * 以Long类型返回属性
	 * 
	 * @param pKey
	 * @return Long 键值
	 */
	@Override
	public Long getLong(String pKey) {
		Object obj = WebplusTypeConvert.convert(get(pKey), "Long", null);
		if (obj != null)
			return (Long) obj;
		else
			return null;
	}

	/**
	 * 以String类型返回属性
	 * 
	 * @param pKey
	 * @return String 键值
	 */
	@Override
	public String getString(String pKey) {
		Object obj = WebplusTypeConvert.convert(get(pKey), "String", null);
		if (obj != null)
			return (String) obj;
		else
			return "";
	}
	/**
	 * 以Double类型返回属性
	 * 
	 * @param pKey
	 * @return Double 键值
	 */
	@Override
	public Double getDouble(String pKey) {
		Object obj = WebplusTypeConvert.convert(get(pKey), "Double", null);
		if (obj != null)
			return (Double) obj;
		else
			return null;
	}

	/**
	 * 以BigDecimal类型返回属性
	 * 
	 * @param pKey
	 * @return BigDecimal 键值
	 */
	@Override
	public BigDecimal getBigDecimal(String pKey) {
		Object obj = WebplusTypeConvert.convert(get(pKey), "BigDecimal", null);
		if (obj != null)
			return (BigDecimal) obj;
		else
			return null;
	}

	/**
	 * 以Date类型返回属性
	 * 
	 * @param pKey
	 * @return Date 键值(yyyy-MM-dd)
	 */
	@Override
	public Date getDate(String pKey) {
		Object obj = WebplusTypeConvert.convert(get(pKey), "Date", WebplusCons.DATE);
		if (obj != null)
			return (Date) obj;
		else
			return null;
	}

	/**
	 * 以Timestamp类型返回属性
	 * 
	 * @param pKey
	 * @return Timestamp 键值(yyyy-MM-dd HH:mm:ss)
	 */
	@Override
	public Timestamp getTimestamp(String pKey) {
		Object obj = WebplusTypeConvert.convert(get(pKey), "Timestamp", WebplusCons.DATETIME);
		if (obj != null)
			return (Timestamp) obj;
		else
			return null;
	}

	/**
	 * 以Boolean类型返回属性
	 * 
	 * @param pKey
	 * @return Boolean 键值
	 */
	@Override
	public Boolean getBoolean(String pKey) {
		Object obj = WebplusTypeConvert.convert(get(pKey), "Boolean", null);
		if (obj != null)
			return (Boolean) obj;
		else
			return null;
	}

	/**
	 * 以List类型返回属性
	 * 
	 * @param pKey
	 * @return List 键值
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<? extends Object> getList(String pKey) {
		return (List<? extends Object>) get(pKey);
	}


	
	
	/**
	 * 压入交易状态信息
	 * 
	 * @param appMsg
	 */
	@Override
	public void setAppMsg(String appMsg) {
		put(WebplusCons.APPMSG_KEY, appMsg);
	}

	/**
	 * 获取交易状态信息
	 */
	@Override
	public String getAppMsg() {
		return getString(WebplusCons.APPMSG_KEY);
	}

	/**
	 * 压入交易状态码
	 * 
	 * @param appCode
	 */
	@Override
	public void setAppCode(int appCode) {
		put(WebplusCons.APPCODE_KEY, appCode);
	}

	/**
	 * 获取交易状态码
	 */
	@Override
	public int getAppCode() {
		return getInteger(WebplusCons.APPCODE_KEY);
	}

	/**
	 * 设置缺省字符串对象
	 */
	@Override
	public void setStringA(String stringA) {
		put(WebplusCons.DEFAULT_STRING_KEY, stringA);
	}

	/**
	 * 获取缺省字符串对象
	 */
	@Override
	public String getStringA() {
		return getString(WebplusCons.DEFAULT_STRING_KEY);
	}

	/**
	 * 设置缺省集合对象
	 */
	@Override
	public void setListA(List<?> listA) {
		put(WebplusCons.DEFAULT_LIST_KEY, listA);
	}

	/**
	 * 获取缺省集合对象
	 */
	@Override
	public List<?> getListA() {
		return getList(WebplusCons.DEFAULT_LIST_KEY);
	}

	/**
	 * 设置缺省BigDecimal对象
	 */
	@Override
	public void setBigDecimalA(BigDecimal bigDecimalA) {
		put(WebplusCons.DEFAULT_BIGDECIMAL_KEY, bigDecimalA);
	}

	/**
	 * 获取缺省BigDecimal对象
	 */
	@Override
	public BigDecimal getBigDecimalA() {
		return getBigDecimal(WebplusCons.DEFAULT_BIGDECIMAL_KEY);
	}

	/**
	 * 设置缺省Integer对象
	 */
	@Override
	public void setIntegerA(Integer integerA) {
		put(WebplusCons.DEFAULT_INTEGER_KEY, integerA);
	}

	/**
	 * 获取缺省Integer对象
	 */
	@Override
	public Integer getIntegerA() {
		return getInteger(WebplusCons.DEFAULT_INTEGER_KEY);
	}

	/**
	 * 设置缺省Boolean对象
	 */
	@Override
	public void setBooleanA(Boolean booleanA) {
		put(WebplusCons.DEFAULT_BOOLEAN_KEY, booleanA);
	}

	/**
	 * 获取缺省Boolean对象
	 */
	@Override
	public Boolean getBooleanA() {
		return getBoolean(WebplusCons.DEFAULT_BOOLEAN_KEY);
	}

	/**
	 * 设置排序器
	 * 
	 * @param order
	 */
	@Override
	public void setOrder(String order) {
		put(WebplusCons.ORDER_KEY, order);
	}

	/**
	 * 获取排序器
	 * 
	 * @return
	 */
	@Override
	public String getOrder() {
		return getString(WebplusCons.ORDER_KEY);
	}

	/**
	 * 控制台打印DTO对象,辅助调试
	 */
	@Override
	public void println() {
		
		System.out.println(WebplusCons.CONSOLE_FLAG1 + this.toString());
		
	}

}
