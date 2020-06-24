package com.sentenial.recruitment.domain;

import com.google.gson.annotations.Expose;

/**
 * Created by IntelliJ IDEA.
 * Project : springboot-bank-account
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 2019-04-17
 * Time: 07:04
 */
public class UserTransaction {
	@Expose
	private Long id;
	private String currency;
	@Expose
	private double amount;
	

	public UserTransaction() {
	}

	public UserTransaction(long id,double amount) {
		this.id=id;
		this.currency="eur";
		this.amount = amount;
	}
	public UserTransaction(long id,String currency,double amount) {
		this.id=id;
		this.currency=currency;
		this.amount = amount;
	}


	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}
}
