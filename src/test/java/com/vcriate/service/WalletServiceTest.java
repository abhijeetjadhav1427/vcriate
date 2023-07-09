package com.vcriate.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.vcriate.entity.History;
import com.vcriate.entity.User;
import com.vcriate.entity.Wallet;
import com.vcriate.enums.WalletStatus;
import com.vcriate.repository.HistoryRepository;
import com.vcriate.repository.UserRepository;
import com.vcriate.repository.WalletRepository;

@SpringBootTest
public class WalletServiceTest {

	@InjectMocks
    private WalletService walletService;

    @Mock
    private WalletRepository walletRepository;
    
    @Mock
    private UserRepository userRepository;
    
    @Mock 
    private HistoryRepository historyRepository;

    @Test
    public void testCreditToWalletSuccess() {
        int userId = 1;
        int amount = 100;
        
        User user = new User();
        user.setId(userId);

        Wallet wallet = new Wallet();
        wallet.setUser(user);
        wallet.setAmount(500);

        when(walletRepository.getWalletByUserId(userId)).thenReturn(wallet);
        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
        when(historyRepository.save(any(History.class))).thenReturn(new History());

        WalletStatus status = walletService.creditToWallet(userId, amount);

        assertEquals(WalletStatus.CREDITED, status);
        assertEquals(600, wallet.getAmount());
        verify(walletRepository, times(1)).save(wallet);
        verify(historyRepository, times(1)).save(any(History.class));
    }

    @Test
    public void testCreditToWalletUserNotFound() {
        int userId = 1;
        int amount = 100;

        when(walletRepository.getWalletByUserId(userId)).thenReturn(null);

        WalletStatus status = walletService.creditToWallet(userId, amount);

        assertEquals(WalletStatus.USER_NOT_FOUND, status);
        verify(walletRepository, never()).save(any(Wallet.class));
        verify(historyRepository, never()).save(any(History.class));
    }

    @Test
    public void testDebitFromWalletSuccess() {
        int userId = 1;
        int amount = 100;
        
        User user = new User();
        user.setId(userId);
        
        Wallet wallet = new Wallet();
        wallet.setUser(user);
        wallet.setAmount(500);

        when(walletRepository.getWalletByUserId(userId)).thenReturn(wallet);
        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
        when(historyRepository.save(any(History.class))).thenReturn(new History());

        WalletStatus status = walletService.debitFromWallet(userId, amount);

        assertEquals(WalletStatus.DEBITED, status);
        assertEquals(400, wallet.getAmount());
        verify(walletRepository, times(1)).save(wallet);
        verify(historyRepository, times(1)).save(any(History.class));
    }

    @Test
    public void testDebitFromWalletUserNotFound() {
        int userId = 1;
        int amount = 100;

        when(walletRepository.getWalletByUserId(userId)).thenReturn(null);

        WalletStatus status = walletService.debitFromWallet(userId, amount);

        assertEquals(WalletStatus.USER_NOT_FOUND, status);
        verify(walletRepository, never()).save(any(Wallet.class));
        verify(historyRepository, never()).save(any(History.class));
    }

    @Test
    public void testDebitFromWalletInsufficientBalance() {
        int userId = 1;
        int amount = 600;
        
        User user = new User();
        user.setId(userId);
        Wallet wallet = new Wallet();
        wallet.setUser(user);
        wallet.setAmount(500);

        when(walletRepository.getWalletByUserId(userId)).thenReturn(wallet);

        WalletStatus status = walletService.debitFromWallet(userId, amount);

        assertEquals(WalletStatus.INSUFFICIENT_BALANCE, status);
        assertEquals(500, wallet.getAmount());
        verify(walletRepository, never()).save(any(Wallet.class));
        verify(historyRepository, never()).save(any(History.class));
    }
}
