package com.edmwat.springsecurity.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edmwat.springsecurity.domain.AccountUser;
import com.edmwat.springsecurity.repo.AccountUserRepo;

import lombok.extern.slf4j.Slf4j;

@Service 
@Slf4j 
public class AccountUserService {

	@Autowired 
	private AccountUserRepo accountUserRepo;
	
	public void saveAccount(AccountUser user) {
		accountUserRepo.save(user);
		log.info("saved user "+user.getEmail());
	}
	
	public boolean checkIfEmailExist(String email) {
		return accountUserRepo.findByEmail(email).isPresent();
	}
	
	public List<AccountUser> getAccounts(){
		return accountUserRepo.findAll();
	}

}
