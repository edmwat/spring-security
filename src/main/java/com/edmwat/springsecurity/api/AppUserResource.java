package com.edmwat.springsecurity.api;

import java.io.IOException;
import java.net.URI;
import java.security.Principal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.edmwat.springsecurity.domain.AccountUser;
import com.edmwat.springsecurity.domain.AppUser;
import com.edmwat.springsecurity.domain.Role;
import com.edmwat.springsecurity.service.AccountUserService;
import com.edmwat.springsecurity.service.AppUserService;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RestController 
@RequestMapping("/api") 
@RequiredArgsConstructor
public class AppUserResource {
	private final AppUserService appUserService;
	private final AccountUserService accUserService;
	
	@GetMapping("/user")
	public ResponseEntity<List<AccountUser>> getUsers(){
		return ResponseEntity.ok().body(accUserService.getAccounts());		
	}
	@GetMapping("/users")
	public ResponseEntity<List<AppUser>> getUser(){
		return ResponseEntity.ok().body(appUserService.getUsers());
	}
	@PostMapping("/user/save")
	public ResponseEntity<AppUser> saveUser(@RequestBody AppUser user){ 
		URI uri = URI.create(ServletUriComponentsBuilder
				.fromCurrentContextPath()
				.path("/api/user/save").toUriString());
		return ResponseEntity.created(uri).body(appUserService.saveUser(user));
	} 
	@PostMapping("/role/save")
	public ResponseEntity<Role> saveUser(@RequestBody Role role){ 
		URI uri = URI.create(ServletUriComponentsBuilder
				.fromCurrentContextPath()
				.path("/api/role/save").toUriString());
		return ResponseEntity.created(uri).body(appUserService.saveRole(role));
	} 
	@PostMapping("/role/addToUser")
	public ResponseEntity<?> addRoleToUser(@RequestBody RoleToUserForm form){ 
		appUserService.addRoleToUser(form.getUsername(), form.getRoleName());
		return ResponseEntity.ok().build();
	} 	
	@GetMapping("/token/refresh")
	public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws JsonGenerationException, JsonMappingException, IOException{ 
		String authizationHeader = request.getHeader("Authorization");
		if(authizationHeader != null && authizationHeader.startsWith("Bearer ")) {		
			try {
				String token = authizationHeader.substring("Bearer ".length());
				Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());				
				JWTVerifier verifier = JWT.require(algorithm).build();
				DecodedJWT decodedJwt = verifier.verify(token);
				String username = decodedJwt.getSubject();
				AppUser user = appUserService.getUser(username);
				String refresh_token = JWT.create()
						.withSubject(user.getUsername())
						.withExpiresAt(new Date(System.currentTimeMillis() * 10 *60 * 1000))
						.withIssuer(request.getRequestURL().toString())
						.withClaim("roles", user.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
						.sign(algorithm);
				
				Map<String,String> tokens = new HashMap<>();
				tokens.put("refresh_token", refresh_token);
				response.setContentType(MediaType.APPLICATION_JSON_VALUE); 		
				new ObjectMapper().writeValue(response.getOutputStream(),tokens);
				
			}catch(Exception ex) {
				response.setStatus(403);
				response.setHeader("error", ex.getMessage());
				Map<String,String> error = new HashMap<>();
				error.put("Error Creating Refresh Token ", ex.getMessage());
				response.setContentType(MediaType.APPLICATION_JSON_VALUE); 					
				new ObjectMapper().writeValue(response.getOutputStream(),error);
				
			}
		}else {
			throw new RuntimeException("Refresh token is missing");
		}
	} 	
}
@Data
class RoleToUserForm{
	private String username;
	private String roleName;
}
