package com.rabobank.interview.bankaccount.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;

import com.rabobank.interview.bankaccount.dto.AccountResponse;
import com.rabobank.interview.bankaccount.entity.Account;
import com.rabobank.interview.bankaccount.entity.Transaction;
import com.rabobank.interview.bankaccount.enums.AccountType;
import com.rabobank.interview.bankaccount.enums.TransactionType;
import com.rabobank.interview.bankaccount.exception.AccountNotFoundException;
import com.rabobank.interview.bankaccount.mapper.ResponseMapper;
import com.rabobank.interview.bankaccount.repository.AccountRepository;

public abstract class CardService implements AccountService {

	@Autowired
	AccountRepository accountRepo;

	public CardService() {
		super();
	}
	
	public abstract AccountResponse deposit(String accountNumber, double amount) throws AccountNotFoundException;
	
	@Override
	public AccountResponse deposit(String accountNumber, AccountType accountType, double amount) throws AccountNotFoundException {
		Account account = accountRepo.findByAccountNumberAndAccountType(accountNumber, accountType).orElseThrow(AccountNotFoundException::new);
	
		//Could use a calculator per accountType
		double newBalance = account.getBalance() + amount;

		Transaction transaction = new Transaction(TransactionType.CREDIT, amount,
				Timestamp.valueOf(LocalDateTime.now()), account.getBalance(), newBalance);
		transaction.setAccount(account);
		account.getTransactions().add(transaction);
		account.setBalance(newBalance);
		accountRepo.save(account);
		
		return ResponseMapper.createResponse(account, transaction);
	}


}