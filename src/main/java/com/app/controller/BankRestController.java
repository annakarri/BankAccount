package com.app.controller;
/**
 * Created by anna.karri on 01/12/16.
 */

import com.app.model.Account;
import com.app.model.Transaction;
import com.app.model.TransactionType;
import com.app.model.repository.AccountRepository;
import com.app.model.repository.TransactionRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@RestController
public class BankRestController {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    TransactionRepository transactionRepository;

    private static final String INPUT_DATE_FORMAT="yyyy-MM-dd";

    @GetMapping("/accounts/{id}")
    public ResponseEntity<Account> getAccount(@PathVariable @Valid final Long id) {
        List<Account> accounts = (List<Account>) accountRepository.findAll();
        Account account = accounts.get(0);
        return new ResponseEntity<Account>(account, HttpStatus.OK);
    }

    @GetMapping("/accounts/{id}/transactions")
    public ResponseEntity<List<Transaction>> getTransactions(
            @PathVariable("id") @Valid final Long accountId,
            @RequestParam(value = "type", required = false) final String transactionType,
            @RequestParam(value = "datefrom", required = false)
            @DateTimeFormat(pattern = INPUT_DATE_FORMAT) final Date datefrom,
            @RequestParam(value = "dateto", required = false)
            @DateTimeFormat(pattern = INPUT_DATE_FORMAT) final Date dateto) {

        List<Transaction> transactions=null;
        if (StringUtils.isNotBlank(transactionType)) {
            try {
                TransactionType type = TransactionType.valueOf(transactionType);
                transactions = transactionRepository.findByType(type);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                return new ResponseEntity<>(transactions, HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(transactions, HttpStatus.OK);
        }

        if (datefrom != null && dateto != null) {
            transactions = transactionRepository.findByDateBetween(datefrom, dateto);
            return new ResponseEntity<>(transactions, HttpStatus.OK);
        }
        if (StringUtils.isBlank(transactionType)
                && datefrom == null && dateto == null) {
            transactions = (List<Transaction>) transactionRepository.findAll();
            return new ResponseEntity<>(transactions, HttpStatus.OK);
        }

        return new ResponseEntity<>(transactions, HttpStatus.BAD_REQUEST);
    }
}
