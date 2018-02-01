package net.mimiduo.boot.pojo.business;

import com.fasterxml.jackson.annotation.JsonFormat;
import net.mimiduo.boot.pojo.common.IdEntity;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = Sell.TABLE_NAME)
public class Sell extends IdEntity implements Serializable {

    private static final long serialVersionUID = 5520498279487119290L;

    protected static final String TABLE_NAME = "t_sell";

    private String clientId;

    private String projectId;

    private String imei;

    private String imsi;

    private String mobile;

    private String province;

    private String city;

    private int operator;

    private String smsc;

    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdate;

    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date modifiledDate;

    public Sell() {
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getImsi() {
        return imsi;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi;
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

    public int getOperator() {
        return operator;
    }

    public void setOperator(int operator) {
        this.operator = operator;
    }

    public String getSmsc() {
        return smsc;
    }

    public void setSmsc(String smsc) {
        this.smsc = smsc;
    }

    // 设定JSON序列化时的日期格式
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    public Date getCreatedate() {
        return createdate;
    }

    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }

    // 设定JSON序列化时的日期格式
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    public Date getModifiledDate() {
        return modifiledDate;
    }

    public void setModifiledDate(Date modifiledDate) {
        this.modifiledDate = modifiledDate;
    }
}
