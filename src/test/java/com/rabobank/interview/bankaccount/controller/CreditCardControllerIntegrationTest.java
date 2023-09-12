package com.rabobank.interview.bankaccount.controller;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.rabobank.interview.bankaccount.dto.AccountRequest;
import com.rabobank.interview.bankaccount.dto.AccountResponse;
import com.rabobank.interview.bankaccount.dto.TransactionDetail;
import com.rabobank.interview.bankaccount.enums.AccountType;
import com.rabobank.interview.bankaccount.enums.Status;
import com.rabobank.interview.bankaccount.enums.TransactionType;

@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
class CreditCardControllerIntegrationTest {

	private static final String NO_CARD = "NO CARD";

	private static final String CREDIT_CARD = "50111";

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void test_withdraw_success() throws Exception {
		AccountRequest request = new AccountRequest();
		request.setAccountNumber(CREDIT_CARD);
		request.setAmount(2500.00);

		AccountResponse response = restTemplate.postForObject("/api/credit-card/withdraw", request, AccountResponse.class);
		List<TransactionDetail> transactionDetails = response.getTransactionDetail();

		assertEquals(CREDIT_CARD, response.getAccountNumber());
		assertEquals(Status.SUCCESS.toString(), response.getStatus());
		assertEquals(AccountType.CREDIT_CARD.toString(), response.getAccountType());
		assertEquals(2475.00, response.getBalance(), 0);
		assertEquals(2500.00, transactionDetails.get(0).getAmount(), 0);
		assertEquals(TransactionType.DEBIT.toString(), transactionDetails.get(0).getTransactionType());
		assertEquals(5000, transactionDetails.get(0).getPreviousBalance(), 0);
	}

	@Test
	public void test_deposit_success() throws Exception {
		AccountRequest request = new AccountRequest();
		request.setAccountNumber(CREDIT_CARD);
		request.setAmount(5000.00);

		AccountResponse response = restTemplate.postForObject("/api/credit-card/deposit", request, AccountResponse.class);
		List<TransactionDetail> transactionDetails = response.getTransactionDetail();

		assertEquals(CREDIT_CARD, response.getAccountNumber());
		assertEquals(Status.SUCCESS.toString(), response.getStatus());
		assertEquals(AccountType.CREDIT_CARD.toString(), response.getAccountType());
		assertEquals(10000.00, response.getBalance(), 0);
		assertEquals(5000.00, transactionDetails.get(0).getAmount(), 0);
		assertEquals(TransactionType.CREDIT.toString(), transactionDetails.get(0).getTransactionType());
		assertEquals(5000, transactionDetails.get(0).getPreviousBalance(), 0);
	}
	
	@Test
	public void test_withdraw_returns_insufficient_balance() throws Exception {
		AccountRequest request = new AccountRequest();
		request.setAccountNumber(CREDIT_CARD);
		request.setAmount(10001.00);

		AccountResponse response = restTemplate.postForObject("/api/credit-card/withdraw", request, AccountResponse.class);
		List<TransactionDetail> transactionDetails = response.getTransactionDetail();

		assertEquals(CREDIT_CARD, response.getAccountNumber());
		assertEquals(Status.ERROR.toString(), response.getStatus());
		assertEquals("Credit Card has insufficient balance for withdrawel." , response.getMessage());
	}
	
	@Test
	public void test_withdraw_returns_account_not_found() throws Exception {
		AccountRequest request = new AccountRequest();
		request.setAccountNumber(NO_CARD);
		request.setAmount(1001.00);

		AccountResponse response = restTemplate.postForObject("/api/credit-card/withdraw", request, AccountResponse.class);

		assertEquals(NO_CARD, response.getAccountNumber());
		assertEquals(Status.ERROR.toString(), response.getStatus());
		assertEquals("Credit Card account not found for withdrawel." , response.getMessage());
	}
	
	@Test
	public void test_deposit_returns_account_not_found() throws Exception {
		AccountRequest request = new AccountRequest();
		request.setAccountNumber(NO_CARD);
		request.setAmount(1001.00);

		AccountResponse response = restTemplate.postForObject("/api/credit-card/deposit", request, AccountResponse.class);

		assertEquals(NO_CARD, response.getAccountNumber());
		assertEquals(Status.ERROR.toString(), response.getStatus());
		assertEquals("Credit Card account not found for deposit." , response.getMessage());
	}

}
