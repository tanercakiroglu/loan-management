package com.bank.loan.management.api.response;

import com.bank.loan.management.api.model.LoanInstallmentDTO;
import java.math.BigDecimal;
import java.util.List;
import lombok.Data;

@Data

public class PayLoanResponse {

  private int numberOfPaidInstallments;
  private List<LoanInstallmentDTO> paidInstallments;
  private BigDecimal totalAmount;
  private Boolean loanPaid;

  public PayLoanResponse(int numberOfPaidInstallments, List<LoanInstallmentDTO> paidInstallments,
      Boolean loanPaid) {
    this.numberOfPaidInstallments = numberOfPaidInstallments;
    this.paidInstallments = paidInstallments;
    this.totalAmount = paidInstallments.stream().map(LoanInstallmentDTO::getPaidAmount)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
    this.loanPaid =loanPaid;
  }
}
