package com.ims.buss.service;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.ims.buss.constant.BussCons;
import com.ims.buss.model.Member;
import com.ims.buss.model.MemberNumber;
import com.ims.buss.model.SmsRecord;
import com.ims.buss.sms.FastooUtil;
import com.ims.buss.util.PointApiUtil;
import com.ims.core.cache.WebplusCache;
import com.ims.core.constant.WebplusCons;
import com.ims.core.matatype.Dto;
import com.ims.core.matatype.Dtos;
import com.ims.core.redis.WebplusJedis;
import com.ims.core.util.WebplusHashCodec;
import com.ims.core.util.WebplusSpringContext;
import com.ims.core.util.WebplusSqlHelp;
import com.ims.core.util.WebplusUtil;
import com.ims.core.vo.R;
import com.ims.core.vo.UserToken;
/**
 * 
 * 类名:com.ims.buss.service.PointService
 * 描述:积分业务控制逻辑
 * 编写者:陈骑元
 * 创建时间:2019年12月15日 上午8:53:35
 * 修改说明:
 */
@Service
public class PointService {
	
	private static Log log = LogFactory.getLog(PointService.class);
	
	@Autowired
	private SmsRecordService smsRecordService;
	
	@Autowired
	private MemberService memberService;
	
	@Autowired
	private MemberNumberService memberNumberService;
	
	
	/**
	 * 
	 * 简要说明：发送验证码
	 * 编写者：陈骑元
	 * 创建时间：2019年12月15日 上午8:54:12
	 * @param 说明
	 * @return 说明
	 */
	public R sendAuthCode(String mobile){
		 WebplusJedis webplusJedis = WebplusSpringContext.getBean("webplusJedis");
		int validTime = WebplusCache.getParamIntValue(BussCons.AUTH_CODE_VALID_TIME_KEY, 5);
		String authCode = WebplusUtil.createRandomCode(4, WebplusCons.RANDOM_TYPE.NUMBER);
		String authCountKey = BussCons.AUTH_CODE_COUNT + mobile;
		
		if (!webplusJedis.exists(authCountKey)) { // 如果不存在
			webplusJedis.setString(authCountKey, "0",3600);
		}
		int maxNum = WebplusCache.getParamIntValue(BussCons.ONE_HOUR_NUM, 5);
		String countStr = webplusJedis.getString(authCountKey);
		int count = Integer.parseInt(countStr);
		if (count >= maxNum) { // 一小时内发送验证码次数不能超过5次
			
			log.error("发送手机[" + mobile + "]验证码失败：获取验证码超过一小时最大限制次数" + maxNum);
			return R.error("制限数を超えました、1時間後また試してくださいs");
		}
		String msg="ゆいポイントの認証コードは"+authCode+"です。";
		R r = FastooUtil.sendSms(mobile, msg);
		if (WebplusCons.SUCCESS == r.getAppCode()) { // 发送成功
			String authCodeKey = BussCons.AUTH_CODE + mobile;
			webplusJedis.setString(authCodeKey, authCode,validTime * 60);
			webplusJedis.setString(authCountKey, (count + 1) + "",3600);
			SmsRecord smsRecord=new SmsRecord();
			smsRecord.setMobile(mobile);
			smsRecord.setMsg(msg);
			smsRecord.setMsgType(BussCons.MSG_TYPE_AUTH);
			smsRecord.setMsgStatus(BussCons.SMS_STATUS_SUBMIT);
			smsRecord.setSendTime(WebplusUtil.getDateTime());
			smsRecord.setMsgId(r.getString("msgid"));
			smsRecordService.insert(smsRecord);
		}
		return r;
		
		
	}
	/**
	 * 
	 * 简要说明：保存注册信息
	 * 编写者：陈骑元
	 * 创建时间：2019年12月16日 上午1:01:49
	 * @param 说明
	 * @return 说明
	 */
	public R saveEnroll(Member member){
		String mobile=member.getMobile();
		EntityWrapper<Member> countWrapper = new EntityWrapper<Member>();
		WebplusSqlHelp.eq(countWrapper, "mobile", mobile);
		Member entity=memberService.selectOne(countWrapper);
		
		if(WebplusUtil.isNotEmpty(entity)&&WebplusUtil.isNotEmpty(entity.getMemberNum())){
			return R.error("電話番号が登録されました、ほかの番号を利用してください");
		}
		String memberId="";
		if(WebplusUtil.isNotEmpty(entity)) {
			memberId=entity.getMemberId();
		}
		String memberNo=this.getMemberNo();
		if(WebplusUtil.isEmpty(memberNo)){
			
			return R.error("会员编号已经用完：現在新規登録できません");
		}
		Dto pDto=Dtos.newDto();
		Date birthDate=member.getBirthDate();
		if(WebplusUtil.isNotEmpty(birthDate)){
			pDto.put("birthDate",WebplusUtil.date2String(birthDate, WebplusCons.DATE));
		}
		pDto.put("password", member.getPassword());
		pDto.put("email", member.getEmail());
		pDto.put("mobilePhone", member.getMobile());
		pDto.put("sex", member.getSex());
		pDto.put("firstName", member.getFirstName());
		pDto.put("lastName", member.getLastName());
		boolean flag=PointApiUtil.enroll(memberNo, pDto);
		member.setPassword(WebplusHashCodec.encrypt(member.getPassword()));
		member.setMemberNum(memberNo);
		member.setEnrollTime(WebplusUtil.getDateTime());
		member.setUsername(member.getFirstName()+member.getLastName());
		if(flag){
			
			if(WebplusUtil.isNotEmpty(memberId)) {
				member.setMemberId(memberId);
				flag=memberService.updateById(member);
			}else {
				
				flag=memberService.insert(member);
			}
			
			if(flag){
				UserToken userToken=new UserToken();
				userToken.setAccount(member.getMobile());
				userToken.setUserId(member.getMemberId());
				userToken.setUsername(member.getUsername());
				String token=WebplusCache.createToken(userToken);
				userToken.setToken(token);
				Dto dataDto=Dtos.newDto();
				WebplusUtil.copyProperties(userToken, dataDto);
				return R.toData(dataDto);
			}
				
		}
		return R.error("注册失败");
	}
	/**
	 * 
	 * 简要说明：更新会员信息
	 * 编写者：陈骑元（chenqiyuan@toonan.com）
	 * 创建时间： 2020年5月2日 下午10:42:19 
	 * @param 说明
	 * @return 说明
	 */
	public R  updateMember(Member member) {
		Member entity=memberService.selectById(member.getMemberId());
		member.setPassword(WebplusHashCodec.decrypt(entity.getPassword()));
		member.setMemberNum(entity.getMemberNum());
		member.setMobile(entity.getMobile());
		member.setUpdateTime(WebplusUtil.getDateTime());
		boolean flag=PointApiUtil.update(member);
		if(flag) {
			String username=member.getFirstName()+member.getLastName();
			member.setUsername(username);
			member.setPassword(null);
			boolean result=memberService.updateById(member);
			if(result) {
				return R.ok();
			}
			
		    
		}
		return R.error();
	}
	/**
	 * 
	 * 简要说明：更新密码
	 * 编写者：陈骑元（chenqiyuan@toonan.com）
	 * 创建时间： 2020年5月2日 下午10:42:19 
	 * @param 说明
	 * @return 说明
	 */
	public R  updatePasword(String memberId,String srcPassword,String newPassword) {
		if(WebplusUtil.isEmpty(srcPassword)) {
			
			return R.error("元パスワードを入力してください");
		}
		if(WebplusUtil.isEmpty(newPassword)) {
			
			return R.error("新パスワードを入力してください");
		}
		Member member=memberService.selectById(memberId);
		String encPassword=WebplusHashCodec.encrypt(srcPassword);
		if(!encPassword.equals(member.getPassword())) {
			return R.error("元パスワード間違いました");
		}
		member.setPassword(newPassword);
		boolean flag=PointApiUtil.update(member);
		if(flag) {
			Member updateMember=new Member();
			updateMember.setMemberId(memberId);
			updateMember.setPassword(WebplusHashCodec.encrypt(newPassword));
			updateMember.setUpdateTime(WebplusUtil.getDateTime());
			boolean result=memberService.updateById(updateMember);
			if(result) {
				return R.ok();
			}
			
		    
		}
		return R.error();
	}
	/***
	 * 
	 * 简要说明：
	 * 编写者：陈骑元
	 * 创建时间：2019年12月16日 上午1:06:01
	 * @param 说明
	 * @return 说明
	 */
	public  synchronized String  getMemberNo(){
		EntityWrapper<MemberNumber> wrapper=new EntityWrapper<MemberNumber>();
		wrapper.eq("status", BussCons.NUMBER_STATUS_EMPTY);
		MemberNumber memberNumber=memberNumberService.selectOne(wrapper);
		if(WebplusUtil.isNotEmpty(memberNumber)){
			memberNumber.setStatus(BussCons.NUMBER_STATUS_USE);
			memberNumber.setUseTime(WebplusUtil.getDateTime());
			memberNumberService.updateById(memberNumber);
			return memberNumber.getMemberNo();
		}
		log.error("会员编号已经使用完成，请联系管理员增加");
		return "";
	}
}
