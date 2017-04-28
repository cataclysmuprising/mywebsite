package com.mycom.products.mywebsite.backend.validationTest;

import java.util.regex.Pattern;

public class ValidateIsValidFloatingPointNumbers {
    public static void main(String[] args) {
	int precision = 2;
	String regex = "[\\-\\+]?[0-9]+(\\.[0-9]{1," + precision + "})?$";
	for (String input : ValidationTest.INPUTS) {
	    System.out.println(regex + " <-----> " + input + " >>> " + Pattern.matches(regex, input));

	}
    }
}