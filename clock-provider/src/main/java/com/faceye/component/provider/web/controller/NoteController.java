package com.faceye.component.provider.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.faceye.component.provider.entity.Note;
import com.faceye.component.provider.service.NoteService;

@RestController
public class NoteController {
	@Autowired
	private NoteService noteService = null;

	@RequestMapping(value = "/note/add", method = RequestMethod.POST)
	public String add(@RequestParam(required = true) String title, @RequestParam(required = true) String text) {
		Note note = new Note();
		note.setTitle(title);
		note.setText(text);
		this.noteService.save(note);
		return "{res:\"success\"}";
	}

	@RequestMapping(value = "/note/{id}", method = RequestMethod.GET)
	public Note get(@PathVariable Long id) {
		String res = "";
		Note note = this.noteService.get(id);
		return note;
	}

	@RequestMapping(value = "/notes", method = RequestMethod.GET)
	public Page<Note> get(@RequestParam(required = true, defaultValue = "1") Integer page,
			@RequestParam(required = true, defaultValue = "15") Integer size) {
		Page<Note> notes = this.noteService.getPage(null, page, size);
		return notes;
	}

	@RequestMapping(value = "/note/{id}", method = RequestMethod.DELETE)
	public String remove(@PathVariable Long id) {
		this.noteService.remove(id);
		return "{res:\"success\"}";
	}

}
