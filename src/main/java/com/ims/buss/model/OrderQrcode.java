package com.ims.buss.model;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.ims.core.matatype.impl.BaseModel;
import java.io.Serializable;

/**
 * <p>
 * 点餐生成二维码
 * </p>
 *
 * @author 陈骑元
 * @since 2019-07-28
 */
@TableName("t_order_qrcode")
public class OrderQrcode extends BaseModel<OrderQrcode> {

    private static final long serialVersionUID = 1L;

    /**
     * 点餐编号
     */
    @TableId("order_no")
    private String orderNo;
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
     * 点餐二维码
     */
    @TableField("order_qrcode")
    private String orderQrcode;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
    
    @TableField(exist = false)
    private int eatNum;
    public int getEatNum() {
		return eatNum;
	}

	public void setEatNum(int eatNum) {
		this.eatNum = eatNum;
	}

	
    
    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
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

    public String getOrderQrcode() {
        return orderQrcode;
    }

    public void setOrderQrcode(String orderQrcode) {
        this.orderQrcode = orderQrcode;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    protected Serializable pkVal() {
        return this.orderNo;
    }

    @Override
    public String toString() {
        return "OrderQrcode{" +
        "orderNo=" + orderNo +
        ", shopId=" + shopId +
        ", deskId=" + deskId +
        ", orderQrcode=" + orderQrcode +
        ", createTime=" + createTime +
        "}";
    }
}
