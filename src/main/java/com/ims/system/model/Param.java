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
 * 键值参数
 * </p>
 *
 * @author 陈骑元
 * @since 2018-04-07
 */
@TableName("sys_param")
public class Param extends BaseModel<Param> {

    private static final long serialVersionUID = 1L;

    /**
     * 参数编号
     */
    @TableId("param_id")
	private String paramId;
    /**
     * 参数名称
     */
	@TableField("param_name")
	private String paramName;
    /**
     * 参数键名
     */
	@TableField("param_key")
	private String paramKey;
    /**
     * 参数键值
     */
	@TableField("param_value")
	private String paramValue;
    /**
     * 参数备注
     */
	@TableField("param_remark")
	private String paramRemark;
    /**
     * 参数类型0:缺省;1:系统2:业务
     */
	@ItemTag(type="param_type")
	@TableField("param_type")
	private String paramType;
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


	public String getParamId() {
		return paramId;
	}

	public void setParamId(String paramId) {
		this.paramId = paramId;
	}

	public String getParamName() {
		return paramName;
	}

	public void setParamName(String paramName) {
		this.paramName = paramName;
	}

	public String getParamKey() {
		return paramKey;
	}

	public void setParamKey(String paramKey) {
		this.paramKey = paramKey;
	}

	public String getParamValue() {
		return paramValue;
	}

	public void setParamValue(String paramValue) {
		this.paramValue = paramValue;
	}

	public String getParamRemark() {
		return paramRemark;
	}

	public void setParamRemark(String paramRemark) {
		this.paramRemark = paramRemark;
	}

	public String getParamType() {
		return paramType;
	}

	public void setParamType(String paramType) {
		this.paramType = paramType;
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
		return this.paramId;
	}

	@Override
	public String toString() {
		return "Param{" +
			"paramId=" + paramId +
			", paramName=" + paramName +
			", paramKey=" + paramKey +
			", paramValue=" + paramValue +
			", paramRemark=" + paramRemark +
			", paramType=" + paramType +
			", status=" + status +
			", editMode=" + editMode +
			", createTime=" + createTime +
			", createBy=" + createBy +
			", updateTime=" + updateTime +
			", updateBy=" + updateBy +
			"}";
	}
}
