package com.ims.buss.model;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.ims.core.matatype.impl.BaseModel;
import java.io.Serializable;

/**
 * <p>
 * 订单关联表
 * </p>
 *
 * @author 陈骑元
 * @since 2019-10-19
 */
@TableName("t_order_link")
public class OrderLink extends BaseModel<OrderLink> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键编号
     */
    @TableId("link_id")
    private String linkId;
    /**
     * 父订单号
     */
    @TableField("parent_order_no")
    private String parentOrderNo;
    /**
     * 子订单号
     */
    @TableField("child_order_no")
    private String childOrderNo;
    /**
     * 创建人
     */
    @TableField("create_by")
    private String createBy;
    /**
     * 创建时间
     */
    @TableField("craete_time")
    private Date craeteTime;


    public String getLinkId() {
        return linkId;
    }

    public void setLinkId(String linkId) {
        this.linkId = linkId;
    }

    public String getParentOrderNo() {
        return parentOrderNo;
    }

    public void setParentOrderNo(String parentOrderNo) {
        this.parentOrderNo = parentOrderNo;
    }

    public String getChildOrderNo() {
        return childOrderNo;
    }

    public void setChildOrderNo(String childOrderNo) {
        this.childOrderNo = childOrderNo;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCraeteTime() {
        return craeteTime;
    }

    public void setCraeteTime(Date craeteTime) {
        this.craeteTime = craeteTime;
    }

    @Override
    protected Serializable pkVal() {
        return this.linkId;
    }

    @Override
    public String toString() {
        return "OrderLink{" +
        "linkId=" + linkId +
        ", parentOrderNo=" + parentOrderNo +
        ", childOrderNo=" + childOrderNo +
        ", createBy=" + createBy +
        ", craeteTime=" + craeteTime +
        "}";
    }
}
