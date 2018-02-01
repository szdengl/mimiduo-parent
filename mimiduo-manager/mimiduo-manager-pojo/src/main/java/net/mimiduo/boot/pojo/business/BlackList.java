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
@Table(name = BlackList.TABLE_NAME)
public class BlackList extends IdEntity implements Serializable{
    /**
     *
     */
    private static final long serialVersionUID = -1509961114229932925L;

    protected static final String TABLE_NAME = "t_blacklist";

    private String mobile;

    public BlackList() {
    }

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
