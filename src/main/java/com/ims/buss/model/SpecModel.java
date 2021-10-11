package com.ims.buss.model;


import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
/**
 * 
 * 类名:com.ims.buss.model.SpecModel
 * 描述:规格模板导入
 * 编写者:陈骑元
 * 创建时间:2019年10月26日 下午10:29:27
 * 修改说明:
 */
@ExcelTarget("specModel")
public class SpecModel {
   
	/**
	 * 类目名称
	 */
	@Excel(name = "类目" ,orderNum="0")
	private String catalogName;
	/**
	 * 菜单名称
	 */
	@Excel(name = "名称" ,orderNum="1")
	private String menuName;
	
	@Excel(name = "時間帯" ,orderNum="2")
	private String mealType;
	/**
	 * 步骤编号
	 */
	@Excel(name = "STEP" ,orderNum="3")
	private String stepNum;
	/**
	 * 步骤名称
	 */
	@Excel(name = "STEP名" ,orderNum="4")
	private String stepName;
	/**
	 * 规格名称
	 */
	@Excel(name = "规格名称" ,orderNum="5")
	private String specName;
	/***
	 * 规格价格
	 */
	@Excel(name = "规格价格" ,orderNum="6")
	private String specPrice;
	
	
	public String getMealType() {
		return mealType;
	}
	public void setMealType(String mealType) {
		this.mealType = mealType;
	}
	public String getCatalogName() {
		return catalogName;
	}
	public void setCatalogName(String catalogName) {
		this.catalogName = catalogName;
	}
	public String getMenuName() {
		return menuName;
	}
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
	public String getStepNum() {
		return stepNum;
	}
	public void setStepNum(String stepNum) {
		this.stepNum = stepNum;
	}
	public String getStepName() {
		return stepName;
	}
	public void setStepName(String stepName) {
		this.stepName = stepName;
	}
	public String getSpecName() {
		return specName;
	}
	public void setSpecName(String specName) {
		this.specName = specName;
	}
	public String getSpecPrice() {
		return specPrice;
	}
	public void setSpecPrice(String specPrice) {
		this.specPrice = specPrice;
	}
	
}
