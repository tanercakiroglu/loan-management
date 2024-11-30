package com.bank.loan.management.api.controller.impl;

import com.bank.common.Loggable;
import com.bank.common.response.WrapperCollectionResponse;
import com.bank.loan.management.api.controller.LoanQueryController;
import com.bank.loan.management.api.model.LoanDTO;
import com.bank.loan.management.api.model.LoanInstallmentDTO;
import com.bank.loan.management.api.service.LoanQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@Loggable
@RequiredArgsConstructor
@RestController
public class LoanQueryControllerImpl implements LoanQueryController {

  private final LoanQueryService loanQueryService;

  @Override
  public WrapperCollectionResponse<LoanDTO> getLoansByCustomer(Long customerId) {
    return WrapperCollectionResponse.of(loanQueryService.getLoansByCustomer(customerId));
  }

  @Override
  public WrapperCollectionResponse<LoanInstallmentDTO> getLoanInstallmentByLoan(Long loanId,
      Long customerId) {
    return WrapperCollectionResponse.of(
        loanQueryService.getLoanInstallmentsByLoan(loanId, customerId));
  }
}
