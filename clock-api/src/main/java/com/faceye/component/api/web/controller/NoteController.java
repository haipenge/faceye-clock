package com.faceye.component.api.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.faceye.component.api.service.NoteService;


@RestController("/api/note")
public class NoteController {
	@Autowired
	RestTemplate restTemplate;
	@Autowired
	private NoteService noteService = null;
    @RequestMapping(value="/add",method = RequestMethod.POST)
	public String add(@RequestParam String title, @RequestParam String text) {
		String res = "";
		res = this.noteService.add(title, text);
		return res;
	}
}
