package net.mimiduo.boot.service.data;


import net.mimiduo.boot.pojo.business.ChannelCharging;
import net.mimiduo.boot.pojo.view.ChargingView;
import net.mimiduo.boot.service.BaseService;

import java.util.List;

/**
 * ${DESCRIPTION}
 *
 * @author:LingDeng
 * @create 2017-12-25 10:51
 **/

public interface ChannelChargingService extends BaseService<ChannelCharging, Long> {

    void ProcessingData(String province);

    List<ChargingView> findByOperator(int operator);
}
