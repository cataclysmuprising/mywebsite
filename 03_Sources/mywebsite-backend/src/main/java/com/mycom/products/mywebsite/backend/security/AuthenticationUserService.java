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
 *	mywebsite-backend - AuthenticationUserService.java
 *	Using JRE 1.8.0_121
 *	@author Than Htike Aung {@literal <rage.cataclysm@gmail.com>}
 *	@Since 2017
 * 
 */
package com.mycom.products.mywebsite.backend.security;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.mycom.products.mywebsite.core.bean.BaseBean;
import com.mycom.products.mywebsite.core.bean.config.LoggedUserBean;
import com.mycom.products.mywebsite.core.bean.config.LoginHistoryBean;
import com.mycom.products.mywebsite.core.bean.config.UserBean;
import com.mycom.products.mywebsite.core.bean.config.UserRoleBean;
import com.mycom.products.mywebsite.core.service.config.api.LoginHistoryService;
import com.mycom.products.mywebsite.core.service.config.api.UserRoleService;
import com.mycom.products.mywebsite.core.service.config.api.UserService;
import com.mycom.products.mywebsite.core.util.FetchMode;

@Component
public class AuthenticationUserService implements UserDetailsService {
    private static final Logger applicationLogger = Logger.getLogger("applicationLogs." + AuthenticationUserService.class.getName());
    private static final Logger errorLogger = Logger.getLogger("errorLogs." + AuthenticationUserService.class.getName());

    @Autowired
    private UserService userService;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private LoginHistoryService loginHistoryService;

    @Override
    public final UserDetails loadUserByUsername(final String loginId) throws UsernameNotFoundException {
	UserBean user = null;
	applicationLogger.info(BaseBean.LOG_BREAKER_OPEN);
	try {
	    HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
	    HashMap<String, Object> map = new HashMap<>();
	    map.put("loginId", loginId);
	    user = userService.select(map, FetchMode.LAZY);
	    if (user == null) {
		throw new UsernameNotFoundException("User doesn`t exist");
	    }
	    LoginHistoryBean loginHistory = new LoginHistoryBean();
	    loginHistory.setIpAddress(getClientIp(request));
	    loginHistory.setOs(getOperatingSystem(request));
	    loginHistory.setUserAgent(getUserAgent(request));
	    loginHistory.setLoginDate(LocalDateTime.now());
	    loginHistory.setUserId(user.getId());
	    loginHistoryService.insert(loginHistory, user.getId());
	    Map<String, Object> criteria = new HashMap<>();
	    criteria.put("userId", user.getId());
	    List<UserRoleBean> userRoles = userRoleService.selectList(criteria, FetchMode.EAGER);
	    List<String> dbRoles = new ArrayList<>();
	    userRoles.forEach(userRole -> {
		dbRoles.add(userRole.getRole().getName());
	    });
	    applicationLogger.info(BaseBean.LOG_PREFIX + "User '" + user.getName() + "' has successfully signed in." + BaseBean.LOG_SUFFIX);
	    applicationLogger.info(BaseBean.LOG_PREFIX + "Roles of :" + user.getName() + " are " + dbRoles + BaseBean.LOG_SUFFIX);
	    // pass user object and roles to LoggedUser
	    LoggedUserBean loggedUser = new LoggedUserBean(user, dbRoles);
	    applicationLogger.info(BaseBean.LOG_BREAKER_CLOSE);
	    return loggedUser;
	} catch (Exception e) {
	    e.printStackTrace();
	    errorLogger.error(BaseBean.LOG_PREFIX + "Signin user is not valid or found" + BaseBean.LOG_SUFFIX, e);
	    return null;
	}
    }

    private String getClientIp(HttpServletRequest request) {
	return request.getRemoteAddr();

    }

    private String getOperatingSystem(HttpServletRequest request) {
	String os = "";
	String userAgent = request.getHeader("User-Agent");
	if (userAgent.toLowerCase().indexOf("windows") >= 0) {
	    os = "Windows";
	} else if (userAgent.toLowerCase().indexOf("mac") >= 0) {
	    os = "Mac";
	} else if (userAgent.toLowerCase().indexOf("x11") >= 0) {
	    os = "Unix";
	} else if (userAgent.toLowerCase().indexOf("android") >= 0) {
	    os = "Android";
	} else if (userAgent.toLowerCase().indexOf("iphone") >= 0) {
	    os = "IPhone";
	} else {
	    os = "UnKnown, More-Info: " + userAgent;
	}
	return os;
    }

    private String getUserAgent(HttpServletRequest request) {
	String userAgent = request.getHeader("User-Agent");
	String user = userAgent.toLowerCase();

	String browser = "";
	if (user.contains("msie")) {
	    String substring = userAgent.substring(userAgent.indexOf("MSIE")).split(";")[0];
	    browser = substring.split(" ")[0].replace("MSIE", "IE") + "-" + substring.split(" ")[1];
	} else if (user.contains("safari") && user.contains("version")) {
	    browser = (userAgent.substring(userAgent.indexOf("Safari")).split(" ")[0]).split("/")[0] + "-"
		    + (userAgent.substring(userAgent.indexOf("Version")).split(" ")[0]).split("/")[1];
	} else if (user.contains("opr") || user.contains("opera")) {
	    if (user.contains("opera"))
		browser = (userAgent.substring(userAgent.indexOf("Opera")).split(" ")[0]).split("/")[0] + "-"
			+ (userAgent.substring(userAgent.indexOf("Version")).split(" ")[0]).split("/")[1];
	    else if (user.contains("opr"))
		browser = ((userAgent.substring(userAgent.indexOf("OPR")).split(" ")[0]).replace("/", "-")).replace("OPR", "Opera");
	} else if (user.contains("chrome")) {
	    browser = (userAgent.substring(userAgent.indexOf("Chrome")).split(" ")[0]).replace("/", "-");
	} else if ((user.indexOf("mozilla/7.0") > -1) || (user.indexOf("netscape6") != -1) || (user.indexOf("mozilla/4.7") != -1)
		|| (user.indexOf("mozilla/4.78") != -1) || (user.indexOf("mozilla/4.08") != -1) || (user.indexOf("mozilla/3") != -1)) {
	    browser = "Netscape-?";

	} else if (user.contains("firefox")) {
	    browser = (userAgent.substring(userAgent.indexOf("Firefox")).split(" ")[0]).replace("/", "-");
	} else if (user.contains("rv")) {
	    browser = "IE";
	} else {
	    browser = "UnKnown, More-Info: " + userAgent;
	}
	return browser;
    }

}
