package com.vcriate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.vcriate.entity.User;
import com.vcriate.enums.WalletStatus;
import com.vcriate.model.WalletRequest;
import com.vcriate.service.UserService;
import com.vcriate.service.WalletService;

@Controller
public class WalletController {
	@Autowired
	private WalletService walletService;
	@Autowired
	private UserService userService;
	
	@PostMapping("/credit/{userId}")
	public ModelAndView creditAmount(@PathVariable int userId, @ModelAttribute WalletRequest walletRequest) {
		WalletStatus status = walletService.creditToWallet(userId, walletRequest.getAmount());
		ModelAndView response = new ModelAndView("index");

		if(status == WalletStatus.USER_NOT_FOUND) {
			return response;
		}
		
		User user = userService.getUserById(userId);
		response.addObject("user", user);
		response.setViewName("account");
		response.addObject("success", "Credited " + walletRequest.getAmount() + " to wallet!!..");
		
		return response;
	}
	
	@PostMapping("/debit/{userId}")
	public ModelAndView debitAmount(@PathVariable int userId, @ModelAttribute WalletRequest walletRequest) {
		WalletStatus status = walletService.debitFromWallet(userId, walletRequest.getAmount());
		ModelAndView response = new ModelAndView("index");
		
		if(status == WalletStatus.USER_NOT_FOUND) {
			return response;
		}
		
		User user = userService.getUserById(userId);
		response.addObject("user", user);
		response.setViewName("account");
		
		if(status == WalletStatus.INSUFFICIENT_BALANCE) {
			response.addObject("error", "Insufficient Balance!!..");
		}
		else if(status == WalletStatus.DEBITED) {
			response.addObject("success", "Debited " + walletRequest.getAmount() + " to wallet!!..");
		}
		
		return response;
	}
}
