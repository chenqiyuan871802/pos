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
 * 打印机表
 * </p>
 *
 * @author 陈骑元
 * @since 2019-09-08
 */
@TableName("t_printer")
public class Printer extends BaseModel<Printer> {

    private static final long serialVersionUID = 1L;

    /**
     * 打印机ID
     */
    @TableId("print_id")
    private String printId;
    /**
     * 打印机号
     */
    @TableField("print_num")
    private String printNum;
    /**
     * 打印机备注
     */
    @TableField("print_remark")
    private String printRemark;
    /**
     * 店铺编号
     */
    @TableField("shop_id")
    private String shopId;
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
     * 是否授权
     */
    @ItemTag(type="whether_type_ch")
    @TableField("whether_grant")
    private String whetherGrant;
    /**
     * 打印机类型
     */
    @ItemTag(type="print_type")
    @TableField("print_type")
    private String printType;
    /**
     * 打印机状态，0离线 1在线 2缺纸
     */
    @ItemTag(type="print_status")
    @TableField("print_status")
    private String printStatus;
    /**
     * 打印机型号
     */
    @TableField("print_model")
    private String printModel;
    /**
     * 打印机秘钥
     */
    @TableField("print_secret_key")
    private String printSecretKey;
    /**
     * 是否注册0否1是
     */
    @ItemTag(type="whether_type_ch")
    @TableField("whether_enroll")
    private String whetherEnroll;
    /**
     * 是否设置默认打印服务0否1是
     */
    
    @TableField("whether_server")
    private String whetherServer;
    /**
     * 网关类型1国内2日本
     */
    @ItemTag(type="gateway_type")
    @TableField("gateway_type")
    private String gatewayType;
    public String getGatewayType() {
		return gatewayType;
	}
    /**
     * 是否POS打印0否1是
     */
    @TableField("whether_pos")
    private String whetherPos;
    
    @TableField(exist = false)
    private String whetherSelect;

	public String getWhetherSelect() {
		return whetherSelect;
	}

	public void setWhetherSelect(String whetherSelect) {
		this.whetherSelect = whetherSelect;
	}

	public String getWhetherPos() {
		return whetherPos;
	}

	public void setWhetherPos(String whetherPos) {
		this.whetherPos = whetherPos;
	}

	public void setGatewayType(String gatewayType) {
		this.gatewayType = gatewayType;
	}

	public String getWhetherServer() {
		return whetherServer;
	}

	public void setWhetherServer(String whetherServer) {
		this.whetherServer = whetherServer;
	}

	/**
     * 店铺
     */
    @TableField(exist = false)
    private String shopName;


    public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getPrintId() {
        return printId;
    }

    public void setPrintId(String printId) {
        this.printId = printId;
    }

    public String getPrintNum() {
        return printNum;
    }

    public void setPrintNum(String printNum) {
        this.printNum = printNum;
    }

    public String getPrintRemark() {
        return printRemark;
    }

    public void setPrintRemark(String printRemark) {
        this.printRemark = printRemark;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
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

    public String getWhetherGrant() {
        return whetherGrant;
    }

    public void setWhetherGrant(String whetherGrant) {
        this.whetherGrant = whetherGrant;
    }

    public String getPrintType() {
        return printType;
    }

    public void setPrintType(String printType) {
        this.printType = printType;
    }

    public String getPrintStatus() {
        return printStatus;
    }

    public void setPrintStatus(String printStatus) {
        this.printStatus = printStatus;
    }

    public String getPrintModel() {
        return printModel;
    }

    public void setPrintModel(String printModel) {
        this.printModel = printModel;
    }

    public String getPrintSecretKey() {
        return printSecretKey;
    }

    public void setPrintSecretKey(String printSecretKey) {
        this.printSecretKey = printSecretKey;
    }

    public String getWhetherEnroll() {
        return whetherEnroll;
    }

    public void setWhetherEnroll(String whetherEnroll) {
        this.whetherEnroll = whetherEnroll;
    }

    @Override
    protected Serializable pkVal() {
        return this.printId;
    }


    @Override
    public String toString() {
        return "Printer{" +
        "printId=" + printId +
        ", printNum=" + printNum +
        ", printRemark=" + printRemark +
        ", shopId=" + shopId +
        ", createTime=" + createTime +
        ", createBy=" + createBy +
        ", updateTime=" + updateTime +
        ", updateBy=" + updateBy +
        ", whetherGrant=" + whetherGrant +
        ", printType=" + printType +
        ", printStatus=" + printStatus +
        ", printModel=" + printModel +
        ", printSecretKey=" + printSecretKey +
        ", whetherEnroll=" + whetherEnroll +
        ", whetherServer=" + whetherServer +
        ", gatewayType=" + gatewayType +
        "}";
    }
}
