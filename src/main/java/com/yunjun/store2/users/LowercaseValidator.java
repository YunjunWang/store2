package com.yunjun.store2.users;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class LowercaseValidator implements ConstraintValidator<Lowercase, String> {
    /**
     * @param value
     * @param constraintValidatorContext
     * @return
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null) {return true;}
        return value.toLowerCase().equals(value);
    }
}
