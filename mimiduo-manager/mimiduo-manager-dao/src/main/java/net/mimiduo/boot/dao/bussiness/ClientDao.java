package net.mimiduo.boot.dao.bussiness;

import java.util.List;


import net.mimiduo.boot.dao.BaseDao;
import net.mimiduo.boot.pojo.business.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;



public interface ClientDao extends PagingAndSortingRepository<Client, Long>, JpaRepository<Client, Long>,
		JpaSpecificationExecutor<Client>, BaseDao<Client, Long> {

	List<Client> findByIsstatus(String isstatus);

	Client findByCode(String code);
}
