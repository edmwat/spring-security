package com.edmwat.springsecurity.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.edmwat.springsecurity.domain.Role;

public interface RoleRepo extends JpaRepository<Role,Long> {
	Role findByname(String name);
}
