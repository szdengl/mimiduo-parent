package net.mimiduo.boot.service.impl.admin;

import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import net.mimiduo.boot.common.cache.LafiteCacheManager;
import net.mimiduo.boot.common.domain.LogRscRecordStatus;
import net.mimiduo.boot.dao.admin.OperationLogDao;
import net.mimiduo.boot.pojo.admin.OperationLog;
import net.mimiduo.boot.service.admin.OperationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;



@Service("operationLogService")
public class OperationLogServiceImpl implements OperationLogService {

	private static final String LOGCACHEKEY = "application.oper_log";

	     @Autowired
	private LafiteCacheManager cacheManager;

	@Autowired
	private OperationLogDao operationLogDao;

	@Override
	public void save(OperationLog log) {
		Object obj = cacheManager.getCache(LOGCACHEKEY);
		if (null != obj && !obj.toString().isEmpty()
				&& Integer.parseInt(obj.toString()) == LogRscRecordStatus.RECORD.getValue()) {
			operationLogDao.save(log);
		}
	}

	@Override
	public List<OperationLog> findOperationLogByUserId(Long id) {
		return operationLogDao.findOperationLogByUserId(id);
	}

	@Override
	public List<OperationLog> findOperationLogByUserIdAndOperTimeBetween(Long userId, Date from, Date to) {

		return operationLogDao.findOperationLogByUserIdAndOperTimeBetween(userId, from, to);
	}

	@Override
	public List<OperationLog> findOperationLogByUserNameAndOperTimeBetween(String userName, Date from, Date to) {

		return operationLogDao.findOperationLogByUserNameAndOperTimeBetween(userName, from, to);
	}

	@Override
	public Page<OperationLog> onSearch(final String userName, final Long userId, final String sysCode, final Date start,
			final Date end, Pageable pageable) {
		Specification<OperationLog> specification = new Specification<OperationLog>() {

			@Override
			public Predicate toPredicate(Root<OperationLog> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = Lists.newArrayList();
				converSearch(predicates, userName, userId, sysCode, start, end, root, query, cb);
				if (predicates.size() > 0) {
					return cb.and(predicates.toArray(new Predicate[predicates.size()]));
				}
				return cb.conjunction();
			}
		};
		return operationLogDao.findAll(specification, pageable);
	}

	protected List<Predicate> converSearch(List<Predicate> predicates, String userName, Long userId, String syscode,
			Date from, Date to, Root<OperationLog> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
		if (userId != null && !userId.equals("")) {
			Path<Long> userIdExp = root.get("userId");
			predicates.add(cb.equal(userIdExp, userId));
		}

		if (userName != null && !userName.equals("")) {
			Path<String> userNameExp = root.get("userName");
			String param = "%" + userName + "%";
			predicates.add(cb.like(userNameExp, param));
		}

		if (from != null) {
			Path<Date> operTimeExp = root.get("operTime");
			predicates.add(cb.greaterThanOrEqualTo(operTimeExp, from));
		}
		if (to != null) {
			Path<Date> operTimeExp = root.get("operTime");
			predicates.add(cb.lessThanOrEqualTo(operTimeExp, to));
		}
		return predicates;
	}

}
