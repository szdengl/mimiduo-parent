package net.mimiduo.boot.pojo.business;


import net.mimiduo.boot.pojo.common.IdEntity;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = ChannelProvince.TABLE_NAME)
@Cacheable
public class ChannelProvince extends IdEntity implements Cloneable,Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 6137182720457362368L;

    protected static final String TABLE_NAME = "t_channel_province";

    private String provinceId;

    private long channelId;

    private int dayLimit;

    private int monthLimit;

    private int startime;

    private int endtime;

    private int isstatus;

    private int orderId;

    private String shieldKeyword;

    private String replyContent;

    public ChannelProvince() {
    }

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public long getChannelId() {
        return channelId;
    }

    public void setChannelId(long channelId) {
        this.channelId = channelId;
    }

    public int getDayLimit() {
        return dayLimit;
    }

    public void setDayLimit(int dayLimit) {
        this.dayLimit = dayLimit;
    }

    public int getMonthLimit() {
        return monthLimit;
    }

    public void setMonthLimit(int monthLimit) {
        this.monthLimit = monthLimit;
    }

    public int getStartime() {
        return startime;
    }

    public void setStartime(int startime) {
        this.startime = startime;
    }

    public int getEndtime() {
        return endtime;
    }

    public void setEndtime(int endtime) {
        this.endtime = endtime;
    }

    public int getIsstatus() {
        return isstatus;
    }

    public void setIsstatus(int isstatus) {
        this.isstatus = isstatus;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getShieldKeyword() {
        return shieldKeyword;
    }

    public void setShieldKeyword(String shieldKeyword) {
        this.shieldKeyword = shieldKeyword;
    }

    public String getReplyContent() {
        return replyContent;
    }

    public void setReplyContent(String replyContent) {
        this.replyContent = replyContent;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
