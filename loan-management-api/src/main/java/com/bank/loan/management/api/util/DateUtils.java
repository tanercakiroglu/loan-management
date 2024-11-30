package com.bank.loan.management.api.util;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DateUtils {

  public static LocalDate getStartOfNextMonth() {
    return LocalDate.now().with(TemporalAdjusters.firstDayOfNextMonth());
  }

  public static LocalDate getStartOfNextMonth(LocalDate date) {
    return date.with(TemporalAdjusters.firstDayOfNextMonth());
  }
}
