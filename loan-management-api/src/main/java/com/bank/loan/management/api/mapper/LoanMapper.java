package com.bank.loan.management.api.mapper;

import com.bank.loan.management.api.entity.Loan;
import com.bank.loan.management.api.model.LoanDTO;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LoanMapper {

  LoanDTO map(Loan loan);

  List<LoanDTO> map(List<Loan> loans);
}
