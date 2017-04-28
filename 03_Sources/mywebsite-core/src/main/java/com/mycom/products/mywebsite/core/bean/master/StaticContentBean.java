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
 *	mywebsite-core - StaticContentBean.java
 *	Using JRE 1.8.0_121
 *	@author Than Htike Aung {@literal <rage.cataclysm@gmail.com>}
 *	@Since 2017
 * 
 */
package com.mycom.products.mywebsite.core.bean.master;

import com.mycom.products.mywebsite.core.bean.BaseBean;

public class StaticContentBean extends BaseBean {
    private static final long serialVersionUID = -5890442627766482224L;
    private String fileName;
    private String filePath;
    private String fileSize;
    private FileType fileType;

    public enum FileType {
	IMAGE, TEXT, PDF, EXCEL, ZIP, UNKNOWN;
    }

    public String getFileName() {
	return fileName;
    }

    public void setFileName(String fileName) {
	this.fileName = fileName;
    }

    public String getFilePath() {
	return filePath;
    }

    public void setFilePath(String filePath) {
	this.filePath = filePath;
    }

    public String getFileSize() {
	return fileSize;
    }

    public void setFileSize(String fileSize) {
	this.fileSize = fileSize;
    }

    public FileType getFileType() {
	return fileType;
    }

    public void setFileType(FileType fileType) {
	this.fileType = fileType;
    }

    @Override
    public String toString() {
	return String.format("StaticContentBean {fileName=%s, filePath=%s, fileSize=%s, fileType=%s, ID=%s}", fileName, filePath, fileSize,
		fileType, getId());
    }

}
