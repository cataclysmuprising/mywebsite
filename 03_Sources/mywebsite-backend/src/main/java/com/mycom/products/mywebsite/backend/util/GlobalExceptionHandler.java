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
 *	mywebsite-backend - GlobalExceptionHandler.java
 *	Using JRE 1.8.0_121
 *	@author Than Htike Aung {@literal <rage.cataclysm@gmail.com>}
 *	@Since 2017
 * 
 */
package com.mycom.products.mywebsite.backend.util;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.HttpSessionRequiredException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.UnsatisfiedServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.mycom.products.mywebsite.core.bean.BaseBean;
import com.mycom.products.mywebsite.core.exception.BusinessException;
import com.mycom.products.mywebsite.core.exception.ConsistencyViolationException;
import com.mycom.products.mywebsite.core.exception.DuplicatedEntryException;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger errorLogger = Logger.getLogger("errorLogs." + GlobalExceptionHandler.class.getName());

    @ExceptionHandler(HttpSessionRequiredException.class)
    public String handleSessionExpired() {
	return "error/401";
    }

    @ExceptionHandler(AccessDeniedException.class)
    public String handleUnAuthorizeAccess(HttpServletRequest request, Exception e) {
	errorLogger.error(BaseBean.LOG_BREAKER_OPEN);
	errorLogger.error(e.getMessage(), e);
	errorLogger.error(BaseBean.LOG_BREAKER_CLOSE);
	return "error/403";
    }

    @ExceptionHandler({ MissingServletRequestParameterException.class, UnsatisfiedServletRequestParameterException.class,
	    ServletRequestBindingException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleBadRequest(HttpServletRequest request, Exception e) {
	errorLogger.error(BaseBean.LOG_BREAKER_OPEN);
	errorLogger.error(e.getMessage(), e);
	errorLogger.error(BaseBean.LOG_BREAKER_CLOSE);
	return "error/405";
    }

    @ExceptionHandler({ MethodArgumentNotValidException.class, NoSuchMethodException.class, SecurityException.class,
	    HttpRequestMethodNotSupportedException.class })
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public String handleMethodNotAllowed(HttpServletRequest request, Exception e) {
	errorLogger.error(BaseBean.LOG_BREAKER_OPEN);
	errorLogger.error(e.getMessage(), e);
	errorLogger.error(BaseBean.LOG_BREAKER_CLOSE);
	return "error/405";
    }

    @ExceptionHandler({ RuntimeException.class, Exception.class })
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleServerError(Exception e) {
	errorLogger.error(BaseBean.LOG_BREAKER_OPEN);
	errorLogger.error(e.getMessage(), e);
	errorLogger.error(BaseBean.LOG_BREAKER_CLOSE);
	return "error/500";
    }

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleBusinessException(BusinessException e) {
	errorLogger.error(BaseBean.LOG_BREAKER_OPEN);
	errorLogger.error(e.getMessage(), e);
	errorLogger.error(BaseBean.LOG_BREAKER_CLOSE);
	return "error/500";
    }

    @ExceptionHandler(DuplicatedEntryException.class)
    @ResponseStatus(HttpStatus.ALREADY_REPORTED)
    public String handleDuplicatedEntryException(DuplicatedEntryException e) {
	errorLogger.error(BaseBean.LOG_BREAKER_OPEN);
	errorLogger.error(e.getMessage(), e);
	errorLogger.error(BaseBean.LOG_BREAKER_CLOSE);
	return "error/208";
    }

    @ExceptionHandler(ConsistencyViolationException.class)
    @ResponseStatus(HttpStatus.IM_USED)
    public String handleConsistencyViolationException(ConsistencyViolationException e) {
	errorLogger.error(BaseBean.LOG_BREAKER_OPEN);
	errorLogger.error(e.getMessage(), e);
	errorLogger.error(BaseBean.LOG_BREAKER_CLOSE);
	return "error/226";
    }
}