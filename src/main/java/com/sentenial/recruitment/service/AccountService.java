package com.sentenial.recruitment.service;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.sentenial.recruitment.domain.Account;

/**
 * Created by IntelliJ IDEA.
 * Project : springboot-bank-account
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 2019-04-17
 * Time: 07:05
 */
@Repository
public interface AccountService extends CrudRepository<Account, Long> {
}