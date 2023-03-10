package com.ims.core.pay;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.domain.AlipayDataDataserviceBillDownloadurlQueryModel;
import com.alipay.api.domain.AlipayFundTransOrderQueryModel;
import com.alipay.api.domain.AlipayFundTransToaccountTransferModel;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.domain.AlipayTradeCancelModel;
import com.alipay.api.domain.AlipayTradeCloseModel;
import com.alipay.api.domain.AlipayTradeCreateModel;
import com.alipay.api.domain.AlipayTradeFastpayRefundQueryModel;
import com.alipay.api.domain.AlipayTradeOrderSettleModel;
import com.alipay.api.domain.AlipayTradePayModel;
import com.alipay.api.domain.AlipayTradePrecreateModel;
import com.alipay.api.domain.AlipayTradeQueryModel;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.request.AlipayDataDataserviceBillDownloadurlQueryRequest;
import com.alipay.api.request.AlipayFundTransOrderQueryRequest;
import com.alipay.api.request.AlipayFundTransToaccountTransferRequest;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradeCancelRequest;
import com.alipay.api.request.AlipayTradeCloseRequest;
import com.alipay.api.request.AlipayTradeCreateRequest;
import com.alipay.api.request.AlipayTradeFastpayRefundQueryRequest;
import com.alipay.api.request.AlipayTradeOrderSettleRequest;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradePayRequest;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayDataDataserviceBillDownloadurlQueryResponse;
import com.alipay.api.response.AlipayFundTransOrderQueryResponse;
import com.alipay.api.response.AlipayFundTransToaccountTransferResponse;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradeCancelResponse;
import com.alipay.api.response.AlipayTradeCloseResponse;
import com.alipay.api.response.AlipayTradeCreateResponse;
import com.alipay.api.response.AlipayTradeFastpayRefundQueryResponse;
import com.alipay.api.response.AlipayTradeOrderSettleResponse;
import com.alipay.api.response.AlipayTradePayResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;

/**
 * 
 * ??????:com.ims.common.support.pay.AliPay
 * ??????:????????????????????????
 * ?????????:?????????
 * ????????????:2017???11???2??? ??????4:17:06
 * ????????????:
 */
public class AliPay {
	private static Log log = LogFactory.getLog(AliPay.class);

	/**
	 * App??????
	 * 
	 * @param model
	 * @param notifyUrl
	 * @return
	 * @throws AlipayApiException
	 */
	public static String startAppPayStr(AlipayTradeAppPayModel model, String notifyUrl) throws AlipayApiException {
		AlipayTradeAppPayResponse response = appPay(model, notifyUrl);
		return response.getBody();
	}

	/**
	 * App ??????
	 * https://doc.open.alipay.com/docs/doc.htm?treeId=54&articleId=106370&docType=1
	 * 
	 * @param model
	 * @param notifyUrl
	 * @return
	 * @throws AlipayApiException
	 */
	public static AlipayTradeAppPayResponse appPay(AlipayTradeAppPayModel model, String notifyUrl)
			throws AlipayApiException {
		// ???????????????API?????????request???,??????????????????????????????,???????????????????????????alipay.trade.app.pay
		AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
		// SDK????????????????????????????????????????????????????????????????????????????????????sdk???model????????????(model???biz_content???????????????????????????biz_content)???
		request.setBizModel(model);
		request.setNotifyUrl(notifyUrl);
		// ???????????????????????????????????????????????????sdkExecute
		AlipayTradeAppPayResponse response = AliPayConfig.build().getAlipayClient().sdkExecute(request);
		return response;
	}

	/**
	 * Wap??????
	 * https://doc.open.alipay.com/docs/doc.htm?spm=a219a.7629140.0.0.dfHHR3&
	 * treeId=203&articleId=105285&docType=1
	 * 
	 * @throws AlipayApiException
	 * @throws IOException
	 */
	public static void wapPay(HttpServletResponse response, AlipayTradeWapPayModel model, String returnUrl,
			String notifyUrl) throws AlipayApiException, IOException {
		String form = wapPayToString(response, model, returnUrl, notifyUrl);
		HttpServletResponse httpResponse = response;
		httpResponse.setContentType("text/html;charset=" + AliPayConfig.build().getCharset());
		httpResponse.getWriter().write(form);// ????????????????????????html???????????????
		httpResponse.getWriter().flush();
	}

	public static String wapPayToString(HttpServletResponse response, AlipayTradeWapPayModel model, String returnUrl,
			String notifyUrl) throws AlipayApiException, IOException {
		AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();// ??????API?????????request
		alipayRequest.setReturnUrl(returnUrl);
		alipayRequest.setNotifyUrl(notifyUrl);// ?????????????????????????????????????????????
		alipayRequest.setBizModel(model);// ??????????????????
		return AliPayConfig.build().getAlipayClient().pageExecute(alipayRequest).getBody(); // ??????SDK????????????
	}

	/**
	 * ??????????????????????????????
	 * https://doc.open.alipay.com/docs/api.htm?spm=a219a.7629065.0.0.XVqALk&apiId=850&docType=4
	 * 
	 * @param notifyUrl
	 * @throws AlipayApiException
	 */
	public static String tradePay(AlipayTradePayModel model, String notifyUrl) throws AlipayApiException {
		AlipayTradePayResponse response = tradePayToResponse(model, notifyUrl);
		return response.getBody();
	}

	public static AlipayTradePayResponse tradePayToResponse(AlipayTradePayModel model, String notifyUrl)
			throws AlipayApiException {
		AlipayTradePayRequest request = new AlipayTradePayRequest();
		request.setBizModel(model);// ??????????????????
		request.setNotifyUrl(notifyUrl);
		return AliPayConfig.build().getAlipayClient().execute(request); // ??????AliPayConfig.build().getAlipayClient()??????API??????????????????response???
	}

	/**
	 * ????????????
	 * https://doc.open.alipay.com/docs/doc.htm?spm=a219a.7629140.0.0.i0UVZn&treeId=193&articleId=105170&docType=1#s4
	 * 
	 * @param notifyUrl
	 * @return
	 * @throws AlipayApiException
	 */
	public static String tradePrecreatePay(AlipayTradePrecreateModel model, String notifyUrl)
			throws AlipayApiException {
		AlipayTradePrecreateResponse response = tradePrecreatePayToResponse(model, notifyUrl);
		return response.getBody();
	}

	public static AlipayTradePrecreateResponse tradePrecreatePayToResponse(AlipayTradePrecreateModel model,
			String notifyUrl) throws AlipayApiException {
		AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
		request.setBizModel(model);
		request.setNotifyUrl(notifyUrl);
		return AliPayConfig.build().getAlipayClient().execute(request);
	}

	/**
	 * ??????????????????????????????
	 * https://doc.open.alipay.com/docs/doc.htm?spm=a219a.7629140.0.0.54Ty29&treeId=193&articleId=106236&docType=1
	 * 
	 * @param content
	 * @return
	 * @throws AlipayApiException
	 */
	public static boolean transfer(AlipayFundTransToaccountTransferModel model) throws AlipayApiException {
		AlipayFundTransToaccountTransferResponse response = transferToResponse(model);
		String result = response.getBody();
		log.info("transfer result>" + result);
		if (response.isSuccess()) {
			return true;
		} else {
			// ??????????????????????????????
			JSONObject jsonObject = JSONObject.parseObject(result);
			String out_biz_no = jsonObject.getJSONObject("alipay_fund_trans_toaccount_transfer_response")
					.getString("out_biz_no");
			AlipayFundTransOrderQueryModel queryModel = new AlipayFundTransOrderQueryModel();
			model.setOutBizNo(out_biz_no);
			boolean isSuccess = transferQuery(queryModel);
			if (isSuccess) {
				return true;
			}
		}
		return false;
	}

	public static AlipayFundTransToaccountTransferResponse transferToResponse(
			AlipayFundTransToaccountTransferModel model) throws AlipayApiException {
		AlipayFundTransToaccountTransferRequest request = new AlipayFundTransToaccountTransferRequest();
		request.setBizModel(model);
		return AliPayConfig.build().getAlipayClient().execute(request);
	}

	/**
	 * ??????????????????
	 * 
	 * @param content
	 * @return
	 * @throws AlipayApiException
	 */
	public static boolean transferQuery(AlipayFundTransOrderQueryModel model) throws AlipayApiException {
		AlipayFundTransOrderQueryResponse response = transferQueryToResponse(model);
		log.info("transferQuery result>" + response.getBody());
		if (response.isSuccess()) {
			return true;
		}
		return false;
	}

	public static AlipayFundTransOrderQueryResponse transferQueryToResponse(AlipayFundTransOrderQueryModel model)
			throws AlipayApiException {
		AlipayFundTransOrderQueryRequest request = new AlipayFundTransOrderQueryRequest();
		request.setBizModel(model);
		return AliPayConfig.build().getAlipayClient().execute(request);
	}

	/**
	 * ??????????????????
	 * https://doc.open.alipay.com/docs/api.htm?spm=a219a.7395905.0.0.8H2JzG&docType=4&apiId=757
	 * 
	 * @param bizContent
	 * @return
	 * @throws AlipayApiException
	 */
	public static boolean isTradeQuery(AlipayTradeQueryModel model) throws AlipayApiException {
		AlipayTradeQueryResponse response = tradeQuery(model);
		if (response.isSuccess()) {
			return true;
		}
		return false;
	}

	public static AlipayTradeQueryResponse tradeQuery(AlipayTradeQueryModel model) throws AlipayApiException {
		AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
		request.setBizModel(model);
		return AliPayConfig.build().getAlipayClient().execute(request);
	}

	/**
	 * ??????????????????
	 * https://doc.open.alipay.com/docs/api.htm?spm=a219a.7395905.0.0.XInh6e&docType=4&apiId=866
	 * 
	 * @param bizContent
	 * @return
	 * @throws AlipayApiException
	 */
	public static boolean isTradeCancel(AlipayTradeCancelModel model) throws AlipayApiException {
		AlipayTradeCancelResponse response = tradeCancel(model);
		if (response.isSuccess()) {
			return true;
		}
		return false;
	}

	public static AlipayTradeCancelResponse tradeCancel(AlipayTradeCancelModel model) throws AlipayApiException {
		AlipayTradeCancelRequest request = new AlipayTradeCancelRequest();
		request.setBizModel(model);
		AlipayTradeCancelResponse response = AliPayConfig.build().getAlipayClient().execute(request);
		return response;
	}

	/**
	 * ????????????
	 * https://doc.open.alipay.com/docs/api.htm?spm=a219a.7629065.0.0.21yRUe&apiId=1058&docType=4
	 * 
	 * @param model
	 * @return
	 * @throws AlipayApiException
	 */
	public static boolean isTradeClose(AlipayTradeCloseModel model) throws AlipayApiException {
		AlipayTradeCloseResponse response = tradeClose(model);
		if (response.isSuccess()) {
			return true;
		}
		return false;
	}

	public static AlipayTradeCloseResponse tradeClose(AlipayTradeCloseModel model) throws AlipayApiException {
		AlipayTradeCloseRequest request = new AlipayTradeCloseRequest();
		request.setBizModel(model);
		return AliPayConfig.build().getAlipayClient().execute(request);

	}

	/**
	 * ??????????????????????????????
	 * https://doc.open.alipay.com/docs/api.htm?spm=a219a.7629065.0.0.21yRUe&apiId=1046&docType=4
	 * 
	 * @param model
	 * @param notifyUrl
	 * @return
	 * @throws AlipayApiException
	 */
	public static AlipayTradeCreateResponse tradeCreate(AlipayTradeCreateModel model, String notifyUrl)
			throws AlipayApiException {
		AlipayTradeCreateRequest request = new AlipayTradeCreateRequest();
		request.setBizModel(model);
		request.setNotifyUrl(notifyUrl);
		return AliPayConfig.build().getAlipayClient().execute(request);
	}

	/**
	 * ??????
	 * https://doc.open.alipay.com/docs/api.htm?spm=a219a.7395905.0.0.SAyEeI&docType=4&apiId=759
	 * 
	 * @param content
	 * @return
	 * @throws AlipayApiException
	 */
	public static String tradeRefund(AlipayTradeRefundModel model) throws AlipayApiException {
		AlipayTradeRefundResponse response = tradeRefundToResponse(model);
		return response.getBody();
	}

	public static AlipayTradeRefundResponse tradeRefundToResponse(AlipayTradeRefundModel model)
			throws AlipayApiException {
		AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
		request.setBizModel(model);
		return AliPayConfig.build().getAlipayClient().execute(request);
	}

	/**
	 * ????????????
	 * https://doc.open.alipay.com/docs/api.htm?spm=a219a.7629065.0.0.KQeTSa&apiId=1049&docType=4
	 * 
	 * @param model
	 * @return
	 * @throws AlipayApiException
	 */
	public static String tradeRefundQuery(AlipayTradeFastpayRefundQueryModel model) throws AlipayApiException {
		AlipayTradeFastpayRefundQueryResponse response = tradeRefundQueryToResponse(model);
		return response.getBody();
	}

	public static AlipayTradeFastpayRefundQueryResponse tradeRefundQueryToResponse(
			AlipayTradeFastpayRefundQueryModel model) throws AlipayApiException {
		AlipayTradeFastpayRefundQueryRequest request = new AlipayTradeFastpayRefundQueryRequest();
		request.setBizModel(model);
		return AliPayConfig.build().getAlipayClient().execute(request);
	}

	/**
	 * ???????????????????????????
	 * 
	 * @param bizContent
	 * @return
	 * @throws AlipayApiException
	 */
	public static String billDownloadurlQuery(AlipayDataDataserviceBillDownloadurlQueryModel model)
			throws AlipayApiException {
		AlipayDataDataserviceBillDownloadurlQueryResponse response = billDownloadurlQueryToResponse(model);
		return response.getBillDownloadUrl();
	}

	public static AlipayDataDataserviceBillDownloadurlQueryResponse billDownloadurlQueryToResponse(
			AlipayDataDataserviceBillDownloadurlQueryModel model) throws AlipayApiException {
		AlipayDataDataserviceBillDownloadurlQueryRequest request = new AlipayDataDataserviceBillDownloadurlQueryRequest();
		request.setBizModel(model);
		return AliPayConfig.build().getAlipayClient().execute(request);
	}

	/**
	 * ??????????????????
	 * https://doc.open.alipay.com/docs/api.htm?spm=a219a.7395905.0.0.nl0RS3&docType=4&apiId=1147
	 * 
	 * @param bizContent
	 * @return
	 * @throws AlipayApiException
	 */
	public static boolean isTradeOrderSettle(AlipayTradeOrderSettleModel model) throws AlipayApiException {
		AlipayTradeOrderSettleResponse response = tradeOrderSettle(model);
		if (response.isSuccess()) {
			return true;
		}
		return false;
	}

	public static AlipayTradeOrderSettleResponse tradeOrderSettle(AlipayTradeOrderSettleModel model)
			throws AlipayApiException {
		AlipayTradeOrderSettleRequest request = new AlipayTradeOrderSettleRequest();
		request.setBizModel(model);
		return AliPayConfig.build().getAlipayClient().execute(request);
	}

	/**
	 * ??????????????????(PC??????)
	 * 
	 * @param model
	 * @param notifyUrl
	 * @param returnUrl
	 * @return
	 * @throws AlipayApiException
	 * @throws IOException
	 */
	public static void tradePage(HttpServletResponse httpResponse, AlipayTradePayModel model, String notifyUrl,
			String returnUrl) throws AlipayApiException, IOException {
		AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
		request.setBizModel(model);
		request.setNotifyUrl(notifyUrl);
		request.setReturnUrl(returnUrl);
		String form = AliPayConfig.build().getAlipayClient().pageExecute(request).getBody();// ??????SDK????????????
		httpResponse.setContentType("text/html;charset=" + AliPayConfig.build().getCharset());
		httpResponse.getWriter().write(form);// ????????????????????????html???????????????
		httpResponse.getWriter().flush();
		httpResponse.getWriter().close();
	}

	/**
	 * ?????????????????????????????????Map
	 * 
	 * @param request
	 * @return
	 */
	public static Map<String, String> toMap(HttpServletRequest request) {
		Map<String, String> params = new HashMap<String, String>();
		Map<String, String[]> requestParams = request.getParameterMap();
		for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
			}
			// ??????????????????????????????????????????????????????
			// valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
			params.put(name, valueStr);
		}
		return params;
	}
}
