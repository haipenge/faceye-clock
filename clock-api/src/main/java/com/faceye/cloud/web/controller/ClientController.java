package com.faceye.cloud.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.faceye.cloud.service.ClientService;
import com.faceye.cloud.service.CustomerService;
@RestController
public class ClientController {
	@Autowired
    RestTemplate restTemplate;
	@Autowired
	private ClientService clientService=null;
	@Autowired
	private CustomerService customerService=null;

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add() {
        return restTemplate.getForEntity("http://clock-provider/add?a=10&b=20", String.class).getBody();
    }
    
    @RequestMapping(value = "/add2", method = RequestMethod.GET)
    public Integer add2() {
        return clientService.add(20, 20);
    }
    
    @RequestMapping(value = "/add3", method = RequestMethod.GET)
    public String add3() {
        return customerService.addService();
    }

}
