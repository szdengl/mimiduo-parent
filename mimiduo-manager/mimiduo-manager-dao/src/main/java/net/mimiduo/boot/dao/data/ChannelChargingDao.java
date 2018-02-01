package net.mimiduo.boot.dao.data;


import net.mimiduo.boot.dao.BaseDao;
import net.mimiduo.boot.pojo.business.ChannelCharging;
import net.mimiduo.boot.pojo.view.ChargingView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * ${DESCRIPTION}
 *
 * @author:LingDeng
 * @create 2017-12-25 10:56
 **/
public interface ChannelChargingDao extends JpaRepository<ChannelCharging,Long>,PagingAndSortingRepository<ChannelCharging,Long>,JpaSpecificationExecutor<ChannelCharging>,BaseDao<ChannelCharging,Long> {
    String findByOperatorSql="select a.province as province,a.channelId as channelId,a.sendnums as sendnums ,a.success as chargings,d.name as company,b.name as channelName,c.name as serviceType,b.fee as price,e.orderId as orderId "
            +" from ChannelCharging as a,Channel as b,Attribute as c,Company as d,ChannelProvince e"
            +" where a.channelId=b.id and b.companyId=d.id and b.serviceType=c.id and a.channelId=e.channelId and a.province=e.provinceId and b.operator=?1";

    @Modifying
    void deleteByProvince(String province);

    @Query(ChannelChargingDao.findByOperatorSql)
    List<ChargingView> findByOperator(int operator);
}
