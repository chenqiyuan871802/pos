package com.ims.buss.print;



import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ims.core.util.WebplusUtil;
import com.ims.core.vo.R;

import net.sf.json.JSONObject;
/**
 * 
 * 类名:com.ims.buss.print.FzCoundUtil
 * 描述:飞猪工具类
 * 编写者:陈骑元
 * 创建时间:2019年8月8日 下午11:59:00
 * 修改说明:
 */
public class FzCloudUtil {
	
	private static Log log = LogFactory.getLog(FzCloudUtil.class);
	
	 /**
     * 发送打印
     * @param  device_number 设备编号
     * @param  content       打印内容
     * @return               发送状态
     */
    public static boolean sendPrint(String deviceNumber, String content,String gatewayType){
    	String jsonStr=FzCloudApi.sendPrint(deviceNumber, content,gatewayType);
    	log.info("飞猪云发送设备["+deviceNumber+"]打印信息："+content+",返回信息："+jsonStr);
    	JSONObject json = JSONObject.fromObject(jsonStr);
     	String code=json.getString("code");
     	 if("1".equals(code)){
     		   
     		 return true;
     	 }
     	 log.error("飞猪云发送设备["+deviceNumber+"]打印失败："+json.getString("message"));
     	 return false;
    }
    
    /**
     * 发送打印
     * @param  device_number 设备编号
     * @param  content       打印内容
     * @return               发送状态
     */
    public static R sendPrintMessage(String deviceNumber, String content,String gatewayType){
    	try {
			String jsonStr = FzCloudApi.sendPrint(deviceNumber, content,gatewayType);
			log.info("飞猪云发送设备[" + deviceNumber + "]打印信息：" + content + ",返回信息：" + jsonStr);
			if (WebplusUtil.isNotEmpty(jsonStr)) {
				if (jsonStr.indexOf("code") > -1) {
					JSONObject json = JSONObject.fromObject(jsonStr);
					String code = json.getString("code");
					if ("1".equals(code)) {

						return R.ok().put("printId", json.getString("print_id"));
					}
					String errorMessage = json.getString("message");
					log.error("飞猪云发送设备[" + deviceNumber + "]打印失败：" + errorMessage + "打印内容：" + content);
					return R.error().put("errorMessage", errorMessage);
				}

			} 
		} catch (Exception e) {
			
		}
		log.error("飞猪云发送设备["+deviceNumber+"]打印失败：网络故障异常;打印内容："+content);
     	 return R.error().put("errorMessage", "网络故障异常");
    }
	 /**
     * 添加终端设备
     * @param  device_number 设备编号
     * @param  device_secret 设备秘钥
     * @param  device_name   设备名称（自定义）
     * @return               执行状态
     */
    public static R addDevice(String deviceNumber, String deviceSecret, String deviceName,String gatewayType){
       String jsonStr=FzCloudApi.addDevice(deviceNumber, deviceSecret, deviceName,gatewayType);
        log.info("飞猪云添加设备["+deviceNumber+"]授权返回信息："+jsonStr);
        JSONObject json = JSONObject.fromObject(jsonStr);
 	   String code=json.getString("code");
 	   if("1".equals(code)){
 		   
 		   return R.ok();
 	   }
 	   String msg="飞猪云添加设备["+deviceNumber+"]授权失败："+json.getString("message");
 	  log.error(msg);
 	   return R.error(msg);
    }

    /**
     * 删除终端设备
     * @param  device_number 设备编号
     * @return               执行状态
     */
    public static R  delDevice(String deviceNumber,String gatewayType){
		String jsonStr = FzCloudApi.delDevice(deviceNumber,gatewayType);
		log.info("飞猪云删除设备[" + deviceNumber + "]授权返回信息：" + jsonStr);
		JSONObject json = JSONObject.fromObject(jsonStr);
		String code = json.getString("code");
		if ("1".equals(code)) {

			return R.ok();
		}
		String msg="飞猪云删除设备[" + deviceNumber + "]授权失败：" + json.getString("message");
		 log.error(msg);
		return R.error(msg);
    }

	
	 /**
     * 获取设备状态
     * @param  deviceNumber 设备编号
     * @return               状态
     */
    public static R getDeviceStatus(String deviceNumber,String gatewayType){
       String jsonStr=FzCloudApi.getDeviceStatus(deviceNumber,gatewayType);
       log.info("获取设备["+deviceNumber+"]状态返回信息："+jsonStr);
       if(WebplusUtil.isNotEmpty(jsonStr)){
    	   JSONObject json = JSONObject.fromObject(jsonStr);
    	   String code=json.getString("code");
    	   if("1".equals(code)){
    		   
    		   return R.ok().put("status", json.getString("status"));
    	   }
    	   String message=json.getString("message");
    	   return R.error(message);
       }
       
       return R.error("获取设备状态失败");
       
       
    }
    /**
     * 
     * 简要说明：
     * 编写者：陈骑元（chenqiyuan@toonan.com）
     * 创建时间： 2020年7月29日 下午10:36:58 
     * @param 说明
     * @return 说明
     */
    public static R sendOpenCashbox(String deviceNumber,String gatewayType) {
    	String jsonStr=FzCloudApi.sendOpenCashbox(deviceNumber, gatewayType);
        log.info("发送钱箱设备["+deviceNumber+"]返回信息："+jsonStr);
        if(WebplusUtil.isNotEmpty(jsonStr)){
     	   JSONObject json = JSONObject.fromObject(jsonStr);
     	   String code=json.getString("code");
     	   if("1".equals(code)){
     		   
     		   return R.ok();
     	   }
     	   String message=json.getString("message");
     	   return R.error(message);
        }
        
        return R.error();
    }

}
