package com.ims.core.pay;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.commons.codec.Charsets;
import org.apache.commons.lang3.StringUtils;

import com.ims.core.util.WebplusHashCodec;
import com.ims.core.util.WebplusUtil;
import com.ims.core.util.WebplusXml;



/**
 * 
 * 类名:com.ims.common.support.pay.WxPayment
 * 描述:微信参数支付处理
 * 编写者:陈骑元
 * 创建时间:2017年11月2日 下午4:42:37
 * 修改说明:
 */

public class WxPayment {
	/**
	 * 构建参数
	 * 
	 * @param appid
	 * @param sub_appid
	 * @param mch_id
	 * @param sub_mch_id
	 * @param device_info
	 * @param body
	 * @param detail
	 * @param attach
	 * @param out_trade_no
	 * @param total_fee
	 * @param spbill_create_ip
	 * @param auth_code
	 * @param paternerKey
	 * @return
	 */
	public static Map<String, String> buildParasMap(String appid, String sub_appid, String mch_id, String sub_mch_id,
			String device_info, String body, String detail, String attach, String out_trade_no, String total_fee,
			String spbill_create_ip, String auth_code, String paternerKey) {
		Map<String, String> queryParas = new HashMap<String, String>();
		queryParas.put("appid", appid);
		queryParas.put("sub_appid", sub_appid);
		queryParas.put("mch_id", mch_id);
		queryParas.put("sub_mch_id", sub_mch_id);
		queryParas.put("device_info", device_info);
		queryParas.put("nonce_str", String.valueOf(System.currentTimeMillis()));
		queryParas.put("body", body);
		queryParas.put("detail", detail);
		queryParas.put("attach", attach);
		queryParas.put("out_trade_no", out_trade_no);
		queryParas.put("total_fee", total_fee);
		queryParas.put("spbill_create_ip", spbill_create_ip);
		queryParas.put("auth_code", auth_code);
		String sign = WxPayment.createSign(queryParas, paternerKey);
		queryParas.put("sign", sign);
		return queryParas;
	}

	/**
	 * 封装查询请求参数 参考代码
	 * 
	 * @param appid
	 * @param sub_appid
	 * @param mch_id
	 * @param sub_mch_id
	 * @param transaction_id
	 * @param out_trade_no
	 * @param paternerKey
	 * @return
	 */
	public static Map<String, String> buildParasMap(String appid, String sub_appid, String mch_id, String sub_mch_id,
			String transaction_id, String out_trade_no, String paternerKey) {
		Map<String, String> params = new HashMap<String, String>();

		params.put("appid", appid);
		params.put("sub_appid", sub_appid);
		params.put("mch_id", mch_id);
		params.put("sub_mch_id", sub_mch_id);
		params.put("transaction_id", transaction_id);
		params.put("out_trade_no", out_trade_no);

		return buildSignAfterParasMap(params, paternerKey);
	}

	/**
	 * 构建统一下单参数
	 * 
	 * @param appid
	 * @param sub_appid
	 *            否
	 * @param mch_id
	 * @param sub_mch_id
	 *            服务商模式下必须
	 * @param device_info
	 *            否
	 * @param body
	 * @param detail
	 *            否
	 * @param attach
	 *            否
	 * @param out_trade_no
	 * @param total_fee
	 * @param spbill_create_ip
	 * @param paternerKey
	 * @param notify_url
	 * @param trade_type
	 * @param product_id
	 *            扫码支付必传
	 * @return
	 */
	public static Map<String, String> buildUnifiedOrderParasMap(String appid, String sub_appid, String mch_id,
			String sub_mch_id, String device_info, String body, String detail, String attach, String out_trade_no,
			String total_fee, String spbill_create_ip, String notify_url, String trade_type, String paternerKey,
			String product_id) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("appid", appid);
		params.put("sub_appid", sub_appid);
		params.put("mch_id", mch_id);
		params.put("sub_mch_id", sub_mch_id);
		params.put("device_info", device_info);
		params.put("body", body);
		params.put("detail", detail);
		params.put("attach", attach);

		params.put("total_fee", total_fee);
		params.put("spbill_create_ip", spbill_create_ip);
		params.put("notify_url", notify_url);
		params.put("trade_type", trade_type);
		params.put("product_id", product_id);

		return buildSignAfterParasMap(params, paternerKey);
	}

	/**
	 * 构建短链接参数
	 * 
	 * @param appid
	 * @param sub_appid
	 * @param mch_id
	 * @param sub_mch_id
	 * @param long_url
	 * @param paternerKey
	 * @return
	 */
	public static Map<String, String> buildShortUrlParasMap(String appid, String sub_appid, String mch_id,
			String sub_mch_id, String long_url, String paternerKey) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("appid", appid);
		params.put("sub_appid", sub_appid);
		params.put("mch_id", mch_id);
		params.put("sub_mch_id", sub_mch_id);
		params.put("long_url", long_url);

		return buildSignAfterParasMap(params, paternerKey);

	}

	/**
	 * 组装签名的字段
	 * 
	 * @param params
	 *            参数
	 * @param urlEncoder
	 *            是否urlEncoder
	 * @return String
	 */
	public static String packageSign(Map<String, String> params, boolean urlEncoder) {
		// 先将参数以其参数名的字典序升序进行排序
		TreeMap<String, String> sortedParams = new TreeMap<String, String>(params);
		// 遍历排序后的字典，将所有参数按"key=value"格式拼接在一起
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (Entry<String, String> param : sortedParams.entrySet()) {
			String value = param.getValue();
			if (StringUtils.isBlank(value)) {
				continue;
			}
			if (first) {
				first = false;
			} else {
				sb.append("&");
			}
			sb.append(param.getKey()).append("=");
			if (urlEncoder) {
				try {
					value = urlEncode(value);
				} catch (UnsupportedEncodingException e) {
				}
			}
			sb.append(value);
		}
		return sb.toString();
	}

	/**
	 * urlEncode
	 * 
	 * @param src
	 *            微信参数
	 * @return String
	 * @throws UnsupportedEncodingException
	 *             编码错误
	 */
	public static String urlEncode(String src) throws UnsupportedEncodingException {
		return URLEncoder.encode(src, Charsets.toCharset("UTF-8").name()).replace("+", "%20");
	}

	/**
	 * 构建签名之后的参数
	 * 
	 * @param params
	 * @param paternerKey
	 * @return Map
	 */
	public static Map<String, String> buildSignAfterParasMap(Map<String, String> params, String paternerKey) {
		params.put("nonce_str", WebplusUtil.uuid2());
		String sign = WxPayment.createSign(params, paternerKey);
		params.put("sign", sign);
		return params;
	}

	/**
	 * 生成签名
	 * 
	 * @param params
	 *            参数
	 * @param paternerKey
	 *            支付密钥
	 * @return sign
	 */
	public static String createSign(Map<String, String> params, String partnerKey) {
		// 生成签名前先去除sign
		params.remove("sign");
		String stringA = packageSign(params, true);
		String stringSignTemp = stringA + "&key=" + partnerKey;
		return WebplusHashCodec.md5(stringSignTemp).toUpperCase();
	}
	 /**
     * 生成签名. 注意，若含有sign_type字段，必须和signType参数保持一致。
     *
     * @param data 待签名数据
     * @param key API密钥
     * @param signType 签名方式
     * @return 签名
     */
    public static String generateSignature(final Map<String, String> data, String key ){
       
            try {
            	 Set<String> keySet = data.keySet();
                 String[] keyArray = keySet.toArray(new String[keySet.size()]);
                 Arrays.sort(keyArray);
                 StringBuilder sb = new StringBuilder();
                 for (String k : keyArray) {
                     
                     if (data.get(k).trim().length() > 0) // 参数值为空，则不参与签名
                         sb.append(k).append("=").append(data.get(k).trim()).append("&");
                 }
                 sb.append("key=").append(key);
                
				return MD5(sb.toString()).toUpperCase();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
       return "";
    }
    /**
     * 生成 MD5
     *
     * @param data 待处理数据
     * @return MD5结果
     */
    public static String MD5(String data) throws Exception {
        java.security.MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] array = md.digest(data.getBytes("UTF-8"));
        StringBuilder sb = new StringBuilder();
        for (byte item : array) {
            sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString().toUpperCase();
    }

	/**
	 * 支付异步通知时校验sign
	 * 
	 * @param params
	 *            参数
	 * @param paternerKey
	 *            支付密钥
	 * @return {boolean}
	 */
	public static boolean verifyNotify(Map<String, String> params, String paternerKey) {
		String sign = params.get("sign");
		String localSign = WxPayment.createSign(params, paternerKey);
		return sign.equals(localSign);
	}

	/**
	 * 判断接口返回的code是否是SUCCESS
	 * 
	 * @param return_code、result_code
	 * @return
	 */
	public static boolean codeIsOK(String return_code) {
		return StringUtils.isNotBlank(return_code) && "SUCCESS".equals(return_code);
	}

	/**
	 * 微信下单map to xml
	 * 
	 * @param params
	 *            参数
	 * @return String
	 */
	public static String toXml(Map<String, String> params) {
		StringBuilder xml = new StringBuilder();
		xml.append("<xml>");
		for (Entry<String, String> entry : params.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			// 略过空值
			if (StringUtils.isBlank(value))
				continue;
			xml.append("<").append(key).append(">");
			xml.append(entry.getValue());
			xml.append("</").append(key).append(">");
		}
		xml.append("</xml>");
		return xml.toString();
	}

	/**
	 * 针对支付的xml，没有嵌套节点的简单处理
	 * 
	 * @param xmlStr
	 *            xml字符串
	 * @return map集合
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String> xmlToMap(String xmlStr) {
		return WebplusXml.parseXml2Map(xmlStr);
	}

	/**
	 * 替换url中的参数
	 * 
	 * @param str
	 * @param regex
	 * @param args
	 * @return
	 */
	public static String replace(String str, String regex, String... args) {
		int length = args.length;
		for (int i = 0; i < length; i++) {
			str = str.replaceFirst(regex, args[i]);
		}
		return str;
	}
}
