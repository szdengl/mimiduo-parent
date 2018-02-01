package net.mimiduo.boot.dao.bussiness;

import java.util.List;


import net.mimiduo.boot.dao.BaseDao;
import net.mimiduo.boot.pojo.business.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CompanyDao extends PagingAndSortingRepository<Company, Long>, JpaRepository<Company, Long>,
		JpaSpecificationExecutor<Company>, BaseDao<Company, Long> {
	List<Company> findCompanyByIsstatus(int isstatus);
}
