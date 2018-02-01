package net.mimiduo.boot.pojo.business;


import net.mimiduo.boot.pojo.common.IdEntity;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name  = Province.TABLE_NAME)
@Cacheable
public class Province extends IdEntity {

    /**
     *
     */
    private static final long serialVersionUID = -6376897444353292624L;

    protected static final String TABLE_NAME = "t_province";

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
