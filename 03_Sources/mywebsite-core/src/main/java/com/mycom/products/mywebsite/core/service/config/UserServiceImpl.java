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
 *	mywebsite-core - UserServiceImpl.java
 *	Using JRE 1.8.0_121
 *	@author Than Htike Aung {@literal <rage.cataclysm@gmail.com>}
 *	@Since 2017
 * 
 */
package com.mycom.products.mywebsite.core.service.config;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mycom.products.mywebsite.core.annotation.TXManageable;
import com.mycom.products.mywebsite.core.bean.BaseBean;
import com.mycom.products.mywebsite.core.bean.config.UserBean;
import com.mycom.products.mywebsite.core.bean.config.UserRoleBean;
import com.mycom.products.mywebsite.core.dao.config.UserDao;
import com.mycom.products.mywebsite.core.dao.config.UserRoleDao;
import com.mycom.products.mywebsite.core.exception.BusinessException;
import com.mycom.products.mywebsite.core.exception.DAOException;
import com.mycom.products.mywebsite.core.exception.DuplicatedEntryException;
import com.mycom.products.mywebsite.core.service.base.JoinedServiceImpl;
import com.mycom.products.mywebsite.core.service.config.api.UserService;

@Service
public class UserServiceImpl extends JoinedServiceImpl<UserBean> implements UserService {
	private static final Logger serviceLogger = Logger.getLogger("serviceLogs." + UserServiceImpl.class.getName());
	private UserDao userDao;

	@Autowired
	private UserRoleDao userRoleDao;

	@Autowired
	public UserServiceImpl(UserDao userDao) {
		super(userDao, userDao);
		this.userDao = userDao;
	}

	@Override
	@TXManageable
	public long insertUserWithRoles(UserBean user,
			long recordRegId) throws DuplicatedEntryException, BusinessException {
		serviceLogger.info(BaseBean.LOG_PREFIX + "This transaction was initiated by User ID # " + recordRegId + BaseBean.LOG_SUFFIX);
		serviceLogger.info(BaseBean.LOG_PREFIX + "Transaction start for inserting" + getObjectName(user) + "informations." + BaseBean.LOG_SUFFIX);
		long lastInsertedId = 0;
		try {
			lastInsertedId = userDao.insert(user, recordRegId);
			if (user.getRoleIds() != null && user.getRoleIds().size() > 0) {
				serviceLogger.info(BaseBean.LOG_PREFIX + "Transaction start for inserting related 'User-Role' informations." + BaseBean.LOG_SUFFIX);
				List<UserRoleBean> userRoles = new ArrayList<>();
				user.getRoleIds().forEach(roleId -> {
					UserRoleBean userRole = new UserRoleBean(user.getId(), roleId);
					userRoles.add(userRole);
				});
				userRoleDao.insert(userRoles, recordRegId);
			}
		} catch (DAOException e) {
			throw new BusinessException(e.getMessage(), e);
		}

		serviceLogger.info(BaseBean.LOG_PREFIX + "Transaction finished successfully for inserting" + getObjectName(user) + "informations." + BaseBean.LOG_SUFFIX);
		return lastInsertedId;
	}

}
