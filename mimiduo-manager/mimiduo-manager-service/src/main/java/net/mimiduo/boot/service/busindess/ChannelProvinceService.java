package net.mimiduo.boot.service.busindess;



import net.mimiduo.boot.pojo.business.ChannelProvince;
import net.mimiduo.boot.service.BaseService;

import java.util.List;

public interface ChannelProvinceService extends BaseService<ChannelProvince,Long> {

    List<ChannelProvince> findByChannelId(long channelId);

    ChannelProvince findByProvinceIdAndChannelId(String province, long channelId);

    List<ChannelProvince> findByProvinceIdAndIsstatusAndStartimeLessThanEqualAndEndtimeGreaterThanEqual(String provinceId, int isstatus, int startime, int endtime);

    void updateByChannelId(long channelId);

    void deleteByChannelId(long channelId);

}
