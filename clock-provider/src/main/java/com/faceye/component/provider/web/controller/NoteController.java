package com.faceye.component.provider.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.faceye.component.provider.entity.Note;
import com.faceye.component.provider.service.NoteService;

@RestController("/note")
public class NoteController {
	@Autowired
	private NoteService noteService = null;

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String add(@RequestParam String title, @RequestParam String text) {
		Note note = new Note();
		note.setTitle(title);
		note.setText(text);
		this.noteService.save(note);
		return "{res:\"success\"}";
	}
}
