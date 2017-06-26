package com.faceye.test.component.api.service;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.util.Assert;

import com.faceye.component.api.Application;
import com.faceye.component.api.service.NoteService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
// @IntegrationTest({"server.port=0", "management.port=0",
// "spring.profiles.active=test"})
public class NoteServiceTestCase {
	private Logger logger = LoggerFactory.getLogger(NoteServiceTestCase.class);
	@Autowired
	private NoteService noteService = null;

	@Test
	public void testAdd() throws Exception {
		String title = "test from api";
		String text = "test from api text.";
		String res = this.noteService.add(title, text);
		logger.debug(">>Res is:" + res);
		Assert.isTrue(StringUtils.contains("success", res));
	}
}
