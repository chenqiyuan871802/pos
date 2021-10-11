package com.ims.core.web;


import org.springframework.beans.factory.annotation.Autowired;

import com.ims.core.cache.WebplusCache;
import com.ims.core.constant.WebplusCons;
import com.ims.core.matatype.Dto;
import com.ims.core.util.WebplusUtil;
import com.ims.core.vo.UserToken;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * 类名:com.ims.common.web.BaseController
 * 描述:基础控制类
 * 编写者:陈骑元
 * 创建时间:2018年4月6日 下午11:15:54
 * 修改说明:
 */
public class BaseController {
    @Autowired
    protected  HttpServletRequest request;
    @Autowired
    protected HttpServletResponse response;
    
    protected org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(this.getClass());
   
    /**
     * 
     * 简要说明：获取token用户
     * 编写者：陈骑元
     * 创建时间：2018年12月22日 下午2:46:39
     * @param 说明
     * @return 说明
     */
    public  UserToken getUserToken(HttpServletRequest httpRequest){
    	String token=httpRequest.getParameter(WebplusCons.TOKEN_PARAM);
    	return getUserToken(token);
    }
    /**
     * 
     * 简要说明：获取token用户
     * 编写者：陈骑元
     * 创建时间：2018年12月22日 下午2:46:39
     * @param 说明
     * @return 说明
     */
    public  UserToken getUserToken(Dto pDto){
    	String token=pDto.getString(WebplusCons.TOKEN_PARAM);
    	return getUserToken(token);
    }
    /**
     * 
     * 简要说明：获取token用户
     * 编写者：陈骑元
     * 创建时间：2018年12月22日 下午2:46:39
     * @param 说明
     * @return 说明
     */
    public  UserToken getUserToken(){
    	String token=request.getParameter(WebplusCons.TOKEN_PARAM);
    	
    	return getUserToken(token);
    }
    /**
     * 
     * 简要说明：返回token
     * 编写者：陈骑元
     * 创建时间：2018年12月22日 下午2:45:03
     * @param 说明
     * @return 说明
     */
    public  UserToken getUserToken(String token){
    	UserToken user=null;
    	if(WebplusUtil.isNotEmpty(token)){
          user=WebplusCache.getUserToken(token);
    	}
        return user;
    }
    /**
     * 
     * 简要说明：获取token用户编号
     * 编写者：陈骑元
     * 创建时间：2018年12月22日 下午2:51:26
     * @param 说明
     * @return 说明
     */
    public String getUserId(){
    	String token=request.getParameter(WebplusCons.TOKEN_PARAM);
    	return getUserId(token);
    }
    
    
    /**
     * 
     * 简要说明：获取token用户编号
     * 编写者：陈骑元
     * 创建时间：2018年12月22日 下午2:49:22
     * @param 说明
     * @return 说明
     */
    public String getUserId(String token){
    	UserToken user=getUserToken(token);
    	if(WebplusUtil.isNotEmpty(user)){
    		
    		return user.getUserId();
    	}
    	return "";
    }
   /**
    * 
    * 简要说明：返回token
    * 编写者：陈骑元
    * 创建时间：2018年12月24日 下午9:24:25
    * @param 说明
    * @return 说明
    */
   public String getToken(){
	   
   	String token=request.getParameter(WebplusCons.TOKEN_PARAM);
   	
   	return token;
   }
   


}
