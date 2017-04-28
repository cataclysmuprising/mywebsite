/*
 * This source file is free software, available under the following license: MIT license. 
 * Copyright (c) 2017, Than Htike Aung
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 * 
 *	mywebsite-backend - LoggingAspect.java
 *	Using JRE 1.8.0_121
 *	@author Than Htike Aung {@literal <rage.cataclysm@gmail.com>}
 *	@Since 2017
 * 
 */
package com.mycom.products.mywebsite.backend.aspect;

import java.lang.reflect.Method;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mycom.products.mywebsite.backend.annotation.Loggable;

@Component
@Aspect
@Order(0)
public class LoggingAspect extends BaseAspect {
    private static final Logger applicationLogger = Logger.getLogger("applicationLogs." + LoggingAspect.class.getName());

    @Before(value = "classAnnotatedWithLoggable(loggable) && publicMethod() && !initBinderMethod()")
    public void beforeMethod(JoinPoint joinPoint, Loggable loggable) throws Throwable {
	// get RequestMapping annotation of target class
	Object[] arguments = joinPoint.getArgs();
	applicationLogger.info(LOG_BREAKER_OPEN);
	MethodSignature signature = (MethodSignature) joinPoint.getSignature();
	Method method = signature.getMethod();
	// get RequestMapping annotation of target class
	RequestMapping rootMapper = joinPoint.getTarget().getClass().getAnnotation(RequestMapping.class);
	RequestMapping methodMapper = method.getAnnotation(RequestMapping.class);
	// skip for non-servlet request methods.
	if (rootMapper == null || methodMapper == null) {
	    return;
	}
	// get class level request mapping URL
	String rootMappingURL = rootMapper.value()[0];
	// get Class Name
	String className = joinPoint.getTarget().getClass().getSimpleName();

	String mappingURL = rootMappingURL;
	if (methodMapper.value().length > 0) {
	    mappingURL += methodMapper.value()[0];
	}
	// set the request method
	String requestMethod = "";
	if (methodMapper.method().length == 0) {
	    requestMethod = RequestMethod.GET.name();
	} else {
	    requestMethod = methodMapper.method()[0].name();
	}
	// create for method String
	String methodName = joinPoint.getSignature().getName();
	StringBuffer sbMethod = new StringBuffer();
	sbMethod.append(" '" + methodName + "'");
	sbMethod.append(" Method [");
	sbMethod.append("mappingURL ('" + mappingURL + "') ,");
	sbMethod.append("requestMethod ('" + requestMethod + "') ");
	sbMethod.append("] ");
	applicationLogger.info(LOG_PREFIX + "Client request for" + sbMethod.toString() + " of '" + className + "' class." + LOG_SUFFIX);
	if (applicationLogger.isDebugEnabled()) {
	    applicationLogger.debug("==================== Request parameters ===========================");
	    if (arguments.length > 0) {
		for (Object arg : arguments) {
		    if (arg != null) {
			applicationLogger.debug(LOG_PREFIX + arg.toString() + LOG_SUFFIX);
		    }
		}
	    } else {
		applicationLogger.debug(LOG_PREFIX + "[EMPTY Request parameters]" + LOG_SUFFIX);
	    }
	    applicationLogger.debug("===================================================================");
	}
    }

    @AfterReturning(value = "classAnnotatedWithLoggable(loggable) && publicMethod() && !initBinderMethod()", returning = "serverResponse")
    public void afterReturnMethod(JoinPoint joinPoint, Loggable loggable, Object serverResponse) throws Throwable {
	MethodSignature signature = (MethodSignature) joinPoint.getSignature();
	Method method = signature.getMethod();
	// get RequestMapping annotation of target class
	RequestMapping rootMapper = joinPoint.getTarget().getClass().getAnnotation(RequestMapping.class);
	RequestMapping methodMapper = method.getAnnotation(RequestMapping.class);
	// skip for non-servlet request methods.
	if (rootMapper == null || methodMapper == null) {
	    return;
	}
	// get class level request mapping URL
	String rootMappingURL = rootMapper.value()[0];
	// get Class Name
	String className = joinPoint.getTarget().getClass().getSimpleName();

	String mappingURL = rootMappingURL;
	if (methodMapper.value().length > 0) {
	    mappingURL += methodMapper.value()[0];
	}
	// set the request method
	String requestMethod = "";
	if (methodMapper.method().length == 0) {
	    requestMethod = RequestMethod.GET.name();
	} else {
	    requestMethod = methodMapper.method()[0].name();
	}
	// create for method String
	String methodName = joinPoint.getSignature().getName();
	StringBuffer sbMethod = new StringBuffer();
	sbMethod.append(" '" + methodName + "'");
	sbMethod.append(" Method [");
	sbMethod.append("mappingURL ('" + mappingURL + "') ,");
	sbMethod.append("requestMethod ('" + requestMethod + "') ");
	sbMethod.append("] ");
	ResponseBody responeBodyAnnotation = method.getAnnotation(ResponseBody.class);
	if (responeBodyAnnotation != null || mappingURL.contains("api/")) {
	    applicationLogger.info(
		    LOG_PREFIX + "Ajax Request : ==> " + sbMethod.toString() + " of '" + className + "' class has been done." + LOG_SUFFIX);
	    if (applicationLogger.isDebugEnabled()) {
		applicationLogger.debug("==================== Ajax Response from server ====================");
		applicationLogger.debug(serverResponse);
		applicationLogger.debug("===================================================================");
	    }
	} else {
	    applicationLogger.info(LOG_PREFIX + "Servlet Request : ==> " + sbMethod.toString() + " of '" + className
		    + "' class has been successfully initiated from server and navigate to new Tiles view [" + serverResponse + "]"
		    + LOG_SUFFIX);
	}
	applicationLogger.info(LOG_BREAKER_CLOSE);
    }

    @Pointcut("@within(loggable)")
    public void classAnnotatedWithLoggable(Loggable loggable) {
    }

}
