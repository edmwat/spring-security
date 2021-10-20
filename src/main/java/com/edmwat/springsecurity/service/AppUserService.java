package com.edmwat.springsecurity.service;

import java.util.List;

import com.edmwat.springsecurity.domain.AppUser;
import com.edmwat.springsecurity.domain.Role;

public interface AppUserService {
	AppUser saveUser(AppUser user);
	Role saveRole(Role role);
	void addRoleToUser(String username, String roleName);
	AppUser getUser(String username);
	List<AppUser> getUsers();
}
