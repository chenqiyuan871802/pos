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
 * 桌位
 * </p>
 *
 * @author 陈骑元
 * @since 2019-11-02
 */
@TableName("t_desk")
public class Desk extends BaseModel<Desk> {

    private static final long serialVersionUID = 1L;

    @TableId("desk_id")
    private String deskId;
    /**
     * 桌号
     */
    @TableField("desk_name")
    private String deskName;
    /**
     * 备注
     */
    @TableField("desk_remark")
    private String deskRemark;
    /**
     * 店铺编号
     */
    @TableField("shop_id")
    private String shopId;
    /**
     * 区域编号
     */
    @TableField("area_id")
    private String areaId;
    /**
     * 桌子状态0空闲1占用
     */
    @ItemTag(type="desk_status")
    @TableField("desk_status")
    private String deskStatus;
    /**
     * 吃饭人数
     */
    @TableField("eat_num")
    private Integer eatNum;
    /**
     * 排序号
     */
    @TableField("sort_no")
    private Integer sortNo;
    /**
     * 创建二维码
     */
    @TableField("order_no")
    private String orderNo;
    /**
     * 开餐时间
     */
    @TableField("order_time")
    private Date orderTime;
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
     * 是否显示价格0否1是
     */
    
    @TableField("whether_show_price")
    private String whetherShowPrice;
    /**
     * 展示类型0全部1普通菜品2自助餐
     */
    @ItemTag(type="display_type")
    @TableField("display_type")
    private String displayType;
    /**
     * 是否自助餐桌位
     */
    @TableField(exist = false)
    private String whetherBuffet;
    /**
     * 自助餐时间时长
     */
    @TableField(exist = false)
    private Integer timeLimit;
    /**
     * 自助餐下单时间
     */
    @TableField(exist = false)
    private String buffetTime;
    public String getBuffetTime() {
		return buffetTime;
	}

	public void setBuffetTime(String buffetTime) {
		this.buffetTime = buffetTime;
	}

	public Integer getTimeLimit() {
		return timeLimit;
	}

	public void setTimeLimit(Integer timeLimit) {
		this.timeLimit = timeLimit;
	}

	public String getWhetherBuffet() {
		return whetherBuffet;
	}

	public void setWhetherBuffet(String whetherBuffet) {
		this.whetherBuffet = whetherBuffet;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	@TableField(exist = false)
    private String areaName;

    public String getDeskId() {
        return deskId;
    }

    public void setDeskId(String deskId) {
        this.deskId = deskId;
    }

    public String getDeskName() {
        return deskName;
    }

    public void setDeskName(String deskName) {
        this.deskName = deskName;
    }

    public String getDeskRemark() {
        return deskRemark;
    }

    public void setDeskRemark(String deskRemark) {
        this.deskRemark = deskRemark;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getDeskStatus() {
        return deskStatus;
    }

    public void setDeskStatus(String deskStatus) {
        this.deskStatus = deskStatus;
    }

    public Integer getEatNum() {
        return eatNum;
    }

    public void setEatNum(Integer eatNum) {
        this.eatNum = eatNum;
    }

    public Integer getSortNo() {
        return sortNo;
    }

    public void setSortNo(Integer sortNo) {
        this.sortNo = sortNo;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
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

    public String getWhetherShowPrice() {
        return whetherShowPrice;
    }

    public void setWhetherShowPrice(String whetherShowPrice) {
        this.whetherShowPrice = whetherShowPrice;
    }

    public String getDisplayType() {
        return displayType;
    }

    public void setDisplayType(String displayType) {
        this.displayType = displayType;
    }

    @Override
    protected Serializable pkVal() {
        return this.deskId;
    }

    @Override
    public String toString() {
        return "Desk{" +
        "deskId=" + deskId +
        ", deskName=" + deskName +
        ", deskRemark=" + deskRemark +
        ", shopId=" + shopId +
        ", areaId=" + areaId +
        ", deskStatus=" + deskStatus +
        ", eatNum=" + eatNum +
        ", sortNo=" + sortNo +
        ", orderNo=" + orderNo +
        ", orderTime=" + orderTime +
        ", createTime=" + createTime +
        ", createBy=" + createBy +
        ", updateTime=" + updateTime +
        ", updateBy=" + updateBy +
        ", whetherShowPrice=" + whetherShowPrice +
        ", displayType=" + displayType +
        "}";
    }
}
