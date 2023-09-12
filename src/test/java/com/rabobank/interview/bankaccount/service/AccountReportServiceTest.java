package com.rabobank.interview.bankaccount.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.rabobank.interview.bankaccount.dto.BalancesResponse;
import com.rabobank.interview.bankaccount.entity.Account;
import com.rabobank.interview.bankaccount.entity.Client;
import com.rabobank.interview.bankaccount.enums.AccountType;
import com.rabobank.interview.bankaccount.exception.AccountNotFoundException;
import com.rabobank.interview.bankaccount.exception.InsufficentBalanceException;
import com.rabobank.interview.bankaccount.repository.AccountRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
class AccountReportServiceTest {

	private static final String CREDIT_CARD_ACCOUNT_NUMBER = "50111";

	private static final String DEBIT_CARD_ACCOUNT_NUMBER = "60111";

	@Autowired
	private AccountReportService accountReportService;

	@MockBean
	private AccountRepository accountRepository;

	@Test
	public void test_balances() throws InsufficentBalanceException, AccountNotFoundException {

		when(accountRepository.findAll()).thenReturn(getAccounts());
		List<BalancesResponse> response = accountReportService.getAllBalances();
		assertEquals(2, response.size());

		assertEquals("333", response.get(0).getAccountNumber());
		assertEquals(1000.00, response.get(0).getBalance(), 0);

		assertEquals("222", response.get(1).getAccountNumber());
		assertEquals(2000.00, response.get(1).getBalance(), 0);
	}

	private List<Account> getAccounts() {
		List<Account> accounts = new ArrayList<>();
		accounts.add(new Account(CREDIT_CARD_ACCOUNT_NUMBER, 1000.00, new Client("Chuck", "Norris", "333"), AccountType.CREDIT_CARD));
		accounts.add(new Account(DEBIT_CARD_ACCOUNT_NUMBER, 2000.00, new Client("Debbie", "Spender", "222"), AccountType.DEBIT_CARD));
		return accounts;
	}

}
