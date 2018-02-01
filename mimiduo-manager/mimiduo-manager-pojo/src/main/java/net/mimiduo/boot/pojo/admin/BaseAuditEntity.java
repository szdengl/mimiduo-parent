package net.mimiduo.boot.pojo.admin;

import net.mimiduo.boot.pojo.common.BaseEntity;

import javax.persistence.MappedSuperclass;

/**
 * 带审计功能实体基类.
 * 
 */
@MappedSuperclass
public abstract class BaseAuditEntity extends BaseEntity<User> {

    private static final long serialVersionUID = -5468150322229859380L;

}
