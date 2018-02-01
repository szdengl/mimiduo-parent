package net.mimiduo.boot.service.impl.business;




import net.mimiduo.boot.dao.BaseDao;
import net.mimiduo.boot.dao.bussiness.IvrDao;
import net.mimiduo.boot.pojo.business.Ivr;
import net.mimiduo.boot.service.busindess.IvrService;
import net.mimiduo.boot.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IvrServiceImpl extends BaseServiceImpl<Ivr,Long> implements IvrService {

    @Autowired
    private IvrDao ivrDao;

    @Override
    public BaseDao<Ivr, Long> getDao() {
        return this.ivrDao;
    }
}
