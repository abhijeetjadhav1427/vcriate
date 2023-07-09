package com.vcriate.service;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vcriate.entity.History;
import com.vcriate.entity.User;
import com.vcriate.entity.Wallet;
import com.vcriate.enums.WalletStatus;
import com.vcriate.repository.HistoryRepository;
import com.vcriate.repository.UserRepository;
import com.vcriate.repository.WalletRepository;

import jakarta.transaction.Transactional;

@Service
public class WalletService {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private WalletRepository walletRepository;
	@Autowired
	private HistoryRepository historyRepository;

	private Wallet getWalletByUserId(int userId) {
		return walletRepository.getWalletByUserId(userId);
	}

	@Transactional
	public WalletStatus creditToWallet(int userId, int amount) {
		Wallet wallet = getWalletByUserId(userId);
		WalletStatus status = WalletStatus.CREDITED;

		if (wallet == null) {
			status = WalletStatus.USER_NOT_FOUND;
		} else {
			saveHistory(userId, "+" + Integer.toString(amount));
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
		} else if (wallet.getAmount() < amount) {
			status = WalletStatus.INSUFFICIENT_BALANCE;
		} else {
			saveHistory(userId, "-" + Integer.toString(amount));
			wallet.setAmount(wallet.getAmount() - amount);
			walletRepository.save(wallet);
		}

		return status;
	}

	@Transactional
	private WalletStatus saveHistory(int userId, String amount) {
		User user = userRepository.findById(userId).orElse(null);
		if (user == null) {
			return WalletStatus.USER_NOT_FOUND;
		}
		
		History history = new History();
		history.setUser(user);
		history.setAmount(amount);
		history.setTimestamp(new Timestamp(System.currentTimeMillis()));
		historyRepository.save(history);
		
		return WalletStatus.HISTORY_SAVED;
	}
}
