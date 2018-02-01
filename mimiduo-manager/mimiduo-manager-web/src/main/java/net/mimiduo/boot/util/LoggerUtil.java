package net.mimiduo.boot.util;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.fasterxml.jackson.annotation.JsonIgnore;
import net.mimiduo.boot.common.annotation.ActionMethod;


/****
 * 
 * 制作打印服务处理日志
 * 
 * @author darren.deng
 *
 */
public final class LoggerUtil {
    private LoggerUtil() {
    }

    public static StringBuilder buildLogTemplate(Method method, Object[] parameters, Object target) {
        ActionMethod actionMethod = method.getAnnotation(ActionMethod.class);
        String serviceName = actionMethod == null ? "未知服务" : actionMethod.serviceName();
        String cmd = actionMethod == null ? "" : actionMethod.value();
        StringBuilder sb = new StringBuilder("|CMD| ");
        sb.append(cmd);
        sb.append("-- |SERVICE| ");
        sb.append(serviceName);
        sb.append("(");
        sb.append(method.getDeclaringClass().getName());
        sb.append(".");
        sb.append(method.getName());
        sb.append(")");
        sb.append("-- |PARAMETERS| ");
        List<Object> params = new ArrayList<Object>(3);
        for (Object object : parameters) {
            if (object == null) {
                params.add(null);
                continue;
            }
            Class<?> c = object.getClass();
            if (SpecialParameterUtil.isMatch(c)) {
                continue;
            }
            if (c.isPrimitive()) {
                params.add(object);
                continue;
            }
            if (Number.class.isAssignableFrom(c) || String.class.isAssignableFrom(c)
                    || Boolean.class.isAssignableFrom(c)) {
                params.add(object);
                continue;
            }
            if (object.getClass().isArray()) {
                params.add(JSON.parseArray(object.toString(),object.getClass()));
            } else if (c.toString().startsWith("class com.mimiduo")) {
                params.add(JSON.parseObject(object.toString()));
            } else {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("unConvertable", c.toString());
                params.add(jsonObject);
            }
        }
        sb.append(Arrays.toString(params.toArray()));
        return sb;
    }
    
    
    public static StringBuilder buildLogWebTemplate(Method method, Object[] parameters, Object target) {
        //ActionMethod actionMethod = method.getAnnotation(ActionMethod.class);
       // String serviceName = actionMethod == null ? "未知服务" : actionMethod.serviceName();
        StringBuilder sb = new StringBuilder();
        sb.append("-- |SERVICE| 方法名");
       // sb.append(serviceName);
        sb.append("(");
        sb.append(method.getDeclaringClass().getName());
        sb.append(".");
        sb.append(method.getName());
        sb.append(")");
        sb.append("-- |PARAMETERS| ");
        List<Object> params = new ArrayList<Object>(3);
        for (Object object : parameters) {
            if (object == null) {
                params.add(null);
                continue;
            }
            Class<?> c = object.getClass();
            if (SpecialParameterUtil.isMatch(c)) {
                continue;
            }
            if (c.isPrimitive()) {
                params.add(object);
                continue;
            }
            if (Number.class.isAssignableFrom(c) || String.class.isAssignableFrom(c)
                    || Boolean.class.isAssignableFrom(c)) {
                params.add(object);
                continue;
            }
            if (object.getClass().isArray()) {
                params.add(JSON.parseArray(object.toString()));
            } else if (c.toString().startsWith("class net.mimiduo")) {
                params.add(JSON.parseObject(object.toString()));
            } else {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("unConvertable", c.toString());
                params.add(jsonObject);
            }
        }
        sb.append(Arrays.toString(params.toArray()));
        return sb;
    }
}
