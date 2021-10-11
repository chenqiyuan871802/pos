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
 * 菜单类目
 * </p>
 *
 * @author 陈骑元
 * @since 2019-09-06
 */
@TableName("t_menu_catalog")
public class MenuCatalog extends BaseModel<MenuCatalog> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键编号
     */
    @TableId("catalog_id")
    private String catalogId;
    /**
     * 类目编号
     */
    @TableField("catalog_index_id")
    private String catalogIndexId;
    /**
     * 类名
     */
    @TableField("catalog_name")
    private String catalogName;
    /**
     * 打印机编号
     */
    @TableField("print_num")
    private String printNum;
    /**
     * 店铺编号
     */
    @TableField("shop_id")
    private String shopId;
    /**
     * 顺序
     */
    @TableField("sort_no")
    private Integer sortNo;
    /**
     * 语言种类
     */
    @TableField("language_type")
    private String languageType;
    /**
     * 备注
     */
    @TableField("catalog_remark")
    private String catalogRemark;
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
     * 是否自助餐0否1是
     */
    @TableField("whether_buffet")
    private String whetherBuffet;
    /**
     * 自助餐图片
     */
    @TableField("buffet_image")
    private String buffetImage;
    /**
     * 自助餐价格
     */
    @TableField("buffet_price")
    private Integer buffetPrice;
    /**
     * 限制点菜品数
     */
    @TableField("limit_num")
    private Integer limitNum;
    /**
     * 自助餐介绍
     */
    @TableField("buffet_introduce")
    private String buffetIntroduce;
    /**
     * 菜品所属1通用2中餐3晚餐
     */
    @ItemTag(type="meal_type")
    @TableField("meal_type")
    private String mealType;
    /**
     * 类目类型0堂食1外带
     */
    @ItemTag(type="catalog_type")
    @TableField("catalog_type")
    private String catalogType;
    /**
     * 税金类型1税前2税后
     */
    @TableField("tax_type")
    private String taxType;
    /**
     * 是否有规格
     */
    @TableField(exist = false)
    private String whetherSpec;
    /**
     * 限制时间，分钟
     */
    @TableField("time_limit")
    private Integer timeLimit;
    /**
     * 是否置顶0否1是
     */
    @TableField("whether_top")
    private String whetherTop;
    
    /**
     * 是否首页
     */
    @TableField("whether_first")
    private String whetherFirst;
    /**
     * 大类编号
     */
    @TableField("large_id")
    private String largeId;

    public String getLargeId() {
		return largeId;
	}

	public void setLargeId(String largeId) {
		this.largeId = largeId;
	}

	public String getWhetherFirst() {
		return whetherFirst;
	}

	public void setWhetherFirst(String whetherFirst) {
		this.whetherFirst = whetherFirst;
	}

	public String getWhetherTop() {
		return whetherTop;
	}

	public void setWhetherTop(String whetherTop) {
		this.whetherTop = whetherTop;
	}

	public Integer getTimeLimit() {
		return timeLimit;
	}

	public void setTimeLimit(Integer timeLimit) {
		this.timeLimit = timeLimit;
	}

	public String getWhetherSpec() {
		return whetherSpec;
	}

	public void setWhetherSpec(String whetherSpec) {
		this.whetherSpec = whetherSpec;
	}

	public String getTaxType() {
		return taxType;
	}

	public void setTaxType(String taxType) {
		this.taxType = taxType;
	}

	public String getCatalogType() {
		return catalogType;
	}

	public void setCatalogType(String catalogType) {
		this.catalogType = catalogType;
	}

	public String getCatalogId() {
        return catalogId;
    }

    public void setCatalogId(String catalogId) {
        this.catalogId = catalogId;
    }

    public String getCatalogIndexId() {
        return catalogIndexId;
    }

    public void setCatalogIndexId(String catalogIndexId) {
        this.catalogIndexId = catalogIndexId;
    }

    public String getCatalogName() {
        return catalogName;
    }

    public void setCatalogName(String catalogName) {
        this.catalogName = catalogName;
    }

    public String getPrintNum() {
        return printNum;
    }

    public void setPrintNum(String printNum) {
        this.printNum = printNum;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public Integer getSortNo() {
        return sortNo;
    }

    public void setSortNo(Integer sortNo) {
        this.sortNo = sortNo;
    }

    public String getLanguageType() {
        return languageType;
    }

    public void setLanguageType(String languageType) {
        this.languageType = languageType;
    }

    public String getCatalogRemark() {
        return catalogRemark;
    }

    public void setCatalogRemark(String catalogRemark) {
        this.catalogRemark = catalogRemark;
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

    public String getWhetherBuffet() {
        return whetherBuffet;
    }

    public void setWhetherBuffet(String whetherBuffet) {
        this.whetherBuffet = whetherBuffet;
    }

    public String getBuffetImage() {
        return buffetImage;
    }

    public void setBuffetImage(String buffetImage) {
        this.buffetImage = buffetImage;
    }

    public Integer getBuffetPrice() {
        return buffetPrice;
    }

    public void setBuffetPrice(Integer buffetPrice) {
        this.buffetPrice = buffetPrice;
    }

    public Integer getLimitNum() {
        return limitNum;
    }

    public void setLimitNum(Integer limitNum) {
        this.limitNum = limitNum;
    }

    public String getBuffetIntroduce() {
        return buffetIntroduce;
    }

    public void setBuffetIntroduce(String buffetIntroduce) {
        this.buffetIntroduce = buffetIntroduce;
    }

    public String getMealType() {
        return mealType;
    }

    public void setMealType(String mealType) {
        this.mealType = mealType;
    }

    @Override
    protected Serializable pkVal() {
        return this.catalogId;
    }

    @Override
    public String toString() {
        return "MenuCatalog{" +
        "catalogId=" + catalogId +
        ", catalogIndexId=" + catalogIndexId +
        ", catalogName=" + catalogName +
        ", printNum=" + printNum +
        ", shopId=" + shopId +
        ", sortNo=" + sortNo +
        ", languageType=" + languageType +
        ", catalogRemark=" + catalogRemark +
        ", createTime=" + createTime +
        ", createBy=" + createBy +
        ", updateTime=" + updateTime +
        ", updateBy=" + updateBy +
        ", whetherBuffet=" + whetherBuffet +
        ", buffetImage=" + buffetImage +
        ", buffetPrice=" + buffetPrice +
        ", limitNum=" + limitNum +
        ", buffetIntroduce=" + buffetIntroduce +
        ", mealType=" + mealType +
        ", catalogType=" + catalogType +
        ", taxType=" + taxType +
        ", timeLimit=" + timeLimit +
        ", whetherTop=" + whetherTop +
        "}";
    }

}
