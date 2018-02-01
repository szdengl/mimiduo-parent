package net.mimiduo.boot.service.impl.business;


import net.mimiduo.boot.dao.BaseDao;
import net.mimiduo.boot.dao.bussiness.ChargingDao;
import net.mimiduo.boot.pojo.business.Charging;
import net.mimiduo.boot.pojo.view.ChargingDaySummary;
import net.mimiduo.boot.service.busindess.ChargingService;
import net.mimiduo.boot.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ChargingServiceImpl extends BaseServiceImpl<Charging,Long> implements ChargingService {

    @Autowired
    private ChargingDao chargingDao;

    @Override
    public BaseDao<Charging, Long> getDao() {
        return this.chargingDao;
    }

    @Override
    public long findByMobileAndStatusAndCreateDateBetween(String mobile, int status, Date star, Date end) {
        Long sumFee =this.chargingDao.sumByMobileAndStatusAndCreateDateBetween(mobile, status, star, end);
        if(sumFee==null){
            sumFee=0L;
        }
        return sumFee.longValue();
    }

    @Override
    public long countByMobileAndChannelIdAndCreateDateBetween(String mobile, long channelID, Date star, Date end) {
        return this.chargingDao.countByMobileAndChannelIdAndCreateDateBetween(mobile, channelID, star, end);
    }

    @Override
    public long countByMobileAndFeeTypeAndCreateDateBetween(String mobile, int feeType, Date star, Date end) {
        return this.chargingDao.countByMobileAndFeeTypeAndCreateDateBetween(mobile, feeType, star, end);
    }

    @Override
    public List<Charging> findByCompanyIdAndChannelIdAndMobileAndStatusAndCreateDateBetween(long compayId, long channelId, String mobile, int status, Date star, Date end) {
        return this.chargingDao.findByCompanyIdAndChannelIdAndMobileAndStatusAndCreateDateBetween(compayId, channelId, mobile, status, star, end);
    }

    @Override
    public List<Charging> findByCompanyIdAndChannelIdAndProvinceAndCityAndStatusAndCreateDateBetween(long companyId, long channelId, String province, String city, int status, Date star, Date end) {
        return this.chargingDao.findByCompanyIdAndChannelIdAndProvinceAndCityAndStatusAndCreateDateBetween(companyId, channelId, province, city, status, star, end);
    }

    @Override
    public List<Charging> findByCompanyIdAndChannelIdAndProvinceAndStatusAndCreateDateBetween(long companyId, long channelId, String province, int status, Date star, Date end) {
        return this.chargingDao.findByCompanyIdAndChannelIdAndProvinceAndStatusAndCreateDateBetween(companyId, channelId, province, status, star, end);
    }

    @Override
    public List<ChargingDaySummary> findByProvinceAndStatusAndCreateDateBetween(String province, Date star, Date end){
        return this.chargingDao.findByProvinceAndStatusAndCreateDateBetween(province, star, end);
    }
}
