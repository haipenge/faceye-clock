package com.faceye.component.api.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.faceye.component.api.service.NoteService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@Service
public class NoteServiceImpl implements NoteService {
	@Autowired
    RestTemplate restTemplate;
	@Override
	@HystrixCommand(fallbackMethod = "addServiceFallback")
	public String add(String title, String text) {
		HttpHeaders header=new HttpHeaders();
		MediaType type=MediaType.parseMediaType("application/json;charset=UTF-8");
		header.setContentType(type);
		header.add("ACCEPT", MediaType.APPLICATION_JSON_UTF8_VALUE);
		
		String url="http://cloud-provider/note/add";
		Map params=new HashMap();
		params.put("title", title);
		params.put("text", text);
//		JSONObject jsonObj=JSONObject.wrap(params);
		HttpEntity entity=new HttpEntity(params,header);
		String res=this.restTemplate.postForEntity(url, entity, String.class, params).getBody().toString();
//		String res=this.restTemplate.postForEntity(url, String.class, params).getBytes().toString();
		return res;
	}

	public String addServiceFallback() {
		return "error";
	}

}
