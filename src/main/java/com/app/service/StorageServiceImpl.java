package com.app.service;
/**
 * Created by anna.karri on 01/12/16.
 */

import com.app.AppException;
import com.app.model.Account;
import com.app.model.repository.AccountRepository;
import com.app.model.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;

@Service
public class StorageServiceImpl implements StorageService{

    @Autowired
    ResourceReader resourceReader;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Override
    public void clearStorage() {
        accountRepository.deleteAll();
        transactionRepository.deleteAll();
    }

    @Override
    public Long store(MultipartFile multipartFile) throws IOException, AppException {
        InputStreamReader inputStreamReader = new InputStreamReader(multipartFile.getInputStream());
        Account account = resourceReader.readFromMultipartFile(inputStreamReader);
        accountRepository.save(account);
        return account.getId();
    }

}
