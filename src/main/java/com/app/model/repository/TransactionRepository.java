package com.app.model.repository;
/**
 * Created by anna.karri on 01/12/16.
 */

import com.app.model.Transaction;
import com.app.model.TransactionType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Long> {

    List<Transaction> findByType(TransactionType type);

    List<Transaction> findByDateBetween(Date dateFrom, Date dateTo);

}
