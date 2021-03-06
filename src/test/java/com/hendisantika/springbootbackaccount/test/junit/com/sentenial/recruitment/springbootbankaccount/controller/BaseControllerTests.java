package com.sentenial.recruitment.springbootbankaccount.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.sentenial.recruitment.service.AccountService;
import com.sentenial.recruitment.service.TransactionsService;

/**
 * Created by IntelliJ IDEA.
 * Project : springboot-bank-account
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 2019-04-17
 * Time: 17:48
 */
public class BaseControllerTests {

    @Autowired
    protected MockMvc mvc;

    @MockBean
    protected AccountService accountService;

    @MockBean
    protected TransactionsService transactionsService;

}