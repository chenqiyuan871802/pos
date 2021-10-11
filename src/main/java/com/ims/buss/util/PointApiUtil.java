package com.ims.buss.util;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import com.google.common.collect.Lists;
import com.ims.buss.constant.BussCons;
import com.ims.buss.model.Member;
import com.ims.core.constant.WebplusCons;
import com.ims.core.matatype.Dto;
import com.ims.core.matatype.Dtos;
import com.ims.core.redis.WebplusJedis;
import com.ims.core.util.WebplusJson;
import com.ims.core.util.WebplusSpringContext;
import com.ims.core.util.WebplusUtil;
import com.ims.core.vo.R;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


/**
 * 
 * 类名:com.ims.buss.util.PointApiUtil
 * 描述:积分api接口
 * 编写者:陈骑元
 * 创建时间:2019年12月15日 下午3:42:47
 * 修改说明:
 */
@Component
public class PointApiUtil {
	
	
	private static Log log = LogFactory.getLog(PointApiUtil.class);
	
	/**
	 * jedis帮助
	 */
	 // API通信基础地址
    public static String apiUrl ;
    // 
    public static String appId ;
    // app秘钥
    public static String appSecret;
    /**
     * 企业编号
     */
    public static String mertCode;
    /**
     *登录名
     */
    public static String loginName;
    /**
     * 登录密码
     */
    public static String password;
    
    private static RestTemplate restTemplate = null;  
   
    @Value("${point.apiUrl}")
	public  void seApiUrl(String apiUrl) {
		
    	PointApiUtil.apiUrl=apiUrl;
	}

	@Value("${point.appId}")
	public void setAppId(String appId) {
		PointApiUtil.appId=appId;
	}
	
	@Value("${point.appSecret}")
	public void setAppSecret(String appSecret) {
		PointApiUtil.appSecret=appSecret;
	}
	@Value("${point.mertCode}")
	public void setMertCode(String mertCode) {
		PointApiUtil.mertCode=mertCode;
	}
	
	@Value("${point.loginName}")
	public void setLoginName(String loginName) {
		PointApiUtil.loginName=loginName;
	}
	
	@Value("${point.password}")
	public void setPassword(String password) {
		PointApiUtil.password=password;
	}
	
	public static synchronized RestTemplate getRestTemplate(){  
        if(restTemplate == null){  
        	  HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
              httpRequestFactory.setConnectionRequestTimeout(60000);
              httpRequestFactory.setConnectTimeout(60000);
              httpRequestFactory.setReadTimeout(180000);
            restTemplate = new RestTemplate(httpRequestFactory);
            restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8)); // 设置编码集
            restTemplate.setErrorHandler(new DefaultResponseErrorHandler()); //error处理
          

        }  
        return restTemplate;  
    }
	/**
	 * 
	 * 简要说明：获取accessToken
	 * 编写者：陈骑元
	 * 创建时间：2019年12月15日 下午9:36:28
	 * @param 说明
	 * @return 说明
	 */
	public static String getAccessToken(){
		WebplusJedis webplusJedis = WebplusSpringContext.getBean("webplusJedis");
		String accessToken=webplusJedis.getString(BussCons.POINT_ACCESS_TOKEN);
		if(WebplusUtil.isEmpty(accessToken)){
			accessToken=login();
			if(WebplusUtil.isNotEmpty(accessToken)){
				webplusJedis.setString(BussCons.POINT_ACCESS_TOKEN, accessToken, 23*3600);
			}
		}
		return accessToken;
	}
	
	/**
	 * 
	 * 简要说明：积分登陆获取token接口
	 * 编写者：陈骑元
	 * 创建时间：2019年12月15日 下午4:34:49
	 * @param 说明
	 * @return 说明
	 */
    public static String login(){
    	MultiValueMap<String,String> param=new LinkedMultiValueMap<>();
		param.add("appId", appId);
		param.add("appSecret", appSecret);
		param.add("mertCode", mertCode);
		param.add("loginName",loginName);
		param.add("password", password);
		try {
			ResponseEntity<String> response = getRestTemplate().postForEntity(apiUrl + "api/v1/login", param, String.class);
			if (HttpStatus.OK.equals(response.getStatusCode())) {
				String cookie = response.getHeaders().get("Set-Cookie").get(0);
				log.info("请求登陆接口返回cookie信息："+cookie);
				String[] cookieArray = cookie.split(";");
				String refreshTokenStr = cookieArray[0];
				String[] array = refreshTokenStr.split("=");
				return array[1];
			} 
		} catch (Exception e) {
			log.info("请求登陆接口返回失败："+e);
		}
		return "";
    }
    /**
     * 
     * 简要说明：积分注册接口
     * 编写者：陈骑元
     * 创建时间：2019年12月16日 下午8:28:41
     * @param 说明
     * @return 说明
     */
    public static boolean enroll(String memberId,Dto pDto){
    	String url = apiUrl + "api/v1/members/" + memberId;
    	pDto.put("action","regist");
    	pDto.put("mertGroupCode", "1400");
    	pDto.put("mertCode", mertCode);
		pDto.put("outletCode", "0001");
		pDto.put("appMertGroupCode", "1400");
		pDto.put("appMertCode", mertCode);
		pDto.put("appOutletCode", "0001");
		pDto.put("memberNo", memberId);
		pDto.put("rowStatus", "A");
		Dto memberDto=Dtos.newDto();
		memberDto.put("member", pDto);
		HttpHeaders httpHeaders = getHeader();
		
    	log.info("会员编号["+memberId+"]进行注册参数打印："+WebplusJson.toJson(memberDto));
    	try {
    		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<Map<String, Object>>  requestEntity = new HttpEntity<Map<String, Object>>(memberDto, httpHeaders);
			ResponseEntity<JSONObject> responseEntity = getRestTemplate().postForEntity(url,  requestEntity,
					JSONObject.class);
			if (HttpStatus.OK.equals(responseEntity.getStatusCode())) {

				return true;

			} 
		} catch (Exception e) {
			log.error("会员编号["+memberId+"]进行注册失败："+e);
		}
		return false;
    }
    
    /**
     * 
     * 简要说明：积分注册更新
     * 编写者：陈骑元
     * 创建时间：2019年12月16日 下午8:28:41
     * @param 说明
     * @return 说明
     */
    public static boolean update(Member member){
    	String memberId=member.getMemberNum();
    	String url = apiUrl + "api/v1/members/" + memberId;
    	Dto pDto=Dtos.newDto();
    	pDto.put("mertGroupCode", "1400");
    	pDto.put("mertCode", mertCode);
		pDto.put("outletCode", "0001");
		pDto.put("appMertGroupCode", "1400");
		pDto.put("appMertCode", mertCode);
		pDto.put("appOutletCode", "0001");
		pDto.put("memberNo", memberId);
		pDto.put("rowStatus", "A");
		Date birthDate=member.getBirthDate();
		if(WebplusUtil.isNotEmpty(birthDate)){
			pDto.put("birthDate",WebplusUtil.date2String(birthDate, WebplusCons.DATE));
		}
		pDto.put("password", member.getPassword());
		pDto.put("email", member.getEmail());
		pDto.put("mobilePhone", member.getMobile());
		pDto.put("sex", member.getSex());
		pDto.put("firstName", member.getFirstName());
		pDto.put("lastName", member.getLastName());
		Dto memberDto=Dtos.newDto();
		memberDto.put("member", pDto);
		HttpHeaders httpHeaders = getHeader();
		
    	log.info("会员编号["+memberId+"]进行更新参数打印："+WebplusJson.toJson(memberDto));
    	try {
    		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<Map<String, Object>>  requestEntity = new HttpEntity<Map<String, Object>>(memberDto, httpHeaders);
            getRestTemplate().put(url, requestEntity);
			return true;
		} catch (Exception e) {
			log.error("会员编号["+memberId+"]进行注册失败："+e);
		}
		return false;
    }
    /**
     * 
     * 简要说明：根据会员编号获取总的积分
     * 编写者：陈骑元
     * 创建时间：2019年12月15日 下午10:33:13
     * @param 说明
     * @return 说明
     */
    public static int  getSummary(String memberId){
    	try {
			String url = apiUrl + "api/v1/members/" + memberId + "/summary";
			HttpHeaders httpHeaders = getHeader();
			httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED); //post表单 ，如果是个json则设置为MediaType.APPLICATION_JSON
			HttpEntity<String> requestEntity = new HttpEntity<String>(null, httpHeaders);
			ResponseEntity<JSONObject> responseEntity = getRestTemplate().exchange(url, HttpMethod.GET, requestEntity,
					JSONObject.class);
			if (HttpStatus.OK.equals(responseEntity.getStatusCode())) {
                log.info("获取会员编号["+memberId+"]返回积分信息："+responseEntity.getBody());
				return responseEntity.getBody().getJSONObject("summary").getInt("point");

			} 
		} catch (Exception e) {
			log.error("获取会员编号["+memberId+"]积分总分信息失败："+e);
		}
	   return 0;
    }
    /**
     * 
     * 简要说明：获取积分明细
     * 编写者：陈骑元
     * 创建时间：2019年12月15日 下午11:14:21
     * @param 说明
     * @return 说明
     */
    public static R getPoints(String memberId,Dto pDto){
    	String url = apiUrl + "api/v1/members/" + memberId + "/points";
    	List<Dto> dataList=Lists.newArrayList();
    	int currentPage=BussUtil.getInt(pDto, "currentPage", 1);
    	int pageSize=BussUtil.getInt(pDto, "pageSize", 10);
    	int limit=pageSize;
    	int offset=(currentPage-1)*pageSize;
    	int count=0;
    	pDto.put("offset", offset);
    	pDto.put("limit", limit);
    	pDto.remove("currentPage");
    	pDto.remove("pageSize");
    	String urlParam=getUrlParam(pDto);
    	if(WebplusUtil.isNotEmpty(urlParam)){
    		url=url+"?"+urlParam;
    	}
    	try {
    		HttpHeaders httpHeaders = getHeader();
    		httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED); //post表单 ，如果是个json则设置为MediaType.APPLICATION_JSON
			HttpEntity<String> requestEntity = new HttpEntity<String>(null, httpHeaders);
			ResponseEntity<JSONObject> responseEntity = getRestTemplate().exchange(url, HttpMethod.GET, requestEntity,
					JSONObject.class);
			if (HttpStatus.OK.equals(responseEntity.getStatusCode())) {
				 log.info("获取会员编号["+memberId+"]返回积分交易详细信息："+responseEntity.getBody());
				 JSONObject jsonObject= responseEntity.getBody();
				 JSONArray jsonArray=jsonObject.getJSONArray("pointTransList");
				 for(int i=0;i<jsonArray.size();i++){
					 JSONObject  object=jsonArray.getJSONObject(i);
					 Dto  dataDto=putJSONObjectToDto( object);
					 String transDate=dataDto.getString("transDate");
					 if(WebplusUtil.isNotEmpty(transDate)){
						 transDate= WebplusUtil.formatStringDate(transDate, BussCons.JP_DATETIME,WebplusCons.DATETIME);
					     dataDto.put("transDate", transDate);
					 }
					 dataList.add(dataDto);
				 }
				 count=jsonObject.getJSONObject("meta").getInt("totalCount");
			} 
		} catch (Exception e) {
			log.error("获取会员编号["+memberId+"]积分积分交易详细信息失败："+e);
		}
		Dto pageDto=Dtos.newDto();
		pageDto.put("currentPage", currentPage);
		pageDto.put("pageSize", pageSize);
		pageDto.put("count", count);
    	return R.toList(dataList).put("page", pageDto);
    }
    /**
     * 
     * 简要说明：积分交易调整
     * 编写者：陈骑元
     * 创建时间：2019年12月16日 下午10:51:55
     * @param 说明
     * @return 说明
     */
    public static boolean adjust(String memberId,String mertCode,String outletCode,int point){
    	String url = apiUrl + "api/v1/points/adjust";
    	Dto pDto=Dtos.newDto();
    	
    	String transDate= WebplusUtil.getDateStr(WebplusCons.DATETIME);
    	if(WebplusUtil.isEmpty(outletCode)) {
    		outletCode="0001";
    	}
    	pDto.put("transNo", "");
    	pDto.put("mertGroupCode", "1400");
    	pDto.put("mertCode", mertCode);
		pDto.put("outletCode", outletCode);
		pDto.put("transDate",transDate );
		pDto.put("memberNo", memberId);
		pDto.put("exchangeable", true);
    	pDto.put("validDate", "9999-12-31T00:00:00");
    	
		pDto.put("point", point);
		
		Dto memberDto=Dtos.newDto();
		memberDto.put("pointAdjust", pDto);
		HttpHeaders httpHeaders = getHeader();
		log.info("会员编号["+memberId+"]进行积分交换打印："+WebplusJson.toJson(memberDto));
    	try {
    		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<Map<String, Object>>  requestEntity = new HttpEntity<Map<String, Object>>(memberDto, httpHeaders);
			ResponseEntity<JSONObject> responseEntity = getRestTemplate().postForEntity(url,  requestEntity,
					JSONObject.class);
			if (HttpStatus.OK.equals(responseEntity.getStatusCode())) {
				log.info("会员编号[" + memberId + "]积分调整返回信息：" + responseEntity.getBody());
				String pointTransId = responseEntity.getBody().getString("pointTransId");
				if (WebplusUtil.isNotEmpty(pointTransId)) {
					return true;
				}

			}else {
				System.out.println(responseEntity.getBody().getString("errors"));
			}
		} catch (Exception e) {
			log.info("会员编号[" + memberId + "]积分调整失败返回信息："+e);
		}
		return false;
    }
    /**
     * 
     * 简要说明：积分生成
     * 编写者：陈骑元
     * 创建时间：2020年1月28日 下午12:10:33
     * @param 说明
     * @return 说明
     */
    public static boolean earn(String memberId,String mertCode,String outletCode,Integer baseTotal,Integer tax,Integer total){
    	String url = apiUrl + "api/v1/points/earn";
    	if(WebplusUtil.isEmpty(outletCode)) {
    		outletCode="0001";
    	}
    	Dto pDto=Dtos.newDto();
    	String transNo=getTransno();
    	pDto.put("lineNo", 1);
    	pDto.put("mertGroupCode", "1400");
    	pDto.put("mertCode", mertCode);
		pDto.put("outletCode", outletCode);
		pDto.put("date", WebplusUtil.getDateStr("yyyyMMdd"));
		pDto.put("transNo", transNo);
		pDto.put("transType", "1");
		pDto.put("time", WebplusUtil.getDateStr("yyyyMMddHHmmss"));
		pDto.put("baseTotal", baseTotal);
		pDto.put("tax", tax);
		pDto.put("total",total);
		pDto.put("memberNo", memberId);
		pDto.put("paymentType", "11");
		log.info("会员编号[" + memberId + "]积分调整发送参数：" +WebplusJson.toJson(pDto));
		HttpHeaders httpHeaders = getHeader();
		try {
			httpHeaders.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<Map<String, Object>>  requestEntity = new HttpEntity<Map<String, Object>>(pDto, httpHeaders);
			ResponseEntity<JSONObject> responseEntity = getRestTemplate().postForEntity(url,  requestEntity,
					JSONObject.class);
			if (HttpStatus.OK.equals(responseEntity.getStatusCode())) {
				log.info("会员编号[" + memberId + "]积分生成返回信息：" + responseEntity.getBody());
				return true;

			} 
		} catch (Exception e) {
			log.info("会员编号[" + memberId + "]积分生成失败返回信息："+e);
		}
		return false;
    }
    /**
     * 
     * 简要说明：获取交易编号
     * 编写者：陈骑元
     * 创建时间：2019年12月16日 下午10:35:52
     * @param 说明
     * @return 说明
     */
    public static String getTransno(){
    	try {
			String url = apiUrl + "api/v1/purchases/transno";
			HttpHeaders httpHeaders = getHeader();
			httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED); //post表单 ，如果是个json则设置为MediaType.APPLICATION_JSON
			HttpEntity<String> requestEntity = new HttpEntity<String>(null, httpHeaders);
			ResponseEntity<JSONObject> responseEntity = getRestTemplate().exchange(url, HttpMethod.GET, requestEntity,
					JSONObject.class);
			if (HttpStatus.OK.equals(responseEntity.getStatusCode())) {
				log.info("获取交易编号返回信息：" + responseEntity.getBody());
				return responseEntity.getBody().getString("transNo");
			} 
		} catch (Exception e) {
			log.info("获取交易编号失败：" + e);
		}
		return "";
    }
    /**
     * 
     * 简要说明：把JSONObject的数据放入Dto中
     * 编写者：陈骑元
     * 创建时间：2019年12月15日 下午11:52:17
     * @param 说明
     * @return 说明
     */
    @SuppressWarnings("unchecked")
	public static Dto putJSONObjectToDto(JSONObject  jsonObject){
    	  Dto dataDto=Dtos.newDto();
    	 Iterator<String> keyValues = jsonObject.keys();
         while (keyValues.hasNext()) {
             String key = keyValues.next();
             String value = jsonObject.getString(key);
             dataDto.put(key, value);
         }
         return dataDto;
    }
    /**
     * 
     * 简要说明：获取get请求Url地址参数
     * 编写者：陈骑元
     * 创建时间：2019年12月15日 下午11:37:49
     * @param 说明
     * @return 说明
     */
    public static String getUrlParam(Dto pDto){
    	StringBuffer params = new StringBuffer();
		// 设置边界
		for (String key:pDto.keySet()) {
			
			String value=pDto.getString(key);
			params.append(key);
			params.append("=");
			params.append(value);
			params.append("&");
		}

		if (params.length() > 0) {
			params = params.deleteCharAt(params.length() - 1);
		}
		
		return params.toString();
    }
    /**
     * 
     * 简要说明：获取请求head
     * 编写者：陈骑元
     * 创建时间：2019年12月15日 下午10:26:21
     * @param 说明
     * @return 说明
     */
    public static HttpHeaders getHeader(){
    	 String accessToken=getAccessToken();
    	 HttpHeaders headers = new HttpHeaders();
         List<String> cookieList = new ArrayList<String>();
         cookieList.add("accessToken="+accessToken);
         headers.put(HttpHeaders.COOKIE,cookieList); //
         return headers;
    }

}
