package com.ims.core.vo;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import com.baomidou.mybatisplus.plugins.Page;



/**
 * 
 * 类名:com.ims.common.util.Page
 * 描述:layui分页VO
 * 编写者:陈骑元
 * 创建时间:2018年4月9日 下午10:22:26 
 * 修改说明:
 */
public class LayuiPage implements Serializable {

	private static final long serialVersionUID = 1L;
    private int code=0;
    private String msg;

	/**
	 * 总记录数
	 */
	private long count;
	/**
	 * 查询数据列表
	 */
	private List<?> data = Collections.emptyList();

	public LayuiPage(){
		
	}

	public LayuiPage(Page<?> page) {
		super();
		this.count =page.getTotal();
		this.data = page.getRecords();
	}
	public LayuiPage(int total, List<?> rows) {
		super();
		this.count = total;
		this.data = rows;
	}


	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public List<?> getData() {
		return data;
	}

	public void setData(List<?> data) {
		this.data = data;
	}
	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
}
