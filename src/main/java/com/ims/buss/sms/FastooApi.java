package com.ims.buss.sms;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ims.core.matatype.Dto;
import com.ims.core.matatype.Dtos;
import com.ims.core.util.WebplusHttp;


/**
 * 
 * 类名:com.ims.buss.sms.FastooApi
 * 描述:Fastoo SMS短信API
 * 编写者:陈骑元
 * 创建时间:2019年12月14日 上午11:49:26
 * 修改说明:
 */
@Component
public class FastooApi {
	
	
	 // API通信基础地址
    public static String API_URL ;
    
    // API_KEY
    public static String API_KEY;
    
    @Value("${sms.api_url}")
	public  void seApiUrl(String apiUrl) {
		
    	FastooApi.API_URL=apiUrl;
	}

  
	@Value("${sms.api_key}")
	public void setApiKey(String apiKey) {
		FastooApi.API_KEY=apiKey;
	}
	/**
	 * 
	 * 简要说明：发送短信记录
	 * 编写者：陈骑元
	 * 创建时间：2019年12月14日 下午2:58:44
	 * @param 说明 mobile 手机号码，多个号码逗号分隔，msg 短信内容
	 * @return 说明
	 */
    public static String sendSms(String mobile,String msg){
    	Dto pDto=Dtos.newDto();
    	pDto.put("apikey", FastooApi.API_KEY);
    	pDto.put("da", mobile);
    	pDto.put("msg", msg);
    	
    	return WebplusHttp.doPost(API_URL+"v1/submit.json", pDto);
    }
    
    
}
