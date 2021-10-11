package com.ims.buss.system;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.ims.core.cache.WebplusCache;
import com.ims.core.constant.WebplusCons;
import com.ims.core.matatype.Dto;
import com.ims.core.matatype.Dtos;
import com.ims.core.util.WebplusFormater;
import com.ims.core.util.WebplusHashCodec;
import com.ims.core.util.WebplusQrcode;
import com.ims.core.util.WebplusSqlHelp;
import com.ims.core.util.WebplusUtil;
import com.ims.core.vo.R;
import com.ims.core.vo.UserToken;

import java.io.File;
import java.util.Date;
import java.util.List;


import com.ims.buss.constant.BussCons;
import com.ims.buss.model.Shop;
import com.ims.buss.service.ShopService;
import org.springframework.stereotype.Controller;
import com.ims.core.web.BaseController;
import com.ims.system.constant.SystemCons;
import com.ims.system.model.RoleUser;
import com.ims.system.model.User;
import com.ims.system.service.RoleService;
import com.ims.system.service.UserService;

/**
 * <p>
 * 店铺表 前端控制器
 * </p>
 *
 * @author 陈骑元
 * @since 2019-07-20
 */
@Controller
@RequestMapping("/buss/shop")
public class ShopController extends BaseController {

    @Autowired
    private ShopService shopService;
    
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;


	/**
	 * 
	 * 简要说明：分页查询 
	 * 编写者：陈骑元
	 * 创建时间：2019-07-20
	 * @param 说明
	 * @return 说明
	 */
	@RequestMapping("list")
	@ResponseBody
	public R list() {
		Dto pDto = Dtos.newDto(request);
		pDto.put("whetherRemove", WebplusCons.WHETHER_NO);
		pDto.setOrder(" create_time DESC ");
		Page<Shop> page =shopService.likePage(pDto);
		return R.toPage(page);
	}



	/**
	 * 
	 * 简要说明： 新增信息保存 
	 * 编写者：陈骑元
	 * 创建时间：2019-07-20
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("save")
	@ResponseBody
	public R save(Shop shop,String account,String password) {
		EntityWrapper<User> countWrapper = new EntityWrapper<User>();
		WebplusSqlHelp.eq(countWrapper, "account", account);
		WebplusSqlHelp.eq(countWrapper, "is_del",WebplusCons.WHETHER_NO);
		int count=userService.selectCount(countWrapper);
		if(count>0){
			return R.warn("该账号已被注册，请注册其它账号。");
		}
		String currentUserId=this.getUserId();
		Date currentTime=WebplusUtil.getDateTime();
		String shopId=WebplusUtil.uuid();
		shop.setShopId(shopId);
		shop.setCreateBy(currentUserId);
		shop.setUpdateBy(currentUserId);
		shop.setCreateTime(currentTime);
		shop.setUpdateTime(currentTime);
	    String filePath=WebplusCache.getParamValue(WebplusCons.SAVE_ROOT_PATH_KEY)+File.separator+BussCons.QRCODE_IMAGE;
	    String reqestUrl=WebplusCache.getParamValue(WebplusCons.REQUEST_URL_KEY)+"/h5/index.html?shopId="+shopId;
	    String fileName=WebplusQrcode.encode(reqestUrl, filePath);
	    shop.setShopQrcode(fileName);
		boolean result = shopService.insert(shop);
		if (result) {
			User user=new User();
			user.setUserId(shopId);
			user.setAccount(account);
			user.setUsername(shop.getShopName());
			user.setCreateBy(currentUserId);
			user.setUpdateBy(currentUserId);
			user.setCreateTime(currentTime);
			user.setUpdateTime(currentTime);
			user.setUserType(SystemCons.USER_TYPE_SHOP);
			String encryptPassword=WebplusHashCodec.md5(password);
			user.setPassword(encryptPassword);
		    userService.insert(user);
		    //保存角色用户信息
		    RoleUser roleUser=new RoleUser();
		    roleUser.setRoleId(BussCons.SHOP_ROLE_ID);
		    roleUser.setUserId(shopId);
		    roleUser.setCreateBy(currentUserId);
		    roleUser.setCreateTime(currentTime);
		    roleService.saveRoleUser(roleUser);
			return R.ok();
		} else {
			return R.error();
		}

	}
	/**
	 * 
	 * 简要说明： 跳转到编辑页面 
	 * 编写者：陈骑元
	 * 创建时间：2019-07-20
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("edit")
	@ResponseBody
	public R edit(String id) {
		Shop shop=shopService.selectById(id);
	    User user=userService.selectById(id);
	    Dto dataDto=Dtos.newDto();
	    WebplusUtil.copyProperties(shop, dataDto);
	    dataDto.put("account", user.getAccount());
		return R.toData(dataDto);
	}
	
	/**
	 * 
	 * 简要说明：修改信息
	 * 编写者：陈骑元
	 * 创建时间：2019-07-20
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("update")
	@ResponseBody
	public R update(Shop shop,String account) {
		String id=shop.getShopId();
		User user=userService.selectById(id);
		if(!account.equals(user.getAccount())){
			EntityWrapper<User> countWrapper = new EntityWrapper<User>();
			WebplusSqlHelp.eq(countWrapper, "account", account);
			WebplusSqlHelp.eq(countWrapper, "is_del",WebplusCons.WHETHER_NO);
			int count=userService.selectCount(countWrapper);
			if(count>0){
				return R.warn("该账号已被注册，请注册其它账号。");
			}
		}
		String currentUserId=this.getUserId();
		Date currentTime=WebplusUtil.getDateTime();
		user.setAccount(account);
		user.setUsername(shop.getShopName());
		user.setUpdateBy(currentUserId);
		user.setUpdateTime(currentTime);
		userService.updateById(user);
		shop.setUpdateBy(currentUserId);
		shop.setUpdateTime(currentTime);
		boolean result = shopService.updateById(shop);
		if (result) {
			return R.ok();
		} else {
			return R.error();
		}
		
	}
	
	
	/**
	 * 
	 * 简要说明：删除信息
	 * 编写者：陈骑元
	 * 创建时间：2019-07-20
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("remove")
	@ResponseBody
	public R remove(String id) {
		String userId=this.getUserId();
		Date currentTime=WebplusUtil.getDateTime();
		Shop shop=new Shop();
		shop.setShopId(id);
		shop.setWhetherRemove(WebplusCons.WHETHER_YES);
		shop.setUpdateBy(userId);
		shop.setUpdateTime(currentTime);
		
		User user=new User();
		user.setUserId(id);
		user.setIsDel(WebplusCons.WHETHER_YES);
		user.setUpdateBy(userId);
		user.setUpdateTime(currentTime);
		userService.updateById(user);
		boolean result = shopService.updateById(shop);
		if (result) {
			return R.ok();
		} else {
			return R.error();
		}
		
	}
	
	/**
	 * 
	 * 简要说明：批量删除信息
	 * 编写者：陈骑元
	 * 创建时间：2019-07-20
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("batchRemove")
	@ResponseBody
	public R batchRemove(String ids) {
		List<String> idList=WebplusFormater.separatStringToList(ids);
		boolean result = shopService.deleteBatchIds(idList);
		if (result) {
			return R.ok();
		} else {
			return R.error();
		}
		
	}
	/**
	 * 
	 * 简要说明：调到店铺详情页面
	 * 编写者：陈骑元
	 * 创建时间：2019-07-20
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("show")
	@ResponseBody
	public R show() {
		UserToken userToken=this.getUserToken()	;
		Shop shop=shopService.selectById(userToken.getUserId());
	    Dto dataDto=Dtos.newDto();
	    WebplusUtil.copyProperties(shop, dataDto);
	    dataDto.put("account", userToken.getAccount());
		return R.toData(dataDto);
	}
	
	/**
	 * 
	 * 简要说明：删除信息
	 * 编写者：陈骑元
	 * 创建时间：2019-07-20
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("updateShopStatus")
	@ResponseBody
	public R updateShopStatus(String shopStatus) {
		String userId=this.getUserId();
		
		Shop shop=new Shop();
		shop.setShopId(userId);
		shop.setShopStatus(shopStatus);
		shop.setUpdateBy(userId);
		shop.setUpdateTime(WebplusUtil.getDateTime());
		boolean result = shopService.updateById(shop);
		if (result) {
			return R.ok();
		} else {
			return R.error();
		}
		
	}
}

