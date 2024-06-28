package com.example.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.MyUser;
import com.example.demo.repo.MyUserRepository;
import com.example.demo.webtoken.JwtService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("")
//@CrossOrigin(origins = "*", allowedHeaders = "*", allowCredentials = "true")
@CrossOrigin
//@CrossOrigin(origins = "http://localhost:4200")
public class RegistrationController {

	@Autowired
	private MyUserRepository myUserRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private JwtService jwtService;

	// This method is used to register a new user
	@PostMapping("/register/user")
	public ResponseEntity<String> createUser(@RequestBody MyUser user) {

		// Check if the username or email already exists
		if (myUserRepository.findByUserName(user.getUserName()).isPresent()
				|| myUserRepository.findByEmailId(user.getEmailId()).isPresent()) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Username or Email already exists");
		}

		// save the user

		user.setFirstName(user.getFirstName());
		user.setLastName(user.getLastName());
		user.setEmailId(user.getEmailId());
		user.setUserName(user.getUserName());
		user.setPassword(passwordEncoder.encode(user.getPassword()));

		MyUser savedUser = myUserRepository.save(user);

		Map<String, Object> regUser = new HashMap<>();
		regUser.put("Firstname", user.getFirstName());
		regUser.put("Lasttname", user.getLastName());
//		regUser.put("Firstname", user.getFirstName());
//		regUser.put("Firstname", user.getFirstName());
		return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");

	}
	
	
	// This method is used to get the user details by id
	
	@GetMapping("/getUser/{id}")
	public ResponseEntity<MyUser> getUserById(@PathVariable Long id) {
	    Optional<MyUser> existUser = myUserRepository.findById(id);
	    if (existUser.isPresent()) {
	        return ResponseEntity.ok(existUser.get());
	    } else {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	    }
	}
	
	// This method is used to update the user details
	
	@PutMapping("/updateUser/{id}")
	public ResponseEntity<String> updateUser(@PathVariable Long id, @RequestBody MyUser updatedUser) {
	    Optional<MyUser> existUser = myUserRepository.findById(id);
	    if (existUser.isPresent()) {
	        MyUser user = existUser.get();
	        
	        user.setFirstName(updatedUser.getFirstName());
	        user.setLastName(updatedUser.getLastName());
	        user.setEmailId(updatedUser.getEmailId());
	        user.setUserName(updatedUser.getUserName());
	        user.setPassword(updatedUser.getPassword());
	        user.setRole(updatedUser.getRole());
	        user.setStatus(updatedUser.getStatus());
	        
	        myUserRepository.save(user);
	        return ResponseEntity.ok("User updated successfully");
	    } else {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
	    }
	}



	// This method is used to get all the users
	@GetMapping("/getUsers")
	public List<MyUser> getAllUsers() {
		
		
//		String username = jwtService.extractUsername(jwt);
		return myUserRepository.findAll();

	}
	
//	@GetMapping("/getUsers")
//	public ResponseEntity<List<MyUser>> getAllUsers(HttpServletRequest request) {
//	    Boolean isUserValid = (Boolean) request.getAttribute("isUserValid");
//
//	    if (isUserValid != null && isUserValid) {
//	        return ResponseEntity.ok(myUserRepository.findAll());
//	    } else {
//	        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
//	    }
//	}


}
