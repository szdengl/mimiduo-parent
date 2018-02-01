package net.mimiduo.boot.pojo.business;


import net.mimiduo.boot.pojo.common.IdEntity;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Table;


/***
 * 属性
 */
@Entity
@Table(name = Attribute.TABLE_NAME)
@Cacheable
public class Attribute extends IdEntity {
    /**
     *
     */
    private static final long serialVersionUID = -1509961114229932925L;

    protected static final String TABLE_NAME = "t_attribute";

    private String name;

    private int type;

    public Attribute() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

}
