package net.mimiduo.boot.service.impl.business;

import java.util.Collection;
import java.util.List;


import net.mimiduo.boot.dao.BaseDao;
import net.mimiduo.boot.dao.bussiness.ProjectDao;
import net.mimiduo.boot.pojo.business.Project;
import net.mimiduo.boot.service.busindess.ProjectService;
import net.mimiduo.boot.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springside.modules.persistence.SearchFilter;


@Service
public class ProjectServiceImpl extends BaseServiceImpl<Project,Long> implements ProjectService {

	@Autowired
	private ProjectDao projectDao;

	@Override
	public BaseDao<Project, Long> getDao() {
		return this.projectDao;
	}

	@Override
	public Page<Project> findAllTopProjects(Collection<SearchFilter> searchFilters, Pageable pageable) {
		return this.findAll(bySearchFilter(searchFilters, Project.class), pageable);
	}

	@Override
	public Project findByClientIdAndCode(String clientId, String projectId) {
		return this.projectDao.findByClientIdAndCode(clientId, projectId);
	}
	
	@Override
	public List<Project> findByClientId(String clientId) {
		return this.projectDao.findByClientId(clientId);
	}

	
}
