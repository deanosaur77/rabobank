package com.rabobank.interview.bankaccount.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.rabobank.interview.bankaccount.entity.Account;
import com.rabobank.interview.bankaccount.entity.Transaction;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Long> {

}