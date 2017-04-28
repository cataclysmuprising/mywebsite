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
 *	mywebsite-core - InsertableServiceImpl.java
 *	Using JRE 1.8.0_121
 *	@author Than Htike Aung {@literal <rage.cataclysm@gmail.com>}
 *	@Since 2017
 * 
 */
package com.mycom.products.mywebsite.core.service.base;

import java.util.List;

import org.apache.log4j.Logger;

import com.mycom.products.mywebsite.core.annotation.TXManageable;
import com.mycom.products.mywebsite.core.bean.BaseBean;
import com.mycom.products.mywebsite.core.dao.api.InsertableDao;
import com.mycom.products.mywebsite.core.exception.BusinessException;
import com.mycom.products.mywebsite.core.exception.DAOException;
import com.mycom.products.mywebsite.core.exception.DuplicatedEntryException;
import com.mycom.products.mywebsite.core.service.base.api.root.InsertableService;

@TXManageable
public class InsertableServiceImpl<T extends BaseBean> implements InsertableService<T> {
    private static final Logger serviceLogger = Logger.getLogger("serviceLogs." + InsertableServiceImpl.class.getName());
    private InsertableDao<T> dao;

    public InsertableServiceImpl(InsertableDao<T> dao) {
	this.dao = dao;
    }

    @Override
    public long insert(T record, long recordRegId) throws DuplicatedEntryException, BusinessException {
	serviceLogger.info(BaseBean.LOG_PREFIX + "This transaction was initiated by User ID # " + recordRegId + BaseBean.LOG_SUFFIX);
	serviceLogger.info(
		BaseBean.LOG_PREFIX + "Transaction start for inserting" + getObjectName(record) + "informations." + BaseBean.LOG_SUFFIX);
	long lastInsertedId = 0;
	try {
	    lastInsertedId = dao.insert(record, recordRegId);
	} catch (DAOException e) {
	    throw new BusinessException(e.getMessage(), e);
	}
	serviceLogger.info(BaseBean.LOG_PREFIX + "Transaction finished successfully for inserting" + getObjectName(record) + "informations."
		+ BaseBean.LOG_SUFFIX);
	return lastInsertedId;
    }

    @Override
    public void insert(List<T> records, long recordRegId) throws DuplicatedEntryException, BusinessException {
	serviceLogger.info(BaseBean.LOG_PREFIX + "This transaction was initiated by User ID # " + recordRegId + BaseBean.LOG_SUFFIX);
	serviceLogger.info(BaseBean.LOG_PREFIX + "Transaction start for inserting multi" + getObjectName(records) + "informations."
		+ BaseBean.LOG_SUFFIX);
	try {
	    dao.insert(records, recordRegId);
	} catch (DAOException e) {
	    throw new BusinessException(e.getMessage(), e);
	}
	serviceLogger.info(BaseBean.LOG_PREFIX + "Transaction finished successfully for inserting multi" + getObjectName(records)
		+ "informations." + BaseBean.LOG_SUFFIX);
    }

}
