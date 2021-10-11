package com.ims.buss.model;

import java.util.Date;


import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.ims.core.annotation.ItemTag;
import com.ims.core.matatype.impl.BaseModel;
import java.io.Serializable;

/**
 * <p>
 * 语言包
 * </p>
 *
 * @author 陈骑元
 * @since 2019-08-03
 */
@TableName("t_language_pack")
public class LanguagePack extends BaseModel<LanguagePack> {

    private static final long serialVersionUID = 1L;

    /**
     * 语言编号
     */
    @TableId("language_id")
    private String languageId;
    /**
     * 语言名称
     */
    @TableField("language_name")
    private String languageName;
    /**
     * 语言对照码
     */
    @TableField("language_code")
    private String languageCode;
    /**
     * 语言对照值
     */
    @TableField("language_value")
    private String languageValue;
    /**
     * 语言类型
     */
    @ItemTag(type="language_type")
    @TableField("language_type")
    private String languageType;
    /**
     * 备注
     */
   
    @TableField("language_remark")
    private String languageRemark;
    /**
     * 当前状态(0:停用;1:启用)
     */
    private String status;
    /**
     * 编辑模式(0:只读;1:可编辑)
     */
    @TableField("edit_mode")
    private String editMode;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 创建用户
     */
    @TableField("create_by")
    private String createBy;
    /**
     * 修改时间
     */
    @TableField("update_time")
    private Date updateTime;
    /**
     * 修改用户
     */
    @TableField("update_by")
    private String updateBy;


    public String getLanguageId() {
        return languageId;
    }

    public void setLanguageId(String languageId) {
        this.languageId = languageId;
    }

    public String getLanguageName() {
        return languageName;
    }

    public void setLanguageName(String languageName) {
        this.languageName = languageName;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public String getLanguageValue() {
        return languageValue;
    }

    public void setLanguageValue(String languageValue) {
        this.languageValue = languageValue;
    }

    public String getLanguageType() {
        return languageType;
    }

    public void setLanguageType(String languageType) {
        this.languageType = languageType;
    }

    public String getLanguageRemark() {
        return languageRemark;
    }

    public void setLanguageRemark(String languageRemark) {
        this.languageRemark = languageRemark;
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
        return this.languageId;
    }

    @Override
    public String toString() {
        return "LanguagePack{" +
        "languageId=" + languageId +
        ", languageName=" + languageName +
        ", languageCode=" + languageCode +
        ", languageValue=" + languageValue +
        ", languageType=" + languageType +
        ", languageRemark=" + languageRemark +
        ", status=" + status +
        ", editMode=" + editMode +
        ", createTime=" + createTime +
        ", createBy=" + createBy +
        ", updateTime=" + updateTime +
        ", updateBy=" + updateBy +
        "}";
    }
}
