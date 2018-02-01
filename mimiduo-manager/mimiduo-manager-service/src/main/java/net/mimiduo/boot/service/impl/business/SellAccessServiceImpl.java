package net.mimiduo.boot.service.impl.business;

import com.google.common.base.Optional;
import net.mimiduo.boot.common.domain.VisitStatus;
import net.mimiduo.boot.common.exception.ExecuteException;
import net.mimiduo.boot.common.util.DateUtils;
import net.mimiduo.boot.dao.BaseDao;
import net.mimiduo.boot.dao.bussiness.SellAccessDao;
import net.mimiduo.boot.pojo.business.Charging;
import net.mimiduo.boot.pojo.business.SellAccess;
import net.mimiduo.boot.pojo.business.Visit;
import net.mimiduo.boot.service.busindess.ChargingService;
import net.mimiduo.boot.service.busindess.SellAccessService;
import net.mimiduo.boot.service.busindess.VisitService;
import net.mimiduo.boot.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;

@Service
public class SellAccessServiceImpl extends BaseServiceImpl<SellAccess,String> implements SellAccessService {

    @Autowired
    private SellAccessDao sellAccessDao;

    @Autowired
    private VisitService visitService;

    @Autowired
    private ChargingService chargingService;

    @Override
    public BaseDao<SellAccess, String> getDao() {
        return this.sellAccessDao;
    }

    @Override
    public void saveVisitRecord(Visit visit, Charging charging){
        if (visit.getMobile() != null || !visit.getMobile().trim().equals("")) {
            Optional<SellAccess> sellAccessOptional=Optional.fromNullable(this.findOne(visit.getImsi()));
            SellAccess sellAccess=null;
            if(!sellAccessOptional.isPresent()){
                sellAccess=new SellAccess();
                sellAccess.setImsi(visit.getImsi());
            }else{
                sellAccess=sellAccessOptional.get();
            }
            visit.setCreateDate(DateUtils.LocalDateTimeToDate(LocalDateTime.now()));
            this.visitService.save(visit);
            sellAccess.setVisits(sellAccess.getVisits()+1);
            if (visit.getStatus() == VisitStatus.sendXml.getValue()) {
                charging.setCity(visit.getCity());
                charging.setMobile(visit.getMobile());
                charging.setProvince(visit.getProvince());
                charging.setStatus(VisitStatus.sendXml.getValue());
                charging.setVisitId(visit.getId());
                charging.setCreateDate(DateUtils.LocalDateTimeToDate(LocalDateTime.now()));
                this.chargingService.save(charging);
                sellAccess.setChargings(sellAccess.getChargings()+1);
            }
            sellAccess.setModifiledDate(DateUtils.LocalDateTimeToDate(LocalDateTime.now()   ));
            this.save(sellAccess);
        }

    }

    @Override
    @Transactional(rollbackFor = ExecuteException.class)
    public void updateVisitRecord(Charging charging,int number,int price){
        charging.setStatus(VisitStatus.success.getValue());
        charging.setFee(number*price);
        charging.setUpdateDate(DateUtils.LocalDateTimeToDate(LocalDateTime.now()));
        this.chargingService.save(charging);
        Visit visit=visitService.findOne(charging.getVisitId());
        visit.setStatus(VisitStatus.success.getValue());
        this.visitService.save(visit);
        SellAccess sellAccess=this.findOne(visit.getImsi());
        sellAccess.setSuccess(sellAccess.getSuccess()+1);
        sellAccess.setMoney(sellAccess.getMoney()+(number*price));
        sellAccess.setModifiledDate(DateUtils.LocalDateTimeToDate(LocalDateTime.now()));
        this.save(sellAccess);
    }
}
