package com.ims.buss.sms;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ims.buss.constant.BussCons;
import com.ims.core.util.WebplusRegex;
import com.ims.core.util.WebplusUtil;
import com.ims.core.vo.R;

import net.sf.json.JSONObject;

/**
 * 
 * 类名:com.ims.buss.sms.FastooUtil
 * 描述:Fastoo短信工具类
 * 编写者:陈骑元
 * 创建时间:2019年12月14日 下午10:44:07
 * 修改说明:
 */
public class FastooUtil {
	
	private static Log log = LogFactory.getLog(FastooUtil.class);
	/**
	 * 
	 * 简要说明：发送短信
	 * 编写者：陈骑元
	 * 创建时间：2019年12月14日 下午10:45:33
	 * @param 说明
	 * @return 说明
	 */
	public static R sendSms(String mobile,String msg){
	     if(WebplusUtil.isEmpty(mobile)){
	    	 
	    	 return R.error("手机号码不能为空");
	     }
	     if(WebplusUtil.isEmpty(msg)){
	    	 
	    	 return R.error("短信内容不能为空");
	     }
	     int len=mobile.length();
	     if(len==11&&!mobile.startsWith("0")){  //中国手机号码
	    	 if(!WebplusRegex.isMobile(mobile)){
	    		 return R.error("手机号码校验不合法");
	    	 }
	     }else{
	    	 if(!WebplusRegex.isJapanMobile(mobile)){
	    		 return R.error("手机号码校验不合法");
	    	 }
	    	 if(mobile.startsWith("0")){  //如果前缀包含0，则去掉
	    		 mobile=mobile.substring(1);
	    	 }
	    	 mobile=BussCons.AREA_CODE_JP+mobile;
	     }
	     
	     String jsonStr=FastooApi.sendSms(mobile, msg);
	     log.info("发送手机["+mobile+"]返回信息："+jsonStr+":其中发送内容="+msg);
	     if(WebplusUtil.isEmpty(jsonStr)){
	    	 
	    	 return R.error();
	     }
	     JSONObject json = JSONObject.fromObject(jsonStr);
	     String code=json.getString("code");
	     if(FastooCons.SUCCESS.equals(code)){
	     		   
	    	 JSONObject data =json.getJSONObject("data");
	    	 
	    	 return R.ok().put("msgid", data.getString("msgid"));
	     }
	     log.error("发送短信失败：错误码code="+code+";错误信息："+FastooCons.errorDto.getString(code));
	     return R.error();
	     
		
	}

}
