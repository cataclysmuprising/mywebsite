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
 *	mywebsite-core - XGenericMapper.java
 *	Using JRE 1.8.0_121
 *	@author Than Htike Aung {@literal <rage.cataclysm@gmail.com>}
 *	@Since 2017
 * 
 */
package com.mycom.products.mywebsite.core.mapper.base;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.mycom.products.mywebsite.core.bean.BaseBean;
import com.mycom.products.mywebsite.core.util.FetchMode;

public interface XGenericMapper<T extends BaseBean> extends InsertableMapper<T> {
    public void insertWithRelatedKeys(@Param("key1") long key1, @Param("key2") long key2, @Param("recordRegId") long recordRegId);

    public long deleteByKeys(@Param("key1") long key1, @Param("key2") long key2);

    public long deleteByCriteria(@Param("criteria") Map<String, Object> criteria);

    public List<Long> selectRelatedKeys(@Param("criteria") Map<String, Object> criteria);

    public T selectByKeys(@Param("key1") long key1, @Param("key2") long key2, @Param("fetchMode") FetchMode fetchMode);

    public List<T> selectMultiRecords(@Param("criteria") Map<String, Object> criteria, @Param("fetchMode") FetchMode fetchMode);

    public long selectCounts(@Param("criteria") Map<String, Object> criteria, @Param("fetchMode") FetchMode fetchMode);

}
