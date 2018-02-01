package net.mimiduo.boot.service.impl.business;

import com.google.common.collect.Lists;

import net.mimiduo.boot.common.exception.ExecuteException;
import net.mimiduo.boot.dao.BaseDao;
import net.mimiduo.boot.dao.bussiness.ChannelProvinceDao;
import net.mimiduo.boot.pojo.business.ChannelProvince;
import net.mimiduo.boot.service.busindess.ChannelProvinceService;
import net.mimiduo.boot.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class ChannelProvinceServiceImpl extends BaseServiceImpl<ChannelProvince,Long > implements ChannelProvinceService {

    @Autowired
    private ChannelProvinceDao channelProvinceDao;

    @Override
    public BaseDao<ChannelProvince, Long> getDao() {
        return this.channelProvinceDao;
    }

    @Override
    public List<ChannelProvince> findByChannelId(long channelId) {
        return this.channelProvinceDao.findByChannelId(channelId);
    }

    @Override
    public ChannelProvince findByProvinceIdAndChannelId(String province, long channelId) {
        return this.channelProvinceDao.findByProvinceIdAndChannelId(province,channelId);
    }


    @Override
    public List<ChannelProvince> findByProvinceIdAndIsstatusAndStartimeLessThanEqualAndEndtimeGreaterThanEqual(String provinceId,int isstatus,int startime,int endtime){
        List<Sort.Order> list= Lists.newArrayList();
        Sort.Order order1 =new Sort.Order(Sort.Direction.ASC,"orderId");
        list.add(order1);
        Sort.Order order2=new Sort.Order(Sort.Direction.DESC,"channelId");
        list.add(order2);
        Sort sort=new Sort(list);
        return this.channelProvinceDao.findByProvinceIdAndIsstatusAndStartimeLessThanEqualAndEndtimeGreaterThanEqual(provinceId, isstatus, startime, endtime,sort);
    }


    @Override
    @Transactional(rollbackFor= ExecuteException.class)
    public void updateByChannelId(long channelId){
        this.channelProvinceDao.updateByChannel(channelId);
    }

    @Override
    @Transactional(rollbackFor= ExecuteException.class)
    public void deleteByChannelId(long channelId){
        this.channelProvinceDao.deleteByChannelId(channelId);
    }


}
