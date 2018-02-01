package net.mimiduo.boot.dao.bussiness;


import net.mimiduo.boot.dao.BaseDao;
import net.mimiduo.boot.pojo.business.PhoneSegment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PhoneSegmentDao  extends PagingAndSortingRepository<PhoneSegment, String>, JpaRepository<PhoneSegment, String>, JpaSpecificationExecutor<PhoneSegment>, BaseDao<PhoneSegment, String> {
}
