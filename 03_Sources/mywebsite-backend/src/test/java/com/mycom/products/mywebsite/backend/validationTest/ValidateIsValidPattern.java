/*
 * @author Mg Than Htike Aung {@literal <rage.cataclysm@gmail.com@address>}
 * @Since 1.0
 * 
 */
package com.mycom.products.mywebsite.backend.validationTest;

import java.util.regex.Pattern;

public class ValidateIsValidPattern {
	public static void main(String[] args) {
		String regex = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+])[A-Za-z\\d][A-Za-z \\d!@#$%^&*()_+]{8,}$";
		String[] samplePasswords = { "Aa@1", "Aa@1 zZ12", " Aa@1zZ12", "user-pwd", "User1@gmail_com", "123456", "USP@ssw0rd", "For the lich king #123", "$$$iii@123##ABC def", "123asdf!@#", "12as#", "Mg Mg is no#1 in class **12** & good at (math)" };
		for (String input : samplePasswords) {
			System.out.println(regex + " <-----> " + input + " >>> " + Pattern.matches(regex, input));

		}
	}
}