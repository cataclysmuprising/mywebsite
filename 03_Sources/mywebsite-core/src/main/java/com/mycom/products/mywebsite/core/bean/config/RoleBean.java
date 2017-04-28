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
 *	mywebsite-core - RoleBean.java
 *	Using JRE 1.8.0_121
 *	@author Than Htike Aung {@literal <rage.cataclysm@gmail.com>}
 *	@Since 2017
 * 
 */
package com.mycom.products.mywebsite.core.bean.config;

import java.util.List;

import com.mycom.products.mywebsite.core.bean.BaseBean;

public class RoleBean extends BaseBean implements java.io.Serializable {
    private static final long serialVersionUID = -5005684482585299863L;
    private String name;
    private String description;
    private List<Long> actionIds;
    private List<Long> userIds;
    private List<ActionBean> actions;
    private List<UserBean> users;

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public List<Long> getActionIds() {
	return actionIds;
    }

    public void setActionIds(List<Long> actionIds) {
	this.actionIds = actionIds;
    }

    public List<Long> getUserIds() {
	return userIds;
    }

    public void setUserIds(List<Long> userIds) {
	this.userIds = userIds;
    }

    public List<ActionBean> getActions() {
	return actions;
    }

    public void setActions(List<ActionBean> actions) {
	this.actions = actions;
    }

    public List<UserBean> getUsers() {
	return users;
    }

    public void setUsers(List<UserBean> users) {
	this.users = users;
    }

    @Override
    public String toString() {
	return String.format("RoleBean {name=%s, description=%s, actionIds=%s, userIds=%s, actions=%s, users=%s, ID=%s}", name, description,
		actionIds, userIds, actions, users, getId());
    }

}
