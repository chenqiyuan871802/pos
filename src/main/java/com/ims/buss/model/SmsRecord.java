package com.ims.buss.model;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.ims.core.matatype.impl.BaseModel;
import java.io.Serializable;

/**
 * <p>
 * 短信记录表
 * </p>
 *
 * @author 陈骑元
 * @since 2019-12-14
 */
@TableName("t_sms_record")
public class SmsRecord extends BaseModel<SmsRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * 短信记录编号
     */
    @TableId("record_id")
    private String recordId;
    /**
     * 手机号码
     */
    private String mobile;
    /**
     * 短信内容
     */
    private String msg;
    /**
     * 发送时间
     */
    @TableField("send_time")
    private Date sendTime;
    /**
     * 短信平台信息编号
     */
    @TableField("msg_id")
    private String msgId;
    /**
     * 0提交成功 1发送成功2发送失败
     */
    @TableField("msg_status")
    private String msgStatus;
    /**
     * 短信类型1普通短信2验证码短信
     */
    @TableField("msg_type")
    private String msgType;


    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getMsgStatus() {
        return msgStatus;
    }

    public void setMsgStatus(String msgStatus) {
        this.msgStatus = msgStatus;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    @Override
    protected Serializable pkVal() {
        return this.recordId;
    }

    @Override
    public String toString() {
        return "SmsRecord{" +
        "recordId=" + recordId +
        ", mobile=" + mobile +
        ", msg=" + msg +
        ", sendTime=" + sendTime +
        ", msgId=" + msgId +
        ", msgStatus=" + msgStatus +
        ", msgType=" + msgType +
        "}";
    }
}
