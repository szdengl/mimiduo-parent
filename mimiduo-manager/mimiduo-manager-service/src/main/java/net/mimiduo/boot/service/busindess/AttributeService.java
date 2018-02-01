package net.mimiduo.boot.service.busindess;


import net.mimiduo.boot.pojo.business.Attribute;
import net.mimiduo.boot.service.BaseService;

import java.util.List;


public interface AttributeService extends BaseService<Attribute, Long> {
    List<Attribute> findByType(int type);
}
