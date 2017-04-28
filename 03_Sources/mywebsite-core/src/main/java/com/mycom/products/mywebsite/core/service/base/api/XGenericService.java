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
 *	mywebsite-core - XGenericService.java
 *	Using JRE 1.8.0_121
 *	@author Than Htike Aung {@literal <rage.cataclysm@gmail.com>}
 *	@Since 2017
 * 
 */
package com.mycom.products.mywebsite.core.service.base.api;

import java.util.List;
import java.util.Map;

import com.mycom.products.mywebsite.core.bean.BaseBean;
import com.mycom.products.mywebsite.core.exception.BusinessException;
import com.mycom.products.mywebsite.core.exception.ConsistencyViolationException;
import com.mycom.products.mywebsite.core.exception.DuplicatedEntryException;
import com.mycom.products.mywebsite.core.service.base.api.root.BaseService;
import com.mycom.products.mywebsite.core.util.FetchMode;

public interface XGenericService<T extends BaseBean> extends BaseService<T> {

    public void insert(T record, long recordRegId) throws DuplicatedEntryException, BusinessException;

    public void insert(List<T> records, long recordRegId) throws DuplicatedEntryException, BusinessException;

    public void insert(long key1, long key2, long recordRegId) throws DuplicatedEntryException, BusinessException;

    public long delete(long key1, long key2, long recordUpdId) throws ConsistencyViolationException, BusinessException;

    public long delete(Map<String, Object> criteria, long recordUpdId) throws ConsistencyViolationException, BusinessException;

    public void merge(long mainKey, List<Long> relatedKeys, long recordUpdId)
	    throws DuplicatedEntryException, ConsistencyViolationException, BusinessException;

    public List<Long> selectByKey1(long key1) throws BusinessException;

    public List<Long> selectByKey2(long key2) throws BusinessException;

    public T select(long key1, long key2, FetchMode fetchMode) throws BusinessException;

    public List<T> selectList(Map<String, Object> criteria, FetchMode fetchMode) throws BusinessException;

    public long selectCounts(Map<String, Object> criteria) throws BusinessException;

}