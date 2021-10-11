package com.ims.system.model;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

/**
 * <p>
 * 角色与用户关联
 * </p>
 *
 * @author 陈骑元
 * @since 2018-10-02
 */
@TableName("sys_role_user")
public class RoleUser extends Model<RoleUser> {

    private static final long serialVersionUID = 1L;

    /**
     * 角色与用户编号
     */
    @TableId("role_user_id")
    private String roleUserId;
    /**
     * 角色编号
     */
    @TableField("role_id")
    private String roleId;
    /**
     * 用户编号
     */
    @TableField("user_id")
    private String userId;
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


    public String getRoleUserId() {
        return roleUserId;
    }

    public void setRoleUserId(String roleUserId) {
        this.roleUserId = roleUserId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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
        return this.roleUserId;
    }

    @Override
    public String toString() {
        return "RoleUser{" +
        ", roleUserId=" + roleUserId +
        ", roleId=" + roleId +
        ", userId=" + userId +
        ", createBy=" + createBy +
        ", createTime=" + createTime +
        "}";
    }
}
