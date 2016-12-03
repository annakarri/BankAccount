package com.app.model.repository;
/**
 * Created by anna.karri on 01/12/16.
 */

import com.app.model.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends CrudRepository<Account, Long> {

}

