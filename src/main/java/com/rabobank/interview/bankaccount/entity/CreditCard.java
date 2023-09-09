package com.rabobank.interview.bankaccount.entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.rabobank.interview.bankaccount.enums.AccountType;

@Entity
@DiscriminatorValue(value="CREDIT_CARD")
public class CreditCard extends Account {

	public CreditCard(String accountNumber, Double balance, Client client, AccountType accountType) {
		super(accountNumber, balance, client, accountType);
	}

	@Override
	public Transaction withdraw(double amount) {
		return new Transaction(null, getBalance(), null, getBalance(), getBalance());
	}
	

}
