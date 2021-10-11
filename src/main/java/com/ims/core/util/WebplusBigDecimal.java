package com.ims.core.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.regex.Pattern;

/**
 * <p>
 * BigDecimal 常用计算工具类
 * </p>
 *
 * @author jobob
 * @since 2018-10-07
 */
public class WebplusBigDecimal {
   
	
	// 默认运算精度
	private static int DEF_SCALE = 10;

    /**
     * <p>
     * 提供精确的加法运算。
     * </p>
     *
     * @param v1 被加数
     * @param v2 加数
     * @return 两个参数的和
     */

    public static BigDecimal add(BigDecimal v1, BigDecimal v2) {
        if (null == v1) {
            v1 = BigDecimal.ZERO;
        }
        if (null == v2) {
            v2 = BigDecimal.ZERO;
        }
        return v1.add(v2);
    }


    /**
     * <p>
     * 提供精确的减法运算。
     * </p>
     *
     * @param v1 被减数
     * @param v2 减数
     * @return 两个参数的差
     */

    public static BigDecimal sub(BigDecimal v1, BigDecimal v2) {
        if (null == v1) {
            v1 = BigDecimal.ZERO;
        }
        if (null == v2) {
            v2 = BigDecimal.ZERO;
        }
        return v1.subtract(v2);
    }


    /**
     * <p>
     * 提供精确的乘法运算。
     * </p>
     *
     * @param v1 被乘数
     * @param v2 乘数
     * @return 两个参数的积
     */
    public static BigDecimal mul(BigDecimal v1, BigDecimal v2) {
        if (null == v1) {
            v1 = BigDecimal.ONE;
        }
        if (null == v2) {
            v2 = BigDecimal.ONE;
        }
        return v1.multiply(v2);
    }


    /**
     * <p>
     * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到
     * 小数点以后10位，以后的数字四舍五入。
     * </p>
     *
     * @param v1 被除数
     * @param v2 除数
     * @return 两个参数的商
     */

    public static BigDecimal div(BigDecimal v1, BigDecimal v2) {
        return div(v1, v2, 10);
    }


    /**
     * <p>
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指
     * 定精度，以后的数字四舍五入。
     * </p>
     *
     * @param v1    被除数
     * @param v2    除数
     * @param scale 表示表示需要精确到小数点以后几位。
     * @return 两个参数的商
     */
    public static BigDecimal div(BigDecimal v1, BigDecimal v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        if (null == v1) {
            v1 = BigDecimal.ONE;
        }
        if (null == v2) {
            v2 = BigDecimal.ONE;
        }
        return v1.divide(v2, scale, BigDecimal.ROUND_HALF_UP);
    }


    /**
     * <p>
     * 提供精确的小数位四舍五入处理。
     * </p>
     *
     * @param v     需要四舍五入的数字
     * @param scale 小数点后保留几位
     * @return 四舍五入后的结果
     */
    public static BigDecimal round(BigDecimal v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        if (null == v) {
            v = BigDecimal.ZERO;
        }
        return v.divide(BigDecimal.ZERO, scale, BigDecimal.ROUND_HALF_UP);
    }


    /**
     * <p>
     * 比较 v1 是否等于 v2
     * </p>
     *
     * @param v1 值 1
     * @param v2 值 2
     * @return
     */
    public static boolean equalThan(BigDecimal v1, BigDecimal v2) {
        if (null == v1 || null == v2) {
            return false;
        }
        return v1.compareTo(v2) == 0;
    }


    /**
     * <p>
     * 比较 v1 是否小于 v2
     * </p>
     *
     * @param v1 值 1
     * @param v2 值 2
     * @return
     */
    public static boolean lessThan(BigDecimal v1, BigDecimal v2) {
        if (null == v1 || null == v2) {
            return false;
        }
        return v1.compareTo(v2) == -1;
    }


    /**
     * <p>
     * 比较 v1 是否小于等于 v2
     * </p>
     *
     * @param v1 值 1
     * @param v2 值 2
     * @return
     */
    public static boolean lessEqThan(BigDecimal v1, BigDecimal v2) {
        if (null == v1 || null == v2) {
            return false;
        }
        return v1.compareTo(v2) <= 0;
    }


    /**
     * <p>
     * 比较 v1 是否大于 v2
     * </p>
     *
     * @param v1 值 1
     * @param v2 值 2
     * @return
     */
    public static boolean greaterThan(BigDecimal v1, BigDecimal v2) {
        if (null == v1 || null == v2) {
            return false;
        }
        return v1.compareTo(v2) == 1;
    }


    /**
     * <p>
     * 比较 v1 是否大于等于 v2
     * </p>
     *
     * @param v1 值 1
     * @param v2 值 2
     * @return
     */
    public static boolean greaterEqThan(BigDecimal v1, BigDecimal v2) {
        if (null == v1 || null == v2) {
            return false;
        }
        return v1.compareTo(v2) >= 0;
    }
    


 	/**
 	 * 提供数据类型转换为BigDecimal
 	 * 
 	 * @param object 原始数据
 	 * @return BigDecimal
 	 */
 	public static final BigDecimal bigDecimal(Object object) {
 		if (object == null) {
 			throw new NullPointerException();
 		}
 		BigDecimal result;
 		try {
 			result = new BigDecimal(String.valueOf(object).replaceAll(",", ""));
 		} catch (NumberFormatException e) {
 			throw new NumberFormatException("Please give me a numeral.Not " + object);
 		}
 		return result;
 	}

 	/**
 	 * 提供(相对)精确的加法运算。
 	 * 
 	 * @param num1 被加数
 	 * @param num2 加数
 	 * @return 两个参数的和
 	 */
 	public static final Double add(Object num1, Object num2) {
 		BigDecimal result = bigDecimal(num1).add(bigDecimal(num2));
 		return result.setScale(DEF_SCALE, BigDecimal.ROUND_HALF_UP).doubleValue();
 	}

 	/**
 	 * 提供(相对)精确的减法运算。
 	 * 
 	 * @param num1 被减数
 	 * @param num2 减数
 	 * @return 两个参数的差
 	 */
 	public static final Double subtract(Object num1, Object num2) {
 		BigDecimal result = bigDecimal(num1).subtract(bigDecimal(num2));
 		return result.setScale(DEF_SCALE, BigDecimal.ROUND_HALF_UP).doubleValue();
 	}

 	/**
 	 * 提供(相对)精确的乘法运算。
 	 * 
 	 * @param num1 被乘数
 	 * @param num2 乘数
 	 * @return 两个参数的积
 	 */
 	public static final Double multiply(Object num1, Object num2) {
 		BigDecimal result = bigDecimal(num1).multiply(bigDecimal(num2));
 		return result.setScale(DEF_SCALE, BigDecimal.ROUND_HALF_UP).doubleValue();
 	}

 	/**
 	 * 提供(相对)精确的除法运算，当发生除不尽的情况时，精度为10位，以后的数字四舍五入。
 	 * 
 	 * @param num1 被除数
 	 * @param num2 除数
 	 * @return 两个参数的商
 	 */
 	public static final Double divide(Object num1, Object num2) {
 		return divide(num1, num2, DEF_SCALE);
 	}

 	/**
 	 * 提供(相对)精确的除法运算。 当发生除不尽的情况时，由scale参数指定精度，以后的数字四舍五入。
 	 * 
 	 * @param num1 被除数
 	 * @param num2 除数
 	 * @param scale 表示表示需要精确到小数点以后几位。
 	 * @return 两个参数的商
 	 */
 	public static final Double divide(Object num1, Object num2, Integer scale) {
 		if (scale == null) {
 			scale = DEF_SCALE;
 		}
 		num2 = num2 == null || Math.abs(new Double(num2.toString())) == 0 ? 1 : num2;
 		if (scale < 0) {
 			throw new IllegalArgumentException("The scale must be a positive integer or zero");
 		}
 		BigDecimal result = bigDecimal(num1).divide(bigDecimal(num2), scale, BigDecimal.ROUND_HALF_UP);
 		return result.doubleValue();
 	}

 	/**
 	 * 提供精确的小数位四舍五入处理。
 	 * 
 	 * @param num 需要四舍五入的数字
 	 * @param scale 小数点后保留几位
 	 * @return 四舍五入后的结果
 	 */
 	public static final Double round(Object num, int scale) {
 		if (scale < 0) {
 			throw new IllegalArgumentException("The scale must be a positive integer or zero");
 		}
 		BigDecimal result = bigDecimal(num).divide(bigDecimal("1"), scale, BigDecimal.ROUND_HALF_UP);
 		return result.doubleValue();
 	}

 	/**
 	 * 获取start到end区间的随机数,不包含start+end
 	 * 
 	 * @param start
 	 * @param end
 	 * @return
 	 */
 	public static final BigDecimal getRandom(int start, int end) {
 		return new BigDecimal(start + Math.random() * end);
 	}

 	/**
 	 * 格式化
 	 * 
 	 * @param obj
 	 * @param pattern
 	 * @return
 	 */
 	public static final String format(Object obj, String pattern) {
 		if (obj == null) {
 			return null;
 		}
 		if (pattern == null || "".equals(pattern)) {
 			pattern = "#";
 		}
 		DecimalFormat format = new DecimalFormat(pattern);
 		return format.format(bigDecimal(obj));
 	}

 	/** 是否数字 */
 	public static final boolean isNumber(Object object) {
 		Pattern pattern = Pattern.compile("\\d+(.\\d+)?$");
 		return pattern.matcher(object.toString()).matches();
 	}
}
