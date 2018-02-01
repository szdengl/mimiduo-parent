package net.mimiduo.boot.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import net.mimiduo.boot.common.cache.ConfigCacheManager;
import net.mimiduo.boot.common.cache.LocalCacheManager;

@Configuration
public class RuntimeCacheConfiguration{

	@Bean(initMethod = "reflush")
	public ConfigCacheManager createConfigCacheManager() {
		return new LocalCacheManager();
	}
}
