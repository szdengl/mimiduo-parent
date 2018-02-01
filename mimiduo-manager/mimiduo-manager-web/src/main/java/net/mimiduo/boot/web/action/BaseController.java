package net.mimiduo.boot.web.action;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 控制器基类.
 * 
 */
public abstract class BaseController {


	@Autowired
	private ApplicationContext ctx;

	public static final String REQ_ATTR_CHANNEL_KEY = "channel";

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		registerCustomEidtorsForWebDataBinder(binder);
	}

	/**
	 * 注册自定义类型转换器.
	 * 
	 */
	protected void registerCustomEidtorsForWebDataBinder(WebDataBinder binder) {
		// 日期格式化
		SimpleDateFormat dateFormat = new SimpleDateFormat(getBinderDatePattern());
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}

	/**
	 * 是Ajax请求?
	 */
	protected boolean isAjaxRequest() {
		return "x-requested-with".equals(this.getRequest().getHeader("XMLHttpRequest"));
	}

	/**
	 * 自定义日期绑定格式.
	 */
	protected String getBinderDatePattern() {
		return "yyyy-MM-dd";
	}

	/**
	 * 设置当前channel.
	 */
	protected void setChannel(final String channel) {
		this.getRequest().setAttribute(REQ_ATTR_CHANNEL_KEY, channel);
	}

	/**
	 * 获取ServletRequestAttributes对象.
	 */
	protected final ServletRequestAttributes getServletRequestAttributes() {
		return (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
	}

	/**
	 * 获取HttpServletRequest对象.
	 */
	protected HttpServletRequest getRequest() {
		return getServletRequestAttributes().getRequest();
	}

	protected final String getStringParameter(String name, String defaultValue) {
		return ServletRequestUtils.getStringParameter(getRequest(), name, defaultValue);
	}

	protected final boolean getBooleanParameter(String name, boolean defaultValue) {
		return ServletRequestUtils.getBooleanParameter(getRequest(), name, defaultValue);
	}

	protected final int getIntParameter(String name, int defaultValue) {
		return ServletRequestUtils.getIntParameter(getRequest(), name, defaultValue);
	}

	protected final double getDoubleParameter(String name, double defaultValue) {
		return ServletRequestUtils.getDoubleParameter(getRequest(), name, defaultValue);
	}

	protected String autoView(String name) {
		final Resource resource = ctx.getResource("classpath:/templates/" + name + ".html");
		if (resource != null && resource.exists()) {
			return name;
		}
		return "_" + name;
	}

}
