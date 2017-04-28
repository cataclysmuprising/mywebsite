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
 *	mywebsite-core - RoleServiceImpl.java
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
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;

import com.mycom.products.mywebsite.core.bean.BaseBean;
import com.mycom.products.mywebsite.core.bean.config.RoleActionBean;
import com.mycom.products.mywebsite.core.bean.config.RoleBean;
import com.mycom.products.mywebsite.core.bean.config.UserRoleBean;
import com.mycom.products.mywebsite.core.dao.config.RoleActionDao;
import com.mycom.products.mywebsite.core.dao.config.RoleDao;
import com.mycom.products.mywebsite.core.dao.config.UserRoleDao;
import com.mycom.products.mywebsite.core.exception.BusinessException;
import com.mycom.products.mywebsite.core.exception.ConsistencyViolationException;
import com.mycom.products.mywebsite.core.exception.DAOException;
import com.mycom.products.mywebsite.core.exception.DuplicatedEntryException;
import com.mycom.products.mywebsite.core.service.base.JoinedServiceImpl;
import com.mycom.products.mywebsite.core.service.config.api.RoleService;
import com.mycom.products.mywebsite.core.util.AuthorityChangeEvent;

@Service
public class RoleServiceImpl extends JoinedServiceImpl<RoleBean> implements RoleService, ApplicationEventPublisherAware {
    private static final Logger serviceLogger = Logger.getLogger("serviceLogs." + RoleServiceImpl.class.getName());

    @Autowired
    private UserRoleDao userRoleDao;

    @Autowired
    private RoleActionDao roleActionDao;

    private RoleDao roleDao;

    private ApplicationEventPublisher publisher;

    @Autowired
    public RoleServiceImpl(RoleDao dao) {
	super(dao, dao);
	this.roleDao = dao;
    }

    // why this service need to implements ApplicationEventPublisherAware ?
    // Ans : Roles and Actions relations will be loaded just one-time. If these
    // were changed, we need to reload them without application restart.
    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher publisher) {
	this.publisher = publisher;
    }

    @Override
    public long insertRoleWithRelations(RoleBean role, long recordRegId) throws DuplicatedEntryException, BusinessException {
	serviceLogger.info(BaseBean.LOG_PREFIX + "This transaction was initiated by User ID # " + recordRegId + BaseBean.LOG_SUFFIX);
	serviceLogger.info(BaseBean.LOG_PREFIX + "Transaction start for inserting" + getObjectName(role) + "information with relations."
		+ BaseBean.LOG_SUFFIX);
	long lastInsertedId = 0;
	try {
	    lastInsertedId = roleDao.insert(role, recordRegId);
	    if (role.getUserIds() != null && role.getUserIds().size() > 0) {
		serviceLogger.info(
			BaseBean.LOG_PREFIX + "Transaction start for inserting related 'User-Role' informations." + BaseBean.LOG_SUFFIX);
		List<UserRoleBean> userRoles = new ArrayList<>();
		role.getUserIds().forEach(userId -> {
		    UserRoleBean userRole = new UserRoleBean(userId, role.getId());
		    userRoles.add(userRole);
		});
		userRoleDao.insert(userRoles, recordRegId);
	    }
	    if (role.getActionIds() != null && role.getActionIds().size() > 0) {
		serviceLogger.info(
			BaseBean.LOG_PREFIX + "Transaction start for inserting related 'Role-Action' informations." + BaseBean.LOG_SUFFIX);
		List<RoleActionBean> roleActions = new ArrayList<>();
		role.getActionIds().forEach(actionId -> {
		    RoleActionBean roleAction = new RoleActionBean(role.getId(), actionId);
		    roleActions.add(roleAction);
		});
		roleActionDao.insert(roleActions, recordRegId);
	    }
	} catch (DAOException e) {
	    throw new BusinessException(e.getMessage(), e);
	}

	serviceLogger.info(BaseBean.LOG_PREFIX + "Transaction finished successfully for inserting" + getObjectName(role)
		+ "information with relations." + BaseBean.LOG_SUFFIX);
	publisher.publishEvent(new AuthorityChangeEvent(this, "INSERT"));
	return lastInsertedId;
    }

    @Override
    public long updateRoleWithRelations(RoleBean role, long recordUpdId)
	    throws DuplicatedEntryException, BusinessException, ConsistencyViolationException {
	serviceLogger.info(BaseBean.LOG_PREFIX + "This transaction was initiated by User ID # " + recordUpdId + BaseBean.LOG_SUFFIX);
	serviceLogger.info(BaseBean.LOG_PREFIX + "Transaction start for updating" + getObjectName(role) + "informations  with relations."
		+ BaseBean.LOG_SUFFIX);
	long effectedRows = 0;
	try {
	    effectedRows = roleDao.update(role, recordUpdId);
	    if (role.getUserIds() != null && role.getUserIds().size() > 0) {
		serviceLogger.info(
			BaseBean.LOG_PREFIX + "Transaction start for inserting related 'User-Role' informations." + BaseBean.LOG_SUFFIX);
		userRoleDao.merge(role.getUserIds(), role.getId(), recordUpdId);
	    }
	    if (role.getActionIds() != null && role.getActionIds().size() > 0) {
		serviceLogger.info(
			BaseBean.LOG_PREFIX + "Transaction start for merging related 'Role-Action' informations." + BaseBean.LOG_SUFFIX);
		roleActionDao.merge(role.getId(), role.getActionIds(), recordUpdId);
	    }
	} catch (DAOException e) {
	    throw new BusinessException(e.getMessage(), e);
	}

	serviceLogger.info(BaseBean.LOG_PREFIX + "Transaction finished successfully for inserting" + getObjectName(role)
		+ "informations  with relations" + BaseBean.LOG_SUFFIX);
	publisher.publishEvent(new AuthorityChangeEvent(this, "INSERT"));
	return effectedRows;
    }

}
