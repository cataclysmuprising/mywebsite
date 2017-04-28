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
 *	mywebsite-core - XGenericServiceImpl.java
 *	Using JRE 1.8.0_121
 *	@author Than Htike Aung {@literal <rage.cataclysm@gmail.com>}
 *	@Since 2017
 * 
 */
package com.mycom.products.mywebsite.core.service.base;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.mycom.products.mywebsite.core.annotation.TXManageable;
import com.mycom.products.mywebsite.core.bean.BaseBean;
import com.mycom.products.mywebsite.core.dao.api.XGenericDao;
import com.mycom.products.mywebsite.core.exception.BusinessException;
import com.mycom.products.mywebsite.core.exception.ConsistencyViolationException;
import com.mycom.products.mywebsite.core.exception.DAOException;
import com.mycom.products.mywebsite.core.exception.DuplicatedEntryException;
import com.mycom.products.mywebsite.core.service.base.api.XGenericService;
import com.mycom.products.mywebsite.core.util.FetchMode;

public class XGenericServiceImpl<T extends BaseBean> implements XGenericService<T> {
    private static final Logger serviceLogger = Logger.getLogger("serviceLogs." + XGenericServiceImpl.class.getName());
    private XGenericDao<T> dao;

    public XGenericServiceImpl(XGenericDao<T> dao) {
	this.dao = dao;
    }

    @Override
    @TXManageable
    public void insert(T record, long recordRegId) throws DuplicatedEntryException, BusinessException {
	serviceLogger.info(BaseBean.LOG_PREFIX + "This transaction was initiated by User ID # " + recordRegId + BaseBean.LOG_SUFFIX);
	serviceLogger.info(
		BaseBean.LOG_PREFIX + "Transaction start for inserting" + getObjectName(record) + "informations." + BaseBean.LOG_SUFFIX);
	try {
	    dao.insert(record, recordRegId);
	} catch (DAOException e) {
	    throw new BusinessException(e.getMessage(), e);
	}
	serviceLogger.info(BaseBean.LOG_PREFIX + "Transaction finished successfully." + BaseBean.LOG_SUFFIX);
    }

    @Override
    @TXManageable
    public void insert(List<T> records, long recordRegId) throws DuplicatedEntryException, BusinessException {
	serviceLogger.info(BaseBean.LOG_PREFIX + "This transaction was initiated by User ID # " + recordRegId + BaseBean.LOG_SUFFIX);
	serviceLogger.info(
		BaseBean.LOG_PREFIX + "Transaction start for inserting" + getObjectName(records) + "informations." + BaseBean.LOG_SUFFIX);
	try {
	    dao.insert(records, recordRegId);
	} catch (DAOException e) {
	    throw new BusinessException(e.getMessage(), e);
	}
	serviceLogger.info(BaseBean.LOG_PREFIX + "Transaction finished successfully." + BaseBean.LOG_SUFFIX);
    }

    @Override
    @TXManageable
    public void insert(long key1, long key2, long recordRegId) throws DuplicatedEntryException, BusinessException {
	serviceLogger.info(BaseBean.LOG_PREFIX + "This transaction was initiated by User ID # " + recordRegId + BaseBean.LOG_SUFFIX);
	serviceLogger.info(BaseBean.LOG_PREFIX + "Transaction start for inserting with keys {key1=" + key1 + ",key2=" + key2 + "}"
		+ BaseBean.LOG_SUFFIX);
	try {
	    dao.insert(key1, key2, recordRegId);
	} catch (DAOException e) {
	    throw new BusinessException(e.getMessage(), e);
	}
	serviceLogger.info(BaseBean.LOG_PREFIX + "Transaction finished successfully." + BaseBean.LOG_SUFFIX);
    }

    @Override
    @TXManageable
    public void merge(long mainKey, List<Long> relatedKeys, long recordUpdId)
	    throws DuplicatedEntryException, ConsistencyViolationException, BusinessException {
	serviceLogger.info(BaseBean.LOG_PREFIX + "This transaction was initiated by User ID # " + recordUpdId + BaseBean.LOG_SUFFIX);
	serviceLogger.info(BaseBean.LOG_PREFIX + "Transaction start for merging (Main Key = " + mainKey + ") with related keys ==> "
		+ relatedKeys + BaseBean.LOG_SUFFIX);
	try {
	    dao.merge(mainKey, relatedKeys, recordUpdId);
	} catch (DAOException e) {
	    throw new BusinessException(e.getMessage(), e);
	}
	serviceLogger.info(BaseBean.LOG_PREFIX + "Transaction finished successfully." + BaseBean.LOG_SUFFIX);
    }

    @Override
    @TXManageable
    public long delete(long key1, long key2, long recordUpdId) throws ConsistencyViolationException, BusinessException {
	serviceLogger.info(BaseBean.LOG_PREFIX + "This transaction was initiated by User ID # " + recordUpdId + BaseBean.LOG_SUFFIX);
	serviceLogger.info(BaseBean.LOG_PREFIX + "Transaction start for delete by Keys ==> {key1=" + key1 + ",key2=" + key2 + "}"
		+ BaseBean.LOG_SUFFIX);
	long totalEffectedRows = 0;
	try {
	    totalEffectedRows = dao.delete(key1, key2, recordUpdId);
	} catch (DAOException e) {
	    throw new BusinessException(e.getMessage(), e);
	}
	serviceLogger.info(BaseBean.LOG_PREFIX + "Transaction finished successfully.");
	return totalEffectedRows;
    }

    @Override
    @TXManageable
    public long delete(Map<String, Object> criteria, long recordUpdId) throws ConsistencyViolationException, BusinessException {
	serviceLogger.info(BaseBean.LOG_PREFIX + "This transaction was initiated by User ID # " + recordUpdId + BaseBean.LOG_SUFFIX);
	serviceLogger.info(BaseBean.LOG_PREFIX + "Transaction start for delete by criteria ==> " + criteria + BaseBean.LOG_SUFFIX);
	long totalEffectedRows = 0;
	try {
	    totalEffectedRows = dao.delete(criteria, recordUpdId);
	} catch (DAOException e) {
	    throw new BusinessException(e.getMessage(), e);
	}
	serviceLogger.info(BaseBean.LOG_PREFIX + "Transaction finished successfully." + BaseBean.LOG_SUFFIX);
	return totalEffectedRows;
    }

    @Override
    public List<Long> selectByKey1(long key1) throws BusinessException {
	serviceLogger.info(BaseBean.LOG_PREFIX + "Transaction start for fetching related keys by key1# " + key1 + BaseBean.LOG_SUFFIX);
	List<Long> relatedKeys = null;
	try {
	    relatedKeys = dao.selectByKey1(key1);
	} catch (DAOException e) {
	    throw new BusinessException(e.getMessage(), e);
	}
	serviceLogger.info(BaseBean.LOG_PREFIX + "Transaction finished successfully." + BaseBean.LOG_SUFFIX);
	return relatedKeys;
    }

    @Override
    public List<Long> selectByKey2(long key2) throws BusinessException {
	serviceLogger.info(BaseBean.LOG_PREFIX + "Transaction start for fetching related keys by key2# " + key2 + BaseBean.LOG_SUFFIX);
	List<Long> relatedKeys = null;
	try {
	    relatedKeys = dao.selectByKey2(key2);
	} catch (DAOException e) {
	    throw new BusinessException(e.getMessage(), e);
	}
	serviceLogger.info(BaseBean.LOG_PREFIX + "Transaction finished successfully." + BaseBean.LOG_SUFFIX);
	return relatedKeys;
    }

    @Override
    public T select(long key1, long key2, FetchMode fetchMode) throws BusinessException {
	serviceLogger.info(BaseBean.LOG_PREFIX + "Transaction start for fetching single record by Keys ==> {key1=" + key1 + ",key2=" + key2
		+ "}" + BaseBean.LOG_SUFFIX);
	T record = null;
	try {
	    record = dao.select(key1, key2, fetchMode);
	} catch (DAOException e) {
	    throw new BusinessException(e.getMessage(), e);
	}
	serviceLogger.info(BaseBean.LOG_PREFIX + "Transaction finished successfully." + BaseBean.LOG_SUFFIX);
	return record;
    }

    @Override
    public List<T> selectList(Map<String, Object> criteria, FetchMode fetchMode) throws BusinessException {
	serviceLogger.info(
		BaseBean.LOG_PREFIX + "Transaction start for fetching multi records by criteria ==> " + criteria + BaseBean.LOG_SUFFIX);
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
    public long selectCounts(Map<String, Object> criteria) throws BusinessException {
	serviceLogger.info(
		BaseBean.LOG_PREFIX + "Transaction start for fetching record counts by criteria ==> " + criteria + BaseBean.LOG_SUFFIX);
	long count = 0;
	try {
	    count = dao.selectCounts(criteria);
	} catch (DAOException e) {
	    throw new BusinessException(e.getMessage(), e);
	}
	serviceLogger.info(BaseBean.LOG_PREFIX + "Transaction finished successfully." + BaseBean.LOG_SUFFIX);
	return count;
    }
}
