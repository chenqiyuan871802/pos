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
 * 菜单配置
 * </p>
 *
 * @author 陈骑元
 * @since 2018-09-28
 */
@TableName("sys_menu")
public class Menu extends BaseModel<Menu> {

    private static final long serialVersionUID = 1L;

    /**
     * 菜单编号
     */
    @TableId("menu_id")
    private String menuId;
    /**
     * 分类科目语义ID
     */
    @TableField("cascade_id")
    private String cascadeId;
    /**
     * 菜单名称
     */
    @TableField("menu_name")
    private String menuName;
    /**
     * 菜单编码
     */
    @TableField("menu_code")
    private String menuCode;
    
    @ItemTag(type="menu_type")
    @TableField("menu_type")
    private String menuType;
    /**
     * 菜单父级编号
     */
    @TableField("parent_id")
    private String parentId;
    /**
     * 图标名称
     */
    @TableField("icon_name")
    private String iconName;
    /**
     * 是否自动展开
     */
    @ItemTag(type="is_auto_expand")
    @TableField("is_auto_expand")
    private String isAutoExpand;
    /**
     * url地址
     */
    private String url;
    /**
     * 备注
     */
    private String remark;
    /**
     * 当前状态(0:停用;1:启用)
     */
    @ItemTag(type="status")
    private String status;
    /**
     * 编辑模式(0:只读;1:可编辑)
     */
    @ItemTag(type="edit_mode")
    @TableField("edit_mode")
    private String editMode;
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
     * 创建人
     */
    @TableField("create_by")
    private String createBy;
    /**
     * 修改时间
     */
    @TableField("update_time")
    private Date updateTime;
    /**
     * 更新用户
     */
    @TableField("update_by")
    private String updateBy;
    /**
     * 模块类型 1父级菜单2子菜单3按钮
     */
    @ItemTag(type="module_type")
    @TableField("module_type")
    private String moduleType;

   

	public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getCascadeId() {
        return cascadeId;
    }

    public void setCascadeId(String cascadeId) {
        this.cascadeId = cascadeId;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getMenuCode() {
        return menuCode;
    }

    public void setMenuCode(String menuCode) {
        this.menuCode = menuCode;
    }

    public String getMenuType() {
        return menuType;
    }

    public void setMenuType(String menuType) {
        this.menuType = menuType;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getIconName() {
        return iconName;
    }

    public void setIconName(String iconName) {
        this.iconName = iconName;
    }

    public String getIsAutoExpand() {
        return isAutoExpand;
    }

    public void setIsAutoExpand(String isAutoExpand) {
        this.isAutoExpand = isAutoExpand;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEditMode() {
        return editMode;
    }

    public void setEditMode(String editMode) {
        this.editMode = editMode;
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
    public String getModuleType() {
		return moduleType;
	}

	public void setModuleType(String moduleType) {
		this.moduleType = moduleType;
	}
    @Override
    protected Serializable pkVal() {
        return this.menuId;
    }
    
    @Override
    public String toString() {
        return "Menu{" +
        ", menuId=" + menuId +
        ", cascadeId=" + cascadeId +
        ", menuName=" + menuName +
        ", menuCode=" + menuCode +
        ", menuType=" + menuType +
        ", parentId=" + parentId +
        ", iconName=" + iconName +
        ", isAutoExpand=" + isAutoExpand +
        ", url=" + url +
        ", remark=" + remark +
        ", status=" + status +
        ", editMode=" + editMode +
        ", sortNo=" + sortNo +
        ", createTime=" + createTime +
        ", createBy=" + createBy +
        ", updateTime=" + updateTime +
        ", updateBy=" + updateBy +
        ", moduleType=" + moduleType +
        "}";
    }
}
