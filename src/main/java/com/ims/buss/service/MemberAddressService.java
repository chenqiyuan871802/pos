package com.ims.buss.service;

import com.ims.buss.model.Member;
import com.ims.buss.model.MemberAddress;
import com.ims.buss.mapper.MemberAddressMapper;
import com.ims.buss.mapper.MemberMapper;
import com.ims.buss.service.MemberAddressService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import com.ims.core.constant.WebplusCons;
import com.ims.core.matatype.Dto;
import com.ims.core.util.WebplusUtil;
import com.baomidou.mybatisplus.plugins.Page;
import com.ims.core.vo.Query;
import com.ims.core.vo.R;
import com.ims.system.constant.SystemCons;

/**
 * <p>
 * 会员地址 服务实现类
 * </p>
 *
 * @author 陈骑元
 * @since 2020-04-23
 */
@Service
public class MemberAddressService extends ServiceImpl<MemberAddressMapper, MemberAddress>  {
    /**
     * 
     */
	@Autowired
	private MemberMapper memberMapper;
   
     /**
	 * 根据Dto查询并返回数据持久化对象集合
	 * 
	 * @return List<MemberAddress>
	 */
	public List<MemberAddress> list(Dto pDto){
	    
	    return baseMapper.list(pDto);
	};
    /**
	 * 根据Dto查询并返回分页数据持久化对象集合
	 * 
	 * @return Page<MemberAddress>
	 */
	public Page<MemberAddress> listPage(Dto pDto){
	    Query<MemberAddress> query=new Query<MemberAddress>(pDto);
	    query.setRecords(baseMapper.listPage(query,pDto));
	    return query;
	};
		
	/**
	 * 根据Dto模糊查询并返回数据持久化对象集合(字符型字段模糊匹配，其余字段精确匹配)
	 * 
	 * @return List<MemberAddress>
	 */
	public List<MemberAddress> like(Dto pDto){
	
	    return baseMapper.like(pDto);
	
	};

	/**
	 * 根据Dto模糊查询并返回分页数据持久化对象集合(字符型字段模糊匹配，其余字段精确匹配)
	 * 
	 * @return Page<MemberAddress>
	 */
	public Page<MemberAddress> likePage(Dto pDto){
	    Query<MemberAddress> query=new Query<MemberAddress>(pDto);
	    query.setRecords(baseMapper.likePage(query,pDto));
	    return query;
	};
	/**
	 * 根据数学表达式进行数学运算
	 * 
	 * @param pDto
	 * @return String
	 */
	 public String calc(Dto pDto){
	 
	     return baseMapper.calc(pDto);
	 }
	 
	 /**
	  * 
	  * 简要说明：获取会员地址
	  * 编写者：陈骑元（chenqiyuan@toonan.com）
	  * 创建时间： 2020年4月25日 上午10:26:34 
	  * @param 说明
	  * @return 说明
	  */
	 public List<MemberAddress> listMemberAddress(Dto pDto){
		 
		 return baseMapper.listMemberAddress(pDto);
	 };
	 /**
	  * 
	  * 简要说明：保存会员地址
	  * 编写者：陈骑元（chenqiyuan@toonan.com）
	  * 创建时间： 2020年4月25日 上午11:01:48 
	  * @param 说明
	  * @return 说明
	  */
	 @Transactional
	 public R saveMemberAddress(MemberAddress memberAddress) {
		 String  mobile=memberAddress.getMobile();
		 String  username=memberAddress.getUsername();
		 if(WebplusUtil.isEmpty(mobile)) {
			 
			 return R.error("The phone number cannot be empty");
		 }
		 if(WebplusUtil.isEmpty(username)) {
			 
			 return R.error("The name cannot be empty");
		 }
		 R verifyR=this.verifyMemberAddress(memberAddress);
		 if(WebplusCons.ERROR==verifyR.getAppCode()) {
			 return verifyR;
		 }
		 Member entity=new Member();
		 entity.setMobile(mobile);
		 Member member=memberMapper.selectOne(entity);
		 String memberId="";
		if(WebplusUtil.isNotEmpty(member)) {
			 memberId=member.getMemberId();
			 member.setUpdateTime(WebplusUtil.getDateTime());
			 member.setUsername(username);
			 memberMapper.updateById(member);
		}else {
			 member=new Member();
			 memberId=WebplusUtil.uuid();
			 member.setMemberId(memberId);
			 member.setUsername(username);
			 member.setMobile(mobile);
			 member.setStatus(SystemCons.USER_STATE_VALID);
			 member.setEnrollTime(WebplusUtil.getDateTime());
			 member.setUpdateTime(WebplusUtil.getDateTime());
			 memberMapper.insert(member);
		}
		memberAddress.setMemberId(memberId);
		memberAddress.setWhetherRemove(WebplusCons.WHETHER_NO);
		memberAddress.setCreateTime(WebplusUtil.getDateTime());
		int insertCount=baseMapper.insert(memberAddress);
		if(insertCount>0) {
		   
			return R.ok();
		}
		
		return R.error();
	    
	 }
	 
	 /**
	  * 
	  * 简要说明： 编辑会员地址
	  * 编写者：陈骑元（chenqiyuan@toonan.com）
	  * 创建时间： 2020年4月25日 上午11:01:48 
	  * @param 说明
	  * @return 说明
	  */
	 @Transactional
	 public R updateMemberAddress(MemberAddress memberAddress) {
		 
		 if(WebplusUtil.isEmpty(memberAddress.getAddressId())) {
			 
			 return R.error("The address number cannot be empty");
		 }
		 
		 R verifyR=this.verifyMemberAddress(memberAddress);
		 if(WebplusCons.ERROR==verifyR.getAppCode()) {
			 return verifyR;
		 }
		 baseMapper.updateById(memberAddress);
		 return R.ok();
	    
	 }
	 /**
	  * 
	  * 简要说明：校验会员地址
	  * 编写者：陈骑元（chenqiyuan@toonan.com）
	  * 创建时间： 2020年4月25日 上午11:08:51 
	  * @param 说明
	  * @return 说明
	  */
	 public R verifyMemberAddress(MemberAddress memberAddress) {
		 if(WebplusUtil.isNotEmpty(memberAddress.getProvince())) {
			 
			 if(WebplusUtil.isEmpty(memberAddress.getCity())) {
				 
				 return R.error("The city code cannot be empty");
			 }
			 if(WebplusUtil.isEmpty(memberAddress.getTown())) {
				 
				 return R.error("The town code cannot be empty");
			 }
			 if(WebplusUtil.isEmpty(memberAddress.getDetailAddress())) {
				 
				 return R.error("The full address cannot be empty");
			 }
		 }
		
		 
		 return R.ok();
		 
	 }
	 /**
	  * 
	  * 简要说明：查询三个会员编号
	  * 编写者：陈骑元（chenqiyuan@toonan.com）
	  * 创建时间： 2020年5月4日 上午7:55:32 
	  * @param 说明
	  * @return 说明
	  */
	 public MemberAddress selectMemberAddress(String addressId) {
		 
		 return baseMapper.selectMemberAddress(addressId);
	 }
}
