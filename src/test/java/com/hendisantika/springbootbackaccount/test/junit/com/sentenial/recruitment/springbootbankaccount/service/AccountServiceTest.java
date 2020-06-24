package com.sentenial.recruitment.springbootbankaccount.service;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import com.sentenial.recruitment.domain.Account;
import com.sentenial.recruitment.service.AccountService;

/**
 * Created by IntelliJ IDEA.
 * Project : springboot-bank-account
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 2019-04-17
 * Time: 17:44
 */
@RunWith(SpringRunner.class)
@DataJpaTest
class AccountServiceTests {

    @Autowired
    AccountService accountService;
    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testFindOne() {
        this.entityManager.persist(new Account(5000.0));
        this.accountService.findAll();
        Account account = this.accountService.findById(4L).get(); // ID 4 because default  3 accounts are created whenever the program runs
        // see Application.init
       //assertThat(account.getAmount()).isEqualTo(5000.0);
      

    }

}