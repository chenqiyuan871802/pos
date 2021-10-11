package com.ims.buss.model;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.ims.core.matatype.impl.BaseModel;
import java.io.Serializable;

/**
 * <p>
 * 菜品大类
 * </p>
 *
 * @author 陈骑元
 * @since 2020-07-26
 */
@TableName("t_menu_large")
public class MenuLarge extends BaseModel<MenuLarge> {

    private static final long serialVersionUID = 1L;

    /**
     * 分类编号
     */
    @TableId("large_id")
    private String largeId;
    /**
     * 大类名称
     */
    @TableField("large_name")
    private String largeName;
    /**
     * 店铺编号
     */
    @TableField("shop_id")
    private String shopId;
    /**
     * 排序号
     */
    @TableField("sort_no")
    private Integer sortNo;
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


    public String getLargeId() {
        return largeId;
    }

    public void setLargeId(String largeId) {
        this.largeId = largeId;
    }

    public String getLargeName() {
        return largeName;
    }

    public void setLargeName(String largeName) {
        this.largeName = largeName;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public Integer getSortNo() {
        return sortNo;
    }

    public void setSortNo(Integer sortNo) {
        this.sortNo = sortNo;
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
        return this.largeId;
    }

    @Override
    public String toString() {
        return "MenuLarge{" +
        "largeId=" + largeId +
        ", largeName=" + largeName +
        ", shopId=" + shopId +
        ", sortNo=" + sortNo +
        ", createTime=" + createTime +
        ", createBy=" + createBy +
        ", updateTime=" + updateTime +
        ", updateBy=" + updateBy +
        "}";
    }
}
