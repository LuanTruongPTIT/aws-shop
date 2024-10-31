package com.aws.account.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneValidator implements ConstraintValidator<ValidatePhone, String> {

  @Override
  public void initialize(ValidatePhone constraintAnnotation) {
    // ConstraintValidator.super.initialize(constraintAnnotation);
  }

  @Override
  public boolean isValid(String phone, ConstraintValidatorContext context) {
    if (phone == null) {
      return false;
    }
    return phone.matches("^\\+?[0-9. ()-]{7,25}$");
  }
}