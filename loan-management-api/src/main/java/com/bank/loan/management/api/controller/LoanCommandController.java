package com.bank.loan.management.api.controller;

import com.bank.common.response.WrapperResponse;
import com.bank.loan.management.api.request.CreateLoanRequest;
import com.bank.loan.management.api.request.PayLoanRequest;
import com.bank.loan.management.api.response.CreateLoanResponse;
import com.bank.loan.management.api.response.PayLoanResponse;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/loan")
public interface LoanCommandController {

  @PostMapping()
  @PreAuthorize("@securityServiceImpl.hasUserOrAdmin(#request.customerId)")
  WrapperResponse<CreateLoanResponse> createLoan(@Valid @RequestBody CreateLoanRequest request);

  @PostMapping("/pay")
  @PreAuthorize("@securityServiceImpl.hasUserOrAdmin(#request.customerId)")
  WrapperResponse<PayLoanResponse> payLoan(@Valid @RequestBody PayLoanRequest request);
}
