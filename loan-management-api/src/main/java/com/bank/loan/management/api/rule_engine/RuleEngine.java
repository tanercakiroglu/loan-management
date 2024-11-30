package com.bank.loan.management.api.rule_engine;

import java.util.List;


public class RuleEngine {

  public static <T> boolean evaluate(T object, Rule<T> rule) {
    return rule.evaluate(object);
  }

  public static <T> boolean evaluateAll(T object, List<Rule<T>> rules) {
    return rules.stream().allMatch(rule -> rule.evaluate(object));
  }

  public static <T> boolean evaluateAny(T object, List<Rule<T>> rules) {
    return rules.stream().anyMatch(rule -> rule.evaluate(object));
  }
}
