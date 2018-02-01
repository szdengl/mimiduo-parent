package net.mimiduo.boot.common.util;

import static java.lang.String.format;

import org.springframework.core.env.PropertyResolver;
import org.springframework.util.Assert;

/**
 * 严格的属性Resolver.
 * 
 * 
 */
public class StrictPropertyResolver implements PropertyResolver {

    private final PropertyResolver resolver;

    private final String prefix;

    public StrictPropertyResolver(PropertyResolver resolver) {
        this(resolver, null);
    }

    public StrictPropertyResolver(PropertyResolver resolver, String prefix) {
        Assert.notNull(resolver, "PropertyResolver must not be null");
        this.resolver = resolver;
        this.prefix = (prefix == null ? "" : prefix);
    }

    @Override
    public String getRequiredProperty(String key) throws IllegalStateException {
        return getRequiredProperty(key, String.class);
    }

    @Override
    public <T> T getRequiredProperty(String key, Class<T> targetType) throws IllegalStateException {
        T value = getProperty(key, targetType);
        Assert.state(value != null, format("required key [%s] not found", key));
        return value;
    }

    @Override
    public String getProperty(String key) {
        return getProperty(key, String.class, null);
    }

    @Override
    public String getProperty(String key, String defaultValue) {
        return getProperty(key, String.class, defaultValue);
    }

    @Override
    public <T> T getProperty(String key, Class<T> targetType) {
        return getProperty(key, targetType, null);
    }

    @Override
    public <T> T getProperty(String key, Class<T> targetType, T defaultValue) {
        return this.resolver.getProperty(fullKey(key), targetType, defaultValue);
    }

    @Deprecated
    @Override
    public <T> Class<T> getPropertyAsClass(String key, Class<T> targetType) {
        return this.resolver.getPropertyAsClass(fullKey(key), targetType);
    }

    @Override
    public boolean containsProperty(String key) {
        return this.resolver.containsProperty(fullKey(prefix) + key);
    }

    @Override
    public String resolvePlaceholders(String text) {
        return this.resolver.resolvePlaceholders(text);
    }

    @Override
    public String resolveRequiredPlaceholders(String text) throws IllegalArgumentException {
        return this.resolver.resolveRequiredPlaceholders(text);
    }

    private String fullKey(final String key) {
        return prefix + key;
    }
}
