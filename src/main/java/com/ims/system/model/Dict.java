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
 * 数据字典
 * </p>
 *
 * @author 陈骑元
 * @since 2018-05-12
 */
@TableName("sys_dict")
public class Dict extends BaseModel<Dict> {

    private static final long serialVersionUID = 1L;

    /**
     * 字典编号
     */
    @TableId("dict_id")
    private String dictId;
    /**
     * 所属字典流水号
     */
    @TableField("dict_index_id")
    private String dictIndexId;
    /**
     * 字典对照码
     */
    @TableField("dict_code")
    private String dictCode;
    /**
     * 字典对照值
     */
    @TableField("dict_value")
    private String dictValue;
    /**
     * 显示颜色
     */
    @TableField("show_color")
    private String showColor;
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
     * 创建用户编号
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
     * 字典标识
     */
    @TableField(exist = false)
	private String dictKey;
    
    /**
     * 字典标识名称
     */
    @TableField(exist = false)
	private String dictName;
    
    public String getDictName() {
		return dictName;
	}

	public void setDictName(String dictName) {
		this.dictName = dictName;
	}

	public String getDictKey() {
		return dictKey;
	}

	public void setDictKey(String dictKey) {
		this.dictKey = dictKey;
	}



    public String getDictId() {
        return dictId;
    }

    public void setDictId(String dictId) {
        this.dictId = dictId;
    }

    public String getDictIndexId() {
        return dictIndexId;
    }

    public void setDictIndexId(String dictIndexId) {
        this.dictIndexId = dictIndexId;
    }

    public String getDictCode() {
        return dictCode;
    }

    public void setDictCode(String dictCode) {
        this.dictCode = dictCode;
    }

    public String getDictValue() {
        return dictValue;
    }

    public void setDictValue(String dictValue) {
        this.dictValue = dictValue;
    }

    public String getShowColor() {
        return showColor;
    }

    public void setShowColor(String showColor) {
        this.showColor = showColor;
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

    @Override
    protected Serializable pkVal() {
        return this.dictId;
    }

    @Override
    public String toString() {
        return "Dict{" +
        ", dictId=" + dictId +
        ", dictIndexId=" + dictIndexId +
        ", dictCode=" + dictCode +
        ", dictValue=" + dictValue +
        ", showColor=" + showColor +
        ", status=" + status +
        ", editMode=" + editMode +
        ", sortNo=" + sortNo +
        ", createTime=" + createTime +
        ", createBy=" + createBy +
        ", updateTime=" + updateTime +
        ", updateBy=" + updateBy +
        "}";
    }
}
