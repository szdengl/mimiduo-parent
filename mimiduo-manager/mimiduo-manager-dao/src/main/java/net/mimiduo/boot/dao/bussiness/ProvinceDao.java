package net.mimiduo.boot.dao.bussiness;


import net.mimiduo.boot.dao.BaseDao;
import net.mimiduo.boot.pojo.business.Province;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ProvinceDao  extends PagingAndSortingRepository<Province, Long>, JpaRepository<Province, Long>, JpaSpecificationExecutor<Province>, BaseDao<Province, Long> {
    Province findByName(String name);
}
