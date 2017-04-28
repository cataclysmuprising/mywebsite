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
 *	mywebsite-core - LoginHistoryBean.java
 *	Using JRE 1.8.0_121
 *	@author Than Htike Aung {@literal <rage.cataclysm@gmail.com>}
 *	@Since 2017
 * 
 */
package com.mycom.products.mywebsite.core.bean.config;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mycom.products.mywebsite.core.bean.BaseBean;
import com.mycom.products.mywebsite.core.util.LocalDateTimeSerializer;

public class LoginHistoryBean extends BaseBean implements Serializable {
    private static final long serialVersionUID = 3454068909862152769L;
    private String ipAddress;
    private String os;
    private String userAgent;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime loginDate;
    private long userId;
    private UserBean user;

    public long getUserId() {
	return userId;
    }

    public void setUserId(long userId) {
	this.userId = userId;
    }

    public LocalDateTime getLoginDate() {
	return loginDate;
    }

    public void setLoginDate(LocalDateTime loginDate) {
	this.loginDate = loginDate;
    }

    public String getOs() {
	return os;
    }

    public void setOs(String os) {
	this.os = os;
    }

    public UserBean getUser() {
	return user;
    }

    public void setUser(UserBean user) {
	this.user = user;
    }

    public String getIpAddress() {
	return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
	this.ipAddress = ipAddress;
    }

    public String getUserAgent() {
	return userAgent;
    }

    public void setUserAgent(String userAgent) {
	this.userAgent = userAgent;
    }

    @Override
    public String toString() {
	return String.format("LoginHistoryBean {ipAddress=%s, os=%s, userAgent=%s, loginDate=%s, userId=%s, user=%s, ID=%s}", ipAddress, os,
		userAgent, loginDate, userId, user, getId());
    }

}
