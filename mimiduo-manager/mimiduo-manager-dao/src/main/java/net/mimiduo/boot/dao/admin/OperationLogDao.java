package net.mimiduo.boot.dao.admin;

import java.util.Date;
import java.util.List;

import net.mimiduo.boot.pojo.admin.OperationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.RepositoryDefinition;


@RepositoryDefinition(domainClass = OperationLog.class, idClass = Long.class)
public interface OperationLogDao extends JpaRepository<OperationLog, Long>, JpaSpecificationExecutor<OperationLog> {

	List<OperationLog> findOperationLogByUserId(Long id);

	List<OperationLog> findOperationLogByUserIdAndOperTimeBetween(Long userId, Date from, Date to);

	List<OperationLog> findOperationLogByUserNameAndOperTimeBetween(String userName, Date from, Date to);
}
