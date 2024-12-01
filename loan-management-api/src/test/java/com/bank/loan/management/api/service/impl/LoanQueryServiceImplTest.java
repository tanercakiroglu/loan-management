package com.bank.loan.management.api.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import com.bank.loan.management.api.entity.Loan;
import com.bank.loan.management.api.entity.LoanInstallment;
import com.bank.loan.management.api.mapper.LoanInstallmentMapperImpl;
import com.bank.loan.management.api.mapper.LoanMapperImpl;
import com.bank.loan.management.api.model.LoanDTO;
import com.bank.loan.management.api.model.LoanInstallmentDTO;
import com.bank.loan.management.api.repository.LoanRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LoanQueryServiceImplTest {

  @InjectMocks
  private LoanQueryServiceImpl loanQueryService;
  @Mock
  private LoanRepository loanRepository;
  @Spy
  private LoanMapperImpl loanMapper;
  @Spy
  private LoanInstallmentMapperImpl loanInstallmentMapper;

  @Test
  void getLoan_returnOk() {
    when(loanRepository.findByCustomerId(anyLong())).thenReturn(List.of(getLoan(new BigDecimal("0"))));

    List<LoanDTO> result = loanQueryService.getLoansByCustomer(
        1l);
    assertEquals(1, result.get(0).getId());
    assertEquals(false, result.get(0).getPaid());
    assertEquals(1, result.get(0).getId());
  }

  @Test
  void getLoanInstallment_returnOk() {
    when(loanRepository.findByCustomerId(anyLong())).thenReturn(List.of(getLoan(new BigDecimal("0"))));

    List<LoanInstallmentDTO> result = loanQueryService.getLoanInstallmentsByLoan(
        1l,1L);
    assertEquals(1, result.get(0).getId());
    assertEquals(false, result.get(0).getPaid());
    assertEquals(6, result.size());
  }


  private Loan getLoan(BigDecimal loanAmount) {
    Loan loan = new Loan();
    loan.setId(1L);
    loan.setLoanAmount(loanAmount);
    loan.setPaid(false);
    loan.setCreateDate(LocalDate.now());
    loan.setLoanInstallments(List.of(getLoanInstallment("2032-06-01")
        , getLoanInstallment("2032-07-01")
        , getLoanInstallment("2032-08-01")
        , getLoanInstallment("2032-09-01")
        , getLoanInstallment("2032-10-01")
        , getLoanInstallment("2032-11-01")
    ));

    return loan;
  }

  private LoanInstallment getLoanInstallment(String dateString) {
    LoanInstallment loanInstallment = new LoanInstallment();
    loanInstallment.setId(1L);
    loanInstallment.setDueDate(LocalDate.parse(dateString));
    loanInstallment.setPaid(false);
    loanInstallment.setAmount(new BigDecimal("1000"));
    return loanInstallment;
  }

}