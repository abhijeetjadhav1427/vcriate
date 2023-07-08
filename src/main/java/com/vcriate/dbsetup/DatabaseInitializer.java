package com.vcriate.dbsetup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.vcriate.entity.User;
import com.vcriate.entity.Wallet;
import com.vcriate.repository.UserRepository;
import com.vcriate.repository.WalletRepository;

@Component
public class DatabaseInitializer implements ApplicationRunner {
	
	private UserRepository userRepository;
	private WalletRepository walletRepository;
	
	@Autowired
	public DatabaseInitializer(UserRepository userRepository, WalletRepository walletRepository) {
		this.userRepository = userRepository;
		this.walletRepository = walletRepository;
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		Wallet wallet = new Wallet();
		wallet.setAmount(1000);

		User user = new User();
		user.setUsername("abhijeet");
		user.setPassword("1234");
		wallet.setUser(user);
		user.setWallet(wallet);
		
		userRepository.save(user);
		walletRepository.save(wallet);
	}

}
