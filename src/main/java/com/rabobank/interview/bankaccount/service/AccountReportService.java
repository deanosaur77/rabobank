package com.rabobank.interview.bankaccount.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rabobank.interview.bankaccount.dto.AccountResponse;
import com.rabobank.interview.bankaccount.dto.BalancesResponse;
import com.rabobank.interview.bankaccount.entity.Account;
import com.rabobank.interview.bankaccount.enums.AccountType;
import com.rabobank.interview.bankaccount.exception.AccountNotFoundException;
import com.rabobank.interview.bankaccount.mapper.ResponseMapper;
import com.rabobank.interview.bankaccount.repository.AccountRepository;

@Service
public class AccountReportService {

	@Autowired
	private AccountRepository accountRepo;

	public List<BalancesResponse> getAllBalances() {
		Iterable<Account> account = accountRepo.findAll();
		return ResponseMapper.createBalancesResponse((List<Account>) account);
	}

	//Convenience method to view transactions
	public AccountResponse getTransactions(String accountNumber) throws AccountNotFoundException{
		Account account = accountRepo.findByAccountNumber(accountNumber)
				.orElseThrow(AccountNotFoundException::new);
		return ResponseMapper.createTransactionResponse(account);
	}
}
