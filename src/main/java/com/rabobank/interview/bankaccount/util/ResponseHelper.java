package com.rabobank.interview.bankaccount.util;

import org.springframework.http.ResponseEntity;

import com.rabobank.interview.bankaccount.dto.AccountResponse;
import com.rabobank.interview.bankaccount.enums.Status;

public class ResponseHelper {
	
	public static ResponseEntity<AccountResponse> createErrorResponse(String account, String message) {
		AccountResponse response = new AccountResponse();
		response.setAccountNumber(account);
		response.setStatus(Status.ERROR.toString());
		response.setMessage(message);
		return ResponseEntity.badRequest().body(response);
	}
}
