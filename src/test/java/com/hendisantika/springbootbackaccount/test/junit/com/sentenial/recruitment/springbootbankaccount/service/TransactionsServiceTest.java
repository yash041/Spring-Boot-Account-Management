package com.sentenial.recruitment.springbootbankaccount.service;

import com.sentenial.recruitment.domain.AccountTransaction;
import com.sentenial.recruitment.domain.TransactionType;
import com.sentenial.recruitment.service.TransactionsService;
import com.sentenial.recruitment.util.AccountUtils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Project : springboot-bank-account
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 2019-04-17
 * Time: 17:45
 */
@RunWith(SpringRunner.class)
@DataJpaTest
class TransactionsServiceTests {

    @Autowired
    TransactionsService transactionsService;
    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testFindByDateBetweenAndType() {
        this.entityManager.persist(new AccountTransaction(1L,TransactionType.WITHDRAWAL.getId(), 1000, new Date()));
        List<AccountTransaction> transactions = transactionsService.findByDateBetweenAndType(AccountUtils.getStartOfDay(new Date()), AccountUtils.getEndOfDay(new Date()), TransactionType.WITHDRAWAL.getId());
//        assertThat(transactions.get(0)).isNotNull();
//        assertThat(transactions.get(0).getType()).isEqualTo(TransactionType.WITHDRAWAL.getId());
//        assertThat(transactions.get(0).getAmount()).isEqualTo(1000);
//        assertThat(transactions.get(0).getDate()).isBetween(AccountUtils.getStartOfDay(new Date()), AccountUtils.getEndOfDay(new Date()));
    }

}