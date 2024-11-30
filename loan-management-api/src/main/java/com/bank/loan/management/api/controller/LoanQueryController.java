package com.bank.loan.management.api.controller;

import com.bank.common.response.WrapperCollectionResponse;
import com.bank.loan.management.api.model.LoanDTO;
import com.bank.loan.management.api.model.LoanInstallmentDTO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/loan")
public interface LoanQueryController {


  @GetMapping("/{customerId}")
  @PreAuthorize("@securityServiceImpl.hasUserOrAdmin(#customerId)")
  WrapperCollectionResponse<LoanDTO> getLoansByCustomer(
      @PathVariable("customerId") Long customerId);


  @GetMapping("/{customerId}/installment/{loanId}")
  @PreAuthorize("@securityServiceImpl.hasUserOrAdmin(#customerId)")
  WrapperCollectionResponse<LoanInstallmentDTO> getLoanInstallmentByLoan(
      @PathVariable("loanId") Long loanId, @PathVariable("customerId") Long customerId);
}
