package com.mycom.products.mywebsite.backend.validationTest;

import java.util.regex.Pattern;

public class ValidateIsValidAlphaNumerics {
    public static void main(String[] args) {
	String regex = "^[a-zA-Z_0-9 ]+$";
	for (String input : ValidationTest.INPUTS) {
	    System.out.println(regex + " <-----> " + input + " >>> " + Pattern.matches(regex, input));

	}
    }
}
