package net.mimiduo.boot.service.impl.business;



import net.mimiduo.boot.dao.BaseDao;
import net.mimiduo.boot.dao.bussiness.MoDao;
import net.mimiduo.boot.pojo.business.Mo;
import net.mimiduo.boot.service.busindess.MoService;
import net.mimiduo.boot.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MoServiceImpl extends BaseServiceImpl<Mo,Long> implements MoService {

    @Autowired
    private MoDao moDao;

    @Override
    public BaseDao<Mo, Long> getDao() {
        return this.moDao;
    }

    @Override
    public Mo findByMobileAndLinkid(String mobile, String linkid) {
        return this.moDao.findByMobileAndLinkid(mobile,linkid);
    }
}
