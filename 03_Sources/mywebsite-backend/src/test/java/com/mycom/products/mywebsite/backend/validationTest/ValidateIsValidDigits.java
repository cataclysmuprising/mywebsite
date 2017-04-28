package com.mycom.products.mywebsite.backend.validationTest;

import java.util.regex.Pattern;

public class ValidateIsValidDigits {
    public static void main(String[] args) {
	String regex = "[\\-\\+]?\\d+";
	for (String input : ValidationTest.INPUTS) {
	    System.out.println(regex + " <-----> " + input + " >>> " + Pattern.matches(regex, input));

	}
    }
}
