package com.ims.buss.system;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.baomidou.mybatisplus.plugins.Page;
import com.ims.core.cache.WebplusCache;
import com.ims.core.constant.WebplusCons;
import com.ims.core.matatype.Dto;
import com.ims.core.matatype.Dtos;
import com.ims.core.util.WebplusFormater;
import com.ims.core.util.WebplusHashCodec;
import com.ims.core.util.WebplusUtil;
import com.ims.core.vo.R;
import java.util.List;

import com.ims.buss.constant.BussCons;
import com.ims.buss.model.Member;
import com.ims.buss.model.MenuDict;
import com.ims.buss.service.MemberService;
import com.ims.buss.util.BussUtil;

import org.springframework.stereotype.Controller;
import com.ims.core.web.BaseController;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author 陈骑元
 * @since 2020-06-04
 */
@Controller
@RequestMapping("/buss/member")
public class MemberController extends BaseController {

    @Autowired
    private MemberService memberService;


	/**
	 * 
	 * 简要说明：分页查询 
	 * 编写者：陈骑元
	 * 创建时间：2020-06-04
	 * @param 说明
	 * @return 说明
	 */
	@RequestMapping("list")
	@ResponseBody
	public R list() {
		Dto pDto = Dtos.newDto(request);
		pDto.put("whetherEnroll",WebplusCons.WHETHER_YES);
		pDto.setOrder( "enroll_time DESC ");
		Page<Member> page =memberService.likePage(pDto);
		WebplusCache.convertItem(page);
		return R.toPage(page);
	}



	/**
	 * 
	 * 简要说明： 新增信息保存 
	 * 编写者：陈骑元
	 * 创建时间：2020-06-04
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("save")
	@ResponseBody
	public R save(Member member) {
		boolean result = memberService.insert(member);
		if (result) {
			return R.ok();
		} else {
			return R.error("保存失败");
		}

	}
	/**
	 * 
	 * 简要说明： 跳转到编辑页面 
	 * 编写者：陈骑元
	 * 创建时间：2020-06-04
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("edit")
	@ResponseBody
	public R edit(String id) {
		Member member=memberService.selectById(id);
		return R.toData(member);
	}
	
	/**
	 * 
	 * 简要说明：修改信息
	 * 编写者：陈骑元
	 * 创建时间：2020-06-04
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("update")
	@ResponseBody
	public R update(Member member) {
		boolean result = memberService.updateById(member);
		if (result) {
			return R.ok();
		} else {
			return R.error("更新失败");
		}
		
	}
	
	
	/**
	 * 
	 * 简要说明：删除信息
	 * 编写者：陈骑元
	 * 创建时间：2020-06-04
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("remove")
	@ResponseBody
	public R remove(String id) {
		boolean result = memberService.deleteById(id);
		if (result) {
			return R.ok();
		} else {
			return R.error("删除失败");
		}
		
	}
	
	/**
	 * 
	 * 简要说明：批量删除信息
	 * 编写者：陈骑元
	 * 创建时间：2020-06-04
	 * @param 说明
	 * @return 说明
	 */
	@PostMapping("batchRemove")
	@ResponseBody
	public R batchRemove(String ids) {
		List<String> idList=WebplusFormater.separatStringToList(ids);
		boolean result = memberService.deleteBatchIds(idList);
		if (result) {
			return R.ok();
		} else {
			return R.error("删除失败");
		}
		
	}
	
	/**
	 * 
	 * @param 导出
	 * @param 
	 * @return
	 */
	@PostMapping("export")
	@ResponseBody
    public R export(){
		Dto pDto = Dtos.newDto(request);
		pDto.put("whetherEnroll",WebplusCons.WHETHER_YES);
		pDto.setOrder( "enroll_time DESC ");
		List<Member> memberList=memberService.like(pDto);
		for(Member member:memberList) {
			String password=member.getPassword();
			if(WebplusUtil.isNotEmpty(password)) {
				member.setPassword(WebplusHashCodec.decrypt(password));
			}
		}
		WebplusCache.convertItem(memberList);
		List<Dto> dataList=WebplusUtil.copyPropertiesList(memberList);
		Dto dataDto=Dtos.newDto();
		dataDto.put("dataList", dataList);
		String fid=BussUtil.createTemplateExcel("excel/export_member_template.xlsx", dataDto);
		if(WebplusUtil.isNotEmpty(fid)){
			
		   
		   return R.ok().put("fid", fid);
		}
		return R.error();
	}
	
}

