package com.example.accountmanager.validation;

import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import com.example.accountmanager.CreationSource;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class SourceConditionValidator implements ConstraintValidator<SourceCondition, Object> {

    private String source;
    private List<String> requiredFields;
    private List<String> requiredAllFieldsBut;
    private String message;

    @Override
    public void initialize(SourceCondition condition) {
        source = condition.source();
        requiredFields = Arrays.asList(condition.requiredFields());
        requiredAllFieldsBut = Arrays.asList(condition.requiredAllFieldsBut());
        message = condition.message();
    }

    @Override
    public boolean isValid(Object objectToValidate, ConstraintValidatorContext context) {
        Optional<String> field = findMissingField(objectToValidate);

        if (field.isPresent()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(message).addPropertyNode(field.get()).addConstraintViolation();
            return false;
        }
        return true;
    }

    private Optional<String> findMissingField(Object objectToValidate) {
        BeanWrapper wrapper = new BeanWrapperImpl(objectToValidate);
        Object sourceValue = wrapper.getPropertyValue("source");

        if (sourceValue != null && source.equalsIgnoreCase(((CreationSource) sourceValue).name())) {
            if (!requiredAllFieldsBut.isEmpty()) {
                for (PropertyDescriptor property : wrapper.getPropertyDescriptors()) {
                    String field = property.getName();
                    if (!requiredAllFieldsBut.contains(field) && wrapper.getPropertyValue(field) == null) {
                        return Optional.of(field);
                    }
                }
            } else {
                for (String field : requiredFields) {
                    if (wrapper.getPropertyValue(field) == null) {
                        return Optional.of(field);
                    }
                }
            }
        }
        return Optional.empty();
    }
}
