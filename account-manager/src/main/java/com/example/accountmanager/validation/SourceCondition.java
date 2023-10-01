package com.example.accountmanager.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Repeatable(SourceConditions.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = SourceConditionValidator.class)
public @interface SourceCondition {

    String source();

    String[] requiredFields() default {};

    String[] requiredAllFieldsBut() default {};

    String message() default "Required field is missing.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
