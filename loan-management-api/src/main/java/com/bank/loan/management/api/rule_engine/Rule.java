package com.bank.loan.management.api.rule_engine;

public interface Rule<T> {

  boolean evaluate(T object);
}
