package com.ims.buss.model;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.ims.core.matatype.impl.BaseModel;
import java.io.Serializable;

/**
 * <p>
 * 会员优惠券
 * </p>
 *
 * @author 陈骑元
 * @since 2020-05-01
 */
@TableName("t_member_coupon")
public class MemberCoupon extends BaseModel<MemberCoupon> {

    private static final long serialVersionUID = 1L;

    /**
     * 会员优惠卷编号
     */
    @TableId("member_coupon_id")
    private String memberCouponId;
    /**
     * 优惠券编号
     */
    @TableField("coupon_id")
    private String couponId;
    /**
     * 会员编号
     */
    @TableField("member_id")
    private String memberId;
    /**
     * 收藏时间
     */
    @TableField("collect_time")
    private Date collectTime;
    /**
     * 状态0待使用1已使用2已过期
     */
    private String status;
    /**
     * 使用时间
     */
    @TableField("use_time")
    private Date useTime;


    public String getMemberCouponId() {
        return memberCouponId;
    }

    public void setMemberCouponId(String memberCouponId) {
        this.memberCouponId = memberCouponId;
    }

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public Date getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(Date collectTime) {
        this.collectTime = collectTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getUseTime() {
        return useTime;
    }

    public void setUseTime(Date useTime) {
        this.useTime = useTime;
    }

    @Override
    protected Serializable pkVal() {
        return this.memberCouponId;
    }

    @Override
    public String toString() {
        return "MemberCoupon{" +
        "memberCouponId=" + memberCouponId +
        ", couponId=" + couponId +
        ", memberId=" + memberId +
        ", collectTime=" + collectTime +
        ", status=" + status +
        ", useTime=" + useTime +
        "}";
    }
}
