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
 *	mywebsite-backend - AuthenticationController.java
 *	Using JRE 1.8.0_121
 *	@author Than Htike Aung {@literal <rage.cataclysm@gmail.com>}
 *	@Since 2017
 * 
 */

package com.mycom.products.mywebsite.backend.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mycom.products.mywebsite.core.bean.BaseBean;

@Controller
public class AuthenticationController extends BaseController {
    private static final Logger applicationLogger = Logger.getLogger("applicationLogs." + AuthenticationController.class.getName());
    private static final Logger errorLogger = Logger.getLogger("errorLogs." + AuthenticationController.class.getName());

    @Override
    public void subInit(Model model) {

    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
	return "login";
    }

    @RequestMapping(value = "/loginfailed", method = RequestMethod.GET)
    public String loginError(Model model) {
	model.addAttribute("messageStyle", "alert-danger");
	model.addAttribute("pageMessage", "Incorrect Login ID or Password.");
	return "login";
    }

    @RequestMapping(value = "/accessDenied", method = RequestMethod.GET)
    public String accessDenied(Model model) {
	if (loginUser != null) {
	    errorLogger.error(BaseBean.LOG_BREAKER_OPEN);
	    errorLogger.error(BaseBean.LOG_PREFIX + "User '" + loginUser.getRealName() + "' has requested for unauthorized contents."
		    + BaseBean.LOG_SUFFIX);
	    errorLogger.error(BaseBean.LOG_BREAKER_CLOSE);
	}
	return "error/403";
    }

    @RequestMapping(value = "/sessionTimeOut", method = RequestMethod.GET)
    public String sessionTimeOut(Model model) {
	if (loginUser != null) {
	    errorLogger.error(BaseBean.LOG_BREAKER_OPEN);
	    errorLogger
		    .error(BaseBean.LOG_PREFIX + "Session of User '" + loginUser.getRealName() + "' was Time-out." + BaseBean.LOG_SUFFIX);
	    errorLogger.error(BaseBean.LOG_BREAKER_CLOSE);
	}
	return "error/401";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(HttpServletRequest request, HttpServletResponse response, Model model, RedirectAttributes attributes) {

	attributes.addFlashAttribute("messageStyle", "alert-success");
	attributes.addFlashAttribute("pageMessage", "You have been logged out successfully.");

	Authentication auth = SecurityContextHolder.getContext().getAuthentication();

	if (auth != null) {
	    new SecurityContextLogoutHandler().logout(request, response, auth);
	    applicationLogger
		    .info(BaseBean.LOG_PREFIX + "User '" + loginUser.getRealName() + "' has been signed out." + BaseBean.LOG_SUFFIX);
	}

	// delete remember me cookie after logout
	Cookie cookie = new Cookie("mywebsite_rbm", null);
	cookie.setMaxAge(0);
	cookie.setPath(StringUtils.hasLength(request.getContextPath()) ? request.getContextPath() : "/");

	response.addCookie(cookie);

	return "redirect:/login?logout";
    }
}