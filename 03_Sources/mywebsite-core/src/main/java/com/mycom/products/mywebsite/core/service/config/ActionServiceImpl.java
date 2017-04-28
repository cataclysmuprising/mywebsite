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
 *	mywebsite-core - ActionServiceImpl.java
 *	Using JRE 1.8.0_121
 *	@author Than Htike Aung {@literal <rage.cataclysm@gmail.com>}
 *	@Since 2017
 * 
 */
package com.mycom.products.mywebsite.core.service.config;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mycom.products.mywebsite.core.bean.BaseBean;
import com.mycom.products.mywebsite.core.bean.config.ActionBean;
import com.mycom.products.mywebsite.core.dao.config.ActionDao;
import com.mycom.products.mywebsite.core.exception.BusinessException;
import com.mycom.products.mywebsite.core.exception.DAOException;
import com.mycom.products.mywebsite.core.service.base.JoinedSelectableServiceImpl;
import com.mycom.products.mywebsite.core.service.base.XGenericServiceImpl;
import com.mycom.products.mywebsite.core.service.config.api.ActionService;

@Service
public class ActionServiceImpl extends JoinedSelectableServiceImpl<ActionBean> implements ActionService {
    private static final Logger serviceLogger = Logger.getLogger("serviceLogs." + XGenericServiceImpl.class.getName());

    private ActionDao actionDao;

    @Autowired
    public ActionServiceImpl(ActionDao actionDao) {
	super(actionDao);
	this.actionDao = actionDao;
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> selectPageNamesByModule(String module) throws BusinessException {
	List<String> results = null;
	serviceLogger.info(BaseBean.LOG_PREFIX + "Transaction start for fetching all 'pageNames' process." + BaseBean.LOG_SUFFIX);
	try {
	    results = actionDao.selectPageNamesByModule(module);
	} catch (DAOException e) {
	    throw new BusinessException(e.getMessage(), e);
	}
	serviceLogger
		.info(BaseBean.LOG_PREFIX + "Transaction finished successfully for fetching 'pageNames' process." + BaseBean.LOG_SUFFIX);
	return results;
    }

}
