package com.faceye.component.api.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.faceye.component.api.service.NoteService;




@RestController
public class NoteController {
	private Logger logger=LoggerFactory.getLogger(NoteController.class);
	@Autowired
	private NoteService noteService = null;
    @RequestMapping(value="/api/note/add",method = RequestMethod.POST)
	public String add(@RequestParam String title, @RequestParam String text) {
		String res = "";
		res = this.noteService.add(title, text);
		return res;
	}
    @RequestMapping("/info")
    public String info(HttpServletRequest request){
    	String code=request.getParameter("code");
    	if(StringUtils.isNotEmpty("code")){
    		logger.debug(">>FaceYe :code is:"+code);
    	}
    	return "info";
    }
}
