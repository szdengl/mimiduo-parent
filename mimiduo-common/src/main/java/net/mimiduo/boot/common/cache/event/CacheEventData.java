package net.mimiduo.boot.common.cache.event;


/**
 * 缓存事件数据模型
 * @version
 */
public class CacheEventData implements EventData {
	/**
	 * 缓存移除事件选择器
	 */
	public static final String TOPIC_REMOVE = "cache.runtimeproperty.remove";
	/**
	 * 缓存刷新事件选择器
	 */
	public static final String TOPIC_REFLUSH = "cache.runtimeproperty.reflush";
	/**
	 * 系统日志级别缓存键
	 */
	public static final String LOGGER_KEY = "application.logger.level";

	private Object key;
	private Object value;

	public CacheEventData() {
		super();
	}

	public CacheEventData(Object key, Object value) {
		super();
		this.key = key;
		this.value = value;
	}

	public Object getKey() {
		return key;
	}

	public void setKey(Object key) {
		this.key = key;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

}
