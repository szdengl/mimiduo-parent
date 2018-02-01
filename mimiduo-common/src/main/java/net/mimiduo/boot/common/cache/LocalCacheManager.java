package net.mimiduo.boot.common.cache;

import java.util.Hashtable;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.springframework.cache.annotation.EnableCaching;

/**
 * 配置本地缓存管理器，将运行时属性加载到本地内存
 * 
 * @version
 */
@EnableCaching
public class LocalCacheManager extends ConfigCacheManager {
	protected Map<String, Object> cache = new Hashtable<String, Object>();
	protected Timer timer = new Timer();
	protected Map<String, RemveTask> tasks = new Hashtable<String, RemveTask>();

	@Override
	public void cache(String key, Object data) {
		if (key == null || data == null) {
			throw new NullPointerException("key and data both required");
		}
		cache.put(key, data);
		super.cache(key, data);
	}

	@Override
	public void cache(String key, Object data, long exp) {
		try {
			this.cache(key, data);
			if (tasks.containsKey(key)) {
				tasks.get(key).cancel();
				timer.purge();
			}
			RemveTask task = new RemveTask(cache, key);
			timer.schedule(task, exp * 1000l);
			tasks.put(key, task);
		} finally {
			super.cache(key, data, exp);
		}
	}

	@Override
	public void reflush() {
		this.cache.clear();
	}

	@Override
	public Object getCache(String key) {
		return cache.get(key);
	}

	@Override
	public Object remove(String key) {
		try {
			return cache.remove(key);
		} finally {
			super.remove(key);
		}
	}

	@Override
	public boolean exsits(String key) {
		return cache.containsKey(key);
	}

	private static class RemveTask extends TimerTask {
		private Map<String, Object> cache;
		private String key;

		public RemveTask(Map<String, Object> cache, String key) {
			super();
			this.cache = cache;
			this.key = key;
		}

		@Override
		public void run() {
			cache.remove(key);
		}

	}
}
