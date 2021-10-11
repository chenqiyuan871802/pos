package com.ims.core.pay;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import com.ims.core.cache.WebplusCache;
import com.ims.core.constant.WebplusCons;
import com.ims.core.matatype.Dto;
import com.ims.core.matatype.Dtos;
import com.ims.core.util.WebplusHttp;
import com.ims.core.util.WebplusUtil;
import com.ims.core.util.WebplusXml;
import com.ims.core.vo.R;
/**
 * 微信支付地方
 * 类名:com.toonan.core.pay.WxPay
 * 描述:
 * 编写者:陈骑元
 * 创建时间:2019年5月18日 上午12:48:22
 * 修改说明:
 */
@Component
@PropertySource(value = "classpath:third.properties")
public class WxPay {
	
	
	private static String appID;
	private static String mch_id;

	private static String apiKey;

	@Value("${wechat.appID}")
	public  void setAppID(String appID) {
		WxPay.appID = appID;
	}

	@Value("${wechat.mch_id}")
	public void setMch_id(String mch_id) {
		WxPay.mch_id = mch_id;
	}

	@Value("${wechat.apiKey}")
	public void setApiKey(String apiKey) {
		WxPay.apiKey = apiKey;
	}



	
	
	
	private static Log log = LogFactory.getLog(WxPay.class);
	
	// 统一下单接口
	private static final String UNIFIEDORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
	// 订单查询
	private static final String ORDERQUERY_URL = "https://api.mch.weixin.qq.com/pay/orderquery";
	// 关闭订单
	private static final String CLOSEORDER_URL = "https://api.mch.weixin.qq.com/pay/closeorder";
	// 撤销订单
	private static final String REVERSE_URL = "https://api.mch.weixin.qq.com/secapi/pay/reverse";
	// 申请退款
	private static final String REFUND_URL = "https://api.mch.weixin.qq.com/secapi/pay/refund";
	// 查询退款
	private static final String REFUNDQUERY_URL = "https://api.mch.weixin.qq.com/pay/refundquery";
	// 下载对账单
	private static final String DOWNLOADBILLY_URL = "https://api.mch.weixin.qq.com/pay/downloadbill";
	// 交易保障
	private static final String REPORT_URL = "https://api.mch.weixin.qq.com/payitil/report";
	// 转换短链接
	private static final String SHORT_URL = "https://api.mch.weixin.qq.com/tools/shorturl";
	// 授权码查询openId接口
	private static final String AUTHCODETOOPENID_URL = "https://api.mch.weixin.qq.com/tools/authcodetoopenid";
	// 刷卡支付
	private static final String MICROPAY_URL = "https://api.mch.weixin.qq.com/pay/micropay";
	// 企业付款
	private static final String TRANSFERS_URL = "https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers";
	// 查询企业付款
	private static final String GETTRANSFERINFO_URL = "https://api.mch.weixin.qq.com/mmpaymkttransfers/gettransferinfo";
	//获取秘钥仿真测试
	private WxPay() {
	}

	/**
	 * 交易类型枚举
	 * 
	 * @author Javen 2017年4月15日
	 *         JSAPI--公众号支付、NATIVE--原生扫码支付、APP--app支付，统一下单接口trade_type的传参可参考这里
	 *         MICROPAY--刷卡支付，刷卡支付有单独的支付接口，不调用统一下单接口
	 */
	public static enum TradeType {
		JSAPI, NATIVE, APP, WAP, MICROPAY
	}
	
	 /**
     * 
     * 简要说明：扫描下单
     * 编写者：陈骑元
     * 创建时间：2019年3月28日 上午12:20:07
     * @param 说明
     * @return 说明
     */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static R pushNativeOrder(String orderId,int totalFee,String spbillCreateIp,String body,String notifyUrl){
		  Map<String,String> params=new HashMap<String,String>();
		  params.put("appid", appID);
		  params.put("mch_id",mch_id);
		  params.put("device_info", "WEB");
		  params.put("nonce_str", WebplusUtil.uuid());
		  params.put("body",body);
		  params.put("out_trade_no", orderId );
		  String paySwitch=WebplusCache.getParamValue(WebplusCons.PAY_SWITCH_KEY);
		  if(WebplusCons.SWITCH_ON.equals(paySwitch)){
			  totalFee=1;
		  }
		  params.put("total_fee", totalFee+"");
		  params.put("spbill_create_ip", spbillCreateIp);
		  
		  params.put("notify_url", notifyUrl);
		  params.put("trade_type", TradeType.NATIVE+"");
		  String sign = WxPayment.generateSignature(params, apiKey);
		  params.put("sign", sign);
		 String returnStr= doPost(UNIFIEDORDER_URL, params);
		 log.info("订单号："+orderId+" 统一下单接口返回信息："+returnStr);
		 Dto returnDto=Dtos.newDto();
		 if(WebplusUtil.isNotEmpty(returnStr)){
			 Map map=WebplusXml.parseXml2Map(returnStr);
			 returnDto.putAll(map);
			 String return_code=returnDto.getString("return_code");
			 String return_msg=returnDto.getString("return_msg");
			 if("SUCCESS".endsWith(return_code)){
				 String result_code=returnDto.getString("result_code");
				 if("SUCCESS".endsWith(result_code)){
					 log.info("订单号："+orderId+" 微信下单成功"); 
					 return R.ok().put("code_url", returnDto.getString("code_url"));
				 }
					 
				 
				 log.error("订单号："+orderId+" 微信下单失败,失败原因："+returnDto.getString("err_code_des")); 
				 return R.error("订单号："+orderId+" 微信下单失败,失败原因："+returnDto.getString("err_code_des"));
			 }else{
				 log.error("订单号："+orderId+" 微信下单失败,失败原因："+return_msg); 
				 return R.error("订单号："+orderId+" 微信下单失败,失败原因："+return_msg);
			 }
		 }else {
			
			 log.error("订单号："+orderId+"，微信统一下单接口调用失败");
			 return R.error("微信统一下单接口调用失败");
		 }
		
	}
	
    /**
     * 
     * 简要说明：微信统一APP下单接口
     * 编写者：陈骑元
     * 创建时间：2019年3月28日 上午12:20:07
     * @param 说明
     * @return 说明
     */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static R pushAppOrder(String orderId,int totalFee,String spbillCreateIp,String notifyUrl,String body,String orderDetail){
		  Map<String,String> params=new HashMap<String,String>();
		  params.put("appid", appID);
		  params.put("mch_id",mch_id);
		  params.put("device_info", "WEB");
		  params.put("nonce_str", WebplusUtil.uuid());
		  params.put("body",body);
		  params.put("out_trade_no", orderId );
		  String paySwitch=WebplusCache.getParamValue(WebplusCons.PAY_SWITCH_KEY);
		  if(WebplusCons.SWITCH_ON.equals(paySwitch)){
			  totalFee=1;
		  }
		  params.put("total_fee", totalFee+"");
		  params.put("spbill_create_ip", spbillCreateIp);
		  params.put("notify_url", notifyUrl);
		  params.put("trade_type", TradeType.APP+"");
		  String sign = WxPayment.generateSignature(params, apiKey);
		  params.put("sign", sign);
		 String returnStr= doPost(UNIFIEDORDER_URL, params);
		 log.info("订单号："+orderId+" 统一下单接口返回信息："+returnStr);
		 Dto returnDto=Dtos.newDto();
		 if(WebplusUtil.isNotEmpty(returnStr)){
			 Map map=WebplusXml.parseXml2Map(returnStr);
			 returnDto.putAll(map);
			 String return_code=returnDto.getString("return_code");
			 String return_msg=returnDto.getString("return_msg");
			 if("SUCCESS".endsWith(return_code)){
				 String result_code=returnDto.getString("result_code");
				 if("SUCCESS".endsWith(result_code)){
					 Map<String,String> signMap=new HashMap<String,String>();
					 signMap.put("appid", returnDto.getString("appid"));
					 signMap.put("partnerid", returnDto.getString("mch_id"));
					 signMap.put("prepayid", returnDto.getString("prepay_id"));
					 signMap.put("package", "Sign=WXPay");
					 signMap.put("noncestr", returnDto.getString("nonce_str"));
					 signMap.put("timestamp", Instant.now().getEpochSecond()+"");
					 String signStr=WxPayment.generateSignature( signMap, apiKey);
					 signMap.put("sign", signStr);
					 log.info("订单号："+orderId+" 微信下单成功"); 
					 return R.toData(signMap);
				 }
					 
				 
				 log.error("订单号："+orderId+" 微信下单失败,失败原因："+returnDto.getString("err_code_des")); 
				 return R.error("订单号："+orderId+" 微信下单失败,失败原因："+returnDto.getString("err_code_des"));
			 }else{
				 log.error("订单号："+orderId+" 微信下单失败,失败原因："+return_msg); 
				 return R.error("订单号："+orderId+" 微信下单失败,失败原因："+return_msg);
			 }
		 }else {
			
			 log.error("订单号："+orderId+"，微信统一下单接口调用失败");
			 return R.error("微信统一下单接口调用失败");
		 }
		
	}
	
    /**
     * 
     * 简要说明：封装微信统一下单接口
     * 编写者：陈骑元
     * 创建时间：2018年10月29日 下午10:53:57
     * @param 说明
     * @return 说明
     */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Dto  pushWechatOrder(String orderId,int totalFee,String spbillCreateIp,String notifyUrl,String openid ) {
		  Map<String,String> params=new HashMap<String,String>();
		  params.put("appid", appID);
		  params.put("mch_id",mch_id);
		  params.put("device_info", "WEB");
		  params.put("nonce_str", WebplusUtil.uuid());
		  params.put("body", "快递费用");
		  params.put("out_trade_no", orderId );
		  String pay_switch=WebplusCache.getParamValue(WebplusCons.PAY_SWITCH_KEY);
		  if(WebplusCons.SWITCH_ON.equals(pay_switch)){
			  totalFee=1;
		  }
		  params.put("total_fee", totalFee+"");
		  params.put("spbill_create_ip", spbillCreateIp);
		  params.put("notify_url", notifyUrl);
		  params.put("trade_type", TradeType.JSAPI+"");
		  params.put("openid", openid);
		  String sign = WxPayment.generateSignature(params, apiKey);
		  params.put("sign", sign);
		 String returnStr= doPost(UNIFIEDORDER_URL, params);
		 log.info("订单号："+orderId+" 统一下单接口返回信息："+returnStr);
		 Dto returnDto=Dtos.newDto();
		 if(WebplusUtil.isNotEmpty(returnStr)){
			 Map map=WebplusXml.parseXml2Map(returnStr);
			 returnDto.putAll(map);
			 String return_code=returnDto.getString("return_code");
			 String return_msg=returnDto.getString("return_msg");
			 if("SUCCESS".endsWith(return_code)){
				 String result_code=returnDto.getString("result_code");
				 if("SUCCESS".endsWith(result_code)){
					 log.info("订单号："+orderId+" 微信下单成功"); 
				 }else{
					 log.error("订单号："+orderId+" 微信下单失败,失败原因："+returnDto.getString("err_code_des")); 
				 }
			 }else{
				 log.error("订单号："+orderId+" 微信下单失败,失败原因："+return_msg); 
			 }
		 }else {
			returnDto.put("return_code", "FAIL");
			returnDto.put("return_msg", "微信统一下单接口调用失败");
			 log.error("订单号："+orderId+"，微信统一下单接口调用失败");
		 }
		return returnDto;
	}
	/**
	 * 统一下单
	 * 服务商模式接入文档:https://pay.weixin.qq.com/wiki/doc/api/native_sl.php?chapter=9_1
	 * 商户模式接入文档:https://pay.weixin.qq.com/wiki/doc/api/native.php?chapter=9_1
	 * 
	 * @param params
	 * @return
	 */
	public static String pushOrder(Map<String, String> params) {
		return doPost(UNIFIEDORDER_URL, params);
	}
    
	/**
	 * 订单查询
	 * 服务商模式接入文档:https://pay.weixin.qq.com/wiki/doc/api/micropay_sl.php?chapter=9_2
	 * 商户模式接入文档:https://pay.weixin.qq.com/wiki/doc/api/micropay.php?chapter=9_2
	 * 
	 * @param params
	 *            请求参数
	 * @return
	 */
	public static String orderQuery(Map<String, String> params) {
		return doPost(ORDERQUERY_URL, params);
	}

	/**
	 * 关闭订单
	 * 服务商模式接入文档:https://pay.weixin.qq.com/wiki/doc/api/jsapi_sl.php?chapter=9_3
	 * 商户模式接入文档:https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_3
	 * 
	 * @param params
	 * @return
	 */
	public static String closeOrder(Map<String, String> params) {
		return doPost(CLOSEORDER_URL, params);
	}

	/**
	 * 撤销订单
	 * 服务商模式接入文档:https://pay.weixin.qq.com/wiki/doc/api/micropay_sl.php?chapter=9_11&index=3
	 * 商户模式接入文档:https://pay.weixin.qq.com/wiki/doc/api/micropay.php?chapter=9_11&index=3
	 * 
	 * @param params
	 *            请求参数
	 * @param certPath
	 *            证书文件目录
	 * @param certPass
	 *            证书密码
	 * @return
	 */
	public static String orderReverse(Map<String, String> params, String certPath, String certPass) {
		return doPostSSL(REVERSE_URL, params, certPath, certPass);
	}

	/**
	 * 申请退款
	 * 服务商模式接入文档:https://pay.weixin.qq.com/wiki/doc/api/micropay_sl.php?chapter=9_4
	 * 商户模式接入文档:https://pay.weixin.qq.com/wiki/doc/api/micropay.php?chapter=9_4
	 * 
	 * @param params
	 *            请求参数
	 * @param certPath
	 *            证书文件目录
	 * @param certPass
	 *            证书密码
	 * @return
	 */
	public static String orderRefund(Map<String, String> params, String certPath, String certPass) {
		return doPostSSL(REFUND_URL, params, certPath, certPass);
	}

	/**
	 * 查询退款
	 * 服务商模式接入文档:https://pay.weixin.qq.com/wiki/doc/api/micropay_sl.php?chapter=9_5
	 * 商户模式接入文档:https://pay.weixin.qq.com/wiki/doc/api/micropay.php?chapter=9_5
	 * 
	 * @param params
	 *            请求参数
	 * @return
	 */
	public static String orderRefundQuery(Map<String, String> params) {
		return doPost(REFUNDQUERY_URL, params);
	}

	/**
	 * 下载对账单
	 * 服务商模式接入文档:https://pay.weixin.qq.com/wiki/doc/api/micropay_sl.php?chapter=9_6
	 * 商户模式接入文档:https://pay.weixin.qq.com/wiki/doc/api/micropay.php?chapter=9_6
	 * 
	 * @param params
	 *            请求参数
	 * @return
	 */
	public static String downloadBill(Map<String, String> params) {
		return doPost(DOWNLOADBILLY_URL, params);
	}

	/**
	 * 交易保障
	 * 服务商模式接入文档:https://pay.weixin.qq.com/wiki/doc/api/micropay_sl.php?chapter=9_14&index=7
	 * 商户模式接入文档:https://pay.weixin.qq.com/wiki/doc/api/micropay.php?chapter=9_14&index=7
	 * 
	 * @param params
	 *            请求参数
	 * @return
	 */
	public static String orderReport(Map<String, String> params) {
		return doPost(REPORT_URL, params);
	}

	/**
	 * 转换短链接
	 * 服务商模式接入文档:https://pay.weixin.qq.com/wiki/doc/api/micropay_sl.php?chapter=9_9&index=8
	 * 商户模式接入文档:https://pay.weixin.qq.com/wiki/doc/api/micropay.php?chapter=9_9&index=8
	 * 
	 * @param params
	 *            请求参数
	 * @return
	 */
	public static String toShortUrl(Map<String, String> params) {
		return doPost(SHORT_URL, params);
	}

	/**
	 * 授权码查询openId
	 * 服务商模式接入文档:https://pay.weixin.qq.com/wiki/doc/api/micropay_sl.php?chapter=9_12&index=9
	 * 商户模式接入文档:
	 * https://pay.weixin.qq.com/wiki/doc/api/micropay.php?chapter=9_13&index=9
	 * 
	 * @param params
	 *            请求参数
	 * @return
	 */
	public static String authCodeToOpenid(Map<String, String> params) {
		return doPost(AUTHCODETOOPENID_URL, params);
	}

	/**
	 * 刷卡支付
	 * 服务商模式接入文档:https://pay.weixin.qq.com/wiki/doc/api/micropay_sl.php?chapter=9_10&index=1
	 * 商户模式接入文档:
	 * https://pay.weixin.qq.com/wiki/doc/api/micropay.php?chapter=9_10&index=1
	 *
	 * @param params
	 *            请求参数
	 * @return
	 */
	public static String micropay(Map<String, String> params) {
		return WxPay.doPost(MICROPAY_URL, params);
	}

	/**
	 * 企业付款
	 * 
	 * @param params
	 *            请求参数
	 * @param certPath
	 *            证书文件目录
	 * @param certPassword
	 *            证书密码
	 * @return {String}
	 */
	public static String transfers(Map<String, String> params, String certPath, String certPassword) {
		return WxPay.doPostSSL(TRANSFERS_URL, params, certPath, certPassword);
	}

	/**
	 * 查询企业付款
	 * 
	 * @param params
	 *            请求参数
	 * @param certPath
	 *            证书文件目录
	 * @param certPassword
	 *            证书密码
	 * @return {String}
	 */
	public static String getTransferInfo(Map<String, String> params, String certPath, String certPassword) {
		return WxPay.doPostSSL(GETTRANSFERINFO_URL, params, certPath, certPassword);
	}

	/**
	 * 商户模式下 扫码模式一之生成二维码
	 * 
	 * @param appid
	 * @param mch_id
	 * @param product_id
	 * @param partnerKey
	 * @param isToShortUrl
	 *            是否转化为短连接
	 * @return
	 */
	public static String getCodeUrl(String appid, String mch_id, String product_id, String partnerKey,
			boolean isToShortUrl) {
		String url = "weixin://wxpay/bizpayurl?sign=XXXXX&appid=XXXXX&mch_id=XXXXX&product_id=XXXXX&time_stamp=XXXXX&nonce_str=XXXXX";
		String timeStamp = Long.toString(System.currentTimeMillis() / 1000);
		String nonceStr = Long.toString(System.currentTimeMillis());
		Map<String, String> packageParams = new HashMap<String, String>();
		packageParams.put("appid", appid);
		packageParams.put("mch_id", mch_id);
		packageParams.put("product_id", product_id);
		packageParams.put("time_stamp", timeStamp);
		packageParams.put("nonce_str", nonceStr);
		String packageSign = WxPayment.createSign(packageParams, partnerKey);
		String qrCodeUrl = WxPayment.replace(url, "XXXXX", packageSign, appid, mch_id, product_id, timeStamp,
				nonceStr);
		if (isToShortUrl) {
			String shortResult = WxPay
					.toShortUrl(WxPayment.buildShortUrlParasMap(appid, null, mch_id, null, qrCodeUrl, partnerKey));
			if (log.isDebugEnabled()) {
				log.info(shortResult);
			}
			Map<String, String> shortMap = WxPayment.xmlToMap(shortResult);
			String return_code = shortMap.get("return_code");
			if (WxPayment.codeIsOK(return_code)) {
				String result_code = shortMap.get("result_code");
				if (WxPayment.codeIsOK(result_code)) {
					qrCodeUrl = shortMap.get("short_url");
				}
			}
		}

		return qrCodeUrl;
	}

	public static String doPost(String url, Map<String, String> params) {
		try {
			String xml=WxPayment.toXml(params);
			return WebplusHttp.post(url, xml);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	public static String doPostSSL(String url, Map<String, String> params, String certPath, String certPass) {
		return WebplusHttp.postSSL(url, WxPayment.toXml(params), certPath, certPass);
	}
	
	
   
	/**
	 * 
	 * 简要说明：微信支付测试
	 * 编写者：陈骑元
	 * 创建时间：2018年9月2日 下午6:21:35
	 * @param 说明
	 * @return 说明
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception{
    
	}
}
