package com.ims.core.matatype;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * 
 * 类描述： * <b>数据传输对象接口</b>
 * <p>
 * 对原生Java Map类型的二次包装，提供<b><i>更加方便的存取API、更强的容错和类型转换机制。</i></b>
 * 在平台二次开发过程中具有很强的实用价值。 开发人员需熟练掌握其提供的相关API。
 * </p>
 * 创建人：陈骑元
 * 创建时间：2016-6-1 上午01:09:22
 * 修改人：蓝枫 
 * 修改时间：2016-6-1 上午01:09:22
 * 修改备注： 
 * @version
 */
public interface Dto extends Map<String, Object> {

	/**
	 * 以Integer类型返回属性
	 * 
	 * @param pKey
	 * @return Integer 键值
	 */
	Integer getInteger(String pKey);

	/**
	 * 以Long类型返回属性
	 * 
	 * @param pKey
	 * @return Long 键值
	 */
	Long getLong(String pKey);

	/**
	 * 以String类型返回属性
	 * 
	 * @param pKey
	 * @return String 键值
	 */
	String getString(String pKey);
	/**
	 * 以Double类型返回属性
	 * 
	 * @param pKey
	 * @return Double 键值
	 */
	Double getDouble(String pKey);

	/**
	 * 以BigDecimal类型返回属性
	 * 
	 * @param pKey
	 * @return BigDecimal 键值
	 */
	BigDecimal getBigDecimal(String pKey);

	/**
	 * 以Date类型返回属性
	 * 
	 * @param pKey
	 * @return Date 键值(yyyy-MM-dd)
	 */
	Date getDate(String pKey);

	/**
	 * 以Timestamp类型返回属性
	 * 
	 * @param pKey
	 * @return Timestamp 键值(yyyy-MM-dd HH:mm:ss)
	 */
	Timestamp getTimestamp(String pKey);

	/**
	 * 以Boolean类型返回属性
	 * 
	 * @param pKey
	 * @return Boolean 键值
	 */
	Boolean getBoolean(String pKey);

	/**
	 * 以List类型返回属性
	 * 
	 * @param pKey
	 * @return List 键值
	 */
	List<? extends Object> getList(String pKey);


	/**
	 * 压入交易状态信息
	 * 
	 * @param appMsg
	 */
	void setAppMsg(String appMsg);

	/**
	 * 获取交易状态信息
	 */
	String getAppMsg();

	/**
	 * 压入交易状态码
	 * 
	 * @param appCode
	 */
	void setAppCode(int appCode);

	/**
	 * 获取交易状态码
	 */
	int getAppCode();

	/**
	 * 设置缺省字符串对象
	 */
	void setStringA(String stringA);

	/**
	 * 获取缺省字符串对象
	 */
	String getStringA();

	/**
	 * 设置缺省集合对象
	 */
	void setListA(List<?> listA);

	/**
	 * 获取缺省集合对象
	 */
	List<?> getListA();

	/**
	 * 设置缺省BigDecimal对象
	 */
	void setBigDecimalA(BigDecimal bigDecimalA);

	/**
	 * 获取缺省BigDecimal对象
	 */
	BigDecimal getBigDecimalA();

	/**
	 * 设置缺省Integer对象
	 */
	void setIntegerA(Integer integerA);

	/**
	 * 获取缺省Integer对象
	 */
	Integer getIntegerA();

	/**
	 * 设置缺省Boolean对象
	 */
	void setBooleanA(Boolean booleanA);

	/**
	 * 获取缺省Boolean对象
	 */
	Boolean getBooleanA();

	/**
	 * 设置排序器
	 * <p>
	 * 用法：qDto.setOrder("sort_no_ DESC");
	 * </p>
	 * 
	 * @param order
	 */
	void setOrder(String order);

	/**
	 * 获取排序器
	 * 
	 * @return
	 */
	String getOrder();

	/**
	 * 控制台打印DTO对象,只打印简单对象。
	 */
	void println();

}
