package net.mimiduo.boot.service.busindess;


import net.mimiduo.boot.pojo.business.Province;
import net.mimiduo.boot.service.BaseService;

public interface ProvinceService extends BaseService<Province, Long> {
    boolean findByName(String name);
}
