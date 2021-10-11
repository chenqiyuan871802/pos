package com.ims.buss.model;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.ims.core.matatype.impl.BaseModel;
import java.io.Serializable;

/**
 * <p>
 * 区域表
 * </p>
 *
 * @author 陈骑元
 * @since 2019-10-14
 */
@TableName("t_area")
public class Area extends BaseModel<Area> {

    private static final long serialVersionUID = 1L;

    @TableId("area_id")
    private String areaId;
    /**
     * 区域名称
     */
    @TableField("area_name")
    private String areaName;
    /**
     * 区域备注
     */
    @TableField("area_remark")
    private String areaRemark;
    /**
     * 排序号
     */
    @TableField("sort_no")
    private Integer sortNo;
    /**
     * 店铺编号
     */
    @TableField("shop_id")
    private String shopId;
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


    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getAreaRemark() {
        return areaRemark;
    }

    public void setAreaRemark(String areaRemark) {
        this.areaRemark = areaRemark;
    }

    public Integer getSortNo() {
        return sortNo;
    }

    public void setSortNo(Integer sortNo) {
        this.sortNo = sortNo;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
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
        return this.areaId;
    }

    @Override
    public String toString() {
        return "Area{" +
        "areaId=" + areaId +
        ", areaName=" + areaName +
        ", areaRemark=" + areaRemark +
        ", sortNo=" + sortNo +
        ", shopId=" + shopId +
        ", createTime=" + createTime +
        ", createBy=" + createBy +
        ", updateTime=" + updateTime +
        ", updateBy=" + updateBy +
        "}";
    }
}
