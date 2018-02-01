package net.mimiduo.boot.pojo.common;

import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class IdEntity extends AbstractPersistable<Long> {

    private static final long serialVersionUID = -1573784109664782022L;

    @Override
    public void setId(Long id) {
        super.setId(id);
    }
}
