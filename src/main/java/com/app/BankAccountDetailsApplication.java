package com.app;
/**
 * Created by anna.karri on 01/12/16.
 */

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.app"})
public class BankAccountDetailsApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankAccountDetailsApplication.class, args);
	}
}
