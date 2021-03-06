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
 *	mywebsite-core - UserRoleBean.java
 *	Using JRE 1.8.0_121
 *	@author Than Htike Aung {@literal <rage.cataclysm@gmail.com>}
 *	@Since 2017
 * 
 */
package com.mycom.products.mywebsite.core.bean.config;

import com.mycom.products.mywebsite.core.bean.BaseBean;

public class UserRoleBean extends BaseBean implements java.io.Serializable {
    private static final long serialVersionUID = 4461809498816982732L;
    private long userId;
    private long roleId;
    private UserBean user;
    private RoleBean role;

    public UserRoleBean() {
    }

    public UserRoleBean(UserBean user, RoleBean role) {
	this.user = user;
	this.role = role;
    }

    public UserRoleBean(long userId, long roleId) {
	this.userId = userId;
	this.roleId = roleId;
    }

    public UserBean getUser() {
	return this.user;
    }

    public void setUser(UserBean user) {
	this.user = user;
    }

    public RoleBean getRole() {
	return this.role;
    }

    public void setRole(RoleBean role) {
	this.role = role;
    }

    public long getUserId() {
	return userId;
    }

    public void setUserId(long userId) {
	this.userId = userId;
    }

    public long getRoleId() {
	return roleId;
    }

    public void setRoleId(int roleId) {
	this.roleId = roleId;
    }

    @Override
    public String toString() {
	return String.format("UserRoleBean {userId=%s, roleId=%s, user=%s, role=%s}", userId, roleId, user, role);
    }

}
