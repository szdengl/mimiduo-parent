package net.mimiduo.boot.common.service.impl;

import java.util.Arrays;


import net.mimiduo.boot.common.service.AppConfig;
import net.mimiduo.boot.common.service.RuntimePropertyService;
import net.mimiduo.boot.common.util.StrictPropertyResolver;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertyResolver;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.PropertySourcesPropertyResolver;
import org.springframework.stereotype.Service;

import com.google.common.base.Objects;
import com.google.common.base.Strings;



@Service
public class AppConfigImpl implements AppConfig {

    private final PropertyResolver appConfigs;

    private final ConfigurableEnvironment env;

    @Autowired
    public AppConfigImpl(ConfigurableEnvironment env, RuntimePropertyService runtimePropertyService) {
        this.env = env;
        MutablePropertySources propertySources = env.getPropertySources();
        propertySources.addFirst(new RuntimePropertySource("runtimeProperty", runtimePropertyService));
        this.appConfigs = new StrictPropertyResolver(new PropertySourcesPropertyResolver(propertySources),
                AppConfig.PREFIX);
    }

    static class RuntimePropertySource extends PropertySource<RuntimePropertyService> {

        private final RuntimePropertyService runtimePropertyService;

        public RuntimePropertySource(String name, RuntimePropertyService runtimePropertyService) {
            super(name);
            this.runtimePropertyService = runtimePropertyService;
        }

        @Override
        public Object getProperty(String name) {
//            if (StringUtils.startsWith(name, AppConfig.PREFIX)) {
//                return runtimePropertyService.getPropertyByKey(name);
//            }
            return null;
        }
    }

    @Override
    public String getName() {
        return getProperty("name");
    }

    @Override
    public String getVersion() {
        return getProperty("version");
    }

    @Override
    public String getDescription() {
        return getProperty("description");
    }

    @Override
    public String getHost() {
        return nvl(getProperty("host"), "localhost");
    }

    @Override
    public String getPort() {
        return nvl(getProperty("port"), this.env.getProperty("server.port"));
    }

    @Override
    public String getURL() {
        return String.format("http://%s%s", getHost(), getPort().equals("80") ? "" : ":" + getPort());
    }

    @Override
    public String getStartYear() {
        return this.getProperty("startYear");
    }

    @Override
    public boolean isProdMode() {
        return matchProfile(AppConfig.MODE_PROD);
    }

    @Override
    public boolean getSSLEnabled() {
        return appConfigs.getProperty("server.SSLEnabled", Boolean.class, false);
    }

    @Override
    public int getSSLPort() {
        return appConfigs.getProperty("server.sslPort", Integer.class, 8443);
    }

    @Override
    public int getPublicSSLPort() {
        return appConfigs.getProperty("server.public.sslPort", Integer.class, getSSLPort());
    }

    private boolean matchProfile(String profile) {
        String[] profiles = env.getActiveProfiles();
        for (String p : profiles) {
            if (Objects.equal(p, profile)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean containsProperty(String key) {
        return appConfigs.containsProperty(key);
    }

    @Override
    public String getProperty(String key) {
        return appConfigs.getProperty(key);
    }

    @Override
    public String getProperty(String key, String defaultValue) {
        return appConfigs.getProperty(key, defaultValue);
    }

    @Override
    public <T> T getProperty(String key, Class<T> targetType) {
        return appConfigs.getProperty(key, targetType);
    }

    @Override
    public <T> T getProperty(String key, Class<T> targetType, T defaultValue) {
        return appConfigs.getProperty(key, targetType, defaultValue);
    }

    @Override
    public <T> Class<T> getPropertyAsClass(String key, Class<T> targetType) {
        return appConfigs.getPropertyAsClass(key, targetType);
    }

    @Override
    public String getRequiredProperty(String key) throws IllegalStateException {
        return appConfigs.getRequiredProperty(key);
    }

    @Override
    public <T> T getRequiredProperty(String key, Class<T> targetType) throws IllegalStateException {
        return appConfigs.getRequiredProperty(key, targetType);
    }

    @Override
    public String resolvePlaceholders(String text) {
        return appConfigs.resolvePlaceholders(text);
    }

    @Override
    public String resolveRequiredPlaceholders(String text) throws IllegalArgumentException {
        return appConfigs.resolveRequiredPlaceholders(text);
    }

    @Override
    public void display() {
        printlnApplicationInfo();
    }

    // /////////////////////////////////////////////////
    // private
    private static String nvl(String s, String es) {
        return Strings.isNullOrEmpty(s) ? es : s;
    }

    private void printlnApplicationInfo() {
        String jdbcURL = env.getProperty("spring.datasource.url");
        String activeProfiles = Arrays.toString(env.getActiveProfiles());
        String name = getName();
        String version = getVersion();
        String desc = getDescription();
        String host = getHost();
        String port = getPort();
        String url = getURL();
        printlnInfo(" App jdbc url: " + jdbcURL);
        printlnInfo(" ActiveProfiles：" + activeProfiles);
        printlnInfo(" App Name：" + name);
        printlnInfo(" App Version：" + version);
        printlnInfo(" App Description：" + desc);
        printlnInfo(" App Host: " + host);
        printlnInfo(" App Port: " + port);
        printlnInfo(" App URL: " + url);
    }

    private void printlnInfo(String info) {
        System.out.println("**MIMIDUO-WEBAPP-STARTER: " + info);
    }
}
