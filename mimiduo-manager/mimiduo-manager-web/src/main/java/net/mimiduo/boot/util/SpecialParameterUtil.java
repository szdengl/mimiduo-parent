package net.mimiduo.boot.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.web.multipart.MultipartFile;

/****
 * 特殊参数适配器
 * 
 * @author darren.deng
 *
 */
public final class SpecialParameterUtil {
    private SpecialParameterUtil() {
    }

    public static Object match(HttpServletRequest request, HttpServletResponse response, MultipartFile file, Class<?> c)
            throws IOException {
        if (HttpServletRequest.class.isAssignableFrom(c)) {
            return request;
        } else if (HttpServletResponse.class.isAssignableFrom(c)) {
            return response;
        } else if (MultipartFile.class.isAssignableFrom(c)) {
            return file;
        } else if (byte[].class.isAssignableFrom(c) && file != null) {
            return file.getBytes();
        } else if (BufferedImage.class.isAssignableFrom(c) && file != null) {
            ByteArrayInputStream input = new ByteArrayInputStream(file.getBytes());
            return ImageIO.read(input);
        } else if (InputStream.class.isAssignableFrom(c)) {
            return file.getInputStream();
        } else if (Subject.class.isAssignableFrom(c)) {
            return SecurityUtils.getSubject();
        }
        return null;
    }

    public static Object getPrimitive(Class<?> c, String valueObject) {
        if (c.isPrimitive()) {
            if (int.class.equals(c)) {
                return Integer.valueOf(StringUtils.isEmpty(valueObject) ? "0" : valueObject);
            } else if (long.class.equals(c)) {
                return Long.valueOf(StringUtils.isEmpty(valueObject) ? "0L" : valueObject);
            } else if (float.class.equals(c)) {
                return Float.valueOf(StringUtils.isEmpty(valueObject) ? "0" : valueObject);
            } else if (double.class.equals(c)) {
                return Double.valueOf(StringUtils.isEmpty(valueObject) ? "0" : valueObject);
            } else if (boolean.class.equals(c)) {
                return Boolean.valueOf(StringUtils.isEmpty(valueObject) ? "false" : valueObject);
            } else if (byte.class.equals(c)) {
                return Byte.valueOf(StringUtils.isEmpty(valueObject) ? "0" : valueObject);
            } else if (short.class.equals(c)) {
                return Short.valueOf(StringUtils.isEmpty(valueObject) ? "0" : valueObject);
            }
        }
        return null;
    }
    
    public static Class<?> getPrimitiveClass(Class<?> arrayClass){
        String arryaName = arrayClass.getName();
        if(arryaName.endsWith("D")){
            return double.class;
        }else if(arryaName.endsWith("I")){
            return int.class;
        }else if(arryaName.endsWith("J")){
            return long.class;
        }else if(arryaName.endsWith("Z")){
            return boolean.class;
        }else if(arryaName.endsWith("F")){
            return float.class;
        }else if(arryaName.endsWith("S")){
            return short.class;
        }else if(arryaName.endsWith("B")){
            return byte.class;
        }
        return null;
    }
    
    public static boolean isMatch(Class<?> c){
        if(HttpServletRequest.class.isAssignableFrom(c)||
                HttpServletResponse.class.isAssignableFrom(c)||
                MultipartFile.class.isAssignableFrom(c)||
                byte[].class.isAssignableFrom(c)||
                BufferedImage.class.isAssignableFrom(c)||
                InputStream.class.isAssignableFrom(c)||
                Subject.class.isAssignableFrom(c)){
            return true;
        }
        return false;
    }

}
