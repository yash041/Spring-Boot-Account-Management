package com.sentenial.recruitment.springbootbankaccount.controller;

import com.google.gson.Gson;
import com.sentenial.recruitment.controller.DepositController;
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

import static org.mockito.BDDMockito.given;
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
 * Time: 17:53
 */
@RunWith(SpringRunner.class)
@WebMvcTest(DepositController.class)
public class DepositControllerTest extends BaseControllerTests {
    @Test
    public void testMaxDepositForTheDay() throws Exception {
        AccountTransaction transaction = new AccountTransaction(1L,TransactionType.DEPOSIT.getId(), 4000, new Date());
        AccountTransaction transaction2 = new AccountTransaction(1L,TransactionType.DEPOSIT.getId(), 5000, new Date());

        List<AccountTransaction> list = new ArrayList<>();
        list.add(transaction);
        list.add(transaction2);

        UserTransaction userTransaction = new UserTransaction(1,8000); // 3rd deposit $15K
        Gson gson = new Gson();
        String json = gson.toJson(userTransaction);

        given(this.transactionsService.findByDateBetweenAndType(AccountUtils.getStartOfDay(new Date()),
                AccountUtils.getEndOfDay(new Date()), TransactionType.DEPOSIT.getId())).willReturn(list);
        this.mvc.perform(post("/deposit/").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk()).andExpect(content().json("{\"success\":false,\"messages\":{\"message\":\"Deposit for the day should not be more than €10k\",\"title\":\"Error\"},\"errors\":{},\"data\":{},\"httpResponseCode\":406}"));
    }

}