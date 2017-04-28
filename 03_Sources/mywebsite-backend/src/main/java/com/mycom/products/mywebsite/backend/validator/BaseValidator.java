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
 *	mywebsite-backend - BaseValidator.java
 *	Using JRE 1.8.0_121
 *	@author Than Htike Aung {@literal <rage.cataclysm@gmail.com>}
 *	@Since 2017
 * 
 */
package com.mycom.products.mywebsite.backend.validator;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.mycom.products.mywebsite.backend.controller.BaseController.PageMode;
import com.mycom.products.mywebsite.backend.util.LocalizedMessageResolver;
import com.mycom.products.mywebsite.core.bean.BaseBean;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class BaseValidator implements Validator {

    @Autowired
    private LocalizedMessageResolver messageSource;
    protected PageMode pageMode;

    public static class UnSupportedValidationCheckException extends RuntimeException {
	private static final long serialVersionUID = 8506363193459502634L;

	public UnSupportedValidationCheckException() {
	    super();
	}

	public UnSupportedValidationCheckException(final String message) {
	    super(message);
	}

	public UnSupportedValidationCheckException(final String message, final Throwable cause) {
	    super(message, cause);
	}

	public UnSupportedValidationCheckException(final Throwable cause) {
	    super(cause);
	}

    }

    @SuppressWarnings("rawtypes")
    public boolean validateIsEmpty(FieldValidator fieldValidator) {
	String targetId = fieldValidator.getTargetId();
	String displayName = fieldValidator.getDisplayName();
	Object target = fieldValidator.getTarget();
	Errors errors = fieldValidator.getErrors();
	if (target == null) {
	    errors.rejectValue(targetId, "", messageSource.getMessage("Validation.common.Field.Required", new Object[] { displayName }));
	    return false;
	} else if (target instanceof String) {
	    if (((String) target).isEmpty()) {
		errors.rejectValue(targetId, "",
			messageSource.getMessage("Validation.common.Field.Required", new Object[] { displayName }));
		return false;
	    }

	} else if (target instanceof Collection) {
	    if (((Collection) target).isEmpty()) {
		errors.rejectValue(targetId, "",
			messageSource.getMessage("Validation.common.Field.ChooseOne", new Object[] { displayName }));
		return false;
	    }
	} else if (target instanceof Map) {
	    if (((Map) target).isEmpty()) {
		errors.rejectValue(targetId, "",
			messageSource.getMessage("Validation.common.Field.ChooseOne", new Object[] { displayName }));
		return false;
	    }
	} else if (target.getClass().isArray()) {
	    if (((Object[]) target).length == 0) {
		errors.rejectValue(targetId, "",
			messageSource.getMessage("Validation.common.Field.ChooseOne", new Object[] { displayName }));
		return false;
	    }
	}
	return true;
    }

    public void validateIsEqual(String targetId, FieldValidator fieldValidator1, FieldValidator fieldValidator2, Errors errors) {
	if (!validateIsEmpty(fieldValidator1)) {
	    return;
	}
	if (!validateIsEmpty(fieldValidator2)) {
	    return;
	}

	if (!fieldValidator1.getTarget().equals(fieldValidator2.getTarget())) {
	    errors.rejectValue(targetId, "", messageSource.getMessage("Validation.common.Field.DoNotMatch",
		    new Object[] { fieldValidator1.getDisplayName(), fieldValidator2.getDisplayName() }));
	}
    }

    public void validateIsValidMinValue(FieldValidator fieldValidator, Number number) {
	if (!validateIsEmpty(fieldValidator)) {
	    return;
	}

	String targetId = fieldValidator.getTargetId();
	String displayName = fieldValidator.getDisplayName();
	Object target = fieldValidator.getTarget();
	Errors errors = fieldValidator.getErrors();

	if (target instanceof String) {
	    if (target.toString().length() < number.intValue()) {
		errors.rejectValue(targetId, "",
			messageSource.getMessage("Validation.common.Field.Min.String", new Object[] { displayName, number }));
	    }
	} else if (target instanceof Number) {
	    Number inputNumber = (Number) target;
	    if (inputNumber.doubleValue() < number.doubleValue()) {
		errors.rejectValue(targetId, "",
			messageSource.getMessage("Validation.common.Field.Min.Number", new Object[] { displayName, number }));
	    }
	} else {
	    throw new UnSupportedValidationCheckException();
	}
    }

    public void validateIsValidMaxValue(FieldValidator fieldValidator, Number number) {
	if (!validateIsEmpty(fieldValidator)) {
	    return;
	}

	String targetId = fieldValidator.getTargetId();
	String displayName = fieldValidator.getDisplayName();
	Object target = fieldValidator.getTarget();
	Errors errors = fieldValidator.getErrors();

	if (target instanceof String) {
	    if (target.toString().length() > number.intValue()) {
		errors.rejectValue(targetId, "",
			messageSource.getMessage("Validation.common.Field.Max.String", new Object[] { displayName, number }));
	    }
	} else if (target instanceof Number) {
	    Number inputNumber = (Number) target;
	    if (inputNumber.doubleValue() > number.doubleValue()) {
		errors.rejectValue(targetId, "",
			messageSource.getMessage("Validation.common.Field.Max.Number", new Object[] { displayName, number }));
	    }
	} else {
	    throw new UnSupportedValidationCheckException();
	}
    }

    public void validateIsValidRangeValue(FieldValidator fieldValidator, Number min, Number max) {
	if (!validateIsEmpty(fieldValidator)) {
	    return;
	}

	String targetId = fieldValidator.getTargetId();
	String displayName = fieldValidator.getDisplayName();
	Object target = fieldValidator.getTarget();
	Errors errors = fieldValidator.getErrors();
	if (target instanceof String) {
	    if (target.toString().length() < min.intValue() || target.toString().length() > max.intValue()) {
		errors.rejectValue(targetId, "",
			messageSource.getMessage("Validation.common.Field.Range.String", new Object[] { displayName, min, max }));
	    }
	} else if (target instanceof Number) {
	    Number number = (Number) target;
	    if (number.doubleValue() < min.doubleValue() || number.doubleValue() > max.doubleValue()) {
		errors.rejectValue(targetId, "",
			messageSource.getMessage("Validation.common.Field.Range.Number", new Object[] { displayName, min, max }));
	    }
	} else {
	    throw new UnSupportedValidationCheckException();
	}
    }

    public void validateIsValidDigits(FieldValidator fieldValidator) {
	if (!validateIsEmpty(fieldValidator)) {
	    return;
	}

	String targetId = fieldValidator.getTargetId();
	String displayName = fieldValidator.getDisplayName();
	if (fieldValidator.getTarget() instanceof String) {
	    String target = (String) fieldValidator.getTarget();
	    Errors errors = fieldValidator.getErrors();
	    if (!Pattern.matches("[\\-\\+]?\\d+", target)) {
		errors.rejectValue(targetId, "",
			messageSource.getMessage("Validation.common.Field.InvalidNumber", new Object[] { displayName }));
	    }
	} else {
	    throw new UnSupportedValidationCheckException();
	}
    }

    // http://stackoverflow.com/questions/15111420/how-to-check-if-a-string-contains-only-digits-in-java#15111450
    public void validateIsValidUnSignDigits(FieldValidator fieldValidator) {
	if (!validateIsEmpty(fieldValidator)) {
	    return;
	}

	String targetId = fieldValidator.getTargetId();
	String displayName = fieldValidator.getDisplayName();
	if (fieldValidator.getTarget() instanceof String) {
	    String target = (String) fieldValidator.getTarget();
	    Errors errors = fieldValidator.getErrors();
	    if (!Pattern.matches("\\d+", target)) {
		errors.rejectValue(targetId, "",
			messageSource.getMessage("Validation.common.Field.InvalidUnsignNumber", new Object[] { displayName }));
	    }
	} else {
	    throw new UnSupportedValidationCheckException();
	}
    }

    public void validateIsValidAlphaBatics(FieldValidator fieldValidator) {
	if (!validateIsEmpty(fieldValidator)) {
	    return;
	}

	String targetId = fieldValidator.getTargetId();
	String displayName = fieldValidator.getDisplayName();
	if (fieldValidator.getTarget() instanceof String) {
	    String target = (String) fieldValidator.getTarget();
	    Errors errors = fieldValidator.getErrors();
	    if (!Pattern.matches("^[a-zA-Z_ ]+$", target)) {
		errors.rejectValue(targetId, "",
			messageSource.getMessage("Validation.common.Field.InvalidAlphabet", new Object[] { displayName }));
	    }
	} else {
	    throw new UnSupportedValidationCheckException();
	}
    }

    // include space and underscore
    public void validateIsValidAlphaNumerics(FieldValidator fieldValidator) {
	if (!validateIsEmpty(fieldValidator)) {
	    return;
	}

	String targetId = fieldValidator.getTargetId();
	String displayName = fieldValidator.getDisplayName();
	if (fieldValidator.getTarget() instanceof String) {
	    String target = (String) fieldValidator.getTarget();
	    Errors errors = fieldValidator.getErrors();
	    if (!Pattern.matches("^[a-zA-Z_0-9 ]+$", target)) {
		errors.rejectValue(targetId, "",
			messageSource.getMessage("Validation.common.Field.InvalidAlphaNumeric", new Object[] { displayName }));
	    }
	} else {
	    throw new UnSupportedValidationCheckException();
	}
    }

    // to validate for sql queries
    public void validateIsValidQueryString(FieldValidator fieldValidator) {
	if (!validateIsEmpty(fieldValidator)) {
	    return;
	}

	String targetId = fieldValidator.getTargetId();
	String displayName = fieldValidator.getDisplayName();
	if (fieldValidator.getTarget() instanceof String) {
	    String target = (String) fieldValidator.getTarget();
	    Errors errors = fieldValidator.getErrors();
	    if (!Pattern.matches("^[a-zA-Z_0-9 \\/\\-\\.\\@]+$", target)) {
		errors.rejectValue(targetId, "",
			messageSource.getMessage("Validation.common.Field.InvalidQueryString", new Object[] { displayName }));
	    }
	} else {
	    throw new UnSupportedValidationCheckException();
	}
    }

    public void validateIsValidCapitalLetters(FieldValidator fieldValidator) {
	if (!validateIsEmpty(fieldValidator)) {
	    return;
	}

	String targetId = fieldValidator.getTargetId();
	String displayName = fieldValidator.getDisplayName();
	if (fieldValidator.getTarget() instanceof String) {
	    String target = (String) fieldValidator.getTarget();
	    Errors errors = fieldValidator.getErrors();
	    if (!Pattern.matches("^[A-Z_ \\-]+$", target)) {
		errors.rejectValue(targetId, "",
			messageSource.getMessage("Validation.common.Field.InvalidCapitalLetter", new Object[] { displayName }));
	    }
	} else {
	    throw new UnSupportedValidationCheckException();
	}
    }

    public void validateIsValidSmallLetters(FieldValidator fieldValidator) {
	if (!validateIsEmpty(fieldValidator)) {
	    return;
	}

	String targetId = fieldValidator.getTargetId();
	String displayName = fieldValidator.getDisplayName();
	if (fieldValidator.getTarget() instanceof String) {
	    String target = (String) fieldValidator.getTarget();
	    Errors errors = fieldValidator.getErrors();
	    if (!Pattern.matches("^[a-z_ \\-]+$", target)) {
		errors.rejectValue(targetId, "",
			messageSource.getMessage("Validation.common.Field.InvalidSmallLetter", new Object[] { displayName }));
	    }
	} else {
	    throw new UnSupportedValidationCheckException();
	}
    }

    // http://stackoverflow.com/questions/15805555/java-regex-to-validate-full-name-allow-only-spaces-and-letters#15806080
    public void validateIsValidUnicodeCharacters(FieldValidator fieldValidator) {
	if (!validateIsEmpty(fieldValidator)) {
	    return;
	}

	String targetId = fieldValidator.getTargetId();
	String displayName = fieldValidator.getDisplayName();
	if (fieldValidator.getTarget() instanceof String) {
	    String target = (String) fieldValidator.getTarget();
	    Errors errors = fieldValidator.getErrors();
	    if (!Pattern.matches("^[\\p{L} .'-]+$", target)) {
		errors.rejectValue(targetId, "",
			messageSource.getMessage("Validation.common.Field.InvalidUnicode", new Object[] { displayName }));
	    }
	} else {
	    throw new UnSupportedValidationCheckException();
	}
    }

    // http://stackoverflow.com/questions/27938415/regex-for-password-atleast-1-letter-1-number-1-special-character-and-should#27938511
    // and add allowed space for between words
    public void validateIsValidPasswordPattern(FieldValidator fieldValidator) {
	if (!validateIsEmpty(fieldValidator)) {
	    return;
	}

	String targetId = fieldValidator.getTargetId();
	String displayName = fieldValidator.getDisplayName();
	if (fieldValidator.getTarget() instanceof String) {
	    String target = (String) fieldValidator.getTarget();
	    Errors errors = fieldValidator.getErrors();
	    if (!Pattern.matches("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+])[A-Za-z\\d][A-Za-z \\d!@#$%^&*()_+]{8,}$", target)) {
		errors.rejectValue(targetId, "",
			messageSource.getMessage("Validation.UserBean.Field.InvalidPassword", new Object[] { displayName }));
	    }
	} else {
	    throw new UnSupportedValidationCheckException();
	}
    }

    // http://stackoverflow.com/questions/8204680/java-regex-email/13013056#13013056
    public void validateIsValidEmail(FieldValidator fieldValidator) {
	if (!validateIsEmpty(fieldValidator)) {
	    return;
	}

	String targetId = fieldValidator.getTargetId();
	if (fieldValidator.getTarget() instanceof String) {
	    String target = (String) fieldValidator.getTarget();
	    Errors errors = fieldValidator.getErrors();
	    String emailPattern = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
	    Pattern pattern = Pattern.compile(emailPattern, Pattern.CASE_INSENSITIVE);
	    if (!pattern.matcher(target).find()) {
		errors.rejectValue(targetId, "", messageSource.getMessage("Validation.common.Field.InvalidEmail", new Object[] { target }));
	    }
	} else {
	    throw new UnSupportedValidationCheckException();
	}
    }

    // http://stackoverflow.com/questions/163360/regular-expression-to-match-urls-in-java#163410
    public void validateIsValidURL(FieldValidator fieldValidator) {
	if (!validateIsEmpty(fieldValidator)) {
	    return;
	}

	String targetId = fieldValidator.getTargetId();
	if (fieldValidator.getTarget() instanceof String) {
	    String target = (String) fieldValidator.getTarget();
	    try {
		new URL(target);
	    } catch (MalformedURLException e) {
		Errors errors = fieldValidator.getErrors();
		errors.rejectValue(targetId, "",
			messageSource.getMessage("Validation.common.Field.InvalidNumber", new Object[] { target }));
	    }
	} else {
	    throw new UnSupportedValidationCheckException();
	}
    }

    // http://stackoverflow.com/questions/308122/simple-regular-expression-for-a-decimal-with-a-precision-of-2#308216
    public void validateIsValidFloatingPointNumbers(FieldValidator fieldValidator, int precision) {
	if (!validateIsEmpty(fieldValidator)) {
	    return;
	}
	String targetId = fieldValidator.getTargetId();
	String displayName = fieldValidator.getDisplayName();
	Errors errors = fieldValidator.getErrors();
	if (fieldValidator.getTarget() instanceof Double) {
	    Double target = (Double) fieldValidator.getTarget();
	    if (!Pattern.matches("[\\-\\+]?[0-9]+(\\.[0-9]{1," + precision + "})?$", target.toString())) {
		errors.rejectValue(targetId, "",
			messageSource.getMessage("Validation.common.Field.InvalidNumber", new Object[] { displayName }));
	    }
	} else if (fieldValidator.getTarget() instanceof Float) {
	    if (fieldValidator.getTarget() instanceof Double) {
		Float target = (Float) fieldValidator.getTarget();
		if (!Pattern.matches("[\\-\\+]?[0-9]+(\\.[0-9]{1," + precision + "})?$", target.toString())) {
		    errors.rejectValue(targetId, "", messageSource.getMessage("Validation.common.Field.InvalidFloatingPointNumber",
			    new Object[] { displayName, precision }));
		}
	    }
	} else {
	    throw new UnSupportedValidationCheckException();
	}
    }

    public PageMode getPageMode() {
	return pageMode;
    }

    public void setPageMode(PageMode pageMode) {
	this.pageMode = pageMode;
    }

    @Override
    public boolean supports(Class<?> clazz) {
	return clazz.isInstance(BaseBean.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
    }
}
