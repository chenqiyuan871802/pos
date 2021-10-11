package com.ims.core.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.apache.commons.beanutils.Converter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;



/**
 * 
 * 类描述： Bean属性复制工具类的日期类型转换器
 * 创建人：陈骑元
 * 创建时间：2016-6-2 上午12:18:11
 * 修改人：蓝枫 
 * 修改时间：2016-6-2 上午12:18:11
 * 修改备注： 
 * @version
 */
public class WebplusBeanDateConverter implements Converter {
	private static Log log = LogFactory.getLog(WebplusBeanDateConverter.class);
	@SuppressWarnings("unchecked")
	@Override
	public <T> T convert(Class<T> myClass, Object myObj) {
		if (WebplusUtil.isEmpty(myObj)) {
			return null;
		}
		try {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return (T) df.parse(myObj.toString());
		} catch (ParseException e) {
			try {
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				return (T) df.parse(myObj.toString());
			} catch (ParseException e1) {
				try {
					SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日");
					return (T) df.parse(myObj.toString());
				} catch (ParseException e2) {
					try {
						SimpleDateFormat dfParse = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
						return (T) dfParse.parse(myObj.toString());
					} catch (ParseException e3) {
						log.warn("对象间日期类型属性复制时由于格式问题解析失败，属性值【{"+myObj.toString()+"}】复制失败。");
						e3.printStackTrace();
					}
				}
			}
		}
		return null;
	}

}
