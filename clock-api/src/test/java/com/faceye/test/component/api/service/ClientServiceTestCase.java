package com.faceye.test.component.api.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import com.faceye.component.api.Application;
import com.faceye.component.api.service.ClientService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class ClientServiceTestCase {
	@Autowired
ClientService clientService=null;
	@Test
	public void testAdd() throws Exception{
		int res=clientService.add(1, 2);
		Assert.isTrue(res>0);
	}
}
