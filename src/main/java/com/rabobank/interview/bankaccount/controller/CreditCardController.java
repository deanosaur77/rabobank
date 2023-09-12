package com.rabobank.interview.bankaccount.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rabobank.interview.bankaccount.dto.AccountRequest;
import com.rabobank.interview.bankaccount.dto.AccountResponse;
import com.rabobank.interview.bankaccount.exception.AccountNotFoundException;
import com.rabobank.interview.bankaccount.exception.InsufficentBalanceException;
import com.rabobank.interview.bankaccount.service.CardService;
import com.rabobank.interview.bankaccount.util.ResponseHelper;

@RestController
@RequestMapping("/api/credit-card")
public class CreditCardController {

    Logger logger = LoggerFactory.getLogger(CreditCardController.class);

	@Autowired
	private CardService creditCardService;

	@PostMapping("/withdraw")
	public ResponseEntity<AccountResponse> withdraw(@RequestBody AccountRequest request) {
		try {
			//We could further enhance logging by adding MDC
			logger.info("Withdrawing from Credit Card: " + request.toString());
			AccountResponse response = creditCardService.withdraw(request.getAccountNumber(), request.getAmount());
			logger.info("Completed Withdrawing from Credit Card: " + response.toString());
			return ResponseEntity.ok(response);
		} catch (InsufficentBalanceException e) {
			return ResponseHelper.createErrorResponse(request.getAccountNumber(), "Credit Card has insufficient balance for withdrawel.");
		} catch (AccountNotFoundException e) {
			return ResponseHelper.createErrorResponse(request.getAccountNumber(), "Credit Card account not found for withdrawel.");
		}
	}

	@PostMapping("/deposit")
	public ResponseEntity<AccountResponse> deposit(@RequestBody AccountRequest request) {
		try {
			logger.info("Depositing to Credit Card: " + request.toString());
			AccountResponse response = creditCardService.deposit(request.getAccountNumber(), request.getAmount());
			logger.info("Completed depositing to Credit Card: " + response.toString());
			return ResponseEntity.ok(response);
		} catch (AccountNotFoundException e) {
			return ResponseHelper.createErrorResponse(request.getAccountNumber(), "Credit Card account not found for deposit.");
		}
	}

}
