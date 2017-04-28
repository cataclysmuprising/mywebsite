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
 *	mywebsite-backend - SettingValidator.java
 *	Using JRE 1.8.0_121
 *	@author Than Htike Aung {@literal <rage.cataclysm@gmail.com>}
 *	@Since 2017
 * 
 */
package com.mycom.products.mywebsite.backend.validator.site.master;

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
import com.mycom.products.mywebsite.core.bean.master.SettingBean;
import com.mycom.products.mywebsite.core.exception.BusinessException;
import com.mycom.products.mywebsite.core.service.master.api.SettingService;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SettingValidator extends BaseValidator {
    @Autowired
    private SettingService settingService;

    @Autowired
    private LocalizedMessageResolver messageSource;

    @Override
    public boolean supports(@SuppressWarnings("rawtypes") Class clazz) {
	return SettingBean.class.equals(clazz);
    }

    @Override
    public void validate(Object obj, Errors errors) {
	SettingBean setting = (SettingBean) obj;
	validateIsValidMaxValue(new FieldValidator("group", "Group", setting.getGroup(), errors), 50);
	validateIsValidAlphaNumerics(new FieldValidator("group", "Group", setting.getGroup(), errors));

	validateIsValidMaxValue(new FieldValidator("subGroup", "Sub Group", setting.getSubGroup(), errors), 50);
	validateIsValidAlphaNumerics(new FieldValidator("subGroup", "Sub Group", setting.getSubGroup(), errors));

	validateIsValidMaxValue(new FieldValidator("type", "Type", setting.getType(), errors), 50);
	validateIsValidAlphaNumerics(new FieldValidator("type", "Type", setting.getType(), errors));

	if (pageMode == PageMode.CREATE) {
	    validateIsValidMaxValue(new FieldValidator("name", "Name", setting.getName(), errors), 50);
	    validateIsValidAlphaNumerics(new FieldValidator("name", "Name", setting.getName(), errors));
	    validateIsUniqueSettingName(new FieldValidator("name", "Name", setting.getName(), errors));
	}
	validateIsValidMaxValue(new FieldValidator("value", "Value", setting.getValue(), errors), 200);
    }

    private void validateIsUniqueSettingName(FieldValidator fieldValidator) {
	if (!validateIsEmpty(fieldValidator)) {
	    return;
	}
	String targetId = fieldValidator.getTargetId();
	if (fieldValidator.getTarget() instanceof String) {
	    String target = (String) fieldValidator.getTarget();
	    Errors errors = fieldValidator.getErrors();
	    HashMap<String, Object> criteria = new HashMap<>();
	    criteria.put("name", target);
	    long count = 0;
	    try {
		count = settingService.selectCounts(criteria);
	    } catch (BusinessException e) {
		throw new RuntimeException("Checking unique setting name process has failed", e);
	    }
	    if (count > 0) {
		errors.rejectValue(targetId, "",
			messageSource.getMessage("Validation.common.Field.Unique", new Object[] { "Setting Name", target }));
	    }
	} else {
	    throw new UnSupportedValidationCheckException();
	}
    }

}