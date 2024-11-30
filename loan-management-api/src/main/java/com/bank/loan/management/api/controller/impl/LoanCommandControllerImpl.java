package com.bank.loan.management.api.controller.impl;

import com.bank.common.Loggable;
import com.bank.common.response.WrapperResponse;
import com.bank.loan.management.api.controller.LoanCommandController;
import com.bank.loan.management.api.model.LoanDTO;
import com.bank.loan.management.api.request.CreateLoanRequest;
import com.bank.loan.management.api.request.PayLoanRequest;
import com.bank.loan.management.api.response.PayLoanResponse;
import com.bank.loan.management.api.service.LoanCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@Loggable
@RequiredArgsConstructor
@RestController
public class LoanCommandControllerImpl implements LoanCommandController {

  private final LoanCommandService loanCommandService;

  @Override
  public WrapperResponse<LoanDTO> createLoan(CreateLoanRequest request) {
    return WrapperResponse.of(loanCommandService.createLoan(request));
  }

  @Override
  public WrapperResponse<PayLoanResponse> payLoan(PayLoanRequest request) {
    return WrapperResponse.of(loanCommandService.pay(request));
  }
}
