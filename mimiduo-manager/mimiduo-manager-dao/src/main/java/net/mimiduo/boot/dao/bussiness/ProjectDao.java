package net.mimiduo.boot.dao.bussiness;

import java.util.List;

import net.mimiduo.boot.dao.BaseDao;
import net.mimiduo.boot.pojo.business.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;


public interface ProjectDao extends  PagingAndSortingRepository<Project, Long>, JpaRepository<Project, Long>,
JpaSpecificationExecutor<Project>, BaseDao<Project, Long> {

	@Query("select p from Project p,Client c where p.clientId=c.id and c.code=?1 and p.code=?2 and p.isstatus=0")
	Project findByClientIdAndCode(String ClientId, String Code);
	
	@Query("select p from Project p,Client c where p.clientId=c.id and p.isstatus=0 and p.clientId=?1")
	List<Project> findByClientId(String ClientId);
}