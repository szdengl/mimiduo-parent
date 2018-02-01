package net.mimiduo.boot.service.admin;

import java.util.Collection;
import java.util.List;

import net.mimiduo.boot.pojo.admin.Organization;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springside.modules.persistence.SearchFilter;



public interface OrganizationService {

    /**
     * 通过过滤条件查所有的组织机构.
     */
    Page<Organization> findAllOrganizations(Collection<SearchFilter> searchFilters, Pageable pageable);

    /**
     * 创建组织机构.
     */
    void createOrganization(Organization organization);

    /**
     * 更新组织机构.
     */
    void updateOrganization(Organization organization);

    /**
     * 通过Id找组织机构.
     */
    Organization findOrganizationById(Long id);

    /**
     * 删除组织机构.
     */
    void deleteOrganization(Long id);

    /**
     * 更新组织机构的编码.
     */
    void updateOrganizationCode(Organization organization);

    /**
     * 机构中存在未更新的空code?
     */
    boolean existsEmptyOrganizationCode();

    /**
     * 通过过滤条件查找所有的组织机构.
     */
    Page<Organization> findAllTopOrganizations(Collection<SearchFilter> searchFilters, Pageable pageable);

    /**
     * 查找所遇的组织机构.
     */
    List<Organization> findAllOrganizations();

    /**
     * 查找所有第一级组织机构.
     */
    List<Organization> findAllTopOrganizations();
    
    /**
     * 判断机构是否已经存在
     * @param name
     * @return
     */
    boolean existsOrganization(Organization organization);
}
