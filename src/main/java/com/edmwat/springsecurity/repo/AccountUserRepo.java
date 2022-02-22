package com.edmwat.springsecurity.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.edmwat.springsecurity.domain.AccountUser;

public interface AccountUserRepo extends JpaRepository<AccountUser, Long>{
	Optional<AccountUser> findByEmail(String email);
}
