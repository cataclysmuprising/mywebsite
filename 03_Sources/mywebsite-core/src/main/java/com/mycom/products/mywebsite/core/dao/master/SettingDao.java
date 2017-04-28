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
 *	mywebsite-core - SettingDao.java
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
import com.mycom.products.mywebsite.core.bean.master.SettingBean;
import com.mycom.products.mywebsite.core.dao.api.CommonGenericDao;
import com.mycom.products.mywebsite.core.dao.api.StandAloneSelectableDao;
import com.mycom.products.mywebsite.core.exception.ConsistencyViolationException;
import com.mycom.products.mywebsite.core.exception.DAOException;
import com.mycom.products.mywebsite.core.exception.DuplicatedEntryException;
import com.mycom.products.mywebsite.core.exception.SaveHistoryFailedException;
import com.mycom.products.mywebsite.core.mapper.master.SettingMapper;

@Repository
public class SettingDao implements CommonGenericDao<SettingBean>, StandAloneSelectableDao<SettingBean> {

    @Autowired
    private SettingMapper settingMapper;
    private static final Logger daoLogger = Logger.getLogger("daoLogs." + SettingDao.class.getName());

    @Override
    @CacheEvict(value = "MasterCache.Setting", allEntries = true)
    public long insert(SettingBean setting, long recordRegId) throws DAOException, DuplicatedEntryException {
	try {
	    daoLogger.debug("[START] : >>> --- Inserting single 'Setting' informations ---");
	    LocalDateTime now = LocalDateTime.now();
	    setting.setRecordRegDate(now);
	    setting.setRecordUpdDate(now);
	    setting.setRecordRegId(recordRegId);
	    setting.setRecordUpdId(recordRegId);
	    setting.setTransactionType(TransactionType.INSERT);
	    settingMapper.insert(setting);
	    daoLogger.debug(
		    "[HISTORY][START] : $1 --- Save 'Setting' informations in history after successfully inserted in major table ---");
	    settingMapper.insertSingleHistoryRecord(setting);
	    daoLogger.debug("[HISTORY][FINISH] : $1 --- Save 'Setting' informations in history ---");
	} catch (DuplicateKeyException e) {
	    String errorMsg = "xxx Insertion process was failed due to Unique Key constraint xxx";
	    throw new DuplicatedEntryException(errorMsg, e);
	} catch (SaveHistoryFailedException e) {
	    String errorMsg = "xxx Error occured while saving 'Setting' informations in history for later tracking xxx";
	    throw new SaveHistoryFailedException(errorMsg, e.getCause());
	} catch (Exception e) {
	    String errorMsg = "xxx Error occured while inserting 'Setting' data ==> " + setting + " xxx";
	    throw new DAOException(errorMsg, e);
	}
	daoLogger.debug("[FINISH] : <<< --- Inserting single 'Setting' informations with new Id # " + setting.getId() + " ---");
	return setting.getId();
    }

    @Override
    @CacheEvict(value = "MasterCache.Setting", allEntries = true)
    public void insert(List<SettingBean> settings, long recordRegId) throws DAOException, DuplicatedEntryException {
	daoLogger.debug("[START] : >>> --- Inserting multi 'Setting' informations ---");
	LocalDateTime now = LocalDateTime.now();
	for (SettingBean setting : settings) {
	    setting.setRecordRegDate(now);
	    setting.setRecordUpdDate(now);
	    setting.setRecordRegId(recordRegId);
	    setting.setRecordUpdId(recordRegId);
	    setting.setTransactionType(TransactionType.INSERT);
	}
	try {
	    settingMapper.insertList(settings);
	    daoLogger.debug(
		    "[HISTORY][START] : $1 --- Save 'Setting' informations in history after successfully inserted in major table ---");
	    settingMapper.insertMultiHistoryRecords(settings);
	    daoLogger.debug("[HISTORY][FINISH] : $1 --- Save 'Setting' informations in history ---");
	} catch (DuplicateKeyException e) {
	    String errorMsg = "xxx Insertion process was failed due to Unique Key constraint. xxx";
	    throw new DuplicatedEntryException(errorMsg, e);
	} catch (SaveHistoryFailedException e) {
	    String errorMsg = "xxx Error occured while saving 'Setting' informations in history for later tracking xxx";
	    throw new SaveHistoryFailedException(errorMsg, e.getCause());
	} catch (Exception e) {
	    String errorMsg = "xxx Error occured while inserting multi 'Setting' datas ==> " + settings + " xxx";
	    throw new DAOException(errorMsg, e);
	}
	daoLogger.debug("[FINISH] : <<< --- Inserting multi 'Setting' informations ---");
    }

    @Override
    @CacheEvict(value = "MasterCache.Setting", allEntries = true)
    public long update(SettingBean setting, long recordUpdId) throws DAOException, DuplicatedEntryException {
	long totalEffectedRows = 0;
	daoLogger.debug("[START] : >>> --- Updating single 'Setting' informations ---");
	try {
	    setting.setRecordUpdId(recordUpdId);
	    daoLogger.debug("[HISTORY][START] : $1 --- Save 'Setting' informations in history before update on major table ---");
	    SettingBean oldData = settingMapper.selectByPrimaryKey(setting.getId());
	    if (oldData == null) {
		throw new SaveHistoryFailedException();
	    }
	    oldData.setTransactionType(TransactionType.UPDATE);
	    oldData.setRecordUpdId(recordUpdId);
	    settingMapper.insertSingleHistoryRecord(oldData);
	    daoLogger.debug("[HISTORY][FINISH] : $1 --- Save 'Setting' informations in history ---");
	    totalEffectedRows = settingMapper.update(setting);
	} catch (DuplicateKeyException e) {
	    String errorMsg = "xxx Updating process was failed due to Unique Key constraint xxx";
	    throw new DuplicatedEntryException(errorMsg, e);
	} catch (SaveHistoryFailedException e) {
	    String errorMsg = "xxx Error occured while saving 'Setting' informations in history for later tracking xxx";
	    throw new SaveHistoryFailedException(errorMsg, e.getCause());
	} catch (Exception e) {
	    String errorMsg = "xxx Error occured while updating 'Setting' data ==> " + setting + " xxx";
	    throw new DAOException(errorMsg, e);
	}
	daoLogger.debug("[FINISH] : <<< --- Updating single 'Setting' informations with Id # " + setting.getId() + " ---");
	return totalEffectedRows;
    }

    @Override
    @CacheEvict(value = "MasterCache.Setting", allEntries = true)
    public void update(List<SettingBean> settings, long recordUpdId) throws DAOException, DuplicatedEntryException {
	daoLogger.debug("[START] : >>> --- Updating multi 'Setting' informations ---");
	for (SettingBean setting : settings) {
	    try {
		setting.setRecordUpdId(recordUpdId);
		daoLogger.debug("[HISTORY][START] : $1 --- Save 'Setting' informations in history before update on major table ---");
		SettingBean oldData = settingMapper.selectByPrimaryKey(setting.getId());
		if (oldData == null) {
		    throw new SaveHistoryFailedException();
		}
		oldData.setTransactionType(TransactionType.UPDATE);
		oldData.setRecordUpdId(recordUpdId);
		settingMapper.insertSingleHistoryRecord(oldData);
		daoLogger.debug("[HISTORY][FINISH] : $1 --- Save 'Setting' informations in history ---");
		settingMapper.update(setting);
	    } catch (DuplicateKeyException e) {
		String errorMsg = "xxx Updating process was failed due to Unique Key constraint xxx";
		throw new DuplicatedEntryException(errorMsg, e);
	    } catch (SaveHistoryFailedException e) {
		String errorMsg = "xxx Error occured while saving 'Setting' informations in history for later tracking xxx";
		throw new SaveHistoryFailedException(errorMsg, e.getCause());
	    } catch (Exception e) {
		String errorMsg = "xxx Error occured while updating 'Setting' data ==> " + setting + " xxx";
		throw new DAOException(errorMsg, e);
	    }
	}
	daoLogger.debug("[FINISH] : <<< --- Updating multi 'Setting' informations ---");
    }

    @Override
    @CacheEvict(value = "MasterCache.Setting", allEntries = true)
    public long update(HashMap<String, Object> criteria, HashMap<String, Object> updateItems, long recordUpdId)
	    throws DAOException, DuplicatedEntryException {
	long totalEffectedRows = 0;
	daoLogger.debug("[START] : >>> --- Updating multi 'Setting' informations with criteria ---");
	try {
	    criteria.put("recordUpdId", recordUpdId);
	    daoLogger.debug("[HISTORY][START] : $1 --- Save 'Setting' informations in history before update on major table ---");
	    List<SettingBean> settings = settingMapper.selectMultiRecords(criteria);
	    if (settings == null) {
		throw new SaveHistoryFailedException();
	    } else if (settings != null && settings.size() > 0) {
		for (SettingBean setting : settings) {
		    setting.setTransactionType(TransactionType.UPDATE);
		    setting.setRecordUpdId(recordUpdId);
		    settingMapper.insertSingleHistoryRecord(setting);
		}
	    }
	    daoLogger.debug("[HISTORY][FINISH] : $1 --- Save 'Setting' informations in history ---");
	    totalEffectedRows = settingMapper.updateWithCriteria(criteria, updateItems);
	} catch (DuplicateKeyException e) {
	    String errorMsg = "xxx Updating process was failed due to Unique Key constraint xxx";
	    throw new DuplicatedEntryException(errorMsg, e);
	} catch (SaveHistoryFailedException e) {
	    String errorMsg = "xxx Error occured while saving 'Setting' informations in history for later tracking xxx";
	    throw new SaveHistoryFailedException(errorMsg, e.getCause());
	} catch (Exception e) {
	    String errorMsg = "xxx Error occured while updating multiple 'Setting' informations [Values] ==> " + updateItems
		    + " with [Criteria] ==> " + criteria + " xxx";
	    throw new DAOException(errorMsg, e);
	}
	daoLogger.debug("[FINISH] : <<< --- Updating multi 'Setting' informations with criteria ---");
	return totalEffectedRows;
    }

    @Override
    @CacheEvict(value = "MasterCache.Setting", allEntries = true)
    public long delete(long primaryKey, long recordUpdId) throws DAOException, ConsistencyViolationException {
	daoLogger.debug("[START] : >>> --- Deleting single 'Setting' informations with primaryKey # " + primaryKey + " ---");
	long totalEffectedRows = 0;
	try {
	    daoLogger.debug("[HISTORY][START] : $1 --- Save 'Setting' informations in history before update on major table ---");
	    SettingBean oldData = settingMapper.selectByPrimaryKey(primaryKey);
	    if (oldData == null) {
		throw new SaveHistoryFailedException();
	    }
	    oldData.setTransactionType(TransactionType.UPDATE);
	    oldData.setRecordUpdId(recordUpdId);
	    settingMapper.insertSingleHistoryRecord(oldData);
	    daoLogger.debug("[HISTORY][FINISH] : $1 --- Save 'Setting' informations in history ---");
	    totalEffectedRows = settingMapper.deleteByPrimaryKey(primaryKey);
	} catch (DataIntegrityViolationException e) {
	    String errorMsg = "xxx Rejected : Deleting process was failed because this entity was connected with other resources.If you try to forcely remove it, entire database will loose consistency xxx";
	    throw new ConsistencyViolationException(errorMsg, e);
	} catch (SaveHistoryFailedException e) {
	    String errorMsg = "xxx Error occured while saving 'Setting' informations in history for later tracking xxx";
	    throw new SaveHistoryFailedException(errorMsg, e.getCause());
	} catch (Exception e) {
	    String errorMsg = "xxx Error occured while deleting 'Setting' data with primaryKey ==> " + primaryKey + " xxx";
	    throw new DAOException(errorMsg, e);
	}
	daoLogger.debug("[FINISH] : <<< --- Deleting single 'Setting' informations with primaryKey # " + primaryKey + " ---");
	return totalEffectedRows;
    }

    @Override
    @CacheEvict(value = "MasterCache.Setting", allEntries = true)
    public long delete(Map<String, Object> criteria, long recordUpdId) throws DAOException, ConsistencyViolationException {
	long totalEffectedRows = 0;
	daoLogger.debug("[START] : >>> --- Deleting 'Setting' informations with criteria  ---");
	try {
	    daoLogger.debug("[HISTORY][START] : $1 --- Save 'Setting' informations in history before delete on major table ---");
	    List<SettingBean> settings = settingMapper.selectMultiRecords(criteria);
	    if (settings == null) {
		throw new SaveHistoryFailedException();
	    } else if (settings != null && settings.size() > 0) {
		for (SettingBean setting : settings) {
		    setting.setTransactionType(TransactionType.DELETE);
		    setting.setRecordUpdId(recordUpdId);
		}
		settingMapper.insertMultiHistoryRecords(settings);
	    }
	    daoLogger.debug("[HISTORY][FINISH] : $1 --- Save 'Setting' informations in history ---");
	    totalEffectedRows = settingMapper.deleteByCriteria(criteria);
	} catch (DataIntegrityViolationException e) {
	    String errorMsg = "xxx Rejected : Deleting process was failed because this entity was connected with other resources.If you try to forcely remove it, entire database will loose consistency xxx";
	    throw new ConsistencyViolationException(errorMsg, e);
	} catch (SaveHistoryFailedException e) {
	    String errorMsg = "xxx Error occured while saving 'Setting' informations in history for later tracking xxx";
	    throw new SaveHistoryFailedException(errorMsg, e.getCause());
	} catch (Exception e) {
	    String errorMsg = "xxx Error occured while deleting 'Setting' data with criteria ==> " + criteria + " xxx";
	    throw new DAOException(errorMsg, e);
	}
	daoLogger.debug("[FINISH] : <<< --- Deleting 'Setting' informations with criteria  ---");
	return totalEffectedRows;
    }

    @Override
    @Cacheable(value = "MasterCache.Setting")
    public SettingBean select(long primaryKey) throws DAOException {
	daoLogger.debug("[START] : >>> --- Fetching 'Setting' informations with primaryKey # " + primaryKey + " ---");
	SettingBean setting = new SettingBean();
	try {
	    setting = settingMapper.selectByPrimaryKey(primaryKey);
	} catch (Exception e) {
	    String errorMsg = "xxx Error occured while fetching single 'Setting' informations with primaryKey ==> " + primaryKey + " xxx";
	    throw new DAOException(errorMsg, e);
	}
	daoLogger.debug("[FINISH] : <<< --- Fetching 'Setting' informations with primaryKey # " + primaryKey + " ---");
	return setting;
    }

    @Override
    @Cacheable(value = "MasterCache.Setting")
    public SettingBean select(Map<String, Object> criteria) throws DAOException {
	daoLogger.debug("[START] : >>> --- Fetching single 'Setting' informations with criteria ---");
	SettingBean setting = new SettingBean();
	try {
	    setting = settingMapper.selectSingleRecord(criteria);
	} catch (Exception e) {
	    String errorMsg = "xxx Error occured while fetching single 'Setting' informations with criteria ==> " + criteria + " xxx";
	    throw new DAOException(errorMsg, e);
	}
	daoLogger.debug("[FINISH] : <<< --- Fetching single 'Setting' informations with criteria ---");
	return setting;
    }

    @Override
    @Cacheable(value = "MasterCache.Setting")
    public List<SettingBean> selectList(Map<String, Object> criteria) throws DAOException {
	daoLogger.debug("[START] : >>> --- Fetching multi 'Setting' informations with criteria ---");
	List<SettingBean> settings = null;
	try {
	    settings = settingMapper.selectMultiRecords(criteria);
	} catch (Exception e) {
	    String errorMsg = "xxx Error occured while fetching multiple 'Setting' informations with criteria ==> " + criteria + " xxx";
	    throw new DAOException(errorMsg, e);
	}
	daoLogger.debug("[FINISH] : <<< --- Fetching multi 'Setting' informations with criteria ---");
	return settings;
    }

    @Override
    @Cacheable(value = "MasterCache.Setting")
    public long selectCounts(Map<String, Object> criteria) throws DAOException {
	daoLogger.debug("[START] : >>> --- Fetching 'Setting' counts with criteria ---");
	long count = 0;
	try {
	    count = settingMapper.selectCounts(criteria);
	} catch (Exception e) {
	    String errorMsg = "xxx Error occured while counting 'Setting' records with criteria ==> " + criteria + " xxx";
	    throw new DAOException(errorMsg, e);
	}
	daoLogger.debug("[FINISH] : <<< --- Fetching 'Setting' counts with criteria ---");
	return count;
    }
}
