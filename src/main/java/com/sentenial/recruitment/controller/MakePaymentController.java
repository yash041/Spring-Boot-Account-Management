package com.sentenial.recruitment.controller;

import java.util.Date;

import org.apache.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.sentenial.recruitment.domain.Account;
import com.sentenial.recruitment.domain.AccountTransaction;
import com.sentenial.recruitment.domain.TransactionType;
import com.sentenial.recruitment.domain.UserTransaction;
import com.sentenial.recruitment.util.StandardJsonResponse;
import com.sentenial.recruitment.util.StandardJsonResponseImpl;

@RestController
@RequestMapping(value = "/{id}/makePayment")
public class MakePaymentController extends BaseController {
	@PostMapping("/")
	public @ResponseBody StandardJsonResponse makePayment(@PathVariable long id,
			@RequestBody UserTransaction userTransaction) {

		StandardJsonResponse jsonResponse = new StandardJsonResponseImpl();
		AccountTransaction accountTransaction;
		Account accountTo = accountService.findById(userTransaction.getId()).get();
		Account accountFrom= accountService.findById(id).get();

		try {
			// check balance
			double balance = accountFrom.getAmount();
			double amount=userTransaction.getAmount();
			if (balance > amount) {
				
				//minus the amount
				accountFrom.setAmount(balance-amount);
				accountService.save(accountFrom);
				
				//add the amount 
				accountTo.setAmount(accountTo.getAmount()+amount);
				accountService.save(accountTo);
								
				
				//save the sent transaction
				accountTransaction = new AccountTransaction(id,
						TransactionType.SENT.getId(), userTransaction.getAmount(), new Date());
				transactionsService.save(accountTransaction);
				
				//save the received transaction
				accountTransaction = new AccountTransaction(userTransaction.getId(),
						TransactionType.RECEIVED.getId(), userTransaction.getAmount(), new Date());
				transactionsService.save(accountTransaction);
				

				jsonResponse.setSuccess(true, "", "Payment transfered Successfully");
				jsonResponse.setHttpResponseCode(HttpStatus.SC_OK);
			} else {
				jsonResponse.setSuccess(false, "Error", "Insufficient funds");
				jsonResponse.setHttpResponseCode(HttpStatus.SC_NOT_ACCEPTABLE);
			}
		} catch (Exception e) {
			logger.error("exception", e);
			jsonResponse.setSuccess(false, StandardJsonResponse.DEFAULT_MSG_TITLE_VALUE,
					StandardJsonResponse.DEFAULT_MSG_NAME_VALUE);
			jsonResponse.setHttpResponseCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			return jsonResponse;
		}
		return jsonResponse;

	}

}
