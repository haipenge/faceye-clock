package com.faceye.component.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.dao.ReflectionSaltSource;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

import com.faceye.component.security.web.service.ResourceService;
import com.faceye.component.security.web.service.UserService;
import com.faceye.component.security.web.service.impl.SecurityAccessDecisionManager;

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
	
	@Autowired
	@Qualifier("webResourceService")
	private ResourceService resourceService = null;
	
	private FilterSecurityInterceptor filterSecurityInterceptor=null;
	private AccessDecisionManager accessDecisionManager = null;
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
	@Bean
	public FilterSecurityInterceptor filterSecurityInterceptor() {
		filterSecurityInterceptor = new FilterSecurityInterceptor();
		filterSecurityInterceptor.setSecurityMetadataSource(resourceService);
		filterSecurityInterceptor.setAuthenticationManager(authenticationManager);
		filterSecurityInterceptor.setAccessDecisionManager(accessDecisionManager());
		return filterSecurityInterceptor;
	}
	
	@Bean
	public AccessDecisionManager accessDecisionManager() {
		List<AccessDecisionVoter<? extends Object>> voters = new ArrayList<AccessDecisionVoter<? extends Object>>();
		voters.add(roleVoter);
		voters.add(authenticatedVoter);
		accessDecisionManager = new SecurityAccessDecisionManager(voters);
		return accessDecisionManager;
	}
}
