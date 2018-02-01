package net.mimiduo.boot.service.impl.business;


import net.mimiduo.boot.dao.BaseDao;
import net.mimiduo.boot.dao.bussiness.AttributeDao;
import net.mimiduo.boot.pojo.business.Attribute;
import net.mimiduo.boot.service.busindess.AttributeService;
import net.mimiduo.boot.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AttributeServiceImpl extends BaseServiceImpl<Attribute, Long> implements AttributeService {

    @Autowired
    private AttributeDao attributeDao;

    @Override
    public BaseDao<Attribute, Long> getDao() {
        return attributeDao;
    }

    @Override
    public List<Attribute> findByType(int type) {
        return this.attributeDao.findByType(type);
    }
}
