package com.ims.core.vo;
/**
 * 
 * 类名:com.toonan.system.model.Item
 * 描述:字典实体类模型
 * 编写者:陈骑元
 * 创建时间:2018年12月15日 下午5:30:31
 * 修改说明:
 */
public class Item {
	/**
	 * 字典类型
	 */
	private String typeCode;
	
	/**
	 * 字典代码
	 */
	private String itemCode;
	
	/**
	 * 字典名称
	 */
	private String itemName;

	
	
	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	
	
   
	

}
