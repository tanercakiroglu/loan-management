package com.bank.loan.management.api.service;

import com.bank.loan.management.api.request.CreateLoanRequest;
import com.bank.loan.management.api.request.PayLoanRequest;
import com.bank.loan.management.api.response.CreateLoanResponse;
import com.bank.loan.management.api.response.PayLoanResponse;

public interface LoanCommandService {

  CreateLoanResponse createLoan(CreateLoanRequest request);

  PayLoanResponse pay(PayLoanRequest request);
}
