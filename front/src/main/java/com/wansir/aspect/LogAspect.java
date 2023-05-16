package com.wansir.aspect;

import com.wansir.annotation.SystemLog;
import com.wansir.pojo.entity.Log;
import com.wansir.service.LogService;
import com.wansir.utils.SecurityUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;

/**
 * 系统日志，切面处理类(通知类）
 */
@Aspect
@Component
public class LogAspect {
	@Autowired
	private LogService logService;
	
	//定义切点 切点表达式指向SystemLog注解，我们在业务方法上可以加上log注解，然后所标注
	//的方法都能进行日志记录
	@Pointcut("@annotation(com.wansir.annotation.SystemLog)")
	public void logPointCut() {
		
	}

	/**
	 * 环绕通知：计算执行时间，保存相关信息到日志表中
	 */
	@Around("logPointCut()")
	public Object around(ProceedingJoinPoint point) throws Throwable {
		long beginTime = System.currentTimeMillis();
		//执行方法
		Object result = point.proceed();
		//执行时长(毫秒)
		long time = System.currentTimeMillis() - beginTime;

		//保存日志
		saveLog(point, time);

		return result;
	}

	/**
	 * 保存日志到数据库
	 * @param joinPoint
	 * @param time
	 */
	private void saveLog(ProceedingJoinPoint joinPoint, long time) {
		// 强转：getSignature()返回的是接口对象，无法获取数据
		// 签名：相当于将切入点的所有信息封装为一个对象，通过签名可以获取切入点的具体信息
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();

		Log log = new Log();
		SystemLog systemLog = method.getAnnotation(SystemLog.class);
		if(systemLog != null){
			//注解上的描述
			log.setOperation(systemLog.value());
		}
		//设置用户名
		log.setUserId(SecurityUtils.getUserId());

		//请求的方法名
		String className = joinPoint.getTarget().getClass().getName();
		String methodName = signature.getName();

		log.setMethod(className + "." + methodName + "()");

		//请求的参数
		Object[] args = joinPoint.getArgs();
		try{
			String params = Arrays.toString(args);
			log.setParams(params);
		}catch (Exception e){

		}

		//获取request
		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = requestAttributes.getRequest();

		//设置IP地址
		log.setIp(request.getRemoteAddr());

		log.setTime(time);
		log.setCreateDate(new Date());
		//保存系统日志
		logService.save(log);
	}
}
