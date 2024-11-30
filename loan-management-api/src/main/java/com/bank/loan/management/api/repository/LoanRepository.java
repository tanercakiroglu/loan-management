package com.bank.loan.management.api.repository;

import com.bank.loan.management.api.entity.Loan;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {

  List<Loan> findByCustomerId(Long customerId);

}
