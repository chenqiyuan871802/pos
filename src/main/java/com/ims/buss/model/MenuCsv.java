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
 * 菜单的csv文件
 * </p>
 *
 * @author 陈骑元
 * @since 2019-08-06
 */
@TableName("t_menu_csv")
public class MenuCsv extends BaseModel<MenuCsv> {

    private static final long serialVersionUID = 1L;

    /**
     * 菜单编号
     */
    @TableId("file_id")
    private String fileId;
    /**
     * 文件名称
     */
    @TableField("file_name")
    private String fileName;
    /**
     * 文件fid
     */
    private String fid;
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
     * 状态0失败1成功
     */
    @ItemTag(type="csv_status")
    private String status;


    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    protected Serializable pkVal() {
        return this.fileId;
    }

    @Override
    public String toString() {
        return "MenuCsv{" +
        "fileId=" + fileId +
        ", fileName=" + fileName +
        ", fid=" + fid +
        ", shopId=" + shopId +
        ", createTime=" + createTime +
        ", status=" + status +
        "}";
    }
}
