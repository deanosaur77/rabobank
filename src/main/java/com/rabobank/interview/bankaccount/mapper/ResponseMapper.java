package com.rabobank.interview.bankaccount.mapper;

import java.util.ArrayList;
import java.util.List;

import com.rabobank.interview.bankaccount.dto.AccountResponse;
import com.rabobank.interview.bankaccount.dto.BalancesResponse;
import com.rabobank.interview.bankaccount.dto.TransactionDetail;
import com.rabobank.interview.bankaccount.entity.Account;
import com.rabobank.interview.bankaccount.entity.Transaction;
import com.rabobank.interview.bankaccount.enums.Status;

//Better to use something like MapStruct for the mappings
public class ResponseMapper {
	
		public static AccountResponse createResponse(Account account, Transaction transaction) {
			AccountResponse response = setAccountDetails(account);
			
			List<TransactionDetail> transactions = new ArrayList<>();
			TransactionDetail transactionDetail = new TransactionDetail();
			transactionDetail.setAmount(transaction.getAmount());
			transactionDetail.setPreviousBalance(transaction.getPreviousBalance());
			transactionDetail.setTransactionType(transaction.getTransactionType().toString());
			transactionDetail.setTime(transaction.getDate().toString());
			transactions.add(transactionDetail);
			
			response.setTransactionDetail(transactions);
			return response;
		}
		
		public static AccountResponse createTransactionResponse(Account account) {
			AccountResponse response = setAccountDetails(account);
			
			List<TransactionDetail> transactions = new ArrayList<>();
			for(Transaction transaction: account.getTransactions()) {
				TransactionDetail transactionDetail = new TransactionDetail();
				transactionDetail.setAmount(transaction.getAmount());
				transactionDetail.setPreviousBalance(transaction.getPreviousBalance());
				transactionDetail.setTransactionType(transaction.getTransactionType().toString());
				transactionDetail.setTime(transaction.getDate().toString());
				transactions.add(transactionDetail);
			}
			
			response.setTransactionDetail(transactions);
			return response;
		}

		public static List<BalancesResponse> createBalancesResponse(List<Account> accounts) {
			List<BalancesResponse> balances = new ArrayList<>();
			for(Account account: accounts) {
				BalancesResponse balancesResponse  = new BalancesResponse();
				balancesResponse.setAccountNumber(account.getAccountNumber());
				balancesResponse.setAccountType(account.getAccountType().toString());
				balancesResponse.setName(account.getClient().getFirstName());
				balancesResponse.setSurname(account.getClient().getLastName());
				balancesResponse.setBalance(account.getBalance());
				balances.add(balancesResponse);
			}
			
			return balances;
		}
		
		private static AccountResponse setAccountDetails(Account account) {
			AccountResponse response = new AccountResponse();
			response.setName(account.getClient().getFirstName());
			response.setSurname(account.getClient().getLastName());
			response.setAccountNumber(account.getAccountNumber());
			response.setBalance(account.getBalance());
			response.setAccountType(account.getAccountType().toString());
			response.setStatus(Status.SUCCESS.toString());
			return response;
		}
}

