package com.ims.buss.model;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.ims.core.annotation.ItemTag;
import com.ims.core.constant.WebplusCons;
import com.ims.core.matatype.impl.BaseModel;
import java.io.Serializable;

/**
 * <p>
 * 会员地址
 * </p>
 *
 * @author 陈骑元
 * @since 2020-04-23
 */
@TableName("t_member_address")
public class MemberAddress extends BaseModel<MemberAddress> {

    private static final long serialVersionUID = 1L;

    /**
     * 地址编号
     */
    @TableId("address_id")
    private String addressId;
    /**
     * 会员编号
     */
    @TableField("member_id")
    private String memberId;
    /**
     * 省编码
     */
    @ItemTag(type=WebplusCons.POSITION_DICT)
    private String province;
    /**
     * 市编码
     */
    @ItemTag(type=WebplusCons.POSITION_DICT)
    private String city;
    /**
     * 镇编码
     */
    @ItemTag(type=WebplusCons.POSITION_DICT)
    private String town;
    /**
     * 详细地址
     */
    @TableField("detail_address")
    private String detailAddress;
    /**
     * 是否删除
     */
    @TableField("whether_remove")
    private String whetherRemove;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 会员名字
     */
    @TableField(exist = false)
    private String username;
    /**
     * 手机号码
     */
    @TableField(exist = false)
    private String mobile;
    
    /**
     * 全地址
     */
    @TableField(exist = false)
    private String fullAddress;
    
    public String getFullAddress() {
		return fullAddress;
	}

	public void setFullAddress(String fullAddress) {
		this.fullAddress = fullAddress;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getDetailAddress() {
        return detailAddress;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
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

    @Override
    protected Serializable pkVal() {
        return this.addressId;
    }

    @Override
    public String toString() {
        return "MemberAddress{" +
        "addressId=" + addressId +
        ", memberId=" + memberId +
        ", province=" + province +
        ", city=" + city +
        ", town=" + town +
        ", detailAddress=" + detailAddress +
        ", whetherRemove=" + whetherRemove +
        ", createTime=" + createTime +
        "}";
    }
}
