package com.vcriate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vcriate.entity.Wallet;
import com.vcriate.enums.WalletStatus;
import com.vcriate.repository.WalletRepository;

import jakarta.transaction.Transactional;

@Service
public class WalletService {
	@Autowired
	private WalletRepository walletRepository;

	private Wallet getWalletByUserId(int userId) {
		return walletRepository.getWalletByUserId(userId);
	}
	
	@Transactional
	public WalletStatus creditToWallet(int userId, int amount) {		
		Wallet wallet = getWalletByUserId(userId);
		WalletStatus status = WalletStatus.CREDITED;
		
		if (wallet == null) {
			status = WalletStatus.USER_NOT_FOUND;
		}
		else {
			wallet.setAmount(wallet.getAmount() + amount);
			walletRepository.save(wallet);	
		}
		
		return status;
	}
	
	@Transactional
	public WalletStatus debitFromWallet(int userId, int amount) {
		Wallet wallet = walletRepository.getWalletByUserId(userId);
		WalletStatus status = WalletStatus.DEBITED;
		
		if (wallet == null) {
			status = WalletStatus.USER_NOT_FOUND;
		}
		else if(wallet.getAmount() < amount) {
			status = WalletStatus.INSUFFICIENT_BALANCE;
		}
		else {
			wallet.setAmount(wallet.getAmount()-amount);
			walletRepository.save(wallet);
		}
		
		return status;
	}
}
