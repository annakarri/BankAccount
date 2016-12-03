package com.app.model;
/**
 * Created by anna.karri on 01/12/16.
 */

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;
import java.util.Date;


@Data
@Entity
public class Transaction {
    @GeneratedValue
    @Id
    private long id;

    @JsonFormat(pattern = DATE_FORMAT)
    private Date date;

    private BigDecimal balance;
    private TransactionType type;
    private String description;
    @ManyToOne
    private Account account;

    public static final String DATE_FORMAT = "dd/MM/yyyy";

}
