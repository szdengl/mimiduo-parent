package net.mimiduo.boot.pojo.business;


import net.mimiduo.boot.pojo.common.IdEntity;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = Channel.TABLE_NAME)
@Cacheable
public class Channel extends IdEntity implements Serializable{

    /**
     *
     */
    private static final long serialVersionUID = 1843167046105527126L;

    protected static final String TABLE_NAME = "t_channel";
    private long companyId;
    private String name;
    private int feeType;
    private int operator;
    private int serviceType;
    private String shieldPort;
    private String shieldCity;
    private Integer accessPrice;
    @Column(nullable = false, length = 2)
    private String isstatus;
    @Column(nullable = false)
    private int fee;
    private int intervals;
    private String keyContent;
    private String openProvince;
    private String thirdCmd;
    private int separate;

    /*** 第一步 ****/
    private String cmd;
    private String spnumber;
    private String flowControl;
    private int visitType = 1;
    private int isExec = 0;
    private int isSycn = 0;
    private int contentType = 1;
    private int isThirdCmd = 0;
    private int isGet = 1;
    /*** 第二步 ****/
    private String cmd1;
    private String spnumber1;
    private String flowControl1;
    private int visitType1 = 1;
    private int isExec1 = 0;
    private int isSycn1 = 0;
    private int contentType1 = 1;
    private int isThirdCmd1 = 0;
    private int isGet1 = 1;
    /*** 第三步 ****/
    private String cmd2;
    private String spnumber2;
    private String flowControl2;
    private int visitType2 = 1;
    private int isExec2 = 0;
    private int isSycn2 = 0;
    private int contentType2 = 1;
    private int isThirdCmd2 = 0;
    private int isGet2 = 1;

    public Channel() {
    }

    public long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(long companyId) {
        this.companyId = companyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFeeType() {
        return feeType;
    }

    public void setFeeType(int feeType) {
        this.feeType = feeType;
    }

    public int getOperator() {
        return operator;
    }

    public void setOperator(int operator) {
        this.operator = operator;
    }

    public int getServiceType() {
        return serviceType;
    }

    public void setServiceType(int serviceType) {
        this.serviceType = serviceType;
    }

    public String getShieldPort() {
        return shieldPort;
    }

    public void setShieldPort(String shieldPort) {
        this.shieldPort = shieldPort;
    }

    public String getShieldCity() {
        return shieldCity;
    }

    public void setShieldCity(String shieldCity) {
        this.shieldCity = shieldCity;
    }

    public Integer getAccessPrice() {
        return accessPrice;
    }

    public void setAccessPrice(Integer accessPrice) {
        this.accessPrice = accessPrice;
    }

    public String getIsstatus() {
        return isstatus;
    }

    public void setIsstatus(String isstatus) {
        this.isstatus = isstatus;
    }

    public int getFee() {
        return fee;
    }

    public void setFee(int fee) {
        this.fee = fee;
    }

    public int getIntervals() {
        return intervals;
    }

    public void setIntervals(int intervals) {
        this.intervals = intervals;
    }

    public String getKeyContent() {
        return keyContent;
    }

    public void setKeyContent(String keyContent) {
        this.keyContent = keyContent;
    }

    public String getOpenProvince() {
        return openProvince;
    }

    public void setOpenProvince(String openProvince) {
        this.openProvince = openProvince;
    }

    public String getThirdCmd() {
        return thirdCmd;
    }

    public void setThirdCmd(String thirdCmd) {
        this.thirdCmd = thirdCmd;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public String getSpnumber() {
        return spnumber;
    }

    public void setSpnumber(String spnumber) {
        this.spnumber = spnumber;
    }

    public String getFlowControl() {
        return flowControl;
    }

    public void setFlowControl(String flowControl) {
        this.flowControl = flowControl;
    }

    public int getVisitType() {
        return visitType;
    }

    public void setVisitType(int visitType) {
        this.visitType = visitType;
    }

    public int getIsExec() {
        return isExec;
    }

    public void setIsExec(int isExec) {
        this.isExec = isExec;
    }

    public int getIsSycn() {
        return isSycn;
    }

    public void setIsSycn(int isSycn) {
        this.isSycn = isSycn;
    }

    public int getContentType() {
        return contentType;
    }

    public void setContentType(int contentType) {
        this.contentType = contentType;
    }

    public int getIsThirdCmd() {
        return isThirdCmd;
    }

    public void setIsThirdCmd(int isThirdCmd) {
        this.isThirdCmd = isThirdCmd;
    }

    public String getCmd1() {
        return cmd1;
    }

    public void setCmd1(String cmd1) {
        this.cmd1 = cmd1;
    }

    public String getSpnumber1() {
        return spnumber1;
    }

    public void setSpnumber1(String spnumber1) {
        this.spnumber1 = spnumber1;
    }

    public String getFlowControl1() {
        return flowControl1;
    }

    public void setFlowControl1(String flowControl1) {
        this.flowControl1 = flowControl1;
    }

    public int getVisitType1() {
        return visitType1;
    }

    public void setVisitType1(int visitType1) {
        this.visitType1 = visitType1;
    }

    public int getIsExec1() {
        return isExec1;
    }

    public void setIsExec1(int isExec1) {
        this.isExec1 = isExec1;
    }

    public int getIsSycn1() {
        return isSycn1;
    }

    public void setIsSycn1(int isSycn1) {
        this.isSycn1 = isSycn1;
    }

    public int getContentType1() {
        return contentType1;
    }

    public void setContentType1(int contentType1) {
        this.contentType1 = contentType1;
    }

    public int getIsThirdCmd1() {
        return isThirdCmd1;
    }

    public void setIsThirdCmd1(int isThirdCmd1) {
        this.isThirdCmd1 = isThirdCmd1;
    }

    public String getCmd2() {
        return cmd2;
    }

    public void setCmd2(String cmd2) {
        this.cmd2 = cmd2;
    }

    public String getSpnumber2() {
        return spnumber2;
    }

    public void setSpnumber2(String spnumber2) {
        this.spnumber2 = spnumber2;
    }

    public String getFlowControl2() {
        return flowControl2;
    }

    public void setFlowControl2(String flowControl2) {
        this.flowControl2 = flowControl2;
    }

    public int getVisitType2() {
        return visitType2;
    }

    public void setVisitType2(int visitType2) {
        this.visitType2 = visitType2;
    }

    public int getIsExec2() {
        return isExec2;
    }

    public void setIsExec2(int isExec2) {
        this.isExec2 = isExec2;
    }

    public int getIsSycn2() {
        return isSycn2;
    }

    public void setIsSycn2(int isSycn2) {
        this.isSycn2 = isSycn2;
    }

    public int getContentType2() {
        return contentType2;
    }

    public void setContentType2(int contentType2) {
        this.contentType2 = contentType2;
    }

    public int getIsThirdCmd2() {
        return isThirdCmd2;
    }

    public void setIsThirdCmd2(int isThirdCmd2) {
        this.isThirdCmd2 = isThirdCmd2;
    }

    public int getIsGet() {
        return isGet;
    }

    public void setIsGet(int isGet) {
        this.isGet = isGet;
    }

    public int getIsGet1() {
        return isGet1;
    }

    public void setIsGet1(int isGet1) {
        this.isGet1 = isGet1;
    }

    public int getIsGet2() {
        return isGet2;
    }

    public void setIsGet2(int isGet2) {
        this.isGet2 = isGet2;
    }

    public int getSeparate() {
        return separate;
    }

    public void setSeparate(int separate) {
        this.separate = separate;
    }

    public String getCmd(int i) {
        switch (i) {
            case 0:
                this.getCmd();
            case 1:
                return this.getCmd1();
            default:
                return this.getCmd2();
        }
    }

    public String getSpnumber(int i) {
        switch (i) {
            case 0:
                this.getSpnumber();
            case 1:
                return this.getSpnumber1();
            default:
                return this.getSpnumber2();
        }
    }

    public String getFlowControl(int i) {
        switch (i) {
            case 0:
                this.getFlowControl();
            case 1:
                return this.getFlowControl1();
            default:
                return this.getFlowControl2();
        }
    }

    public int getVisitType(int i) {
        switch (i) {
            case 0:
                this.getVisitType();
            case 1:
                return this.getVisitType1();
            default:
                return this.getVisitType2();
        }
    }

    public int getIsExec(int i) {
        switch (i) {
            case 0:
                return this.getIsExec();
            case 1:
                return this.getIsExec1();
            default:
                return this.getIsExec2();
        }
    }

    public int getIsSycn(int i) {
        switch (i) {
            case 0:
                return this.getIsSycn();
            case 1:
                return this.getIsSycn1();
            default:
                return this.getIsSycn2();
        }
    }

    public int getContentType(int i) {
        switch (i) {
            case 0:
                return this.getContentType();
            case 1:
                return this.getContentType1();
            default:
                return this.getContentType2();
        }
    }

    public int getIsThirdCmd(int i) {
        switch (i) {
            case 0:
                return this.getIsThirdCmd();
            case 1:
                return this.getIsThirdCmd1();
            default:
                return this.getIsThirdCmd2();
        }
    }

    public int getIsGet(int i) {
        switch (i) {
            case 0:
                return this.getIsGet();
            case 1:
                return this.getIsGet1();
            default:
                return this.getIsGet2();
        }
    }


}
