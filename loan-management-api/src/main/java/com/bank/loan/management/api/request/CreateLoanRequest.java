package com.bank.loan.management.api.request;

import com.bank.common.validator.OneOf;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Data;


@Data
public class CreateLoanRequest {

  @NotNull(message = "loan.management.invalid.amount.value")
  private BigDecimal amount;
  @NotNull(message = "loan.management.invalid.interest.rate")
  @DecimalMin(value = "0.1", message = "loan.management.invalid.interest.rate.range")
  @DecimalMax(value = "0.5", message = "loan.management.invalid.interest.rate.range")
  private BigDecimal interestRate;
  @OneOf(value = {6, 9, 12, 24}, message = "loan.management.invalid.installment.values")
  private int installments;
  @NotNull(message = "loan.management.invalid.customer.id")
  private Long customerId;
}
