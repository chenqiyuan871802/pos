package com.ims.system.model;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

/**
 * <p>
 * 菜单模块-角色关联表
 * </p>
 *
 * @author 陈骑元
 * @since 2018-10-02
 */
@TableName("sys_role_menu")
public class RoleMenu extends Model<RoleMenu> {

    private static final long serialVersionUID = 1L;

    /**
     * 角色与菜单关联编号
     */
    @TableId("role_menu_id")
    private String roleMenuId;
    /**
     *  角色流水号
     */
    @TableField("role_id")
    private String roleId;
    /**
     * 功能模块流水号
     */
    @TableField("menu_id")
    private String menuId;
    /**
     * 权限类型 1 经办权限 2管理权限
     */
    @TableField("grant_type")
    private String grantType;
    /**
     * 创建人ID
     */
    @TableField("create_by")
    private String createBy;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;


    public String getRoleMenuId() {
        return roleMenuId;
    }

    public void setRoleMenuId(String roleMenuId) {
        this.roleMenuId = roleMenuId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getGrantType() {
        return grantType;
    }

    public void setGrantType(String grantType) {
        this.grantType = grantType;
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
        return this.roleMenuId;
    }

    @Override
    public String toString() {
        return "RoleMenu{" +
        ", roleMenuId=" + roleMenuId +
        ", roleId=" + roleId +
        ", menuId=" + menuId +
        ", grantType=" + grantType +
        ", createBy=" + createBy +
        ", createTime=" + createTime +
        "}";
    }
}
