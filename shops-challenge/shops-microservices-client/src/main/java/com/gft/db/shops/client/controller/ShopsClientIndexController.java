package com.gft.db.shops.client.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ShopsClientIndexController {

	@RequestMapping("/")
	public String home() {
		return "index";
	}
}
