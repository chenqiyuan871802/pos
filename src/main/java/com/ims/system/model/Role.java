package com.ims.system.model;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.ims.core.annotation.ItemTag;
import com.ims.core.matatype.impl.BaseModel;

/**
 * <p>
 * 角色表
 * </p>
 *
 * @author 陈骑元
 * @since 2018-10-02
 */
@TableName("sys_role")
public class Role extends BaseModel<Role> {

    private static final long serialVersionUID = 1L;

    /**
     *  流水号
     */
    @TableId("role_id")
    private String roleId;
    /**
     * 角色名称
     */
    @TableField("role_name")
    private String roleName;
    /**
     * 当前状态 1启用 0禁用
     */
    @ItemTag(type="status")
    private String status;
    /**
     * 角色类型
     */
    @ItemTag(type="role_type")
    @TableField("role_type")
    private String roleType;
    /**
     * 备注
     */
    @TableField("role_remark")
    private String roleRemark;
    /**
     * 编辑模式(0:只读;1:可编辑)
     */
    @ItemTag(type="edit_mode")
    @TableField("edit_mode")
    private String editMode;
    /**
     * 创建用户编号
     */
    @TableField("create_by")
    private String createBy;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 修改用户ID
     */
    @TableField("update_by")
    private String updateBy;
    /**
     * 修改时间
     */
    @TableField("update_time")
    private Date updateTime;


    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRoleType() {
        return roleType;
    }

    public void setRoleType(String roleType) {
        this.roleType = roleType;
    }

    public String getRoleRemark() {
        return roleRemark;
    }

    public void setRoleRemark(String roleRemark) {
        this.roleRemark = roleRemark;
    }

    public String getEditMode() {
        return editMode;
    }

    public void setEditMode(String editMode) {
        this.editMode = editMode;
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

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    protected Serializable pkVal() {
        return this.roleId;
    }

    @Override
    public String toString() {
        return "Role{" +
        ", roleId=" + roleId +
        ", roleName=" + roleName +
        ", status=" + status +
        ", roleType=" + roleType +
        ", roleRemark=" + roleRemark +
        ", editMode=" + editMode +
        ", createBy=" + createBy +
        ", createTime=" + createTime +
        ", updateBy=" + updateBy +
        ", updateTime=" + updateTime +
        "}";
    }
}
