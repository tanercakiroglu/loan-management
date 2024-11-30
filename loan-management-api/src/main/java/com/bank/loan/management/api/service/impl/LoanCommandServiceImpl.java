package com.bank.loan.management.api.service.impl;

import static com.bank.loan.management.api.util.ErrorCode.AMOUNT_NOT_ENOUGH;
import static com.bank.loan.management.api.util.ErrorCode.LOAN_LIMIT_NOT_ENOUGH;
import static java.util.stream.Collectors.toList;

import com.bank.common.exception.BusinessException;
import com.bank.loan.management.api.entity.Customer;
import com.bank.loan.management.api.entity.Loan;
import com.bank.loan.management.api.entity.LoanInstallment;
import com.bank.loan.management.api.mapper.LoanInstallmentMapper;
import com.bank.loan.management.api.mapper.LoanMapper;
import com.bank.loan.management.api.mapper.PossibleLoanMapper;
import com.bank.loan.management.api.model.LoanDTO;
import com.bank.loan.management.api.model.PossibleLoanDTO;
import com.bank.loan.management.api.repository.CustomerRepository;
import com.bank.loan.management.api.repository.LoanRepository;
import com.bank.loan.management.api.request.CreateLoanRequest;
import com.bank.loan.management.api.request.PayLoanRequest;
import com.bank.loan.management.api.response.PayLoanResponse;
import com.bank.loan.management.api.rule_engine.LoanLimitRule;
import com.bank.loan.management.api.rule_engine.RuleEngine;
import com.bank.loan.management.api.service.LoanCommandService;
import com.bank.loan.management.api.util.DateUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoanCommandServiceImpl implements LoanCommandService {

  private final CustomerRepository customerRepository;
  private final PossibleLoanMapper possibleLoanMapper;
  private final LoanInstallmentMapper loanInstallmentMapper;
  private final LoanLimitRule loanLimitRule;
  private final LoanRepository loanRepository;
  private final LoanMapper loanMapper;
  private final static int maxPayableInstallment = 3;


  @Transactional
  public synchronized LoanDTO createLoan(CreateLoanRequest request) {
    Customer customer = getCustomer(request.getCustomerId());
    BigDecimal totalAmount = calculateTotalAmount(request);

    PossibleLoanDTO possibleLoan = possibleLoanMapper.mergeFromRequestAndDatabaseInfo(
        request, customer, totalAmount);

    if (RuleEngine.evaluate(possibleLoan, loanLimitRule)) {
      throw new BusinessException(LOAN_LIMIT_NOT_ENOUGH);
    }

    BigDecimal installmentAmount = calculateInstallmentAmount(request, totalAmount);
    List<LoanInstallment> installments = new ArrayList<>();

    LocalDate theFirstDayOfNextMonth = LocalDate.now();
    for (int i = 0; i < request.getInstallments(); i++) {
      theFirstDayOfNextMonth = DateUtils.getStartOfNextMonth(theFirstDayOfNextMonth);
      installments.add(populateInstallment(installmentAmount, theFirstDayOfNextMonth));
    }
    Loan loan = populateLoan(request, customer, installments, totalAmount);

    Loan saved = loanRepository.save(loan);
    customer.setUsedCreditLimit(customer.getUsedCreditLimit().add(totalAmount));
    customerRepository.save(customer);
    return loanMapper.map(saved);
  }


  @Override
  @Transactional
  public synchronized PayLoanResponse pay(PayLoanRequest request) {
    Customer customer = getCustomer(request.getCustomerId());
    Loan loan = getLoan(request, customer);
    List<LoanInstallment> installments = getSortedInstallment(loan);
    BigDecimal amount = request.getAmount();
    List<LoanInstallment> paidInstallments = new ArrayList<>();
    if (installments.get(0).getAmount().compareTo(amount) > 0) {
      throw new BusinessException(AMOUNT_NOT_ENOUGH);
    }
    int numberOfPaidInstallments = 0;
    for (LoanInstallment installment : installments) {
      if (installment.getPaid()) {
        continue;
      }
      if (numberOfPaidInstallments == maxPayableInstallment) {
        break;
      }
      if (amount.subtract(installment.getAmount()).compareTo(BigDecimal.ZERO) >= 0) {
        installment.setPaymentDate(LocalDate.now());
        installment.setPaidAmount(installment.getAmount());
        installment.setPaid(true);
        amount = amount.subtract(installment.getAmount());
        paidInstallments.add(installment);
        BigDecimal usedCreditLimit = customer.getUsedCreditLimit()
            .subtract(installment.getAmount());
        if (usedCreditLimit.compareTo(BigDecimal.ZERO) > 0) {
          customer.setUsedCreditLimit(usedCreditLimit);
        } else {
          customer.setUsedCreditLimit(BigDecimal.ZERO);
        }
        numberOfPaidInstallments++;
      } else {
        break;
      }
    }

    if (installments.stream().allMatch(LoanInstallment::getPaid)) {
      loan.setPaid(true);
    }
    loan.setLoanInstallments(installments);
    loanRepository.save(loan);
    customerRepository.save(customer);
    return new PayLoanResponse(numberOfPaidInstallments,
        loanInstallmentMapper.map(paidInstallments), loan.getPaid());
  }

  private List<LoanInstallment> getSortedInstallment(Loan loan) {
    return loan.getLoanInstallments().stream()
        .sorted(Comparator.comparing(LoanInstallment::getDueDate))
        .collect(toList());
  }

  private static Loan getLoan(PayLoanRequest request, Customer customer) {
    return customer
        .getLoans()
        .stream()
        .filter(loan -> loan.getId().equals(request.getLoanId()))
        .filter(loan -> !loan.getPaid())
        .findFirst()
        .orElseThrow(() -> new EntityNotFoundException(String.valueOf(request.getLoanId())));
  }

  private Customer getCustomer(Long customerId) {
    return customerRepository.findById(customerId).
        orElseThrow(() -> new EntityNotFoundException(String.valueOf(customerId)));
  }

  private BigDecimal calculateInstallmentAmount(CreateLoanRequest request, BigDecimal totalAmount) {
    return totalAmount.divide(new BigDecimal(request.getInstallments()), 2, RoundingMode.HALF_UP);
  }

  private Loan populateLoan(CreateLoanRequest request, Customer customer,
      List<LoanInstallment> installments, BigDecimal totalAmount) {
    Loan loan = new Loan();
    installments.forEach(installment -> installment.setLoan(loan));
    loan.setCustomer(customer);
    loan.setLoanInstallments(installments);
    loan.setCreateDate(LocalDate.now());
    loan.setNumberOfInstallments(request.getInstallments());
    loan.setLoanAmount(totalAmount);
    loan.setPaid(false);
    return loan;
  }

  private BigDecimal calculateTotalAmount(CreateLoanRequest request) {
    return request.getAmount().multiply(BigDecimal.ONE.add(request.getInterestRate()));
  }

  private LoanInstallment populateInstallment(BigDecimal installmentAmount, LocalDate dueDate) {
    LoanInstallment installment = new LoanInstallment();
    installment.setAmount(installmentAmount);
    installment.setDueDate(dueDate);
    installment.setPaid(false);
    return installment;
  }
}
