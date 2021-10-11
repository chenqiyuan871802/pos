package com.ims.buss.model;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.ims.core.matatype.impl.BaseModel;
import java.io.Serializable;

/**
 * <p>
 * 步骤规格
 * </p>
 *
 * @author 陈骑元
 * @since 2019-08-28
 */
@TableName("t_step_spec")
public class StepSpec extends BaseModel<StepSpec> {

    private static final long serialVersionUID = 1L;

    /**
     * 规格编号
     */
    @TableId("spec_id")
    private String specId;
    /**
     * 规格索引编号
     */
    @TableField("spec_index_id")
    private String specIndexId;
    @TableField("spec_name")
    private String specName;
    /**
     * 规格价格
     */
    @TableField("spec_price")
    private Integer specPrice;
    /**
     * 步骤规格索引编号
     */
    @TableField("step_index_id")
    private String stepIndexId;
    /**
     * 店铺编号
     */
    @TableField("shop_id")
    private String shopId;
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
    /**
     * 排序号
     */
    @TableField("sort_no")
    private Integer sortNo;
    @TableField(exist = false)
    private String stepName;
    @TableField(exist = false)
    private String stepNum;
    @TableField(exist = false)
    private String catalogName;
    @TableField(exist = false)
    private String menuName;
    @TableField(exist = false)
    private String menuIndexId;
    @TableField(exist = false)
    private String mealType;
    
    public String getMealType() {
		return mealType;
	}

	public void setMealType(String mealType) {
		this.mealType = mealType;
	}

	public String getMenuIndexId() {
		return menuIndexId;
	}

	public void setMenuIndexId(String menuIndexId) {
		this.menuIndexId = menuIndexId;
	}

	public String getStepName() {
		return stepName;
	}

	public void setStepName(String stepName) {
		this.stepName = stepName;
	}

	public String getStepNum() {
		return stepNum;
	}

	public void setStepNum(String stepNum) {
		this.stepNum = stepNum;
	}

	public String getCatalogName() {
		return catalogName;
	}

	public void setCatalogName(String catalogName) {
		this.catalogName = catalogName;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public String getSpecId() {
        return specId;
    }

    public void setSpecId(String specId) {
        this.specId = specId;
    }

    public String getSpecIndexId() {
        return specIndexId;
    }

    public void setSpecIndexId(String specIndexId) {
        this.specIndexId = specIndexId;
    }

    public String getSpecName() {
        return specName;
    }

    public void setSpecName(String specName) {
        this.specName = specName;
    }

    public Integer getSpecPrice() {
        return specPrice;
    }

    public void setSpecPrice(Integer specPrice) {
        this.specPrice = specPrice;
    }

    public String getStepIndexId() {
        return stepIndexId;
    }

    public void setStepIndexId(String stepIndexId) {
        this.stepIndexId = stepIndexId;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
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

    public Integer getSortNo() {
        return sortNo;
    }

    public void setSortNo(Integer sortNo) {
        this.sortNo = sortNo;
    }

    @Override
    protected Serializable pkVal() {
        return this.specId;
    }

    @Override
    public String toString() {
        return "StepSpec{" +
        "specId=" + specId +
        ", specIndexId=" + specIndexId +
        ", specName=" + specName +
        ", specPrice=" + specPrice +
        ", stepIndexId=" + stepIndexId +
        ", shopId=" + shopId +
        ", languageType=" + languageType +
        ", createTime=" + createTime +
        ", createBy=" + createBy +
        ", updateTime=" + updateTime +
        ", updateBy=" + updateBy +
        ", sortNo=" + sortNo +
        "}";
    }
}
