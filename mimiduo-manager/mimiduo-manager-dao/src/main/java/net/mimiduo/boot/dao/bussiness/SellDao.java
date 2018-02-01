package net.mimiduo.boot.dao.bussiness;


import net.mimiduo.boot.dao.BaseDao;
import net.mimiduo.boot.pojo.business.Sell;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface SellDao  extends PagingAndSortingRepository<Sell, Long>, JpaRepository<Sell, Long>,
        JpaSpecificationExecutor<Sell>, BaseDao<Sell, Long> {

    Sell findByMobile(String mobile);

    Sell findByImsi(String imsi);
}
