package net.mimiduo.boot.common.cache;

/**
 * 对象缓存管理接口，用于做本地对象缓存管理工作，推荐一次加载，多次读取的模式。
 * 
 * @version
 */
public interface LafiteCacheManager {
	/**
	 * 缓存一个对象，如果对象已经存在会抛出IllegalStateException
	 * 
	 * @param key
	 * @param data
	 */
	void cache(String key, Object data);

	/**
	 * 缓存一个对象,并设置过期时间（单位秒）
	 * 
	 * @param key
	 * @param data
	 * @param exp
	 */
	void cache(String key, Object data, long exp);

	/**
	 * 从缓存堆里面获取一个对象
	 * 
	 * @param key
	 * @return
	 */
	Object getCache(String key);

	/**
	 * 以泛型方式从缓存堆里面获取一个对象
	 * 
	 * @param key
	 * @param cls
	 * @return
	 */
	<T> T getCache(String key, Class<T> cls);

	/**
	 * 从缓存堆里面移除一个对象
	 * 
	 * @param key
	 * @return
	 */
	Object remove(String key);

	/**
	 * 刷新缓存堆
	 * 
	 */
	void reflush();

	/**
	 * 确认当前缓存堆里面是否存在某个对象
	 * 
	 * @param key
	 * @return
	 */
	boolean exsits(String key);
}
