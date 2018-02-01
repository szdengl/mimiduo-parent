package net.mimiduo.boot.service.impl;

import java.io.Serializable;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;


import net.mimiduo.boot.dao.BaseDao;
import net.mimiduo.boot.service.BaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springside.modules.persistence.DynamicSpecifications;


/**
 * 数据业务服务根接口实现
 * 
 * @author darren.deng
 *
 * @param <E>
 * @param <ID>
 */
@Transactional
public abstract class BaseServiceImpl<E, ID extends Serializable> extends DynamicSpecifications
		implements BaseService<E, ID> {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	protected static final Specs builtinSpecs = new Specs();

	public static class Specs {
		public <T> Specification<T> parentIsNull() {
			return new Specification<T>() {
				@Override
				public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
					Path<Object> path = root.get("parent");
					return cb.isNull(path);
				}
			};
		}
	}

	public abstract BaseDao<E, ID> getDao();

	@Override
	public List<E> findAll() {
		return getDao().findAll();
	}

	@Override
	public List<E> findAll(Sort sort) {
		return getDao().findAll(sort);
	}

	@Override
	public List<E> findAll(Iterable<ID> ids) {
		return getDao().findAll(ids);
	}

	@Transactional
	@Override
	public <S extends E> List<S> save(Iterable<S> entities) {
		return getDao().save(entities);
	}

	@Override
	public void flush() {
		getDao().flush();
	}

	@Override
	@Transactional
	public <S extends E> S saveAndFlush(S entity) {
		return getDao().saveAndFlush(entity);
	}

	@Transactional
	@Override
	public void deleteInBatch(Iterable<E> entities) {
		getDao().deleteAllInBatch();
	}

	@Transactional
	@Override
	public void deleteAllInBatch() {
		getDao().deleteAllInBatch();
	}

	@Override
	public E getOne(ID id) {
		return getDao().getOne(id);
	}

	@Override
	public Page<E> findAll(Pageable arg0) {
		return getDao().findAll(arg0);
	}

	@Override
	public long count() {
		return getDao().count();
	}

	@Transactional
	@Override
	public void delete(ID arg0) {
		getDao().delete(arg0);
	}

	@Transactional
	@Override
	public void delete(E arg0) {
		getDao().delete(arg0);
	}

	@Transactional
	@Override
	public void delete(Iterable<? extends E> arg0) {
		getDao().delete(arg0);
	}

	@Transactional
	@Override
	public void deleteAll() {
		getDao().deleteAll();
	}

	@Override
	public E findOne(Specification<E> spec) {
		return getDao().findOne(spec);
	}

	@Override
	public List<E> findAll(Specification<E> spec) {
		return getDao().findAll(spec);
	}

	@Override
	public Page<E> findAll(Specification<E> spec, Pageable pageable) {
		return getDao().findAll(spec, pageable);
	}

	@Override
	public List<E> findAll(Specification<E> spec, Sort sort) {
		return getDao().findAll(spec, sort);
	}

	@Override
	public long count(Specification<E> spec) {
		return getDao().count(spec);
	}

	@Transactional
	@Override
	public <S extends E> S save(S entity) {
		return getDao().save(entity);
	}

	@Override
	public E findOne(ID id) {
		return getDao().findOne(id);
	}

	@Override
	public boolean exists(ID id) {
		return getDao().exists(id);
	}

}
