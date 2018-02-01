package net.mimiduo.boot.configuration;

import java.io.IOException;
import java.util.Map;

import net.mimiduo.boot.pojo.SessionUser;
import net.mimiduo.boot.service.admin.UserService;
import net.mimiduo.boot.service.impl.admin.AuditorAwareImpl;
import net.mimiduo.boot.service.impl.admin.ShiroDbRealm;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.mgt.RememberMeManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.mgt.WebSecurityManager;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.apache.shiro.web.servlet.Cookie;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.Resource;

import com.google.common.base.Charsets;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.google.common.io.Resources;



@Configuration
@ConditionalOnWebApplication
public class ShiroConfiguration {

    @Bean(name = "NBS")
    public LaiguoBootSecuriy nConvergeBootS() {
        return new LaiguoBootSecuriy();
    }

    @Bean
    public AuditorAwareImpl auditorProvider(UserService userService) {
        return new AuditorAwareImpl(userService);
    }

    class LaiguoBootSecuriy {

        /**
         * 获取当前会话用户.
         */
        public SessionUser getCurrentUser() {
            if (SecurityUtils.getSubject() == null) {
                return null;
            }
            return (SessionUser) SecurityUtils.getSubject().getPrincipal();
        }
    }

    @Configuration
    @ConditionalOnWebApplication
    @Order(Ordered.LOWEST_PRECEDENCE - 100)
    public static class DefaultShiroConfiguration {

        @Bean
        public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
            return new LifecycleBeanPostProcessor();
        }

        /**
         * 用户授权信息Cache, 采用EhCache.
         */
        @Bean
        public EhCacheManager ehCacheManager() {
            EhCacheManager ret = new EhCacheManager();
            ret.setCacheManagerConfigFile("classpath:conf/ehcache/ehcache-shiro.xml");
            return ret;
        }

        @Bean
        public Cookie cookie() {
            SimpleCookie ret = new SimpleCookie("rememberMe");
            ret.setHttpOnly(true);
            ret.setMaxAge(3600 * 24 * 7); // 7 天
            return ret;
        }

        @Bean
        public RememberMeManager rememberMeManager(Cookie cookie) {
            CookieRememberMeManager ret = new CookieRememberMeManager();
            ret.setCipherKey(org.apache.shiro.codec.Base64.decode("4AvVhmFLUs0KTA3Kprsdag=="));
            ret.setCookie(cookie);

            return ret;
        }

        /**
         * Shiro's main business-tier object for web-enabled applications.
         */
        @Bean
        public DefaultWebSecurityManager securityManager(Realm realm, EhCacheManager ehCacheManager,
                RememberMeManager rememberMeManager) {
            DefaultWebSecurityManager ret = new DefaultWebSecurityManager();
            ret.setRealm(realm);
            ret.setCacheManager(ehCacheManager);
            ret.setRememberMeManager(rememberMeManager);

            return ret;
        }

        @Bean
        public Realm shiroRealm(UserService userService) {
            return new ShiroDbRealm(userService);
        }
    }
    
    @Configuration
    @ConditionalOnWebApplication
    @Order(Ordered.LOWEST_PRECEDENCE - 100)
    public static class Filters {

        private static Logger LOGGER = LoggerFactory.getLogger(Filters.class);
        @Autowired
        ApplicationContext ctx;

        
        @Bean
        public AbstractShiroFilter shiroFilter(WebSecurityManager securityManager) throws Exception{
        	ShiroFilterFactoryBean ret = new ShiroFilterFactoryBean();
        	ret.setSecurityManager(securityManager);
            ret.setLoginUrl("/passport/login");
            ret.setSuccessUrl("/passport/dispatch");
            ret.setUnauthorizedUrl("/passport/unauthorized");
            setFilterChainDefinitionMap(ret);
            return (AbstractShiroFilter) ret.getObject();
        }
        
        private void setFilterChainDefinitionMap(ShiroFilterFactoryBean ret) {
            Map<String, String> filterChainDefinitionMap = Maps.newLinkedHashMap();
            //shiro rul过滤参考
            //http://blog.csdn.net/hxpjava1/article/details/7035724
            filterChainDefinitionMap.put("/", "anon");
            filterChainDefinitionMap.put("/assets/**", "anon");
            filterChainDefinitionMap.put("/static/**", "anon");
            filterChainDefinitionMap.put("/imgFile/**", "anon");
            filterChainDefinitionMap.put("/public/**", "anon");
            filterChainDefinitionMap.put("/favicon.ico", "anon");
            filterChainDefinitionMap.put("/usercode/**", "anon");
            filterChainDefinitionMap.put("/passport/register/**", "anon");
            filterChainDefinitionMap.put("/passport/logout", "logout");
            filterChainDefinitionMap.put("/passport/dispatch", "user");

            final Resource resource = ctx.getResource("classpath:/conf/acl.conf");

            if (resource.exists()) {
                LOGGER.info("load classpath:/conf/acl.properties");
                try {
                    loadFromConfig(resource, filterChainDefinitionMap);
                } catch (Exception e) {
                    throw new RuntimeException("读取acl.properties出错!", e);
                }
            } else {
                LOGGER.warn("classpath:/conf/acl.properties don't exists. use defaults");
                filterChainDefinitionMap.put("/examples/**", "anon");
            }

            //filterChainDefinitionMap.put("/manage/**", sslExpr() + ",roles[管理员]");
            filterChainDefinitionMap.put("/api/**", "anon");
            filterChainDefinitionMap.put("/**", "user");
            filterChainDefinitionMap.put("/admin/**", "user");
            ret.setFilterChainDefinitionMap(filterChainDefinitionMap);
        }

        private void loadFromConfig(Resource resource, Map<String, String> filterChainDefinitionMap) throws IOException {
            Iterable<String> lines = Iterables.filter(Resources.readLines(resource.getURL(), Charsets.UTF_8),
                    new Predicate<String>() {
                        @Override
                        public boolean apply(String input) { // 去掉注释行
                            return !input.trim().startsWith("#");
                        }
                    });

            for (String line : lines) {
                if (!line.contains("=")) { // 去掉空行
                    continue;
                }

                String[] arr = line.split("=");
                if (arr.length != 2) {
                    throw new RuntimeException("acl.conf格式不合法!");
                }
                String key = arr[0].trim();
                String value = arr[1].trim();
                LOGGER.info("{}={}", key, value);

                filterChainDefinitionMap.put(key, value);
            }
        }
    }
}
