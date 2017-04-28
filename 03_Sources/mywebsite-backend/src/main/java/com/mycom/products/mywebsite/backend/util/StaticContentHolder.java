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
 *	mywebsite-backend - StaticContentHolder.java
 *	Using JRE 1.8.0_121
 *	@author Than Htike Aung {@literal <rage.cataclysm@gmail.com>}
 *	@Since 2017
 * 
 */
package com.mycom.products.mywebsite.backend.util;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Files;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.mycom.products.mywebsite.core.bean.master.StaticContentBean;
import com.mycom.products.mywebsite.core.bean.master.StaticContentBean.FileType;
import com.mycom.products.mywebsite.core.exception.BusinessException;
import com.mycom.products.mywebsite.core.service.master.api.StaticContentService;

@Controller
@RequestMapping(value = "/files/**")
public class StaticContentHolder extends HttpServlet {
    private static final long serialVersionUID = -4968188621073367744L;
    @Autowired
    private StaticContentService staticContentService;

    @Override
    @RequestMapping(method = RequestMethod.GET)
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	String contentString = request.getServletPath().substring("/files/".length());
	long contentId = -1l;
	try {
	    contentId = Integer.parseInt(contentString);
	} catch (NumberFormatException | ArithmeticException e) {
	    throw new ServletException("Unknown file content.", e.getCause());
	}
	StaticContentBean content;
	try {
	    content = staticContentService.select(contentId);
	} catch (BusinessException e) {
	    throw new ServletException("Error loading content information.");
	}
	if (content == null) {
	    throw new ServletException("Content not found.");
	}
	File file = new File(content.getFilePath());
	if (content.getFileType() == FileType.PDF) {
	    response.setContentType("application/pdf; charset=UTF-8");
	} else if (content.getFileType() == FileType.ZIP) {
	    response.setContentType("application/zip; charset=UTF-8");
	} else if (content.getFileType() == FileType.IMAGE) {
	    response.setContentType("image/jpeg; charset=UTF-8");
	} else {
	    response.setHeader("Content-Type", request.getContentType());
	}
	String fileName = URLEncoder.encode(content.getFileName(), "UTF-8");
	response.setCharacterEncoding("UTF-8");
	response.setHeader("Cache-Control", "no-cache,no-store,max-age=0");
	response.setHeader("Pragma", "No-cache");
	response.setHeader("Content-Length", String.valueOf(file.length()));
	response.setHeader("Content-Disposition", "inline; filename=" + fileName);
	Files.copy(file.toPath(), response.getOutputStream());
    }

}