package com.bank.loan.management.api.service;

import com.bank.loan.management.api.model.LoanDTO;
import com.bank.loan.management.api.model.LoanInstallmentDTO;
import java.util.List;

public interface LoanQueryService {

  List<LoanDTO> getLoansByCustomer(Long customerId);

  List<LoanInstallmentDTO> getLoanInstallmentsByLoan(Long loanId, Long customerId);
}
