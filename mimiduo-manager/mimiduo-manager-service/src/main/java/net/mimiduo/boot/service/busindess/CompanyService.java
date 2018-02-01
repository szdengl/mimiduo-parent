package net.mimiduo.boot.service.busindess;

import java.util.Collection;
import java.util.List;


import net.mimiduo.boot.pojo.business.Company;
import net.mimiduo.boot.service.BaseService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springside.modules.persistence.SearchFilter;


@CacheConfig(cacheNames = "company")
public interface CompanyService  extends BaseService<Company, Long> {

	Page<Company> findAllCompany(Collection<SearchFilter> searchFilters, Pageable pageable);

	List<Company> findCompanyByIsstatus(int isstatus);


	@CachePut
	void updateCompany(Company company);

	@CacheEvict
	void deleteCompany(long id);

	@Cacheable
	Company findById(long id);
}
