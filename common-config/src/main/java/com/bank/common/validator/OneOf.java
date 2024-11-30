package com.bank.common.validator;


import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target({FIELD, PARAMETER})
@Retention(RUNTIME)
@Constraint(validatedBy = OneOfValidator.class)
public @interface OneOf {

  String message() default "...provide your default message here...";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  /**
   * The array of allowed values.
   */
  int[] value();
}
