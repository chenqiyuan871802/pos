package com.ims.system.shiro;


import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;

import com.ims.core.constant.WebplusCons;
import com.ims.core.util.WebplusUtil;
import com.ims.core.vo.R;


/**
 * 
 * 类名:com.toonan.shiro.filter.TokenFilter
 * 描述:token校验器
 * 编写者:陈骑元
 * 创建时间:2019年1月2日 下午7:50:57
 * 修改说明:
 */
public class TokenFilter extends AuthenticatingFilter {
	
	/**
     * 父类会在请求进入拦截器后调用该方法，返回true则继续，返回false则会调用onAccessDenied()。这里在不通过时，还调用了isPermissive()方法，我们后面解释。
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        if(this.isLoginRequest(request, response))
            return true;
        boolean allowed = false;
        try {
            allowed = executeLogin(request, response);
        } catch(IllegalStateException e){ //not found any token
        }catch (Exception e) {
        }
        return allowed || super.isPermissive(mappedValue);
    }
    /**
     * 这里重写了父类的方法，使用我们自己定义的Token类，提交给shiro。这个方法返回null的话会直接抛出异常，进入isAccessAllowed（）的异常处理逻辑。
     */
    @Override
    protected AuthenticationToken createToken(ServletRequest servletRequest, ServletResponse servletResponse) {
    	
    	HttpServletRequest req = (HttpServletRequest) servletRequest;
         String authorization = req.getParameter(WebplusCons.TOKEN_PARAM);
         if(WebplusUtil.isNotEmpty(authorization)){
        	 TokenRealm token = new  TokenRealm(authorization);
        	 return token;
         }

        return null;
    }
    /**
      * 如果这个Filter在之前isAccessAllowed（）方法中返回false,则会进入这个方法。我们这里直接返回错误的response
      */
    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
    	HttpServletRequest req = (HttpServletRequest) servletRequest;
    	HttpServletResponse response = (HttpServletResponse) servletResponse;
        String token = req.getParameter(WebplusCons.TOKEN_PARAM);
        R r=null;
        if(WebplusUtil.isEmpty(token)){
        	r= R.error(WebplusCons.EMPTY_TOKEN,"アクセスエラ(Unauthorized)");
        }else{
        	r= R.error(WebplusCons.ERROR_TOKEN,"アクセスエラ(Unauthorized)：token無効");
        }
        WebplusUtil.writeObject(response, r);  
        return false;
    }
    /**
     *  如果Shiro Login认证成功，会进入该方法，等同于用户名密码登录成功，我们这里还判断了是否要刷新Token
     */
    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) throws Exception {
       
        return true;
    }
    /**
      * 如果调用shiro的login认证失败，会回调这个方法，这里我们什么都不做，因为逻辑放到了onAccessDenied（）中。
      */
    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
      
        return false;
    }
}
