package com.faceye.component.security.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller("cloud-security")
public class DefaultController {

	/**
	 * 首页
	 * 
	 * @return
	 * @Desc:
	 * @Author:haipenge
	 * @Date:2017年7月2日 下午5:28:08
	 */
	@RequestMapping("/")
	public String index() {
		return "index";
	}
	
//	@RequestMapping("/error")
//	public String error(){
//	   return "error/error";
//	}

	@RequestMapping("/index")
	public String indexHtml() {
		return "index";
	}
//	
//	@RequestMapping("/default")
//	public String home(){
//		return "index";
//	}
}
