package com.app.model;
/**
 * Created by anna.karri on 01/12/16.
 */

import lombok.Getter;

@Getter
public enum TransactionType {
    BP("Bill payment"),
    FT("Fund transfer"),
    IT("International transfer");

    private String description;

    private TransactionType(String description) {
        this.description = description;
    }
}
