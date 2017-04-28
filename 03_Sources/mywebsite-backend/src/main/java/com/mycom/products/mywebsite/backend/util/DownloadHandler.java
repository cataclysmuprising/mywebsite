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
 *	mywebsite-backend - DownloadHandler.java
 *	Using JRE 1.8.0_121
 *	@author Than Htike Aung {@literal <rage.cataclysm@gmail.com>}
 *	@Since 2017
 * 
 */
package com.mycom.products.mywebsite.backend.util;

import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.html.WebColors;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.mycom.products.mywebsite.core.bean.config.RoleBean;
import com.mycom.products.mywebsite.core.bean.config.UserBean;
import com.mycom.products.mywebsite.core.bean.config.UserBean.Gender;
import com.mycom.products.mywebsite.core.exception.BusinessException;
import com.mycom.products.mywebsite.core.service.config.api.UserService;
import com.mycom.products.mywebsite.core.util.FetchMode;

@Controller
@RequestMapping(value = "/download")
public class DownloadHandler {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
    protected final void downloadUserInformation(@PathVariable int id, HttpServletRequest request, final HttpServletResponse response)
	    throws ServletException, BusinessException, DocumentException {
	UserBean user = userService.select(id, FetchMode.EAGER);
	if (user == null) {
	    return;
	}
	Document document = new Document();
	document.setMargins(70, 70, 20, 20);

	try {
	    response.setContentType("application/pdf");
	    response.setHeader("Content-Disposition", "attachment; filename=\"" + user.getName() + "_profile.pdf\"");
	    PdfWriter.getInstance(document, response.getOutputStream());
	    document.open();
	    Image profileImage = null;
	    try {
		profileImage = Image.getInstance(user.getContent().getFilePath());
	    } catch (Exception e) {
		// e.printStackTrace();
	    }
	    if (profileImage != null) {
		profileImage.setAlignment(Image.MIDDLE | Image.TEXTWRAP);
		profileImage.setBorder(Image.BOX);
		profileImage.setBorderWidth(5);
		BaseColor bgcolor = WebColors.getRGBColor("#E5E3E3");
		profileImage.setBorderColor(bgcolor);
		profileImage.scaleToFit(100, 100);
		document.add(profileImage);
	    }
	    document.add(Chunk.NEWLINE);
	    document.add(Chunk.NEWLINE);

	    // Adding Table Data
	    PdfPTable table = new PdfPTable(2); // 2 columns.
	    table.setWidthPercentage(100); // Width 100%
	    table.setSpacingBefore(15f); // Space before table
	    table.setSpacingAfter(15f); // Space after table

	    // Set Column widths
	    float[] columnWidths = { 1f, 2f, };
	    table.setWidths(columnWidths);

	    // Name
	    setTableHeader("Name", table);
	    setTableContent(user.getName(), table);

	    // Gender
	    setTableHeader("Gender", table);
	    String gender = "Male";
	    if (user.getGender() == Gender.FEMALE) {
		gender = "Female";
	    }
	    setTableContent(gender, table);

	    // Age
	    setTableHeader("Age", table);
	    setTableContent("" + user.getAge(), table);

	    // Date of Birth
	    setTableHeader("DOB", table);
	    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	    setTableContent(dateFormatter.format(user.getDob()), table);

	    // Email
	    setTableHeader("Email", table);
	    setTableContent(user.getEmail(), table);

	    // NRC
	    setTableHeader("NRC", table);
	    setTableContent(user.getNrc(), table);

	    // Phone
	    setTableHeader("Phone", table);
	    setTableContent(user.getPhone(), table);

	    // Roles
	    String roleStr = "";
	    List<RoleBean> roles = user.getRoles();
	    if (roles != null && roles.size() > 0) {
		Iterator<RoleBean> itr = roles.iterator();
		while (itr.hasNext()) {
		    RoleBean role = itr.next();
		    roleStr += role.getName();
		    if (itr.hasNext()) {
			roleStr += ",";
		    }
		}
	    }
	    setTableHeader("Role(s)", table);
	    setTableContent(roleStr, table);

	    // Address
	    setTableHeader("Address", table);
	    setTableContent(user.getAddress(), table);

	    document.add(table);
	    document.add(new Paragraph(new Date().toString()));

	} catch (Exception e) {
	    e.printStackTrace();

	}
	document.close();
    }

    private void setTableHeader(String value, PdfPTable table) {
	PdfPCell cell = new PdfPCell(new Paragraph(value));
	cell.setBorderColor(BaseColor.GRAY);
	cell.setFixedHeight(25);
	BaseColor myColor = WebColors.getRGBColor("#F7F7F7");
	cell.setBackgroundColor(myColor);
	cell.setPaddingLeft(10);
	cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	table.addCell(cell);

    }

    private void setTableContent(String value, PdfPTable table) {
	PdfPCell cell = new PdfPCell(new Paragraph(value));
	cell.setBorderColor(BaseColor.GRAY);
	cell.setPaddingLeft(10);
	cell.setHorizontalAlignment(Element.ALIGN_LEFT);
	cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	table.addCell(cell);
    }
}
