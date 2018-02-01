package net.mimiduo.boot.common.cache;

import java.util.List;

import net.mimiduo.boot.common.cache.event.EventData;
import org.springframework.beans.factory.annotation.Autowired;

import net.mimiduo.boot.common.cache.event.CacheEventData;

import reactor.core.Reactor;
import reactor.event.Event;

/**
 * 运行时属性缓存管理器，加载运行时属性
 * 
 * @version
 */
public abstract class ConfigCacheManager implements LafiteCacheManager {


	@SuppressWarnings("unchecked")
	@Override
	public <T> T getCache(String key, Class<T> cls) {
		return (T) getCache(key);
	}

	@Autowired
	protected Reactor reactor;

	protected void publishEvent(String topic, EventData eventData) {
		reactor.notify(topic, Event.wrap(eventData));
	}

	/**
	 * 实现CacheManager的cache方法，但只是传播缓存事件
	 */
	@Override
	public void cache(String key, Object data) {
		this.publishEvent(CacheEventData.TOPIC_REFLUSH, new CacheEventData(key, data));
	}

	/**
	 * 实现CacheManager的cache方法，但只是传播缓存事件
	 */
	@Override
	public void cache(String key, Object data, long exp) {
		this.publishEvent(CacheEventData.TOPIC_REFLUSH, new CacheEventData(key, data));
	}

	/**
	 * 实现CacheManager的remove方法，但只是传播缓存事件
	 */
	@Override
	public Object remove(String key) {
		this.publishEvent(CacheEventData.TOPIC_REMOVE, new CacheEventData(key, null));
		return null;
	}


}
