package net.mimiduo.boot.dao.bussiness;


import net.mimiduo.boot.dao.BaseDao;
import net.mimiduo.boot.pojo.business.Charging;
import net.mimiduo.boot.pojo.view.ChargingDaySummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Date;
import java.util.List;

public interface ChargingDao extends PagingAndSortingRepository<Charging, Long>, JpaRepository<Charging, Long>,
        JpaSpecificationExecutor<Charging>, BaseDao<Charging, Long> {


    /***
     *  获取这个客户的这段时间内已经成功计费数据
     * @param mobile
     * @param status
     * @param star
     * @param end
     * @return
     */
    @Query("select sum(fee) from Charging where mobile=?1 and status=?2 and createDate between ?3 and ?4")
    Long sumByMobileAndStatusAndCreateDateBetween(String mobile, int status, Date star, Date end);

    /***
     * 获取这个客户的这段时间内下这个通道计费数据
     * @param mobile
     * @param channelId
     * @param star
     * @param end
     * @return
     */
    long countByMobileAndChannelIdAndCreateDateBetween(String mobile, long channelId, Date star, Date end);

    long countByMobileAndFeeTypeAndCreateDateBetween(String mobile, int feeType, Date star, Date end);

    List<Charging> findByCompanyIdAndChannelIdAndMobileAndStatusAndCreateDateBetween(long compayId, long channelId, String mobile, int status, Date star, Date end);

    List<Charging> findByCompanyIdAndChannelIdAndProvinceAndCityAndStatusAndCreateDateBetween(long companyId, long channelId, String province, String city, int status, Date star, Date end);

    List<Charging> findByCompanyIdAndChannelIdAndProvinceAndStatusAndCreateDateBetween(long companyId, long channelId, String province, int status, Date star, Date end);

    @Query("select province as province,channelId as channelId,count(id) as sendnums,sum(status) as chargings from Charging  where province=?1 and status in (0,1) and createDate >=?2 and createDate <?3 group by province,channelId")
    List<ChargingDaySummary> findByProvinceAndStatusAndCreateDateBetween(String province, Date star, Date end);

}
