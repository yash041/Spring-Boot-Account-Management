package com.sentenial.recruitment.springbootbankaccount.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sentenial.recruitment.controller.WithdrawalController;
import com.sentenial.recruitment.domain.Account;
import com.sentenial.recruitment.domain.AccountTransaction;
import com.sentenial.recruitment.domain.TransactionType;
import com.sentenial.recruitment.domain.UserTransaction;
import com.sentenial.recruitment.util.AccountUtils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by IntelliJ IDEA.
 * Project : springboot-bank-account
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 2019-04-17
 * Time: 17:54
 */
@RunWith(SpringRunner.class)
@WebMvcTest(WithdrawalController.class)
public class WithdrawalControllerTest extends BaseControllerTests {
    @Test
    public void testWithdrawalExceedsCurrentBalance() throws Exception {

        UserTransaction userTransaction = new UserTransaction(1,"gbp",6000);
        Gson gson = new Gson();
        String json = gson.toJson(userTransaction);

        given(this.accountService.findById(1L)).willReturn(Optional.of(new Account(5000)));

        this.mvc.perform(post("/withdrawal/").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk()).andExpect(content().json("{\"success\":false,\"messages\":{\"message\":\"You have insufficient funds\",\"title\":\"Error\"},\"errors\":{},\"data\":{},\"httpResponseCode\":406}"));

    }

    @Test
    public void testMaxWithdrawalForTheDay() throws Exception {

        AccountTransaction transaction = new AccountTransaction(1L,TransactionType.WITHDRAWAL.getId(), 4000, new Date());
        AccountTransaction transaction2 = new AccountTransaction(1L, TransactionType.WITHDRAWAL.getId(), 5000, new Date());

        List<AccountTransaction> list = new ArrayList<>();
        list.add(transaction);
        list.add(transaction2);

        UserTransaction userTransaction = new UserTransaction(1,8000);
        Gson gson = new GsonBuilder()
        		  .excludeFieldsWithoutExposeAnnotation()
        		  .create();
        String json = gson.toJson(userTransaction);
        

        given(this.accountService.findById(1L)).willReturn(Optional.of(new Account(20000)));

        given(this.transactionsService.findByDateBetweenAndType(AccountUtils.getStartOfDay(new Date()),
                AccountUtils.getEndOfDay(new Date()), TransactionType.WITHDRAWAL.getId())).willReturn(list);

        this.mvc.perform(post("/withdrawal/").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk()).andExpect(content().json("{\"success\":false,\"messages\":{\"message\":\"Withdrawal per day should not be more than €10k\",\"title\":\"Error\"},\"errors\":{},\"data\":{},\"httpResponseCode\":406}"));

    }

    @Test
    public void testMaxWithdrawalPerTransaction() throws Exception {

        AccountTransaction transaction = new AccountTransaction(1L,TransactionType.WITHDRAWAL.getId(), 500, new Date());
        AccountTransaction transaction2 = new AccountTransaction(1L,TransactionType.WITHDRAWAL.getId(), 750, new Date());

        List<AccountTransaction> list = new ArrayList<>();
        list.add(transaction);
        list.add(transaction2);

        UserTransaction userTransaction = new UserTransaction(1, 2500);
        Gson gson = new Gson();
        String json = gson.toJson(userTransaction);

        given(this.accountService.findById(1L)).willReturn(Optional.of(new Account(5000)));

        given(this.transactionsService.findByDateBetweenAndType(AccountUtils.getStartOfDay(new Date()),
                AccountUtils.getEndOfDay(new Date()), TransactionType.WITHDRAWAL.getId())).willReturn(list);

        this.mvc.perform(post("/withdrawal/").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk()).andExpect(content().json("{\"success\":false,\"messages\":{\"message\":\"Exceeded Maximum Withdrawal Per Transaction\",\"title\":\"Error\"},\"errors\":{},\"data\":{},\"httpResponseCode\":406}"));

    }

   
    @Test
    public void testSuccessfulWithdrawal() throws Exception {

        AccountTransaction transaction = new AccountTransaction(1L,TransactionType.WITHDRAWAL.getId(), 1500, new Date());
        AccountTransaction transaction2 = new AccountTransaction(1L,TransactionType.WITHDRAWAL.getId(), 1750, new Date());

        List<AccountTransaction> list = new ArrayList<>();
        list.add(transaction);
        list.add(transaction2);

        UserTransaction userTransaction = new UserTransaction(1L, 1000);
        Gson gson = new Gson();
        String json = gson.toJson(userTransaction);

        given(this.accountService.findById(1L)).willReturn(Optional.of(new Account(5000)));

        given(this.transactionsService.findByDateBetweenAndType(AccountUtils.getStartOfDay(new Date()),
                AccountUtils.getEndOfDay(new Date()), TransactionType.WITHDRAWAL.getId())).willReturn(list);

        when(this.transactionsService.save(any(AccountTransaction.class))).thenReturn(transaction);
        when(this.accountService.save(any(Account.class))).thenReturn(new Account(5000));

        this.mvc.perform(post("/withdrawal/").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk()).andExpect(content().json("{\"success\":true,\"messages\":{\"message\":\"Withdrawal sucessfully Transacted\",\"title\":\"\"},\"errors\":{},\"data\":{},\"httpResponseCode\":200}"));

    }

}