package net.mimiduo.boot.service.busindess;


import net.mimiduo.boot.pojo.business.Sell;
import net.mimiduo.boot.pojo.business.SellAccess;
import net.mimiduo.boot.service.BaseService;

public interface SellService extends BaseService<Sell,Long> {

    Sell findByMobile(String mobile);

    Sell findByImsi(String imsi);

    void saveSellAndSellAccess(Sell sell, SellAccess sellAccess);

    Sell updateSellBySegment(Sell sell);
}
