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
 *	mywebsite-backend - AjaxExceptionHandlerAspect.java
 *	Using JRE 1.8.0_121
 *	@author Than Htike Aung {@literal <rage.cataclysm@gmail.com>}
 *	@Since 2017
 * 
 */
package com.mycom.products.mywebsite.backend.aspect;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mycom.products.mywebsite.backend.annotation.HandleAjaxException;
import com.mycom.products.mywebsite.backend.controller.BaseController.PageMessage;
import com.mycom.products.mywebsite.backend.controller.BaseController.PageMessageStyle;
import com.mycom.products.mywebsite.backend.util.LocalizedMessageResolver;
import com.mycom.products.mywebsite.core.bean.BaseBean;
import com.mycom.products.mywebsite.core.exception.ConsistencyViolationException;
import com.mycom.products.mywebsite.core.exception.DuplicatedEntryException;

@Component
@Aspect
@Order(1)
public class AjaxExceptionHandlerAspect extends BaseAspect {
    private static final Logger errorLogger = Logger.getLogger("errorLogs." + AjaxExceptionHandlerAspect.class.getName());

    @Autowired
    private LocalizedMessageResolver messageSource;

    @SuppressWarnings("unchecked")
    @Around(value = "methodAnnotatedWithHandleAjaxException(ajaxException) && publicMethod() && !initBinderMethod()")
    public @ResponseBody Map<String, Object> handleExeptionforajaxMethods(ProceedingJoinPoint joinPoint, HandleAjaxException ajaxException)
	    throws Throwable {
	Map<String, Object> response = new HashMap<>();
	try {
	    response = (Map<String, Object>) joinPoint.proceed();
	} catch (Exception e) {
	    errorLogger.error(BaseBean.LOG_BREAKER_OPEN);
	    errorLogger.error(e.getMessage(), e);
	    errorLogger.error(BaseBean.LOG_BREAKER_CLOSE);
	    response.put("status", HttpStatus.BAD_REQUEST);
	    // default title and message
	    String title = "Bad Request";
	    String message = messageSource.getMessage("Serverity.common.Page.AjaxError");

	    if (e instanceof ServletRequestBindingException) {
		title = "";
		message = e.getMessage();
		// send an e-mail to authorized person
	    } else if (e instanceof DuplicatedEntryException) {
		title = "Duplicated";
		message = messageSource.getMessage("Serverity.common.Page.DuplicatedRecordErrorMessage");

	    } else if (e instanceof ConsistencyViolationException) {
		title = "Rejected";
		message = messageSource.getMessage("Serverity.common.Page.ConsistencyViolationErrorMessage");

	    }
	    response.put("pageMessage", new PageMessage(title, message, PageMessageStyle.ERROR.getValue()));
	}
	return response;

    }

    @Pointcut("@annotation(ajaxException)")
    public void methodAnnotatedWithHandleAjaxException(HandleAjaxException ajaxException) {
    }

}
