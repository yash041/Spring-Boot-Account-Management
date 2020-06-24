package com.sentenial.recruitment.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.sentenial.recruitment.service.AccountService;
import com.sentenial.recruitment.service.TransactionsService;

/**
 * Created by IntelliJ IDEA.
 * Project : springboot-bank-account
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 2019-04-17
 * Time: 07:10
 */
public class BaseController {
    protected static final long ACCOUNT_ID = 1L;
    protected final Log logger = LogFactory.getLog(getClass());
    @Autowired
    AccountService accountService;

    @Autowired
    TransactionsService transactionsService;
}
