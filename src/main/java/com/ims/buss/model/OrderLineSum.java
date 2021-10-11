package com.ims.buss.model;
/**
 * 
 * @ClassName:  OrderLineSum   
 * @Description:订单汇总列表
 * @author: 陈骑元（chenqiyuan@toonan.com)
 * @date:   2020年7月28日 下午11:31:50     
 * @Copyright: 2020 www.toonan.com Inc. All rights reserved. 
 * 注意：本内容仅限于广州市图南软件有限公司内部传阅，禁止外泄以及用于其他的商业目
 */
public class OrderLineSum {
	/**
	 * 大类名称
	 */
	private String largeName;
	/**
	 * 菜单种类
	 */
	private String catalogName;
	/**
	 * 菜单名称
	 */
	private String menuName;
	/**
	 * 菜单单价
	 */
	private Integer menuPrice;
	/**
	 * 成品单价
	 */
	private Integer costPrice;
	/**
	 * 购买份数
	 */
	private Integer buyNum;
	/**
	 * 销售金额
	 */
	private Integer salesAmount;
	
	/**
	 * 成本 金额
	 */
	private Integer costAmount;
	/**
	 * 利润金额
	 */
	private Integer profitAmount;
	/**
	 * 大类编号
	 */
	private String largeId;
	/**
	 * 菜品种类索引编号
	 */
	private String catalogIndexId;
	/**
	 * 菜品索引编号
	 */
	private String menuIndexId;
	/**
	 * 占比
	 */
	private Double proportion;
	
	public String getLargeName() {
		return largeName;
	}
	public void setLargeName(String largeName) {
		this.largeName = largeName;
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
	public Integer getMenuPrice() {
		return menuPrice;
	}
	public void setMenuPrice(Integer menuPrice) {
		this.menuPrice = menuPrice;
	}
	public Integer getCostPrice() {
		return costPrice;
	}
	public void setCostPrice(Integer costPrice) {
		this.costPrice = costPrice;
	}
	public Integer getBuyNum() {
		return buyNum;
	}
	public void setBuyNum(Integer buyNum) {
		this.buyNum = buyNum;
	}
	public Integer getSalesAmount() {
		return salesAmount;
	}
	public void setSalesAmount(Integer salesAmount) {
		this.salesAmount = salesAmount;
	}
	public Integer getCostAmount() {
		return costAmount;
	}
	public void setCostAmount(Integer costAmount) {
		this.costAmount = costAmount;
	}
	public Integer getProfitAmount() {
		return profitAmount;
	}
	public void setProfitAmount(Integer profitAmount) {
		this.profitAmount = profitAmount;
	}
	public String getLargeId() {
		return largeId;
	}
	public void setLargeId(String largeId) {
		this.largeId = largeId;
	}
	public String getCatalogIndexId() {
		return catalogIndexId;
	}
	public void setCatalogIndexId(String catalogIndexId) {
		this.catalogIndexId = catalogIndexId;
	}
	public String getMenuIndexId() {
		return menuIndexId;
	}
	public void setMenuIndexId(String menuIndexId) {
		this.menuIndexId = menuIndexId;
	}
	public Double getProportion() {
		return proportion;
	}
	public void setProportion(Double proportion) {
		this.proportion = proportion;
	}

}
