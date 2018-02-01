package net.mimiduo.boot.pojo.business;

import com.fasterxml.jackson.annotation.JsonFormat;
import net.mimiduo.boot.pojo.common.IdEntity;
import org.springframework.data.annotation.CreatedDate;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = Visit.TABLE_NAME)
public class Visit  extends IdEntity implements Serializable{
    /**
     *
     */
    private static final long serialVersionUID = 1226631344159545355L;

    protected static final String TABLE_NAME = "t_visit";

    private String clientId;

    private String projectId;

    private String imsi;

    private String ip;

    private String mobile;

    private String imei;

    private String smsc;

    private String province;

    private String city;

    private int status;

    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createDate;

    public Visit() {
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

    public String getImsi() {
        return imsi;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getSmsc() {
        return smsc;
    }

    public void setSmsc(String smsc) {
        this.smsc = smsc;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
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
}
