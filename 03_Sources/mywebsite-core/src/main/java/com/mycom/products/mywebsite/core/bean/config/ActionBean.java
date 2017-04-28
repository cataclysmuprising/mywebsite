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
 *	mywebsite-core - ActionBean.java
 *	Using JRE 1.8.0_121
 *	@author Than Htike Aung {@literal <rage.cataclysm@gmail.com>}
 *	@Since 2017
 * 
 */
package com.mycom.products.mywebsite.core.bean.config;

import java.util.List;

import com.mycom.products.mywebsite.core.bean.BaseBean;

public class ActionBean extends BaseBean implements java.io.Serializable {
    private static final long serialVersionUID = -7891646684080105708L;
    private String module;
    private String page;
    private String actionName;
    private String displayName;
    private String url;
    private List<Integer> roleIds;
    private List<RoleBean> roles;
    private ActionType actionType;
    private String description;

    public enum ActionType {
	MAIN, SUB;
    }

    public String getUrl() {
	return url;
    }

    public void setUrl(String url) {
	this.url = url;
    }

    public String getPage() {
	return page;
    }

    public void setPage(String page) {
	this.page = page;
    }

    public List<RoleBean> getRoles() {
	return roles;
    }

    public void setRoles(List<RoleBean> roles) {
	this.roles = roles;
    }

    public List<Integer> getRoleIds() {
	return roleIds;
    }

    public void setRoleIds(List<Integer> roleIds) {
	this.roleIds = roleIds;
    }

    public ActionType getActionType() {
	return actionType;
    }

    public void setActionType(ActionType actionType) {
	this.actionType = actionType;
    }

    public String getActionName() {
	return actionName;
    }

    public void setActionName(String actionName) {
	this.actionName = actionName;
    }

    public String getDisplayName() {
	return displayName;
    }

    public void setDisplayName(String displayName) {
	this.displayName = displayName;
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public String getModule() {
	return module;
    }

    public void setModule(String module) {
	this.module = module;
    }

    @Override
    public String toString() {
	return String.format(
		"ActionBean {module=%s, page=%s, actionName=%s, displayName=%s, url=%s, roleIds=%s, roles=%s, actionType=%s, description=%s, ID=%s}",
		module, page, actionName, displayName, url, roleIds, roles, actionType, description, getId());
    }

}
