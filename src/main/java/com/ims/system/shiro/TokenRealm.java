package com.ims.system.shiro;

import org.apache.shiro.authc.AuthenticationToken;
/**
 * 
 * 类名:com.toonan.shiro.TokenRealm
 * 描述:token认证
 * 编写者:陈骑元
 * 创建时间:2019年1月2日 下午7:40:10
 * 修改说明:
 */
public class TokenRealm  implements AuthenticationToken{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// 密钥
    private String token;

    public TokenRealm(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }



}
