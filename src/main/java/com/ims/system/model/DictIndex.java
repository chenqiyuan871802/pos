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
 * 数据字典索引表
 * </p>
 *
 * @author 陈骑元
 * @since 2018-05-12
 */
@TableName("sys_dict_index")
public class DictIndex extends BaseModel<DictIndex> {

    private static final long serialVersionUID = 1L;

    /**
     * 流水号
     */
    @TableId("dict_index_id")
    private String dictIndexId;
    /**
     * 字典标识
     */
    @TableField("dict_key")
    private String dictKey;
    /**
     * 字典名称
     */
    @TableField("dict_name")
    private String dictName;
    /**
     * 字典分类 1系统2业务
     */
    @ItemTag(type="dict_type")
    @TableField("dict_type")
    private String dictType;
    /**
     * 备注
     */
    @TableField("dict_remark")
    private String dictRemark;
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


    public String getDictIndexId() {
        return dictIndexId;
    }

    public void setDictIndexId(String dictIndexId) {
        this.dictIndexId = dictIndexId;
    }

    public String getDictKey() {
        return dictKey;
    }

    public void setDictKey(String dictKey) {
        this.dictKey = dictKey;
    }

    public String getDictName() {
        return dictName;
    }

    public void setDictName(String dictName) {
        this.dictName = dictName;
    }

    public String getDictType() {
        return dictType;
    }

    public void setDictType(String dictType) {
        this.dictType = dictType;
    }

    public String getDictRemark() {
        return dictRemark;
    }

    public void setDictRemark(String dictRemark) {
        this.dictRemark = dictRemark;
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
        return this.dictIndexId;
    }

    @Override
    public String toString() {
        return "DictIndex{" +
        ", dictIndexId=" + dictIndexId +
        ", dictKey=" + dictKey +
        ", dictName=" + dictName +
        ", dictType=" + dictType +
        ", dictRemark=" + dictRemark +
        ", createTime=" + createTime +
        ", createBy=" + createBy +
        ", updateTime=" + updateTime +
        ", updateBy=" + updateBy +
        "}";
    }
}
