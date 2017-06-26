package com.faceye.cloud.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;

import com.faceye.component.security.web.service.UserService;

@Configuration
@Order(1)
public class SecurityBasicConfiguration {
	private RoleVoter roleVoter=null;
	private AuthenticatedVoter authenticatedVoter=null;
	private Md5PasswordEncoder passwordEncoder=null;
	private ReflectionSaltSource saltSource=null;
	@Autowired
	@Qualifier("webUserService")
	private UserService userService = null;
	private DaoAuthenticationProvider daoAuthenticationProvider=null;
	private AuthenticationManager authenticationManager=null;
	@Bean
	public RoleVoter roleVoter(){
		roleVoter=new RoleVoter();
		roleVoter.setRolePrefix("ROLE_");
		return roleVoter;
	}
	@Bean
	public AuthenticatedVoter authenticatedVoter(){
		authenticatedVoter=new AuthenticatedVoter();
		return authenticatedVoter;
	}
	@Bean
	public Md5PasswordEncoder passwordEncoder() {
		passwordEncoder = new Md5PasswordEncoder();
		passwordEncoder.setEncodeHashAsBase64(false);
		return passwordEncoder;
	}

	@Bean
	public ReflectionSaltSource saltSource() {
		saltSource = new ReflectionSaltSource();
		saltSource.setUserPropertyToUse("getUsername");
		return saltSource;
	}
	@Bean
	public AuthenticationManager authenticationManager(){
		List<AuthenticationProvider> providers=new ArrayList<AuthenticationProvider>(0);
		providers.add(daoAuthenticationProvider());
		authenticationManager=new ProviderManager(providers);
		return authenticationManager;
	}
	@Bean
	public DaoAuthenticationProvider daoAuthenticationProvider(){
		daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
		daoAuthenticationProvider.setSaltSource(saltSource());
		daoAuthenticationProvider.setUserDetailsService(userService);
		return daoAuthenticationProvider;
	}
}
