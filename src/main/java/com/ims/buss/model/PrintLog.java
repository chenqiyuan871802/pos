package com.ims.buss.model;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.ims.core.matatype.impl.BaseModel;
import java.io.Serializable;

/**
 * <p>
 * 打印日志表
 * </p>
 *
 * @author 陈骑元
 * @since 2020-01-18
 */
@TableName("t_print_log")
public class PrintLog extends BaseModel<PrintLog> {

    private static final long serialVersionUID = 1L;

    /**
     * 日志编号（主键）
     */
    @TableId("log_id")
    private String logId;
    /**
     * 店铺编号
     */
    @TableField("shop_id")
    private String shopId;
    /**
     * 订单编号
     */
    @TableField("order_id")
    private String orderId;
    /**
     * 打印机
     */
    @TableField("print_num")
    private String printNum;
    /**
     * 桌号
     */
    @TableField("desk_name")
    private String deskName;
    /**
     * 菜单名称
     */
    @TableField("print_menu_name")
    private String printMenuName;
    /**
     * 购买数量
     */
    @TableField("buy_num")
    private Integer buyNum;
    /**
     * 日志状态-1失败1成功
     */
    @TableField("log_status")
    private String logStatus;
    /**
     * 日志记录时间
     */
    @TableField("log_time")
    private Date logTime;
    /**
     * 打印返回日志编号
     */
    @TableField("print_id")
    private String printId;
    /**
     * 日志错误信息
     */
    @TableField("error_message")
    private String errorMessage;
    /**
     * 错误次数
     */
    @TableField("error_count")
    private Integer errorCount;
    /**
     * 更新时间
     */
    @TableField("update_time")
    private Date updateTime;


    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPrintNum() {
        return printNum;
    }

    public void setPrintNum(String printNum) {
        this.printNum = printNum;
    }

    public String getDeskName() {
        return deskName;
    }

    public void setDeskName(String deskName) {
        this.deskName = deskName;
    }

    public String getPrintMenuName() {
        return printMenuName;
    }

    public void setPrintMenuName(String printMenuName) {
        this.printMenuName = printMenuName;
    }

    public Integer getBuyNum() {
        return buyNum;
    }

    public void setBuyNum(Integer buyNum) {
        this.buyNum = buyNum;
    }

    public String getLogStatus() {
        return logStatus;
    }

    public void setLogStatus(String logStatus) {
        this.logStatus = logStatus;
    }

    public Date getLogTime() {
        return logTime;
    }

    public void setLogTime(Date logTime) {
        this.logTime = logTime;
    }

    public String getPrintId() {
        return printId;
    }

    public void setPrintId(String printId) {
        this.printId = printId;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Integer getErrorCount() {
        return errorCount;
    }

    public void setErrorCount(Integer errorCount) {
        this.errorCount = errorCount;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    protected Serializable pkVal() {
        return this.logId;
    }

    @Override
    public String toString() {
        return "PrintLog{" +
        "logId=" + logId +
        ", shopId=" + shopId +
        ", orderId=" + orderId +
        ", printNum=" + printNum +
        ", deskName=" + deskName +
        ", printMenuName=" + printMenuName +
        ", buyNum=" + buyNum +
        ", logStatus=" + logStatus +
        ", logTime=" + logTime +
        ", printId=" + printId +
        ", errorMessage=" + errorMessage +
        ", errorCount=" + errorCount +
        ", updateTime=" + updateTime +
        "}";
    }
}
