package com.bank.loan.management.api.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.bank.common.exception.BusinessException;
import com.bank.loan.management.api.entity.Customer;
import com.bank.loan.management.api.entity.Loan;
import com.bank.loan.management.api.entity.LoanInstallment;
import com.bank.loan.management.api.mapper.LoanInstallmentMapperImpl;
import com.bank.loan.management.api.mapper.LoanMapperImpl;
import com.bank.loan.management.api.mapper.PossibleLoanMapperImpl;
import com.bank.loan.management.api.model.LoanDTO;
import com.bank.loan.management.api.repository.CustomerRepository;
import com.bank.loan.management.api.repository.LoanRepository;
import com.bank.loan.management.api.request.CreateLoanRequest;
import com.bank.loan.management.api.request.PayLoanRequest;
import com.bank.loan.management.api.response.PayLoanResponse;
import com.bank.loan.management.api.rule_engine.LoanLimitRule;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LoanCommandServiceImplTest {

  @InjectMocks
  private LoanCommandServiceImpl loanCommandService;

  @Mock
  private CustomerRepository customerRepository;

  @Spy
  private PossibleLoanMapperImpl possibleLoanMapper;

  @Spy
  private LoanMapperImpl loanMapper;

  @Spy
  private LoanInstallmentMapperImpl loanInstallmentMapper;

  @Spy
  private LoanLimitRule loanLimitRule;

  @Mock
  private LoanRepository loanRepository;

  /*
   You should check if customer has enough limit to get this new loan
   */

  @Test
  void checkCustomerHasEnoughLimit_throwBusinessException() {

    when(customerRepository.findById(anyLong())).thenReturn(
        Optional.of(getCustomer(new BigDecimal("1000"), new BigDecimal("2000"))));

    assertThrows(BusinessException.class,
        () -> loanCommandService.createLoan(
            getCreateLoanRequest(new BigDecimal("900"), 5, 1L, new BigDecimal("0.2"))));

    verify(possibleLoanMapper, times(1)).mergeFromRequestAndDatabaseInfo(any(), any(), any());
    verify(loanLimitRule, times(1)).evaluate(any());

  }

  /*
 You should check if customer has enough limit to get this new loan
 */
  @Test
  void checkCustomerHasEnoughLimit_returnOk() {

    when(customerRepository.findById(anyLong())).thenReturn(
        Optional.of(getCustomer(new BigDecimal("1000"), new BigDecimal("2000"))));

    when(loanRepository.save(any())).thenReturn(getLoan(new BigDecimal("960")));
    LoanDTO result = loanCommandService.createLoan(
        getCreateLoanRequest(new BigDecimal("800"), 5, 1L, new BigDecimal("0.2")));

    verify(possibleLoanMapper, times(1)).mergeFromRequestAndDatabaseInfo(any(), any(), any());
    verify(loanLimitRule, times(1)).evaluate(any());
    verify(loanRepository, times(1)).save(any());
    verify(customerRepository, times(1)).save(any());
    assertEquals(1L, result.getId());
  }

  /*
    All installments should have same amount. Total amount for loan should be
    amount * (1 + interest rate)
   */

  @Test
  void checkAllInstallmentHaveSameAmount_returnOk() {
    when(customerRepository.findById(anyLong())).thenReturn(
        Optional.of(getCustomer(new BigDecimal("1000"), new BigDecimal("2000"))));

    when(loanRepository.save(any())).thenReturn(getLoan(new BigDecimal("960")));
    LoanDTO result = loanCommandService.createLoan(
        getCreateLoanRequest(new BigDecimal("800"), 5, 1L, new BigDecimal("0.2")));

    assertEquals(result.getLoanAmount().setScale(2),
        new BigDecimal("800").multiply(new BigDecimal("0.2").add(BigDecimal.ONE)).setScale(2));

  }

  /*
    Installments should be paid wholly or not at all. So if installments amount is
    10 and you send 20, 2 installments can be paid. If you send 15, only 1 can be
    paid. If you send 5, no installments can be paid.

    Earliest installment should be paid first and if there are more money then you
    should continue to next installment.
     Installments have due date that still more than 3 calendar months cannot be
    paid. So if we were in January, you could pay only for January, February and
    March installments.
 */

  @Test
  void payInstallmentWholly_returnOk() {
    when(customerRepository.findById(anyLong())).thenReturn(
        Optional.of(getCustomer(new BigDecimal("1000"), new BigDecimal("9000"))));

    when(loanRepository.save(any())).thenReturn(getLoan(new BigDecimal("6000")));
    PayLoanResponse result = loanCommandService.pay(
        getPayLoanRequest(new BigDecimal("3000"), 1L, 1L));

    assertEquals(3, result.getNumberOfPaidInstallments());
    assertEquals(Month.JUNE, result.getPaidInstallments().get(0).getDueDate().getMonth());
    assertEquals(Month.JULY, result.getPaidInstallments().get(1).getDueDate().getMonth());
    assertEquals(Month.AUGUST, result.getPaidInstallments().get(2).getDueDate().getMonth());
  }

  /*
  - A result should be returned to inform how many installments paid, total
   amount spent and if loan is paid completely.
  - Necessary updates should be done in customer, loan and installment tables.
    If an installment is paid before due date, make a discount equal to
    installmentAmount*0.001*(number of days before due date) So in this case
    paidAmount of installment will be lower than amount.
  - If an installment is paid after due date, add a penalty equal to
    installmentAmount *0.001*(number of days after due date) So in this case
    paidAmount of installment will be higher than amount
   */

  @Test
  void payInstallment_returnOk() {
    when(customerRepository.findById(anyLong())).thenReturn(
        Optional.of(getCustomer(new BigDecimal("1000"), new BigDecimal("9000"))));

    when(loanRepository.save(any())).thenReturn(getLoan(new BigDecimal("6000")));
    PayLoanResponse result = loanCommandService.pay(
        getPayLoanRequest(new BigDecimal("3000"), 1L, 1L));

    assertEquals(3, result.getNumberOfPaidInstallments());
    //assertEquals(new BigDecimal("2363.00").setScale(2), result.getTotalAmount().setScale(2));
  }

  @Test
  void payInstallment_notEnoughAmount_throwBusinessException() {
    when(customerRepository.findById(anyLong())).thenReturn(
        Optional.of(getCustomer(new BigDecimal("1000"), new BigDecimal("9000"))));

    assertThrows(BusinessException.class, () -> loanCommandService.pay(
        getPayLoanRequest(new BigDecimal("30"), 1L, 1L)));
  }

  private PayLoanRequest getPayLoanRequest(BigDecimal amount, long loanId, long customerId) {
    PayLoanRequest payLoanRequest = new PayLoanRequest();
    payLoanRequest.setAmount(amount);
    payLoanRequest.setLoanId(loanId);
    payLoanRequest.setCustomerId(customerId);
    return payLoanRequest;
  }

  private Loan getLoan(BigDecimal loanAmount) {
    Loan loan = new Loan();
    loan.setId(1L);
    loan.setLoanAmount(loanAmount);
    loan.setPaid(false);
    loan.setCreateDate(LocalDate.now());
    loan.setLoanInstallments(List.of(getLoanInstallment("2025-06-01")
        , getLoanInstallment("2025-07-01")
        , getLoanInstallment("2025-08-01")
        , getLoanInstallment("2025-09-01")
        , getLoanInstallment("2025-10-01")
        , getLoanInstallment("2025-11-01")
    ));

    return loan;
  }

  private LoanInstallment getLoanInstallment(String dateString) {
    LoanInstallment loanInstallment = new LoanInstallment();
    loanInstallment.setDueDate(LocalDate.parse(dateString));
    loanInstallment.setPaid(false);
    loanInstallment.setAmount(new BigDecimal("1000"));
    return loanInstallment;
  }

  private Customer getCustomer(BigDecimal usedCreditLimit, BigDecimal totalCreditLimit) {
    Customer customer = new Customer();
    customer.setId(1L);
    customer.setUsedCreditLimit(usedCreditLimit);
    customer.setCreditLimit(totalCreditLimit);
    customer.setName("abc");
    customer.setSurname("def");
    customer.setLoans(List.of(getLoan(new BigDecimal("6000"))));
    return customer;
  }

  private CreateLoanRequest getCreateLoanRequest(BigDecimal amount, int installments,
      Long customerId, BigDecimal interestRate) {
    CreateLoanRequest request = new CreateLoanRequest();
    request.setAmount(amount);
    request.setInstallments(installments);
    request.setCustomerId(customerId);
    request.setInterestRate(interestRate);
    return request;
  }
}