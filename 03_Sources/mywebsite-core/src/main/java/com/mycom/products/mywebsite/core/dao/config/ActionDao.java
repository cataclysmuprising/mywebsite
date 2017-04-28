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
 *	mywebsite-core - ActionDao.java
 *	Using JRE 1.8.0_121
 *	@author Than Htike Aung {@literal <rage.cataclysm@gmail.com>}
 *	@Since 2017
 * 
 */
package com.mycom.products.mywebsite.core.dao.config;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import com.mycom.products.mywebsite.core.bean.config.ActionBean;
import com.mycom.products.mywebsite.core.dao.api.JoinedSelectableDao;
import com.mycom.products.mywebsite.core.exception.DAOException;
import com.mycom.products.mywebsite.core.mapper.config.ActionMapper;
import com.mycom.products.mywebsite.core.util.FetchMode;

@Repository
public class ActionDao implements JoinedSelectableDao<ActionBean>, Serializable {
    private static final long serialVersionUID = 1L;
    @Autowired
    private ActionMapper actionMapper;
    private static final Logger daoLogger = Logger.getLogger("daoLogs." + ActionDao.class.getName());

    @Override
    @Cacheable(value = "ConfigurationCache.Action")
    public ActionBean select(long primaryKey, FetchMode fetchMode) throws DAOException {
	daoLogger.debug("[START] : >>> --- Fetching 'Action' informations with primaryKey # " + primaryKey + " ---");
	ActionBean action = new ActionBean();
	try {
	    action = actionMapper.selectByPrimaryKey(primaryKey, fetchMode);
	} catch (Exception e) {
	    String errorMsg = "xxx Error occured while fetching single 'Action' informations with primaryKey ==> " + primaryKey + " xxx";
	    throw new DAOException(errorMsg, e);
	}
	daoLogger.debug("[FINISH] : <<< --- Fetching 'Action' informations with primaryKey # " + primaryKey + " ---");
	return action;
    }

    @Override
    @Cacheable(value = "ConfigurationCache.Action")
    public ActionBean select(Map<String, Object> criteria, FetchMode fetchMode) throws DAOException {
	daoLogger.debug("[START] : >>> --- Fetching single 'Action' informations with criteria ---");
	ActionBean action = new ActionBean();
	try {
	    action = actionMapper.selectSingleRecord(criteria, fetchMode);
	} catch (Exception e) {
	    String errorMsg = "xxx Error occured while fetching single 'Action' informations with criteria ==> " + criteria + " xxx";
	    throw new DAOException(errorMsg, e);
	}
	daoLogger.debug("[FINISH] : <<< --- Fetching single 'Action' informations with criteria ---");
	return action;
    }

    @Override
    @Cacheable(value = "ConfigurationCache.Action")
    public List<ActionBean> selectList(Map<String, Object> criteria, FetchMode fetchMode) throws DAOException {
	daoLogger.debug("[START] : >>> --- Fetching multi 'Action' informations with criteria ---");
	List<ActionBean> actions = null;
	try {
	    actions = actionMapper.selectMultiRecords(criteria, fetchMode);
	} catch (Exception e) {
	    String errorMsg = "xxx Error occured while fetching multiple 'Action' informations with criteria ==> " + criteria + " xxx";
	    throw new DAOException(errorMsg, e);
	}
	daoLogger.debug("[FINISH] : <<< --- Fetching multi 'Action' informations with criteria ---");
	return actions;
    }

    @Override
    @Cacheable(value = "ConfigurationCache.Action")
    public long selectCounts(Map<String, Object> criteria, FetchMode fetchMode) throws DAOException {
	daoLogger.debug("[START] : >>> --- Fetching 'Action' counts with criteria ---");
	long count = 0;
	try {
	    count = actionMapper.selectCounts(criteria, fetchMode);
	} catch (Exception e) {
	    String errorMsg = "xxx Error occured while counting 'Action' records with criteria ==> " + criteria + " xxx";
	    throw new DAOException(errorMsg, e);
	}
	daoLogger.debug("[FINISH] : <<< --- Fetching 'Action' counts with criteria ---");
	return count;
    }

    @Cacheable(value = "ConfigurationCache.Action")
    public List<String> selectPageNamesByModule(String module) throws DAOException {
	daoLogger.debug("[START] : >>> --- Fetching all 'PageNames' by module = '" + module + "' ---");
	List<String> pageNames = null;
	try {
	    pageNames = actionMapper.selectPageNamesByModule(module);
	} catch (Exception e) {
	    String errorMsg = "xxx Error occured while fetching all 'PageNames' by module = '" + module + "' xxx";
	    throw new DAOException(errorMsg, e);
	}
	daoLogger.debug("[FINISH] : <<< --- Fetching all 'PageNames' by module = '" + module + "' ---");
	return pageNames;
    }
}
