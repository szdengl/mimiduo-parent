package net.mimiduo.boot.service.busindess;


import net.mimiduo.boot.pojo.business.PhoneSegment;
import net.mimiduo.boot.pojo.data.MobileAttribute;
import net.mimiduo.boot.service.BaseService;

public interface PhoneSegmentService extends BaseService<PhoneSegment,String> {

    MobileAttribute setSell(String mobile);

}
