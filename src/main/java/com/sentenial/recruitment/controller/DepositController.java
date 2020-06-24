package com.sentenial.recruitment.controller;

import com.sentenial.recruitment.domain.Account;
import com.sentenial.recruitment.domain.AccountTransaction;
import com.sentenial.recruitment.domain.TransactionType;
import com.sentenial.recruitment.domain.UserTransaction;
import com.sentenial.recruitment.util.AccountUtils;
import com.sentenial.recruitment.util.StandardJsonResponse;
import com.sentenial.recruitment.util.StandardJsonResponseImpl;

import org.apache.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Created by IntelliJ IDEA.
 * Project : springboot-bank-account
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 2019-04-17
 * Time: 07:11
 */
@RestController
@RequestMapping("/deposit")
public class DepositController extends BaseController {

    private static final double MAX_DEPOSIT_PER_TRANSACTION = 2000; // €2k
    private static final double MAX_DEPOSIT_PER_DAY = 10000; // €10k
    

    @PostMapping("/")
    public @ResponseBody
    StandardJsonResponse makeDeposit(@RequestBody UserTransaction userTransaction) {

        StandardJsonResponse jsonResponse = new StandardJsonResponseImpl();

        try {

            double total = 0;

            // check maximum limit deposit for the day has been reached
            List<AccountTransaction> deposits = transactionsService.findByDateBetweenAndType(AccountUtils.getStartOfDay(new Date()),
                    AccountUtils.getEndOfDay(new Date()), TransactionType.DEPOSIT.getId());

            if (deposits.size() > 0) {
                for (AccountTransaction accountTransaction : deposits) {
                    total += accountTransaction.getAmount();
                }
                if (total + userTransaction.getAmount() > MAX_DEPOSIT_PER_DAY) {
                    jsonResponse.setSuccess(false, "Error", "Deposit for the day should not be more than €10k");
                    jsonResponse.setHttpResponseCode(HttpStatus.SC_NOT_ACCEPTABLE);
                    return jsonResponse;
                }
            }

            // Check whether the amount being deposited exceeds the MAX_DEPOSIT_PER_TRANSACTION
            if (userTransaction.getAmount() > MAX_DEPOSIT_PER_TRANSACTION) {
                jsonResponse.setSuccess(false, "Error", "Deposit per transaction should not be more than €2k");
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
