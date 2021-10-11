package com.ims.buss.model;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.ims.core.matatype.impl.BaseModel;
import java.io.Serializable;

/**
 * <p>
 * 菜单规格
 * </p>
 *
 * @author 陈骑元
 * @since 2019-07-20
 */
@TableName("t_menu_spec")
public class MenuSpec extends BaseModel<MenuSpec> {

    private static final long serialVersionUID = 1L;

    /**
     * 规格编号
     */
    @TableId("spec_id")
    private String specId;
    /**
     * 菜单目录索引
     */
    @TableField("menu_index_id")
    private String menuIndexId;
    /**
     * 规格索引编号
     */
    @TableField("spec_index_id")
    private String specIndexId;
    /**
     * 规格名称
     */
    @TableField("spec_name")
    private String specName;
    /**
     * 规格价格
     */
    @TableField("spec_price")
    private Integer specPrice;
    /**
     * 店铺编号
     */
    @TableField("shop_id")
    private String shopId;
    /**
     * 语种
     */
    @TableField("language_type")
    private String languageType;
    /**
     * 规格类型 0主规格1子规格
     */
    @TableField("spec_type")
    private String specType;
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


    public String getSpecId() {
        return specId;
    }

    public void setSpecId(String specId) {
        this.specId = specId;
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

    public String getSpecName() {
        return specName;
    }

    public void setSpecName(String specName) {
        this.specName = specName;
    }

    public Integer getSpecPrice() {
        return specPrice;
    }

    public void setSpecPrice(Integer specPrice) {
        this.specPrice = specPrice;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getLanguageType() {
        return languageType;
    }

    public void setLanguageType(String languageType) {
        this.languageType = languageType;
    }

    public String getSpecType() {
        return specType;
    }

    public void setSpecType(String specType) {
        this.specType = specType;
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
        return this.specId;
    }

    @Override
    public String toString() {
        return "MenuSpec{" +
        "specId=" + specId +
        ", menuIndexId=" + menuIndexId +
        ", specIndexId=" + specIndexId +
        ", specName=" + specName +
        ", specPrice=" + specPrice +
        ", shopId=" + shopId +
        ", languageType=" + languageType +
        ", specType=" + specType +
        ", createTime=" + createTime +
        ", createBy=" + createBy +
        ", updateTime=" + updateTime +
        ", updateBy=" + updateBy +
        "}";
    }
}
