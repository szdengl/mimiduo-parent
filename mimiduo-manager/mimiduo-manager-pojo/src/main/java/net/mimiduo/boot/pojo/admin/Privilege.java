package net.mimiduo.boot.pojo.admin;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 权限.
 * 
 */
@Entity
@Table(name = Privilege.TABLE_NAME)
public class Privilege extends BaseAuditEntity {

    public static final String TABLE_NAME = "t_privilege";

    private static final long serialVersionUID = 1296297093696533643L;

    // 名称.
    @Column(length = 200, nullable = false, unique = true)
    @Size(max = 200)
    @NotNull
    private String name;

    // 类型.
    @Column(length = 100)
    @Size(max = 100)
    private String type;

    // 资源.
    @Column(length = 100)
    @Size(max = 100)
    private String target;

    // 操作.
    @Column(length = 100)
    @Size(max = 100)
    private String method;

    @Column(length = 200)
    @Size(max = 200)
    private String scope;

    @Column(length = 500)
    @Size(max = 500)
    private String description;

    private boolean readOnly = false;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }
}
