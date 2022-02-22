package com.edmwat.springsecurity.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import com.edmwat.springsecurity.domain.AccountUser;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service 
@Slf4j 
@AllArgsConstructor
public class CustomOidcUserService extends OidcUserService{

	private AccountUserService accountUserService;

	@Override
	public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
		
		OidcUser oidcUser = super.loadUser(userRequest);
		
		boolean emailExist = accountUserService.checkIfEmailExist(oidcUser.getEmail());
		if(!emailExist) {
			log.info("save user if not exist!!! "+oidcUser.getEmail());
			ArrayList<SimpleGrantedAuthority> authorities = new ArrayList<>();
			authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
			authorities.add(new SimpleGrantedAuthority("ROLE_MANAGER"));
			AccountUser user = new AccountUser(oidcUser.getFullName(),oidcUser.getEmail(),authorities);			
			accountUserService.saveAccount(user);
		}
		
		return oidcUser;
	}

	
}
