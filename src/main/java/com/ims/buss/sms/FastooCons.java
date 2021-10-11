package com.ims.buss.sms;

import com.ims.core.matatype.Dto;
import com.ims.core.matatype.Dtos;

/**
 * 
 * 类名:com.ims.buss.sms.FastooCons
 * 描述:短信产量
 * 编写者:陈骑元
 * 创建时间:2019年12月14日 下午10:48:16
 * 修改说明:
 */
public class FastooCons {
	
	/**
	 * 成功代码
	 */
	public final static String SUCCESS="0";
	
	public final static Dto errorDto=Dtos.newDto();
	
	static{
		errorDto.put("0", "提交成功");
		errorDto.put("101", "没有此用户");
		errorDto.put("200", "金额不足");
		errorDto.put("203", "非法IP地址访问");
		errorDto.put("204", "模板不匹配");
		errorDto.put("205", "下发号码无效");
		errorDto.put("400", "请求参数错误");
		errorDto.put("600", "系统异常");
	}

}
