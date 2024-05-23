package com.globe.mart.contoller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {
	
	@RequestMapping("/main/stores")
	public String view() {
		return("storespage.jsp");
	}
	
	@RequestMapping("/main/sales")
	public String view1() {
		return("salespage.jsp");
	}
	
}
