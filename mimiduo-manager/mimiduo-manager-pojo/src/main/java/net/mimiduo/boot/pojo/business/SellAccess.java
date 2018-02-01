package net.mimiduo.boot.pojo.business;

import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Entity
@Table(name=SellAccess.TABLE_NAME ,uniqueConstraints = @UniqueConstraint(columnNames = {"imsi"}))
public class SellAccess implements Serializable {

    protected static final String TABLE_NAME = "t_sell_access";

    @Id
    private String imsi;

    private int visits;

    private int chargings;

    private int success;

    private int money;

    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date modifiledDate;


    public SellAccess() {
    }

    public String getImsi() {
        return imsi;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi;
    }

    public int getVisits() {
        return visits;
    }

    public void setVisits(int visits) {
        this.visits = visits;
    }

    public int getChargings() {
        return chargings;
    }

    public void setChargings(int chargings) {
        this.chargings = chargings;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public Date getModifiledDate() {
        return modifiledDate;
    }

    public void setModifiledDate(Date modifiledDate) {
        this.modifiledDate = modifiledDate;
    }
}
