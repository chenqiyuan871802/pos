package com.ims.buss.model;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ims.core.annotation.ItemTag;
import com.ims.core.matatype.impl.BaseModel;
import java.io.Serializable;

/**
 * <p>
 * 优惠券
 * </p>
 *
 * @author 陈骑元
 * @since 2020-05-01
 */
@TableName("t_shop_coupon")
public class ShopCoupon extends BaseModel<ShopCoupon> {

    private static final long serialVersionUID = 1L;

    /**
     * 优惠券编号
     */
    @TableId("coupon_id")
    private String couponId;
    /**
     * 店铺编号
     */
    @TableField("shop_id")
    private String shopId;
    /**
     * 优惠券标题
     */
    @TableField("coupon_title")
    private String couponTitle;
    /**
     * 优惠券宣传语
     */
    @TableField("coupon_slogan")
    private String couponSlogan;
    /**
     * 有效起始日期
     */
    @JsonFormat(pattern="yyyy-MM-dd")
    @TableField("valid_begin_date")
    private Date validBeginDate;
    /**
     * 有效结束日期
     */
    @JsonFormat(pattern="yyyy-MM-dd")
    @TableField("valid_end_date")
    private Date validEndDate;
    /**
     * 优惠卷介绍
     */
    @TableField("coupon_introduce")
    private String couponIntroduce;
    /**
     * 优惠券状态0待发布1上架2下架
     */
    @ItemTag(type="coupon_status")
    @TableField("coupon_status")
    private String couponStatus;
    /**
     * 优惠券图片
     */
    @TableField("coupon_image")
    private String couponImage;
    /**
     * 是否删除0否1是
     */
    @TableField("whether_remove")
    private String whetherRemove;
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
     * 店铺名称
     */
    @TableField(exist = false)
    private String shopName;
    
    @TableField(exist = false)
    private String shopTelephone;
    
    @TableField(exist = false)
    private String shopAddress;
    @TableField(exist = false)
    private String whetherCollect;
    public String getWhetherCollect() {
		return whetherCollect;
	}

	public void setWhetherCollect(String whetherCollect) {
		this.whetherCollect = whetherCollect;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getShopTelephone() {
		return shopTelephone;
	}

	public void setShopTelephone(String shopTelephone) {
		this.shopTelephone = shopTelephone;
	}

	public String getShopAddress() {
		return shopAddress;
	}

	public void setShopAddress(String shopAddress) {
		this.shopAddress = shopAddress;
	}

	public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getCouponTitle() {
        return couponTitle;
    }

    public void setCouponTitle(String couponTitle) {
        this.couponTitle = couponTitle;
    }

    public String getCouponSlogan() {
        return couponSlogan;
    }

    public void setCouponSlogan(String couponSlogan) {
        this.couponSlogan = couponSlogan;
    }

    public Date getValidBeginDate() {
        return validBeginDate;
    }

    public void setValidBeginDate(Date validBeginDate) {
        this.validBeginDate = validBeginDate;
    }

    public Date getValidEndDate() {
        return validEndDate;
    }

    public void setValidEndDate(Date validEndDate) {
        this.validEndDate = validEndDate;
    }

    public String getCouponIntroduce() {
        return couponIntroduce;
    }

    public void setCouponIntroduce(String couponIntroduce) {
        this.couponIntroduce = couponIntroduce;
    }

    public String getCouponStatus() {
        return couponStatus;
    }

    public void setCouponStatus(String couponStatus) {
        this.couponStatus = couponStatus;
    }

    public String getCouponImage() {
        return couponImage;
    }

    public void setCouponImage(String couponImage) {
        this.couponImage = couponImage;
    }

    public String getWhetherRemove() {
        return whetherRemove;
    }

    public void setWhetherRemove(String whetherRemove) {
        this.whetherRemove = whetherRemove;
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
        return this.couponId;
    }

    @Override
    public String toString() {
        return "ShopCoupon{" +
        "couponId=" + couponId +
        ", shopId=" + shopId +
        ", couponTitle=" + couponTitle +
        ", couponSlogan=" + couponSlogan +
        ", validBeginDate=" + validBeginDate +
        ", validEndDate=" + validEndDate +
        ", couponIntroduce=" + couponIntroduce +
        ", couponStatus=" + couponStatus +
        ", couponImage=" + couponImage +
        ", whetherRemove=" + whetherRemove +
        ", createTime=" + createTime +
        ", createBy=" + createBy +
        ", updateTime=" + updateTime +
        ", updateBy=" + updateBy +
        "}";
    }
}
