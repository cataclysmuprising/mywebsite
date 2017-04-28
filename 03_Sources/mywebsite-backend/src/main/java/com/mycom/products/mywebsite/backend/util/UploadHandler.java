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
 *	mywebsite-backend - UploadHandler.java
 *	Using JRE 1.8.0_121
 *	@author Than Htike Aung {@literal <rage.cataclysm@gmail.com>}
 *	@Since 2017
 * 
 */
package com.mycom.products.mywebsite.backend.util;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mycom.products.mywebsite.backend.EntryPoint;
import com.mycom.products.mywebsite.core.bean.config.LoggedUserBean;
import com.mycom.products.mywebsite.core.bean.master.StaticContentBean;
import com.mycom.products.mywebsite.core.bean.master.StaticContentBean.FileType;
import com.mycom.products.mywebsite.core.service.master.api.StaticContentService;

@Controller
@RequestMapping(value = "/ajax/upload")
public class UploadHandler extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Autowired
    private StaticContentService contentService;

    @Override
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST)
    protected final void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
	ServletFileUpload uploadHandler = new ServletFileUpload(new DiskFileItemFactory());
	PrintWriter writer = response.getWriter();
	// checks if the request actually contains upload file
	if (!ServletFileUpload.isMultipartContent(request)) {
	    // if not, we stop here
	    writer.println("Error: Form must has enctype=multipart/form-data.");
	    writer.flush();
	    return;
	}
	JSONObject json = new JSONObject();
	SimpleDateFormat fmtYMD = new SimpleDateFormat("/" + "yyyyMMdd");
	Date today = new Date();
	String uploadPath = EntryPoint.getUploadPath() + "/";

	try {
	    List<FileItem> items = uploadHandler.parseRequest(request);
	    if (items != null && items.size() > 0) {
		String saveDir = "", fileCategory = "";
		for (FileItem item : items) {
		    if (item.isFormField()) {
			fileCategory = item.getString();
		    }
		}
		saveDir = fileCategory + fmtYMD.format(today);
		// creates the directory if it does not exist
		File uploadDir = new File(uploadPath + saveDir);
		if (!uploadDir.exists()) {
		    uploadDir.mkdirs();
		}
		List<HashMap<String, String>> uploadFiles = new ArrayList<>();
		for (FileItem item : items) {
		    // processes only fields that are not form fields
		    if (!item.isFormField()) {
			if (saveDir.length() == 0) {
			    json.put("messageCode", "V1001");
			    json.put("messageParams", "File upload type");
			    json.put("status", HttpStatus.BAD_REQUEST);
			    response.setContentType("application/json");
			    writer.write(json.toString());
			    writer.flush();
			}
			String originalFileName = "", saveFileName = "", format = "", fileSize = "";
			// set the default format to png when it is profileImage
			if (fileCategory.equals("profilePicture")) {
			    format = ".png";
			}
			// can't predict fileName and format would be included.
			// For instance, blob won't be.
			try {
			    originalFileName = item.getName().substring(0, item.getName().lastIndexOf("."));
			} catch (Exception e) {
			    // Nothing to do. Skip
			}
			try {
			    format = item.getName().substring(item.getName().lastIndexOf("."), item.getName().length());
			} catch (Exception e) {
			    // Nothing to do. Skip
			}

			fileSize = getReadableFileSize(item.getSize());
			UUID uuid = UUID.randomUUID();
			saveFileName = new File(uuid.toString() + format).getName();
			String filePath = uploadPath + saveDir + "/" + saveFileName;
			if (fileCategory.equals("profilePicture")) {
			    saveProfileImage(item, filePath);
			}
			// Time to save in DB
			LoggedUserBean loginUser;
			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (principal instanceof LoggedUserBean) {
			    loginUser = (LoggedUserBean) principal;
			} else {
			    throw new SecurityException("Unauthorize File Upload process was attempted.");
			}
			StaticContentBean content = new StaticContentBean();
			content.setFileName(originalFileName + format);
			content.setFilePath(filePath);
			content.setFileSize(fileSize);
			content.setFileType(FileType.valueOf(getFileType(format)));
			long lastInsertedId = contentService.insert(content, loginUser.getId());
			// else .... other file types go here

			HashMap<String, String> fileItem = new HashMap<>();
			fileItem.put("contentId", "" + lastInsertedId);
			uploadFiles.add(fileItem);

		    }
		}
		json.put("uploadFiles", uploadFiles);
		json.put("status", HttpStatus.OK);
		response.setContentType("application/json");
		writer.write(json.toString());
		writer.flush();
	    }
	} catch (FileUploadException e) {
	    throw new RuntimeException("File upload Error !", e);
	} catch (Exception e) {
	    throw new RuntimeException("File upload Error !", e);
	} finally {
	    writer.close();
	}
    }

    private void saveProfileImage(FileItem item, String filePath) throws Exception {
	File storeFile = new File(filePath);
	// save original image from client side on disk
	item.write(storeFile);
	// serverside image resize( OK Let's do it from batch)
	// BufferedImage originalImage;
	// InputStream in = new FileInputStream(storeFile);
	// originalImage = ImageIO.read(in);
	// int imgType = BufferedImage.SCALE_SMOOTH;
	// BufferedImage resizedImage = new BufferedImage(150, 150, imgType);
	// Graphics2D g = resizedImage.createGraphics();
	// g.drawImage(originalImage, 0, 0, 150, 150, null);
	// g.dispose();
	// g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
	// RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	// g.setRenderingHint(RenderingHints.KEY_RENDERING,
	// RenderingHints.VALUE_RENDER_QUALITY);
	// g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
	// RenderingHints.VALUE_ANTIALIAS_ON);
	// // convert BufferedImage to byte array
	// ByteArrayOutputStream baos = new ByteArrayOutputStream();
	// ImageIO.write(resizedImage, "JPEG", baos);
	// FileUtils.writeByteArrayToFile(storeFile, baos.toByteArray());
	// baos.flush();
	// baos.close();

    }

    private String getReadableFileSize(long size) {
	if (size <= 0)
	    return "0";
	final String[] units = new String[] { "B", "KB", "MB", "GB", "TB" };
	int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
	return new DecimalFormat("#,##0.##").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    private String getFileType(String fileExtension) {
	String format = fileExtension.toLowerCase().substring(1, fileExtension.length());
	if ("gif,jpg,jpeg,png".contains(format)) {
	    return FileType.IMAGE.name();
	} else if ("xls,xlsx".contains(format)) {
	    return FileType.EXCEL.name();
	} else if ("pdf".contains(format)) {
	    return FileType.PDF.name();
	} else if ("txt".contains(format)) {
	    return FileType.TEXT.name();
	} else if ("zip,tar,tar.gz".contains(format)) {
	    return FileType.ZIP.name();
	} else {
	    return FileType.UNKNOWN.name();
	}
    }
}
