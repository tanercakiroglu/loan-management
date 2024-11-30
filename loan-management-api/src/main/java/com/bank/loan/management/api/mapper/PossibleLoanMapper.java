package com.bank.loan.management.api.mapper;

import com.bank.loan.management.api.entity.Customer;
import com.bank.loan.management.api.model.PossibleLoanDTO;
import com.bank.loan.management.api.request.CreateLoanRequest;
import java.math.BigDecimal;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class PossibleLoanMapper {

  public PossibleLoanDTO mergeFromRequestAndDatabaseInfo(CreateLoanRequest request,
      Customer customer, BigDecimal totalAmount) {
    PossibleLoanDTO possibleLoanDTO = new PossibleLoanDTO();
    possibleLoanDTO.setAmount(totalAmount);
    possibleLoanDTO.setInstallments(request.getInstallments());
    possibleLoanDTO.setInterestRate(request.getInterestRate());
    possibleLoanDTO.setCustomerId(request.getCustomerId());
    possibleLoanDTO.setCreditLimit(customer.getCreditLimit());
    possibleLoanDTO.setUsedCreditLimit(customer.getUsedCreditLimit());
    return possibleLoanDTO;
  }
}
