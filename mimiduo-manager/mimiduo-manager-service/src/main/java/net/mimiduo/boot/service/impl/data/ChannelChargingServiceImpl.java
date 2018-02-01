package net.mimiduo.boot.service.impl.data;


import com.google.common.collect.Lists;
import net.mimiduo.boot.common.util.DateUtils;
import net.mimiduo.boot.dao.BaseDao;
import net.mimiduo.boot.dao.data.ChannelChargingDao;
import net.mimiduo.boot.pojo.business.ChannelCharging;
import net.mimiduo.boot.pojo.view.ChargingDaySummary;
import net.mimiduo.boot.pojo.view.ChargingView;
import net.mimiduo.boot.service.busindess.ChargingService;
import net.mimiduo.boot.service.data.ChannelChargingService;
import net.mimiduo.boot.service.impl.BaseServiceImpl;
import org.hibernate.Criteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.RollbackException;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 * ${DESCRIPTION}
 *
 * @author:LingDeng
 * @create 2017-12-25 10:55
 **/
@Service
public class ChannelChargingServiceImpl extends BaseServiceImpl<ChannelCharging,Long> implements ChannelChargingService {

    @Autowired
    private ChannelChargingDao channelChargingDao;

    @Autowired
    private ChargingService chargingService;

    @Override
    public BaseDao<ChannelCharging, Long> getDao() {
        return this.channelChargingDao;
    }



    @Override
    @Transactional(rollbackFor=Exception.class)
    public void ProcessingData(String province){
        // 开始时间
        this.channelChargingDao.deleteByProvince(province);
        Long begin = System.currentTimeMillis();
        Date starDate = DateUtils.LocalDateToDate(LocalDate.now());
        Date endDate = DateUtils.LocalDateToDate(LocalDate.now().plusDays(1));
        List<ChargingDaySummary> chargingDaySummaryList = chargingService.findByProvinceAndStatusAndCreateDateBetween(province, starDate, endDate);
        List<ChannelCharging> channelChargingList= Lists.newArrayList();
        chargingDaySummaryList.forEach(chargingDaySummary -> {
            ChannelCharging channelCharging = new ChannelCharging();
            channelCharging.setChannelId(chargingDaySummary.getChannelId());
            channelCharging.setProvince(chargingDaySummary.getProvince());
            channelCharging.setSendnums(chargingDaySummary.getSendnums());
            channelCharging.setSuccess(chargingDaySummary.getChargings());
            channelChargingList.add(channelCharging);
        });
        this.channelChargingDao.save(channelChargingList);
        // 结束时间
        Long end = System.currentTimeMillis();
        // 耗时
        System.out.println("数据插入"+province+"花费时间 : " + (end - begin) / 1000 + " s");
    }

    @Override
    public List<ChargingView> findByOperator(int operator){
        return this.channelChargingDao.findByOperator(operator);
    }
}
