package com.ims.core.util;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.common.collect.Lists;
import com.ims.core.constant.WebplusCons;
import com.ims.core.matatype.Dto;
import com.ims.core.matatype.Dtos;
import com.ims.core.matatype.impl.BaseModel;
import com.ims.core.matatype.impl.HashDto;

/**
 * <b>辅助工具类</b>
 * 
 * @author OSWorks-XC
 * @since 1.0
 * @date 2009-1-22
 */
@SuppressWarnings("all")
public class WebplusUtil {
	
	private static Log log = LogFactory.getLog(WebplusUtil.class);

	/**
	 * 判断对象是否Empty(null或元素为0)<br>
	 * 实用于对如下对象做判断:String Collection及其子类 Map及其子类
	 * 
	 * @param pObj
	 *            待检查对象
	 * @return boolean 返回的布尔值
	 */
	public static boolean isEmpty(Object pObj) {
		if (pObj == null)
			return true;
		if (pObj == "")
			return true;
		if (pObj instanceof String) {
			if (((String) pObj).length() == 0) {
				return true;
			}
		} else if (pObj instanceof Collection) {
			if (((Collection) pObj).size() == 0) {
				return true;
			}
		} else if (pObj instanceof Map) {
			if (((Map) pObj).size() == 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 将字符串型日期转换为日期型
	 * 
	 * @param strDate
	 * @return
	 */
	public static Date stringToDate(String strDate) {
		Date tmpDate = (new SimpleDateFormat(WebplusCons.DATETIME)).parse(strDate, new ParsePosition(0));
		if (tmpDate == null) {
			tmpDate = (new SimpleDateFormat(WebplusCons.DATE)).parse(strDate, new ParsePosition(0));
		}
		return tmpDate;
	}

	/**
	 * 判断对象是否为NotEmpty(!null或元素>0)<br>
	 * 实用于对如下对象做判断:String Collection及其子类 Map及其子类
	 * 
	 * @param pObj
	 *            待检查对象
	 * @return boolean 返回的布尔值
	 */
	public static boolean isNotEmpty(Object pObj) {
		if (pObj == null)
			return false;
		if (pObj == "")
			return false;
		if (pObj instanceof String) {
			if (((String) pObj).length() == 0) {
				return false;
			}
		} else if (pObj instanceof Collection) {
			if (((Collection) pObj).size() == 0) {
				return false;
			}
		} else if (pObj instanceof Map) {
			if (((Map) pObj).size() == 0) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 只要其中一个对象是empty，返回true，否则返回false
	 * 
	 * @param o
	 * @return
	 */
	public static final boolean isAnyEmpty(Object... o) {
		for (int i = 0; i < o.length; i++) {
			Object obj = o[i];
			if (isEmpty(obj)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 所有对象都不为empty，返回true，否则返回false
	 * 
	 * @param o
	 * @return
	 */
	public static final boolean isNotAnyEmpty(Object... o) {
		return !isAnyEmpty(o);
	}

	/**
	 * Java对象之间属性值拷贝(Dto、Map、JavaBean)
	 * 
	 * @param pFromObj
	 *            Bean源对象
	 * @param pToObj
	 *            Bean目标对象
	 */
	public static void copyProperties(Object pFromObj, Object pToObj) {
		if (pToObj != null) {
			try {
				// 支持属性空值复制
				BeanUtilsBean.getInstance().getConvertUtils().register(false, true, 0);
				// 日期类型复制
				WebplusBeanDateConverter converter = new WebplusBeanDateConverter();
				ConvertUtils.register(converter, java.util.Date.class);
				ConvertUtils.register(converter, java.sql.Date.class);
				BeanUtils.copyProperties(pToObj, pFromObj);
			} catch (Exception e) {
				throw new RuntimeException("JavaBean之间的属性值拷贝发生错误", e);
			}
		}
	}

	/**
	 * 将JavaBean对象中的属性值拷贝到Dto对象
	 * 
	 * @param pFromObj
	 *            JavaBean对象源
	 * @param pToDto
	 *            Dto目标对象
	 */
	public static void copyProperties(Object pFromObj, Dto pToDto) {
		if (pToDto != null) {
			try {
				pToDto.putAll(BeanUtils.describe(pFromObj));
				pToDto.remove("class");
			} catch (Exception e) {
				throw new RuntimeException("将JavaBean属性值拷贝到Dto对象发生错误", e);
			}
		}
	}

	/**
	 * 将传入的身份证号码进行校验，并返回一个对应的18位身份证
	 * 
	 * @param personIDCode
	 *            身份证号码
	 * @return String 十八位身份证号码
	 * @throws 无效的身份证号
	 */
	public static String getFixedPersonIDCode(String personIDCode) {
		if (personIDCode == null)
			throw new RuntimeException("输入的身份证号无效，请检查");

		if (personIDCode.length() == 18) {
			if (isIdentity(personIDCode))
				return personIDCode;
			else
				throw new RuntimeException("输入的身份证号无效，请检查");
		} else if (personIDCode.length() == 15)
			return fixPersonIDCodeWithCheck(personIDCode);
		else
			throw new RuntimeException("输入的身份证号无效，请检查");
	}

	/**
	 * 修补15位居民身份证号码为18位，并校验15位身份证有效性
	 * 
	 * @param personIDCode
	 *            十五位身份证号码
	 * @return String 十八位身份证号码
	 * @throws 无效的身份证号
	 */
	public static String fixPersonIDCodeWithCheck(String personIDCode) {
		if (personIDCode == null || personIDCode.trim().length() != 15)
			throw new RuntimeException("输入的身份证号不足15位，请检查");

		if (!isIdentity(personIDCode))
			throw new RuntimeException("输入的身份证号无效，请检查");

		return fixPersonIDCodeWithoutCheck(personIDCode);
	}

	/**
	 * 修补15位居民身份证号码为18位，不校验身份证有效性
	 * 
	 * @param personIDCode
	 *            十五位身份证号码
	 * @return 十八位身份证号码
	 * @throws 身份证号参数不是15位
	 */
	public static String fixPersonIDCodeWithoutCheck(String personIDCode) {
		String id17 = null;
		if (personIDCode != null && personIDCode.length() == 17)
			id17 = personIDCode;
		else if (personIDCode != null || personIDCode.trim().length() == 15)
			id17 = personIDCode.substring(0, 6) + "19" + personIDCode.substring(6, 15); // 15位身份证补'19'
		else
			throw new RuntimeException("输入的身份证号不足15位，请检查");

		char[] code = { '1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2' }; // 11个校验码字符
		int[] factor = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2, 1 }; // 18个加权因子
		int[] idcd = new int[18];
		int sum; // 根据公式 ∑(ai×Wi) 计算
		int remainder; // 第18位校验码
		for (int i = 0; i < 17; i++) {
			idcd[i] = Integer.parseInt(id17.substring(i, i + 1));
		}
		sum = 0;
		for (int i = 0; i < 17; i++) {
			sum = sum + idcd[i] * factor[i];
		}
		remainder = sum % 11;
		String lastCheckBit = String.valueOf(code[remainder]);
		return id17 + lastCheckBit;
	}

	/**
	 * 判断是否是有效的18位或15位居民身份证号码
	 * 
	 * @param identity
	 *            18位或15位居民身份证号码
	 * @return 是否为有效的身份证号码
	 */
	public static boolean isIdentity(String identity) {
		if (identity == null)
			return false;
		if (identity.length() == 18 || identity.length() == 15) {
			String id15 = null;
			String id17 = null;
			if (identity.length() == 18) {
				id17 = identity.substring(0, 17);
				id15 = identity.substring(0, 6) + identity.substring(8, 17);
			} else
				id15 = identity;
			try {
				Long.parseLong(id15); // 校验是否为数字字符串

				String birthday = "19" + id15.substring(6, 12);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
				sdf.parse(birthday); // 校验出生日期
				if (identity.length() == 18 && !fixPersonIDCodeWithoutCheck(id17).equals(identity))
					return false; // 校验18位身份证
			} catch (Exception e) {
				return false;
			}
			return true;
		} else
			return false;
	}

	/**
	 * 从身份证号中获取出生日期，身份证号可以为15位或18位
	 * 
	 * @param identity
	 *            身份证号
	 * @return 出生日期
	 * @throws 身份证号出生日期段有误
	 */
	public static Timestamp getBirthdayFromPersonIDCode(String identity) {
		String id = getFixedPersonIDCode(identity);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		try {
			Timestamp birthday = new Timestamp(sdf.parse(id.substring(6, 14)).getTime());
			return birthday;
		} catch (ParseException e) {
			throw new RuntimeException("不是有效的身份证号，请检查", e);
		}
	}

	/**
	 * 从身份证号获取性别
	 * 
	 * @param identity
	 *            身份证号
	 * @return 性别代码
	 * @throws Exception
	 *             无效的身份证号码
	 */
	public static String getGenderFromPersonIDCode(String identity) {
		String id = getFixedPersonIDCode(identity);
		char sex = id.charAt(16);
		return sex % 2 == 0 ? "2" : "1";
	}

	private static String DigitUppercaseStrings[] = new String[] { "零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖" };

	private static String moneyStrings[] = new String[] { "", "拾", "佰", "仟", "万", "拾", "佰", "仟", "亿", "拾", "佰", "仟",
			"万", "拾", "佰", "仟", "亿", "拾", "佰", "仟", "万", "拾", "佰", "仟" };

	/**
	 * 将货币转换为大写形式(类内部调用)
	 * 
	 * @param val
	 * @return String
	 */
	private static String PositiveIntegerToHanStr(String NumStr) {
		// 输入字符串必须正整数，只允许前导空格(必须右对齐)，不宜有前导零
		String RMBStr = "";
		boolean lastzero = false;
		boolean hasvalue = false; // 亿、万进位前有数值标记
		int len, n;
		len = NumStr.length();
		if (len > 15)
			return "数值过大!";
		for (int i = len - 1; i >= 0; i--) {
			if (NumStr.charAt(len - i - 1) == ' ')
				continue;
			n = NumStr.charAt(len - i - 1) - '0';
			if (n < 0 || n > 9)
				return "输入含非数字字符!";

			if (n != 0) {
				if (lastzero)
					RMBStr += DigitUppercaseStrings[0]; // 若干零后若跟非零值，只显示一个零
				// 除了亿万前的零不带到后面
				// if( !( n==1 && (i%4)==1 && (lastzero || i==len-1) ) )
				// 如十进位前有零也不发壹音用此行
				if (!(n == 1 && (i % 4) == 1 && i == len - 1)) // 十进位处于第一位不发壹音
					RMBStr += DigitUppercaseStrings[n];
				RMBStr += moneyStrings[i]; // 非零值后加进位，个位为空
				hasvalue = true; // 置万进位前有值标记

			} else {
				if ((i % 8) == 0 || ((i % 8) == 4 && hasvalue)) // 亿万之间必须有非零值方显示万
					RMBStr += moneyStrings[i]; // “亿”或“万”
			}
			if (i % 8 == 0)
				hasvalue = false; // 万进位前有值标记逢亿复位
			lastzero = (n == 0) && (i % 4 != 0);
		}

		if (RMBStr.length() == 0)
			return DigitUppercaseStrings[0]; // 输入空字符或"0"，返回"零"
		return RMBStr;
	}

	/**
	 * 将货币转换为大写形式
	 * 
	 * @param val
	 *            传入的数据
	 * @return String 返回的人民币大写形式字符串
	 */
	public static String numToRMBStr(double val) {
		String SignStr = "";
		String TailStr = "";
		long fraction, integer;
		int jiao, fen;

		if (val < 0) {
			val = -val;
			SignStr = "负";
		}
		if (val > 99999999999999.999 || val < -99999999999999.999)
			return "数值位数过大!";
		// 四舍五入到分
		long temp = Math.round(val * 100);
		integer = temp / 100;
		fraction = temp % 100;
		jiao = (int) fraction / 10;
		fen = (int) fraction % 10;
		if (jiao == 0 && fen == 0) {
			TailStr = "整";
		} else {
			TailStr = DigitUppercaseStrings[jiao];
			if (jiao != 0)
				TailStr += "角";
			// 零元后不写零几分
			if (integer == 0 && jiao == 0)
				TailStr = "";
			if (fen != 0)
				TailStr += DigitUppercaseStrings[fen] + "分";
		}
		// 下一行可用于非正规金融场合，0.03只显示“叁分”而不是“零元叁分”
		// if( !integer ) return SignStr+TailStr;
		return SignStr + PositiveIntegerToHanStr(String.valueOf(integer)) + "元" + TailStr;
	}

	/**
	 * 校验手机号
	 * 
	 * @param mobile
	 * @return 校验通过返回true，否则返回false
	 */
	public static boolean checkMobile(String mobile) {
		if (isEmpty(mobile)) {
			return false;
		}
		return Pattern.matches("^(13[0-9]|14[5-9]|15[012356789]|166|17[0-8]|18[0-9]|19[8-9])[0-9]{8}$", mobile);
	}

	/**
	 * 获取指定年份和月份对应的天数
	 * 
	 * @param year
	 *            指定的年份
	 * @param month
	 *            指定的月份
	 * @return int 返回天数
	 */
	public static int getDaysInMonth(int year, int month) {
		if ((month == 1) || (month == 3) || (month == 5) || (month == 7) || (month == 8) || (month == 10)
				|| (month == 12)) {
			return 31;
		} else if ((month == 4) || (month == 6) || (month == 9) || (month == 11)) {
			return 30;
		} else {
			if (((year % 4) == 0) && ((year % 100) != 0) || ((year % 400) == 0)) {
				return 29;
			} else {
				return 28;
			}
		}
	}

	/**
	 * 根据所给的起止时间来计算间隔的天数
	 * 
	 * @param startDate
	 *            起始时间
	 * @param endDate
	 *            结束时间
	 * @return int 返回间隔天数
	 */
	public static int getIntervalDays(java.sql.Date startDate, java.sql.Date endDate) {
		long startdate = startDate.getTime();
		long enddate = endDate.getTime();
		long interval = enddate - startdate;
		int intervalday = (int) (interval / (1000 * 60 * 60 * 24));
		return intervalday;
	}

	/**
	 * 根据所给的起止时间来计算间隔的月数
	 * 
	 * @param startDate
	 *            起始时间
	 * @param endDate
	 *            结束时间
	 * @return int 返回间隔月数
	 */
	public static int getIntervalMonths(java.sql.Date startDate, java.sql.Date endDate) {
		Calendar startCal = Calendar.getInstance();
		startCal.setTime(startDate);
		Calendar endCal = Calendar.getInstance();
		endCal.setTime(endDate);
		int startDateM = startCal.MONTH;
		int startDateY = startCal.YEAR;
		int enddatem = endCal.MONTH;
		int enddatey = endCal.YEAR;
		int interval = (enddatey * 12 + enddatem) - (startDateY * 12 + startDateM);
		return interval;
	}

	/**
	 * 返回指定格式的当前日期时间字符串
	 * 
	 * @param format
	 * @return
	 */
	public static String getDateTimeStr(String format) {
		String returnStr = null;
		SimpleDateFormat f = new SimpleDateFormat(format);
		Date date = new Date();
		returnStr = f.format(date);
		return returnStr;
	}

	/**
	 * 返回缺省格式的当前日期时间字符串 默认格式:yyyy-mm-dd hh:mm:ss
	 * 
	 * @return String
	 */
	public static String getDateTimeStr() {
		return getDateTimeStr("yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 返回自定义格式的当前日期时间字符串
	 * 
	 * @param format
	 *            格式规则
	 * @return String 返回当前字符串型日期时间
	 */
	public static String getDateStr(String format) {
		String returnStr = null;
		SimpleDateFormat f = new SimpleDateFormat(format);
		Date date = new Date();
		returnStr = f.format(date);
		return returnStr;
	}

	/**
	 * 返回当前日期字符串 缺省格式：yyyy-MM-dd
	 * 
	 * @return String
	 */
	public static String getDateStr() {
		return getDateStr("yyyy-MM-dd");
	}

	/**
	 * 返回当前日期Date对象
	 */
	public static Date getDate() {
		Object obj = WebplusTypeConvert.convert(getDateStr(), "Date", "yyyy-MM-dd");
		if (obj != null)
			return (Date) obj;
		else
			return null;
	}

	/**
	 * 返回当前日期Timestamp对象
	 */
	public static Timestamp getDateTime() {
		Object obj = WebplusTypeConvert.convert(getDateTimeStr(), "Timestamp", "yyyy-MM-dd HH:mm:ss");
		if (obj != null)
			return (Timestamp) obj;
		else
			return null;
	}

	/**
	 * 返回指定格式的字符型日期
	 * 
	 * @param date
	 * @param formatString
	 * @return
	 */
	public static String date2String(Date date, String formatString) {
		if (isEmpty(date)) {
			return null;
		}
		SimpleDateFormat simpledateformat = new SimpleDateFormat(formatString);
		String strDate = simpledateformat.format(date);
		return strDate;
	}

	/**
	 * 将字符串型日期转换为日期型
	 * 
	 * @param strDate
	 *            字符串型日期
	 * @param srcDateFormat
	 *            源日期格式
	 * @param dstDateFormat
	 *            目标日期格式
	 * @return Date 返回的util.Date型日期
	 */
	public static Date stringToDate(String strDate, String srcDateFormat, String dstDateFormat) {
		Date rtDate = null;
		Date tmpDate = (new SimpleDateFormat(srcDateFormat)).parse(strDate, new ParsePosition(0));
		String tmpString = null;
		if (tmpDate != null)
			tmpString = (new SimpleDateFormat(dstDateFormat)).format(tmpDate);
		if (tmpString != null)
			rtDate = (new SimpleDateFormat(dstDateFormat)).parse(tmpString, new ParsePosition(0));
		return rtDate;
	}

	/**
	 * 将字符串型日期转换为日期型
	 * 
	 * @param strDate
	 *            字符串型日期
	 * @param srcDateFormat
	 *            源日期格式
	 * @param dstDateFormat
	 *            目标日期格式
	 * @return Date 返回的util.Date型日期
	 */
	public static String formatStringDate(String strDate, String srcDateFormat, String dstDateFormat) {
		SimpleDateFormat sf = new SimpleDateFormat(srcDateFormat, Locale.CHINA);
		Date date = null;
		try {
			date = sf.parse(strDate);
		} catch (ParseException e1) {

			e1.printStackTrace();
		}
		SimpleDateFormat sf1 = new SimpleDateFormat(dstDateFormat, Locale.CHINA);
		return sf1.format(date);

	}

	/**
	 * 对文件流输出下载的中文文件名进行编码 屏蔽各种浏览器版本的差异性<br>
	 * 
	 * @param agent
	 *            request.getHeader("USER-AGENT");
	 */
	public static String encodeChineseDownloadFileName(String agent, String pFileName) {
		try {
			if (null != agent && -1 != agent.indexOf("MSIE")) {
				pFileName = URLEncoder.encode(pFileName, "utf-8");
			} else {
				pFileName = new String(pFileName.getBytes("utf-8"), "iso8859-1");
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return pFileName;
	}

	/**
	 * 根据日期获取星期
	 * 
	 * @param strdate
	 * @return
	 */
	public static String getWeekDayByDate(String strdate) {
		final String dayNames[] = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
		SimpleDateFormat sdfInput = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		Date date = new Date();
		try {
			date = sdfInput.parse(strdate);
		} catch (ParseException e) {
			throw new RuntimeException("日期类型转换出错", e);
		}
		calendar.setTime(date);
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		if (dayOfWeek < 0)
			dayOfWeek = 0;
		return dayNames[dayOfWeek];
	}

	/**
	 * 区分今天昨天前天
	 * 
	 * @param createTime
	 * @return
	 */
	public static String parseNewDate(String createTime) {
		try {
			String ret = "";
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			long create = sdf.parse(createTime).getTime();
			Calendar now = Calendar.getInstance();
			long ms = 1000
					* (now.get(Calendar.HOUR_OF_DAY) * 3600 + now.get(Calendar.MINUTE) * 60 + now.get(Calendar.SECOND));// 毫秒数
			long ms_now = now.getTimeInMillis();
			if (ms_now - create < ms) {
				ret = "今天";
			} else if (ms_now - create < (ms + 24 * 3600 * 1000)) {
				ret = "昨天";
			} else if (ms_now - create < (ms + 24 * 3600 * 1000 * 2)) {
				ret = "前天";
			} else {
				ret = "更早";
			}
			return ret;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取当前时间（日期＋时间），格式为：yyyy-MM-dd HH:mm:ss
	 * 
	 * @return String
	 */
	public static String getCurrentDate() {
		String dateString = "";
		Calendar calendar = null;
		SimpleDateFormat formatter = null;

		try {
			calendar = new GregorianCalendar();
			formatter = new SimpleDateFormat("yyyy-MM-dd");
			dateString = formatter.format(calendar.getTime());
		} catch (Exception ex) {
		}
		return dateString;
	}

	/**
	 * 获取当前时间（日期＋时间），格式为：yyyy-MM-dd HH:mm:ss
	 * 
	 * @return String
	 */
	public static String getCurrentDateTime() {
		String dateString = "";
		Calendar calendar = null;
		SimpleDateFormat formatter = null;

		try {
			calendar = new GregorianCalendar();
			formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			dateString = formatter.format(calendar.getTime());
		} catch (Exception ex) {
		}
		return dateString;
	}

	/**
	 * 获取时间，格式为：HH:mm:ss
	 * 
	 * @return String
	 */
	public static String getCurrentTime() {
		String dateString = "";
		Calendar calendar = null;
		SimpleDateFormat formatter = null;

		try {
			calendar = new GregorianCalendar();
			formatter = new SimpleDateFormat("HH:mm:ss");
			dateString = formatter.format(calendar.getTime());
		} catch (Exception ex) {
		}
		return dateString;
	}

	/**
	 * 按照指定的格式获得当前日期信息
	 * 
	 * @param dateFormat
	 *            String 日期格式，支持的格式包括： yyyy:4位年 MM:两位月 dd:两位天 HH:两位小时 mm:两位分钟
	 *            ss:两位秒 上述各格式可以进行排列组合以获得更多格式的数据
	 * @return String
	 */
	public static String getCurrentDate(String dateFormat) {
		String dateString = "";
		Calendar calendar = null;
		SimpleDateFormat formatter = null;

		try {
			calendar = new GregorianCalendar();
			formatter = new SimpleDateFormat(dateFormat);
			dateString = formatter.format(calendar.getTime());
		} catch (Exception ex) {
		}
		return dateString;
	}

	/**
	 * 按照指定的格式获得相对当前日期指定偏移量的日期信息
	 * 
	 * @param dateFormat
	 *            String 日期格式，支持的格式包括： yyyy:4位年 MM:两位月 dd:两位天 HH:两位小时 mm:两位分钟
	 *            ss:两位秒 上述各格式可以进行排列组合以获得更多格式的数据
	 * @param offset
	 *            int 日期偏移量，整数型号，0表示当天，正数表示日期增加，负数表示日期减少
	 *            如-2表示取相对当前日期两天前的数据，如当前日期为2006-5-23, 偏移-2后的数据为2006-5-21
	 * @return String
	 */
	public static String getCurrentDate(String dateFormat, int offset) {
		String dateString = "";
		Calendar calendar = null;
		SimpleDateFormat formatter = null;

		try {
			calendar = new GregorianCalendar();
			formatter = new SimpleDateFormat(dateFormat);
			calendar.set(java.util.Calendar.DAY_OF_MONTH, calendar.get(java.util.Calendar.DAY_OF_MONTH) + offset);
			dateString = formatter.format(calendar.getTime());
		} catch (Exception ex) {
		}
		return dateString;
	}

	/**
	 * 按照指定的格式获得相对当前日期指定偏移量的日期信息
	 * 
	 * @param dateFormat
	 *            String 日期格式，支持的格式包括： yyyy:4位年 MM:两位月 dd:两位天 HH:两位小时 mm:两位分钟
	 *            ss:两位秒 上述各格式可以进行排列组合以获得更多格式的数据
	 * @param offset
	 *            int 日期偏移量，整数型号，0表示当天，正数表示日期增加，负数表示日期减少
	 *            如-2表示取相对当前日期两天前的数据，如当前日期为2006-5-23, 偏移-2后的数据为2006-5-21
	 * @return String
	 */
	public static Timestamp getDateTime(int offset) {
		String dateString = "";
		Calendar calendar = null;
		SimpleDateFormat formatter = null;
		try {
			calendar = new GregorianCalendar();
			formatter = new SimpleDateFormat(WebplusCons.DATETIME);
			calendar.set(java.util.Calendar.DAY_OF_MONTH, calendar.get(java.util.Calendar.DAY_OF_MONTH) + offset);
			dateString = formatter.format(calendar.getTime());
			Object obj = WebplusTypeConvert.convert(dateString, "Timestamp", WebplusCons.DATETIME);
			if (obj != null)
				return (Timestamp) obj;
			else
				return null;
		} catch (Exception ex) {
		}
		return null;
	}

	/**
	 * 获取输入日期所在月份的月末日期, 输入日清应为yyyy-mm-dd格式
	 * 
	 * @param this_day
	 *            String 目前的日期
	 * @return String
	 */
	public static String getLastDate(String this_day) {
		String dateString = "";
		SimpleDateFormat formatter = null;

		try {
			Calendar cal = Calendar.getInstance();
			formatter = new SimpleDateFormat("yyyy-MM-dd");
			java.util.Date date = formatter.parse(this_day);
			cal.setTime(date);
			int lastDay = cal.getActualMaximum(cal.DATE);
			dateString = this_day.substring(0, this_day.length() - 2) + lastDay;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return dateString;
	}

	/**
	 * 获取上个月的第一天
	 * 
	 * @param dataFormat日期格式化
	 * @return
	 */
	public static String getLastMonthFisrtDay(String dataFormat) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(dataFormat);
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -1); // 得到前一个月
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		String fisrtDay = dateFormat.format(calendar.getTime());
		return fisrtDay;

	}

	/**
	 * 获取上个月的最后一天
	 * 
	 * @param dataFormat日期格式化
	 * @return
	 */
	public static String getLastMonthLastDay(String dataFormat) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(dataFormat);
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 0);
		String lastDay = dateFormat.format(calendar.getTime());
		return lastDay;

	}
	/**
	 * 获取距离以前月份的第一天
	 * 
	 * @param dataFormat日期格式化
	 * @return
	 */
	public static String getMonthFisrtDay(String dataFormat,int month) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(dataFormat);
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -month); // 得到前一个月
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		String fisrtDay = dateFormat.format(calendar.getTime());
		return fisrtDay;
		
	}
	


	/**
	 * 获取当前月的第一天
	 * 
	 * @param dataFormat日期格式化
	 * @return
	 */
	public static String getCurrentMonthFisrtDay(String dataFormat) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(dataFormat);
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		String fisrtDay = dateFormat.format(calendar.getTime());
		return fisrtDay;

	}

	/**
	 * 获取上个月的最后一天
	 * 
	 * @param dataFormat日期格式化
	 * @return
	 */
	public static String getCurrentMonthLastDay(String dataFormat) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(dataFormat);
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		String lastDay = dateFormat.format(calendar.getTime());
		return lastDay;

	}

	/**
	 * 比较两个日期大小如果返回1说明时间1比时间2大，返回-1是小于，返回0说明等于
	 * 
	 * @param dateTimeOne
	 * @param dateTimeTwo
	 * @return
	 */
	public static int compareDate(String dateTimeOne, String dateTimeTwo) {

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date dateOne = df.parse(dateTimeOne);
			Date dateTwo = df.parse(dateTimeTwo);
			if (dateOne.getTime() > dateTwo.getTime()) {
				return 1;
			} else if (dateOne.getTime() < dateTwo.getTime()) {
				return -1;
			} else {
				return 0;
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return 0;
	}

	/**
	 * 
	 * 简要说明：比较两个时间大小如果返回1说明时间1比时间2大，返回-1是小于，返回0说明等于 编写者：陈骑元 创建时间：2016年9月7日
	 * 上午11:05:25
	 * 
	 * @param 说明
	 * @return 说明
	 */
	public static int compareDateTime(String dateTimeOne, String dateTimeTwo) {

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date dateOne = df.parse(dateTimeOne);
			Date dateTwo = df.parse(dateTimeTwo);
			if (dateOne.getTime() > dateTwo.getTime()) {
				return 1;
			} else if (dateOne.getTime() < dateTwo.getTime()) {
				return -1;
			} else {
				return 0;
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return 0;
	}

	/**
	 * 
	 * 简要说明：计算两个日期相差的天数， 编写者：陈骑元 创建时间：2017年3月23日 下午5:34:16
	 * 
	 * @param 说明
	 *            startDate 起始日期 endDate 结束日期
	 * @return 说明
	 */
	public static int daysBetween(Date startDate, Date endDate) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			startDate = sdf.parse(sdf.format(startDate));
			endDate = sdf.parse(sdf.format(endDate));
			Calendar cal = Calendar.getInstance();
			cal.setTime(startDate);
			long time1 = cal.getTimeInMillis();
			cal.setTime(endDate);
			long time2 = cal.getTimeInMillis();
			long between_days = (time2 - time1) / (1000 * 3600 * 24);
			return Integer.parseInt(String.valueOf(between_days));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 
	 * 简要说明：计算两个日期相差的天数， 编写者：陈骑元 创建时间：2017年3月23日 下午5:34:16
	 * 
	 * @param 说明
	 *            startDate 起始日期 endDate 结束日期
	 * @return 说明
	 */
	public static int minutesBetween(Date startDate, Date endDate) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(WebplusCons.DATETIMEMIN);
			startDate = sdf.parse(sdf.format(startDate));
			endDate = sdf.parse(sdf.format(endDate));
			Calendar cal = Calendar.getInstance();
			cal.setTime(startDate);
			long time1 = cal.getTimeInMillis();
			cal.setTime(endDate);
			long time2 = cal.getTimeInMillis();
			long between_minutes = (time2 - time1) / (1000 * 60);
			return Integer.parseInt(String.valueOf(between_minutes));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 
	 * 简要说明：计算两个人字符串日期相差的天数 编写者：陈骑元 创建时间：2017年3月23日 下午5:35:41
	 * 
	 * @param 说明
	 *            startDate 起始日期 endDate 结束日期
	 * @return 说明
	 */
	public static int daysBetween(String startDate, String endDate) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar cal = Calendar.getInstance();
			cal.setTime(sdf.parse(startDate));
			long time1 = cal.getTimeInMillis();
			cal.setTime(sdf.parse(endDate));
			long time2 = cal.getTimeInMillis();
			long between_days = (time2 - time1) / (1000 * 3600 * 24);
			return Integer.parseInt(String.valueOf(between_days));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 
	 * 简要说明：当前时间相加的天数 编写者：陈骑元 创建时间：2017年3月24日 下午2:52:41
	 * 
	 * @param 说明
	 * @return 说明
	 */
	public static String plusDay(int num, String newDate, String dateFormat) {
		try {
			SimpleDateFormat format = new SimpleDateFormat(dateFormat);
			Date currdate = format.parse(newDate);
			System.out.println("现在的日期是：" + currdate);
			Calendar ca = Calendar.getInstance();
			ca.setTime(currdate);
			ca.add(Calendar.DATE, num);// num为增加的天数，可以改变的
			currdate = ca.getTime();
			String enddate = format.format(currdate);
			return enddate;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 
	 * 简要说明：当前时间相加的天数
	 *  编写者：陈骑元 创建时间：2017年3月24日 下午2:52:41
	 * 
	 * @param 说明
	 * @return 说明
	 */
	public static String plusDay(int num, String newDate) {

		return plusDay(num, newDate, WebplusCons.DATE);
	}

	/**
	 * 
	 * 简要说明：生成随机字符串 编写者：
	 * 陈骑元 创建时间：2017年2月23日 下午2:08:36
	 * 
	 * @param 说明
	 *            randomLength 字符串长度 randomType 随机类型
	 * @return 说明
	 */
	public static String createRandomCode(int randomLength, String randomType) {
		// 参与随机的字符串
		StringBuffer randomSb = new StringBuffer();
		if (WebplusCons.RANDOM_TYPE.FULL.equals(randomType)) {
			randomSb.append(WebplusCons.UPPER_LETTER).append(WebplusCons.LOWCASE_LETTER).append(WebplusCons.NUMBER_LETTER);
		} else if (WebplusCons.RANDOM_TYPE.UPPER.equals(randomType)) {
			randomSb.append(WebplusCons.UPPER_LETTER);
		} else if (WebplusCons.RANDOM_TYPE.LOWCASE.equals(randomType)) {
			randomSb.append(WebplusCons.LOWCASE_LETTER);
		} else if (WebplusCons.RANDOM_TYPE.NUMBER.equals(randomType)) {
			randomSb.append(WebplusCons.NUMBER_LETTER);
		} else if (WebplusCons.RANDOM_TYPE.UPPER_NUMBER.equals(randomType)) {
			randomSb.append(WebplusCons.UPPER_LETTER).append(WebplusCons.NUMBER_LETTER);
		} else if (WebplusCons.RANDOM_TYPE.UPPER_LOWCASE.equals(randomType)) {
			randomSb.append(WebplusCons.UPPER_LETTER).append(WebplusCons.LOWCASE_LETTER);
		} else {
			randomSb.append(WebplusCons.LOWCASE_LETTER).append(WebplusCons.NUMBER_LETTER);
		}

		char[] charSequence = randomSb.toString().toCharArray();
		Random random = new Random();
		StringBuffer randomCode = new StringBuffer();
		// 如果随机长度小于1默认是长度是4
		if (randomLength < 1) {
			randomLength = 4;
		}
		for (int i = 0; i < randomLength; i++) {
			int index = random.nextInt(charSequence.length);
			randomCode.append(charSequence[index]);
		}
		return randomCode.toString();
	}

	/**
	 * JS输出含有\n的特殊处理
	 * 
	 * @param pStr
	 * @return
	 */
	public static String replace4JsOutput(String pStr) {

		pStr = pStr.replace("\r\n", "<br/>&nbsp;&nbsp;");
		pStr = pStr.replace("\t", "&nbsp;&nbsp;&nbsp;&nbsp;");
		pStr = pStr.replace(" ", "&nbsp;");
		return pStr;
	}

	/**
	 * 获取class文件所在绝对路径
	 * 
	 * @param cls
	 * @return
	 * @throws IOException
	 */
	public static String getPathFromClass(Class cls) {
		String path = null;
		if (cls == null) {
			throw new NullPointerException();
		}
		URL url = getClassLocationURL(cls);
		if (url != null) {
			path = url.getPath();
			if ("jar".equalsIgnoreCase(url.getProtocol())) {
				try {
					path = new URL(path).getPath();
				} catch (MalformedURLException e) {
				}
				int location = path.indexOf("!/");
				if (location != -1) {
					path = path.substring(0, location);
				}
			}
			File file = new File(path);
			try {
				path = file.getCanonicalPath();
			} catch (IOException e) {
				throw new RuntimeException("获取class文件所在绝对路径出错", e);
			}
		}
		return path;
	}

	/**
	 * 这个方法可以通过与某个类的class文件的相对路径来获取文件或目录的绝对路径。 通常在程序中很难定位某个相对路径，特别是在B/S应用中。
	 * 通过这个方法，我们可以根据我们程序自身的类文件的位置来定位某个相对路径。
	 * 比如：某个txt文件相对于程序的Test类文件的路径是../../resource/test.txt，
	 * 那么使用本方法Path.getFullPathRelateClass("../../resource/test.txt",Test.class)
	 * 得到的结果是txt文件的在系统中的绝对路径。
	 * 
	 * @param relatedPath
	 *            相对路径
	 * @param cls
	 *            用来定位的类
	 * @return 相对路径所对应的绝对路径
	 * @throws IOException
	 *             因为本方法将查询文件系统，所以可能抛出IO异常
	 */
	public static String getFullPathRelateClass(String relatedPath, Class cls) {
		String path = null;
		if (relatedPath == null) {
			throw new NullPointerException();
		}
		String clsPath = getPathFromClass(cls);
		File clsFile = new File(clsPath);
		String tempPath = clsFile.getParent() + File.separator + relatedPath;
		File file = new File(tempPath);
		try {
			path = file.getCanonicalPath();
		} catch (IOException e) {
			throw new RuntimeException("获取class文件所在绝对路径出错", e);
		}
		return path;
	}

	/**
	 * 获取类的class文件位置的URL
	 * 
	 * @param cls
	 * @return
	 */
	private static URL getClassLocationURL(final Class cls) {
		if (cls == null)
			throw new IllegalArgumentException("null input: cls");
		URL result = null;
		final String clsAsResource = cls.getName().replace('.', '/').concat(".class");
		final ProtectionDomain pd = cls.getProtectionDomain();
		if (pd != null) {
			final CodeSource cs = pd.getCodeSource();
			if (cs != null)
				result = cs.getLocation();
			if (result != null) {
				if ("file".equals(result.getProtocol())) {
					try {
						if (result.toExternalForm().endsWith(".jar") || result.toExternalForm().endsWith(".zip"))
							result = new URL("jar:".concat(result.toExternalForm()).concat("!/").concat(clsAsResource));
						else if (new File(result.getFile()).isDirectory())
							result = new URL(result, clsAsResource);
					} catch (MalformedURLException ignore) {
					}
				}
			}
		}
		if (result == null) {
			final ClassLoader clsLoader = cls.getClassLoader();
			result = clsLoader != null ? clsLoader.getResource(clsAsResource)
					: ClassLoader.getSystemResource(clsAsResource);
		}
		return result;
	}

	/**
	 * 字符串编码转换工具类
	 * 
	 * @param pString
	 * @return
	 */
	public static String getGBK(String pString) {
		if (isEmpty(pString)) {
			return "";
		}
		try {
			pString = new String(pString.getBytes("ISO-8859-1"), "GBK");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("不支持的字符集编码", e);
		}
		return pString;
	}

	/**
	 * 检查当前ClassLoader种,是否存在指定class
	 * 
	 * @param pClass
	 *            类路径
	 * @return
	 */
	public static boolean isExistClass(String pClass) {
		try {
			Class.forName(pClass);
		} catch (ClassNotFoundException e) {
			return false;
		}
		return true;
	}

	/**
	 * 判断是否是IE浏览器
	 * 
	 * @param userAgent
	 *            request.getHeader("USER-AGENT")
	 * @return
	 */
	public static boolean isIE(String userAgent) {
		userAgent = userAgent.toLowerCase();
		return StringUtils.contains(userAgent, "msie");
	}

	/**
	 * 获取IE版本号
	 * 
	 * @param userAgent
	 *            request.getHeader("USER-AGENT")
	 * @return
	 */
	public static String getIeVersion(String userAgent) {
		String ieVersion = "";
		if (isIE(userAgent)) {
			userAgent = WebplusUtil.trimAll(userAgent);
			userAgent = StringUtils.lowerCase(userAgent);
			if (StringUtils.contains(userAgent, "rv:")) {
				// IE11: Mozilla/5.0 (MSIE 9.0; qdesk 2.5.1277.202; Windows NT
				// 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko
				ieVersion = StringUtils.substringBetween(userAgent, "rv:", ".");
			} else {
				// IE6-10: Mozilla / 5.0(compatible; MSIE 6.0-10.0; Windows NT
				// 6.2; Trident / 6.0)
				ieVersion = StringUtils.substringBetween(userAgent, "msie", ".");
			}
		}
		return ieVersion;
	}

	/**
	 * 判断是否是Chrome浏览器
	 * 
	 * @param userAgent
	 *            request.getHeader("USER-AGENT")
	 * @return
	 */
	public static boolean isChrome(String userAgent) {
		userAgent = userAgent.toLowerCase();
		return StringUtils.contains(userAgent, "chrome");
	}

	/**
	 * 判断是否是Firefox浏览器
	 * 
	 * @param userAgent
	 *            request.getHeader("USER-AGENT")
	 * @return
	 */
	public static boolean isFirefox(String userAgent) {
		userAgent = userAgent.toLowerCase();
		return StringUtils.contains(userAgent, "firefox");
	}

	/**
	 * 获取客户端类型
	 * 
	 * @param userAgent
	 * @return
	 */
	public static String getClientExplorerType(HttpServletRequest request) {
		String userAgent = request.getHeader("USER-AGENT").toLowerCase();
		String explorer = "未知浏览器";
		if (isIE(userAgent)) {
			int index = userAgent.indexOf("msie");
			explorer = userAgent.substring(index, index + 8);
		} else if (isChrome(userAgent)) {
			int index = userAgent.indexOf("chrome");
			explorer = userAgent.substring(index, index + 12);
		} else if (isFirefox(userAgent)) {
			int index = userAgent.indexOf("firefox");
			explorer = userAgent.substring(index, index + 11);
		}
		return explorer.toUpperCase();
	}

	/**
	 * 替换空字符串，原生trim只能替换字符串前后
	 * 
	 * @param aString
	 * @return
	 */
	public static String trimAll(String aString) {
		if (WebplusUtil.isEmpty(aString)) {
			return aString;
		}
		return aString.replaceAll(" ", "");
	}

	/**
	 * 打印调试对象
	 */
	public static void debug(Object object) {
		if (log.isDebugEnabled()) {
			System.out.println(object);
		}
	}

	/**
	 * 打印出错信息
	 * 
	 * @return
	 */
	public static void error(Object object) {
		System.out.print(getDateTimeStr() + " ERROR " + object + "\n");
	}

	/**
	 * 打印警告信息
	 * 
	 * @return
	 */
	public static void warn(Object object) {
		System.out.print(getDateTimeStr() + " WARN " + object + "\n");
	}

	/**
	 * 打印提示信息
	 * 
	 * @return
	 */
	public static void info(Object object) {
		System.out.print(getDateTimeStr() + " INFO " + object + "\n");
	}

	/**
	 * 产生[0-9]之间的随机数
	 * 
	 * @return
	 */
	public static int random() {
		return (int) (Math.random() * 10);
	}

	/**
	 * 产生指定范围[min-max]之间的随机数
	 * 
	 * @return
	 */
	public static long randomBetween(long min, long max) {
		return Math.round(Math.random() * (max - min) + min);
	}

	/**
	 * 产生随机简体汉字
	 * 
	 * @return
	 */
	public static String randomSimplified() {
		String outCharacter = null;
		int hightPos, lowPos;
		Random random = new Random();
		hightPos = (176 + Math.abs(random.nextInt(39)));
		lowPos = (161 + Math.abs(random.nextInt(93)));
		byte[] b = new byte[2];
		b[0] = (new Integer(hightPos).byteValue());
		b[1] = (new Integer(lowPos).byteValue());
		try {
			outCharacter = new String(b, "GBK");
		} catch (UnsupportedEncodingException ex) {
			ex.printStackTrace();
		}
		return outCharacter;
	}

	/**
	 * 将List<Dto>对象的Dto的key转换为小写
	 * 
	 * <p>
	 * 因为调用存储过程返回的游标结果集中的Dto的Key大小写问题不能被拦截器统一转换，所以在这里做一次转换后返回。
	 * 
	 * @return
	 */
	public static List<Dto> lowercaseDtos(List<Dto> list) {
		if (list == null) {
			return ListUtils.EMPTY_LIST;
		}
		List<Dto> lowerDtos = Lists.newArrayList();
		for (Dto dto : list) {
			Dto lowerDto = Dtos.newDto();
			Iterator<String> keyIterator = (Iterator) dto.keySet().iterator();
			while (keyIterator.hasNext()) {
				String key = (String) keyIterator.next();
				lowerDto.put(StringUtils.lowerCase(key), dto.get(key));
			}
			lowerDtos.add(lowerDto);
		}
		return lowerDtos;
	}

	/**
	 * 获取主机名
	 * 
	 * @return
	 */
	public static String getHostName() {
		String hostName = null;
		InetAddress inetAddress = null;
		try {
			inetAddress = InetAddress.getLocalHost();
			hostName = inetAddress.getHostName();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return hostName;
	}

	/**
	 * 获取主机IP
	 * 
	 * @return
	 */
	public static String getHostAddress() {
		String hostAddress = null;
		InetAddress inetAddress = null;
		try {
			inetAddress = InetAddress.getLocalHost();
			hostAddress = inetAddress.getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return hostAddress;
	}


	/**
	 * 
	 * 简要说明：把首字母转换为小写 编写者：陈骑元 创建时间：2016年7月19日 下午11:49:45
	 * 
	 * @param 说明
	 * @return 说明
	 */
	public static String firstToLetter(String str) {
		if (isNotEmpty(str)) {
			char[] array = str.toCharArray();

			if (array[0] >= 'A' && array[0] <= 'Z') {
				array[0] += 32;
			}
			return String.valueOf(array);
		}
		return "";

	}

	/**
	 * Hex解码.
	 * 
	 * @throws DecoderException
	 */
	public static final byte[] decodeHex(String input) throws DecoderException {
		if (isNotEmpty(input)) {
			return Hex.decodeHex(input.toCharArray());
		}
		return new byte[] {};
	}

	/**
	 * 16进制编码.
	 */
	public static final String encodeHex(byte[] input) {
		return Hex.encodeHexString(input);
	}

	/**
	 * 使用不可见字符串WebplusCons.MARK_CSV将字符串隔开，重新组成一串字符串
	 * 
	 * @param strs
	 * @return
	 */
	public static final String joinStringWithUnseeChar(Collection<String> strs) {
		return joinStringWithSplitor(strs, WebplusCons.MARK_CSV);
	}

	/**
	 * 用任何分隔符将字符串容器中的字符串组成一串字符串。
	 * 
	 * @param strs
	 * @param splitor
	 * @return
	 */
	public static final String joinStringWithSplitor(Collection<String> strs, String splitor) {
		if (isEmpty(strs)) {
			return "";
		}
		StringBuffer res = new StringBuffer("");
		for (String s : strs) {
			res.append(s + splitor);
		}
		if (isNotEmpty(res.toString())) {
			return res.toString().substring(0, res.toString().length() - 1);
		}
		return res.toString();
	}

	/**
	 * 
	 * 简要说明：替换指定位置的字符 编写者：陈骑元 创建时间：2017年3月11日 下午10:13:53
	 * 
	 * @param 说明
	 * @return 说明
	 */
	public static String replaceString(String sourceStr, String str, int index) {
		if (isEmpty(sourceStr) || isEmpty(str)) {
			throw new RuntimeException("源字符串不能为空或替换的字符不能为空");
		}
		int len = sourceStr.length();

		char[] array = sourceStr.toCharArray();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < array.length; i++) {
			if (i == index - 1) {
				sb.append(str);
			} else {
				sb.append(array[i]);
			}
		}
		return sb.toString();
	}

	/**
	 * 
	 * 简要说明：替换字符串的字符 编写者：陈骑元 创建时间：2017年5月18日 下午5:35:40
	 * 
	 * @param 说明
	 *            sourceStr 原字符串 replaceStr 要替换字符串 start 起始位置 end结束位置
	 * @return 说明
	 */
	public static String repalceString(String sourceStr, String replaceStr, int start, int end) {
		if (isEmpty(sourceStr) || isEmpty(replaceStr)) {
			throw new RuntimeException("源字符串不能为空或替换的字符不能为空");
		}
		int len = sourceStr.length();
		if (start > len) {

			throw new RuntimeException("替换起始位置不能大于源字符串的长度");
		}
		if (start > end) {
			throw new RuntimeException("替换结束位置必须大于起始位置");
		}
		StringBuffer sb = new StringBuffer(sourceStr);
		String returnStr = sb.replace(start, end, replaceStr).toString();
		return returnStr;

	}

	/**
	 * 
	 * 简要说明：提取字符串数字 编写者：陈骑元 创建时间：2017年6月17日 下午3:02:11
	 * 
	 * @param 说明
	 * @return 说明
	 */
	public static String getNumber(String str) {
		Pattern pattern = Pattern.compile("\\d+");
		Matcher matcher = pattern.matcher(str);
		while (matcher.find()) {
			return matcher.group(0);
		}
		return "";
	}

	
    /**
	 * 生成树路径ID，如：01.01.01
	 * 
	 * @param curMaxNode
	 *            本级当前最大节点ID，如果要生成本级第一个节点则传XX.XX.00(XX.XX为父节点ID)。
	 * @param maxValue
	 *            本级节点ID允许的最大值
	 * @return
	 */
	public static String createCascadeId(String curMaxNode, int maxValue) {
		String prefix = StringUtils.substringBeforeLast(curMaxNode, ".");
		String last = StringUtils.substringAfterLast(curMaxNode, ".");
		if (WebplusUtil.isEmpty(last)) {
			throw new RuntimeException("ID生成器生成节点ID参数错误");
		}
		int intLast = Integer.valueOf(last);
		if (intLast == maxValue || intLast > maxValue) {
			throw new RuntimeException("树ID生成器本级节点号源用尽");
		}
		String thisNode = String.valueOf(intLast + 1);
		thisNode = StringUtils.leftPad(thisNode, String.valueOf(maxValue).length(), "0");
		return prefix + "." + thisNode;
	}
	
	/**
	 * 
	 * 简要说明：检查字符是否包含在结合中
	 * 编写者：陈骑元 
	 * 创建时间：2018年12月19日 下午2:52:28
	 * @param 说明  str字符串 separateStr分隔字符串
	 * @return 说明 包含返回true 否则是false
	 */
	public static boolean contains(String str,String separateStr){
		if(WebplusUtil.isNotAnyEmpty(str,separateStr)){
			List<String> strList=WebplusFormater.separatStringToList(separateStr);
			return contains(str,strList);
		
		}
		return false;
	}
	/**
	 * 
	 * 简要说明：检查字符是否包含在结合中
	 * 编写者：陈骑元
	 * 创建时间：2018年12月19日 下午2:52:28
	 * @param 说明 包含返回true 否则是false
	 * @return 说明
	 */
	public static boolean contains(String str,List<String> strList){
		if(WebplusUtil.isNotAnyEmpty(str,strList)){
			
			return strList.contains(str);
		}
		return false;
	}
	
	/**
	 * 
	 * 简要说明：去除list集合中重复数据
	 * 编写者：陈骑元
	 * 创建时间：2018年12月27日 下午12:24:38
	 * @param 说明
	 * @return 说明
	 */
	public static List removeRepeat(List dataList){
		if(WebplusUtil.isNotEmpty(dataList)){
			 List tempList = new ArrayList();  
		        for(int i=0;i<dataList.size();i++){  
		            if(!tempList.contains(dataList.get(i))){  
		            	tempList.add(dataList.get(i));  
		            }  
		        }  
		
		     return tempList;
		}
		return Lists.newArrayList();
	}
	
	/**
	 * 将Request请求参数封装为Dto对象
	 * 
	 * @param request
	 * @return
	 */
	@SuppressWarnings("all")
	public static Dto getParamAsDto(HttpServletRequest request) {
		Dto dto = new HashDto();
		Map<String, String[]> map = request.getParameterMap();
		Iterator<String> keyIterator = (Iterator) map.keySet().iterator();
		while (keyIterator.hasNext()) {
			String key = (String) keyIterator.next();
			String value = "";
			if(map.get(key).length<=1){
				value=map.get(key)[0];
			}else{
				value=StringUtils.join(map.get(key), ",");
			}
			dto.put(key, value);
		}
		
		return dto;
	}
	/**
	 * 向Response流输出业务数据字符串
	 * <p/>
	 * 如果传入的参数是一个Json字符串则直接输出。若为一个常规字符串则将其自动作为Key为appmsg的json属性值输出。(缺省追加参数：
	 * appcode=1 success=true)
	 * 
	 * @param response
	 * @param outString
	 *            输出符合Json格式的业务数据字符串 / 提示信息常规字符串
	 */
	public static void writeObject(HttpServletResponse response,Object outObject) {
		
		String outString=WebplusJson.toJson(outObject);
		writeJson(response,outString);
	}
	/**
	 * 向Response流输出业务数据字符串
	 * <p/>
	 * 如果传入的参数是一个Json字符串则直接输出。若为一个常规字符串则将其自动作为Key为appmsg的json属性值输出。(缺省追加参数：
	 * appcode=1 success=true)
	 * 
	 * @param response
	 * @param outString
	 *            输出符合Json格式的业务数据字符串 / 提示信息常规字符串
	 */
	public static void writeJson(HttpServletResponse response, String outString) {
		
	
		try {
			response.setContentType("application/json;charset=UTF-8");
			response.setCharacterEncoding("UTF-8");
			PrintWriter writer = response.getWriter();
			writer.write(outString);
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 向Response流输出字符串
	 * <p/>
	 * <p>
	 * 输出字符串不做任何加工，原样输出。
	 * 
	 * @param response
	 * @param outString
	 *            输出字符串
	 */
	public static void writeRaw(HttpServletResponse response, String outString) {
		response.setCharacterEncoding("utf-8");
		response.setHeader("Content-type", "text/html;charset=UTF-8");
		try {
			response.getWriter().write(outString == null ? "" : outString);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 返回去除连接符-的UUID
	 * 
	 * @return
	 */
	public static String uuid() {
		String uuid = uuid2();
		return uuid.replaceAll("-", "");
	}
	
	/**
	 * 返回原生UUID
	 * 
	 * @return
	 */
	public static String uuid2() {
		return UUID.randomUUID().toString();
	}
    /**
     * 
     * 简要说明：创建文件编号
     * 编写者：陈骑元
     * 创建时间：2019年7月21日 下午11:32:47
     * @param 说明 prefix 前端
     * @return 说明
     */
	public static String createFileId(String prefix){
		
		String fileId=prefix+WebplusUtil.getCurrentDate("yyyyMMddHHmmssSSS");
		
		return fileId;
	}
	/**
	 * 
	 * 简要说明：格式化金额
	 * 编写者：陈骑元
	 * 创建时间：2019年7月23日 下午12:19:45
	 * @param 说明
	 * @return 说明
	 */
	public static String formatAmount(Integer amount){
		DecimalFormat decimalFormat = new DecimalFormat("###,###");
	    return decimalFormat.format(amount);
	}
	/**
	 * 
	 * 简要说明：根据key值获取Dto集合对象的值
	 * 编写者：陈骑元
	 * 创建时间：2020年1月13日 下午9:44:56
	 * @param 说明
	 * @return 说明
	 */
	public static List<String> getListDtoByKey(List<Dto> dataList,String key){
		List<String> strList=Lists.newArrayList();
		if(WebplusUtil.isNotAnyEmpty(dataList,key)){
			for(Dto dataDto:dataList){
				String value=dataDto.getString(key);
				if(WebplusUtil.isNotEmpty(value)){
					strList.add(value);
				}
			}
		}
		
		return strList;
	}
	/**
	 * 
	 * 简要说明：检查集合是否存在这个值
	 * 编写者：陈骑元（chenqiyuan@toonan.com）
	 * 创建时间： 2020年7月20日 下午11:01:45 
	 * @param 说明
	 * @return 说明
	 */
	public static boolean checkExistByList(String str,List<String> strList) {
		if(WebplusUtil.isNotEmpty(strList)) {
			for(String strTmp:strList) {
				if(strTmp.equals(str)) {
					return true;
				}
			}
		}
		return false;
	}
	/**
	 * 将JavaBean对象中的属性值拷贝到Dto对象
	 * 
	 * @param pFromObj
	 *            JavaBean对象源
	 * @param pToDto
	 *            Dto目标对象
	 */
	public static List<Dto> copyPropertiesList(List<?> objList) {
		List<Dto> dataList=Lists.newArrayList();
		if(WebplusUtil.isNotEmpty(objList)){
			
			for(int i=0;i<objList.size();i++){
				Object obj=objList.get(i);
				Dto dataDto=Dtos.newDto();
				WebplusUtil.copyProperties(obj, dataDto);
				if(obj instanceof BaseModel){
			        	dataDto.putAll(((BaseModel) obj).getExtMap());
			    }
				dataDto.put("seqNum", i+1);
				dataList.add(dataDto);
			}
		}
		return dataList;
	}
		
	/**
	 * 距离现在现在多少秒
	 * @param passTimeStr
	 * @return
	 */
    public static long plusSecond(String passTimeStr) {
       Date psssTime=WebplusUtil.stringToDate(passTimeStr, WebplusCons.DATETIME, WebplusCons.DATETIME);
       Date nowTime=WebplusUtil.getDateTime();
       long diff =nowTime.getTime() - psssTime.getTime();// 这样得到的差值是微秒级别
       
       return diff/1000;
    }
    /**
	 * 
	 * 简要说明：检查字符串是否存在集合里面
	 * 编写者：陈骑元（chenqiyuan@toonan.com）
	 * 创建时间： 2020年12月31日 下午3:19:08 
	 * @param 说明
	 * @return 说明
	 */
	public static boolean checkStrExistInList(String str,List<String> strList) {
		  if(WebplusUtil.isNotAnyEmpty(str,strList)) {
			  for(String strTmp:strList) {
				   if(str.equals(strTmp)) {
					   
					   return true;
				   }
			  }
		  }
		  return false;
	}
    /** 
   	 * 简要说明：检查字符串是否存在
   	 * 编写者：陈骑元（chenqiyuan@toonan.com）
   	 * 创建时间： 2020年12月31日 下午3:19:08 
   	 * @param 说明
   	 * @return 说明
   	 */
   	public static boolean checkStrExistInList(String str,String separateStr) {
   		  List<String> strList=WebplusFormater.separatStringToList(separateStr);
   		  return checkStrExistInList(str,strList);
   	}
   	
   	 /** 
   		 * 简要说明：检查字符串是否存在
   		 * 编写者：陈骑元（chenqiyuan@toonan.com）
   		 * 创建时间： 2020年12月31日 下午3:19:08 
   		 * @param 说明
   		 * @return 说明
   		 */
   	public static boolean checkStrExistInList(String str,List<?> dataList,String key) {
   		   if(WebplusUtil.isNotAnyEmpty(str,dataList,key)) {
   			   for(Object obj:dataList){
   					Dto dataDto=Dtos.newDto();
   					if(obj instanceof Map) {
   						dataDto.putAll((Map)obj);
   					}else {
   						WebplusUtil.copyProperties(obj, dataDto);
   					}
   					if(dataDto.containsKey(key)) {
   						String value=dataDto.getString(key);
   						if(str.equals(value)) {
   							
   							return true;
   						}
   					}
   					
   				}
   		   }
   		   return false;
   			 
   	}
	/**
	 * 测试
	 * 
	 * @param args
	 * @throws ParseException
	 */
	public static void main(String args[]) throws ParseException {
	       
	    System.out.println(WebplusUtil.getCurrentDate(WebplusCons.DATE,365*2));
	}
}