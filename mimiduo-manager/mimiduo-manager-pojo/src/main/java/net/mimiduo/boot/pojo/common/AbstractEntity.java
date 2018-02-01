package net.mimiduo.boot.pojo.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import net.mimiduo.boot.common.domain.ActiveStatus;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;


@MappedSuperclass
public abstract class AbstractEntity extends IdEntity {

    private static final long serialVersionUID = -863095561995028355L;

    @Version
    private long version;

    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date lastModifiedDate;

    @Column
    @NotNull
    private int isDeleted = 0;

    @Column
    @NotNull
    private int isActived = 0;

    @Transient
    private String isActivedDisplay;

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }

    public int getIsActived() {
        return isActived;
    }

    public void setIsActived(int isActived) {
        this.isActived = isActived;
    }

    public String getIsActivedDisplay() {
        if (this.getIsActived() == ActiveStatus.ACTIVE.getValue()) {
            return ActiveStatus.ACTIVE.getText();
        } else if (this.getIsActived() == ActiveStatus.UNACTIVE.getValue()) {
            return ActiveStatus.UNACTIVE.getText();
        }
        return "";
    }

    public void setIsActivedDisplay(String isActivedDisplay) {
        this.isActivedDisplay = isActivedDisplay;
    }

}
