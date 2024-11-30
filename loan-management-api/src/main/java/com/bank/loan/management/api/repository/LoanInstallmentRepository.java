package com.bank.loan.management.api.repository;

import com.bank.loan.management.api.entity.LoanInstallment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanInstallmentRepository extends JpaRepository<LoanInstallment, Long> {


}
