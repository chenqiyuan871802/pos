package com.ims.buss.print;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ims.buss.constant.BussCons;
import com.ims.core.cache.WebplusCache;
import com.ims.core.util.WebplusUtil;
import com.ims.core.vo.R;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;


/***
 * 
 * 类名:com.ims.buss.lav.LavUtil
 * 描述:易联云操作工具类
 * 编写者:陈骑元
 * 创建时间:2019年8月4日 下午12:28:30
 * 修改说明:
 */
@Component
public class LavUtil {
	
	private static Log log = LogFactory.getLog(LavUtil.class);
	
	/**
     * 易联云颁发给开发者的应用ID 非空值
     */
	public static String CLIENT_ID;
	/**
     * 易联云颁发给开发者的应用secret 非空值
     */
	public static String CLIENT_SECRET;
	/**
	 * 易云联连接地址
	 */
    public static String MAIN_HOST_URL;

	@Value("${lav.client_id}")
	public  void setClientId(String clientId) {
		LavUtil.CLIENT_ID = clientId;
	}

	@Value("${lav.client_secret}")
	public void setClientSecret(String clientSecret) {
		LavUtil.CLIENT_SECRET=clientSecret;
	}
	
	@Value("${lav.host_url}")
	public void setHostUrl(String  hostUrl) {
		LavUtil.MAIN_HOST_URL=hostUrl;
	}
	
	/**
	 * 
	 * 简要说明：获取AccessToken
	 * 编写者：陈骑元
	 * 创建时间：2019年8月4日 下午12:59:47
	 * @param 说明
	 * @return 说明
	 */
	public static String getAccessToken(){
		String token=WebplusCache.getString(BussCons.LAV_ACCESS_TOKEN);
		if(WebplusUtil.isEmpty(token)){
			token=getFreedomToken();
			
		}
		return token;
		
	}
	/**
	 * 
	 * 简要说明：自由应用获取token
	 * 编写者：陈骑元
	 * 创建时间：2019年8月4日 下午12:51:45
	 * @param 说明
	 * @return 说明
	 */
    public static String getFreedomToken() {
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        String result = LavApi.getToken(CLIENT_ID,
                "client_credentials",
                LavApi.getSin(timestamp),
                "all",
                timestamp,
                LavApi.getuuid());
         log.info("自由应用服务获取token接口返回数据："+result);
        try {
            JSONObject json = JSONObject.fromObject(result);
            String error=json.getString("error");
            String errorDescription=json.getString("error_description");
            if("0".equals(json.getString("error"))){
            	   JSONObject body = json.getJSONObject("body");
                  String accessToken= body.getString("access_token");
                  if(WebplusUtil.isNotEmpty(accessToken)){
      				WebplusCache.setString(BussCons.LAV_ACCESS_TOKEN, accessToken, 0);
      				return accessToken;
      			  }
                 
            }
            log.error("获取自由开发的accessToken出错：错误代码="+error+",错误描述="+errorDescription);
         
        } catch (JSONException e) {
            e.printStackTrace();
          log.error(("getFreedomToken出现Json异常！" + e));
        }
        return result;
    }
    
    /**
     * 
     * 简要说明：发送打印
     * 编写者：陈骑元
     * 创建时间：2019年8月4日 下午1:38:11
     * @param 说明
     * @return 说明
     */
    public static boolean sendPrint(String machine_code, String content, String origin_id) {
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        String token=getAccessToken();
        if(WebplusUtil.isEmpty(token)){
        	 log.error("发送打印失败：获取授权访问accessToken为空");    
             return false;
        }
         String result= LavApi.print(CLIENT_ID,token,machine_code, content,origin_id,LavApi.getSin(timestamp),LavApi.getuuid(), timestamp);
         log.info("发送易联云打印机["+machine_code+"]打印,发送内容："+content+",返回信息："+result);   
         JSONObject json = JSONObject.fromObject(result);
         String error=json.getString("error");
         String errorDescription=json.getString("error_description");
         if("0".equals(error)){
         	  
               return true;
         }
         if("18".equals(error)){  //token超时重新发送
        	 token=getFreedomToken();
            result= LavApi.print(CLIENT_ID,token,machine_code, content,origin_id,LavApi.getSin(timestamp),LavApi.getuuid(), timestamp);
            log.info("token失败，重新发送易联云打印机["+machine_code+"]打印,发送内容："+content+",返回信息："+result); 
            return true;
         }
         
         log.error("发送易联云打印机["+machine_code+"]失败：错误代码="+error+",错误描述="+errorDescription);
         return false;
    }
    /**
     * 
     * 简要说明：添加终端授权 开放应用服务模式不需要此接口 ,自有应用服务模式所需参数
     * 编写者：陈骑元
     * 创建时间：2019年8月4日 下午1:03:45
     * @param 说明  machine_code 机器终端号，msign 授权秘钥
     * @return 说明
     */
    public static  R addPrinter(String machine_code, String msign) {
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        String token=getAccessToken();
        if(WebplusUtil.isEmpty(token)){
        	 log.error("添加终端授权失败：获取授权访问accessToken为空");    
             return R.error("添加终端授权失败：获取授权访问accessToken为空");
        }
        String result=LavApi.addPrinter(CLIENT_ID,machine_code,msign,token,LavApi.getSin(timestamp),LavApi.getuuid(),timestamp);
        log.info("添加终端授权返回信息："+result);   
        JSONObject json = JSONObject.fromObject(result);
        String error=json.getString("error");
        String errorDescription=json.getString("error_description");
        if("0".equals(json.getString("error"))){
        	  
              return R.ok();
        }
        String msg="添加终端授权失败：错误代码="+error+",错误描述="+errorDescription;
        log.error(msg);
        return R.error(msg);
        	
              
    }
    /**
     * 
     * 简要说明：删除终端授权
     * 编写者：陈骑元
     * 创建时间：2019年8月4日 下午1:12:43
     * @param 说明
     * @return 说明
     */
    public static R deletePrinter(String machine_code) {
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        String token=getAccessToken();
        if(WebplusUtil.isEmpty(token)){
        	 log.error("删除终端授权失败：获取授权访问accessToken为空");    
             return R.error("删除终端授权失败：获取授权访问accessToken为空");
        }
        String result=LavApi.deletePrinter(CLIENT_ID, token, machine_code, LavApi.getSin(timestamp), LavApi.getuuid(), timestamp);
        log.info("删除终端授权返回信息："+result);   
        JSONObject json = JSONObject.fromObject(result);
        String error=json.getString("error");
        String errorDescription=json.getString("error_description");
        if("0".equals(json.getString("error"))){
        	  
              return R.ok();
        }
        String msg="删除终端授权失败：错误代码="+error+",错误描述="+errorDescription;
        log.error(msg);
        return R.error(msg);
    }
    
    
}
