package net.mimiduo.boot.dao.bussiness;



import net.mimiduo.boot.dao.BaseDao;
import net.mimiduo.boot.pojo.business.Attribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface AttributeDao extends PagingAndSortingRepository<Attribute, Long>, JpaRepository<Attribute, Long>, JpaSpecificationExecutor<Attribute>, BaseDao<Attribute, Long> {
    List<Attribute> findByType(int type);
}
