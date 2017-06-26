package com.faceye.component.provider.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.faceye.component.provider.entity.Note;
import com.faceye.component.provider.repository.jpa.NoteRepository;
import com.faceye.component.provider.service.NoteService;
import com.faceye.feature.service.impl.BaseServiceImpl;

@Service
public class NoteServiceImpl extends BaseServiceImpl<Note, Long, NoteRepository> implements NoteService {

	@Autowired
	public NoteServiceImpl(NoteRepository dao) {
		super(dao);
	}

	

}
