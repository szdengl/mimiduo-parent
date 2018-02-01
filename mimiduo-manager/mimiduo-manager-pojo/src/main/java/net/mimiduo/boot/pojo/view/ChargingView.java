package net.mimiduo.boot.pojo.view;


/**
 * ${DESCRIPTION}
 *
 * @author:LingDeng
 * @create 2017-12-25 19:46
 **/
public interface ChargingView  extends ChargingDaySummary{
    String getCompany();
    String getChannelName();
    String getServiceType();
    int getPrice();
    int getOrderId();
}
