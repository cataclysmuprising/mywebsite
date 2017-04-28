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
 *	mywebsite-backend - SettingController.java
 *	Using JRE 1.8.0_121
 *	@author Than Htike Aung {@literal <rage.cataclysm@gmail.com>}
 *	@Since 2017
 * 
 */

package com.mycom.products.mywebsite.backend.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mycom.products.mywebsite.backend.annotation.HandleAjaxException;
import com.mycom.products.mywebsite.backend.annotation.HandleServletException;
import com.mycom.products.mywebsite.backend.annotation.Loggable;
import com.mycom.products.mywebsite.backend.annotation.ValidateEntity;
import com.mycom.products.mywebsite.backend.validator.site.master.SettingValidator;
import com.mycom.products.mywebsite.core.bean.master.SettingBean;
import com.mycom.products.mywebsite.core.exception.BusinessException;
import com.mycom.products.mywebsite.core.exception.ConsistencyViolationException;
import com.mycom.products.mywebsite.core.exception.DuplicatedEntryException;
import com.mycom.products.mywebsite.core.service.master.api.SettingService;

@Controller
@Loggable
@RequestMapping("/settings")
public class SettingController extends BaseController {
    @Autowired
    private SettingService settingService;

    @InitBinder("setting")
    public void initBinder(WebDataBinder binder) {
	binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @Override
    public void subInit(Model model) throws BusinessException {
	setAuthorities(model, "setting");
    }

    @RequestMapping(method = RequestMethod.GET)
    public String home(Model model) {
	return "setting_home";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add(Model model) {
	model.addAttribute("pageMode", PageMode.CREATE);
	model.addAttribute("setting", new SettingBean());
	return "setting_dataForm";
    }

    @HandleServletException(errorView = "setting_dataForm", pageMode = PageMode.CREATE)
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ValidateEntity(validator = SettingValidator.class)
    public String add(Model model, RedirectAttributes redirectAttributes, @ModelAttribute("setting") SettingBean setting,
	    BindingResult bindResult) throws BusinessException, DuplicatedEntryException {
	settingService.insert(setting, loginUser.getId());
	setPageMessage(redirectAttributes, "Success", "Serverity.common.Page.SuccessfullyRegistered", PageMessageStyle.SUCCESS, "Setting");
	return "redirect:/settings";
    }

    @RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
    @HandleServletException
    public String edit(@PathVariable int id, Model model) throws BusinessException {
	model.addAttribute("pageMode", PageMode.EDIT);
	SettingBean setting = settingService.select(id);
	model.addAttribute("setting", setting);
	return "setting_dataForm";
    }

    @RequestMapping(value = "/{id}/edit", method = RequestMethod.POST)
    @HandleServletException(errorView = "setting_dataForm", pageMode = PageMode.EDIT)
    @ValidateEntity(validator = SettingValidator.class)
    public String edit(Model model, RedirectAttributes redirectAttributes, @PathVariable int id,
	    @ModelAttribute("setting") @Validated SettingBean setting, BindingResult bindResult)
	    throws BusinessException, DuplicatedEntryException {
	settingService.update(setting, loginUser.getId());
	setPageMessage(redirectAttributes, "Success", "Serverity.common.Page.SuccessfullyUpdated", PageMessageStyle.SUCCESS, "Setting");
	return "redirect:/settings";
    }

    @RequestMapping(value = "/{id}/delete", method = RequestMethod.GET)
    @HandleServletException(errorView = "setting_home", pageMode = PageMode.DELETE)
    public String delete(@PathVariable int id, RedirectAttributes redirectAttributes, Model model)
	    throws BusinessException, ConsistencyViolationException {
	settingService.delete(id, loginUser.getId());
	setPageMessage(redirectAttributes, "Success", "Serverity.common.Page.SuccessfullyRemoved", PageMessageStyle.SUCCESS, "Setting");
	return "redirect:/settings";
    }

    @RequestMapping(value = "/api/search", method = RequestMethod.POST)
    @HandleAjaxException
    public @ResponseBody Map<String, Object> search(@RequestParam(required = false) Integer start, @RequestParam int length,
	    @RequestParam String orderBy, @RequestParam String orderAs, @RequestParam String word,
	    @RequestParam(required = false) String includeIds, @RequestParam(required = false) String excludeIds) throws BusinessException {
	Map<String, Object> results = new HashMap<>();
	Map<String, Object> criteria = new HashMap<>();

	criteria.put("offset", start);
	criteria.put("limit", length);
	criteria.put("orderBy", orderBy);
	criteria.put("orderAs", orderAs);
	if (includeIds != null && includeIds.length() > 0) {
	    criteria.put("includeIds", includeIds.split(","));
	}
	if (excludeIds != null && excludeIds.length() > 0) {
	    criteria.put("excludeIds", excludeIds.split(","));
	}
	if (word != null && word.length() > 0) {
	    criteria.put("word", getPureString(word));
	}
	List<SettingBean> settings = settingService.selectList(criteria);
	results.put("iTotalDisplayRecords", settingService.selectCounts(criteria));
	results.put("aaData", settings);

	return results;
    }
}
