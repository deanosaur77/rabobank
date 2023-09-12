package com.rabobank.interview.bankaccount.controller;

import static org.junit.Assert.assertEquals;

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

import com.rabobank.interview.bankaccount.dto.BalancesResponse;
import com.rabobank.interview.bankaccount.enums.AccountType;

@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
class AccountControllerIntegrationTest {

	private static final String CREDIT_CARD_NUMBER = "50111";
	private static final String DEBIT_CARD_NUMBER = "60111";

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void test_account_balances() throws Exception {

		 BalancesResponse[] balances = restTemplate.getForObject("/api/account/balances",
				BalancesResponse[].class);

		assertEquals(2, balances.length);
		
		assertEquals(CREDIT_CARD_NUMBER, balances[0].getAccountNumber());
		assertEquals(AccountType.CREDIT_CARD.toString(), balances[0].getAccountType());
		assertEquals(5000, balances[0].getBalance(),0);

		assertEquals(DEBIT_CARD_NUMBER, balances[1].getAccountNumber());
		assertEquals(AccountType.DEBIT_CARD.toString(), balances[1].getAccountType());
		assertEquals(10000, balances[1].getBalance(),0);
	}

}
