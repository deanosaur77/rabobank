package com.rabobank.interview.bankaccount.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.rabobank.interview.bankaccount.entity.Account;
import com.rabobank.interview.bankaccount.enums.AccountType;

@Repository
public interface AccountRepository extends CrudRepository<Account, Long> {

	public Optional<Account> findByAccountNumberAndAccountType(String accountNumber, AccountType accountType);

	public Optional<Account> findByAccountNumber(String accountNumber);

}