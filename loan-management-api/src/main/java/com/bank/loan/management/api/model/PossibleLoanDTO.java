package com.bank.loan.management.api.model;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class PossibleLoanDTO {

  private BigDecimal amount;
  private BigDecimal interestRate;
  private int installments;
  private Long customerId;
  private BigDecimal creditLimit;
  private BigDecimal usedCreditLimit;
}
