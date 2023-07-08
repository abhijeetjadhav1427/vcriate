package com.vcriate.controller;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.vcriate.entity.User;
import com.vcriate.enums.WalletStatus;
import com.vcriate.model.WalletRequest;
import com.vcriate.service.UserService;
import com.vcriate.service.WalletService;

@WebMvcTest(WalletController.class)
public class WalletControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WalletService walletService;

    @MockBean
    private UserService userService;

    @Test
    public void testCreditAmount() throws Exception {
        int userId = 1;
        int amount = 10;

        WalletRequest walletRequest = new WalletRequest();
        walletRequest.setAmount(amount);

        WalletStatus status = WalletStatus.CREDITED;
        User user = new User();
        user.setId(userId);

        when(walletService.creditToWallet(eq(userId), eq(amount))).thenReturn(status);
        when(userService.getUserById(eq(userId))).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.post("/credit/{userId}", userId)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("amount", String.valueOf(amount))
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("user"))
                .andExpect(MockMvcResultMatchers.view().name("account"))
                .andExpect(MockMvcResultMatchers.model().attribute("success", "Credited " + amount + " to wallet!!.."));
    }

    @Test
    public void testDebitAmount() throws Exception {
        int userId = 1;
        int amount = 5;

        WalletRequest walletRequest = new WalletRequest();
        walletRequest.setAmount(amount);

        WalletStatus status = WalletStatus.DEBITED;
        User user = new User();
        user.setId(userId);

        when(walletService.debitFromWallet(eq(userId), eq(amount))).thenReturn(status);
        when(userService.getUserById(eq(userId))).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.post("/debit/{userId}", userId)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("amount", String.valueOf(amount))
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("user"))
                .andExpect(MockMvcResultMatchers.view().name("account"))
                .andExpect(MockMvcResultMatchers.model().attribute("success", "Debited " + amount + " to wallet!!.."));
    }
}