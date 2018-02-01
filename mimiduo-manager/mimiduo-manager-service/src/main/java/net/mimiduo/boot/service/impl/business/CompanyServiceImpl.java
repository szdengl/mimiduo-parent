package net.mimiduo.boot.service.impl.business;

import java.util.Collection;
import java.util.List;

import net.mimiduo.boot.common.exception.ExecuteException;
import net.mimiduo.boot.dao.BaseDao;
import net.mimiduo.boot.dao.bussiness.CompanyDao;
import net.mimiduo.boot.pojo.business.Company;
import net.mimiduo.boot.service.busindess.ChannelService;
import net.mimiduo.boot.service.busindess.CompanyService;
import net.mimiduo.boot.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.persistence.SearchFilter;

@Service
public class CompanyServiceImpl extends BaseServiceImpl<Company, Long> implements CompanyService {
	@Autowired
	private CompanyDao companyDao;

	@Autowired
	private ChannelService channelService;

	@Transactional(readOnly = true)
	@Override
	public Page<Company> findAllCompany(Collection<SearchFilter> searchFilters, Pageable pageable) {
		return companyDao.findAll(bySearchFilter(searchFilters, Company.class), pageable);
	}

	@Override
	public List<Company> findCompanyByIsstatus(int status) {
		return companyDao.findCompanyByIsstatus(status);
	}


	@Override
	@Transactional(rollbackFor = ExecuteException.class)
	public void updateCompany(Company company){
		this.save(company);
		if(company.getIsstatus()==1){
			this.channelService.updateChannelByCompanyId(company.getId());
		}
	}

	@Override
	@Transactional(rollbackFor = ExecuteException.class)
	public void deleteCompany(long id){
		this.delete(id);
		this.channelService.deleteChannelByCompanyId(id);
	}

	@Override
	public Company findById(long id) {
		return this.findOne(id);
	}

	@Override
	public BaseDao<Company, Long> getDao() {
		return this.companyDao;
	}

	

	
}
