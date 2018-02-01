package net.mimiduo.boot.dao.bussiness;


import net.mimiduo.boot.dao.BaseDao;
import net.mimiduo.boot.pojo.business.Visit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface VisitDao extends PagingAndSortingRepository<Visit, Long>, JpaRepository<Visit, Long>,
        JpaSpecificationExecutor<Visit>, BaseDao<Visit, Long> {



}
