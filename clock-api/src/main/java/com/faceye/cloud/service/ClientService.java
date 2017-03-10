package com.faceye.cloud.service;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.faceye.cloud.service.impl.ClientServiceHystrix;

@FeignClient(value="clock-provider",fallback =ClientServiceHystrix.class)
public interface ClientService {
	@RequestMapping(method = RequestMethod.GET, value = "/add")
    Integer add(@RequestParam(value = "a") Integer a, @RequestParam(value = "b") Integer b);
}
