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
 * APK版本管理
 * </p>
 *
 * @author 陈骑元
 * @since 2019-10-14
 */
@TableName("t_apk_version")
public class ApkVersion extends BaseModel<ApkVersion> {

    private static final long serialVersionUID = 1L;

    /**
     * 版本编号
     */
    @TableId("version_id")
    private String versionId;
    /**
     * apk名称
     */
    @TableField("apk_name")
    private String apkName;
    /**
     * 文件fid
     */
    private String fid;
    /**
     * 版本名称
     */
    @TableField("version_name")
    private String versionName;
    /**
     * 版本号
     */
    @TableField("version_num")
    private String versionNum;
    /**
     * 升级说明
     */
    @TableField("version_desc")
    private String versionDesc;
    /**
     * 发布状态0强制1不强制
     */
    @ItemTag(type="release_status")
    @TableField("release_status")
    private String releaseStatus;
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
     * apk类型1安卓2POS
     */
    @ItemTag(type="apk_type")
    @TableField("apk_type")
    private String apkType;
    public String getApkType() {
		return apkType;
	}

	public void setApkType(String apkType) {
		this.apkType = apkType;
	}

	public String getVersionId() {
        return versionId;
    }

    public void setVersionId(String versionId) {
        this.versionId = versionId;
    }

    public String getApkName() {
        return apkName;
    }

    public void setApkName(String apkName) {
        this.apkName = apkName;
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getVersionNum() {
        return versionNum;
    }

    public void setVersionNum(String versionNum) {
        this.versionNum = versionNum;
    }

    public String getVersionDesc() {
        return versionDesc;
    }

    public void setVersionDesc(String versionDesc) {
        this.versionDesc = versionDesc;
    }

    public String getReleaseStatus() {
        return releaseStatus;
    }

    public void setReleaseStatus(String releaseStatus) {
        this.releaseStatus = releaseStatus;
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

    @Override
    protected Serializable pkVal() {
        return this.versionId;
    }

    @Override
    public String toString() {
        return "ApkVersion{" +
        "versionId=" + versionId +
        ", apkName=" + apkName +
        ", fid=" + fid +
        ", versionName=" + versionName +
        ", versionNum=" + versionNum +
        ", versionDesc=" + versionDesc +
        ", releaseStatus=" + releaseStatus +
        ", createTime=" + createTime +
        ", createBy=" + createBy +
        ", updateTime=" + updateTime +
        ", updateBy=" + updateBy +
        "}";
    }
}
