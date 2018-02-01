package net.mimiduo.boot.dao.bussiness;


import net.mimiduo.boot.dao.BaseDao;
import net.mimiduo.boot.pojo.business.Ivr;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface IvrDao extends PagingAndSortingRepository<Ivr, Long>, JpaRepository<Ivr, Long>,
        JpaSpecificationExecutor<Ivr>, BaseDao<Ivr, Long> {
}
