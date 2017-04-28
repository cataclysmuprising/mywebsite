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
 *	mywebsite-core - RoleDao.java
 *	Using JRE 1.8.0_121
 *	@author Than Htike Aung {@literal <rage.cataclysm@gmail.com>}
 *	@Since 2017
 * 
 */
package com.mycom.products.mywebsite.core.dao.config;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;

import com.mycom.products.mywebsite.core.bean.BaseBean.TransactionType;
import com.mycom.products.mywebsite.core.bean.config.RoleBean;
import com.mycom.products.mywebsite.core.dao.api.CommonGenericDao;
import com.mycom.products.mywebsite.core.dao.api.JoinedSelectableDao;
import com.mycom.products.mywebsite.core.exception.ConsistencyViolationException;
import com.mycom.products.mywebsite.core.exception.DAOException;
import com.mycom.products.mywebsite.core.exception.DuplicatedEntryException;
import com.mycom.products.mywebsite.core.exception.SaveHistoryFailedException;
import com.mycom.products.mywebsite.core.mapper.config.RoleMapper;
import com.mycom.products.mywebsite.core.util.FetchMode;

@Repository
public class RoleDao implements CommonGenericDao<RoleBean>, JoinedSelectableDao<RoleBean> {

    @Autowired
    private RoleMapper roleMapper;
    private static final Logger daoLogger = Logger.getLogger("daoLogs." + RoleDao.class.getName());

    @Override
    @CacheEvict(value = "ConfigurationCache.Role", allEntries = true)
    public long insert(RoleBean role, long recordRegId) throws DAOException, DuplicatedEntryException {
	try {
	    daoLogger.debug("[START] : >>> --- Inserting single 'Role' informations ---");
	    LocalDateTime now = LocalDateTime.now();
	    role.setRecordRegDate(now);
	    role.setRecordUpdDate(now);
	    role.setRecordRegId(recordRegId);
	    role.setRecordUpdId(recordRegId);
	    role.setTransactionType(TransactionType.INSERT);
	    roleMapper.insert(role);
	    daoLogger.debug("[HISTORY][START] : $1 --- Save 'Role' informations in history after successfully inserted in major table ---");
	    roleMapper.insertSingleHistoryRecord(role);
	    daoLogger.debug("[HISTORY][FINISH] : $1 --- Save 'Role' informations in history ---");
	} catch (DuplicateKeyException e) {
	    String errorMsg = "xxx Insertion process was failed due to Unique Key constraint xxx";
	    throw new DuplicatedEntryException(errorMsg, e);
	} catch (SaveHistoryFailedException e) {
	    String errorMsg = "xxx Error occured while saving 'Role' informations in history for later tracking xxx";
	    throw new SaveHistoryFailedException(errorMsg, e.getCause());
	} catch (Exception e) {
	    String errorMsg = "xxx Error occured while inserting 'Role' data ==> " + role + " xxx";
	    throw new DAOException(errorMsg, e);
	}
	daoLogger.debug("[FINISH] : <<< --- Inserting single 'Role' informations with new Id # " + role.getId() + " ---");
	return role.getId();
    }

    @Override
    @CacheEvict(value = "ConfigurationCache.Role", allEntries = true)
    public void insert(List<RoleBean> roles, long recordRegId) throws DAOException, DuplicatedEntryException {
	daoLogger.debug("[START] : >>> --- Inserting multi 'Role' informations ---");
	LocalDateTime now = LocalDateTime.now();
	for (RoleBean role : roles) {
	    role.setRecordRegDate(now);
	    role.setRecordUpdDate(now);
	    role.setRecordRegId(recordRegId);
	    role.setRecordUpdId(recordRegId);
	    role.setTransactionType(TransactionType.INSERT);
	}
	try {
	    roleMapper.insertList(roles);
	    daoLogger.debug("[HISTORY][START] : $1 --- Save 'Role' informations in history after successfully inserted in major table ---");
	    roleMapper.insertMultiHistoryRecords(roles);
	    daoLogger.debug("[HISTORY][FINISH] : $1 --- Save 'Role' informations in history ---");
	} catch (DuplicateKeyException e) {
	    String errorMsg = "xxx Insertion process was failed due to Unique Key constraint. xxx";
	    throw new DuplicatedEntryException(errorMsg, e);
	} catch (SaveHistoryFailedException e) {
	    String errorMsg = "xxx Error occured while saving 'Role' informations in history for later tracking xxx";
	    throw new SaveHistoryFailedException(errorMsg, e.getCause());
	} catch (Exception e) {
	    String errorMsg = "xxx Error occured while inserting multi 'Role' datas ==> " + roles + " xxx";
	    throw new DAOException(errorMsg, e);
	}
	daoLogger.debug("[FINISH] : <<< --- Inserting multi 'Role' informations ---");
    }

    @Override
    @CacheEvict(value = "ConfigurationCache.Role", allEntries = true)
    public long update(RoleBean role, long recordUpdId) throws DAOException, DuplicatedEntryException {
	long totalEffectedRows = 0;
	daoLogger.debug("[START] : >>> --- Updating single 'Role' informations ---");
	try {
	    role.setRecordUpdId(recordUpdId);
	    daoLogger.debug("[HISTORY][START] : $1 --- Save 'Role' informations in history before update on major table ---");
	    RoleBean oldData = roleMapper.selectByPrimaryKey(role.getId(), FetchMode.LAZY);
	    if (oldData == null) {
		throw new SaveHistoryFailedException();
	    }
	    oldData.setTransactionType(TransactionType.UPDATE);
	    oldData.setRecordUpdId(recordUpdId);
	    roleMapper.insertSingleHistoryRecord(oldData);
	    daoLogger.debug("[HISTORY][FINISH] : $1 --- Save 'Role' informations in history ---");
	    totalEffectedRows = roleMapper.update(role);
	} catch (DuplicateKeyException e) {
	    String errorMsg = "xxx Updating process was failed due to Unique Key constraint xxx";
	    throw new DuplicatedEntryException(errorMsg, e);
	} catch (SaveHistoryFailedException e) {
	    String errorMsg = "xxx Error occured while saving 'Role' informations in history for later tracking xxx";
	    throw new SaveHistoryFailedException(errorMsg, e.getCause());
	} catch (Exception e) {
	    String errorMsg = "xxx Error occured while updating 'Role' data ==> " + role + " xxx";
	    throw new DAOException(errorMsg, e);
	}
	daoLogger.debug("[FINISH] : <<< --- Updating single 'Role' informations with Id # " + role.getId() + " ---");
	return totalEffectedRows;
    }

    @Override
    @CacheEvict(value = "ConfigurationCache.Role", allEntries = true)
    public void update(List<RoleBean> roles, long recordUpdId) throws DAOException, DuplicatedEntryException {
	daoLogger.debug("[START] : >>> --- Updating multi 'Role' informations ---");
	for (RoleBean role : roles) {
	    try {
		role.setRecordUpdId(recordUpdId);
		daoLogger.debug("[HISTORY][START] : $1 --- Save 'Role' informations in history before update on major table ---");
		RoleBean oldData = roleMapper.selectByPrimaryKey(role.getId(), FetchMode.LAZY);
		if (oldData == null) {
		    throw new SaveHistoryFailedException();
		}
		oldData.setTransactionType(TransactionType.UPDATE);
		oldData.setRecordUpdId(recordUpdId);
		roleMapper.insertSingleHistoryRecord(oldData);
		daoLogger.debug("[HISTORY][FINISH] : $1 --- Save 'Role' informations in history ---");
		roleMapper.update(role);
	    } catch (DuplicateKeyException e) {
		String errorMsg = "xxx Updating process was failed due to Unique Key constraint xxx";
		throw new DuplicatedEntryException(errorMsg, e);
	    } catch (SaveHistoryFailedException e) {
		String errorMsg = "xxx Error occured while saving 'Role' informations in history for later tracking xxx";
		throw new SaveHistoryFailedException(errorMsg, e.getCause());
	    } catch (Exception e) {
		String errorMsg = "xxx Error occured while updating 'Role' data ==> " + role + " xxx";
		throw new DAOException(errorMsg, e);
	    }
	}
	daoLogger.debug("[FINISH] : <<< --- Updating multi 'Role' informations ---");
    }

    @Override
    @CacheEvict(value = "ConfigurationCache.Role", allEntries = true)
    public long update(HashMap<String, Object> criteria, HashMap<String, Object> updateItems, long recordUpdId)
	    throws DAOException, DuplicatedEntryException {
	long totalEffectedRows = 0;
	daoLogger.debug("[START] : >>> --- Updating multi 'Role' informations with criteria ---");
	try {
	    criteria.put("recordUpdId", recordUpdId);
	    daoLogger.debug("[HISTORY][START] : $1 --- Save 'Role' informations in history before update on major table ---");
	    List<RoleBean> roles = roleMapper.selectMultiRecords(criteria, FetchMode.LAZY);
	    if (roles == null) {
		throw new SaveHistoryFailedException();
	    } else if (roles != null && roles.size() > 0) {
		for (RoleBean role : roles) {
		    role.setTransactionType(TransactionType.UPDATE);
		    role.setRecordUpdId(recordUpdId);
		    roleMapper.insertSingleHistoryRecord(role);
		}
	    }
	    daoLogger.debug("[HISTORY][FINISH] : $1 --- Save 'Role' informations in history ---");
	    totalEffectedRows = roleMapper.updateWithCriteria(criteria, updateItems);
	} catch (DuplicateKeyException e) {
	    String errorMsg = "xxx Updating process was failed due to Unique Key constraint xxx";
	    throw new DuplicatedEntryException(errorMsg, e);
	} catch (SaveHistoryFailedException e) {
	    String errorMsg = "xxx Error occured while saving 'Role' informations in history for later tracking xxx";
	    throw new SaveHistoryFailedException(errorMsg, e.getCause());
	} catch (Exception e) {
	    String errorMsg = "xxx Error occured while updating multiple 'Role' informations [Values] ==> " + updateItems
		    + " with [Criteria] ==> " + criteria + " xxx";
	    throw new DAOException(errorMsg, e);
	}
	daoLogger.debug("[FINISH] : <<< --- Updating multi 'Role' informations with criteria ---");
	return totalEffectedRows;
    }

    @Override
    @CacheEvict(value = "ConfigurationCache.Role", allEntries = true)
    public long delete(long primaryKey, long recordUpdId) throws DAOException, ConsistencyViolationException {
	daoLogger.debug("[START] : >>> --- Deleting single 'Role' informations with primaryKey # " + primaryKey + " ---");
	long totalEffectedRows = 0;
	try {
	    daoLogger.debug("[HISTORY][START] : $1 --- Save 'Role' informations in history before update on major table ---");
	    RoleBean oldData = roleMapper.selectByPrimaryKey(primaryKey, FetchMode.LAZY);
	    if (oldData == null) {
		throw new SaveHistoryFailedException();
	    }
	    oldData.setTransactionType(TransactionType.UPDATE);
	    oldData.setRecordUpdId(recordUpdId);
	    roleMapper.insertSingleHistoryRecord(oldData);
	    daoLogger.debug("[HISTORY][FINISH] : $1 --- Save 'Role' informations in history ---");
	    totalEffectedRows = roleMapper.deleteByPrimaryKey(primaryKey);
	} catch (DataIntegrityViolationException e) {
	    String errorMsg = "xxx Rejected : Deleting process was failed because this entity was connected with other resources.If you try to forcely remove it, entire database will loose consistency xxx";
	    throw new ConsistencyViolationException(errorMsg, e);
	} catch (SaveHistoryFailedException e) {
	    String errorMsg = "xxx Error occured while saving 'Role' informations in history for later tracking xxx";
	    throw new SaveHistoryFailedException(errorMsg, e.getCause());
	} catch (Exception e) {
	    String errorMsg = "xxx Error occured while deleting 'Role' data with primaryKey ==> " + primaryKey + " xxx";
	    throw new DAOException(errorMsg, e);
	}
	daoLogger.debug("[FINISH] : <<< --- Deleting single 'Role' informations with primaryKey # " + primaryKey + " ---");
	return totalEffectedRows;
    }

    @Override
    @CacheEvict(value = "ConfigurationCache.Role", allEntries = true)
    public long delete(Map<String, Object> criteria, long recordUpdId) throws DAOException, ConsistencyViolationException {
	long totalEffectedRows = 0;
	daoLogger.debug("[START] : >>> --- Deleting 'Role' informations with criteria  ---");
	try {
	    daoLogger.debug("[HISTORY][START] : $1 --- Save 'Role' informations in history before delete on major table ---");
	    List<RoleBean> roles = roleMapper.selectMultiRecords(criteria, FetchMode.LAZY);
	    if (roles == null) {
		throw new SaveHistoryFailedException();
	    } else if (roles != null && roles.size() > 0) {
		for (RoleBean role : roles) {
		    role.setTransactionType(TransactionType.DELETE);
		    role.setRecordUpdId(recordUpdId);
		}
		roleMapper.insertMultiHistoryRecords(roles);
	    }
	    daoLogger.debug("[HISTORY][FINISH] : $1 --- Save 'Role' informations in history ---");
	    totalEffectedRows = roleMapper.deleteByCriteria(criteria);
	} catch (DataIntegrityViolationException e) {
	    String errorMsg = "xxx Rejected : Deleting process was failed because this entity was connected with other resources.If you try to forcely remove it, entire database will loose consistency xxx";
	    throw new ConsistencyViolationException(errorMsg, e);
	} catch (SaveHistoryFailedException e) {
	    String errorMsg = "xxx Error occured while saving 'Role' informations in history for later tracking xxx";
	    throw new SaveHistoryFailedException(errorMsg, e.getCause());
	} catch (Exception e) {
	    String errorMsg = "xxx Error occured while deleting 'Role' data with criteria ==> " + criteria + " xxx";
	    throw new DAOException(errorMsg, e);
	}
	daoLogger.debug("[FINISH] : <<< --- Deleting 'Role' informations with criteria  ---");
	return totalEffectedRows;
    }

    @Override
    @Cacheable(value = "ConfigurationCache.Role")
    public RoleBean select(long primaryKey, FetchMode fetchMode) throws DAOException {
	daoLogger.debug("[START] : >>> --- Fetching 'Role' informations with primaryKey # " + primaryKey + " ---");
	RoleBean role = new RoleBean();
	try {
	    role = roleMapper.selectByPrimaryKey(primaryKey, fetchMode);
	} catch (Exception e) {
	    String errorMsg = "xxx Error occured while fetching single 'Role' informations with primaryKey ==> " + primaryKey + " xxx";
	    throw new DAOException(errorMsg, e);
	}
	daoLogger.debug("[FINISH] : <<< --- Fetching 'Role' informations with primaryKey # " + primaryKey + " ---");
	return role;
    }

    @Override
    @Cacheable(value = "ConfigurationCache.Role")
    public RoleBean select(Map<String, Object> criteria, FetchMode fetchMode) throws DAOException {
	daoLogger.debug("[START] : >>> --- Fetching single 'Role' informations with criteria ---");
	RoleBean role = new RoleBean();
	try {
	    role = roleMapper.selectSingleRecord(criteria, fetchMode);
	} catch (Exception e) {
	    String errorMsg = "xxx Error occured while fetching single 'Role' informations with criteria ==> " + criteria + " xxx";
	    throw new DAOException(errorMsg, e);
	}
	daoLogger.debug("[FINISH] : <<< --- Fetching single 'Role' informations with criteria ---");
	return role;
    }

    @Override
    @Cacheable(value = "ConfigurationCache.Role")
    public List<RoleBean> selectList(Map<String, Object> criteria, FetchMode fetchMode) throws DAOException {
	daoLogger.debug("[START] : >>> --- Fetching multi 'Role' informations with criteria ---");
	List<RoleBean> roles = null;
	try {
	    roles = roleMapper.selectMultiRecords(criteria, fetchMode);
	} catch (Exception e) {
	    String errorMsg = "xxx Error occured while fetching multiple 'Role' informations with criteria ==> " + criteria + " xxx";
	    throw new DAOException(errorMsg, e);
	}
	daoLogger.debug("[FINISH] : <<< --- Fetching multi 'Role' informations with criteria ---");
	return roles;
    }

    @Override
    @Cacheable(value = "ConfigurationCache.Role")
    public long selectCounts(Map<String, Object> criteria, FetchMode fetchMode) throws DAOException {
	daoLogger.debug("[START] : >>> --- Fetching 'Role' counts with criteria ---");
	long count = 0;
	try {
	    count = roleMapper.selectCounts(criteria, fetchMode);
	} catch (Exception e) {
	    String errorMsg = "xxx Error occured while counting 'Role' records with criteria ==> " + criteria + " xxx";
	    throw new DAOException(errorMsg, e);
	}
	daoLogger.debug("[FINISH] : <<< --- Fetching 'Role' counts with criteria ---");
	return count;
    }
}
