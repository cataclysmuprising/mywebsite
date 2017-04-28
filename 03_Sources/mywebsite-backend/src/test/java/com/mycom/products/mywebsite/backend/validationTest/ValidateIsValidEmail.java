package com.mycom.products.mywebsite.backend.validationTest;

import java.util.regex.Pattern;

public class ValidateIsValidEmail {
    public static void main(String[] args) {
	String emailPattern = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
	Pattern pattern = Pattern.compile(emailPattern, Pattern.CASE_INSENSITIVE);
	for (String input : ValidationTest.INPUTS) {
	    System.out.println(emailPattern + " <-----> " + input + " >>> " + pattern.matcher(input).find());

	}
    }
}