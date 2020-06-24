package com.sentenial.recruitment.controller;

import com.sentenial.recruitment.domain.Account;
import com.sentenial.recruitment.util.StandardJsonResponse;
import com.sentenial.recruitment.util.StandardJsonResponseImpl;

import org.apache.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
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
@RequestMapping(value = "/balance")
public class BalanceController extends BaseController {

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public @ResponseBody
    StandardJsonResponse getBalance(@PathVariable("id") long id) {

        StandardJsonResponse jsonResponse = new StandardJsonResponseImpl();
        HashMap<String, Object> responseData = new HashMap<>();

        try {
            Optional<Account> account = Optional.of(accountService.findById(id).get());

            if (account.isPresent()) {
            	responseData.put("Account Id", id);
                responseData.put("balance", "€" + account.get().getAmount());

                jsonResponse.setSuccess(true);
                jsonResponse.setData(responseData);
                jsonResponse.setHttpResponseCode(HttpStatus.SC_OK);
            } else {
                jsonResponse.setSuccess(false, "Resource not found", StandardJsonResponse.RESOURCE_NOT_FOUND_MSG);
                jsonResponse.setHttpResponseCode(HttpStatus.SC_NO_CONTENT);
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
