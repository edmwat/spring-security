package com.edmwat.springsecurity.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.edmwat.springsecurity.domain.AppUser;

public interface AppUserRepo extends JpaRepository<AppUser,Long> {
	AppUser findByUsername(String username);
}
