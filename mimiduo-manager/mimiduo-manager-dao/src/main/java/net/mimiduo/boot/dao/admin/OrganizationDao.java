package net.mimiduo.boot.dao.admin;

import java.util.List;

import net.mimiduo.boot.pojo.admin.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;


public interface OrganizationDao extends JpaRepository<Organization, Long>, JpaSpecificationExecutor<Organization> {

    List<Organization> findByParentIsNull();

    long countByCodeIsNull();
    
    @Query("select u from Organization u where u.name = ?1")
    Organization findByName(String name);
    
    @Modifying
    @Query("update Organization u set u.isDeleted = 1 where u.id = ?1")
    void deleteOrganization(Long id);
}
