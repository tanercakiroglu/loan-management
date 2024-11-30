package com.bank.loan.management.api.mapper;

import com.bank.loan.management.api.entity.Loan;
import com.bank.loan.management.api.model.LoanDTO;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN,
    nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL)
public interface LoanMapper {

  LoanDTO map(Loan loan);

  List<LoanDTO> map(List<Loan> loans);
}
