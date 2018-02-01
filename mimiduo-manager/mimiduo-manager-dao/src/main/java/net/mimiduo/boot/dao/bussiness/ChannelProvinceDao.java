package net.mimiduo.boot.dao.bussiness;


import net.mimiduo.boot.dao.BaseDao;
import net.mimiduo.boot.pojo.business.ChannelProvince;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ChannelProvinceDao extends PagingAndSortingRepository<ChannelProvince, Long>, JpaRepository<ChannelProvince, Long>,
        JpaSpecificationExecutor<ChannelProvince>, BaseDao<ChannelProvince, Long> {

    List<ChannelProvince> findByChannelId(long channelId);

    ChannelProvince findByProvinceIdAndChannelId(String Province, long channelId);

    List<ChannelProvince> findByProvinceIdAndIsstatusAndStartimeLessThanEqualAndEndtimeGreaterThanEqual(String provinceId, int isstatus, int startime, int endtime, Sort sort);

    @Modifying
    @Query("update ChannelProvince set isstatus=1 where channelId=?1")
    void updateByChannel(long channelId);

    @Modifying
    void deleteByChannelId(long channelId);

}
