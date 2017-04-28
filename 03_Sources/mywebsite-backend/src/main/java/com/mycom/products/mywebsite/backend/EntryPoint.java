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
 *	mywebsite-backend - EntryPoint.java
 *	Using JRE 1.8.0_121
 *	@author Than Htike Aung {@literal <rage.cataclysm@gmail.com>}
 *	@Since 2017
 * 
 */
package com.mycom.products.mywebsite.backend;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.mycom.products.mywebsite.core.bean.BaseBean;
import com.mycom.products.mywebsite.core.bean.master.SettingBean;
import com.mycom.products.mywebsite.core.exception.BusinessException;
import com.mycom.products.mywebsite.core.service.master.api.SettingService;

public class EntryPoint implements InitializingBean {
    @Autowired
    private SettingService settingService;

    public static final String MODULE_NAME = "back-end";
    private static String projectVersion;
    private static boolean isProduction;
    private static String uploadPath;
    private static final Logger applicationLogger = Logger.getLogger("applicationLogs." + EntryPoint.class.getName());
    private static final Logger errorLogger = Logger.getLogger("errorLogs." + EntryPoint.class.getName());

    @Override
    public void afterPropertiesSet() throws Exception {
	initUploadPath();
	loadProjectSettings();
	if (uploadPath != null) {
	    File file = new File(uploadPath);
	    if (!file.exists()) {
		file.mkdirs();
	    }
	}
    }

    public static String getUploadPath() {
	return uploadPath;
    }

    public static String getProjectVersion() {
	return projectVersion;
    }

    public static boolean isProductionMode() {
	return isProduction;
    }

    private void loadProjectSettings() {
	String path = "/project.properties";
	InputStream stream = getClass().getResourceAsStream(path);
	if (stream == null) {
	    projectVersion = "UNKNOWN";
	}
	Properties props = new Properties();
	try {
	    props.load(stream);
	    stream.close();
	    projectVersion = (String) props.get("version");
	    isProduction = Boolean.valueOf(props.get("isProduction").toString());
	} catch (IOException e) {
	    throw new RuntimeException("Error loading project settings.", e);
	}
    }

    private void initUploadPath() {
	applicationLogger.info(BaseBean.LOG_PREFIX + "Transaction start for fetching 'File Upload Directory' information.");
	HashMap<String, Object> criteria = new HashMap<>();
	criteria.put("name", "UploadPath");
	try {
	    SettingBean setting = settingService.select(criteria);
	    if (setting != null) {
		uploadPath = setting.getValue();
	    }
	} catch (BusinessException e) {
	    errorLogger.error(BaseBean.LOG_BREAKER_OPEN);
	    errorLogger.error(BaseBean.LOG_PREFIX + "Retrieving 'File Upload Directory' process has failed." + BaseBean.LOG_SUFFIX);
	    errorLogger.error(e.getMessage(), e);
	    errorLogger.error(BaseBean.LOG_BREAKER_CLOSE);
	}
    }
}
