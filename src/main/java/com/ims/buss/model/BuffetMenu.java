package com.ims.buss.model;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.ims.core.matatype.impl.BaseModel;
import java.io.Serializable;

/**
 * <p>
 * 自助餐菜单关联表
 * </p>
 *
 * @author 陈骑元
 * @since 2019-08-28
 */
@TableName("t_buffet_menu")
public class BuffetMenu extends BaseModel<BuffetMenu> {

    private static final long serialVersionUID = 1L;

    /**
     * 自助餐菜单编号
     */
    @TableId("buffet_menu_id")
    private String buffetMenuId;
    @TableField("catalog_index_id")
    private String catalogIndexId;
    @TableField("menu_index_id")
    private String menuIndexId;
    /**
     * 创建人编号
     */
    @TableField("create_by")
    private String createBy;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;


    public String getBuffetMenuId() {
        return buffetMenuId;
    }

    public void setBuffetMenuId(String buffetMenuId) {
        this.buffetMenuId = buffetMenuId;
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

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    protected Serializable pkVal() {
        return this.buffetMenuId;
    }

    @Override
    public String toString() {
        return "BuffetMenu{" +
        "buffetMenuId=" + buffetMenuId +
        ", catalogIndexId=" + catalogIndexId +
        ", menuIndexId=" + menuIndexId +
        ", createBy=" + createBy +
        ", createTime=" + createTime +
        "}";
    }
}
