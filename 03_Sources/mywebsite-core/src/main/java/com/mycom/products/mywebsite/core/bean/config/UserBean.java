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
 *	mywebsite-core - UserBean.java
 *	Using JRE 1.8.0_121
 *	@author Than Htike Aung {@literal <rage.cataclysm@gmail.com>}
 *	@Since 2017
 * 
 */
package com.mycom.products.mywebsite.core.bean.config;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mycom.products.mywebsite.core.bean.BaseBean;
import com.mycom.products.mywebsite.core.bean.master.StaticContentBean;
import com.mycom.products.mywebsite.core.util.LocalDateTimeSerializer;

public class UserBean extends BaseBean implements Serializable {
    private static final long serialVersionUID = -6106913047232092123L;

    public enum Gender {
	MALE, FEMALE, OTHER;
    }

    private Integer age;
    private String loginId;
    private String name;
    private String email;
    @JsonIgnore
    private String password;
    @JsonIgnore
    private String confirmPassword;
    private String phone;
    private String nrc;
    private Set<Long> roleIds;
    private Gender gender;

    private LocalDate dob;
    private String address;
    private List<RoleBean> roles;
    private String dobAsString;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime lastLoginDate;
    private int contentId;
    private StaticContentBean content;

    public UserBean() {
	gender = Gender.MALE;
	content = new StaticContentBean();
    }

    public Integer getAge() {
	return age;
    }

    public void setAge(Integer age) {
	this.age = age;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public Gender getGender() {
	return gender;
    }

    public void setGender(Gender gender) {
	this.gender = gender;
    }

    public String getEmail() {
	return email;
    }

    public void setEmail(String email) {
	this.email = email;
    }

    public String getNrc() {
	return nrc;
    }

    public void setNrc(String nrc) {
	this.nrc = nrc;
    }

    public String getPhone() {
	return phone;
    }

    public void setPhone(String phone) {
	this.phone = phone;
    }

    public LocalDate getDob() {
	return dob;
    }

    public void setDob(LocalDate dob) {
	this.dob = dob;
	this.dobAsString = convertDateAsString(dob);
    }

    public String getPassword() {
	return password;
    }

    public void setPassword(String password) {
	this.password = password;
    }

    public String getConfirmPassword() {
	return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
	this.confirmPassword = confirmPassword;
    }

    public String getAddress() {
	return address;
    }

    public void setAddress(String address) {
	this.address = address;
    }

    public List<RoleBean> getRoles() {
	return roles;
    }

    public void setRoles(List<RoleBean> roles) {
	this.roles = roles;
    }

    public String getDobAsString() {
	return dobAsString;
    }

    public void setDobAsString(String dobAsString) {
	this.dobAsString = dobAsString;
	this.dob = convertStringAsDate(dobAsString);
    }

    public Set<Long> getRoleIds() {
	return roleIds;
    }

    public void setRoleIds(Set<Long> roleIds) {
	this.roleIds = roleIds;
    }

    public LocalDateTime getLastLoginDate() {
	return lastLoginDate;
    }

    public void setLastLoginDate(LocalDateTime lastLoginDate) {
	this.lastLoginDate = lastLoginDate;
    }

    public String getLoginId() {
	return loginId;
    }

    public void setLoginId(String loginId) {
	this.loginId = loginId;
    }

    public int getContentId() {
	return contentId;
    }

    public void setContentId(int contentId) {
	this.contentId = contentId;
    }

    public StaticContentBean getContent() {
	return content;
    }

    public void setContent(StaticContentBean content) {
	this.content = content;
    }

    @Override
    public String toString() {
	return String.format(
		"UserBean {age=%s, loginId=%s, name=%s, email=%s, password=%s, confirmPassword=%s, phone=%s, nrc=%s, roleIds=%s, gender=%s, dob=%s, address=%s, roles=%s, dobAsString=%s, lastLoginDate=%s, contentId=%s, content=%s, ID=%s}",
		age, loginId, name, email, password, confirmPassword, phone, nrc, roleIds, gender, dob, address, roles, dobAsString,
		lastLoginDate, contentId, content, getId());
    }

}
