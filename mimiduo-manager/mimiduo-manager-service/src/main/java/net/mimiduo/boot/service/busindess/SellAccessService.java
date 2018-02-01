package net.mimiduo.boot.service.busindess;


import net.mimiduo.boot.pojo.business.Charging;
import net.mimiduo.boot.pojo.business.SellAccess;
import net.mimiduo.boot.pojo.business.Visit;
import net.mimiduo.boot.service.BaseService;

public interface SellAccessService extends BaseService<SellAccess,String> {

    void saveVisitRecord(Visit visit, Charging charging);

    void updateVisitRecord(Charging charging, int number, int price);
}
