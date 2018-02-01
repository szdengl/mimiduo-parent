package net.mimiduo.boot.service.busindess;


import net.mimiduo.boot.pojo.business.Mo;
import net.mimiduo.boot.service.BaseService;

public interface MoService extends BaseService<Mo,Long> {

    Mo findByMobileAndLinkid(String mobile, String linkid);
}
