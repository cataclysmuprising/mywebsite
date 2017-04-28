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
 *	mywebsite-core - BaseBean.java
 *	Using JRE 1.8.0_121
 *	@author Than Htike Aung {@literal <rage.cataclysm@gmail.com>}
 *	@Since 2017
 * 
 */
package com.mycom.products.mywebsite.core.bean;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class BaseBean implements Serializable {
    private static final long serialVersionUID = 5987804145999725843L;
    public static final String LOG_BREAKER_OPEN = "**********************************************************************";
    public static final String LOG_BREAKER_CLOSE = "############################## xxxxxxxx ##############################";
    public static final String LOG_PREFIX = "----------  ";
    public static final String LOG_SUFFIX = "  ----------";

    public enum TransactionType {
	INSERT, UPDATE, DELETE;
    }

    private long id;

    @JsonIgnore
    private long recordRegId;
    @JsonIgnore
    private long recordUpdId;
    @JsonIgnore
    private LocalDateTime recordRegDate;
    @JsonIgnore
    private LocalDateTime recordUpdDate;
    @JsonIgnore
    private TransactionType transactionType;

    public long getId() {
	return id;
    }

    public void setId(long id) {
	this.id = id;
    }

    public long getRecordRegId() {
	return recordRegId;
    }

    public void setRecordRegId(long recordRegId) {
	this.recordRegId = recordRegId;
    }

    public LocalDateTime getRecordRegDate() {
	return recordRegDate;
    }

    public void setRecordRegDate(LocalDateTime recordRegDate) {
	this.recordRegDate = recordRegDate;
    }

    public TransactionType getTransactionType() {
	return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
	this.transactionType = transactionType;
    }

    public long getRecordUpdId() {
	return recordUpdId;
    }

    public void setRecordUpdId(long recordUpdId) {
	this.recordUpdId = recordUpdId;
    }

    public LocalDateTime getRecordUpdDate() {
	return recordUpdDate;
    }

    public void setRecordUpdDate(LocalDateTime recordUpdDate) {
	this.recordUpdDate = recordUpdDate;
    }

    protected String convertDateAsString(LocalDate date) {
	DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	String dateString = "";
	if (date != null) {
	    dateString = dateFormatter.format(date);
	}
	return dateString;
    }

    protected LocalDate convertStringAsDate(String dateString) {
	DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	LocalDate date = null;
	if (dateString != null && dateString.length() > 0) {
	    date = LocalDate.parse(dateString, dateFormatter);
	}
	return date;
    }

    @Override
    public String toString() {
	return String.format("*** id=%s, recordRegId=%s, recordUpdId=%s, recordRegDate=%s, recordUpdDate=%s ***", id, recordRegId,
		recordUpdId, recordRegDate, recordUpdDate);
    }

}
