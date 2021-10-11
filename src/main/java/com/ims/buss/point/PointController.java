package com.ims.buss.point;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.ims.buss.constant.BussCons;
import com.ims.buss.model.Member;
import com.ims.buss.model.MemberCoupon;
import com.ims.buss.model.Shop;
import com.ims.buss.model.ShopCoupon;
import com.ims.buss.service.MemberCouponService;
import com.ims.buss.service.MemberService;
import com.ims.buss.service.PointService;
import com.ims.buss.service.ShopCouponService;
import com.ims.buss.service.ShopService;
import com.ims.buss.util.PointApiUtil;
import com.ims.core.cache.WebplusCache;
import com.ims.core.constant.WebplusCons;
import com.ims.core.matatype.Dto;
import com.ims.core.matatype.Dtos;
import com.ims.core.util.WebplusFile;
import com.ims.core.util.WebplusHashCodec;
import com.ims.core.util.WebplusQrcode;
import com.ims.core.util.WebplusRegex;
import com.ims.core.util.WebplusSqlHelp;
import com.ims.core.util.WebplusUtil;
import com.ims.core.vo.R;
import com.ims.core.vo.UserToken;
import com.ims.core.web.BaseController;
import com.ims.system.constant.SystemCons;

/**
 * 
 * 类名:com.ims.buss.point.PointController
 * 描述:积分控制类
 * 编写者:陈骑元
 * 创建时间:2019年12月15日 上午8:45:25
 * 修改说明:
 */
@Controller
@RequestMapping("/point")
public class PointController extends BaseController {
	
	@Autowired
	private PointService pointService;
	
	@Autowired
	private MemberService memberService;
	@Autowired
	private ShopCouponService shopCouponService;
	@Autowired
	private ShopService shopService;
	@Autowired
	private MemberCouponService memberCouponService;
	
	/**
	 * 
	 * 简要说明：登录账号
	 * 编写者：陈骑元
	 * 创建时间：2019年12月15日 下午2:31:18
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("login")
	@ResponseBody
	public R  login(String account,String password,String shopId){
        if(WebplusUtil.isEmpty(account)){
			
			return R.error("アカウントを入力してください");
		}
		if(WebplusUtil.isEmpty(password)){
			
			return R.error("パスワードを入力してください");
		}
		EntityWrapper<Member> memberWrapper = new EntityWrapper<Member>();
		WebplusSqlHelp.eq(memberWrapper, "binary mobile", account);
		Member member=memberService.selectOne(memberWrapper);
		if(WebplusUtil.isEmpty(member)){
			
			return R.error("このアカウントは存在しません");
		}
		if(SystemCons.USER_STATUS_LOCK.equals(member.getStatus())){
			
			return R.error("アカウントがロックされます");
		}
		password=WebplusHashCodec.encrypt(password);
		if(!password.equals(member.getPassword())){
			
			return R.error("パスワードが間違っています");
		}
		UserToken userToken=new UserToken();
		userToken.setAccount(member.getMobile());
		userToken.setUserId(member.getMemberId());
		userToken.setUsername(member.getUsername());
		String token=WebplusCache.createToken(userToken);
		userToken.setToken(token);
		
		Dto dataDto=Dtos.newDto();
		WebplusUtil.copyProperties(userToken, dataDto);
		if(WebplusUtil.isNotEmpty(shopId)) {
			Shop shop=shopService.selectById(shopId);
			if(WebplusUtil.isNotEmpty(shop)) {
				dataDto.put("shopId", shop.getShopId());
				dataDto.put("orderFoodType", shop.getOrderFoodType());
			}
		}
		return R.toData(dataDto);
	}
	
	/**
	 * 
	 * 简要说明：注册账号
	 * 编写者：陈骑元
	 * 创建时间：2019年12月15日 下午2:31:18
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("enroll")
	@ResponseBody
	public R  enroll(Member member,String authCode){
		String mobile = member.getMobile();
		String email = member.getEmail();
		if (WebplusUtil.isEmpty(mobile)) {

			return R.error("携帯番号は必須です");
		}
		mobile=mobile.trim(); 
		int len = mobile.length();
		if (len == 11&!mobile.startsWith("0")) { // 中国手机号码
			if (!WebplusRegex.isMobile(mobile)) {
				return R.error("無効な電話番号です");
			}
		} else {
			if (!WebplusRegex.isJapanMobile(mobile)) {
				return R.error("無効な電話番号です");
		    }

		}
		
		 if(WebplusUtil.isEmpty(member.getPassword())){
			 return R.error("パスワードは必須ですs");
		 }
		if (WebplusUtil.isEmpty(email)) {
			return R.error("メールは必須ですs");
		}
		if (!WebplusRegex.isEmail(email)) {
			return R.error("無効なemail");
		}
		if(WebplusUtil.isEmpty(member.getFirstName())) {
			
			return R.error("firstNameは必須です");
		}
        if(WebplusUtil.isEmpty(member.getLastName())) {
			
			return R.error("lastNameは必須です");
		}
        if(WebplusUtil.isEmpty(member.getSex())) {
        	
        	return R.error("性別を選択してください");
        }
		if(WebplusUtil.isEmpty(authCode)){
			return R.error("認証コードは必須です");
		}
      
		String key=BussCons.AUTH_CODE+mobile;
		String cacheAuthCode=WebplusCache.getString(key);
		String commonAuthCode=WebplusCache.getParamValue("common_auth_code");
		if(!(authCode.equals(cacheAuthCode)||authCode.equals(commonAuthCode))){
			return R.error("無効な認証コードですs");
		}
		
		return pointService.saveEnroll(member);
	}
	/**
	 * 
	 * 简要说明：获取会员信息接口
	 * 编写者：陈骑元（chenqiyuan@toonan.com）
	 * 创建时间： 2020年5月2日 下午11:12:15 
	 * @param 说明
	 * @return 说明
	 */
	@GetMapping("getMember")
	@ResponseBody
	public R getMember() {
		String memberId=this.getUserId();
		Member member=memberService.selectById(memberId);
		return R.toData(member);
	}
	/**
	 * 
	 * 简要说明：注册账号
	 * 编写者：陈骑元
	 * 创建时间：2019年12月15日 下午2:31:18
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("updateMember")
	@ResponseBody
	public R  updateMember(Member member){
		
		String email = member.getEmail();
		
		if (WebplusUtil.isEmpty(email)) {
			return R.error("メールは必須ですs");
		}
		if (!WebplusRegex.isEmail(email)) {
			return R.error("無効なemail");
		}
		if(WebplusUtil.isEmpty(member.getFirstName())) {
			
			return R.error("firstNameは必須です");
		}
        if(WebplusUtil.isEmpty(member.getLastName())) {
			
			return R.error("lastNameは必須です");
		}
        if(WebplusUtil.isEmpty(member.getSex())) {
        	
        	return R.error("性別を選択してください");
        }
		String userId=this.getUserId();
		member.setMemberId(userId);
		return pointService.updateMember(member);
	}
	
	/**
	 * 
	 * 简要说明：注册账号
	 * 编写者：陈骑元
	 * 创建时间：2019年12月15日 下午2:31:18
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("updatePassword")
	@ResponseBody
	public R  updatePassword(String srcPassword,String newPassword){
		String memberId=this.getUserId();
		
		return pointService.updatePasword(memberId, srcPassword, newPassword);
	}
	/**
	 * 
	 * 简要说明：发送验证码
	 * 编写者：陈骑元
	 * 创建时间：2019年7月27日 上午12:04:28
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("sendAuthCode")
	@ResponseBody
	public R sendAuthCode(String mobile) {
		if(WebplusUtil.isEmpty(mobile)){
			
			return R.error("携帯番号は必須です");
		}
		mobile=mobile.trim();
		int len=mobile.length();
		 if(len==11&&!mobile.startsWith("0")){  //中国手机号码
	    	 if(!WebplusRegex.isMobile(mobile)){
	    		 return R.error("無効な電話番号です");
	    	 }
	     }else{
	    	 if(!WebplusRegex.isJapanMobile(mobile)){
	    		 return R.error("無効な電話番号です");
	    	 }
	    	 
	     }
		 return pointService.sendAuthCode(mobile);
	}
	/**
	 * 
	 * 简要说明：获取总分
	 * 编写者：陈骑元
	 * 创建时间：2019年7月27日 上午12:04:28
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("getSummary")
	@ResponseBody
	public R getSummary() {
		String memberId=this.getUserId();
		Member member=memberService.selectById(memberId);
		int point=PointApiUtil.getSummary(member.getMemberNum());
		Dto dataDto=Dtos.newDto();
		dataDto.put("totalPoint", point);
		dataDto.put("memberNo", member.getMemberNum());
		return R.toData(dataDto);
	}
	
	/**
	 * 
	 * 简要说明：获取积分明细
	 * 编写者：陈骑元
	 * 创建时间：2019年7月27日 上午12:04:28
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("listPoint")
	@ResponseBody
	public R listPoint(String currentPage,String pageSize) {
		String memberId=this.getUserId();
		Member member=memberService.selectById(memberId);
		Dto pDto=Dtos.newDto();
		pDto.put("currentPage", currentPage);
		pDto.put("pageSize", pageSize);
		return PointApiUtil.getPoints(member.getMemberNum(), pDto);
	}
    /**
     * 
     * 简要说明：展示二维码图片
     * 编写者：陈骑元
     * 创建时间：2019年12月16日 上午1:37:29
     * @param 说明
     * @return 说明
     */
	@RequestMapping(value = "showQrcodeImage")
	public void showQrcodeImage(){
		String memberId=this.getUserId();
		Member member=memberService.selectById(memberId);
		String memberNum=member.getMemberNum();
		String folderPath=WebplusCache.getParamValue(WebplusCons.SAVE_ROOT_PATH_KEY	);
		String fileFolder=folderPath+File.separator+BussCons.MEMBER_QRCODE;
		WebplusFile.createFolder(fileFolder);
		String fileName=memberNum+".png";
		File file=new File(fileFolder,fileName);
		String filePath=fileFolder+File.separator+fileName;
		if(file.exists()){
			
			WebplusFile.downloadFile(request, response,  filePath, fileName);
		}else{
			WebplusQrcode.encode(memberNum, "", fileFolder, fileName, true);
			
			WebplusFile.downloadFile(request, response,  filePath, fileName);
		}
	}
	/**
	 * 
	 * 简要说明：查询店铺信息
	 * 编写者：陈骑元（chenqiyuan@toonan.com）
	 * 创建时间： 2020年5月2日 上午7:12:08 
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("listShopPage")
	@ResponseBody
	public R listShopPage(String shopName,String shopTelephone,String whetherCoupon,String currentPage,String pageSize) {
		Dto pDto=Dtos.newPage(currentPage, pageSize);
		pDto.put("shopName", shopName);
		pDto.put("shopTelephone",shopTelephone);
		pDto.put("whetherRemove", WebplusCons.WHETHER_NO);
		pDto.put("whetherCoupon",whetherCoupon);
	    pDto.setOrder("a.create_time DESC ");
	    Page<Shop>  page=shopService.listShopPage(pDto);
	    
	    return R.toApiPage(page);
	    
	}
	/**
	 * 
	 * 简要说明：查询店铺优惠券
	 * 编写者：陈骑元（chenqiyuan@toonan.com）
	 * 创建时间： 2020年5月2日 上午7:12:08 
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("listShopCouponPage")
	@ResponseBody
	public R listShopCouponPage(String shopName,String shopTelephone,String currentPage,String pageSize) {
		Dto pDto=Dtos.newPage(currentPage, pageSize);
		pDto.put("shopName", shopName);
		pDto.put("shopTelephone",shopTelephone);
		pDto.put("whetherRemove", WebplusCons.WHETHER_NO);
		pDto.put("couponStatus", BussCons.COUPON_STATUS_PUTAWAY);
	    pDto.setOrder("a.create_time DESC ");
	    Page<ShopCoupon>  page=shopCouponService.listShopCouponPage(pDto);
	    
	    return R.toApiPage(page);
	    
	}
	/**
	 * 
	 * 简要说明：查询店铺优惠券
	 * 编写者：陈骑元（chenqiyuan@toonan.com）
	 * 创建时间： 2020年5月2日 上午7:12:08 
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("listMemberCouponPage")
	@ResponseBody
	public R listMemberCouponPage(String currentPage,String pageSize) {
		String userId=this.getUserId();
		Dto pDto=Dtos.newPage(currentPage, pageSize);
		pDto.put("memberId", userId);
		pDto.setOrder("c.collect_time ASC ");
		Page<ShopCoupon>  page=shopCouponService.listMemberCouponPage(pDto);
		
		return R.toApiPage(page);
		
	}
	/**
	 * 
	 * 简要说明：查询优惠券详情
	 * 编写者：陈骑元（chenqiyuan@toonan.com）
	 * 创建时间： 2020年5月2日 上午7:12:08 
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("getCouponDetail")
	@ResponseBody
	public R getCouponDetail(String couponId) {
		if(WebplusUtil.isEmpty(couponId)) {
			
			return R.error("クーポンNOは必須です");
		}
		ShopCoupon shopCoupon=shopCouponService.selectById(couponId);
		if(WebplusUtil.isEmpty(shopCoupon)) {
			
		  return R.error("クーポン間違いました");
		}
		
		String memberId=this.getUserId();
		EntityWrapper<MemberCoupon> wrapper=new EntityWrapper<MemberCoupon>();
		wrapper.eq("member_id", memberId);
		wrapper.eq("coupon_id", couponId);
		int count=memberCouponService.selectCount(wrapper);
		String whetherCollect=WebplusCons.WHETHER_NO;
		if(count>0) {
			whetherCollect=WebplusCons.WHETHER_YES;
		}
		shopCoupon.setWhetherCollect(whetherCollect);
		Shop shop=shopService.selectById(shopCoupon.getShopId());
		return R.ok().put("shop", shop).put("shopCoupon", shopCoupon);
		
	}
	/**
	 * 
	 * 简要说明：收藏优惠券信息
	 * 编写者：陈骑元（chenqiyuan@toonan.com）
	 * 创建时间： 2020年5月2日 上午7:12:08 
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("saveCollectCoupon")
	@ResponseBody
	public R saveCollectCoupon(String couponId) {
		if(WebplusUtil.isEmpty(couponId)) {
			
			return R.error("クーポンNOは必須です");
		}
		String memberId=this.getUserId();
		EntityWrapper<MemberCoupon> wrapper=new EntityWrapper<MemberCoupon>();
		wrapper.eq("member_id", memberId);
		wrapper.eq("coupon_id", couponId);
		int count=memberCouponService.selectCount(wrapper);
		if(count>0) {
			
			return R.error("クーポンは利用されました");
		}
		MemberCoupon memberCoupon=new MemberCoupon();
		memberCoupon.setCouponId(couponId);
		memberCoupon.setMemberId(memberId);
		memberCoupon.setCollectTime(WebplusUtil.getDateTime());
		memberCoupon.setStatus(BussCons.COLLECT_STATUS_VALID);
		boolean result=memberCouponService.insert(memberCoupon);
		if(result) {
			
			return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 
	 * 简要说明：取消收藏优惠券信息
	 * 编写者：陈骑元（chenqiyuan@toonan.com）
	 * 创建时间： 2020年5月2日 上午7:12:08 
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("cancelCollectCoupon")
	@ResponseBody
	public R cancelCollectCoupon(String couponId) {
		if(WebplusUtil.isEmpty(couponId)) {
			
			return R.error("クーポンNOは必須です");
		}
		String memberId=this.getUserId();
		EntityWrapper<MemberCoupon> wrapper=new EntityWrapper<MemberCoupon>();
		wrapper.eq("member_id", memberId);
		wrapper.eq("coupon_id", couponId);
		MemberCoupon memberCoupon=memberCouponService.selectOne(wrapper);
		if(WebplusUtil.isEmpty(memberCoupon)) {
			
			return R.error("クーポン間違いました");
		}
		if(BussCons.COLLECT_STATUS_USE.equals(memberCoupon.getStatus())) {
			return R.error("クーポンは利用されました、キャンセルはできません");
		}
		boolean result=memberCouponService.deleteById(memberCoupon.getMemberCouponId());
		if(result) {
			
			return R.ok();
		}
		return R.error();
	}
	/**
	 * 
	 * 简要说明：获取店铺详情
	 * 编写者：陈骑元（chenqiyuan@toonan.com）
	 * 创建时间： 2020年5月3日 下午1:25:18 
	 * @param 说明
	 * @return 说明
	 */
	@GetMapping("getShopDetail")
	@ResponseBody
	public R getShopDetail(String shopId) {
		if(WebplusUtil.isEmpty(shopId)) {
			return R.error("店舗IDは必須です");
		}
		Shop shop =shopService.selectById(shopId);
		Dto pDto=Dtos.newDto();
		pDto.put("shopId", shopId);
		pDto.put("whetherRemove", WebplusCons.WHETHER_NO);
		pDto.put("couponStatus", BussCons.COUPON_STATUS_PUTAWAY);
	    pDto.setOrder("create_time DESC ");
		List<ShopCoupon> couponList=shopCouponService.list(pDto);
		return R.ok().put("shop", shop).put("couponList", couponList);
	}
}
