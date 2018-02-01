package net.mimiduo.boot.pojo.business;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = PhoneSegment.TABLE_NAME)
public class PhoneSegment {

    protected static final String TABLE_NAME = "t_phone";

    private String prefix;

    @Id
    private String phone;

    private String province;

    private String city;

    private String types;

    private String code;

    private String zip;

    private int isp;

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public int getIsp() {
        return isp;
    }

    public void setIsp(int isp) {
        this.isp = isp;
    }
}
