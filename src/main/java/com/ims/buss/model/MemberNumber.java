package com.ims.buss.model;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.ims.core.matatype.impl.BaseModel;
import java.io.Serializable;

/**
 * <p>
 * 会员号码表
 * </p>
 *
 * @author 陈骑元
 * @since 2019-12-15
 */
@TableName("t_member_number")
public class MemberNumber extends BaseModel<MemberNumber> {

    private static final long serialVersionUID = 1L;

    /**
     * 会员编号
     */
    @TableId("member_no")
    private String memberNo;
    /**
     * 状态0未占用1占用
     */
    private String status;
    /**
     * 使用时间
     */
    @TableField("use_time")
    private Date useTime;


    public String getMemberNo() {
        return memberNo;
    }

    public void setMemberNo(String memberNo) {
        this.memberNo = memberNo;
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
        return this.memberNo;
    }

    @Override
    public String toString() {
        return "MemberNumber{" +
        "memberNo=" + memberNo +
        ", status=" + status +
        ", useTime=" + useTime +
        "}";
    }
}
