package com.ims.system.controller;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ims.core.constant.WebplusCons;
import com.ims.core.vo.R;
import com.ims.core.vo.TreeModel;
import com.ims.core.vo.UserToken;
import com.ims.core.web.BaseController;
import com.ims.system.constant.SystemCons;
import com.ims.system.model.User;
import com.ims.system.service.UserService;
import com.ims.system.util.SystemCache;



/**
 * 
 * 类名:com.ims.system.controller.SystemController
 * 描述:系统控制类
 * 编写者:陈骑元
 * 创建时间:2018年5月4日 下午9:45:59
 * 修改说明:
 */
@Controller
@RequestMapping("/system/main")
public class MainController extends BaseController {
	
	 private String prefix = "system/main/"; 
	
	 @Autowired
     private UserService userService;
	 
	 
	 /**
		 * 
		 * 简要说明：初始化主页面
		 * 编写者：陈骑元 
		 * 创建时间：2018-05-01
		 * @param 说明
		 * @return 说明
		 */
		@PostMapping("initMain")
		public String initMain(String menuType,Model model) {
			UserToken user=this.getUserToken();
			String whetherSuper=WebplusCons.WHETHER_NO;
			if(SystemCons.SUPER_ADMIN.equals(user.getAccount())){
				whetherSuper=WebplusCons.WHETHER_YES;
			}
			List<TreeModel> cardMenuList=SystemCache.getCardMenu(user.getUserId(),menuType,whetherSuper);
			model.addAttribute("user", user);
			model.addAttribute("menuList", cardMenuList);//获取菜单树
			return prefix+"main";
		}
		/**
		 * 
		 * 简要说明：加菜卡片菜单
		 * 编写者：陈骑元
		 * 创建时间：2019年5月8日 下午1:50:28
		 * @param 说明
		 * @return 说明
		 */
		@PostMapping("loadCardMenu")
		@ResponseBody
		public R loadCardMenu(String menuType){
			UserToken user=this.getUserToken();
			String whetherSuper=WebplusCons.WHETHER_NO;
			if(SystemCons.SUPER_ADMIN.equals(user.getAccount())){
				whetherSuper=WebplusCons.WHETHER_YES;
			}
			List<TreeModel> cardMenuList=SystemCache.getCardMenu(user.getUserId(),menuType,whetherSuper);
	    	Set<String> grantSet=SystemCache.getAuthPermissions(user.getUserId(),whetherSuper);
			return R.toDataAndList(grantSet, cardMenuList);
		}
		/**
		 * 
		 * 简要说明：初始化主页面
		 * 编写者：陈骑元 
		 * 创建时间：2018-05-01
		 * @param 说明
		 * @return 说明
		 */
		@GetMapping("mainIndex")
		public String mainIndex() {
			
			return prefix + "mainIndex";
		}
		
		/**
		 * 
		 * 简要说明：解锁屏幕
		 * 编写者：陈骑元 
		 * 创建时间：2018-05-01
		 * @param 说明
		 * @return 说明
		 */
		@PostMapping("unlockScreen")
		@ResponseBody
		public R unlockScreen(String password) {
			UserToken userToken=this.getUserToken();
			User user=userService.selectById(userToken.getUserId());
			if(user.getPassword().equals(password)){
				
				return R.ok();
			}
			return R.warn("パスワードが間違ってます");
		}


}
