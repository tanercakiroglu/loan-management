package com.bank.loan.management.api.request;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class PayLoanRequest {

  @NotNull(message = "loan.management.invalid.customer.id")
  private Long customerId;

  @NotNull(message = "loan.management.invalid.loan.id")
  private Long loanId;

  @NotNull(message = "loan.management.invalid.amount.value")
  private BigDecimal amount;

}
