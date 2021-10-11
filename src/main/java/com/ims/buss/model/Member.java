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
 * 会员表
 * </p>
 *
 * @author 陈骑元
 * @since 2020-04-23
 */
@TableName("t_member")
public class Member extends BaseModel<Member> {

    private static final long serialVersionUID = 1L;

    /**
     * 会员ID
     */
    @TableId("member_id")
    private String memberId;
    /**
     * 会员编号
     */
    @TableField("member_num")
    private String memberNum;
    /**
     * 手机号码
     */
    private String mobile;
    /**
     * 姓
     */
    @TableField("first_name")
    private String firstName;
    /**
     * 名字
     */
    @TableField("last_name")
    private String lastName;
    /**
     * 全名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    private String email;
    /**
     * 出生日期
     */
    @JsonFormat(pattern="yyyy-MM-dd")
    @TableField("birth_date")
    private Date birthDate;
    /**
     * 性别
     */
    @ItemTag(type="FM")
    private String sex;
    /**
     * 用户状态 1:正常2:停用 3:锁定
     */
    private String status;
    /**
     * 注册时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("enroll_time")
    private Date enrollTime;
    /**
     * 更细时间
     */
    @TableField("update_time")
    private Date updateTime;
   

    /**
     * 是否绑定信用卡0否1是
     */
    @TableField("whether_card")
    private String whetherCard;


    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getMemberNum() {
        return memberNum;
    }

    public void setMemberNum(String memberNum) {
        this.memberNum = memberNum;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getEnrollTime() {
        return enrollTime;
    }

    public void setEnrollTime(Date enrollTime) {
        this.enrollTime = enrollTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getWhetherCard() {
        return whetherCard;
    }

    public void setWhetherCard(String whetherCard) {
        this.whetherCard = whetherCard;
    }

    @Override
    protected Serializable pkVal() {
        return this.memberId;
    }

    @Override
    public String toString() {
        return "Member{" +
        "memberId=" + memberId +
        ", memberNum=" + memberNum +
        ", mobile=" + mobile +
        ", firstName=" + firstName +
        ", lastName=" + lastName +
        ", username=" + username +
        ", password=" + password +
        ", email=" + email +
        ", birthDate=" + birthDate +
        ", sex=" + sex +
        ", status=" + status +
        ", enrollTime=" + enrollTime +
        ", updateTime=" + updateTime +
        ", whetherCard=" + whetherCard +
        "}";
    }
}
