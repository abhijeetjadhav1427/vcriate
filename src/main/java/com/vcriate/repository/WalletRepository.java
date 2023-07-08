package com.vcriate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.vcriate.entity.Wallet;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Integer> {
	@Query("SELECT w from Wallet w where w.user.id = ?1")
	public Wallet getWalletByUserId(int userId);
}
