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
 *	mywebsite-backend - DbFilterInvocationSecurityMetadataSource.java
 *	Using JRE 1.8.0_121
 *	@author Than Htike Aung {@literal <rage.cataclysm@gmail.com>}
 *	@Since 2017
 * 
 */
package com.mycom.products.mywebsite.backend.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.context.ApplicationListener;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;

import com.mycom.products.mywebsite.backend.EntryPoint;
import com.mycom.products.mywebsite.core.bean.BaseBean;
import com.mycom.products.mywebsite.core.bean.config.ActionBean;
import com.mycom.products.mywebsite.core.bean.config.ActionBean.ActionType;
import com.mycom.products.mywebsite.core.bean.config.RoleActionBean;
import com.mycom.products.mywebsite.core.exception.BusinessException;
import com.mycom.products.mywebsite.core.service.config.api.RoleActionService;
import com.mycom.products.mywebsite.core.util.AuthorityChangeEvent;
import com.mycom.products.mywebsite.core.util.FetchMode;

public class DbFilterInvocationSecurityMetadataSource
	implements FilterInvocationSecurityMetadataSource, ApplicationListener<AuthorityChangeEvent>, InitializingBean, DisposableBean {
    private static final Logger applicationLogger = Logger
	    .getLogger("applicationLogs." + DbFilterInvocationSecurityMetadataSource.class.getName());
    private static final Logger errorLogger = Logger.getLogger("errorLogs." + DbFilterInvocationSecurityMetadataSource.class.getName());

    @Autowired
    private RoleActionService roleActionService;

    @Autowired
    private EhCacheCacheManager cacheManager;

    private HashMap<String, List<String>> urlRolesMapper;
    private HashMap<String, String> actionUrlMapper;
    private static List<RoleActionBean> roleActions;
    private static Set<ActionBean> mendatoryActions;

    @Override
    public void afterPropertiesSet() throws BusinessException {
	applicationLogger.info(BaseBean.LOG_BREAKER_OPEN);
	applicationLogger.info(BaseBean.LOG_PREFIX + "Initializing roles for specified URLs and Actions" + BaseBean.LOG_SUFFIX);
	initializeRolesAndActions();
	applicationLogger.info(BaseBean.LOG_BREAKER_CLOSE);
    }

    @Override
    public void onApplicationEvent(AuthorityChangeEvent event) {
	applicationLogger.info("*** Authorities were changed by Application ***");
	try {
	    initializeRolesAndActions();
	} catch (BusinessException e) {
	    errorLogger.error("xxxxxx Reloading Roles and related Actions Failed ! xxxxxx");
	    errorLogger.error(e.getMessage(), e);
	}
	applicationLogger.info("----- Reloading Roles and related Actions Finished ! -----");
    }

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
	FilterInvocation fi = (FilterInvocation) object;
	String url = fi.getRequestUrl();
	// applicationLogger.info("Request Url====> " + url);

	List<String> roles = getAssociatedRolesByUrl(url);
	// applicationLogger.info("Url Associated Roles :" + roles);
	if (roles == null) {
	    return null;
	}
	String[] rolesArr = new String[roles.size()];
	rolesArr = roles.toArray(rolesArr);
	return SecurityConfig.createList(rolesArr);
    }

    private void initializeRolesAndActions() throws BusinessException {
	urlRolesMapper = new HashMap<>();
	actionUrlMapper = new HashMap<>();
	HashMap<String, Object> criteria = new HashMap<>();
	criteria.put("module", EntryPoint.MODULE_NAME);
	criteria.put("asPerAction", true);
	roleActions = roleActionService.selectList(criteria, FetchMode.EAGER);
	roleActions.forEach(roleAction -> {
	    String url = roleAction.getAction().getUrl();
	    actionUrlMapper.put(roleAction.getAction().getActionName(), roleAction.getAction().getUrl());
	    if (this.urlRolesMapper.containsKey(url)) {
		List<String> roles = this.urlRolesMapper.get(url);
		if (roleAction.getRole() == null) {
		    roles = new ArrayList<>();
		} else {
		    roles.add(roleAction.getRole().getName());
		}

	    } else {
		if (roleAction.getRole() == null) {
		    this.urlRolesMapper.put(url, new ArrayList<>());
		} else {
		    List<String> roles = new ArrayList<>();
		    roles.add(roleAction.getRole().getName());
		    this.urlRolesMapper.put(url, roles);
		}
	    }
	});
    }

    public List<String> getAssociatedRolesByUrl(String url) {

	List<String> roles = null;
	Supplier<Stream<Entry<String, List<String>>>> streamSupplier = () -> urlRolesMapper.entrySet().parallelStream()
		.filter(entry -> entry.getKey().matches(url));
	if (streamSupplier.get().count() > 0) {
	    roles = streamSupplier.get().flatMap(entry -> entry.getValue().parallelStream()).collect(Collectors.toList());
	    if (roles.size() == 0) {
		roles.add("SYSTEM_ROLE");
	    }
	}
	return roles;
    }

    public List<String> getAssociatedRolesByAction(String actionName) {
	List<String> roles = actionUrlMapper.entrySet().parallelStream().filter(entry -> actionName.equals(entry.getKey()))
		.flatMap(entry -> urlRolesMapper.get(entry.getValue()) == null ? Stream.empty()
			: urlRolesMapper.get(entry.getValue()).parallelStream())
		.collect(Collectors.toList());
	return roles;
    }

    public Set<ActionBean> getMendatoryActionsForApplcation() {
	if (mendatoryActions == null) {
	    mendatoryActions = roleActions.parallelStream().filter(roleAction -> roleAction.getAction().getActionType() == ActionType.MAIN)
		    .map(roleAction -> roleAction.getAction()).collect(Collectors.toSet());
	}
	return mendatoryActions;
    }

    public Set<ActionBean> getActionsOfPage(String page) {
	Predicate<RoleActionBean> pageActionFilter = new Predicate<RoleActionBean>() {
	    @Override
	    public boolean test(RoleActionBean roleAction) {
		ActionBean action = roleAction.getAction();
		return action.getActionType() == ActionType.SUB && action.getPage().equalsIgnoreCase(page);
	    }
	};
	return roleActions.parallelStream().filter(pageActionFilter).map(roleAction -> roleAction.getAction()).collect(Collectors.toSet());
    }

    @Override
    public void destroy() throws Exception {
	applicationLogger.info("xxxxxxxxxxxxxxxxx clear all caches before application was shutting down xxxxxxxxxxxxxxxxx");
	cacheManager.getCacheManager().clearAll();
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
	return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
	return true;
    }

}
