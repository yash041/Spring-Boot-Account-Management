package com.sentenial.recruitment.controller;

import com.sentenial.recruitment.domain.Account;
import com.sentenial.recruitment.domain.AccountTransaction;
import com.sentenial.recruitment.domain.TransactionType;
import com.sentenial.recruitment.domain.UserTransaction;
import com.sentenial.recruitment.util.AccountUtils;
import com.sentenial.recruitment.util.StandardJsonResponse;
import com.sentenial.recruitment.util.StandardJsonResponseImpl;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Project : springboot-bank-account
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 2019-04-17
 * Time: 07:12
 */
@RestController
@RequestMapping(value = "/withdrawal")
public class WithdrawalController extends BaseController {

    private static final double MAX_WITHDRAWAL_PER_TRANSACTION = 2000; // €2k
    private static final double MAX_WITHDRAWAL_PER_DAY = 10000; // €10k
    
    
    @Value("${gbp.to.eur}")
    private double gbpToEuro;;

    @PostMapping("/")
    public @ResponseBody
    StandardJsonResponse makeWithDrawal(@RequestBody UserTransaction userTransaction) {

        StandardJsonResponse jsonResponse = new StandardJsonResponseImpl();

        try {

            double total = 0;

            // check balance
            double balance = accountService.findById(userTransaction.getId()).get().getAmount();
            
            //check for the currency
            if("gbp".equals(userTransaction.getCurrency())) {
            	userTransaction.setAmount(userTransaction.getAmount()*gbpToEuro);
            }
            
           
           
            if (userTransaction.getAmount() > balance) {
                jsonResponse.setSuccess(false, "Error", "You have insufficient funds");
                jsonResponse.setHttpResponseCode(HttpStatus.SC_NOT_ACCEPTABLE);
                return jsonResponse;
            }


            // check maximum limit withdrawal for the day has been reached
            List<AccountTransaction> withdrawals = transactionsService.findByDateBetweenAndType(AccountUtils.getStartOfDay(new Date()),
                    AccountUtils.getEndOfDay(new Date()), TransactionType.WITHDRAWAL.getId());

            if (withdrawals.size() > 0) {
                for (AccountTransaction accountTransaction : withdrawals) {
                    total += accountTransaction.getAmount();
                }
                if (total + userTransaction.getAmount() > MAX_WITHDRAWAL_PER_DAY) {
                    jsonResponse.setSuccess(false, "Error", "Withdrawal per day should not be more than €10k");
                    jsonResponse.setHttpResponseCode(HttpStatus.SC_NOT_ACCEPTABLE);
                    return jsonResponse;
                }
            }

            // Check whether the amount being withdrawn exceeds the MAX_WITHDRAWAL_PER_TRANSACTION
            if (userTransaction.getAmount() > MAX_WITHDRAWAL_PER_TRANSACTION) {
                jsonResponse.setSuccess(false, "Error", "Exceeded Maximum Withdrawal Per Transaction");
                jsonResponse.setHttpResponseCode(HttpStatus.SC_NOT_ACCEPTABLE);
                return jsonResponse;
            }

           

        } catch (Exception e) {
            logger.error("exception", e);
            jsonResponse.setSuccess(false, StandardJsonResponse.DEFAULT_MSG_TITLE_VALUE, StandardJsonResponse.DEFAULT_MSG_NAME_VALUE);
            jsonResponse.setHttpResponseCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            return jsonResponse;
        }

        return jsonResponse;
    }

}
