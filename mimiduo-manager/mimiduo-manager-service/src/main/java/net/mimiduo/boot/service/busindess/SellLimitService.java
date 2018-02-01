package net.mimiduo.boot.service.busindess;


import net.mimiduo.boot.pojo.business.Charging;
import net.mimiduo.boot.pojo.business.Sell;
import net.mimiduo.boot.pojo.business.Visit;

public interface SellLimitService {

    /***
     * 所属的业务是否超限
     * @param sell
     * @param channelID
     * @return
     */
    boolean bChannelLimit(Sell sell, long channelID);

    String  findBySell(Sell sell, Visit visit, Charging charging, StringBuilder log, boolean isNew);
}
