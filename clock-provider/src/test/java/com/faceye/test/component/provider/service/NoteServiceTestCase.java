package com.faceye.test.component.provider.service;

import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.collections.CollectionUtils;
import org.eclipse.core.runtime.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.faceye.component.Application;
import com.faceye.component.provider.entity.Note;
import com.faceye.component.provider.service.NoteService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@Transactional
public class NoteServiceTestCase {
	@Autowired
	private NoteService noteService=null;
	@Before
	@Rollback(true)
	public void set() throws Exception{
		this.noteService.removeAll();
	}
	@Test
	@Rollback(true)
   public void saveNote() throws Exception{
	   Note note =new Note();
	  note.setTitle("test-note");
	  note.setText("test-text");
	  this.noteService.save(note);
	  List<Note> notes=this.noteService.getAll();
	 Assert.isTrue(CollectionUtils.isNotEmpty(notes)&&notes.size()==1);
   }
}
