package com.bank.loan.management.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.bank.*"})
public class LoanManagementApi {

  public static void main(String[] args) {
    SpringApplication.run(LoanManagementApi.class, args);
  }
}