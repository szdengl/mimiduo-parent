package net.mimiduo.boot.service.busindess;

import java.util.Collection;
import java.util.List;


import net.mimiduo.boot.pojo.business.Project;
import net.mimiduo.boot.service.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springside.modules.persistence.SearchFilter;



public interface ProjectService extends BaseService<Project, Long> {
	
	Page<Project> findAllTopProjects(Collection<SearchFilter> searchFilters, Pageable pageable);

	Project findByClientIdAndCode(String clientId, String projectId);

	List<Project> findByClientId(String clientId);



}
