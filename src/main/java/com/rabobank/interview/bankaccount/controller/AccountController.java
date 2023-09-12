package com.rabobank.interview.bankaccount.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.rabobank.interview.bankaccount.dto.AccountResponse;
import com.rabobank.interview.bankaccount.dto.BalancesResponse;
import com.rabobank.interview.bankaccount.exception.AccountNotFoundException;
import com.rabobank.interview.bankaccount.service.AccountReportService;
import com.rabobank.interview.bankaccount.util.ResponseHelper;

@RestController
@RequestMapping("/api/account")
public class AccountController {

    Logger logger = LoggerFactory.getLogger(DebitCardController.class);

	@Autowired
	private AccountReportService accountReportService;

	@GetMapping("/balances")
	public @ResponseBody ResponseEntity<List<BalancesResponse>> getBalance() {
			logger.info("Getting balances");
			List<BalancesResponse> response = accountReportService.getAllBalances();
			return ResponseEntity.ok(response);
	}

	
	//ADDED this so that you can view the transaction history :-)
	@GetMapping("/transactions")
	public ResponseEntity<AccountResponse> getTransactions(@RequestParam String accountNumber) {
		try {
			AccountResponse response = accountReportService.getTransactions(accountNumber);
			return ResponseEntity.ok(response);
		} catch (AccountNotFoundException e) {
			logger.error("Error getting transactions.", e);
			return ResponseHelper.createErrorResponse(accountNumber, "Account not found.");
		}
	}

}
