package com.mycom.products.mywebsite.backend.validationTest;

public class ValidationTest {
    public static final String[] INPUTS = { "1", "-1", "+1", "0.1", "-7.8", "12345", "25-", ".5", "2.5", "2.05",
	    "1.2345", "-123.456789", "1,000.01,234", "abc", "123abcABC", "AA", "aA", "a b c", "a-b", "12/12/2017",
	    "my_variable", "a_Z", "'''", "$'", "`^!()+_", "@345", "#<include>", "....", "user@gmail.com",
	    "cataclysmuprisingp@gmail.com", "user@[IPv6:2001:DB8::1]", "user@localserver", "https://www.google.com",
	    "file:///D:/offlineWebsites/W3C/www.w3schools.com/index.html", "François Hollande" };
}
