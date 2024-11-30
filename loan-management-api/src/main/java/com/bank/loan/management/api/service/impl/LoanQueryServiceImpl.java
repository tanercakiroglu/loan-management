package com.bank.loan.management.api.service.impl;

import com.bank.loan.management.api.mapper.LoanInstallmentMapper;
import com.bank.loan.management.api.mapper.LoanMapper;
import com.bank.loan.management.api.model.LoanDTO;
import com.bank.loan.management.api.model.LoanInstallmentDTO;
import com.bank.loan.management.api.repository.LoanRepository;
import com.bank.loan.management.api.service.LoanQueryService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoanQueryServiceImpl implements LoanQueryService {

  private final LoanRepository loanRepository;
  private final LoanMapper loanMapper;
  private final LoanInstallmentMapper loanInstallmentMapper;

  @Override
  public List<LoanDTO> getLoansByCustomer(Long customerId) {
    return loanMapper.map(loanRepository.findByCustomerId(customerId));
  }

  @Transactional
  @Override
  public List<LoanInstallmentDTO> getLoanInstallmentsByLoan(Long loanId, Long customerId) {
    return loanInstallmentMapper.map(loanRepository.findByCustomerId(customerId)
        .stream()
        .filter(loan -> loan.getId().equals(loanId))
        .findFirst()
        .orElseThrow(() -> new EntityNotFoundException(String.valueOf(loanId))).
        getLoanInstallments());

  }
}
