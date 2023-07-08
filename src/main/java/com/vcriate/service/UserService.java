package com.vcriate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vcriate.entity.User;
import com.vcriate.model.LoginRequest;
import com.vcriate.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;
	
	public User getUserById(int userId) {
		return userRepository.findById(userId).orElse(null);
	}

	public User getUser(LoginRequest request) {
		User user = userRepository.findByUsernameAndPassword(request.getUsername(), request.getPassword());

		return user;
	}

	@Transactional
	public User loginUser(User user) {
		user.setLoggedIn(true);
		
		return userRepository.save(user);
	}

	public void logout(int userId) {
		User user = userRepository.findById(userId).orElse(null);
		
		if(user != null) {
			user.setLoggedIn(false);
			userRepository.save(user);
		}
	}
}
