package com.faceye.component.security.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
@RequestMapping("/user")
public class UserController {

	@RequestMapping("/login")
	public String login(HttpServletRequest request){
		return "security/user/login";
	}
}
