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
 *	mywebsite-core - RoleActionDao.java
 *	Using JRE 1.8.0_121
 *	@author Than Htike Aung {@literal <rage.cataclysm@gmail.com>}
 *	@Since 2017
 * 
 */
package com.mycom.products.mywebsite.core.dao.config;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

import com.mycom.products.mywebsite.core.bean.config.RoleActionBean;
import com.mycom.products.mywebsite.core.dao.api.XGenericDao;
import com.mycom.products.mywebsite.core.exception.ConsistencyViolationException;
import com.mycom.products.mywebsite.core.exception.DAOException;
import com.mycom.products.mywebsite.core.exception.DuplicatedEntryException;
import com.mycom.products.mywebsite.core.mapper.config.RoleActionMapper;
import com.mycom.products.mywebsite.core.util.FetchMode;

@Repository
public class RoleActionDao implements XGenericDao<RoleActionBean> {

    @Autowired
    private RoleActionMapper roleActionMapper;
    private static final Logger daoLogger = Logger.getLogger("daoLogs." + RoleActionDao.class.getName());

    @Override
    @CacheEvict(value = { "ConfigurationCache.RoleAction", "ConfigurationCache.Role", "ConfigurationCache.Action" }, allEntries = true)
    public void insert(RoleActionBean roleAction, long recordRegId) throws DAOException, DuplicatedEntryException {
	try {
	    daoLogger.debug("[START] : >>> --- Inserting single 'RoleAction' informations ---");
	    LocalDateTime now = LocalDateTime.now();
	    roleAction.setRecordRegDate(now);
	    roleAction.setRecordUpdDate(now);
	    roleAction.setRecordRegId(recordRegId);
	    roleAction.setRecordUpdId(recordRegId);
	    roleActionMapper.insert(roleAction);
	} catch (DuplicateKeyException e) {
	    String errorMsg = "xxx Insertion process was failed due to Unique Key constraint xxx";
	    throw new DuplicatedEntryException(errorMsg, e);
	} catch (Exception e) {
	    String errorMsg = "xxx Error occured while inserting 'RoleAction' data ==> " + roleAction + " xxx";
	    throw new DAOException(errorMsg, e);
	}
	daoLogger.debug("[FINISH] : <<< --- Inserting single 'RoleAction' informations ---");
    }

    @Override
    public void insert(List<RoleActionBean> roleActions, long recordRegId) throws DAOException, DuplicatedEntryException {
	daoLogger.debug("[START] : >>> --- Inserting multi 'RoleAction' informations ---");
	LocalDateTime now = LocalDateTime.now();
	for (RoleActionBean roleAction : roleActions) {
	    roleAction.setRecordRegDate(now);
	    roleAction.setRecordUpdDate(now);
	    roleAction.setRecordRegId(recordRegId);
	    roleAction.setRecordUpdId(recordRegId);
	}
	try {
	    roleActionMapper.insertList(roleActions);
	} catch (DuplicateKeyException e) {
	    String errorMsg = "xxx Insertion process was failed due to Unique Key constraint. xxx";
	    throw new DuplicatedEntryException(errorMsg, e);
	} catch (Exception e) {
	    String errorMsg = "xxx Error occured while inserting 'RoleAction' datas ==> " + roleActions + " xxx";
	    throw new DAOException(errorMsg, e);
	}
	daoLogger.debug("[FINISH] : <<< --- Inserting multi 'RoleAction' informations ---");
    }

    @Override
    @CacheEvict(value = { "ConfigurationCache.RoleAction", "ConfigurationCache.Role", "ConfigurationCache.Action" }, allEntries = true)
    public void insert(long roleId, long actionId, long recordRegId) throws DuplicatedEntryException, DAOException {
	daoLogger.debug("[START] : >>> --- Inserting single 'RoleAction' informations ---");
	try {
	    roleActionMapper.insertWithRelatedKeys(roleId, actionId, recordRegId);
	} catch (DuplicateKeyException e) {
	    String errorMsg = "xxx Insertion process was failed due to Unique Key constraint xxx";
	    throw new DuplicatedEntryException(errorMsg, e);
	} catch (Exception e) {
	    String errorMsg = "xxx Error occured while inserting 'RoleAction' data ==> roleId = " + roleId + " , actionId = " + actionId
		    + " xxx";
	    throw new DAOException(errorMsg, e);
	}
	daoLogger.debug("[FINISH] : <<< --- Inserting single 'RoleAction' informations ---");
    }

    @Override
    @CacheEvict(value = { "ConfigurationCache.RoleAction", "ConfigurationCache.Role", "ConfigurationCache.Action" }, allEntries = true)
    public long delete(long roleId, long actionId, long recordUpdId) throws ConsistencyViolationException, DAOException {
	daoLogger.debug("[START] : >>> --- Deleting single 'RoleAction' informations with ==> roleId " + roleId + " , actionId = "
		+ actionId + " ---");
	long effectedRows = 0;
	try {
	    effectedRows = roleActionMapper.deleteByKeys(roleId, actionId);
	} catch (DataIntegrityViolationException e) {
	    String errorMsg = "xxx Rejected : Deleting process was failed because this entity was connected with other resources.If you try to forcely remove it, entire database will loose consistency xxx";
	    throw new ConsistencyViolationException(errorMsg, e);
	} catch (Exception e) {
	    String errorMsg = "xxx Error occured while deleting 'RoleAction' data with ==> roleId = " + roleId + " , actionId = " + actionId
		    + " xxx";
	    throw new DAOException(errorMsg, e);
	}
	daoLogger.debug("[FINISH] : <<< --- Deleting single 'RoleAction' informations with ==> roleId " + roleId + " , actionId = "
		+ actionId + " ---");
	return effectedRows;
    }

    @Override
    @CacheEvict(value = { "ConfigurationCache.RoleAction", "ConfigurationCache.Role", "ConfigurationCache.Action" }, allEntries = true)
    public long delete(Map<String, Object> criteria, long recordUpdId) throws ConsistencyViolationException, DAOException {
	daoLogger.debug("[START] : >>> --- Deleting 'RoleAction' informations with criteria  ---");
	long effectedRows = 0;
	try {
	    effectedRows = roleActionMapper.deleteByCriteria(criteria);
	} catch (DataIntegrityViolationException e) {
	    String errorMsg = "xxx Rejected : Deleting process was failed because this entity was connected with other resources.If you try to forcely remove it, entire database will loose consistency xxx";
	    throw new ConsistencyViolationException(errorMsg, e);
	} catch (Exception e) {
	    String errorMsg = "xxx Error occured while deleting 'RoleAction' data with criteria ==> " + criteria + " xxx";
	    throw new DAOException(errorMsg, e);
	}
	daoLogger.debug("[FINISH] : <<< --- Deleting 'RoleAction' informations with criteria  ---");
	return effectedRows;
    }

    @Override
    @CacheEvict(value = { "ConfigurationCache.RoleAction", "ConfigurationCache.Role", "ConfigurationCache.Action" }, allEntries = true)
    public void merge(long roleId, List<Long> actionIds, long recordUpdId)
	    throws DuplicatedEntryException, ConsistencyViolationException, DAOException {
	daoLogger.debug("[START] : >>> --- Merging  'RoleAction' informations for roleId # =" + roleId + " with related actionIds ="
		+ actionIds + " ---");
	List<Long> insertIds = new ArrayList<>();
	List<Long> removeIds = new ArrayList<>();
	daoLogger.debug("[START] : $1 --- Fetching old related actionIds for roleId # " + roleId + " ---");
	List<Long> oldRelatedActions = selectByKey1(roleId);
	daoLogger.debug("[FINISH] : $1 --- Fetching old related actionIds for roleId # " + roleId + " ==> " + oldRelatedActions + " ---");
	if (oldRelatedActions != null && oldRelatedActions.size() > 0) {
	    for (long actionId : actionIds) {
		if (!oldRelatedActions.contains(actionId)) {
		    insertIds.add(actionId);
		}
	    }

	    for (long actionId : oldRelatedActions) {
		if (!actionIds.contains(actionId)) {
		    removeIds.add(actionId);
		}
	    }
	} else {
	    insertIds = actionIds;
	}
	if (removeIds.size() > 0) {
	    daoLogger.debug("[START] : $2 --- Removing  related actionIds " + removeIds + " for roleId # " + roleId
		    + " these have been no longer used  ---");
	    HashMap<String, Object> criteria = new HashMap<>();
	    criteria.put("roleId", roleId);
	    criteria.put("actionIds", removeIds);
	    roleActionMapper.deleteByCriteria(criteria);
	    daoLogger.debug("[FINISH] : $2 --- Removing  related actionIds " + removeIds + " for roleId # " + roleId
		    + " these have been no longer used  ---");
	}

	if (insertIds.size() > 0) {
	    daoLogger.debug("[START] : $3 --- Inserting newly selected actionIds " + insertIds + " for roleId # " + roleId + " ---");
	    List<RoleActionBean> roleActions = new ArrayList<>();
	    for (Long actionId : insertIds) {
		RoleActionBean roleAction = new RoleActionBean(roleId, actionId);
		roleActions.add(roleAction);
	    }
	    LocalDateTime now = LocalDateTime.now();
	    for (RoleActionBean roleAction : roleActions) {
		roleAction.setRecordRegDate(now);
		roleAction.setRecordUpdDate(now);
		roleAction.setRecordRegId(recordUpdId);
		roleAction.setRecordUpdId(recordUpdId);
	    }
	    roleActionMapper.insertList(roleActions);
	    daoLogger.debug("[FINISH] : $3 --- Inserting newly selected actionIds " + insertIds + " for roleId # " + roleId + " ---");
	}

	daoLogger.debug("[FINISH] : <<< --- Merging 'RoleAction' informations for roleId # =" + roleId + " with related actionIds ="
		+ actionIds.toString() + " ---");

    }

    @Override
    @CacheEvict(value = { "ConfigurationCache.RoleAction", "ConfigurationCache.Role", "ConfigurationCache.Action" }, allEntries = true)
    public void merge(List<Long> roleIds, long actionId, long recordUpdId)
	    throws DuplicatedEntryException, ConsistencyViolationException, DAOException {
	daoLogger.debug("[START] : >>> --- Merging  'RoleAction' informations for actionId # =" + actionId + " with related roleIds ="
		+ roleIds + " ---");
	List<Long> insertIds = new ArrayList<>();
	List<Long> removeIds = new ArrayList<>();
	daoLogger.debug("[START] : $1 --- Fetching old related roleIds for actionId # " + actionId + " ---");
	List<Long> oldRelatedRoleIds = selectByKey2(actionId);
	daoLogger.debug("[FINISH] : $1 --- Fetching old related roleIds for actionId # " + actionId + " ==> " + oldRelatedRoleIds + " ---");
	if (oldRelatedRoleIds != null && oldRelatedRoleIds.size() > 0) {
	    for (long roleId : roleIds) {
		if (!oldRelatedRoleIds.contains(roleId)) {
		    insertIds.add(roleId);
		}
	    }

	    for (long roleId : oldRelatedRoleIds) {
		if (!roleIds.contains(roleId)) {
		    removeIds.add(roleId);
		}
	    }
	} else {
	    insertIds = roleIds;
	}
	if (removeIds.size() > 0) {
	    daoLogger.debug("[START] : $2 --- Removing  related roleIds " + removeIds + " for actionId # " + actionId
		    + " these have been no longer used  ---");
	    HashMap<String, Object> criteria = new HashMap<>();
	    criteria.put("actionId", actionId);
	    criteria.put("roleIds", removeIds);
	    roleActionMapper.deleteByCriteria(criteria);
	    daoLogger.debug("[FINISH] : $2 --- Removing  related roleIds " + removeIds + " for actionId # " + actionId
		    + " these have been no longer used  ---");
	}

	if (insertIds.size() > 0) {
	    daoLogger.debug("[START] : $3 --- Inserting newly selected roleIds " + insertIds + " for actionId # " + actionId + " ---");
	    List<RoleActionBean> roleActions = new ArrayList<>();
	    for (Long roleId : insertIds) {
		RoleActionBean roleAction = new RoleActionBean(roleId, actionId);
		roleActions.add(roleAction);
	    }
	    LocalDateTime now = LocalDateTime.now();
	    for (RoleActionBean roleAction : roleActions) {
		roleAction.setRecordRegDate(now);
		roleAction.setRecordUpdDate(now);
		roleAction.setRecordRegId(recordUpdId);
		roleAction.setRecordUpdId(recordUpdId);
	    }
	    roleActionMapper.insertList(roleActions);
	    daoLogger.debug("[FINISH] : $3 --- Inserting newly selected roleIds " + insertIds + " for actionId # " + actionId + " ---");
	}

	daoLogger.debug("[FINISH] : <<< --- Merging 'RoleAction' informations for actionId # =" + actionId + " with related roleIds ="
		+ roleIds.toString() + " ---");

    }

    @Override
    @Cacheable(value = "ConfigurationCache.RoleAction")
    public List<Long> selectByKey1(long key1) throws DAOException {
	daoLogger.debug("[START] : >>> --- Fetching related actionIds with roleId # " + key1 + " ---");
	List<Long> actionIds = null;
	try {
	    Map<String, Object> criteria = new HashMap<>();
	    criteria.put("roleId", key1);
	    actionIds = roleActionMapper.selectRelatedKeys(criteria);
	} catch (Exception e) {
	    String errorMsg = "xxx Error occured while fetching related 'Action' keys with roleId ==> " + key1 + " xxx";
	    throw new DAOException(errorMsg, e);
	}
	daoLogger.debug("[FINISH] : <<< --- Fetching related actionIds with roleId # " + key1 + " ---");
	return actionIds;
    }

    @Override
    @Cacheable(value = "ConfigurationCache.RoleAction")
    public List<Long> selectByKey2(long key2) throws DAOException {
	daoLogger.debug("[START] : >>> --- Fetching related roleIds with actionId # " + key2 + " ---");
	List<Long> roleIds = null;
	try {
	    Map<String, Object> criteria = new HashMap<>();
	    criteria.put("actionId", key2);
	    roleIds = roleActionMapper.selectRelatedKeys(criteria);
	} catch (Exception e) {
	    String errorMsg = "xxx Error occured while fetching related 'Role' keys with actionId ==> " + key2 + " xxx";
	    throw new DAOException(errorMsg, e);
	}
	daoLogger.debug("[FINISH] : <<< --- Fetching related roleIds with actionId # " + key2 + " ---");
	return roleIds;
    }

    @Override
    @Cacheable(value = "ConfigurationCache.RoleAction")
    public RoleActionBean select(long roleId, long actionId, FetchMode fetchMode) throws DAOException {
	daoLogger.debug("[START] : >>> --- Fetching single 'RoleAction' informations with ==> roleId = " + roleId + " , actionId = "
		+ actionId + " ---");
	RoleActionBean roleAction = null;
	try {
	    // Noticed : we don't allow for filtering from joined
	    // tables.FetchMode is just only for eager or lazy loading
	    roleAction = roleActionMapper.selectByKeys(roleId, actionId, fetchMode);
	} catch (Exception e) {
	    String errorMsg = "xxx Error occured while fetching single 'RoleAction' informations with ==> roleId = " + roleId
		    + " , actionId = " + actionId + " xxx";
	    throw new DAOException(errorMsg, e);
	}
	daoLogger.debug("[FINISH] : <<< --- Fetching single 'RoleAction' informations with ==> roleId = " + roleId + " , actionId = "
		+ actionId + " ---");
	return roleAction;
    }

    @Override
    @Cacheable(value = "ConfigurationCache.RoleAction")
    public List<RoleActionBean> selectList(Map<String, Object> criteria, FetchMode fetchMode) throws DAOException {
	daoLogger.debug("[START] : >>> --- Fetching multi 'RoleAction' informations with criteria ---");
	List<RoleActionBean> results = null;
	try {
	    // Noticed : we don't allow for filtering from joined
	    // tables.FetchMode is just only for eager or lazy loading
	    results = roleActionMapper.selectMultiRecords(criteria, fetchMode);
	} catch (Exception e) {
	    String errorMsg = "xxx Error occured while fetching multiple 'RoleAction' informations with criteria ==> " + criteria + " xxx";
	    throw new DAOException(errorMsg, e);
	}
	daoLogger.debug("[FINISH] : <<< --- Fetching multi 'RoleAction' informations with criteria ---");
	return results;
    }

    @Override
    @Cacheable(value = "ConfigurationCache.RoleAction")
    public long selectCounts(Map<String, Object> criteria) throws DAOException {
	daoLogger.debug("[START] : >>> --- Fetching 'RoleAction' counts with criteria ---");
	long count = 0;
	try {
	    // we don't allow for filtering from joined tables
	    count = roleActionMapper.selectCounts(criteria, null);
	} catch (Exception e) {
	    String errorMsg = "xxx Error occured while counting 'RoleAction' records with criteria ==> " + criteria + " xxx";
	    throw new DAOException(errorMsg, e);
	}
	daoLogger.debug("[FINISH] : <<< --- Fetching 'RoleAction' counts with criteria ---");
	return count;
    }
}
