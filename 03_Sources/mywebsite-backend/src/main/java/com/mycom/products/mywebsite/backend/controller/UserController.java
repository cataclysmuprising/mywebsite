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
 *	mywebsite-backend - UserController.java
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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.ServletRequestBindingException;
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
import com.mycom.products.mywebsite.backend.dto.PasswordDto;
import com.mycom.products.mywebsite.backend.util.LocalizedMessageResolver;
import com.mycom.products.mywebsite.backend.validator.BaseValidator;
import com.mycom.products.mywebsite.backend.validator.FieldValidator;
import com.mycom.products.mywebsite.backend.validator.site.config.UserValidator;
import com.mycom.products.mywebsite.core.bean.config.UserBean;
import com.mycom.products.mywebsite.core.exception.BusinessException;
import com.mycom.products.mywebsite.core.exception.ConsistencyViolationException;
import com.mycom.products.mywebsite.core.exception.DuplicatedEntryException;
import com.mycom.products.mywebsite.core.service.config.api.LoginHistoryService;
import com.mycom.products.mywebsite.core.service.config.api.UserService;
import com.mycom.products.mywebsite.core.util.Cryptographic;
import com.mycom.products.mywebsite.core.util.FetchMode;

@Controller
@Loggable
@RequestMapping("/users")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private LoginHistoryService loginHistoryService;

    @Autowired
    private BaseValidator baseValidator;

    @Autowired
    private LocalizedMessageResolver messageSource;

    @InitBinder("user")
    public void initBinder(WebDataBinder binder) {
	binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @Override
    public void subInit(Model model) throws BusinessException {
	setAuthorities(model, "user");
    }

    @RequestMapping(method = RequestMethod.GET)
    public String home(Model model) {
	return "user_home";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add(Model model) {
	model.addAttribute("pageMode", PageMode.CREATE);
	model.addAttribute("user", new UserBean());
	return "user_dataForm";
    }

    @HandleServletException(errorView = "user_dataForm", pageMode = PageMode.CREATE)
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ValidateEntity(validator = UserValidator.class)
    public String add(Model model, RedirectAttributes redirectAttributes, @ModelAttribute("user") UserBean user, BindingResult bindResult)
	    throws BusinessException, DuplicatedEntryException {
	// hashing password before insert
	user.setPassword(Cryptographic.getSha256CheckSum(user.getPassword()));
	userService.insertUserWithRoles(user, loginUser.getId());
	setPageMessage(redirectAttributes, "Success", "Serverity.common.Page.SuccessfullyRegistered", PageMessageStyle.SUCCESS, "User");
	return "redirect:/users";
    }

    @RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
    @HandleServletException
    public String edit(@PathVariable int id, Model model) throws BusinessException {
	model.addAttribute("pageMode", PageMode.EDIT);
	UserBean user = userService.select(id, FetchMode.EAGER);
	model.addAttribute("user", user);
	return "user_dataForm";
    }

    @RequestMapping(value = "/{id}/edit", method = RequestMethod.POST)
    @HandleServletException(errorView = "user_dataForm", pageMode = PageMode.EDIT)
    @ValidateEntity(validator = UserValidator.class)
    public String edit(Model model, RedirectAttributes redirectAttributes, @PathVariable int id,
	    @ModelAttribute("user") @Validated UserBean user, BindingResult bindResult) throws BusinessException, DuplicatedEntryException {
	userService.update(user, loginUser.getId());
	setPageMessage(redirectAttributes, "Success", "Serverity.common.Page.SuccessfullyUpdated", PageMessageStyle.SUCCESS, "User");
	return "redirect:/users";
    }

    @RequestMapping(value = "/{id}/delete", method = RequestMethod.GET)
    @HandleServletException(errorView = "user_home", pageMode = PageMode.DELETE)
    public String delete(@PathVariable int id, RedirectAttributes redirectAttributes, Model model)
	    throws BusinessException, ConsistencyViolationException {
	if (id == loginUser.getId()) {
	    setPageMessage(redirectAttributes, "Error", "Error.user.Remove.Self", PageMessageStyle.ERROR);
	    return "redirect:/users";
	}
	userService.delete(id, loginUser.getId());
	setPageMessage(redirectAttributes, "Success", "Serverity.common.Page.SuccessfullyRemoved", PageMessageStyle.SUCCESS, "User");
	return "redirect:/users";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @HandleServletException
    public String detail(@PathVariable int id, Model model) throws BusinessException {
	model.addAttribute("pageMode", PageMode.VIEW);
	UserBean user = userService.select(id, FetchMode.EAGER);
	model.addAttribute("user", user);
	return "user_detail";
    }

    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    @HandleServletException
    public String profile(Model model) throws BusinessException {
	model.addAttribute("pageMode", PageMode.PROFILE);
	UserBean user = userService.select(loginUser.getId(), FetchMode.EAGER);
	model.addAttribute("user", user);
	return "user_dataForm";
    }

    @RequestMapping(value = "/profile", method = RequestMethod.POST)
    @HandleServletException(errorView = "user_dataForm", pageMode = PageMode.PROFILE)
    @ValidateEntity(validator = UserValidator.class)
    public String profile(Model model, RedirectAttributes redirectAttributes, @ModelAttribute("user") @Validated UserBean user,
	    BindingResult bindResult) throws BusinessException, DuplicatedEntryException {
	user.setId(loginUser.getId());
	user.setPassword(null);
	user.setConfirmPassword(null);
	user.setRoleIds(null);
	userService.update(user, loginUser.getId());
	setPageMessage(redirectAttributes, "Success", "Success.userProfile.Edit", PageMessageStyle.SUCCESS);
	return "redirect:/users";
    }

    @RequestMapping(value = "/{loginId}/changedPassword", method = RequestMethod.POST)
    @HandleAjaxException
    public @ResponseBody Map<String, Object> changedPassword(@PathVariable String loginId, @RequestParam String oldPassword,
	    @RequestParam String newPassword, @RequestParam String confirmPassword)
	    throws BusinessException, DuplicatedEntryException, ConsistencyViolationException, ServletRequestBindingException {
	PasswordDto passwordDto = new PasswordDto();
	passwordDto.setOldPassword(oldPassword);
	passwordDto.setNewPassword(newPassword);
	passwordDto.setConfirmPassword(confirmPassword);
	Errors errors = new BeanPropertyBindingResult(passwordDto, "passwordDto");
	// validate passwords
	baseValidator.validateIsEqual("newPassword",
		new FieldValidator("newPassword", "New Password", passwordDto.getNewPassword(), errors),
		new FieldValidator("confirmPassword", "Confirm Password", passwordDto.getConfirmPassword(), errors), errors);
	baseValidator
		.validateIsValidPasswordPattern(new FieldValidator("oldPassword", "Old Password", passwordDto.getOldPassword(), errors));
	baseValidator
		.validateIsValidPasswordPattern(new FieldValidator("newPassword", "New Password", passwordDto.getNewPassword(), errors));
	baseValidator.validateIsValidPasswordPattern(
		new FieldValidator("confirmPassword", "Confirm Password", passwordDto.getConfirmPassword(), errors));

	// fetch user information with given loginId
	HashMap<String, Object> criteria = new HashMap<>();
	criteria.put("loginId", loginId);
	UserBean user = userService.select(criteria, FetchMode.LAZY);
	if (user == null) {
	    throw new ServletRequestBindingException("Trying to make illegal access for changing password of unknown loginId=" + loginId);
	}
	// check is oldpassword correct ?
	if (!user.getPassword().equals(Cryptographic.getSha256CheckSum(oldPassword))) {
	    errors.rejectValue("oldPassword", "", "Incorrect old password.Try again !");
	}
	if (errors.hasErrors()) {
	    return setAjaxFormFieldErrors(errors, "change_");
	}
	// update user's password
	user.setPassword(Cryptographic.getSha256CheckSum(newPassword));
	userService.update(user, loginUser.getId());

	// response success
	Map<String, Object> response = new HashMap<>();
	response.put("status", HttpStatus.OK);
	return setAjaxPageMessage(response, "Success",
		messageSource.getMessage("Validation.UserBean.Page.PasswordUpdateSuccessful", new Object[] { user.getName() }),
		PageMessageStyle.SUCCESS);
    }

    @RequestMapping(value = "/{loginId}/resetPassword", method = RequestMethod.POST)
    @HandleAjaxException
    public @ResponseBody Map<String, Object> resetPassword(@PathVariable String loginId, @RequestParam String newPassword,
	    @RequestParam String confirmPassword)
	    throws BusinessException, DuplicatedEntryException, ConsistencyViolationException, ServletRequestBindingException {
	PasswordDto passwordDto = new PasswordDto();
	passwordDto.setNewPassword(newPassword);
	passwordDto.setConfirmPassword(confirmPassword);
	Errors errors = new BeanPropertyBindingResult(passwordDto, "passwordDto");
	// validate passwords
	baseValidator.validateIsEqual("newPassword",
		new FieldValidator("newPassword", "New Password", passwordDto.getNewPassword(), errors),
		new FieldValidator("confirmPassword", "Confirm Password", passwordDto.getConfirmPassword(), errors), errors);
	baseValidator
		.validateIsValidPasswordPattern(new FieldValidator("newPassword", "New Password", passwordDto.getNewPassword(), errors));
	baseValidator.validateIsValidPasswordPattern(
		new FieldValidator("confirmPassword", "Confirm Password", passwordDto.getConfirmPassword(), errors));

	// fetch user information with given loginId
	HashMap<String, Object> criteria = new HashMap<>();
	criteria.put("loginId", loginId);
	UserBean user = userService.select(criteria, FetchMode.LAZY);
	if (user == null) {
	    throw new ServletRequestBindingException("Trying to make illegal access for changing password of unknown loginId=" + loginId);
	}
	if (errors.hasErrors()) {
	    return setAjaxFormFieldErrors(errors, "reset_");
	}
	// update user's password
	user.setPassword(Cryptographic.getSha256CheckSum(newPassword));
	userService.update(user, loginUser.getId());

	// response success
	Map<String, Object> response = new HashMap<>();
	response.put("status", HttpStatus.OK);
	return setAjaxPageMessage(response, "Success",
		messageSource.getMessage("Validation.UserBean.Page.PasswordUpdateSuccessful", new Object[] { user.getName() }),
		PageMessageStyle.SUCCESS);
    }

    @RequestMapping(value = "/api/{id}/loginHistories")
    @HandleAjaxException
    public @ResponseBody Map<String, Object> searchLoginHistoryByUserId(@RequestParam int start, @RequestParam int length,
	    @RequestParam String id) throws BusinessException, ServletRequestBindingException {
	Map<String, Object> results = new HashMap<>();
	Map<String, Object> criteria = new HashMap<>();
	criteria.put("offset", start);
	criteria.put("limit", length);
	try {
	    criteria.put("userId", Integer.parseInt(id));
	} catch (NumberFormatException e) {
	    throw new ServletRequestBindingException(
		    "Trying to make illegal access for fetching LoginHistory informations with unknown userId # " + id);
	}

	results.put("aaData", loginHistoryService.selectList(criteria, FetchMode.LAZY));
	results.put("iTotalDisplayRecords", loginHistoryService.selectCounts(criteria, FetchMode.LAZY));

	return results;
    }

    @RequestMapping(value = "/api/search", method = RequestMethod.POST)
    @HandleAjaxException
    public @ResponseBody Map<String, Object> search(@RequestParam(required = false) Integer start, @RequestParam int length,
	    @RequestParam String orderBy, @RequestParam String orderAs, @RequestParam String word,
	    @RequestParam(required = false) String loginId, @RequestParam(required = false) String roleId,
	    @RequestParam(required = false) String includeIds, @RequestParam(required = false) String excludeIds,
	    @RequestParam(required = false) String fetchMode) throws BusinessException {
	FetchMode mode = FetchMode.LAZY;
	if (fetchMode != null && fetchMode.equals("EAGER")) {
	    mode = FetchMode.valueOf("EAGER");
	}
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
	if (loginId != null && loginId.length() > 0) {
	    criteria.put("loginId", getPureString(loginId));
	}
	if (roleId != null && roleId.length() > 0) {
	    try {
		criteria.put("roleId", Integer.parseInt(roleId));
	    } catch (NumberFormatException e) {
		// ignore this error , just parse exception
	    }
	}

	List<UserBean> users = userService.selectList(criteria, mode);
	results.put("iTotalDisplayRecords", userService.selectCounts(criteria, mode));
	results.put("aaData", users);

	return results;
    }
}
