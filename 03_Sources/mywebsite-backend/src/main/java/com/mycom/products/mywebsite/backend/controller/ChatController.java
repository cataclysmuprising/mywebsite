package com.mycom.products.mywebsite.backend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.mycom.products.mywebsite.backend.annotation.Loggable;
import com.mycom.products.mywebsite.core.exception.BusinessException;

@Controller
@Loggable
@RequestMapping("/chat")
public class ChatController extends BaseController {

	@Override
	public void subInit(Model model) throws BusinessException {
		setAuthorities(model, "chat");
	}

	@RequestMapping(method = RequestMethod.GET)
	public String home(Model model) {
		return "chat_home";
	}
}
