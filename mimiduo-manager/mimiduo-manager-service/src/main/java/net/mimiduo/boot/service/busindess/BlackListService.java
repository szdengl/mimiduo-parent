package net.mimiduo.boot.service.busindess;


import net.mimiduo.boot.pojo.business.BlackList;
import net.mimiduo.boot.service.BaseService;

public interface BlackListService extends BaseService<BlackList, Long> {

    /**
     * 这个号码是否黑名单
     * @param mobile
     * @return
     */
    boolean isBlackByMobile(String mobile);

}
