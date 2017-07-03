package com.faceye.component.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.dao.ReflectionSaltSource;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.faceye.component.security.web.service.ResourceService;
import com.faceye.component.security.web.service.UserService;

@Configuration
@EnableWebSecurity
// @Order(4)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
	private Logger logger = LoggerFactory.getLogger(WebSecurityConfiguration.class);
	@Autowired
	@Qualifier("webUserService")
	private UserService userService = null;
	@Autowired
	@Qualifier("webResourceService")
	private ResourceService resourceService = null;

	@Autowired
	private Md5PasswordEncoder passwordEncoder = null;
	@Autowired
	private ReflectionSaltSource saltSource = null;

	// @Autowired
	// private RoleVoter roleVoter = null;
	// @Autowired
	// private AuthenticatedVoter authenticatedVoter = null;
	@Autowired
	private DaoAuthenticationProvider daoAuthenticationProvider = null;
	@Autowired
	private FilterSecurityInterceptor filterSecurityInterceptor = null;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		super.configure(http);
		// .cors().disable().csrf().disable()
		// http.httpBasic().and().addFilterBefore(filterSecurityInterceptor(),
		// FilterSecurityInterceptor.class).authorizeRequests()
		// .antMatchers("/index.html", "/security/user/login",
		// "/").permitAll().anyRequest().authenticated().and().csrf()
		// .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()).and().cors();

		// .loginPage("/security/user/login")
		http.authorizeRequests().antMatchers("/", "/user/login", "/static/**").permitAll().and().formLogin()
				.loginPage("/user/login").permitAll().successHandler(new SimpleUrlAuthenticationSuccessHandler("/"))
				.failureHandler(new SimpleUrlAuthenticationFailureHandler("/user/login?error=failure")).and()
				.exceptionHandling().accessDeniedHandler(new AccessDeniedHandlerImpl())
				.accessDeniedPage("/user/login?error=forbiden").and().logout()
				.logoutRequestMatcher(new AntPathRequestMatcher("/user/logout", "GET")).permitAll()
				.logoutSuccessUrl("/user/login?action=logout_success")
				.logoutSuccessHandler(new SimpleUrlLogoutSuccessHandler()).invalidateHttpSession(true)
				.addLogoutHandler(new SecurityContextLogoutHandler()).and().exceptionHandling()
				.accessDeniedHandler(new AccessDeniedHandlerImpl())
				.accessDeniedPage("/user/login?error=forbiden")
				.and().csrf().disable()
//				.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()).and().cors().and()
				.addFilterBefore(filterSecurityInterceptor, FilterSecurityInterceptor.class);

		// and().authorizeRequests().anyRequest().authenticated()
		// http.authorizeRequests().antMatchers("/index.html**",
		// "/security/user/login**", "/shutdown**").permitAll()
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
		// .and().csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()).and().cors().and().addFilterBefore(filterSecurityInterceptor,
		// FilterSecurityInterceptor.class)
		// ;
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/favor.ico", "**/**/*.js","**/**/*.css","**/**/*.png","**/**/*.jpg", "/shutdown", "/user/login");
		super.configure(web);
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userService).passwordEncoder(passwordEncoder).and()
				.authenticationProvider(daoAuthenticationProvider);
		// authenticationManager=auth.build();
	}

	// @Bean
	// public AuthenticationManager authenticationManager(){
	// List<AuthenticationProvider> providers=new
	// ArrayList<AuthenticationProvider>(0);
	// providers.add(daoAuthenticationProvider);
	// authenticationManager=new ProviderManager(providers);
	// return authenticationManager;
	// }
	// @Bean
	// public DaoAuthenticationProvider daoAuthenticationProvider(){
	// daoAuthenticationProvider = new DaoAuthenticationProvider();
	// daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
	// daoAuthenticationProvider.setSaltSource(saltSource);
	// daoAuthenticationProvider.setUserDetailsService(userService);
	// return daoAuthenticationProvider;
	// }
}
