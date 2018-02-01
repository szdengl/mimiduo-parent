package net.mimiduo.boot.web.action;

import java.util.Collection;
import java.util.List;


import net.mimiduo.boot.common.util.ResponseResult;
import net.mimiduo.boot.util.BootstrapUIPage;
import net.mimiduo.boot.util.DataConverters;
import net.mimiduo.boot.util.EasyUIPage;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springside.modules.mapper.JsonMapper;
import org.springside.modules.persistence.SearchFilter;
import org.springside.modules.web.Servlets;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;




public abstract class CommonController extends CurrentUserableController {


	protected final JsonMapper nonEmptyJsonMapper() {
		return JsonMapper.nonEmptyMapper();
	}

	protected final JsonMapper nonDefaultJsonMapper() {
		return JsonMapper.nonDefaultMapper();
	}

	protected final ResponseResult successResult() {
		return ResponseResult.success();
	}

	protected final ResponseResult successResult(String message) {
		return ResponseResult.success(message);
	}

	protected final ResponseResult failureResult() {
		return ResponseResult.failure();
	}

	protected final ResponseResult failureResult(String message) {
		return ResponseResult.failure(message);
	}

	/**
	 * 根据请求数据获取SearchFilters.
	 */
	protected Collection<SearchFilter> getSearchFilters() {
		return SearchFilter.parse(Servlets.getParametersStartingWith(this.getRequest(), "f_")).values();
	}

	/**
	 * 将Page转换为EasyUI的page数据结构对象.
	 * 
	 */
	protected <T> EasyUIPage<T> toEasyUIPage(Page<T> page) {
		return DataConverters.pageToEasyUI(page);
	}

	protected <T> BootstrapUIPage<T> toBootstrapUIPage(Page<T> page) {
		return DataConverters.pageToBootstrapUI(page);
	}

	protected ResponseResult exceptionAsResult(Exception e) {
		return this.failureResult("操作未完成: " + e.getMessage()).message(ExceptionUtils.getRootCauseMessage(e));
	}

	protected Sort getSortFromEasyUIDatagrid() {
		return getSortFromEasyUIDatagridOrElse(null);
	}

	protected Sort getSortFromEasyUIDatagridOrElse(final Sort defaultSort) {
		String sortStr = getStringParameter("sort", "");
		if (Strings.isNullOrEmpty(sortStr)) {
			return defaultSort;
		}

		String orderStr = getStringParameter("order", "");
		List<String> sortFields = Lists.newArrayList(Splitter.on(",").split(sortStr));
		List<String> orderFields = Lists.newArrayList(Splitter.on(",").split(orderStr));
		List<Order> orders = Lists.newArrayList();
		for (int i = 0; i < sortFields.size(); i++) {
			String sort = sortFields.get(i);
			String order = (i < orderFields.size()) ? orderFields.get(i) : null;
			orders.add(new Order(Direction.fromStringOrNull(order), sort));
		}

		return new Sort(orders);
	}

	protected PageRequest getPageRequestFromEasyUIDatagrid() {
		return getPageRequest(getSortFromEasyUIDatagrid());
	}

	protected PageRequest getPageRequestFromEasyUIDatagridWithInitSort(final Sort sort) {
		return getPageRequest(getSortFromEasyUIDatagridOrElse(sort));
	}

	protected PageRequest getPageRequestFromBootStrapDatagridWithInitSort(final Sort sort) {
		return getBootstrapPageRequest(getSortFromEasyUIDatagridOrElse(sort));
	}

	protected PageRequest getBootstrapPageRequest(final Sort sort) {
		return getBootstrapPageRequest(this.getIntParameter("limit", getDefaultPageSize()), sort);
	}

	protected PageRequest getBootstrapPageRequest(final int pageSize, final Sort sort) {
		int offset = this.getIntParameter("offset", 0);
		int page = offset / pageSize;
		return getPageRequest(page, pageSize, sort);
	}

	protected PageRequest getPageRequest(final Sort sort) {
		return getPageRequest(this.getIntParameter("rows", getDefaultPageSize()), sort);
	}

	protected PageRequest getPageRequest(final int pageSize, final Sort sort) {
		int page = this.getIntParameter("page", 0) - 1;
		if (page < 0) {
			page = 0;
		}
		return getPageRequest(page, pageSize, sort);
	}

	protected PageRequest getPageRequest(final int page, final int pageSize, final Sort sort) {
		return new PageRequest(page, pageSize, sort);
	}

	protected int getDefaultPageSize() {
		return 50;
	}

	private final Sort _idAscSort = getFieldAscSort("id");
	private final Sort _idDescSort = getFieldDescSort("id");

	protected final Sort getIdAscSort() {
		return _idAscSort;
	}

	protected final Sort getIdDescSort() {
		return _idDescSort;
	}

	protected final Sort getFieldAscSort(String field) {
		return new Sort(Direction.ASC, field);
	}

	protected final Sort getFieldDescSort(String field) {
		return new Sort(Direction.DESC, field);
	}

	protected final Sort getSort(String field, String order) {
		return new Sort(Direction.fromStringOrNull(order), field);
	}
}
