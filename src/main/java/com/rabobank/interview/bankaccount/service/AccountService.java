package com.rabobank.interview.bankaccount.service;

import org.springframework.stereotype.Service;

import com.rabobank.interview.bankaccount.dto.AccountResponse;
import com.rabobank.interview.bankaccount.enums.AccountType;
import com.rabobank.interview.bankaccount.exception.AccountNotFoundException;
import com.rabobank.interview.bankaccount.exception.InsufficentBalanceException;

@Service
public interface AccountService {

	public AccountResponse deposit(String accountNumber, AccountType accountType, double amount) throws AccountNotFoundException;

	public AccountResponse withdraw(String accountNumber, double amount)
			throws InsufficentBalanceException, AccountNotFoundException;

}