package com.ims.buss.model;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.ims.core.matatype.impl.BaseModel;
import java.io.Serializable;

/**
 * <p>
 * 店铺表
 * </p>
 *
 * @author 陈骑元
 * @since 2020-01-05
 */
@TableName("t_shop")
public class Shop extends BaseModel<Shop> {

    private static final long serialVersionUID = 1L;

    /**
     * 店铺编号
     */
    @TableId("shop_id")
    private String shopId;
    /**
     * 店铺名称
     */
    @TableField("shop_name")
    private String shopName;
    /**
     * 店铺首图
     */
    @TableField("shop_first_image")
    private String shopFirstImage;
    /**
     * 店铺地址
     */
    @TableField("shop_address")
    private String shopAddress;
    /**
     * 店铺电话
     */
    @TableField("shop_telephone")
    private String shopTelephone;
    /**
     * 桌位费
     */
    @TableField("table_amount")
    private Integer tableAmount;
    /**
     * 税金
     */
    private Integer taxes;
    /**
     * 二维码地址
     */
    @TableField("shop_qrcode")
    private String shopQrcode;
    /**
     * 语言
     */
    private String language;
    /**
     * 时间分割
     */
    @TableField("time_limit")
    private String timeLimit;
    /**
     * 是否删除 0否1是
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
     * 店铺宣传语
     */
    @TableField("shop_slogan")
    private String shopSlogan;
    /**
     * 店铺介绍（日语）
     */
    @TableField("shop_introduce")
    private String shopIntroduce;
    /**
     * 店铺介绍(简体)
     */
    @TableField("shop_introduce_ch")
    private String shopIntroduceCh;
    /**
     * 店铺介绍（繁体）
     */
    @TableField("shop_introduce_tc")
    private String shopIntroduceTc;
    /**
     * 店铺介绍(韩文)
     */
    @TableField("shop_introduce_kro")
    private String shopIntroduceKro;
    /**
     * 店铺介绍(英文)
     */
    @TableField("shop_introduce_eng")
    private String shopIntroduceEng;
    /**
     * 报表统计时间段00:00
     */
    @TableField("time_report")
    private String timeReport;
    /**
     * 午餐起始时间
     */
    @TableField("time_limit_start")
    private String timeLimitStart;
    /**
     * 下单时间间隔
     */
    @TableField("order_limit")
    private Integer orderLimit;
    /**
     * 服务推送类型0所有1app2打印机
     */
    @TableField("push_type")
    private String pushType;
    /**
     * 积分店铺编码
     */
    @TableField("mert_code")
    private String mertCode;
    /**
     * 分店编码
     */
    @TableField("outlet_code")
    private String outletCode;
    /**
     * 是否有优惠券
     */
    @TableField(exist = false)
    private String whetherCoupon;
    
    /**
     * 订餐类型0所有1配送2外带
     */
    @TableField("order_food_type")
    private String orderFoodType;
    /**
     * 配送费
     */
    @TableField("deliver_amount")
    private Integer deliverAmount;
    /**
     * 配送时间
     */
    @TableField("deliver_time")
    private Integer deliverTime;
    

	/**
     * 最小金额
     */
    @TableField("min_amount")
    private Integer minAmount;
    /**
     * 午休备注
     */
    @TableField("rest_note")
    private String restNote;
    
    /**
     * 店铺状态1营业2停业
     */
    @TableField("shop_status")
    private String shopStatus;
    /**
     * 配送说明
     */
    @TableField("deliver_note")
    private String deliverNote;
    /**
     * 支付配置编号
     */
    @TableField("pay_config_id")
    private String payConfigId;
    /**
     * 支付账号
     */
    @TableField("pay_account")
    private String payAccount;
    /**
     * 支付密码
     */
    @TableField("pay_password")
    private String payPassword;

    public String getPayConfigId() {
		return payConfigId;
	}

	public void setPayConfigId(String payConfigId) {
		this.payConfigId = payConfigId;
	}

	public String getPayAccount() {
		return payAccount;
	}

	public void setPayAccount(String payAccount) {
		this.payAccount = payAccount;
	}

	public String getPayPassword() {
		return payPassword;
	}

	public void setPayPassword(String payPassword) {
		this.payPassword = payPassword;
	}

	public String getDeliverNote() {
		return deliverNote;
	}

	public void setDeliverNote(String deliverNote) {
		this.deliverNote = deliverNote;
	}

	public String getShopStatus() {
		return shopStatus;
	}

	public void setShopStatus(String shopStatus) {
		this.shopStatus = shopStatus;
	}

	public Integer getDeliverTime() {
		return deliverTime;
	}

	public void setDeliverTime(Integer deliverTime) {
		this.deliverTime = deliverTime;
	}
    public Integer getMinAmount() {
		return minAmount;
	}

	public void setMinAmount(Integer minAmount) {
		this.minAmount = minAmount;
	}

	public String getRestNote() {
		return restNote;
	}

	public void setRestNote(String restNote) {
		this.restNote = restNote;
	}

	public Integer getDeliverAmount() {
		return deliverAmount;
	}

	public void setDeliverAmount(Integer deliverAmount) {
		this.deliverAmount = deliverAmount;
	}

	public String getOrderFoodType() {
		return orderFoodType;
	}

	public void setOrderFoodType(String orderFoodType) {
		this.orderFoodType = orderFoodType;
	}

	public String getWhetherCoupon() {
		return whetherCoupon;
	}

	public void setWhetherCoupon(String whetherCoupon) {
		this.whetherCoupon = whetherCoupon;
	}

	public String getMertCode() {
		return mertCode;
	}

	public void setMertCode(String mertCode) {
		this.mertCode = mertCode;
	}

	public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopFirstImage() {
        return shopFirstImage;
    }

    public void setShopFirstImage(String shopFirstImage) {
        this.shopFirstImage = shopFirstImage;
    }

    public String getShopAddress() {
        return shopAddress;
    }

    public void setShopAddress(String shopAddress) {
        this.shopAddress = shopAddress;
    }

    public String getShopTelephone() {
        return shopTelephone;
    }

    public void setShopTelephone(String shopTelephone) {
        this.shopTelephone = shopTelephone;
    }

    public Integer getTableAmount() {
        return tableAmount;
    }

    public void setTableAmount(Integer tableAmount) {
        this.tableAmount = tableAmount;
    }

    public Integer getTaxes() {
        return taxes;
    }

    public void setTaxes(Integer taxes) {
        this.taxes = taxes;
    }

    public String getShopQrcode() {
        return shopQrcode;
    }

    public void setShopQrcode(String shopQrcode) {
        this.shopQrcode = shopQrcode;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(String timeLimit) {
        this.timeLimit = timeLimit;
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

    public String getShopSlogan() {
        return shopSlogan;
    }

    public void setShopSlogan(String shopSlogan) {
        this.shopSlogan = shopSlogan;
    }

    public String getShopIntroduce() {
        return shopIntroduce;
    }

    public void setShopIntroduce(String shopIntroduce) {
        this.shopIntroduce = shopIntroduce;
    }

    public String getShopIntroduceCh() {
        return shopIntroduceCh;
    }

    public void setShopIntroduceCh(String shopIntroduceCh) {
        this.shopIntroduceCh = shopIntroduceCh;
    }

    public String getShopIntroduceTc() {
        return shopIntroduceTc;
    }

    public void setShopIntroduceTc(String shopIntroduceTc) {
        this.shopIntroduceTc = shopIntroduceTc;
    }

    public String getShopIntroduceKro() {
        return shopIntroduceKro;
    }

    public void setShopIntroduceKro(String shopIntroduceKro) {
        this.shopIntroduceKro = shopIntroduceKro;
    }

    public String getShopIntroduceEng() {
        return shopIntroduceEng;
    }

    public void setShopIntroduceEng(String shopIntroduceEng) {
        this.shopIntroduceEng = shopIntroduceEng;
    }

    public String getTimeReport() {
        return timeReport;
    }

    public void setTimeReport(String timeReport) {
        this.timeReport = timeReport;
    }

    public String getTimeLimitStart() {
        return timeLimitStart;
    }

    public void setTimeLimitStart(String timeLimitStart) {
        this.timeLimitStart = timeLimitStart;
    }

    public Integer getOrderLimit() {
        return orderLimit;
    }

    public void setOrderLimit(Integer orderLimit) {
        this.orderLimit = orderLimit;
    }

    public String getOutletCode() {
        return outletCode;
    }

    public void setOutletCode(String outletCode) {
        this.outletCode = outletCode;
    }

    @Override
    protected Serializable pkVal() {
        return this.shopId;
    }

    @Override
    public String toString() {
        return "Shop{" +
        "shopId=" + shopId +
        ", shopName=" + shopName +
        ", shopFirstImage=" + shopFirstImage +
        ", shopAddress=" + shopAddress +
        ", shopTelephone=" + shopTelephone +
        ", tableAmount=" + tableAmount +
        ", taxes=" + taxes +
        ", shopQrcode=" + shopQrcode +
        ", language=" + language +
        ", timeLimit=" + timeLimit +
        ", whetherRemove=" + whetherRemove +
        ", createTime=" + createTime +
        ", createBy=" + createBy +
        ", updateTime=" + updateTime +
        ", updateBy=" + updateBy +
        ", shopSlogan=" + shopSlogan +
        ", shopIntroduce=" + shopIntroduce +
        ", shopIntroduceCh=" + shopIntroduceCh +
        ", shopIntroduceTc=" + shopIntroduceTc +
        ", shopIntroduceKro=" + shopIntroduceKro +
        ", shopIntroduceEng=" + shopIntroduceEng +
        ", timeReport=" + timeReport +
        ", timeLimitStart=" + timeLimitStart +
        ", orderLimit=" + orderLimit +
        ", pushType=" + pushType +
        ", mertCode=" + mertCode +
        ", outletCode=" + outletCode +
        "}";
    }


	public String getPushType() {
		return pushType;
	}

	public void setPushType(String pushType) {
		this.pushType = pushType;
	}
}
