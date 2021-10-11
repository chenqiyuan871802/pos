package com.ims.core.vo;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.plugins.Page;
import com.ims.core.constant.WebplusCons;
import com.ims.core.matatype.Dto;
import com.ims.core.matatype.Dtos;
import com.ims.core.matatype.impl.HashDto;

/**
 * 
 * 类名:com.ims.common.util.R
 * 描述:返回值封装
 * 编写者:陈骑元
 * 创建时间:2018年4月5日 下午10:49:20
 * 修改说明:
 */
public class R extends HashDto {
	private static final long serialVersionUID = 1L;
    /**
     * 默认操作成功代码
     */
	public R() {
		put(WebplusCons.APPCODE_KEY, WebplusCons.SUCCESS);
		put(WebplusCons.APPMSG_KEY, "完了");
	}
    /**
     * 
     * 简要说明：操作错误代码
     * 编写者：陈骑元
     * 创建时间：2019年4月25日 下午2:25:45
     * @param 说明
     * @return 说明
     */
	public static R error() {
		return error(WebplusCons.ERROR, "操作失敗");
	}
    /**
     * 
     * 简要说明：传入错误信息操作代码
     * 编写者：陈骑元
     * 创建时间：2019年4月25日 下午2:25:57
     * @param 说明
     * @return 说明
     */
	public static R error(String msg) {
		return error(WebplusCons.ERROR, msg);
	}
	/**
	 * 
	 * 简要说明：令牌错误输出参数
	 * 编写者：陈骑元
	 * 创建时间：2019年4月25日 下午2:29:12
	 * @param 说明
	 * @return 说明
	 */
	public static R errorToken(String msg) {
		
		return error(WebplusCons.ERROR_TOKEN, msg);
	}
	/**
	 * 
	 * 简要说明：自定义代码错误代码和错误信息的操作
	 * 编写者：陈骑元
	 * 创建时间：2019年4月25日 下午2:29:49
	 * @param 说明
	 * @return 说明
	 */
	public static R error(int code, String msg) {
		R r = new R();
		r.put(WebplusCons.APPCODE_KEY, code);
		r.put(WebplusCons.APPMSG_KEY, msg);
		return r;
	}
    /**
     * 
     * 简要说明：默认警告
     * 编写者：陈骑元
     * 创建时间：2019年4月25日 下午2:30:21
     * @param 说明
     * @return 说明
     */
	public static R warn() {
		return warn(WebplusCons.WARN, "操作不合法");
	}
    /**
     * 
     * 简要说明：传入警告信息的警告输出
     * 编写者：陈骑元
     * 创建时间：2019年4月25日 下午2:30:37
     * @param 说明
     * @return 说明
     */
	public static R warn(String msg) {
		return warn(WebplusCons.WARN, msg);
	}
    /**
     * 
     * 简要说明：传入警告代码 警告信息的警告输出
     * 编写者：陈骑元
     * 创建时间：2019年4月25日 下午2:32:14
     * @param 说明
     * @return 说明
     */
	public static R warn(int code, String msg) {
		R r = new R();
		r.put(WebplusCons.APPCODE_KEY, code);
		r.put(WebplusCons.APPMSG_KEY, msg);
		return r;
	}
    /**
     * 
     * 简要说明：输出成功信息的
     * 编写者：陈骑元
     * 创建时间：2019年4月25日 下午2:32:37
     * @param 说明
     * @return 说明
     */
	public static R ok(String msg) {
		R r = new R();
		r.put(WebplusCons.APPMSG_KEY, msg);
		return r;
	}
    /**
     * 
     * 简要说明：输出OK对象
     * 编写者：陈骑元
     * 创建时间：2019年4月25日 下午2:33:10
     * @param 说明
     * @return 说明
     */
	public static R ok(Dto dataDto) {
		R r = new R();
		r.putAll(dataDto);
		return r;
	}
	/**
	 * 
	 * 简要说明：Map结合对象
	 * 编写者：陈骑元
	 * 创建时间：2019年4月25日 下午2:33:17
	 * @param 说明
	 * @return 说明
	 */
	public static R ok(Map<String, Object> map) {
		R r = new R();
		r.putAll(map);
		return r;
	}
    /**
     * 
     * 简要说明：默认出操作成功
     * 编写者：陈骑元
     * 创建时间：2019年4月25日 下午2:33:21
     * @param 说明
     * @return 说明
     */
	public static R ok() {
		return new R();
	}
	/**
	 * 
	 * 简要说明：输出单条的数据模型
	 * 编写者：陈骑元
	 * 创建时间：2019年4月25日 下午3:33:06
	 * @param 说明
	 * @return 说明
	 */
	public static R toData(Object data) {
		R r=new R();
		r.remove(WebplusCons.APPMSG_KEY);
		r.put("data", data);
		return r;
	}
	/**
	 * 
	 * 简要说明：系统输出分页
	 * 编写者：陈骑元
	 * 创建时间：2019年4月25日 下午4:10:51
	 * @param 说明
	 * @return 说明
	 */
	public static R toPage(Page<?> page){
		if(WebplusCons.DEFAULT_PAGE.equals(WebplusCons.PAGE_EASYUI)){
			
			return toEasyuiPage(page);
		}else{
			
			return toLayuiPage(page);
		}
	}
	/**
	 * 
	 * 简要说明：输出API前端分页
	 * 编写者：陈骑元
	 * 创建时间：2019年4月25日 下午3:33:35
	 * @param 说明
	 * @return 说明
	 */
	public static R toApiPage(Page<?> page) {
		R r=new R();
		r.remove(WebplusCons.APPMSG_KEY);
		r.put("dataList", page.getRecords());
		Dto pageDto=Dtos.newDto();
		pageDto.put("currentPage", page.getCurrent());
		pageDto.put("pageSize", page.getSize());
		pageDto.put("count", page.getTotal());
		r.put("page", pageDto);
		return r;
	}
	/**
	 * 
	 * 简要说明：输出eayui的分页
	 * 编写者：陈骑元
	 * 创建时间：2019年4月25日 下午3:45:47
	 * @param 说明
	 * @return 说明
	 */
	public static R toEasyuiPage(Page<?> page) {
		R r=new R();
	    r.remove(WebplusCons.APPCODE_KEY);
	    r.remove(WebplusCons.APPMSG_KEY);
	    r.put("total", page.getTotal());
	    r.put("rows", page.getRecords());
		return r;
	}
	/**
	 * 
	 * 简要说明：输出Layui分页
	 * 编写者：陈骑元
	 * 创建时间：2019年4月25日 下午3:37:25
	 * @param 说明
	 * @return 说明
	 */
	public static R toLayuiPage(Page<?> page) {
		R r=new R();
		r.remove(WebplusCons.APPCODE_KEY);
		r.remove(WebplusCons.APPMSG_KEY);
		r.put("code", 0);
		r.put("count", page.getTotal());
		r.put("data", page.getRecords());
		return r;
	}
    /**
     * 
     * 简要说明：输出List的模型
     * 编写者：陈骑元
     * 创建时间：2019年4月25日 下午3:35:10
     * @param 说明
     * @return 说明
     */
	public static R toList(List<?> dataList) {
		R r=new R();
		r.remove(WebplusCons.APPMSG_KEY);
		r.put("dataList", dataList);
		return r;
	}

    
	@Override
	public R put(String key, Object value) {
		super.put(key, value);
		return this;
	}
	 /**
     * 
     * 简要说明：输出单个对象和集合对象
     * 编写者：陈骑元
     * 创建时间：2019年4月25日 下午3:35:10
     * @param 说明
     * @return 说明
     */
	public static R toDataAndList(Object data,List<?> dataList) {
		R r=new R();
		/*r.remove(WebplusCons.APPMSG_KEY);*/
		r.put("dataList", dataList);
		r.put("data",data);
		return r;
	}
}
