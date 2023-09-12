package com.rabobank.interview.bankaccount.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rabobank.interview.bankaccount.dto.AccountResponse;
import com.rabobank.interview.bankaccount.entity.Account;
import com.rabobank.interview.bankaccount.entity.Transaction;
import com.rabobank.interview.bankaccount.enums.AccountType;
import com.rabobank.interview.bankaccount.enums.TransactionType;
import com.rabobank.interview.bankaccount.exception.AccountNotFoundException;
import com.rabobank.interview.bankaccount.exception.InsufficentBalanceException;
import com.rabobank.interview.bankaccount.mapper.ResponseMapper;
import com.rabobank.interview.bankaccount.repository.AccountRepository;

@Service
public class DebitCardService extends CardService {

	@Autowired
	private AccountRepository accountRepo;

	@Override
	public AccountResponse withdraw(String accountNumber, double amount) throws InsufficentBalanceException, AccountNotFoundException {
		Account account = accountRepo.findByAccountNumberAndAccountType(accountNumber, AccountType.DEBIT_CARD).orElseThrow(AccountNotFoundException::new);

		//A good idea is to introduce calculators per accountType
		double newBalance = account.getBalance() - amount;

		if (newBalance < 0)
			throw new InsufficentBalanceException();

		Transaction transaction = new Transaction(TransactionType.DEBIT, amount, Timestamp.valueOf(LocalDateTime.now()),
				account.getBalance(), newBalance);
		transaction.setAccount(account);
		account.getTransactions().add(transaction);
		account.setBalance(newBalance);
		accountRepo.save(account);
		
		AccountResponse response = ResponseMapper.createResponse(account, transaction);
		return response;
	}

	@Override
	public AccountResponse deposit(String accountNumber, double amount) throws AccountNotFoundException {
		return super.deposit(accountNumber, AccountType.DEBIT_CARD, amount);
	}

}
