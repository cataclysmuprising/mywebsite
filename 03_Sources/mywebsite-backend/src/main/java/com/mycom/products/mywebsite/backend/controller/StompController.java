package com.mycom.products.mywebsite.backend.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.mycom.products.mywebsite.backend.dto.CalcInput;
import com.mycom.products.mywebsite.backend.dto.Result;

@Controller
public class StompController {

	@MessageMapping("/hello")
	@SendTo("/topic/greet")
	public Result addNum(CalcInput input) throws Exception {
		System.err.println("Reach Here .....................");
		Thread.sleep(2000);
		Result result = new Result(input.getNum1() + "+" + input.getNum2() + "=" + (input.getNum1() + input.getNum2()));
		return result;
	}

}
