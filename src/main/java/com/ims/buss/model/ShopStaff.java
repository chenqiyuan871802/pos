package com.ims.buss.model;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.ims.core.matatype.impl.BaseModel;
import java.io.Serializable;

/**
 * <p>
 * 店铺员工
 * </p>
 *
 * @author 陈骑元
 * @since 2020-04-23
 */
@TableName("t_shop_staff")
public class ShopStaff extends BaseModel<ShopStaff> {

    private static final long serialVersionUID = 1L;

    /**
     * 员工ID
     */
    @TableId("staff_id")
    private String staffId;
    /**
     * 员工号
     */
    @TableField("staff_num")
    private String staffNum;
    /**
     * 员工姓名
     */
    @TableField("staff_name")
    private String staffName;
    /**
     * 员工类型0缺省默认
     */
    @TableField("staff_type")
    private String staffType;
    /**
     * 电话
     */
    @TableField("staff_phone")
    private String staffPhone;
    /**
     * 店铺编号
     */
    @TableField("shop_id")
    private String shopId;
    /**
     * 备注
     */
    @TableField("staff_remark")
    private String staffRemark;
    /**
     * 是否删除0否1是
     */
    @TableField("whether_remove")
    private String whetherRemove;
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


    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getStaffNum() {
        return staffNum;
    }

    public void setStaffNum(String staffNum) {
        this.staffNum = staffNum;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getStaffType() {
        return staffType;
    }

    public void setStaffType(String staffType) {
        this.staffType = staffType;
    }

    public String getStaffPhone() {
        return staffPhone;
    }

    public void setStaffPhone(String staffPhone) {
        this.staffPhone = staffPhone;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getStaffRemark() {
        return staffRemark;
    }

    public void setStaffRemark(String staffRemark) {
        this.staffRemark = staffRemark;
    }

    public String getWhetherRemove() {
        return whetherRemove;
    }

    public void setWhetherRemove(String whetherRemove) {
        this.whetherRemove = whetherRemove;
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

    @Override
    protected Serializable pkVal() {
        return this.staffId;
    }

    @Override
    public String toString() {
        return "ShopStaff{" +
        "staffId=" + staffId +
        ", staffNum=" + staffNum +
        ", staffName=" + staffName +
        ", staffType=" + staffType +
        ", staffPhone=" + staffPhone +
        ", shopId=" + shopId +
        ", staffRemark=" + staffRemark +
        ", whetherRemove=" + whetherRemove +
        ", createTime=" + createTime +
        ", createBy=" + createBy +
        ", updateTime=" + updateTime +
        ", updateBy=" + updateBy +
        "}";
    }
}
