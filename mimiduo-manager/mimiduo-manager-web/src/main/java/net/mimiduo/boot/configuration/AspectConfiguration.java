package net.mimiduo.boot.configuration;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

import net.mimiduo.boot.common.annotation.LogIgnore;
import net.mimiduo.boot.util.LoggerUtil;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.aop.ProxyMethodInvocation;
import org.springframework.context.annotation.Configuration;


/**
 * 日志拦截配置
 * @version
 */
@Aspect
@Configuration
public class AspectConfiguration {
	private static final Logger LOGGER = Logger.getLogger(AspectConfiguration.class);

	@Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping)")  
	public void excudeService() {
	}

	/**
	 * 用时计算日志
	 * @author A61
	 * @param thisJoinPoint
	 * @return
	 * @throws Throwable
	 */
	@Around("excudeService()")
	public Object around(ProceedingJoinPoint thisJoinPoint)
			throws Throwable {
		StringBuilder builder = loggerMessage(thisJoinPoint);
		if(builder==null){
			return thisJoinPoint.proceed();
		}
		builder.append("-- |USE TIME|");
		long time = System.currentTimeMillis();
		Object proceed = thisJoinPoint.proceed();
		builder.append(System.currentTimeMillis()-time);
		LOGGER.debug(builder);
		return proceed;
	}

	private StringBuilder loggerMessage(JoinPoint thisJoinPoint)
			throws NoSuchMethodException {
		try {
			Field field = thisJoinPoint.getClass().getDeclaredField("methodInvocation");
			field.setAccessible(true);
			ProxyMethodInvocation invocation = (ProxyMethodInvocation) field.get(thisJoinPoint);
			Method method = invocation.getMethod();
			if(method.getAnnotation(LogIgnore.class)!=null){
				return null;
			}
			return LoggerUtil.buildLogWebTemplate(method, thisJoinPoint.getArgs(), thisJoinPoint.getTarget());
		} catch (Exception e) {
			LOGGER.error(e);
			StringBuilder builder = new StringBuilder("-- |SERVICE|");
			builder.append(thisJoinPoint);
			builder.append("-- |PARAMETERS|");
			builder.append(Arrays.toString(thisJoinPoint.getArgs()));
			return builder;
		}
	}
	
	/**
	 * 服务调用前日志
	 * @author A61
	 * @param thisJoinPoint
	 * @throws Throwable
	 */
	@Before("excudeService()")
	public void before(JoinPoint thisJoinPoint)
			throws Throwable {
		StringBuilder builder = loggerMessage(thisJoinPoint);
		if(builder!=null){
			builder.append("-- |beggin call|");
			LOGGER.info(builder);
		}
	}
	
	/**
	 * 成功调用后日志
	 * @author A61
	 * @param thisJoinPoint
	 * @throws Throwable
	 */
	@AfterReturning("excudeService()")
	public void afterReturning(JoinPoint thisJoinPoint)
			throws Throwable {
		StringBuilder builder = loggerMessage(thisJoinPoint);
		if(builder!=null){
			builder.append("-- |complete call|");
			LOGGER.info(builder);
		}
	}
	
	/**
	 * 调用异常后日志
	 * @author A61
	 * @param thisJoinPoint
	 * @throws Throwable
	 */
	@AfterThrowing("excudeService()")
	public void afterThrowing(JoinPoint thisJoinPoint)
			throws Throwable {
		StringBuilder builder = loggerMessage(thisJoinPoint);
		if(builder!=null){
			builder.append("-- |call failure|");
			LOGGER.error(builder);
		}
	}
	
	

}
