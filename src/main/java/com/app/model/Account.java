package com.app.model;
/**
 * Created by anna.karri on 01/12/16.
 */

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;


@Data
@Entity
public class Account {
    @GeneratedValue
    @Id
    private long id;

    private BigDecimal balance;

    @OneToMany(cascade = CascadeType.PERSIST)
    private List<Transaction> transactions;
}
