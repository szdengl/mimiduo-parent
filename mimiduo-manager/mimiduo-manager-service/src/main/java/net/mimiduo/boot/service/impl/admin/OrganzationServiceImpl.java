package net.mimiduo.boot.service.impl.admin;

import java.util.Collection;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;


import net.mimiduo.boot.dao.BaseDao;
import net.mimiduo.boot.dao.admin.OrganizationDao;
import net.mimiduo.boot.pojo.admin.Organization;
import net.mimiduo.boot.service.admin.OrganizationService;
import net.mimiduo.boot.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.persistence.DynamicSpecifications;
import org.springside.modules.persistence.SearchFilter;

import com.google.common.collect.Lists;



@Service
@Transactional(readOnly = true)
public class OrganzationServiceImpl extends BaseServiceImpl<Organization, Long> implements OrganizationService {

	@Autowired
    private OrganizationDao organizationDao;

	@Override
	public BaseDao<Organization, Long> getDao() {
		return this.getDao();
	}
	
    @Override
    @Transactional
    public void createOrganization(Organization organization) {
        organizationDao.save(organization);
        updateOrganizationCode(organization);
    }

    @Override
    @Transactional
    public void updateOrganization(Organization organization) {
        organizationDao.save(organization);
        updateOrganizationCode(organization);
    }

    @Override
    @Transactional
    public void deleteOrganization(Long id) {
        organizationDao.deleteOrganization(id);
    }

    @Override
    @Transactional
    // 规则： "[${parent1.id}.${parent2.id}.]${this.id}"
    public void updateOrganizationCode(Organization organization) {
        final String idAsCodepart = organization.getId().toString() + ".";
        if (organization.getParent() == null) {
            organization.setCode(idAsCodepart);
        } else {
            organization.setCode(organization.getParent().getCode() + idAsCodepart);
        }

        organizationDao.save(organization);
    }

    @Override
    public boolean existsEmptyOrganizationCode() {
        return (organizationDao.countByCodeIsNull() > 0);
    }

    @Override
    public Organization findOrganizationById(Long id) {
        return organizationDao.findOne(id);
    }

    @Override
    public Page<Organization> findAllOrganizations(Collection<SearchFilter> searchFilters, Pageable pageable) {
        return organizationDao.findAll(DynamicSpecifications.bySearchFilter(searchFilters, Organization.class),
                pageable);
    }

    @Override
    public Page<Organization> findAllTopOrganizations(Collection<SearchFilter> searchFilters, Pageable pageable) {
        Specifications<Organization> spec = Specifications.where(
                DynamicSpecifications.bySearchFilter(searchFilters, Organization.class)).and(
                new Specification<Organization>() {
                    @Override
                    public Predicate toPredicate(Root<Organization> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                        Path<Object> path = root.get("parent");
                        return cb.isNull(path);
                    }
                });
        return organizationDao.findAll(spec, pageable);
    }

    @Override
    public List<Organization> findAllOrganizations() {
        return Lists.newArrayList(organizationDao.findAll());
    }

    @Override
    public List<Organization> findAllTopOrganizations() {
        return organizationDao.findByParentIsNull();
    }

    @Autowired
    public void setOrganizationDao(OrganizationDao organizationDao) {
        this.organizationDao = organizationDao;
    }

	@Override
	public boolean existsOrganization(Organization organization) {
		Organization orgCheck = this.organizationDao.findByName(organization.getName());
		if(orgCheck != null && orgCheck.getId() != organization.getId()){
			return true;
		}else{
			return false;
		}
		
	}

	
}
