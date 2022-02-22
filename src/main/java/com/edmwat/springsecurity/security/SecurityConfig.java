package com.edmwat.springsecurity.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;

import com.edmwat.springsecurity.repo.AccountUserRepo;
import com.edmwat.springsecurity.service.AccountUserService;
import com.edmwat.springsecurity.service.CustomOAuth2UserService;
import com.edmwat.springsecurity.service.CustomOidcUserService;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@Configuration 
@EnableWebSecurity 
@AllArgsConstructor 
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	private final UserDetailsService userDetailService;
	private final BCryptPasswordEncoder bCryptPasswordEncoder; 
	private final CustomOidcUserService customOidcUserService;
	
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailService).passwordEncoder(bCryptPasswordEncoder);
	}
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.csrf().disable()
			.authorizeRequests().antMatchers("/token/refresh").permitAll()
			.and()
			.authorizeRequests().antMatchers(HttpMethod.GET, "/api/users/**").hasAnyAuthority("ROLE_MANAGER")
			.and()
			.authorizeRequests().antMatchers(HttpMethod.POST, "/api/users/save/**").hasAnyAuthority("ROLE_ADMIN")
			.and()
			.authorizeRequests().anyRequest().authenticated()
			.and()
	        .oauth2Login()
	        .authorizationEndpoint()
            .and()
            .userInfoEndpoint()
            	.oidcUserService(customOidcUserService)
            	.userService(new CustomOAuth2UserService());
	}
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception{
		return super.authenticationManagerBean();
	}
}


