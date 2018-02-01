package net.mimiduo.boot.pojo.admin;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.collect.Lists;
import net.mimiduo.boot.common.util.IdSerializer;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 角色.
 * 
 */
@Entity
@Table(name = Role.TABLE_NAME)
public class Role extends BaseAuditEntity {

    public static final String TABLE_NAME = "t_role";

    private static final long serialVersionUID = -369638231589109651L;

    // 名称.
    @Column(length = 200, nullable = false, unique = true)
    @Size(max = 200)
    @NotNull
    private String name;

    // 领域.
    @Column(length = 200)
    @Size(max = 200)
    private String realm;

    // 描述.
    @Column(length = 500)
    @Size(max = 500)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @JsonSerialize(using = IdSerializer.class)
    private Role parent;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Role> children = Lists.newArrayList();

    @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE, CascadeType.PERSIST })
    @JoinTable(name = "t_role_privilege", joinColumns = @JoinColumn(name = "role_id"), inverseJoinColumns = @JoinColumn(name = "privilege_id"), uniqueConstraints = { @UniqueConstraint(columnNames = {
            "role_id", "privilege_id" }) })
    @OrderBy("id ASC")
    @JsonIgnore
    private List<Privilege> privileges = Lists.newArrayList();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "role", fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @OrderBy("id ASC")
    @JsonIgnore
    private List<UserRole> userRoles = Lists.newArrayList();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRealm() {
        return realm;
    }

    public void setRealm(String realm) {
        this.realm = realm;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Role getParent() {
        return parent;
    }

    public void setParent(Role parent) {
        this.parent = parent;
    }

    public List<Role> getChildren() {
        return children;
    }

    public void setChildren(List<Role> children) {
        this.children = children;
    }

    public List<Privilege> getPrivileges() {
        return privileges;
    }

    public void setPrivileges(List<Privilege> privileges) {
        this.privileges = privileges;
    }

    public void addPrivilege(Privilege privilege) {
        this.getPrivileges().add(privilege);
    }

    public void removePrivilege(Privilege privilege) {
        this.getPrivileges().remove(privilege);
    }
}
