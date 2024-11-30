package com.bank.loan.management.api.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Data;

@Data
public class LoanDTO {

  private Long id;

  private BigDecimal loanAmount;

  private int numberOfInstallments;

  private LocalDate createDate;

  private Boolean paid;
}
