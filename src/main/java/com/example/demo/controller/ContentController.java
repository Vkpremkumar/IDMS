package com.example.demo.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.LoginForm;
import com.example.demo.entity.MyUser;
import com.example.demo.repo.MyUserRepository;
import com.example.demo.service.MyUserDetailService;
import com.example.demo.webtoken.JwtService;

@RestController
@RequestMapping("")
@CrossOrigin
//@CrossOrigin(origins = "http://localhost:4200")
public class ContentController {

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private MyUserDetailService myUserDetailService;
	
	@Autowired
	private MyUserRepository myUserRepository;
	
	@GetMapping("/home")
	public String handleWelcome() {
		return "Welcome to home";
	}

	@GetMapping("/admin/home")
	public String handleAdminHome() {
		return "Welcome to home_admin";
	}

	@GetMapping("/user/home")
	public String handleUserHome() {
		return "Welcome to home_user";
	}
	
	
	// This method is used to authenticate the user (login)
	@PostMapping("/authenticate")
	public ResponseEntity<Map<String, String>> authenticateAndGetToken(@RequestBody LoginForm loginForm) {
		try {

			// find the user by username
			MyUser existUser = myUserRepository.findByUserName(loginForm.userName())
					.orElseThrow(() -> new UsernameNotFoundException("Invalid credentials"));

			// check if the user is active
			if (existUser.getStatus() == MyUser.UserStatus.INACTIVE) {
				throw new IllegalStateException("User is inActive");
			}

			Authentication authentication = authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(loginForm.userName(), loginForm.password()));
			if (authentication.isAuthenticated()) {
				String token = jwtService.generateToken(myUserDetailService.loadUserByUsername(loginForm.userName()));

				Map<String, String> response = new HashMap<>();
				response.put("Token",token);
				response.put("username", loginForm.userName());
//				response.put("password", loginForm.password());

				MyUser existsUser = myUserRepository.findByUserName(loginForm.userName()).get();
				if (existsUser != null) {
					response.put("Firstname", existsUser.getFirstName());
					response.put("Lastname", existsUser.getLastName());
					response.put("email", existsUser.getEmailId());
					response.put("Role", existsUser.getRole());
				}
				return ResponseEntity.ok(response);
			} else {
				throw new UsernameNotFoundException("Invalid credentials");
			}
		} catch (BadCredentialsException e) {
			Map<String, String> errorResponse = new HashMap<>();
			errorResponse.put("error", "Invalid username or password");
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(errorResponse);
		} catch (IllegalStateException e) {
			Map<String, String> errorResponse = new HashMap<>();
			errorResponse.put("error", e.getMessage());
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
		}
	}
}
