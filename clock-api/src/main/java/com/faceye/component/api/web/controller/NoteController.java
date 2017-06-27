package com.faceye.component.api.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.faceye.component.api.service.NoteService;


@RestController
public class NoteController {
	@Autowired
	private NoteService noteService = null;
    @RequestMapping(value="/api/note/add",method = RequestMethod.POST)
	public String add(@RequestParam String title, @RequestParam String text) {
		String res = "";
		res = this.noteService.add(title, text);
		return res;
	}
}
