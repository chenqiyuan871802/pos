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
 * 组织机构
 * </p>
 *
 * @author 陈骑元
 * @since 2018-05-14
 */
@TableName("sys_dept")
public class Dept extends BaseModel<Dept> {

    private static final long serialVersionUID = 1L;

    /**
     * 流水号
     */
    @TableId("dept_id")
    private String deptId;
    /**
     * 节点语义ID
     */
    @TableField("cascade_id")
    private String cascadeId;
    /**
     * 组织名称
     */
    @TableField("dept_name")
    private String deptName;
    /**
     * 父节点流水号
     */
    @TableField("parent_id")
    private String parentId;
    /**
     * 机构代码
     */
    @TableField("dept_code")
    private String deptCode;
    /**
     * 主要负责人
     */
    private String manager;
    /**
     * 部门电话
     */
    private String phone;
    /**
     * 传真
     */
    private String fax;
    /**
     * 地址
     */
    private String address;
    /**
     * 是否自动展开
     */
    @ItemTag(type="is_auto_expand")
    @TableField("is_auto_expand")
    private String isAutoExpand;
    /**
     * 节点图标文件名称
     */
    @TableField("icon_name")
    private String iconName;
    /**
     * 排序号
     */
    @TableField("sort_no")
    private Integer sortNo;
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
     * 修改用户ID
     */
    @TableField("update_by")
    private String updateBy;
    /**
     * 上级菜单名称
     */
    @TableField(exist = false)
    private String parentName;

    public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getCascadeId() {
        return cascadeId;
    }

    public void setCascadeId(String cascadeId) {
        this.cascadeId = cascadeId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIsAutoExpand() {
        return isAutoExpand;
    }

    public void setIsAutoExpand(String isAutoExpand) {
        this.isAutoExpand = isAutoExpand;
    }

    public String getIconName() {
        return iconName;
    }

    public void setIconName(String iconName) {
        this.iconName = iconName;
    }

    public Integer getSortNo() {
        return sortNo;
    }

    public void setSortNo(Integer sortNo) {
        this.sortNo = sortNo;
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
        return this.deptId;
    }

    @Override
    public String toString() {
        return "Dept{" +
        ", deptId=" + deptId +
        ", cascadeId=" + cascadeId +
        ", deptName=" + deptName +
        ", parentId=" + parentId +
        ", deptCode=" + deptCode +
        ", manager=" + manager +
        ", phone=" + phone +
        ", fax=" + fax +
        ", address=" + address +
        ", isAutoExpand=" + isAutoExpand +
        ", iconName=" + iconName +
        ", sortNo=" + sortNo +
        ", remark=" + remark +
        ", isDel=" + isDel +
        ", createTime=" + createTime +
        ", createBy=" + createBy +
        ", updateTime=" + updateTime +
        ", updateBy=" + updateBy +
        "}";
    }
}
