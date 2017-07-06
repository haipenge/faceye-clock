package com.faceye.component.security;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.access.vote.UnanimousBased;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.TokenApprovalStore;
import org.springframework.security.oauth2.provider.approval.TokenStoreUserApprovalHandler;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationManager;
import org.springframework.security.oauth2.provider.client.ClientCredentialsTokenEndpointFilter;
import org.springframework.security.oauth2.provider.client.ClientDetailsUserDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.web.access.expression.WebExpressionVoter;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.faceye.component.security.web.service.UserService;
import com.faceye.component.security.web.service.impl.AccessDeniedHandlerImpl;

@Configuration
@EnableAuthorizationServer
@Order(2)
public class OAuth2AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

	private Logger logger = LoggerFactory.getLogger(OAuth2AuthorizationServerConfiguration.class);
	@Autowired
	private DataSource dataSource;
	@Autowired
	private RoleVoter roleVoter = null;
	@Autowired
	private AuthenticatedVoter authenticatedVoter = null;
	@Autowired
	@Qualifier("webUserService")
	private UserService userService = null;
	@Autowired
	@Qualifier("daoAuthenticationProvider")
	private DaoAuthenticationProvider authenticationProvider = null;
	@Autowired
	private AuthenticationManager authenticationManager = null;

	// private BCryptPasswordEncoder passwordEncoder = new
	// BCryptPasswordEncoder();

	// @Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		security.checkTokenAccess("permitAll()").checkTokenAccess("isAuthenticated()");
		security.allowFormAuthenticationForClients();
		security.accessDeniedHandler(oauth2AccessDeniedHandler());
		security.addTokenEndpointAuthenticationFilter(clientCredentialsTokenEndpointFilter());
		security.authenticationEntryPoint(oauth2AuthenticationEntryPoint());

	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.withClientDetails(clientDetails());

	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints.tokenStore(tokenStore()).authenticationManager(authenticationManager)
				.userApprovalHandler(oauthUserApprovalHandler()).authorizationCodeServices(authorizationCodeServices())
				.tokenServices(tokenServices()).setClientDetailsService(clientDetails());
	}
	///////////////////////////////// Bean
	///////////////////////////////// Define//////////////////////////////////////////////////////////

	@Bean
	public TokenStore tokenStore() {
		if (dataSource == null) {
			logger.debug(">>datasource is null.");
		}
		return new JdbcTokenStore(dataSource);
	}

	@Bean
	public ClientDetailsService clientDetails() {
		return new JdbcClientDetailsService(dataSource);
	}

	@Bean
	public AuthenticationManager oauth2AuthenticationManager() {
		OAuth2AuthenticationManager oauth2AuthenticationManager = new OAuth2AuthenticationManager();
		oauth2AuthenticationManager.setClientDetailsService(clientDetails());
		oauth2AuthenticationManager.setTokenServices(tokenServices());
		return oauth2AuthenticationManager;
	}

	@Bean
	public DefaultTokenServices tokenServices() {
		DefaultTokenServices tokenServices = new DefaultTokenServices();
		tokenServices.setTokenStore(tokenStore());
		tokenServices.setSupportRefreshToken(true);
		tokenServices.setAccessTokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(30)); // 30天
		tokenServices.setClientDetailsService(clientDetails());
		return tokenServices;
	}

	@Bean
	public JdbcAuthorizationCodeServices authorizationCodeServices() {
		JdbcAuthorizationCodeServices authorizationCodeServices = new JdbcAuthorizationCodeServices(dataSource);
		return authorizationCodeServices;
	}

	@Bean
	public ClientDetailsUserDetailsService oauth2ClientDetailsUserService() {
		ClientDetailsUserDetailsService oauth2ClientDetailsUserService = new ClientDetailsUserDetailsService(
				clientDetails());
		return oauth2ClientDetailsUserService;
	}

	@Bean
	public OAuth2AuthenticationEntryPoint oauth2AuthenticationEntryPoint() {
		OAuth2AuthenticationEntryPoint oauth2AuthenticationEntryPoint = new OAuth2AuthenticationEntryPoint();
		return oauth2AuthenticationEntryPoint;
	}

	@Bean
	public DefaultOAuth2RequestFactory oauth2RequestFactory() {
		DefaultOAuth2RequestFactory oauth2RequestFactory = new DefaultOAuth2RequestFactory(clientDetails());
		return oauth2RequestFactory;
	}

	@Bean
	public OAuth2AccessDeniedHandler oauth2AccessDeniedHandler() {
		OAuth2AccessDeniedHandler oauth2AccessDeniedHandler = new OAuth2AccessDeniedHandler();
		return oauth2AccessDeniedHandler;
	}

	@Bean
	public TokenStoreUserApprovalHandler oauthUserApprovalHandler() {
		TokenStoreUserApprovalHandler oauthUserApprovalHandler = new TokenStoreUserApprovalHandler();
		oauthUserApprovalHandler.setTokenStore(tokenStore());
		oauthUserApprovalHandler.setClientDetailsService(clientDetails());
		oauthUserApprovalHandler.setRequestFactory(oauth2RequestFactory());
		return oauthUserApprovalHandler;
	}

	@Bean
	public UnanimousBased oauth2AccessDecisionManager() {
		List<AccessDecisionVoter<? extends Object>> voters = new ArrayList<AccessDecisionVoter<? extends Object>>(0);
		AccessDecisionVoter webExpressionVoter = new WebExpressionVoter();
		voters.add(webExpressionVoter);
		voters.add(roleVoter);
		voters.add(authenticatedVoter);
		// RoleVoter roleVoter=new RoleVoter();
		// roleVoter.setRolePrefix("ROLE_");
		UnanimousBased oauth2AccessDecisionManager = new UnanimousBased(voters);
		return oauth2AccessDecisionManager;
	}

	@Bean
	public ClientCredentialsTokenEndpointFilter clientCredentialsTokenEndpointFilter() {
		ClientCredentialsTokenEndpointFilter clientCredentialsTokenEndpointFilter = new ClientCredentialsTokenEndpointFilter();
		clientCredentialsTokenEndpointFilter.setAuthenticationManager(oauth2AuthenticationManager());
		return clientCredentialsTokenEndpointFilter;
	}

	@Bean
	public ApprovalStore approvalStore() throws Exception {
		TokenApprovalStore store = new TokenApprovalStore();
		store.setTokenStore(tokenStore());
		return store;
	}

	// public DaoAuthenticationProvider daoAuthenticationProvider(){
	// DaoAuthenticationProvider daoAuthenticationProvider = new
	// DaoAuthenticationProvider();
	// daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
	// daoAuthenticationProvider.setSaltSource(saltSource);
	// daoAuthenticationProvider.setUserDetailsService(userService);
	// return daoAuthenticationProvider;
	// }
	// @Bean
	// public PropertiesFactoryBean property(){
	// PropertiesFactoryBean property=new PropertiesFactoryBean();
	////// List<String> locations=new ArrayList<String>(0);
	//// List<Resource> resources=new ArrayList<Resource>(0);
	//// Resource resource=new Resource("classpath*:/*.properties");
	//// resources.add(resource);
	////// locations.add("classpath*:/*.properties");
	//// property.setLocations(resources.toArray(new
	// Resource[resources.size()]));
	// return property;
	// }
	// @Bean
	// public PreferencesPlaceholderConfigurer propertyConfigurer(){
	// PreferencesPlaceholderConfigurer propertyConfigurer=new
	// PreferencesPlaceholderConfigurer();
	//// propertyConfigurer.setProperties(property());
	// return propertyConfigurer;
	// }

	@Configuration
	@EnableResourceServer
	public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {
		@Autowired
		private DefaultTokenServices tokenServices = null;
		private final static String API_RESOURCE_ID = "api-resource";
		@Autowired
		public FilterSecurityInterceptor filterSecurityInterceptor = null;

		// @Override
		public void configure(ResourceServerSecurityConfigurer resources) {
			resources.resourceId(API_RESOURCE_ID);
			resources.tokenServices(tokenServices);
		}

		@Override
		public void configure(HttpSecurity http) throws Exception {
			// http.csrf().disable();
			// http.cors().disable();

			// http.authorizeRequests().regexMatchers("/*").authenticated().antMatchers("/index.html*",
			// "/security/user/login*",
			// "/shutdown*").permitAll().and().formLogin().loginPage("/security/user/login").permitAll();
			// http.authorizeRequests().regexMatchers("/shutdown").permitAll();
			// .and().authorizeRequests().antMatchers("/api").access("#oauth2.hasScope('read')");

			// http.authorizeRequests().antMatchers("/",
			// "/user/login").permitAll().anyRequest().authenticated().and()
			// .formLogin().loginPage("/user/login").permitAll()
			// .failureHandler(new
			// SimpleUrlAuthenticationFailureHandler("/user/login?error=failure"))
			// .successHandler(new
			// SimpleUrlAuthenticationSuccessHandler("/")).and().logout()
			// .logoutRequestMatcher(new AntPathRequestMatcher("/user/logout",
			// "GET"))
			// .logoutSuccessUrl("/?action=logout_success")
			// .logoutSuccessHandler(new
			// SimpleUrlLogoutSuccessHandler()).invalidateHttpSession(true)
			// .addLogoutHandler(new
			// SecurityContextLogoutHandler()).and().exceptionHandling()
			// .accessDeniedHandler(new
			// AccessDeniedHandlerImpl()).accessDeniedPage("/user/login?error=forbiden")
			// .and().csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()).and().cors().and()
			// .addFilterBefore(filterSecurityInterceptor,
			// FilterSecurityInterceptor.class);

			// ;
			// .antMatchers("/index.html*", "/security/user/login*",
			// "/shutdown*").permitAll()
			// .and().formLogin().loginPage("/security/user/login").permitAll()
			// .failureHandler(new
			// SimpleUrlAuthenticationFailureHandler("/security/user/login?error=failure"))
			// .successHandler(new
			// SimpleUrlAuthenticationSuccessHandler("/index.html"))
			// .and().logout()
			// .logoutRequestMatcher(new
			// AntPathRequestMatcher("/security/user/logout","GET")).logoutSuccessUrl("/index.html?action=logout_success")
			// .logoutSuccessHandler(new
			// SimpleUrlLogoutSuccessHandler()).invalidateHttpSession(true).addLogoutHandler(new
			// SecurityContextLogoutHandler())
			// .and().exceptionHandling().
			// accessDeniedHandler(new
			// AccessDeniedHandlerImpl()).accessDeniedPage("/security/user/login?error=forbiden")
			// .and().csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
			// .and().cors();

			// http.authorizeRequests().anyRequest().fullyAuthenticated();

			// http.authorizeRequests().anyRequest().authenticated();

			// authorizeRequests().regexMatchers("/shutdown*").permitAll().and()
			logger.debug(">>FaceYe --> run in oauth2 oauthorization server configuration.");
			http.authorizeRequests().antMatchers("/api").access("#oauth2.hasScope('read')");
			// http.requestMatchers().antMatchers("/oauth/token**").and().
			// CorsFilter f=null;
			// http.
			// addFilter(headerAdminFilter).
			// authorizeRequests().
			// regexMatchers("/login.*").permitAll().
			// regexMatchers("/api.*").fullyAuthenticated().
			// regexMatchers("/jolokia.*").hasRole(ADMINISTRATOR).
			// regexMatchers("/appadmin.*").hasRole(ADMINISTRATOR).
			// regexMatchers(".*").fullyAuthenticated().
			// and().
			// formLogin().loginPage("/login").successHandler(new
			// RedirectingAuthenticationSuccessHandler()).
			// and().
			// exceptionHandling().authenticationEntryPoint(new
			// RestAwareAuthenticationEntryPoint("/login"));

		}

	}

}
