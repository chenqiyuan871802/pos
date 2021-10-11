package com.ims.core.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/**
 * 
 * 类描述： <b>格式转换器</b>
 * 创建人：陈骑元
 * 创建时间：2016-6-1 上午01:33:10
 * 修改人：蓝枫 
 * 修改时间：2016-6-1 上午01:33:10
 * 修改备注： 
 * @version
 */
public class WebplusFormater {

	/**
	 * 将字符串数组转换为由,分割的字符串
	 * 
	 * @param arrStrings
	 *            输入数组
	 * @return
	 */
	public static String toString(String[] arrStrings) {
		String outString = toSeparatString(arrStrings, ",");
		return outString;
	}

	/**
	 * 将字符串数组转换为由指定分隔符分割的字符串
	 * 
	 * @param arrStrings
	 *            输入数组
	 * @param separator
	 *            指定分隔符
	 * @return
	 */
	public static String toSeparatString(String[] arrStrings, String separator) {
		String outString = "";
		if (arrStrings != null && arrStrings.length > 0) {
			for (int i = 0; i < arrStrings.length; i++) {
				outString += arrStrings[i] + separator;
			}
		}
		outString = outString.substring(0, outString.length() - 1);
		return outString;
	}

	/**
	 * 将List转换为由,分割的字符串
	 * 
	 * @param list
	 *            输入List
	 * @return
	 */
	public static String toString(List<?> list) {
		String outString = toSeparatString(list, ",");
		return outString;
	}

	/**
	 * 将List转换为由指定分隔符分割的字符串
	 * 
	 * @param list
	 *            输入List
	 * @param separator
	 *            分隔符
	 * @return
	 */
	public static String toSeparatString(List<?> list, String separator) {
		StringBuilder sb = new StringBuilder();
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				if (i < list.size() - 1) {
					sb.append(list.get(i) + separator);
				} else {
					sb.append(list.get(i));
				}
			}
		}
		return sb.toString();
	}
	/**
	 * 将字符串分割存入到List中
	 * @param str 字符串
	 * @return
	 */
	public static List<String> separatStringToList(String str ){
		List<String> stringList= separatStringToList(str,",");
		return stringList;
	}
	/**
	 * 将字符串分割存入到List中
	 * @param str 字符串
	 * @param separator 分隔符
	 * @return
	 */
	public static List<String> separatStringToList(String str,String separator ){
		List<String> stringList=new ArrayList<String>();
		if(WebplusUtil.isNotEmpty(str)){
			if (str.contains(separator)) {
				stringList = Arrays.asList(str.split(separator));
			} else {
				stringList.add(str);
			}
		}
		return stringList;
	}
	
}
