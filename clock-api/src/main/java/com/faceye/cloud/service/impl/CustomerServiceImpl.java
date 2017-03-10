package com.faceye.cloud.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.faceye.cloud.service.CustomerService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@Service
public class CustomerServiceImpl implements CustomerService {
	@Autowired
    RestTemplate restTemplate;

    @HystrixCommand(fallbackMethod = "addServiceFallback")
    public String addService() {
        return restTemplate.getForEntity("http://cloud-provider/add?a=30&b=20", String.class).getBody();
    }

    public String addServiceFallback() {
        return "error";
    }

}
