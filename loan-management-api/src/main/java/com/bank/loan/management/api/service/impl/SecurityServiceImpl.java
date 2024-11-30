package com.bank.loan.management.api.service.impl;

import com.bank.loan.management.api.entity.Customer;
import com.bank.loan.management.api.repository.CustomerRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SecurityServiceImpl {

  private final CustomerRepository customerRepository;

  public boolean hasUserOrAdmin(Long id) {

    Customer user = customerRepository.findById(
        id).orElseThrow(() -> new EntityNotFoundException(String.valueOf(id)));
    Authentication authentication = SecurityContextHolder.getContext()
        .getAuthentication();

    return authentication.getName().equals(user.getName()) || authentication.getAuthorities()
        .contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
  }
}
