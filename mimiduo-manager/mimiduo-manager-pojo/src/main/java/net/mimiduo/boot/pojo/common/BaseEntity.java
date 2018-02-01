package net.mimiduo.boot.pojo.common;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class BaseEntity<U> extends AbstractEntity {

    private static final long serialVersionUID = 1678372157852941273L;

    @ManyToOne
    @CreatedBy
    private U createdBy;

    @ManyToOne
    @LastModifiedBy
    private U lastModifiedBy;
    

    public U getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(final U createdBy) {
        this.createdBy = createdBy;
    }

    public U getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(final U lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }
    
}
