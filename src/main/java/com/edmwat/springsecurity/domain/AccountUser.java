package com.edmwat.springsecurity.domain;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity 
@Data 
@NoArgsConstructor 
public class AccountUser{
	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	private Long id;
	private String fullName;
	private String email;
	private ArrayList<SimpleGrantedAuthority> authorities;
	
	public AccountUser(String fullname, String email, ArrayList<SimpleGrantedAuthority> authorities) {
		this.fullName = fullname;
		this.email=email;
		this.authorities = authorities;		
	}
}