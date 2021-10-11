package com.ims.buss.model;


import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
/**
 * 
 * 类名:com.ims.buss.model.MenuModel
 * 描述:菜单EXCEL模型
 * 编写者:陈骑元
 * 创建时间:2019年8月7日 上午8:31:19
 * 修改说明:
 */
@ExcelTarget("menuModel")
public class MenuModel {
	
	@Excel(name = "カテゴリー" ,orderNum="0")
	private String catalogName;
	@Excel(name = "名前" ,orderNum="1")
	private String menuName;
	@Excel(name = "简称" ,orderNum="2")
	private String shortName;
	@Excel(name = "説明" ,orderNum="3")
	private String menuIntroduce;
	@Excel(name = "価格" ,orderNum="4")
	private String menuPrice;
	@Excel(name = "番号" ,orderNum="5")
	private String menuNum;
	@Excel(name = "時間帯" ,orderNum="6")
	private String mealType;
	public String getMenuNum() {
		return menuNum;
	}
	public void setMenuNum(String menuNum) {
		this.menuNum = menuNum;
	}
	public String getShortName() {
		return shortName;
	}
	public String getMealType() {
		return mealType;
	}
	public void setMealType(String mealType) {
		this.mealType = mealType;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
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
	public String getMenuIntroduce() {
		return menuIntroduce;
	}
	public void setMenuIntroduce(String menuIntroduce) {
		this.menuIntroduce = menuIntroduce;
	}
	public String getMenuPrice() {
		return menuPrice;
	}
	public void setMenuPrice(String menuPrice) {
		this.menuPrice = menuPrice;
	}


}
