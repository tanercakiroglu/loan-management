package com.bank.loan.management.api.mapper;

import com.bank.loan.management.api.entity.LoanInstallment;
import com.bank.loan.management.api.model.LoanInstallmentDTO;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LoanInstallmentMapper {

  @Mapping(target = "loan.loan", ignore = true)
  LoanInstallmentDTO map(LoanInstallment loan);

  @Mapping(target = "loans.loan",  ignore = true)
  List<LoanInstallmentDTO> map(List<LoanInstallment> loans);
}
