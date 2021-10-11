package com.ims.buss.model;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.ims.core.matatype.impl.BaseModel;
import java.io.Serializable;

/**
 * <p>
 * 钱箱记录表
 * </p>
 *
 * @author 陈骑元
 * @since 2020-07-15
 */
@TableName("t_cashbox_record")
public class CashboxRecord extends BaseModel<CashboxRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * 记录编号
     */
    @TableId("record_id")
    private String recordId;
    /**
     * 店铺编号
     */
    @TableField("shop_id")
    private String shopId;
    /**
     * 金额
     */
    private Integer amount;
    /**
     * 记录编号0储备金1出金2入金
     */
    @TableField("record_type")
    private String recordType;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;


    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getRecordType() {
        return recordType;
    }

    public void setRecordType(String recordType) {
        this.recordType = recordType;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    protected Serializable pkVal() {
        return this.recordId;
    }

    @Override
    public String toString() {
        return "CashboxRecord{" +
        "recordId=" + recordId +
        ", shopId=" + shopId +
        ", amount=" + amount +
        ", recordType=" + recordType +
        ", createTime=" + createTime +
        "}";
    }
}
