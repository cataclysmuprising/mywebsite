/*
 * This source file is free software, available under the following license: MIT license. 
 * Copyright (c) 2017, Than Htike Aung
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 * 
 *	mywebsite-backend - ActionController.java
 *	Using JRE 1.8.0_121
 *	@author Than Htike Aung {@literal <rage.cataclysm@gmail.com>}
 *	@Since 2017
 * 
 */

package com.mycom.products.mywebsite.backend.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mycom.products.mywebsite.backend.annotation.HandleAjaxException;
import com.mycom.products.mywebsite.backend.annotation.Loggable;
import com.mycom.products.mywebsite.core.bean.config.ActionBean;
import com.mycom.products.mywebsite.core.exception.BusinessException;
import com.mycom.products.mywebsite.core.service.config.api.ActionService;
import com.mycom.products.mywebsite.core.util.FetchMode;

@Controller
@Loggable
@RequestMapping("/actions")
public class ActionController extends BaseController {

    @Autowired
    private ActionService actionService;

    @Override
    public void subInit(Model model) throws BusinessException {
	setAuthorities(model, "action");
    }

    @RequestMapping(value = "api/pages", method = RequestMethod.GET)
    @HandleAjaxException
    public @ResponseBody Map<String, Object> searchAllPageNames() throws BusinessException {
	Map<String, Object> results = new HashMap<String, Object>();
	List<ActionBean> actions = actionService.selectList(null, FetchMode.LAZY);
	List<String> pageNames = actions.parallelStream().filter(distinctByKey(action -> action.getPage())).map(action -> action.getPage())
		.collect(Collectors.toList());
	results.put("pageNames", pageNames);
	return results;
    }

    @RequestMapping(value = "/api/search")
    @HandleAjaxException
    public @ResponseBody Map<String, Object> search(@RequestParam int start, @RequestParam int length, @RequestParam String orderBy,
	    @RequestParam String orderAs, @RequestParam String word, @RequestParam(required = false) String page,
	    @RequestParam(required = false) String includeIds, @RequestParam(required = false) String excludeIds,
	    @RequestParam(required = false) String fetchMode) throws BusinessException {
	FetchMode mode = FetchMode.LAZY;
	if (fetchMode != null && fetchMode.equals("EAGER")) {
	    mode = FetchMode.valueOf("EAGER");
	}
	Map<String, Object> results = new HashMap<>();
	Map<String, Object> criteria = new HashMap<>();

	criteria.put("module", "backend");
	criteria.put("offset", start);
	criteria.put("limit", length);
	criteria.put("orderBy", orderBy);
	criteria.put("orderAs", orderAs);
	if (includeIds != null && includeIds.length() > 0) {
	    criteria.put("includeIds", includeIds.split(","));
	}
	if (excludeIds != null && excludeIds.length() > 0) {
	    criteria.put("excludeIds", excludeIds.split(","));
	}
	if (word != null && word.length() > 0) {
	    criteria.put("word", getPureString(word));
	}
	if (page != null && page.length() > 0) {
	    criteria.put("page", page);
	}
	results.put("iTotalDisplayRecords", actionService.selectCounts(criteria, mode));
	results.put("aaData", actionService.selectList(criteria, mode));

	return results;
    }

}
