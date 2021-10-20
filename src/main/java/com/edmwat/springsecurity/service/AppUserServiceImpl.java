package com.edmwat.springsecurity.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.edmwat.springsecurity.domain.AppUser;
import com.edmwat.springsecurity.domain.Role;
import com.edmwat.springsecurity.repo.AppUserRepo;
import com.edmwat.springsecurity.repo.RoleRepo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service @RequiredArgsConstructor 
@Transactional @Slf4j  
public class AppUserServiceImpl implements AppUserService,UserDetailsService {
	private final AppUserRepo appUserRepo;
	private final RoleRepo roleRepo;
	private final PasswordEncoder passwordEncoder;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		AppUser user = appUserRepo.findByUsername(username);
		if(user == null) {
			log.error("username not found in the database!");
			throw new UsernameNotFoundException("username not found in the database!");
		}else {
			log.info("username found in the database!");
		}
		Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
		user.getRoles().forEach(role ->{
			authorities.add(new SimpleGrantedAuthority(role.getName()));
		});
		return new User(user.getUsername(),user.getPassword(),authorities);
	}

	@Override
	public AppUser saveUser(AppUser user) {	
		log.info("Saving User to the database");
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return appUserRepo.save(user);
	}

	@Override
	public Role saveRole(Role role) {	
		log.info("Saving role {} to the database ", role.getName());
		return roleRepo.save(role);
	}

	@Override
	public void addRoleToUser(String username, String roleName) {
		log.info("Adding roles {} to the user {} ",username,roleName);
		AppUser user = appUserRepo.findByUsername(username);
		Role role = roleRepo.findByname(roleName); 
		user.getRoles().add(role);	
	}
 
	@Override
	public AppUser getUser(String username) {
		log.info("Get a single user from the database");
		return appUserRepo.findByUsername(username);
	}

	@Override
	public List<AppUser> getUsers() {
		log.info("Fetching all users");
		return appUserRepo.findAll();
	}

	
}
