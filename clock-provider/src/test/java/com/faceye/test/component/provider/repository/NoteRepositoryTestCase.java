package com.faceye.test.component.provider.repository;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.eclipse.core.runtime.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.faceye.component.Application;
import com.faceye.component.provider.entity.Note;
import com.faceye.component.provider.repository.jpa.NoteRepository;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@Rollback(true)
public class NoteRepositoryTestCase {
	
	@Autowired
	private NoteRepository noteRepository=null;
	@Test
	@Rollback(true)
	public void testSaveNote() throws Exception{
		Note note=new Note();
		note.setTitle("I am a test note");
		note.setText("I am a test note text .");
		this.noteRepository.save(note);
		List<Note> notes=this.noteRepository.getPage(null, 1, 0).getContent();
		Assert.isTrue(CollectionUtils.isNotEmpty(notes));
	}

}
