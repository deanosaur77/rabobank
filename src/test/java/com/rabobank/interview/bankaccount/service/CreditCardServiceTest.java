package com.rabobank.interview.bankaccount.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.rabobank.interview.bankaccount.dto.AccountResponse;
import com.rabobank.interview.bankaccount.entity.Account;
import com.rabobank.interview.bankaccount.entity.Client;
import com.rabobank.interview.bankaccount.enums.AccountType;
import com.rabobank.interview.bankaccount.enums.TransactionType;
import com.rabobank.interview.bankaccount.exception.AccountNotFoundException;
import com.rabobank.interview.bankaccount.exception.InsufficentBalanceException;
import com.rabobank.interview.bankaccount.repository.AccountRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
class CreditCardServiceTest {

	private static final int INTEREST_RATE = 1;

	private static final double CREDIT_CARD_INITIAL = 500.00;

	private static final String CREDIT_CARD_ACCOUNT_NUMBER = "60112233";

	private static final String ID_NUMBER = "1122334455";

	@Autowired
	private CreditCardService creditCardService;

	@MockBean
	private AccountRepository accountRepository;

	@Test
	public void test_withdrawel_credit_card_insufficient_funds() throws InsufficentBalanceException, AccountNotFoundException {
		when(accountRepository.findByAccountNumberAndAccountType(CREDIT_CARD_ACCOUNT_NUMBER, AccountType.CREDIT_CARD)).thenReturn(getValidCreditCardAccount());
		assertThrows(InsufficentBalanceException.class, () -> creditCardService.withdraw(CREDIT_CARD_ACCOUNT_NUMBER, 1250.00));
	}

	@Test
	public void test_withdrawel_credit_card_success() throws InsufficentBalanceException, AccountNotFoundException {
		double withdrawel_amount = 100.00;

		when(accountRepository.findByAccountNumberAndAccountType(CREDIT_CARD_ACCOUNT_NUMBER, AccountType.CREDIT_CARD))
				.thenReturn(getValidCreditCardAccount());
		AccountResponse response = creditCardService.withdraw(CREDIT_CARD_ACCOUNT_NUMBER, withdrawel_amount);

		assertEquals(CREDIT_CARD_INITIAL - (withdrawel_amount + INTEREST_RATE * withdrawel_amount / 100),
				response.getBalance().doubleValue(), 0);

		assertEquals(TransactionType.DEBIT.toString(), response.getTransactionDetail().get(0).getTransactionType());

		assertEquals(withdrawel_amount, response.getTransactionDetail().get(0).getAmount(), 0);

	}

	@Test
	public void test_deposit_credit_card_success() throws AccountNotFoundException {
		double deposit = 100.00;

		when(accountRepository.findByAccountNumberAndAccountType(CREDIT_CARD_ACCOUNT_NUMBER, AccountType.CREDIT_CARD))
				.thenReturn(getValidCreditCardAccount());
		AccountResponse response = creditCardService.deposit(CREDIT_CARD_ACCOUNT_NUMBER, deposit);

		assertEquals(CREDIT_CARD_INITIAL + deposit, response.getBalance().doubleValue(), 0);

		assertEquals(TransactionType.CREDIT.toString(), response.getTransactionDetail().get(0).getTransactionType());

		assertEquals(deposit, response.getTransactionDetail().get(0).getAmount(), 0);
	}

	@Test
	public void test_deposit_credit_card_not_found() throws InsufficentBalanceException, AccountNotFoundException {
		when(accountRepository.findByAccountNumberAndAccountType(CREDIT_CARD_ACCOUNT_NUMBER, AccountType.CREDIT_CARD)).thenReturn(Optional.empty());
		assertThrows(AccountNotFoundException.class, () -> creditCardService.withdraw(CREDIT_CARD_ACCOUNT_NUMBER, 1250.00));
	}

	private Optional<Account> getValidCreditCardAccount() {
		return Optional
				.of(new Account(CREDIT_CARD_ACCOUNT_NUMBER, CREDIT_CARD_INITIAL, getClient(), AccountType.CREDIT_CARD));
	}

	private Client getClient() {
		return new Client("Debbie", "Spender", ID_NUMBER);
	}
}
