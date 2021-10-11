package com.ims.buss.model;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.ims.core.matatype.impl.BaseModel;
import java.io.Serializable;

/**
 * <p>
 * 菜品规格步骤
 * </p>
 *
 * @author 陈骑元
 * @since 2019-08-28
 */
@TableName("t_menu_step")
public class MenuStep extends BaseModel<MenuStep> {

    private static final long serialVersionUID = 1L;

    /**
     * 步骤编号
     */
    @TableId("step_id")
    private String stepId;
    /**
     * 步骤索引编号
     */
    @TableField("step_index_id")
    private String stepIndexId;
    /**
     * 菜单索引编号
     */
    @TableField("menu_index_id")
    private String menuIndexId;
    /**
     *  店铺编号
     */
    @TableField("shop_id")
    private String shopId;
    /**
     * 步骤编号
     */
    @TableField("step_num")
    private String stepNum;
    /**
     * 名称
     */
    @TableField("step_name")
    private String stepName;
    /**
     * 选择类型1单选2多选
     */
    @TableField("choose_type")
    private String chooseType;
    /**
     * 最多限制多少个，0不限制
     */
    @TableField("max_choose")
    private Integer maxChoose;
    public Integer getMaxChoose() {
		return maxChoose;
	}

	public void setMaxChoose(Integer maxChoose) {
		this.maxChoose = maxChoose;
	}

	/**
     * 语言
     */
    @TableField("language_type")
    private String languageType;
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
    @TableField(exist = false)
    private List<StepSpec> stepSpecList;


    public List<StepSpec> getStepSpecList() {
		return stepSpecList;
	}

	public void setStepSpecList(List<StepSpec> stepSpecList) {
		this.stepSpecList = stepSpecList;
	}

	public String getStepId() {
        return stepId;
    }

    public void setStepId(String stepId) {
        this.stepId = stepId;
    }

    public String getStepIndexId() {
        return stepIndexId;
    }

    public void setStepIndexId(String stepIndexId) {
        this.stepIndexId = stepIndexId;
    }

    public String getMenuIndexId() {
        return menuIndexId;
    }

    public void setMenuIndexId(String menuIndexId) {
        this.menuIndexId = menuIndexId;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getStepNum() {
        return stepNum;
    }

    public void setStepNum(String stepNum) {
        this.stepNum = stepNum;
    }

    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    public String getChooseType() {
        return chooseType;
    }

    public void setChooseType(String chooseType) {
        this.chooseType = chooseType;
    }

    public String getLanguageType() {
        return languageType;
    }

    public void setLanguageType(String languageType) {
        this.languageType = languageType;
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
        return this.stepId;
    }

    @Override
    public String toString() {
        return "MenuStep{" +
        "stepId=" + stepId +
        ", stepIndexId=" + stepIndexId +
        ", menuIndexId=" + menuIndexId +
        ", shopId=" + shopId +
        ", stepNum=" + stepNum +
        ", stepName=" + stepName +
        ", chooseType=" + chooseType +
        ", languageType=" + languageType +
        ", createTime=" + createTime +
        ", createBy=" + createBy +
        ", updateTime=" + updateTime +
        ", updateBy=" + updateBy +
        "}";
    }
}
