package net.mimiduo.boot.configuration;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import net.mimiduo.boot.common.service.AppConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.MultipartConfigElement;
import java.nio.charset.Charset;
import java.util.List;

@Configuration
@ConditionalOnWebApplication
public class WebappConfiguration  extends WebMvcConfigurerAdapter {

    private static Logger LOGGER = LoggerFactory.getLogger(WebappConfiguration.class);

    @Autowired
    private AppConfig appConfig;

//    @Bean
//    public CharacterEncodingFilter characterEncodingFilter() {
//        final CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
//        characterEncodingFilter.setEncoding(Charsets.UTF_8.name());
//        characterEncodingFilter.setForceEncoding(true);
//        return characterEncodingFilter;
//    }
//
//
//    @Bean
//    public StringHttpMessageConverter stringHttpMessageConverter() {
//        return new StringHttpMessageConverter(Charsets.UTF_8);
//    }

    @Bean
    public HttpMessageConverter<String> responseBodyConverter() {
        StringHttpMessageConverter converter = new StringHttpMessageConverter(
                Charset.forName("UTF-8"));
        return converter;
    }

    @Override
    public void configureMessageConverters(
            List<HttpMessageConverter<?>> converters) {
        super.configureMessageConverters(converters);
        converters.add(responseBodyConverter());
    }

    @Override
    public void configureContentNegotiation(
            ContentNegotiationConfigurer configurer) {
        configurer.favorPathExtension(false);
    }

    // @see
    // http://stackoverflow.com/questions/13943439/json-response-download-in-ie710
    @Bean
    public MappingJackson2HttpMessageConverter mappingJacksonHttpMessageConverter() {
        final MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(ImmutableList.of(MediaType.TEXT_HTML, MediaType.APPLICATION_JSON));
        return converter;
    }

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        String maxFileSize = appConfig.getProperty("multipart.max-file-size", "10MB");
        String maxRequestSize = appConfig.getProperty("multipart.max-file-size", "10MB");
        LOGGER.info("multipart.max-file-size: {}, multipart.max-file-size: {}", maxFileSize, maxRequestSize);

        factory.setMaxFileSize(maxFileSize);
        factory.setMaxRequestSize(maxRequestSize);
        return factory.createMultipartConfig();
    }

  

    @Bean
    public Module jodaModule() {
        return new JodaModule();
    }

}
