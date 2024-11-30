package com.bank.loan.management.api.service;

import com.bank.loan.management.api.model.LoanDTO;
import com.bank.loan.management.api.request.CreateLoanRequest;
import com.bank.loan.management.api.request.PayLoanRequest;
import com.bank.loan.management.api.response.PayLoanResponse;

public interface LoanCommandService {

  LoanDTO createLoan(CreateLoanRequest request);

  PayLoanResponse pay(PayLoanRequest request);
}
