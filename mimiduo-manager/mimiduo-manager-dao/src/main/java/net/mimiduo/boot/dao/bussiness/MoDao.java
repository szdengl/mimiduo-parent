package net.mimiduo.boot.dao.bussiness;


import net.mimiduo.boot.dao.BaseDao;
import net.mimiduo.boot.pojo.business.Mo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface MoDao extends PagingAndSortingRepository<Mo, Long>, JpaRepository<Mo, Long>,
        JpaSpecificationExecutor<Mo>, BaseDao<Mo, Long> {

    Mo findByMobileAndLinkid(String mobile, String linkid);
}
