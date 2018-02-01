package net.mimiduo.boot.dao.bussiness;

import net.mimiduo.boot.dao.BaseDao;
import net.mimiduo.boot.pojo.business.BlackList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface BlackListDao  extends PagingAndSortingRepository<BlackList, Long>, JpaRepository<BlackList, Long>, JpaSpecificationExecutor<BlackList>, BaseDao<BlackList, Long> {

    BlackList findByMobile(String mobile);
}
