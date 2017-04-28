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
 *	mywebsite-core - StaticContentDao.java
 *	Using JRE 1.8.0_121
 *	@author Than Htike Aung {@literal <rage.cataclysm@gmail.com>}
 *	@Since 2017
 * 
 */
package com.mycom.products.mywebsite.core.dao.master;

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
import com.mycom.products.mywebsite.core.bean.master.StaticContentBean;
import com.mycom.products.mywebsite.core.dao.api.CommonGenericDao;
import com.mycom.products.mywebsite.core.dao.api.StandAloneSelectableDao;
import com.mycom.products.mywebsite.core.exception.ConsistencyViolationException;
import com.mycom.products.mywebsite.core.exception.DAOException;
import com.mycom.products.mywebsite.core.exception.DuplicatedEntryException;
import com.mycom.products.mywebsite.core.exception.SaveHistoryFailedException;
import com.mycom.products.mywebsite.core.mapper.master.StaticContentMapper;

@Repository
public class StaticContentDao implements CommonGenericDao<StaticContentBean>, StandAloneSelectableDao<StaticContentBean> {

    @Autowired
    private StaticContentMapper staticContentMapper;
    private static final Logger daoLogger = Logger.getLogger("daoLogs." + StaticContentDao.class.getName());

    @Override
    @CacheEvict(value = "MasterCache.StaticContent", allEntries = true)
    public long insert(StaticContentBean staticContent, long recordRegId) throws DAOException, DuplicatedEntryException {
	try {
	    daoLogger.debug("[START] : >>> --- Inserting single 'StaticContent' informations ---");
	    LocalDateTime now = LocalDateTime.now();
	    staticContent.setRecordRegDate(now);
	    staticContent.setRecordUpdDate(now);
	    staticContent.setRecordRegId(recordRegId);
	    staticContent.setRecordUpdId(recordRegId);
	    staticContent.setTransactionType(TransactionType.INSERT);
	    staticContentMapper.insert(staticContent);
	    daoLogger.debug(
		    "[HISTORY][START] : $1 --- Save 'StaticContent' informations in history after successfully inserted in major table ---");
	    staticContentMapper.insertSingleHistoryRecord(staticContent);
	    daoLogger.debug("[HISTORY][FINISH] : $1 --- Save 'StaticContent' informations in history ---");
	} catch (DuplicateKeyException e) {
	    String errorMsg = "xxx Insertion process was failed due to Unique Key constraint xxx";
	    throw new DuplicatedEntryException(errorMsg, e);
	} catch (SaveHistoryFailedException e) {
	    String errorMsg = "xxx Error occured while saving 'StaticContent' informations in history for later tracking xxx";
	    throw new SaveHistoryFailedException(errorMsg, e.getCause());
	} catch (Exception e) {
	    String errorMsg = "xxx Error occured while inserting 'StaticContent' data ==> " + staticContent + " xxx";
	    throw new DAOException(errorMsg, e);
	}
	daoLogger.debug("[FINISH] : <<< --- Inserting single 'StaticContent' informations with new Id # " + staticContent.getId() + " ---");
	return staticContent.getId();
    }

    @Override
    @CacheEvict(value = "MasterCache.StaticContent", allEntries = true)
    public void insert(List<StaticContentBean> staticContents, long recordRegId) throws DAOException, DuplicatedEntryException {
	daoLogger.debug("[START] : >>> --- Inserting multi 'StaticContent' informations ---");
	LocalDateTime now = LocalDateTime.now();
	for (StaticContentBean staticContent : staticContents) {
	    staticContent.setRecordRegDate(now);
	    staticContent.setRecordUpdDate(now);
	    staticContent.setRecordRegId(recordRegId);
	    staticContent.setRecordUpdId(recordRegId);
	    staticContent.setTransactionType(TransactionType.INSERT);
	}
	try {
	    staticContentMapper.insertList(staticContents);
	    daoLogger.debug(
		    "[HISTORY][START] : $1 --- Save 'StaticContent' informations in history after successfully inserted in major table ---");
	    staticContentMapper.insertMultiHistoryRecords(staticContents);
	    daoLogger.debug("[HISTORY][FINISH] : $1 --- Save 'StaticContent' informations in history ---");
	} catch (DuplicateKeyException e) {
	    String errorMsg = "xxx Insertion process was failed due to Unique Key constraint. xxx";
	    throw new DuplicatedEntryException(errorMsg, e);
	} catch (SaveHistoryFailedException e) {
	    String errorMsg = "xxx Error occured while saving 'StaticContent' informations in history for later tracking xxx";
	    throw new SaveHistoryFailedException(errorMsg, e.getCause());
	} catch (Exception e) {
	    String errorMsg = "xxx Error occured while inserting multi 'StaticContent' datas ==> " + staticContents + " xxx";
	    throw new DAOException(errorMsg, e);
	}
	daoLogger.debug("[FINISH] : <<< --- Inserting multi 'StaticContent' informations ---");
    }

    @Override
    @CacheEvict(value = "MasterCache.StaticContent", allEntries = true)
    public long update(StaticContentBean staticContent, long recordUpdId) throws DAOException, DuplicatedEntryException {
	long totalEffectedRows = 0;
	daoLogger.debug("[START] : >>> --- Updating single 'StaticContent' informations ---");
	try {
	    staticContent.setRecordUpdId(recordUpdId);
	    daoLogger.debug("[HISTORY][START] : $1 --- Save 'StaticContent' informations in history before update on major table ---");
	    StaticContentBean oldData = staticContentMapper.selectByPrimaryKey(staticContent.getId());
	    if (oldData == null) {
		throw new SaveHistoryFailedException();
	    }
	    oldData.setTransactionType(TransactionType.UPDATE);
	    oldData.setRecordUpdId(recordUpdId);
	    staticContentMapper.insertSingleHistoryRecord(oldData);
	    daoLogger.debug("[HISTORY][FINISH] : $1 --- Save 'StaticContent' informations in history ---");
	    totalEffectedRows = staticContentMapper.update(staticContent);
	} catch (DuplicateKeyException e) {
	    String errorMsg = "xxx Updating process was failed due to Unique Key constraint xxx";
	    throw new DuplicatedEntryException(errorMsg, e);
	} catch (SaveHistoryFailedException e) {
	    String errorMsg = "xxx Error occured while saving 'StaticContent' informations in history for later tracking xxx";
	    throw new SaveHistoryFailedException(errorMsg, e.getCause());
	} catch (Exception e) {
	    String errorMsg = "xxx Error occured while updating 'StaticContent' data ==> " + staticContent + " xxx";
	    throw new DAOException(errorMsg, e);
	}
	daoLogger.debug("[FINISH] : <<< --- Updating single 'StaticContent' informations with Id # " + staticContent.getId() + " ---");
	return totalEffectedRows;
    }

    @Override
    @CacheEvict(value = "MasterCache.StaticContent", allEntries = true)
    public void update(List<StaticContentBean> staticContents, long recordUpdId) throws DAOException, DuplicatedEntryException {
	daoLogger.debug("[START] : >>> --- Updating multi 'StaticContent' informations ---");
	for (StaticContentBean staticContent : staticContents) {
	    try {
		staticContent.setRecordUpdId(recordUpdId);
		daoLogger.debug("[HISTORY][START] : $1 --- Save 'StaticContent' informations in history before update on major table ---");
		StaticContentBean oldData = staticContentMapper.selectByPrimaryKey(staticContent.getId());
		if (oldData == null) {
		    throw new SaveHistoryFailedException();
		}
		oldData.setTransactionType(TransactionType.UPDATE);
		oldData.setRecordUpdId(recordUpdId);
		staticContentMapper.insertSingleHistoryRecord(oldData);
		daoLogger.debug("[HISTORY][FINISH] : $1 --- Save 'StaticContent' informations in history ---");
		staticContentMapper.update(staticContent);
	    } catch (DuplicateKeyException e) {
		String errorMsg = "xxx Updating process was failed due to Unique Key constraint xxx";
		throw new DuplicatedEntryException(errorMsg, e);
	    } catch (SaveHistoryFailedException e) {
		String errorMsg = "xxx Error occured while saving 'StaticContent' informations in history for later tracking xxx";
		throw new SaveHistoryFailedException(errorMsg, e.getCause());
	    } catch (Exception e) {
		String errorMsg = "xxx Error occured while updating 'StaticContent' data ==> " + staticContent + " xxx";
		throw new DAOException(errorMsg, e);
	    }
	}
	daoLogger.debug("[FINISH] : <<< --- Updating multi 'StaticContent' informations ---");
    }

    @Override
    @CacheEvict(value = "MasterCache.StaticContent", allEntries = true)
    public long update(HashMap<String, Object> criteria, HashMap<String, Object> updateItems, long recordUpdId)
	    throws DAOException, DuplicatedEntryException {
	long totalEffectedRows = 0;
	daoLogger.debug("[START] : >>> --- Updating multi 'StaticContent' informations with criteria ---");
	try {
	    criteria.put("recordUpdId", recordUpdId);
	    daoLogger.debug("[HISTORY][START] : $1 --- Save 'StaticContent' informations in history before update on major table ---");
	    List<StaticContentBean> staticContents = staticContentMapper.selectMultiRecords(criteria);
	    if (staticContents == null) {
		throw new SaveHistoryFailedException();
	    } else if (staticContents != null && staticContents.size() > 0) {
		for (StaticContentBean staticContent : staticContents) {
		    staticContent.setTransactionType(TransactionType.UPDATE);
		    staticContent.setRecordUpdId(recordUpdId);
		    staticContentMapper.insertSingleHistoryRecord(staticContent);
		}
	    }
	    daoLogger.debug("[HISTORY][FINISH] : $1 --- Save 'StaticContent' informations in history ---");
	    totalEffectedRows = staticContentMapper.updateWithCriteria(criteria, updateItems);
	} catch (DuplicateKeyException e) {
	    String errorMsg = "xxx Updating process was failed due to Unique Key constraint xxx";
	    throw new DuplicatedEntryException(errorMsg, e);
	} catch (SaveHistoryFailedException e) {
	    String errorMsg = "xxx Error occured while saving 'StaticContent' informations in history for later tracking xxx";
	    throw new SaveHistoryFailedException(errorMsg, e.getCause());
	} catch (Exception e) {
	    String errorMsg = "xxx Error occured while updating multiple 'StaticContent' informations [Values] ==> " + updateItems
		    + " with [Criteria] ==> " + criteria + " xxx";
	    throw new DAOException(errorMsg, e);
	}
	daoLogger.debug("[FINISH] : <<< --- Updating multi 'StaticContent' informations with criteria ---");
	return totalEffectedRows;
    }

    @Override
    @CacheEvict(value = "MasterCache.StaticContent", allEntries = true)
    public long delete(long primaryKey, long recordUpdId) throws DAOException, ConsistencyViolationException {
	daoLogger.debug("[START] : >>> --- Deleting single 'StaticContent' informations with primaryKey # " + primaryKey + " ---");
	long totalEffectedRows = 0;
	try {
	    daoLogger.debug("[HISTORY][START] : $1 --- Save 'StaticContent' informations in history before update on major table ---");
	    StaticContentBean oldData = staticContentMapper.selectByPrimaryKey(primaryKey);
	    if (oldData == null) {
		throw new SaveHistoryFailedException();
	    }
	    oldData.setTransactionType(TransactionType.UPDATE);
	    oldData.setRecordUpdId(recordUpdId);
	    staticContentMapper.insertSingleHistoryRecord(oldData);
	    daoLogger.debug("[HISTORY][FINISH] : $1 --- Save 'StaticContent' informations in history ---");
	    totalEffectedRows = staticContentMapper.deleteByPrimaryKey(primaryKey);
	} catch (DataIntegrityViolationException e) {
	    String errorMsg = "xxx Rejected : Deleting process was failed because this entity was connected with other resources.If you try to forcely remove it, entire database will loose consistency xxx";
	    throw new ConsistencyViolationException(errorMsg, e);
	} catch (SaveHistoryFailedException e) {
	    String errorMsg = "xxx Error occured while saving 'StaticContent' informations in history for later tracking xxx";
	    throw new SaveHistoryFailedException(errorMsg, e.getCause());
	} catch (Exception e) {
	    String errorMsg = "xxx Error occured while deleting 'StaticContent' data with primaryKey ==> " + primaryKey + " xxx";
	    throw new DAOException(errorMsg, e);
	}
	daoLogger.debug("[FINISH] : <<< --- Deleting single 'StaticContent' informations with primaryKey # " + primaryKey + " ---");
	return totalEffectedRows;
    }

    @Override
    @CacheEvict(value = "MasterCache.StaticContent", allEntries = true)
    public long delete(Map<String, Object> criteria, long recordUpdId) throws DAOException, ConsistencyViolationException {
	long totalEffectedRows = 0;
	daoLogger.debug("[START] : >>> --- Deleting 'StaticContent' informations with criteria  ---");
	try {
	    daoLogger.debug("[HISTORY][START] : $1 --- Save 'StaticContent' informations in history before delete on major table ---");
	    List<StaticContentBean> staticContents = staticContentMapper.selectMultiRecords(criteria);
	    if (staticContents == null) {
		throw new SaveHistoryFailedException();
	    } else if (staticContents != null && staticContents.size() > 0) {
		for (StaticContentBean staticContent : staticContents) {
		    staticContent.setTransactionType(TransactionType.DELETE);
		    staticContent.setRecordUpdId(recordUpdId);
		}
		staticContentMapper.insertMultiHistoryRecords(staticContents);
	    }
	    daoLogger.debug("[HISTORY][FINISH] : $1 --- Save 'StaticContent' informations in history ---");
	    totalEffectedRows = staticContentMapper.deleteByCriteria(criteria);
	} catch (DataIntegrityViolationException e) {
	    String errorMsg = "xxx Rejected : Deleting process was failed because this entity was connected with other resources.If you try to forcely remove it, entire database will loose consistency xxx";
	    throw new ConsistencyViolationException(errorMsg, e);
	} catch (SaveHistoryFailedException e) {
	    String errorMsg = "xxx Error occured while saving 'StaticContent' informations in history for later tracking xxx";
	    throw new SaveHistoryFailedException(errorMsg, e.getCause());
	} catch (Exception e) {
	    String errorMsg = "xxx Error occured while deleting 'StaticContent' data with criteria ==> " + criteria + " xxx";
	    throw new DAOException(errorMsg, e);
	}
	daoLogger.debug("[FINISH] : <<< --- Deleting 'StaticContent' informations with criteria  ---");
	return totalEffectedRows;
    }

    @Override
    @Cacheable(value = "MasterCache.StaticContent")
    public StaticContentBean select(long primaryKey) throws DAOException {
	daoLogger.debug("[START] : >>> --- Fetching 'StaticContent' informations with primaryKey # " + primaryKey + " ---");
	StaticContentBean staticContent = new StaticContentBean();
	try {
	    staticContent = staticContentMapper.selectByPrimaryKey(primaryKey);
	} catch (Exception e) {
	    String errorMsg = "xxx Error occured while fetching single 'StaticContent' informations with primaryKey ==> " + primaryKey
		    + " xxx";
	    throw new DAOException(errorMsg, e);
	}
	daoLogger.debug("[FINISH] : <<< --- Fetching 'StaticContent' informations with primaryKey # " + primaryKey + " ---");
	return staticContent;
    }

    @Override
    @Cacheable(value = "MasterCache.StaticContent")
    public StaticContentBean select(Map<String, Object> criteria) throws DAOException {
	daoLogger.debug("[START] : >>> --- Fetching single 'StaticContent' informations with criteria ---");
	StaticContentBean staticContent = new StaticContentBean();
	try {
	    staticContent = staticContentMapper.selectSingleRecord(criteria);
	} catch (Exception e) {
	    String errorMsg = "xxx Error occured while fetching single 'StaticContent' informations with criteria ==> " + criteria + " xxx";
	    throw new DAOException(errorMsg, e);
	}
	daoLogger.debug("[FINISH] : <<< --- Fetching single 'StaticContent' informations with criteria ---");
	return staticContent;
    }

    @Override
    @Cacheable(value = "MasterCache.StaticContent")
    public List<StaticContentBean> selectList(Map<String, Object> criteria) throws DAOException {
	daoLogger.debug("[START] : >>> --- Fetching multi 'StaticContent' informations with criteria ---");
	List<StaticContentBean> staticContents = null;
	try {
	    staticContents = staticContentMapper.selectMultiRecords(criteria);
	} catch (Exception e) {
	    String errorMsg = "xxx Error occured while fetching multiple 'StaticContent' informations with criteria ==> " + criteria
		    + " xxx";
	    throw new DAOException(errorMsg, e);
	}
	daoLogger.debug("[FINISH] : <<< --- Fetching multi 'StaticContent' informations with criteria ---");
	return staticContents;
    }

    @Override
    @Cacheable(value = "MasterCache.StaticContent")
    public long selectCounts(Map<String, Object> criteria) throws DAOException {
	daoLogger.debug("[START] : >>> --- Fetching 'StaticContent' counts with criteria ---");
	long count = 0;
	try {
	    count = staticContentMapper.selectCounts(criteria);
	} catch (Exception e) {
	    String errorMsg = "xxx Error occured while counting 'StaticContent' records with criteria ==> " + criteria + " xxx";
	    throw new DAOException(errorMsg, e);
	}
	daoLogger.debug("[FINISH] : <<< --- Fetching 'StaticContent' counts with criteria ---");
	return count;
    }
}
