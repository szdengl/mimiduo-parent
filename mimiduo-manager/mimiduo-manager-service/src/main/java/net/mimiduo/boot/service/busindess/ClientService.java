package net.mimiduo.boot.service.busindess;

import org.springside.modules.persistence.SearchFilter;
import net.mimiduo.boot.pojo.business.Client;
import net.mimiduo.boot.service.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Collection;
import java.util.List;


public interface ClientService extends BaseService<Client, Long> {

	Page<Client> findAllTopClients(Collection<SearchFilter> searchFilters, PageRequest pageable);
	
	List<Client> findByIsstatus(String isstatus);

	Client findByCode(String code);
	
}
