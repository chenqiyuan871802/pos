package com.ims.buss.model;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.ims.core.annotation.ItemTag;
import com.ims.core.matatype.impl.BaseModel;
import java.io.Serializable;

/**
 * <p>
 * 订单信息
 * </p>
 *
 * @author 陈骑元
 * @since 2019-07-23
 */
@TableName("t_order")
public class Order extends BaseModel<Order> {

    private static final long serialVersionUID = 1L;

    /**
     * 订单编号
     */
    @TableId("order_id")
    private String orderId;
    /**
     * 店铺编号
     */
    @TableField("shop_id")
    private String shopId;
    /**
     * 桌号编号
     */
    @TableField("desk_id")
    private String deskId;
    /**
     * 桌号名称
     */
    @TableField("desk_name")
    private String deskName;
    /**
     * 进餐人数
     */
    @TableField("eat_num")
    private Integer eatNum;
    /**
     * 桌位金额
     */
    @TableField("desk_amount")
    private Integer deskAmount;
    /**
     * 税金
     */
    @TableField("tax_amount")
    private Integer taxAmount;
    /**
     * 菜单金额
     */
    @TableField("menu_amount")
    private Integer menuAmount;
    /**
     * 总金额
     */
    @TableField("total_amount")
    private Integer totalAmount;
    /**
     * 收银
     */
    @TableField("cash_amount")
    private Integer cashAmount;
    /**
     * 支付方式
     */
    @TableField("pay_way")
    private String payWay;
    @TableField("bill_way")
    private String billWay;
    /**
     * 是否结算
     */
    @ItemTag(type="whether_type")
    @TableField("whether_pay")
    private String whetherPay;
    /**
     * 支付时间
     */
    @TableField("pay_time")
    private Date payTime;
    /**
     * 订单时间
     */
    @TableField("order_time")
    private Date orderTime;
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
     * 是否删除0否1是
     */
    @TableField("whether_remove")
    private String whetherRemove;
    /**
     * 删除时间
     */
    @TableField("remove_time")
    private Date removeTime;
    /**
     * 备注
     */
    @TableField("order_remark")
    private String orderRemark;

    @TableField(exist = false)
    private List<OrderLine> dataList;
    
    @TableField(exist = false)
    private Integer smallTotalAmount;
    //税前金额
    @TableField(exist = false)
    private Integer taxBeforeAmount;
    //税后金额
    @TableField(exist = false)
    private Integer taxAfterAmount;
    
    /**
     * 消费税
     */
    @TableField("consume_tax")
    private Integer consumeTax;
	
	/**
     * 外卖税金
     */
    @TableField("out_tax_amount")
    private Integer outTaxAmount;
    /**
     * 订单类型1普通单2合并母单3合并子单
     */
    @TableField("order_type")
    private String orderType;
    /**
     * 积分抵扣金额
     */
    @TableField("point_amount")
    private Integer pointAmount;
    @TableField("plus_point")
    private Integer plusPoint;
    /**
     * 抵扣积分
     */
    @TableField("sub_point")
    private Integer subPoint;
    /**
     * 抵扣会员编号
     */
    @TableField("member_id")
    private String memberId;
    
    /**
     * 就餐人数
     */
    @TableField("desk_num")
    private Integer deskNum;
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
     * 订单状态 0待处理1准备中2配送中3待提货4完成
     */
    @ItemTag(type="order_status")
    @TableField("order_status")
    private String orderStatus;
    /**
     * 配送地址编号
     */
    @TableField("address_id")
    private String addressId;
    /**
     * 订单地址
     */
    @TableField("order_address")
    private String orderAddress;
    /**
     * 附件地址
     */
    @TableField("append_address")
    private String appendAddress;
    /**
     * 下单员工
     */
    @TableField("order_staff")
    private String orderStaff;
    /**
     * 最后操作员工
     */
    @TableField("update_staff")
    private String updateStaff;
    /**
     * 订单类别1配送2外带
     */
    @ItemTag(type="category_type")
    @TableField("order_category")
    private String orderCategory;
   
    /**
     * 外卖电话
     */
    private String mobile;
    /**
     * 外卖用户名
     */
    private String username;
    /**
     * 拿货时间
     */
    @TableField("pick_up_time")
    private Date pickUpTime;
    /**
     * 是否需要发票0否1是
     */
    @TableField("whether_receipt")
    private String whetherReceipt;
    /**
     * 发票抬头
     */
    @TableField("receipt_title")
    private String receiptTitle;
    @TableField(exist = false)
    private String shopName;
    @TableField(exist = false)
    private String shopFirstName;
    
    /**
     * 配送费
     */
    @TableField("deliver_amount")
    private Integer deliverAmount;
    
    /**
     * 取餐码
     */
    @TableField("food_num")
    private String foodNum;
    
    /**
     * 信用卡金额
     */
    @TableField("credit_amount")
    private Integer creditAmount;
    /**
     * 优惠券金额
     */
    @TableField("coupons_amount")
    private Integer couponsAmount;
    /**
     * 找零金额
     */
    @TableField("zero_amount")
    private Integer zeroAmount;
    
    /**
     * 其他金额
     */
    @TableField("other_amount")
    private Integer otherAmount;
    public Integer getOtherAmount() {
		return otherAmount;
	}

	public void setOtherAmount(Integer otherAmount) {
		this.otherAmount = otherAmount;
	}

	public Integer getZeroAmount() {
		return zeroAmount;
	}

	public void setZeroAmount(Integer zeroAmount) {
		this.zeroAmount = zeroAmount;
	}

	@TableField(exist = false)
    private Integer historyTotalAmount;
    
    public Integer getHistoryTotalAmount() {
		return historyTotalAmount;
	}

	public void setHistoryTotalAmount(Integer historyTotalAmount) {
		this.historyTotalAmount = historyTotalAmount;
	}

	public Integer getCreditAmount() {
		return creditAmount;
	}

	public void setCreditAmount(Integer creditAmount) {
		this.creditAmount = creditAmount;
	}

	public Integer getCouponsAmount() {
		return couponsAmount;
	}

	public void setCouponsAmount(Integer couponsAmount) {
		this.couponsAmount = couponsAmount;
	}

	public String getFoodNum() {
		return foodNum;
	}

	public void setFoodNum(String foodNum) {
		this.foodNum = foodNum;
	}

	public Integer getDeliverAmount() {
		return deliverAmount;
	}

	public void setDeliverAmount(Integer deliverAmount) {
		this.deliverAmount = deliverAmount;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getShopFirstName() {
		return shopFirstName;
	}

	public void setShopFirstName(String shopFirstName) {
		this.shopFirstName = shopFirstName;
	}

	public Date getPickUpTime() {
		return pickUpTime;
	}

	public void setPickUpTime(Date pickUpTime) {
		this.pickUpTime = pickUpTime;
	}

	public String getWhetherReceipt() {
		return whetherReceipt;
	}

	public void setWhetherReceipt(String whetherReceipt) {
		this.whetherReceipt = whetherReceipt;
	}

	public String getReceiptTitle() {
		return receiptTitle;
	}

	public void setReceiptTitle(String receiptTitle) {
		this.receiptTitle = receiptTitle;
	}

	public String getOrderCategory() {
		return orderCategory;
	}

	public void setOrderCategory(String orderCategory) {
		this.orderCategory = orderCategory;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getOrderAddress() {
		return orderAddress;
	}

	public void setOrderAddress(String orderAddress) {
		this.orderAddress = orderAddress;
	}

	public String getAppendAddress() {
		return appendAddress;
	}

	public void setAppendAddress(String appendAddress) {
		this.appendAddress = appendAddress;
	}

	public String getOrderStaff() {
		return orderStaff;
	}

	public void setOrderStaff(String orderStaff) {
		this.orderStaff = orderStaff;
	}

	public String getUpdateStaff() {
		return updateStaff;
	}

	public void setUpdateStaff(String updateStaff) {
		this.updateStaff = updateStaff;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getAddressId() {
		return addressId;
	}

	public void setAddressId(String addressId) {
		this.addressId = addressId;
	}

	public Integer getConsumeTax() {
		return consumeTax;
	}

	public void setConsumeTax(Integer consumeTax) {
		this.consumeTax = consumeTax;
	}

    public Integer getTaxBeforeAmount() {
		return taxBeforeAmount;
	}

	public void setTaxBeforeAmount(Integer taxBeforeAmount) {
		this.taxBeforeAmount = taxBeforeAmount;
	}

	public Integer getTaxAfterAmount() {
		return taxAfterAmount;
	}

	public void setTaxAfterAmount(Integer taxAfterAmount) {
		this.taxAfterAmount = taxAfterAmount;
	}

    public Integer getDeskNum() {
		return deskNum;
	}

	public void setDeskNum(Integer deskNum) {
		this.deskNum = deskNum;
	}

	public Integer getPointAmount() {
		return pointAmount;
	}

	public void setPointAmount(Integer pointAmount) {
		this.pointAmount = pointAmount;
	}

	public Integer getPlusPoint() {
		return plusPoint;
	}

	public void setPlusPoint(Integer plusPoint) {
		this.plusPoint = plusPoint;
	}

	public Integer getSubPoint() {
		return subPoint;
	}

	public void setSubPoint(Integer subPoint) {
		this.subPoint = subPoint;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
    public Integer getOutTaxAmount() {
		return outTaxAmount;
	}

	public void setOutTaxAmount(Integer outTaxAmount) {
		this.outTaxAmount = outTaxAmount;
	}

	public Integer getSmallTotalAmount() {
		return smallTotalAmount;
	}

	public void setSmallTotalAmount(Integer smallTotalAmount) {
		this.smallTotalAmount = smallTotalAmount;
	}

	public List<OrderLine> getDataList() {
		return dataList;
	}

	public void setDataList(List<OrderLine> dataList) {
		this.dataList = dataList;
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

    public String getDeskId() {
        return deskId;
    }

    public void setDeskId(String deskId) {
        this.deskId = deskId;
    }

    public String getDeskName() {
        return deskName;
    }

    public void setDeskName(String deskName) {
        this.deskName = deskName;
    }

    public Integer getEatNum() {
        return eatNum;
    }

    public void setEatNum(Integer eatNum) {
        this.eatNum = eatNum;
    }

    public Integer getDeskAmount() {
        return deskAmount;
    }

    public void setDeskAmount(Integer deskAmount) {
        this.deskAmount = deskAmount;
    }

    public Integer getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(Integer taxAmount) {
        this.taxAmount = taxAmount;
    }

    public Integer getMenuAmount() {
        return menuAmount;
    }

    public void setMenuAmount(Integer menuAmount) {
        this.menuAmount = menuAmount;
    }

    public Integer getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Integer totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getCashAmount() {
        return cashAmount;
    }

    public void setCashAmount(Integer cashAmount) {
        this.cashAmount = cashAmount;
    }

    public String getPayWay() {
        return payWay;
    }

    public void setPayWay(String payWay) {
        this.payWay = payWay;
    }

    public String getBillWay() {
        return billWay;
    }

    public void setBillWay(String billWay) {
        this.billWay = billWay;
    }

    public String getWhetherPay() {
        return whetherPay;
    }

    public void setWhetherPay(String whetherPay) {
        this.whetherPay = whetherPay;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
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

    public String getWhetherRemove() {
        return whetherRemove;
    }

    public void setWhetherRemove(String whetherRemove) {
        this.whetherRemove = whetherRemove;
    }

    public Date getRemoveTime() {
        return removeTime;
    }

    public void setRemoveTime(Date removeTime) {
        this.removeTime = removeTime;
    }

    public String getOrderRemark() {
        return orderRemark;
    }

    public void setOrderRemark(String orderRemark) {
        this.orderRemark = orderRemark;
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
        return this.orderId;
    }

    @Override
    public String toString() {
        return "Order{" +
        "orderId=" + orderId +
        ", shopId=" + shopId +
        ", deskId=" + deskId +
        ", deskName=" + deskName +
        ", eatNum=" + eatNum +
        ", deskAmount=" + deskAmount +
        ", taxAmount=" + taxAmount +
        ", outTaxAmount=" + outTaxAmount +
        ", menuAmount=" + menuAmount +
        ", totalAmount=" + totalAmount +
        ", cashAmount=" + cashAmount +
        ", payWay=" + payWay +
        ", billWay=" + billWay +
        ", whetherPay=" + whetherPay +
        ", payTime=" + payTime +
        ", orderTime=" + orderTime +
        ", createTime=" + createTime +
        ", createBy=" + createBy +
        ", updateTime=" + updateTime +
        ", updateBy=" + updateBy +
        ", whetherRemove=" + whetherRemove +
        ", removeTime=" + removeTime +
        ", orderRemark=" + orderRemark +
        ", orderType=" + orderType +
        ", pointAmount=" + pointAmount +
        ", plusPoint=" + plusPoint +
        ", subPoint=" + subPoint +
        ", memberId=" + memberId +
        ", deskNum=" + deskNum +
        ", subAmount=" + subAmount +
        ", subRate=" + subRate +
        "}";
    }
}
