package net.mimiduo.boot.service.impl.business;

import java.util.Collection;
import java.util.List;

import net.mimiduo.boot.dao.BaseDao;
import net.mimiduo.boot.dao.bussiness.ClientDao;
import net.mimiduo.boot.pojo.business.Client;
import net.mimiduo.boot.service.busindess.ClientService;
import net.mimiduo.boot.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Component;
import org.springside.modules.persistence.SearchFilter;


@Component
public class ClientServiceImpl extends BaseServiceImpl<Client, Long> implements ClientService {

	@Autowired
	private ClientDao clientDao;

	@Override
	public BaseDao<Client, Long> getDao() {
		return this.clientDao;
	}
	
	@Override
	public Page<Client> findAllTopClients(Collection<SearchFilter> searchFilters, PageRequest page) {
		Specifications<Client> spec = Specifications.where(bySearchFilter(searchFilters, Client.class));
		return clientDao.findAll(spec, page);
	}

	@Override
	public List<Client> findByIsstatus(String isstatus) {
		return clientDao.findByIsstatus(isstatus);
	}

	@Override
	public Client findByCode(String code) {
		return clientDao.findByCode(code);
	}


}
