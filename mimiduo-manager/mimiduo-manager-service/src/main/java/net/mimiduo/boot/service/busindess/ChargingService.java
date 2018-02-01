package net.mimiduo.boot.service.busindess;



import net.mimiduo.boot.pojo.business.Charging;
import net.mimiduo.boot.pojo.view.ChargingDaySummary;
import net.mimiduo.boot.service.BaseService;

import java.util.Date;
import java.util.List;

public interface ChargingService  extends BaseService<Charging,Long> {

    long findByMobileAndStatusAndCreateDateBetween(String mobile, int status, Date star, Date end);

    long  countByMobileAndChannelIdAndCreateDateBetween(String mobile, long channelID, Date star, Date end);

    long countByMobileAndFeeTypeAndCreateDateBetween(String mobile, int feeType, Date star, Date end);

    List<Charging> findByCompanyIdAndChannelIdAndMobileAndStatusAndCreateDateBetween(long compayId, long channelId, String mobile, int status, Date star, Date end);

    List<Charging> findByCompanyIdAndChannelIdAndProvinceAndCityAndStatusAndCreateDateBetween(long companyId, long channelId, String province, String city, int status, Date star, Date end);

    List<Charging> findByCompanyIdAndChannelIdAndProvinceAndStatusAndCreateDateBetween(long companyId, long channelId, String province, int status, Date star, Date end);

    List<ChargingDaySummary> findByProvinceAndStatusAndCreateDateBetween(String province, Date star, Date end);

}
