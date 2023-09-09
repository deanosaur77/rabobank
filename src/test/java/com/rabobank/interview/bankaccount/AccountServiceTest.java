package com.rabobank.interview.bankaccount;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.rabobank.interview.bankaccount.dto.Response;
import com.rabobank.interview.bankaccount.entity.Account;
import com.rabobank.interview.bankaccount.entity.Client;
import com.rabobank.interview.bankaccount.entity.DebitCard;
import com.rabobank.interview.bankaccount.enums.AccountType;
import com.rabobank.interview.bankaccount.enums.TransactionType;
import com.rabobank.interview.bankaccount.repository.AccountRepository;
import com.rabobank.interview.bankaccount.service.AccountService;

@SpringBootTest
class AccountServiceTest {

	private static final String DEBIT_CARD_ACCOUNT_NUMBER = "50112233";

	private static final String ID_NUMBER = "1122334455";

	@Autowired
	private AccountService accountService;

	@MockBean
	private AccountRepository accountRepository;

	@Test
	public void test_withdrawel_debit_card_success() throws Exception {
		double withdrawel_amount = 100.00;
		
		when(accountRepository.findByAccountNumber(DEBIT_CARD_ACCOUNT_NUMBER)).thenReturn(getValidAccount());
		Response response = accountService.withdraw(DEBIT_CARD_ACCOUNT_NUMBER, withdrawel_amount);
		
		assertEquals(900.00, response.getBalance().doubleValue(), 0);
		
		assertEquals(TransactionType.DEBIT.toString(), response.getTransactionDetails().getTransactionType());
		
		assertEquals(withdrawel_amount, response.getTransactionDetails().getAmount(),0);
		
		assertEquals(1000.00, response.getTransactionDetails().getPreviousBalance(),0);
	}

	private DebitCard getValidAccount() {
		return new DebitCard(DEBIT_CARD_ACCOUNT_NUMBER, 1000.00, getClient(), AccountType.DEBIT_CARD);
	}

	private Client getClient() {
		return new Client("Debbie", "Spender", ID_NUMBER);
	}

	@Test
	public void test_withdrawel_debit_card_insufficient_funds() {
		Response response = accountService.withdraw(DEBIT_CARD_ACCOUNT_NUMBER, 250.00);
		assertEquals("FAILED", response.getStatus());
		assertEquals("INSUFFICIENT FUNDS", response.getMessage());
	}

	@Test
	public void test_deposit_debit_card_success() {

	}

	@Test
	public void test_deposit_debit_card_failure() {

	}

	@Test
	public void test_withdrawel_credit_card_insufficient_funds() {

	}

	@Test
	public void test_withdrawel_credit_card_success() {

	}

	@Test
	public void test_deposit_credit_card_success() {

	}

	@Test
	public void test_deposit_credit_card_failure() {

	}
}
