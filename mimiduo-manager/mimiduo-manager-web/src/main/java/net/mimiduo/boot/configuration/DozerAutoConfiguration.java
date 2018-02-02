package net.mimiduo.boot.configuration;

import org.dozer.spring.DozerBeanMapperFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;


@Configuration
public class DozerAutoConfiguration {

	@Autowired
	ApplicationContext ctx;

	@Bean
	public DozerBeanMapperFactoryBean dozerBeanMapperFactoryBean() throws IOException {
		DozerBeanMapperFactoryBean ret = new DozerBeanMapperFactoryBean();
		ret.setMappingFiles(ctx.getResources("classpath*:/conf/conf.dozer/*mapping.xml"));
		return ret;
	}
}