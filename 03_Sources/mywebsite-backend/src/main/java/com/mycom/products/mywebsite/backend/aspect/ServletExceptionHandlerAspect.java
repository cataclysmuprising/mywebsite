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
 *	mywebsite-backend - ServletExceptionHandlerAspect.java
 *	Using JRE 1.8.0_121
 *	@author Than Htike Aung {@literal <rage.cataclysm@gmail.com>}
 *	@Since 2017
 * 
 */
package com.mycom.products.mywebsite.backend.aspect;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.support.BindingAwareModelMap;

import com.mycom.products.mywebsite.backend.annotation.HandleServletException;
import com.mycom.products.mywebsite.backend.annotation.ValidateEntity;
import com.mycom.products.mywebsite.backend.controller.BaseController.PageMessage;
import com.mycom.products.mywebsite.backend.controller.BaseController.PageMessageStyle;
import com.mycom.products.mywebsite.backend.controller.BaseController.PageMode;
import com.mycom.products.mywebsite.backend.exception.ValidationFailedException;
import com.mycom.products.mywebsite.backend.util.LocalizedMessageResolver;
import com.mycom.products.mywebsite.backend.validator.BaseValidator;
import com.mycom.products.mywebsite.core.bean.BaseBean;
import com.mycom.products.mywebsite.core.exception.BusinessException;
import com.mycom.products.mywebsite.core.exception.ConsistencyViolationException;
import com.mycom.products.mywebsite.core.exception.DuplicatedEntryException;

@Component
@Aspect
@Order(1)
public class ServletExceptionHandlerAspect extends BaseAspect {
    private static final Logger errorLogger = Logger.getLogger("errorLogs." + ServletExceptionHandlerAspect.class.getName());
    @Autowired
    private ApplicationContext appContext;
    @Autowired
    private LocalizedMessageResolver messageSource;

    @Around(value = "methodAnnotatedWithHandleServletException(servletException) && publicMethod() && !initBinderMethod()")
    public String handleExeptionforServletMethods(ProceedingJoinPoint joinPoint, HandleServletException servletException) throws Throwable {
	String pageSuccessReturn = null;
	String errorView = servletException.errorView();
	PageMode pageMode = servletException.pageMode();
	MethodSignature signature = (MethodSignature) joinPoint.getSignature();
	Method method = signature.getMethod();
	ValidateEntity validationMapper = method.getAnnotation(ValidateEntity.class);
	Model model = null;
	Errors errors = null;
	Object validationTarget = null;
	Object[] arguments = joinPoint.getArgs();
	for (Object arg : arguments) {
	    if (arg != null) {
		if (arg instanceof BindingAwareModelMap) {
		    model = (Model) arg;
		}
		if (validationMapper != null && arg instanceof BeanPropertyBindingResult) {
		    errors = (Errors) arg;
		}
		if (validationMapper != null && arg instanceof BaseBean) {
		    validationTarget = arg;
		}
	    }
	}
	try {
	    if (validationMapper != null) {
		BaseValidator validator = appContext.getBean(validationMapper.validator());
		validator.setPageMode(pageMode);
		validator.validate(validationTarget, errors);
		if (errors.hasErrors()) {
		    throw new ValidationFailedException(BaseBean.LOG_PREFIX + "Validation failed for '"
			    + validationTarget.getClass().getSimpleName() + "'." + BaseBean.LOG_SUFFIX);
		}
	    }
	    pageSuccessReturn = (String) joinPoint.proceed();
	} catch (Exception e) {
	    errorLogger.error(BaseBean.LOG_BREAKER_OPEN);
	    errorLogger.error(e.getMessage(), e);
	    if (errorLogger.isDebugEnabled()) {
		if (e instanceof ValidationFailedException) {
		    Map<String, String> validationErrors = new HashMap<>();
		    List<FieldError> errorFields = errors.getFieldErrors();
		    errorFields.forEach(item -> {
			if (!validationErrors.containsKey(item.getField())) {
			    validationErrors.put(item.getField(), item.getDefaultMessage());
			}
		    });
		    validationErrors.entrySet().forEach(entry -> {
			errorLogger.debug(entry.getKey() + " ==> " + entry.getValue());
		    });
		}
	    }
	    errorLogger.error(BaseBean.LOG_BREAKER_CLOSE);
	    if (model != null) {
		model.addAttribute("pageMode", pageMode);
	    }
	    if (e instanceof ValidationFailedException) {
		if (errorView.length() == 0) {
		    errorView = "error/500";
		} else {
		    if (model != null && errors != null) {
			model.addAttribute("pageMode", pageMode);
			Map<String, String> validationErrors = new HashMap<>();
			List<FieldError> errorFields = errors.getFieldErrors();
			errorFields.forEach(item -> {
			    if (!validationErrors.containsKey(item.getField())) {
				validationErrors.put(item.getField(), item.getDefaultMessage());
			    }
			});
			model.addAttribute("validationErrors", validationErrors);
			model.addAttribute("pageMessage",
				new PageMessage("Validation Error",
					messageSource.getMessage("Validation.common.Page.ValidationErrorMessage"),
					PageMessageStyle.ERROR.getValue()));
		    }
		}
	    } else if (e instanceof DuplicatedEntryException) {
		if (errorView.length() == 0) {
		    errorView = "error/208";
		} else {
		    if (model != null) {
			model.addAttribute("pageMessage",
				new PageMessage("Duplicated",
					messageSource.getMessage("Serverity.common.Page.DuplicatedRecordErrorMessage"),
					PageMessageStyle.ERROR.getValue()));
		    }
		}
	    } else if (e instanceof ConsistencyViolationException) {
		if (errorView.length() == 0) {
		    errorView = "error/226";
		} else {
		    if (model != null) {
			model.addAttribute("pageMessage",
				new PageMessage("Rejected",
					messageSource.getMessage("Serverity.common.Page.ConsistencyViolationErrorMessage"),
					PageMessageStyle.ERROR.getValue()));
		    }
		}
	    } else if (e instanceof BusinessException) {
		if (errorView.length() == 0) {
		    errorView = "error/500";
		} else {
		    if (model != null) {
			model.addAttribute("pageMessage",
				new PageMessage("Application Error",
					messageSource.getMessage("Serverity.common.Page.ApplicationErrorMessage"),
					PageMessageStyle.ERROR.getValue()));
		    }
		}
	    } else {
		if (errorView.length() == 0) {
		    errorView = "error/500";
		} else {
		    if (model != null) {
			model.addAttribute("pageMessage", new PageMessage("Server Error",
				messageSource.getMessage("Serverity.common.Page.ServerErrorMessage"), PageMessageStyle.ERROR.getValue()));
		    }
		}
	    }
	}
	if (errorView.length() == 0) {
	    errorView = "error/500";
	}
	return pageSuccessReturn == null ? errorView : pageSuccessReturn;

    }

    @Pointcut("@annotation(servletException)")
    public void methodAnnotatedWithHandleServletException(HandleServletException servletException) {
    }

}
