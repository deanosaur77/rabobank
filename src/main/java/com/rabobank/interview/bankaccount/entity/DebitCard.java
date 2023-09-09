package com.rabobank.interview.bankaccount.entity;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.rabobank.interview.bankaccount.enums.AccountType;
import com.rabobank.interview.bankaccount.enums.TransactionType;

@Entity
@DiscriminatorValue(value = "DEBIT_CARD")
public class DebitCard extends Account {

	public DebitCard(String accountNumber, Double balance, Client client, AccountType accountType) {
		super(accountNumber, balance, client, accountType);
	}

	@Override
	public Transaction withdraw(double amount) {
		double newBalance = getBalance() - amount;
		
		Transaction transaction = new Transaction(TransactionType.DEBIT, amount, Timestamp.valueOf(LocalDateTime.now()), getBalance(), newBalance);
		transaction.setAccount(this);		
		getTransactions().add(transaction);		
		setBalance(newBalance);

		return transaction;
	}
}
