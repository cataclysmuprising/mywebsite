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
 *	mywebsite-backend - BaseController.java
 *	Using JRE 1.8.0_121
 *	@author Than Htike Aung {@literal <rage.cataclysm@gmail.com>}
 *	@Since 2017
 * 
 */
package com.mycom.products.mywebsite.backend.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mycom.products.mywebsite.backend.EntryPoint;
import com.mycom.products.mywebsite.backend.security.DbFilterInvocationSecurityMetadataSource;
import com.mycom.products.mywebsite.backend.util.LocalizedMessageResolver;
import com.mycom.products.mywebsite.core.bean.config.LoggedUserBean;
import com.mycom.products.mywebsite.core.exception.BusinessException;

@Controller
public abstract class BaseController {

    public enum PageMode {
	VIEW, CREATE, EDIT, DELETE, PROFILE;
    }

    public static class PageMessage {
	private String title;
	private String message;
	private String style;

	public PageMessage(String title, String message, String style) {
	    this.title = title;
	    this.message = message;
	    this.style = style;
	}

	public String getTitle() {
	    return title;
	}

	public String getMessage() {
	    return message;
	}

	public String getStyle() {
	    return style;
	}

    }

    public enum PageMessageStyle {
	DEFAULT("default"), INFO("info"), SUCCESS("success"), WARNING("warning"), ERROR("error");

	private String value;

	PageMessageStyle(String value) {
	    this.value = value;
	}

	public String getValue() {
	    return this.value;
	}
    }

    @Autowired
    private DbFilterInvocationSecurityMetadataSource securityMetaData;

    @Autowired
    private LocalizedMessageResolver messageSource;

    protected LoggedUserBean loginUser;

    @ModelAttribute
    public void init(Model model, HttpServletRequest request) throws BusinessException {
	Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	if (principal instanceof LoggedUserBean) {
	    loginUser = (LoggedUserBean) principal;
	} else {
	    loginUser = null;
	}
	if (loginUser != null) {
	    model.addAttribute("loginUserName", loginUser.getRealName());
	    model.addAttribute("loginUserId", loginUser.getId());
	    model.addAttribute("contentId", loginUser.getContentId());
	    model.addAttribute("since", loginUser.getRegisterDate());
	}
	model.addAttribute("projectVersion", EntryPoint.getProjectVersion());
	model.addAttribute("isProduction", EntryPoint.isProductionMode());
	// Add the mendatory actions for the application
	securityMetaData.getMendatoryActionsForApplcation().forEach(action -> {
	    String actionName = action.getActionName();
	    model.addAttribute(actionName, getAccessRolesForAction(actionName));
	});
	subInit(model);
    }

    protected void setAuthorities(Model model, String page) throws BusinessException {
	// applicationLogger.info("----------------------------------------------------------------------");
	if (loginUser == null) {
	    return;
	}
	Collection<? extends GrantedAuthority> authorities = loginUser.getAuthorities();
	Iterator<? extends GrantedAuthority> itr = authorities.iterator();
	List<String> loginUserRoles = new ArrayList<>();
	while (itr.hasNext()) {
	    loginUserRoles.add(itr.next().getAuthority());
	}
	// applicationLogger.info(BaseBean.LOG_PREFIX + "User '" +
	// loginUser.getRealName() + "' own " + loginUserRoles + " Roles." +
	// BaseBean.LOG_SUFFIX);
	model.addAttribute("page", page.toLowerCase());
	// appLogger.info("User '" + loginUser.getRealName() + "' request '" +
	// WordUtils.capitalize(page) + "' related informations.");

	HashMap<String, Boolean> accessments = new HashMap<>();
	securityMetaData.getActionsOfPage(page).forEach(action -> {
	    String actionName = action.getActionName();
	    String accessRoles = getAccessRolesForAction(action.getActionName());
	    for (String role : loginUserRoles) {
		if (accessRoles.contains("'" + role + "'")) {
		    accessments.put(actionName, true);
		    break;
		} else {
		    accessments.put(actionName, false);
		}
	    }
	    model.addAttribute(actionName, accessRoles);
	});
	model.addAttribute("accessments", accessments);
	// applicationLogger.info(BaseBean.LOG_PREFIX + "User '" +
	// loginUser.getRealName() + "' has authorities for " + accessments +
	// BaseBean.LOG_SUFFIX);
	// applicationLogger.info("----------------------------------------------------------------------");
    }

    private String getAccessRolesForAction(String actionName) {
	List<String> roles = securityMetaData.getAssociatedRolesByAction(actionName);
	String accessRoles = "";
	if (roles != null) {
	    for (int i = 0; i < roles.size(); i++) {
		if (roles.get(i) != null) {
		    accessRoles += "'" + roles.get(i) + "'";
		    if (i + 1 != roles.size()) {
			accessRoles += ",";
		    }
		}
	    }
	    // appLogger.info("Access Roles for action [" + actionName + "] ===
	    // > " + accessRoles);
	}
	return accessRoles;
    }

    protected String getPureString(String input) {
	return input.replaceAll("[^a-zA-Z0-9> \\/\\-\\.]+", "").trim();
    }

    protected void setFormFieldErrors(Errors errors, Model model, PageMode pageMode) {
	model.addAttribute("pageMode", pageMode);
	Map<String, String> validationErrors = new HashMap<>();
	List<FieldError> errorFields = errors.getFieldErrors();
	errorFields.forEach(item -> {
	    if (!validationErrors.containsKey(item.getField())) {
		validationErrors.put(item.getField(), item.getDefaultMessage());
	    }
	});
	model.addAttribute("validationErrors", validationErrors);
	setPageMessage(model, "Validation Error", "Validation.common.Page.ValidationErrorMessage", PageMessageStyle.ERROR);
    }

    protected Map<String, Object> setAjaxFormFieldErrors(Errors errors, String errorKeyPrefix) {
	Map<String, Object> response = new HashMap<>();
	Map<String, String> fieldErrors = new HashMap<>();
	response.put("status", HttpStatus.METHOD_NOT_ALLOWED);

	List<FieldError> errorFields = errors.getFieldErrors();
	errorFields.forEach(item -> {
	    if (!fieldErrors.containsKey(item.getField())) {
		if (errorKeyPrefix != null) {
		    fieldErrors.put(errorKeyPrefix + item.getField(), item.getDefaultMessage());
		} else {
		    fieldErrors.put(item.getField(), item.getDefaultMessage());
		}
	    }
	});
	response.put("fieldErrors", fieldErrors);
	response.put("type", "validationError");
	setAjaxPageMessage(response, "Validation Error", "Validation.common.Page.ValidationErrorMessage", PageMessageStyle.ERROR);
	return response;
    }

    protected void setPageMessage(Model model, String messageTitle, String messageCode, PageMessageStyle style, Object... messageParams) {
	model.addAttribute("pageMessage",
		new PageMessage(messageTitle, messageSource.getMessage(messageCode, messageParams), style.getValue()));
    }

    protected void setPageMessage(RedirectAttributes redirectAttributes, String messageTitle, String messageCode, PageMessageStyle style,
	    Object... messageParams) {
	redirectAttributes.addFlashAttribute("pageMessage",
		new PageMessage(messageTitle, messageSource.getMessage(messageCode, messageParams), style.getValue()));
    }

    protected Map<String, Object> setAjaxPageMessage(Map<String, Object> response, String messageTitle, String messageCode,
	    PageMessageStyle style, Object... messageParams) {
	if (response == null) {
	    response = new HashMap<>();
	}
	response.put("pageMessage", new PageMessage(messageTitle, messageSource.getMessage(messageCode, messageParams), style.getValue()));
	return response;
    }

    // http://stackoverflow.com/questions/23699371/java-8-distinct-by-property
    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
	Map<Object, Boolean> seen = new ConcurrentHashMap<>();
	return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    public abstract void subInit(Model model) throws BusinessException;
}
