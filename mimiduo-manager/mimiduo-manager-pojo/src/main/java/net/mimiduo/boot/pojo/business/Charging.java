package net.mimiduo.boot.pojo.business;


import net.mimiduo.boot.pojo.common.IdEntity;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = Charging.TABLE_NAME)
public class Charging extends IdEntity implements Serializable {
    private static final long serialVersionUID = -1240123641598734544L;

    protected static final String TABLE_NAME = "t_charging";

    private long visitId;

    private long channelId;

    private long companyId;

    private String mobile;

    private String province;

    private String city;

    private int fee;

    private int feeType;

    private int status;

    @CreatedDate
    private Date createDate;

    @LastModifiedDate
    private Date updateDate;

    public Charging() {
    }

    public long getVisitId() {
        return visitId;
    }

    public void setVisitId(long visitId) {
        this.visitId = visitId;
    }

    public long getChannelId() {
        return channelId;
    }

    public void setChannelId(long channelId) {
        this.channelId = channelId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public int getFee() {
        return fee;
    }

    public void setFee(int fee) {
        this.fee = fee;
    }

    public long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(long companyId) {
        this.companyId = companyId;
    }

    public int getFeeType() {
        return feeType;
    }

    public void setFeeType(int feeType) {
        this.feeType = feeType;
    }
}
