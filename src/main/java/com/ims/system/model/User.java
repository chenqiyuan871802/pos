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
 * 用户基本信息表
 * </p>
 *
 * @author 陈骑元
 * @since 2018-09-28
 */
@TableName("sys_user")
public class User extends BaseModel<User> {

    private static final long serialVersionUID = 1L;

    /**
     * 用户编号
     */
    @TableId("user_id")
    private String userId;
    /**
     * 用户登录帐号
     */
    private String account;
    /**
     * 密码
     */
    private String password;
    /**
     * 用户姓名
     */
    private String username;
    /**
     * 锁定次数 默认5次
     */
    @TableField("lock_num")
    private Integer lockNum;
    /**
     * 密码错误次数  如果等于锁定次数就自动锁定用户
     */
    @TableField("error_num")
    private Integer errorNum;
    /**
     * 性别  1:男2:女3:未知
     */
    @ItemTag(type="sex")
    private String sex;
    /**
     * 用户状态 1:正常2:停用 3:锁定
     */
    @ItemTag(type="user_status")
    private String status;
    /**
     * 用户类型
     */
    @ItemTag(type="user_type")
    @TableField("user_type")
    private String userType;
    /**
     * 所属部门流水号
     */
    @TableField("dept_id")
    private String deptId;
    /**
     * 联系电话
     */
    private String mobile;
    /**
     * QQ号码
     */
    private String qq;
    /**
     * 微信
     */
    private String wechat;
    /**
     * 电子邮件
     */
    private String email;
    /**
     * 身份证号
     */
    private String idno;
    /**
     * 界面风格
     */
    private String style;
    /**
     * 联系地址
     */
    private String address;
    /**
     * 备注
     */
    private String remark;
    /**
     * 是否已删除 0有效 1删除
     */
    @TableField("is_del")
    private String isDel;
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
    /**
     * 组织机构名称
     */
    @TableField(exist = false)
    private String deptName;
    
    public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getLockNum() {
        return lockNum;
    }

    public void setLockNum(Integer lockNum) {
        this.lockNum = lockNum;
    }

    public Integer getErrorNum() {
        return errorNum;
    }

    public void setErrorNum(Integer errorNum) {
        this.errorNum = errorNum;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIdno() {
        return idno;
    }

    public void setIdno(String idno) {
        this.idno = idno;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getIsDel() {
        return isDel;
    }

    public void setIsDel(String isDel) {
        this.isDel = isDel;
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
        return this.userId;
    }

    @Override
    public String toString() {
        return "User{" +
        ", userId=" + userId +
        ", account=" + account +
        ", password=" + password +
        ", username=" + username +
        ", lockNum=" + lockNum +
        ", errorNum=" + errorNum +
        ", sex=" + sex +
        ", status=" + status +
        ", userType=" + userType +
        ", deptId=" + deptId +
        ", mobile=" + mobile +
        ", qq=" + qq +
        ", wechat=" + wechat +
        ", email=" + email +
        ", idno=" + idno +
        ", style=" + style +
        ", address=" + address +
        ", remark=" + remark +
        ", isDel=" + isDel +
        ", createTime=" + createTime +
        ", createBy=" + createBy +
        ", updateTime=" + updateTime +
        ", updateBy=" + updateBy +
        "}";
    }
}
