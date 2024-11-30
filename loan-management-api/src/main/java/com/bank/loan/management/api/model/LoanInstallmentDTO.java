package com.bank.loan.management.api.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Data;

@Data
public class LoanInstallmentDTO {

  private Long id;

  private BigDecimal amount;

  private BigDecimal paidAmount;

  private LocalDate dueDate;

  private LocalDate paymentDate;

  private Boolean paid;
}
