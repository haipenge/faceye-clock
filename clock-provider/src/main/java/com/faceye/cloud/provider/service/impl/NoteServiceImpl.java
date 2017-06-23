package com.faceye.cloud.provider.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.faceye.cloud.provider.entity.Note;
import com.faceye.cloud.provider.repository.jpa.NoteRepository;
import com.faceye.cloud.provider.service.NoteService;
import com.faceye.feature.service.impl.BaseServiceImpl;

@Service
public class NoteServiceImpl extends BaseServiceImpl<Note, Long, NoteRepository> implements NoteService {

	@Autowired
	public NoteServiceImpl(NoteRepository dao) {
		super(dao);
	}

	

}
