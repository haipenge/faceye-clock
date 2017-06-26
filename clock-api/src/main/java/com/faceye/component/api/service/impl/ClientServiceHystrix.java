package com.faceye.component.api.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.faceye.component.api.service.ClientService;

@Service
public class ClientServiceHystrix implements ClientService {

	@Override
	public Integer add(@RequestParam(value = "a") Integer a, @RequestParam(value = "b") Integer b) {
		 return -9999;
	}
}
