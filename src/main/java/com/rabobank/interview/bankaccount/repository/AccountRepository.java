package com.rabobank.interview.bankaccount.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.rabobank.interview.bankaccount.entity.Account;

@Repository
public interface AccountRepository extends CrudRepository<Account, Long> {

	public Account findByAccountNumber(String accountNumber);

}