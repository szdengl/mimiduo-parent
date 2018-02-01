package net.mimiduo.boot.service.busindess;

import net.mimiduo.boot.pojo.business.Visit;
import net.mimiduo.boot.service.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springside.modules.persistence.SearchFilter;
import java.util.Collection;
public interface VisitService extends BaseService<Visit,Long> {
    Page<Visit> findAllVisit(Collection<SearchFilter> searchFilters, Pageable pageable);

}
