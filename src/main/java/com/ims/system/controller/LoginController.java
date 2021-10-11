package com.ims.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ims.core.cache.WebplusCache;
import com.ims.core.constant.WebplusCons;
import com.ims.core.vo.R;
import com.ims.core.web.BaseController;
import com.ims.system.service.UserService;



/**
 * 
 * 类名:com.toonan.system.controller.LoginController
 * 描述:登录控制类
 * 编写者:陈骑元
 * 创建时间:2018年12月15日 下午1:57:09
 * 修改说明:
 */
@Controller
public class LoginController extends BaseController{
	
	
	 @Autowired
	 private UserService userService;
	/*
	 *//**
	  * 
	  * 简要说明：登录首页
	  * 编写者：陈骑元
	  * 创建时间：2019年1月9日 上午9:45:40
	  * @param 说明
	  * @return 说明
	  */
	 
	 @GetMapping({ "/", "index" })
     public String initLogin(Model model) {
			return "forward:login.html";
     }
	  
	
	
	/**
	 *
	 * 简要说明：开始登陆
	 * 编写者：陈骑元 
	 * 创建时间：2018-04-12
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("doLogin")
	@ResponseBody
	public R doLogin(String account,String password) {
		
		R r=userService.doLogin(account, password,WebplusCons.WHETHER_YES);
		
		return r;
	}
	
	
	/**注销并安全退出系统
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	
	@PostMapping("logout")
	@ResponseBody
	R logout(String token) {
		WebplusCache.removeToken(token);
		return R.ok();
	}

	/**
	 * 
	 * 简要说明：403未授权
	 * 编写者：陈骑元
	 * 创建时间：2019年1月9日 上午11:27:43
	 * @param 说明
	 * @return 说明
	 */
	@GetMapping("/401")
	String error401() {
		return "forward:error/401.html";
	}
	
}
