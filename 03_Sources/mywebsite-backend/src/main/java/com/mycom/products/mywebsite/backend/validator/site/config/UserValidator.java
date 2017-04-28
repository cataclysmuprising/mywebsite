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
 *	mywebsite-backend - UserValidator.java
 *	Using JRE 1.8.0_121
 *	@author Than Htike Aung {@literal <rage.cataclysm@gmail.com>}
 *	@Since 2017
 * 
 */
package com.mycom.products.mywebsite.backend.validator.site.config;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import com.mycom.products.mywebsite.backend.controller.BaseController.PageMode;
import com.mycom.products.mywebsite.backend.util.LocalizedMessageResolver;
import com.mycom.products.mywebsite.backend.validator.BaseValidator;
import com.mycom.products.mywebsite.backend.validator.FieldValidator;
import com.mycom.products.mywebsite.core.bean.config.UserBean;
import com.mycom.products.mywebsite.core.exception.BusinessException;
import com.mycom.products.mywebsite.core.service.config.api.UserService;
import com.mycom.products.mywebsite.core.util.FetchMode;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class UserValidator extends BaseValidator {
    @Autowired
    private LocalizedMessageResolver messageSource;

    @Autowired
    private UserService userService;

    @Override
    public boolean supports(@SuppressWarnings("rawtypes") Class clazz) {
	return UserBean.class.equals(clazz);
    }

    @Override
    public void validate(Object obj, Errors errors) {
	UserBean user = (UserBean) obj;
	validateIsValidRangeValue(new FieldValidator("name", "User Name", user.getName(), errors), 5, 50);

	validateIsValidRangeValue(new FieldValidator("nrc", "NRC", user.getNrc(), errors), 5, 50);

	if (pageMode == PageMode.CREATE) {
	    validateIsValidAlphaNumerics(new FieldValidator("loginId", "Login Id", user.getLoginId(), errors));
	    validateIsValidRangeValue(new FieldValidator("loginId", "Login Id", user.getLoginId(), errors), 5, 50);

	    validateIsValidEmail(new FieldValidator("email", "Email", user.getEmail(), errors));
	    validateIsUniqueEmail(new FieldValidator("email", "Email", user.getEmail(), errors));

	    validateIsValidPasswordPattern(new FieldValidator("password", "Password", user.getPassword(), errors));
	    validateIsValidPasswordPattern(new FieldValidator("confirmPassword", "Confirm Password", user.getConfirmPassword(), errors));
	    validateIsEqual("password", new FieldValidator("password", "Password", user.getPassword(), errors),
		    new FieldValidator("confirmPassword", "Confirm Password", user.getConfirmPassword(), errors), errors);

	    validateIsEmpty(new FieldValidator("roles", "Role", user.getRoleIds(), errors));
	}

	validateIsValidRangeValue(new FieldValidator("phone", "Phone Number", user.getPhone(), errors), 5, 50);

	validateIsValidMinValue(new FieldValidator("age", "Age", user.getAge(), errors), 18);
	validateIsValidMaxValue(new FieldValidator("age", "Age", user.getAge(), errors), 100);
    }

    private void validateIsUniqueEmail(FieldValidator fieldValidator) {
	if (!validateIsEmpty(fieldValidator)) {
	    return;
	}
	String targetId = fieldValidator.getTargetId();
	if (fieldValidator.getTarget() instanceof String) {
	    String target = (String) fieldValidator.getTarget();
	    Errors errors = fieldValidator.getErrors();
	    HashMap<String, Object> criteria = new HashMap<>();
	    criteria.put("email", target);
	    long count = 0;
	    try {
		count = userService.selectCounts(criteria, FetchMode.LAZY);
	    } catch (BusinessException e) {
		throw new RuntimeException("Checking unique email process has failed", e);
	    }
	    if (count > 0) {
		errors.rejectValue(targetId, "",
			messageSource.getMessage("Validation.common.Field.Unique", new Object[] { "Email Address", target }));
	    }
	} else {
	    throw new UnSupportedValidationCheckException();
	}
    }
}