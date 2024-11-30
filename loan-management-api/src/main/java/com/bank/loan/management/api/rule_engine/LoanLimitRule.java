package com.bank.loan.management.api.rule_engine;

import com.bank.loan.management.api.model.PossibleLoanDTO;
import java.math.BigDecimal;
import org.springframework.stereotype.Component;

@Component
public class LoanLimitRule implements Rule<PossibleLoanDTO> {

  @Override
  public boolean evaluate(PossibleLoanDTO possibleLoanDTO) {
    return possibleLoanDTO.getCreditLimit()
        .subtract(possibleLoanDTO.getUsedCreditLimit())
        .subtract(possibleLoanDTO.getAmount()).compareTo(BigDecimal.ZERO) <= 0;
  }

}
