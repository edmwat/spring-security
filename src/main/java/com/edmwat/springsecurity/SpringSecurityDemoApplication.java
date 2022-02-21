package com.edmwat.springsecurity;

import java.util.ArrayList;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.edmwat.springsecurity.domain.AppUser;
import com.edmwat.springsecurity.domain.Role;
import com.edmwat.springsecurity.service.AppUserService;
  
@SpringBootApplication     
public class SpringSecurityDemoApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(SpringSecurityDemoApplication.class, args);
	}
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	CommandLineRunner run(AppUserService service) {
		return args ->{
			service.saveRole(new Role(null,"ROLE_USER"));
			service.saveRole(new Role(null,"ROLE_MANAGER"));
			service.saveRole(new Role(null,"ROLE_ADMIN"));
			service.saveRole(new Role(null,"ROLE_SUPER_ADMIN"));
			
			service.saveUser(new AppUser(null,"Jamie Foxx","foxx","1234",new ArrayList<>()));
			service.saveUser(new AppUser(null,"Travolter","tray","1234",new ArrayList<>()));
			service.saveUser(new AppUser(null,"Edard Stock","stock","1234",new ArrayList<>()));
			service.saveUser(new AppUser(null,"Lord bailies","bailies","1234",new ArrayList<>()));
			
			service.addRoleToUser("foxx", "ROLE_USER");
			service.addRoleToUser("tray", "ROLE_MANAGER");
			service.addRoleToUser("stock", "ROLE_ADMIN");
			service.addRoleToUser("bailies", "ROLE_SUPER_ADMIN");
			service.addRoleToUser("bailies", "ROLE_MANAGER");
			service.addRoleToUser("bailies", "ROLE_ADMIN");
		};
	}
}
