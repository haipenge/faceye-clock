package com.faceye.component.api.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.faceye.component.api.service.NoteService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@Service
public class NoteServiceImpl implements NoteService {
	@Autowired
	private RestTemplate restTemplate;

	@Override
	@HystrixCommand(fallbackMethod = "addServiceFallback")
	public String add(String title, String text) {
		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		String url = "http://clock-provider/note/add";
		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		params.add("title", title);
		params.add("text", text);
		HttpEntity entity = new HttpEntity(params, header);
		// ResponseEntity<String> responseEntity =
		// restTemplate.exchange("https://url", HttpMethod.POST, entity,
		// String.class);
		String res = this.restTemplate.postForEntity(url, entity, String.class).getBody();
		return res;
	}

	public String addServiceFallback(String title, String text) {
		return "error";
	}

}
