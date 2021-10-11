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
 * 菜单目录
 * </p>
 *
 * @author 陈骑元
 * @since 2019-09-06
 */
@TableName("t_menu_dict")
public class MenuDict extends BaseModel<MenuDict> {

    private static final long serialVersionUID = 1L;

    @TableId("menu_id")
    private String menuId;
    /**
     * 菜单索引编号
     */
    @TableField("menu_index_id")
    private String menuIndexId;
    /**
     * 菜品名
     */
    @TableField("menu_name")
    private String menuName;
    /**
     * 菜单价格
     */
    @TableField("menu_price")
    private Integer menuPrice;
    /**
     * 简称
     */
    @TableField("short_name")
    private String shortName;
    /**
     * 类目索引编号
     */
    @TableField("catalog_index_id")
    private String catalogIndexId;
    /**
     * 图片名称
     */
    @TableField("menu_image")
    private String menuImage;
    /**
     * 介绍
     */
    @TableField("menu_introduce")
    private String menuIntroduce;
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
     * 菜品类型0通用1常规2自助餐
     */
    @ItemTag(type="food_type")
    @TableField("menu_type")
    private String menuType;
    /**
     * 菜品所属1通用2中餐3晚餐
     */
    @ItemTag(type="meal_type")
    @TableField("meal_type")
    private String mealType;
    /**
     * 排序号
     */
    @TableField("sort_no")
    private Integer sortNo;
    /**
     * 是否存在规格0否1是
     */
    @TableField("whether_spec")
    private String whetherSpec;
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
     * 类目名称
     */
    @TableField(exist = false)
    private String catalogName;
    
    /**
     * 菜单序号
     */
    @TableField("menu_num")
    private String menuNum;
    
    /**
     * 菜单状态1在售，2售完
     */
    @TableField("menu_status")
    private String menuStatus;
    
    /**
     * 类目名称
     */
    @TableField(exist = false)
    private String whetherBuffet;
    
    /**
     * 税前，税后
     */
    @TableField(exist = false)
    private String taxType;
    /**
     * 成本价格
     */
    @TableField("cost_price")
    private Integer costPrice;
  


    public Integer getCostPrice() {
		return costPrice;
	}

	public void setCostPrice(Integer costPrice) {
		this.costPrice = costPrice;
	}

	public String getTaxType() {
		return taxType;
	}

	public void setTaxType(String taxType) {
		this.taxType = taxType;
	}

	public String getWhetherBuffet() {
		return whetherBuffet;
	}

	public void setWhetherBuffet(String whetherBuffet) {
		this.whetherBuffet = whetherBuffet;
	}

	public String getMenuStatus() {
		return menuStatus;
	}

	public void setMenuStatus(String menuStatus) {
		this.menuStatus = menuStatus;
	}

	public String getMenuNum() {
		return menuNum;
	}

	public void setMenuNum(String menuNum) {
		this.menuNum = menuNum;
	}

	public String getCatalogName() {
		return catalogName;
	}

	public void setCatalogName(String catalogName) {
		this.catalogName = catalogName;
	}
    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getMenuIndexId() {
        return menuIndexId;
    }

    public void setMenuIndexId(String menuIndexId) {
        this.menuIndexId = menuIndexId;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public Integer getMenuPrice() {
        return menuPrice;
    }

    public void setMenuPrice(Integer menuPrice) {
        this.menuPrice = menuPrice;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getCatalogIndexId() {
        return catalogIndexId;
    }

    public void setCatalogIndexId(String catalogIndexId) {
        this.catalogIndexId = catalogIndexId;
    }

    public String getMenuImage() {
        return menuImage;
    }

    public void setMenuImage(String menuImage) {
        this.menuImage = menuImage;
    }

    public String getMenuIntroduce() {
        return menuIntroduce;
    }

    public void setMenuIntroduce(String menuIntroduce) {
        this.menuIntroduce = menuIntroduce;
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

    public String getMenuType() {
        return menuType;
    }

    public void setMenuType(String menuType) {
        this.menuType = menuType;
    }

    public String getMealType() {
        return mealType;
    }

    public void setMealType(String mealType) {
        this.mealType = mealType;
    }

    public Integer getSortNo() {
        return sortNo;
    }

    public void setSortNo(Integer sortNo) {
        this.sortNo = sortNo;
    }

    public String getWhetherSpec() {
        return whetherSpec;
    }

    public void setWhetherSpec(String whetherSpec) {
        this.whetherSpec = whetherSpec;
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
        return this.menuId;
    }

    @Override
    public String toString() {
        return "MenuDict{" +
        "menuId=" + menuId +
        ", menuIndexId=" + menuIndexId +
        ", menuName=" + menuName +
        ", menuPrice=" + menuPrice +
        ", shortName=" + shortName +
        ", catalogIndexId=" + catalogIndexId +
        ", menuImage=" + menuImage +
        ", menuIntroduce=" + menuIntroduce +
        ", shopId=" + shopId +
        ", languageType=" + languageType +
        ", menuType=" + menuType +
        ", mealType=" + mealType +
        ", menuNum=" + menuNum +
        ", sortNo=" + sortNo +
        ", whetherSpec=" + whetherSpec +
        ", createTime=" + createTime +
        ", createBy=" + createBy +
        ", updateTime=" + updateTime +
        ", updateBy=" + updateBy +
        ", menuStatus=" + menuStatus +
        "}";
    }
}
