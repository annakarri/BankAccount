package com.app.controller;

import com.app.model.Account;
import com.app.model.Transaction;
import com.app.model.TransactionType;
import com.app.model.repository.AccountRepository;
import com.app.model.repository.TransactionRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class BankRestControllerTest {

    @InjectMocks
    BankRestController bankRestController;

    @Mock
    AccountRepository accountRepository;

    @Mock
    TransactionRepository transactionRepository;

    @Mock
    private Account account;

    @Mock
    private Transaction transaction;

    @Mock
    private ArrayList<Account> accounts;

    @Mock
    private ArrayList<Transaction> transactions;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        account = mock(Account.class);
        transaction = mock(Transaction.class);

        when(accountRepository.findAll()).thenReturn(accounts);
        when(transactionRepository.findAll()).thenReturn(transactions);

        when(accounts.get(anyInt())).thenReturn(account);
    }

    @Test
    public void test_get_account() {
        ResponseEntity<Account> actual = bankRestController.getAccount(1L);

        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actual.getBody()).isEqualTo(account);
    }

    @Test
    public void test_get_all_transactions() {
        ResponseEntity<List<Transaction>> actual = bankRestController.getTransactions(
                1L, null, null, null);

        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actual.getBody()).isEqualTo(transactions);
    }

    @Test
    public void test_get_transactions_by_type() {
        when(transactionRepository.findByType(any(TransactionType.class))).thenReturn(transactions);

        ResponseEntity<List<Transaction>> actual = bankRestController.getTransactions(
                1L, "FT", null, null);

        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actual.getBody()).isEqualTo(transactions);
    }

    @Test
    public void test_get_transactions_by_type_unknown_type() {
        ResponseEntity<List<Transaction>> actual = bankRestController.getTransactions(
                1L, "XXX", null, null);

        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(actual.getBody()).isNull();
    }

    @Test
    public void test_get_transactions_by_daterange() {
        when(transactionRepository.findByDateBetween(any(Date.class), any(Date.class)))
                .thenReturn(transactions);

        ResponseEntity<List<Transaction>> actual = bankRestController.getTransactions(
                1L, null, new Date(), new Date());

        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actual.getBody()).isEqualTo(transactions);
    }

    @Test
    public void test_get_transactions_by_invalid_daterange() {
        ResponseEntity<List<Transaction>> actual = bankRestController.getTransactions(
                1L, null, null, new Date());

        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(actual.getBody()).isNull();
    }
}
