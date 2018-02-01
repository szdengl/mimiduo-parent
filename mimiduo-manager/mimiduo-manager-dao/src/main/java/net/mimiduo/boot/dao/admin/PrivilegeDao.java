package net.mimiduo.boot.dao.admin;

import java.util.List;

import net.mimiduo.boot.pojo.admin.Privilege;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;



public interface PrivilegeDao extends JpaRepository<Privilege, Long>, JpaSpecificationExecutor<Privilege> {

    Privilege findByTargetAndMethod(String target, String method);

    Privilege findByTargetAndMethodAndScope(String target, String method, String scope);

    Privilege findByName(String name);

    @Query("SELECT p FROM Privilege p WHERE id IN (select p.id FROM Role r JOIN r.privileges p where r.id = ?1)")
    List<Privilege> findPrivilegesById(Long roleId, Sort sort);
    
    @Modifying
    @Query("update Privilege u set u.isDeleted = 1 where u.id = ?1")
    void deletePrivileges(Long id);
}
