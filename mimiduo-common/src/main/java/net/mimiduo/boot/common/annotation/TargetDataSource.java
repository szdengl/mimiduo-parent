package net.mimiduo.boot.common.annotation;

import java.lang.annotation.*;

/**
 * ${DESCRIPTION}
 *
 * @author:LingDeng
 * @create 2017-11-22 5:15
 **/
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TargetDataSource {
    String value();
}
