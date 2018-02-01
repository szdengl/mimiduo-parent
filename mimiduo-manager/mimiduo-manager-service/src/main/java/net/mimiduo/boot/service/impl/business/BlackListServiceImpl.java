package net.mimiduo.boot.service.impl.business;

import com.google.common.base.Optional;
import net.mimiduo.boot.dao.BaseDao;
import net.mimiduo.boot.dao.bussiness.BlackListDao;
import net.mimiduo.boot.pojo.business.BlackList;
import net.mimiduo.boot.service.busindess.BlackListService;
import net.mimiduo.boot.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BlackListServiceImpl extends BaseServiceImpl<BlackList, Long> implements BlackListService {

    @Autowired
    private BlackListDao blackListDao;

    @Override
    public BaseDao<BlackList, Long> getDao() {
        return this.blackListDao;
    }

    /**
     * 这个号码是否黑名单
     *
     * @param mobile
     * @return
     */
    @Override
    public boolean isBlackByMobile(String mobile) {
        boolean bResult=false;
        Optional<BlackList> blackListOptional=Optional.fromNullable(this.blackListDao.findByMobile(mobile));
        if(blackListOptional.isPresent()){
            bResult=true;
        }
        return bResult;
    }
}
