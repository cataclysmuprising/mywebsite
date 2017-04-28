package com.mycom.products.mywebsite.backend.validationTest;

import java.util.regex.Pattern;

public class ValidateIsValidUnicodeCharacters {
    public static void main(String[] args) {
	String regex = "^[\\p{L} .'-]+$";
	for (String input : ValidationTest.INPUTS) {
	    System.out.println(regex + " <-----> " + input + " >>> " + Pattern.matches(regex, input));

	}
    }
}