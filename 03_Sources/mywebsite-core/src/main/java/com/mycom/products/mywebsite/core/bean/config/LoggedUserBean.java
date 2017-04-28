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
 *	mywebsite-core - LoggedUserBean.java
 *	Using JRE 1.8.0_121
 *	@author Than Htike Aung {@literal <rage.cataclysm@gmail.com>}
 *	@Since 2017
 * 
 */
package com.mycom.products.mywebsite.core.bean.config;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class LoggedUserBean implements UserDetails {
    private static final long serialVersionUID = 1260667404772902490L;
    private UserBean user;
    private List<String> roles;

    public LoggedUserBean(UserBean user, List<String> roles) {
	this.user = user;
	this.roles = roles;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
	List<SimpleGrantedAuthority> authorities = new ArrayList<>();
	for (String role : roles) {
	    authorities.add(new SimpleGrantedAuthority(role));
	}
	return authorities;
    }

    @Override
    public String getPassword() {
	return user.getPassword();
    }

    @Override
    public String getUsername() {
	// Login in with LoginId , not userName. So , this must set with email
	// If you don't understand , don't touch this.. :)
	return user.getLoginId();
    }

    // use this method to get user name
    public String getRealName() {
	return user.getName();
    }

    public long getId() {
	return user.getId();
    }

    public String getRegisterDate() {
	DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMM, yyyy");
	String recordRegisterDateString = "";
	LocalDateTime recordRegisterDate = user.getRecordRegDate();
	if (recordRegisterDate != null) {
	    recordRegisterDateString = dateFormatter.format(recordRegisterDate);
	}
	return recordRegisterDateString;
    }

    public int getContentId() {
	return user.getContentId();
    }

    @Override
    public boolean isAccountNonExpired() {
	return true;
    }

    @Override
    public boolean isAccountNonLocked() {
	return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
	return true;
    }

    @Override
    public boolean isEnabled() {
	return true;
    }

}
