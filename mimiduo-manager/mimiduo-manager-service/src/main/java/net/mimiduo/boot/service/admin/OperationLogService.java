package net.mimiduo.boot.service.admin;

import java.util.Date;
import java.util.List;

import net.mimiduo.boot.pojo.admin.OperationLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface OperationLogService {

	void save(OperationLog log);

	List<OperationLog> findOperationLogByUserId(Long id);

	List<OperationLog> findOperationLogByUserIdAndOperTimeBetween(Long userId, Date from, Date to);

	List<OperationLog> findOperationLogByUserNameAndOperTimeBetween(String userName, Date from, Date to);

	Page<OperationLog> onSearch(String userName, Long userId, String sysCode, Date start, Date end, Pageable pageable);
}
