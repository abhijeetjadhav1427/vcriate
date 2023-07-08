package com.vcriate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.vcriate.entity.User;
import com.vcriate.model.LoginRequest;
import com.vcriate.service.UserService;

@Controller
public class AuthController {
	@Autowired
	private UserService userService;
	
	@GetMapping("/")
	public String getLogin() {
		return "index";
	}
	
	@PostMapping("/login")
	public ModelAndView login(@ModelAttribute LoginRequest loginRequest) {
		User user = userService.getUser(loginRequest);
		ModelAndView response = new ModelAndView("index");
		
		if(user != null) {
			if(user.isLoggedIn()) {
				response.addObject("error", "User already logged in!!..");
			}
			else {
				userService.loginUser(user);
				response.setViewName("account");
				response.addObject("user", user);
			}
		}
		else {
			response.addObject("error", "User doesn't exists with given credentials!!..");
		}
		
		return response;
	}
	
	@GetMapping("/logout/{userId}")
	public String logout(@PathVariable int userId) {
		userService.logout(userId);
		
		return "index";
	}
}
