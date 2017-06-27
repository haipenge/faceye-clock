package com.faceye.test.component.provider.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import com.faceye.component.Application;
import com.faceye.component.provider.entity.Note;
import com.faceye.component.provider.service.NoteService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class NoteControllerTestCase {
	private Logger logger = LoggerFactory.getLogger(NoteControllerTestCase.class);
	@Autowired
	private NoteService noteService=null;
	MockMvc mvc;

	@Autowired
	WebApplicationContext webApplicationConnect;

	@Before
	@Transactional
	@Rollback(true)
	public void setUp() throws Exception {
		mvc = MockMvcBuilders.webAppContextSetup(webApplicationConnect).build();
		this.prepearData();
	}
	/**
	 * 准备测试数据
	 * @throws Exception
	 */
	private void prepearData() throws Exception{
		List<Note> notes=new ArrayList<Note>(0);
		for(int i=0;i<100;i++){
			Note note =new Note();
			note.setTitle("Note Test Title "+i);
			note.setText("Note test text "+i);
			notes.add(note);
		}
		this.noteService.save(notes);
	}
	@After
	public void clear() throws Exception{
		
	}
	@Test
	public void testAdd() throws Exception {
		String uri = "/note/add";
		MultiValueMap params = new LinkedMultiValueMap();
		params.set("title", "title form test case");
		params.set("text", "text from test case");
		URI u = null;
		MvcResult mvcResult = mvc
				.perform(MockMvcRequestBuilders.post(uri).params(params).accept(MediaType.APPLICATION_JSON))
				.andReturn();
		String result = mvcResult.getResponse().getContentAsString();
		int status = mvcResult.getResponse().getStatus();
		logger.debug(">>FaceYe :" + result + ",status is:" + status);
		Assert.isTrue(status == 200 && StringUtils.contains(result, "success"));
	}
	@Test
	public void testGet() throws Exception{
		Note note =new Note();
		note.setTitle("note in test case for get");
		note.setText("note in test case for get of text");
		Note res=this.noteService.save(note);
		String uri="/note/"+res.getId();
		MvcResult mvcResult=mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON)).andReturn();
		String response=mvcResult.getResponse().getContentAsString();
		int status=mvcResult.getResponse().getStatus();
		Assert.isTrue(status==200 && StringUtils.contains(response, note.getTitle()));
	}
	@Test
	public void testRemove() throws Exception{
		Note note =new Note();
		note.setTitle("note in test case for get");
		note.setText("note in test case for get of text");
		Note res=this.noteService.save(note);
		String uri="/note/"+res.getId();
		MvcResult mvcResult=mvc.perform(MockMvcRequestBuilders.delete(uri).accept(MediaType.APPLICATION_JSON)).andReturn();
		String response=mvcResult.getResponse().getContentAsString();
		int status=mvcResult.getResponse().getStatus();
		Note deleteNote=this.noteService.get(res.getId());
		Assert.isTrue(deleteNote==null &&status==200 && StringUtils.contains(response, "success"));
	}
	@Test
	public void testGetPage() throws Exception{
		int page =1;
		int size=10;
		String uri="/notes";
		MvcResult mvcResult=mvc.perform(MockMvcRequestBuilders.get(uri).param("page", ""+page).param("size", ""+size).accept(MediaType.APPLICATION_JSON)).andReturn();
		String response=mvcResult.getResponse().getContentAsString();
		int status=mvcResult.getResponse().getStatus();
		logger.debug(">>Face Page Response is:"+response);
		Assert.isTrue(status==200  && StringUtils.isNotEmpty(response));
	}
}
