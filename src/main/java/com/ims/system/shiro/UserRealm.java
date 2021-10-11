package com.ims.system.shiro;


import java.util.HashSet;
import java.util.Set;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.stereotype.Service;

import com.ims.core.cache.WebplusCache;
import com.ims.core.constant.WebplusCons;
import com.ims.core.util.WebplusUtil;
import com.ims.core.vo.UserToken;
import com.ims.system.constant.SystemCons;
import com.ims.system.util.SystemCache;
@Service
public class UserRealm extends AuthorizingRealm{
	
	
	  /**
     * 大坑，必须重写此方法，不然Shiro会报错
     */
    @Override
    public boolean supports(AuthenticationToken token) {
    	
        return token instanceof TokenRealm;
    }
    /**
     * 访问权限控制
     */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		String token = principals.toString();
	    UserToken user=WebplusCache.getUserToken(token);
		Set<String> perms =new HashSet<String>();
	    if(WebplusUtil.isNotEmpty(user)){
	        String whetherSuper=WebplusCons.WHETHER_NO;
	        if(SystemCons.SUPER_ADMIN.equals(user.getAccount())){
	        	whetherSuper=WebplusCons.WHETHER_YES;
	        }
	    	perms=SystemCache.getAuthPermissions(user.getUserId(),whetherSuper);
	    }
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		info.setStringPermissions(perms);
		return info;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authToken) throws AuthenticationException {
		String token = (String) authToken.getCredentials();
		if(WebplusUtil.isEmpty(token)){
			throw new AuthenticationException("token为空，系统拒绝访问");
		}
		if(WebplusCache.checkAndRefreshToken(token)){
			return new SimpleAuthenticationInfo(token, token, "userRealm");
		}
		throw new AuthenticationException("token过期或无效");
		
		
	}

}
