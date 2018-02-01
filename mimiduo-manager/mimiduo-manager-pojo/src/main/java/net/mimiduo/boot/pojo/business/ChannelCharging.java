package net.mimiduo.boot.pojo.business;



import net.mimiduo.boot.pojo.common.IdEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * ${DESCRIPTION}
 *
 * @author:LingDeng
 * @create 2017-12-25 10:39
 **/
@Entity
@Table(name = ChannelCharging.TABLE_NAME)
public class ChannelCharging  extends IdEntity implements Serializable {

    protected static final String TABLE_NAME = "t_channel_charging";


    private String province;
    private long channelId;
    private int sendnums;
    private int success;


    public ChannelCharging() {
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public long getChannelId() {
        return channelId;
    }

    public void setChannelId(long channelId) {
        this.channelId = channelId;
    }

    public int getSendnums() {
        return sendnums;
    }

    public void setSendnums(int sendnums) {
        this.sendnums = sendnums;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }
}
