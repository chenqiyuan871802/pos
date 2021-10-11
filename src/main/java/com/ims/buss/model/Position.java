package com.ims.buss.model;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.ims.core.annotation.ItemTag;
import com.ims.core.constant.WebplusCons;
import com.ims.core.matatype.impl.BaseModel;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author 陈骑元
 * @since 2020-04-23
 */
@TableName("t_position")
public class Position extends BaseModel<Position> {

    private static final long serialVersionUID = 1L;

    /**
     * 自增编号
     */
    @TableId(value = "position_id", type = IdType.AUTO)
    private Integer positionId;
    /**
     * 代码
     */
    @TableField("position_code")
    private String positionCode;
    /**
     * 名称
     */
    @TableField("position_name")
    private String positionName;
    /**
     * 上级代码
     */
    @ItemTag(type=WebplusCons.POSITION_DICT)
    @TableField("parent_code")
    private String parentCode;
    /**
     * 类型1省2市3镇
     */
    @ItemTag(type="position_type")
    @TableField("position_type")
    private String positionType;
    /**
     * 日文名称
     */
    private String kana;


    public Integer getPositionId() {
        return positionId;
    }

    public void setPositionId(Integer positionId) {
        this.positionId = positionId;
    }

    public String getPositionCode() {
        return positionCode;
    }

    public void setPositionCode(String positionCode) {
        this.positionCode = positionCode;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public String getPositionType() {
        return positionType;
    }

    public void setPositionType(String positionType) {
        this.positionType = positionType;
    }

    public String getKana() {
        return kana;
    }

    public void setKana(String kana) {
        this.kana = kana;
    }

    @Override
    protected Serializable pkVal() {
        return this.positionId;
    }

    @Override
    public String toString() {
        return "Position{" +
        "positionId=" + positionId +
        ", positionCode=" + positionCode +
        ", positionName=" + positionName +
        ", parentCode=" + parentCode +
        ", positionType=" + positionType +
        ", kana=" + kana +
        "}";
    }
}
