package com.sentenial.recruitment.controller;

import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.sentenial.recruitment.domain.AccountTransaction;
import com.sentenial.recruitment.util.StandardJsonResponse;
import com.sentenial.recruitment.util.StandardJsonResponseImpl;

@RestController
@RequestMapping(value = "/transaction")
public class TransactionController extends BaseController {
	 @RequestMapping(value = "/{id}", method = RequestMethod.GET)
	    public @ResponseBody
	    StandardJsonResponse getTransactions(@PathVariable("id") long id) {
		 
		 StandardJsonResponse jsonResponse = new StandardJsonResponseImpl();
	        HashMap<String, Object> responseData = new HashMap<>();
	        
	        try {
	            List<AccountTransaction> accountTransaction = transactionsService.findByAccountId(id);
	            

	            if (accountTransaction.size() > 0 ) {
	            	int i=0;
	            	for(AccountTransaction a:accountTransaction) {
	            		String j=Integer.toString(i+1);
	            		responseData.put(j, a);
	            		i++;
	            	
	            	}
	            	
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
