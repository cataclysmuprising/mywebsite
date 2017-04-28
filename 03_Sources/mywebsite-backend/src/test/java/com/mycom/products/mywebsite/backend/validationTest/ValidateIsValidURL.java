package com.mycom.products.mywebsite.backend.validationTest;

import java.net.MalformedURLException;
import java.net.URL;

public class ValidateIsValidURL {
    public static void main(String[] args) {
	for (String input : ValidationTest.INPUTS) {
	    try {
		new URL(input);
	    } catch (MalformedURLException e) {
		System.out.println(input + " >>> " + false);
		continue;
	    }
	    System.out.println(input + " >>> " + true);

	}
    }
}