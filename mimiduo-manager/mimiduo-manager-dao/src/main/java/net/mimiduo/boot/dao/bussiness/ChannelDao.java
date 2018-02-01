package net.mimiduo.boot.dao.bussiness;

import java.util.List;

import net.mimiduo.boot.dao.BaseDao;
import net.mimiduo.boot.pojo.business.Channel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;



public interface ChannelDao extends PagingAndSortingRepository<Channel, Long>, JpaRepository<Channel, Long>,
		JpaSpecificationExecutor<Channel>, BaseDao<Channel, Long> {

	List<Channel> findByCompanyIdAndIsstatus(long companyId, String istatus);

	List<Channel> findByServiceType(int serviceType);

	List<Channel> findByCompanyIdAndCmdAndSpnumber(long companyId, String cmd, String spnumber);

	List<Channel> findByCompanyIdAndSpnumberAndFeeType(long companyId, String spnumber, int feeType);

	@Modifying
	@Query("update Channel set isstatus=1 where companyId=?1")
	void updateByCompanyId(long companyId);

	@Modifying
	void deleteByCompanyId(long companyId);

}
