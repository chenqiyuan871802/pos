package com.ims.buss.print;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ims.core.matatype.Dto;
import com.ims.core.matatype.Dtos;
import com.ims.core.util.WebplusHttp;


/**
 * 
 * 类名:com.ims.buss.print.FzCloudApi
 * 描述:飞猪云API
 * 编写者:陈骑元
 * 创建时间:2019年8月8日 下午11:46:31
 * 修改说明:
 */
@Component
public class FzCloudApi {
	/**
	 * 网关类型 国内，国外
	 */
	public static String GATEWAY_TYPE_CH="1";
	public static String GATEWAY_TYPE_JP="2";
	 // API通信基础地址
    public static String API_URL ;
	 // 日本通信url
    public static String API_JP_URL ;
    // 用户ID
    public static String USER_ID ;
    // API_KEY
    public static String API_KEY;
 
	@Value("${fz.api_url}")
	public  void seApiUrl(String apiUrl) {
		
		FzCloudApi.API_URL=apiUrl;
	}
	@Value("${fz.api_jp_url}")
	public  void seApiJpUrl(String apiJpUrl) {
		
		FzCloudApi.API_JP_URL=apiJpUrl;
	}

	@Value("${fz.user_id}")
	public void setUserId(String userId) {
		FzCloudApi.USER_ID=userId;
	}
	
	@Value("${fz.api_key}")
	public void setApiKey(String apiKey) {
		FzCloudApi.API_KEY=apiKey;
	}
	/**
	 * 
	 * 简要说明：发送钱箱指令
	 * 编写者：陈骑元（chenqiyuan@toonan.com）
	 * 创建时间： 2020年7月29日 下午10:34:50 
	 * @param 说明
	 * @return 说明
	 */
	public static String sendOpenCashbox(String deviceNumber,String gatewayType) {
		Dto paramDto=Dtos.newDto();
        paramDto.put("user_id", USER_ID);
        paramDto.put("device_number", deviceNumber);
        paramDto.put("sign", getSign(paramDto, API_KEY));
        return sendBase("cashbox/add",paramDto,gatewayType);
	}
	 /**
     * 发送打印
     * @param  device_number 设备编号
     * @param  content       打印内容
     * @return               发送状态
     */
    public static String sendPrint(String device_number, String content,String gatewayType){
        Dto paramDto=Dtos.newDto();
        paramDto.put("user_id", USER_ID);
        paramDto.put("device_number", device_number);
        paramDto.put("content", content);
        paramDto.put("sign", getSign(paramDto, API_KEY));
        return sendBase("Task/add",paramDto,gatewayType);
    }

    /**
     * 添加终端设备
     * @param  device_number 设备编号
     * @param  device_secret 设备秘钥
     * @param  device_name   设备名称（自定义）
     * @return               执行状态
     */
    public static String addDevice(String device_number, String device_secret, String device_name,String gatewayType){
       Dto paramDto=Dtos.newDto();
        paramDto.put("user_id", USER_ID);
        paramDto.put("device_number", device_number);
        paramDto.put("device_secret", device_secret);
        paramDto.put("device_name", device_name);
        paramDto.put("sign", getSign(paramDto, API_KEY));
        
        return sendBase("Device/add",paramDto,gatewayType);
    }

    /**
     * 删除终端设备
     * @param  device_number 设备编号
     * @return               执行状态
     */
    public static String delDevice(String device_number,String gatewayType){
       Dto paramDto=Dtos.newDto();
        paramDto.put("user_id", USER_ID);
        paramDto.put("device_number", device_number);
        paramDto.put("sign", getSign(paramDto, API_KEY));
        return sendBase("Device/del",paramDto,gatewayType);
    }

    /**
     * 获取设备状态
     * @param  device_number 设备编号
     * @return               状态
     */
    public static String getDeviceStatus(String device_number,String gatewayType){
        Dto paramDto=Dtos.newDto();
        paramDto.put("user_id", USER_ID);
        paramDto.put("device_number", device_number);
        paramDto.put("sign", getSign(paramDto, API_KEY));
        return sendBase("Device/status",paramDto,gatewayType);
    }

    /**
     * 获取订单打印状态
     * @param  print_id 打印订单ID
     * @return          状态
     */
    public static String getPrintStatus(String print_id,String gatewayType){
       Dto paramDto=Dtos.newDto();
        paramDto.put("user_id", USER_ID);
        paramDto.put("print_id", print_id);
        paramDto.put("sign", getSign(paramDto, API_KEY));
        return sendBase("Task/status",paramDto,gatewayType);
    }

    /**
     * 取消未打印订单
     * @param  print_id 打印订单ID
     * @return          状态
     */
    public static String cancelNotprint(String print_id,String gatewayType){
       Dto paramDto=Dtos.newDto();
        paramDto.put("user_id", USER_ID);
        paramDto.put("print_id", print_id);
        paramDto.put("sign", getSign(paramDto, API_KEY));
        return sendBase("Task/cancel",paramDto,gatewayType);
    }

    /**
     * 取消所有未打印订单
     * @param  device_number 设备编号
     * @return          状态
     */
    public static String cancelNotprintAll(String device_number,String gatewayType){
        Dto paramDto=Dtos.newDto();
        paramDto.put("user_id", USER_ID);
        paramDto.put("device_number", device_number);
        paramDto.put("sign", getSign(paramDto, API_KEY));
        return sendBase("Task/cancelAll",paramDto,gatewayType);
    }
    /**
     * 
     * 简要说明：发送基本接口
     * 编写者：陈骑元
     * 创建时间：2020年2月4日 下午12:17:18
     * @param 说明
     * @return 说明
     */
    private static String sendBase(String url,Dto paramDto,String gatewayType){
    	if(GATEWAY_TYPE_JP.equals(gatewayType)){
    		 return WebplusHttp.doPost(API_JP_URL + url,paramDto);
    	}else{
    		 return WebplusHttp.doPost(API_URL + url,paramDto);
    	}
    }

    private static String getSign(Dto paramDto, String apikey){
        Object[] key_arr = paramDto.keySet().toArray();
        Arrays.sort(key_arr);
        StringBuffer stringBuffer = new StringBuffer();
        for (Object key : key_arr) {
            String val = paramDto.getString(key+"");
            stringBuffer.append("&").append(key).append("=").append(val);
        }
        // API_KEY
        System.out.println("API_KEY【"+API_KEY+"】");
        //即将要签名的字符串
        String preSign=stringBuffer.append("&apikey="+apikey).toString().replaceFirst("&", "");
        System.out.println("即将要签名的字符串【"+preSign+"】");
        //生成签名sign
        String sign=getMD5(preSign);
        System.out.println("签名结果【"+sign+"】");
        return sign;
    }

    /**
     * 计算MD5
     *
     * @param input
     * @return String
     */
    private static String getMD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes("UTF-8"));
            BigInteger number = new BigInteger(1, messageDigest);
            String hashtext = number.toString(16);
            // Now we need to zero pad it if you actually want the full 32 chars.
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
