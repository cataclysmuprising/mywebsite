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
 *	mywebsite-core - RoleActionBean.java
 *	Using JRE 1.8.0_121
 *	@author Than Htike Aung {@literal <rage.cataclysm@gmail.com>}
 *	@Since 2017
 * 
 */
package com.mycom.products.mywebsite.core.bean.config;

import com.mycom.products.mywebsite.core.bean.BaseBean;

public class RoleActionBean extends BaseBean implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    private long roleId;
    private long actionId;

    private RoleBean role;
    private ActionBean action;

    public RoleActionBean() {
    }

    public RoleActionBean(RoleBean role, ActionBean action) {
	this.role = role;
	this.action = action;
    }

    public RoleActionBean(long roleId, long actionId) {
	this.roleId = roleId;
	this.actionId = actionId;
    }

    public RoleBean getRole() {
	return role;
    }

    public void setRole(RoleBean role) {
	this.role = role;
    }

    public ActionBean getAction() {
	return action;
    }

    public void setAction(ActionBean action) {
	this.action = action;
    }

    public long getRoleId() {
	return roleId;
    }

    public void setRoleId(long roleId) {
	this.roleId = roleId;
    }

    public long getActionId() {
	return actionId;
    }

    public void setActionId(Integer actionId) {
	this.actionId = actionId;
    }

    @Override
    public String toString() {
	return String.format("RoleActionBean {roleId=%s, actionId=%s, role=%s, action=%s}", roleId, actionId, role, action);
    }

}
