package com.rabobank.interview.bankaccount.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rabobank.interview.bankaccount.dto.Response;
import com.rabobank.interview.bankaccount.dto.TransactionDetails;
import com.rabobank.interview.bankaccount.entity.Account;
import com.rabobank.interview.bankaccount.entity.CreditCard;
import com.rabobank.interview.bankaccount.entity.DebitCard;
import com.rabobank.interview.bankaccount.entity.Transaction;
import com.rabobank.interview.bankaccount.repository.AccountRepository;

@Service
public class AccountService {

	@Autowired
	private AccountRepository accountRepo;

	public Response withdraw(String accountNumber, double amount) {
		Account account = accountRepo.findByAccountNumber(accountNumber);
		Transaction transaction =  account.withdraw(amount);
		accountRepo.save(account);
		Response response = createResponse(account, transaction);
		return response;
	}

	//Better to use something like MapStruct for the mappings
	private Response createResponse(Account account, Transaction transaction) {
		Response response = new Response();
		response.setAccountNumber(account.getAccountNumber());
		response.setBalance(account.getBalance());
		response.setAccountType(account.getAccountType().toString());
		response.setStatus("SUCCESS");
		
		TransactionDetails transactionDetails = new TransactionDetails();
		transactionDetails.setAmount(transaction.getAmount());
		transactionDetails.setPreviousBalance(transaction.getPreviousBalance());
		transactionDetails.setTransactionType(transaction.getTransactionType().toString());
		response.setTransactionDetails(transactionDetails);
		
		return response;
	}

	public Response deposit(String accountNumber, double amount) {
		return new Response();
	}

}
