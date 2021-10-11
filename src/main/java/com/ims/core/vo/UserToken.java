package com.ims.core.vo;
/**
 * 
 * 类名:com.toonan.vo.UserToken
 * 描述:
 * 编写者:陈骑元
 * 创建时间:2019年4月25日 下午9:11:52
 * 修改说明:
 */
public class UserToken {
	/**
	 * 用户编号
	 */
	private String userId;
	/***
	 * 用户名称
	 */
	private String username;
	/**
	 * 用户账号
	 */
	private String account;
	/***
	 * 用户token
	 */
	private String token;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	

}
