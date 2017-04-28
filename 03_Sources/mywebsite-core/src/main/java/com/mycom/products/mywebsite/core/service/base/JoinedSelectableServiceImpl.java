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
 *	mywebsite-core - JoinedSelectableServiceImpl.java
 *	Using JRE 1.8.0_121
 *	@author Than Htike Aung {@literal <rage.cataclysm@gmail.com>}
 *	@Since 2017
 * 
 */
package com.mycom.products.mywebsite.core.service.base;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.mycom.products.mywebsite.core.bean.BaseBean;
import com.mycom.products.mywebsite.core.dao.api.JoinedSelectableDao;
import com.mycom.products.mywebsite.core.exception.BusinessException;
import com.mycom.products.mywebsite.core.exception.DAOException;
import com.mycom.products.mywebsite.core.service.base.api.root.JoinedSelectableService;
import com.mycom.products.mywebsite.core.util.FetchMode;

public class JoinedSelectableServiceImpl<T extends BaseBean> implements JoinedSelectableService<T> {
    private static final Logger serviceLogger = Logger.getLogger("serviceLogs." + JoinedSelectableServiceImpl.class.getName());
    private JoinedSelectableDao<T> dao;

    public JoinedSelectableServiceImpl(JoinedSelectableDao<T> dao) {
	this.dao = dao;
    }

    @Override
    public T select(long primaryKey, FetchMode fetchMode) throws BusinessException {
	serviceLogger.info(BaseBean.LOG_PREFIX + "Transaction start for fetch by primary key # " + primaryKey + " with fetchMode ==> "
		+ fetchMode + BaseBean.LOG_SUFFIX);
	T record = null;
	try {
	    record = dao.select(primaryKey, fetchMode);
	} catch (DAOException e) {
	    throw new BusinessException(e.getMessage(), e);
	}
	serviceLogger.info(BaseBean.LOG_PREFIX + "Transaction finished successfully." + BaseBean.LOG_SUFFIX);
	return record;
    }

    @Override
    public T select(Map<String, Object> criteria, FetchMode fetchMode) throws BusinessException {
	serviceLogger.info(BaseBean.LOG_PREFIX + "Transaction start for fetching single record by criteria ==> " + criteria
		+ " with fetchMode ==> " + fetchMode + BaseBean.LOG_SUFFIX);
	T record = null;
	try {
	    record = dao.select(criteria, fetchMode);
	} catch (DAOException e) {
	    throw new BusinessException(e.getMessage(), e);
	}
	serviceLogger.info(BaseBean.LOG_PREFIX + "Transaction finished successfully." + BaseBean.LOG_SUFFIX);
	return record;
    }

    @Override
    public List<T> selectList(Map<String, Object> criteria, FetchMode fetchMode) throws BusinessException {
	serviceLogger.info(BaseBean.LOG_PREFIX + "Transaction start for fetching multi records by criteria ==> " + criteria
		+ " with fetchMode ==> " + fetchMode + BaseBean.LOG_SUFFIX);
	List<T> records = null;
	try {
	    records = dao.selectList(criteria, fetchMode);
	} catch (DAOException e) {
	    throw new BusinessException(e.getMessage(), e);
	}
	serviceLogger.info(BaseBean.LOG_PREFIX + "Transaction finished successfully." + BaseBean.LOG_SUFFIX);
	return records;
    }

    @Override
    public long selectCounts(Map<String, Object> criteria, FetchMode fetchMode) throws BusinessException {
	serviceLogger.info(BaseBean.LOG_PREFIX + "Transaction start for fetching record counts by criteria ==> " + criteria
		+ " with fetchMode ==> " + fetchMode + BaseBean.LOG_SUFFIX);
	long count = 0;
	try {
	    count = dao.selectCounts(criteria, fetchMode);
	} catch (DAOException e) {
	    throw new BusinessException(e.getMessage(), e);
	}
	serviceLogger.info(BaseBean.LOG_PREFIX + "Transaction finished successfully." + BaseBean.LOG_SUFFIX);
	return count;
    }

}
