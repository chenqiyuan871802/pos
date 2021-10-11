package com.ims.core.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * 类名:com.toonan.util.WebplusRegex
 * 描述:正则表达式处理
 * 编写者:陈骑元
 * 创建时间:2019年4月25日 下午6:36:03
 * 修改说明:
 */
public class WebplusRegex {

    /**
     * 正则常量
     */
    public static final String PASSWORD = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,20}$";
    public static final String EMAIL = "^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*\\.[a-zA-Z0-9]{2,6}$";
    public static final String ID_CARD = "[1-9]\\d{13,16}[a-zA-Z0-9]{1}";
    public static final String MOBILE = "^(13[0-9]|14[5-9]|15[012356789]|166|17[0-8]|18[0-9]|19[8-9])[0-9]{8}$";
    public static final String PHONE = "(\\+\\d+)?(\\d{3,4}\\-?)?\\d{7,8}$";
    public static final String DIGIT = "\\-?[1-9]\\d+";
    public static final String DECIMALS = "\\-?[1-9]\\d+(\\.\\d+)?";
    public static final String CHARACTER = "^[A-Za-z]+$";
    public static final String BLANK_SPACE = "\\s+";
    public static final String CHINESE = "^[\u4E00-\u9FA5]+$";
    public static final String BIRTHDAY = "[1-9]{4}([-./])\\d{1,2}\\1\\d{1,2}";
    public static final String URL = "(https?://(w{3}\\.)?)?\\w+\\.\\w+(\\.[a-zA-Z]+)*(:\\d{1,5})?(/\\w*)*(\\??(.+=.*)?(&.+=.*)?)?";
    public static final String POST_CODE = "[1-9]\\d{5}";
    public static final String IP_ADDRESS = "[1-9](\\d{1,2})?\\.(0|([1-9](\\d{1,2})?))\\.(0|([1-9](\\d{1,2})?))\\.(0|([1-9](\\d{1,2})?))";
    public static final String NUMBER = "^([+|-])?([1-9]\\d*\\.\\d*|0\\.\\d+|[1-9]\\d*|0)$";
    public static final String ENGLISH = "^[a-zA-Z]+$";
    public static final String JP_MOBILE = "0?\\d{10}$";


    /**
     * <p>
     * 验证 password 是否为字母数字混合 6 - 20 位长度密码
     * </p>
     *
     * @param password 密码
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean isPassword(String password) {
        return matches(PASSWORD, password);
    }

    /**
     * <p>
     * 验证Email
     * </p>
     *
     * @param email email地址，格式：zhangsan@zuidaima.com，zhangsan@xxx.com.cn，xxx代表邮件服务商
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean isEmail(String email) {
        return matches(EMAIL, email);
    }

    /**
     * <p>
     * 验证身份证号码
     * </p>
     *
     * @param idCard 居民身份证号码15位或18位，最后一位可能是数字或字母
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean isIdCard(String idCard) {
        return matches(ID_CARD, idCard);
    }
    
    
    /**
     * <p>
     * 验证手机号码
     * </p>
     *
     * @param mobile 移动、联通、电信运营商的号码段
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean isJapanMobile(String mobile) {
        return matches(JP_MOBILE, mobile);
    }
    /**
     * <p>
     * 验证手机号码
     * </p>
     *
     * @param mobile 移动、联通、电信运营商的号码段
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean isMobile(String mobile) {
        return matches(MOBILE, mobile);
    }

    /**
     * <p>
     * 验证固定电话号码
     * </p>
     *
     * @param phone 电话号码，格式：国家（地区）电话代码 + 区号（城市代码） + 电话号码，如：+8602085588447
     *              <p><b>国家（地区） 代码 ：</b>标识电话号码的国家（地区）的标准国家（地区）代码。它包含从 0 到 9 的一位或多位数字，
     *              数字之后是空格分隔的国家（地区）代码。</p>
     *              <p><b>区号（城市代码）：</b>这可能包含一个或多个从 0 到 9 的数字，地区或城市代码放在圆括号——
     *              对不使用地区或城市代码的国家（地区），则省略该组件。</p>
     *              <p><b>电话号码：</b>这包含从 0 到 9 的一个或多个数字 </p>
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean isPhone(String phone) {
        return matches(PHONE, phone);
    }

    /**
     * <p>
     * 验证整数（正整数和负整数）
     * </p>
     *
     * @param digit 一位或多位0-9之间的整数
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean isDigit(String digit) {
        return matches(DIGIT, digit);
    }

    /**
     * <p>
     * 验证整数和浮点数（正负整数和正负浮点数）
     * </p>
     *
     * @param decimals 一位或多位0-9之间的浮点数，如：1.23，233.30
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean isDecimals(String decimals) {
        return matches(DECIMALS, decimals);
    }

    /**
     * <p>
     * 验证空白字符
     * </p>
     *
     * @param blankSpace 空白字符，包括：空格、\t、\n、\r、\f、\x0B
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean isBlankSpace(String blankSpace) {
        return matches(BLANK_SPACE, blankSpace);
    }

    /**
     * <p>
     * 验证中文
     * </p>
     *
     * @param chinese 中文字符
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean isChinese(String chinese) {
        return matches(CHINESE, chinese);
    }

    /**
     * <p>
     * 验证日期（年月日）
     * </p>
     *
     * @param birthday 日期，格式：1992-09-03，或1992.09.03
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean isBirthday(String birthday) {
        return matches(BIRTHDAY, birthday);
    }

    /**
     * <p>
     * 验证URL地址
     * </p>
     *
     * @param url 格式：http://blog.csdn.net:80/xyang81/article/details/7705960? 或 http://www.csdn.net:80
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean isURL(String url) {
        return matches(URL, url);
    }

    /**
     * <p>
     * 获取网址 URL 的一级域
     * </p>
     *
     * @param url
     * @return
     */
    public static String getSubDomain(String url) {
        Pattern p = Pattern.compile("(?<=http://|\\.)[^.]*?\\.(com|cn|net|org|biz|info|cc|tv)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = p.matcher(url);
        matcher.find();
        return matcher.group();
    }

    /**
     * <p>
     * 获取网址 URL 域名
     * </p>
     *
     * @param url
     * @return
     */
    public static String getDomain(String url) {
        Pattern p = Pattern.compile("[^//]*?\\.(com|cn|net|org|biz|info|cc|tv)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = p.matcher(url);
        matcher.find();
        return matcher.group();
    }

    /**
     * <p>
     * 匹配中国邮政编码
     * </p>
     *
     * @param postcode 邮政编码
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean isPostcode(String postcode) {
        return matches(POST_CODE, postcode);
    }

    /**
     * <p>
     * 匹配IP地址(简单匹配，格式，如：192.168.1.1，127.0.0.1，没有匹配IP段的大小)
     * </p>
     *
     * @param ipAddress IPv4标准地址
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean isIpAddress(String ipAddress) {
        return matches(IP_ADDRESS, ipAddress);
    }

    /**
     * <p>
     * 匹配 input 字符串是否全为字母
     * </p>
     *
     * @param input 字符串
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean isCharacter(String input) {
        return matches(CHARACTER, input);
    }

    /**
     * <p>
     * 正则匹配
     * </p>
     *
     * @param regex 正则
     * @param input 字符串
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean matches(String regex, CharSequence input) {
        if (null == input) {
            return false;
        }
        return Pattern.matches(regex, input);
    }

    /**
     * <p>
     * 正则匹配是否数字
     * </p>
     */
    public static boolean isNumber(String number) {
        return matches(NUMBER, number);
    }

    /**
     * <p>
     * 正则匹配是纯英文
     * </p>
     */
    public static boolean isEnglish(String english) {
        return matches(ENGLISH, english);
    }
    /**
     * 
     * 简要说明：验证字符串是否包含中文
     * 编写者：陈骑元
     * 创建时间：2018年12月30日 上午12:01:51
     * @param 说明
     * @return 说明
     */
    public static boolean isContainChinese(String str) {
    	 Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
    	 Matcher m = p.matcher(str);
    	 if (m.find()) {
    	  return true;
    	 }
    	 return false;
    }
   public static void main(String[] args){
	   String mobile="09019448683";
	   if(mobile.startsWith("0")){
		   mobile=mobile.substring(1);
	   }
	   System.out.println(mobile);
   }
}
