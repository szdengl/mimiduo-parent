package net.mimiduo.boot.service.impl.business;



import net.mimiduo.boot.dao.BaseDao;
import net.mimiduo.boot.dao.bussiness.VisitDao;
import net.mimiduo.boot.pojo.business.Visit;
import net.mimiduo.boot.service.busindess.VisitService;
import net.mimiduo.boot.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springside.modules.persistence.SearchFilter;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Service
public class VisitServiceImpl extends BaseServiceImpl<Visit,Long> implements VisitService {

    @Autowired
    private VisitDao visitDao;

    @Override
    public BaseDao<Visit, Long> getDao() {
        return this.visitDao;
    }

    @Override
    public Page<Visit> findAllVisit(Collection<SearchFilter> searchFilters, Pageable pageable) {
        return visitDao.findAll(bySearchFilter(searchFilters, Visit.class), pageable);
    }
}
