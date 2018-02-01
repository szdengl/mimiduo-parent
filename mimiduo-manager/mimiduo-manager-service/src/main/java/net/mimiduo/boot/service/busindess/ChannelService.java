package net.mimiduo.boot.service.busindess;

import org.springside.modules.persistence.SearchFilter;
import net.mimiduo.boot.pojo.business.Channel;
import net.mimiduo.boot.pojo.business.Charging;
import net.mimiduo.boot.pojo.business.Sell;
import net.mimiduo.boot.pojo.business.Visit;
import net.mimiduo.boot.service.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;




public interface ChannelService extends BaseService<Channel, Long> {
	void updateChannel(Channel channel);

	void updateChannelByCompanyId(long id);

	void deleteChannel(long id);

	void deleteChannelByCompanyId(long id);

	Page<Channel> findAllChannels(Collection<SearchFilter> searchFilters, Pageable pageable);

	List<Channel> findByCompanyId(int companyId);

	List<Channel> findByServiceType(int serviceType);

	List<Channel> findByCompanyIdAndCmdAndSpnumber(long companyId, String cmd, String spnumber);

	List<Channel> findByCompanyIdAndSpnumberAndFeeType(long companyId, String spnumber, int feeType);

	String findBySellAndIsNew(Sell sell, Boolean isNew, Charging charging, Visit visit, StringBuilder log);

	void updateRecord(String mobile, String spnumber, String cmd, long companyId, int number, int feeType);
}
