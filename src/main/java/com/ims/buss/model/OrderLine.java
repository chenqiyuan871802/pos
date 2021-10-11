package com.ims.buss.model;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.ims.core.matatype.impl.BaseModel;
import java.io.Serializable;

/**
 * <p>
 * 订单明细
 * </p>
 *
 * @author 陈骑元
 * @since 2019-09-01
 */
@TableName("t_order_line")
public class OrderLine extends BaseModel<OrderLine> {

	private static final long serialVersionUID = 1L;

	/**
	 * 明细编号
	 */
	@TableId("line_id")
	private String lineId;
	/**
	 * 订单编号
	 */
	@TableField("order_id")
	private String orderId;
	/**
	 * 店铺编号
	 */
	@TableField("shop_id")
	private String shopId;
	/**
	 * 类目或者自助餐编号
	 */
	@TableField("catalog_index_id")
	private String catalogIndexId;
	/**
	 * 菜品索引
	 */
	@TableField("menu_index_id")
	private String menuIndexId;
	/**
	 * 菜品规格索引
	 */
	@TableField("spec_index_id")
	private String specIndexId;
	/**
	 * 菜单名称
	 */
	@TableField("menu_name")
	private String menuName;
	/**
	 * 打印菜单名称
	 */
	@TableField("print_menu_name")
	private String printMenuName;
	/**
	 * 菜单价格
	 */
	@TableField("menu_price")
	private Integer menuPrice;
	/**
	 * 数量
	 */
	@TableField("buy_num")
	private Integer buyNum;
	/**
	 * 是否打印0否1是
	 */
	@TableField("whether_print")
	private String whetherPrint;
	/**
	 * 是否追加0否1是
	 */
	@TableField("whether_add")
	private String whetherAdd;
	/**
	 * 创建时间
	 */
	@TableField("create_time")
	private Date createTime;
	/**
	 * 创建人ID
	 */
	@TableField("create_by")
	private String createBy;
	/**
	 * 修改时间
	 */
	@TableField("update_time")
	private Date updateTime;
	/**
	 * 修改用户编号
	 */
	@TableField("update_by")
	private String updateBy;
	/**
	 * 是否规格0否1是
	 */
	@TableField("whether_spec")
	private String whetherSpec;
	/**
	 * 是否自助餐菜单0否1是
	 */
	@TableField("whether_buffet")
	private String whetherBuffet;
	/**
	 * 规格上级编号
	 */
	@TableField("parent_id")
	private String parentId;
	/**
	 * 自助餐可选菜单个数
	 */
	@TableField("choose_num")
	private Integer chooseNum;
	/**
	 * 是否外卖
	 */
	@TableField("whether_take_out")
	private String whetherTakeOut;

	@TableField(exist = false)
	private List<OrderLine> specList;
	/**
	 * 税金类型1税前2税后
	 */
	@TableField("tax_type")
	private String taxType;
	 /**
     * 递减金额
     */
    @TableField("sub_amount")
    private Integer subAmount;
    /**
     * 抵扣率
     */
    @TableField("sub_rate")
    private Integer subRate;
	/**
	 * 打印类目编号
	 */
	@TableField(exist = false)
	private String printCatalogIndexId;

	public String getPrintCatalogIndexId() {
		return printCatalogIndexId;
	}

	public void setPrintCatalogIndexId(String printCatalogIndexId) {
		this.printCatalogIndexId = printCatalogIndexId;
	}

	/**
	 * 菜单价格
	 */
	@TableField(exist = false)
	private Integer menuSumPrice;

	public Integer getMenuSumPrice() {
		return menuSumPrice;
	}

	public void setMenuSumPrice(Integer menuSumPrice) {
		this.menuSumPrice = menuSumPrice;
	}

	public List<OrderLine> getSpecList() {
		return specList;
	}

	public void setSpecList(List<OrderLine> specList) {
		this.specList = specList;
	}

	public String getWhetherTakeOut() {
		return whetherTakeOut;
	}

	public void setWhetherTakeOut(String whetherTakeOut) {
		this.whetherTakeOut = whetherTakeOut;
	}

	public String getLineId() {
		return lineId;
	}

	public void setLineId(String lineId) {
		this.lineId = lineId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getShopId() {
		return shopId;
	}

	public void setShopId(String shopId) {
		this.shopId = shopId;
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

	public String getSpecIndexId() {
		return specIndexId;
	}

	public void setSpecIndexId(String specIndexId) {
		this.specIndexId = specIndexId;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public String getPrintMenuName() {
		return printMenuName;
	}

	public void setPrintMenuName(String printMenuName) {
		this.printMenuName = printMenuName;
	}

	public Integer getMenuPrice() {
		return menuPrice;
	}

	public void setMenuPrice(Integer menuPrice) {
		this.menuPrice = menuPrice;
	}

	public Integer getBuyNum() {
		return buyNum;
	}

	public void setBuyNum(Integer buyNum) {
		this.buyNum = buyNum;
	}

	public String getWhetherPrint() {
		return whetherPrint;
	}

	public void setWhetherPrint(String whetherPrint) {
		this.whetherPrint = whetherPrint;
	}

	public String getWhetherAdd() {
		return whetherAdd;
	}

	public void setWhetherAdd(String whetherAdd) {
		this.whetherAdd = whetherAdd;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public String getWhetherSpec() {
		return whetherSpec;
	}

	public void setWhetherSpec(String whetherSpec) {
		this.whetherSpec = whetherSpec;
	}

	public String getWhetherBuffet() {
		return whetherBuffet;
	}

	public void setWhetherBuffet(String whetherBuffet) {
		this.whetherBuffet = whetherBuffet;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public Integer getChooseNum() {
		return chooseNum;
	}

	public void setChooseNum(Integer chooseNum) {
		this.chooseNum = chooseNum;
	}

	public String getTaxType() {
		return taxType;
	}

	public void setTaxType(String taxType) {
		this.taxType = taxType;
	}

	 public Integer getSubAmount() {
	        return subAmount;
	    }

	    public void setSubAmount(Integer subAmount) {
	        this.subAmount = subAmount;
	    }

	    public Integer getSubRate() {
	        return subRate;
	    }

	    public void setSubRate(Integer subRate) {
	        this.subRate = subRate;
	    }

	    @Override
	    protected Serializable pkVal() {
	        return this.lineId;
	    }

	    @Override
	    public String toString() {
	        return "OrderLine{" +
	        "lineId=" + lineId +
	        ", orderId=" + orderId +
	        ", shopId=" + shopId +
	        ", catalogIndexId=" + catalogIndexId +
	        ", menuIndexId=" + menuIndexId +
	        ", specIndexId=" + specIndexId +
	        ", menuName=" + menuName +
	        ", printMenuName=" + printMenuName +
	        ", menuPrice=" + menuPrice +
	        ", buyNum=" + buyNum +
	        ", whetherPrint=" + whetherPrint +
	        ", whetherAdd=" + whetherAdd +
	        ", createTime=" + createTime +
	        ", createBy=" + createBy +
	        ", updateTime=" + updateTime +
	        ", updateBy=" + updateBy +
	        ", whetherSpec=" + whetherSpec +
	        ", whetherBuffet=" + whetherBuffet +
	        ", parentId=" + parentId +
	        ", chooseNum=" + chooseNum +
	        ", whetherTakeOut=" + whetherTakeOut +
	        ", taxType=" + taxType +
	        ", subAmount=" + subAmount +
	        ", subRate=" + subRate +
	        "}";
	    }
}
