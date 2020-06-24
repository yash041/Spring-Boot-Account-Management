package com.sentenial.recruitment;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.sentenial.recruitment.domain.Account;
import com.sentenial.recruitment.service.AccountService;

@SpringBootApplication
public class SpringbootBankAccountApplication {
	
	
    public static void main(String[] args) {
        SpringApplication.run(SpringbootBankAccountApplication.class, args);
    }

    @Bean
    public CommandLineRunner init(AccountService accountService) {
        return (args) -> {
            // create account
            accountService.save(new Account(5000));
            accountService.save(new Account(5000));
            accountService.save(new Account(5000));
        };
    }

}
